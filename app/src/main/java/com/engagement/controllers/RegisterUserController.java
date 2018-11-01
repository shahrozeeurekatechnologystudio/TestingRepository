package com.engagement.controllers;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.engagement.EngagementSdk;
import com.engagement.interfaces.UserActionsListener;
import com.engagement.models.registeruser.EngagementUser;
import com.engagement.restkit.ApiUrl;
import com.engagement.restkit.RestCalls;
import com.engagement.utils.Constants;
import com.engagement.utils.EngagementSdkLog;
import com.engagement.utils.LoginUserInfo;

import org.json.JSONObject;


public class RegisterUserController {
    private static RegisterUserController instance;
    private UserActionsListener userActionsListener;

    public static RegisterUserController getSingletonInstance() {
        if (instance == null) {
            instance = new RegisterUserController();
        }
        return instance;
    }

    public void registerUser(EngagementUser engagementUser,JSONObject extraParams, UserActionsListener userActionsListener) {
        registerUserRestCalls(engagementUser,extraParams, userActionsListener);
    }
    public void updateInfo(String userId,JSONObject extraParams, UserActionsListener userActionsListener) {
        updateInfoRestCalls(userId,extraParams, userActionsListener);
    }
    private void registerUserRestCalls(EngagementUser engagementUser,JSONObject extraParams,UserActionsListener userActionsListener) {
        try {
            this.userActionsListener = userActionsListener;
            JSONObject params = new JSONObject();
            if(engagementUser!=null) {
                params.put("firstname", engagementUser.getFirstName());
                params.put("lastname", engagementUser.getLastName());
                params.put("email", engagementUser.getEmail());
                params.put("is_active", engagementUser.isIsActive());
                params.put("email_subscription", engagementUser.getEmailNotificationSubscription());
                params.put("country", engagementUser.getCountry());
                params.put("phone_number", engagementUser.getPhoneNumber());
                params.put("dob", engagementUser.getDateOfBirth());
                params.put("profile_image", engagementUser.getProfileImageURL());
                params.put("user_id", engagementUser.getUserID());

                if (engagementUser.getLongitude() != null && !engagementUser.getLongitude().equalsIgnoreCase("")) {
                    params.put("longitude", engagementUser.getLongitude());
                }
                if (engagementUser.getLatitude() != null && !engagementUser.getLatitude().equalsIgnoreCase("")) {
                    params.put("latitude", engagementUser.getLatitude());
                }
            }
            if(extraParams!=null)
              params.put("extra_params", extraParams);
            RestCalls addActionDetailRequest = new RestCalls(Request.Method.POST, ApiUrl.getRegisterUserUrl(),
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
    private void updateInfoRestCalls(String userId,JSONObject extraParams,UserActionsListener userActionsListener) {
        try {
            this.userActionsListener = userActionsListener;
            JSONObject params = new JSONObject();
            params.put("user_id", userId);
            if(extraParams!=null)
                params.put("extra_params", extraParams);
            RestCalls addActionDetailRequest = new RestCalls(Request.Method.POST, ApiUrl.getRegisterUserUrl(),
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
                        if (response.getJSONObject(Constants.API_RESPONSE_DATA_KEY) != null &&
                                response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).getJSONObject(Constants.API_RESPONSE_USER_DATA_KEY) != null
                                && response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).getJSONObject(Constants.API_RESPONSE_USER_DATA_KEY).get(Constants.API_RESPONSE_USER_TOKEN_KEY) != null
                                && response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).getJSONObject(Constants.API_RESPONSE_USER_DATA_KEY).get(Constants.API_RESPONSE_USER_TOKEN_KEY).toString() != null) {
                            LoginUserInfo.setValueForKey(Constants.LOGIN_USER_SESSION_TOKEN_KEY, response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).getJSONObject(Constants.API_RESPONSE_USER_DATA_KEY).get(Constants.API_RESPONSE_USER_TOKEN_KEY).toString());
                        }
                        if (EngagementSdk.getSingletonInstance() != null && EngagementSdk.getSingletonInstance().getEngagementUser() != null
                                && EngagementSdk.getSingletonInstance().getEngagementUser().getUserID() != null) {
                            LoginUserInfo.setValueForKey(Constants.LOGIN_USER_ID_KEY, EngagementSdk.getSingletonInstance().getEngagementUser().getUserID());
                        }
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