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
    private Shape[] visSpaces = new Shape[27];
    private ArrayList<Ellipse2D> visPieces = new ArrayList<Ellipse2D>();
    private JPanel stat;
    GameLoop gameLoop;

    public BoardView(Board b, int frameWidth, int frameHeight) {
        startX = frameWidth/20;
        startY = frameHeight/30;
        width = frameWidth/20;
        height = frameHeight/3;
        space = 15;
        board=b;
        gameLoop= b.getGameLoop();
        setBackground(Variables.GAME_BACKGROUND_COLOR);
        StatusPanel s=new StatusPanel(10);
        stat = new JPanel(new BorderLayout());
        JButton passTurn = new JButton("Pass Turn");
        passTurn.addActionListener(actionEvent -> {
            gameLoop.changeTurn();
            System.out.println("REQUEST TURN PASS");
        });
        stat.add(passTurn, BorderLayout.NORTH);
        stat.add(s,BorderLayout.EAST);
        this.repaint();

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

        StatusPanel.updateRedPiecesDied(board.getPlayer2().getPiecesSlain());
        StatusPanel.updateRedPiecesInPlay(board.getPlayer2().getPiecesInPlay());
        StatusPanel.updateRedPiecesOutOfPlay(board.getPlayer2().getPiecesOutOfPlay());


        StatusPanel.updateWhitePiecesDied(board.getPlayer1().getPiecesSlain());
        StatusPanel.updateWhitePiecesInPlay(board.getPlayer1().getPiecesInPlay());
        StatusPanel.updateWhitePiecesOutOfPlay(board.getPlayer1().getPiecesOutOfPlay());

        StatusPanel.updateCurrentPlayer("" + gameLoop.getCurrentPlayer().toString());


        Ellipse2D dummyPiece;
        Polygon dummySpace;

        for (int i = 0; i < board.getSpaces().length; i++) {
            //SPACE PLACEMENTS
            int currentStartX = startX;
            int currentStartY = startY;
            Space p = board.getSpaces()[i];

            if (i % 2 == 0) {
                g.setColor(Variables.EVEN_SPACES_COLOR);
            } else {
                g.setColor(Variables.ODD_SPACES_COLOR);
            }

            if (i < 13) {
                if (i == 0) {
                    dummySpace = new Polygon(new int[]{startX, startX + width * 8, startX + width * 8, startX},
                            new int[]{startY + height * 2 + space * 2, startY + height * 2 + space * 2, startY + height * 2 + height / 2 + space * 2, startY + height * 2 + height / 2 + space * 2},
                            4);
                    // System.out.println("boardview start x= "+ startX +" start y= "+startY);
                    //System.out.println("boardview space= "+space+" height= "+height+" width= "+width);
                } else {
                    currentStartX = startX + (13 - i) * (width + space) - space;
                    currentStartY = startY + height + space;
                    dummySpace = new Polygon(new int[]{currentStartX, currentStartX - width / 2, currentStartX - width}, new int[]{currentStartY + height, currentStartY, currentStartY + height}, 3);
                }

            } else {
                if (i == 25) {
                    dummySpace = new Polygon(new int[]{startX + width * 8 + space, space + startX + width * 16, space + startX + width * 16, startX + width * 8 + space},
                            new int[]{startY + height * 2 + space * 2, startY + height * 2 + space * 2, startY + height * 2 + height / 2 + space * 2, startY + height * 2 + height / 2 + space * 2},
                            4);
                } else {
                    currentStartX = startX + (i % 13) * (width + space);
                    currentStartY = startY;
                    dummySpace = new Polygon(new int[]{currentStartX, currentStartX + width, currentStartX + width / 2}, new int[]{currentStartY, currentStartY, currentStartY + height}, 3);

                }
            }
            visSpaces[i] = dummySpace;
            g.fillPolygon(dummySpace);

            //PIECE PLACEMENTS
            if (p.getSize() > 0) {

                if (i < 13) {

                    int currentXPiece = currentStartX - (width);
                    for (int k = 1; k <= p.getSize(); k++) {
                        if (k < 7) {
                            g.setColor(p.getPieces().get(k - 1).getColor());
                            if (i == 0) {
                                dummyPiece = new Ellipse2D.Double(startX + k * width, startY + (height * 2) + 30 + (p.getPieces().get(k - 1).getId() * 30) + space, width, 30);
                            } else {
                                dummyPiece = new Ellipse2D.Double(currentXPiece, currentStartY + height - k * 30, width, 30);
                            }

                            visPieces.add(dummyPiece);
                            g.fill(dummyPiece);
                        } else {
                            if (k == 7) {
                                g.setColor(Color.GRAY);
                                String pLeft = "x" + p.getSize();
                                FontMetrics fm = g.getFontMetrics();
                                double textWidth = fm.getStringBounds(pLeft, g).getWidth();
                                int xPos;
                                int yPos;
                                if (i == 0) {
                                    xPos = (int) ((startX + k * width) - textWidth / 2 - width / 2);
                                    yPos = (int) ((startY + (height * 2) + 30 + (p.getPieces().get(k - 1).getId() * 30) + space) + fm.getMaxAscent() / 2 + space);
                                } else {
                                    xPos = (int) (currentXPiece - textWidth / 2 + width / 2);
                                    yPos = (int) ((currentStartY + height - k * 30) + fm.getMaxAscent() / 2 + space * 3);
                                }
                                g.drawString(pLeft, xPos, yPos);
                            }
                        }
                    }
                } else {
                    int currentXPiece = currentStartX;
                    //startX+width*8+ space,space+startX+width*16
                    for (int k = 1; k <= p.getSize(); k++) {
                        if (k < 7) {
                            g.setColor(p.getPieces().get(k - 1).getColor());
                            if (i == 25) {
                                dummyPiece = new Ellipse2D.Double(startX + 8 * width + space + k * width,
                                        startY + (height * 2) + 30 + (p.getPieces().get(k - 1).getId() * 30) + space, width, 30);
                            } else {
                                dummyPiece = new Ellipse2D.Double(currentXPiece, currentStartY + (k - 1) * 30, width, 30);
                            }
                            visPieces.add(dummyPiece);
                            g.fill(dummyPiece);
                        } else {
                            if (k == 7) {
                                g.setColor(Color.GRAY);
                                String pLeft = "x" + p.getSize();
                                FontMetrics fm = g.getFontMetrics();
                                double textWidth = fm.getStringBounds(pLeft, g).getWidth();
                                int xPos;
                                int yPos;
                                if (i == 25) {
                                    xPos = (int) ((startX + 8 * width + space + k * width) - width / 2 - space + space / 4);
                                    yPos = (int) (startY + (height * 2) + 30 + (p.getPieces().get(k - 1).getId() * 30) + space * 2 + space / 4);
                                } else {
                                    xPos = (int) ((currentXPiece - textWidth / 2) + width / 2);
                                    yPos = (int) (((currentStartY + (k - 1) * 30) - (space / 2)));
                                }
                                g.drawString(pLeft, xPos, yPos);
                            }

                        }
                    }
                }
            }
        }

        g.setColor(Variables.OUT_OF_PLAY_SPACE_COLOR);
        //out of Play space
        dummySpace = new Polygon(new int[]{startX + width * 16, startX + width * 17, startX + width * 17, startX + width * 16},
                new int[]{startY, startY, startY + height * 2 + space, startY + height * 2 + space},
                4);
        visSpaces[26] = dummySpace;
        g.fillPolygon(dummySpace);


        if (this.getMouseListeners().length == 0) {
            this.addMouseListener(new InputHandler(visSpaces, visPieces, this));
        }
        // draw the line in the middle        
        int lineX = (startX + (13 - 7) * (width + space) - space) + space / 2;
        g.fillRect(lineX, startY, 5, (startY + height * 2 + space) - startY);
        g.setColor(Color.BLUE);

        gameLoop.process();


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
        frame.add(stat, BorderLayout.EAST);
    }
    public void removeStatPanel(){stat.setVisible(false);}
}