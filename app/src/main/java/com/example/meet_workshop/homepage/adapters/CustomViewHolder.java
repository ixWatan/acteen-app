package com.example.meet_workshop.homepage.adapters;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet_workshop.R;
import com.example.meet_workshop.homepage.interfaces.SelectListener;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    ImageView uPictureIv, pImageIv;
    TextView uNameTv, pTimeTv, pDescriptionTv, pTitleTv, pLikesTv;
    ImageButton moreBtn;
    Button likeBtn, commentBtn, shareBtn;
    CardView cardView;

    public CustomViewHolder(@NonNull View itemView, SelectListener selectListener) {
        super(itemView);

        //init views
          /* uPictureIv = itemView.findViewById(R.id.uPictureIv);
           pLikesTv = itemView.findViewById(R.id.pLikesTv);*/
        pTitleTv = itemView.findViewById(R.id.pTitleTv);
        pImageIv = itemView.findViewById(R.id.pImageIv);
        cardView = itemView.findViewById(R.id.cardView);
     /*      uNameTv = itemView.findViewById(R.id.uNameTv);
           pTimeTv = itemView.findViewById(R.id.uTimeTv);
           pDescriptionTv = itemView.findViewById(R.id.pDescriptionTv);
           moreBtn = itemView.findViewById(R.id.moreBtn);
           likeBtn = itemView.findViewById(R.id.likeBtn);
           commentBtn = itemView.findViewById(R.id.commentBtn);
           shareBtn = itemView.findViewById(R.id.shareBtn);*/



    }

}
