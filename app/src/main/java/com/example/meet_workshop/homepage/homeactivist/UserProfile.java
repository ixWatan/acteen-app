package com.example.meet_workshop.homepage.homeactivist;

import android.app.ProgressDialog;
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

public class UserProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView userNameTextView, cityTextView;

    private ImageButton profileImageButton;

    private ProgressDialog pd;



    private ImageButton notificationButton;
    private ImageButton searchButton;

    private ImageButton homePageButton;
    private ImageView profileImageView, settingsIcon;
    private Button signOutButton, editProfileButton;

    private static final int EDIT_PROFILE_PICTURE_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mAuth = FirebaseAuth.getInstance();


        settingsIcon = findViewById(R.id.settingsIcon);
        editProfileButton = findViewById(R.id.editprofile_button);
        userNameTextView = findViewById(R.id.name_activist);
        cityTextView = findViewById(R.id.city_activist);
        profileImageView = findViewById(R.id.profileImageView);
        profileImageButton = findViewById(R.id.nav_profileActivist);
        searchButton = findViewById(R.id.nav_searchActivist);
        notificationButton = findViewById(R.id.nav_bellActivist);
        homePageButton = findViewById(R.id.nav_homeActivist);
        signOutButton = findViewById(R.id.buttonHabibi);
        profileImageView.setClickable(false);

        ImageButton NavButton = (ImageButton) this.findViewById(R.id.nav_profileActivist);
        NavButton.setColorFilter(Color.rgb(0,0,0)); // Yellow Tint

        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsPageActivist();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfilePictureActivity();
            }
        });

        /*profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfile();
            }
        });*/


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


        pd = new ProgressDialog(this);
        pd.setMessage("Loading Profile...");
        pd.show();

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
                                String city = document.getString("city");
                                String profilePictureUrl = document.getString("profilePictureUrl");

                                // Update the profile picture ImageView with the new URL
                                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                                    Glide.with(UserProfile.this)
                                            .load(profilePictureUrl + "?timestamp=" + System.currentTimeMillis())
                                            .into(profileImageView);
                                } else {
                                    // Display the default profile picture
                                    Glide.with(UserProfile.this)
                                            .load(R.drawable.default_profile_picture)
                                            .into(profileImageView);
                                }

                                // Populate the views with the retrieved information
                                userNameTextView.setText(userName);
                                cityTextView.setText(city);
                            }
                        } else {
                            Toast.makeText(UserProfile.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Load and display the user's profile picture using a library like Glide or Picasso
        String profilePictureUrl = getIntent().getStringExtra("profilePictureUrl");
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Glide.with(UserProfile.this)
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


        pd.dismiss();


    }

    private void openSettingsPageActivist() {

        Intent intent = new Intent(UserProfile.this, SettingsPageActivist.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
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
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == EDIT_PROFILE_PICTURE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            byte[] croppedImageBytes = data.getByteArrayExtra("croppedImage");
            if (croppedImageBytes != null) {
                Bitmap croppedBitmap = BitmapFactory.decodeByteArray(croppedImageBytes, 0, croppedImageBytes.length);
                profileImageView.setImageBitmap(croppedBitmap);

                // Update the profile picture in Firebase Firestore
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Convert the bitmap to a byte array
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();

                    // Upload the byte array to Firebase Storage and update the profile picture URL in Firestore
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference profilePictureRef = storageRef.child("profile_pictures").child(userId + ".jpg");

                    UploadTask uploadTask = profilePictureRef.putBytes(imageBytes);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL of the uploaded image
                            profilePictureRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    String profilePictureUrl = downloadUri.toString();

                                    // Update the profile picture URL in Firestore
                                    db.collection("teenActivists").document(userId)
                                            .update("profilePictureUrl", profilePictureUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Profile picture URL updated successfully
                                                    // You can display a success message if needed
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update profile picture URL
                                                    // You can display an error message if needed
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to get the download URL of the uploaded image
                                    // You can display an error message if needed
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to upload the image to Firebase Storage
                            // You can display an error message if needed
                        }
                    });
                }
            }
        }
    }


    /*private void openUserProfile(){
        Intent intent = new Intent(UserProfile.this, UserProfile.class);
        startActivity(intent);
    }*/

    private void openHomePage() {
        // Start the HomeActivity
        Intent intent = new Intent(UserProfile.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void openNotification() {
        Intent intent = new Intent(UserProfile.this, NotificationActivityActivist.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void openSearch() {
        Intent intent = new Intent(UserProfile.this, SearchActivityActivist.class);
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
        } else if ("com.example.meet_workshop.homepage.homeactivist.NotificationActivityActivist".equals(lastActivity)) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }else if ("com.example.meet_workshop.homepage.homeactivist.SettingsPageActivist".equals(lastActivity)) {
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }else if ("com.example.meet_workshop.homepage.homeactivist.EditProfilePictureActivity".equals(lastActivity)) {
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }
    }
}
