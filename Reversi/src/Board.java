import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Board {

    int[][] board;
    int[] score = {2, 2};

    Board() {
        board = new int[Const.SIZE][Const.SIZE];
        createBoard();
    }

    private void createBoard() {
        for (int i = 0; i < Const.SIZE; i++)
            for (int j = 0; j < Const.SIZE; j++)
                board[i][j] = Const.EMPTY;
        int mid = Const.SIZE/2;
        board[mid][mid] = Const.WHITE;
        board[mid-1][mid-1] = Const.WHITE;
        board[mid][mid-1] = Const.BLACK;
        board[mid-1][mid] = Const.BLACK;

    }

    int[][] getCurrentBoardState() {
        // Should probs be a Board

        int[][] boardCopy = new int[Const.SIZE][Const.SIZE];
        for (int i = 0; i < Const.SIZE ; i++)
            for (int j = 0; j < Const.SIZE ; j++)
                boardCopy[i][j] = board[i][j];


            return boardCopy;
    }


    public ArrayList<Point> getAvailableMoves(Player player) {
        ArrayList<Point> moves = new ArrayList<Point>();
        return moves;
    }

    public void makeMove(Point move, Player player) {
        // Update board based on move, flip bricks, etc.
        // Maybe one method per diagonal flip bricks

        int playerColor = player.getColor();
        int opponentColor = (playerColor == Const.BLACK) ? Const.WHITE : Const.BLACK;

        ArrayList<Point> flippedBricks = new ArrayList<>();
        flippedBricks.add(move);

        for (int i = 0; i < 8; ++i) {
            int row = move.row + DIRECTIONS[i].row;
            int col = move.col + DIRECTIONS[i].col;

            boolean hasOpBetween = false;
            ArrayList<Point> mayFlippedBricks = new ArrayList<>();

            while (pointOnBoard(row,col)) {

                if (board[row][col] == opponentColor)  {
                    hasOpBetween = true;
                    mayFlippedBricks.add(new Point(col,row));
                }
                else if ((board[row][col] == playerColor) && hasOpBetween) {
                    flippedBricks.addAll(mayFlippedBricks);
                    break;
                }
                else
                    break;

                row += DIRECTIONS[i].row;
                col += DIRECTIONS[i].col;
            }
        }

        updateBoardWithNewBricks(flippedBricks, player);
        updateScore(flippedBricks, player);
    }

    private void updateScore(ArrayList<Point> flippedBricks, Player player) {
        int opColor = player.getColor() == Const.BLACK ? Const.WHITE : Const.BLACK;
        System.out.println(flippedBricks.size());
        score[opColor-1] -= flippedBricks.size() - 1;
        score[player.getColor()-1] += flippedBricks.size();


    }


    private void updateBoardWithNewBricks(ArrayList<Point> flippedBricks, Player player) {
        for (Point p : flippedBricks) {
            board[p.row][p.col] = player.getColor();
        }
    }



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

    // May need boardStateMatrix or one board object for each state here later
    private boolean legalMove(Point move, Player player) {

        int[][] state = getCurrentBoardState();

        if(move.row > 7 || move.col > 7 )
            return false;

        if (state[move.row][move.col] != Const.EMPTY)
            return false;


        int playerColor = player.getColor();
        int opponentColor = (playerColor == Const.BLACK) ? Const.WHITE : Const.BLACK;


        for (int i = 0; i < 8; ++i) {
            int row = move.row + DIRECTIONS[i].row;
            int col = move.col + DIRECTIONS[i].col;

            boolean hasOpBetween = false;

            while (pointOnBoard(row,col)) {

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

    public HashSet<Point> getLegalMoves(Player player) {
        HashSet<Point> legalMoves = new HashSet<>();

        for (int i = 0; i < Const.SIZE ; i++)
            for (int j = 0; j < Const.SIZE ; j++) {
                Point point = new Point(i,j);
                if (legalMove(point, player))
                    legalMoves.add(point);

            }

        return legalMoves;
    }

    private boolean pointOnBoard(int row, int col) {
        return row >=0 && row < 8 && col >= 0 && col < 8;

    }


    public int getScore(Player p) {
        return score[p.getColor() - 1];
    }

}
