package com.bansi.eventreminder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.card.MaterialCardView;
import com.bansi.eventreminder.R;

import com.bansi.eventreminder.adapter.EventAdapter;

import com.bansi.eventreminder.helper.Prefmanager;
import com.bansi.eventreminder.model.EventModel;
import com.bansi.eventreminder.sqlite.DbHandler;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class EventsActivity extends ForeGroundDialogActivity {

    public static EventsActivity instance = null;

    public EventsActivity() {
        instance = this;
    }

    public static synchronized EventsActivity getInstance() {
        if (instance == null) {
            instance = new EventsActivity();
        }
        return instance;
    }
    @BindView(R.id.ivMenu)
    ImageView mivMenu;


    @BindView(R.id.rel)
    RelativeLayout mfrm;

    @BindView(R.id.mtCalender)
    CalendarView mtCalender;

    @BindView(R.id.lnCal)
    LinearLayout mlnCal;

    @BindView(R.id.fbAdd)
    FloatingActionButton mAdd;

    @BindView(R.id.rvEvents)
    RecyclerView mRvEvents;

    @BindView(R.id.tvNoEvent)
    TextView mtvNoEvent;

    @BindView(R.id.ivDown)
    ImageView mivDown;

    @BindView(R.id.ivUp)
    ImageView mIvUp;

    @BindView(R.id.relative)
    RelativeLayout mrelative;

    @BindView(R.id.tvMonth)
    TextView mtvMonth;

    @BindView(R.id.tvYear)
    TextView mtvYear;

    EventAdapter eventAdapter;
    ArrayList<EventModel> eventModels = new ArrayList<>();
    ArrayList<EventModel> SelectedEvent = new ArrayList<>();
    DbHandler handler;
    Prefmanager prefmanager;

    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);

        ButterKnife.bind(this);
        eventModels.clear();
        mfrm.getForeground().setAlpha(0);



        Calendar calendar=Calendar.getInstance();
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year=calendar.get(Calendar.YEAR);
        mtvYear.setText(String.valueOf(year));
        mtvMonth.setText(month);
        LinearLayoutManager ln = new LinearLayoutManager(EventsActivity.this, RecyclerView.VERTICAL, false);
        mRvEvents.setLayoutManager(ln);
        eventAdapter = new EventAdapter(eventModels, this);
        mRvEvents.setAdapter(eventAdapter);

        mtCalender.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                Calendar clickedDayCalendar = eventDay.getCalendar();
                clickedDayCalendar.getTime();

                eventModels.clear();
                handler = new DbHandler(EventsActivity.this);
                eventModels.addAll(handler.selectedEvent(date(clickedDayCalendar.getTime())));
                eventAdapter.notifyDataSetChanged();

                if (!(eventModels.size() == 0)) {
                    mRvEvents.setVisibility(View.VISIBLE);
                    mtvNoEvent.setVisibility(View.GONE);
                } else {
                    mRvEvents.setVisibility(View.GONE);
                    mtvNoEvent.setVisibility(View.VISIBLE);
                }
                mlnCal.setVisibility(View.GONE);
                mIvUp.setVisibility(View.GONE);
                mivDown.setVisibility(View.VISIBLE);
                mfrm.getForeground().setAlpha(0);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventModels.clear();
        handler = new DbHandler(this);
        eventModels.addAll(handler.eventListToday());

        if (!(eventModels.size() == 0)) {
            mRvEvents.setVisibility(View.VISIBLE);
            mtvNoEvent.setVisibility(View.GONE);
        } else {
            mRvEvents.setVisibility(View.GONE);
            mtvNoEvent.setVisibility(View.VISIBLE);
        }
    }

    public String date(Date strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat sd = new SimpleDateFormat("dd/M/yyyy");
        Date eventDate = null;
        String str = null;
        try {
            eventDate = sdf.parse(String.valueOf(strDate));
            str = sd.format(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private List<Calendar> getSelectedDays() {

        List<Calendar> calendars = new ArrayList<>();
        for (int i = 0; i < eventModels.size(); i++) {
            Calendar calendar = DateUtils.getCalendar();
            calendar.setTime(strToDate(eventModels.get(i).getEventDate()));
            calendars.add(calendar);
        }
        return calendars;
    }

    public Date strToDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        Date eventDate = null;
        try {
            eventDate = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return eventDate;
    }

    @OnClick(R.id.lnCal)
    public void onclickrel() {

        mlnCal.setVisibility(View.GONE);
        mlnCal.setVisibility(View.GONE);
        mfrm.getForeground().setAlpha(0);
        mRvEvents.setVisibility(View.VISIBLE);
        mIvUp.setVisibility(View.GONE);
        mivDown.setVisibility(View.VISIBLE);

        if (!(eventModels.size() == 0)) {
            mRvEvents.setVisibility(View.VISIBLE);
            mtvNoEvent.setVisibility(View.GONE);
        } else {
            mRvEvents.setVisibility(View.GONE);
            mtvNoEvent.setVisibility(View.VISIBLE);
        }

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ivDown)
    public void onclickdown() {
        mlnCal.setVisibility(View.VISIBLE);
        mfrm.getForeground().setAlpha(180);
        mIvUp.setVisibility(View.VISIBLE);
        mivDown.setVisibility(View.GONE);

        List<EventDay> events = new ArrayList<>();
        SelectedEvent.clear();
        handler = new DbHandler(this);
        SelectedEvent.addAll(handler.eventList());
        for (int i = 0; i < SelectedEvent.size(); i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(strToDate(SelectedEvent.get(i).getEventDate()));

            if (handler.eventList().get(i).getIs_completed().equals("0")) {
                events.add(new EventDay(cal, R.drawable.pending_event));

            } else if (handler.eventList().get(i).getIs_completed().equals("1")) {
                events.add(new EventDay(cal, R.drawable.completed_event));
            }
        }
        mtCalender.setSelectedDates(getSelectedDays());
        mtCalender.setEvents(events);
        eventAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ivUp)
    public void onclickUp() {
        SelectedEvent.clear();
        handler = new DbHandler(this);
        SelectedEvent.addAll(handler.eventList());
        mlnCal.setVisibility(View.GONE);
        mfrm.getForeground().setAlpha(0);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);

        mIvUp.setVisibility(View.GONE);
        mivDown.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.fbAdd)
    public void onclickAdd() {

        Intent i = new Intent(EventsActivity.this, AddEventActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.ivMenu)
    public void OnClickMenu() {
        checkPermission();
        PopupMenu popupMenu = new PopupMenu(EventsActivity.this, mivMenu);

        popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.backup:
                        exportDB("EventsReminder.db");

                        break;
                     case R.id.restore:
                         restore();

                         break;
                    case R.id.Category:
                        Intent i =new Intent(EventsActivity.this,CategoryAddingActivity.class);
                        startActivity(i);

                        break;
                    case R.id.Msgs:
                        Intent in=new Intent(EventsActivity.this,PredefinedMessageActivity.class);
                        startActivity(in);
                        break;
                }return true;
                }

        });
        popupMenu.show();

    }


    public void checkPermission(){


        if (ContextCompat.checkSelfPermission(EventsActivity.this,
    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(EventsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        } else {
            ActivityCompat.requestPermissions(EventsActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                   100);
        }
    } else {
    }
    }

    private void exportDB(String db_name) {


        File sd = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                File.separator  +
                File.separator);

        boolean success = true;
        if (!sd.exists()) {
            success = sd.mkdir();
        }

        if (success) {

            File data = Environment.getDataDirectory();
            FileChannel source = null;
            FileChannel destination = null;
            String currentDBPath = "/data/" +"com.swayam.eventreminder" +"/databases/"+"EventsReminder.db";
            String backupDBPath = db_name;
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Toast.makeText(this, "Please wait"+backupDB.toString(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this, "Nooooooo", Toast.LENGTH_SHORT).show();
        }

    }

    public void restore() {


        try {
            File sd = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +
                    File.separator  +
                    File.separator);
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/" +"com.swayam.eventreminder" +"/databases/"+"EventsReminder.db";
                String backupDBPath = "EventsReminder.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(backupDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(getApplicationContext(), "Database Restored successfully", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "exception"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}