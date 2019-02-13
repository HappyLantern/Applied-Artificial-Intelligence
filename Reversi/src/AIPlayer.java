import java.util.HashSet;

public class AIPlayer implements Player {
    private int color;
    private String name;
    private int reasoning;

    public AIPlayer(String name, int color, int reasoning) {
        this.name = name;
        this.color = color;
        this.reasoning = reasoning;
    }

    @Override
    public Point makeMove(HashSet<Point> moves) {
        return moves.iterator().next();
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
