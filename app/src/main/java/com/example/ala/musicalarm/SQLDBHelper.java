package com.example.ala.musicalarm;


import static com.example.ala.musicalarm.DBConstants.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +

                    COLUMN_DAY_OF_WEEK + " INTEGER, " +
                    COLUMN_HOUR + " INTEGER, " +
                    COLUMN_MINUTE + " INTEGER, " +
                    COLUMN_VOLUME +" INTEGER, " +
                    COLUMN_VIBRATION + " INTEGER, " +
                    COLUMN_SHUFFLE + " INTEGER, " +
                    COLUMN_REPEAT + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public SQLDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    public void clearTable(SQLiteDatabase db){
        db.execSQL("delete from "+ TABLE_NAME);

    }



}
