package com.bansi.eventreminder.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.bansi.eventreminder.activity.EventsActivity.NOTIFICATION_CHANNEL_ID;

public class MyNotificationPublisher extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,NotificationService.class));
    }
}
