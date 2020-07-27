// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.os.Parcel;
import android.os.Parcelable;

public class DTVPlaybackParams implements Parcelable
{
    public static final int PLAYBACK_ST_STOPPED = 0;
    public static final int PLAYBACK_ST_PLAYING = 1;
    public static final int PLAYBACK_ST_PAUSED = 2;
    public static final int PLAYBACK_ST_FFFB = 3;
    public static final int PLAYBACK_ST_EXIT = 4;
    private String filePath;
    private int status;
    private long currentTime;
    private long totalTime;
    public static final Parcelable.Creator<DTVPlaybackParams> CREATOR;
    
    public void readFromParcel(final Parcel in) {
        this.status = in.readInt();
        this.currentTime = in.readLong();
        this.totalTime = in.readLong();
    }
    
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(this.status);
        dest.writeLong(this.currentTime);
        dest.writeLong(this.totalTime);
    }
    
    public DTVPlaybackParams(final Parcel in) {
        this.readFromParcel(in);
    }
    
    public DTVPlaybackParams() {
    }
    
    public DTVPlaybackParams(final String filePath, final long totalTime) {
        this.status = 0;
        this.currentTime = 0L;
        this.totalTime = totalTime;
        this.filePath = filePath;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public static Parcelable.Creator<DTVPlaybackParams> getCreator() {
        return DTVPlaybackParams.CREATOR;
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public long getCurrentTime() {
        return this.currentTime;
    }
    
    public long getTotalTime() {
        return this.totalTime;
    }
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<DTVPlaybackParams>() {
            public DTVPlaybackParams createFromParcel(final Parcel in) {
                return new DTVPlaybackParams(in);
            }
            
            public DTVPlaybackParams[] newArray(final int size) {
                return new DTVPlaybackParams[size];
            }
        };
    }
}
