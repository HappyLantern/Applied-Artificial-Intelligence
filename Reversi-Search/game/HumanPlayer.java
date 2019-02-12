package game;

public class HumanPlayer implements Player {


  private int color;
  private String name;

  public HumanPlayer(String name, int color) {
    this.name = name;
    this.color = color;
  }

  public boolean makeMove(Point p) {
    return false;
  }
}
