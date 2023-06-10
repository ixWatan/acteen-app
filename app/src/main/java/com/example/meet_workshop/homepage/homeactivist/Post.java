package com.example.meet_workshop.homepage.homeactivist;

public class Post {
    private String imageUrl;
    private String caption;

    public Post() {
        // Default constructor required for Firestore
    }

    public Post(String imageUrl, String caption) {
        this.imageUrl = imageUrl;
        this.caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
