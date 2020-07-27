// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.os.Parcel;
import android.os.Parcelable;

public class TVSatelliteParams implements Parcelable
{
    private static final String TAG = "TVSatelliteParams";
    public static final int SEC_22k_ON = 0;
    public static final int SEC_22k_OFF = 1;
    public static final int SEC_22k_AUTO = 2;
    public static final int SEC_VOLTAGE_13V = 0;
    public static final int SEC_VOLTAGE_18V = 1;
    public static final int SEC_VOLTAGE_OFF = 2;
    public static final int SEC_VOLTAGE_AUTO = 3;
    public static final int SEC_TONE_BURST_NONE = 0;
    public static final int SEC_TONE_BURST_A = 1;
    public static final int SEC_TONE_BURST_B = 2;
    public static final int DISEQC_MODE_NONE = 0;
    public static final int DISEQC_MODE_V1_0 = 1;
    public static final int DISEQC_MODE_V1_1 = 2;
    public static final int DISEQC_MODE_V1_2 = 3;
    public static final int DISEQC_MODE_V1_3 = 4;
    public static final int DISEQC_MODE_SMARTV = 5;
    public static final int DISEQC_COMMITTED_AA = 0;
    public static final int DISEQC_COMMITTED_AB = 1;
    public static final int DISEQC_COMMITTED_BA = 2;
    public static final int DISEQC_COMMITTED_BB = 3;
    public static final int DISEQC_NONE = 4;
    public static final int DISEQC_UNCOMMITTED_0 = 240;
    public static final int DISEQC_UNCOMMITTED_1 = 241;
    public static final int DISEQC_UNCOMMITTED_2 = 242;
    public static final int DISEQC_UNCOMMITTED_3 = 243;
    public static final int DISEQC_UNCOMMITTED_4 = 244;
    public static final int DISEQC_UNCOMMITTED_5 = 245;
    public static final int DISEQC_UNCOMMITTED_6 = 246;
    public static final int DISEQC_UNCOMMITTED_7 = 247;
    public static final int DISEQC_UNCOMMITTED_8 = 248;
    public static final int DISEQC_UNCOMMITTED_9 = 249;
    public static final int DISEQC_UNCOMMITTED_10 = 250;
    public static final int DISEQC_UNCOMMITTED_11 = 251;
    public static final int DISEQC_UNCOMMITTED_12 = 252;
    public static final int DISEQC_UNCOMMITTED_13 = 253;
    public static final int DISEQC_UNCOMMITTED_14 = 254;
    public static final int DISEQC_UNCOMMITTED_15 = 255;
    public double local_longitude;
    public double local_latitude;
    public int lnb_num;
    public int lnb_lof_hi;
    public int lnb_lof_lo;
    public int lnb_lof_threadhold;
    public int sec_22k_status;
    public int sec_voltage_status;
    public int sec_tone_burst;
    public int diseqc_mode;
    public int diseqc_committed;
    public int diseqc_uncommitted;
    public int diseqc_repeat_count;
    public int diseqc_sequence_repeat;
    public int diseqc_fast;
    public int diseqc_order;
    public int motor_num;
    public int motor_position_num;
    public double sat_longitude;
    private int user_band;
    private int ub_freq;
    public static final Parcelable.Creator<TVSatelliteParams> CREATOR;
    
    public void readFromParcel(final Parcel in) {
        this.local_longitude = in.readDouble();
        this.local_latitude = in.readDouble();
        this.lnb_num = in.readInt();
        this.lnb_lof_hi = in.readInt();
        this.lnb_lof_lo = in.readInt();
        this.lnb_lof_threadhold = in.readInt();
        this.sec_22k_status = in.readInt();
        this.sec_voltage_status = in.readInt();
        this.sec_tone_burst = in.readInt();
        this.diseqc_mode = in.readInt();
        this.diseqc_committed = in.readInt();
        this.diseqc_uncommitted = in.readInt();
        this.diseqc_repeat_count = in.readInt();
        this.diseqc_sequence_repeat = in.readInt();
        this.diseqc_fast = in.readInt();
        this.diseqc_order = in.readInt();
        this.motor_num = in.readInt();
        this.motor_position_num = in.readInt();
        this.sat_longitude = in.readDouble();
        this.user_band = in.readInt();
        this.ub_freq = in.readInt();
    }
    
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeDouble(this.local_longitude);
        dest.writeDouble(this.local_latitude);
        dest.writeInt(this.lnb_num);
        dest.writeInt(this.lnb_lof_hi);
        dest.writeInt(this.lnb_lof_lo);
        dest.writeInt(this.lnb_lof_threadhold);
        dest.writeInt(this.sec_22k_status);
        dest.writeInt(this.sec_voltage_status);
        dest.writeInt(this.sec_tone_burst);
        dest.writeInt(this.diseqc_mode);
        dest.writeInt(this.diseqc_committed);
        dest.writeInt(this.diseqc_uncommitted);
        dest.writeInt(this.diseqc_repeat_count);
        dest.writeInt(this.diseqc_sequence_repeat);
        dest.writeInt(this.diseqc_fast);
        dest.writeInt(this.diseqc_order);
        dest.writeInt(this.motor_num);
        dest.writeInt(this.motor_position_num);
        dest.writeDouble(this.sat_longitude);
        dest.writeInt(this.user_band);
        dest.writeInt(this.ub_freq);
    }
    
    public TVSatelliteParams(final Parcel in) {
        this.readFromParcel(in);
    }
    
    public TVSatelliteParams() {
    }
    
    public TVSatelliteParams(final double sat_longitude) {
        this.sat_longitude = sat_longitude;
    }
    
    public void setSatelliteRecLocal(final double local_longitude, final double local_latitude) {
        this.local_longitude = local_longitude;
        this.local_latitude = local_latitude;
    }
    
    public double getSatelliteRecLocalLongitude() {
        return this.local_longitude;
    }
    
    public double getSatelliteRecLocalLatitude() {
        return this.local_latitude;
    }
    
    public void setSatelliteLnb(final int lnb_num, final int lnb_lof_hi, final int lnb_lof_lo, final int lnb_lof_threadhold) {
        this.lnb_num = lnb_num;
        this.lnb_lof_hi = lnb_lof_hi;
        this.lnb_lof_lo = lnb_lof_lo;
        this.lnb_lof_threadhold = lnb_lof_threadhold;
    }
    
    public int getSatelliteLnbNum() {
        return this.lnb_num;
    }
    
    public int getSatelliteLnbLofhi() {
        return this.lnb_lof_hi;
    }
    
    public int getSatelliteLnbLofLo() {
        return this.lnb_lof_lo;
    }
    
    public int getSatelliteLnbLofthreadhold() {
        return this.lnb_lof_threadhold;
    }
    
    public void setSec22k(final int sec_22k_status) {
        this.sec_22k_status = sec_22k_status;
    }
    
    public int getSec22k() {
        return this.sec_22k_status;
    }
    
    public void setSecVoltage(final int sec_voltage_status) {
        this.sec_voltage_status = sec_voltage_status;
    }
    
    public int getSecVoltage() {
        return this.sec_voltage_status;
    }
    
    public void setSecToneBurst(final int sec_tone_burst) {
        this.sec_tone_burst = sec_tone_burst;
    }
    
    public int getSecToneBurst() {
        return this.sec_tone_burst;
    }
    
    public void setDiseqcMode(final int diseqc_mode) {
        this.diseqc_mode = diseqc_mode;
    }
    
    public int getDiseqcMode() {
        return this.diseqc_mode;
    }
    
    public void setDiseqcCommitted(final int diseqc_committed) {
        this.diseqc_committed = diseqc_committed;
    }
    
    public int getDiseqcCommitted() {
        return this.diseqc_committed;
    }
    
    public void setDiseqcUncommitted(final int diseqc_uncommitted) {
        this.diseqc_uncommitted = diseqc_uncommitted;
    }
    
    public int getDiseqcUncommitted() {
        return this.diseqc_uncommitted;
    }
    
    public void setDiseqcRepeatCount(final int diseqc_repeat_count) {
        this.diseqc_repeat_count = diseqc_repeat_count;
    }
    
    public int getDiseqcRepeatCount() {
        return this.diseqc_repeat_count;
    }
    
    public void setDiseqcSequenceRepeat(final int diseqc_sequence_repeat) {
        this.diseqc_sequence_repeat = diseqc_sequence_repeat;
    }
    
    public int getDiseqcSequenceRepeat() {
        return this.diseqc_sequence_repeat;
    }
    
    public void setDiseqcFast(final int diseqc_fast) {
        this.diseqc_fast = diseqc_fast;
    }
    
    public int getDiseqcFast() {
        return this.diseqc_fast;
    }
    
    public void setDiseqcOrder(final int diseqc_order) {
        this.diseqc_order = diseqc_order;
    }
    
    public int getDiseqcOrder() {
        return this.diseqc_order;
    }
    
    public void setMotorNum(final int motor_num) {
        this.motor_num = motor_num;
    }
    
    public int getMotorNum() {
        return this.motor_num;
    }
    
    public void setMotorPositionNum(final int motor_position_num) {
        this.motor_position_num = motor_position_num;
    }
    
    public int getMotorPositionNum() {
        return this.motor_position_num;
    }
    
    public void setSatelliteLongitude(final double sat_longitude) {
        this.sat_longitude = sat_longitude;
    }
    
    public double getSatelliteLongitude() {
        return this.sat_longitude;
    }
    
    public void setUnicableParams(final int user_band, final int ub_freq) {
        this.user_band = user_band;
        this.ub_freq = ub_freq;
    }
    
    public int getUnicableUserband() {
        return this.user_band;
    }
    
    public int getUnicableUbfreq() {
        return this.ub_freq;
    }
    
    public boolean equals(final TVSatelliteParams params) {
        return this.local_longitude == params.local_longitude && this.local_latitude == params.local_latitude && this.lnb_num == params.lnb_num && this.lnb_lof_hi == params.lnb_lof_hi && this.lnb_lof_lo == params.lnb_lof_lo && this.lnb_lof_threadhold == params.lnb_lof_threadhold && this.sec_22k_status == params.sec_22k_status && this.sec_voltage_status == params.sec_voltage_status && this.sec_tone_burst == params.sec_tone_burst && this.diseqc_mode == params.diseqc_mode && this.diseqc_committed == params.diseqc_committed && this.diseqc_uncommitted == params.diseqc_uncommitted && this.diseqc_repeat_count == params.diseqc_repeat_count && this.diseqc_sequence_repeat == params.diseqc_sequence_repeat && this.diseqc_fast == params.diseqc_fast && this.diseqc_order == params.diseqc_order && this.motor_num == params.motor_num && this.motor_position_num == params.motor_position_num && this.sat_longitude == params.sat_longitude && this.user_band == params.user_band && this.ub_freq == params.ub_freq;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public static Parcelable.Creator<TVSatelliteParams> getCreator() {
        return TVSatelliteParams.CREATOR;
    }
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<TVSatelliteParams>() {
            public TVSatelliteParams createFromParcel(final Parcel in) {
                return new TVSatelliteParams(in);
            }
            
            public TVSatelliteParams[] newArray(final int size) {
                return new TVSatelliteParams[size];
            }
        };
    }
}
