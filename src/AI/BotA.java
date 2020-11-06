package AI;
import World.*;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Timer;


public class BotA extends Player.Bot{

//    assume board is structured in list from 1-25
//    standard start position
    //TODO Make gameloop for Bot to train/game in
    //TODO wait for Double moves
    //TODO Use profiles in bot to make player 1 think 1 move ahead and player 2 2 or 3 moves ahead. Using winning as evolution for bot
    //TODO Optimize selection out of ValidMoves using GA

    //0 is white and 1 is red
    public double[] weightsarr={1,1,1,1,1};
    public BotA(int id) {
        super(id);
    }
    public BotA(int id, double[] weightsarr) {
        super(id);
        this.weightsarr = weightsarr;
    }
    public double EvaluationFunc(){
        return this.OtherPiecesSlain()*weightsarr[0] + this.pipCount()*weightsarr[1] + this.DoneScore()*weightsarr[2] + this.DoneBoardScore()*weightsarr[3] + this.piecesAlone()*weightsarr[4];
    }

    public void PlayerLoop() {
        while (this.B.getDie().getCurRoll().length > 0) {
            this.B.checkAllPiecesHome();
            this.ExecuteNextMove();
        }
    }
    public void TwoDeepLoop(){
        while (this.B.getDie().getCurRoll().length > 0) {
            this.B.checkAllPiecesHome();
            this.ExecuteDeeperMove();
        }
    }

    //returns 0 if W lost, 1 if W won
    public int SingleGameLoop(){
        while(!this.B.checkWinCondition()){
            if(this.B.getGameLoop().getCurrentPlayer().getId() == 1){
                this.TwoDeepLoop();
            }else{
                this.PlayerLoop();

            } }
        if(this.B.getGameLoop().getCurrentPlayer().getId() == 0){
            return 0;
        }
        return 1;
    }


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
    public void ExecuteDeeperMove(){
        ArrayList<Space> all_selected = GetAllSelectedSpaces();
        ArrayList<Space> all_selected_2d;

        ArrayList<Space> all_highest_moves = GetHighestMoves(all_selected);
        ArrayList<Space> all_highest_moves_2d;

        double[] value_moves = new double[all_selected.size()];
        double[] value_moves_2d;
        int pieceID =0;
        double average = 0;
        double[] maxarr = new double[all_highest_moves.size()];

        if(all_highest_moves.size()>0) {
            for (int i = 0; i < all_selected.size(); i++) {
                if(all_selected.get(i).getPieces().size()>0) {
                    pieceID = all_selected.get(i).getPieces().get(0).getId();
                }
                this.B.playerMoveNoCheck(all_selected.get(i).getId(), all_highest_moves.get(i).getId(), pieceID);
                all_selected_2d = GetAllSelectedSpaces();
                all_highest_moves_2d = GetHighestMoves(all_selected_2d);
                value_moves_2d = new double[all_selected_2d.size()];
                this.B.getGameLoop().SwitchPlayer();
                if(all_highest_moves_2d.size()>0) {
                    for (int j = 0; j < all_selected_2d.size(); j++) {
                        if(all_selected_2d.get(j).getPieces().size()>0) {
                            pieceID = all_selected_2d.get(j).getPieces().get(0).getId();
                        }
                        this.B.playerMoveNoCheck(all_selected_2d.get(j).getId(), all_highest_moves_2d.get(j).getId(), pieceID);
                        value_moves_2d[j] = EvaluationFunc();
                        this.B.playerMoveNoCheck(all_highest_moves_2d.get(j).getId(), all_selected_2d.get(j).getId(), pieceID);
                    }
                    maxarr[i] = max(value_moves_2d);
                }
                else{
                    maxarr[i] = 0;
                }
                this.B.playerMoveNoCheck(all_highest_moves.get(i).getId(), all_selected.get(i).getId(), pieceID);
                this.B.getGameLoop().SwitchPlayer();
            }
            int index = argmin(maxarr);
            B.forceHomeCheck();
            B.playerMove(all_selected.get(index).getId(), all_highest_moves.get(index).getId());
            //System.out.println(this.getId());
        }
        else{
            for(Integer inter: this.B.getDie().getCurRoll()){
                this.B.getDie().removeUsedRoll(inter);
            }
        }
    }

    public void ExecuteDeeperMove2(){
        // Making the first move of Layer 1
        int pieceID = 0;
        ArrayList<int[]> MovesMade = new ArrayList<>();
        ArrayList<Space> all_selected = GetAllSelectedSpaces(); // All the spaces that can make a valid move
        ArrayList<Board> boardsLayerOneF = new ArrayList<>();
        for (int i = 0; i < all_selected.size(); i++){
            ArrayList<Space> valid_moves = this.B.getValidMoves(all_selected.get(i));
            for (int j = 0; j < valid_moves.size(); j++) {
//                System.out.print(space.getId());
//                System.out.print(" -> ");
//                System.out.print(move.getId());
//                System.out.println();
                int[] move = {all_selected.get(i).getId(),valid_moves.get(j).getId()};
                if (!MovesMade.contains(move)){
                    MovesMade.add(move);
                    if(all_selected.get(i).getPieces().size()>0) {
                        pieceID = all_selected.get(i).getPieces().get(0).getId();
                    }
                    this.B.playerMoveNoCheck(move[0], move[1], pieceID);
                    boardsLayerOneF.add(this.B.clone()); // TODO TRY TO COPY
                    boardsLayerOneF.get(boardsLayerOneF.size()-1).getDie().removeUsedRoll(move[1]-move[0]);
                    this.B.playerMoveNoCheck(move[1], move[0], pieceID);
                }
            }
        }

        // Make second move of layer one
        ArrayList<Board> boardsLayerOneS = new ArrayList<>();
        for (Board board : boardsLayerOneF){
            pieceID = 0;
            MovesMade.clear();
            all_selected = GetAllSelectedSpaces(board); // All the spaces that can make a valid move
            for (int i = 0; i < all_selected.size(); i++){
                ArrayList<Space> valid_moves = board.getValidMoves(all_selected.get(i));
                for (int j = 0; j < valid_moves.size(); j++) {
//                System.out.print(space.getId());
//                System.out.print(" -> ");
//                System.out.print(move.getId());
//                System.out.println();
                    int[] move = {all_selected.get(i).getId(),valid_moves.get(j).getId()};
                    if (!MovesMade.contains(move)){
                        MovesMade.add(move);
                        if(all_selected.get(i).getPieces().size()>0) {
                            pieceID = all_selected.get(i).getPieces().get(0).getId();
                        }
                        board.playerMoveNoCheck(move[0], move[1], pieceID);
                        boardsLayerOneS.add(board.clone()); // TODO TRY TO COPY ?????
                        boardsLayerOneS.get(boardsLayerOneS.size()-1).getDie().removeUsedRoll(move[1]-move[0]);
                        board.playerMoveNoCheck(move[1], move[0], pieceID);
                    }
                }
            }
        }

        // For layer 2, to the exact same for every board in boardsLayerOneS as we did to this.B.
        // This will give a lot of boards. Determine the best possible board for the bot.
    }

    public void ExecuteDeeperMove3(){
        ArrayList<Board> boardsLayerOne = GetAllPossibleBoards(this.B);

        ArrayList<Board> boardsLayerTwo = new ArrayList<>();
        for (Board board: boardsLayerOne){
            board.getGameLoop().SwitchPlayer();
            for (Board layertwoboard : GetAllPossibleBoards(board)){
                boardsLayerTwo.add(layertwoboard);
            }
        }

        // Get best board for player. Minimax? -> And execute best move.
    }

    public ArrayList<Board> GetAllPossibleBoards(Board board){
        ArrayList<Board> boards = new ArrayList<>();
        if (board.getDie().getCurRoll().length == 2){
            ArrayList<Board> singlemoveboards = GetAllPossibleBoards(board);
            for (Board singlemoveboard : singlemoveboards){
                //boards.add(GetAllPossibleBoards(singlemoveboard)); // TODO ???? // Lines below a solution?
                for (Board doublemoveboard : GetAllPossibleBoards(singlemoveboard)){
                    boards.add(doublemoveboard);
                }
            }
        }
        else if (board.getDie().getCurRoll().length == 1){
            int pieceID = 0;
            ArrayList<int[]> MovesMade = new ArrayList<>();
            ArrayList<Space> all_selected = GetAllSelectedSpaces(board); // All the spaces that can make a valid move
            for (int i = 0; i < all_selected.size(); i++){
                ArrayList<Space> valid_moves = board.getValidMoves(all_selected.get(i));
                for (int j = 0; j < valid_moves.size(); j++) {
//                System.out.print(space.getId());
//                System.out.print(" -> ");
//                System.out.print(move.getId());
//                System.out.println();
                    int[] move = {all_selected.get(i).getId(),valid_moves.get(j).getId()};
                    if (!MovesMade.contains(move)){
                        MovesMade.add(move);
                        if(all_selected.get(i).getPieces().size()>0) {
                            pieceID = all_selected.get(i).getPieces().get(0).getId();
                        }
                        board.playerMoveNoCheck(move[0], move[1], pieceID);
                        boards.add(board.clone()); // TODO TRY TO COPY ?????
                        boards.get(boards.size()-1).getDie().removeUsedRoll(move[1]-move[0]);
                        board.playerMoveNoCheck(move[1], move[0], pieceID);
                    }
                }
            }
            return boards;
        }
        else {
            return boards;
        }
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
                this.B.playerMoveNoCheck(all_selected.get(i).getId(), all_highest_moves.get(i).getId(), pieceID);
                value_moves[i] = EvaluationFunc();
                this.B.playerMoveNoCheck(all_highest_moves.get(i).getId(), all_selected.get(i).getId(), pieceID);
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
        if(this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
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
    @Override
    public void executeTurn()  {
        this.B.checkAllPiecesHome();
        if(this.getId() == 1){
            //this.ExecuteDeeperMove();
            this.ExecuteDeeperMove2();
            this.ExecuteNextMove();
        }
        else{
            this.ExecuteNextMove();
        }
        B.getGameLoop().repaintBV();
//        pauseBot();
    }

    @Override
    public String getName() {
        return "BotA";
    }


}
