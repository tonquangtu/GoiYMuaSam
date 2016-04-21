package com.mycompany.mainui.model;

import java.util.List;

/**
 * Created by Dell on 4/16/2016.
 */
public class GetCommentData extends ResponseFromServerData {


    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
