package com.easontm.tyler.clicker;

import java.util.UUID;

/**
 * Created by Tyler on 5/11/2016.
 */
public class Clicker {

    private UUID mId;
    private String mTitle;
    private int mCount;
    private int mGoal;

    public Clicker() {
        mId = UUID.randomUUID();
        mCount = 0;
        mGoal = 0;
    }

    public Clicker(int countIn, int goalIn, String titleIn) {
        mId = UUID.randomUUID();
        mCount = countIn;
        mGoal = goalIn;
        mTitle = titleIn;
    }

    public UUID getmId() {
        return mId;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int mCount) {
        this.mCount = mCount;
    }

    public int getmGoal() {
        return mGoal;
    }

    public void setmGoal(int mGoal) {
        this.mGoal = mGoal;
    }
}
