package com.wangdahoo.example;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class WebViewActivity extends AppCompatActivity {

    final String TAG = "WEBVIEW_ACTIVITY";

    final String CUSTOM_PROTOCOL_SCHEME = "wvjbscheme";
    final String QUEUE_HAS_MESSAGE = "__WVJB_QUEUE_MESSAGE__";

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = new WebView(this);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        if (Build.VERSION.SDK_INT >= 19) {
            webView.setWebContentsDebuggingEnabled(true);
        }

        MessageDispatcher messageDispatcher = new MessageDispatcher();
        messageDispatcher.registerHandler("DialogAlert", new DialogAlertHandler(this));

        webView.addJavascriptInterface(messageDispatcher, "MessageDispatcher");
        JSBridgeUtils.injectJSBridge(webView, "WebViewJavascriptBridge.js");

        ViewGroup layout = (ViewGroup) findViewById(R.id.webview_activity);
        layout.addView(webView);

//        webView.loadUrl("http://www.baidu.com");

        webView.loadUrl("file:///android_asset/index.html");
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.i(TAG, "url: " + url);

            // 拦截js端发送过来的消息
            if (url.startsWith(CUSTOM_PROTOCOL_SCHEME + "://" + QUEUE_HAS_MESSAGE)) {
                String script = "WebViewJavascriptBridge._fetchQueue()";
                webView.loadUrl("javascript:window.MessageDispatcher.onReceiveMessage(" + script + ")");
                return true;
            }

            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
