package game;

public class Game {

    private Board board;
    private Player[] player = new Player[2]();
    private Player black;
    private Player white;
    private int turn = Player.BLACK;

        public Game(Board board, Player black, Player white) {
          this.board = board;
          this.black = black;
          this.white = white;
          player[Player.BLACK] = black;
          player[Player.WHITE] = white;

        }

        public String start() {
          System.out.println("STARRTTIIIIIIIIIINGG JAJAHAHERY(UEHAEJEOANEPJGEAJIGOEAGJIEA");
          return ".";
          IO.init()

          while(activeGame) {


          }




        }
}
