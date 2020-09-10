package World;

public class Board {
    private Space[] spaces;
    private int width;
    private int height;

    public Board(int width, int height) {
        this.width=width;
        this.height=height;
        //TODO:@alaa
        spaces = new Space[25];//0 is for the "eaten" ,1-12 is the bottom(right-left), 13-24 is top (Left to right)
        int y_offset = height / 2;
        int cury = 0;
        createSide(width, cury);
        cury += y_offset;
        createSide(width, cury);

        addPieces(0,5,0);
        addPieces(4,3,1);
        addPieces(6,5,1);
        addPieces(11,2,0);

        addPieces(12,5,1);
        addPieces(16,3,0);
        addPieces(18,5,0);
        addPieces(23,2,1);




    }

    private void addPieces(int spaceIndex, int num, int colorId) {
        for(int i = 0;i< num;i++)
            spaces[spaceIndex].getPieces().add(new Piece(colorId));
    }

    private void createSide(int width, int y) {
        int curx = 0;
        int x_offset = width / 12;
        for (int i = 0; i < spaces.length; i++) {
            spaces[i] = new Space(curx, y);
            curx += x_offset;
        }
    }


    public String toString() {
        String res="";
        for(int i=0;i<spaces.length;i++){
            res+= spaces[i]+ "\n";
        }


        return res;
    }

    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }

}









