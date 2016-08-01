package com.wangdahoo.example;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.wangdahoo.jsbridge.BaseMessageHandler;
import com.wangdahoo.jsbridge.Callback;
import com.wangdahoo.jsbridge.MessageHandler;

import org.json.JSONObject;

/**
 * Created by tom on 16/8/1.
 */
public class DialogAlertHandler extends BaseMessageHandler implements MessageHandler {

    String title = "提示";
    String content = "";
    String btnOkText = "确定";

    Context context;

    public DialogAlertHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handle(JSONObject message, final Callback callback) {

        JSONObject data = getData(message);
        final String callbackId = getStringValueByField(message, "callbackId");

        if (data.has("title"))
            title = getStringValueByField(data, "title");
        if (data.has("content"))
            content = getStringValueByField(data, "content");
        if (data.has("btnOkText"))
            btnOkText = getStringValueByField(data, "btnOkText");

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(btnOkText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null)
                            callback.onCallback(makeResponse(callbackId));
                    }
                });

        builder.create();
        builder.show();
    }

}
