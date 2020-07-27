// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.os.Parcel;
import android.os.Parcelable;

public class TVProgramNumber implements Parcelable
{
    public static final int MINOR_CHECK_NONE = 0;
    public static final int MINOR_CHECK_UP = 1;
    public static final int MINOR_CHECK_DOWN = 2;
    public static final int MINOR_CHECK_NEAREST_UP = 3;
    public static final int MINOR_CHECK_NEAREST_DOWN = 4;
    private int major;
    private int minor;
    private int minorCheck;
    private boolean atscMode;
    public static final Parcelable.Creator<TVProgramNumber> CREATOR;
    
    public TVProgramNumber(final int no) {
        this.major = no;
        this.minor = 0;
        this.atscMode = false;
        this.minorCheck = 0;
    }
    
    public TVProgramNumber(final TVProgramNumber no) {
        this.major = no.major;
        this.minor = no.minor;
        this.atscMode = no.atscMode;
        this.minorCheck = 0;
    }
    
    public TVProgramNumber(final int major, final int minor) {
        this.major = major;
        this.minor = minor;
        this.atscMode = true;
        this.minorCheck = 0;
    }
    
    public TVProgramNumber(final int major, final int minor, final int minorCheck) {
        this.major = major;
        this.minor = minor;
        this.atscMode = true;
        this.minorCheck = minorCheck;
    }
    
    public TVProgramNumber(final Parcel in) {
        this.readFromParcel(in);
    }
    
    public void readFromParcel(final Parcel in) {
        this.major = in.readInt();
        this.minor = in.readInt();
        this.atscMode = (in.readInt() != 0);
        this.minorCheck = in.readInt();
    }
    
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(this.major);
        dest.writeInt(this.minor);
        dest.writeInt((int)(this.atscMode ? 1 : 0));
        dest.writeInt(this.minorCheck);
    }
    
    public int getNumber() {
        return this.major;
    }
    
    public int getMajor() {
        return this.major;
    }
    
    public int getMinor() {
        return this.minor;
    }
    
    public boolean isATSCMode() {
        return this.atscMode;
    }
    
    public int getMinorCheck() {
        return this.minorCheck;
    }
    
    public boolean equals(final TVProgramNumber no) {
        return no != null && this.major == no.major && this.minor == no.minor;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public static Parcelable.Creator<TVProgramNumber> getCreator() {
        return TVProgramNumber.CREATOR;
    }
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<TVProgramNumber>() {
            public TVProgramNumber createFromParcel(final Parcel in) {
                return new TVProgramNumber(in);
            }
            
            public TVProgramNumber[] newArray(final int size) {
                return new TVProgramNumber[size];
            }
        };
    }
}
