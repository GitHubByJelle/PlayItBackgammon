package Visualisation;


import GUI.StatusPanel;
import Utils.Variables;
import World.Board;
import World.GameLoop;
import World.Space;



import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class BoardView extends JPanel  {
    private int startX;
    private int startY;
    private int width;
    private int height;
    private int space;
    Board board;
    private Shape[] visSpaces = new Shape[25];
    private ArrayList<Ellipse2D> visPieces = new ArrayList<Ellipse2D>();
    int startPlayer = 1;
    StatusPanel s ;
    GameLoop g;

    public BoardView(Board b, int frameWidth, int frameHeight) {
        startX = frameWidth/20;
        startY = frameHeight/30;
        width = frameWidth/20;
        height = frameHeight/3;
        space = 15;
        board=b;
        setBackground(Variables.GAME_BACKGROUND_COLOR);
        s=new StatusPanel(10);
        g= new GameLoop(b);

    }

    @Override
    protected void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        //basically just to make things look smooth
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHints(rh);

        StatusPanel.updateDiceRolls(board.getDie().getCurRoll());
        StatusPanel.updateBlackPiecesDied(board.getPlayer2().getPiecesSlain());
        StatusPanel.updateBlackPiecesInPlay(board.getPlayer2().getPiecesInPlay());
        StatusPanel.updateBlackPiecesOutOfPlay(board.getPlayer2().getPiecesOutOfPlay());
        StatusPanel.updateWhitePiecesDied(board.getPlayer1().getPiecesSlain());
        StatusPanel.updateWhitePiecesInPlay(board.getPlayer1().getPiecesInPlay());
        StatusPanel.updateWhitePiecesOutOfPlay(board.getPlayer1().getPiecesOutOfPlay());

        Ellipse2D dummyPiece;
        Polygon dummySpace;

        g.setColor(Color.BLUE);

        for (int i = 0; i < board.getSpaces().length; i++) {
            //SPACE PLACEMENTS
            int currentStartX, currentStartY;
            Space p = board.getSpaces()[i];

            if (i % 2 == 0) {
                g.setColor(Variables.EVEN_SPACES_COLOR);

            } else {
                g.setColor(Variables.ODD_SPACES_COLOR);
            }

            if (i < 13) {
                if (i == 0) {
                    currentStartX = startX +  (13-i) * (width + space);
                    currentStartY = startY+ height+ space;
                } else {

                    currentStartX = startX +  (13-i) * (width + space) -space;
                    currentStartY = startY+ height+ space;
                    dummySpace = new Polygon(new int[]{currentStartX, currentStartX - width/2, currentStartX - width }, new int[]{currentStartY+height, currentStartY, currentStartY + height}, 3);
                    if(i == 1) {
                    }
                    visSpaces[i]=dummySpace;
                    g.fillPolygon(dummySpace);
                }

            }else {
                if (i == 25) {
                    currentStartX = startX + (i%13) * (width + space);
                    currentStartY = startY;
                } else {
                    currentStartX = startX + (i % 13) * (width + space);
                    currentStartY = startY;

                    dummySpace = new Polygon(new int[]{currentStartX, currentStartX + width, currentStartX + width / 2}, new int[]{currentStartY, currentStartY, currentStartY + height}, 3);
                    visSpaces[i]=dummySpace;
                    g.fillPolygon(dummySpace);
                }
            }

            //PIECE PLACEMENTS
            if (p.getSize() > 0) {
                if (i<13){
                    int currentXPiece = currentStartX - (width);
                    for (int k = 1; k <= p.getSize(); k++) {
                        g.setColor(p.getPieces().get(k-1).getColor());
                        dummyPiece =new Ellipse2D.Double(currentXPiece, currentStartY+height- k*30, width, 30);
                        visPieces.add(dummyPiece);
                        g.fill(dummyPiece);
                    }
                }else{
                    int currentXPiece = currentStartX;
                    for (int k = 0; k < p.getSize(); k++) {
                        g.setColor(p.getPieces().get(k).getColor());
                        dummyPiece =new Ellipse2D.Double(currentXPiece, currentStartY + k * 30, width, 30);
                        visPieces.add(dummyPiece);
                        g.fill(dummyPiece);
                    }
                }
            }
        }



        if(this.getMouseListeners().length==0){
            this.addMouseListener(new InputHandler(startPlayer,visSpaces,visPieces, this));
        }
    }




    public int getSpace() {
        return space;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartX() {
        return startX;
    }

    public void addStatPane(JFrame frame){
        frame.add(s, BorderLayout.EAST);
    }
}