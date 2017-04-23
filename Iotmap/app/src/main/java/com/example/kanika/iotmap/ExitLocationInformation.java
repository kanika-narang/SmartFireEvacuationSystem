package com.example.kanika.iotmap;

/**
 * Created by kanika on 20/3/17.
 */
import android.annotation.SuppressLint;
import java.util.*;
import java.io.*;
class EInfo{
    int x;
    int y;
    String ExitName;
    int ExitId;
    EInfo()
    {
        x=0;
        y=0;
        ExitId=0;
        ExitName="NoExit";// By default value
    }
}
public class ExitLocationInformation {
    public EInfo[] eInfo = new EInfo[4];//since there are 4 exits.

    public EInfo[] getExits()
    {
        for(int i=0;i<4;i++)
        {
            eInfo[i]=new EInfo();
        }
        //exit1 main exit near the reception area
        eInfo[0].x=32;
        eInfo[0].y=1020;
        eInfo[0].ExitId=1;
        eInfo[0].ExitName="Main Exit from Reception Area ";

        //exit2 exit towards the girls hostel
        eInfo[1].x=643;
        eInfo[1].y=1022;
        eInfo[1].ExitId=2;
        eInfo[1].ExitName="Exit Towards the Girls Hostel";

        //exit3 exit towards Main Gate of campus.
        eInfo[2].x=342;
        eInfo[2].y=39;
        eInfo[2].ExitId=3;
        eInfo[2].ExitName="Network Lab Exit";

        //exit4 exit from the Back Gate
        eInfo[3].x=352;
        eInfo[3].y=1976;
        eInfo[3].ExitId=4;
        eInfo[3].ExitName="Director Sir Room's Exit";

        return eInfo;


    }



}
