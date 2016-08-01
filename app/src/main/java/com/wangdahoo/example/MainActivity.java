package com.wangdahoo.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wangdahoo.jsbridge.JSBridgeWebView;

public class MainActivity extends AppCompatActivity {

    JSBridgeWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (JSBridgeWebView) findViewById(R.id.web_view);

//        mWebView.loadUrl("http://www.baidu.com");
        mWebView.loadUrl("file:///android_asset/index.html");

    }
}
