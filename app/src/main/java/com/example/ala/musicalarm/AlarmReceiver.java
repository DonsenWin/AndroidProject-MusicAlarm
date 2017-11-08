package com.example.ala.musicalarm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

public class AlarmReceiver extends WakefulBroadcastReceiver{

    @Override
    public void onReceive(final Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        extras.putBoolean(MainActivity.A,true);


        Intent launchIntent = new Intent(context   , MainActivity.class);
        launchIntent.putExtras(extras);


        //Intent launchIntent = packageManager.getLaunchIntentForPackage("com.example.ala.musicalarm");
       launchIntent.setFlags(
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|
                        Intent.FLAG_ACTIVITY_NEW_TASK|
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED

        );
        context.startActivity(launchIntent);

        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

    }
    }