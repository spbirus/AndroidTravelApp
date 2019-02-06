package com.example.travel.travelapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private ArrayList<MarkerOptions> neighborhoodMarkers;
    private String username = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Create ArrayList of neighborhood markers
        neighborhoodMarkers = new ArrayList<MarkerOptions>();
        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
        DatabaseReference neighborhoodTable = fb.child("neighborhoods");

        neighborhoodTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data){
                //generate markers dynamically from fb data
                for(DataSnapshot neighborhood: data.getChildren()){
                    String neighborhoodCapitalized = neighborhood.getKey().substring(0, 1).toUpperCase() + neighborhood.getKey().substring(1);
                    neighborhoodMarkers.add(new MarkerOptions().position(new LatLng(neighborhood.child("lat").getValue(double.class),
                            neighborhood.child("long").getValue(double.class))).title(neighborhoodCapitalized));
                }
                //create map
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){
                //error
            }

        });

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMinZoomPreference(12);
        LatLng pgh = new LatLng(40.440625, -79.995886);
        googleMap.setOnMarkerClickListener(this);
        for(MarkerOptions marker : neighborhoodMarkers) { // Add the markers
            googleMap.addMarker(marker);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pgh));
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        String neighborhood = marker.getTitle().toLowerCase(); // Get the title from the marker to denote the neighborhood
        // Start Places Activity with the neighborhood information
        Intent intent = new Intent(this, PlacesActivity.class);
        intent.putExtra("neighborhood", neighborhood);
        startActivity(intent);

        return false;

    }

}