// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvdataprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.amlogic.tvservice.TVConfig;
import com.amlogic.tvutil.TVConfigValue;

public class TVDataProvider extends ContentProvider
{
    private static final String TAG = "TVDataProvider";
    private static final String DB_NAME = "dvb.db";
    private static final String AUTHORITY = "com.amlogic.tv.tvdataprovider";
    private static final int RD_SQL = 1;
    private static final int WR_SQL = 2;
    private static final int RD_CONFIG = 3;
    private static final int WR_CONFIG = 4;
    private static final UriMatcher URI_MATCHER;
    public static final Uri RD_URL;
    public static final Uri WR_URL;
    public static final Uri RD_CONFIG_URL;
    public static final Uri WR_CONFIG_URL;
    private static int openCount;
    private static TVDatabase db;
    private static boolean modified;
    private static TVConfig config;
    
    public static synchronized void openDatabase(final Context context, final TVConfig cfg) {
        String modeStr = null;
        try {
            modeStr = cfg.getString("tv:dtv:mode");
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (TVDataProvider.openCount == 0) {
            TVDataProvider.db = new TVDatabase(context, "dvb.db", modeStr);
            TVDataProvider.modified = true;
        }
        if (cfg != null) {
            TVDataProvider.config = cfg;
        }
        ++TVDataProvider.openCount;
    }
    
    public static synchronized void closeDatabase(final Context context) {
        if (TVDataProvider.openCount <= 0) {
            return;
        }
        --TVDataProvider.openCount;
        if (TVDataProvider.openCount == 0) {
            Log.d("TVDataProvider", "close database");
            TVDataProvider.db.unsetup(context);
            TVDataProvider.db.close();
        }
    }
    
    public static synchronized void restore(final Context context) {
        TVDataProvider.db.getWritableDatabase().execSQL("delete from net_table");
        TVDataProvider.db.getWritableDatabase().execSQL("delete from ts_table");
        TVDataProvider.db.getWritableDatabase().execSQL("delete from srv_table");
        TVDataProvider.db.getWritableDatabase().execSQL("delete from evt_table");
        TVDataProvider.db.getWritableDatabase().execSQL("delete from booking_table");
        TVDataProvider.db.getWritableDatabase().execSQL("delete from grp_table");
        TVDataProvider.db.getWritableDatabase().execSQL("delete from grp_map_table");
        TVDataProvider.db.getWritableDatabase().execSQL("delete from dimension_table");
        TVDataProvider.db.getWritableDatabase().execSQL("delete from sat_para_table");
        TVDataProvider.db.getWritableDatabase().execSQL("delete from region_table");
        TVDataProvider.modified = true;
        TVDataProvider.db.loadBuiltins(context);
    }
    
    public static synchronized void importDatabase(final Context context, final String inputXmlPath) throws Exception {
        TVDataProvider.db.importFromXml(context, inputXmlPath);
        TVDataProvider.modified = true;
    }
    
    public static synchronized void exportDatabase(final Context context, final String outputXmlPath) throws Exception {
        TVDataProvider.db.exportToXml(context, outputXmlPath);
    }
    
    public boolean onCreate() {
        openDatabase(this.getContext(), null);
        return true;
    }
    
    public String getType(final Uri uri) {
        return null;
    }
    
    public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
        final int id = TVDataProvider.URI_MATCHER.match(uri);
        Cursor c = null;
        if (id == 1) {
            c = TVDataProvider.db.getReadableDatabase().rawQuery(selection, (String[])null);
        }
        else if (id == 2) {
            TVDataProvider.db.getWritableDatabase().execSQL(selection);
            TVDataProvider.modified = true;
        }
        else if (id == 3) {
            if (selection == null || selectionArgs == null || selectionArgs.length < 1 || TVDataProvider.config == null) {
                Log.d("TVDataProvider", "Cannot read config, invalid args.");
                return c;
            }
            final String strType = selectionArgs[0];
            if (!strType.equalsIgnoreCase("Int") && !strType.equalsIgnoreCase("String") && !strType.equalsIgnoreCase("Boolean")) {
                Log.d("TVDataProvider", "Invalid value type, must in (Int, String, Boolean)");
                return c;
            }
            final String[] strCur = { "value" };
            final MatrixCursor mc = new MatrixCursor(strCur);
            try {
                if (strType.equalsIgnoreCase("Int")) {
                    mc.addRow(new Object[] { TVDataProvider.config.getInt(selection) });
                }
                else if (strType.equalsIgnoreCase("String")) {
                    mc.addRow(new Object[] { TVDataProvider.config.getString(selection) });
                }
                else if (strType.equalsIgnoreCase("Boolean")) {
                    mc.addRow(new Object[] { TVDataProvider.config.getBoolean(selection) ? 1 : 0 });
                }
            }
            catch (Exception ex) {}
            c = (Cursor)mc;
        }
        else if (id == 4 && selection != null) {
            if (selection == null || selectionArgs == null || selectionArgs.length < 2 || TVDataProvider.config == null) {
                Log.d("TVDataProvider", "Cannot write config, invalid args.");
                return c;
            }
            final String strType = selectionArgs[0];
            if (!strType.equalsIgnoreCase("Int") && !strType.equalsIgnoreCase("String") && !strType.equalsIgnoreCase("Boolean")) {
                Log.d("TVDataProvider", "Invalid value type, must in (Int, String, Boolean)");
                return c;
            }
            final String strValue = selectionArgs[1];
            try {
                if (strType.equalsIgnoreCase("Int")) {
                    TVDataProvider.config.set(selection, new TVConfigValue(Integer.parseInt(strValue)));
                }
                else if (strType.equalsIgnoreCase("String")) {
                    TVDataProvider.config.set(selection, new TVConfigValue(strValue));
                }
                else if (strType.equalsIgnoreCase("Boolean")) {
                    TVDataProvider.config.set(selection, new TVConfigValue(Integer.parseInt(strValue) != 0));
                }
            }
            catch (Exception ex2) {}
        }
        return c;
    }
    
    public Uri insert(final Uri uri, final ContentValues initialValues) {
        return null;
    }
    
    public int delete(final Uri uri, final String where, final String[] whereArgs) {
        return 0;
    }
    
    public int update(final Uri uri, final ContentValues values, final String where, final String[] whereArgs) {
        return 0;
    }
    
    public static synchronized void sync() {
        if (TVDataProvider.modified) {
            TVDataProvider.modified = false;
            final TVDatabase db = TVDataProvider.db;
            TVDatabase.sync();
        }
    }
    
    static {
        RD_URL = Uri.parse("content://com.amlogic.tv.tvdataprovider/rd_db");
        WR_URL = Uri.parse("content://com.amlogic.tv.tvdataprovider/wr_db");
        RD_CONFIG_URL = Uri.parse("content://com.amlogic.tv.tvdataprovider/rd_config");
        WR_CONFIG_URL = Uri.parse("content://com.amlogic.tv.tvdataprovider/wr_config");
        TVDataProvider.openCount = 0;
        TVDataProvider.modified = false;
        (URI_MATCHER = new UriMatcher(-1)).addURI("com.amlogic.tv.tvdataprovider", "rd_db", 1);
        TVDataProvider.URI_MATCHER.addURI("com.amlogic.tv.tvdataprovider", "wr_db", 2);
        TVDataProvider.URI_MATCHER.addURI("com.amlogic.tv.tvdataprovider", "rd_config", 3);
        TVDataProvider.URI_MATCHER.addURI("com.amlogic.tv.tvdataprovider", "wr_config", 4);
    }
}
