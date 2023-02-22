package com.bansi.eventreminder.helper;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.util.TimeUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bansi.eventreminder.R;
import com.bansi.eventreminder.activity.EventDetailActivity;
import com.bansi.eventreminder.activity.EventsActivity;
import com.bansi.eventreminder.activity.ForeGroundDialogActivity;
import com.bansi.eventreminder.activity.NotificationView;
import com.bansi.eventreminder.activity.PredefinedMessageActivity;
import com.bansi.eventreminder.activity.SendMessageActivity;
import com.bansi.eventreminder.activity.ValidationDialog;
import com.bansi.eventreminder.model.EventModel;
import com.bansi.eventreminder.sqlite.DbHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.bansi.eventreminder.activity.EventsActivity.NOTIFICATION_CHANNEL_ID;

public class NotificationService extends Service {

    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    ArrayList<EventModel> eventModels = new ArrayList<>();
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notification;
    DbHandler dbhandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        startTimer();
        return START_STICKY;
    }
    @Override
    public void onCreate() {
        eventModels.clear();
        Log.e(TAG, "onCreate");
        this.startForeground();

    }
    private void startForeground(){

    }
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        timer.schedule(timerTask, 5000,1000); //
    }
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stoptimertask();
        Intent broadcastReceiver=new Intent(this,MyNotificationPublisher.class);
        sendBroadcast(broadcastReceiver);
    }
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {

                dbhandler=new DbHandler(NotificationService.this);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");

                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                String currentDate = sdf.format(date);
                eventModels.clear();
                eventModels.addAll(dbhandler.completedEventList());

                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                Date currentLocalTime = cal.getTime();
                DateFormat current_date = new SimpleDateFormat("HH:mm a");
                current_date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

                Date d=new Date();

                SimpleDateFormat sdf1=new SimpleDateFormat("hh:mm a");

                String currentTimeString = sdf1.format(d);

                Log.w("current time.........",""+currentTimeString);

                for(int i=0; i<eventModels.size(); i++)
                {
                    String eventName=eventModels.get(i).getEventName();
                    String StrEventDate = eventModels.get(i).getEventDate();
                    Date eventDate = null;
                    Date todayDate = null;
                    try {
                        eventDate = sdf.parse(StrEventDate);
                        todayDate = sdf.parse(currentDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(eventDate.getTime() == todayDate.getTime())
                    {
                        String eventTime = eventModels.get(i).getTime();
                        Log.w("EVENT time.........",""+eventTime);

                        if(checktimings(currentTimeString,eventTime)) {
                            dbhandler.updateevent(eventModels.get(i).getId(),"1");

                            if (appInForeground(NotificationService.this)){
                                ForeGroundDialogActivity.getInstance().showDialog(eventModels.get(i).getDescription(),eventModels.get(i).getEventName(),eventModels.get(i).getTime(),eventModels.get(i).getEventDate(),day(eventModels.get(i).getEventDate()));

                            }else {
                                setNotify(i);
                            }
                            cancel();
                        }
                    }
                }
            }
        };
    }
    private boolean checktimings(String time, String endtime) {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

        Date d=new Date();
        SimpleDateFormat sdf1=new SimpleDateFormat("hh:mm a");
        String currentDateTimeString = sdf1.format(d);
        Log.w("current time.........",""+currentDateTimeString);

        Date date1 = new Date();
        Date date2 = new Date();

        try {
            date1 = sdf.parse(currentDateTimeString);
            date2 = sdf.parse(endtime);

            long difference = date1.getTime() - date2.getTime();

            int days = (int) (difference / 1000*60*60*24);
            int hours = (int) ((difference - 1000*60*60*24*days) / (1000*60*60));
            long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);

            Log.e("Minutes = " , minutes + " Hours = " + hours + "   Days " + days + " Difference = " + difference);

            if(date1.compareTo(date2) > 0) {
                Log.e("Date Compare", "false"  + sdf.format(date2) + "  " + sdf.format(date1));
            }
            else if(date1.compareTo(date2) < 0) {
                Log.e("Date Compare", "false" );
            }
            else
            {
                Log.e("Date Compare", "true"  + sdf.format(date2) + "  " + sdf.format(date1));

            }

            if(minutes >= 0) {
                return true;

            } else {

                return false;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return false;
    }
    public String day(String day){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        SimpleDateFormat sd = new SimpleDateFormat("EEEE");
        Date eventDate = null;
        String str=null;
        try {
            eventDate = sdf.parse(day);
            str=sd.format(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  str;
    }
    public void setNotify(int position){
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Reminders", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription(eventModels.get(position).getDescription());
                // Configure the notification channel.
                notificationChannel.enableLights(true);
                //notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);

                notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setColor(getResources().getColor(R.color.white))
                .setSmallIcon(R.mipmap.swayam_notification)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.swayam_notification))

                //.setTicker("ticker value")
                .setContentTitle(eventModels.get(position).getEventName())
                .setAutoCancel(true)
                .setContentText("Reminder Message");

        sendNotification(position);
    }
    public void sendNotification(int position){

        notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

     //Intent mIntent = new Intent(this, EventsActivity.class);
     Intent notificationIntent = new Intent(this, PredefinedMessageActivity.class)
       .putExtra("name",eventModels.get(position).getEventName())
       .putExtra("id",Prefs.getPreference(this,Constant.ID))
             .putExtra("category",Prefs.getPreference(this,Constant.CATEGORY));

     /*  .putExtra("date",eventModels.get(position).getEventDate())
       .putExtra("day",day(eventModels.get(position).getEventDate()))
       .putExtra("des",eventModels.get(position).getDescription())
       .putExtra("time",eventModels.get(position).getTime());*/

     notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
             | Intent.FLAG_ACTIVITY_SINGLE_TOP);

     PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
     notification.setContentIntent(pendingIntent);
     Notification notification1=notification.build();

     notification1.flags |= Notification.FLAG_AUTO_CANCEL;
     notification1.defaults |= Notification.DEFAULT_SOUND;
     notificationManager.notify(0,notification1);
    }
    private boolean appInForeground(@NonNull Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (runningAppProcess.processName.equals(context.getPackageName()) &&
                    runningAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }


}
