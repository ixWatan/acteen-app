package com.example.meet_workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.meet_workshop.SlideShowAct.SlideShowActivityAct1;
import com.example.meet_workshop.SlideShowOrg.SlideShowActivityOrg1;
import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.example.meet_workshop.homepage.homeorganization.CampaignManagementOrgActivity;
import com.example.meet_workshop.homepage.homeorganization.HomeOrgActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class StartEveryThing extends AppCompatActivity {


    private Button Organization;

    private Button Activist;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_every_thing);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            autoLogin(currentUser.getUid());
        }



        Organization = findViewById(R.id.Organization);
        Organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ChooseLocationActivity
                openOrganizationFragments();
            }
        });

        Activist = findViewById(R.id.Activist);
        Activist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ChooseLocationActivity
                openActivistFragments();
            }
        });

    }

    private void openActivistFragments() {
        Intent intent = new Intent(this, SlideShowActivityAct1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


    }

    private void openOrganizationFragments() {
        Intent intent = new Intent(this, SlideShowActivityOrg1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    private void autoLogin(String userId) {
        // Query Firestore to get the user's account type
        db.collection("teenActivists").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // User is an activist, redirect to activist home page
                        startActivity(new Intent(this, HomeActivity.class));
                        finish();
                    } else {
                        // User is an organization, redirect to organization home page
                        startActivity(new Intent(this, CampaignManagementOrgActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle Firestore query failure
                    Toast.makeText(this, "Error fetching user data!", Toast.LENGTH_SHORT).show();
                });
    }

    // ... Implement the login process using Firebase Authentication ...

    // ...

}