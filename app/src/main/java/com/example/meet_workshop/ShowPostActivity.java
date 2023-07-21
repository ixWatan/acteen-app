package com.example.meet_workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.example.meet_workshop.homepage.models.ModelPost;
import com.squareup.picasso.Picasso;


public class ShowPostActivity extends AppCompatActivity {


    String orgName;
    String postImage;

    String postDescreption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);

        TextView nameOrgTv = (TextView) findViewById(R.id.showNameOrg);
        TextView postDescreptionTv = (TextView) findViewById(R.id.showDescreption);
        ImageView postImageIv = (ImageView) findViewById(R.id.showImagePost);

        postImage = getIntent().getStringExtra("post_image");
        orgName = getIntent().getStringExtra("org_name");
        postDescreption = getIntent().getStringExtra("post_descreption");

        nameOrgTv.setText(orgName);
        postDescreptionTv.setText(postDescreption);
        try {
            Picasso.get().load(postImage).into(postImageIv);

        } catch (Exception e ) {
            Toast.makeText(this, "shit", Toast.LENGTH_SHORT).show();
        }



    }
}