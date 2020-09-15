package AI;
import World.*;

public class Bot {
//    assume board is structured in list from 1-25
//    standard start postition
    public static void main(String[] args) {
        Board b= new Board();

        System.out.println(b);
        System.out.println(pipCount(b));
    }
    // Number of pieces enemy pieces slain
    //Positive
    public static double OtherPiecesSlain(Board B, Board prevB){
        return numberOfEnemyPieces(prevB) - numberOfEnemyPieces(B);
    }

    // Count the number of enemy pieces
    public static double numberOfEnemyPieces(Board B){
        double numberPieces = 0;
        for(int i = 0; i<25; i++){
            for (Piece piece : B.getSpaces()[i].getPieces()){
                if (piece.getId() == 1){
                    numberPieces++;
                }
            }
        }
        return numberPieces;
    }

    // Calculate the total number of points that the bot must move his pieces to bring them home
    // Negative
    public static double pipCount(Board B){
        double pip = 0;
        for(int i = 0; i<25; i++){
            for (Piece piece : B.getSpaces()[i].getPieces()){
                if (piece.getId() == 0){
                    pip += 25-i;
                }
            }
        }
        return -pip;
    }
    // How many pieces are finished, assuming finished position is at 0.
    // Positive
    public static double DoneScore(Board B){
        return B.getSpaces()[0].getSize();
    }
    // How many pieces are in the last board
    // Positive
    public static double DoneBoardScore(Board B){
        double score = 0;
        for(int i = 1; i<7; i++){
            if(B.getSpaces()[i].getSize() >0 && B.getSpaces()[i].getPieces().get(0).getId() == 1){
                score =+ B.getSpaces()[i].getSize();
            }
        }
        return score;
    }
    // How many pieces of your own board are alone
    // Negative
    public static double piecesAlone(Board B){
        double alone = 0;
        for(int i = 0; i<25; i++){
            if(B.getSpaces()[i].getSize() == 1  && B.getSpaces()[i].getPieces().get(0).getId() == 1){
                alone++;
            }
        }
        return -alone;
    }

}
