package com.example.meet_workshop.homepage.homeactivist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meet_workshop.MainActivity;
import com.example.meet_workshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView userNameTextView;
    private ImageView profileImageView;
    private TextView followersTextView;
    private TextView followingTextView;
    private TextView postsTextView;
    private ImageButton profileImageButton;
    private ImageButton searchPageButton;
    private ImageButton videoPageButton;
    private ImageButton addPostButton;
    private ImageButton homePageButton;

    private Button buttonHabibi;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mAuth = FirebaseAuth.getInstance();
        userNameTextView = findViewById(R.id.userNameTextView);
        profileImageView = findViewById(R.id.profileImageView);
        followersTextView = findViewById(R.id.followersTextView);
        followingTextView = findViewById(R.id.followingTextView);
        postsTextView = findViewById(R.id.postsTextView);
        profileImageButton = findViewById(R.id.nav_profile);
        searchPageButton = findViewById(R.id.nav_search);
        videoPageButton = findViewById(R.id.nav_video);
        addPostButton = findViewById(R.id.nav_addPost);
        homePageButton = findViewById(R.id.nav_home);

        // Find the Sign Out button by ID
        buttonHabibi = findViewById(R.id.buttonHabibi);

        // Set click listener for the button
        buttonHabibi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        // Retrieve user information from the Firestore database
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid(); // Replace with the actual user ID
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("teenActivists").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String userName = document.getString("name");
                                int followersCount = document.getLong("followers").intValue();

                                // Populate the views with the retrieved information
                                userNameTextView.setText(userName);
                                followersTextView.setText("Followers \n        "+ followersCount);
                            }
                        } else {
                            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
    private void signOut() {
        // Sign out from Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Show a toast message
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();

        // Start the MainActivity
        Intent intent = new Intent(UserProfile.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optionally, call finish() to prevent the user from returning to the UserProfileActivity using the back button

    }

    private void openUserProfile() {
        // Start the UserProfile activity
        Intent intent = new Intent(UserProfile.this, UserProfile.class);
        startActivity(intent);
    }
    private void openAddPost() {
        // Start the UserProfile activity
        Intent intent = new Intent(UserProfile.this, AddPostActivity.class);
        startActivity(intent);
    }

    private void openHomePage() {
        // Start the UserProfile activity
        Intent intent = new Intent(UserProfile.this, HomeActivity.class);
        startActivity(intent);
    }

    private void openSearchPage() {
        // Start the SearchPage activity
        Intent intent = new Intent(UserProfile.this, SearchPage.class);
        startActivity(intent);
    }

    private void openVideoPage() {
        // Start the VideoPage activity
        Intent intent = new Intent(UserProfile.this, VideoPage.class);
        startActivity(intent);
    }
}

