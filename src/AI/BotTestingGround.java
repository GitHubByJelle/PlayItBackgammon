package AI;

import World.Board;
import World.Player;

public class BotTestingGround {
    public static void main(String []args){
        Board b= new Board();
        Player.Bot one= new BotA(0);
        Player.Bot two = new BotA(1);
        one.pausing=false;
        two.pausing=false;
        b.setPlayers(one,two);
        b.createBotLoop();
        while(!b.checkWinCondition()){
            b.getLoop().process();
            System.out.println(b);
        }

        if(b.getPlayer1().getPiecesOutOfPlay()==15){
            System.out.println("ONE WINS");
        }else if(b.getPlayer2().getPiecesOutOfPlay()==15) {
            System.out.println("TWO WINS");
        }else{
            System.out.println("Question Life decisions");
        }
    }
}
