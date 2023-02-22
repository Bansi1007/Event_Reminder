package com.bansi.eventreminder.helper;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Prefs {
    private static final String PREFS_NAME = "preferenceName";

    public static boolean setPreference(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getPreference(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, "defaultValue");
    }

    public static void putBoolean(Context context , String key , boolean value)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName() , Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key , value);
        editor.commit();
    }

    public static boolean getBoolean(Context context , String key)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName() , Activity.MODE_PRIVATE);
        return preferences.getBoolean(key , false);

    }


}