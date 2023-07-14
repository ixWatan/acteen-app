package com.example.meet_workshop.homepage.homeactivist;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meet_workshop.R;

import java.util.List;

public class
ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder> {

    private Context context;
    private List<Uri> imageUris;
    private OnImageRemoveListener imageRemoveListener;

    public ImagePagerAdapter(Context context, List<Uri> imageUris, OnImageRemoveListener imageRemoveListener) {
        this.context = context;
        this.imageUris = imageUris;
        this.imageRemoveListener = imageRemoveListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        Glide.with(context).load(imageUri).into(holder.imageView);

        holder.removeButton.setOnClickListener(v -> {
            if (imageRemoveListener != null) {
                imageRemoveListener.onImageRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton removeButton;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }


    public interface OnImageRemoveListener {
        void onImageRemoved(int position);
    }
}

