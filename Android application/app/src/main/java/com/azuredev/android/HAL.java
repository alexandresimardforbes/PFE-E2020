package com.azuredev.android;

import android.content.Context;

interface HAL {

    String getRemoteValue();

    String getUID(String uniqueID);
}
