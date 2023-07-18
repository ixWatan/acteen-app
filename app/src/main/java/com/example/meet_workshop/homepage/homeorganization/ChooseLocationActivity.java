package com.example.meet_workshop.homepage.homeorganization;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.maps.SupportMapFragment;
import com.example.meet_workshop.R;
import com.google.android.gms.maps.MapFragment;

public class ChooseLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        // Get a reference to the FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin a FragmentTransaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Create an instance of your existing MapFragment
        SupportMapFragment mapFragment = new SupportMapFragment();

        // Replace the content of the mapContainer with the mapFragment
        fragmentTransaction.replace(R.id.fragment_container, mapFragment);

        // Commit the FragmentTransaction
        fragmentTransaction.commit();
    }
}

