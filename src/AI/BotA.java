package AI;
import World.*;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.*;




public class BotA {

//    assume board is structured in list from 1-25
//    standard start position
    //TODO Make gameloop for Bot to train/game in
    //TODO wait for Double moves
    //TODO Use profiles in bot to make player 1 think 1 move ahead and player 2 2 or 3 moves ahead. Using winning as evolution for bot
    //TODO Optimize selection out of ValidMoves using GA

    //False is white and True is red
    public boolean profile;
    public double[] weightsarr;
    public Board B;

    public static void main(String[] args) {
        Board b= new Board();
        b.setPlayers("Human", "Human");
        b.createBotLoop();
//        System.out.println(b);
        double[] weightsarr = {1,1,1,1,1};
        BotA bot = new BotA(false, weightsarr, b);
//        System.out.println(bot.pipCount(b));
        System.out.println(b.toString());
//        b.getValidMoves(b.getSpaces()[5]);
//        bot.getAllValidMoves(b);
//        System.out.println(bot.GetHighestSubSpace(b.getSpaces()[6], b, b));
        System.out.println(bot.SingleGameLoop());
        System.out.println(b.toString());

    }

    public BotA(boolean profile, double[] weightsarr, Board b){
        this.profile = profile;
        this.weightsarr = weightsarr;
        this.B = b;
    }

    public double EvaluationFunc(){
        return this.OtherPiecesSlain()*weightsarr[0] + this.pipCount()*weightsarr[1] + this.DoneScore()*weightsarr[2] + this.DoneBoardScore()*weightsarr[3] + this.piecesAlone()*weightsarr[4];
    }

//    public void ExecuteHighestMove (ArrayList<Space> selected, double[] values, ArrayList<Space> highest_moves, Board B){
//        int i = argmax(values);
//        B.playerMove(selected.get(i).getId(), highest_moves.get(i).getId());
//    }
    public void PlayerLoop() {
        while (this.B.getDie().getCurRoll().length > 0) {
            this.B.checkAllPiecesHome();
            this.ExecuteNextMove();
            System.out.println(this.B.toString());
        }
    }
    //returns 0 if W lost, 1 if W won
    public int SingleGameLoop(){
        while(!this.B.checkWinCondition()){
            this.B.getDie().printCurRoll();
            System.out.println();
            this.PlayerLoop();
            B.getGameLoop().process();

            //this.B.getDie().getNextRoll();
            this.profile =!profile;

        }
        if(this.B.getGameLoop().getCurrentPlayer().getId() == 0){
            return 0;
        }
        return 1;
    }

    public ArrayList<Space> GetHighestMoves(ArrayList<Space> selected_spaces){
        ArrayList<Space> moves = new ArrayList<Space>();
        System.out.println(selected_spaces.size());
        for(int i = 0; i<selected_spaces.size(); i++){
            ArrayList<Space> submoves = this.B.getValidMoves(selected_spaces.get(i));
            System.out.println(submoves.size());
            if(submoves.size()>0) {
                moves.add(GetHighestSubSpace(selected_spaces.get(i), submoves));
            }else {
                selected_spaces.remove(i);
            }

        }
        return moves;
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
//                System.out.println("Selected movesblablabl");
//                System.out.println(all_selected.get(i).getId());
//                System.out.println(all_highest_moves.get(i).getId());
//                all_highest_moves.get(i).getId();
                this.B.playerMoveNoCheck(all_selected.get(i).getId(), all_highest_moves.get(i).getId(), pieceID);
                value_moves[i] = EvaluationFunc();
                this.B.playerMoveNoCheck(all_highest_moves.get(i).getId(), all_selected.get(i).getId(), pieceID);
            }
            int index = argmax(value_moves);
            System.out.println("MOVES CHOSEN");
            System.out.println(value_moves[index]);
            System.out.println(all_selected.get(index).getId());
            System.out.println(all_highest_moves.get(index).getId());
            B.BotMove(all_selected.get(index).getId(), all_highest_moves.get(index).getId());
        }
        else{
            for(Integer inter: this.B.getDie().getCurRoll()){
                this.B.getDie().removeUsedRoll(inter);
            }
        }
    }
    public ArrayList<Space> GetAllSelectedSpaces(){
        ArrayList returnSpaces = new ArrayList<Space>();
        if(profile) {
            if(this.B.getSpaces()[25].getSize() > 0){
                returnSpaces.add(this.B.getSpaces()[25]);
                return returnSpaces;
            }
            for (Space space : this.B.getSpaces()) {
                if (space.getSize() != 0) {
                    if (space.getPieces().get(0).getId() == 1) {
                        System.out.println(this.B.getValidMoves(space).size());
                        System.out.println(space.getId());
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
                        System.out.println(this.B.getValidMoves(space).size());
                        System.out.println(space.getId());
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
//        ArrayList<Space> moves = this.B.getValidMoves(selected);
        double[] valuemoves = new double[moves.size()];
        int pieceID = 0;
        for(int i = 0; i < moves.size(); i++){
            if(selected.getPieces().size()>0) {
                pieceID = selected.getPieces().get(0).getId();
            }
            this.B.playerMoveNoCheck(selected.getId(), moves.get(i).getId(), pieceID);
            valuemoves[i] = EvaluationFunc();
            this.B.playerMoveNoCheck(moves.get(i).getId(), selected.getId(), pieceID);
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
        if(this.profile) {
            double pip = 0;
            for (int i = 1; i <= 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 1) {
                        pip += i;
                    }
                }
            }
            if(this.B.getSpaces()[0].getSize() > 0 && this.B.getSpaces()[0].getPieces().get(0).getId() == 1){
                pip += this.B.getSpaces()[0].getSize()*24;
            }
            return -pip;
        } else {
            double pip = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 0) {
                        pip += 25 - i;
                    }
                }
            }
            return -pip;
        }
    }
    // How many pieces are finished, assuming finished position is at 0.
    // Positive
    public double DoneScore(){
        if(this.profile) {
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
        if(this.profile) {
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
        if(this.profile) {
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

}
