import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class IO {

    public static void printBoard(Board b) {
        int[][] board = b.getCurrentBoardState();
        System.out.println(" ");
        System.out.println("    A B C D E F G H");
        System.out.println("--------------------------");
        for (int i = 0; i < board.length; i++) {
            System.out.print(i+1 + " | ");
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j] +  " ");
            }
            System.out.println(" ");
        }
    }

    public static Point getMove(ArrayList<Point> moves) {
        Scanner scan = new Scanner(System.in);
        while(true) {
            printMoves(moves);

            String move = scan.nextLine();
            move.trim();
            int x = move.charAt(0)-65;
            int y = move.charAt(1);
            Point p = new Point(x,y);

            if (moves.contains(p)) {
                scan.close();
                return p;
            }
            System.out.println("Try again...");
        }
    }

    public static Player init() {
        System.out.println("Hello!");
        System.out.println("Warm welcome to our implementation of Reversi! \n");
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

    public static void printMoves(ArrayList<Point> moves) {
        for (Point p : moves)
            System.out.print(p.print() + " ");
        System.out.println();
    }

    public static void printScore(int blackScore, int whiteScore) {
        System.out.println("Current score; Black: " + blackScore + "White: " + whiteScore);
    }
    public static void printWinner(Player player, int scoreDiff) {
        System.out.println("Player " + player.getName() + " won with " + scoreDiff + " points. Congratulations!");
    }
    public static void printDraw() {
        System.out.println("It's a draw. Try again!");

    }

    public static void printPlayers(HashMap<Integer,Player> playerHashMap) {
        System.out.println(" ");
        System.out.println("BLACK - " + playerHashMap.get(Const.BLACK).getName()) ;
        System.out.println("WHITE - " + playerHashMap.get(Const.WHITE).getName()) ;

    }
}
