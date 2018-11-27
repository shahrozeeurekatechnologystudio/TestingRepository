package com.engagement.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.engagement.R;
import com.squareup.picasso.Picasso;

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
    public static void loadRGBImage(Context context, String path, ImageView imageView) {
        if(path != null && !path.equalsIgnoreCase(""))
            Picasso.with(context)
                    .load(path)
                    .error(R.mipmap.ic_tick_white)
                    .config(Bitmap.Config.RGB_565)
                    .into(imageView);
    }
    public static int getHeightPixels(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return height;
    }
    public static void setUserDefaultNull() {
        LoginUserInfo.setValueForKey(Constants.LOGIN_USER_ID_KEY, null);
        LoginUserInfo.setValueForKey(Constants.LOGIN_USER_SESSION_TOKEN_KEY, null);
        LoginUserInfo.setValueForKey(Constants.LOGIN_USER_EMAIL_KEY, null);
        LoginUserInfo.setValueForKey(Constants.COMPANY_KEY, null);
        LoginUserInfo.setValueForKey(Constants.LANGUAGE_KEY, null);

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
