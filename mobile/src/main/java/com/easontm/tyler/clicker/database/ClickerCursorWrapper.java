package com.easontm.tyler.clicker.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.easontm.tyler.clicker.Click;
import com.easontm.tyler.clicker.Clicker;

import com.easontm.tyler.clicker.database.ClickerDbSchema.ClickerTable;
import com.easontm.tyler.clicker.database.ClickerDbSchema.ClickTable;

import java.text.DateFormat;
import java.util.UUID;

/**
 * Fetches Clickers and Clicks from the DB and returns them as objects.
 *
 * Created by Tyler on 6/5/2016.
 */
public class ClickerCursorWrapper extends CursorWrapper {

    public ClickerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Clicker getClicker() {
        String uuidString = getString(getColumnIndex(ClickerTable.Cols.UUID));
        String title = getString(getColumnIndex(ClickerTable.Cols.TITLE));
        String goal = getString(getColumnIndex(ClickerTable.Cols.GOAL));
        String type = getString(getColumnIndex(ClickerTable.Cols.TYPE));
        String location = getString(getColumnIndex(ClickerTable.Cols.LOCATION_STATUS));

        Clicker clicker = new Clicker(UUID.fromString(uuidString));
        clicker.setTitle(title);
        clicker.setGoal(Integer.parseInt(goal));
        clicker.setType(Integer.parseInt(type));
        clicker.setLocationOn(Boolean.parseBoolean(location));

        return clicker;
    }

    public Click getClick() {
        String uuidString = getString(getColumnIndex(ClickTable.Cols.UUID));
        String parent_id = getString(getColumnIndex(ClickTable.Cols.PARENT_ID));
        String timestamp = getString(getColumnIndex(ClickTable.Cols.TIMESTAMP));
        String latitude = getString(getColumnIndex(ClickTable.Cols.LATITUDE));
        String longitude = getString(getColumnIndex(ClickTable.Cols.LONGITUDE));
        String value = getString(getColumnIndex(ClickTable.Cols.VALUE));

        Click click = new Click(UUID.fromString(uuidString));
        click.setParentId(UUID.fromString(parent_id));
        click.setTimestamp(timestamp);
        //click.setLatitude(Double.parseDouble(latitude));
        //click.setLongitude(Double.parseDouble(longitude));
        click.setLatitude(latitude == null ? null : Double.parseDouble(latitude));
        click.setLongitude(longitude == null ? null : Double.parseDouble(longitude));
        click.setValue(Integer.parseInt(value));

        return click;
    }
}
