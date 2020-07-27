package com.azuredev.android;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

public class SimulatedHAL implements HAL {

    @Override
    public String getRemoteValue() {
        return "bidon";
    }

    @Override
    public String getUID(String uniqueID) {
            return uniqueID;
    }
}
