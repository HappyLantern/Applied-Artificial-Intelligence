import java.util.ArrayList;
import java.util.HashSet;

public class Board {

    int[][] board;


    private ArrayList<Point> blackScore;
    private ArrayList<Point> whiteScore;

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

    public void makeMove(Point move) {
        // Update board based on move, flip bricks, etc.
        // Maybe one method per diagonal flip bricks
        updateScore();
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

        if(move.y > 7 || move.x > 7 )
            return false;

        if (state[move.y][move.x] != Const.EMPTY)
            return false;


        int playerColor = player.getColor();
        int opponentColor = (playerColor == Const.BLACK) ? Const.WHITE : Const.BLACK;


        for (int i = 0; i < 8; ++i) {
            int row = move.y + DIRECTIONS[i].y;
            int col = move.x + DIRECTIONS[i].x;

            boolean hasOpBetween = false;

            while (pointOnBoard(row,col)) {

                if (board[row][col] == opponentColor)
                    hasOpBetween = true;
                else if ((board[row][col] == playerColor) && hasOpBetween)
                    return true;
                else
                    break;

                row += DIRECTIONS[i].y;
                col += DIRECTIONS[i].x;
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


    private void updateScore()  {

    }

    public int getScore() {
        return 0;
    }

}
