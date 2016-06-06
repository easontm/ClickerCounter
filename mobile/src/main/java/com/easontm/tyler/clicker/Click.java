package com.easontm.tyler.clicker;

import java.util.Date;
import java.util.UUID;

/**
 * Created by drink on 6/5/2016.
 */
public class Click {
    private UUID mId;
    private int mParentId;
    //private Date timestamp;
    private String timestamp;
    private String location;

    public Click() {
        this(UUID.randomUUID());
    }

    public Click(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public int getParentId() {
        return mParentId;
    }

    public void setParentId(int parentId) {
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
}
