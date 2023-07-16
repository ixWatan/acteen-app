package com.example.meet_workshop.homepage.homeorganization;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.meet_workshop.MainActivity;
import com.example.meet_workshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;


public class AddEventOrgActivity extends AppCompatActivity  {
    FirebaseAuth firebaseAuth;
    DatabaseReference userDbRef;
    ActionBar actionBar;

    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;


    //permissions array
    String[] cameraPermissions;
    String[] storagePermissions;

    //views
    EditText titleEt,descripionEt;
    ImageView imageIv;
    Button uploadbtn;

    private ImageButton profileImageButton,addEventButton,campaignManagementButton,homePageButton;

    //user info
    String name, email, uid, dp;

    //picked image will be saved in this uri
    Uri image_uri = null;

    //progress bar
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        homePageButton = findViewById(R.id.nav_home);
        addEventButton = findViewById(R.id.nav_addPost);
        campaignManagementButton = findViewById(R.id.nav_manage);
        profileImageButton = findViewById(R.id.nav_profile);
        ImageButton NavButton = findViewById(R.id.nav_addPost);
        NavButton.setColorFilter(Color.rgb(255, 223, 54)); // Yellow Tint

        actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Post");
        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //init permissions arrays
        cameraPermissions = new String[] {android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pd = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUserStatus();

        //get some info of current user to include in the post
        userDbRef = FirebaseDatabase.getInstance().getReference("organizations");
        Query query = userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    name = ""+ ds.child("name").getValue();
                    email = ""+ ds.child("email").getValue();
                    dp = ""+ ds.child("dp").getValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //init views
        titleEt = findViewById(R.id.pTitleEt);
        descripionEt = findViewById(R.id.pDescriptionEt);
        imageIv = findViewById(R.id.pImageIv);
        uploadbtn = findViewById(R.id.pUploadBtn);


        //get image from camera/gallery on click
        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show image pick dialog
                showImagePickDialog();
            }
        });



        //upload button click listener
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get data(title, description) from EditText
                String title = titleEt.getText().toString().trim();
                String description = descripionEt.getText().toString().trim();


                if (TextUtils.isEmpty(title)){
                    Toast.makeText(AddEventOrgActivity.this, "Enter title...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(description)) {
                    Toast.makeText(AddEventOrgActivity.this, "Enter description...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (image_uri == null){
                    //post without image
                    uploadData(title, description, "noImage" );
                }
                else {
                    //post with image
                    uploadData(title, description, String.valueOf(image_uri));
                }
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

    private void uploadData(String title, String description, String uri) {
        pd.setMessage("Publishing post...");
        pd.show();

        //for post-image name, post-id, post-publish-time

    }

    private void showImagePickDialog() {
        //options (camera,gallery) to show in dialog
        String[] options = {"Camera","Gallery"};

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        //set Options to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //item click handle
                if (which==0){
                    //camera clicked
                    if(!checkCameraPermission())
                        requestCameraPermission();
                    else
                        picFromCamera();
                }
                if (which==1){
                    //gallery clicked
                    if (!checkStoragePermission())
                        requestStoragePermission();
                    else
                        pickFromGallery();
                }
            }
        });

        //create and show dialog
        builder.create().show();
    }

    private void pickFromGallery() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void picFromCamera() {
        //intent to pick image from camera
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        //check if storage permissions is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission(){
        //request runtime storage permission
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        //check if camera permissions is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestCameraPermission(){
        //request runtime camera permission
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            //user is sign in stay here
            email = user.getEmail();
            uid = user.getUid();
        } else {
            //user not signed in go to ain activity
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//goto previous activity
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu_user,menu);

        menu.findItem(R.id.action_add_post).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id = R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    //handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //this method is called when user press Allow or Deny from permission request dialog
        // here we will handle permission cases (allowed and denied)

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length < 0){
                    boolean cameraAccepted = grantResults[0] == getPackageManager().PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == getPackageManager().PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted){
                        //both permissions are granted
                        picFromCamera();

                    }
                    else {
                        //camera or gallery both permissions were denied
                        Toast.makeText(this, "Camera and Storage both permission are necessary...", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length < 0) {
                    boolean storageAccepted = grantResults[0] == getPackageManager().PERMISSION_GRANTED;
                    if (storageAccepted){
                        //storage permission granted
                        pickFromGallery();
                    }
                    else {
                        //camera or gallery both permissions were denied
                        Toast.makeText(this, "Storage permission necessary...", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //this method will be called after picking an image from the camera or gallery
        if(resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //image is picked from the gallery, get uri of the image
                image_uri = data.getData();

                //set ImageView
                imageIv.setImageURI(image_uri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera, get the uri of the image
                imageIv.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
