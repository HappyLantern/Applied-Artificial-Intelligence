public class Point  {
    int x;
    int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }


    String printInHumanLanguage() {
        String col = Character.toString((char)(x+65));
        return col + (y+1);
    }

}
