package com.wangdahoo.jsbridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by tom on 16/8/1.
 */

@SuppressLint("SetJavaScriptEnabled")
public class JSBridgeWebView extends WebView {

    final String TAG = "JBWV";

    public JSBridgeWebView(Context context) {
        super(context);
        init();
    }

    public JSBridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JSBridgeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        this.setInitialScale(0);
        this.setVerticalScrollBarEnabled(false);

        // WebView Debugging
        if (Build.VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // Enable JavaScript
        final WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        //Don't save any form data in the application
        settings.setSaveFormData(false);
        settings.setSavePassword(false);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }

        // Enable database
        // We keep this disabled because we use or shim to get around DOM_EXCEPTION_ERROR_16
        String databasePath = this.getContext().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(databasePath);

        // Enable AppCache
        settings.setAppCacheMaxSize(5 * 1048576);
        settings.setAppCachePath(databasePath);
        settings.setAppCacheEnabled(true);

        // Enable DOM storage
        settings.setDomStorageEnabled(true);

        // Reset UserAgent
        String defaultUserAgent = settings.getUserAgentString();
        settings.setUserAgentString(defaultUserAgent + " JSBridge/1.0.0");
    }
}
