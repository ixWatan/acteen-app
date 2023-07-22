package com.example.meet_workshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    String postTags;

    String postLocation;
    String postTimeS;
    String postTimeE;

    String postDate;

    String LocationAndTime;


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
        TextView postLocationAndTime = (TextView) findViewById(R.id.showLocationAndDateAndTime);


        postImage = getIntent().getStringExtra("post_image");
        uDp = getIntent().getStringExtra("post_user_pfp");
        orgName = getIntent().getStringExtra("org_name");
        postDescreption = getIntent().getStringExtra("post_description");
        postTimePosted = getIntent().getStringExtra("post_timePosted");
        postTitle = getIntent().getStringExtra("post_title");
        postTags = getIntent().getStringExtra("post_tags");
        postLocation = getIntent().getStringExtra("post_location");
        postTimeS = getIntent().getStringExtra("post_startT");
        postTimeE = getIntent().getStringExtra("post_endT");
        postDate = getIntent().getStringExtra("post_date");



        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(postTimePosted));
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String pTime = df.format("dd/MM/yyyy hh:mm aa", calendar).toString();

         LocationAndTime = postLocation + "," + postDate + "," + postTimeS + "-" + postTimeE;



        nameOrgTv.setText(orgName);
        postDescreptionTv.setText(postDescreption);
        postLocationAndTime.setText(LocationAndTime);
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

        loadTags();



    }

    private void loadTags() {

        TextView Tag1 = (TextView) findViewById(R.id.showTagLabel1);
        TextView Tag2 = (TextView) findViewById(R.id.showTagLabel2);
        TextView Tag3 = (TextView) findViewById(R.id.showTagLabel3);

        String[] myArray = postTags.split(",");

        int numberOfHashtags = myArray.length;



        if (numberOfHashtags >= 1) {
            Tag1.setText(myArray[0]);
            Tag1.setVisibility(View.VISIBLE);
        } else {
            Tag1.setVisibility(View.GONE);
        }

        if (numberOfHashtags >= 2) {
            Tag2.setText(myArray[1]);
            Tag2.setVisibility(View.VISIBLE);
        } else {
            Tag2.setVisibility(View.GONE);
        }

        if (numberOfHashtags >= 3) {
            Tag3.setText(myArray[2]);
            Tag3.setVisibility(View.VISIBLE);
        } else {
            Tag3.setVisibility(View.GONE);
        }
    }

}