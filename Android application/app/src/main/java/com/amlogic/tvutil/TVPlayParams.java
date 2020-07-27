// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.os.Parcel;
import android.os.Parcelable;

public class TVPlayParams implements Parcelable
{
    public static final int PLAY_PROGRAM_NUMBER = 0;
    public static final int PLAY_PROGRAM_ID = 1;
    public static final int PLAY_PROGRAM_UP = 2;
    public static final int PLAY_PROGRAM_DOWN = 3;
    private int type;
    private TVProgramNumber number;
    private int id;
    public static final Parcelable.Creator<TVPlayParams> CREATOR;
    
    public void readFromParcel(final Parcel in) {
        this.type = in.readInt();
        if (this.type == 1) {
            this.id = in.readInt();
        }
        else if (this.type == 0) {
            this.number = new TVProgramNumber(in);
        }
    }
    
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(this.type);
        if (this.type == 1) {
            dest.writeInt(this.id);
        }
        else if (this.type == 0) {
            this.number.writeToParcel(dest, flags);
        }
    }
    
    public TVPlayParams(final Parcel in) {
        this.readFromParcel(in);
    }
    
    public static TVPlayParams playProgramByNumber(final TVProgramNumber no) {
        final TVPlayParams tp = new TVPlayParams(0);
        tp.number = no;
        return tp;
    }
    
    public static TVPlayParams playProgramByID(final int id) {
        final TVPlayParams tp = new TVPlayParams(1);
        tp.id = id;
        return tp;
    }
    
    public static TVPlayParams playProgramUp() {
        final TVPlayParams tp = new TVPlayParams(2);
        return tp;
    }
    
    public static TVPlayParams playProgramDown() {
        final TVPlayParams tp = new TVPlayParams(3);
        return tp;
    }
    
    public TVPlayParams(final int type) {
        this.type = type;
    }
    
    public TVPlayParams(final TVPlayParams tp) {
        this.type = tp.type;
        this.number = new TVProgramNumber(tp.number);
        this.id = tp.id;
    }
    
    public boolean equals(final TVPlayParams tp) {
        if (tp.type != this.type) {
            return false;
        }
        if (tp.type == 0) {
            return tp.number.equals(this.number);
        }
        return tp.type == 1 && tp.id == this.id;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public static Parcelable.Creator<TVPlayParams> getCreator() {
        return TVPlayParams.CREATOR;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getProgramID() throws Exception {
        if (this.type != 1) {
            throw new Exception();
        }
        return this.id;
    }
    
    public TVProgramNumber getProgramNumber() throws Exception {
        if (this.type != 0) {
            throw new Exception();
        }
        return this.number;
    }
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<TVPlayParams>() {
            public TVPlayParams createFromParcel(final Parcel in) {
                return new TVPlayParams(in);
            }
            
            public TVPlayParams[] newArray(final int size) {
                return new TVPlayParams[size];
            }
        };
    }
}
