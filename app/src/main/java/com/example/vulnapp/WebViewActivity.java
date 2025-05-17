package com.example.vulnapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends MainActivity {

    private WebView webView;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // ❌ Allows JS execution
        webSettings.setAllowFileAccess(true); // ❌ Local file access
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true); // Optional: localStorage, etc.
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW); // ❌ Insecure content over HTTPS

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            // ❌ Trusts all URLs, no origin validation
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false; // Allow all URLs to load
            }
        });

        // ❌ Exposes Java object to JS
        webView.addJavascriptInterface(new InsecureJSInterface(), "AndroidInterface");

        // Get and load the URL
        String url = getIntent().getDataString();
        if (url != null) {
            webView.loadUrl(url);
        }
    }

    public class InsecureJSInterface {
        @JavascriptInterface
        public void showToast(String message) {
            // ❌ Arbitrary JS code can call this method
            Toast.makeText(WebViewActivity.this, "From JS: " + message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}

