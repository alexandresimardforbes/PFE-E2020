// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.content.Context;
import android.database.Cursor;

import com.amlogic.tvdataprovider.TVDataProvider;

public class TVGroup
{
    private static final String TAG = "TVGroup";
    private Context context;
    private int id;
    private String name;
    
    public void setID(final int id) {
        this.id = id;
    }
    
    public int getID() {
        return this.id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public TVGroup() {
    }
    
    private TVGroup(final Context context, final Cursor c) {
        this.context = context;
        int col = c.getColumnIndex("db_id");
        this.id = c.getInt(col);
        col = c.getColumnIndex("name");
        this.name = c.getString(col);
    }
    
    public static TVGroup[] selectByGroup(final Context context, final boolean no_skip) {
        TVGroup[] p = null;
        final boolean where = false;
        final String cmd = "select * from grp_table ";
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                p = new TVGroup[c.getCount()];
                do {
                    p[id++] = new TVGroup(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return p;
    }
    
    public static void addGroup(final Context context, final String group_name) {
        final Cursor c = context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "insert into grp_table (name) values (" + group_name + ")", (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }
    
    public static void deleteGroup(final Context context, final int id) {
        final Cursor c = context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "delete from grp_table where db_id = " + id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }
    
    public static void editGroup(final Context context, final int id, final String name) {
        final Cursor c = context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update grp_table set name= " + name + " where db_id = " + id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }
}
