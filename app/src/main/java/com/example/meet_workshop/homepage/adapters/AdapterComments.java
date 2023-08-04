package com.example.meet_workshop.homepage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet_workshop.R;
import com.example.meet_workshop.homepage.models.ModelComment;
import com.example.meet_workshop.homepage.models.ModelPost;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyHolder>{

    Context context;
    List<ModelComment> commentList;

    public AdapterComments(Context context, List<ModelComment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_comments, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        String uid = commentList.get(i).getUid();
        String username = commentList.get(i).getuName();
        String pfp = commentList.get(i).getuDp();
        String cid = commentList.get(i).getcId();
        String commentText = commentList.get(i).getComment();
        String timestamp = commentList.get(i).getTimestamp();

        // convert timestamp to date format
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String pTime = df.format("dd/MM/yyy", calendar).toString();

        myHolder.usernameTv.setText(username);
        myHolder.commentTextTv.setText(commentText);
        myHolder.timeTv.setText(pTime);
        try {
            Picasso.get().load(pfp).placeholder(R.drawable.activistr_logo).into(myHolder.pfpIv);
        } catch (Exception e) {

        }




    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView pfpIv;
        TextView usernameTv, commentTextTv, timeTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            pfpIv = itemView.findViewById(R.id.profilePic);
            commentTextTv = itemView.findViewById(R.id.commentCommentText);
            timeTv = itemView.findViewById(R.id.commentTime);
            usernameTv = itemView.findViewById(R.id.commentUser);

        }
    }
}
