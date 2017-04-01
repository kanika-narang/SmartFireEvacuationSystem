 package com.example.kanika.iotmap;

import android.annotation.SuppressLint;

 class RouterInformation {
     String BSSID, floorNumber, roomNumber, additionalInformation;
     int lattitude, longitude, id;
 }

public class RouterLocationInformation {

    /* List<String> RouterBSSID,FloorNumber,RoomNumber,AdditionalInformation; */
    private static String groundFloor = "Floor 0";
    private static String firstFloor = "Floor 1";
    private static String secondFloor = "Floor 2";
    public int globalCounter = 0; // Global Building Router Counter
    public RouterInformation[] rInfo = new RouterInformation[300];// = new RouterInformation[300]; //Assuming 300 routers


    @SuppressLint("DefaultLocale")
    public RouterInformation[] fillAllRouterLocationInformation() {

        // start changes
        for (int i = 0; i <= 9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("a0:d3:c1:b4:2b:3%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "Jaya Ma'am Room No:119 ";
            rInfo[globalCounter].lattitude = 142;
            rInfo[globalCounter].longitude = 1432;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 1;
            globalCounter++;
        }

        for (int i = 0; i <=9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("6c:b0:ce:78:19:b%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "Inside MCR Room No. 106 ";
            rInfo[globalCounter].lattitude = 605;
            rInfo[globalCounter].longitude = 1473;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 2;
            globalCounter++;
        }

        for (int i = 0; i <= 9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("a0:d3:c1:b4:8e:b%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "Faculty Room No. 133 ";
            rInfo[globalCounter].lattitude = 260;
            rInfo[globalCounter].longitude = 574;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 3;
            globalCounter++;
        }


        for (int i = 0; i <=9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("58:20:b1:10:e2:5%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "Network and communication Lab Room No. 101";
            rInfo[globalCounter].lattitude = 506;
            rInfo[globalCounter].longitude = 392;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 4;
            globalCounter++;
        }

        for (int i = 0; i <= 9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("00:3a:98:b9:b3:4%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "Reception Area";
            rInfo[globalCounter].lattitude = 180;
            rInfo[globalCounter].longitude = 1008;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 5;
            globalCounter++;
        }

        for (int i = 0; i <= 9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("a0:d3:c1:b4:be:5%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "Inside Room S5 ";
            rInfo[globalCounter].lattitude = 133;
            rInfo[globalCounter].longitude = 1640;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 6;
            globalCounter++;
        }

        for (int i = 0; i <= 9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("a0:d3:c1:b4:8e:d%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "Passage of ground floor";
            rInfo[globalCounter].lattitude = 358;
            rInfo[globalCounter].longitude = 1304;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 7;
            globalCounter++;
        }

        for (int i = 0; i <= 9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("a0:d3:c1:b4:8e:f%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "MCR Room No. 106 ";
            rInfo[globalCounter].lattitude = 405;
            rInfo[globalCounter].longitude = 1390;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 8;
            globalCounter++;
        }


        for (int i = 0; i <= 9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("a0:d3:c1:b4:be:3%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "In way of MCR ";//Data center
            rInfo[globalCounter].lattitude = 440;
            rInfo[globalCounter].longitude = 1119;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 2;
            globalCounter++;
        }

        for (int i = 0; i <= 9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("6c:b0:ce:85:29:d%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "106 MCR ";//204
            rInfo[globalCounter].lattitude = 605;
            rInfo[globalCounter].longitude = 1473;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 2;
            globalCounter++;
        }

        for (int i = 0; i <= 9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("00:3a:98:b9:90:a%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "Neelam Sinha ma'am Room ";
            rInfo[globalCounter].lattitude = 200;
            rInfo[globalCounter].longitude = 1900;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 13;
            globalCounter++;
        }

        for (int i = 0; i <= 9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("a0:d3:c1:b4:9e:b%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "S8/S5 ";
            rInfo[globalCounter].lattitude = 133;
            rInfo[globalCounter].longitude = 1653;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 10;
            globalCounter++;
        }


        for (int i = 0; i <= 9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("3c:a8:2a:77:60:3%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "Enterance Gate from Hostel";
            rInfo[globalCounter].lattitude = 587;
            rInfo[globalCounter].longitude = 980;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 11;
            globalCounter++;
        }

        for (int i = 0; i <= 9; i++) {
            rInfo[globalCounter] = new RouterInformation();
            rInfo[globalCounter].BSSID = String.format("00:3a:98:b9:bd:6%d", i);
            rInfo[globalCounter].floorNumber = groundFloor;
            rInfo[globalCounter].roomNumber = "  Room No: 132 ";//ceems lab
            rInfo[globalCounter].lattitude = 223;
            rInfo[globalCounter].longitude = 693;
            rInfo[globalCounter].additionalInformation = " No additional Information ";
            rInfo[globalCounter].id = 12;
            globalCounter++;
        }

        return rInfo;
    }
}
