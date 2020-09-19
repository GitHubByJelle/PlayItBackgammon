package src.World;

import java.util.*;

public class Die {
    private int pointer=-1;
    private List<int[]> DieList = new ArrayList<int[]>();
    public Die(){
    }

    public void generateDie(){
        int roll1=0;
        int roll2=0;
        Random rn = new Random();
        for(int i=0;i<150;i++){
            roll1 = rn.nextInt(6) + 1;
            roll2 = rn.nextInt(6) + 1;
            int[] list = new int[2];
            list[0] = roll1;
            list[1] = roll2;
            DieList.add(list);
        }
    }

    public int[] getNextRoll(){
        ++pointer;
        //check that we still have rolls
        //if we dont, delete the current list, make a new one and start from the begining
        if(pointer>=DieList.size()){
            DieList.clear();
            generateDie();
            pointer=0;
        }
        return DieList.get(pointer);
    }

    public int[] getCurRoll(){
        return DieList.get(pointer);
    }

    public List getDieList(){
        return DieList;
    }

    public void printList(){
        System.out.print("{ ");
        for(int i=0;i<DieList.size();i++){
            if(i%10==0){
                System.out.println("\n");
            }
            System.out.print(" {" + DieList.get(i)[0]+ "," + DieList.get(i)[1] + "} ");

        }
        System.out.print(" }\n");
    }

    public void printCurRoll(){
        System.out.println(Arrays.toString(DieList.get(pointer)));
    }

    public boolean isDouble(int[] roll){
       return roll[0]==roll[1];
    }
}
