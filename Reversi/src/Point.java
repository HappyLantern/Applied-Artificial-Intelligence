public class Point  {
    int col;
    int row;

    Point(int col, int row) {
        this.col = col;
        this.row = row;
    }


    String printInHumanLanguage() {
        String col = Character.toString((char)(this.col +65));
        return col + (row +1);
    }

}
