package com.example.meet_workshop.homepage.homeactivist;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.example.meet_workshop.R;

public class HomeActivity extends AppCompatActivity {

    private ImageButton profileImageButton;
    private ImageButton searchPageButton;
    private ImageButton videoPageButton;
    private ImageButton addPostButton;
    private ImageButton homePageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profileImageButton = findViewById(R.id.nav_profile);
        searchPageButton = findViewById(R.id.nav_search);
        videoPageButton = findViewById(R.id.nav_video);
        addPostButton = findViewById(R.id.nav_addPost);
        homePageButton = findViewById(R.id.nav_home);


        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                openUserProfile();
            }
        });

        searchPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the searchPageButton click event
                openSearchPage();
            }
        });

        videoPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the videoPageButton click event
                openVideoPage();
            }
        });
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the videoPageButton click event
                openAddPost();
            }
        });
        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the videoPageButton click event
                openHomePage();
            }
        });
    }

    public void onNavHomeClick(View view) {
        // Handle the nav_home button click
        openHomePage();
    }

    public void onNavSearchClick(View view) {
        // Handle the nav_search button click
        openSearchPage();
    }

    public void onNavProfileClick(View view) {
        // Handle the nav_profile button click
        openUserProfile();
    }

    public void onNavVideoClick(View view) {
        // Handle the nav_notifications button click
        openVideoPage();
    }

    public void onNavAddPostClick(View view) {
        // Handle the nav_settings button click
        openAddPost();
    }

    private void openUserProfile() {
        // Start the UserProfile activity
        Intent intent = new Intent(HomeActivity.this, UserProfile.class);
        startActivity(intent);
    }
    private void openAddPost() {
        // Start the UserProfile activity
        Intent intent = new Intent(HomeActivity.this, NewPost.class);
        startActivity(intent);
    }

    private void openHomePage() {
        // Start the UserProfile activity
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void openSearchPage() {
        // Start the SearchPage activity
        Intent intent = new Intent(HomeActivity.this, SearchPage.class);
        startActivity(intent);
    }

    private void openVideoPage() {
        // Start the VideoPage activity
        Intent intent = new Intent(HomeActivity.this, VideoPage.class);
        startActivity(intent);
    }
}


