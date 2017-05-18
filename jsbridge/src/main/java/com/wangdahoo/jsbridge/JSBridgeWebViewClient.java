package com.wangdahoo.jsbridge;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by tom on 16/8/1.
 */
public class JSBridgeWebViewClient extends WebViewClient {

    final String TAG = "JBWV_C";

    final String CUSTOM_PROTOCOL_SCHEME = "wvjbscheme";
    final String QUEUE_HAS_MESSAGE = "__WVJB_QUEUE_MESSAGE__";

    JSBridgeWebView webView;

    private Callback callback = new Callback() {
        @Override
        public void onCallback(final String data) {
            Log.i(TAG, "Message has been handled by handler, send back response to js"); // 再把消息发回给js环境

            webView.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:window.WebViewJavascriptBridge._responseBackFromJava('" + data + "')");
                }
            });
        }
    };

    public JSBridgeWebViewClient(JSBridgeWebView _webView, MessageDispatcher messageDispatcher) {
        super();
        webView = _webView;
        messageDispatcher.setCallback(callback);
        webView.addJavascriptInterface(messageDispatcher, "MessageDispatcher");

//        改为网页端加载WebViewJavascriptBridge.js
//        JSBridgeUtils.injectJSBridge(webView, "WebViewJavascriptBridge.js");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // 拦截js端发送过来的消息
        if (url.startsWith(CUSTOM_PROTOCOL_SCHEME + "://" + QUEUE_HAS_MESSAGE)) {
            String script = "WebViewJavascriptBridge._fetchQueue()";
            webView.loadUrl("javascript:window.MessageDispatcher.onReceiveMessage(" + script + ")");
            return true;
        }

        return super.shouldOverrideUrlLoading(view, url);
    }
}
