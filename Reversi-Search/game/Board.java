package game;

import java.util.ArrayList;

public class Board {

  int[][] board;
  private static final int SIZE = 8;
  private static final int EMPTY = 0;
  private ArrayList<Point> blackScore;
  private ArrayList<Point> whiteScore;

  public Board() {
    board = new int[SIZE][SIZE];
    resetBoard();
  }

  public void resetBoard() {
    for (int i = 0; i < SIZE; i++)
      for (int j = 0; j < SIZE; j++)
        board[i][j] = EMPTY;
    int mid = SIZE/2;
    board[mid][mid] = Player.WHITE;
    board[mid-1][mid-1] = Player.WHITE;
    board[mid][mid-1] = Player.BLACK;
    board[mid-1][mid] = Player.BLACK;
    score = new int[] {0, 0};
  }

  public ArrayList<Point> getMoves(Player player) {
    ArrayList<Point> moves = new ArrayList<Point>();
    return moves;
    }

  public void updateBoard(String move) {
    // Update board based on move, flip bricks, etc.
    // Maybe one method per diagonal flip bricks
    updateScore();
  }

  private void updateScore()  {

  }

  public int[] getScore() {
    return score;
  }
}
