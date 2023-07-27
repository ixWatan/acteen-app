package com.example.meet_workshop.homepage.homeorganization;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet_workshop.R;
import com.example.meet_workshop.homepage.adapters.AdapterPosts;
import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.example.meet_workshop.homepage.homeactivist.SearchActivityActivist;
import com.example.meet_workshop.homepage.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeOrgActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;

    private ProgressDialog pd;


    private ImageButton profileImageButton;
    private ImageButton addEventButton;
    private ImageButton campaignManagementButton;
    private ImageButton homePageButton;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_org);
        profileImageButton = findViewById(R.id.nav_profile);
        //homePageButton = findViewById(R.id.nav_home);
        campaignManagementButton = findViewById(R.id.nav_manage);
        addEventButton = findViewById(R.id.nav_addPost);
        //searchButton = findViewById(R.id.nav_search);

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


       /* ImageButton NavButton = (ImageButton) this.findViewById(R.id.nav_home);
        NavButton.setColorFilter(Color.rgb(0,0,0)); // Yellow Tint*/


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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearch();
            }
        });


    }

    private void openAddEventOrgActivity() {
        Intent intent = new Intent(HomeOrgActivity.this, AddEventOrgActivity.class);
        startActivity(intent);
    }

    private void openCampaignManagement() {
        Intent intent = new Intent(HomeOrgActivity.this, CampaignManagementOrgActivity.class);
        startActivity(intent);
    }

    private void loadPosts() {
    /*    pd = new ProgressDialog(this);
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
*//*
                    adapterPosts = new AdapterPosts(HomeOrgActivity.this, postList);
*//*

                    //set  adapter to recyclerview
                    recyclerView.setAdapter(adapterPosts);
                }

                pd.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // in case of error
            }

        });*/
    }




    private void openUserProfile() {
        // Start the UserProfile activity
        Intent intent = new Intent(HomeOrgActivity.this, UserProfileOrgActivity.class);
        startActivity(intent);
    }


    private void openHomePage() {
        // Start the UserProfile activity
        Intent intent = new Intent(HomeOrgActivity.this, HomeOrgActivity.class);
        startActivity(intent);
    }

    private void openSearch() {
        Intent intent = new Intent(this, SearchActivityOrg.class);
        startActivity(intent);
    }




}