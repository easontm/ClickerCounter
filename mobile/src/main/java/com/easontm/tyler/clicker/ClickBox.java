package com.easontm.tyler.clicker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.easontm.tyler.clicker.database.ClickerBaseHelper;
import com.easontm.tyler.clicker.database.ClickerCursorWrapper;
import com.easontm.tyler.clicker.database.ClickerDbSchema;
import com.easontm.tyler.clicker.database.ClickerDbSchema.ClickTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ClickBox is a single(ton) location for the rest of the program
 * to access Click information and interact with the DB, either
 * posting or querying information.
 *
 * Created by Tyler on 7/2/2016.
 */
public class ClickBox {

    private static ClickBox sClickBox;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private static final String TAG = "ClickBox";

    public static ClickBox get(Context context) {
        if (sClickBox == null) {
            sClickBox = new ClickBox(context);
        }
        return sClickBox;
    }

    private ClickBox(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ClickerBaseHelper(mContext)
                .getWritableDatabase();
    }


    private static ContentValues getContentValues(Click click) {
        ContentValues values = new ContentValues();
        values.put(ClickTable.Cols.UUID, click.getId().toString());
        values.put(ClickTable.Cols.PARENT_ID, click.getParentId().toString());
        values.put(ClickTable.Cols.TIMESTAMP, click.getTimestamp());
        values.put(ClickTable.Cols.LATITUDE, click.getLatitude());
        values.put(ClickTable.Cols.LONGITUDE, click.getLongitude());
        values.put(ClickTable.Cols.VALUE, click.getValue());

        return values;
    }

    private ClickerCursorWrapper queryClicks(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ClickerDbSchema.ClickTable.NAME,
                null, // all columns
                whereClause,
                whereArgs,
                null, //group by
                null, //having
                null //order by
        );

        return new ClickerCursorWrapper(cursor);
    }

    public void addClick(Click c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(ClickTable.NAME, null, values);
        Log.i(TAG, "Click inserted: " + c.getValue() + ", Time: " + c.getTimestamp() +
                ", Latitude: " + c.getLatitude() + ", Longitude: " + c.getLongitude());
    }

    public List<Click> getClicks(UUID parentId) {
        List<Click> clicks = new ArrayList<>();

        ClickerCursorWrapper cursor = queryClicks(
                ClickTable.Cols.PARENT_ID + " = ?",
                new String[] { parentId.toString() }
        );

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                clicks.add(cursor.getClick());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return clicks;
    }

    public int getClickCount(Clicker c) {
        UUID clickerId = c.getId();
        Cursor cursor = mDatabase.rawQuery("SELECT SUM(" + ClickTable.Cols.VALUE +
                ") FROM " + ClickTable.NAME + " WHERE " + ClickTable.Cols.PARENT_ID + " = ?",
                new String[] { clickerId.toString() });

        try {
            if (cursor.getCount() == 0) {
                return 0;
            }
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            cursor.close();
        }
    }

    public int getNumOfClicks(Clicker c) {
        UUID clickerId = c.getId();
        Cursor cursor = mDatabase.rawQuery("SELECT COUNT(*) FROM " + ClickTable.NAME +
                " WHERE " + ClickTable.Cols.PARENT_ID + " = ?", new String[] { clickerId.toString()});

        try {
            if (cursor.getCount() == 0) {
                return 0;
            }
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            cursor.close();
        }
    }

    public void deleteClicks(UUID parentId) {
        String uuidString = parentId.toString();
        mDatabase.delete(ClickTable.NAME,
                ClickTable.Cols.PARENT_ID + " = ?",
                new String[] { uuidString });
    }
}
