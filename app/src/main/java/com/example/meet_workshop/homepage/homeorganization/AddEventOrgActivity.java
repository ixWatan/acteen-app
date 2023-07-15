package com.example.meet_workshop.homepage.homeorganization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.meet_workshop.R;
import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.example.meet_workshop.homepage.homeactivist.UserProfile;

public class AddEventOrgActivity extends AppCompatActivity {
    private ImageButton profileImageButton;
    private ImageButton addEventButton;
    private ImageButton campaignManagementButton;
    private ImageButton homePageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_org);
        profileImageButton = findViewById(R.id.nav_profile);
        homePageButton = findViewById(R.id.nav_home);
        campaignManagementButton = findViewById(R.id.nav_manage);
        addEventButton = findViewById(R.id.nav_addPost);

        ImageButton NavButton = (ImageButton) this.findViewById(R.id.nav_addPost);
        NavButton.setColorFilter(Color.rgb(255, 223, 54)); // Yellow Tint


        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                openUserProfile();
            }
        });


        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the videoPageButton click event
                openHomePage();
            }
        });

        campaignManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                openCampaignManagement();
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                openAddEventOrgActivity();
            }
        });

    }

    private void openAddEventOrgActivity() {
        Intent intent = new Intent(this, AddEventOrgActivity.class);
        startActivity(intent);
    }

    private void openCampaignManagement() {
        Intent intent = new Intent(this, CampaignManagementOrgActivity.class);
        startActivity(intent);
    }


    private void openUserProfile() {
        // Start the UserProfile activity
        Intent intent = new Intent(this, UserProfileOrgActivity.class);
        startActivity(intent);
    }


    private void openHomePage() {
        // Start the UserProfile activity
        Intent intent = new Intent(this, HomeOrgActivity.class);
        startActivity(intent);
    }

}