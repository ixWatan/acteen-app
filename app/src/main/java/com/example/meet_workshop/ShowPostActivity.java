package com.example.meet_workshop;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    String locationLinkReal;

    String postTimeS;
    String postTimeE;

    String postDate;

    String LocationAndTime;

    //Add To Calender Btn
    Button addToCalender;


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
        postLocation = getIntent().getStringExtra("post_locationLink");
        locationLinkReal = getIntent().getStringExtra("post_location");
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






        // Get a reference to the button
        addToCalender = findViewById(R.id.addToCalenderBtn);

        // Set an onClick listener for the button
        addToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start
                /*String postTitle = "Your Event Title";
                String locationLinkReal = "Your Event Location";
                String postDate = "2023-08-25";  // date format: "yyyy-MM-dd"
                String postTimeS = "14:30";  // time format: "HH:mm"
                String postTimeE = "16:00";  // time format: "HH:mm"*/


                //Toast.makeText(ShowPostActivity.this, " date: " + postDate + " ST " + postTimeS + " date2: " + postDate + " ET "  + postTimeE, Toast.LENGTH_SHORT).show();
                // Parse the post date and time
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                Date startDate = null;
                Date endDate = null;
                try {
                    startDate = dateFormat.parse(postDate + " " + postTimeS);
                    endDate = dateFormat.parse(postDate + " " + postTimeE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Check if startDate and endDate are null, if they are, return from this function early
                if(startDate == null || endDate == null) {
                    Toast.makeText(ShowPostActivity.this, "Invalid date/time format.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create an intent to open the Calendar app
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startDate.getTime())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endDate.getTime())
                        .putExtra(CalendarContract.Events.TITLE, postTitle)
                        .putExtra(CalendarContract.Events.DESCRIPTION,postDescreption)
                        .putExtra(CalendarContract.Events.EVENT_LOCATION, locationLinkReal);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // Show an error message if no app can handle the intent
                    Toast.makeText(ShowPostActivity.this, "No application can handle this action.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //End

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