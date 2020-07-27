// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amlogic.tvdataprovider.TVDataProvider;

public class TVSatellite
{
    private static final String TAG = "TVSatellite";
    public Context context;
    public int sat_id;
    public String sat_name;
    public TVSatelliteParams tv_satparams;
    
    public TVSatellite() {
    }
    
    public TVSatellite(final Context context) {
        this.context = context;
    }
    
    private void constructFromCursor(final Context context, final Cursor cur) {
        int numColumn = 0;
        this.context = context;
        this.tv_satparams = new TVSatelliteParams();
        numColumn = cur.getColumnIndex("db_id");
        final int sat_id = cur.getInt(numColumn);
        this.sat_id = sat_id;
        numColumn = cur.getColumnIndex("sat_name");
        final String sat_name = cur.getString(numColumn);
        this.sat_name = sat_name;
        numColumn = cur.getColumnIndex("sat_longitude");
        final int sat_longitude = cur.getInt(numColumn);
        this.tv_satparams.setSatelliteLongitude(sat_longitude);
        numColumn = cur.getColumnIndex("lnb_num");
        final int lnb_no = cur.getInt(numColumn);
        numColumn = cur.getColumnIndex("lof_hi");
        final int lof_hi = cur.getInt(numColumn);
        numColumn = cur.getColumnIndex("lof_lo");
        final int lof_lo = cur.getInt(numColumn);
        numColumn = cur.getColumnIndex("lof_threshold");
        final int lof_threshold = cur.getInt(numColumn);
        this.tv_satparams.setSatelliteLnb(lnb_no, lof_hi, lof_lo, lof_threshold);
        numColumn = cur.getColumnIndex("voltage");
        final int voltage = cur.getInt(numColumn);
        this.tv_satparams.setSecVoltage(voltage);
        numColumn = cur.getColumnIndex("signal_22khz");
        final int signal_22khz = cur.getInt(numColumn);
        this.tv_satparams.setSec22k(signal_22khz);
        numColumn = cur.getColumnIndex("tone_burst");
        final int tone_burst = cur.getInt(numColumn);
        this.tv_satparams.setSecToneBurst(tone_burst);
        numColumn = cur.getColumnIndex("diseqc_mode");
        final int diseqc_mode = cur.getInt(numColumn);
        this.tv_satparams.setDiseqcMode(diseqc_mode);
        numColumn = cur.getColumnIndex("committed_cmd");
        final int lnb_diseqc_mode_config10 = cur.getInt(numColumn);
        this.tv_satparams.setDiseqcCommitted(lnb_diseqc_mode_config10);
        numColumn = cur.getColumnIndex("uncommitted_cmd");
        final int lnb_diseqc_mode_config11 = cur.getInt(numColumn);
        this.tv_satparams.setDiseqcUncommitted(lnb_diseqc_mode_config11);
        numColumn = cur.getColumnIndex("fast_diseqc");
        final int fast_diseqc = cur.getInt(numColumn);
        this.tv_satparams.setDiseqcFast(fast_diseqc);
        numColumn = cur.getColumnIndex("repeat_count");
        final int repeat_count = cur.getInt(numColumn);
        this.tv_satparams.setDiseqcRepeatCount(repeat_count);
        numColumn = cur.getColumnIndex("sequence_repeat");
        final int sequence_repeat = cur.getInt(numColumn);
        this.tv_satparams.setDiseqcSequenceRepeat(sequence_repeat);
        numColumn = cur.getColumnIndex("cmd_order");
        final int cmd_order = cur.getInt(numColumn);
        this.tv_satparams.setDiseqcOrder(cmd_order);
        numColumn = cur.getColumnIndex("motor_num");
        final int motor_num = cur.getInt(numColumn);
        this.tv_satparams.setMotorNum(motor_num);
        numColumn = cur.getColumnIndex("pos_num");
        final int pos_num = cur.getInt(numColumn);
        this.tv_satparams.setMotorPositionNum(pos_num);
        numColumn = cur.getColumnIndex("longitude");
        final double longitude = cur.getDouble(numColumn);
        numColumn = cur.getColumnIndex("latitude");
        final double latitude = cur.getDouble(numColumn);
        this.tv_satparams.setSatelliteRecLocal(longitude, latitude);
        this.tv_satparams.setUnicableParams(-1, 0);
    }
    
    private TVSatellite(final Context context, final Cursor c) {
        this.constructFromCursor(context, c);
    }
    
    public TVSatellite(final Context context, final String sat_name, final double sat_longitude) {
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from sat_para_table where sat_longitude = " + sat_longitude, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                this.constructFromCursor(context, c);
            }
            else {
                String cmd_i = "insert into sat_para_table(sat_name,lnb_num,lof_hi,lof_lo,lof_threshold,signal_22khz,";
                cmd_i += "voltage,motor_num,pos_num,lo_direction,la_direction,longitude,latitude,";
                cmd_i += "sat_longitude,diseqc_mode,tone_burst,committed_cmd,uncommitted_cmd,repeat_count,sequence_repeat,fast_diseqc,cmd_order) ";
                cmd_i = cmd_i + "values('" + sqliteEscape(sat_name) + "',0" + ",10600000" + ",9750000" + ",11700000" + "," + 2;
                cmd_i += ",3,0,0,0,0,0,0";
                cmd_i = cmd_i + "," + sat_longitude + "," + 0 + "," + 0;
                cmd_i += ",4,4,0,0,0,0)";
                context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd_i, (String[])null, (String)null);
                final String cmd_s = "select * from sat_para_table where sat_longitude = " + sat_longitude;
                final Cursor cr = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd_s, (String[])null, (String)null);
                if (cr != null) {
                    if (cr.moveToFirst()) {
                        this.constructFromCursor(context, cr);
                    }
                    else {
                        this.sat_id = -1;
                    }
                    cr.close();
                }
            }
            c.close();
        }
    }
    
    public static TVSatellite[] tvSatelliteList(final Context context) {
        TVSatellite[] satList = null;
        int sat_count = 0;
        int sat_index = 0;
        final String cmd = "select * from sat_para_table";
        final Cursor cur = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        sat_count = cur.getCount();
        if (sat_count > 0 && cur != null) {
            if (cur.moveToFirst()) {
                satList = new TVSatellite[sat_count];
                while (!cur.isAfterLast()) {
                    satList[sat_index] = new TVSatellite(context, cur);
                    cur.moveToNext();
                    ++sat_index;
                }
            }
            cur.close();
        }
        return satList;
    }
    
    public static TVSatellite tvSatelliteSelect(final Context context, final int sat_id) {
        TVSatellite sat = null;
        final String cmd = "select * from sat_para_table where db_id = " + sat_id;
        final Cursor cur = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                sat = new TVSatellite(context, cur);
            }
            cur.close();
        }
        return sat;
    }
    
    public void tvSatelliteDel(final Context context) {
        final int sat_id = this.sat_id;
        Log.d("TVSatellite", "tvSatelliteDel:" + sat_id);
        TVChannel.tvChannelDelBySatID(context, sat_id);
        final Cursor c = context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "delete from sat_para_table where db_id = " + sat_id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }
    
    public static void tvSatelliteDel(final Context context, final int sat_id) {
        Log.d("TVSatellite", "tvSatelliteDel:" + sat_id);
        TVChannel.tvChannelDelBySatID(context, sat_id);
        final Cursor c = context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "delete from sat_para_table where db_id = " + sat_id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }
    
    private void setSatelliteId(final int sat_id) {
        this.sat_id = sat_id;
    }
    
    public int getSatelliteId() {
        return this.sat_id;
    }
    
    public void setSatelliteName(final String sat_name) {
        this.sat_name = sat_name;
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set sat_name = '" + sqliteEscape(sat_name) + "' where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public String getSatelliteName() {
        return this.sat_name;
    }
    
    public TVSatelliteParams getParams() {
        return this.tv_satparams;
    }
    
    public void setSatelliteRecLocal(final double local_longitude, final double local_latitude) {
        this.tv_satparams.setSatelliteRecLocal(local_longitude, local_latitude);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set longitude = " + local_longitude + ", latitude = " + local_latitude, (String[])null, (String)null);
    }
    
    public void setSatelliteLnb(final int lnb_num, final int lnb_lof_hi, final int lnb_lof_lo, final int lnb_lof_threadhold) {
        this.tv_satparams.setSatelliteLnb(lnb_num, lnb_lof_hi, lnb_lof_lo, lnb_lof_threadhold);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set lnb_num = " + lnb_num + ", lof_hi = " + lnb_lof_hi + ", lof_lo = " + lnb_lof_lo + ", lof_threshold = " + lnb_lof_threadhold + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setSec22k(final int sec_22k_status) {
        this.tv_satparams.setSec22k(sec_22k_status);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set signal_22khz = " + sec_22k_status + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setSecVoltage(final int sec_voltage_status) {
        this.tv_satparams.setSecVoltage(sec_voltage_status);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set voltage = " + sec_voltage_status + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setSecToneBurst(final int sec_tone_burst) {
        this.tv_satparams.setSecToneBurst(sec_tone_burst);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set tone_burst = " + sec_tone_burst + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setDiseqcMode(final int diseqc_mode) {
        this.tv_satparams.setDiseqcMode(diseqc_mode);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set diseqc_mode = " + diseqc_mode + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setDiseqcCommitted(final int diseqc_committed) {
        this.tv_satparams.setDiseqcCommitted(diseqc_committed);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set committed_cmd = " + diseqc_committed + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setDiseqcUncommitted(final int diseqc_uncommitted) {
        this.tv_satparams.setDiseqcUncommitted(diseqc_uncommitted);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set uncommitted_cmd = " + diseqc_uncommitted + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setDiseqcRepeatCount(final int diseqc_repeat_count) {
        this.tv_satparams.setDiseqcRepeatCount(diseqc_repeat_count);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set repeat_count = " + diseqc_repeat_count + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setDiseqcSequenceRepeat(final int diseqc_sequence_repeat) {
        this.tv_satparams.setDiseqcSequenceRepeat(diseqc_sequence_repeat);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set sequence_repeat = " + diseqc_sequence_repeat + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setDiseqcFast(final int diseqc_fast) {
        this.tv_satparams.setDiseqcFast(diseqc_fast);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set fast_diseqc = " + diseqc_fast + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setDiseqcOrder(final int diseqc_order) {
        this.tv_satparams.setDiseqcOrder(diseqc_order);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set cmd_order = " + diseqc_order + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setMotorNum(final int motor_num) {
        this.tv_satparams.setMotorNum(motor_num);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set motor_num = " + motor_num + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setMotorPositionNum(final int motor_position_num) {
        this.tv_satparams.setMotorPositionNum(motor_position_num);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set pos_num = " + motor_position_num + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setSatelliteLongitude(final double sat_longitude) {
        this.tv_satparams.setSatelliteLongitude(sat_longitude);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update sat_para_table set sat_longitude = " + sat_longitude + " where db_id = " + this.sat_id, (String[])null, (String)null);
    }
    
    public void setUnicableParams(final int user_band, final int ub_freq) {
        this.tv_satparams.setUnicableParams(user_band, ub_freq);
    }
    
    public int getValidMotorPositionNum(final int motor_num, final int cur_motor_position_num) {
        int posCount = 0;
        int posIndex = 0;
        int[] posList = null;
        int numColumn = 0;
        int pos = 1;
        Log.d("TVSatellite", "motor_num:" + motor_num + "cur_motor_position_num:" + cur_motor_position_num);
        if (cur_motor_position_num == 0 || cur_motor_position_num == -1) {
            final Cursor cur = this.context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from sat_para_table where motor_num = " + motor_num + " order by pos_num ASC", (String[])null, (String)null);
            posCount = cur.getCount();
            if (posCount > 0) {
                if (cur != null) {
                    if (cur.moveToFirst()) {
                        posList = new int[posCount];
                        while (!cur.isAfterLast()) {
                            numColumn = cur.getColumnIndex("pos_num");
                            posList[posIndex] = cur.getInt(numColumn);
                            cur.moveToNext();
                            ++posIndex;
                        }
                    }
                    cur.close();
                }
                int conflict = 0;
                for (int i = 1; i <= 255; ++i) {
                    conflict = 0;
                    for (int j = 0; j < posCount; ++j) {
                        if (i == posList[j]) {
                            conflict = 1;
                            break;
                        }
                    }
                    if (conflict != 1) {
                        Log.d("TVSatellite", "new pos " + i);
                        pos = i;
                        break;
                    }
                    Log.d("TVSatellite", "conflict " + i);
                }
            }
        }
        else {
            pos = cur_motor_position_num;
        }
        return pos;
    }
    
    public static String sqliteEscape(String keyWord) {
        keyWord = keyWord.replace("'", "''");
        return keyWord;
    }
}
