package com.azuredev.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.webkit.JavascriptInterface;

public class JSInterface {
    Context mContext;
    HAL hal;
    TVClientImpl client;

    JSInterface(Context c, TVClientImpl client, HAL hal) {
        mContext = c;
        this.hal = hal;
        this.client = client;
    }

    @JavascriptInterface
    public void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @JavascriptInterface
    public String LoadPreferences(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString(key, "");
    }

    @JavascriptInterface
    public String getRemoteValue() {
        return hal.getRemoteValue();
    }

    @JavascriptInterface
    public String getUID() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String uniqueID = sharedPreferences.getString("uuid_key", "");
        return hal.getUID(uniqueID);
    }

    @JavascriptInterface
    public void startDigitalChannelScan(){
        MainActivity mainApp = (MainActivity)mContext;
        mainApp.startDigitalChannelScan();

    }

    @JavascriptInterface
    public void setDigitalChannelUp(){
        client.channelUp();
    }

    @JavascriptInterface
    public void setDigitalChannelDown(){
        client.channelDown();
    }

    @JavascriptInterface
    public void setDigitalChannelById(int id){
        client.playProgram(id);
    }

    @JavascriptInterface
    public void setNextInputSource(){
        MainActivity mainApp = (MainActivity)mContext;
        mainApp.setNextInputSource();
    }

    @JavascriptInterface
    public void setInputSource(String source){
        MainActivity mainApp = (MainActivity)mContext;
        mainApp.setInputSource(source);
    }

}
