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


public class FireBaseTokenController {


    public static void sendRegistrationToServer(String token,UserActionsListener userActionsListener) {
        try {
            if (EngagementSdk.getSingletonInstance() != null &&
                    EngagementSdk.getSingletonInstance().getContext()!= null &&
                    LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null) != null) {
                JSONObject params = new JSONObject();
                params.put("user_id",
                        LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null));
                params.put("fire_base_key", token);
                params.put("device_token", token);
                params.put("is_login",
                        true);
                RestCalls myReq = new RestCalls(Request.Method.POST, ApiUrl.getRegisterTokenLink(),
                        params, responseListener(false,userActionsListener),
                        errorListener(userActionsListener));
                EngagementSdk.getSingletonInstance().getRequestQueue().add(myReq);
            }
            else {
                if (userActionsListener != null) {
                    userActionsListener.onError("");
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (userActionsListener != null) {
                if(e.toString()!=null)
                    userActionsListener.onError(e.toString());
                else
                    userActionsListener.onError("");

            }
        }


    }

    public static void sendRegistrationToServerExpireOnLogOut(String token,UserActionsListener userActionsListener) {
        try {
            if (EngagementSdk.getSingletonInstance() != null &&
                    EngagementSdk.getSingletonInstance().getContext()!= null &&
                    LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null) != null) {
                JSONObject params = new JSONObject();
                params.put("user_id",
                        LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null));
                params.put("fire_base_key", token);
                params.put("device_token", token);
                params.put("is_login",
                        false);
                RestCalls myReq = new RestCalls(Request.Method.POST, ApiUrl.getRegisterTokenLink(),
                        params, responseListener(true,userActionsListener),
                        errorListener(userActionsListener));
                EngagementSdk.getSingletonInstance().getRequestQueue().add(myReq);
            }
            else {
                if (userActionsListener != null) {
                    userActionsListener.onError("");
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            if (userActionsListener != null) {
                if(e.toString()!=null)
                  userActionsListener.onError(e.toString());
                else
                    userActionsListener.onError("");

            }
        }


    }

    private static Response.Listener<JSONObject> responseListener(final boolean isLogoutUser,final UserActionsListener userActionsListener) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getJSONObject(Constants.API_RESPONSE_META_KEY).getString(Constants.API_RESPONSE_CODE_KEY).toString()
                            .equalsIgnoreCase(Constants.SERVER_OK_REQUEST_CODE)) {
                        if (isLogoutUser) {
                            LoginUserInfo.setValueForKey(Constants.LOGIN_USER_EMAIL_KEY, null);
                            LoginUserInfo.setValueForKey(Constants.LOGIN_USER_SESSION_TOKEN_KEY, null);

                        } else {
                            if (response.getJSONObject(Constants.API_RESPONSE_DATA_KEY) != null &&
                                    response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).getJSONObject(Constants.API_RESPONSE_USER_DATA_KEY) != null) {
                                if (response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).getJSONObject(Constants.API_RESPONSE_USER_DATA_KEY).get(Constants.API_RESPONSE_USER_EMAIL_KEY) != null
                                        && response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).getJSONObject(Constants.API_RESPONSE_USER_DATA_KEY).get(Constants.API_RESPONSE_USER_EMAIL_KEY).toString() != null) {
                                    LoginUserInfo.setValueForKey(Constants.LOGIN_USER_EMAIL_KEY, response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).getJSONObject(Constants.API_RESPONSE_USER_DATA_KEY).get(Constants.API_RESPONSE_USER_EMAIL_KEY).toString());
                                }

                                if (response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).getJSONObject(Constants.API_RESPONSE_USER_DATA_KEY).get(Constants.API_RESPONSE_USER_TOKEN_KEY) != null
                                        && response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).getJSONObject(Constants.API_RESPONSE_USER_DATA_KEY).get(Constants.API_RESPONSE_USER_TOKEN_KEY).toString() != null) {
                                    LoginUserInfo.setValueForKey(Constants.LOGIN_USER_SESSION_TOKEN_KEY, response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).getJSONObject(Constants.API_RESPONSE_USER_DATA_KEY).get(Constants.API_RESPONSE_USER_TOKEN_KEY).toString().toString());
                                }
                            }
                        }
                        if (userActionsListener != null) {
                            userActionsListener.onCompleted(response);
                        }
                    } else {
                        if (response.getJSONArray(Constants.API_RESPONSE_ERROR_KEY) != null && response.getJSONArray(Constants.API_RESPONSE_ERROR_KEY).get(0) != null &&
                                response.getJSONArray(Constants.API_RESPONSE_ERROR_KEY).get(0).toString() != null) {
                            EngagementSdkLog.logDebug(EngagementSdkLog.TAG_VOLLEY_ERROR, response.getJSONArray(Constants.API_RESPONSE_ERROR_KEY).get(0).toString());
                            if (userActionsListener != null) {
                                userActionsListener.onError(response.getJSONArray(Constants.API_RESPONSE_ERROR_KEY).get(0).toString());
                            }
                        }
                        else{
                            if (userActionsListener != null) {
                                userActionsListener.onError("");
                            }
                        }

                    }

                } catch (Exception e) {
                    if (userActionsListener != null) {
                        if(e.toString()!=null)
                            userActionsListener.onError(e.toString());
                        else
                            userActionsListener.onError("");

                    }
                    e.printStackTrace();
                }

            }
        };
    }

    private static Response.ErrorListener errorListener(final UserActionsListener userActionsListener) {
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
                    else{
                        if (userActionsListener != null) {
                            userActionsListener.onError("");
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    if (userActionsListener != null) {
                        if(e.toString()!=null)
                            userActionsListener.onError(e.toString());
                        else
                            userActionsListener.onError("");

                    }

                }
            }
        };
    }
}
