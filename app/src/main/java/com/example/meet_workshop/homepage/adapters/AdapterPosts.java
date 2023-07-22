package com.example.meet_workshop.homepage.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.meet_workshop.R;
import com.example.meet_workshop.homepage.interfaces.SelectListener;
import com.example.meet_workshop.homepage.models.ModelPost;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<CustomViewHolder> {

    private SelectListener listener;


    Context context;
    List<ModelPost> postList;
    String pLocationLinkReal;
    String pLocationLink;

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

        pLocationLinkReal = postList.get(i).getpLocationLinkReal();
        SpannableString spannableString = new SpannableString(pLocationLinkReal);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pLocationLink));
                context.startActivity(browserIntent);
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);    // To give the link an underline
                ds.setColor(Color.BLUE);      // Optional: To change the link color
            }
        };

        spannableString.setSpan(clickableSpan, 0, pLocationLinkReal.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        myHolder.pLocationTv.setText(spannableString);
        myHolder.pLocationTv.setMovementMethod(LinkMovementMethod.getInstance());

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
        pLocationLink = postList.get(i).getpLocationLink();
        pLocationLinkReal = postList.get(i).getpLocationLinkReal();
        String pDate = postList.get(i).getpDate();









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
        //myHolder.pLocationTv.setText(pLocationLinkReal);
        myHolder.pDateTv.setText(removeLastFiveCharacters(pDate));
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

    public static String removeLastFiveCharacters(String input) {
        if (input == null || input.length() <= 5) {
            // If the input string is null or its length is less than or equal to 5, return an empty string
            return "";
        } else {
            // Remove the last 5 characters and return the edited string
            return input.substring(0, input.length() - 5);
        }
    }

}













