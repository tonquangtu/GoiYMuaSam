package com.mycompany.mainui.model;

/**
 * Created by Dell on 4/9/2016.
 */
public class CreateAccountData extends ResponseFromServerData {

    private int type;
    private int lock;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }
}
