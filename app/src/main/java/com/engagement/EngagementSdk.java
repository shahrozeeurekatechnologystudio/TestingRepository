package com.engagement;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.engagement.controllers.CampaignController;
import com.engagement.controllers.CompanyLoginController;
import com.engagement.controllers.UserActionController;
import com.engagement.interfaces.DeepLinkActionsListener;
import com.engagement.interfaces.MessageActionsListener;
import com.engagement.interfaces.UserActionsListener;
import com.engagement.models.registeruser.EngagementUser;
import com.engagement.restkit.ApiUrl;
import com.engagement.utils.EngagementSdkLog;
import com.engagement.utils.UserActionsModeEnums;

import org.json.JSONObject;

import java.util.Map;

public class EngagementSdk {
    public static final String TAG = "VolleyPatterns";
    private static EngagementSdk sInstance;
    private EngagementUser engagementUser;
    private Application context;
    private boolean isSdkLogEnable;
    private Activity activeActivity;
    private RequestQueue mRequestQueue;

    private EngagementSdk(Application applicationContext, String engagementSdkBaseUrl, boolean isSdkLogEnable) {
        this.context = applicationContext;
        this.isSdkLogEnable = isSdkLogEnable;
        setupActivityListener(applicationContext);
        ApiUrl.setSitePrefix(engagementSdkBaseUrl);
        CompanyLoginController.getSingletonInstance().companyLogin(new UserActionsListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(JSONObject object) {
                if (object != null && object.toString() != null)
                    EngagementSdkLog.logDebug(EngagementSdkLog.TAG, object.toString());

            }

            @Override
            public void onError(String exception) {
                EngagementSdkLog.logDebug(EngagementSdkLog.TAG_VOLLEY_ERROR, exception);

            }
        });
    }

    public static void sdkInitialize(Application context, String engagementSdkBaseUrl, boolean isSdkLogEnable) {
        sInstance = new EngagementSdk(context, engagementSdkBaseUrl, isSdkLogEnable);
    }

    public static synchronized EngagementSdk getSingletonInstance() {
        return sInstance;
    }

    public static void setEngagementSdkMsg(Context context, Map<String, String> data, JSONObject notificationPayLoad, Class<?> cls, int notificationStatusBarIcon, MessageActionsListener messageActionsListener, DeepLinkActionsListener deepLinkActionsListener) {
        CampaignController.getSingletonInstance().setEngagementMessage(context, data, notificationPayLoad, cls, notificationStatusBarIcon, messageActionsListener, deepLinkActionsListener);
    }

    private void setupActivityListener(Application context) {
        context.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                activeActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public Activity getActiveActivity() {
        return activeActivity;
    }

    public void registerEngagementUser(EngagementUser engagementUser, UserActionsModeEnums userActionsModeEnums, UserActionsListener userActionsListener) {
        if (engagementUser != null)
            this.engagementUser = engagementUser;
        UserActionController.getSingletonInstance().hitUserAction(engagementUser, userActionsModeEnums, userActionsListener);
    }

    public RequestQueue getRequestQueue() {
        if (this.mRequestQueue == null) {
            this.mRequestQueue = Volley.newRequestQueue(this.context);
        }

        return this.mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? "VolleyPatterns" : tag);
        VolleyLog.d("Adding request to queue: %s", new Object[]{req.getUrl()});
        this.getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag("VolleyPatterns");
        this.getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (this.mRequestQueue != null) {
            this.mRequestQueue.cancelAll(tag);
        }

    }

    public EngagementUser getEngagementUser() {
        return engagementUser;
    }

    public void setEngagementUser(EngagementUser engagementUser) {
        this.engagementUser = engagementUser;
    }

    public void sdkLogOut(String fcmToken, final UserActionsListener userActionsListener) {
       /* FireBaseTokenController.sendRegistrationToServerExpireOnLogOut(fcmToken, new UserActionsListener() {
            @Override
            public void onStart() {
                if(userActionsListener!=null)
                    userActionsListener.onStart();

            }

            @Override
            public void onCompleted(JSONObject object) {
                setEngagementUser(null);
                ConstantFunctions.setUserDefaultNull();
                if(userActionsListener!=null)
                    userActionsListener.onCompleted(object);
            }

            @Override
            public void onError(String exception) {
                setEngagementUser(null);
                ConstantFunctions.setUserDefaultNull();
                if(userActionsListener!=null)
                    userActionsListener.onError(exception);

            }
        });*/
        //sInstance = null;
        //context = null;

    }

    public Application getContext() {
        return context;
    }

    public boolean isSdkLogEnable() {
        return isSdkLogEnable;
    }

    public void setUpdateUserLocation(String longitude, String latitude) {
        if (EngagementSdk.getSingletonInstance() != null && EngagementSdk.getSingletonInstance().getEngagementUser() != null) {
            EngagementSdk.getSingletonInstance().getEngagementUser().setLatitude(latitude);
            EngagementSdk.getSingletonInstance().getEngagementUser().setLongitude(longitude);
        }
    }
}
