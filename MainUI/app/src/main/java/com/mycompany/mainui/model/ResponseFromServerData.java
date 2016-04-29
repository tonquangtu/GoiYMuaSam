package com.mycompany.mainui.model;

/**
 * Created by Dell on 4/9/2016.
 */
public class ResponseFromServerData {

    private int success;
    private String message;
    private String id;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
