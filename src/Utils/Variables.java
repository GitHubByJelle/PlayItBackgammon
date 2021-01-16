package Utils;

import AI.GA.TMM;
import World.Player;
import AI.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class Variables {


    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final double FRAME_WIDTH= 900 ;
    public static final double FRAME_HEIGHT = 650;


    //colors
    public static final Color RED_PIECE_COLOR = new Color(190,0,0);
    public static final Color WHITE_PIECE_COLOR = Color.WHITE;

    public static final Color ODD_SPACES_COLOR = new Color(156, 89, 60);
    public static final Color EVEN_SPACES_COLOR = new Color(91, 61, 41);
    public static final Color OUT_OF_PLAY_SPACE_COLOR= new Color(91, 61, 41);
    public static final Color RECOLOR_SPACES_COLOR =new Color(250,20,20,50);

    public static final Color GAME_BACKGROUND_COLOR = new Color(213, 165, 117);


    public static final String HUMAN = "Human";
    public static final String TMM = "TMM";
    public static final String RANDOM = "RandomBot";
    public static final String SIMPLEBOT="SimpleBot";
    public static final String BP="PrimeBlitzingBot";
    public static final String ABB="AlphaBetaBot";
    public static final String TDG="TD-Gammon";



    //for now its gonna be like this
    public static final String[] PLAYERS= {HUMAN,  RANDOM, SIMPLEBOT,BP,TMM,ABB,TDG};
    public static final String[] BOTS= {RANDOM, SIMPLEBOT,BP,TMM,TDG};

    public static String GET_RANDOM_BOT(){
        return BOTS[ (int)(Math.random() * ((BOTS.length) + 1) )];
    }


    public static final ArrayList<int[]> All_POSS_ROLLS = getPossRolls();
    private static ArrayList<int[]> getPossRolls(){
        Set<int[]> res= new HashSet<int[]>();
        int [] roll;
        for(int first =1; first<=6; first++){
            for(int second =1; second<=6; second++){
                if(second==first)
                    roll= new int[]{first,first,first,first};
                else
                    roll=new int []{first,second};

                res.add(roll);
            }
        }

        return new ArrayList<>(res);
    }




}
