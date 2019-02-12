import java.util.ArrayList;

public class AIPlayer implements Player {
    private int color;
    private String name;

    public AIPlayer(String name, int color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public Point makeMove(ArrayList<Point> moves) {
        return null;
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
