package com.example.ala.musicalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import static com.example.ala.musicalarm.DBConstants.*;

public class ChooseDateFragment extends Fragment implements
        TimePickerDialog.OnTimeSetListener
{

    DateFragmentInterface mCallback;
    boolean[] daysPicked = new boolean[7];
    private  int volume =50;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    SQLDBHelper sqldbHelper;
    private int minutes=0;
    private int hours=0;
    private boolean isRepeatOn;
    private boolean isShuffleOn;
    private boolean isVibrationOn;
    public ArrayList<Alarm> alarmListF;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup parent, Bundle savedInstanceState) {


    sqldbHelper= new SQLDBHelper(getActivity());

        AlarmDatabase alarmsDatabase = new AlarmDatabase(getActivity());
        SQLiteDatabase db = sqldbHelper.getReadableDatabase();
long length =
        DatabaseUtils.queryNumEntries(db,TABLE_NAME);

        alarmListF= new ArrayList<Alarm>();

     for(int i =0; i<length;i++){
        alarmListF.add( alarmsDatabase.getAlarm(i));
     }
db.close();

        final ScrollView scrollView =
                (ScrollView) inflater.inflate(R.layout.fragment_choose_date, parent,false );
                dateListBuilder(scrollView,inflater);


        final Button mMondayButton = (Button) scrollView.findViewById(R.id.buttonMonday);
        mMondayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),R.string.Monday,Toast.LENGTH_SHORT).show();
                if(!daysPicked[0]){ mMondayButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    daysPicked[0]=true;

                }else {daysPicked[0]=false;
                    mMondayButton.setBackgroundColor(getResources().getColor(R.color.colorMenu));}

            }
        });
        final Button mTuesdayButton = (Button) scrollView.findViewById(R.id.buttonTuesday);
        mTuesdayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),R.string.Tuesday,Toast.LENGTH_SHORT).show();
                if(!daysPicked[1]){
                    mTuesdayButton.setBackgroundColor(getResources()
                            .getColor(R.color.colorPrimaryDark));
                daysPicked[1]=true;
            }else{ daysPicked[1]=false;
                mTuesdayButton.setBackgroundColor(getResources().getColor(R.color.colorMenu));
            }}
        });

        final Button mWednesdayButton = (Button) scrollView.findViewById(R.id.buttonWednesday);
        mWednesdayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),R.string.Wednesday,Toast.LENGTH_SHORT).show();
                if(!daysPicked[2]){mWednesdayButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    daysPicked[2]=true;
                }else{
                    daysPicked[2]=false;
                    mWednesdayButton.setBackgroundColor(getResources().getColor(R.color.colorMenu));
                }
            }
        });

        final Button mThursdayButton = (Button) scrollView.findViewById(R.id.buttonThursday);
        mThursdayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),R.string.Thursday,Toast.LENGTH_SHORT).show();
                if(!daysPicked[3]){mThursdayButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    daysPicked[3]=true;

                } else {
                    daysPicked[3]=false;
                    mThursdayButton.setBackgroundColor(getResources().getColor(R.color.colorMenu));
                }
            }
        });

        final Button mFridayButton = (Button) scrollView.findViewById(R.id.buttonFriday);
        mFridayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),R.string.Friday,Toast.LENGTH_SHORT).show();
                if(!daysPicked[4]){mFridayButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    daysPicked[4]=true;
                }else {
                    daysPicked[4]=false;
                    mFridayButton.setBackgroundColor(getResources().getColor(R.color.colorMenu));
                }
            }
        });

        final Button mSaturdayButton = (Button) scrollView.findViewById(R.id.buttonSaturday);
        mSaturdayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),R.string.Saturday,Toast.LENGTH_SHORT).show();
                if(!daysPicked[5]){mSaturdayButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    daysPicked[5]=true;
                }else {
                    daysPicked[5] = false;
                    mSaturdayButton.setBackgroundColor(getResources().getColor(R.color.colorMenu));
                }
            }
        });

        final Button mSundayButton = (Button) scrollView.findViewById(R.id.buttonSunday);
        mSundayButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),R.string.Sunday,Toast.LENGTH_SHORT).show();
                if(!daysPicked[6]){mSundayButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    daysPicked[6]=true;
                }else{
                    daysPicked[6] = false;
                    mSundayButton.setBackgroundColor(getResources().getColor(R.color.colorMenu));
                }
            }
        });



        Button mSetButton = (Button) scrollView.findViewById(R.id.setAlarm);
        mSetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TimePicker timePicker = (TimePicker) scrollView.findViewById(R.id.timePicker);

                timePicker.clearFocus();
                onTimeSet(timePicker,timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());
            }
        });


        Button cancelAlarm = (Button) scrollView.findViewById(R.id.cancelAlarm);
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pendingIntent!=null && alarmManager!=null)
                alarmManager.cancel(pendingIntent);


                AlarmManager aManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
                Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
                PendingIntent pIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                aManager.cancel(pIntent);

            }
        });

        Button mButtonDrop = (Button) scrollView.findViewById(R.id.deleteEntries);
        mButtonDrop.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               sqldbHelper= new SQLDBHelper(getActivity());
                                               SQLiteDatabase db = sqldbHelper.getWritableDatabase();
                                               sqldbHelper.clearTable(db);
                                               db.close();
                                           }
                                       });

        return scrollView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Switch shuffleOn = (Switch) view.findViewById(R.id.switchShuffle);
        shuffleOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {  isShuffleOn = isChecked;

            }
        });

        Switch eachWeek = (Switch) view.findViewById(R.id.switchEachWeek);
        eachWeek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRepeatOn = isChecked;
            }
        });

        Switch vibrationOn = (Switch) view.findViewById(R.id.switchVibrate);
        vibrationOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isVibrationOn =isChecked;
            }
        });

        SeekBar volumeSeekbar = (SeekBar) view.findViewById(R.id.seekBarVolume);
        volumeSeekbar.setMax(100);
        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume= progress;
                final Toast toast=new Toast(getActivity());
                Toast.makeText(getActivity(),"Volume: "+volume, Toast.LENGTH_SHORT).show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {  }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {  }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Toast.makeText(getActivity()," "+hourOfDay +" "+minute, Toast.LENGTH_SHORT).show();

        minutes=minute;
        hours=hourOfDay;

        alarmManager =(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        boolean b =false;
        for(int i=0; i<7; i++){
            if(daysPicked[i]){
                int t=(((i+1)%7)+1); //offset calendar
                setAlarmOnDay(t);
                AlarmDatabase alarmsDatabase = new AlarmDatabase(getActivity());

                Alarm alarm = new Alarm(t, hours, minutes, volume,
                        isVibrationOn, isShuffleOn, isRepeatOn);
                alarmsDatabase.addAlarm(alarm);
            }
        }

        if(daysPicked[0]==daysPicked[1]== daysPicked[2]==daysPicked[3]==
                daysPicked[4]==daysPicked[5]==daysPicked[6]==false){
            b=true;}


        if(b) {
            Calendar calendar = Calendar.getInstance();
            int t = calendar.get(Calendar.DAY_OF_WEEK);
            setAlarmOnDay(t);
            AlarmDatabase alarmsDatabase = new AlarmDatabase(getActivity());
            Alarm alarm = new Alarm(t, hours, minutes, volume,
                    isVibrationOn, isShuffleOn, isRepeatOn);
            alarmsDatabase.addAlarm(alarm);
        }



    }

    private void setAlarmOnDay( int dayInt){


        Log.d("MyActivity", "Alarm On");
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setLenient(true);
        calendar.set(Calendar.DAY_OF_WEEK,dayInt);


        Intent myIntent = new Intent(getActivity(), AlarmReceiver.class);

        Bundle myBundle = new Bundle();

        myBundle.putInt(MainActivity.V,volume);
        myBundle.putBoolean(MainActivity.Ra,isShuffleOn);
        myBundle.putBoolean(MainActivity.Vib,isVibrationOn);

        myIntent.putExtras(myBundle);

        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        if(isRepeatOn){alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),7*24*60*60*1000,pendingIntent);}
        else{alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);}



    }

//inits

    void dateListBuilder(ScrollView scrollView, LayoutInflater inflater){


        LinearLayout linearLayout = (LinearLayout) scrollView.findViewById(R.id.LLDates);
        for (Alarm a: alarmListF) {

            View view  = inflater.inflate(R.layout.alarm_item_layout, linearLayout, false);

            TextView daysView = (TextView) view.findViewById(R.id.alarm_days);
            TextView hourView = (TextView) view.findViewById(R.id.alarm_hour);
            TextView shuffleView = (TextView) view.findViewById(R.id.shuffleIndicator);
            TextView repeatView = (TextView) view.findViewById(R.id.repeatIndicator);
            TextView vibrationView = (TextView) view.findViewById(R.id.vibrationIndicator);

            String s="";
            int d;
            d=a.getDay();
            switch (d){
                case 1:
                    s=getString(R.string.Sunday);
                    break;
                case 2:
                    s=getString(R.string.Monday);
                    break;
                case 3:
                    s=getString(R.string.Tuesday);
                    break;
                case 4:
                    s=getString(R.string.Wednesday);
                    break;
                case 5:
                    s=getString(R.string.Thursday);
                    break;
                case 6:
                    s=getString(R.string.Friday);
                    break;
                case 7:
                    s=getString(R.string.Saturday);
                    break;
                default:
                    break;
            }


            daysView.setText(s);
            String e= "";
            e= a.getHour()+ " : " + a.getMinute();
            hourView.setText(e);
            if(a.isShuffle()){shuffleView.setText(" Yes ");}else {shuffleView.setText(" No ");}
            if(a.isRepeat()){repeatView.setText(" Yes ");}else {repeatView.setText(" No ");}
            if(a.isVibrate()){vibrationView.setText(" Yes ");}else {vibrationView.setText(" No ");}



            linearLayout.addView(view);
        }


    }

    void initializeDays(boolean [] b){
    for (int i=0; i<7;i++){ b[i]=false; }   }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        initializeDays(daysPicked);
    }


    //activity communication
    public interface DateFragmentInterface{
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (DateFragmentInterface) activity;
        }catch (ClassCastException ex){
            Log.e("DateFragment", "must implement DateFragmentInterface", ex );
        }
    }


}