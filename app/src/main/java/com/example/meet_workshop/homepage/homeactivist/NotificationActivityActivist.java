package com.example.meet_workshop.homepage.homeactivist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.meet_workshop.MainActivity;
import com.example.meet_workshop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

public class NotificationActivityActivist extends AppCompatActivity {

    private FirebaseAuth mAuth;



    private ImageButton profileImageButton;

    private ImageButton notificationButton;
    private ImageButton searchButton;


    private ImageButton homePageButton;
    private ImageView profileImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_activist);
        mAuth = FirebaseAuth.getInstance();

        profileImageView = findViewById(R.id.profileImageView);
        profileImageButton = findViewById(R.id.nav_profileActivist);
        searchButton = findViewById(R.id.nav_searchActivist);
        notificationButton = findViewById(R.id.nav_bellActivist);
        homePageButton = findViewById(R.id.nav_homeActivist);
        notificationButton.setClickable(false);
        ImageButton NavButton = (ImageButton) this.findViewById(R.id.nav_bellActivist);
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

        /*notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotification();
            }
        });*/

        // Retrieve user information from the Firestore database
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("teenActivists").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String userName = document.getString("name");
                                String email = document.getString("email");
                                String profilePictureUrl = document.getString("profilePictureUrl");

                                // Update the profile picture ImageView with the new URL
                                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                                    Glide.with(this)
                                            .load(profilePictureUrl + "?timestamp=" + System.currentTimeMillis())
                                            .into(profileImageView);
                                } else {
                                    // Display the default profile picture
                                    Glide.with(this)
                                            .load(R.drawable.default_profile_picture)
                                            .into(profileImageView);
                                }

                            }
                        } else {
                            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Load and display the user's profile picture using a library like Glide or Picasso
        String profilePictureUrl = getIntent().getStringExtra("profilePictureUrl");
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Glide.with(this)
                    .load(profilePictureUrl)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(profileImageView);

        } else {
            // Set the default profile picture
            Glide.with(this)
                    .load(R.drawable.default_profile_picture)
                    .into(profileImageView);
        }


    }





    private void openUserProfile(){
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void openHomePage() {
        // Start the HomeActivity
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /*private void openNotification() {
        Intent intent = new Intent(this, NotificationActivityActivist.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }*/

    private void openSearch() {
        Intent intent = new Intent(this, SearchActivityActivist.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lastActivity = prefs.getString("lastActivity", "");
        if ("com.example.meet_workshop.homepage.homeactivist.HomeActivity".equals(lastActivity)) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if ("com.example.meet_workshop.homepage.homeactivist.SearchActivityActivist".equals(lastActivity)) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if ("com.example.meet_workshop.homepage.homeactivist.UserProfile".equals(lastActivity)) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

}
