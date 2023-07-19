package com.example.meet_workshop.homepage.homeorganization;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.meet_workshop.MainActivity;
import com.example.meet_workshop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import pub.devrel.easypermissions.EasyPermissions;


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

    private static final int MAP_ACTIVITY_REQUEST_CODE = 1001;


    //permissions array
    String[] cameraPermissions;
    String[] storagePermissions;

    //views
    EditText titleEt,descripionEt;
    ImageView imageIv;
    Button uploadbtn, isEventBtn, pLocationBtn;

    //show location TV
    TextView locationTagTextView;

    // if event or not
    boolean eventTruth = false;


    //user info
    String name, email, uid, dp;

    //picked image will be saved in this uri
    Uri image_uri = null;

    //progress bar
    ProgressDialog pd;


/*
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        /*
        homePageButton = findViewById(R.id.nav_home);
        addEventButton = findViewById(R.id.nav_addPost);
        campaignManagementButton = findViewById(R.id.nav_manage);
        profileImageButton = findViewById(R.id.nav_profile);
        ImageButton NavButton = findViewById(R.id.nav_addPost);
        NavButton.setColorFilter(Color.rgb(255, 223, 54)); // Yellow Tint*/

        locationTagTextView = findViewById(R.id.locationTagTextView);

        pLocationBtn = findViewById(R.id.pLocationBtn);
        pLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ChooseLocationActivity
                openLocationActivity();
            }
        });

        isEventBtn = findViewById(R.id.pIsEventBtn);
        isEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the isEventBtn
                isEventBtn();
            }
        });


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

        actionBar.setSubtitle(email);

        //get some info of current user to include in the post
        userDbRef = FirebaseDatabase.getInstance().getReference("organizations");
        Query query = userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
        /*
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
        });*/

    }

    private void openLocationActivity() {
        Intent intent = new Intent(this, ChooseLocationActivity.class);
        startActivity(intent);
    }


    private void uploadData(String title, String description, String uri) {
        pd.setMessage("Publishing post...");
        pd.show();

        //for post-image name, post-id, post-publish-time
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post_" + timeStamp;

        if (!uri.equals("noImage")){
            //post with image
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image is upload to firebase storage, now get it's url
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());

                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()){

                                //url is received upload post to fireBase storage
                                HashMap<Object, String> hashMap = new HashMap<>();
                                //put post info
                                hashMap.put("uid", uid);
                                hashMap.put("uName", name);
                                hashMap.put("uEmail", email);
                                hashMap.put("uDp", dp);
                                hashMap.put("pId", timeStamp);
                                hashMap.put("pTitle", title);
                                hashMap.put("pDescription", description);
                                hashMap.put("pImage", downloadUri);
                                hashMap.put("pTime", timeStamp);

                                //path to store post data
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                //put data in this ref
                                ref.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //added in database
                                                pd.dismiss();
                                                Toast.makeText(AddEventOrgActivity.this, "Post published", Toast.LENGTH_SHORT).show();
                                                //reset views
                                                titleEt.setText("");
                                                descripionEt.setText("");
                                                imageIv.setImageURI(null);
                                                image_uri = null;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed adding post in database
                                                pd.dismiss();
                                                Toast.makeText(AddEventOrgActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed uploading image
                            pd.dismiss();
                            Toast.makeText(AddEventOrgActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            //post without image
            //url is received upload post to fireBase storage
            HashMap<Object, String> hashMap = new HashMap<>();
            //put post info
            hashMap.put("uid", uid);
            hashMap.put("uName", name);
            hashMap.put("uEmail", email);
            hashMap.put("uDp", dp);
            hashMap.put("pId", timeStamp);
            hashMap.put("pTitle", title);
            hashMap.put("pDescription", description);
            hashMap.put("pImage", "noImage");
            hashMap.put("pTime", timeStamp);

            //path to store post data
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
            //put data in this ref
            ref.child(timeStamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //added in database
                            pd.dismiss();
                            Toast.makeText(AddEventOrgActivity.this, "Post published", Toast.LENGTH_SHORT).show();
                            titleEt.setText("");
                            descripionEt.setText("");
                            imageIv.setImageURI(null);
                            image_uri = null;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed adding post in database
                            pd.dismiss();
                            Toast.makeText(AddEventOrgActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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
                        pickFromCamera();
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

    // Capture image from gallery
    private ActivityResultLauncher<String> galleryLauncher;

    private void pickFromGallery() {
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                openGallery();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            galleryLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            openGallery();
        }
    }




    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
        } else {
            Toast.makeText(this, "No gallery app found", Toast.LENGTH_SHORT).show();
        }
    }





    // Capture image from camera
    private void pickFromCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkStoragePermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void requestStoragePermission() {
        EasyPermissions.requestPermissions(
                this,
                "Storage permission is required to access the gallery",
                STORAGE_REQUEST_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
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
        getMenuInflater().inflate(R.menu.menu_main,menu);

        menu.findItem(R.id.action_add_post).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }



    //handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromCamera();
                } else {
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
                }
                break;

            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                } else {
                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "im onnnnnnn", Toast.LENGTH_SHORT).show();

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                if (data != null) {
                    image_uri = data.getData();
                    imageIv.setImageURI(image_uri);
                }
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                imageIv.setImageURI(image_uri);
            }
        }

        if (requestCode == MAP_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get the selected location from the ChooseLocationActivity
            double latitude = data.getDoubleExtra("SelectedLat", 0.0);
            double longitude = data.getDoubleExtra("SelectedLng", 0.0);

            // Update the locationTagTextView with the selected location
            String locationTag = "Location: " + latitude + ", " + longitude;
            locationTagTextView.setText(locationTag);
        }
    }

/*
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
*/
    private void isEventBtn() {
        eventTruth = !eventTruth; // Toggle the boolean value

        // Update the button text based on the boolean value
        if (eventTruth) {
            isEventBtn.setText("Not An Event");
        } else {
            isEventBtn.setText("An Event");
        }

        // Update the value in the Realtime Database
        // Replace "your_database_reference" with the actual reference to your Realtime Database node
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("your_database_reference");
        databaseReference.setValue(eventTruth);
    }




    private void openMapActivity() {
        Intent intent = new Intent(AddEventOrgActivity.this, ChooseLocationActivity.class);
        startActivityForResult(intent, MAP_ACTIVITY_REQUEST_CODE);
    }


}