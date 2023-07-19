package com.example.meet_workshop.homepage.homeactivist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet_workshop.R;
import com.example.meet_workshop.homepage.adapters.AdapterPosts;
import com.example.meet_workshop.homepage.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

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


        // navbar icons init
        profileImageButton = findViewById(R.id.nav_profileActivist);
        searchButton = findViewById(R.id.nav_searchActivist);
        notificationButton = findViewById(R.id.nav_bellActivist);
        homePageButton = findViewById(R.id.nav_homeActivist);
        getSupportActionBar().hide();

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

        loadPosts();



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
        NavButton.setColorFilter(Color.rgb(255, 223, 54)); // Yellow Tint
    }



    private void openUserProfile() {
        // Start the UserProfile activity
        Intent intent = new Intent(HomeActivity.this, UserProfile.class);
        startActivity(intent);
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
                    adapterPosts = new AdapterPosts(HomeActivity.this, postList);

                    //set  adapter to recyclerview
                    recyclerView.setAdapter(adapterPosts);
                }

                pd.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // in case of error
                Toast.makeText(HomeActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }



    private void openHomePage() {
        // Start the UserProfile activity
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
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


}


