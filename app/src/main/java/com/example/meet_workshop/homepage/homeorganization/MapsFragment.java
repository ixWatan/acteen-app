package com.example.meet_workshop.homepage.homeorganization;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {
    private AutoCompleteTextView placeAutoCompleteTextView;
    private Button searchButton;
    private PlacesClient placesClient;
    private GoogleMap mMap;
    private Marker selectedMarker;
    private Button confirmButton;
    private FusedLocationProviderClient fusedLocationClient;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLocation();
            }
        });

        placeAutoCompleteTextView = view.findViewById(R.id.placeAutoCompleteTextView);
        placeAutoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            Place place = (Place) parent.getItemAtPosition(position);
            if (place != null) {
                LatLng selectedLocation = place.getLatLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15f));
                if (selectedMarker != null) {
                    selectedMarker.remove();
                }
                selectedMarker = mMap.addMarker(new MarkerOptions().position(selectedLocation).draggable(true));
                confirmButton.setVisibility(View.VISIBLE);
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialize the Places API
        Places.initialize(requireContext(), "AIzaSyAEBeBv-EtGsHmSq9CNC6qfuEv6mTH0YH0");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerDragListener(this);

        // Enable the My Location button
        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
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

    private boolean checkLocationPermission() {
        int permissionState = requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void confirmLocation() {
        if (selectedMarker != null) {
            LatLng selectedLocation = selectedMarker.getPosition();
            // Handle the confirmed location
            String locationString = "Selected Location: " + selectedLocation.latitude + ", " + selectedLocation.longitude;
            Toast.makeText(requireContext(), locationString, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(requireActivity(), AddEventOrgActivity.class);

            // Add the selected location as extra data
            intent.putExtra("SelectedLat", selectedLocation.latitude);
            intent.putExtra("SelectedLng", selectedLocation.longitude);

            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkLocationPermission()) {
                    mMap.setMyLocationEnabled(true);
                    fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
                        }
                    });
                }
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                if (place != null) {
                    LatLng selectedLocation = place.getLatLng();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15f));
                    if (selectedMarker != null) {
                        selectedMarker.remove();
                    }
                    selectedMarker = mMap.addMarker(new MarkerOptions().position(selectedLocation).draggable(true));
                    confirmButton.setVisibility(View.VISIBLE);
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                if (status != null) {
                    Toast.makeText(requireContext(), "Place search error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
