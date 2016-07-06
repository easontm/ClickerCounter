package com.easontm.tyler.clicker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.easontm.tyler.clicker.database.ClickerDbSchema.ClickerTable;
import com.easontm.tyler.clicker.database.ClickerDbSchema.ClickTable;

/**
 * Created by drink on 6/5/2016.
 */
public class ClickerBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "clickerBase.db";

    public ClickerBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ClickerTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            ClickerTable.Cols.UUID + ", " +
            ClickerTable.Cols.TITLE + ", " +
            ClickerTable.Cols.COUNT + ", " +
            ClickerTable.Cols.GOAL + ", " +
            ClickerTable.Cols.TYPE + ")"
        );

        db.execSQL("create table " + ClickTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ClickTable.Cols.UUID + ", " +
                ClickTable.Cols.PARENT_ID + ", " +
                ClickTable.Cols.TIMESTAMP + ", " +
                ClickTable.Cols.LOCATION + ", " +
                ClickTable.Cols.VALUE + ", " +
                " FOREIGN KEY (" + ClickTable.Cols.PARENT_ID + ") REFERENCES " +
                ClickerTable.NAME + "(_id));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
