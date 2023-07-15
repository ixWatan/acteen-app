package com.example.meet_workshop.homepage.homeactivist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meet_workshop.R;

public class HomeActivity extends AppCompatActivity {

    private ImageButton profileImageButton;

    private ImageButton homePageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        profileImageButton = findViewById(R.id.nav_profileActivist);
        homePageButton = findViewById(R.id.nav_homeActivist);


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

        ImageButton NavButton = (ImageButton) this.findViewById(R.id.nav_homeActivist);
        NavButton.setColorFilter(Color.rgb(255, 223, 54)); // Yellow Tint
    }



    private void openUserProfile() {
        // Start the UserProfile activity
        Intent intent = new Intent(HomeActivity.this, UserProfile.class);
        startActivity(intent);
    }

    private void openHomePage() {
        // Start the UserProfile activity
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(intent);
    }


}


