package com.engagement.controllers;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.engagement.EngagementSdk;
import com.engagement.interfaces.UserActionsListener;
import com.engagement.restkit.ApiUrl;
import com.engagement.restkit.RestCalls;
import com.engagement.utils.ConstantFunctions;
import com.engagement.utils.Constants;
import com.engagement.utils.EngagementSdkLog;
import com.engagement.utils.LoginUserInfo;

import org.json.JSONException;
import org.json.JSONObject;


public class NotificationActionController {
    private static NotificationActionController instance;
    private UserActionsListener userActionsListener;

    public static NotificationActionController getSingletonInstance() {
        if (instance == null) {
            instance = new NotificationActionController();
        }
        return instance;
    }

    public void hitNotificationActionController(boolean enable_notification, UserActionsListener userActionsListener) {
        JSONObject paramsData = new JSONObject();
        try {
            paramsData.put("enable_notification", enable_notification);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getNewsFeedControllerRestCalls(paramsData, userActionsListener);
    }

    public void hitNotificationActionController(boolean enable_notification, boolean email_notification, UserActionsListener userActionsListener) {
        JSONObject paramsData = new JSONObject();
        try {
            paramsData.put("enable_notification", enable_notification);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            paramsData.put("email_notification", email_notification);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getNewsFeedControllerRestCalls(paramsData, userActionsListener);
    }

    private void getNewsFeedControllerRestCalls(JSONObject paramsData, UserActionsListener userActionsListener) {
        try {
            this.userActionsListener = userActionsListener;
            JSONObject params = new JSONObject();
            ConstantFunctions.appendCommonParameterTORequestActionForm(params, Constants.RESOURCE_NOTIFICATION_TOGGLE_VALUE, Constants.ACTION_NOTIFICATION_VALUE);
            if (EngagementSdk.getSingletonInstance() != null &&
                    EngagementSdk.getSingletonInstance().getContext() != null &&
                    LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null) != null) {
                paramsData.put("user_id", LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null));
            }
            params.put("data", paramsData);
            RestCalls addActionDetailRequest = new RestCalls(Request.Method.POST, ApiUrl.getNotificationToggleUrl(),
                    params, responseListener(),
                    errorListener());
            if (userActionsListener != null) {
                userActionsListener.onStart();
            }
            EngagementSdk.getSingletonInstance().getRequestQueue().add(addActionDetailRequest);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            try {
                if (e.toString() != null) {
                    if (userActionsListener != null) {
                        userActionsListener.onError(e.toString());
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private Response.Listener<JSONObject> responseListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getJSONObject(Constants.API_RESPONSE_META_KEY).getString(Constants.API_RESPONSE_CODE_KEY).toString()
                            .equalsIgnoreCase(Constants.SERVER_OK_REQUEST_CODE)) {
                        if (userActionsListener != null) {
                            userActionsListener.onCompleted(response);
                        }


                    } else {
                        if (response != null && response.toString() != null) {
                            if (userActionsListener != null) {
                                userActionsListener.onError(response.toString());
                            }
                            EngagementSdkLog.logDebug(EngagementSdkLog.TAG_VOLLEY_ERROR, response.toString());
                        }

                    }
                } catch (Exception e) {
                    if (userActionsListener != null) {
                        userActionsListener.onError(e.toString());
                    }
                    e.printStackTrace();
                }

            }
        };
    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error != null && error.toString() != null) {
                        EngagementSdkLog.logDebug(EngagementSdkLog.TAG_VOLLEY_ERROR, error.toString());
                        if (userActionsListener != null) {
                            userActionsListener.onError(error.toString());

                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();

                }
            }
        };
    }

}
