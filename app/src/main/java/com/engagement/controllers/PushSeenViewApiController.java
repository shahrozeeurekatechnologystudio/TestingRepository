package com.engagement.controllers;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.engagement.EngagementSdk;
import com.engagement.R;
import com.engagement.interfaces.UserActionsListener;
import com.engagement.restkit.ApiUrl;
import com.engagement.restkit.RestCalls;
import com.engagement.utils.Constants;
import com.engagement.utils.EngagementSdkLog;
import com.engagement.utils.LoginUserInfo;

import org.json.JSONObject;


public class PushSeenViewApiController {
    private static PushSeenViewApiController instance;
    private UserActionsListener userActionsListener;

    public static PushSeenViewApiController getSingletonInstance() {
        if (instance == null) {
            instance = new PushSeenViewApiController();
        }
        return instance;
    }

    public void hitSeenApi(UserActionsListener userActionsListener) {
        hitSeenApiRestCalls(userActionsListener);
    }

    private void hitSeenApiRestCalls(UserActionsListener userActionsListener) {
        JSONObject params = new JSONObject();
        try {
            this.userActionsListener = userActionsListener;
            if (userActionsListener != null) {
                userActionsListener.onStart();
            }
            if (!LoginUserInfo.getValueForKey(Constants.TRACK_KEY, "").equalsIgnoreCase("")) {
                params.put("track_key", LoginUserInfo.getValueForKey(Constants.TRACK_KEY, ""));
                RestCalls addActionDetailRequest = new RestCalls(Request.Method.POST, ApiUrl.getPushTrackViewUrl(),
                        params, responseListener(),
                        errorListener());
                if (EngagementSdk.getSingletonInstance() != null && EngagementSdk.getSingletonInstance().getContext() != null) {
                    EngagementSdk.getSingletonInstance().getRequestQueue().add(addActionDetailRequest);
                }
            } else {
                if (userActionsListener != null) {
                    if (EngagementSdk.getSingletonInstance() != null && EngagementSdk.getSingletonInstance().getActiveActivity() != null) {
                        userActionsListener.onError(EngagementSdk.getSingletonInstance().getActiveActivity().getResources().getString(R.string.no_campaign_receive_yet));
                    }
                }
            }


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
                        if (userActionsListener != null && response.getString(Constants.API_RESPONSE_MESSAGE_KEY) != null) {
                            userActionsListener.onError(response.getString(Constants.API_RESPONSE_MESSAGE_KEY));
                        }
                        if (response.getJSONArray(Constants.API_RESPONSE_ERROR_KEY) != null && response.getJSONArray(Constants.API_RESPONSE_ERROR_KEY).get(0) != null &&
                                response.getJSONArray(Constants.API_RESPONSE_ERROR_KEY).get(0).toString() != null) {
                            EngagementSdkLog.logDebug(EngagementSdkLog.TAG_VOLLEY_ERROR, response.getJSONArray(Constants.API_RESPONSE_ERROR_KEY).get(0).toString());
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