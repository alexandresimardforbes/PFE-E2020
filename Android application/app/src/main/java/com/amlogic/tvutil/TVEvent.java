// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.content.Context;
import android.database.Cursor;

import com.amlogic.tvdataprovider.TVDataProvider;

public class TVEvent
{
    private Context context;
    private int id;
    private int dvbEventID;
    private String name;
    private String description;
    private TVProgram program;
    private long start;
    private long end;
    private int dvbContent;
    private int dvbViewAge;
    private int sub_flag;
    private String descr;
    private String ext_descr;
    private TVDimension.VChipRating[] vchipRatings;
    
    TVEvent(final Context context, final Cursor c) {
        this.descr = null;
        this.ext_descr = null;
        this.vchipRatings = null;
        this.context = context;
        int col = c.getColumnIndex("db_id");
        this.id = c.getInt(col);
        col = c.getColumnIndex("event_id");
        this.dvbEventID = c.getInt(col);
        col = c.getColumnIndex("name");
        this.name = c.getString(col);
        col = c.getColumnIndex("start");
        this.start = c.getInt(col) * 1000L;
        col = c.getColumnIndex("end");
        this.end = c.getInt(col) * 1000L;
        col = c.getColumnIndex("nibble_level");
        this.dvbContent = c.getInt(col);
        col = c.getColumnIndex("parental_rating");
        this.dvbViewAge = c.getInt(col);
        col = c.getColumnIndex("sub_flag");
        this.sub_flag = c.getInt(col);
        col = c.getColumnIndex("db_srv_id");
        final int programID = c.getInt(col);
        this.program = TVProgram.selectByID(context, programID);
        col = c.getColumnIndex("rrt_ratings");
        final String rrtRatings = c.getString(col);
        final String[] ratings = rrtRatings.split(",");
        if (ratings != null && ratings.length > 0) {
            this.vchipRatings = new TVDimension.VChipRating[ratings.length];
            final TVDimension dm = new TVDimension();
            for (int i = 0; i < ratings.length; ++i) {
                final String[] rating = ratings[i].split(" ");
                if (rating.length >= 3) {
                    this.vchipRatings[i] = dm.new VChipRating(Integer.parseInt(rating[0]), Integer.parseInt(rating[1]), Integer.parseInt(rating[2]));
                }
                else {
                    this.vchipRatings[i] = null;
                }
            }
        }
    }
    
    public static TVEvent selectByID(final Context context, final int id) {
        TVEvent e = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from evt_table where evt_table.db_id = " + id, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                e = new TVEvent(context, c);
            }
            c.close();
        }
        return e;
    }
    
    public static void tvEventDelBySrvID(final Context context, final int db_srv_id) {
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "delete from evt_table where evt_table.db_srv_id = " + db_srv_id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }
    
    public int getID() {
        return this.id;
    }
    
    public int getDVBEventID() {
        return this.dvbEventID;
    }
    
    public String getName() {
        return TVMultilingualText.getText(this.context, this.name);
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public TVProgram getProgram() {
        return this.program;
    }
    
    public long getStartTime() {
        return this.start;
    }
    
    public long getEndTime() {
        return this.end;
    }
    
    public int getDVBContent() {
        return this.dvbContent;
    }
    
    public int getDVBViewAge() {
        return this.dvbViewAge;
    }
    
    public int getSubFlag() {
        return this.sub_flag;
    }
    
    public void setSubFlag(final int f) {
        this.sub_flag = f;
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update evt_table set sub_flag = " + f + " where evt_table.db_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }
    
    public String getEventDescr() {
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from evt_table where evt_table.db_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                final int col = c.getColumnIndex("descr");
                this.descr = c.getString(col);
            }
            c.close();
        }
        return TVMultilingualText.getText(this.context, this.descr);
    }
    
    public String getEventExtDescr() {
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from evt_table where evt_table.db_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                final int col = c.getColumnIndex("ext_descr");
                this.ext_descr = c.getString(col);
            }
            c.close();
        }
        return TVMultilingualText.getText(this.context, this.ext_descr);
    }
    
    public TVDimension.VChipRating[] getVChipRatings() {
        return this.vchipRatings;
    }
}
