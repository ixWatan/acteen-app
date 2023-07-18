package com.example.meet_workshop.homepage.homeorganization;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meet_workshop.R;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChooseLocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private PlacesClient placesClient;

    private LatLng selectedLocation;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private EditText locationSearchEditText;

    private GoogleMap mMap;
    private Marker selectedMarker;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        locationSearchEditText = findViewById(R.id.locationBtn);
        locationSearchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performLocationSearch();
                return true;
            }
            return false;
        });

        // Initialize the Places API
        Places.initialize(getApplicationContext(), "AIzaSyD48qyP0H6YM9-TezNQYys-n6i5TUgf3pY");

        // Create a PlacesClient instance
        placesClient = Places.createClient(this);







        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (checkLocationPermission()) {
            initializeMap();
        } else {
            requestLocationPermission();
        }


    }

    private boolean checkLocationPermission() {
        int permissionState = PackageManager.PERMISSION_GRANTED;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionState = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }
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

        selectedMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
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
            String locationString = "Selected Location: " + selectedLocation.latitude + ", " + selectedLocation.longitude;
            Toast.makeText(this, locationString, Toast.LENGTH_SHORT).show();
        }
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
                    // Update the map and marker accordingly
                    // ...

                    // Example: Move the camera to the selected location
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15f));
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                if (status != null) {
                    Toast.makeText(this, "Place search error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void performLocationSearch() {
        String searchQuery = locationSearchEditText.getText().toString().trim();

        // Set up the fields to be retrieved for the autocomplete results
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Set up the intent for the autocomplete activity
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .setCountries(Collections.singletonList("YOUR_COUNTRY_CODE"))
                .build(this);

        // Start the autocomplete activity
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }


}
