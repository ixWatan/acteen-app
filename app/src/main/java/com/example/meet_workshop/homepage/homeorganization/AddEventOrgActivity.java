package com.example.meet_workshop.homepage.homeorganization;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.meet_workshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddEventOrgActivity extends AppCompatActivity implements ImagePagerAdapter.OnImageRemoveListener {

    private static final int PICK_IMAGE_REQUEST = 1;
    private List<Uri> croppedImageUris;

    private EditText captionEditText;
    private List<Uri> imageUris;
    private ImagePagerAdapter imagePagerAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    int totalImages;
    int successfulUploads = 0;
    private FirebaseStorage firebaseStorage;

    private ImageButton profileImageButton;
    private ImageButton addEventButton;
    private ImageButton campaignManagementButton;
    private ImageButton homePageButton;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private static final int UCROP_REQUEST_CODE = UCrop.REQUEST_CROP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        homePageButton = findViewById(R.id.nav_home);
        addEventButton = findViewById(R.id.nav_addPost);
        campaignManagementButton = findViewById(R.id.nav_manage);
        profileImageButton = findViewById(R.id.nav_profile);
        croppedImageUris = new ArrayList<>();

        ImageView selectImageButton = findViewById(R.id.select_image_button);
        captionEditText = findViewById(R.id.caption_edit_text);

        selectImageButton.setOnClickListener(v -> openImagePicker());

        Button uploadButton = findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(v -> {
            if (currentUser != null && currentUser.getUid() != null) {
                uploadPost();
            } else {
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize the list of image URIs
        imageUris = new ArrayList<>();

        // Set up the view pager
        ViewPager2 viewPager = findViewById(R.id.image_view_pager);

        // Instantiate the image pager adapter with the listener
        imagePagerAdapter = new ImagePagerAdapter(this, imageUris, this);
        viewPager.setAdapter(imagePagerAdapter);

        ImageButton NavButton = findViewById(R.id.nav_addPost);
        NavButton.setColorFilter(Color.rgb(255, 223, 54)); // Yellow Tint

        profileImageButton.setOnClickListener(v -> openUserProfile());

        homePageButton.setOnClickListener(v -> openHomePage());

        campaignManagementButton.setOnClickListener(v -> openCampaignManagement());

        addEventButton.setOnClickListener(v -> openAddEventOrgActivity());
    }

    private void uploadPost() {
        // Check if cropped images are available
        if (!croppedImageUris.isEmpty()) {
            // Get the Firebase Firestore instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Retrieve the user ID
            String userId = currentUser.getUid();

            // Create a reference to the user's document
            DocumentReference userDocRef = db.collection("organizations").document(userId);

            // Create a reference to the "posts" subcollection under the user's document
            CollectionReference postsRef = userDocRef.collection("posts");

            // Generate a unique ID for the post
            String postId = UUID.randomUUID().toString();

            // Create a reference to the "images" subcollection under the post document
            CollectionReference imagesRef = postsRef.document(postId).collection("images");

            // Get the caption text
            String caption = captionEditText.getText().toString();

            // Create a map with the post data
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("caption", caption);

            // Upload the post data to the "posts" subcollection with the generated post ID
            postsRef.document(postId)
                    .set(postMap)
                    .addOnSuccessListener(aVoid -> {
                        // Initialize the successful uploads counter
                        successfulUploads = 0;

                        // Iterate over the cropped image URIs
                        for (Uri croppedImageUri : croppedImageUris) {
                            // Check if the croppedImageUri is not null
                            if (croppedImageUri != null) {
                                // Generate a unique ID for the image
                                String imageId = UUID.randomUUID().toString();

                                // Create a map with the image URI
                                Map<String, Object> imageMap = new HashMap<>();
                                imageMap.put("imageUrl", croppedImageUri.toString());

                                // Upload the image URL to the "images" subcollection with the generated image ID
                                imagesRef.document(imageId)
                                        .set(imageMap)
                                        .addOnSuccessListener(aVoid1 -> {
                                            // Increment the successful uploads counter
                                            successfulUploads++;

                                            // Check if all images have been uploaded successfully
                                            // Check if all images have been uploaded successfully
                                            if (successfulUploads == croppedImageUris.size()) {
                                                // All images uploaded successfully
                                                Toast.makeText(AddEventOrgActivity.this, "All posts uploaded successfully", Toast.LENGTH_SHORT).show();

                                                // Clear the caption EditText
                                                captionEditText.setText("");

                                                // Reset the successful uploads counter
                                                successfulUploads = 0;

                                                // Clear the imageUris list
                                                imageUris.clear();

                                                // Notify the imagePagerAdapter of data change
                                                imagePagerAdapter.notifyDataSetChanged();

                                                // Clear the cropped image URIs list
                                                croppedImageUris.clear();
                                            }

                                        })
                                        .addOnFailureListener(e -> {
                                            // Error occurred while uploading the image URL to Firestore
                                            String errorMessage = "Failed to upload image URL: " + e.getMessage();
                                            Toast.makeText(AddEventOrgActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Error occurred while uploading the post data to Firestore
                        String errorMessage = "Failed to upload post data: " + e.getMessage();
                        Toast.makeText(AddEventOrgActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    });
        } else {
            // No cropped images available, show an error message
            Toast.makeText(this, "Please crop and select images", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple image selection
        startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGE_REQUEST);
    }

    private void startCropActivity(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_image_" + croppedImageUris.size() + ".jpg"));

        UCrop uCrop = UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500);

        // Customize UCrop options if needed
        // uCrop.<method>()

        uCrop.start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                // Multiple images are selected
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    startCropActivity(imageUri);
                }
            } else if (data.getData() != null) {
                // Single image is selected
                Uri imageUri = data.getData();
                startCropActivity(imageUri);
            }
        } else if (requestCode == UCROP_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Handle the cropped image result
            Uri croppedImageUri = UCrop.getOutput(data);
            if (croppedImageUri != null) {
                // Add the cropped image URI to the list
                croppedImageUris.add(croppedImageUri);

                // Do something with the cropped image URI
                // For example, upload the image to Firebase Storage
                uploadCroppedImage(croppedImageUri);
            }
        }
    }

    private void uploadCroppedImage(Uri croppedImageUri) {
        // Perform the image upload to Firebase Storage
        // You can use the croppedImageUri to get the cropped image file and upload it
        // to Firebase Storage using the code you already have for uploading images.

        // Update the imageUris list used by the imagePagerAdapter
        imageUris.add(croppedImageUri);
        imagePagerAdapter.notifyDataSetChanged();

        // Add the cropped image URI to the croppedImageUris list
        croppedImageUris.add(croppedImageUri);
    }


    private void removeImage(int position) {
        imageUris.remove(position);
        imagePagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onImageRemoved(int position) {
        removeImage(position);
    }

    private void openAddEventOrgActivity() {
        Intent intent = new Intent(this, AddEventOrgActivity.class);
        startActivity(intent);
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
}
