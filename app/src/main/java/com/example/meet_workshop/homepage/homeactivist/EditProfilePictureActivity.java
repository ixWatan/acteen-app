package com.example.meet_workshop.homepage.homeactivist;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.meet_workshop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.IOException;
import java.io.InputStream;

public class EditProfilePictureActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private FirebaseAuth mAuth;

    private ImageView profileImageView;
    private Button saveButton;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_picture);
        mAuth = FirebaseAuth.getInstance();

        profileImageView = findViewById(R.id.profile_image_view);
        saveButton = findViewById(R.id.save_button);

        // Retrieve the current profile picture URL or file path from the intent extras
        String currentProfilePictureUrl = getIntent().getStringExtra("profilePictureUrl");

        // Display the current profile picture in the ImageView
        if (currentProfilePictureUrl != null && !currentProfilePictureUrl.isEmpty()) {
            Glide.with(this).load(currentProfilePictureUrl).into(profileImageView);
        }

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    // Process and save the new profile picture
                    String newProfilePictureUrl = selectedImageUri.toString();
                    updateProfilePicture(newProfilePictureUrl);
                } else {
                    // No image selected
                    Toast.makeText(EditProfilePictureActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectImage() {
        // Create an intent to capture a new image using the camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Start the camera activity and retrieve the captured image
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }


    private void updateProfilePicture(String profilePictureUrl) {
        // Perform the necessary logic to update the user's profile picture
        // This may involve uploading the image to a storage service, updating the user's profile data, etc.

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid(); // Replace with the actual user ID
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Update the profile picture URL in the Firestore document
            db.collection("teenActivists").document(userId)
                    .update("profilePictureUrl", profilePictureUrl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Profile picture update successful
                            Toast.makeText(EditProfilePictureActivity.this, "Profile picture updated successfully", Toast.LENGTH_SHORT).show();

                            // Once the update is complete, navigate back to the UserProfileActivity or the relevant activity
                            Intent intent = new Intent();
                            intent.putExtra("profilePictureUrl", profilePictureUrl);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Profile picture update failed
                            Toast.makeText(EditProfilePictureActivity.this, "Failed to update profile picture. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            // Perform image manipulation, such as resizing
            Bitmap resizedBitmap = resizeImage(selectedImageUri);

            // Display the resized image in the profileImageView
            profileImageView.setImageBitmap(resizedBitmap);
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Retrieve the captured image from the intent
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Perform image manipulation, such as resizing
            Bitmap resizedBitmap = resizeImage(imageBitmap);

            // Display the resized image in the profileImageView
            profileImageView.setImageBitmap(resizedBitmap);
        }
    }

    private Bitmap resizeImage(Uri imageUri) {
        try {
            // Load the image from the URI using InputStream and decode it into a Bitmap
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Resize the image to the desired dimensions (e.g., 500x500 pixels)
            int desiredWidth = 500;
            int desiredHeight = 500;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, false);

            // Clean up resources
            inputStream.close();
            bitmap.recycle();

            return resizedBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Bitmap resizeImage(Bitmap imageBitmap) {
        // Resize the image to the desired dimensions (e.g., 500x500 pixels)
        int desiredWidth = 500;
        int desiredHeight = 500;
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, desiredWidth, desiredHeight, false);

        return resizedBitmap;
    }


}
