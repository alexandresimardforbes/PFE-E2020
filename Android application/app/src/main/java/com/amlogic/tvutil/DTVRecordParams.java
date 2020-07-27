// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.os.Parcel;
import android.os.Parcelable;

public class DTVRecordParams implements Parcelable
{
    private static final String TAG = "DTVRecordParams";
    private String recFilePath;
    private String storagePath;
    private String prefixFileName;
    private String suffixFileName;
    private String programName;
    private int programID;
    private int pmtPID;
    private int pmtProgramNumber;
    private long currRecordSize;
    private long currRecordTime;
    private long recTotalTime;
    private boolean isTimeshift;
    private boolean isRecTest;
    private TVProgram.Video video;
    private TVProgram.Audio[] audios;
    private TVProgram.Subtitle[] subtitles;
    private TVProgram.Teletext[] teletexts;
    public static final Parcelable.Creator<DTVRecordParams> CREATOR;
    
    public void readFromParcel(final Parcel in) {
        this.currRecordSize = in.readLong();
        this.currRecordTime = in.readLong();
        this.recTotalTime = in.readLong();
        this.programID = in.readInt();
        this.programName = in.readString();
        this.recFilePath = in.readString();
        this.isTimeshift = (in.readInt() != 0);
        this.isRecTest = (in.readInt() != 0);
        final TVProgram p = new TVProgram();
        this.video = null;
        int cnt = in.readInt();
        if (cnt > 0) {
            final int pid = in.readInt();
            final int fmt = in.readInt();
            this.video = p.new Video(pid, fmt);
        }
        this.audios = null;
        cnt = in.readInt();
        if (cnt > 0) {
            this.audios = new TVProgram.Audio[cnt];
            for (int i = 0; i < cnt; ++i) {
                final int pid = in.readInt();
                final int fmt = in.readInt();
                final String lang = in.readString();
                this.audios[i] = p.new Audio(pid, lang, fmt);
            }
        }
        this.subtitles = null;
        cnt = in.readInt();
        if (cnt > 0) {
            this.subtitles = new TVProgram.Subtitle[cnt];
            for (int j = 0; j < cnt; ++j) {
                final int pid = in.readInt();
                final int type = in.readInt();
                final int num1 = in.readInt();
                final int num2 = in.readInt();
                final String lang = in.readString();
                this.subtitles[j] = p.new Subtitle(pid, lang, type, num1, num2);
            }
        }
        this.teletexts = null;
        cnt = in.readInt();
        if (cnt > 0) {
            this.teletexts = new TVProgram.Teletext[cnt];
            for (int k = 0; k < cnt; ++k) {
                final int pid = in.readInt();
                final int mag = in.readInt();
                final int page = in.readInt();
                final String lang = in.readString();
                this.teletexts[k] = p.new Teletext(pid, lang, mag, page);
            }
        }
    }
    
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeLong(this.currRecordSize);
        dest.writeLong(this.currRecordTime);
        dest.writeLong(this.recTotalTime);
        dest.writeInt(this.programID);
        dest.writeString(this.programName);
        dest.writeString(this.recFilePath);
        dest.writeInt((int)(this.isTimeshift ? 1 : 0));
        dest.writeInt((int)(this.isRecTest ? 1 : 0));
        if (this.video != null) {
            dest.writeInt(1);
            dest.writeInt(this.video.getPID());
            dest.writeInt(this.video.getFormat());
        }
        else {
            dest.writeInt(0);
        }
        if (this.audios != null) {
            dest.writeInt(this.audios.length);
            for (int i = 0; i < this.audios.length; ++i) {
                dest.writeInt(this.audios[i].getPID());
                dest.writeInt(this.audios[i].getFormat());
                dest.writeString(this.audios[i].getLang());
            }
        }
        else {
            dest.writeInt(0);
        }
        if (this.subtitles != null) {
            dest.writeInt(this.subtitles.length);
            for (int i = 0; i < this.subtitles.length; ++i) {
                dest.writeInt(this.subtitles[i].getPID());
                dest.writeInt(this.subtitles[i].getType());
                if (this.subtitles[i].getType() == 1) {
                    dest.writeInt(this.subtitles[i].getCompositionPageID());
                    dest.writeInt(this.subtitles[i].getAncillaryPageID());
                }
                else if (this.subtitles[i].getType() == 2) {
                    dest.writeInt(this.subtitles[i].getMagazineNumber());
                    dest.writeInt(this.subtitles[i].getPageNumber());
                }
                else {
                    dest.writeInt(0);
                    dest.writeInt(0);
                }
                dest.writeString(this.subtitles[i].getLang());
            }
        }
        else {
            dest.writeInt(0);
        }
        if (this.teletexts != null) {
            dest.writeInt(this.teletexts.length);
            for (int i = 0; i < this.teletexts.length; ++i) {
                dest.writeInt(this.teletexts[i].getPID());
                dest.writeInt(this.teletexts[i].getMagazineNumber());
                dest.writeInt(this.teletexts[i].getPageNumber());
                dest.writeString(this.teletexts[i].getLang());
            }
        }
        else {
            dest.writeInt(0);
        }
    }
    
    public DTVRecordParams(final Parcel in) {
        this.readFromParcel(in);
    }
    
    public DTVRecordParams() {
    }
    
    public DTVRecordParams(final TVBooking book, final String storagePath, final String prefixName, final String suffixName, final boolean isTimeshift, final boolean isRecTest) {
        this.storagePath = storagePath;
        final TVProgram prog = book.getProgram();
        if (prog == null) {
            this.video = null;
            this.audios = null;
            this.subtitles = null;
            this.teletexts = null;
            this.programID = -1;
            this.pmtPID = 8191;
            this.pmtProgramNumber = 65535;
            this.programName = null;
        }
        else {
            this.video = prog.getVideo();
            this.audios = prog.getAllAudio();
            this.subtitles = prog.getAllSubtitle();
            this.teletexts = prog.getAllTeletext();
            this.programID = prog.getID();
            this.pmtPID = prog.getPmtPID();
            this.pmtProgramNumber = prog.getDVBServiceID();
            this.programName = prog.getName();
        }
        this.recTotalTime = book.getDuration();
        this.isTimeshift = isTimeshift;
        this.isRecTest = isRecTest;
        this.currRecordSize = 0L;
        this.currRecordTime = 0L;
        this.prefixFileName = prefixName;
        this.suffixFileName = suffixName;
    }
    
    public DTVRecordParams(final int pmtPid, final int vpid, final int vfmt, final int apid, final int afmt, final String storagePath, final String prefixName, final int duration) {
        this.storagePath = storagePath;
        final TVProgram prog = new TVProgram(vpid, vfmt, apid, afmt);
        this.video = prog.getVideo();
        this.audios = prog.getAllAudio();
        this.subtitles = null;
        this.teletexts = null;
        this.programID = -1;
        this.pmtPID = pmtPid;
        this.pmtProgramNumber = 8191;
        this.programName = null;
        this.recTotalTime = duration;
        this.isTimeshift = false;
        this.isRecTest = true;
        this.currRecordSize = 0L;
        this.currRecordTime = 0L;
        this.prefixFileName = prefixName;
        this.suffixFileName = "ts";
    }
    
    public int describeContents() {
        return 0;
    }
    
    public static Parcelable.Creator<DTVRecordParams> getCreator() {
        return DTVRecordParams.CREATOR;
    }
    
    public long getCurrentRecordTime() {
        return this.currRecordTime;
    }
    
    public long getTotalRecordTime() {
        return this.recTotalTime;
    }
    
    public long getCurrentRecordSize() {
        return this.currRecordSize;
    }
    
    public String getRecordFilePath() {
        return this.recFilePath;
    }
    
    public int getProgramID() {
        return this.programID;
    }
    
    public String getProgramName() {
        return this.programName;
    }
    
    public TVProgram.Video getVideo() {
        return this.video;
    }
    
    public TVProgram.Audio[] getAllAudio() {
        return this.audios;
    }
    
    public TVProgram.Subtitle[] getAllSubtitle() {
        return this.subtitles;
    }
    
    public TVProgram.Teletext[] getAllTeletext() {
        return this.teletexts;
    }
    
    public boolean getTimeshiftMode() {
        return this.isTimeshift;
    }
    
    public void setProgramID(final int id) {
        this.programID = id;
    }
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<DTVRecordParams>() {
            public DTVRecordParams createFromParcel(final Parcel in) {
                return new DTVRecordParams(in);
            }
            
            public DTVRecordParams[] newArray(final int size) {
                return new DTVRecordParams[size];
            }
        };
    }
}
