package com.azuredev.android;

public class SimulatedHAL implements HAL {

    @Override
    public String getRemoteValue() {
        return "bidon";
    }

    @Override
    public String getUID() {
        return "Fake MAC";
    }
}
