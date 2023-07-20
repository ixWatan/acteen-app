package com.example.meet_workshop.homepage.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.meet_workshop.R;
import com.example.meet_workshop.homepage.interfaces.RecyclerViewInterface;
import com.example.meet_workshop.homepage.models.ModelPost;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {

    private final RecyclerViewInterface recyclerViewInterface;


    Context context;
    List<ModelPost> postList;

    public AdapterPosts(Context context, List<ModelPost> postList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.postList = postList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @androidx.annotation.NonNull
    @Override
    public MyHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup viewGroup, int i) {

        // inflate layout row_post.xml
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, viewGroup, false);
        return new MyHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull MyHolder myHolder, int i) {

        // get data
        String uid = postList.get(i).getUid();
        String uEmail = postList.get(i).getuEmail();
        String uName = postList.get(i).getuName();
        String uDp = postList.get(i).getuDp();
        String pId = postList.get(i).getpId();
        String pTitle = postList.get(i).getpTitle();
        String pDescreption = postList.get(i).getpDecor();
        String pImage = postList.get(i).getpImage();
        String pTimeStamp = postList.get(i).getpTime();



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




        // handle button clicks




    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView uPictureIv, pImageIv;
        TextView uNameTv, pTimeTv, pDescriptionTv, pTitleTv, pLikesTv;
        ImageButton moreBtn;
        Button likeBtn, commentBtn, shareBtn;

        public MyHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            //init views
           /* uPictureIv = itemView.findViewById(R.id.uPictureIv);
            pLikesTv = itemView.findViewById(R.id.pLikesTv);*/
            pTitleTv = itemView.findViewById(R.id.pTitleTv);
            pImageIv = itemView.findViewById(R.id.pImageIv);
      /*      uNameTv = itemView.findViewById(R.id.uNameTv);
            pTimeTv = itemView.findViewById(R.id.uTimeTv);
            pDescriptionTv = itemView.findViewById(R.id.pDescriptionTv);
            moreBtn = itemView.findViewById(R.id.moreBtn);
            likeBtn = itemView.findViewById(R.id.likeBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }
    }
}