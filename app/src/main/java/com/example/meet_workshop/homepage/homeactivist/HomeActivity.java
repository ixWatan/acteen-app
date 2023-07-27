package com.example.meet_workshop.homepage.homeactivist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.meet_workshop.R;
import com.example.meet_workshop.ShowPostActivity;
import com.example.meet_workshop.homepage.adapters.AdapterPosts;
import com.example.meet_workshop.homepage.interfaces.SelectListener;
import com.example.meet_workshop.homepage.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements SelectListener {

    FirebaseAuth firebaseAuth;

    private ImageView profileImageView;

    private FirebaseAuth mAuth;


    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;


    private ImageButton profileImageButton;

    private ProgressDialog pd;

    private ImageButton homePageButton;

    private ImageButton notificationButton;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //profile pic on top init
        profileImageView = findViewById(R.id.profileImageView);
        mAuth = FirebaseAuth.getInstance();


        // navbar icons init
        profileImageButton = findViewById(R.id.nav_profileActivist);
        searchButton = findViewById(R.id.nav_searchActivist);
        notificationButton = findViewById(R.id.nav_bellActivist);
        homePageButton = findViewById(R.id.nav_homeActivist);
        homePageButton.setClickable(false);

        //end of navbar stuff

        // post recycler view properties
        recyclerView = findViewById(R.id.postRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //show newest first
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        // set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);

        ProgressDialog pd;




        // init post list
        postList = new ArrayList<>();

        // init the adapter
        adapterPosts = new AdapterPosts(this, postList, this);

        // set the adapter to recyclerview
        recyclerView.setAdapter(adapterPosts);



        loadPosts();







        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the profileImageButton click event
                openUserProfile();
            }
        });


        /*homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the videoPageButton click event
                openHomePage();
            }
        });*/

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearch();
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotification();
            }
        });

        ImageButton NavButton = (ImageButton) this.findViewById(R.id.nav_homeActivist);
        NavButton.setColorFilter(Color.rgb(0,0,0)); // Yellow Tint

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("teenActivists").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String userName = document.getString("name");
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

                            }
                        } else {
                            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });
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







    private void loadPosts() {
        pd = new ProgressDialog(this);
        pd.setMessage("Loading Posts...");
        pd.show();
        //path of  all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");

        // get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    postList.add(modelPost);


                    // adapter
                    adapterPosts = new AdapterPosts(HomeActivity.this, postList, HomeActivity.this);

                    //set  adapter to recyclerview
                    recyclerView.setAdapter(adapterPosts);

                }

                adapterPosts.notifyDataSetChanged();


                pd.dismiss();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // in case of error
                Toast.makeText(HomeActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onItemClicked(ModelPost modelPost) {
        // Handle the item click event

        Intent intent = new Intent(this, ShowPostActivity.class);
        intent.putExtra("org_name",  modelPost.getuName());
        intent.putExtra("post_image",  modelPost.getpImage());
        intent.putExtra("post_description",  modelPost.getpDescription());
        intent.putExtra("post_timePosted",  modelPost.getpTime());
        intent.putExtra("post_title",  modelPost.getpTitle());
        intent.putExtra("post_user_pfp",  modelPost.getuDp());
        intent.putExtra("post_endT", modelPost.getpEndT());
        intent.putExtra("post_startT", modelPost.getpStartT());
        intent.putExtra("post_date", modelPost.getpDate());
        intent.putExtra("post_tags", modelPost.getpHashtags());
        intent.putExtra("post_locationLink", modelPost.getpLocationLink());
        intent.putExtra("post_location", modelPost.getpLocationLinkReal());
        startActivity(intent);
    }


    /*private void openHomePage() {
        // Start the UserProfile activity
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }*/

    private void openNotification() {
        Intent intent = new Intent(this, NotificationActivityActivist.class);
        //intent.putExtra("ActivityRec", "2"); // replace "key" with your actual key and "value" with actual value
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void openSearch() {
        Intent intent = new Intent(this, SearchActivityActivist.class);
        //intent.putExtra("ActivityRec", "1"); // replace "key" with your actual key and "value" with actual value
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void openUserProfile() {
        // Start the UserProfile activity
        Intent intent = new Intent(HomeActivity.this, UserProfile.class);
        //intent.putExtra("ActivityRec", "3"); // replace "key" with your actual key and "value" with actual value
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


    }



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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lastActivity = prefs.getString("lastActivity", "");
        if ("com.example.meet_workshop.homepage.homeactivist.NotificationActivityActivist".equals(lastActivity)) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if ("com.example.meet_workshop.homepage.homeorganization.UserProfileOrgActivity".equals(lastActivity)) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }*/
    }

}














