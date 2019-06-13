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

import static com.engagement.utils.Constants.API_RESPONSE_USER_TOKEN_KEY;


public class LogoOutController {
    private static LogoOutController instance;
    private UserActionsListener userActionsListener;

    public static LogoOutController getSingletonInstance() {
        if (instance == null) {
            instance = new LogoOutController();
        }
        return instance;
    }

    public void logOut(UserActionsListener userActionsListener) {
        logOutRestCalls(userActionsListener);
    }

    private void logOutRestCalls(UserActionsListener userActionsListener) {
        try {
            this.userActionsListener = userActionsListener;
            JSONObject jsonObjectParams = new JSONObject();
            ConstantFunctions.appendCommonParameterTORequest(jsonObjectParams, Constants.RESOURCE_USER_VALUE, Constants.METHOD_SUBSCRIBE_VALUE);
            JSONObject params = new JSONObject();
            if (LoginUserInfo.getValueForKey(Constants.LOGIN_USER_SESSION_TOKEN_KEY, null) != null) {
                params.put(API_RESPONSE_USER_TOKEN_KEY, LoginUserInfo.getValueForKey(Constants.LOGIN_USER_SESSION_TOKEN_KEY, null));
            }
            if (LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null) != null) {
                params.put(Constants.LOGIN_USER_ID_KEY, LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null));
            }
            params.put(Constants.MODE_KEY, "logout");
            jsonObjectParams.put("data", params);
            RestCalls addActionDetailRequest = new RestCalls(Request.Method.POST, ApiUrl.getCompanyLogoutLink(),
                    jsonObjectParams, responseListener(),
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
