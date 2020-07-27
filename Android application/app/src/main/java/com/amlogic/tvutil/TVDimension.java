// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amlogic.tvdataprovider.TVDataProvider;

public class TVDimension
{
    private static final String TAG = "TVDimension";
    public static final int REGION_US = 1;
    public static final int REGION_CANADA = 2;
    public static final int REGION_TAIWAN = 3;
    public static final int REGION_SOUTHKOREA = 4;
    private Context context;
    private int id;
    private int indexj;
    private int ratingRegion;
    private int graduatedScale;
    private int[] lockValues;
    private String name;
    private String ratingRegionName;
    private String[] abbrevValues;
    private String[] textValues;
    private boolean isPGAll;
    
    TVDimension(final Context context, final Cursor c) {
        this.context = context;
        int col = c.getColumnIndex("db_id");
        this.id = c.getInt(col);
        col = c.getColumnIndex("index_j");
        this.indexj = c.getInt(col);
        col = c.getColumnIndex("rating_region");
        this.ratingRegion = c.getInt(col);
        col = c.getColumnIndex("graduated_scale");
        this.graduatedScale = c.getInt(col);
        col = c.getColumnIndex("name");
        this.name = c.getString(col);
        col = c.getColumnIndex("rating_region_name");
        this.ratingRegionName = c.getString(col);
        col = c.getColumnIndex("values_defined");
        final int valuesDefined = c.getInt(col);
        this.lockValues = new int[valuesDefined];
        this.abbrevValues = new String[valuesDefined];
        this.textValues = new String[valuesDefined];
        for (int i = 0; i < valuesDefined; ++i) {
            col = c.getColumnIndex("abbrev" + i);
            this.abbrevValues[i] = c.getString(col);
            col = c.getColumnIndex("text" + i);
            this.textValues[i] = c.getString(col);
            col = c.getColumnIndex("locked" + i);
            this.lockValues[i] = c.getInt(col);
        }
        if (this.ratingRegion == 1 && this.name.equals("All")) {
            this.isPGAll = true;
        }
        else {
            this.isPGAll = false;
        }
    }
    
    public TVDimension() {
    }
    
    private int getUSPGAllLockStatus(final String abbrev) {
        final TVDimension dm0 = selectByIndex(this.context, 1, 0);
        final TVDimension dm2 = selectByIndex(this.context, 1, 5);
        final String[] dm0Abbrev = dm0.getAbbrev();
        final String[] dm5Abbrev = dm2.getAbbrev();
        for (int j = 0; j < dm0Abbrev.length; ++j) {
            Log.d("TVDimension", dm0Abbrev[j]);
            if (abbrev.equals(dm0Abbrev[j])) {
                return dm0.getLockStatus(j + 1);
            }
        }
        for (int j = 0; j < dm5Abbrev.length; ++j) {
            Log.d("TVDimension", dm0Abbrev[j]);
            if (abbrev.equals(dm5Abbrev[j])) {
                return dm2.getLockStatus(j + 1);
            }
        }
        return -1;
    }
    
    private void setUSPGAllLockStatus(final String abbrev, final int lock) {
        final TVDimension dm0 = selectByIndex(this.context, 1, 0);
        final TVDimension dm2 = selectByIndex(this.context, 1, 5);
        final String[] dm0Abbrev = dm0.getAbbrev();
        final String[] dm5Abbrev = dm2.getAbbrev();
        for (int j = 0; j < dm0Abbrev.length; ++j) {
            if (abbrev.equals(dm0Abbrev[j])) {
                dm0.setLockStatus(j + 1, lock);
                return;
            }
        }
        for (int j = 0; j < dm5Abbrev.length; ++j) {
            if (abbrev.equals(dm5Abbrev[j])) {
                dm2.setLockStatus(j + 1, lock);
                return;
            }
        }
    }
    
    private int[] getUSPGAllLockStatus(final String[] abbrevs) {
        final int[] lockAll = new int[abbrevs.length];
        for (int i = 0; i < abbrevs.length; ++i) {
            lockAll[i] = this.getUSPGAllLockStatus(abbrevs[i]);
        }
        return lockAll;
    }
    
    private void setUSPGAllLockStatus(final String[] abbrevs, final int[] lock) {
        for (int i = 0; i < abbrevs.length; ++i) {
            this.setUSPGAllLockStatus(abbrevs[i], lock[i]);
        }
    }
    
    public static TVDimension selectByID(final Context context, final int id) {
        TVDimension e = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from dimension_table where evt_table.db_id = " + id, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                e = new TVDimension(context, c);
            }
            c.close();
        }
        return e;
    }
    
    public static TVDimension[] selectByRatingRegion(final Context context, final int ratingRegionID) {
        TVDimension[] d = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from dimension_table where rating_region = " + ratingRegionID, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                d = new TVDimension[c.getCount()];
                do {
                    d[id++] = new TVDimension(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return d;
    }
    
    public static TVDimension selectByIndex(final Context context, final int ratingRegionID, final int index) {
        TVDimension d = null;
        String cmd = "select * from dimension_table where rating_region = " + ratingRegionID;
        cmd = cmd + " and index_j=" + index;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                d = new TVDimension(context, c);
            }
            c.close();
        }
        return d;
    }
    
    public static TVDimension selectByName(final Context context, final int ratingRegionID, final String dimensionName) {
        TVDimension d = null;
        String cmd = "select * from dimension_table where rating_region = " + ratingRegionID;
        cmd = cmd + " and name='" + dimensionName + "'";
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                d = new TVDimension(context, c);
            }
            c.close();
        }
        return d;
    }
    
    public static TVDimension[] selectUSDownloadable(final Context context) {
        TVDimension[] d = null;
        final String cmd = "select * from dimension_table where rating_region >= 5";
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                d = new TVDimension[c.getCount()];
                do {
                    d[id++] = new TVDimension(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return d;
    }
    
    public static boolean isBlocked(final Context context, final VChipRating definedRating) {
        if (definedRating != null) {
            final TVDimension dm = selectByIndex(context, definedRating.getRegion(), definedRating.getDimension());
            if (dm != null) {
                return dm.getLockStatus(definedRating.getValue()) == 1;
            }
        }
        return false;
    }
    
    public int getID() {
        return this.id;
    }
    
    public int getRatingRegion() {
        return this.ratingRegion;
    }
    
    public String getRatingRegionName() {
        return this.ratingRegionName;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getGraduatedScale() {
        return this.graduatedScale;
    }
    
    public int[] getLockStatus() {
        if (this.lockValues.length <= 1) {
            return null;
        }
        if (this.isPGAll) {
            return this.getUSPGAllLockStatus(this.abbrevValues);
        }
        final int[] l = new int[this.lockValues.length - 1];
        System.arraycopy(this.lockValues, 1, l, 0, l.length);
        return l;
    }
    
    public int getLockStatus(final int valueIndex) {
        if (valueIndex >= this.lockValues.length) {
            return -1;
        }
        if (this.isPGAll) {
            return this.getUSPGAllLockStatus(this.abbrevValues[valueIndex]);
        }
        return this.lockValues[valueIndex];
    }
    
    public int[] getLockStatus(final String[] abbrevs) {
        int[] l = null;
        if (abbrevs != null) {
            if (this.isPGAll) {
                return this.getUSPGAllLockStatus(abbrevs);
            }
            l = new int[abbrevs.length];
            for (int i = 0; i < abbrevs.length; ++i) {
                l[i] = -1;
                for (int j = 0; j < this.abbrevValues.length; ++j) {
                    if (abbrevs[i].equals(this.abbrevValues[j])) {
                        l[i] = this.lockValues[j];
                        break;
                    }
                }
            }
        }
        return l;
    }
    
    public String[] getAbbrev() {
        if (this.abbrevValues.length > 1) {
            final String[] a = new String[this.abbrevValues.length - 1];
            System.arraycopy(this.abbrevValues, 1, a, 0, a.length);
            return a;
        }
        return null;
    }
    
    public String getAbbrev(final int valueIndex) {
        if (valueIndex >= this.abbrevValues.length) {
            return null;
        }
        return this.abbrevValues[valueIndex];
    }
    
    public String[] getText() {
        if (this.textValues.length > 1) {
            final String[] t = new String[this.textValues.length - 1];
            System.arraycopy(this.textValues, 1, t, 0, t.length);
            return t;
        }
        return null;
    }
    
    public String getText(final int valueIndex) {
        if (valueIndex >= this.textValues.length) {
            return null;
        }
        return this.textValues[valueIndex];
    }
    
    public void setLockStatus(final int valueIndex, final int status) {
        if (valueIndex >= this.lockValues.length) {
            return;
        }
        if (this.isPGAll) {
            this.setUSPGAllLockStatus(this.abbrevValues[valueIndex], this.lockValues[valueIndex]);
        }
        else if (this.lockValues[valueIndex] != -1 && this.lockValues[valueIndex] != status) {
            this.lockValues[valueIndex] = status;
            String cmd = "update dimension_table set locked" + valueIndex;
            cmd = cmd + "=" + status + " where db_id = " + this.id;
            this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
        }
    }
    
    public void setLockStatus(final int[] status) {
        if (status == null || status.length != this.lockValues.length - 1) {
            Log.d("TVDimension", "Cannot set lock status, invalid param");
            return;
        }
        if (this.isPGAll) {
            this.setUSPGAllLockStatus(this.abbrevValues, status);
        }
        else {
            for (int i = 0; i < status.length; ++i) {
                this.setLockStatus(i + 1, status[i]);
            }
        }
    }
    
    public void setLockStatus(final String[] abbrevs, final int[] locks) {
        if (abbrevs == null || locks == null) {
            return;
        }
        if (abbrevs.length != locks.length) {
            Log.d("TVDimension", "Invalid abbrevs or locks, length must be equal");
            return;
        }
        if (this.isPGAll) {
            this.setUSPGAllLockStatus(abbrevs, locks);
        }
        else {
            for (int i = 0; i < abbrevs.length; ++i) {
                for (int j = 0; j < this.abbrevValues.length; ++j) {
                    if (abbrevs[i].equals(this.abbrevValues[j])) {
                        this.setLockStatus(j, locks[i]);
                        break;
                    }
                }
            }
        }
    }
    
    public class VChipRating
    {
        private int region;
        private int dimension;
        private int value;
        
        public VChipRating(final int region, final int dimension, final int value) {
            this.region = region;
            this.dimension = dimension;
            this.value = value;
        }
        
        public int getRegion() {
            return this.region;
        }
        
        public int getDimension() {
            return this.dimension;
        }
        
        public int getValue() {
            return this.value;
        }
    }
}
