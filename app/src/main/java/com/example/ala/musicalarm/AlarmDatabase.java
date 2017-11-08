package com.example.ala.musicalarm;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static com.example.ala.musicalarm.DBConstants.*;

public class AlarmDatabase {


    private SQLDBHelper sqldbHelper;

    public AlarmDatabase (Context context) {
        this.sqldbHelper = new SQLDBHelper(context);
    }

    public void addAlarm(Alarm alarm) {
        SQLiteDatabase db = sqldbHelper.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DAY_OF_WEEK, alarm.getDay());
            values.put(COLUMN_HOUR, alarm.getHour());
            values.put(COLUMN_MINUTE, alarm.getMinute());

            values.put(COLUMN_VOLUME, alarm.getVolume());
            values.put(COLUMN_VIBRATION, alarm.isVibrate());
            values.put(COLUMN_SHUFFLE, alarm.isShuffle());

            db.insert(TABLE_NAME, null, values);

            Log.d(" db ", "  ok  ");


        } finally {
            db.close();
        }
    }

    public Alarm getAlarm(int position) {
        SQLiteDatabase db = sqldbHelper.getReadableDatabase();



        try {
            String[] projection = { COLUMN_DAY_OF_WEEK , COLUMN_HOUR , COLUMN_MINUTE ,

                    COLUMN_VOLUME ,COLUMN_VIBRATION,COLUMN_SHUFFLE, COLUMN_REPEAT};

            String sortOrder = COLUMN_DAY_OF_WEEK + " ASC";

            Cursor cursor = db.query(TABLE_NAME,  // The table to query
                    projection,                   // The columns to return
                    null,                         // The columns for the WHERE clause
                    null,                         // The values for the WHERE clause
                    null,                         // don't group the rows
                    null,                         // don't filter by row groups
                    sortOrder                     // The sort order
            );

            return new Alarm(position, cursor);

        } finally {
            db.close();

        }
    }
}
