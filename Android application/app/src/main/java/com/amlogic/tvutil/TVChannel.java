// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.amlogic.tvdataprovider.TVDataProvider;

public class TVChannel implements Parcelable
{
    private static final String TAG = "TVChannel";
    private Context context;
    private int id;
    private int dvbTSID;
    private int dvbOrigNetID;
    private int fendID;
    private int tsSourceID;
    public TVChannelParams params;
    public static final Parcelable.Creator<TVChannel> CREATOR;

    public static Parcelable.Creator<TVChannel> getCreator() {
        return TVChannel.CREATOR;
    }

    public TVChannel(final Parcel in) {
        this.readFromParcel(in);
    }

    public void readFromParcel(final Parcel in) {
        this.id = in.readInt();
        this.dvbTSID = in.readInt();
        this.dvbOrigNetID = in.readInt();
        this.fendID = in.readInt();
        this.tsSourceID = in.readInt();
        this.params = (TVChannelParams)in.readParcelable(TVChannelParams.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.dvbTSID);
        dest.writeInt(this.dvbOrigNetID);
        dest.writeInt(this.fendID);
        dest.writeInt(this.tsSourceID);
        dest.writeParcelable((Parcelable)this.params, 0);
    }

    private void constructFromCursor(final Context context, final Cursor c) {
        this.context = context;
        int col = c.getColumnIndex("db_id");
        this.id = c.getInt(col);
        col = c.getColumnIndex("ts_id");
        this.dvbTSID = c.getInt(col);
        col = c.getColumnIndex("src");
        final int src = c.getInt(col);
        col = c.getColumnIndex("freq");
        final int freq = c.getInt(col);
        if (src == 1) {
            col = c.getColumnIndex("mod");
            final int mod = c.getInt(col);
            col = c.getColumnIndex("symb");
            final int symb = c.getInt(col);
            this.params = TVChannelParams.dvbcParams(freq, mod, symb);
        }
        else if (src == 2) {
            col = c.getColumnIndex("bw");
            final int bw = c.getInt(col);
            col = c.getColumnIndex("dvbt_flag");
            final int ofdm_mode = c.getInt(col);
            if (ofdm_mode == 0) {
                this.params = TVChannelParams.dvbtParams(freq, bw);
            }
            else {
                this.params = TVChannelParams.dvbt2Params(freq, bw);
            }
        }
        else if (src == 3) {
            col = c.getColumnIndex("mod");
            final int mod = c.getInt(col);
            col = c.getColumnIndex("inver");
            final int inversion = c.getInt(col);
            this.params = TVChannelParams.atscParams(freq, mod, inversion);
        }
        else if (src == 4) {
            col = c.getColumnIndex("std");
            final int std = c.getInt(col);
            col = c.getColumnIndex("aud_mode");
            final int aud_mode = c.getInt(col);
            col = c.getColumnIndex("flags");
            final int afc_flag = c.getInt(col);
            this.params = TVChannelParams.analogParams(freq, std, aud_mode, afc_flag);
        }
        else if (src == 0) {
            col = c.getColumnIndex("symb");
            final int symb = c.getInt(col);
            col = c.getColumnIndex("db_sat_para_id");
            final int satid = c.getInt(col);
            col = c.getColumnIndex("polar");
            final int satpolar = c.getInt(col);
            this.params = TVChannelParams.dvbsParams(context, freq, symb, satid, satpolar);
        }
        else if (src == 5) {
            col = c.getColumnIndex("bw");
            final int bw = c.getInt(col);
            this.params = TVChannelParams.dtmbParams(freq, bw);
        }
        else if (src == 6) {
            col = c.getColumnIndex("bw");
            final int bw = c.getInt(col);
            col = c.getColumnIndex("dvbt_flag");
            final int layer = c.getInt(col);
            (this.params = TVChannelParams.isdbtParams(freq, bw)).setISDBTLayer(layer);
        }
        this.fendID = 0;
    }

    private TVChannel(final Context context, final Cursor c) {
        this.constructFromCursor(context, c);
    }

    public TVChannel(final Context context, final TVChannelParams p) {
        String cmd_select = "select * from ts_table where ts_table.src = " + p.getMode() + " and ts_table.freq = " + p.getFrequency();
        if (p.isDVBSMode()) {
            cmd_select = cmd_select + " and ts_table.db_sat_para_id = " + p.getSatId() + " and ts_table.polar = " + p.getPolarisation();
        }
        Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd_select, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst() && p.getMode() != 4) {
                Log.d("TVChannel", "&&&&&&update............................p.getFrequency()=" + p.getFrequency());
                String cmd = "update ts_table set ";
                final int chanID = c.getInt(c.getColumnIndex("db_id"));
                if (p.isDVBCMode()) {
                    cmd = cmd + "symb=" + p.getSymbolRate();
                    cmd = cmd + ", mod=" + p.getModulation();
                }
                else if (p.isDVBTMode()) {
                    cmd = cmd + "bw=" + p.getBandwidth();
                }
                else if (p.isDVBSMode()) {
                    cmd = cmd + "symb=" + p.getSymbolRate();
                }
                else if (p.isAnalogMode()) {
                    cmd = cmd + "std=" + p.getStandard();
                    cmd = cmd + ", aud_mode=" + p.getAudioMode();
                }
                else {
                    cmd = cmd + "freq =" + p.getFrequency();
                }
                cmd = cmd + " where db_id = " + chanID;
                context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
                c.close();
                c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from ts_table where db_id=" + chanID, (String[])null, (String)null);
                if (c != null && c.moveToFirst()) {
                    this.constructFromCursor(context, c);
                }
            }
            else {
                Log.d("TVChannel", "....insert............................p.getFrequency()=" + p.getFrequency());
                String cmd = "insert into ts_table(src,freq,db_sat_para_id,polar,db_net_id,";
                cmd += "ts_id,symb,mod,bw,snr,ber,strength,std,aud_mode,flags) ";
                cmd = cmd + "values(" + p.getMode() + "," + p.getFrequency() + ",";
                if (p.isDVBCMode()) {
                    cmd = cmd + "-1,-1,-1,65535," + p.getSymbolRate() + "," + p.getModulation() + ",0,0,0,0,0,0,0)";
                }
                else if (p.isDVBTMode()) {
                    cmd = cmd + "-1,-1,-1,65535,0,0," + p.getBandwidth() + ",0,0,0,0,0,0)";
                }
                else if (p.isDVBSMode()) {
                    cmd = cmd + p.getSatId() + "," + p.getPolarisation() + ",-1,65535," + p.getSymbolRate() + ",0,0,0,0,0,0,0,0)";
                }
                else if (p.isAnalogMode()) {
                    cmd = cmd + "-1,-1,-1,65535,0,0,0,0,0,0," + p.getStandard() + "," + p.getAudioMode() + ",0)";
                }
                else if (p.isATSCMode()) {
                    cmd += "-1,-1,-1,65535,0,0,0,0,0,0,0,0,0)";
                }
                else {
                    cmd += "-1,-1,-1,65535,0,0,0,0,0,0,0,0,0)";
                }
                context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
                final Cursor cr = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from ts_table where ts_table.src = " + p.getMode() + " and ts_table.freq = " + p.getFrequency() + " order by db_id desc limit 1", (String[])null, (String)null);
                if (cr != null) {
                    if (cr.moveToFirst()) {
                        this.constructFromCursor(context, cr);
                    }
                    else {
                        this.id = -1;
                    }
                    cr.close();
                }
            }
            c.close();
        }
    }

    public static TVChannel[] tvChannelList(final Context context, final int sat_id) {
        TVChannel[] channelList = null;
        int tschannel_count = 0;
        int tschannel_index = 0;
        final String cmd = "select * from ts_table where db_sat_para_id = " + sat_id;
        final Cursor cur = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        tschannel_count = cur.getCount();
        if (tschannel_count > 0 && cur != null) {
            if (cur.moveToFirst()) {
                channelList = new TVChannel[tschannel_count];
                while (!cur.isAfterLast()) {
                    channelList[tschannel_index] = new TVChannel(context, cur);
                    cur.moveToNext();
                    ++tschannel_index;
                }
            }
            cur.close();
        }
        return channelList;
    }

    public static TVChannel selectByID(final Context context, final int id) {
        TVChannel chan = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from ts_table where ts_table.db_id = " + id, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                chan = new TVChannel(context, c);
            }
            c.close();
        }
        return chan;
    }

    public static TVChannel selectByParams(final Context context, final TVChannelParams params) {
        TVChannel chan = null;
        String cmd = "select * from ts_table where ts_table.src = " + params.getMode() + " and ts_table.freq = " + params.getFrequency();
        if (params.isDVBSMode()) {
            cmd = cmd + " and ts_table.db_sat_para_id = " + params.getSatId() + " and ts_table.polar = " + params.getPolarisation();
        }
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                chan = new TVChannel(context, c);
            }
            c.close();
        }
        return chan;
    }

    public void tvChannelDel(final Context context) {
        final int ts_db_id = this.id;
        TVProgram.tvProgramDelByChannelID(context, ts_db_id);
        final Cursor c = context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "delete from ts_table where db_id = " + ts_db_id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }

    public static void tvChannelDelBySatID(final Context context, final int sat_id) {
        Log.d("TVChannel", "tvChannelDelBySatID:" + sat_id);
        TVProgram.tvProgramDelBySatID(context, sat_id);
        final Cursor c = context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "delete from ts_table where db_sat_para_id = " + sat_id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }

    public int getID() {
        return this.id;
    }

    public int getDVBTSID() {
        if (this.params != null && !this.params.isDVBMode()) {
            throw new UnsupportedOperationException();
        }
        return this.dvbTSID;
    }

    public int getDVBOrigNetID() {
        if (this.params != null && !this.params.isDVBMode()) {
            throw new UnsupportedOperationException();
        }
        return this.dvbOrigNetID;
    }

    public int getFrontendID() {
        return this.fendID;
    }

    public int getTSSourceID() {
        return this.tsSourceID;
    }

    public TVChannelParams getParams() {
        return this.params;
    }

    public boolean isDVBCMode() {
        return this.params != null && this.params.isDVBCMode();
    }

    public boolean isDVBTMode() {
        return this.params != null && this.params.isDVBTMode();
    }

    public boolean isDVBSMode() {
        return this.params != null && this.params.isDVBSMode();
    }

    public boolean isDVBMode() {
        return this.params != null && this.params.isDVBMode();
    }

    public boolean isATSCMode() {
        return this.params != null && this.params.isATSCMode();
    }

    public boolean isAnalogMode() {
        return this.params != null && this.params.isAnalogMode();
    }

    public void setFrequency(final int frequency) {
        this.params.setFrequency(frequency);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update ts_table set freq=" + frequency + " where db_id = " + this.id, (String[])null, (String)null);
    }

    public void setSymbolRate(final int symbolRate) {
        this.params.setSymbolRate(symbolRate);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update ts_table set symb=" + symbolRate + " where db_id = " + this.id, (String[])null, (String)null);
    }

    public void setPolarisation(final int sat_polarisation) {
        this.params.setPolarisation(sat_polarisation);
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update ts_table set polar=" + sat_polarisation + " where db_id = " + this.id, (String[])null, (String)null);
    }

    public boolean setATVAudio(final int audio) {
        boolean ret = false;
        if (this.params != null) {
            ret = this.params.setATVAudio(audio);
        }
        if (ret) {
            this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update ts_table set aud_mode=" + audio + " where db_id = " + this.id, (String[])null, (String)null);
        }
        return ret;
    }

    public boolean setATVVideoFormat(final TVConst.CC_ATV_VIDEO_STANDARD fmt) {
        boolean ret = false;
        if (this.params != null) {
            ret = this.params.setATVVideoFormat(fmt);
        }
        if (ret) {
            this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update ts_table set std=" + this.params.getStandard() + " where db_id = " + this.id, (String[])null, (String)null);
        }
        return ret;
    }

    public boolean setATVAudioFormat(final TVConst.CC_ATV_AUDIO_STANDARD fmt) {
        boolean ret = false;
        if (this.params != null) {
            ret = this.params.setATVAudioFormat(fmt);
        }
        if (ret) {
            this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update ts_table set std=" + this.params.getStandard() + " where db_id = " + this.id, (String[])null, (String)null);
        }
        return ret;
    }

    public boolean setATVFreq(final int freq) {
        boolean ret = false;
        if (this.params != null && this.params.getMode() == 4) {
            this.params.frequency = freq;
            this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update ts_table set freq=" + this.params.frequency + " where db_id = " + this.id, (String[])null, (String)null);
            ret = true;
        }
        return ret;
    }

    public boolean setATVAfcData(final int data) {
        boolean ret = false;
        if (this.params != null && this.params.getMode() == 4 && this.params.afc_data != data) {
            this.params.afc_data = data;
            this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update ts_table set flags=" + this.params.afc_data + " where db_id = " + this.id, (String[])null, (String)null);
            ret = true;
        }
        return ret;
    }

    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<TVChannel>() {
            public TVChannel createFromParcel(final Parcel in) {
                return new TVChannel(in);
            }
            
            public TVChannel[] newArray(final int size) {
                return new TVChannel[size];
            }
        };
    }
}
