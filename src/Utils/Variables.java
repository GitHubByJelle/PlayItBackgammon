package Utils;

import java.awt.*;

public final class Variables {

    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static double SCREEN_WIDTH=  screenSize.getWidth();
    public static double  SCREEN_HEIGHT= screenSize.getHeight();


    //colors
    public static Color RED_PIECE_COLOR = new Color(204,204,204);//change these later
    public static Color WHITE_PIECE_COLOR = Color.BLACK;

    public static Color ODD_SPACES =Color.WHITE;
    public static Color EVEN_SPACES= new Color(51,204,255);

    //fornow its gonna be like this, later we can change it to store AI objects @ali
    public static String[] PLAYERS= {"Human", "BotA", "BotB"};

}
