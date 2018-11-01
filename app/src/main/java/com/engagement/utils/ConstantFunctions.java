package com.engagement.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ConstantFunctions {

    public static Date getCurrentDateInGMT() {
        Date date = null;
        try {
            SimpleDateFormat sdfUTCTimeZoneFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdfUTCTimeZoneFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String DateInUTC = sdfUTCTimeZoneFormat.format(new Date());
            SimpleDateFormat sdfDefaultTimeZoneFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdfDefaultTimeZoneFormat.setTimeZone(TimeZone.getDefault());
            date = sdfDefaultTimeZoneFormat.parse(DateInUTC);
        } catch (Exception e) {

        }
        return date;
    }

    public static int getHeightPixels(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return height;
    }

    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
