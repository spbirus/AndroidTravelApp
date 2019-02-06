package com.example.travel.travelapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by burri on 3/28/2018.
 */

public class MyReceiver extends BroadcastReceiver {

    private final static String TAG = MyReceiver.class.getSimpleName();

    public MyReceiver() {
        Log.d(TAG, "==========================================");
        Log.d(TAG, "MyReceiver: "+MyReceiver.class.getCanonicalName());
        Log.d(TAG, "==========================================");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "==========================================");
        Log.d(TAG, "onReceive: ");
        Log.d(TAG, "==========================================");

        String storeInfo = intent.getStringExtra("storeInfo");
        String[] info = storeInfo.split("/"); //[0] is name, [1] is latitude, [2] is longitude

        // Pick a neighborhood based off of latitude and longitude
        double lon = Double.parseDouble(info[2]);
        double lat = Double.parseDouble(info[1]);
        Log.v("longitude", lon + "");
        Log.v("latitutde", lat + "");
        String neighborhood = "";
        // Figured out these coordinates based on https://www.latlong.net
        if(lon < -79.973420) {
            if(lat > 40.444463) {
                // north side
                neighborhood = "northside";
            } else {
                // downtown
                neighborhood = "downtown";
            }
        } else {
            // oakland
            neighborhood = "oakland";
        }


        Intent i = new Intent(context, LoginActivity.class);
        i.putExtra("neighborhood", neighborhood);
        i.putExtra("place", info[0]);
        context.startActivity(i);


    }
}
