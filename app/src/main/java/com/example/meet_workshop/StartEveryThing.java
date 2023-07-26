package com.example.meet_workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.meet_workshop.SlideShowAct.SlideShowActivityAct1;
import com.example.meet_workshop.SlideShowOrg.SlideShowActivityOrg1;


public class StartEveryThing extends AppCompatActivity {


    private Button Organization;

    private Button Activist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_every_thing);


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

}