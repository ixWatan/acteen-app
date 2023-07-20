package com.example.meet_workshop.homepage.homeorganization;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import pub.devrel.easypermissions.EasyPermissions;


public class AddEventOrgActivity extends AppCompatActivity  {
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;

    //permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    //private static final int MAP_ACTIVITY_REQUEST_CODE = 1001;


    //permissions array
    String[] cameraPermissions;
    String[] storagePermissions;

    //views
    EditText titleEt,descripionEt;

    //Date, Start Time, End Time
    EditText editTextDate, editTextStart, editTextEnd;
    ImageView imageIv;
    Button uploadbtn, pLocationBtn;

    //show location TV
    TextView locationTagTextView;

    //user info
    String name, email, uid, dp;

    //picked image will be saved in this uri
    Uri image_uri = null;

    //progress bar
    ProgressDialog pd;

    private double latitude;
    private double longitude;
    private String mapsUri;

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

        editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(AddEventOrgActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                editTextDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        editTextStart = findViewById(R.id.editTextStart);
        editTextStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                TimePickerDialog picker = new TimePickerDialog(AddEventOrgActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                editTextStart.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        editTextEnd = findViewById(R.id.editTextEnd);
        editTextEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                TimePickerDialog picker = new TimePickerDialog(AddEventOrgActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                editTextEnd.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });


        locationTagTextView = findViewById(R.id.locationTagTextView);

        // Retrieve the Intent object
        Intent intent = getIntent();

        // Retrieve the data from the Intent
        latitude = intent.getDoubleExtra("SelectedLat", 0.0);
        longitude = intent.getDoubleExtra("SelectedLng", 0.0);
        if (latitude > 0.0 && longitude > 0.0){
                // Create the Google Maps URI
                mapsUri = "http://maps.google.com/maps?q=" + latitude + "," + longitude;

                // Create a clickable link
                SpannableString locationLink = new SpannableString("View Location");
                locationLink.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        // Open Google Maps when the link is clicked
                        Uri gmmIntentUri = Uri.parse(mapsUri);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }, 0, locationLink.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                // Set the location link text in the TextView
                locationTagTextView.setText(locationLink);
                locationTagTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }


        pLocationBtn = findViewById(R.id.pLocationBtn);
        pLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ChooseLocationActivity
                openLocationActivity();
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
//        userDbRef = FirebaseDatabase.getInstance().getReference("organizations");
//        Query query = userDbRef.orderByChild("email").equalTo(email);
        /*query.addValueEventListener(new ValueEventListener() {
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
        });*/


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
                    Toast.makeText(AddEventOrgActivity.this, "Add an image...", Toast.LENGTH_SHORT).show();
                    return;
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<Object, String> hashMap = new HashMap<>();

        //DocumentReference dbRef = db.collection("organizations").document(uid);
        db.collection("organizations").document(uid).get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name = document.getString("organization_name");
                        Toast.makeText(AddEventOrgActivity.this, name, Toast.LENGTH_SHORT).show();
                        email = document.getString("email");
                        dp  = document.getString("profilePictureUrl");
                        Toast.makeText(AddEventOrgActivity.this, dp, Toast.LENGTH_SHORT).show();

                        String selectedDate = editTextDate.getText().toString();
                        String selectedStartTime = editTextStart.getText().toString();
                        String selectedEndTime = editTextEnd.getText().toString();

                        //ade date, start time and end time
                        hashMap.put("pDate", selectedDate);
                        hashMap.put("pStartT", selectedStartTime);
                        hashMap.put("pEndT", selectedEndTime);

                        // Add the link to the post data
                        hashMap.put("pLocationLink", mapsUri);

                        //ad name and image
                        hashMap.put("uName", name);
                        hashMap.put("uDp", dp);
                    }
                });

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
                                //put post info
                                hashMap.put("uid", uid);
                                hashMap.put("uEmail", email);
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
                                                longitude = 0.0;
                                                latitude = 0.0;
                                                mapsUri = "";
                                                locationTagTextView.setText("Location");
                                                editTextDate.setText("");
                                                editTextStart.setText("");
                                                editTextEnd.setText("");

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

        }
        /*else {
            //post without image
            //url is received upload post to fireBase storage
            //put post info
            String mapsLink = locationTagTextView.getText().toString();
            // Add the link to the post data
            hashMap.put("pLocationLink", mapsLink);
            hashMap.put("uName", name);
            hashMap.put("uDp", dp);
            hashMap.put("uid", uid);
            hashMap.put("uEmail", email);
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
                            longitude = 0.0;
                            latitude = 0.0;
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
        }*/
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






    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
        } else {
            Toast.makeText(this, "No gallery app found", Toast.LENGTH_SHORT).show();
        }
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
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestStoragePermission() {
        EasyPermissions.requestPermissions(
                this,
                "Storage permission is required to access the gallery",
                STORAGE_REQUEST_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );
    }

    private void requestCameraPermission(){
        //request runtime camera permission
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
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
    protected void onStart() {
        super.onStart();
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





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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



    /*private void openMapActivity() {
        Intent intent = new Intent(AddEventOrgActivity.this, ChooseLocationActivity.class);
        startActivityForResult(intent, MAP_ACTIVITY_REQUEST_CODE);
    }*/


   /* @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Put data into the bundle with a key to access it later
        outState.putString("Title", titleEt.getText().toString());
        outState.putString("Description", descripionEt.getText().toString());
        outState.putString("Date", editTextDate.getText().toString());
        outState.putString("StartTime", editTextStart.getText().toString());
        outState.putString("EndTime", editTextEnd.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve and set the data from the bundle
        titleEt.setText(savedInstanceState.getString("Title"));
        descripionEt.setText(savedInstanceState.getString("Description"));
        editTextDate.setText(savedInstanceState.getString("Date"));
        editTextStart.setText(savedInstanceState.getString("StartTime"));
        editTextEnd.setText(savedInstanceState.getString("EndTime"));
    }
*/
   @Override
   protected void onPause() {
       super.onPause();


       SharedPreferences prefs = getSharedPreferences("myprefs", MODE_PRIVATE);
       SharedPreferences.Editor editor = prefs.edit();

       editor.putString("Title", titleEt.getText().toString());
       editor.putString("Description", descripionEt.getText().toString());
       editor.putString("Date", editTextDate.getText().toString());
       editor.putString("StartTime", editTextStart.getText().toString());
       editor.putString("EndTime", editTextEnd.getText().toString());

       // Save the Uri as a String
       if (image_uri != null) {
           editor.putString("ImageUri", image_uri.toString());
       }



       editor.apply();
   }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();

        SharedPreferences prefs = getSharedPreferences("myprefs", MODE_PRIVATE);

        titleEt.setText(prefs.getString("Title", ""));
        descripionEt.setText(prefs.getString("Description", ""));
        editTextDate.setText(prefs.getString("Date", ""));
        editTextStart.setText(prefs.getString("StartTime", ""));
        editTextEnd.setText(prefs.getString("EndTime", ""));

        String imageUriString = prefs.getString("ImageUri", null);
        if (imageUriString != null) {
            image_uri = Uri.parse(imageUriString);
            try {
                InputStream inputStream = getContentResolver().openInputStream(image_uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageIv.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}