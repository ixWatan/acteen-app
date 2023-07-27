package com.example.meet_workshop.homepage.homeorganization;

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

import java.io.ByteArrayOutputStream;

public class UserProfileOrgActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView userNameTextView;

    private ImageButton profileImageButton;
    private ImageButton campaignManagementButton;
    private ImageButton addEventButton;

    private ImageButton searchButton;

    private ImageButton homePageButton;
    private ImageView profileImageView;
    private Button signOutButton;

    ProgressDialog pd;

    private static final int EDIT_PROFILE_PICTURE_REQUEST_CODE = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_org);
        mAuth = FirebaseAuth.getInstance();
        userNameTextView = findViewById(R.id.name_org);
        profileImageView = findViewById(R.id.profileImageView);
        pd = new ProgressDialog(this);

        // Navbar organization stuff 5 icons
        //profileImageButton = findViewById(R.id.nav_profile);
        //homePageButton = findViewById(R.id.nav_home);
        campaignManagementButton = findViewById(R.id.nav_manage);
        addEventButton = findViewById(R.id.nav_addPost);
        //searchButton = findViewById(R.id.nav_search);
        profileImageButton= findViewById(R.id.nav_profile);
        profileImageButton.setClickable(false);


        signOutButton = findViewById(R.id.buttonHabibi); // Initialize the sign-out button



        ImageButton NavButton = (ImageButton) this.findViewById(R.id.nav_profile);
        NavButton.setColorFilter(Color.rgb(0,0,0)); // Yellow Tint

        /*searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearch();
            }
        });*/
       /* profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfilePictureOrgActivity();
            }
        });*/

      /*  profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfile();
            }
        });*/

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the sign-out button click event
                signOut();
            }
        });


        /*homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the videoPageButton click event
                openHomePage();
            }
        });*/

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

        TextView emailTextView = (TextView) this.findViewById(R.id.email_org);

        // Retrieve user information from the Firestore database
        FirebaseUser user = mAuth.getCurrentUser();
        pd.setMessage("Reloading Profile");
        pd.show();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("organizations").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String userName = document.getString("organization_name");
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

                                // Populate the views with the retrieved information
                                userNameTextView.setText(userName);
                                emailTextView.setText(email);

                                pd.cancel();
                                pd.dismiss();
                            }
                        } else {
                            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });

            pd.cancel();
            pd.dismiss();
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

    private void openEditProfilePictureOrgActivity() {
        Intent intent = new Intent(this, EditProfilePictureOrgActivity.class);
        startActivityForResult(intent, EDIT_PROFILE_PICTURE_REQUEST_CODE);
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
                                    db.collection("organizations").document(userId)
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


    private void signOut() {
        // Sign out from Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Show a toast message
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();

        // Start the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("profilePictureUrl", ""); // Clear the current profile picture URL
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void openAddEventOrgActivity() {
        Intent intent = new Intent(this, AddEventOrgActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }




    private void openCampaignManagement() {
        Intent intent = new Intent(this, CampaignManagementOrgActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }

  /*  private void openUserProfile() {
        // Start the UserProfile activity
        Intent intent = new Intent(this, UserProfileOrgActivity.class);
        startActivity(intent);
    }*/

   /* private void openHomePage() {
        // Start the UserProfile activity
        Intent intent = new Intent(this, HomeOrgActivity.class);
        startActivity(intent);
    }

    private void openSearch() {
        Intent intent = new Intent(this, SearchActivityOrg.class);
        startActivity(intent);
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
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }





}
