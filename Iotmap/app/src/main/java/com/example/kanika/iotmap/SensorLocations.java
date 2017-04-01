package com.example.kanika.iotmap;

/**
 * Created by kanika on 20/3/17.
 */
class SInfo
{
    int x;
    int y;
    int id;
    String locationInfo;
}
public class SensorLocations {

    SInfo sinfo[]=new SInfo[3];// Since there are three locations where we are puting sensors

    public SInfo[] getSInfo()
    {
        for(int i=0;i<3;i++)
        {
            sinfo[i]=new SInfo();
        }

        //sensor 1
        sinfo[0].x=180;
        sinfo[0].y=1008;
        sinfo[0].id=1;
        sinfo[0].locationInfo="Reception area";
        //sensor 2
        sinfo[1].x=407;
        sinfo[1].y=1359;
        sinfo[1].id=2;
        sinfo[1].locationInfo="106 Room No.";
        //sensor 3
        sinfo[2].x=167;
        sinfo[2].y=1603;
        sinfo[2].id=3;
        sinfo[2].locationInfo="Faculty Room";

        return sinfo;
    }

}
