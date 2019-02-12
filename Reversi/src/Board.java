import java.util.ArrayList;

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

    private void updateScore()  {

    }

    public int getScore() {
        return 0;
    }

}
