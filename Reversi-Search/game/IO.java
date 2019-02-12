package game;

// Preliminary done
public class IO {

    public void printBoard(Board b) {
      int[][] board = b.getBoard();
      System.out.println("A B C D E F G H");
      System.out.println("--------------------------");
      for (int i = 0; i < board.size; i++) {
        System.out.print(i+1 + " | ");
        for (int j = 0; j < board.size; j++) {
          System.out.print(board[i][j] +  " ");
        }
      }
    }

    public Point getMove(ArrayList<Point> moves) {
      Scanner scan = new Scanner(System.in);
      while(true) {
        String move = scan.nextLine();
        move.trim();
        int x = move.charAt(0)-65;
        int y = move.charAt(1);
        Point p = new Point(x,y);

        if (moves.contains(p)) {
          scan.close()
          return p;
        }
        System.out.println("Try agäin...");
      }
    }

    public Player init() {
      System.out.println("Hello!");
      System.out.println("Varm welcome to our implementation of reversi.
                        With the pleasentry out of the way all that remains is;
                        will you take the black pill or the white pill?
                        Think carefully about your next choice. This AI is deadly.
                        Enter BLACK or WHITE below:");
     Scanner scan = new Scanner(System.in);
     System.out.println("Enter name please: ");
     String name = scan.nextLine();
     System.out.println("Enter color please (BLACK/WHITE): ");
     String color = scan.nextLine().toUpper();
     int c = Player.WHITE;
     if (color.equals("BLACK"))
       c = Player.BLACK;
     return new Player(name, c);
    }

    public void printMoves(ArrayList<Point> moves) {
      for (Point p : moves) {
        System.out.print(p.print() + " ");
      }
      System.out.println();
    }

    public void printScore(int blackScore, int whiteScore) {
      System.put.println("Current score; Black: " + blackScore + "White: " + whiteScore);
    }
    public void printWinner(Player player, int scoreDiff) {
      if (scorediff == 0)
        System.out.println("It's a draw. Try again!");
      else
        System.out.println("Player " + player.name + " won with " + scoreDiff + " points. Congratulations!");
    }

}
