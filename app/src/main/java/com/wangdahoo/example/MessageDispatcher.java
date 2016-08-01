package com.wangdahoo.example;

import android.webkit.JavascriptInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息派发组件
 * 拦截js端发送过来的消息，然后派发给消息中指定的消息处理组件（Handler）进行处理
 */

public class MessageDispatcher {
    final String TAG = "MESSAGE_DISPATCHER";
    final String DEFAULT_HANDLER = "DefaultHandler";

    Map<String, MessageHandler> handlers;

    Callback callback;

    public MessageDispatcher(Callback callback) {
        this.handlers = new HashMap<String, MessageHandler>();
        this.callback = callback;
        this.registerHandler(DEFAULT_HANDLER, new BaseMessageHandler());
    }

    /**
     * 注：该方法需要通过注释@JavascriptInterface曝露到js环境中
     * @param messageQueueStr
     */
    @JavascriptInterface
    public void onReceiveMessage(String messageQueueStr) {
        try {
            JSONArray messageQueue = new JSONArray(messageQueueStr);
            for (int i=0; i<messageQueue.length(); i++) {
                JSONObject message = messageQueue.getJSONObject(i);
                String handlerName = message.getString("handlerName");

                if (handlers.containsKey(handlerName)) {
                    handlers.get(handlerName).handle(message, callback);
                } else {
                    handlers.get(DEFAULT_HANDLER).handle(message, callback);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void registerHandler(String handlerName, MessageHandler handler) {
        this.handlers.put(handlerName, handler);
    }

}
