package com.bansi.eventreminder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bansi.eventreminder.R;
import com.bansi.eventreminder.helper.Constant;
import com.bansi.eventreminder.helper.Prefmanager;
import com.bansi.eventreminder.helper.Prefs;
import com.bansi.eventreminder.model.CategoryModel;
import com.bansi.eventreminder.sqlite.DbHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import java.util.Date;

public class AddEventActivity extends AppCompatActivity /*implements AdapterView.OnItemSelectedListener*/ {

    @BindView(R.id.scScrolview)
    ScrollView mSc;

    @BindView(R.id.linear)
    LinearLayout mlinear;

    @BindView(R.id.tvHeader)
    TextView mtvHeader;

    @BindView(R.id.ivBack)
    ImageView mivBack;

    @BindView(R.id.etSDate)
    public  EditText metStartDate;

    @BindView(R.id.etDes)
    EditText metDes;

    @BindView(R.id.etSTime)
    EditText metSTime;

    @BindView(R.id.etName)
    EditText metName;

    @BindView(R.id.btnAddEvent)
    TextView mbtnAddEvent;

    @BindView(R.id.spCategory)
    Spinner mspCategory;
    boolean is_login = false;
  /*  @BindView(R.id.etCategory)
    EditText metCategory;*/

    Prefmanager prefmanager;
    private Calendar calendar;
    private int year, day, month;
    int  myHour, myMinute, eventDay;
    DbHandler handler;
    String dateC,date,currentDate;
    String msg_id;
    static boolean isDataLoaded = false;

    String id="";
    ArrayList<CategoryModel>categoryModels=new ArrayList<>();
   // ArrayList<CategoryModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ButterKnife.bind(this);
        mivBack.setVisibility(View.VISIBLE);
        metStartDate.setFocusable(false);
        metStartDate.setClickable(true);
        mtvHeader.setVisibility(View.VISIBLE);
        mtvHeader.setText("Add Event");
        metSTime.setFocusable(false);
        metSTime.setClickable(true);
        metDes.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Log.w("monthhhh",":"+month);
        currentDate(year,month,day);
        handler = new DbHandler(getApplicationContext());

      /*  if ( isDataLoaded )
        {
           // categoryModels = handler.getAllLabels();
            SpinnerData();

        }
        else
        {

           handler.insertLabel("");
        }
*/
     /*   if ( handler == null )
        {
            handler = new DbHandler(getApplicationContext());
        }

        if ( isDataLoaded )
        {
           SpinnerData();
        }else {
            handler.insertLabel("");
            }
*/

      /*  prefmanager=new Prefmanager(this);
        if (!prefmanager.isFirstTimeLaunch()) {

           // prefmanager.setFirstTimeLaunch(false);
            SpinnerData();

        }else {
            prefmanager.setFirstTimeLaunch(true);
            handler = new DbHandler(getApplicationContext());
            handler.insertLabel("");

        }
*/
        //handler.insertLabel("");
        SpinnerData();


    }
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1,arg2,arg3);
                }
            };
    private DatePickerDialog.OnDateSetListener currentdate=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            currentDate(year,month+1,dayOfMonth);
        }
    };

    public String time(String Time){
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
        Date eventDate = null;
        String str=null;
        try {
            eventDate = timeFormatter.parse(Time);
            str=time.format(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  str;
    }

    private void showDate(int year, int i, int day) {

        calendar.set(Calendar.HOUR_OF_DAY,day);
        calendar.set(Calendar.MONTH,i);
        calendar.set(Calendar.YEAR,year);
        date=day+"/"+(i+1)+"/"+year;
        metStartDate.setText(date);

    }
    private void currentDate(int year, int i, int day) {

         dateC=day+ "/" + (month+1) +"/"+year;
        // Log.e("current Dateeeeeeeeeeeee",":"+dateC);
    }

    @OnClick(R.id.btnAddEvent)
    public void onclickadd() {
        if (Valid()) {
            String name = metName.getText().toString();
            String edate = date;
            String time =metSTime.getText().toString();
            String description = metDes.getText().toString();
            currentDate = dateC;
            String category=mspCategory.getSelectedItem().toString();
            int id= handler.Categoryid(category);

            String is_completed = "0";
            handler.insertEvent(is_completed, name, edate, currentDate, time, description,category,id);
            Log.w("added", ":" + name);
            Intent i = new Intent(AddEventActivity.this, EventsActivity.class);
            startActivity(i);
            finish();
        }
    }
    @OnClick(R.id.ivBack)
    public void onclick() {
        categoryModels.clear();
        onBackPressed();
    }

    @OnClick(R.id.etSDate)
    public void onclickDate(){
        hideKeyboard();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                myDateListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
         eventDay=datePickerDialog.getDatePicker().getDayOfMonth();
    }

    TimePickerDialog .OnTimeSetListener myTimeListener=new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            myHour = hourOfDay;
            myMinute = minute;
            String currentDate=dateC;
            String  EDate=date;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");

            Date EventDate = null;
            Date todayDate = null;
            try {
                todayDate=sdf.parse(currentDate);
                EventDate=sdf.parse(EDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (todayDate.getTime() == EventDate.getTime()) {

                if (myHour < calendar.get(Calendar.HOUR_OF_DAY)) {
                    showDialog();
                    ValidationDialog.getInstance().mtvValidation1.setText("Event time required!");
                    ValidationDialog.getInstance().mtvValid.setText("Please enter valid time.");

                } else {

                    myHour = hourOfDay;
                    myMinute = minute;

                    showTime();
                }
            } else {

                myHour = hourOfDay;
                myMinute = minute;

                showTime();
            }
        }
    };
  /*  @OnClick(R.id.spCategory)
    public void cpinner(){

    }*/
    @OnItemSelected(R.id.spCategory)
    public void onIclickSpinner(){

        // msg.clear();
     /*     msg_id=  mspCategory.getSelectedItem().toString();
       id= handler.Categoryid(msg_id);
        */
        // SpinnerDatamsg();
        //list.clear();
        handler=new DbHandler(getApplicationContext());
       // list.clear();
        msg_id=mspCategory.getSelectedItem().toString();
        id= String.valueOf(handler.Categoryid(msg_id));
        Prefs.setPreference(this,Constant.ID, id);
        Prefs.setPreference(this,Constant.CATEGORY, msg_id);
    }
 /*  @OnItemSelected(R.id.spCategory)
    public void onIclickSpinner(){
      // msg.clear();
       *//* msg_id=*//*  mspCategory.getSelectedItem().toString();
      // id= handler.Categoryid(msg_id);
       SpinnerData();

      // id_interface.id(id);
      // Prefs.putString(Constant.ID, String.valueOf(id));
      // SpinnerDatamsg();
       //mspCategory.getSelectedItem().toString();
    }
*/
    @OnClick(R.id.etSTime)
    public void onclickstime(){
        hideKeyboard();
        calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog=new TimePickerDialog(this,myTimeListener,calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }
    private void showTime(){
        metSTime.setText(time(myHour+":"+myMinute));
    }
    public boolean Valid(){
    boolean is_error = true;

    if (metName.getText().toString().equals(""))
        {
            is_error=false;
            showDialog();
            ValidationDialog.getInstance().mtvValidation1.setText("Event name required!");
            ValidationDialog.getInstance().mtvValid.setText("Please enter event name. ");

        }else if (metDes.getText().toString().equals(""))
        {
            is_error=false;
            showDialog();
            ValidationDialog.getInstance().mtvValidation1.setText("Event description required!");
            ValidationDialog.getInstance().mtvValid.setText("Please enter event description. ");
        }
       else if (metStartDate.getText().toString().equals(""))
       {
        is_error=false;
        showDialog();
           ValidationDialog.getInstance().mtvValidation1.setText("Event date required!");
           ValidationDialog.getInstance().mtvValid.setText("Please enter event date. ");

       }
      else if (metSTime.getText().toString().equals("")){

              is_error=false;
             showDialog();
        ValidationDialog.getInstance().mtvValidation1.setText("Event time required!");
        ValidationDialog.getInstance().mtvValid.setText("Please select event time. ");
        }
        /*if (mspCategory.getSelectedItem().toString().trim().equals("")) {
            is_error=false;
            showDialog();
            ValidationDialog.getInstance().mtvValidation1.setText("Event category required!");
            ValidationDialog.getInstance().mtvValid.setText("Please select event category. ");
        }*/

         if (!is_error){
            return false;
        }else {
            return true;
        }
    }
    public void showDialog(){
        Dialog dialog=new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window=dialog.getWindow();
        dialog.setContentView(R.layout.activity_validation_dialog);

        window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        ValidationDialog.getInstance().mtvValid = dialog.findViewById(R.id.tvValidation);
        ValidationDialog.getInstance().mtvValidation1 = dialog.findViewById(R.id.tvValidation1);


        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        window.setGravity(Gravity.TOP);

        dialog.show();

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        },3000);
    }
    public void hideKeyboard(){
       try {
           InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
           imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

       } catch (Exception e) {
           // TODO: handle exception
       }
   }



    private void SpinnerData() {
//        prefmanager.setFirstTimeLaunch(false);
        categoryModels.clear();

        handler= new DbHandler(getApplicationContext());

            categoryModels.addAll(handler.getAllLabels());

            ArrayAdapter<CategoryModel> dataAdapter = new ArrayAdapter<CategoryModel>(this,
                    android.R.layout.simple_spinner_item, categoryModels);

            mspCategory.setAdapter(dataAdapter);


        Log.e("SpinnerData",":"+categoryModels.size());


    }
}






