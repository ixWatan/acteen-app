package com.example.meet_workshop.homepage.homeorganization;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {


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


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialize the Places API
        Places.initialize(requireContext(), "AIzaSyAEBeBv-EtGsHmSq9CNC6qfuEv6mTH0YH0");

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query here
                // For example, you can start the place autocomplete activity
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .setInitialQuery(query)
                        .build(requireActivity());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // You can also handle changes in the search text here if necessary
                return false;
            }
        });


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
            String locationAddress = getAddressFromLatLng(selectedLocation);
            Toast.makeText(requireContext(), "Selected Location: " + locationAddress, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(requireActivity(), AddEventOrgActivity.class);

            // Add the selected location address as extra data
            intent.putExtra("SelectedLocation", locationAddress);
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

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            return add;
        } catch (IOException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }

}
