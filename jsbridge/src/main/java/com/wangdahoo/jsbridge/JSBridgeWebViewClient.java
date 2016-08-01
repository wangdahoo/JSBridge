package com.wangdahoo.jsbridge;

import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by tom on 16/8/1.
 */
public class JSBridgeWebViewClient extends WebViewClient {

    final String CUSTOM_PROTOCOL_SCHEME = "wvjbscheme";
    final String QUEUE_HAS_MESSAGE = "__WVJB_QUEUE_MESSAGE__";

    JSBridgeWebView webView;
    MessageDispatcher messageDispatcher;

    public JSBridgeWebViewClient(JSBridgeWebView webView) {
        super();
        this.webView = webView;
        webView.addJavascriptInterface(webView.messageDispatcher, "MessageDispatcher");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        String script = "WebViewJavascriptBridge._fetchMessageQueue()";

        if (url.equals(CUSTOM_PROTOCOL_SCHEME + "://" + QUEUE_HAS_MESSAGE)) {
            webView.loadUrl("javascript:MessageDispatcher.onReceiveMessage(" + script + ")");
            return true;
        }

        return super.shouldOverrideUrlLoading(view, url);
    }
}
