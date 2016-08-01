package com.wangdahoo.jsbridge;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by tom on 16/8/1.
 */
public class DefaultMessageHandler implements MessageHandler {

    final String TAG = "DEFAULT_MESSAGE_HANDLER";

    @Override
    public void handle(JSONObject message, Callback callback) {
        Log.i(TAG, message.toString());
    }
}
