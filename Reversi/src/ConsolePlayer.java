import java.util.ArrayList;

public class ConsolePlayer implements Player {
    private int color;
    private String name;

    public ConsolePlayer(String name, int color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public Point makeMove(ArrayList<Point> moves) {
        return IO.getMove(moves);
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
