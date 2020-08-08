package com.azuredev.android;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class JSInterface {
    private final WebView myWebView;
    private final HAL hal;

    public JSInterface(HAL hal, WebView myWebView) {
        this.myWebView = myWebView;
        this.hal = hal;

//      myWebView.loadUrl("javascript: if(window);
    }

    @JavascriptInterface
    public void SavePreferences(String key, String value) {
        hal.savePreferences(key, value);
    }

    @JavascriptInterface
    public String LoadPreferences(String key) {
        return hal.loadPreferences(key);
    }

    @JavascriptInterface
    public String getRemoteValue() {
        return hal.getRemoteValue();
    }

    @JavascriptInterface
    public String getUID() {
        return hal.getUID();
    }

    @JavascriptInterface
    public String getChannel() {
        return hal.getChannel();
    }

    @JavascriptInterface
    public void startDigitalChannelScan() {
        hal.startDigitalChannelScan();
    }

    @JavascriptInterface
    public void setNextInputSource() {
        hal.setNextInputSource();
    }

    @JavascriptInterface
    public void setInputSource(String source) {
        hal.setInputSource(source);
    }

    @JavascriptInterface
    public void channelUp() {
        hal.channelUp();
    }

    @JavascriptInterface
    public void channelDown() {
        hal.channelDown();
    }

    @JavascriptInterface
    public void resume() {
        hal.resume();
    }

    @JavascriptInterface
    public void pause() {
        hal.pause();
    }
}
