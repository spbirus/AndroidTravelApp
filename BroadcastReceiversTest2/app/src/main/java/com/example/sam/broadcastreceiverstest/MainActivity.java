package com.example.sam.broadcastreceiverstest;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String storeName = "Walgreens";
    String latitude = "40.442279";
    String longitude = "-80.002267";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void broadcastClick(View view){
        String p = "com.example.travel.travelapp";
        Toast.makeText(this,p, Toast.LENGTH_LONG);
        Log.v("broadcast", p);
        Intent intent = new Intent();
        intent.setAction("com.example.travel.travelapp.DISCONNECT");
        intent.setClassName(p, p+".MyReceiver");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra("storeInfo", storeName + "/" + latitude + "/" + longitude);
        sendBroadcast(intent);
    }

    public void intentClick1(View view){
        intent1("Oreos", 3.99);
    }

    public void intentClick2(View view){
        intent2("dairy");
    }

    private void intent1(String item, double price){
        Log.v("intent1", "this");
        Intent finishedIntent = new Intent("com.example.travel.travelapp.ScoreActivity");
        finishedIntent.putExtra("foodItem", item);
        finishedIntent.putExtra("price", price);
        startActivity(finishedIntent);
    }

    private void intent2(String randomFoodCategory){
        Log.v("intent2", "this");
        Intent finishedIntent = new Intent("com.example.travel.travelapp.ProfileActivity");
        finishedIntent.putExtra("foodCategory", randomFoodCategory);
        startActivity(finishedIntent);
    }

    public void serviceClick(View view) {
        Random rand = new Random();
        int randomNum = rand.nextInt(101);
        Intent intent = new Intent("com.example.travel.travelapp.ScoreService");
        intent.setComponent(new ComponentName("com.example.travel.travelapp", "com.example.travel.travelapp.ScoreService"));
        intent.putExtra("setScore", randomNum);
        startService(intent);
    }
}
