package com.engagement.controllers;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.engagement.EngagementSdk;
import com.engagement.interfaces.UserActionsListener;
import com.engagement.restkit.ApiUrl;
import com.engagement.restkit.RestCalls;
import com.engagement.utils.Constants;
import com.engagement.utils.EngagementSdkLog;
import com.engagement.utils.LoginUserInfo;

import org.json.JSONObject;


public class ApiTriggerController {
    private static ApiTriggerController instance;
    private UserActionsListener userActionsListener;

    public static ApiTriggerController getSingletonInstance() {
        if (instance == null) {
            instance = new ApiTriggerController();
        }
        return instance;
    }

    public void registerTriggerAction(String campaignCode, JSONObject extraParams, UserActionsListener userActionsListener) {
        registerTriggerActionRestCalls(campaignCode, extraParams, userActionsListener);
    }

    private void registerTriggerActionRestCalls(String campaignCode, JSONObject extraParams, UserActionsListener userActionsListener) {
        try {
            this.userActionsListener = userActionsListener;
            JSONObject params = new JSONObject();
            if (EngagementSdk.getSingletonInstance() != null &&
                    EngagementSdk.getSingletonInstance().getContext()!= null &&
                    LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null) != null) {
                    params.put("user_id", LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null));
            }
            if (campaignCode != null && !campaignCode.equalsIgnoreCase("")) {
                params.put("campaign_code", campaignCode);
            }
            if (extraParams != null)
                params.put("extra_params", extraParams);
            RestCalls addActionDetailRequest = new RestCalls(Request.Method.POST, ApiUrl.getApiTriggerLink(),
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
