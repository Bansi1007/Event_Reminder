package com.bansi.eventreminder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bansi.eventreminder.R;
import com.bansi.eventreminder.helper.NotificationService;

public class SplashActivity extends AppCompatActivity {

    Intent mservice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mservice=new Intent(this, NotificationService.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i =new Intent(SplashActivity.this,EventsActivity.class);
                startActivity(i);
                finish();

                if(!isMyServiceRunning(NotificationService.class)){
                    startService(mservice);
                }
            }
        },3000);
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}