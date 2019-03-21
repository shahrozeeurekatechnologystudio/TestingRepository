package com.engagement.controllers;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.engagement.EngagementSdk;
import com.engagement.interfaces.UserActionsListener;
import com.engagement.models.registeruser.EngagementUser;
import com.engagement.restkit.ApiUrl;
import com.engagement.restkit.RestCalls;
import com.engagement.utils.ConstantFunctions;
import com.engagement.utils.Constants;
import com.engagement.utils.EngagementSdkLog;
import com.engagement.utils.LoginUserInfo;
import com.engagement.utils.UserActionsModeEnums;

import org.json.JSONObject;


public class UserActionController {
    private static UserActionController instance;
    private UserActionsListener userActionsListener;

    public static UserActionController getSingletonInstance() {
        if (instance == null) {
            instance = new UserActionController();
        }
        return instance;
    }

    public void hitUserAction(UserActionsModeEnums userActionsModeEnums, EngagementUser engagementUser, UserActionsListener userActionsListener) {
        hitUserActionRestCalls(userActionsModeEnums,engagementUser, userActionsListener);
    }
    public void updateInfo(UserActionsModeEnums userActionsModeEnums,JSONObject extraParams, UserActionsListener userActionsListener) {
        updateInfoRestCalls(userActionsModeEnums,extraParams, userActionsListener);
    }
    private void hitUserActionRestCalls(UserActionsModeEnums userActionsModeEnums,EngagementUser engagementUser,UserActionsListener userActionsListener) {
        try {
            if (EngagementSdk.getSingletonInstance() != null && EngagementSdk.getSingletonInstance().getContext()!=null
                    && engagementUser != null) {
                if (engagementUser.getCompanyKey() != null
                        && !engagementUser.getCompanyKey().equalsIgnoreCase("")) {
                    LoginUserInfo.setValueForKey(Constants.COMPANY_KEY, engagementUser.getCompanyKey());
                    if (engagementUser.getLanguage() != null
                            && !engagementUser.getLanguage().equalsIgnoreCase("")) {
                        LoginUserInfo.setValueForKey(Constants.LANGUAGE_KEY, engagementUser.getLanguage());
                    }
                }
            }
            this.userActionsListener = userActionsListener;
            JSONObject jsonObjectParams = new JSONObject();
            ConstantFunctions.appendCommonParameterTORequest(jsonObjectParams);
            JSONObject params = new JSONObject();
            if(engagementUser!=null) {
                params.put(Constants.MODE_KEY,userActionsModeEnums);
                params.put("user_id", engagementUser.getUserID());
                params.put("firstname", engagementUser.getFirstName());
                params.put("lastname", engagementUser.getLastName());
                params.put("username", engagementUser.getUserName());
                params.put("email", engagementUser.getEmail());
                params.put("device_token", engagementUser.getDeviceToken());
                params.put("is_active", engagementUser.isIsActive());
                params.put("email_subscription", engagementUser.getEmailNotificationSubscription());
                params.put("country", engagementUser.getCountry());
                params.put("phone_number", engagementUser.getPhoneNumber());
                params.put("dob", engagementUser.getDateOfBirth());
                params.put("profile_image", engagementUser.getProfileImageURL());

                if (engagementUser.getLongitude() != null && !engagementUser.getLongitude().equalsIgnoreCase("")) {
                    params.put("longitude", engagementUser.getLongitude());
                }
                if (engagementUser.getLatitude() != null && !engagementUser.getLatitude().equalsIgnoreCase("")) {
                    params.put("latitude", engagementUser.getLatitude());
                }
            }
            jsonObjectParams.put("data",params);
            RestCalls addActionDetailRequest = new RestCalls(Request.Method.POST, ApiUrl.getUserActionLink(),
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
    private void updateInfoRestCalls(UserActionsModeEnums userActionsModeEnums,JSONObject extraParams,UserActionsListener userActionsListener) {
        try {
            this.userActionsListener = userActionsListener;
            JSONObject jsonObjectParams = new JSONObject();
            ConstantFunctions.appendCommonParameterTORequest(jsonObjectParams);
            extraParams.put(Constants.MODE_KEY,userActionsModeEnums);
            if (EngagementSdk.getSingletonInstance() != null &&
                    EngagementSdk.getSingletonInstance().getContext()!= null &&
                    LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null) != null) {
                extraParams.put("user_id", LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null));
            }
            jsonObjectParams.put("data",extraParams);
            RestCalls addActionDetailRequest = new RestCalls(Request.Method.POST, ApiUrl.getUserActionLink(),
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
                        if (response.getJSONObject(Constants.API_RESPONSE_DATA_KEY) != null
                                && response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).get(Constants.API_RESPONSE_USER_TOKEN_KEY) != null
                                && response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).get(Constants.API_RESPONSE_USER_TOKEN_KEY).toString() != null) {
                            LoginUserInfo.setValueForKey(Constants.LOGIN_USER_SESSION_TOKEN_KEY, response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).get(Constants.API_RESPONSE_USER_TOKEN_KEY).toString());
                        }
                        if (response.getJSONObject(Constants.API_RESPONSE_DATA_KEY) != null
                                && response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).get(Constants.LOGIN_USER_ID_KEY) != null
                                && response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).get(Constants.LOGIN_USER_ID_KEY).toString() != null) {
                            LoginUserInfo.setValueForKey(Constants.LOGIN_USER_ID_KEY, response.getJSONObject(Constants.API_RESPONSE_DATA_KEY).get(Constants.LOGIN_USER_ID_KEY).toString());
                        }
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