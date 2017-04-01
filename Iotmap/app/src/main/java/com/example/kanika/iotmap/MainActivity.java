package com.example.kanika.iotmap;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


class MyCanvas extends View {
    Coordinate fireL;
    Coordinate safeL;
    String color;
    public MyCanvas(Context context,Coordinate fireL,String color,Coordinate safeL) {
        super(context);
        this.fireL=fireL;
        this.color=color;
        this.safeL=safeL;
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint pBackground = new Paint();

            pBackground.setColor(Color.GREEN);
            canvas.drawCircle((int) safeL.x, (int) safeL.y, 10, pBackground);
            pBackground.setColor(Color.RED);
            canvas.drawCircle((int) fireL.x, (int) fireL.y, 10, pBackground);
        
        }
}
class Coordinate
{
    int x,y;
    Coordinate()
    {
        x=0;
        y=0;
    }
}

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity /*implements View.OnTouchListener*/ {

    private View fullView;
    ImageView iv;
    int globalRouterCount;
    public RouterInformation[] rInfo;
    MediaPlayer mediaPlayer;
    public EInfo[] einfo;
    public SInfo[] sinfo;
    List<ScanResult> scanList;
    Locations paths;
    int counter1 = 1;
    String android_id;
    long UpdateTillTime = 10000000;
    long UpdateFrequency = 5000; // Update every 10 seconds
    long millisUntilFinished2;
    BroadcastReceiver broadcastReceiver;
    Coordinate NewNearExit=new Coordinate();
    Coordinate userlocation=new Coordinate();
    int userId=0;
    Coordinate firelocation=new Coordinate();
    EInfo exitNearFire=new EInfo();
    EInfo nearestSafeExit=new EInfo();
    Coordinate OldSafeExit=new Coordinate();
    boolean fire=false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fullView = (View) findViewById(R.id.activity_main);
        iv = (ImageView) findViewById(R.id.mapimageview);
        iv.getLayoutParams().width=762;
        iv.getLayoutParams().height=2000;

        // iv.setOnTouchListener(this);


        // starting the indoor positioning
        RouterLocationInformation routerLocationInformation = new RouterLocationInformation();
        rInfo = routerLocationInformation.fillAllRouterLocationInformation();
        globalRouterCount = routerLocationInformation.globalCounter;

        // get sensor location info
        SensorLocations sensorlocation=new SensorLocations();
        sinfo=sensorlocation.getSInfo();

        //get exit location information
        ExitLocationInformation exitlocationinformation=new ExitLocationInformation();
        einfo=exitlocationinformation.getExits();


        //get path details
        paths=new Locations();


        ActionStartsHere();
        //ending it
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void ActionStartsHere() {
        getWifiNetworksList2();
    }

    public void getWifiNetworksList2() {
        new CountDownTimer(UpdateTillTime, UpdateFrequency) { //Update every 8 seconds till 1000 seconds -->parameters are in milliseconds
            @Override
            public void onTick(long millisUntilFinished) {

                // Display Data by Every Ten Second
                //tv = null;
                scanList = null;
                //millisUntilFinished2 = 0;
                counter1++;

                getWifiNetworksList(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                // ActionStartsHere();
                finish();
            }

        }.start();
    }


    public void getWifiNetworksList(long millisUntilFinished) {
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        // Get List of Wifi
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        millisUntilFinished2 = millisUntilFinished;

        broadcastReceiver = new BroadcastReceiver() {

            @SuppressLint({"UseValueOf", "DefaultLocale"})
            @Override
            public void onReceive(Context context, Intent intent) {


                scanList = wifiManager.getScanResults();
                // System.out.println("\n  Number Of Wifi connections :" + " " + scanList.size() + "\n\n");
                HashMap<ScanResult, Double> mmap = new HashMap<ScanResult, Double>();
                //  System.out.println("hello");
                for (int i = 0; i < scanList.size(); i++) {

                    ScanResult s = scanList.get(i);
                    double lev = s.level;
                    double distanceFromWifi = calculateDistance(lev, 2400.0);
                    // System.out.println("distance = "+distanceFromWifi);
                    mmap.put(s, distanceFromWifi);

                }

                int ctr = 1;

                Map<ScanResult, Double> sortedList = sortByValue(mmap);

                for (Map.Entry<ScanResult, Double> entry : sortedList.entrySet()) {
                    Double distance;
                    distance = entry.getValue();
                    if (distance <= 20) {
                        ScanResult s = entry.getKey();

                        System.out.println(new Integer(ctr++).toString() + ". ");
                        System.out.println("SSID: " + (s.SSID));
                        System.out.println("BSSID: " + (s.BSSID));
                        System.out.println(String.format("Signal Strength : " + (s.level)));
                        System.out.println(String.format("Distance from wifi in meters " + distance));

                        System.out.println("\n ====YOUR LOCATION INFORMATION ===\n");


                        boolean BSSIDFound = false;
                        if (isOnline()) {
                            for (int i = 0; i < globalRouterCount; i++) {
                                if (rInfo[i].BSSID.equals(s.BSSID)) {

                                    System.out.println("\n BSSID : " + rInfo[i].BSSID);
                                    System.out.println("\n Floor Information : " + rInfo[i].floorNumber);
                                    System.out.println("\n Room Information : " + rInfo[i].roomNumber);
                                    System.out.println("\n Additional Information : " + rInfo[i].additionalInformation);
                                    System.out.println("\n Lattiude : " + rInfo[i].lattitude);
                                    System.out.println("\n Longitude : " + rInfo[i].longitude);
                                    System.out.println("\n -------- --------- \n ");
                                    BSSIDFound = true;

                                    userlocation.x=rInfo[i].lattitude;
                                    userlocation.y=rInfo[i].longitude;
                                    userId=rInfo[i].id;
                                    System.out.println(userlocation.x+" "+userlocation.y);
                                    Toast.makeText(getBaseContext(), "Room:"+rInfo[i].roomNumber+" x="+rInfo[i].lattitude+" y= "+ rInfo[i].longitude, Toast.LENGTH_SHORT).show();

                                    //Check for internet connection and Insert into mysql database


                                    break;
                                }
                            }
                            if (BSSIDFound == false) {
                                System.out.println("\n NO LOCATION INFORMATION AVAILABLE BUT YOU ARE CONNECTED TO INTERNET!! \n ");
                                System.out.println(userlocation.x+" "+userlocation.y);
                                userlocation.x=0;
                                userlocation.y=0;
                                System.out.println(userlocation.x+" "+userlocation.y);
                                Toast.makeText(getBaseContext(), " NO LOCATION ", Toast.LENGTH_SHORT).show();
                                System.out.println("\n -------- --------- \n ");
                            }
                        } else {
                            System.out.println("\n YOU ARE NOT CONNECTED TO INTERNET!! \n ");
                            System.out.println(" \n Checking internet connectivity in " + UpdateFrequency / 1000 + " seconds............\n");
                            System.out.println("\n  \n ");
                        }


                        break;

                    } else if (distance > 20) {
                        break;
                    }
                    break;
                }

                // tv.setText(sb);
            }

        };

        this.registerReceiver(broadcastReceiver, filter);
        wifiManager.startScan();

        setFire();
        if(fire==true)
        {
            // If fire is true turn on the alarm
            mediaPlayer= MediaPlayer.create(this, R.raw.firealarm);
           // mediaPlayer.start();
            //get the nearest fire exit location
            getExitNearFire();
            // get  safest nearest location
            getnearestExit();
            NewNearExit.x=(int)nearestSafeExit.x;
            NewNearExit.y=(int)nearestSafeExit.y;
            boolean change=getchange(OldSafeExit,NewNearExit);
            if(change==true) {

                View v1 = new MyCanvas(getApplicationContext(), firelocation, "GREEN", NewNearExit);
                Bitmap bitmap1 = Bitmap.createBitmap(762, 2000, Bitmap.Config.ARGB_8888);
                Canvas canvas1 = new Canvas(bitmap1);
                v1.draw(canvas1);
                iv.setImageBitmap(bitmap1);

                if (paths.Map.containsKey(userId)) {
                    List<String> exitPaths = paths.Map.get(userId).path;
                    if (nearestSafeExit.ExitId == 1) {
                        Toast.makeText(getBaseContext(), exitPaths.get(2), Toast.LENGTH_SHORT).show();

                    } else if (nearestSafeExit.ExitId == 2) {
                        Toast.makeText(getBaseContext(), exitPaths.get(1), Toast.LENGTH_SHORT).show();
                    } else if (nearestSafeExit.ExitId == 3) {
                        Toast.makeText(getBaseContext(), exitPaths.get(0), Toast.LENGTH_SHORT).show();
                    } else if (nearestSafeExit.ExitId == 4) {
                        Toast.makeText(getBaseContext(), exitPaths.get(3), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }


    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public double calculateDistance(double signalLevelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    static <K, V extends Comparable<? super V>>
    SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                return (e1.getValue()).compareTo(e2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    @Override
    protected void onPause() {
        this.unregisterReceiver(this.broadcastReceiver);
        super.onPause();


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    /*@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int topParam =  iv.getPaddingTop();
        int rightParam =  iv.getPaddingRight();
        int maxTopParam = topParam+iv.getMaxHeight();
        int maxRightParam = rightParam + iv.getMaxWidth();
        System.out.println(topParam);
        System.out.println(rightParam);
        System.out.println(maxRightParam);
        System.out.println(maxTopParam);
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
        }
        Toast.makeText(this, "x="+x+" y="+y, Toast.LENGTH_SHORT).show();


        return false;
    }

    */


    public void getnearestExit()
    {
        double min=10000.0;
        EInfo se=new EInfo();
        for(int i=0;i<einfo.length;i++) {
            if(einfo[i].x!=exitNearFire.x && einfo[i].y!=exitNearFire.y) {
                double xDiff = Math.abs(userlocation.x - einfo[i].x);
                double xSqr = Math.pow(xDiff, 2);

                double yDiff = Math.abs(userlocation.y - einfo[i].y);
                double ySqr = Math.pow(yDiff, 2);

                double output = Math.sqrt(xSqr + ySqr);
                if (output < min) {
                    min = output;
                    se = einfo[i];
                }
            }

            // System.out.println("Distance = " + output);
        }
        nearestSafeExit=se;
    }

    public void getExitNearFire()
    {
        double min=10000.0;
        EInfo nfe=new EInfo();
        for(int i=0;i<einfo.length;i++) {
            double xDiff =  Math.abs(firelocation.x- einfo[i].x);
            double xSqr = Math.pow(xDiff, 2);

            double yDiff =  Math.abs(firelocation.y- einfo[i].y);
            double ySqr = Math.pow(yDiff, 2);

            double output = Math.sqrt(xSqr + ySqr);
            if(output<min)
            {
                min=output;
                nfe=einfo[i];
            }

            System.out.println("Distance = " + output);
        }

        exitNearFire=nfe;

    }


    public void setFire()
    {
        fire=true;
        firelocation.x=360;
        firelocation.y=1800;
    }


    public void getExitDirection()
    {
        //if()
    }

    public boolean getchange(Coordinate old,Coordinate neww)
    {
        if(old.x==neww.x && old.y==neww.y)
        {
            return false;
        }
        return true;
    }
};
