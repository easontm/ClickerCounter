package com.easontm.tyler.clicker;

import java.util.UUID;

/**
 * Clickers are the core object of the Clicker app (naturally).
 * Users create Clickers to track anything they want that can be
 * quantified with integers. There are 3 types, which indicate
 * how the user may modify their data. They can either increment,
 * decrement, or both. Switching between types is for the sole purpose
 * of cleaning up the UI.
 *
 * Created by Tyler on 5/11/2016.
 */
public class Clicker {

    private UUID mId;
    private String mTitle;
    private int mGoal;
    private int mType;
    private boolean isLocationOn;

    public Clicker() {
        mId = UUID.randomUUID();
        mGoal = 0;
        mType = 0;
    }

    public Clicker(UUID id) {
        mId = id;
        mType = 0;
    }

    public Clicker(int goalIn, String titleIn) {
        mId = UUID.randomUUID();
        mGoal = goalIn;
        mTitle = titleIn;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getGoal() {
        return mGoal;
    }

    public void setGoal(int mGoal) {
        this.mGoal = mGoal;
    }

    /* Type 0: ++ | Type 1: -- | Type 2: +- */
    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public boolean isLocationOn() {
        return isLocationOn;
    }

    public void setLocationOn(boolean locationOn) {
        isLocationOn = locationOn;
    }
}
