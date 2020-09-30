package World;

import java.util.*;

public class Die {
    private int pointer=-1;
    private List<int[]> DieList = new ArrayList<int[]>();
    public Die(){
    }

    public void generateDie(){
        int roll1=0;
        int roll2=0;
        //force the first roll to be smth u r testing for
        DieList.add(new int[]{1,2});
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
        System.out.print(Arrays.toString(DieList.get(pointer)));
    }

    public boolean isDouble(int[] roll){

       return roll.length>1 &&roll[0]==roll[1];
    }

    public void changeCurRoll(int [] newRoll){
        DieList.set(pointer, newRoll);
    }



    public void removeUsedRoll(int target){
       int [] newRoll= new int[getCurRoll().length-1];
       int index=0;
      for(int i=0;i<getCurRoll().length;i++){
          if(getCurRoll()[i]==target){
              continue;
          }else{
              newRoll[index]=getCurRoll()[i];
              System.out.println(target+" "+Arrays.toString(newRoll));
              ++index;
          }
      }
      changeCurRoll(newRoll);
    }



}
