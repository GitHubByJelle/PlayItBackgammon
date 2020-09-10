package src.khai;

import java.util.ArrayList;
import java.util.Stack;

public class Game {
    Board board;
    Stack<Piece> allPieces;
    ArrayList<Point> allPoints;
    int[] startPlayer1 = {5, 7, 12, 23};
    int[] startPlayer2 = {18, 16, 11, 0};
    int[] startPieces = {5, 3, 5, 2};

    public Game() {
        allPieces = new Stack<>();
        allPoints = new ArrayList<>();
        setUpBoard();

    }

    private void setUpBoard() {
        generateAllPieces();
        generateAllPoints();
        setUp();
    }

    private void generateAllPieces() {
        for (int j = 1; j < 3; j++) {
            for (int i = 0; i < 15; i++) {
                Piece piece = new Piece(j);
                allPieces.add(piece);
            }
        }
    }

    private void generateAllPoints() {
        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 12; j++) {
                Point point = new Point(i * j, i);
                allPoints.add(point);
            }
        }
    }

    private void setUp() {
        for(int i = 0; i < startPlayer2.length;i++){
            int p2 = startPlayer2[i];
            int num = startPieces[i];
            for(int j = 0; j < num; j++){
                allPoints.get(p2).addPiece(allPieces.pop());
            }
            allPoints.get(p2).setDominantId();
        }
        for(int i = 0; i < startPlayer1.length;i++){
            int p1 = startPlayer1[i];
            int num = startPieces[i];
            for(int j = 0; j < num; j++){
                allPoints.get(p1).addPiece(allPieces.pop());
            }
            allPoints.get(p1).setDominantId();

        }

    }
    public void disPlay(){
        System.out.println(allPoints.size());
        for(Point p : allPoints){
            if(p.getSize() > 0){
                System.out.println(p);
            }
        }
    }

}
