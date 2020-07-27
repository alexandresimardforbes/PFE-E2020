// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TVScanParams implements Parcelable
{
    public static final int TV_MODE_ATV = 0;
    public static final int TV_MODE_DTV = 1;
    public static final int TV_MODE_ADTV = 2;
    public static final int DTV_MODE_AUTO = 1;
    public static final int DTV_MODE_MANUAL = 2;
    public static final int DTV_MODE_ALLBAND = 3;
    public static final int DTV_MODE_BLIND = 4;
    public static final int DTV_OPTION_UNICABLE = 16;
    public static final int DTV_OPTION_FTA = 32;
    public static final int DTV_OPTION_NO_TV = 64;
    public static final int DTV_OPTION_NO_RADIO = 128;
    public static final int DTV_OPTION_ISDBT_ONESEG = 256;
    public static final int DTV_OPTION_ISDBT_FULLSEG = 512;
    public static final int ATV_MODE_AUTO = 1;
    public static final int ATV_MODE_MANUAL = 2;
    private int mode;
    private int fendID;
    private int dtvMode;
    private int dtvOptions;
    private int sat_id;
    private TVSatelliteParams tv_satparams;
    private int tsSourceID;
    private TVChannelParams startParams;
    private TVChannelParams[] chooseListParams;
    private int atvMode;
    private int startFreq;
    private int direction;
    private int channelID;
    public static final Parcelable.Creator<TVScanParams> CREATOR;

    public void readFromParcel(final Parcel in) {
        this.mode = in.readInt();
        this.fendID = in.readInt();
        if (this.mode == 1 || this.mode == 2) {
            this.dtvMode = in.readInt();
            this.dtvOptions = in.readInt();
            this.tsSourceID = in.readInt();
            if (this.dtvMode == 2 || this.dtvMode == 1) {
                this.startParams = new TVChannelParams(in);
            }
            if (this.dtvMode == 4 || this.dtvMode == 3) {
                this.sat_id = in.readInt();
                final int satparams_notnull = in.readInt();
                if (satparams_notnull == 1) {
                    this.tv_satparams = new TVSatelliteParams(in);
                }
            }
            if (this.dtvMode == 3) {
                final int length = in.readInt();
                if (length > 0) {
                    in.readTypedArray((Object[])(this.chooseListParams = new TVChannelParams[length]), (Parcelable.Creator) TVChannelParams.CREATOR);
                }
            }
        }
        else if (this.mode == 0) {
            this.atvMode = in.readInt();
            this.startFreq = in.readInt();
            this.direction = in.readInt();
            this.channelID = in.readInt();
        }
    }

    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(this.mode);
        dest.writeInt(this.fendID);
        if (this.mode == 1 || this.mode == 2) {
            dest.writeInt(this.dtvMode);
            dest.writeInt(this.dtvOptions);
            dest.writeInt(this.tsSourceID);
            if (this.dtvMode == 2 || this.dtvMode == 1) {
                this.startParams.writeToParcel(dest, flags);
            }
            if (this.dtvMode == 4 || this.dtvMode == 3) {
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
            }
            if (this.dtvMode == 3) {
                int length = 0;
                if (this.chooseListParams != null) {
                    length = this.chooseListParams.length;
                }
                dest.writeInt(length);
                if (length > 0) {
                    dest.writeTypedArray((Parcelable[])this.chooseListParams, flags);
                }
            }
        }
        else if (this.mode == 0) {
            dest.writeInt(this.atvMode);
            dest.writeInt(this.startFreq);
            dest.writeInt(this.direction);
            dest.writeInt(this.channelID);
        }
    }

    public TVScanParams() {
    }

    public TVScanParams(final Parcel in) {
        this.readFromParcel(in);
    }

    public TVScanParams(final int mode) {
        this.mode = mode;
    }

    public int getTvMode() {
        return this.mode;
    }

    public int getDtvMode() {
        return this.dtvMode;
    }

    public int getAtvMode() {
        return this.atvMode;
    }

    public int getTsSourceID() {
        return this.tsSourceID;
    }

    public int getAtvStartFreq() {
        return this.startFreq;
    }

    public void setAtvStartFreq(final int sf) {
        this.startFreq = sf;
    }

    public int getAtvChannelID() {
        return this.channelID;
    }

    public void setAtvChannelID(final int chanID) {
        this.channelID = chanID;
    }

    public int getSatID() {
        return this.sat_id;
    }

    public TVChannelParams[] getCnannelChooseList() {
        return this.chooseListParams;
    }

    public void setDtvOptions(final int options) {
        this.dtvOptions = options;
    }

    public int getDtvOptions() {
        return this.dtvOptions;
    }

    public TVChannelParams getParams() {
        return this.startParams;
    }

    public TVScanParams(final TVScanParams sp) {
        this.mode = sp.mode;
        this.dtvMode = sp.dtvMode;
        this.dtvOptions = sp.dtvOptions;
        this.sat_id = sp.sat_id;
        this.tv_satparams = sp.tv_satparams;
        this.fendID = sp.fendID;
        this.tsSourceID = sp.tsSourceID;
        this.startParams = sp.startParams;
        this.chooseListParams = sp.chooseListParams;
        this.atvMode = sp.atvMode;
        this.startFreq = sp.startFreq;
        this.direction = sp.direction;
        this.channelID = sp.channelID;
    }

    public static TVScanParams dtvManualScanParams(final int fendID, final TVChannelParams params) {
        final TVScanParams sp = new TVScanParams(1);
        sp.dtvMode = 2;
        sp.fendID = fendID;
        sp.startParams = params;
        return sp;
    }

    public static TVScanParams dtvAutoScanParams(final int fendID, final TVChannelParams mainParams) {
        final TVScanParams sp = new TVScanParams(1);
        sp.dtvMode = 1;
        sp.fendID = fendID;
        sp.startParams = mainParams;
        return sp;
    }

    public static TVScanParams dtvBlindScanParams(final int fendID, final int sat_id, final int tsSourceID) {
        final TVScanParams sp = new TVScanParams(1);
        sp.dtvMode = 4;
        sp.fendID = fendID;
        sp.sat_id = sat_id;
        sp.tsSourceID = tsSourceID;
        return sp;
    }

    public static TVScanParams dtvBlindScanParams(final int fendID, final TVSatelliteParams tv_satparams, final int tsSourceID) {
        final TVScanParams sp = new TVScanParams(1);
        sp.dtvMode = 4;
        sp.fendID = fendID;
        sp.tv_satparams = tv_satparams;
        sp.tsSourceID = tsSourceID;
        return sp;
    }

    public static TVScanParams dtvAllbandScanParams(final int fendID, final int tsSourceID) {
        final TVScanParams sp = new TVScanParams(1);
        sp.dtvMode = 3;
        sp.fendID = fendID;
        sp.tsSourceID = tsSourceID;
        sp.chooseListParams = null;
        return sp;
    }

    public static TVScanParams dtvAllbandScanParams(final int fendID, final int tsSourceID, final TVChannelParams[] channelList) {
        final TVScanParams sp = new TVScanParams(1);
        sp.dtvMode = 3;
        sp.fendID = fendID;
        sp.tsSourceID = tsSourceID;
        sp.chooseListParams = channelList;
        if (tsSourceID == 0) {
            sp.sat_id = channelList[0].sat_id;
            sp.tv_satparams = channelList[0].tv_satparams;
        }
        return sp;
    }

    public static TVScanParams dtvAllbandScanParams(final int fendID, final int tsSourceID, final ArrayList<TVChannelParams> channelList) {
        final TVScanParams sp = new TVScanParams(1);
        sp.dtvMode = 3;
        sp.fendID = fendID;
        sp.tsSourceID = tsSourceID;
        TVChannelParams[] channelParaList = null;
        if (channelList.size() > 0) {
            channelParaList = new TVChannelParams[channelList.size()];
            for (int i = 0; i < channelList.size(); ++i) {
                channelParaList[i] = new TVChannelParams(0);
                channelParaList[i].frequency = channelList.get(i).frequency;
                channelParaList[i].symbolRate = channelList.get(i).symbolRate;
                channelParaList[i].sat_id = channelList.get(i).sat_id;
                channelParaList[i].sat_polarisation = channelList.get(i).sat_polarisation;
                channelParaList[i].tv_satparams = channelList.get(i).tv_satparams;
            }
            sp.chooseListParams = channelParaList;
            if (tsSourceID == 0) {
                sp.sat_id = channelParaList[0].sat_id;
                sp.tv_satparams = channelParaList[0].tv_satparams;
            }
        }
        return sp;
    }

    public static TVScanParams atvManualScanParams(final int fendID, final int startFreq, final int direction, final int channelID) {
        final TVScanParams sp = new TVScanParams(0);
        sp.atvMode = 2;
        sp.fendID = fendID;
        sp.startFreq = startFreq;
        sp.direction = direction;
        sp.channelID = channelID;
        return sp;
    }

    public static TVScanParams atvManualScanParams(final int fendID, final int direction) {
        final TVScanParams sp = new TVScanParams(0);
        sp.atvMode = 2;
        sp.fendID = fendID;
        sp.startFreq = 0;
        sp.direction = direction;
        sp.channelID = -1;
        return sp;
    }

    public static TVScanParams atvAutoScanParams(final int fendID) {
        final TVScanParams sp = new TVScanParams(0);
        sp.atvMode = 1;
        sp.fendID = fendID;
        return sp;
    }

    public static TVScanParams adtvScanParams(final int fendID, final int dtvTsSourceID) {
        final TVScanParams sp = new TVScanParams(2);
        sp.dtvMode = 3;
        sp.fendID = fendID;
        sp.tsSourceID = dtvTsSourceID;
        return sp;
    }

    public int describeContents() {
        return 0;
    }

    public static Parcelable.Creator<TVScanParams> getCreator() {
        return TVScanParams.CREATOR;
    }

    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<TVScanParams>() {
            public TVScanParams createFromParcel(final Parcel in) {
                return new TVScanParams(in);
            }
            
            public TVScanParams[] newArray(final int size) {
                return new TVScanParams[size];
            }
        };
    }
}
