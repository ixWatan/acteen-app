package com.example.meet_workshop;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class OrgUser {
    private String name;
    private String email;
    private String password;


    private String DescriptionOfOrg;
    private String webLink;
    private String phoneNum;

    private String orgType;

    @Override
    public String toString() {
        return "OrgUser{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", DescriptionOfOrg='" + DescriptionOfOrg + '\'' +
                ", webLink='" + webLink + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", orgType='" + orgType + '\'' +
                ", followers=" + followers +
                ", following=" + following +
                ", posts=" + posts +
                '}';
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public OrgUser(String orgType) {
        this.orgType = orgType;
    }

    private int followers;
    private int following;
    private int posts;

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }


    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }





    public OrgUser(String name, String email, String password, String city, String region, String age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.following = 0;
        this.followers = 0;
        this.posts = 0;
    }



    public OrgUser(String descriptionOfOrg, String webLink, String phoneNum) {
        this.DescriptionOfOrg = descriptionOfOrg;
        this.webLink = webLink;
        this.phoneNum = phoneNum;
    }
    public String getDescriptionOfOrg() {
        return DescriptionOfOrg;
    }

    public void setDescriptionOfOrg(String descriptionOfOrg) {
        DescriptionOfOrg = descriptionOfOrg;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }



    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

