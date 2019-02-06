package com.example.travel.travelapp;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MenuActivity extends AppCompatActivity {
    private static String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        String neighborhood = "";
        String place = "";

        Intent i = getIntent();

        // Check to see if the intent included a neighborhood & place to add
        if (i.hasExtra("neighborhood") && i.hasExtra("place")) {
            neighborhood = i.getStringExtra("neighborhood");
            place = i.getStringExtra("place");
            if (!neighborhood.equals("") && !place.equals("")) {
                // PLACE & NEIGHBORHOOD RECEIVED FROM BROADCAST RECEIVER, ADD IT TO THE DATABASE & DISPLAY MESSAGE
                DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
                fb.child("neighborhoods").child(neighborhood).child("places").child(place).setValue(true);
                Toast.makeText(this, place + " added to the " + neighborhood + " neighborhood!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void clickMapButton(View view) {
        Intent mapIntent = new Intent(this, MapActivity.class);
        startActivity(mapIntent);
    }

    public void clickBucketButton(View view) {
        Intent bucketIntent = new Intent(this, BucketListActivity.class);
        startActivity(bucketIntent);
    }

    public void clickProfileButton(View view) {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        startActivity(profileIntent);
    }

    public void clickBroadcastButton(View view) {
        Intent finishedIntent = new Intent("edu.pitt.cs1699.stocks.BALANCE");
        sendBroadcast(finishedIntent);
    }

    public void clickCheckStockPrice(View view) {
        Intent intent = new Intent("edu.pitt.cs1699.stocks.StockPriceService");
        intent.setComponent(new ComponentName("edu.pitt.cs1699.stocks", "edu.pitt.cs1699.stocks.StockPriceService"));
        startService(intent);
    }

    public void clickScoreButton(View view) {
        Intent scoreIntent = new Intent(this, ScoreActivity.class);
        startActivity(scoreIntent);
    }

}
