import java.util.HashSet;

public interface Player {

    Point makeMove(HashSet<Point> moves);

    String getName();
    int getColor();
}
