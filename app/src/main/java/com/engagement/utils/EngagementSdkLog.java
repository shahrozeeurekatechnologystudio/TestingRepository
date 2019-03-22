package com.engagement.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.engagement.EngagementSdk;


public class EngagementSdkLog {

    public static final String TAG = "EngagementSdk";
    public static final String TAG_VOLLEY_ERROR = "EngagementSdk VolleyError";


    /**
     * logging allowed? When Run As Android Application it returns true.
     * When Export Android Application with release key, it returns false
     */
    public static boolean loggingEnabled() {
        if (EngagementSdk.getSingletonInstance() != null)
            return EngagementSdk.getSingletonInstance().isSdkLogEnable();
        else
            return false;
    }

    public static void logDebug(String message) {
        if (loggingEnabled()) {
            Log.d(TAG, message);
        }
    }

    public static void logDebug(String tag, String message) {
        if (loggingEnabled()) {
            Log.d(tag, message);
        }
    }

    public static void logInfo(String tag, String message) {
        if (loggingEnabled()) {
            Log.i(tag, message);
        }
    }

    public static void logInfo(String message) {
        if (loggingEnabled()) {
            Log.v(TAG, message);
        }
    }

    public static void logError(String message) {
        if (loggingEnabled()) {
            Log.e(TAG, message);
        }
    }


    public static void logError(String tag, String message) {
        if (loggingEnabled()) {
            Log.e(tag, message);
        }
    }

    public static void logError(String tag, String message, Throwable throwable) {
        if (loggingEnabled()) {
            Log.e(tag, message, throwable);
        }
    }

    public static void logVerbose(String tag, String message) {
        if (loggingEnabled()) {
            Log.v(tag, message);
        }
    }

    public static void logWarning(String tag, String message) {
        if (loggingEnabled()) {
            Log.w(tag, message);
        }
    }

    public static void toast(Context context, String resp) {
        if (loggingEnabled()) {
            Toast.makeText(context, resp, Toast.LENGTH_LONG).show();
        }
    }

    public static void intent(String tag, Intent intent) {
        if (loggingEnabled()) {
            Bundle bundle = intent.getExtras();
            if (bundle == null || bundle.keySet() == null) return;
            for (String key : bundle.keySet()) {
                if (bundle.get(key) != null && bundle.get(key).toString() != null) {
                    Log.v(tag, key + ": " + bundle.get(key).toString());
                } else {
                    Log.v(tag, key + " IS NULL");
                }
            }
        }
    }


}
