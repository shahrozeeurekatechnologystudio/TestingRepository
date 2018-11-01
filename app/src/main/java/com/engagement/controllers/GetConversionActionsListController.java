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

import org.json.JSONObject;


public class GetConversionActionsListController {
    private static GetConversionActionsListController instance;
    private UserActionsListener userActionsListener;

    public static GetConversionActionsListController getSingletonInstance() {
        if (instance == null) {
            instance = new GetConversionActionsListController();
        }
        return instance;
    }

    public void getConversionActionsListController(String userID, String data_type, UserActionsListener userActionsListener) {
        getConversionActionsListControllerRestCalls(userID, data_type, userActionsListener);
    }

    private void getConversionActionsListControllerRestCalls(String userID, String data_type, UserActionsListener userActionsListener) {
        try {
            this.userActionsListener = userActionsListener;
            JSONObject params = new JSONObject();
            if (userID != null && !userID.equalsIgnoreCase("")) {
                params.put("user_id", userID);
            }
            if (data_type != null && !data_type.equalsIgnoreCase("")) {
                params.put("data_type", data_type);
            }
            RestCalls addActionDetailRequest = new RestCalls(Request.Method.POST, ApiUrl.getListTriggerEventUrl(),
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
