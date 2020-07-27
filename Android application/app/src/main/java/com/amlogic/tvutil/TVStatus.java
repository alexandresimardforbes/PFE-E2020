// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.os.Parcel;
import android.os.Parcelable;

public class TVStatus implements Parcelable
{
    public int programType;
    public TVProgramNumber programNo;
    public int programID;
    public static final Parcelable.Creator<TVStatus> CREATOR;
    
    public void readFromParcel(final Parcel in) {
        this.programType = in.readInt();
        this.programID = in.readInt();
        final int have_no = in.readInt();
        if (have_no != 0) {
            this.programNo = new TVProgramNumber(in);
        }
    }
    
    public void writeToParcel(final Parcel dest, final int flag) {
        dest.writeInt(this.programType);
        dest.writeInt(this.programID);
        if (this.programNo == null) {
            dest.writeInt(0);
        }
        else {
            dest.writeInt(1);
            this.programNo.writeToParcel(dest, flag);
        }
    }
    
    public TVStatus(final Parcel in) {
        this.readFromParcel(in);
    }
    
    public TVStatus() {
    }
    
    public int describeContents() {
        return 0;
    }
    
    public static Parcelable.Creator<TVStatus> getCreator() {
        return TVStatus.CREATOR;
    }
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<TVStatus>() {
            public TVStatus createFromParcel(final Parcel in) {
                return new TVStatus(in);
            }
            
            public TVStatus[] newArray(final int size) {
                return new TVStatus[size];
            }
        };
    }
}
