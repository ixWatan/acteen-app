package com.example.meet_workshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.meet_workshop.homepage.homeorganization.ChooseLocationActivity;


public class StartEveryThing extends AppCompatActivity {


    private Button Organization;

    private Button Activist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_every_thing);


        Organization = findViewById(R.id.Organization);
        Organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ChooseLocationActivity
                openOrganizationFragments();
            }
        });

        Activist = findViewById(R.id.Activist);
        Activist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ChooseLocationActivity
                openActivistFragments();
            }
        });

    }

    private void openActivistFragments() {
        ActivistFragment1 fragment = new ActivistFragment1();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openOrganizationFragments() {
        OnboardingFragment1 fragment = new OnboardingFragment1();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}