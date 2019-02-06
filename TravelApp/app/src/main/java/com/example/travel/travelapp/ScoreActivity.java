package com.example.travel.travelapp;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        String foodItem = "test";
        double price = 0;

        Intent i = getIntent();

        foodItem = i.getStringExtra("foodItem");
        price = i.getDoubleExtra("price", 0);

        EditText item = (EditText) findViewById(R.id.editText1);
        EditText p = (EditText) findViewById(R.id.editText2);
        String pr = Double.toString(price);
        item.setText(foodItem);
        p.setText(pr);
    }

    public void onClickAddName(View view) {
        // add a new score
        ContentValues values = new ContentValues();
        values.put(MyContentProvider.NAME,
                ((EditText)findViewById(R.id.editText1)).getText().toString());

        values.put(MyContentProvider.GRADE, ((EditText)findViewById(R.id.editText2)).getText().toString());

        Uri uri = getContentResolver().insert(MyContentProvider.CONTENT_URI, values);

        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
    }

    public void onClickMenu(View view){
        Intent menuIntent = new Intent(this, MenuActivity.class);
        startActivity(menuIntent);
    }

    public void onClickSignIn(View view){
        Intent signIntent = new Intent(this, LoginActivity.class);
        startActivity(signIntent);
    }
}
