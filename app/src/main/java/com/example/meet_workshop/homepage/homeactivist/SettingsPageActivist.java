package com.example.meet_workshop.homepage.homeactivist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.meet_workshop.MainActivity;
import com.example.meet_workshop.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsPageActivist extends AppCompatActivity {

    private ImageButton notificationButton;
    private ImageButton searchButton;

    private SearchView search;
    private ImageButton profileImageButton;

    private ImageButton homePageButton;

    private FirebaseAuth mAuth;

    private Button signOutButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page_activist);

        profileImageButton = findViewById(R.id.nav_profileActivist);
        searchButton = findViewById(R.id.nav_searchActivist);
        notificationButton = findViewById(R.id.nav_bellActivist);
        homePageButton = findViewById(R.id.nav_homeActivist);
        signOutButton = findViewById(R.id.buttonHabibi);


        ImageButton NavButton = (ImageButton) this.findViewById(R.id.nav_profileActivist);
        NavButton.setColorFilter(Color.rgb(0,0,0)); // Yellow Tint

        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfile();
            }
        });




        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearch();
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotification();
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
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
        finish(); // Optionally, call finish() to prevent the user from returning to the UserProfileActivity using the back button
    }

    private void openUserProfile(){
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }

    private void openHomePage() {
        // Start the HomeActivity
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void openNotification() {

        Intent intent = new Intent(this, NotificationActivityActivist.class);
        startActivity(intent);
    }

    private void openSearch() {
        Intent intent = new Intent(this, SearchActivityActivist.class);
        startActivity(intent);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }


}