package com.example.meet_workshop.homepage.homeactivist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.example.meet_workshop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.UUID;

public class AddPostActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText captionEditText;
    private List<Uri> imageUris;
    private ImagePagerAdapter imagePagerAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    int totalImages;
    int successfulUploads = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        ImageView selectImageButton = findViewById(R.id.select_image_button);
        captionEditText = findViewById(R.id.caption_edit_text);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        Button uploadButton = findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null && currentUser.getUid() != null) {
                    uploadPost();
                } else {
                    Toast.makeText(AddPostActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Initialize the list of image URIs
        imageUris = new ArrayList<>();

        // Initialize the image pager adapter
        imagePagerAdapter = new ImagePagerAdapter(this, imageUris);

        // Set up the view pager
        ViewPager2 viewPager = findViewById(R.id.image_view_pager);
        viewPager.setAdapter(imagePagerAdapter);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGE_REQUEST);
    }

    private void uploadPost() {
        // Check if images are selected
        if (!imageUris.isEmpty()) {
            // Get the Firebase Firestore instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Retrieve the user ID
            String userId = currentUser.getUid();

            // Create a reference to the user's document
            DocumentReference userDocRef = db.collection("teenActivists").document(userId);

            // Create a reference to the "posts" subcollection under the user's document
            CollectionReference postsRef = userDocRef.collection("posts");

            // Generate a unique ID for the post
            String postId = UUID.randomUUID().toString();

            totalImages = imageUris.size();
            String string = String.valueOf(totalImages);
            Toast.makeText(this,string.toString() , Toast.LENGTH_SHORT).show();

            // Create a reference to the "images" subcollection under the post document
            CollectionReference imagesRef = postsRef.document(postId).collection("images");

            successfulUploads = 0;

            // Iterate over the selected images
            for (Uri imageUri : imageUris) {
                // Check if the imageUri is not null
                if (imageUri != null) {
                    // Generate a unique ID for the image
                    String imageId = UUID.randomUUID().toString();

                    // Create a map with the image URI
                    Map<String, Object> imageMap = new HashMap<>();
                    imageMap.put("imageUrl", imageUri.toString());

                    // Create a map with the caption URI
                    Map<String, Object> captionMap = new HashMap<>();
                    captionMap.put("caption", captionEditText.getText().toString());
                    postsRef.document(postId).set(captionMap);

                    // Upload the image to the "images" subcollection with the generated image ID
                    imagesRef.document(imageId)
                            .set(imageMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Image uploaded successfully
                                    // Increment the successful uploads counter
                                    successfulUploads++;

                                    // Check if all images have been uploaded successfully
                                    if (successfulUploads == imageUris.size()) {
                                        // All images uploaded successfully
                                        Toast.makeText(AddPostActivity.this, "All posts uploaded successfully", Toast.LENGTH_SHORT).show();
                                        // Perform any additional actions here
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error occurred while uploading the image
                                    String errorMessage = "Failed to upload image: " + e.getMessage();
                                    Toast.makeText(AddPostActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }


        } else {
            // No images selected, show an error message
            Toast.makeText(AddPostActivity.this, "Please select images", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                // Multiple images are selected
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUris.add(imageUri);
                }
            } else if (data.getData() != null) {
                // Single image is selected
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
            }

            // Update the imagePagerAdapter with the new image URIs
            imagePagerAdapter.notifyDataSetChanged();
        }
    }
}



