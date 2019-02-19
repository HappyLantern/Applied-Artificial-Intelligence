
import java.util.HashMap;
import java.util.HashSet;

class Game {

    private Board board;
    static HashMap<Integer,Player> players;
    private HashMap<Integer,Boolean> playerHasMoves;
    private int turn;
    private boolean activeGame = false;

    void start() {
        resetGame();
        IO.printBoard(board);
        HashSet<Point> availableMoves;

        activeGame = true;
        while(activeGame) {

            if (!playerHasMoves.get(Const.BLACK) && !playerHasMoves.get(Const.WHITE))
                break;

            System.out.println("TURN: " + (turn == Const.BLACK ? "BLACK" : "WHITE"));

            availableMoves = board.getLegalMoves(players.get(turn));
            IO.printMoves(availableMoves);

            if (availableMoves.isEmpty()) {
                playerHasMoves.put(turn, false);
                turn = turn == Const.BLACK ? Const.WHITE : Const.BLACK;
                continue;
            } else {
                playerHasMoves.put(turn, true);
            }

            Point move = null;
            boolean isLegalMove = false;
            while (!isLegalMove) {
                move = players.get(turn).makeMove(availableMoves, new Board(board));
                for (Point m : availableMoves) {
                    if (m.col == move.col && m.row == move.row)
                        isLegalMove = true;
                }
            }

            System.out.println((turn == Const.BLACK ? "BLACK" : "WHITE") + " made the move: " + move.printInHumanLanguage());
            board.updateBoard(move, players.get(turn));
            IO.printBoard(board);
            turn = turn == Const.BLACK ? Const.WHITE : Const.BLACK;
        }

        int blackScore = board.getScore(players.get(Const.BLACK));
        int whiteScore = board.getScore(players.get(Const.WHITE));
        if (blackScore == whiteScore)
            IO.printDraw();
        else if (blackScore > whiteScore)
            IO.printWinner(players.get(Const.BLACK), blackScore-whiteScore);
        else
            IO.printWinner(players.get(Const.WHITE), whiteScore-blackScore);
    }

    private void resetGame() {
        board = new Board();
        players = new HashMap<>();
        playerHasMoves = new HashMap<>();
        turn = Const.BLACK;

        ConsolePlayer consolePlayer = (ConsolePlayer) IO.init();
        int aiColor = consolePlayer.getColor() == Const.BLACK ? Const.WHITE : Const.BLACK;

        AIPlayer aiPlayer = IO.initAI(aiColor);

        players.put(consolePlayer.getColor(), consolePlayer);
        players.put(aiPlayer.getColor(), aiPlayer);

        playerHasMoves.put(consolePlayer.getColor(), true);
        playerHasMoves.put(aiPlayer.getColor(), true);

        IO.printPlayers(players);
    }
}
