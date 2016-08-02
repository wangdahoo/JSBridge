package com.wangdahoo.example;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;

import com.wangdahoo.jsbridge.JSBridgeWebView;
import com.wangdahoo.jsbridge.JSBridgeWebViewClient;
import com.wangdahoo.jsbridge.MessageDispatcher;

public class WebViewActivity extends AppCompatActivity {

    final String TAG = "WV_ACTIVITY";

    JSBridgeWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = new JSBridgeWebView(this);
        MessageDispatcher messageDispatcher = new MessageDispatcher();
        messageDispatcher.registerHandler("DialogAlert", new DialogAlertHandler(this));
        webView.setWebViewClient(new JSBridgeWebViewClient(webView, messageDispatcher));
        webView.setWebChromeClient(new WebChromeClient());

        webView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        if (Build.VERSION.SDK_INT >= 19) {
            webView.setWebContentsDebuggingEnabled(true);
        }

        ViewGroup layout = (ViewGroup) findViewById(R.id.webview_activity);
        layout.addView(webView);

//        webView.loadUrl("http://www.baidu.com");

        webView.loadUrl("file:///android_asset/index.html");
    }
}
