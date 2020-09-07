package World;

import java.util.*;

public class Die {
    private List<int[]> DieList = new ArrayList<int[]>();
    public Die(){

    }
    public void generateDie(){
        int roll1=0;
        int roll2=0;
        Random rn = new Random();
        for(int i=0;i<151;i++){
        roll1 = rn.nextInt(6) + 1;
        roll2 = rn.nextInt(6) + 1;
        int[] list = new int[2];
        list[0] = roll1;
        list[1] = roll2;
        DieList.add(list);  
        }
    }
    public List getDieList(){
        return DieList;
    }

    public void printList(){
        generateDie();
        System.out.print("{ ");
        for(int i=0;i<DieList.size();i++){
            System.out.print(" {" + DieList.get(i)[0]+ "," + DieList.get(i)[1] + "} ");
        }
        System.out.print(" }");
    }
}