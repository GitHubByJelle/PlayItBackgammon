package AI.AlphaBeta;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import AI.SimpleBot;
import World.*;

public class AlphaBetaBot extends Player.Bot {
    public double[] evaluator = {0.27492492486083797, 0.7603447308733254, 0.6324866448907414, 0.18347935996872392, 0.3290528252791358};
    private int[][] DIES_COMBINATION = new int[21][2];
    int[] currentDie;
    private enum Node {
        MAX("MAX"), MIN("MIN"), CHANCE("CHANCE");

        private String name;

        Node(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static final Node[] NODES = new Node[]{
            Node.MAX, Node.CHANCE, Node.MIN, Node.CHANCE
    };

    private static final int DEFAULT_DEPTH = 1;

    private double[] moveQuality;
    public  Bot opponent;
    private int initialDepth = -1;
    public StringBuilder treeStringRepresentationBuilder = new StringBuilder("");
    private boolean logExpectiminimaxTree = false;

    public AlphaBetaBot(int id) {
        super(id);
//        System.out.println("Test tostring");
//        System.out.println(this.B);
//        System.out.println("-------------------------------------------------");

        opponent = new SimpleBot(1);


    }


    @Override
    public String getName() {
        return "AlphBetaBot";
    }

    @Override
    public void executeTurn() {
//        B.getGameLoop().repaintBV();
//        alpha_beta_pruning_result();
//        pauseBot();
//        B.getGameLoop().repaintBV();
    }

    private double expectiminimax(int depth, int currentNodeIndex) {
        double result = 0.;
        List<Move> moves;

        if (initialDepth == -1) initialDepth = depth;


        switch (NODES[currentNodeIndex]) {
            case MAX:
                System.out.println("entering max zone");
                generateMoves2();

                moves = this.generateMoves2();;

                if (!moves.isEmpty()) {
                    double[] moveQuality = new double[moves.size()];

                    for (int i = 0; i < moves.size(); i++) {
                        System.out.println("Loop through max moves");
                        makeMove(moves.get(i));
                        moveQuality[i] = expectiminimax(depth - 1, (currentNodeIndex + 1) % 4);
                        undoMove(moves.get(i));

                    }

                    if (depth == initialDepth) {
                        this.moveQuality = new double[moveQuality.length];
                        System.arraycopy(moveQuality, 0, this.moveQuality, 0, moveQuality.length);
                    }
                    result = max(moveQuality);
                } else {
                    if (depth == initialDepth) moveQuality = new double[0];
                    result = -0.9;
                }
                break;
            case MIN:
                System.out.println("Entering min zone");
                moves = opponent.generateMoves2();

                if (!moves.isEmpty()) {
                    double[] moveQuality = new double[moves.size()];

                    for (int i = 0; i < moves.size(); i++) {
                        System.out.println("Loop through min moves");
                        opponent.makeMove(moves.get(i));
                        moveQuality[i] = expectiminimax(depth - 1, (currentNodeIndex + 1) % 4);
                        opponent.undoMove(moves.get(i));

                    }

                    result = min(moveQuality);
                } else {
                    result = 0.9;
                }
                break;
            case CHANCE:
                System.out.println("Entering chance zone, with depth: " + depth +" and node: " + NODES[currentNodeIndex].toString());
                // TODO What to do here?
                if (depth == 0) { // at the root, get the heuristic value
                    result = EvaluationFunction();
                    if(NODES[currentNodeIndex - 1] != Node.MAX){
                        result = -result;
                    }
                } else {
                    int[][] dice = DIES_COMBINATION; // For all dies combination,calculate the heuristic value with weight for that die combination to happen, get the state that is most likely to happen,
                    List<Double> values = new ArrayList<>();
                    for (int i = 0; i < dice.length; i++) {
                        if(NODES[(currentNodeIndex + 1) % 4] == Node.MAX) {
                            int[] currentDie = getCurrentDie();
                            System.out.println("Current die: " + Arrays.toString(currentDie));
                            this.B.getDie().setCurRoll(currentDie);
                        }else {
                            currentDie = opponent.getDie(i);
                            opponent.B.getDie().setCurRoll(currentDie);
                        }
                        values.add(expectiminimax(depth - 1, (currentNodeIndex + 1) % 4));
                        if (NODES[(currentNodeIndex + 1) % 4] == Node.MAX) {
                            this.B.getDie().setCurRoll(currentDie);
                        } else {
                            opponent.B.getDie().setCurRoll(currentDie);
                        }
                    }
                    result = weightedAverage(values);
                    System.out.println("Weighted average: " + result);
                }
                break;
        }


        return result;
    }
    public int[] getCurrentDie(){
        return this.currentDie;

    }
    private static double min(double... values) {
        double min = Double.MAX_VALUE;
        for (double value : values) {
            if (value < min)
                min = value;
        }
        return min;
    }
    public int[] getRandomDie(){
        Random random = new Random();
        int r = random.nextInt(20);
        return DIES_COMBINATION[r];
    }
    private static double max(double... values) {
        double max = -Double.MAX_VALUE;
        for (double value : values) {
            if (value > max)
                max = value;
        }
        return max;
    }

    private double weightedAverage(List<Double> values) {
        double weightedSum = 0.;
        double coefficientSum = 0.;
        int i = 0;
        for (Double value : values) {
            double coefficient = (double) (1 / 21);
            weightedSum += value * coefficient;
            coefficientSum += coefficient;
        }
        return weightedSum / coefficientSum;
    }
    public ArrayList<Move> generateMoves2() {
        ArrayList<Move> possible_moves = new ArrayList<>();
        Space[] allSpaces = this.B.getSpaces();
        for (Space space : allSpaces) {
            if (space.getSize() > 0) {
                if (space.getPieces().get(0).getId() == this.id) {
                    ArrayList<Space> validMoves = this.B.getValidMoves(space);
                    if (validMoves.size() > 0) {
                        for (Space v : validMoves) {
                            Move move = new Move(space.getId(), v.getId(), this.id);
                            possible_moves.add(move);
                        }
                    }
                }
            }
        }
        System.out.println(possible_moves);
        return possible_moves;
    }





    // evaluation function to evaluate the whole board status
    public double EvaluationFunction() {
        return (this.OtherPiecesSlain() * evaluator[0] + this.pipCount() * evaluator[1]
                + this.DoneScore() * evaluator[2] + this.DoneBoardScore() * evaluator[3]
                + this.piecesAlone() * evaluator[4]);
    }

    public double OtherPiecesSlain() {
        for (Space space : this.B.getSpaces()) {
            if (space.getSize() == 2 && (space.getPieces().get(0).getId() + space.getPieces().get(1).getId()) == 1) {
                return 1;
            }
        }
        return 0;
    }

    public double pipCount() {
        if (this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
            double pip = 0;
            for (int i = 1; i <= 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 1) {
                        pip += Math.pow(i, 2);
                    }
                }
            }
            if (this.B.getSpaces()[0].getSize() > 0 && this.B.getSpaces()[0].getPieces().get(0).getId() == 1) {
                pip += this.B.getSpaces()[0].getSize() * 24;
            }
            return -pip;
        } else {
            double pip = 0;
            for (int i = 0; i < 25; i++) {
                for (Piece piece : this.B.getSpaces()[i].getPieces()) {
                    if (piece.getId() == 0) {
                        pip += Math.pow(25 - i, 2);
                    }
                }
            }
            return -pip;
        }
    }

    public double DoneScore() {
        if (this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
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

    public double DoneBoardScore() {
        if (this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
            double score = 0;
            for (int i = 1; i < 7; i++) {
                if (this.B.getSpaces()[i].getSize() > 0 && this.B.getSpaces()[i].getPieces().get(0).getId() == 1) {
                    score = +this.B.getSpaces()[i].getSize();
                }
            }
            return score;
        } else {
            double score = 0;
            for (int i = 19; i < 25; i++) {
                if (this.B.getSpaces()[i].getSize() > 0 && this.B.getSpaces()[i].getPieces().get(0).getId() == 0) {
                    score = +this.B.getSpaces()[i].getSize();
                }
            }
            return score;
        }
    }

    public double piecesAlone() {
        if (this.B.getGameLoop().getCurrentPlayer().getId() == 1) {
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
    public Move getBestMove() {
        initialDepth = -1;
        Instant start = Instant.now();

        expectiminimax(DEFAULT_DEPTH, 0);
        Instant finish = Instant.now();

        initialDepth = -1;
        ArrayList<Move> moves = generateMoves2();
        System.out.println(Arrays.toString(moveQuality));
        int maxQualityMoveIndex = -1;
        double maxMoveQuality = -Double.MAX_VALUE;
        for (int i = 0; i < moveQuality.length; i++) {
            if (moveQuality[i] > maxMoveQuality) {
                maxMoveQuality = moveQuality[i];
                maxQualityMoveIndex = i;
            }
        }

        return maxQualityMoveIndex != -1 ? moves.get(maxQualityMoveIndex) : null;
    }
}
