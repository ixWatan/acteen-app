package com.example.meet_workshop.homepage.homeorganization;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.meet_workshop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class EditProfilePictureOrgActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 3;

    private FirebaseAuth mAuth;
    private Button selectImageButton;
    private Button cameraButton;

    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_picture_org);
        mAuth = FirebaseAuth.getInstance();

        selectImageButton = findViewById(R.id.select_image_button);
        cameraButton = findViewById(R.id.camera_button);

        String currentProfilePictureUrl = getIntent().getStringExtra("profilePictureUrl");



        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
            }
        });


    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    private void updateProfilePicture(String profilePictureUrl) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Upload the cropped image to Firebase Storage and obtain the download URL
            uploadImageToStorage(profilePictureUrl, new OnImageUploadListener() {
                @Override
                public void onImageUploadSuccess(String downloadUrl) {
                    // Update the profile picture URL in the Firestore document
                    db.collection("organizations").document(userId)
                            .update("profilePictureUrl", downloadUrl)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EditProfilePictureOrgActivity.this, "Profile picture updated successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent();
                                    intent.putExtra("profilePictureUrl", downloadUrl);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditProfilePictureOrgActivity.this, "Failed to update profile picture. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                @Override
                public void onImageUploadFailure() {
                    Toast.makeText(EditProfilePictureOrgActivity.this, "Failed to upload profile picture. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void uploadImageToStorage(String profilePictureUrl, final OnImageUploadListener listener) {
        Glide.with(this)
                .asBitmap()
                .load(profilePictureUrl)
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        listener.onImageUploadFailure();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        listener.onImageUploadSuccess(profilePictureUrl);

                        return false;
                    }
                })
                .submit();
    }

    interface OnImageUploadListener {
        void onImageUploadSuccess(String downloadUrl);

        void onImageUploadFailure();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            startCropActivity(selectedImageUri);
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            selectedImageUri = getImageUri(imageBitmap);
            startCropActivity(selectedImageUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            handleCropResult(data);
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void startCropActivity(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_image.jpg"));

        UCrop uCrop = UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500);

        uCrop.start(this);
    }

    private void handleCropResult(Intent result) {
        final Uri croppedImageUri = UCrop.getOutput(result);

        if (croppedImageUri != null) {
            try {
                Bitmap croppedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), croppedImageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] croppedImageBytes = byteArrayOutputStream.toByteArray();

                Intent intent = new Intent();
                intent.putExtra("croppedImage", croppedImageBytes);
                setResult(RESULT_OK, intent);
                finish();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to crop image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to crop image", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startCameraActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraActivity();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCameraActivity() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
}
