package com.azuredev.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.webkit.JavascriptInterface;

public class JSInterface {
    Context mContext;
    HAL hal;
    JSInterface(Context c, HAL hal){
        mContext = c;
        this.hal = hal;
    }

    @JavascriptInterface
    public void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @JavascriptInterface
    public String LoadPreferences(String key){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString(key, "");
    }

    @JavascriptInterface
    public String getRemoteValue() {
        return hal.getRemoteValue();
    }

    @JavascriptInterface
    public String getUID() { return hal.getUID();}

}
