package com.mycompany.mainui.model;

/**
 * Created by Dell on 4/16/2016.
 */
public class Comment {

    private String username;
    private String comment;

    public Comment(String comment, String username) {
        this.comment = comment;
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
