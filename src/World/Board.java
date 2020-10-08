package World;



import java.util.ArrayList;

public class Board {
    private Space[] spaces;
    private World.Die die;
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
/*

        addPieces(1, 2, 0);
        addPieces(6, 5, 1);
        addPieces(8, 3, 1);
        addPieces(12, 5, 0);

        addPieces(13, 5, 1);
        addPieces(17, 3, 0);
        addPieces(19, 5, 0);
        addPieces(24, 2, 1);

 */

   //     testboard A

       addPieces(1,3,1);
       addPieces(5,3,1);
      addPieces(4,3,1);
        addPieces(3,3,1);
        addPieces(2,3,1);

        addPieces(24,3,0);
        addPieces(20,3,0);
       addPieces(21,3,0);
        addPieces(22,3,0);
        addPieces(23,3,0);

        //testBoard B
//        addPieces(1,1,0);
//        addPieces(2,2,1);
//        addPieces(3,3,1);
//        addPieces(4,1,1);
//        addPieces(6,5,1);
//        addPieces(7,1,1);
//        addPieces(8,2,1);
//        addPieces(9,1,1);
//
//        addPieces(17,1,0);
//        addPieces(19,9,0);
//        addPieces(20,2,0);
//        addPieces(21,2,0);

        outOfPlay = new Space(26);

        //to correct for is home values of the pieces
        for (int i = 0; i < spaces.length; i++) {
            spaces[i].setDominateId();
            for (int a = 0; a < spaces[i].getPieces().size(); a++) {
                spaces[i].checkHome(spaces[i].getPieces().get(a));
            }
        }



    }
    public void createLoop(){
        gameLoop= new GameLoop(this);

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


    public String toString() {
        String res = String.format("%2d  %15s | %15s  %2d\n", 0, spaces[0], spaces[25], (25));
        for (int i = 1; i <= 12; i++) {
            res += String.format("%2d  %15s | %15s  %2d\n", i, spaces[i], spaces[25 - i], (25 - i));
        }

        return res;

    }

    public Space[] getSpaces() {
        return spaces;
    }

    public ArrayList<Space> getValidMoves(Space selected) {

        ArrayList<Space> res = new ArrayList<Space>();

        Space target;
        int[] roll = die.getCurRoll();

        int bigger=0;
        int smaller=0;

        for (int i = 0; i < roll.length; i++) {
            //check for eaten
            if(selected.getId() + roll[i] < 25 && selected.getId() + roll[i] > 0){//check for bounds
                target = spaces[selected.getId() + roll[i]];
                if (validityCheck(selected, target))
                    res.add(spaces[selected.getId() + roll[i]]);

            } else {
                //check if all the pieces are home in case the rolls can take the current piece out of play(eaten Space)
                if (allPiecesHome(selected.getPieces().get(0).getId())) {

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
        }return res;

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
        return target.isEmpty() || //if the target space is empty
                piecesOfSameColor(selected, target) || //if the space has pieces of the same color
                pieceCanBeEaten(selected, target); //target has one piece and its color doesnt match
    }

    private boolean piecesOfSameColor(Space selected, Space target) {
        return target.getPieces().get(0).getId() == selected.getPieces().get(0).getId();
    }

    private boolean pieceCanBeEaten(Space selected, Space target) {
        return (target.getPieces().size() == 1 && target.getPieces().get(0).getId() != selected.getPieces().get(0).getId());
    }


    public boolean playerMove(int from, int to) {
        int id = gameLoop.getCurrentPlayer().getId();

        ArrayList<Space> poss = getValidMoves(spaces[from]);
        if(to==26) {
            if(allPiecesHome(gameLoop.getCurrentPlayer().getId()) && poss.contains(outOfPlay)) {
                moveOutOfPlay(from);
                lastPlays(from,to);

                gameLoop.getCurrentPlayer().pieceOut();
                return true;
            }else{
                System.out.println("Move invalid: All pieces must be home");
                return false;
            }
        }else if ((to!=from) && validityCheck(spaces[from], spaces[to]) && poss.contains(spaces[to]) ) {
            if(from==0 ||from==25){
                gameLoop.getCurrentPlayer().revivePiece();
            }
            die.removeUsedRoll(to - from);
            spaces[from].movePiece(spaces[to]);
            gameLoop.checkEaten(to);
            return true;

        } else {
            System.out.println("Move invalid");
            return false;
        }
    }
    //Used for thinking in bot
    public void playerMoveNoCheck(int from, int to){
        spaces[from].movePiece(spaces[to]);
    }

    public Die getDie() {
        return die;
    }


    private boolean checkIfCanDominate(int pieceId, Space to) {
        int size = to.getSize();
        if (to.getDominantId() != pieceId) {
            return size <= 1;
        }
        return true;
    }


    public boolean checkWinCondition() {
        return player1.getPiecesOutOfPlay()==15 ||player2.getPiecesOutOfPlay()==15;
    }


    public  void setPlayers(String one, String two){
        //if the player is Human
        for(int i = 0; i< Utils.Variables.PLAYERS.length; i++){
            //if the player is Human
            if(one.equals(Utils.Variables.PLAYERS[i])){
                player1= new World.Player(0,one);
            }

            if(two.equals(Utils.Variables.PLAYERS[i])){
                player2= new World.Player(1,two);
            }
        }
    }

    public Player getPlayer1(){return player1;}
    public Player getPlayer2(){return player2;}
    public GameLoop getLoop(){return gameLoop;}


    public void moveToEatenSpace(int k){
        Piece p= spaces[k].getPieces().get(0);
        if(p.getId()==player1.getId())
            spaces[k].movePiece(spaces[0]);
        else
            spaces[k].movePiece(spaces[25]);
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

}

