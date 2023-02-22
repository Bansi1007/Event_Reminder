package com.bansi.eventreminder.helper;

import android.bluetooth.le.AdvertiseData;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lincoln on 05/05/16.
 */
public class Prefmanager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME ="welcome";

    public static  String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String SET_ADDRESS = "SetAddress";

    public Prefmanager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setString(String address) {
        editor.putString(SET_ADDRESS, address);
        editor.commit();
    }

    public String getString() {
        return pref.getString(SET_ADDRESS, "");
    }
}
