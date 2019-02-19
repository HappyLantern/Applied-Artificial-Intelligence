
import java.util.HashSet;

public class AIPlayer implements Player {

    private int color;
    private String name;
    private long reasoning;
    private long startTime;
    private long stopTime;
    private int maxDepth;

    private int searchedNodes;

    public AIPlayer(String name, int color, long reasoning) {
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
        searchedNodes = 0;
        maxDepth = 8;
        startTime = System.currentTimeMillis();
        stopTime = startTime + reasoning;


        Point bestMove = null;
        int highestValue = Integer.MIN_VALUE;

        for (Point move : moves) {
            Board b = new Board(board);
            b.updateBoard(move, getPlayerTurn(searchDepth));
            int value = minValue(b, Integer.MIN_VALUE, Integer.MAX_VALUE, ++searchDepth);
            if (value >= highestValue) {
                highestValue = value;
                bestMove = move;
            }
        }

        IO.printMiniMaxSearchInfo((System.currentTimeMillis() - startTime), searchedNodes, maxDepth);

        return bestMove;
    }

    private int maxValue(Board board, int alpha, int beta, int searchDepth) {
        searchDepth++;
        searchedNodes++;
        long elapsedTime = System.currentTimeMillis();
        HashSet<Point> moves = board.getLegalMoves(getPlayerTurn(searchDepth));


        if (moves.isEmpty() || elapsedTime > stopTime || searchDepth > maxDepth)
        if (isTerminal(board) || elapsedTime > stopTime || searchDepth > maxDepth)
        return getUtility(board, searchDepth);



        int bestValue = Integer.MIN_VALUE;


        for (Point move : moves) {
            Board b = new Board(board);
            b.updateBoard(move, getPlayerTurn(searchDepth));
            bestValue = Math.max(bestValue, maxValue(b, alpha, beta, searchDepth));
            if (bestValue >= beta)
            return bestValue;
            alpha = Math.max(alpha, bestValue);
        }
        return bestValue;
    }

    private int minValue(Board board, int alpha, int beta, int searchDepth) {
        searchDepth++;
        searchedNodes++;
        long elapsedTime = System.currentTimeMillis();

        HashSet<Point> moves = board.getLegalMoves(getPlayerTurn(searchDepth));

        if (moves.isEmpty() || elapsedTime > stopTime || searchDepth > maxDepth)
        if (isTerminal(board) || elapsedTime > stopTime || searchDepth > maxDepth)
        return getUtility(board, searchDepth);

        int bestValue = Integer.MAX_VALUE;

        for (Point move : moves) {
            Board b = new Board(board);
            b.updateBoard(move, getPlayerTurn(searchDepth));
            bestValue = Math.min(bestValue, maxValue(b, alpha, beta, searchDepth));
            if (bestValue <= alpha)
            return bestValue;
            beta = Math.min(beta, bestValue);
        }

        return bestValue;
    }

    private Player getPlayerTurn(int searchDepth) {
        if (searchDepth % 2 == 0)
        return Game.players.get(color);
        else
        return color == Const.BLACK ? Game.players.get(Const.WHITE) : Game.players.get(Const.WHITE);

    }

    private int getUtility(Board boardState, int searchDepth) {
        int opColor = (color == Const.BLACK ? Const.WHITE : Const.BLACK);
        if (searchDepth % 2 == 0)
        return boardState.getUtilityValue(color, opColor);
        else
        return boardState.getUtilityValue(opColor, color);
    }

    private boolean isTerminal(Board board) {
        return board.isTerminal(Game.players.get(Const.BLACK), Game.players.get(Const.WHITE));
    }
}
