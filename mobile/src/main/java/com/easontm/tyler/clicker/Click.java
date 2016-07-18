package com.easontm.tyler.clicker;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by drink on 6/5/2016.
 */
public class Click {
    private UUID mId;
    private UUID mParentId;
    //private Date timestamp;
    private String mTimestamp;
    private Double mLatitude;
    private Double mLongitude;
    private int mValue;

    public Click(UUID parentId, int value) {
        this(UUID.randomUUID());
        mValue = value;
        mParentId = parentId;

        Date now = new Date();
        SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mTimestamp = iso.format(now);

    }

    public Click(UUID parentId, int value, Location location) {
        this(UUID.randomUUID());
        mValue = value;
        mParentId = parentId;

        Date now = new Date();
        SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mTimestamp = iso.format(now);

        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();

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

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        this.mTimestamp = timestamp;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        this.mValue = value;
    }
}
