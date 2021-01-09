package World;

import AI.AlphaBeta.Move;

import java.util.ArrayList;
import java.util.Random;

public abstract class Player {

    public int id;//matches piece id
    public int piecesInPlay;
    public int piecesOutOfPlay;
    public int piecesSlain;//or eaten


    Player(int id) {
        this.id = id;
        piecesInPlay = 15;
        piecesOutOfPlay = 0;
        piecesSlain = 0;
    }

    public int getPiecesInPlay() {
        return piecesInPlay;
    }

    public void resetPlayer() {
        piecesInPlay = 15;
        piecesOutOfPlay = 0;
        piecesSlain = 0;
    }

    public int getPiecesOutOfPlay() {
        return piecesOutOfPlay;
    }

    public int getPiecesSlain() {
        return piecesSlain;
    }

    public int getId() {
        return id;
    }

    public void pieceSlain() {
        --piecesInPlay;
        ++piecesSlain;
    }

    public void revivePiece() {
        --piecesSlain;
        ++piecesInPlay;
    }

    public void pieceOut() {
        --piecesInPlay;
        ++piecesOutOfPlay;
    }

    public abstract void executeTurn();

    public abstract void setBoard(Board B);

    public abstract String getName();

    public String toString() {
        return "Player " + id + " " + getName();
    }

    ;

    public void printSelectedMove(int from, int to) {
        System.out.println("SELECTED MOVE FROM: " + from + " TO " + to);
    }



    public static class Human extends Player {

        @Override
        public void executeTurn() {
        }

        @Override
        public void setBoard(Board B) {
        }

        @Override
        public String getName() {
            return "Human";
        }


        public Human(int id) {
            super(id);
        }
    }

    public static class Bot extends Player {
        //things common to all bots
        public Board B;
        public boolean pausing = true;
         int[][] DIES_COMBINATION = new int[21][2];

        public Bot(int id) {
            super(id);
            generateAllDieCombination();
        }

        @Override
        public void executeTurn() {
            // bot.executeTurn();
            B.getGameLoop().repaintBV();
            pauseBot();
        }

        public void pauseBot() {
            if (pausing) {
                try {
                    Thread.sleep((100));
                } catch (InterruptedException e) {
                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                }
            }

        }

        public void requestPassTurn() {
            B.getGameLoop().changeTurn();
            //System.out.println("PASS TURN");
        }

        @Override
        public void setBoard(Board B) {
            this.B = B;
        }

        @Override
        public String getName() {
            return "";
        }


        //bot "looking" at the board
        protected ArrayList<Space[]> getPossibleMoves(ArrayList<Space> possFrom) {
            ArrayList<Space[]> res = new ArrayList<>();
            ArrayList<Space> possPer;//possible moves per possible selected from
            for (int i = 0; i < possFrom.size(); i++) {
                possPer = B.getValidMoves(possFrom.get(i));
                if (possPer.size() > 0) {
                    for (int l = 0; l < possPer.size(); l++) {
                        res.add(new Space[]{possFrom.get(i), possPer.get(l)});
                    }
                }
            }
            return res;
        }
        public int[] getRandomDie(){
            Random random = new Random();
            int r = random.nextInt(20);
            return DIES_COMBINATION[r];
        }
        public void generateAllDieCombination() {
            int index = 0;
            for (int i = 1; i < 7; i++) {
                for (int j = i; j < 7; j++) {
                    int[] die = {i, j};
                    DIES_COMBINATION[index++] = die;
                }
            }
        }
        protected ArrayList<Space> getPossibleFrom() {
            ArrayList<Space> selection = new ArrayList<>();
            Space[] spaces = B.getSpaces();
            if (!spaces[B.getGameLoop().getSlainSpace(id)].isEmpty()) {
                selection.add(spaces[B.getGameLoop().getSlainSpace(id)]);
            } else {
                if (id == 0) {
                    for (int i = 1; i <= spaces.length - 2; i++) {
                        if (!spaces[i].getPieces().isEmpty())
                            if (spaces[i].getPieces().get(0).getId() == id) {
                                selection.add(spaces[i]);
                            }
                    }
                } else {
                    for (int i = spaces.length - 2; i >= 1; i--) {
                        if (!spaces[i].getPieces().isEmpty())
                            if (spaces[i].getPieces().get(0).getId() == id) {
                                selection.add(spaces[i]);
                            }
                    }
                }
            }
            return selection;

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

        public void makeMove(Move move) {
            this.B.BotMove(move);
        }

        public void undoMove(Move move) {
//        System.out.println("Undo move: from: " + move.from + " to: " + move.to);
            int temp = move.to;
            move.to = move.from;
            move.from = temp;
//        System.out.println("Undo move: from: " + move.from + " to: " + move.to);
            this.B.undoBotMove(move);

        }

        public int[] getDie(int i){
            return DIES_COMBINATION[i];
        }
    }
}
