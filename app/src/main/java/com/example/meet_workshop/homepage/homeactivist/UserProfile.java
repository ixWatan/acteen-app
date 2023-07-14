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

import com.bumptech.glide.Glide;
import com.example.meet_workshop.MainActivity;
import com.example.meet_workshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView userNameTextView;
    private TextView followersTextView;
    private TextView followingTextView;
    private TextView postsTextView;
    private ImageButton profileImageButton;
    private ImageButton searchPageButton;
    private ImageButton videoPageButton;
    private ImageButton addPostButton;
    private ImageButton homePageButton;
    private ImageView profileImageView;
    private Button signOutButton;

    private static final int EDIT_PROFILE_PICTURE_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mAuth = FirebaseAuth.getInstance();

        userNameTextView = findViewById(R.id.userNameTextView);
        profileImageView = findViewById(R.id.profileImageView);
        followersTextView = findViewById(R.id.followersTextView);
        followingTextView = findViewById(R.id.followingTextView);
        postsTextView = findViewById(R.id.postsTextView);
        profileImageButton = findViewById(R.id.nav_profile);
        addPostButton = findViewById(R.id.nav_addPost);
        homePageButton = findViewById(R.id.nav_home);
        signOutButton = findViewById(R.id.buttonHabibi);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfilePictureActivity();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfilePictureActivity();
            }
        });




        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                String profilePictureUrl = document.getString("profilePictureUrl");

                                // Update the profile picture ImageView with the new URL
                                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                                    Glide.with(UserProfile.this).load(profilePictureUrl).into(profileImageView);
                                } else {
                                    // Display the default profile picture
                                    Glide.with(UserProfile.this).load(R.drawable.default_profile_picture).into(profileImageView);
                                }

                                // Populate the views with the retrieved information
                                userNameTextView.setText(userName);
                                followersTextView.setText("Followers: " + followersCount);
                            }
                        } else {
                            Toast.makeText(UserProfile.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Load and display the user's profile picture using a library like Glide or Picasso
        String profilePictureUrl = getIntent().getStringExtra("profilePictureUrl");
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Glide.with(this).load(profilePictureUrl).into(profileImageView);
        } else {
            // Set the default profile picture
            Glide.with(this).load(R.drawable.default_profile_picture).into(profileImageView);
        }
    }

    private void signOut() {
        // Sign out from Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Show a toast message
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();

        // Start the MainActivity
        Intent intent = new Intent(UserProfile.this, MainActivity.class);
        intent.putExtra("profilePictureUrl", ""); // Clear the current profile picture URL
        startActivity(intent);
        finish(); // Optionally, call finish() to prevent the user from returning to the UserProfileActivity using the back button
    }

    private void openEditProfilePictureActivity() {
        Intent intent = new Intent(UserProfile.this, EditProfilePictureActivity.class);
        startActivityForResult(intent, EDIT_PROFILE_PICTURE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_PICTURE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String profilePictureUrl = data.getStringExtra("profilePictureUrl");
            if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                Glide.with(this).load(profilePictureUrl).into(profileImageView);
            } else {
                Glide.with(this).load(R.drawable.default_profile_picture).into(profileImageView);
            }
        }
    }

    private void openAddPost() {
        // Start the AddPostActivity
        Intent intent = new Intent(UserProfile.this, AddPostActivity.class);
        startActivity(intent);
    }

    private void openHomePage() {
        // Start the HomeActivity
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
