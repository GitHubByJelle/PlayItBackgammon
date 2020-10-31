package AI;
import World.*;

import java.util.*;


public class BotA
{
//    assume board is structured in list from 1-25
//    standard start position
    //TODO Wait for nextValidMoves to be implemented
    //TODO Use Evaluation function to select move out of nextValidMoves
    //TODO Use profiles in bot to make player 1 think 1 move ahead and player 2 2 or 3 moves ahead. Using winning as evolution for bot
    //TODO Optimize selection out of ValidMoves using GA

    //False is white and True is red
    public boolean profile;
    public double[] weightsarr;

    public static void main(String[] args) {
        Board b= new Board();
//        System.out.println(b);
        double[] weightsarr = {0.1,2,3,23,2};
        BotA bot = new BotA(true, weightsarr);
//        System.out.println(bot.pipCount(b));
        System.out.println(b.toString());
//        b.getValidMoves(b.getSpaces()[5]);
//        bot.getAllValidMoves(b);
        System.out.println("DICE");
        System.out.println(bot.GetHighestSubSpace(b.getSpaces()[6], b, b));
        bot.ExecuteNextMove(b, b);
    }

    public BotA(boolean profile, double[] weightsarr){
        this.profile = profile;
        this.weightsarr = weightsarr;
    }


    public double EvaluationFunc(Board B, Board prevB){
        return this.OtherPiecesSlain(B, prevB)*weightsarr[0] + this.pipCount(B)*weightsarr[1] + this.DoneScore(B)*weightsarr[2] + this.DoneBoardScore(B)*weightsarr[3] + this.piecesAlone(B)*weightsarr[4];
    }

//    public void ExecuteHighestMove (ArrayList<Space> selected, double[] values, ArrayList<Space> highest_moves, Board B){
//        int i = argmax(values);
//        B.playerMove(selected.get(i).getId(), highest_moves.get(i).getId());
//    }
    public ArrayList<Space> GetHighestMoves(Board B, Board prevB, ArrayList<Space> selected_spaces){
        ArrayList<Space> moves = new ArrayList<Space>();
        for(Space selected : selected_spaces){
            moves.add(GetHighestSubSpace(selected, B, prevB));
        }
        return moves;
    }
    public void ExecuteNextMove(Board B, Board prevB){
        ArrayList<Space> all_selected = GetAllSelectedSpaces(B);
        ArrayList<Space> all_highest_moves = GetHighestMoves(B, prevB, all_selected);
        double[] value_moves = new double[all_selected.size()];
        for(int i = 0; i<all_selected.size(); i++){
            B.playerMoveNoCheck(all_selected.get(i).getId(), all_highest_moves.get(i).getId());
            value_moves[i] = EvaluationFunc(B,prevB);
            B.playerMoveNoCheck(all_highest_moves.get(i).getId(), all_selected.get(i).getId());
        }
        int index = argmax(value_moves);
        System.out.println("MOVES CHOSEN");
        System.out.println(value_moves[index]);
        System.out.println(all_selected.get(index).getId());
        System.out.println(all_highest_moves.get(index).getId());
        B.playerMove(all_selected.get(index).getId(), all_highest_moves.get(index).getId());
    }
    public ArrayList<Space> GetAllSelectedSpaces(Board B){
        ArrayList returnSpaces = new ArrayList<Space>();
        if(profile) {
            for (Space space : B.getSpaces()) {
                if (space.getSize() != 0) {
                    if (space.getPieces().get(0).getId() == 1) {
                        returnSpaces.add(space);
                    }
                }
            }
            return returnSpaces;
        } else
        {
            for (Space space : B.getSpaces()) {
                if (space.getSize() != 0) {
                    if (space.getPieces().get(0).getId() == 0) {
                        returnSpaces.add(space);
                    }
                }
            }
            return returnSpaces;
        }
    }
    public Space GetHighestSubSpace(Space selected, Board B, Board prevB){
        ArrayList<Space> moves = B.getValidMoves(selected);
        double[] valuemoves = new double[moves.size()];
        for(int i = 0; i < moves.size(); i++){
            B.playerMoveNoCheck(selected.getId(), moves.get(i).getId());
            valuemoves[i] = EvaluationFunc(B,prevB);
            B.playerMoveNoCheck(moves.get(i).getId(), selected.getId());
        }
        System.out.println(moves.get(argmax(valuemoves)).toString());
        return moves.get(argmax(valuemoves));
    }

    // Number of pieces enemy pieces slain
    // Positive
    public double OtherPiecesSlain(Board B, Board prevB){
        return numberOfEnemyPieces(prevB) - numberOfEnemyPieces(B);
    }
    // Count the number of enemy pieces
    public double numberOfEnemyPieces(Board B){
        if(this.profile) {
            double numberPieces = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 0) {
                        numberPieces++;
                    }
                }
            }
            return numberPieces;
        }else{
            double numberPieces = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 1) {
                        numberPieces++;
                    }
                }
            }
            return numberPieces;
        }
    }

    // Calculate the total number of points that the bot must move his pieces to bring them home
    // Negative
    public double pipCount(Board B){
        if(this.profile) {
            double pip = 0;
            for (int i = 1; i < 25; i++) {
                for (Piece piece : B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 1) {
                        pip += i;
                    }
                }
            }
            if(B.getSpaces()[0].getSize() > 0 && B.getSpaces()[0].getPieces().get(0).getId() == 1){
                pip += B.getSpaces()[0].getSize()*24;
            }
            return -pip;
        } else {
            double pip = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : B.getSpaces()[i].getPieces()) {
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
    public double DoneScore(Board B){
        if(this.profile) {
            double numberPieces = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 1) {
                        numberPieces++;
                    }
                }
            }
            return 15 - numberPieces;
        } else {
            double numberPieces = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : B.getSpaces()[i].getPieces()) {
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
    public double DoneBoardScore(Board B){
        if(this.profile) {
            double score = 0;
            for (int i = 1; i < 7; i++) {
                if (B.getSpaces()[i].getSize() > 0 && B.getSpaces()[i].getPieces().get(0).getId() == 1) {
                    score = +B.getSpaces()[i].getSize();
                }
            }
            return score;
        } else {
            double score = 0;
            for (int i = 19; i < 25; i++) {
                if (B.getSpaces()[i].getSize() > 0 && B.getSpaces()[i].getPieces().get(0).getId() == 0) {
                    score = +B.getSpaces()[i].getSize();
                }
            }
            return score;
        }
    }
    // How many pieces of your own board are alone
    // Negative
    public double piecesAlone(Board B){
        if(this.profile) {
            double alone = 0;
            for (int i = 0; i < 25; i++) {
                if (B.getSpaces()[i].getSize() == 1 && B.getSpaces()[i].getPieces().get(0).getId() == 1) {
                    alone++;
                }
            }
            return -alone;
        } else {
            double alone = 0;
            for (int i = 0; i < 25; i++) {
                if (B.getSpaces()[i].getSize() == 1 && B.getSpaces()[i].getPieces().get(0).getId() == 0) {
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
