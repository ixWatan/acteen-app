package com.example.meet_workshop.homepage.homeorganization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.meet_workshop.R;

public class CampaignManagementOrgActivity extends AppCompatActivity {

    private ImageButton profileImageButton;

    private  ImageButton searchButton;
    private ImageButton addEventButton;
    private ImageButton campaignManagementButton;
    private ImageButton homePageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //management view stuff

        /*audienceAnalysis  = findViewById(R.id.audience_analysis);
        campaignAnalysis = findViewById(R.id.campaign_analysis);
        subscriptionAnalysis = findViewById(R.id.subscripton_service);
*/



        setContentView(R.layout.activity_campaign_management_org);
        // Navbar organization stuff 5 icons
        profileImageButton = findViewById(R.id.nav_profile);
        homePageButton = findViewById(R.id.nav_home);
        campaignManagementButton = findViewById(R.id.nav_manage);
        addEventButton = findViewById(R.id.nav_addPost);
        searchButton = findViewById(R.id.nav_search);

        RelativeLayout audienceAnalysis = (RelativeLayout) this.findViewById(R.id.audience_analysis);
        RelativeLayout campaignAnalysis = (RelativeLayout) this.findViewById(R.id.campaign_analysis);
        RelativeLayout subscriptionAnalysis = (RelativeLayout) this.findViewById(R.id.subscripton_service);


        ImageButton NavButton = (ImageButton) this.findViewById(R.id.nav_manage);
        NavButton.setColorFilter(Color.rgb(0,0,0)); // Yellow Tint

        audienceAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                openAudienceAnalysis();
            }
        });

        campaignAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                openCampaignAnalysis();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearch();
            }
        });

        subscriptionAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                openSubscriptionAnalysis();
            }
        });


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

    private void openAudienceAnalysis() {
        Intent intent = new Intent(CampaignManagementOrgActivity.this, AudienceAnalysisOrgActivity.class);
        startActivity(intent);
    }

    private void openCampaignAnalysis() {
        Intent intent = new Intent(CampaignManagementOrgActivity.this, CampaignAnalyticsOrgActivity.class);
        startActivity(intent);

    }

    private void openSubscriptionAnalysis() {
        Intent intent = new Intent(CampaignManagementOrgActivity.this, SubscriptionServiceOrgActivity.class);
        startActivity(intent);

    }

    private void openAddEventOrgActivity() {
        Intent intent = new Intent(this, AddEventOrgActivity.class);
        startActivity(intent);
    }

    private void openCampaignManagement() {
        Intent intent = new Intent(this, CampaignManagementOrgActivity.class);
        startActivity(intent);
    }

    private void openSearch() {
        Intent intent = new Intent(this, SearchActivityOrg.class);
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