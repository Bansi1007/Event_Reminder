package com.bansi.eventreminder.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bansi.eventreminder.model.CategoryModel;
import com.bansi.eventreminder.model.EventModel;
import com.bansi.eventreminder.model.ListMsgs;
import com.bansi.eventreminder.model.MessageModel;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    Context context;
    SQLiteDatabase database;
    static int count = 0;
    public static final String DBNAME = "EventsReminder.db";
    public static final int DBVERSION = 2;
    public static final int NEW_DBVERSION = 3;
    public static final String TABLE_NAME = "event";
    public static final String CATEGORY_TABLE = "category_table";
    public static final String MSG_TABLE = "msg_table";
    public static final String ID = "id";
    public static final String EVENT_NAME = "eventName";
    public static final String EVENT_DATE = "event_date";
    public static final String IS_COMPLETED = "isCompleted";
    public static final String CURRENT_DATE = "currentDate";
    public static final String EVENT_TIME = "eventTime";
    public static final String EVENT_DESCRIPTION = "eventDescription";
    public static final String EVENT_Category = "eventCategory";
    public static final String EVENT_Category_Add = "eventCategoryAdd";
    public static final String EVENT_Category_ID = "eventCategoryID";
    public static final String PREDEFINED_MSG = "predefined_msg";
    public static final String MSG_ID = "msg_id";


    ArrayList<EventModel> eventModel = new ArrayList<>();
    ArrayList<EventModel> SelectedEvent = new ArrayList<>();
    ArrayList<CategoryModel> categoryModels = new ArrayList<>();
   ArrayList<MessageModel> messageModels = new ArrayList<>();
   // ArrayList<ListMsgs> messageModels = new ArrayList<>();

    // List<String> list = new ArrayList<String>();
    public DbHandler(@Nullable Context context) {
        super(context, DBNAME, null, 2);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + IS_COMPLETED + " TEXT," + EVENT_NAME + " TEXT, " + EVENT_DESCRIPTION + " TEXT, " + CURRENT_DATE + " TEXT," + EVENT_DATE + " TEXT," + EVENT_TIME + " TEXT," + EVENT_Category + " TEXT, " + EVENT_Category_ID + " INTEGER"+
                ")";
        db.execSQL(CREATE_TABLE);


            db.execSQL("CREATE TABLE IF NOT EXISTS " + CATEGORY_TABLE + "(" + EVENT_Category_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + EVENT_Category_Add + " TEXT" +
                ")");


        db.execSQL("CREATE TABLE IF NOT EXISTS " + MSG_TABLE + "(" + PREDEFINED_MSG + " TEXT, " + EVENT_Category_ID + " INTEGER," + MSG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


      /*  if (oldVersion<12){
             db.execSQL("ALTER TABLE "+ TABLE_NAME + " ADD COLUMN "+ EVENT_Category +" TEXT ");

        }else if (oldVersion<11){
            db.execSQL("CREATE TABLE IF NOT EXISTS " + CATEGORY_TABLE + "(" + EVENT_Category_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ EVENT_Category_Add + " TEXT" +
                    ")");

        }*/
    }

    public void insertEvent(String is_completed, String name, String date, String currentDate, String time, String description, String category,int id_category) {
        database = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(IS_COMPLETED, is_completed);
        cv.put(EVENT_NAME, name);
        cv.put(EVENT_DATE, date);
        cv.put(CURRENT_DATE, currentDate);
        cv.put(EVENT_TIME, time);
        cv.put(EVENT_DESCRIPTION, description);
        cv.put(EVENT_Category, category);
        cv.put(EVENT_Category_ID,id_category);
        //cv.put(EVENT_DAY,day);

        long rowInserted = database.insert(TABLE_NAME, null, cv);
        if (rowInserted != -1)
            Log.w("New row added", ":" + rowInserted);
        else
            Log.w("something went wrong", ":");

    }

    public ArrayList<EventModel> eventList() {
        database = this.getWritableDatabase();
        Cursor c = database.rawQuery("select * from " + TABLE_NAME + ";", new String[]{});
        //Cursor c = database.rawQuery("select * from " + TABLE_NAME + " where "+ EVENT_DATE + " = "+ CURRENT_DATE +";", new String[]{});

        if (c != null) {

            while (c.moveToNext()) {
                EventModel model = new EventModel();
                model.setEventName(c.getString(c.getColumnIndex(EVENT_NAME)));
                model.setDescription(c.getString(c.getColumnIndex(EVENT_DESCRIPTION)));
                model.setCurrentDate(c.getString(c.getColumnIndex(CURRENT_DATE)));
                model.setTime(c.getString(c.getColumnIndex(EVENT_TIME)));
                model.setId(c.getString(c.getColumnIndex(ID)));
                model.setIs_completed(c.getString(c.getColumnIndex(IS_COMPLETED)));
                model.setEventDate(c.getString(c.getColumnIndex(EVENT_DATE)));


                eventModel.add(model);

            }
        } else {

        }
        c.close();
        return eventModel;
    }

    public ArrayList<EventModel> eventListToday() {
        database = this.getWritableDatabase();
        // Cursor c = database.rawQuery("select * from " + TABLE_NAME  +";", new String[]{});
        Cursor c = database.rawQuery("select * from " + TABLE_NAME + " where " + EVENT_DATE + " = " + CURRENT_DATE + ";", new String[]{});

        if (c != null) {

            while (c.moveToNext()) {
                EventModel model = new EventModel();
                model.setEventName(c.getString(c.getColumnIndex(EVENT_NAME)));
                model.setDescription(c.getString(c.getColumnIndex(EVENT_DESCRIPTION)));
                model.setCurrentDate(c.getString(c.getColumnIndex(CURRENT_DATE)));
                model.setTime(c.getString(c.getColumnIndex(EVENT_TIME)));
                model.setId(c.getString(c.getColumnIndex(ID)));
                model.setIs_completed(c.getString(c.getColumnIndex(IS_COMPLETED)));
                model.setEventDate(c.getString(c.getColumnIndex(EVENT_DATE)));

                eventModel.add(model);

            }
        } else {

        }
        c.close();
        return eventModel;
    }

    public ArrayList<EventModel> completedEventList() {
        database = this.getWritableDatabase();
        Cursor c = database.rawQuery("select * from " + TABLE_NAME + " where " + IS_COMPLETED + " = '0' " + ";", null);
        if (c != null) {
            while (c.moveToNext()) {
                EventModel model = new EventModel();
                model.setEventName(c.getString(c.getColumnIndex(EVENT_NAME)));
                model.setDescription(c.getString(c.getColumnIndex(EVENT_DESCRIPTION)));
                model.setCurrentDate(c.getString(c.getColumnIndex(CURRENT_DATE)));
                model.setEventDate(c.getString(c.getColumnIndex(EVENT_DATE)));
                model.setTime(c.getString(c.getColumnIndex(EVENT_TIME)));
                model.setId(c.getString(c.getColumnIndex(ID)));
                model.setIs_completed(c.getString(c.getColumnIndex(IS_COMPLETED)));
                eventModel.add(model);
            }
        } else {
            Log.e("Cursor completeEvent ", "null");
        }
        c.close();
        database.close();
        return eventModel;
    }

    public boolean updateevent(String id, String is_completed) {
        database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(IS_COMPLETED, is_completed);

        database.update(TABLE_NAME, cv, ID + "=" + id, null);

        return true;

    }

    public ArrayList<EventModel> selectedEvent(String date) {
        database = this.getWritableDatabase();
        Cursor c = database.rawQuery("select * from " + TABLE_NAME + " where " + EVENT_DATE + "='" + date + "';", null);

        if (c != null) {
            while (c.moveToNext()) {
                EventModel model = new EventModel();
                model.setEventName(c.getString(c.getColumnIndex(EVENT_NAME)));
                model.setDescription(c.getString(c.getColumnIndex(EVENT_DESCRIPTION)));
                model.setCurrentDate(c.getString(c.getColumnIndex(CURRENT_DATE)));
                model.setEventDate(c.getString(c.getColumnIndex(EVENT_DATE)));
                model.setTime(c.getString(c.getColumnIndex(EVENT_TIME)));
                model.setId(c.getString(c.getColumnIndex(ID)));
                model.setIs_completed(c.getString(c.getColumnIndex(IS_COMPLETED)));
                SelectedEvent.add(model);
            }
        } else {
            Log.e("Cursor select event ", "null");
        }
        c.close();
        database.close();
        return SelectedEvent;

    }
    public void insertnewCategory(String label){
        categoryModels.clear();
        database=this.getWritableDatabase();

        ContentValues cv=new ContentValues();

        cv.put(EVENT_Category_Add,label);
        database.insert(CATEGORY_TABLE,null,cv);
        database.close();
    }
    public void insertLabel(String label) {
        categoryModels.clear();
        database = this.getWritableDatabase();
        categoryModels.clear();

        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();
        ContentValues values3 = new ContentValues();
        ContentValues values4 = new ContentValues();
        ContentValues values5 = new ContentValues();



        values1.put(EVENT_Category_Add,"Birthday");
        values2.put(EVENT_Category_Add,"Anniversary");
        values3.put(EVENT_Category_Add,"Meeting");
        values4.put(EVENT_Category_Add,"Conference");
        values5.put(EVENT_Category_Add,"Others");

       // values.put(EVENT_Category_Add, label);

        database.insert(CATEGORY_TABLE, null, values1);
        database.insert(CATEGORY_TABLE, null, values2);
        database.insert(CATEGORY_TABLE, null, values3);
        database.insert(CATEGORY_TABLE, null, values4);
        database.insert(CATEGORY_TABLE, null, values5);


        database.close();

    }
    public ArrayList<CategoryModel> getAllLabels() {
        categoryModels.clear();
        database = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + CATEGORY_TABLE;

        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {
                    CategoryModel model = new CategoryModel();
                    model.setId(cursor.getInt(cursor.getColumnIndex(EVENT_Category_ID)));
                    model.setName(cursor.getString(cursor.getColumnIndex(EVENT_Category_Add)));
                    categoryModels.add(model);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        return categoryModels;
    }
    public void insertMsg(String msg,int id) {

            database = this.getWritableDatabase();
           // messageModels.clear();
            ContentValues cv = new ContentValues();
            ContentValues cv1 = new ContentValues();
            ContentValues cv2 = new ContentValues();
            ContentValues cv3 = new ContentValues();
            ContentValues cv4 = new ContentValues();
            ContentValues cv5 = new ContentValues();
            ContentValues cv6 = new ContentValues();
            ContentValues cv7 = new ContentValues();
            ContentValues cv8 = new ContentValues();
            ContentValues cv9 = new ContentValues();
            ContentValues cv10 = new ContentValues();
            ContentValues cv11 = new ContentValues();
            ContentValues cv12 = new ContentValues();

            cv.put(PREDEFINED_MSG, "happy anniversary");
            cv1.put(PREDEFINED_MSG, "happy birthday");
            cv2.put(PREDEFINED_MSG, "many many happy birthday");
            cv3.put(PREDEFINED_MSG, "happy holy");
            cv4.put(PREDEFINED_MSG, "Meeting today");
            cv5.put(PREDEFINED_MSG, "conference today");
            cv6.put(PREDEFINED_MSG, "business meeting");
            cv7.put(PREDEFINED_MSG, "meeting at 8 o'clock");
            cv8.put(PREDEFINED_MSG, "meeting at 10 o'clock");
            cv9.put(PREDEFINED_MSG, "All the best");
            cv10.put(PREDEFINED_MSG, "God bless you");
            cv11.put(PREDEFINED_MSG, "live long");
            cv12.put(PREDEFINED_MSG, "have a happy and healthy year ahead");

            cv.put(EVENT_Category_ID, 2);
            cv1.put(EVENT_Category_ID, 1);
            cv2.put(EVENT_Category_ID, 1);
            cv3.put(EVENT_Category_ID, 5);
            cv4.put(EVENT_Category_ID, 3);
            cv5.put(EVENT_Category_ID, 4);
            cv6.put(EVENT_Category_ID, 3);
            cv7.put(EVENT_Category_ID, 3);
            cv8.put(EVENT_Category_ID, 3);
            cv9.put(EVENT_Category_ID, 3);
            cv10.put(EVENT_Category_ID, 1);
            cv11.put(EVENT_Category_ID, 1);
            cv12.put(EVENT_Category_ID, 1);

            database.insert(MSG_TABLE, null, cv);
            database.insert(MSG_TABLE, null, cv1);
            database.insert(MSG_TABLE, null, cv2);
            database.insert(MSG_TABLE, null, cv3);
            database.insert(MSG_TABLE, null, cv4);
            database.insert(MSG_TABLE, null, cv5);
            database.insert(MSG_TABLE, null, cv6);
            database.insert(MSG_TABLE, null, cv7);
            database.insert(MSG_TABLE, null, cv8);
            database.insert(MSG_TABLE, null, cv9);
            database.insert(MSG_TABLE, null, cv10);
            database.insert(MSG_TABLE, null, cv11);
            database.insert(MSG_TABLE, null, cv12);

            database.close();

    }
    public ArrayList<CategoryModel> categoryName() {
        categoryModels.clear();
        database = this.getReadableDatabase();
        String category_name = "";
        Cursor c = database.rawQuery("select " + EVENT_Category_Add + " from " + CATEGORY_TABLE, null);
        if (c != null) {
            while (c.moveToNext()) {

                CategoryModel model = new CategoryModel();
                model.setName(c.getString(c.getColumnIndex(EVENT_Category_Add)));
                categoryModels.add(model);
            }
        } else {
            Log.e("Cursor complete ", "null");
        }
        c.close();
        database.close();
        return categoryModels;
    }
    public int Categoryid(String name) {
        categoryModels.clear();
        database = this.getReadableDatabase();

        int id = 0;
        Cursor c = database.rawQuery("select " + EVENT_Category_ID + " from " + CATEGORY_TABLE + " where " + EVENT_Category_Add + "='" + name + "';", null);
        if (c != null) {
            while (c.moveToNext()) {

                CategoryModel model = new CategoryModel();
                id = (c.getInt(c.getColumnIndex(EVENT_Category_ID)));
                Log.w("id :: ", "" + id);
            }
        } else {
            Log.e("Cursor completeEvent ", "null");
        }
        c.close();
        database.close();
        return id;
    }
    public ArrayList<MessageModel> preDefinedMsgList(int id) {
        //messageModels.clear();
        database = this.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM " + MSG_TABLE + " Where " + EVENT_Category_ID + "=" + id + ";", null);
       // Cursor c = database.rawQuery("SELECT * FROM " + MSG_TABLE +  ";", null);
        if (c != null) {
            c.moveToFirst();
            while (c.moveToNext()) {

                MessageModel model = new MessageModel();
                model.setMsg(c.getString(c.getColumnIndex(PREDEFINED_MSG)));

                messageModels.add(model);
                Log.e("Message List Size ",":"+messageModels.size());
            }
        }
        c.close();
        database.close();
        return messageModels;
    }

}
