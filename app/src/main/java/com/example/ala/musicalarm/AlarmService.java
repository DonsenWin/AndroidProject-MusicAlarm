package com.example.ala.musicalarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;


public class AlarmService extends IntentService {
    private Bundle extras;

    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    public void onHandleIntent(Intent intent) {
        extras = intent.getExtras();
        sendNotification("Alarm!");

    }

    private void sendNotification(String msg) {
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent i = new Intent(this, MainActivity.class);

        i.putExtras(extras);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                i, 0);

        NotificationCompat.Builder alarmNotificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                this).setContentTitle(msg).setSmallIcon(R.drawable.close_hover)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(9, alarmNotificationBuilder.build());


    }

}
