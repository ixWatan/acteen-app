package com.example.meet_workshop.homepage.models;



public class ModelPost {

    String pId, pTitle, pDecor, pImage, pTime, uid, uEmail, uDp, uName;



    boolean PostIsAnEvent;

    public ModelPost() {
        this.pId = "ID";
        this.PostIsAnEvent = false;
        this.pTitle = "Title";
        this.pDecor = "Descreption";
        this.pImage = "Image";
        this.pTime = "Time";
        this.uid = "uid";
        this.uEmail = "gmal.com";
        this.uDp = "profile";
        this.uName = "Name";
    }


    public ModelPost(String pId, String pTitle, String pDecor, String pImage, String pTime, String uid, String uEmail, String uDp, String uName, boolean PostIsAnEvent) {
        this.pId = pId;
        this.PostIsAnEvent = PostIsAnEvent;
        this.pTitle = pTitle;
        this.pDecor = pDecor;
        this.pImage = pImage;
        this.pTime = pTime;
        this.uid = uid;
        this.uEmail = uEmail;
        this.uDp = uDp;
        this.uName = uName;
    }

    public boolean isPostIsAnEvent() {
        return PostIsAnEvent;
    }

    public void setPostIsAnEvent(boolean postIsAnEvent) {
        PostIsAnEvent = postIsAnEvent;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDecor() {
        return pDecor;
    }

    public void setpDecor(String pDecor) {
        this.pDecor = pDecor;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uPd) {
        this.uDp = uPd;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
