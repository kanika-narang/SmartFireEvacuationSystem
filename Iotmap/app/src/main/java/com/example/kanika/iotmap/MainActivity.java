package com.example.kanika.iotmap;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import android.graphics.Bitmap;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

//import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

//to show the fire/exit locations on the map
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
    RouterInformation UserRouterLocation=new RouterInformation();
    int userId=0;
    Coordinate firelocation=new Coordinate();
    EInfo exitNearFire=new EInfo();
    EInfo nearestSafeExit=new EInfo();
    Coordinate OldSafeExit=new Coordinate();
    boolean fire=false;
    boolean notifiedaboutfire=false;
    int userandroidid=0;
    int countofalarm=0;

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
        mediaPlayer= MediaPlayer.create(this, R.raw.firealarm);

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

        System.out.println("Android Id............" + android_id);
        System.out.println("userid ......................"+Math.abs(android_id.hashCode()/100000));
        userandroidid=Math.abs(android_id.hashCode()/10000);
        senduserdata();
        System.out.println("sent//////////////////");
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

                                    userlocation.x = rInfo[i].lattitude;
                                    userlocation.y = rInfo[i].longitude;
                                    userId = rInfo[i].id;
                                    UserRouterLocation = rInfo[i];
                                    System.out.println(userlocation.x + " " + userlocation.y);
                                    // Toast.makeText(getBaseContext(), "Room:"+rInfo[i].roomNumber+" x="+rInfo[i].lattitude+" y= "+ rInfo[i].longitude, Toast.LENGTH_SHORT).show();

                                    //Check for internet connection and Insert into mysql database


                                    break;
                                }
                            }
                            if (BSSIDFound == false) {
                                System.out.println("\n NO LOCATION INFORMATION AVAILABLE BUT YOU ARE CONNECTED TO INTERNET!! \n ");
                                System.out.println(userlocation.x + " " + userlocation.y);
                                userlocation.x = 0;
                                userlocation.y = 0;
                                System.out.println(userlocation.x + " " + userlocation.y);
                            final Toast    t2 = Toast.makeText(getBaseContext(), "No Location Found", Toast.LENGTH_SHORT);
                                t2.show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        t2.cancel();
                                    }
                                }, 1000);
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
        System.out.println("FIREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
        setFire();
        if (fire == true) {

            System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
            // If fire is true turn on the alarm
            if (notifiedaboutfire == false) {
                notify(this, 1, "FIRE", "Please Evacuate the building", null);

                mediaPlayer.start();
                notifiedaboutfire = true;
            } else {
                if (countofalarm == 500) {
                    mediaPlayer.stop();
                } else {
                    countofalarm++;
                }

            }
            //get the nearest fire exit location
            getExitNearFire();
            // get  safest nearest location
            getnearestExit();
            NewNearExit.x = (int) nearestSafeExit.x;
            NewNearExit.y = (int) nearestSafeExit.y;
            boolean change = getchange(OldSafeExit, NewNearExit);
            System.out.println(change);
            if (change == true) {

                View v1 = new MyCanvas(getApplicationContext(), firelocation, "GREEN", NewNearExit);
                Bitmap bitmap1 = Bitmap.createBitmap(762, 2000, Bitmap.Config.ARGB_8888);
                Canvas canvas1 = new Canvas(bitmap1);
                v1.draw(canvas1);
                iv.setImageBitmap(bitmap1);
                final Toast t1;
                if (paths.Map.containsKey(userId)) {
                    List<String> exitPaths = paths.Map.get(userId).path;
                    if (nearestSafeExit.ExitId == 1) {
                        t1 = Toast.makeText(getBaseContext(), "User Loaction " + UserRouterLocation.roomNumber + " Nearest Exit " + exitPaths.get(2), Toast.LENGTH_SHORT);
                        t1.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t1.cancel();
                            }
                        }, 1500);
                        //   notifylocation(this,1,"Location","User Loaction "+UserRouterLocation.roomNumber+" Nearest Exit "+exitPaths.get(2),null);

                    } else if (nearestSafeExit.ExitId == 2) {
                        t1 = Toast.makeText(getBaseContext(), "User Loaction " + UserRouterLocation.roomNumber + " Nearest Exit " + exitPaths.get(1), Toast.LENGTH_SHORT);
                        t1.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t1.cancel();
                            }
                        }, 1500);
                        //  notifylocation(this,1,"Location","User Loaction "+UserRouterLocation.roomNumber+" Nearest Exit "+exitPaths.get(1),null);
                    } else if (nearestSafeExit.ExitId == 3) {
                        t1 = Toast.makeText(getBaseContext(), "User Loaction " + UserRouterLocation.roomNumber + " Nearest Exit " + exitPaths.get(0), Toast.LENGTH_SHORT);
                        t1.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t1.cancel();
                            }
                        }, 1500);
                        // notifylocation(this,1,"Location","User Loaction "+UserRouterLocation.roomNumber+" Nearest Exit "+exitPaths.get(0),null);
                    } else if (nearestSafeExit.ExitId == 4) {
                        t1 = Toast.makeText(getBaseContext(), "User Loaction " + UserRouterLocation.roomNumber + " Nearest Exit " + exitPaths.get(3), Toast.LENGTH_SHORT);
                        t1.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t1.cancel();
                            }
                        }, 1500);
                        //  notifylocation(this,1,"Location","User Loaction "+UserRouterLocation.roomNumber+" Nearest Exit "+exitPaths.get(3),null);
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
        final JSONObject json1 = new JSONObject();
        try {
            json1.put("num_records","1");
            json1.put("sensor_token", "bzr4f033oy97md7e");
            json1.put("device_token", "sya0c85x69fwl9ol");
        }catch (Exception e) {
            System.out.println("error " + e);
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://theiotdashboard.tk/api/getsensordata/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // your response
                        try{
                            //JSONParser parser_obj = new JSONParser();
                            System.out.println("Hi");

                            System.out.println("Done...........................");
                            //  System.out.println(json);
                            System.out.println("--------------------------");
                            System.out.println(response);
                            System.out.println("--------------------------");

                            String data_json=response.substring(1,response.length()-1);
                            String st=data_json.replace("\\","");

                            JSONObject newjson=new JSONObject(st);
                            System.out.println("Done..............."+newjson);
                            org.json.JSONArray ja=newjson.getJSONArray("data");
                            System.out.println(ja);

                            org.json.JSONArray ja0= ja.getJSONArray(0);
                            System.out.println("JSONARRAY.........."+ja0);
                            String jaFire=ja0.getString(0);
                            System.out.println("Fire......."+jaFire);
                            if(jaFire.equals("1")){fire=true;}else{fire=false;}
                            System.out.println("fffffffffffffff "+fire);



                        }
                        catch(Exception error){
                            System.out.println("error.........."+error);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
            }
        })
        {
            @Override
            public byte[] getBody() throws AuthFailureError {
                //   String your_string_json = ; // put your json
                return json1.toString().getBytes();
            }

            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.start();

        firelocation.x=360;
        firelocation.y=1800;
    }



   /* public void setFire()
    {
        final JSONObject json1 = new JSONObject();


        try {
            *//*json1.put("userid", "37");
            json1.put("x", "10.11");
            json1.put("y", "11.12");
            *//*
            json1.put("num_records","1");
            json1.put("sensor_token", "bzr4f033oy97md7e");
            json1.put("device_token", "sya0c85x69fwl9ol");
            *//*json1.put("latitude","1");
            json1.put("longitude", "bzr4f033oy97md7e");
            json1.put("destination", "sya0c85x69fwl9ol");
        *//*} catch (Exception e) {
            System.out.println("error " + e);

        }
        RequestQueue queue = Volley.newRequestQueue(this);
       String url ="http://theiotdashboard.tk/api/getsensordata/";
       // String url="http://139.59.33.166:8080/JSONServlet";
        System.out.println(json1.toString());
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // your response
                        try{
                        //JSONParser parser_obj = new JSONParser();
                            System.out.println("Hi");
                           *//* String temp = "{\"header\": [\"Fire\", \"ServerTimestamp\"], \"data\": [[\"1\", \"2017/04/10 21:45:46\"]], \"error\": {}}";
                            System.out.println(temp);*//*
                           // JSONObject json = (JSONObject) new JSONParser().parse(response);

                            System.out.println("Done...........................");
                          //  System.out.println(json);
                        System.out.println("--------------------------");
                        System.out.println(response);
                        System.out.println("--------------------------");
                          *//*  String  ss="{\"header\": [\"Fire\", \"ServerTimestamp\"],\"data\": [[\"1\", \"2017/04/10 21:45:46\"]], \"error\": {}}";
                           // String ss=response;
                            JSONObject obj = new JSONObject(ss);
                            System.out.println(" bbbbbbbbbbbbbbbbb"+obj);
*//*
                           *//* System.out.println("..................."+ss.charAt(0)+"..........."+ss.charAt(ss.length()-1));
                            String result=ss.substring(1,ss.length()-1);
                            System.out.println("qqqqqqqqqqqqqqqqqqqqqqqq"+result);
                           *//* //JSONObject obj2 = new JSONObject(result);
                            System.out.println(" bbbbbbbbbbbbbbbbb"+obj2);
                            *//*org.json.JSONArray arr=new org.json.JSONArray(obj.getString("data"));
                            int firereading=Integer.parseInt(arr.get(0).toString());
                            if(firereading==1){fire=true;}else{fire=false;}
                            System.out.println("------fire--------------------");
                            System.out.println(firereading);
                            *//*System.out.println("-----------fire---------------");
                        }
                        catch(Exception error){
                            System.out.println("error.........."+error);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                //   String your_string_json = ; // put your json
                return json1.toString().getBytes();
            }

            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.start();

        firelocation.x=360;
        firelocation.y=1800;
    }
*/

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




    public static void notify(Context context, int id, String titleResId, String textResId, PendingIntent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String title = titleResId;
        String text = textResId;

        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.image)
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setTicker(title)
                .setContentIntent(intent);
        notificationManager.notify(id, builder.build());
    }

    public  void senduserdata()
    {
        String sensor_token="m5luoy1i59r97wlo";
        String device_token="undgkdo1moudqkpc";
        String url="http://theiotdashboard.tk/api/send/";
        final JSONObject json1 = new JSONObject();
        try {
            json1.put("userid",userandroidid);
            json1.put("x","1");
            json1.put("y","1");
            json1.put("sensor_token", sensor_token);
            json1.put("device_token", device_token);
        }catch (Exception e) {
            System.out.println("error " + e);
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // your response
                        try{
                            //JSONParser parser_obj = new JSONParser();
                            System.out.println("Hi");

                            System.out.println("Done...........................");
                            //  System.out.println(json);
                            System.out.println("--------------------------");
                            System.out.println(response);
                            System.out.println("--------------------------");

                           /* String data_json=response.substring(1,response.length()-1);
                            String st=data_json.replace("\\","");

                            JSONObject newjson=new JSONObject(st);
                            System.out.println("Done..............."+newjson);
                            org.json.JSONArray ja=newjson.getJSONArray("data");
                            System.out.println(ja);

                            org.json.JSONArray ja0= ja.getJSONArray(0);
                            System.out.println("JSONARRAY.........."+ja0);
                            String jaFire=ja0.getString(0);
                            System.out.println("Fire......."+jaFire);*/



                        }
                        catch(Exception error){
                            System.out.println("error.........."+error);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
            }
        })
        {
            @Override
            public byte[] getBody() throws AuthFailureError {
                //   String your_string_json = ; // put your json
                return json1.toString().getBytes();
            }

            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        queue.start();

    }

}
