package search;

import java.util.ArrayList;

import game.Game;

public class MinimaxAB {

	Game game;
	long stopTime;
	long startTime;
	int nodes;
	ArrayList<Action> actions;

	public MinimaxAB(Game game) {
		this.game = game;
	}

	public Action makeDecision(State state) {
		nodes = 0;
		startTime = System.currentTimeMillis();
		stopTime = startTime + 2000;
		int d = 0;
		State temp = new State(state.getMatrix(), state.getPlayer());
		actions = new ArrayList<Action>();
		Action result = null;

		double resultValue = Double.NEGATIVE_INFINITY;
		for (Action action : game.getActions(temp)) {
			double value = minValue(game.getResult(temp, action), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, d);

			if (value >= resultValue) {
				result = action;
				resultValue = value;
			}
		}
		System.out.println("Time: " + (System.currentTimeMillis() - startTime) + "ms");
		System.out.println("Nodes: " + nodes);
		return result;

	}

	public double maxValue(State state, double alpha, double beta, int d) { // returns an utility
		nodes++;
		long time = System.currentTimeMillis();
		if (game.isTerminal(state) || time > stopTime || d >= 3) {
			return game.getUtility(state);
		}

		double value = Double.NEGATIVE_INFINITY;
		for (Action action : game.getActions(state)) {
			value = Math.max(value, minValue(game.getResult(state, action), alpha, beta, d++));
			if (value >= beta)
				return value;
			alpha = Math.max(alpha, value);
		}
		return value;

	}

	public double minValue(State state, double alpha, double beta, int d) { // returns an utility
		nodes++;
		long time = System.currentTimeMillis();

		if (game.isTerminal(state) || time > stopTime || d >= 3) {
			return game.getUtility(state);
		}

		double value = Double.POSITIVE_INFINITY;
		for (Action action : game.getActions(state)) {
			value = Math.min(value, maxValue(game.getResult(state, action), alpha, beta, d++));
			if (value <= alpha)
				return value;
			beta = Math.min(beta, value);
		}
		return value;
	}

}
