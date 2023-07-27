package com.example.meet_workshop.homepage.homeorganization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.meet_workshop.R;

public class SubscriptionServiceOrgActivity extends AppCompatActivity {

    private ImageButton backButton;

    private ImageButton profileImageButton;
    private ImageButton addEventButton;
    private ImageButton campaignManagementButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_service_org);

        backButton = findViewById(R.id.backButton);

        profileImageButton = findViewById(R.id.nav_profile);
        campaignManagementButton = findViewById(R.id.nav_manage);
        addEventButton = findViewById(R.id.nav_addPost);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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