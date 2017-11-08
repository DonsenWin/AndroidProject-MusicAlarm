package com.example.ala.musicalarm;


import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import static com.example.ala.musicalarm.DBConstants.*;


public class Alarm implements Parcelable {


    protected Alarm(Parcel in) {
        day = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        volume = in.readInt();
        vibrate = in.readByte() != 0;
        shuffle = in.readByte() != 0;
        repeat = in.readByte() != 0;
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

     public int getVolume() {
        return volume;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public boolean isRepeat() {
        return repeat;
    }

    private int     day;
    private int        hour;
    private int        minute;
    private int        volume;
    private boolean    vibrate;
    private boolean    shuffle;
    private boolean    repeat;



    public Alarm(int day,
                 int hour, int minute,int volume,
                 boolean vibrate, boolean shuffle,boolean repeat) {

        this.day=day;
        this.hour=hour;
        this.minute=minute;
        this.volume=volume;
        this.vibrate=vibrate;
        this.shuffle=shuffle;
        this.repeat=repeat;
    }

    public Alarm(int position, Cursor c) {
try{
    c.moveToPosition(position);

        day = c.getInt(ICOLUMN_DAY_OF_WEEK);
        hour = c.getInt(ICOLUMN_HOUR);
        minute = c.getInt(ICOLUMN_MINUTE);
        volume =c.getInt(ICOLUMN_VOLUME);
        vibrate = c.getInt(ICOLUMN_VIBRATION) == 1;
        shuffle = c.getInt(ICOLUMN_SHUFFLE)==1;
        repeat= c.getInt(ICOLUMN_REPEAT)==1;


        c.close();
    }finally {
    c.close();
    }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int flags) {

        p.writeInt(day);
        p.writeInt(hour);
        p.writeInt(minute);
        p.writeInt(volume);
        p.writeInt(vibrate ? 1 : 0);
        p.writeInt(shuffle ? 1 : 0);
        p.writeInt(repeat ? 1 :0);

    }

    }

