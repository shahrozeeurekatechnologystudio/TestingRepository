package com.engagement.interfaces;



import org.json.JSONObject;

/**
 * Created by Shoaib on 8/9/2016.
 */
public abstract class UserActionsListener {

    public abstract void  onStart();
    public abstract void onCompleted(JSONObject object);
    public abstract void onError(String exception);
}
