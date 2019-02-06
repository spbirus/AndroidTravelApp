package com.example.travel.travelapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    String username = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();

        String foodCategory = "";

        foodCategory = intent.getStringExtra("foodCategory");
        Log.v("food category", ""+foodCategory);
        if(foodCategory != null)
            Toast.makeText(this, "Received the category: "+foodCategory,Toast.LENGTH_LONG).show();

        username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usertable = fb.child("users").child(username);

        // If getting setScore from Broadcast Receiver
        if(intent.hasExtra("setScore")) {
            int score = intent.getIntExtra("setScore", 0);
            Log.d("ProfileAct", "Score: " + score);
            usertable.child("score").setValue(score);
        }

        usertable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView name = (TextView) findViewById(R.id.name);
                TextView bio = (TextView) findViewById(R.id.bio);
                TextView score = (TextView) findViewById(R.id.score);
                name.setText(username);
                if(dataSnapshot.child("bio").exists()){
                    bio.setText(dataSnapshot.child("bio").getValue(String.class));
                } else {
                    bio.setText("Fill in your bio!");
                }
                if(dataSnapshot.child("score").exists()){
                    Log.v("dataSnapshot", dataSnapshot.toString());
                    String points = dataSnapshot.child("score").getValue(Long.class).toString();
                    Log.v("score", points);
                    score.setText(points);
                } else {
                    bio.setText("0 pts");
                    dataSnapshot.child("score").getRef().setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void clickEditProfileButton(View view){
        Intent profileIntent = new Intent(this, EditProfileActivity.class);
        profileIntent.putExtra("name", username);
        startActivity(profileIntent);
    }
}
