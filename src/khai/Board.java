package src.khai;

import java.util.ArrayList;

public class Board {
    ArrayList<Point> points;
    int id;
    public Board(ArrayList<Point> points) {
        this.points = points;
        this.id = points.get(0).id;
    }
}
