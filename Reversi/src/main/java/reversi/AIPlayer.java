package reversi;

import java.util.HashSet;

public class AIPlayer implements Player {

  private int color;
  private String name;
  private int reasoning;
  private long startTime;
  private long stopTime;
  private int maxDepth;

  int searchedNodes = 0;

  public AIPlayer(String name, int color, int reasoning) {
    this.name = name;
    this.color = color;
    this.reasoning = reasoning;
  }

  @Override
  public Point makeMove(HashSet<Point> moves, Board board) {
    return makeDecision(moves, board);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getColor() {
    return color;
  }

  private Point makeDecision(HashSet<Point> moves, Board board) {
    int searchDepth = 0;
    maxDepth = 4;
    startTime = System.currentTimeMillis();
    stopTime = startTime + reasoning*1000;


    Point bestMove = null;
    int highestValue = Integer.MIN_VALUE;

    for (Point move : moves) {
      board.updateBoard(move, getTurnColor(searchDepth));
      int value = minValue(board, Integer.MIN_VALUE, Integer.MAX_VALUE, searchDepth);
      if (value >= highestValue) {
        highestValue = value;
        bestMove = move;
      }
    }

    System.out.println("Time: " + (System.currentTimeMillis() - startTime) + "ms");
    System.out.println("Iterated nodes: " + searchedNodes);

    return bestMove;
  }

  private int maxValue(Board board, int alpha, int beta, int searchDepth) {

    searchedNodes++;
    long elapsedTime = System.currentTimeMillis();
    if (board.isTerminal() || elapsedTime > stopTime || searchDepth > maxDepth);
      return getUtility(board, searchDepth);

    int bestValue = Integer.MIN_VALUE;
    for (Point move : moves) { // moves does not exist. Whos turn is it?? Kepppppppokappa
      board.updateBoard(move, getTurnColor(searchDepth));
      bestValue = Math.max(bestValue, maxValue(board, alpha, beta, searthDepth++));
      if (bestValue >= beta)
        return bestValue;
      alpha = Math.max(alpha, bestValue);
    }
    return bestValue;
  }

  private int minValue(Board board, int alpha, int beta, int searchDepth) {
    searchedNodes++;
    long elapsedTime = System.currentTimeMillis();
    if (board.isTerminal() || elapsedTime > stopTime || searchDepth > maxDepth);
      return getUtility(board, searchDepth);

    int bestValue = Integer.MAX_VALUE;
    for (Point move : moves) {
      board.updateBoard(move, getTurnColor(searchDepth));
      bestValue = Math.min(value, maxValue(board, alpha, beta, searchDepth++));
      if (bestValue <= alpha)
        return bestValue;
      beta = Math.min(beta, bestValue);
    }
  }

  private int getTurnColor(int searchDepth) {
      if (searchDepth % 2 == 0)
        return color;
      else
        return color == Const.BLACK ? Const.WHITE : Const.BLACK;

  }

  private int getUtility(Board boardState, int searchDepth) {
    int opColor = (color == Const.BLACK ? Const.WHITE : Const.BLACK);
    if (searchDepth % 2 == 0)
      return boardState.getUtilityValue(color, opColor);
    else
      return boardState.getUtilityValue(opColor, color);
  }
}
