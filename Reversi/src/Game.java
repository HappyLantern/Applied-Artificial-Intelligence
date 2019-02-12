import java.util.HashMap;

class Game {

    private Board board;
    private HashMap<Integer,Player> players;
    private int turn;
    private boolean activeGame = false;

    Game() { }

    void start() {
        System.out.println("STARRTTIIIIIIIIIINGG JAJAHAHERY(UEHAEJEOANEPJGEAJIGOEAGJIEA");
        resetGame();

        activeGame = true;

        IO.printBoard(board);
        IO.printMoves(board.getLegalMoves(players.get(Const.BLACK)));
//        while(activeGame) {
//
//
//        }

    }

    private void resetGame() {
        board = new Board();
        players = new HashMap<>();
        turn = Const.BLACK;

        ConsolePlayer consolePlayer = (ConsolePlayer) IO.init();
        int aiColor = consolePlayer.getColor() == Const.BLACK ? Const.WHITE : Const.BLACK;
        AIPlayer aiPlayer = new AIPlayer("Mr MiniMax McAlphaBetaPruneFace", aiColor);

        players.put(consolePlayer.getColor(), consolePlayer);
        players.put(aiPlayer.getColor(), aiPlayer);

        IO.printPlayers(players);

    }
}
