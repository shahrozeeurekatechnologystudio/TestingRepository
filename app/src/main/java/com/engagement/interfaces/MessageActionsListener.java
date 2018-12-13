package com.engagement.interfaces;



import android.os.Bundle;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Shoaib on 8/9/2016.
 */
public abstract class MessageActionsListener {

    public abstract void  onMessageSilent(Map<String, String> map);
    public abstract void  onMessageSilent(Bundle bundle);
    public abstract void  onMessageNotPlatformEngagement(Map<String, String> map);
    public abstract void  onMessageNotPlatformEngagement(Bundle bundle);

}
