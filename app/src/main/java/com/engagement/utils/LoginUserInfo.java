package com.engagement.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.engagement.EngagementSdk;


public class LoginUserInfo {

    private static final String LOGIN_USER_PREFERENCE_NAME = "engagement_user_preferences";
    private static SharedPreferences entertainerGoUserPrefs;


    private static void initializeSharedPreferences() {
        if (entertainerGoUserPrefs == null)
            entertainerGoUserPrefs = EngagementSdk.getSingletonInstance().getContext().getSharedPreferences(
                    LOGIN_USER_PREFERENCE_NAME,
                    Activity.MODE_PRIVATE);
    }
    private static void initializeSharedPreferences(Context context) {
        if (entertainerGoUserPrefs == null)
            entertainerGoUserPrefs = context.getSharedPreferences(
                    LOGIN_USER_PREFERENCE_NAME,
                    Activity.MODE_PRIVATE);
    }
    public static String getValueForKey(String key, String defaultValue) {
        initializeSharedPreferences();
        if (entertainerGoUserPrefs != null)
            return entertainerGoUserPrefs.getString(
                    key, defaultValue);
        return null;
    }
    public static String getValueForKey(String key, String defaultValue, Context context) {
        initializeSharedPreferences(context);
        if (entertainerGoUserPrefs != null)
            return entertainerGoUserPrefs.getString(
                    key, defaultValue);
        return null;
    }
    public static boolean getValueForKey(String key, boolean defaultValue) {
        initializeSharedPreferences();
        if (entertainerGoUserPrefs != null)
            return entertainerGoUserPrefs.getBoolean(
                    key, defaultValue);
        return false;
    }

    public static void setValueForKey(String key, boolean value) {
        initializeSharedPreferences();
        if (entertainerGoUserPrefs != null)
            entertainerGoUserPrefs.edit().putBoolean(key, value).apply();
    }

    public static int getValueForKey(String key, int defaultValue) {
        initializeSharedPreferences();
        if (entertainerGoUserPrefs != null)
            return entertainerGoUserPrefs.getInt(
                    key, defaultValue);
        return 0;

    }

    public static void setValueForKey(String key, String value) {
        initializeSharedPreferences();
        if (entertainerGoUserPrefs != null)
            entertainerGoUserPrefs.edit().putString(key, value).apply();
    }

}
