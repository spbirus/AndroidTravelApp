package com.example.travel.travelapp;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final int REQ_CODE_GOOGLE_SIGNIN = 32767 / 2;

    private GoogleApiClient google;
    private FirebaseAuth mAuth;
    String neighborhood;
    String place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        if(intent.hasExtra("neighborhood") && intent.hasExtra("place")) {
            neighborhood = intent.getStringExtra("neighborhood");
            place = intent.getStringExtra("place");
        } else {
            neighborhood = "";
            place = "";
        }
        Log.v("neighborhood recd", "-->" + neighborhood);
        Log.v("place recd", "-->" + place);

        SignInButton button = (SignInButton) findViewById(R.id.sign_in_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInClick(v);
            }
        });

        // request the user's ID, email address, and basic profile
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // build API client with access to Sign-In API and options above
        google = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .addConnectionCallbacks(this)
                .build();



    }



    /*
     * This method is called when the Sign in with Google button is clicked.
     * It launches the Google Sign-in activity and waits for a result.
     */
    public void signInClick(View view) {
        //Toast.makeText(this, "Sign in was clicked!", Toast.LENGTH_SHORT).show();
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(google);
        startActivityForResult(intent, REQ_CODE_GOOGLE_SIGNIN);
    }






    /*
     * This method is called when Google Sign-in comes back to my activity.
     * We grab the sign-in results and display the user's name and email address.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQ_CODE_GOOGLE_SIGNIN) {
            // google sign-in has returned
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            if (result.isSuccess()) {
                // yay; user logged in successfully
                GoogleSignInAccount acct = result.getSignInAccount();
                Log.v("login", "success " + acct.getDisplayName() + " " +acct.getEmail());

                mAuth = FirebaseAuth.getInstance();
                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d("tag", "signInWithCredential", task.getException());
                            //Toast.makeText(LoginActivity.this, "Authentication failed.",
                            //        Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(LoginActivity.this, "Authentication Sucessful.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                final String username = acct.getDisplayName();
                //Check if user has firebase entry, add one if not
                DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
                DatabaseReference usertable = fb.child("users");
                usertable.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean found = false;
                        //create pathway to later check if places have been visited
                        if(!dataSnapshot.child(username).exists()){
                            dataSnapshot.child(username).child("neighborhoods").child("oakland").child("None").getRef().setValue("false");
                            dataSnapshot.child(username).child("neighborhoods").child("downtown").child("None").getRef().setValue("false");
                            dataSnapshot.child(username).child("neighborhoods").child("north side").child("None").getRef().setValue("false");
                        }

                        // connect to Google server to log in

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent startIntent = new Intent(this, MenuActivity.class );
                startIntent.putExtra("name",acct.getDisplayName());
                startIntent.putExtra("id",acct.getId());
                Log.d("Neighborhood", "-->" + neighborhood);
                Log.d("Place", "-->" + place);
                startIntent.putExtra("neighborhood", neighborhood);
                startIntent.putExtra("place", place);
                startActivityForResult(startIntent, 1);
            } else {
                Log.v("login", "failure");
            }
        }
    }



    // this method is required for the GoogleApiClient.OnConnectionFailedListener above
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("login", "onConnectionFailed");
    }

    // this method is required for the GoogleApiClient.ConnectionCallbacks above
    public void onConnected(Bundle bundle) {
        Log.v("login","onConnected");
    }

    // this method is required for the GoogleApiClient.ConnectionCallbacks above
    public void onConnectionSuspended(int i) {
        Log.v("login","onConnectionSuspended");
    }
}

