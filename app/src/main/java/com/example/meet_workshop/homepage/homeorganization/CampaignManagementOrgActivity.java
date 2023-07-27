package com.example.meet_workshop.homepage.homeorganization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.meet_workshop.R;

public class CampaignManagementOrgActivity extends AppCompatActivity {

    private ImageButton profileImageButton;

    //private  ImageButton searchButton;
    private ImageButton addEventButton;
    private ImageButton campaignManagementButton;
    //private ImageButton homePageButton;

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
        //homePageButton = findViewById(R.id.nav_home);
        addEventButton = findViewById(R.id.nav_addPost);
        //searchButton = findViewById(R.id.nav_search);
        campaignManagementButton= findViewById(R.id.nav_manage);
        campaignManagementButton.setClickable(false);

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

        /*searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearch();
            }
        });*/

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


        /*homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the videoPageButton click event
                openHomePage();
            }
        });*/

        /*campaignManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                openCampaignManagement();
            }
        });*/

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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    private void openCampaignAnalysis() {
        Intent intent = new Intent(CampaignManagementOrgActivity.this, CampaignAnalyticsOrgActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    private void openSubscriptionAnalysis() {
        Intent intent = new Intent(CampaignManagementOrgActivity.this, SubscriptionServiceOrgActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    private void openAddEventOrgActivity() {
        Intent intent = new Intent(this, AddEventOrgActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

/*    private void openCampaignManagement() {
        Intent intent = new Intent(this, CampaignManagementOrgActivity.class);
        startActivity(intent);

    }*/

   /* private void openSearch() {
        Intent intent = new Intent(this, SearchActivityOrg.class);
        startActivity(intent);
    }*/



    private void openUserProfile() {
        // Start the UserProfile activity
        Intent intent = new Intent(this, UserProfileOrgActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }


  /*  private void openHomePage() {
        // Start the UserProfile activity
        Intent intent = new Intent(this, HomeOrgActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }*/

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

}