import java.util.HashSet;

public interface Player {

    Point makeMove(HashSet<Point> moves, Board board);

    String getName();
    int getColor();
}
