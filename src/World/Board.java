package World;



import AI.*;
import Utils.Variables;

import javax.swing.*;
import java.util.ArrayList;

public class Board {
    private Space[] spaces;
    private Die die;
    private Space outOfPlay;

    private World.Player player1;
    private World.Player player2;
    private GameLoop gameLoop;

    public Board() {
        die = new World.Die();
        die.generateDie();
        die.getNextRoll();//remove this later

        spaces = new Space[26];
        //0 is for the "eaten" for player 1, 25 is is for player 2 ,
        // 1-12 is the bottom(right-left),
        // 13-24 is top (Left to right),
        // out of play has id 26 but is not included in the array because it should not be accessable
        createSpaces();

        //actuall board

        addPieces(1, 2, 0);
        addPieces(6, 5, 1);
        addPieces(8, 3, 1);
        addPieces(12, 5, 0);

        addPieces(13, 5, 1);
        addPieces(17, 3, 0);
        addPieces(19, 5, 0);
        addPieces(24, 2, 1);


//     testboard A
//
//       addPieces(18,1,1);

//       addPieces(1,3,1);
//       addPieces(5,3,1);
//       addPieces(4,3,1);
//       addPieces(3,3,1);
//       addPieces(2,3,1);
//
//       addPieces(24,3,0);
//       addPieces(20,3,0);
//       addPieces(21,3,0);
//       addPieces(22,3,0);
//       addPieces(23,3,0);
        outOfPlay = new Space(26);

        //to correct for is home values of the pieces
        forceHomeCheck();
    }


    public void forceHomeCheck(){
        for (int i = 0; i < spaces.length; i++) {
            for (int a = 0; a < spaces[i].getPieces().size(); a++) {
                spaces[i].checkHome(spaces[i].getPieces().get(a));
            }
        }
    }
    //methods for board creation
    private void addPieces(int spaceIndex, int num, int colorId) {
        for (int i = 0; i < num; i++)
            spaces[spaceIndex].getPieces().add(new World.Piece(colorId));
    }
    private void createSpaces() {
        int x = 0;
        for (int i = 0; i < spaces.length; i++) {
            spaces[i] = new Space(i);
        }
    }

    //validity checks
    public ArrayList<Space> getValidMoves(Space selected) {

        ArrayList<Space> res = new ArrayList<Space>();

        Space target;
        int[] roll = die.getCurRoll();

        int bigger=0;
        int smaller=0;

        for (int i = 0; i < roll.length; i++) {
            if(selected.getId() + roll[i] < 25 && selected.getId() + roll[i] > 0){//check for bounds
                target = spaces[selected.getId() + roll[i]];
                if (validityCheck(selected, target)) {
                    res.add(spaces[selected.getId() + roll[i]]);
                }

            } else {
                //check if all the pieces are home in case the rolls can take the current piece out of play(eaten Space)
                if (!selected.isEmpty() &&allPiecesHome(selected.getPieces().get(0).getId())) {

                    if (selected.getId()>6) {
                        for (int j = 24; j > 18; j--) {
                            if (spaces[j].getPieces().size() > 0) {
                                bigger = j;
                            }
                        }

                        if (die.getCurRoll().length > 1) {
                            if ((25 - selected.getId()) == die.getCurRoll()[0] || (25 - selected.getId()) == die.getCurRoll()[1])
                                res.add(outOfPlay);

                            else if (selected.getId()== bigger && die.getCurRoll()[0] > (25 - selected.getId()) || selected.getId()== bigger && die.getCurRoll()[1] > (25 - selected.getId())) {
                                res.add(outOfPlay);
                            }
                            else if (selected.getId() > bigger && die.getCurRoll()[0] > (25 - selected.getId()) && selected.getId() > bigger && die.getCurRoll()[1] > (25 - selected.getId())) {
                                res.remove(outOfPlay);
                            }
                        } else {
                            if ((25 - selected.getId()) == die.getCurRoll()[0])
                                res.add(outOfPlay);

                            else if (selected.getId() == bigger && die.getCurRoll()[0] > (25 - selected.getId())) {
                                res.add(outOfPlay);
                            } else if (selected.getId() > bigger && die.getCurRoll()[0] > (25 - selected.getId())) {
                                res.remove(outOfPlay);
                            }
                        }
                    } else {
                        for (int j = 1; j < 6; j++) {
                            if (spaces[j].getPieces().size() > 0) {
                                bigger = j;
                            }
                        }
                        if (die.getCurRoll().length > 1) {
                            if (selected.getId() == Math.abs(die.getCurRoll()[0]) || selected.getId() == Math.abs(die.getCurRoll()[1]))
                                res.add(outOfPlay);

                            else if (selected.getId() == bigger && Math.abs(die.getCurRoll()[0]) > selected.getId() || selected.getId() == bigger && Math.abs(die.getCurRoll()[1]) > selected.getId() ) {
                                res.add(outOfPlay);
                            }
                            else if (selected.getId() < bigger && Math.abs(die.getCurRoll()[0]) > selected.getId() && Math.abs(die.getCurRoll()[1]) > selected.getId()) {
                                res.remove(outOfPlay);
                            }
                        } else {
                            if (selected.getId() == Math.abs(die.getCurRoll()[0]))
                                res.add(outOfPlay);

                            else if (selected.getId() == bigger && Math.abs(die.getCurRoll()[0]) > selected.getId()) {
                                res.add(outOfPlay);
                            }
                            else if (selected.getId() < bigger && Math.abs(die.getCurRoll()[0]) > selected.getId()) {
                                res.remove(outOfPlay);
                            }

                        }
                    }
                }
            }
        }
        return res;
    }


    private boolean allPiecesHome(int pieceID) {
        Piece cur;
        for (int i = 0; i < spaces.length; i++) {
            for (int x = 0; x < spaces[i].getPieces().size(); x++) {
                cur = spaces[i].getPieces().get(x);

                if (cur.getId() == pieceID && !cur.isHome) {
                    return false;
                }
            }
        }
        return true;
    }

    //check if the target space is empty or if it has pieces of the same color, or if it has 1 piece of the opposite color
    public boolean validityCheck(Space selected, Space target) {
        return !selected.isEmpty() &&(target.isEmpty() || //if the target space is empty
                piecesOfSameColor(selected, target) || //if the space has pieces of the same color
                pieceCanBeEaten(selected, target)); //target has one piece and its color doesnt match
    }

    private boolean piecesOfSameColor(Space selected, Space target) {
        return target.getPieces().get(0).getId() == selected.getPieces().get(0).getId();
    }

    private boolean pieceCanBeEaten(Space selected, Space target) {
        return (target.getPieces().size() == 1 && target.getPieces().get(0).getId() != selected.getPieces().get(0).getId());
    }


    public boolean playerMove(int from, int to) {
//        System.out.println(gameLoop.getCurrentPlayer());
//        System.out.println(gameLoop.getCurrentPlayer().getId());

        int id = gameLoop.getCurrentPlayer().getId();

        ArrayList<Space> poss = getValidMoves(spaces[from]);
        if(to==26) {

            if(allPiecesHome(gameLoop.getCurrentPlayer().getId()) && poss.contains(outOfPlay)) {
                moveOutOfPlay(from);
                lastPlays(from,to);

                gameLoop.getCurrentPlayer().pieceOut();
                gameLoop.repaintBV();
                return true;
            }else{
                System.out.println("Move invalid: All pieces must be home");
                return false;
            }
        }else if ((to!=from) && validityCheck(spaces[from], spaces[to]) && poss.contains(spaces[to]) ) {
            if(from==0 ||from==25){
                gameLoop.getCurrentPlayer().revivePiece();
                gameLoop.repaintBV();
            }
            die.removeUsedRoll(to - from);
            spaces[from].movePiece(spaces[to]);
            gameLoop.checkEaten(to);
            gameLoop.repaintBV();
            return true;

        } else {
            this.getDie().printCurRoll();
            System.out.println("from: " + from + "  to: " + to);
            System.out.println( validityCheck(spaces[from], spaces[to]));
            System.out.println(poss.contains(spaces[to]));
            System.out.println(this.toString());
            System.out.println("Move invalid");
            return false;
        }

    }

    //Used for thinking in bot
    public void playerMoveNoCheck(int from, int to, int pieceID){
        if(to>=26){
            spaces[from].getPieces().remove(0);
        }else if(from==26){
            spaces[to].getPieces().add(new Piece(pieceID));

        } else{
            spaces[from].moveBotPiece(spaces[to]);
        }
    }

    //1 liners
    public Space[] getSpaces() {
        return spaces;
    }
    public void setDice(Die die){ this.die = die; }
    public GameLoop getGameLoop(){ return this.gameLoop; }
    public Die getDie() { return die; }
    public boolean checkWinCondition() { return player1.getPiecesOutOfPlay()==15 ||player2.getPiecesOutOfPlay()==15; }
    public void createLoop(JFrame frame){ gameLoop= new GameLoop(this, frame); }
    public void createBotLoop(){ gameLoop= new GameLoop(this); }
    public Player getPlayer1(){return player1;}
    public Player getPlayer2(){return player2;}

    //players setup
    public  void setPlayers(String one, String two){

        if(one.equals(Variables.HUMAN)){
            player1= new Player.Human(0);
        }
        if(two.equals(Variables.HUMAN)){
            player2= new Player.Human(1);
        }
        if(one.equals(Variables.BOTA)){
            player1= new BotA(0);
            player1.setBoard(this);
        }
        if(two.equals(Variables.BOTA)){
            player2= new BotA(1);
            player2.setBoard(this);
        }
        if(one.equals(Variables.SIMPLEBOT)){
            player1= new SimpleBot(0);
            player1.setBoard(this);
        }
        if(two.equals(Variables.SIMPLEBOT)){
            player2= new SimpleBot(1);
            player2.setBoard(this);
        }
        if(one.equals(Variables.BP)){
            player1= new PBE(0);
            player1.setBoard(this);
        }
        if(two.equals(Variables.BP)){
            player2= new PBE(1);
            player2.setBoard(this);
        }


//        if(player1==null ||player2==null) {
//            for (int i = 1; i < Utils.Variables.GET_PLAYER_TYPES().length; i++) {
//                if (one.equals(Utils.Variables.GET_PLAYER_TYPES()[i])) {
//                    player1 = new Player.Bot(0, Variables.PLAYERS[i]);
//                }
//                if (one.equals(Utils.Variables.GET_PLAYER_TYPES()[i])) {
//                    player2 = new Player.Bot(1,Variables.PLAYERS[i]);
//                }
//
//            }
//        }
    }
    public void setPlayers(Player one, Player two){
        player1=one;
        player1.setBoard(this);
        player2=two;
        player2.setBoard(this);
    }



    public void moveToEatenSpace(int k){
        Piece p= spaces[k].getPieces().get(0);
        if(p.getId()==player1.getId())
            spaces[k].movePiece(spaces[0]);
        else
            spaces[k].movePiece(spaces[25]);
    }

    public void moveBackFromEatenSpace(int k, Piece p){
        if(p.getId()==player1.getId())
            spaces[0].movePiece(spaces[k]);
        else
            spaces[25].movePiece(spaces[k]);
    }
    public void moveBackFromEatenSpaceID(int k, int p){
        if(p==player1.getId())
            spaces[0].movePiece(spaces[k]);
        else
            spaces[25].movePiece(spaces[k]);
    }

    public void moveOutOfPlay(int k){
        spaces[k].movePiece(outOfPlay);
    }
    private void doNotAllow(Space space) {
        space.movePiece(outOfPlay);
        System.out.println("not allowed, you first need to advance the further pieces");
    }

    public void lastPlays(int from, int to) {
        int newFrom= 26-from;
        if (from > 6) {
            if (26 - (from + 1) <= 6) {
                if (die.getCurRoll().length > 1) {
                    if (26 - (from + 1) == die.getCurRoll()[0] || 26 - (from + 1) == die.getCurRoll()[1])
                        die.removeUsedRoll(to - (from + 1));
                    else if (26 - (from + 1) < die.getCurRoll()[0] || 26 - (from + 1) < die.getCurRoll()[1])
                        die.removeUsedRollOutOfPlay();
                } else {
                    if (26 - (from + 1) == die.getCurRoll()[0])
                        die.removeUsedRoll(to - (from + 1));
                    else if (26 - (from + 1) < die.getCurRoll()[0])
                        die.removeUsedRollOutOfPlay();
                }
            }
        } else if (26 - newFrom <= 6)
            if (die.getCurRoll().length > 1) {
                if (-(26 - newFrom) == die.getCurRoll()[0] || -(26 - (newFrom)) == die.getCurRoll()[1])
                    die.removeUsedRoll(-(26 - newFrom));
                else if (-(26 - newFrom) > die.getCurRoll()[0] || -(26 - newFrom) > die.getCurRoll()[1])
                    die.removeUsedRollOutOfPlay();
            } else {
                if (-(26 - newFrom) == die.getCurRoll()[0])
                    die.removeUsedRoll(-(26 - newFrom));
                else if (-(26 - newFrom) > die.getCurRoll()[0])
                    die.removeUsedRollOutOfPlay();
            }
    }

    public void checkAllPiecesHome(){
        for(Space space : this.getSpaces()){
            for(Piece piece: space.getPieces()){
                space.checkHome(piece);
                //System.out.println(piece.isHome);
                if(!piece.isHome){
                    //System.out.println(piece.toString());
                }
            }
        }
    }
    //Probably temporary but thanks to this function we can work on Evolution until the unknown bug is fixed
    public void BotMove(int from, int to){
        int id = gameLoop.getCurrentPlayer().getId();

        ArrayList<Space> poss = getValidMoves(spaces[from]);
        if(to==26) {
            moveOutOfPlay(from);
            lastPlays(from,to);

            gameLoop.getCurrentPlayer().pieceOut();


        }else if ((to!=from) && validityCheck(spaces[from], spaces[to]) && poss.contains(spaces[to]) ) {
            if(from==0 ||from==25){
                gameLoop.getCurrentPlayer().revivePiece();
            }
            die.removeUsedRoll(to - from);
            spaces[from].movePiece(spaces[to]);
            gameLoop.checkEaten(to);

        } else {
//            System.out.println(to);
//            System.out.println(from);
//            System.out.println( validityCheck(spaces[from], spaces[to]));
//            System.out.println(poss.contains(spaces[to]));
//            System.out.println(this.toString());
            System.out.println("Move invalid");
        }
    }

     public void botMove(Move move){
        if(isGoingToEat(move.from,move.to,move.playerId)){
            move.isKill = true;
        }
        if(isGoingOut(move.from,move.to,move.playerId)){
            move.isMoveOut = true;
        }
        BotMove(move.from, move.to);

    }
    private boolean isGoingToEat(int from, int to, int id){
        if(this.getSpaces()[to].getSize() == 1){
            if (this.getSpaces()[to].getPieces().get(0).getId() != id){
                return true;
            }
        }
        return false;
    }
    private boolean isGoingOut(int from, int to, int id){
        if(this.getSpaces()[to].getSize() == 26 ){
                return true;
        }
        return false;
    }


    @Override
    public String toString() {
        String res = String.format("%2d  %15s | %15s  %2d\n", 0, spaces[0], spaces[25], (25));
        for (int i = 1; i <= 12; i++) {
            res += String.format("%2d  %15s | %15s  %2d\n", i, spaces[i], spaces[25 - i], (25 - i));
        }

        return res;

    }

}

