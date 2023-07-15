package com.example.meet_workshop.homepage.homeorganization;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
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
import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.example.meet_workshop.homepage.homeactivist.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;

public class UserProfileOrgActivity extends AppCompatActivity {

    private ImageButton profileImageButton;
    private ImageButton addEventButton;

    private FirebaseAuth mAuth;

    private ImageButton campaignManagementButton;
    private ImageButton homePageButton;

    //firebase storage
    StorageReference storageReference;

    //path for stored images
    String path = "ProfileImgs";


    private Button signOutButton; // Added sign-out button

    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    String permissions[];

    Uri image_uri;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_org);
        profileImageButton = findViewById(R.id.nav_profile);
        homePageButton = findViewById(R.id.nav_home);
        campaignManagementButton = findViewById(R.id.nav_manage);
        addEventButton = findViewById(R.id.nav_addPost);
        signOutButton = findViewById(R.id.btn_sign_out); // Initialize the sign-out button
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); // Get the FirebaseAuth instance
        FirebaseApp.initializeApp(this);

        ImageView ProfilePicImageView = (ImageView) this.findViewById(R.id.profileImageViewOrg);

        // Email and Name text views
        TextView nameTextView = (TextView) this.findViewById(R.id.name_text_org);
        TextView emailTextView = (TextView) this.findViewById(R.id.email_text_org);

        permissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};



        ImageButton NavButton = (ImageButton) this.findViewById(R.id.nav_profile);
        NavButton.setColorFilter(Color.rgb(255, 223, 54)); // Yellow Tint

        addUserInfoToTextView();


        // Edit Profile for Organization on click
        ProfilePicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the sign-out button click event
                editProfilePicture();
            }
        });

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

    private boolean checkPermissionForStorage() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_DENIED);

        return result;

    }

    private void requestPermissionForStorage() {

        ActivityCompat.requestPermissions(this, permissions, STORAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Allow Permissions", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK) {
            // IMAGE PICKED FROM GALLERY
            if(requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();

                uploadProfileCoverPhoto(image_uri);

            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {

        // storage stuff
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // firebase stuff
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); // Get the FirebaseAuth instance
        FirebaseApp.initializeApp(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // end of firebase stuff
        String filePathAndName = user.getUid();

        StorageReference storageReference2nd = storageRef.child(filePathAndName);
        storageReference2nd.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image uploaded successfully
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();


                // check if image is uploaded or not
                if(uriTask.isSuccessful()) {
                    // uploaded

                    WriteBatch batch = db.batch();
                    HashMap<String, Object> results = new HashMap<>();
                    results.put("imageUrls", downloadUri.toString());



                } else {
                    //error
                    Toast.makeText(UserProfileOrgActivity.this, "There is an error", Toast.LENGTH_SHORT).show();

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // image not uploaded successfully, an error is there

            }
        });

    }

    private void editProfilePicture() {
        if(!checkPermissionForStorage()) {
            requestPermissionForStorage();
        } else {
            pickFromGallery();
        }
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

    private void addUserInfoToTextView() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); // Get the FirebaseAuth instance
        FirebaseApp.initializeApp(this);


        ImageView ProfilePicImageView = (ImageView) this.findViewById(R.id.profileImageViewOrg);

        // Email and Name text views
        TextView nameTextView = (TextView) this.findViewById(R.id.name_text_org);
        TextView emailTextView = (TextView) this.findViewById(R.id.email_text_org);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid(); // Replace with the actual user ID
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("organizations").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String userName = document.getString("organization_name");
                                String image = document.getString("profilePictureUrl");
                                String email = document.getString("email");

                                // Update the profile picture ImageView with the new URL
                                if (image != null && !image.isEmpty()) {
                                    Glide.with(UserProfileOrgActivity.this).load(image).into(ProfilePicImageView);
                                } else {
                                    // Display the default profile picture
                                    Glide.with(UserProfileOrgActivity.this).load(R.drawable.default_profile_picture).into(ProfilePicImageView);
                                }

                                // Populate the views with the retrieved information
                                nameTextView.setText(userName);
                                emailTextView.setText(email);
                            }
                        } else {
                            Toast.makeText(UserProfileOrgActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}
