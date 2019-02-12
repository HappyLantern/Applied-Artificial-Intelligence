import java.util.ArrayList;

public interface Player {

    Point makeMove(ArrayList<Point> moves);

    String getName();
    int getColor();
}
