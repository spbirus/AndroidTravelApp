package com.example.travel.travelapp;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BucketListActivity extends AppCompatActivity {

    String username = "";
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);
        Intent intent = getIntent();
        username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        ll = findViewById(R.id.checkBoxLayout);

        makeList();

    }

    public void addBucket(View view){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add New Activity");
        alert.setMessage("Bucket List Activity");
        final DatabaseReference fb = FirebaseDatabase.getInstance().getReference();

        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                fb.child("users").child(username).child("activities").child(input.getText().toString());
                fb.child("users").child(username).child("activities").child(input.getText().toString()).setValue(false);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        AlertDialog builder = alert.create();
        builder.show();
        makeList();

    }

    private void makeList(){

        // Loops through each place
        DatabaseReference fb = FirebaseDatabase.getInstance().getReference().child("users").child(username).child("activities");
        //event listeners
        fb.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot data){
                ll.removeAllViews();
                for(final DataSnapshot activity: data.getChildren()){
                    CheckBox cb = new CheckBox(getApplicationContext());
                    cb.setText(activity.getKey());
                    cb.setTextColor(Color.BLACK);
                    Log.v("Activity done?", "-" + activity.getValue().toString());
                    if(activity.getValue().toString().equals("true")) {
                        cb.setChecked(true);
                    } else {
                        cb.setChecked(false);
                    }

                    cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                completedActivity(activity.getKey());
                                //buy shares
                                Toast.makeText(BucketListActivity.this, "Buy shares", Toast.LENGTH_LONG).show();
                                try {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.setClassName("edu.pitt.cs1699.stocks", "edu.pitt.cs1699.stocks.BuyShares");
                                    startActivity(intent);
                                } catch(ActivityNotFoundException e) {
                                    Log.d("BucketList", "Stocks activity not found");
                                }
                            } else{
                                unCompletedActivity(activity.getKey());
                                Toast.makeText(BucketListActivity.this, "Sell shares", Toast.LENGTH_LONG).show();
                                try {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.setClassName("edu.pitt.cs1699.stocks", "edu.pitt.cs1699.stocks.SellShares");
                                    startActivity(intent);
                                } catch(ActivityNotFoundException e) {
                                    Log.d("BucketList", "Stocks activity not found");
                                }
                            }
                        }
                    });
                    ll.addView(cb);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });

    }

    private void completedActivity(String activity){
        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
        fb.child("users").child(username).child("activities").child(activity).setValue(true);
    }

    private void unCompletedActivity(String activity){
        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
        fb.child("users").child(username).child("activities").child(activity).setValue(false);
    }

}