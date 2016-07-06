package com.easontm.tyler.clicker;

import java.util.Date;
import java.util.UUID;

/**
 * Created by drink on 6/5/2016.
 */
public class Click {
    private UUID mId;
    private UUID mParentId;
    //private Date timestamp;
    private String timestamp;
    private String location;
    private int mValue;

    public Click(UUID parentId, int value) {
        this(UUID.randomUUID());
        mValue = value;
        mParentId = parentId;
    }

    public Click(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public UUID getParentId() {
        return mParentId;
    }

    public void setParentId(UUID parentId) {
        mParentId = parentId;
    }

    /*
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    */

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        this.mValue = value;
    }
}
