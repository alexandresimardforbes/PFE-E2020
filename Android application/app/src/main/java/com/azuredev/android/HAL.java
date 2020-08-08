package com.azuredev.android;


interface HAL {

    String getRemoteValue();

    String getUID();

    void savePreferences(String key, String value);

    String loadPreferences(String key);

    void startDigitalChannelScan();

    void setNextInputSource();

    void setInputSource(String source);

    String getChannel();

    void channelUp();

    void channelDown();

    void resume();

    void pause();
}
