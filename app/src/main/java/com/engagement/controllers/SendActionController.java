package com.engagement.controllers;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.engagement.EngagementSdk;
import com.engagement.R;
import com.engagement.interfaces.UserActionsListener;
import com.engagement.restkit.ApiUrl;
import com.engagement.restkit.RestCalls;
import com.engagement.utils.ActionsEnums;
import com.engagement.utils.ConstantFunctions;
import com.engagement.utils.Constants;
import com.engagement.utils.EngagementSdkLog;
import com.engagement.utils.LoginUserInfo;

import org.json.JSONArray;
import org.json.JSONObject;


public class SendActionController {
    private static SendActionController instance;
    private UserActionsListener userActionsListener;

    public static SendActionController getSingletonInstance() {
        if (instance == null) {
            instance = new SendActionController();
        }
        return instance;
    }

    public void sendEvents(ActionsEnums actionsEnums, JSONObject params, UserActionsListener userActionsListener) {
        registerSendEventsRestCalls(actionsEnums, params, userActionsListener);
    }

    private void registerSendEventsRestCalls(ActionsEnums actionsEnum, JSONObject params, UserActionsListener userActionsListener) {
        if (params != null && (EngagementSdk.getSingletonInstance() != null &&
                EngagementSdk.getSingletonInstance().getContext()!= null &&
                LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null) != null)) {
            JSONObject jsonObjectSendToApi = new JSONObject();
            try {
                this.userActionsListener = userActionsListener;
                if (userActionsListener != null) {
                    userActionsListener.onStart();
                }
                params.put("user_id", LoginUserInfo.getValueForKey(Constants.LOGIN_USER_ID_KEY, null));
                if (actionsEnum == ActionsEnums.ACTION) {
                    ConstantFunctions.appendCommonParameterTORequest(jsonObjectSendToApi, Constants.RESOURCE_CAMPAIGN_ACTION_TRIGGER_VALUE, Constants.METHOD_SEND_VALUE);
                    jsonObjectSendToApi.put("data", params);
                    RestCalls addActionDetailRequest = new RestCalls(Request.Method.POST,  ApiUrl.getCampaignTriggerEventUrl(),
                            jsonObjectSendToApi, responseListener(),
                            errorListener());
                    EngagementSdk.getSingletonInstance().getRequestQueue().add(addActionDetailRequest);
                } else if (actionsEnum == ActionsEnums.CONVERSION) {
                    if(!LoginUserInfo.getValueForKey(Constants.CAMPAIGN_RECEIVE_DATE,"").equalsIgnoreCase("")
                            && !LoginUserInfo.getValueForKey(Constants.TRACK_KEY,"").equalsIgnoreCase("")) {
                        params.put("track_key",  new JSONArray(LoginUserInfo.getValueForKey(Constants.TRACK_KEY, "")));
                        params.put("campaign_receive_date",LoginUserInfo.getValueForKey(Constants.CAMPAIGN_RECEIVE_DATE,""));
                        ConstantFunctions.appendCommonParameterTORequest(jsonObjectSendToApi, Constants.RESOURCE_CAMPAIGN_CONVERSION_TRIGGER_VALUE, Constants.METHOD_SEND_VALUE);
                        jsonObjectSendToApi.put("data", params);
                        RestCalls addActionDetailRequest = new RestCalls(Request.Method.POST, ApiUrl.getConversionTriggerEventUrl(),
                                jsonObjectSendToApi, responseListener(),
                                errorListener());
                        EngagementSdk.getSingletonInstance().getRequestQueue().add(addActionDetailRequest);
                    }
                    else{
                        if (userActionsListener != null) {
                            if(EngagementSdk.getSingletonInstance()!=null && EngagementSdk.getSingletonInstance().getActiveActivity()!=null){
                              userActionsListener.onError(EngagementSdk.getSingletonInstance().getActiveActivity().getResources().getString(R.string.no_campaign_receive_yet));
                            }
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
