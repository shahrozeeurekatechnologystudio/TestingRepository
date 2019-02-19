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
import com.engagement.UIViews.EngagementDialogBanner;
import com.engagement.UIViews.EngagementDialogBottom;
import com.engagement.UIViews.EngagementDialogFullScreen;
import com.engagement.UIViews.EngagementDialogPushNotification;
import com.engagement.UIViews.EngagementDialogTop;
import com.engagement.UIViews.EngagementDialogMiddle;
import com.engagement.interfaces.DeepLinkActionsListener;
import com.engagement.interfaces.MessageActionsListener;
import com.engagement.interfaces.UserActionsListener;
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

    public void setEngagementMessage(Context context, Map<String, String> data, JSONObject notificationPayLoad, Class<?> cls, int notificationStatusBarIcon, MessageActionsListener messageActionsListener, DeepLinkActionsListener deepLinkActionsListener) {
        if (data != null) {
            try {
                if (data.containsKey(Constants.PUSH_NOTIFICATION_ALERT)
                        && data.get(Constants.PUSH_NOTIFICATION_ALERT) != null) {
                    handlePushReceivedData(context, data, notificationPayLoad, data.get(Constants.PUSH_NOTIFICATION_ALERT), cls, notificationStatusBarIcon, messageActionsListener, deepLinkActionsListener);
                } else if (data.toString() != null && data.toString().split("=") != null && data.toString().split("=").length > 0 &&
                        data.toString().split("=")[1] != null) {

                    handlePushReceivedData(context, data, notificationPayLoad, data.toString().split("=")[1], cls, notificationStatusBarIcon, messageActionsListener, deepLinkActionsListener);

                } else {
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handlePushReceivedData(Context context, Map<String, String> data, JSONObject notificationPayLoad, String messageBody, Class<?> cls, int notificationStatusBarIcon, MessageActionsListener messageActionsListener, DeepLinkActionsListener deepLinkActionsListener) {
        try {
            JSONObject dataNotificationPayLoad = new JSONObject(messageBody);
            if (dataNotificationPayLoad != null && dataNotificationPayLoad.has(Constants.MESSAGE_KEY_TYPE_PLATFORM) && dataNotificationPayLoad.get(Constants.MESSAGE_KEY_TYPE_PLATFORM) != null && dataNotificationPayLoad.getBoolean(Constants.MESSAGE_KEY_TYPE_PLATFORM)) {
                if (dataNotificationPayLoad != null && dataNotificationPayLoad.has(Constants.MESSAGE_KEY_TYPE_SILENT) && dataNotificationPayLoad.get(Constants.MESSAGE_KEY_TYPE_SILENT) != null && !dataNotificationPayLoad.getBoolean(Constants.MESSAGE_KEY_TYPE_SILENT)) {
                    if (dataNotificationPayLoad != null && dataNotificationPayLoad.has(Constants.LOGIN_USER_ID_KEY) && dataNotificationPayLoad.get(Constants.LOGIN_USER_ID_KEY) != null && dataNotificationPayLoad.getString(Constants.LOGIN_USER_ID_KEY) != null) {
                        if (LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null, context) != null && dataNotificationPayLoad.getString(Constants.LOGIN_USER_ID_KEY).equalsIgnoreCase(LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null, context))) {
                            try {
                                if (dataNotificationPayLoad != null && dataNotificationPayLoad.has(Constants.TRACK_KEY) && dataNotificationPayLoad.get(Constants.TRACK_KEY) != null && dataNotificationPayLoad.getString(Constants.TRACK_KEY) != null
                                        ) {
                                    LoginUserInfo.setValueForKey(Constants.TRACK_KEY, dataNotificationPayLoad.getString(Constants.TRACK_KEY));
                                }
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                                LoginUserInfo.setValueForKey(Constants.CAMPAIGN_RECEIVE_DATE, simpleDateFormat.format(new Date()));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                            List<ActivityManager.RunningTaskInfo> services = activityManager != null ? activityManager.getRunningTasks(Integer.MAX_VALUE) : null;
                            boolean isActivityFound = false;

                            if (services.get(0).topActivity.getPackageName().equalsIgnoreCase(context.getPackageName())) {
                                isActivityFound = true;
                            }

                            if (isActivityFound) {
                                setEngagementMessageShowingInAppAndPush(messageBody, notificationPayLoad, deepLinkActionsListener);
                            } else {
                                showNotification(context, messageBody, cls, notificationStatusBarIcon, notificationPayLoad);
                            }

                        }
                    }
                } else if (dataNotificationPayLoad != null && dataNotificationPayLoad.has(Constants.MESSAGE_KEY_TYPE_SILENT) && dataNotificationPayLoad.get(Constants.MESSAGE_KEY_TYPE_SILENT) != null && dataNotificationPayLoad.getBoolean(Constants.MESSAGE_KEY_TYPE_SILENT)) {
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

    private void showNotification(Context context, String messageBody, Class<?> cls, int notificationStatusBarIcon, JSONObject notificationPayLoad) {
        try {
            NotificationCompat.Builder mBuilder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    mBuilder =
                            new NotificationCompat.Builder(context, Constants.DEFAULT_CHANNEL_ID)
                                    .setAutoCancel(true)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setContentTitle(context.getResources().getString(R.string.app_name));
                } catch (NoSuchMethodError e) {
                    mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setAutoCancel(true)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setContentTitle(context.getResources().getString(R.string.app_name));
                }
            } else {
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
            bundle.putString(Constants.ENGAGEMENT_PUSH_NOTIFICATION_DATA_PAYLOAD,
                    messageBody);
            if (notificationPayLoad != null) {
                bundle.putString(
                        Constants.ENGAGEMENT_PUSH_NOTIFICATION_PAYLOAD,
                        notificationPayLoad.toString());
            }
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

    public void handlePushNotificationFlow(Intent intent, DeepLinkActionsListener deepLinkActionsListener, MessageActionsListener messageActionsListener) {
        if (EngagementSdk.getSingletonInstance() != null && EngagementSdk.getSingletonInstance().getContext() != null && !EngagementSdk.getSingletonInstance().getActiveActivity().isFinishing()) {
            Bundle activityBundle = intent.getExtras();
            if (activityBundle != null) {
                if (activityBundle.containsKey(Constants.ENGAGEMENT_PUSH_NOTIFICATION_DATA_PAYLOAD)) {
                    if (intent.getExtras()
                            .getString(Constants.ENGAGEMENT_PUSH_NOTIFICATION_DATA_PAYLOAD) != null &&
                            intent.getExtras()
                                    .getString(Constants.ENGAGEMENT_PUSH_NOTIFICATION_PAYLOAD) != null) {
                        JSONObject notificationPayLoad = null;
                        try {
                            notificationPayLoad = new JSONObject(intent.getExtras()
                                    .getString(Constants.ENGAGEMENT_PUSH_NOTIFICATION_PAYLOAD));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (notificationPayLoad != null) {
                            setEngagementMessageShowingInAppAndPush(intent.getExtras()
                                    .getString(Constants.ENGAGEMENT_PUSH_NOTIFICATION_DATA_PAYLOAD), notificationPayLoad, deepLinkActionsListener);
                        }
                    } else if (intent.getExtras()
                            .getString(Constants.ENGAGEMENT_PUSH_NOTIFICATION_DATA_PAYLOAD) != null) {
                        setEngagementMessageShowingInAppAndPush(intent.getExtras()
                                .getString(Constants.ENGAGEMENT_PUSH_NOTIFICATION_DATA_PAYLOAD), null, deepLinkActionsListener);

                    }
                    activityBundle.remove(Constants.ENGAGEMENT_PUSH_NOTIFICATION_DATA_PAYLOAD);
                    intent.removeExtra(Constants.ENGAGEMENT_PUSH_NOTIFICATION_DATA_PAYLOAD);
                    activityBundle.remove(Constants.ENGAGEMENT_PUSH_NOTIFICATION_PAYLOAD);
                    intent.removeExtra(Constants.ENGAGEMENT_PUSH_NOTIFICATION_PAYLOAD);

                } else if (activityBundle.containsKey(Constants.ENGAGEMENT_PUSH_NOTIFICATION_DATA_PAYLOAD_IN_KILL_STATE) &&
                        activityBundle.getBundle(Constants.ENGAGEMENT_PUSH_NOTIFICATION_DATA_PAYLOAD_IN_KILL_STATE) != null) {
                    try {
                        Bundle bundle = activityBundle.getBundle(Constants.ENGAGEMENT_PUSH_NOTIFICATION_DATA_PAYLOAD_IN_KILL_STATE);
                        if (bundle.containsKey(Constants.PUSH_NOTIFICATION_ALERT)
                                && bundle.get(Constants.PUSH_NOTIFICATION_ALERT) != null
                                && bundle.getString(Constants.PUSH_NOTIFICATION_ALERT) != null) {
                            JSONObject dataNotificationPayLoad = new JSONObject(bundle.getString(Constants.PUSH_NOTIFICATION_ALERT));
                            if (dataNotificationPayLoad != null && dataNotificationPayLoad.has(Constants.MESSAGE_KEY_TYPE_PLATFORM) && dataNotificationPayLoad.get(Constants.MESSAGE_KEY_TYPE_PLATFORM) != null && dataNotificationPayLoad.getBoolean(Constants.MESSAGE_KEY_TYPE_PLATFORM)) {
                                if (dataNotificationPayLoad != null && dataNotificationPayLoad.has(Constants.MESSAGE_KEY_TYPE_SILENT) && dataNotificationPayLoad.get(Constants.MESSAGE_KEY_TYPE_SILENT) != null && !dataNotificationPayLoad.getBoolean(Constants.MESSAGE_KEY_TYPE_SILENT)) {
                                    if (dataNotificationPayLoad != null && dataNotificationPayLoad.has(Constants.LOGIN_USER_ID_KEY) && dataNotificationPayLoad.get(Constants.LOGIN_USER_ID_KEY) != null && dataNotificationPayLoad.getString(Constants.LOGIN_USER_ID_KEY) != null) {
                                        if (LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null, EngagementSdk.getSingletonInstance().getContext()) != null && dataNotificationPayLoad.getString(Constants.LOGIN_USER_ID_KEY).equalsIgnoreCase(LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null, EngagementSdk.getSingletonInstance().getContext()))) {
                                            try {
                                                if (dataNotificationPayLoad != null && dataNotificationPayLoad.has(Constants.TRACK_KEY) && dataNotificationPayLoad.get(Constants.TRACK_KEY) != null && dataNotificationPayLoad.getString(Constants.TRACK_KEY) != null
                                                        ) {
                                                    LoginUserInfo.setValueForKey(Constants.TRACK_KEY, dataNotificationPayLoad.getString(Constants.TRACK_KEY));
                                                }
                                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                                                LoginUserInfo.setValueForKey(Constants.CAMPAIGN_RECEIVE_DATE, simpleDateFormat.format(new Date()));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            PushSeenViewApiController.getSingletonInstance().hitSeenApi(new UserActionsListener() {
                                                @Override
                                                public void onStart() {

                                                }

                                                @Override
                                                public void onCompleted(JSONObject object) {

                                                }

                                                @Override
                                                public void onError(String exception) {

                                                }
                                            });
                                        }
                                    }
                                } else if (dataNotificationPayLoad != null && dataNotificationPayLoad.has(Constants.MESSAGE_KEY_TYPE_SILENT) && dataNotificationPayLoad.get(Constants.MESSAGE_KEY_TYPE_SILENT) != null && dataNotificationPayLoad.getBoolean(Constants.MESSAGE_KEY_TYPE_SILENT)) {
                                    if (messageActionsListener != null) {
                                        messageActionsListener.onMessageSilent(bundle);
                                    }
                                }
                            } else {
                                if (messageActionsListener != null) {
                                    messageActionsListener.onMessageNotPlatformEngagement(bundle);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void setEngagementMessageShowingInAppAndPush(String result, final JSONObject notificationPayLoad, final DeepLinkActionsListener deepLinkActionsListener) {
        try {
            final JSONObject notificationJson = new JSONObject(result);
            final String icon;
            final String deepLinkUri;
            final String backgroundColor;
            if (notificationJson != null && notificationJson.has(Constants.LOGIN_USER_ID_KEY) && notificationJson.get(Constants.LOGIN_USER_ID_KEY) != null && notificationJson.getString(Constants.LOGIN_USER_ID_KEY) != null) {
                if (EngagementSdk.getSingletonInstance() != null && !EngagementSdk.getSingletonInstance().getActiveActivity().isFinishing()
                        && notificationJson.getString(Constants.LOGIN_USER_ID_KEY).equalsIgnoreCase(LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null))) {
                    if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_CAMPAIGN_TYPE) != null) {
                        if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_CAMPAIGN_TYPE).equals(Constants.MESSAGE_KEY_TYPE_PUSH)) {
                            if (notificationPayLoad != null) {
                                if (notificationPayLoad.has(Constants.ENGAGEMENT_PUSH_ICON_KEY) && notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_ICON_KEY) != null) {
                                    icon = notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_ICON_KEY);

                                } else {
                                    icon = "";
                                }
                                if (notificationPayLoad.has(Constants.ENGAGEMENT_PUSH_BACKGROUND_COLOR) && notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_BACKGROUND_COLOR) != null) {
                                    backgroundColor = notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_BACKGROUND_COLOR);

                                } else {
                                    backgroundColor = "";
                                }
                                if (notificationPayLoad.has(Constants.ENGAGEMENT_PUSH_LINK_KEY) && notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_LINK_KEY) != null) {
                                    deepLinkUri = notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_LINK_KEY);

                                } else {
                                    deepLinkUri = "";
                                }
                                if (notificationPayLoad.has(Constants.ENGAGEMENT_PUSH_TITLE_KEY) && notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_TITLE_KEY) != null && notificationPayLoad.has(Constants.ENGAGEMENT_PUSH_BODY_KEY) && notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_BODY_KEY) != null) {
                                    EngagementSdk.getSingletonInstance().getActiveActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            try {
                                                new EngagementDialogPushNotification(EngagementSdk.getSingletonInstance().getActiveActivity(), notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_TITLE_KEY), notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_BODY_KEY), icon, backgroundColor, deepLinkUri, deepLinkActionsListener).showDialog();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });

                                } else if (notificationPayLoad.has(Constants.ENGAGEMENT_PUSH_TITLE_KEY) && notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_TITLE_KEY) == null && notificationPayLoad.has(Constants.ENGAGEMENT_PUSH_BODY_KEY) && notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_BODY_KEY) != null) {
                                    EngagementSdk.getSingletonInstance().getActiveActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            try {
                                                new EngagementDialogPushNotification(EngagementSdk.getSingletonInstance().getActiveActivity(), "", notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_BODY_KEY), icon, backgroundColor, deepLinkUri, deepLinkActionsListener).showDialog();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                } else if (notificationPayLoad.has(Constants.ENGAGEMENT_PUSH_TITLE_KEY) && notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_TITLE_KEY) != null && notificationPayLoad.has(Constants.ENGAGEMENT_PUSH_BODY_KEY) && notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_BODY_KEY) == null) {
                                    EngagementSdk.getSingletonInstance().getActiveActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            try {
                                                new EngagementDialogPushNotification(EngagementSdk.getSingletonInstance().getActiveActivity(), notificationPayLoad.getString(Constants.ENGAGEMENT_PUSH_TITLE_KEY), "", icon, backgroundColor, deepLinkUri, deepLinkActionsListener).showDialog();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }
                                PushSeenViewApiController.getSingletonInstance().hitSeenApi(new UserActionsListener() {
                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onCompleted(JSONObject object) {

                                    }

                                    @Override
                                    public void onError(String exception) {

                                    }
                                });
                            } else {
                                if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_DATA) != null) {
                                    EngagementSdk.getSingletonInstance().getActiveActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            try {
                                                new EngagementDialogPushNotification(EngagementSdk.getSingletonInstance().getActiveActivity(), "", notificationJson.getString(Constants.MESSAGE_KEY_TYPE_DATA), null, null, null, null).showDialog();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }
                            }
                        } else if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_MESSAGE_TYPE) != null && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_MESSAGE_TYPE).equals(Constants.MESSAGE_KEY_TYPE_DIALOG)
                                && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK) != null) {
                            if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_POSITION) != null && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_POSITION).equals(Constants.POSITION_TOP)) {
                                showTopBanner(EngagementSdk.getSingletonInstance().getActiveActivity(), notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK));
                            } else if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_POSITION) != null && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_POSITION).equals(Constants.POSITION_BOTTOM)) {
                                showBottomBanner(EngagementSdk.getSingletonInstance().getActiveActivity(), notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK));
                            } else if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_POSITION) != null && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_POSITION).equals(Constants.POSITION_MIDDLE)) {
                                showMiddleBanner(EngagementSdk.getSingletonInstance().getActiveActivity(), notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK));
                            }
                        } else if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_MESSAGE_TYPE) != null && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_MESSAGE_TYPE).equals(Constants.MESSAGE_KEY_TYPE_FULL_SCREEN)
                                && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK) != null) {
                            showFullScreen(EngagementSdk.getSingletonInstance().getActiveActivity(), notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK));
                        } else if (notificationJson.getString(Constants.MESSAGE_KEY_TYPE_MESSAGE_TYPE) != null && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_MESSAGE_TYPE).equals(Constants.MESSAGE_KEY_TYPE_BANNER)
                                && notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK) != null) {
                            showBanner(EngagementSdk.getSingletonInstance().getActiveActivity(), notificationJson.getString(Constants.MESSAGE_KEY_TYPE_IN_APP_VIEW_LINK));
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
                new EngagementDialogTop(context, msg, Constants.POSITION_TOP);
            }
        });

    }

    private void showBottomBanner(final Activity context, final String msg) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                new EngagementDialogBottom(context, msg, Constants.POSITION_BOTTOM);
            }
        });

    }

    private void showMiddleBanner(final Activity context, final String msg) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                new EngagementDialogMiddle(context, msg, Constants.POSITION_MIDDLE);
            }
        });
    }

    private void showFullScreen(final Activity context, final String msg) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                new EngagementDialogFullScreen(context, msg, Constants.POSITION_FULL);
            }
        });

    }
    private void showBanner(final Activity context, final String msg) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                new EngagementDialogBanner(context, msg, Constants.POSITION_FULL);
            }
        });

    }
}
