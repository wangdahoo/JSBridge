package com.wangdahoo.jsbridge;

import android.telecom.Call;
import android.webkit.JavascriptInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tom on 16/8/1.
 */
public class MessageDispatcher {
    private Callback callback;

    private DefaultMessageHandler defaultMessageHandler = new DefaultMessageHandler();

    private Map<String, MessageHandler> handlers = new HashMap<String, MessageHandler>();

    public MessageDispatcher(Callback callback) {
        this.callback = callback;
    }

    public MessageDispatcher() {

    }

    public void addMessageHandler(String handlerName, MessageHandler handler) {
        handlers.put(handlerName, handler);
    }

    @JavascriptInterface
    public void onReceiveMessage(String messageQueueStr) {
        try {
            JSONArray messageQueue = new JSONArray(messageQueueStr);
            for (int i=0; i<messageQueue.length(); i++) {
                JSONObject message = messageQueue.getJSONObject(i);

                String handlerName = message.getString("handlerName");
                MessageHandler handler = handlers.get(handlerName);

                if (handler != null) {
                    handler.handle(message, callback);
                } else {
                    defaultMessageHandler.handle(message, callback);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
