package com.example.meet_workshop.homepage.homeorganization;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.SupportMapFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.meet_workshop.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.libraries.places.api.model.Place;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class ChooseLocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {


    private LatLng selectedLocation;

    private AutoCompleteTextView placeAutoCompleteTextView;
    private Button searchButton;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private PlacesClient placesClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private GoogleMap mMap;
    private Marker selectedMarker;
    private Button confirmButton;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);


        ChooseLocationActivity chooseLocationActivity = new ChooseLocationActivity();
        LatLng selectedLocation = chooseLocationActivity.getSelectedLocation();

        // Initialize the Places API
        Places.initialize(getApplicationContext(), "AIzaSyD48qyP0H6YM9-TezNQYys-n6i5TUgf3pY");

        // Create a PlacesClient instance
        placesClient = Places.createClient(this);

        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLocation();
            }
        });
        placeAutoCompleteTextView = findViewById(R.id.placeAutoCompleteTextView);
        searchButton = findViewById(R.id.searchButton);

        // Set click listener for the Search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the autocomplete activity
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .build(ChooseLocationActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check location permission and initialize the map if granted
        if (checkLocationPermission()) {
            initializeMap();
        } else {
            requestLocationPermission();
        }
    }

    private boolean checkLocationPermission() {
        int permissionState = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerDragListener(this);

        // Enable the My Location button
        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
                }
            });
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (selectedMarker != null) {
            selectedMarker.remove();
        }

        // Add a marker at the clicked location and enable dragging
        selectedMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));

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
        if (selectedMarker != null) {
            selectedLocation = selectedMarker.getPosition();
            // Handle the confirmed location
            String locationString = "Selected Location: " + selectedLocation.latitude + ", " + selectedLocation.longitude;
            Toast.makeText(this, locationString, Toast.LENGTH_SHORT).show();
        }
    }


    public LatLng getSelectedLocation() {
        return selectedLocation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if (place != null) {
                    // Handle the selected place
                    LatLng selectedLocation = place.getLatLng();
                    // Update the map and marker
                    // ...
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                if (status != null) {
                    Toast.makeText(this, "Place search error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
