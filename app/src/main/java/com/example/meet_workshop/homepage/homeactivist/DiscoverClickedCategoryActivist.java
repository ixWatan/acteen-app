package com.example.meet_workshop.homepage.homeactivist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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

public class DiscoverClickedCategoryActivist extends AppCompatActivity implements SelectListener {

    private ImageView profileImageView;

    private FirebaseAuth mAuth;

    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;

    String categoryClickedText;

    private ImageButton notificationButton;
    private ImageButton searchButton;
    private ImageButton backButton;

    private ImageButton profileImageButton;

    private ImageButton homePageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_clicked_category_activist);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //profile pic on top init
        profileImageView = findViewById(R.id.profileImageView);
        mAuth = FirebaseAuth.getInstance();

        categoryClickedText = getIntent().getStringExtra("category_clicked");

        TextView categoryTv = (TextView) findViewById(R.id.CategoryTv);

        categoryTv.setText(categoryClickedText);


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



        profileImageButton = findViewById(R.id.nav_profileActivist);
        searchButton = findViewById(R.id.nav_searchActivist);
        notificationButton = findViewById(R.id.nav_bellActivist);
        homePageButton = findViewById(R.id.nav_homeActivist);





        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfile();
            }
        });




        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

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


        // load profile pic
        loadProfilePic();

        //load posts
        loadPosts();

        ImageButton NavButton = (ImageButton) this.findViewById(R.id.nav_searchActivist);
        NavButton.setColorFilter(Color.rgb(0,0,0)); // Yellow Tint



    }

    private void loadPosts() {
        //path of  all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");



        // get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelPost modelPost = ds.getValue(ModelPost.class);

                    if(modelPost.getpHashtags().toLowerCase().contains("environment")
                    ){
                        postList.add(modelPost);
                    }

                    // adapter
                    adapterPosts = new AdapterPosts(DiscoverClickedCategoryActivist.this, postList, DiscoverClickedCategoryActivist.this);

                    //set  adapter to recyclerview
                    recyclerView.setAdapter(adapterPosts);

                }

                adapterPosts.notifyDataSetChanged();





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // in case of error
                Toast.makeText(DiscoverClickedCategoryActivist.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void loadProfilePic() {
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

    @Override
    public void onItemClicked(ModelPost modelPost) {

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



    private void openUserProfile(){
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }

    private void openHomePage() {
        // Start the HomeActivity
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void openNotification() {

        Intent intent = new Intent(this, NotificationActivityActivist.class);
        startActivity(intent);
    }

    private void openSearch() {
        Intent intent = new Intent(this, SearchActivityActivist.class);
        startActivity(intent);
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
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

}