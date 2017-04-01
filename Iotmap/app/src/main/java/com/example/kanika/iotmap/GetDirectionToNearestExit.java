package com.example.kanika.iotmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kanika on 21/3/17.
 */


class Locations{
    class Coordinate{
        int x;
        int y;
        List<String> path= new ArrayList<String>();

        Coordinate(int x,int y, List <String>L)
        {
            this.x=x;
            this.y=y;
            this.path=L;

        }
    }
    HashMap<Integer,Coordinate> Map =new HashMap<Integer,Coordinate>();
    Locations()
    {
        List<String> temp = new ArrayList<String>();
        temp.add("Exit 1(Network Lab exit)");
        temp.add("Exit 2(Girls Hostel)");
        temp.add("Exit 3(Reception Arae)");
        temp.add("Exit 4(Director Sir)");
        Map.put(1,new Coordinate(142,1432, temp));
        temp = new ArrayList<String>();
        temp.add("Exit 11(Network Lab exit)");
        temp.add("Exit 12(Girls Hostel)");
        temp.add("Exit 13(Reception Arae)");
        temp.add("Exit 14(Director Sir)");
        Map.put(2,new Coordinate(605,1473, temp));
        temp = new ArrayList<String>();
        temp.add("Exit 21(Network Lab exit)");
        temp.add("Exit 22(Girls Hostel)");
        temp.add("Exit 23(Reception Arae)");
        temp.add("Exit 24(Director Sir)");
        Map.put(3,new Coordinate(260,574, temp));
        temp = new ArrayList<String>();
        temp.add("Exit 11(Network Lab exit)");
        temp.add("Exit 12(Girls Hostel)");
        temp.add("Exit 13(Reception Arae)");
        temp.add("Exit 14(Director Sir)");
        Map.put(4,new Coordinate(506,392, temp));
        temp = new ArrayList<String>();
        temp.add("Exit 11(Network Lab exit)");
        temp.add("Exit 12(Girls Hostel)");
        temp.add("Exit 13(Reception Arae)");
        temp.add("Exit 14(Director Sir)");
        Map.put(5,new Coordinate(180,1008, temp));
        temp = new ArrayList<String>();
        temp.add("Exit 11(Network Lab exit)");
        temp.add("Exit 12(Girls Hostel)");
        temp.add("Exit 13(Reception Arae)");
        temp.add("Exit 14(Director Sir)");
        Map.put(6,new Coordinate(133,1640, temp));
        temp = new ArrayList<String>();
        temp.add("Exit 11(Network Lab exit)");
        temp.add("Exit 12(Girls Hostel)");
        temp.add("Exit 13(Reception Arae)");
        temp.add("Exit 14(Director Sir)");
        Map.put(7,new Coordinate(358,1304, temp));
        temp = new ArrayList<String>();
        temp.add("Exit 11(Network Lab exit)");
        temp.add("Exit 12(Girls Hostel)");
        temp.add("Exit 13(Reception Arae)");
        temp.add("Exit 14(Director Sir)");
        Map.put(8,new Coordinate(405,1390, temp));
        temp = new ArrayList<String>();
        temp.add("Exit 11(Network Lab exit)");
        temp.add("Exit 12(Girls Hostel)");
        temp.add("Exit 13(Reception Arae)");
        temp.add("Exit 14(Director Sir)");
        Map.put(9,new Coordinate(440,1119, temp));
        temp = new ArrayList<String>();
        temp.add("Exit 11(Network Lab exit)");
        temp.add("Exit 12(Girls Hostel)");
        temp.add("Exit 13(Reception Arae)");
        temp.add("Exit 14(Director Sir)");
        Map.put(10,new Coordinate(133,1653, temp));
        temp = new ArrayList<String>();
        temp.add("Exit 11(Network Lab exit)");
        temp.add("Exit 12(Girls Hostel)");
        temp.add("Exit 13(Reception Arae)");
        temp.add("Exit 14(Director Sir)");
        Map.put(11,new Coordinate(587,980, temp));
        temp = new ArrayList<String>();
        temp.add("Exit 11(Network Lab exit)");
        temp.add("Exit 12(Girls Hostel)");
        temp.add("Exit 13(Reception Arae)");
        temp.add("Exit 14(Director Sir)");
        Map.put(12,new Coordinate(223,693, temp));
        temp = new ArrayList<String>();
        temp.add("Exit 11(Network Lab exit)");
        temp.add("Exit 12(Girls Hostel)");
        temp.add("Exit 13(Reception Arae)");
        temp.add("Exit 14(Director Sir)");
        Map.put(13,new Coordinate(200,1900, temp));








    }

}
public class GetDirectionToNearestExit {

    String Direction="";
   public void getdirection(Coordinate mylocation,Coordinate nearestsafelocation)
   {
       if(mylocation.x==142 && mylocation.y==1432)
       {
            Direction="don't know";
       }
       else if(mylocation.x==605 && mylocation.y==1473)
       {
            Direction="Come Out of the Room ";
       }
       else if(mylocation.x==260 && mylocation.y==574)
       {

       }
       else if(mylocation.x==506 && mylocation.y==392)
       {

       }
       else if(mylocation.x==180 && mylocation.y==1008)
       {

       }
       else if(mylocation.x==133 && mylocation.y==1640)
       {

       }
       else if(mylocation.x==358 && mylocation.y==1304)
       {

       }
       else if(mylocation.x==405 && mylocation.y==1390)
       {

       }
   }

}
