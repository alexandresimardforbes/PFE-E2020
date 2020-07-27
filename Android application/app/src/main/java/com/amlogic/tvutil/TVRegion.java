// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.content.Context;
import android.database.Cursor;

import com.amlogic.tvdataprovider.TVDataProvider;

import java.util.ArrayList;
import java.util.HashSet;

public class TVRegion
{
    private Context context;
    private int id;
    private int sourceMode;
    private String name;
    private String country;
    private String source;
    private TVChannelParams[] channels;
    
    TVRegion(final Context context, final Cursor c) {
        this.context = context;
        int col = c.getColumnIndex("db_id");
        this.id = c.getInt(col);
        col = c.getColumnIndex("fe_type");
        this.sourceMode = c.getInt(col);
        col = c.getColumnIndex("name");
        this.name = c.getString(col);
        this.country = this.name.substring(0, this.name.indexOf(44));
        this.source = this.name.substring(this.name.indexOf(44) + 1);
        final Cursor cur = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from region_table where name = '" + sqliteEscape(this.name) + "' and fe_type = " + this.sourceMode, (String[])null, (String)null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                int frequency = 0;
                int channelCount = 0;
                final int inversion = 0;
                this.channels = new TVChannelParams[cur.getCount()];
                do {
                    col = cur.getColumnIndex("frequency");
                    frequency = cur.getInt(col);
                    col = cur.getColumnIndex("modulation");
                    final int modulation = cur.getInt(col);
                    col = cur.getColumnIndex("bandwidth");
                    final int bandwidth = cur.getInt(col);
                    col = cur.getColumnIndex("symbol_rate");
                    final int symbolRate = cur.getInt(col);
                    col = cur.getColumnIndex("ofdm_mode");
                    final int ofdmMode = cur.getInt(col);
                    switch (this.sourceMode) {
                        default: {
                            continue;
                        }
                        case 1: {
                            this.channels[channelCount++] = TVChannelParams.dvbcParams(frequency, modulation, symbolRate);
                            continue;
                        }
                        case 2: {
                            this.channels[channelCount++] = TVChannelParams.dvbt2Params(frequency, bandwidth);
                            continue;
                        }
                        case 3: {
                            this.channels[channelCount++] = TVChannelParams.atscParams(frequency, modulation, inversion);
                            continue;
                        }
                        case 4: {
                            this.channels[channelCount++] = TVChannelParams.analogParams(frequency, 0, 0, 0);
                            continue;
                        }
                        case 5: {
                            this.channels[channelCount++] = TVChannelParams.dtmbParams(frequency, bandwidth);
                            continue;
                        }
                        case 6: {
                            this.channels[channelCount++] = TVChannelParams.isdbtParams(frequency, bandwidth);
                            continue;
                        }
                    }
                } while (cur.moveToNext());
            }
            cur.close();
        }
    }
    
    public static TVRegion selectByID(final Context context, final int id) {
        TVRegion e = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from region_table where region_table.db_id = " + id, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                e = new TVRegion(context, c);
            }
            c.close();
        }
        return e;
    }
    
    public static String sqliteEscape(String keyWord) {
        keyWord = keyWord.replace("'", "''");
        return keyWord;
    }
    
    public static TVRegion selectByName(final Context context, final String name) {
        TVRegion e = null;
        if (name == null) {
            return null;
        }
        final String cmd = "select * from region_table where name = '" + sqliteEscape(name) + "'";
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                e = new TVRegion(context, c);
            }
            c.close();
        }
        return e;
    }
    
    public static TVRegion[] selectByCountry(final Context context, final String countryName) {
        TVRegion[] ret = null;
        if (countryName == null) {
            return null;
        }
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from region_table", (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                final HashSet set = new HashSet();
                final ArrayList list = new ArrayList();
                do {
                    final int col = c.getColumnIndex("name");
                    final String name = c.getString(col);
                    if (name.contains(countryName) && set.add(name)) {
                        list.add(new TVRegion(context, c));
                    }
                } while (c.moveToNext());
                ret = (TVRegion[]) list.toArray(new TVRegion[0]);
            }
            c.close();
        }
        return ret;
    }
    
    public static String[] getAllCountry(final Context context) {
        String[] ret = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from region_table", (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                final HashSet set = new HashSet();
                do {
                    final int col = c.getColumnIndex("name");
                    final String name = c.getString(col);
                    final String countryName = name.substring(0, name.indexOf(44));
                    set.add(countryName);
                } while (c.moveToNext());
                ret = (String[])set.toArray(new String[0]);
            }
            c.close();
        }
        return ret;
    }
    
    public static String[] getCountryByATSC(final Context context) {
        String[] ret = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from region_table", (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                final HashSet set = new HashSet();
                do {
                    final int col = c.getColumnIndex("name");
                    final String name = c.getString(col);
                    if (name.contains("ATSC")) {
                        set.add(name);
                    }
                } while (c.moveToNext());
                ret = (String[])set.toArray(new String[0]);
            }
            c.close();
        }
        return ret;
    }
    
    public static String[] getCountryByDVBC(final Context context) {
        String[] ret = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from region_table", (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                final HashSet set = new HashSet();
                do {
                    final int col = c.getColumnIndex("name");
                    final String name = c.getString(col);
                    if (name.contains("DVB-C")) {
                        set.add(name);
                    }
                } while (c.moveToNext());
                ret = (String[])set.toArray(new String[0]);
            }
            c.close();
        }
        return ret;
    }
    
    public static String[] getCountryByDVBT(final Context context) {
        String[] ret = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from region_table", (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                final HashSet set = new HashSet();
                do {
                    final int col = c.getColumnIndex("name");
                    final String name = c.getString(col);
                    if (name.contains("DVB-T")) {
                        final String countryName = name.substring(0, name.indexOf(44));
                        set.add(countryName);
                    }
                } while (c.moveToNext());
                ret = (String[])set.toArray(new String[0]);
            }
            c.close();
        }
        return ret;
    }
    
    public static String[] getCountryByISDBT(final Context context) {
        String[] ret = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from region_table", (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                final HashSet set = new HashSet();
                do {
                    final int col = c.getColumnIndex("name");
                    final String name = c.getString(col);
                    if (name.contains("ISDBT")) {
                        final String countryName = name.substring(0, name.indexOf(44));
                        set.add(countryName);
                    }
                } while (c.moveToNext());
                ret = (String[])set.toArray(new String[0]);
            }
            c.close();
        }
        return ret;
    }
    
    public int getID() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getCountry() {
        return this.country;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public int getSourceMode() {
        return this.sourceMode;
    }
    
    public TVChannelParams[] getChannelParams() {
        return this.channels;
    }
    
    public TVChannelParams getChannelParams(final int channelNo) {
        TVChannelParams ch = null;
        if (this.channels != null && channelNo >= 1 && channelNo <= this.channels.length) {
            ch = this.channels[channelNo - 1];
        }
        return ch;
    }
    
    public int getChannelNo(final int frequency) {
        int ret = -1;
        if (this.channels != null) {
            for (int i = 0; i < this.channels.length; ++i) {
                if (this.channels[i].getFrequency() == frequency) {
                    ret = i + 1;
                    break;
                }
            }
        }
        return ret;
    }
}
