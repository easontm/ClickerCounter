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

    public Clicker(UUID id) {
        mId = id;
    }

    public Clicker(int countIn, int goalIn, String titleIn) {
        mId = UUID.randomUUID();
        mCount = countIn;
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

    public int getCount() {
        return mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

    public int getGoal() {
        return mGoal;
    }

    public void setGoal(int mGoal) {
        this.mGoal = mGoal;
    }

    public void incCount() {
        mCount++;
    }

    public void decCount() {
        mCount--;
    }
}
