package com.azuredev.android;

public class fakeHAL extends HAL {
    @Override
    public String getRemoteValue() {
        return "bidon";
    }

    @Override
    public String getUID() {
        return "Fake MAC";
    }
}
