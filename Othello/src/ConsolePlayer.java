import java.util.HashSet;

public class ConsolePlayer implements Player {

  private int color;
  private String name;

  public ConsolePlayer(String name, int color) {
    this.name = name;
    this.color = color;
  }

  @Override
  public Point makeMove(HashSet<Point> moves, Board board) {

    //return moves.iterator().next();
    return IO.getConsolePlayerMove(moves);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getColor() {
    return color;
  }
}
