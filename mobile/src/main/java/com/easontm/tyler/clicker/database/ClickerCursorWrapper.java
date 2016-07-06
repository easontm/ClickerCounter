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
 * Created by drink on 6/5/2016.
 */
public class ClickerCursorWrapper extends CursorWrapper {

    public ClickerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Clicker getClicker() {
        String uuidString = getString(getColumnIndex(ClickerTable.Cols.UUID));
        String title = getString(getColumnIndex(ClickerTable.Cols.TITLE));
        String count = getString(getColumnIndex(ClickerTable.Cols.COUNT));
        String goal = getString(getColumnIndex(ClickerTable.Cols.GOAL));
        String type = getString(getColumnIndex(ClickerTable.Cols.TYPE));

        Clicker clicker = new Clicker(UUID.fromString(uuidString));
        clicker.setTitle(title);
        clicker.setCount(Integer.parseInt(count));
        clicker.setGoal(Integer.parseInt(goal));
        clicker.setType(Integer.parseInt(type));

        return clicker;
    }

    public Click getClick() {
        String uuidString = getString(getColumnIndex(ClickTable.Cols.UUID));
        String parent_id = getString(getColumnIndex(ClickTable.Cols.PARENT_ID));
        String timestamp = getString(getColumnIndex(ClickTable.Cols.TIMESTAMP));
        String location = getString(getColumnIndex(ClickTable.Cols.LOCATION));
        String value = getString(getColumnIndex(ClickTable.Cols.VALUE));

        Click click = new Click(UUID.fromString(uuidString));
        click.setParentId(UUID.fromString(parent_id));
        //DateFormat df =
        click.setTimestamp(timestamp);
        click.setLocation(location);
        click.setValue(Integer.parseInt(value));

        return click;
    }
}
