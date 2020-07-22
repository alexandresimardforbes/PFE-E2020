package com.azuredev.android;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.noanandroidapplication.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_web);

        WebView myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        myWebView.addJavascriptInterface(new JSInterface(this), "Android"); //You will access this via Android.method(args);

        myWebView.loadUrl("https://www.google.com"); // todo Load local web page

//        myWebView.loadUrl("javascript: var result = window.Android.FetchJavaData(); window.DoStuff(result)");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

}