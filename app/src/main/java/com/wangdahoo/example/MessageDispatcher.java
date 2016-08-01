package com.wangdahoo.example;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.wangdahoo.jsbridge.MessageHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 消息派发组件
 * 拦截js端发送过来的消息，然后派发给消息中指定的消息处理组件（Handler）进行处理
 */

public class MessageDispatcher {
    final String TAG = "MESSAGE_DISPATCHER";

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

                Log.i("TAG", handlerName);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
