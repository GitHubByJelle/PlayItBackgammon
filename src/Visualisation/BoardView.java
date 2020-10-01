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
    private Shape[] visSpaces = new Shape[26];
    private ArrayList<Ellipse2D> visPieces = new ArrayList<Ellipse2D>();
    StatusPanel s ;
    GameLoop gameLoop;

    public BoardView(Board b, int frameWidth, int frameHeight) {
        startX = frameWidth/20;
        startY = frameHeight/30;
        width = frameWidth/20;
        height = frameHeight/3;
        space = 15;
        board=b;
        setBackground(Variables.GAME_BACKGROUND_COLOR);
        s=new StatusPanel(10);
        gameLoop= b.getLoop();
        gameLoop.setBoardView(this);

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
        StatusPanel.updateWhitePiecesDied(board.getPlayer2().getPiecesSlain());
        StatusPanel.updateWhitePiecesInPlay(board.getPlayer2().getPiecesInPlay());
        StatusPanel.updateWhitePiecesOutOfPlay(board.getPlayer2().getPiecesOutOfPlay());
        StatusPanel.updateRedPiecesDied(board.getPlayer1().getPiecesSlain());
        StatusPanel.updateRedPiecesInPlay(board.getPlayer1().getPiecesInPlay());
        StatusPanel.updateRedPiecesOutOfPlay(board.getPlayer1().getPiecesOutOfPlay());
        StatusPanel.updateCurrentPlayer(""+gameLoop.getCurrentPlayer().toString());
        gameLoop.process();
        Ellipse2D dummyPiece;
        Polygon dummySpace;

        g.setColor(Color.BLUE);

        for (int i = 0; i < board.getSpaces().length; i++) {
            //SPACE PLACEMENTS
            int currentStartX = startX;
            int currentStartY=startY;
            Space p = board.getSpaces()[i];

            if (i % 2 == 0) {
                g.setColor(Variables.EVEN_SPACES_COLOR);
            } else {
                g.setColor(Variables.ODD_SPACES_COLOR);
            }

            if (i < 13) {
                if (i == 0) {
                    dummySpace = new Polygon(new int[]{startX, startX+((space-1)+width)*6, startX+((space-1)+width)*6,startX },
                            new int[]{startY+height*2 +space*2,startY+height*2+ space*2 ,startY+height*2+height/2 +space*2,startY+height*2+height/2+space*2},
                            4);
                } else {
                    currentStartX = startX +  (13-i) * (width + space) -space;
                    currentStartY = startY+ height+ space;
                    dummySpace = new Polygon(new int[]{currentStartX, currentStartX - width/2, currentStartX - width }, new int[]{currentStartY+height, currentStartY, currentStartY + height}, 3);
                }

            }else {
                if (i == 25) {
                    dummySpace = new Polygon(new int[]{startX+((space-1)+width)*6  +space*2,  2*(startX+((space-3)+width)*6)-space,2*(startX+((space-3)+width)*6)-space, (startX+((space-1)+width)*6) +space*2 },
                            new int[]{startY+height*2 +space*2,startY+height*2+ space*2 ,startY+height*2+height/2 +space*2,startY+height*2+height/2+space*2},
                            4);
                }
//                else if (i == 26) {
//
//                }
                else {
                    currentStartX = startX + (i % 13) * (width + space);
                    currentStartY = startY;
                    dummySpace = new Polygon(new int[]{currentStartX, currentStartX + width, currentStartX + width / 2}, new int[]{currentStartY, currentStartY, currentStartY + height}, 3);

                }
            }
            visSpaces[i]=dummySpace;
            g.fillPolygon(dummySpace);

            //PIECE PLACEMENTS
            if (p.getSize() > 0) {
                if (i<13){

                    int currentXPiece = currentStartX - (width);
                    for (int k = 1; k <= p.getSize(); k++) {

                        g.setColor(p.getPieces().get(k-1).getColor());
                        if(i==0){
                            dummyPiece= new Ellipse2D.Double(startX+k*width, startY+(height*2)+30+(p.getPieces().get(k-1).getId()*30)+space, width, 30);
                        }else {
                            dummyPiece = new Ellipse2D.Double(currentXPiece, currentStartY + height - k * 30, width, 30);
                        }
                        visPieces.add(dummyPiece);
                        g.fill(dummyPiece);
                    }
                }else{
                    int currentXPiece = currentStartX;
                    for (int k = 1; k <=p.getSize(); k++) {
                        g.setColor(p.getPieces().get(k-1).getColor());

                        if(i==25){
                            dummyPiece= new Ellipse2D.Double(startX+(startX+6*width)+k*(width*2), startY+(height*2)+30+(p.getPieces().get(k-1).getId()*30)+space, width, 30);

                        }else {
                            dummyPiece = new Ellipse2D.Double(currentXPiece, currentStartY + (k-1) * 30, width, 30);

                        }
                        visPieces.add(dummyPiece);
                        g.fill(dummyPiece);
                    }
                }
            }
        }


        //out of Play space

        if(this.getMouseListeners().length==0){
            this.addMouseListener(new InputHandler(visSpaces,visPieces, this));
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