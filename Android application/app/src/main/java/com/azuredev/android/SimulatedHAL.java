package com.azuredev.android;

public class SimulatedHAL implements HAL {

    @Override
    public String getRemoteValue() {
        return "FakeValue";
    }

    @Override
    public String getUID() {
        return "FakeUUID";
    }

    @Override
    public void savePreferences(String key, String value) {

    }

    @Override
    public String loadPreferences(String key) {
        return null;
    }

    @Override
    public void startDigitalChannelScan() {

    }

    @Override
    public void setNextInputSource() {

    }

    @Override
    public void setInputSource(String source) {

    }

    @Override
    public String getChannel() {
        return null;
    }

    @Override
    public void channelUp() {

    }

    @Override
    public void channelDown() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }
}
