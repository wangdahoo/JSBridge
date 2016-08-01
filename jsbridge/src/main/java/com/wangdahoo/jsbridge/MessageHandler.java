package com.wangdahoo.jsbridge;


import org.json.JSONObject;

/**
 * Created by tom on 16/8/1.
 */
public interface MessageHandler {
    public void handle(JSONObject message, Callback callback);
}
