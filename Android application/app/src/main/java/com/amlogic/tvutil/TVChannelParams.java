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

public class TVChannelParams implements Parcelable
{
    private static String TAG;
    public static final int FE_HAS_SIGNAL = 1;
    public static final int FE_HAS_CARRIER = 2;
    public static final int FE_HAS_VITERBI = 4;
    public static final int FE_HAS_SYNC = 8;
    public static final int FE_HAS_LOCK = 16;
    public static final int FE_TIMEDOUT = 32;
    public static final int FE_REINIT = 64;
    public static final int MODE_QPSK = 0;
    public static final int MODE_QAM = 1;
    public static final int MODE_OFDM = 2;
    public static final int MODE_ATSC = 3;
    public static final int MODE_ANALOG = 4;
    public static final int MODE_DTMB = 5;
    public static final int MODE_ISDBT = 6;
    public static final int OFDM_MODE_DVBT = 0;
    public static final int OFDM_MODE_DVBT2 = 1;
    public static final int BANDWIDTH_8_MHZ = 0;
    public static final int BANDWIDTH_7_MHZ = 1;
    public static final int BANDWIDTH_6_MHZ = 2;
    public static final int BANDWIDTH_AUTO = 3;
    public static final int BANDWIDTH_5_MHZ = 4;
    public static final int BANDWIDTH_10_MHZ = 5;
    public static final int MODULATION_QPSK = 0;
    public static final int MODULATION_QAM_16 = 1;
    public static final int MODULATION_QAM_32 = 2;
    public static final int MODULATION_QAM_64 = 3;
    public static final int MODULATION_QAM_128 = 4;
    public static final int MODULATION_QAM_256 = 5;
    public static final int MODULATION_QAM_AUTO = 6;
    public static final int MODULATION_VSB_8 = 7;
    public static final int MODULATION_VSB_16 = 8;
    public static final int MODULATION_PSK_8 = 9;
    public static final int MODULATION_APSK_16 = 10;
    public static final int MODULATION_APSK_32 = 11;
    public static final int MODULATION_DQPSK = 12;
    public static final int AUDIO_MONO = 0;
    public static final int AUDIO_STEREO = 1;
    public static final int AUDIO_LANG2 = 2;
    public static final int AUDIO_SAP = 2;
    public static final int AUDIO_LANG1 = 3;
    public static final int AUDIO_LANG1_LANG2 = 4;
    public static final int STD_PAL_B = 1;
    public static final int STD_PAL_B1 = 2;
    public static final int STD_PAL_G = 4;
    public static final int STD_PAL_H = 8;
    public static final int STD_PAL_I = 16;
    public static final int STD_PAL_D = 32;
    public static final int STD_PAL_D1 = 64;
    public static final int STD_PAL_K = 128;
    public static final int STD_PAL_M = 256;
    public static final int STD_PAL_N = 512;
    public static final int STD_PAL_Nc = 1024;
    public static final int STD_PAL_60 = 2048;
    public static final int STD_NTSC_M = 4096;
    public static final int STD_NTSC_M_JP = 8192;
    public static final int STD_NTSC_443 = 16384;
    public static final int STD_NTSC_M_KR = 32768;
    public static final int STD_SECAM_B = 65536;
    public static final int STD_SECAM_D = 131072;
    public static final int STD_SECAM_G = 262144;
    public static final int STD_SECAM_H = 524288;
    public static final int STD_SECAM_K = 1048576;
    public static final int STD_SECAM_K1 = 2097152;
    public static final int STD_SECAM_L = 4194304;
    public static final int STD_SECAM_LC = 8388608;
    public static final int STD_ATSC_8_VSB = 16777216;
    public static final int STD_ATSC_16_VSB = 33554432;
    public static final int STD_NTSC = 45056;
    public static final int STD_SECAM_DK = 3276800;
    public static final int STD_SECAM = 16711680;
    public static final int STD_PAL_BG = 7;
    public static final int STD_PAL_DK = 224;
    public static final int STD_PAL = 255;
    public static final int TUNER_STD_MN = 46848;
    public static final int STD_B = 65539;
    public static final int STD_GH = 786444;
    public static final int STD_DK = 3277024;
    public static final int STD_M = 4352;
    public static final int STD_BG = 327687;
    public static final int COLOR_AUTO = 33554432;
    public static final int COLOR_PAL = 67108864;
    public static final int COLOR_NTSC = 134217728;
    public static final int COLOR_SECAM = 268435456;
    public static final int SAT_POLARISATION_H = 0;
    public static final int SAT_POLARISATION_V = 1;
    public static final int ISDBT_LAYER_ALL = 0;
    public static final int ISDBT_LAYER_A = 0;
    public static final int ISDBT_LAYER_B = 0;
    public static final int ISDBT_LAYER_C = 0;
    public int inversion;
    public int mode;
    public int frequency;
    public int symbolRate;
    public int modulation;
    public int bandwidth;
    public int ofdm_mode;
    public int audio;
    public int standard;
    public int afc_data;
    public int sat_id;
    public TVSatelliteParams tv_satparams;
    public int sat_polarisation;
    public int isdbtLayer;
    public static final Parcelable.Creator<TVChannelParams> CREATOR;

    public static int getModeFromString(final String str) {
        if (str.equals("dvbt")) {
            return 2;
        }
        if (str.equals("dvbc")) {
            return 1;
        }
        if (str.equals("dvbs")) {
            return 0;
        }
        if (str.equals("atsc")) {
            return 3;
        }
        if (str.equals("analog")) {
            return 4;
        }
        if (str.equals("dtmb")) {
            return 5;
        }
        return -1;
    }

    public void readFromParcel(final Parcel in) {
        this.mode = in.readInt();
        this.frequency = in.readInt();
        if (this.mode == 1 || this.mode == 0) {
            this.symbolRate = in.readInt();
        }
        if (this.mode == 1 || this.mode == 3) {
            this.modulation = in.readInt();
        }
        this.inversion = in.readInt();
        if (this.mode == 2 || this.mode == 5 || this.mode == 6) {
            this.bandwidth = in.readInt();
            this.ofdm_mode = in.readInt();
        }
        if (this.mode == 4) {
            this.audio = in.readInt();
            this.standard = in.readInt();
            this.afc_data = in.readInt();
        }
        if (this.mode == 0) {
            this.sat_id = in.readInt();
            final int satparams_notnull = in.readInt();
            if (satparams_notnull == 1) {
                this.tv_satparams = new TVSatelliteParams(in);
            }
            this.sat_polarisation = in.readInt();
        }
        if (this.mode == 6) {
            this.isdbtLayer = in.readInt();
        }
    }

    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(this.mode);
        dest.writeInt(this.frequency);
        if (this.mode == 1 || this.mode == 0) {
            dest.writeInt(this.symbolRate);
        }
        if (this.mode == 1 || this.mode == 3) {
            dest.writeInt(this.modulation);
        }
        dest.writeInt(this.inversion);
        if (this.mode == 2 || this.mode == 5 || this.mode == 6) {
            dest.writeInt(this.bandwidth);
            dest.writeInt(this.ofdm_mode);
        }
        if (this.mode == 4) {
            dest.writeInt(this.audio);
            dest.writeInt(this.standard);
            dest.writeInt(this.afc_data);
        }
        if (this.mode == 0) {
            dest.writeInt(this.sat_id);
            int satparams_notnull = 0;
            if (this.tv_satparams != null) {
                satparams_notnull = 1;
            }
            else {
                satparams_notnull = 0;
            }
            dest.writeInt(satparams_notnull);
            if (satparams_notnull == 1) {
                this.tv_satparams.writeToParcel(dest, flags);
            }
            dest.writeInt(this.sat_polarisation);
        }
        if (this.mode == 6) {
            dest.writeInt(this.isdbtLayer);
        }
    }

    public TVChannelParams(final Parcel in) {
        this.readFromParcel(in);
    }

    public TVChannelParams(final int mode) {
        this.mode = mode;
    }

    public static TVChannelParams dvbcParams(final int frequency, final int modulation, final int symbolRate) {
        final TVChannelParams tp = new TVChannelParams(1);
        tp.frequency = frequency;
        tp.modulation = modulation;
        tp.symbolRate = symbolRate;
        return tp;
    }

    public static TVChannelParams dvbtParams(final int frequency, final int bandwidth) {
        final TVChannelParams tp = new TVChannelParams(2);
        tp.frequency = frequency;
        tp.bandwidth = bandwidth;
        tp.ofdm_mode = 0;
        return tp;
    }

    public static TVChannelParams dvbt2Params(final int frequency, final int bandwidth) {
        final TVChannelParams tp = new TVChannelParams(2);
        Log.d(TVChannelParams.TAG, "---new DVBT2 channel params---");
        tp.frequency = frequency;
        tp.bandwidth = bandwidth;
        tp.ofdm_mode = 1;
        return tp;
    }

    public static TVChannelParams dvbsParams(final Context context, final int frequency, final int symbolRate, final int sat_id, final int sat_polarisation) {
        final TVChannelParams tp = new TVChannelParams(0);
        tp.frequency = frequency;
        tp.symbolRate = symbolRate;
        tp.sat_id = sat_id;
        tp.sat_polarisation = sat_polarisation;
        final TVSatellite sat = TVSatellite.tvSatelliteSelect(context, sat_id);
        tp.tv_satparams = sat.getParams();
        return tp;
    }

    public static TVChannelParams atscParams(final int frequency, final int modulation, final int inversion) {
        final TVChannelParams tp = new TVChannelParams(3);
        tp.frequency = frequency;
        tp.modulation = modulation;
        tp.inversion = inversion;
        return tp;
    }

    public static TVChannelParams atscParams(final int frequency, final int modulation) {
        final TVChannelParams tp = new TVChannelParams(3);
        tp.frequency = frequency;
        tp.modulation = modulation;
        return tp;
    }

    public static TVChannelParams analogParams(final int frequency, final int std, final int audio, final int afc_flag) {
        final TVChannelParams tp = new TVChannelParams(4);
        tp.frequency = frequency;
        tp.audio = audio;
        tp.standard = std;
        tp.afc_data = afc_flag;
        return tp;
    }

    public static TVChannelParams dtmbParams(final int frequency, final int bandwidth) {
        final TVChannelParams tp = new TVChannelParams(5);
        tp.frequency = frequency;
        tp.bandwidth = bandwidth;
        return tp;
    }

    public static TVChannelParams isdbtParams(final int frequency, final int bandwidth) {
        final TVChannelParams tp = new TVChannelParams(6);
        tp.frequency = frequency;
        tp.bandwidth = bandwidth;
        tp.isdbtLayer = 0;
        Log.i(TVChannelParams.TAG, "bandwidth==" + bandwidth);
        return tp;
    }

    public static TVChannelParams[] channelCurAllbandParams(final Context context, final String region, final int mode) {
        TVChannelParams[] channelList = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from region_table where name='" + region + "' and source=" + mode, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                final int col = c.getColumnIndex("frequencies");
                final String freqs = c.getString(col);
                if (freqs != null && freqs.length() > 0) {
                    final String[] flist = freqs.split(" ");
                    if (flist != null && flist.length > 0) {
                        int frequency = 0;
                        int bandwidth = 0;
                        if (mode == 0) {
                            channelList = new TVChannelParams[flist.length];
                            for (int i = 0; i < channelList.length; ++i) {
                                frequency = Integer.parseInt(flist[i]);
                                channelList[i] = dvbsParams(context, frequency, 0, 0, 0);
                            }
                        }
                        else if (mode == 1) {
                            channelList = new TVChannelParams[flist.length];
                            for (int i = 0; i < channelList.length; ++i) {
                                frequency = Integer.parseInt(flist[i]);
                                channelList[i] = dvbcParams(frequency, 0, 0);
                            }
                        }
                        else if (mode == 2) {
                            channelList = new TVChannelParams[flist.length / 2];
                            for (int i = 0; i < flist.length; ++i) {
                                if (i % 2 == 0) {
                                    frequency = Integer.parseInt(flist[i]);
                                }
                                else {
                                    bandwidth = Integer.parseInt(flist[i]);
                                    channelList[i / 2] = dvbtParams(frequency, bandwidth);
                                }
                            }
                        }
                        else if (mode == 3) {
                            channelList = new TVChannelParams[flist.length];
                            for (int i = 0; i < channelList.length; ++i) {
                                frequency = Integer.parseInt(flist[i]);
                                channelList[i] = atscParams(frequency, 7, 0);
                            }
                        }
                        else if (mode == 4) {
                            channelList = new TVChannelParams[flist.length];
                            for (int i = 0; i < channelList.length; ++i) {
                                frequency = Integer.parseInt(flist[i]);
                                channelList[i] = analogParams(frequency, 0, 0, 0);
                            }
                        }
                    }
                }
            }
            c.close();
        }
        return channelList;
    }

    public static TVConst.CC_ATV_AUDIO_STANDARD AudioStd2Enum(final int data) {
        TVConst.CC_ATV_AUDIO_STANDARD std = null;
        if ((data & 0xE0) == 0xE0 || (data & 0x320000) == 0x320000) {
            std = TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_DK;
        }
        else {
            if ((data & 0x10) == 0x10) {
                return TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_I;
            }
            if ((data & 0x7) == 0x7 || (data & 0x10000) == 0x10000 || (data & 0x40000) == 0x40000) {
                std = TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_BG;
            }
            else if ((data & 0x100) == 0x100 || (data & 0x1000) == 0x1000) {
                std = TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_M;
            }
            else if ((data & 0x400000) == 0x400000) {
                std = TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_L;
            }
        }
        return std;
    }

    public static TVConst.CC_ATV_VIDEO_STANDARD VideoStd2Enum(final int data) {
        if ((data & 0x4000000) == 0x4000000) {
            return TVConst.CC_ATV_VIDEO_STANDARD.CC_ATV_VIDEO_STD_PAL;
        }
        if ((data & 0x8000000) == 0x8000000) {
            return TVConst.CC_ATV_VIDEO_STANDARD.CC_ATV_VIDEO_STD_NTSC;
        }
        if ((data & 0x10000000) == 0x10000000) {
            return TVConst.CC_ATV_VIDEO_STANDARD.CC_ATV_VIDEO_STD_SECAM;
        }
        if ((data & 0x2000000) == 0x2000000) {
            return TVConst.CC_ATV_VIDEO_STANDARD.CC_ATV_VIDEO_STD_AUTO;
        }
        return null;
    }

    public static int Change2VideoStd(final int data) {
        int videostd = 0;
        if (data == TVConst.CC_ATV_VIDEO_STANDARD.CC_ATV_VIDEO_STD_AUTO.ordinal()) {
            videostd = 33554432;
        }
        else if (data == TVConst.CC_ATV_VIDEO_STANDARD.CC_ATV_VIDEO_STD_PAL.ordinal()) {
            videostd = 67108864;
        }
        else if (data == TVConst.CC_ATV_VIDEO_STANDARD.CC_ATV_VIDEO_STD_NTSC.ordinal()) {
            videostd = 134217728;
        }
        else if (data == TVConst.CC_ATV_VIDEO_STANDARD.CC_ATV_VIDEO_STD_SECAM.ordinal()) {
            videostd = 268435456;
        }
        return videostd;
    }

    public static int Change2AudioStd(final int video_std, final int audio_std) {
        int tmpTunerStd = 0;
        if (audio_std < 0 || audio_std < TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_DK.ordinal() || audio_std > TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_AUTO.ordinal()) {}
        if (video_std == TVConst.CC_ATV_VIDEO_STANDARD.CC_ATV_VIDEO_STD_AUTO.ordinal()) {
            if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_DK.ordinal()) {
                tmpTunerStd |= 0xE0;
            }
            else if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_I.ordinal()) {
                tmpTunerStd |= 0x10;
            }
            else if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_BG.ordinal()) {
                tmpTunerStd |= 0x7;
            }
            else if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_M.ordinal()) {
                tmpTunerStd |= 0x1000;
            }
        }
        else if (video_std == TVConst.CC_ATV_VIDEO_STANDARD.CC_ATV_VIDEO_STD_PAL.ordinal()) {
            if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_DK.ordinal()) {
                tmpTunerStd |= 0xE0;
            }
            else if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_I.ordinal()) {
                tmpTunerStd |= 0x10;
            }
            else if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_BG.ordinal()) {
                tmpTunerStd |= 0x7;
            }
            else if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_M.ordinal()) {
                tmpTunerStd |= 0x100;
            }
        }
        else if (video_std == TVConst.CC_ATV_VIDEO_STANDARD.CC_ATV_VIDEO_STD_NTSC.ordinal()) {
            if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_DK.ordinal()) {
                tmpTunerStd |= 0xE0;
            }
            else if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_I.ordinal()) {
                tmpTunerStd |= 0x10;
            }
            else if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_BG.ordinal()) {
                tmpTunerStd |= 0x7;
            }
            else if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_M.ordinal()) {
                tmpTunerStd |= 0x1000;
            }
        }
        else if (video_std == TVConst.CC_ATV_VIDEO_STANDARD.CC_ATV_VIDEO_STD_SECAM.ordinal()) {
            if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_DK.ordinal()) {
                tmpTunerStd |= 0x320000;
            }
            else if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_I.ordinal()) {
                tmpTunerStd |= 0x10;
            }
            else if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_BG.ordinal()) {
                tmpTunerStd |= 0x50000;
            }
            else if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_M.ordinal()) {
                tmpTunerStd |= 0x1000;
            }
            else if (audio_std == TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_L.ordinal()) {
                tmpTunerStd |= 0x400000;
            }
        }
        return tmpTunerStd;
    }

    public static int getTunerStd(final int video_std, final int audio_std) {
        final int vdata = Change2VideoStd(video_std);
        final int adata = Change2AudioStd(video_std, audio_std);
        return vdata | adata;
    }

    static TVConst.CC_ATV_AUDIO_STANDARD ATVHandleAudioStdCfg(final String key) {
        if (key.equals("DK")) {
            return TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_DK;
        }
        if (key.equals("I")) {
            return TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_I;
        }
        if (key.equals("BG")) {
            return TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_BG;
        }
        if (key.equals("M")) {
            return TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_M;
        }
        if (key.equals("L")) {
            return TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_L;
        }
        if (key.equals("AUTO")) {
            return TVConst.CC_ATV_AUDIO_STANDARD.CC_ATV_AUDIO_STD_AUTO;
        }
        return null;
    }

    public boolean setATVAudio(final int audio) {
        if (this.audio == audio) {
            return false;
        }
        this.audio = audio;
        return true;
    }

    public boolean setATVVideoFormat(final TVConst.CC_ATV_VIDEO_STANDARD fmt) {
        final int afmt = AudioStd2Enum(this.standard).ordinal();
        final int std = getTunerStd(fmt.ordinal(), afmt);
        if (std == this.standard) {
            return false;
        }
        this.standard = std;
        return true;
    }

    public boolean setATVAudioFormat(final TVConst.CC_ATV_AUDIO_STANDARD fmt) {
        final int vfmt = VideoStd2Enum(this.standard).ordinal();
        final int std = getTunerStd(vfmt, fmt.ordinal());
        if (std == this.standard) {
            return false;
        }
        this.standard = std;
        return true;
    }

    public int getMode() {
        return this.mode;
    }

    public boolean isDVBMode() {
        return this.mode == 0 || this.mode == 1 || this.mode == 2 || this.mode == 5;
    }

    public boolean isDVBCMode() {
        return this.mode == 1;
    }

    public boolean isDVBTMode() {
        return this.mode == 2;
    }

    public boolean isDVBSMode() {
        return this.mode == 0;
    }

    public boolean isATSCMode() {
        return this.mode == 3;
    }

    public boolean isAnalogMode() {
        return this.mode == 4;
    }

    public boolean isDTMBMode() {
        return this.mode == 5;
    }

    public boolean isISDBTMode() {
        return this.mode == 6;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public int getInversion() {
        return this.inversion;
    }

    public int getOFDM_Mode() {
        return this.ofdm_mode;
    }

    public void setFrequency(final int frequency) {
        this.frequency = frequency;
    }

    public int getAudioMode() {
        if (!this.isAnalogMode()) {
            throw new UnsupportedOperationException();
        }
        return this.audio;
    }

    public int getStandard() {
        if (!this.isAnalogMode()) {
            throw new UnsupportedOperationException();
        }
        return this.standard;
    }

    public int getBandwidth() {
        if (this.mode != 2 && this.mode != 5) {
            throw new UnsupportedOperationException();
        }
        return this.bandwidth;
    }

    public int getModulation() {
        if (this.mode != 1) {
            throw new UnsupportedOperationException();
        }
        return this.modulation;
    }

    public int getSymbolRate() {
        if (this.mode != 0 && this.mode != 1) {
            throw new UnsupportedOperationException();
        }
        return this.symbolRate;
    }

    public void setSymbolRate(final int symbolRate) {
        if (this.mode != 0 && this.mode != 1) {
            throw new UnsupportedOperationException();
        }
        this.symbolRate = symbolRate;
    }

    public int getSatId() {
        if (this.mode != 0) {
            throw new UnsupportedOperationException();
        }
        return this.sat_id;
    }

    public int getPolarisation() {
        if (this.mode != 0) {
            throw new UnsupportedOperationException();
        }
        return this.sat_polarisation;
    }

    public int getISDBTLayer() {
        if (this.mode != 6) {
            throw new UnsupportedOperationException();
        }
        return this.isdbtLayer;
    }

    public void setISDBTLayer(final int layer) {
        if (this.mode != 6) {
            throw new UnsupportedOperationException();
        }
        this.isdbtLayer = this.isdbtLayer;
    }

    public void setPolarisation(final int sat_polarisation) {
        if (this.mode != 0) {
            throw new UnsupportedOperationException();
        }
        this.sat_polarisation = sat_polarisation;
    }

    public boolean equals(final TVChannelParams params) {
        if (this.mode != params.mode) {
            return false;
        }
        if (this.mode == 4 && Math.abs(this.frequency - params.frequency) > 2000000) {
            return false;
        }
        if (this.frequency != params.frequency) {
            return false;
        }
        if (this.mode == 0) {
            if (this.sat_polarisation != params.sat_polarisation) {
                return false;
            }
            if (!this.tv_satparams.equals(params.tv_satparams)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals_frontendevt(final TVChannelParams params) {
        if (this.mode != params.mode) {
            return false;
        }
        if (this.mode != 0) {
            if (this.frequency != params.frequency) {
                return false;
            }
        }
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public static Parcelable.Creator<TVChannelParams> getCreator() {
        return TVChannelParams.CREATOR;
    }

    static {
        TVChannelParams.TAG = "TVChannelParams";
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<TVChannelParams>() {
            public TVChannelParams createFromParcel(final Parcel in) {
                return new TVChannelParams(in);
            }
            
            public TVChannelParams[] newArray(final int size) {
                return new TVChannelParams[size];
            }
        };
    }
}
