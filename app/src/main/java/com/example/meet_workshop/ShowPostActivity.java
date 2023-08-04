package com.example.meet_workshop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.meet_workshop.homepage.adapters.AdapterComments;
import com.example.meet_workshop.homepage.adapters.AdapterPosts;
import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.example.meet_workshop.homepage.homeorganization.AddEventOrgActivity;
import com.example.meet_workshop.homepage.models.ModelComment;
import com.example.meet_workshop.homepage.models.ModelPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.internal.Util;


public class ShowPostActivity extends AppCompatActivity {


    String orgName;
    String postImage;

    RecyclerView recyclerView;

    ProgressDialog pd;

    String postDescreption;

    String postTimePosted;

    String postTitle;
    String uDp;

    String postTags;

    String postLocation;

    String locationLinkReal;

    String postTimeS;
    String postTimeE, postId;

    String postDate, postLikes, postComments;;

    String LocationAndTime, UserProfilePictureUrl;

    //Add To Calender Btn
    Button addToCalender;

    ImageView sendIcon;

    EditText commentEt;



    private FirebaseAuth mAuth;

    List<ModelComment> commentList;
    AdapterComments adapterComments;

    TextView viewMoreTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);
        mAuth = FirebaseAuth.getInstance();






        TextView nameOrgTv = (TextView) findViewById(R.id.showNameOrg);
        TextView postDescreptionTv = (TextView) findViewById(R.id.showDescreption);
        TextView postTimePostedTv = (TextView) findViewById(R.id.showPostTimePosted);
        ImageView postImageIv = (ImageView) findViewById(R.id.showImagePost);
        TextView postTitleTv = (TextView) findViewById(R.id.showPostTitle);
        ImageView postPorfileIv = (ImageView) findViewById(R.id.showPostProfileImg);
        TextView postLocationAndTime = (TextView) findViewById(R.id.showLocationAndDateAndTime);
        TextView postCommentsTv = (TextView) findViewById(R.id.commentsCount);
        TextView postLikesTv = (TextView) findViewById(R.id.likesCount);










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
        postId = getIntent().getStringExtra("post_id");
        postComments = getIntent().getStringExtra("post_comments");
        postLikes =  getIntent().getStringExtra("post_likes");
        UserProfilePictureUrl = getIntent().getStringExtra("userProfilePicUrl");








        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(postTimePosted));
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String pTime = df.format("dd/MM/yyyy hh:mm aa", calendar).toString();

         LocationAndTime = locationLinkReal + "," + postDate + "," + postTimeS + "-" + postTimeE;



        nameOrgTv.setText(orgName);
        postDescreptionTv.setText(postDescreption);
        postLocationAndTime.setText(LocationAndTime);
        postTitleTv.setText(postTitle);
        postTimePostedTv.setText(pTime);
        postCommentsTv.setText(postComments + " Comments");
        postLikesTv.setText(postLikes + " Likes");
        try {
            Picasso.get().load(postImage).into(postImageIv);

        } catch (Exception e ) {
            Toast.makeText(this, "shit", Toast.LENGTH_SHORT).show();
        }

        try {
            Picasso.get().load(uDp).into(postPorfileIv);
        } catch (Exception e) {

        }

        sendIcon = findViewById(R.id.commentIconSend);
        commentEt = findViewById(R.id.comment_edittext);
        
        postCommentsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommentsDialog();
            }
        });

        sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });

        // load user info
        //------
        //---

        ImageView userProfileIv = (ImageView) findViewById(R.id.showPostProfileImgComment);


        FirebaseUser user = mAuth.getCurrentUser();
        Toast.makeText(this, user.toString(), Toast.LENGTH_SHORT).show();


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
                                        .into(userProfileIv);
                            } else {
                                // Display the default profile picture
                                Glide.with(this)
                                        .load(R.drawable.default_profile_picture)
                                        .into(userProfileIv);
                            }

                        }
                    } else {
                        Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });


        // Load and display the user's profile picture using a library like Glide or Picasso
        String profilePictureUrl = getIntent().getStringExtra("profilePictureUrl");
        if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
            Glide.with(this)
                    .load(profilePictureUrl)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(userProfileIv);

        } else {
            // Set the default profile picture
            Glide.with(this)
                    .load(R.drawable.default_profile_picture)
                    .into(userProfileIv);
        }


        //---
        //------
        // load user info










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

    private int currentCommentBatch = 9; // Number of comments to load initially
    private int commentsPerPage = 9; // Number of comments to load in each subsequent batch
    private boolean hasMoreComments = true; // Indicates whether more comments are available to load

    private void showCommentsDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_comments);

       viewMoreTextView = dialog.findViewById(R.id.viewMoreTv);


        // Find the RecyclerView inside the Dialog layout
        recyclerView = dialog.findViewById(R.id.commentsRecyclerView);

        // Set the layout manager for the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // Show newest first
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        // Load comments for the RecyclerView
        loadComments();

        ImageView commentsSheetRemoveButton = dialog.findViewById(R.id.remove_icon_comments);

        commentsSheetRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply the slide-out animation when dismissing the dialog
                dialog.dismiss();
                dialog.getWindow().setWindowAnimations(R.style.DialogAnimationSlideOut);
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Set the slide-in animation when showing the dialog
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlideIn;

        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void loadComments() {

        pd = new ProgressDialog(this);
        pd.setMessage("Loading Comments...");
        pd.show();


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //show newest first


        // set layout to recyclerview
        recyclerView.setLayoutManager(layoutManager);





        // init post list
        commentList = new ArrayList<>();

        // init the adapter
        adapterComments = new AdapterComments(this, commentList);

        // set the adapter to recyclerview
        recyclerView.setAdapter(adapterComments);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        Query query = ref.limitToFirst(currentCommentBatch);

        // get all data from this ref
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelComment modelComment = ds.getValue(ModelComment.class);
                    commentList.add(modelComment);
                }

                // Update adapter
                adapterComments.notifyDataSetChanged();

                // Check if there are more comments to load
                if (commentList.size() >= currentCommentBatch) {
                    viewMoreTextView.setVisibility(View.VISIBLE);
                } else {
                    viewMoreTextView.setVisibility(View.GONE);
                }

                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // in case of error
                Toast.makeText(ShowPostActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });

        // Set onClickListener for "View More" TextView
        viewMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCommentBatch += commentsPerPage; // Increment the number of comments to load
                loadMoreComments();
            }
        });
    }

    private void loadMoreComments() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        Query query = ref.orderByKey().endAt(commentList.get(commentList.size() - 1).getcId()).limitToLast(commentsPerPage);

        // get all data from this ref
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ModelComment> additionalComments = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelComment modelComment = ds.getValue(ModelComment.class);
                    additionalComments.add(modelComment);
                }

                // Add the additional comments to the bottom of the current list
                commentList.addAll(additionalComments);

                // Update adapter
                adapterComments.notifyDataSetChanged();

                // Check if there are more comments to load
                if (additionalComments.size() >= commentsPerPage) {
                    viewMoreTextView.setVisibility(View.VISIBLE);
                } else {
                    viewMoreTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // in case of error
                Toast.makeText(ShowPostActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postComment() {
        pd = new ProgressDialog(this);
        pd.setMessage("Adding Comment...");

        //get data from comment edittext view
        String commentText = commentEt.getText().toString().trim();

        //check if the comment edittext is empty
        if(TextUtils.isEmpty(commentText)) {
            Toast.makeText(ShowPostActivity.this, "Comment is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // load user info
        //------
        //---

        ImageView userProfileIv = (ImageView) findViewById(R.id.showPostProfileImgComment);


        FirebaseUser user = mAuth.getCurrentUser();
        Toast.makeText(this, user.toString(), Toast.LENGTH_SHORT).show();



        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();





        //---
        //------
        // load user info

        String timeStamp = String.valueOf(System.currentTimeMillis());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");

        HashMap<Object, String> hashMap = new HashMap<>();


        //ade date, start time and end time
        hashMap.put("cId", timeStamp);
        hashMap.put("comment", commentText);
        hashMap.put("timestamp", timeStamp);
        hashMap.put("uid", user.getUid());
        hashMap.put("uName", user.getDisplayName());
        hashMap.put("uDp", UserProfilePictureUrl);

        ref.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                //comment add successfully
                pd.dismiss();

                Toast.makeText(ShowPostActivity.this, "Done", Toast.LENGTH_SHORT).show();
                commentEt.setText("");
                updateCommentCount();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //comment not added - something is wrong
                pd.dismiss();
                Toast.makeText(ShowPostActivity.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();



            }
        });




    }


    boolean mProcessComment = false;

    private void updateCommentCount() {
        mProcessComment = true;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mProcessComment) {
                    String comments = ""+ snapshot.child("pComments").getValue();
                    int newCommentVal = Integer.parseInt(comments) + 1;
                    ref.child("pComments").setValue("" + newCommentVal);
                    mProcessComment = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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