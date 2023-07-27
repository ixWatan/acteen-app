package com.example.meet_workshop.homepage.homeorganization;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meet_workshop.MainActivity;
import com.example.meet_workshop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class AddEventOrgActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;

    //permissions
    private static final int UCROP_REQUEST_CODE = 942;


    //picked image will be saved in this uri
    Uri image_uri = null;

    //private static final int MAP_ACTIVITY_REQUEST_CODE = 1001;


    //views
    EditText titleEt, descripionEt;

    //Date, Start Time, End Time
    EditText editTextDate, editTextStart, editTextEnd;

    String selectedDate;
    String selectedStartTime;
    String selectedEndTime;

    ImageView imageIv;
    Button uploadbtn, pLocationBtn;

    //show location TV
    TextView locationTagTextView;

    //user info
    String name, email, uid, dp;

    //progress bar
    ProgressDialog pd;

    private String locationAddressInAddEvent;

    private double latitude;
    private double longitude;
    private String mapsUri;

    SpannableString locationLink;


    private static final int PICK_IMAGE = 1;

    //#

    String[] hashTagsList = {"#Environment" , "#Volunteering", "#Protests", "#Women's Rights", "#Human Rights","#Racism", "#LGBTQ+", "#Animals", "#Petitions", "#Education"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    String  selectHashTag;

    TextView selectedHashtagsTv;
    List<String> selectedHashtags;

    LinearLayout hashtagsContainer;




/*
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        hashtagsContainer = findViewById(R.id.hashtags_container);

        // drop down menu for regions
        autoCompleteTextView = findViewById(R.id.autocomplete_Tv);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item, hashTagsList);


        autoCompleteTextView.setAdapter(adapterItems);
        selectedHashtagsTv = findViewById(R.id.selected_hashtags_tv);
        selectedHashtags = new ArrayList<>();
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedHashtag = adapterView.getItemAtPosition(position).toString();

                if (selectedHashtags.size() >= 3) {
                    Toast.makeText(AddEventOrgActivity.this, "You can select only 3 hashtags", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!selectedHashtags.contains(selectedHashtag)) {
                    selectedHashtags.add(selectedHashtag);
                    TextView hashtagTv = new TextView(AddEventOrgActivity.this);
                    hashtagTv.setText("  " + selectedHashtag);
                    hashtagTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectedHashtags.remove(selectedHashtag);
                            hashtagsContainer.removeView(view);
                        }
                    });
                    hashtagsContainer.addView(hashtagTv);
                    autoCompleteTextView.setText("");
                } else {
                    Toast.makeText(AddEventOrgActivity.this, "You have already selected this hashtag", Toast.LENGTH_SHORT).show();
                }

                // Clear the AutoCompleteTextView text
                autoCompleteTextView.setText("");
            }
        });





        //init views
        titleEt = findViewById(R.id.pTitleEt);
        descripionEt = findViewById(R.id.pDescriptionEt);
        imageIv = findViewById(R.id.pImageIv);
        uploadbtn = findViewById(R.id.pUploadBtn);

        imageIv = findViewById(R.id.pImageIv);
        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });





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
                                // Note: monthOfYear is zero-based, so we add 1 to get the correct month number
                                String formattedDate = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                                editTextDate.setText(formattedDate);
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
                                String formattedHour = String.format("%02d", sHour);
                                String formattedMinute = String.format("%02d", sMinute);
                                editTextStart.setText(formattedHour + ":" + formattedMinute);
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
                                String formattedHour = String.format("%02d", sHour);
                                String formattedMinute = String.format("%02d", sMinute);
                                editTextEnd.setText(formattedHour + ":" + formattedMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });



        locationTagTextView = findViewById(R.id.locationTagTextView);

        // Retrieve the Intent object
        Intent intent = getIntent();

        // Retrieve the data from the Intent
        String locationAddressInAddEvent = intent.getStringExtra("SelectedLocation");
        latitude = intent.getDoubleExtra("SelectedLat", 0.0);
        longitude = intent.getDoubleExtra("SelectedLng", 0.0);
        if (locationAddressInAddEvent != null && !locationAddressInAddEvent.isEmpty()) {
            // Create the Google Maps URI using the address
            mapsUri = "http://maps.google.com/maps?q=" + Uri.encode(locationAddressInAddEvent);

            // Create a clickable link
            locationLink = new SpannableString(locationAddressInAddEvent);
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


        pd = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUserStatus();


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


        //upload button click listener
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get data(title, description) from EditText
                String title = titleEt.getText().toString().trim();
                String description = descripionEt.getText().toString().trim();


                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(AddEventOrgActivity.this, "Enter title...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(description)) {
                    Toast.makeText(AddEventOrgActivity.this, "Enter description...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (image_uri == null) {
                    Toast.makeText(AddEventOrgActivity.this, "Add an image...", Toast.LENGTH_SHORT).show();
                    return;
                } else {
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

        // Check if at least one hashtag is selected
        if (selectedHashtags.isEmpty()) {
            Toast.makeText(AddEventOrgActivity.this, "You must select at least one hashtag", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if location is selected

        if (mapsUri == null || mapsUri.isEmpty()) {
            Toast.makeText(AddEventOrgActivity.this, "You must select a location", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if date is selected
        selectedDate = editTextDate.getText().toString();
        if (selectedDate.isEmpty()) {
            Toast.makeText(AddEventOrgActivity.this, "You must select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if start hour is selected
        selectedStartTime = editTextStart.getText().toString();
        if (selectedStartTime.isEmpty()) {
            Toast.makeText(AddEventOrgActivity.this, "You must select a start hour", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if end hour is selected
        selectedEndTime = editTextEnd.getText().toString();
        if (selectedEndTime.isEmpty()) {
            Toast.makeText(AddEventOrgActivity.this, "You must select an end hour", Toast.LENGTH_SHORT).show();
            return;
        }
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
                        dp = document.getString("profilePictureUrl");
                        Toast.makeText(AddEventOrgActivity.this, dp, Toast.LENGTH_SHORT).show();

                        selectedDate = editTextDate.getText().toString();
                        selectedStartTime = editTextStart.getText().toString();
                        selectedEndTime = editTextEnd.getText().toString();

                        //ade date, start time and end time
                        hashMap.put("pDate", selectedDate);
                        hashMap.put("pStartT", selectedStartTime);
                        hashMap.put("pEndT", selectedEndTime);

                        // Add the link to the post data
                        hashMap.put("pLocationLinkReal", locationTagTextView.getText().toString());


                        // Add the link to the post data
                        hashMap.put("pLocationLink", mapsUri);

                        //ad name and image
                        hashMap.put("uName", name);
                        hashMap.put("uDp", dp);

                        // Convert list of hashtags to a comma-separated string
                        String joinedHashtags = TextUtils.join(", ", selectedHashtags);
                        hashMap.put("pHashtags", joinedHashtags);
                    }
                });

        if (!uri.equals("noImage")) {
            //post with image
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image is upload to firebase storage, now get it's url
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;

                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()) {

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
                                                locationAddressInAddEvent = "";
                                                mapsUri = "";
                                                locationTagTextView.setText("Location");
                                                editTextDate.setText("");
                                                editTextStart.setText("");
                                                editTextEnd.setText("");
                                                // Clear the AutoCompleteTextView input
                                                autoCompleteTextView.setText("");

                                                // Reinitialize the adapter (this is optional and depends on your use case)
                                                adapterItems = new ArrayAdapter<String>(AddEventOrgActivity.this, R.layout.list_item, hashTagsList);
                                                autoCompleteTextView.setAdapter(adapterItems);

                                                // Reset the selected hash tag
                                                selectHashTag = "";

                                                // Clear the TextView that displays the selected hashtags
                                                selectedHashtagsTv.setText("");

                                                // Clear the list of selected hashtags
                                                selectedHashtags.clear();




                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed adding post in database
                                                pd.dismiss();
                                                Toast.makeText(AddEventOrgActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AddEventOrgActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {

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


    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }


    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //user is sign in stay here
            email = user.getEmail();
            uid = user.getUid();
        } else {
            //user not signed in go to ain activity
            startActivity(new Intent(this, MainActivity.class));
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
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.action_add_post).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    image_uri = uri;

                    // Prepare the Uri for the cropped image
                    Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));

                    // Start UCrop
                    UCrop.of(image_uri, outputUri)
                            .withAspectRatio(1, 1) // square image
                            .start(AddEventOrgActivity.this, UCROP_REQUEST_CODE);
                }
            });



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UCROP_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri resultUri = UCrop.getOutput(data);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                imageIv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}