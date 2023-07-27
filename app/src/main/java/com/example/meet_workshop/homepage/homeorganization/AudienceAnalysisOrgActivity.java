package com.example.meet_workshop.homepage.homeorganization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.meet_workshop.R;

public class AudienceAnalysisOrgActivity extends AppCompatActivity {

    private ImageButton profileImageButton;
    private ImageButton addEventButton;
    private ImageButton campaignManagementButton;

    private ImageButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audience_analysis_org);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profileImageButton = findViewById(R.id.nav_profile);
        campaignManagementButton = findViewById(R.id.nav_manage);
        addEventButton = findViewById(R.id.nav_addPost);
        //searchButton = findViewById(R.id.nav_search);

        ImageButton NavButton = (ImageButton) this.findViewById(R.id.nav_manage);
        NavButton.setColorFilter(Color.rgb(0,0,0)); // Yellow Tint


        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                openUserProfile();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}