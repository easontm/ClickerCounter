package com.easontm.tyler.clicker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.easontm.tyler.clicker.database.ClickerBaseHelper;
import com.easontm.tyler.clicker.database.ClickerCursorWrapper;
import com.easontm.tyler.clicker.database.ClickerDbSchema.ClickerTable;
import com.easontm.tyler.clicker.database.ClickerDbSchema.ClickTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by drink on 6/5/2016.
 */
public class ClickerBox {
    private static ClickerBox sClickerBox;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ClickerBox get(Context context) {
        if (sClickerBox == null) {
            sClickerBox = new ClickerBox(context);
        }
        return sClickerBox;
    }

    private ClickerBox(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ClickerBaseHelper(mContext)
                .getWritableDatabase();
    }

    public List<Clicker> getClickers() {
        List<Clicker> clickers = new ArrayList<>();

        ClickerCursorWrapper cursor = queryClickers(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                clickers.add(cursor.getClicker());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return clickers;
    }

    private ClickerCursorWrapper queryClickers(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ClickerTable.NAME,
                null, // all columns
                whereClause,
                whereArgs,
                null, //group by
                null, //having
                null //order by
        );

        return new ClickerCursorWrapper(cursor);
    }

    public Clicker getClicker(UUID id) {
        ClickerCursorWrapper cursor = queryClickers(
                ClickerTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getClicker();
        } finally {
            cursor.close();
        }
    }

    public void updateClicker(Clicker clicker) {
        String uuidString = clicker.getId().toString();
        ContentValues values = getContentValues(clicker);

        mDatabase.update(ClickerTable.NAME, values,
                ClickerTable.Cols.UUID + " = ?",
                new String[] { uuidString} );
    }

    private static ContentValues getContentValues(Clicker clicker) {
        ContentValues values = new ContentValues();
        values.put(ClickerTable.Cols.UUID, clicker.getId().toString());
        values.put(ClickerTable.Cols.TITLE, clicker.getTitle());
        values.put(ClickerTable.Cols.COUNT, clicker.getCount());
        values.put(ClickerTable.Cols.GOAL, clicker.getGoal());
        values.put(ClickerTable.Cols.TYPE, clicker.getType());
        values.put(ClickerTable.Cols.LOCATION_STATUS, String.valueOf(clicker.isLocationOn()));

        return values;
    }

    public void addClicker(Clicker c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(ClickerTable.NAME, null, values);
    }

    public void deleteClicker(Clicker c) {
        String uuidString = c.getId().toString();
        mDatabase.delete(ClickerTable.NAME,
                ClickerTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }
}
