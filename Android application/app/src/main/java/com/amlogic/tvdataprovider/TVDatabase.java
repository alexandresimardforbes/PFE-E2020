// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvdataprovider;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.amlogic.tvutil.TVDBTransformer;

import java.io.File;

public class TVDatabase extends SQLiteOpenHelper
{
    private static final String TAG = "TVDatabase";
    private static final String DEFAULT_DB_PATH = "tv_default.xml";
    private static final int DB_VERSION = 8;
    private static final String DB_VERSION_FIELD = "DATABASE_VERSION";
    private String strMode;
    
    private native void native_db_setup(final String p0, final boolean p1, final SQLiteDatabase p2);
    
    private native void native_db_unsetup();
    
    private static native void native_db_sync();
    
    private void insertNewDimension(final int region, final String regionName, final String name, final int indexj, final int[] lock, final String[] abbrev, final String[] text) {
        String cmd = "insert into dimension_table(rating_region,rating_region_name,name,graduated_scale,";
        cmd += "values_defined,index_j,version,abbrev0,text0,locked0,abbrev1,text1,locked1,abbrev2,text2,locked2,";
        cmd += "abbrev3,text3,locked3,abbrev4,text4,locked4,abbrev5,text5,locked5,abbrev6,text6,locked6,";
        cmd += "abbrev7,text7,locked7,abbrev8,text8,locked8,abbrev9,text9,locked9,abbrev10,text10,locked10,";
        cmd += "abbrev11,text11,locked11,abbrev12,text12,locked12,abbrev13,text13,locked13,abbrev14,text14,locked14,";
        cmd = cmd + "abbrev15,text15,locked15) values(" + region + ",'" + regionName + "','" + name + "',0," + lock.length + "," + indexj + ",0";
        for (int i = 0; i < 16; ++i) {
            if (i < lock.length) {
                cmd = cmd + ",'" + abbrev[i] + "'";
                cmd = cmd + ",'" + text[i] + "'";
                cmd = cmd + ",'" + lock[i] + "'";
            }
            else {
                cmd += ",''";
                cmd += ",''";
                cmd += ",-1";
            }
        }
        cmd += ")";
        this.getWritableDatabase().execSQL(cmd);
    }
    
    public void builtinAtscDimensions() {
        this.getWritableDatabase().execSQL("delete from dimension_table");
        final String[] abbrev0 = { "", "None", "TV-G", "TV-PG", "TV-14", "TV-MA" };
        final String[] text0 = { "", "None", "TV-G", "TV-PG", "TV-14", "TV-MA" };
        final int[] lock0 = { -1, -1, 0, 0, 0, 0 };
        final String[] abbrev2 = { "", "D", "TV-G", "TV-PG", "TV-14", "TV-MA" };
        final String[] text2 = { "", "D", "TV-G", "TV-PG", "TV-14", "TV-MA" };
        final int[] lock2 = { -1, -1, -1, 0, 0, -1 };
        final String[] abbrev3 = { "", "L", "TV-G", "TV-PG", "TV-14", "TV-MA" };
        final String[] text3 = { "", "L", "TV-G", "TV-PG", "TV-14", "TV-MA" };
        final int[] lock3 = { -1, -1, -1, 0, 0, 0 };
        final String[] abbrev4 = { "", "S", "TV-G", "TV-PG", "TV-14", "TV-MA" };
        final String[] text4 = { "", "S", "TV-G", "TV-PG", "TV-14", "TV-MA" };
        final int[] lock4 = { -1, -1, -1, 0, 0, 0 };
        final String[] abbrev5 = { "", "V", "TV-G", "TV-PG", "TV-14", "TV-MA" };
        final String[] text5 = { "", "V", "TV-G", "TV-PG", "TV-14", "TV-MA" };
        final int[] lock5 = { -1, -1, -1, 0, 0, 0 };
        final String[] abbrev6 = { "", "TV-Y", "TV-Y7" };
        final String[] text6 = { "", "TV-Y", "TV-Y7" };
        final int[] lock6 = { -1, 0, 0 };
        final String[] abbrev7 = { "", "FV", "TV-Y7" };
        final String[] text7 = { "", "FV", "TV-Y7" };
        final int[] lock7 = { -1, -1, 0 };
        final String[] abbrev8 = { "", "N/A", "G", "PG", "PG-13", "R", "NC-17", "X", "NR" };
        final String[] text8 = { "", "MPAA Rating Not Applicable", "Suitable for AllAges", "Parental GuidanceSuggested", "Parents Strongly Cautioned", "Restricted, under 17 must be accompanied by adult", "No One 17 and Under Admitted", "No One 17 and Under Admitted", "\u201cNot Rated by MPAA" };
        final int[] lock8 = { -1, -1, 0, 0, 0, 0, 0, 0, 0 };
        final String[] abbrevall = { "TV-Y", "TV-Y7", "TV-G", "TV-PG", "TV-14", "TV-MA" };
        final String[] textall = { "TV-Y", "TV-Y7", "TV-G", "TV-PG", "TV-14", "TV-MA" };
        final int[] lockall = { 0, 0, 0, 0, 0, 0 };
        this.insertNewDimension(1, "US (50 states + possessions)", "Entire Audience", 0, lock0, abbrev0, text0);
        this.insertNewDimension(1, "US (50 states + possessions)", "Dialogue", 1, lock2, abbrev2, text2);
        this.insertNewDimension(1, "US (50 states + possessions)", "Language", 2, lock3, abbrev3, text3);
        this.insertNewDimension(1, "US (50 states + possessions)", "Sex", 3, lock4, abbrev4, text4);
        this.insertNewDimension(1, "US (50 states + possessions)", "Violence", 4, lock5, abbrev5, text5);
        this.insertNewDimension(1, "US (50 states + possessions)", "Children", 5, lock6, abbrev6, text6);
        this.insertNewDimension(1, "US (50 states + possessions)", "Fantasy violence", 6, lock7, abbrev7, text7);
        this.insertNewDimension(1, "US (50 states + possessions)", "MPAA", 7, lock8, abbrev8, text8);
        this.insertNewDimension(1, "US (50 states + possessions)", "All", -1, lockall, abbrevall, textall);
        final String[] cabbrev0 = { "E", "C", "C8+", "G", "PG", "14+", "18+" };
        final String[] ctext0 = { "Exempt", "Children", "8+", "General", "PG", "14+", "18+" };
        final int[] clock0 = { 0, 0, 0, 0, 0, 0, 0 };
        final String[] cabbrev2 = { "E", "G", "8 ans+", "13 ans+", "16 ans+", "18 ans+" };
        final String[] ctext2 = { "Exempt\u00e9es", "Pour tous", "8+", "13+", "16+", "18+" };
        final int[] clock2 = { 0, 0, 0, 0, 0, 0 };
        this.insertNewDimension(2, "Canada", "Canadian English Language Rating", 0, clock0, cabbrev0, ctext0);
        this.insertNewDimension(2, "Canada", "Codes fran\u00e7ais du Canada", 1, clock2, cabbrev2, ctext2);
    }
    
    public void unsetup(final Context context) {
        this.native_db_unsetup();
    }
    
    public TVDatabase(final Context context, final String dbName, final String mode) {
        super(context, dbName, (SQLiteDatabase.CursorFactory)null, 8);
        this.strMode = mode;
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        final int curVer = pref.getInt("DATABASE_VERSION", -1);
        boolean create = false;
        final File file = context.getDatabasePath(dbName);
        Log.d("TVDatabase", "database version: DB_VERSION 8, curVer " + curVer);
        if (curVer != 8 || !file.exists()) {
            create = true;
            if (file.exists()) {
                Log.d("TVDatabase", "Database version changed, delete the current database.");
                file.delete();
            }
        }
        if (!file.exists()) {
            try {
                Log.d("TVDatabase", "Creating database file ...");
                file.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.native_db_setup(file.toString(), create, this.getWritableDatabase());
        if (create) {
            this.loadBuiltins(context);
            pref.edit().putInt("DATABASE_VERSION", 8).commit();
        }
    }
    
    public void importFromXml(final Context context, final String inputXmlPath) throws Exception {
        TVDBTransformer.transform(context, 1, this.getWritableDatabase(), inputXmlPath, this.strMode);
    }
    
    public void exportToXml(final Context context, final String outputXmlPath) throws Exception {
        TVDBTransformer.transform(context, 0, this.getReadableDatabase(), outputXmlPath, this.strMode);
    }
    
    public void loadBuiltins(final Context context) {
        try {
            Log.d("TVDatabase", "Loading default database from tv_default.xml...");
            this.importFromXml(context, "tv_default.xml");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("TVDatabase", "Generating builtin v-chip dimensions...");
        this.builtinAtscDimensions();
    }
    
    public static void sync() {
        native_db_sync();
    }
    
    public void onCreate(final SQLiteDatabase db) {
    }
    
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }
    
    static {
        System.loadLibrary("jnitvdatabase");
    }
}
