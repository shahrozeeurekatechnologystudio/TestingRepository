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

import org.json.JSONObject;


public class NewsFeedController {
    private static NewsFeedController instance;
    private UserActionsListener userActionsListener;

    public static NewsFeedController getSingletonInstance() {
        if (instance == null) {
            instance = new NewsFeedController();
        }
        return instance;
    }

    public void getNewsFeed(String latitude, String longitude, UserActionsListener userActionsListener) {
        getNewsFeedRestCalls(latitude, longitude, false, userActionsListener);
    }

    public void getNewsFeedCount(String latitude, String longitude, UserActionsListener userActionsListener) {
        getNewsFeedRestCalls(latitude, longitude, true, userActionsListener);
    }

    private void getNewsFeedRestCalls(String latitude, String longitude, boolean isFeedCount, UserActionsListener userActionsListener) {
        try {
            this.userActionsListener = userActionsListener;
            JSONObject params = new JSONObject();
            if (isFeedCount)
                ConstantFunctions.appendCommonParameterTORequest(params, Constants.RESOURCE_NEWS_FEED_VALUE, Constants.METHOD_COUNT_VALUE);
            else
                ConstantFunctions.appendCommonParameterTORequest(params, Constants.RESOURCE_NEWS_FEED_VALUE, Constants.METHOD_LISTING_VALUE);
            JSONObject paramsData = new JSONObject();
            if (EngagementSdk.getSingletonInstance() != null &&
                    EngagementSdk.getSingletonInstance().getContext() != null &&
                    LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null) != null) {
                paramsData.put("user_id", LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null));
            }
            if (latitude != null && !latitude.equalsIgnoreCase("")) {
                paramsData.put("latitude", latitude);
            }
            if (longitude != null && !longitude.equalsIgnoreCase("")) {
                paramsData.put("longitude", longitude);
            }
            params.put("data", paramsData);
            RestCalls addActionDetailRequest = new RestCalls(Request.Method.POST, ApiUrl.getNewsFeedUrl(),
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
