package com.example.meet_workshop.homepage.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet_workshop.R;
import com.example.meet_workshop.homepage.homeactivist.HomeActivity;
import com.example.meet_workshop.homepage.interfaces.SelectListener;
import com.example.meet_workshop.homepage.models.ModelPost;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<CustomViewHolder> {

    private SelectListener listener;


    Context context;
    List<ModelPost> postList;

    public AdapterPosts(Context context, List<ModelPost> postList, SelectListener selectListener) {
        this.context = context;
        this.postList = postList;
        this.listener = selectListener;
    }



    @androidx.annotation.NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup viewGroup, int i) {

        // inflate layout row_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, viewGroup, false);
        return new CustomViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull CustomViewHolder myHolder, int i) {

        myHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(postList.get(i));
            }
        });


        // get data
        String uid = postList.get(i).getUid();
        String uEmail = postList.get(i).getuEmail();
        String uName = postList.get(i).getuName();
        String uDp = postList.get(i).getuDp();
        String pId = postList.get(i).getpId();
        String pTitle = postList.get(i).getpTitle();
        String pDescreption = postList.get(i).getpDescription();
        String pImage = postList.get(i).getpImage();
        String pTimeStamp = postList.get(i).getpTime();
        String pLocationLink = postList.get(i).getpLocationLink();






        // convert timestamp to date format
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String pTime = df.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        // set data

/*
       myHolder.uNameTv.setText(uName);
*/
/*
       myHolder.pTimeTv.setText(pTime);
*/
        myHolder.pTitleTv.setText(pTitle);
        myHolder.pLocationTv.setText(pLocationLink);

        // set user profile pic

        try {
            Picasso.get().load(uDp).placeholder(R.drawable.activistr_logo).into(myHolder.uPictureIv);
        } catch (Exception e) {

        }

        // set post image

        // if there is no image dont show it
        if(pImage.equals("noImage")) {

            // hide imageView
            myHolder.pImageIv.setVisibility(View.GONE);

        }
        else {
            try {
                Picasso.get().load(pImage).into(myHolder.pImageIv);

            } catch (Exception e ) {

            }
        }


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

}













