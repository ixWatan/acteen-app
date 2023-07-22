package com.example.meet_workshop.homepage.models;



public class ModelPost {

    String pId, pTitle, pDescription, pImage, pTime, uid, uEmail, uDp, uName, pStartT, pEndT, pDate, pTag, pLocationLink, pAddress;


    public ModelPost() {
        this.pId = "ID";
        this.pTitle = "Title";
        this.pDescription = "Descreption";
        this.pImage = "Image";
        this.pTime = "Time";
        this.uid = "uid";
        this.uEmail = "gmal.com";
        this.uDp = "profile";
        this.uName = "Name";
        this.pStartT = "pStartT";
        this.pEndT = "pEndT";
        this.pDate = "pDate";
        this.pTag = "pTag";
        this.pLocationLink = "pLocationLink";
        this.pAddress = "pAddress";
    }


    public ModelPost(String pId, String pTitle, String pDescription, String pImage, String pTime, String uid, String uEmail, String uDp,
                     String uName,String pStartT, String pEndT, String pDate, String pTag, String pLocationLink, String pAddress) {
        this.pId = pId;
        this.pTitle = pTitle;
        this.pDescription = pDescription;
        this.pImage = pImage;
        this.pTime = pTime;
        this.uid = uid;
        this.uEmail = uEmail;
        this.uDp = uDp;
        this.uName = uName;
        this.pStartT = pStartT;
        this.pEndT = pEndT;
        this.pDate = pDate;
        this.pTag = pTag;
        this.pLocationLink = pLocationLink;
        this.pAddress= pAddress;

    }

    public String getpLocationLink() {
        return pLocationLink;
    }

    public void setpLocationLink(String pLocationLink) {
        this.pLocationLink = pLocationLink;
    }

    public String getpStartT() {
        return pStartT;
    }

    public void setpStartT(String pStartT) {
        this.pStartT = pStartT;
    }

    public String getpEndT() {
        return pEndT;
    }

    public void setpEndT(String pEndT) {
        this.pEndT = pEndT;
    }

    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }

    public String getpTag() {
        return pTag;
    }

    public void setpTag(String pTag) {
        this.pTag = pTag;
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

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDecor) {
        this.pDescription = pDecor;
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

    public String getpAddress() {
        return pAddress;
    }

    public void setpAddress(String pAddress) {
        this.pAddress = pAddress;
    }
}