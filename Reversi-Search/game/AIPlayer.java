package game;

public class AIPlayer implements Player {

  private int color;
  private String name;

  public AIPlayer(String name, int color) {
    this.name = name;
    this.color = color;
  }


  // Min-max search with alpha-beta pruning goes here
  public boolean makeMove(Point p) {
    return false;
  }
}
