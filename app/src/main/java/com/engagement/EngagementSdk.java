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
import com.engagement.controllers.FireBaseTokenController;
import com.engagement.controllers.RegisterUserController;
import com.engagement.interfaces.DeepLinkActionsListener;
import com.engagement.interfaces.MessageActionsListener;
import com.engagement.interfaces.UserActionsListener;
import com.engagement.models.registeruser.EngagementUser;
import com.engagement.restkit.ApiUrl;
import com.engagement.utils.ConstantFunctions;
import com.engagement.utils.Constants;
import com.engagement.utils.LoginUserInfo;


import org.json.JSONObject;

import java.util.Map;

public class EngagementSdk {
    private EngagementUser engagementUser;
    private Application context;
    private Activity activeActivity;
    public static final String TAG = "VolleyPatterns";
    private RequestQueue mRequestQueue;
    private static EngagementSdk sInstance;

    private EngagementSdk(Application applicationContext, String engagementSdkBaseUrl) {
        this.context = applicationContext;
        setupActivityListener(applicationContext);
        ApiUrl.setSitePrefix(engagementSdkBaseUrl);
    }
    private void setupActivityListener(Application  context) {
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
    public   Activity getActiveActivity(){
        return activeActivity;
    }
    public void registerEngagementUser(EngagementUser engagementUser, JSONObject extraParams, final UserActionsListener userActionsListener) {
        if (engagementUser != null)
            this.engagementUser = engagementUser;
        RegisterUserController.getSingletonInstance().registerUser(engagementUser, extraParams, userActionsListener);
    }

    public static void sdkInitialize(Application context, String engagementSdkBaseUrl) {
        sInstance = new EngagementSdk(context, engagementSdkBaseUrl);
    }

    public static synchronized EngagementSdk getSingletonInstance() {
        return sInstance;
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

    public void sdkLogOut(String fcmToken,UserActionsListener userActionsListener) {
        FireBaseTokenController.sendRegistrationToServerExpireOnLogOut(fcmToken,userActionsListener);
        setEngagementUser(null);
        ConstantFunctions.setUserDefaultNull();
        //sInstance = null;
        //context = null;

    }

    public void setEngagementUser(EngagementUser engagementUser) {
        this.engagementUser = engagementUser;
    }

    public Application getContext() {
        return context;
    }

    public static void setEngagementSdkMsg(Context context, Map<String, String> data, JSONObject notificationPayLoad, Class<?> cls, int notificationStatusBarIcon, MessageActionsListener messageActionsListener, DeepLinkActionsListener deepLinkActionsListener) {
        CampaignController.getSingletonInstance().setEngagementMessage(context, data,notificationPayLoad, cls, notificationStatusBarIcon, messageActionsListener,deepLinkActionsListener);
    }

    public void setUpdateUserLocation(String longitude, String latitude) {
        if (EngagementSdk.getSingletonInstance() != null && EngagementSdk.getSingletonInstance().getEngagementUser() != null) {
            EngagementSdk.getSingletonInstance().getEngagementUser().setLatitude(latitude);
            EngagementSdk.getSingletonInstance().getEngagementUser().setLongitude(longitude);
        }
    }
}
