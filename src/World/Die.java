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
      //  DieList.add(new int[]{1,2});
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
    public void seeNextRoll(){
        pointer++;
    }
    public void goRollBack(){
        pointer--;
    }
    public void deleteNumber(int num){
        for(int i = 0; i<this.getCurRoll().length; i++){
            if(this.getCurRoll()[i]==num){
                this.getCurRoll()[i] = 0;
                break;
            }
        }
    }
    public void addNumber(int num){
        for(int i = 0; i<this.getCurRoll().length; i++){
            if(this.getCurRoll()[i]==0){
                this.getCurRoll()[i] = num;
                break;
            }
        }
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
       return roll.length==2 && (roll[0]!=0 ||roll[1]!=0)&&roll[0]==roll[1];
    }

    public void changeCurRoll(int [] newRoll){
        DieList.set(pointer, newRoll);
    }



    public void removeUsedRoll(int target){
       int [] newRoll= new int[getCurRoll().length-1];
       int index=0;
       boolean found=false;
       if(newRoll.length!=0)
            for(int i=0;i<getCurRoll().length;i++){
                if(getCurRoll()[i]==target && !found){
                    found=true;
                    continue;
                }else{
                    newRoll[index]=getCurRoll()[i];
                    ++index;
                }
            }
      changeCurRoll(newRoll);
    }


    public void removeUsedRollOutOfPlay() {

        int max=0;
        for(int i=0;i<getCurRoll().length;i++){
           if(Math.abs(getCurRoll()[i])>Math.abs(max)){
               max=getCurRoll()[i];
           }
        }

        removeUsedRoll(max);

    }

    public void setDieTo(ArrayList<int[]> i) {
        DieList=i;
    }
}
