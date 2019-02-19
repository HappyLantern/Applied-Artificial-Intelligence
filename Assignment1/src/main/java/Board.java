import java.util.ArrayList;
import java.util.HashSet;

public class Board {

  int[] score = {2, 2};
  int[][] board;
  private static final Point[] DIRECTIONS = {
            new Point(-1, -1),  // NW
            new Point(0, -1),   // N
            new Point(1, -1),   // NE
            new Point(1, 0),    // E
            new Point(1, 1),    // SE
            new Point(0, 1),    // S
            new Point(-1, 1),   // SW
            new Point(-1, 0),   // W
    };

    public Board() {
        board = new int[Const.SIZE][Const.SIZE];
        createBoard();
    }

    public Board(Board oldBoard) {
        this.board = oldBoard.getCurrentBoardState();
        this.score[Const.BLACK - 1] = oldBoard.score[Const.BLACK - 1];
        this.score[Const.WHITE - 1] = oldBoard.score[Const.WHITE - 1];

    }

    public int[][] getCurrentBoardState() {
        int[][] boardCopy = new int[Const.SIZE][Const.SIZE];

        for (int i = 0; i < Const.SIZE; i++)
            System.arraycopy(board[i], 0, boardCopy[i], 0, Const.SIZE);

        return boardCopy;
    }

    public void updateBoard(Point move, Player currentPlayer) {
        int opponentColor = (currentPlayer.getColor() == Const.BLACK) ? Const.WHITE : Const.BLACK;

        ArrayList<Point> flippedBricks = new ArrayList<>();
        flippedBricks.add(move);
        for (int i = 0; i < 8; ++i) {
            int row = move.row + DIRECTIONS[i].row;
            int col = move.col + DIRECTIONS[i].col;

            boolean hasOpBetween = false;
            ArrayList<Point> mayFlippedBricks = new ArrayList<>();

            while (pointOnBoard(row, col)) {

                if (board[row][col] == opponentColor) {
                    hasOpBetween = true;
                    mayFlippedBricks.add(new Point(col, row));
                } else if ((board[row][col] == currentPlayer.getColor()) && hasOpBetween) {
                    flippedBricks.addAll(mayFlippedBricks);
                    break;
                } else
                    break;

                row += DIRECTIONS[i].row;
                col += DIRECTIONS[i].col;
            }
        }
        updateBoardWithNewBricks(flippedBricks, currentPlayer);
        updateScore(flippedBricks, currentPlayer);
    }

    public HashSet<Point> getLegalMoves(Player player) {
        HashSet<Point> legalMoves = new HashSet<>();

        for (int i = 0; i < Const.SIZE; i++)
            for (int j = 0; j < Const.SIZE; j++) {
                Point point = new Point(i, j);
                if (legalMove(point, player))
                    legalMoves.add(point);
            }
        return legalMoves;
    }

    public boolean isTerminal(Player black, Player white) {
        HashSet<Point> legalMoves1 = getLegalMoves(black);
        HashSet<Point> legalMoves2 = getLegalMoves(white);
        if (legalMoves1.isEmpty() && legalMoves2.isEmpty())
            return true;
        return false;
    }

    public int getUtilityValue(int color, int opColor) {

          int sum = 0;
          // endgame
          if (score[Const.WHITE-1] + score[Const.BLACK-1] > 58) {
              for (int i = 0; i < 8; i++)
                  for (int j = 0; j < 8; j++)
                      if (board[i][j] == color)
                          sum += 1;
              return sum;
          }
          // init
          int n = 0;
          for (int i = 0; i < 8; i++) {
              for (int j = 0; j < 8; j++) {
                  if (board[i][j] == color)
                      sum += Const.eval[n];
                  n++;
              }
          }
          return sum;
      }

    public int getScore(Player p) {
        return score[p.getColor() - 1];
    }

    private void createBoard() {
        for (int i = 0; i < Const.SIZE; i++)
            for (int j = 0; j < Const.SIZE; j++)
                board[i][j] = Const.EMPTY;
        int mid = Const.SIZE / 2;
        board[mid][mid] = Const.WHITE;
        board[mid - 1][mid - 1] = Const.WHITE;
        board[mid][mid - 1] = Const.BLACK;
        board[mid - 1][mid] = Const.BLACK;
    }

    private void updateScore(ArrayList<Point> flippedBricks, Player player) {
        int opColor = player.getColor() == Const.BLACK ? Const.WHITE : Const.BLACK;
        score[opColor - 1] -= flippedBricks.size() - 1;
        score[player.getColor() - 1] += flippedBricks.size();
    }

    private void updateBoardWithNewBricks(ArrayList<Point> flippedBricks, Player player) {
        for (Point p : flippedBricks) {
            board[p.row][p.col] = player.getColor();
        }
    }

    private boolean legalMove(Point move, Player player) {
        int[][] state = getCurrentBoardState();

        if (move.row > 7 || move.col > 7)
            return false;

        if (state[move.row][move.col] != Const.EMPTY)
            return false;

        int playerColor = player.getColor();
        int opponentColor = (playerColor == Const.BLACK) ? Const.WHITE : Const.BLACK;

        for (int i = 0; i < 8; ++i) {
            int row = move.row + DIRECTIONS[i].row;
            int col = move.col + DIRECTIONS[i].col;

            boolean hasOpBetween = false;

            while (pointOnBoard(row, col)) {
                if (board[row][col] == opponentColor)
                    hasOpBetween = true;
                else if ((board[row][col] == playerColor) && hasOpBetween)
                    return true;
                else
                    break;

                row += DIRECTIONS[i].row;
                col += DIRECTIONS[i].col;
            }
        }
        return false;
    }

    private boolean pointOnBoard(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}
