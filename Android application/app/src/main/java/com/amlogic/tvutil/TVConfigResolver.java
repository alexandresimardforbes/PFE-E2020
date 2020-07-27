// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.content.Context;
import android.database.Cursor;

import com.amlogic.tvdataprovider.TVDataProvider;

public class TVConfigResolver
{
    private static final String TAG = "TVConfigResolver";
    
    public static int getConfig(final Context context, final String name, final int defaultValue) {
        int ret = defaultValue;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_CONFIG_URL, (String[])null, name, new String[] { "Int" }, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                final int col = c.getColumnIndex("value");
                ret = c.getInt(col);
            }
            c.close();
        }
        return ret;
    }
    
    public static String getConfig(final Context context, final String name, final String defaultValue) {
        String ret = defaultValue;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_CONFIG_URL, (String[])null, name, new String[] { "String" }, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                final int col = c.getColumnIndex("value");
                ret = c.getString(col);
            }
            c.close();
        }
        return ret;
    }
    
    public static boolean getConfig(final Context context, final String name, final boolean defaultValue) {
        boolean ret = defaultValue;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_CONFIG_URL, (String[])null, name, new String[] { "Boolean" }, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                final int col = c.getColumnIndex("value");
                ret = (c.getInt(col) != 0);
            }
            c.close();
        }
        return ret;
    }
}
