package AI;
import World.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Bot {
//    assume board is structured in list from 1-25
//    standard start position
    //False is white and True is red
    //TODO Wait for nextValidMoves to be implemented
    //TODO Use Evaluation function to select move out of nextValidMoves
    //TODO Use profiles in bot to make player 1 think 1 move ahead and player 2 2 or 3 moves ahead. Using winning as evolution for bot
    //TODO Optimize selection out of ValidMoves using GA
    public boolean profile;
    public static void main(String[] args) {
        Board b= new Board();
//        System.out.println(b);
//        double[] weightsarr = {0.1,2,3,23,2};
        Bot bot = new Bot(true);
//        System.out.println(bot.pipCount(b));
        System.out.println(b.toString());
        b.getValidMoves(b.getSpaces()[5]);
//        bot.getAllValidMoves(b);
        System.out.println("DICE");
        b.getDice().printCurRoll();
    //        System.out.println(bot.getAllValidMoves(b));
    }
    public Bot(boolean profile){
        this.profile = profile;
    }
    public double EvaluationFunc(Board B, Board prevB, double[] weightsarr){
        return this.OtherPiecesSlain(B, prevB)*weightsarr[0] + this.pipCount(B)*weightsarr[1] + this.DoneScore(B)*weightsarr[2] + this.DoneBoardScore(B)*weightsarr[3] + this.piecesAlone(B)*weightsarr[4];
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

}
