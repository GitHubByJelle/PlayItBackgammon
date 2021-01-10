package AI.GA;
import World.*;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Timer;


public class TMM extends Player.Bot{

//    assume board is structured in list from 1-25
//    standard start position
    //TODO Make gameloop for Bot to train/game in
    //TODO wait for Double moves
    //TODO Use profiles in bot to make player 1 think 1 move ahead and player 2 2 or 3 moves ahead. Using winning as evolution for bot
    //TODO Optimize selection out of ValidMoves using GA

    //0 is white and 1 is red
    public double[] weightsarr = {1.51317561056466, 0.007260608076508351, 0.509931152361962, 0.29851968813689045, 0.2, 2.0, 1.5000000000000004};
    public TMM(int id) {
        super(id);
    }
    public TMM(int id, double[] weightsarr) {
        super(id);
        this.weightsarr = weightsarr;
    }
    public double EvaluationFunc(){
        int [][] currentBoard = getBoardRep();
        int [][] home= getHome(currentBoard);
        return this.OtherPiecesSlain()*weightsarr[0] + this.pipCount()*weightsarr[1] + this.DoneScore()*weightsarr[2] + this.DoneBoardScore()*weightsarr[3] + this.piecesAlone()*weightsarr[4] + this.countWalls(home, 2)*weightsarr[5] + this.countHighStacks(home)*weightsarr[6];
    }
    private int [][] getBoardRep(){
        int[][] board = new int[24][3];//{id,num pieces this player, num pieces opp}
        for(int i=0;i<board.length;i++){
            board[i][0]=B.getSpaces()[i+1].getId();
            if(!B.getSpaces()[i+1].isEmpty() && B.getSpaces()[i+1].getPieces().get(0).getId()==id)
                board[i][1]=B.getSpaces()[i+1].getSize();
            else
                board[i][1]=0;

            if(!B.getSpaces()[i+1].isEmpty() && B.getSpaces()[i+1].getPieces().get(0).getId()!=id)
                board[i][2]=B.getSpaces()[i+1].getSize();
            else
                board[i][2]=0;


        }
        return board;
    }
    private int countWalls(int[][] currentHomeSpace, int wallSize) {
        int numWalls=0;
        for(int i=0;i<currentHomeSpace.length;i++){
            if(currentHomeSpace[i][1]>=wallSize){
                ++numWalls;
            }
        }
        return numWalls;
    }

    private int countHighStacks(int[][] home) {
        int res =0;
        int totalPieces=0;
        for(int i=0;i<home.length; i++){
            if(home[i][1]>=4){
                ++res;
            }
            totalPieces+=home[i][1];
        }
        if(totalPieces==15){
            res=0;
        }
        return -res;
    }
    private int[][] getHome(int[][] currentBoard) {
        int[][] homeSpaces= new int[6][3];//per space, [id,number of this player's pieces] {{6,5},{5,0},{4,0}{3,0},{2,0},{1,2}}
        int spaceIndex=0;
        if(id==0){
            for(int i=19;i<=24;i++){
                homeSpaces[spaceIndex]= new int[]{i, currentBoard[i-1][1]};
                ++spaceIndex;
            }
        }else{
            for(int i=6;i>=1;i--){
                homeSpaces[spaceIndex]= new int[]{i, currentBoard[i-1][1]};
                ++spaceIndex;
            }
        }
        return homeSpaces;
    }
//    public void TwoDeepLoop(){
//        while (this.B.getDie().getCurRoll().length > 0) {
//            this.B.checkAllPiecesHome();
//            this.ExecuteDeeperMove();
//        }
//    }

//    //returns 0 if W lost, 1 if W won
//    public int SingleGameLoop(){
//        while(!this.B.checkWinCondition()){
//            if(this.B.getGameLoop().getCurrentPlayer().getId() == 1){
//                this.TwoDeepLoop();
//            }else{
//                this.PlayerLoop();
//
//            } }
//        if(this.B.getGameLoop().getCurrentPlayer().getId() == 0){
//            return 0;
//        }
//        return 1;
//    }


    public ArrayList<Space> GetHighestMoves(ArrayList<Space> selected_spaces){
        ArrayList<Space> moves = new ArrayList<Space>();
        for(int i = 0; i<selected_spaces.size(); i++){
            ArrayList<Space> submoves = this.B.getValidMoves(selected_spaces.get(i));
            if(submoves.size()>0) {
                moves.add(GetHighestSubSpace(selected_spaces.get(i), submoves));
            }else {
                selected_spaces.remove(i);
                i = i-1;
            }

        }
        return moves;
    }
    //TODO Alpha Beta Pruning
    public double[] GetBestMoveCheat(int deepnessconstr, boolean player){
        ArrayList<Space> all_selected = GetAllSelectedSpaces();
        ArrayList<Space> all_highest_moves = GetHighestMoves(all_selected);
        double[] value_moves = new double[all_selected.size()];
        double[] bestmove;
        boolean eatMove = false;
        int pieceID =0;
        int validroll;
        if(all_highest_moves.size()>0) {
            for (int i = 0; i < all_selected.size(); i++) {
                if(all_selected.get(i).getPieces().size()>0) {
                    pieceID = all_selected.get(i).getPieces().get(all_selected.get(i).getPieces().size()-1).getId();
                }
                validroll = getValidRoll(all_selected.get(i).getId(), all_highest_moves.get(i).getId(), this.B.getGameLoop().getCurrentPlayer().getId());
                if(all_highest_moves.get(i).getPieces().size() == 1 && this.B.getGameLoop().getCurrentPlayer().getId() != all_highest_moves.get(i).getPieces().get(0).getId() && all_highest_moves.get(i).getId() != 26){
                    eatMove = true;
                } else {
                    eatMove = false;
                }

                this.B.playerMoveNoCheck(all_selected.get(i).getId(), all_highest_moves.get(i).getId(), pieceID);
                if(eatMove) {
                    this.B.getGameLoop().checkEaten(all_highest_moves.get(i).getId());
                }

                this.B.getDie().deleteNumber(validroll);
                if(deepnessconstr > 0) {
                    if(diceCopyEmpty(this.B.getDie().getCurRoll())){
                        this.B.getGameLoop().SwitchPlayer();
                        this.B.getDie().seeNextRoll();
                        bestmove = GetBestMove(deepnessconstr-1);
                        this.B.getGameLoop().SwitchPlayer();
                        this.B.getDie().goRollBack();
                        value_moves[i] = bestmove[2];
                    } else{
                        bestmove = GetBestMove(deepnessconstr-1);
                        value_moves[i] = bestmove[2];

                    }
                }else{
                    value_moves[i] = EvaluationFunc();

                }
                this.B.playerMoveNoCheck(all_highest_moves.get(i).getId(), all_selected.get(i).getId(), pieceID);
                if(eatMove){
                    this.B.getGameLoop().SwitchPlayer();
                    this.B.moveBackFromEatenSpaceID(all_highest_moves.get(i).getId(), this.B.getGameLoop().getCurrentPlayer().getId());
                    this.B.getGameLoop().getCurrentPlayer().revivePiece();
                    this.B.getGameLoop().SwitchPlayer();
                }
                this.B.getDie().addNumber(validroll);

            }
            int index = 99;
            if(this.B.getGameLoop().getCurrentPlayer().getId() == (player ? 1 : 0)){
                index = argmax(value_moves);
            } else{
                index = argmin(value_moves);

            }
            double[] returnlist = {all_selected.get(index).getId(), all_highest_moves.get(index).getId(), value_moves[index]};
            return returnlist;

        }
        else{
            return new double[3];
        }
    }
    public double[] GetBestMove(int deepnessconstr){
        ArrayList<Space> all_selected = GetAllSelectedSpaces();
        ArrayList<Space> all_highest_moves = GetHighestMoves(all_selected);
        double[] value_moves = new double[all_selected.size()];
        double[] bestmove;
        boolean eatMove = false;
        int pieceID =0;
        int validroll;
        if(all_highest_moves.size()>0) {
            for (int i = 0; i < all_selected.size(); i++) {
                if(all_selected.get(i).getPieces().size()>0) {
                    pieceID = all_selected.get(i).getPieces().get(all_selected.get(i).getPieces().size()-1).getId();
                }
                validroll = getValidRoll(all_selected.get(i).getId(), all_highest_moves.get(i).getId(), this.B.getGameLoop().getCurrentPlayer().getId());
                if(all_highest_moves.get(i).getPieces().size() == 1 && this.B.getGameLoop().getCurrentPlayer().getId() != all_highest_moves.get(i).getPieces().get(0).getId() && all_highest_moves.get(i).getId() != 26){
                    eatMove = true;
                } else {
                    eatMove = false;
                }
                this.B.playerMoveNoCheck(all_selected.get(i).getId(), all_highest_moves.get(i).getId(), pieceID);
                if(eatMove) {
                    this.B.getGameLoop().checkEaten(all_highest_moves.get(i).getId());
                }

                this.B.getDie().deleteNumber(validroll);
                if(deepnessconstr > 0 && !diceCopyEmpty(this.B.getDie().getCurRoll())) {
                    bestmove = GetBestMove(deepnessconstr-1);
                    value_moves[i] = bestmove[2];
                }else{
                    value_moves[i] = EvaluationFunc();

                }
                this.B.playerMoveNoCheck(all_highest_moves.get(i).getId(), all_selected.get(i).getId(), pieceID);
                if(eatMove){
                    this.B.getGameLoop().SwitchPlayer();
                    this.B.moveBackFromEatenSpaceID(all_highest_moves.get(i).getId(), this.B.getGameLoop().getCurrentPlayer().getId());
                    this.B.getGameLoop().getCurrentPlayer().revivePiece();
                    this.B.getGameLoop().SwitchPlayer();
                }
                this.B.getDie().addNumber(validroll);

            }
            int index = argmax(value_moves);
            double[] returnlist = {all_selected.get(index).getId(), all_highest_moves.get(index).getId(), value_moves[index]};
            return returnlist;

        }
        else{
            return new double[3];
        }
    }
    public int getValidRoll(int from, int to, int pieceID){
        if(this.B.getDie().getCurRoll().length == 1 ){
            return this.B.getDie().getCurRoll()[0];
        }
        else if(to==26 && pieceID == 0){
            for(Integer i: this.B.getDie().getCurRoll()){
                if(i >= to - from - 1){
                    return i;
                }
            }
            System.out.println("Shouldn't be here");
            return to - from - 1;
        } else if(to == 26 && pieceID == 1){
            for(Integer i: this.B.getDie().getCurRoll()){
                if(i <= to - from - 26){
                    return i;
                }
            }
            System.out.println("Shouldn't be here");
            return to-from-26;
        } else{
            return to - from;
        }
    }
    public boolean diceCopyEmpty(int[] dicecopy){
        for(Integer inte: dicecopy){
            if(inte!=0){
                return false;
            }
        }
        return true;
    }
    public void ExecuteNextMove2Cheat(int deepness, boolean player){
        double[] move = GetBestMoveCheat(deepness, player);
        B.forceHomeCheck();
        if(move[0]==0 && move[1]==0){
            for(Integer inter: this.B.getDie().getCurRoll()){
                this.B.getDie().removeUsedRoll(inter);
            }
        } else{
            B.playerMove((int) move[0], (int) move[1]);
        }
    }
    public void ExecuteNextMove2(int deepness){
        double[] move = GetBestMove(deepness);
        B.forceHomeCheck();
        if(move[0]==0 && move[1]==0){
            for(Integer inter: this.B.getDie().getCurRoll()){
                this.B.getDie().removeUsedRoll(inter);
            }
        } else{
            B.playerMove((int) move[0], (int) move[1]);
        }
    }
    public int[] diceCopy(int[] die){
        int[] returndice = new int[die.length];
        for(int i = 0; i<die.length; i++){
            returndice[i] = die[i];
        }
        return returndice;
    }

    public void ExecuteNextMove(){
        ArrayList<Space> all_selected = GetAllSelectedSpaces();
        ArrayList<Space> all_highest_moves = GetHighestMoves(all_selected);
        double[] value_moves = new double[all_selected.size()];
        int pieceID =0;
        if(all_highest_moves.size()>0) {
            for (int i = 0; i < all_selected.size(); i++) {
                if(all_selected.get(i).getPieces().size()>0) {
                    pieceID = all_selected.get(i).getPieces().get(0).getId();
                }
                this.B.playerMoveNoCheckBot(all_selected.get(i).getId(), all_highest_moves.get(i).getId(), pieceID);
                value_moves[i] = EvaluationFunc();
                this.B.playerMoveNoCheckBot(all_highest_moves.get(i).getId(), all_selected.get(i).getId(), pieceID);
            }
            int index = argmax(value_moves);
            B.forceHomeCheck();
            B.playerMove(all_selected.get(index).getId(), all_highest_moves.get(index).getId());

        }
        else{
            for(Integer inter: this.B.getDie().getCurRoll()){
                this.B.getDie().removeUsedRoll(inter);
            }
        }
    }

    public ArrayList<Space> GetAllSelectedSpaces(Board board){
        ArrayList returnSpaces = new ArrayList<Space>();
        if(board.getGameLoop().getCurrentPlayer().getId() == 1) {
            if(board.getSpaces()[25].getSize() > 0){
                returnSpaces.add(board.getSpaces()[25]);
                return returnSpaces;
            }
            for (Space space : board.getSpaces()) {
                if (space.getSize() != 0) {
                    if (space.getPieces().get(0).getId() == 1) {
                        if(board.getValidMoves(space).size()>0) {
                            returnSpaces.add(space);
                        }
                    }
                }
            }
            return returnSpaces;
        } else
        {
            if(board.getSpaces()[0].getSize() > 0){
                returnSpaces.add(board.getSpaces()[0]);
                return returnSpaces;
            }
            for (Space space : board.getSpaces()) {
                if (space.getSize() != 0) {
                    if (space.getPieces().get(0).getId() == 0) {
                        if(board.getValidMoves(space).size()>0) {
                            returnSpaces.add(space);
                        }
                    }
                }
            }
            return returnSpaces;
        }
    }

    public ArrayList<Space> GetAllSelectedSpaces(){
        ArrayList returnSpaces = new ArrayList<Space>();
        if(this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
            if(this.B.getSpaces()[25].getSize() > 0){
                returnSpaces.add(this.B.getSpaces()[25]);
                return returnSpaces;
            }
            for (Space space : this.B.getSpaces()) {
                if (space.getSize() != 0) {
                    if (space.getPieces().get(0).getId() == 1) {
                        if(this.B.getValidMoves(space).size()>0) {
                            returnSpaces.add(space);
                        }
                    }
                }
            }
            return returnSpaces;
        } else
        {
            if(this.B.getSpaces()[0].getSize() > 0){
                returnSpaces.add(this.B.getSpaces()[0]);
                return returnSpaces;
            }
            for (Space space : this.B.getSpaces()) {
                if (space.getSize() != 0) {
                    if (space.getPieces().get(0).getId() == 0) {
                        if(this.B.getValidMoves(space).size()>0) {
                            returnSpaces.add(space);
                        }
                    }
                }
            }
            return returnSpaces;
        }
    }

    public Space GetHighestSubSpace(Space selected, ArrayList<Space> moves){
        double[] valuemoves = new double[moves.size()];
        int pieceID = 0;
        for(int i = 0; i < moves.size(); i++){
            if(selected.getPieces().size()>0) {
                pieceID = selected.getPieces().get(0).getId();
            }
            this.B.playerMoveNoCheckBot(selected.getId(), moves.get(i).getId(), pieceID);
            valuemoves[i] = EvaluationFunc();
            this.B.playerMoveNoCheckBot(moves.get(i).getId(), selected.getId(), pieceID);
        }
        return moves.get(argmax(valuemoves));
    }

    // Number of pieces enemy pieces slain
    // Positive
    public double OtherPiecesSlain(){
        for(Space space : this.B.getSpaces()){
               if(space.getSize()==2 && (space.getPieces().get(0).getId() + space.getPieces().get(1).getId()) == 1 ){
                   return 1;
               }
        }
        return 0;
    }
    // Calculate the total number of points that the bot must move his pieces to bring them home
    // Negative
    public double pipCount(){
        if(this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
            double pip = 0;
            for (int i = 1; i <= 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 1) {
                        pip += Math.pow(i,2);
                    }
                }
            }
            if(this.B.getSpaces()[0].getSize() > 0 && this.B.getSpaces()[0].getPieces().get(0).getId() == 1){
                pip += this.B.getSpaces()[0].getSize()*24;
            }
            return - pip;
        } else {
            double pip = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 0) {
                        pip += Math.pow(25 - i,2);
                    }
                }
            }
            return - pip;
        }
    }
    // How many pieces are finished, assuming finished position is at 0.
    // Positive
    public double DoneScore(){
        if(this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
            double numberPieces = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 1) {
                        numberPieces++;
                    }
                }
            }
            return 15 - numberPieces;
        } else {
            double numberPieces = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 0) {
                        numberPieces++;
                    }
                }
            }
            return 15 - numberPieces;
        }
    }
    // How many pieces are in the last board
    // Positive
    public double DoneBoardScore(){
        if(this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
            double score = 0;
            for (int i = 1; i < 7; i++) {
                if (this.B.getSpaces()[i].getSize() > 0 && this.B.getSpaces()[i].getPieces().get(0).getId() == 1) {
                    score =+ this.B.getSpaces()[i].getSize();
                }
            }
            return score;
        } else {
            double score = 0;
            for (int i = 19; i < 25; i++) {
                if (this.B.getSpaces()[i].getSize() > 0 && this.B.getSpaces()[i].getPieces().get(0).getId() == 0) {
                    score =+ this.B.getSpaces()[i].getSize();
                }
            }
            return score;
        }
    }
    // How many pieces of your own board are alone
    // Negative
    public double piecesAlone(){
        if(this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
            double alone = 0;
            for (int i = 0; i < 25; i++) {
                if (this.B.getSpaces()[i].getSize() == 1 && this.B.getSpaces()[i].getPieces().get(0).getId() == 1) {
                    alone++;
                }
            }
            return -alone;
        } else {
            double alone = 0;
            for (int i = 0; i < 25; i++) {
                if (this.B.getSpaces()[i].getSize() == 1 && this.B.getSpaces()[i].getPieces().get(0).getId() == 0) {
                    alone++;
                }
            }
            return -alone;
        }
    }


    static int argmax(double[] weights) {
        if (weights == null)
            return -1;
        if (weights.length == 1)
            return 0;
        double max = weights[0];
        int maxindex = 0;
        for (int i = 1; i < weights.length; i++)
            if (weights[i] > max) {
                maxindex = i;
                max = weights[i];
            }
        return maxindex;
    }
    static double max(double[] weights) {
        if (weights == null)
            return -1;
        if (weights.length == 1)
            return 0;
        double max = weights[0];
        int maxindex = 0;
        for (int i = 1; i < weights.length; i++)
            if (weights[i] > max) {
                maxindex = i;
                max = weights[i];
            }
        return max;
    }
    static int argmin(double[] weights) {
        if (weights == null)
            return -1;
        if (weights.length == 1)
            return 0;
        double min = weights[0];
        int minindex = 0;
        for (int i = 1; i < weights.length; i++)
            if (weights[i] < min) {
                minindex = i;
                min = weights[i];
            }
        return minindex;
    }
    static double min(double[] weights) {
        if (weights == null)
            return -1;
        if (weights.length == 1)
            return 0;
        double min = weights[0];
        int minindex = 0;
        for (int i = 1; i < weights.length; i++)
            if (weights[i] < min) {
                minindex = i;
                min = weights[i];
            }
        return min;
    }
    static double avg(double[] weights){
        double x = 0;
        for(int i = 0; i<weights.length; i++){
            x = x + weights[i];
        }
        return x/weights.length;
    }
    static int[] arrayCopy(int[] arr){
        int[] returnarray= new int[arr.length];
        for(int i = 0; i<arr.length; i++){
            returnarray[i] = arr[i];
        }
        return returnarray;
    }
    public void setRandomWeights(){
        Random r = new Random();
        for(int i = 0; i<this.weightsarr.length; i++){
            this.weightsarr[i] = r.nextDouble();
        }
    }
    public double[] getWeightsarr(){
        return this.weightsarr;
    }


    public void setDifferentWeights(double[] arr){
        for(int i = 0; i<arr.length; i++){
            this.weightsarr[i] = arr[i];
        }
    }
    public boolean isInArray(int[] arr, int a){
        for (int element : arr) {
            if (element == a) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void executeTurn()  {
        if(this.getId() == 1){
            //this.ExecuteDeeperMove();
//            this.ExecuteDeeperMove2();
            this.weightsarr = new double[]{1.6701575815795195, 0.010922843688447176, 0.548225117255872, 0.518327607152693, 0.2, 2.0, 1.5000000000000002};
            this.ExecuteNextMove2(4);
        }
        else{
            this.ExecuteNextMove2(4);
        }
        B.getGameLoop().repaintBV();
//        pauseBot();
    }

    @Override
    public String getName() {
        return "BotA";
    }


}
