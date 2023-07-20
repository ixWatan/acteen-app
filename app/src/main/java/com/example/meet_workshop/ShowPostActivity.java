package com.example.meet_workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.meet_workshop.homepage.models.ModelPost;



public class ShowPostActivity extends AppCompatActivity {


    String orgName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);

        TextView nameOrgTv = (TextView) findViewById(R.id.showNameOrg);

        orgName = getIntent().getStringExtra("org_name");

        nameOrgTv.setText(orgName);



    }
}