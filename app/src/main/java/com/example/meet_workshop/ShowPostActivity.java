package com.example.meet_workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.example.meet_workshop.homepage.models.ModelPost;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;


public class ShowPostActivity extends AppCompatActivity {


    String orgName;
    String postImage;

    String postDescreption;

    String postTimePosted;

    String postTitle;
    String uDp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);

        TextView nameOrgTv = (TextView) findViewById(R.id.showNameOrg);
        TextView postDescreptionTv = (TextView) findViewById(R.id.showDescreption);
        TextView postTimePostedTv = (TextView) findViewById(R.id.showPostTimePosted);
        ImageView postImageIv = (ImageView) findViewById(R.id.showImagePost);
        TextView postTitleTv = (TextView) findViewById(R.id.showPostTitle);
        ImageView postPorfileIv = (ImageView) findViewById(R.id.showPostProfileImg);


        postImage = getIntent().getStringExtra("post_image");
        uDp = getIntent().getStringExtra("post_user_pfp");
        orgName = getIntent().getStringExtra("org_name");
        postDescreption = getIntent().getStringExtra("post_descreption");
        postTimePosted = getIntent().getStringExtra("post_timePosted");
        postTitle = getIntent().getStringExtra("post_title");

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(postTimePosted));
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String pTime = df.format("dd/MM/yyyy hh:mm aa", calendar).toString();



        nameOrgTv.setText(orgName);
        postDescreptionTv.setText(postDescreption);
        postTitleTv.setText(postTitle);
        postTimePostedTv.setText(pTime);
        try {
            Picasso.get().load(postImage).into(postImageIv);

        } catch (Exception e ) {
            Toast.makeText(this, "shit", Toast.LENGTH_SHORT).show();
        }

        try {
            Picasso.get().load(uDp).into(postPorfileIv);
        } catch (Exception e) {

        }



    }
}