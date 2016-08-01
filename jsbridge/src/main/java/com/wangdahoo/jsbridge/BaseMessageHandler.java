package com.wangdahoo.jsbridge;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tom on 16/8/1.
 */
public class BaseMessageHandler implements MessageHandler {

    public static String getStringValueByField(JSONObject jsonObject, String field) {
        String value = null;
        try {
            value = jsonObject.getString(field);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static JSONObject getData(JSONObject message) {
        JSONObject data = null;
        try {
            data = message.getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void handle(JSONObject message, Callback callback) {
        String callbackId = getStringValueByField(message, "callbackId");

        if (callback != null)
            callback.onCallback(makeResponse(callbackId));
    }

    public static String makeResponseWithResults(String callbackId, Object[] results) {
        JSONObject response = initResponse(callbackId);
        try {
            for (int i=0; i<results.length/2; i++) {
                String key = (String) results[2*i];
                response.put(key, results[2*i + 1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return response.toString();
    }

    public static String makeResponse(String callbackId) {
        return initResponse(callbackId).toString();
    }

    private static JSONObject initResponse(String callbackId) {
        JSONObject response = new JSONObject();

        try {
            response.put("callbackId", callbackId);
            response.put("status", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

}
