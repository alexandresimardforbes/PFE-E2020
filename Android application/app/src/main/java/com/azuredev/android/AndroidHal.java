package com.azuredev.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.UUID;

public class AndroidHal implements HAL {
    private final TVClientImpl client;
    private final Context context;

    AndroidHal(TVClientImpl client, final Context context) {
        this.client = client;
        this.context= context;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            generateUniqueID();
        } else {
            generateMACAddress();
        }
    }

    @Override
    public String getRemoteValue() {
        return null;
    }

    @Override
    public String getUID() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String uniqueID = sharedPreferences.getString("uuid_key", "");
        return uniqueID;
    }

    @Override
    public void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @Override
    public String loadPreferences(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");
    }

    @Override
    public void startDigitalChannelScan() {
        client.startScanATSC();
    }

    @Override
    public void setNextInputSource() {
        client.setNextInputSource();
    }

    @Override
    public void setInputSource(String source) {
        client.setInputSource(source);
    }

    @Override
    public String getChannel() {
        return String.valueOf(client.getCurrentProgramNumber().getMajor()); // To add minor version of channel + "." + client.getCurrentProgramNumber().getMinor();
    }

    @Override
    public void channelUp() {
        client.channelUp();
    }

    @Override
    public void channelDown() {
        client.channelDown();
    }

    @Override
    public void resume() {
        client.resume();
    }

    @Override
    public void pause() {
        client.pause();
    }

    private void generateUniqueID() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPreferences.getString("uuid_key", "");
        if (TextUtils.isEmpty(value)) {
            String uuid = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uuid_key", uuid);
            editor.commit();
        }
    }

    private void generateMACAddress() {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String macAddress = info.getMacAddress();
        if (macAddress == null) {
            macAddress = "Device don't have mac address or wi-fi is disabled";
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPreferences.getString("uuid_key", "");
        if (TextUtils.isEmpty(value)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("uuid_key", macAddress);
            editor.commit();
        }
    }
}
