import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class IO {

    static Point getConsolePlayerMove(HashSet<Point> moves) {
        Scanner scan = new Scanner(System.in);

        String move = scan.nextLine().toUpperCase();
        int col = move.charAt(0) - 65;
        int row = move.charAt(1) - 49;
        Point p = new Point(col, row);

        return p;
    }

    static Player init() {
        System.out.println("Hello!");
        System.out.println("Warm welcome to our implementation of Othello! \n");
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter name please: ");
        String name = scan.nextLine();
        System.out.println("Enter color please (BLACK/WHITE): ");
        String colorInput = scan.nextLine().toUpperCase();

        int playerColor = Const.WHITE;
        if (colorInput.equals("BLACK") || colorInput.equals("B"))
            playerColor = Const.BLACK;

        return new ConsolePlayer(name, playerColor);
    }

    static AIPlayer initAI(int aiColor) {
        Scanner scan = new Scanner(System.in);
        System.out.println("How much reasoning time (Unit: ms) is Mr MiniMax McAlphaBetaPruneFace allowed to have?");
        long reasoning = scan.nextLong();
        return new AIPlayer("Mr MiniMax McAlphaBetaPruneFace", aiColor, reasoning);
    }

    static void printBoard(Board b) {
        int[][] board = b.getCurrentBoardState();
        System.out.println("Current score; \nBlack: " + b.score[Const.BLACK - 1] + "\nWhite: " + b.score[Const.WHITE - 1]);
        System.out.println(" ");
        System.out.println("    A B C D E F G H");
        System.out.println("--------------------------");
        for (int i = 0; i < board.length; i++) {
            System.out.print(i + 1 + " | ");
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println(" ");
        }
    }

    static void printMoves(HashSet<Point> moves) {
        System.out.println(" ");
        for (Point p : moves)
            System.out.print(p.printInHumanLanguage() + " ");
        System.out.println();
    }

    static void printWinner(Player player, int scoreDiff) {
        System.out.println("Player " + player.getName() + " won with " + scoreDiff + " points. Congratulations!");
    }

    static void printDraw() {
        System.out.println("It's a draw. Try again!");
    }

    static void printPlayers(HashMap<Integer, Player> playerHashMap) {
        System.out.println(" ");
        System.out.println("BLACK - " + playerHashMap.get(Const.BLACK).getName());
        System.out.println("WHITE - " + playerHashMap.get(Const.WHITE).getName());
    }

    public static void printMiniMaxSearchInfo(long time, int searchedNodes, int searchDepth) {
        System.out.println(" ");
        System.out.println("Time: " + time + "ms");
        System.out.println("Iterated nodes: " + searchedNodes);
        System.out.println("Depth: " + searchDepth);


    }
}
