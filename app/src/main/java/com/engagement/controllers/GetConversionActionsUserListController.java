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


public class GetConversionActionsUserListController {
    private static GetConversionActionsUserListController instance;
    private UserActionsListener userActionsListener;

    public static GetConversionActionsUserListController getSingletonInstance() {
        if (instance == null) {
            instance = new GetConversionActionsUserListController();
        }
        return instance;
    }

    public void getConversionActionsUserListController(String data_type, UserActionsListener userActionsListener) {
        getConversionActionsUserListControllerRestCalls(data_type, userActionsListener);
    }

    private void getConversionActionsUserListControllerRestCalls(String data_type, UserActionsListener userActionsListener) {
        try {
            this.userActionsListener = userActionsListener;
            JSONObject jsonObjectSendToApi = new JSONObject();
            ConstantFunctions.appendCommonParameterTORequest(jsonObjectSendToApi, Constants.RESOURCE_GET_USER_ACTION, Constants.METHOD_LIST_VALUE);
            JSONObject paramsData = new JSONObject();
            if (EngagementSdk.getSingletonInstance() != null &&
                    EngagementSdk.getSingletonInstance().getContext() != null &&
                    LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null) != null) {
                paramsData.put("user_id", LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null));
            }
            if (data_type != null && !data_type.equalsIgnoreCase("")) {
                paramsData.put("data_type", data_type);
            }
            jsonObjectSendToApi.put("data", paramsData);
            RestCalls addActionDetailRequest = new RestCalls(Request.Method.POST, ApiUrl.getListTriggerEventUrl(),
                    jsonObjectSendToApi, responseListener(),
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
