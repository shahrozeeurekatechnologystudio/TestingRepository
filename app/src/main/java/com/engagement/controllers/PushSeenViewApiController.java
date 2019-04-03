package com.engagement.controllers;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.engagement.EngagementSdk;
import com.engagement.R;
import com.engagement.interfaces.UserActionsListener;
import com.engagement.restkit.ApiUrl;
import com.engagement.restkit.RestCalls;
import com.engagement.utils.ConstantFunctions;
import com.engagement.utils.Constants;
import com.engagement.utils.EngagementSdkLog;
import com.engagement.utils.LoginUserInfo;

import org.json.JSONArray;
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

    public void hitSeenApi(String mode, String actualUrl, UserActionsListener userActionsListener) {
        hitSeenApiRestCalls(mode, actualUrl, userActionsListener);
    }

    private void hitSeenApiRestCalls(String mode, String actualUrl, UserActionsListener userActionsListener) {
        JSONObject params = new JSONObject();
        ConstantFunctions.appendCommonParameterTORequest(params, Constants.RESOURCE_CAMPAIGN_TRACKING_VALUE, Constants.METHOD_SERVICE_VALUE);
        try {
            this.userActionsListener = userActionsListener;
            if (userActionsListener != null) {
                userActionsListener.onStart();
            }
            if (!LoginUserInfo.getValueForKey(Constants.TRACK_KEY, "").equalsIgnoreCase("")) {
                JSONObject paramsData = new JSONObject();
                if (EngagementSdk.getSingletonInstance() != null &&
                        EngagementSdk.getSingletonInstance().getContext() != null &&
                        LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null) != null) {
                    paramsData.put("user_id", LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null));
                }
                paramsData.put("track_key", new JSONArray(LoginUserInfo.getValueForKey(Constants.TRACK_KEY, "")));
                if (mode != null && !mode.equalsIgnoreCase("")) {
                    paramsData.put("mode", mode);
                }
                if (actualUrl != null && !actualUrl.equalsIgnoreCase("")) {
                    paramsData.put("action_url", actualUrl);
                }
                params.put("data", paramsData);
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

    private void hitSeenApiRestCalls(UserActionsListener userActionsListener) {
        JSONObject params = new JSONObject();
        ConstantFunctions.appendCommonParameterTORequest(params, Constants.RESOURCE_CAMPAIGN_TRACKING_VALUE, Constants.METHOD_SERVICE_VALUE);
        try {
            this.userActionsListener = userActionsListener;
            if (userActionsListener != null) {
                userActionsListener.onStart();
            }
            if (!LoginUserInfo.getValueForKey(Constants.TRACK_KEY, "").equalsIgnoreCase("")) {
                JSONObject paramsData = new JSONObject();
                if (EngagementSdk.getSingletonInstance() != null &&
                        EngagementSdk.getSingletonInstance().getContext() != null &&
                        LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null) != null) {
                    paramsData.put("user_id", LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null));
                }
                paramsData.put("track_key",  new JSONArray(LoginUserInfo.getValueForKey(Constants.TRACK_KEY, "")));
                paramsData.put("mode", Constants.MODE_VIEWED);
                params.put("data", paramsData);
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
