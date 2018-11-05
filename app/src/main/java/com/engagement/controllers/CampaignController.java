package com.engagement.controllers;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.engagement.EngagementSdk;
import com.engagement.R;
import com.engagement.UIViews.PushNotificationDialog;
import com.engagement.UIViews.DialogBannerTopBottom;
import com.engagement.UIViews.DialogMessageMiddleFull;
import com.engagement.interfaces.MessageActionsListener;
import com.engagement.utils.Constants;
import com.engagement.utils.LoginUserInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


public class CampaignController {


    private static CampaignController instance;


    public static CampaignController getSingletonInstance() {
        if (instance == null) {
            instance = new CampaignController();
        }
        return instance;
    }

    public void setEngagementMessage(Context context, Map<String, String> data, Class<?> cls, int notificationStatusBarIcon, MessageActionsListener messageActionsListener) {
        if (data != null) {
            try {
                if (data.containsKey(Constants.PUSH_NOTIFICATION_ALERT)
                        && data.get(Constants.PUSH_NOTIFICATION_ALERT) != null) {
                    handlePushReceivedData(context, data, data.get(Constants.PUSH_NOTIFICATION_ALERT), cls, notificationStatusBarIcon, messageActionsListener);
                } else if (data.toString() != null && data.toString().split("=") != null && data.toString().split("=").length > 0 &&
                        data.toString().split("=")[1] != null) {

                    handlePushReceivedData(context, data, data.toString().split("=")[1], cls, notificationStatusBarIcon, messageActionsListener);

                } else {
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handlePushReceivedData(Context context, Map<String, String> data, String messageBody, Class<?> cls, int notificationStatusBarIcon, MessageActionsListener messageActionsListener) {
        try {
            JSONObject notificationJson = new JSONObject(messageBody);
            if (notificationJson != null && notificationJson.has(Constants.MESSAGE_KEY_TYPE_PLATFORM) && notificationJson.get(Constants.MESSAGE_KEY_TYPE_PLATFORM) != null && notificationJson.getBoolean(Constants.MESSAGE_KEY_TYPE_PLATFORM)) {
                if (notificationJson != null && notificationJson.has(Constants.MESSAGE_KEY_TYPE_SILENT) && notificationJson.get(Constants.MESSAGE_KEY_TYPE_SILENT) != null && !notificationJson.getBoolean(Constants.MESSAGE_KEY_TYPE_SILENT)) {
                    if (notificationJson != null && notificationJson.has(Constants.LOGIN_USER_ID_KEY) && notificationJson.get(Constants.LOGIN_USER_ID_KEY) != null && notificationJson.getString(Constants.LOGIN_USER_ID_KEY) != null) {
                        if (LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null, context) != null && notificationJson.getString(Constants.LOGIN_USER_ID_KEY).equalsIgnoreCase(LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null, context))) {
                            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                            List<ActivityManager.RunningTaskInfo> services = activityManager != null ? activityManager.getRunningTasks(Integer.MAX_VALUE) : null;
                            boolean isActivityFound = false;

                            if (services.get(0).topActivity.getPackageName().equalsIgnoreCase(context.getPackageName())) {
                                isActivityFound = true;
                            }

                            if (isActivityFound) {
                                setEngagementMessageShowingInAppAndPush(messageBody);
                            } else {
                                showNotification(context, messageBody, cls, notificationStatusBarIcon);
                            }
                            try {
                                if (notificationJson != null && notificationJson.has("track_key") && notificationJson.get("track_key") != null && notificationJson.getString("track_key") != null
                                        ) {
                                    LoginUserInfo.setValueForKey(Constants.TRACK_KEY, notificationJson.getString("track_key"));
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                                LoginUserInfo.setValueForKey(Constants.CAMPAIGN_RECEIVE_DATE, simpleDateFormat.format(new Date()));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else if (notificationJson != null && notificationJson.has(Constants.MESSAGE_KEY_TYPE_SILENT) && notificationJson.get(Constants.MESSAGE_KEY_TYPE_SILENT) != null && notificationJson.getBoolean(Constants.MESSAGE_KEY_TYPE_SILENT)) {
                    if (messageActionsListener != null)
                        messageActionsListener.onMessageSilent(data);
                }
            } else {
                if (messageActionsListener != null)
                    messageActionsListener.onMessageNotPlatformEngagement(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotification(Context context, String messageBody, Class<?> cls, int notificationStatusBarIcon) {
        try {
            NotificationCompat.Builder mBuilder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    mBuilder =
                            new NotificationCompat.Builder(context, Constants.DEFAULT_CHANNEL_ID)
                                    .setAutoCancel(true)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setContentTitle(context.getResources().getString(R.string.app_name));
                }catch (NoSuchMethodError e) {
                    mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setAutoCancel(true)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setContentTitle(context.getResources().getString(R.string.app_name));
                }
            }
            else {
                mBuilder =
                        new NotificationCompat.Builder(context)
                                .setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_SOUND)
                                .setContentTitle(context.getResources().getString(R.string.app_name));
            }

            JSONObject tempJson = null;
            try {
                tempJson = new JSONObject(messageBody);
                if (tempJson.getString(Constants.MESSAGE_KEY_TYPE_CAMPAIGN_TYPE) != null && tempJson.getString(Constants.MESSAGE_KEY_TYPE_CAMPAIGN_TYPE).equals(Constants.MESSAGE_KEY_TYPE_PUSH)) {
                    if (tempJson.getString(Constants.MESSAGE_KEY_TYPE_DATA) != null)
                        mBuilder.setContentText(tempJson.getString(Constants.MESSAGE_KEY_TYPE_DATA));
                    else
                        mBuilder.setContentText(context.getResources().getString(R.string.notification_alert_without_push));
                } else {
                    mBuilder.setContentText(context.getResources().getString(R.string.notification_alert_without_push));
                }
            } catch (Exception e) {
                mBuilder.setContentText(context.getResources().getString(R.string.notification_alert_without_push));
            }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setSmallIcon(notificationStatusBarIcon);
                mBuilder.setColor(context.getResources().getColor(R.color.color_primary));
            } else {
                mBuilder.setSmallIcon(notificationStatusBarIcon);
            }
            Bundle bundle = new Bundle();
            bundle.putString(Constants.PUSH_NOTIFICATION_DATA,
                    messageBody);
            Intent notificationIntent = new Intent(context, cls);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notificationIntent.putExtras(bundle);
            PendingIntent contentIntent = PendingIntent.getActivity(
                    context, new Random().nextInt(900000), notificationIntent, 0);
            mBuilder.setContentIntent(contentIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = mNotificationManager.getNotificationChannel(Constants.DEFAULT_CHANNEL_ID);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(Constants.DEFAULT_CHANNEL_ID, Constants.DEFAULT_CHANNEL_TITLE, importance);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mNotificationManager.createNotificationChannel(mChannel);
                }
            }
            mNotificationManager.notify(new Random().nextInt(900000), mBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handlePushNotificationFlow(Intent intent) {
        if (EngagementSdk.getSingletonInstance()!=null && EngagementSdk.getSingletonInstance().getContext()!=null && !EngagementSdk.getSingletonInstance().getActiveActivity().isFinishing()) {
            Bundle activityBundle = intent.getExtras();
            if (activityBundle != null) {
                if (activityBundle.containsKey(Constants.PUSH_NOTIFICATION_DATA)) {
                    if (intent.getExtras()
                            .getString(Constants.PUSH_NOTIFICATION_DATA) != null) {
                        setEngagementMessageShowingInAppAndPush(intent.getExtras()
                                .getString(Constants.PUSH_NOTIFICATION_DATA));
                    }
                    activityBundle.remove(Constants.PUSH_NOTIFICATION_DATA);
                    intent.removeExtra(Constants.PUSH_NOTIFICATION_DATA);

                }
            }
        }
    }

    private void setEngagementMessageShowingInAppAndPush(String result) {
        try {
            final JSONObject notificationJson = new JSONObject(result);
            if (notificationJson != null && notificationJson.has(Constants.LOGIN_USER_ID_KEY) && notificationJson.get(Constants.LOGIN_USER_ID_KEY) != null && notificationJson.getString(Constants.LOGIN_USER_ID_KEY) != null) {
                if (EngagementSdk.getSingletonInstance() != null && !EngagementSdk.getSingletonInstance().getActiveActivity().isFinishing()
                        && notificationJson.getString(Constants.LOGIN_USER_ID_KEY).equalsIgnoreCase(LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null))) {
                    if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_CAMPAIGN_TYPE) != null) {
                        if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_CAMPAIGN_TYPE).equals(Constants.MESSAGE_KEY_TYPE_PUSH) && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_DATA) != null) {
                            EngagementSdk.getSingletonInstance().getActiveActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        new PushNotificationDialog(EngagementSdk.getSingletonInstance().getActiveActivity(), notificationJson.getString(Constants.MESSAGE_KEY_TYPE_DATA)).showDialog();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } else if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_MESSAGE_TYPE) != null && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_MESSAGE_TYPE).equals(Constants.MESSAGE_KEY_TYPE_DIALOG)
                                && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK) != null) {
                            if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_POSITION) != null && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_POSITION).equals(Constants.POSITION_TOP)) {
                                showTopBanner(EngagementSdk.getSingletonInstance().getActiveActivity(), notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK));
                            } else if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_POSITION) != null && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_POSITION).equals(Constants.POSITION_BOTTOM)) {
                                showBottomBanner(EngagementSdk.getSingletonInstance().getActiveActivity(), notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK));
                            } else if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_POSITION) != null && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_POSITION).equals(Constants.POSITION_MIDDLE)) {
                                showMiddleBanner(EngagementSdk.getSingletonInstance().getActiveActivity(),notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK));
                            } else {
                                showFullBanner(EngagementSdk.getSingletonInstance().getActiveActivity(), notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK));
                            }
                        } else {
                            if(notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK) != null)
                                showFullBanner(EngagementSdk.getSingletonInstance().getActiveActivity(), notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void showTopBanner(final Activity context, final String msg) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                new DialogBannerTopBottom(context, msg, Constants.POSITION_TOP);
            }
        });

    }

    private void showBottomBanner(final Activity context, final String msg) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                new DialogBannerTopBottom(context, msg, Constants.POSITION_BOTTOM);
            }
        });

    }

    private void showMiddleBanner(final Activity context, final String msg) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                new DialogMessageMiddleFull(context, msg, Constants.POSITION_MIDDLE);
            }
        });
    }

    private void showFullBanner(final Activity context, final String msg) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                new DialogMessageMiddleFull(context, msg, Constants.POSITION_FULL);
            }
        });

    }
}
