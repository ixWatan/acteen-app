package com.example.meet_workshop.homepage.homeorganization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.meet_workshop.MainActivity;
import com.example.meet_workshop.R;
import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.example.meet_workshop.homepage.homeactivist.UserProfile;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileOrgActivity extends AppCompatActivity {

    private ImageButton profileImageButton;
    private ImageButton addEventButton;
    private ImageButton campaignManagementButton;
    private ImageButton homePageButton;
    private Button signOutButton; // Added sign-out button




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_org);
        profileImageButton = findViewById(R.id.nav_profile);
        homePageButton = findViewById(R.id.nav_home);
        campaignManagementButton = findViewById(R.id.nav_manage);
        addEventButton = findViewById(R.id.nav_addPost);
        signOutButton = findViewById(R.id.btn_sign_out); // Initialize the sign-out button

        ImageButton NavButton = (ImageButton) this.findViewById(R.id.nav_profile);
        NavButton.setColorFilter(Color.rgb(255, 223, 54)); // Yellow Tint

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the sign-out button click event
                signOut();
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

    private void signOut() {
        // Sign out from Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Show a toast message
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();

        // Start the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("profilePictureUrl", ""); // Clear the current profile picture URL
        startActivity(intent);
        finish(); // Optionally, call finish() to prevent the user from returning to the UserProfileOrgActivity using the back button
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
