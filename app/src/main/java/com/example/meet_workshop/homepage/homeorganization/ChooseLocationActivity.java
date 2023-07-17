package com.example.meet_workshop.homepage.homeorganization;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.SupportMapFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meet_workshop.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChooseLocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {

    double initialLatitude = 0; // Replace with the latitude of your initial location
    double initialLongitude = 0; // Replace with the longitude of your initial location
    float DEFAULT_ZOOM = 12f; // Replace with your desired default zoom level

    //store the selected location:
    private LatLng selectedLocation;

    private GoogleMap mMap;
    private Marker selectedMarker;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        // Inside your onCreate method or initialization method
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLocation();
            }
        });

        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLocation();
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at the initial location
        LatLng initialLocation = new LatLng(initialLatitude, initialLongitude);
        mMap.addMarker(new MarkerOptions().position(initialLocation));

        // Set the camera position to the initial location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, DEFAULT_ZOOM));

        // Set a click listener on the map to allow the user to select a location
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Clear any existing markers
                mMap.clear();

                // Add a marker at the clicked location
                mMap.addMarker(new MarkerOptions().position(latLng));

                // Update the selectedLocation variable
                selectedLocation = latLng;
            }
        });
    }



    @Override
    public void onMapClick(LatLng latLng) {
        if (selectedMarker != null) {
            selectedMarker.remove();
        }

        // Add a marker at the clicked location and enable dragging
        selectedMarker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));

        // Show the confirm button
        confirmButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        // No implementation needed
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        // No implementation needed
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // No implementation needed
    }

    private void confirmLocation() {
        if (selectedLocation != null) {
            // Create an intent to return the selected location to the previous activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedLocation", selectedLocation);

            // Set the result and finish the activity
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        } else {
            // Handle the case when the user hasn't selected a location
            Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show();
        }
    }

}
