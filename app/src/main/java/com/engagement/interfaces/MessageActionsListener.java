package com.engagement.interfaces;



import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Shoaib on 8/9/2016.
 */
public abstract class MessageActionsListener {

    public abstract void  onMessageSilent(Map<String, String> data);
    public abstract void  onMessageNotPlatformEngagement(Map<String, String> data);

}
