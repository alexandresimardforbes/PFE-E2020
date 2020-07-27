// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class TVMessage implements Parcelable
{
    public static final int TYPE_PROGRAM_BLOCK = 1;
    public static final int TYPE_PROGRAM_UNBLOCK = 2;
    public static final int TYPE_SIGNAL_LOST = 3;
    public static final int TYPE_SIGNAL_RESUME = 4;
    public static final int TYPE_DATA_LOST = 5;
    public static final int TYPE_DATA_RESUME = 6;
    public static final int TYPE_BOOKING_REMIND = 7;
    public static final int TYPE_BOOKING_START = 8;
    public static final int TYPE_CONFIG_CHANGED = 9;
    public static final int TYPE_SCAN_PROGRESS = 10;
    public static final int TYPE_SCAN_STORE_BEGIN = 11;
    public static final int TYPE_SCAN_STORE_END = 12;
    public static final int TYPE_SCAN_END = 13;
    public static final int TYPE_PROGRAM_UPDATE = 14;
    public static final int TYPE_PROGRAM_START = 15;
    public static final int TYPE_PROGRAM_STOP = 16;
    public static final int TYPE_TIME_UPDATE = 17;
    public static final int TYPE_EVENT_UPDATE = 18;
    public static final int TYPE_INPUT_SOURCE_CHANGED = 19;
    public static final int TYPE_PROGRAM_NUMBER = 20;
    public static final int TYPE_RECORDS_UPDATE = 21;
    public static final int TYPE_RECORD_CONFLICT = 22;
    public static final int TYPE_RECORD_END = 23;
    public static final int TYPE_VGA_ADJUST_OK = 24;
    public static final int TYPE_VGA_ADJUST_FAILED = 25;
    public static final int TYPE_VGA_ADJUST_DOING = 26;
    public static final int TYPE_SIG_CHANGE = 27;
    public static final int TYPE_BLINDSCAN_PROGRESS = 28;
    public static final int TYPE_BLINDSCAN_NEWCHANNEL = 29;
    public static final int TYPE_BLINDSCAN_END = 30;
    public static final int TYPE_SEC_LNBSSWITCHCFGVALID = 31;
    public static final int TYPE_SEC_POSITIONERSTOP = 32;
    public static final int TYPE_SEC_POSITIONERDISABLELIMIT = 33;
    public static final int TYPE_SEC_POSITIONEREASTLIMIT = 34;
    public static final int TYPE_SEC_POSITIONERWESTLIMIT = 35;
    public static final int TYPE_SEC_POSITIONEREAST = 36;
    public static final int TYPE_SEC_POSITIONERWEST = 37;
    public static final int TYPE_SEC_POSITIONERSTORE = 38;
    public static final int TYPE_SEC_POSITIONERGOTO = 39;
    public static final int TYPE_SEC_POSITIONERGOTOX = 40;
    public static final int TYPE_PROGRAM_SWITCH = 41;
    public static final int TYPE_SCAN_DTV_CHANNEL = 42;
    public static final int TYPE_TRANSFORM_DB_START = 43;
    public static final int TYPE_TRANSFORM_DB_END = 44;
    public static final int TYPE_PLAYBACK_START = 45;
    public static final int TYPE_PLAYBACK_STOP = 46;
    public static final int TYPE_PROGRAM_SCRAMBLED = 47;
    public static final int TYPE_SCREEN_OFF = 48;
    public static final int TYPE_SCREEN_ON = 49;
    public static final int TYPE_NIT_TABLE_VER_CHANGED = 50;
    public static final int TYPE_AUDIO_AC3_NO_LICENCE = 51;
    public static final int TYPE_AUDIO_AC3_LICENCE_RESUME = 52;
    public static final int TYPE_USB_ERROR = 53;
    public static final int TYPE_RESTORE_FACTORY_END = 54;
    public static final int TYPE_START_RECORD = 55;
    private static final String TAG = "TVMessage";
    private int type;
    private int programID;
    private int channelID;
    private int bookingID;
    private int programType;
    private TVProgramNumber programNo;
    private String cfgName;
    private TVConfigValue cfgValue;
    private int scanProgress;
    private int scanTotalChanCount;
    private int scanCurChanNo;
    private TVChannelParams scanCurChanParams;
    private int scanCurChanLocked;
    private String scanProgramName;
    private int scanProgramType;
    private String scanMsg;
    private int inputSource;
    private TvinInfo tvin_info;
    private int parentalRating;
    private String vchipDimension;
    private String vchipAbbrev;
    private String vchipText;
    private TVChannelParams secCurParams;
    private int secPositionerMoveUnit;
    private DTVRecordParams recordParams;
    private int reserved;
    private int errorCode;
    public static final int REC_ERR_NONE = 0;
    public static final int REC_ERR_OPEN_FILE = 1;
    public static final int REC_ERR_WRITE_FILE = 2;
    public static final int REC_ERR_ACCESS_FILE = 3;
    public static final int REC_ERR_SYSTEM = 4;
    public static final int TRANSDB_ERR_NONE = 0;
    public static final int TRANSDB_ERR_INVALID_FILE = 1;
    public static final int TRANSDB_ERR_SYSTEM = 2;
    private int recordConflict;
    public static final int REC_CFLT_START_NEW = 0;
    public static final int REC_CFLT_START_TIMESHIFT = 2;
    public static final int REC_CFLT_SWITCH_PROGRAM = 3;
    private int programBlockType;
    public static final int BLOCK_BY_LOCK = 0;
    public static final int BLOCK_BY_PARENTAL_CONTROL = 1;
    public static final int BLOCK_BY_VCHIP = 2;
    private int flags;
    private static final int FLAG_PROGRAM_ID = 1;
    private static final int FLAG_CHANNEL_ID = 2;
    private static final int FLAG_BOOKING_ID = 4;
    private static final int FLAG_CONFIG = 8;
    private static final int FLAG_SCAN = 16;
    private static final int FLAG_INPUT_SOURCE = 32;
    private static final int FLAG_PROGRAM_NUMBER = 64;
    private static final int FLAG_RECORD_CONFLICT = 128;
    private static final int FLAG_PROGRAM_BLOCK = 256;
    private static final int FLAG_ERROR_CODE = 512;
    private static final int FLAG_SEC = 1024;
    private static final int FLAG_RECORD_PARAM = 2048;
    public static final Parcelable.Creator<TVMessage> CREATOR;

    public void readFromParcel(final Parcel in) {
        this.type = in.readInt();
        this.flags = in.readInt();
        if ((this.flags & 0x1) != 0x0) {
            this.programID = in.readInt();
        }
        if ((this.flags & 0x2) != 0x0) {
            this.channelID = in.readInt();
        }
        if ((this.flags & 0x4) != 0x0) {
            this.bookingID = in.readInt();
        }
        if ((this.flags & 0x40) != 0x0) {
            this.programNo = new TVProgramNumber(in);
            this.programType = in.readInt();
        }
        if ((this.flags & 0x8) != 0x0) {
            this.cfgName = in.readString();
            this.cfgValue = new TVConfigValue(in);
        }
        if ((this.flags & 0x20) != 0x0) {
            this.inputSource = in.readInt();
        }
        if ((this.flags & 0x10) != 0x0 && this.type == 10) {
            this.scanProgress = in.readInt();
            this.scanTotalChanCount = in.readInt();
            this.scanCurChanNo = in.readInt();
            this.scanCurChanParams = new TVChannelParams(in);
            this.scanCurChanLocked = in.readInt();
            this.scanProgramName = in.readString();
            this.scanProgramType = in.readInt();
        }
        else if ((this.flags & 0x10) != 0x0 && this.type == 28) {
            this.scanProgress = in.readInt();
            this.scanMsg = in.readString();
        }
        else if ((this.flags & 0x10) != 0x0 && this.type == 29) {
            this.scanCurChanParams = new TVChannelParams(in);
        }
        else if ((this.flags & 0x10) != 0x0 && this.type == 42) {
            this.scanCurChanNo = in.readInt();
        }
        if ((this.flags & 0x80) != 0x0) {
            this.recordConflict = in.readInt();
        }
        if ((this.flags & 0x200) != 0x0) {
            this.errorCode = in.readInt();
        }
        if ((this.flags & 0x100) != 0x0) {
            this.programBlockType = in.readInt();
            this.parentalRating = in.readInt();
            this.vchipDimension = in.readString();
            this.vchipAbbrev = in.readString();
            this.vchipText = in.readString();
        }
        if ((this.flags & 0x400) != 0x0) {
            if (this.type == 31 || this.type == 36 || this.type == 37 || this.type == 38 || this.type == 39 || this.type == 40) {
                this.secCurParams = new TVChannelParams(in);
            }
            if (this.type == 36 || this.type == 37) {
                this.secPositionerMoveUnit = in.readInt();
            }
        }
        if ((this.flags & 0x800) != 0x0) {
            this.recordParams = new DTVRecordParams(in);
        }
        this.reserved = in.readInt();
    }

    public void writeToParcel(final Parcel dest, final int flag) {
        dest.writeInt(this.type);
        dest.writeInt(this.flags);
        if ((this.flags & 0x1) != 0x0) {
            dest.writeInt(this.programID);
        }
        if ((this.flags & 0x2) != 0x0) {
            dest.writeInt(this.channelID);
        }
        if ((this.flags & 0x4) != 0x0) {
            dest.writeInt(this.bookingID);
        }
        if ((this.flags & 0x40) != 0x0) {
            this.programNo.writeToParcel(dest, flag);
            dest.writeInt(this.programType);
        }
        if ((this.flags & 0x8) != 0x0) {
            dest.writeString(this.cfgName);
            this.cfgValue.writeToParcel(dest, flag);
        }
        if ((this.flags & 0x20) != 0x0) {
            dest.writeInt(this.inputSource);
        }
        if ((this.flags & 0x10) != 0x0 && this.type == 10) {
            dest.writeInt(this.scanProgress);
            dest.writeInt(this.scanTotalChanCount);
            dest.writeInt(this.scanCurChanNo);
            this.scanCurChanParams.writeToParcel(dest, flag);
            dest.writeInt(this.scanCurChanLocked);
            dest.writeString(this.scanProgramName);
            dest.writeInt(this.scanProgramType);
        }
        else if ((this.flags & 0x10) != 0x0 && this.type == 28) {
            dest.writeInt(this.scanProgress);
            dest.writeString(this.scanMsg);
        }
        else if ((this.flags & 0x10) != 0x0 && this.type == 29) {
            this.scanCurChanParams.writeToParcel(dest, flag);
        }
        else if ((this.flags & 0x10) != 0x0 && this.type == 42) {
            dest.writeInt(this.scanCurChanNo);
        }
        if ((this.flags & 0x80) != 0x0) {
            dest.writeInt(this.recordConflict);
        }
        if ((this.flags & 0x200) != 0x0) {
            dest.writeInt(this.errorCode);
        }
        if ((this.flags & 0x100) != 0x0) {
            dest.writeInt(this.programBlockType);
            dest.writeInt(this.parentalRating);
            dest.writeString(this.vchipDimension);
            dest.writeString(this.vchipAbbrev);
            dest.writeString(this.vchipText);
        }
        if ((this.flags & 0x400) != 0x0) {
            if (this.type == 31 || this.type == 36 || this.type == 37 || this.type == 38 || this.type == 39 || this.type == 40) {
                this.secCurParams.writeToParcel(dest, flag);
            }
            if (this.type == 36 || this.type == 37) {
                dest.writeInt(this.secPositionerMoveUnit);
            }
        }
        if ((this.flags & 0x800) != 0x0) {
            this.recordParams.writeToParcel(dest, flag);
        }
        dest.writeInt(this.reserved);
    }

    public TVMessage(final Parcel in) {
        this.reserved = -1;
        this.readFromParcel(in);
    }

    public TVMessage() {
        this.reserved = -1;
    }

    public TVMessage(final int type) {
        this.reserved = -1;
        this.type = type;
    }

    public TVMessage(final int type, final int reserved) {
        this.reserved = -1;
        this.type = type;
        this.reserved = reserved;
    }

    public int getType() {
        return this.type;
    }

    public int getReservedValue() {
        return this.reserved;
    }

    public int getSource() {
        return this.inputSource;
    }

    public int getProgramID() {
        if ((this.flags & 0x1) != 0x1) {
            throw new UnsupportedOperationException();
        }
        return this.programID;
    }

    public TVProgramNumber getProgramNumber() {
        if ((this.flags & 0x40) != 0x40) {
            throw new UnsupportedOperationException();
        }
        return this.programNo;
    }

    public int getProgramType() {
        if ((this.flags & 0x40) == 0x40 || (this.flags & 0x10) == 0x10) {
            return this.programType;
        }
        throw new UnsupportedOperationException();
    }

    public int getChannelID() {
        if ((this.flags & 0x2) != 0x2) {
            throw new UnsupportedOperationException();
        }
        return this.channelID;
    }

    public int getBookingID() {
        if ((this.flags & 0x4) != 0x4) {
            throw new UnsupportedOperationException();
        }
        return this.bookingID;
    }

    public int getScanProgress() {
        if ((this.flags & 0x10) != 0x10) {
            throw new UnsupportedOperationException();
        }
        return this.scanProgress;
    }

    public int getScanTotalChanCount() {
        if ((this.flags & 0x10) != 0x10) {
            throw new UnsupportedOperationException();
        }
        return this.scanTotalChanCount;
    }

    public int getScanCurChanNo() {
        if ((this.flags & 0x10) != 0x10) {
            throw new UnsupportedOperationException();
        }
        return this.scanCurChanNo;
    }

    public TVChannelParams getScanCurChanParams() {
        if ((this.flags & 0x10) != 0x10) {
            throw new UnsupportedOperationException();
        }
        return this.scanCurChanParams;
    }

    public int getScanCurChanLockStatus() {
        if ((this.flags & 0x10) != 0x10) {
            throw new UnsupportedOperationException();
        }
        return this.scanCurChanLocked;
    }

    public String getScanProgramName() {
        if ((this.flags & 0x10) != 0x10) {
            throw new UnsupportedOperationException();
        }
        return this.scanProgramName;
    }

    public int getScanProgramType() {
        if ((this.flags & 0x10) != 0x10) {
            throw new UnsupportedOperationException();
        }
        return this.scanProgramType;
    }

    public String getScanMsg() {
        if ((this.flags & 0x10) != 0x10) {
            throw new UnsupportedOperationException();
        }
        return this.scanMsg;
    }

    public int getRecordConflict() {
        if ((this.flags & 0x80) != 0x80) {
            throw new UnsupportedOperationException();
        }
        return this.recordConflict;
    }

    public int getErrorCode() {
        if ((this.flags & 0x200) != 0x200) {
            throw new UnsupportedOperationException();
        }
        return this.errorCode;
    }

    public String getConfigName() {
        if ((this.flags & 0x8) != 0x8) {
            throw new UnsupportedOperationException();
        }
        return this.cfgName;
    }

    public TVConfigValue getConfigValue() {
        if ((this.flags & 0x8) != 0x8) {
            throw new UnsupportedOperationException();
        }
        return this.cfgValue;
    }

    public int getProgramBlockType() {
        if ((this.flags & 0x100) != 0x100) {
            throw new UnsupportedOperationException();
        }
        return this.programBlockType;
    }

    public int getParentalRating() {
        if ((this.flags & 0x100) != 0x100) {
            throw new UnsupportedOperationException();
        }
        return this.parentalRating;
    }

    public String getVChipDimension() {
        if ((this.flags & 0x100) != 0x100) {
            throw new UnsupportedOperationException();
        }
        return this.vchipDimension;
    }

    public String getVChipAbbrev() {
        if ((this.flags & 0x100) != 0x100) {
            throw new UnsupportedOperationException();
        }
        return this.vchipAbbrev;
    }

    public String getVChipValueText() {
        if ((this.flags & 0x100) != 0x100) {
            throw new UnsupportedOperationException();
        }
        return this.vchipText;
    }

    public TVChannelParams getSecCurChanParams() {
        if ((this.flags & 0x400) != 0x400) {
            throw new UnsupportedOperationException();
        }
        return this.secCurParams;
    }

    public int getSecPositionerMoveUnit() {
        if ((this.flags & 0x400) != 0x400) {
            throw new UnsupportedOperationException();
        }
        return this.secPositionerMoveUnit;
    }

    public DTVRecordParams getPlaybackMediaInfo() {
        if ((this.flags & 0x800) != 0x800) {
            throw new UnsupportedOperationException();
        }
        return this.recordParams;
    }

    public static TVMessage programBlock(final int programID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 257;
        msg.type = 1;
        msg.programBlockType = 0;
        msg.programID = programID;
        return msg;
    }

    public static TVMessage programBlock(final int programID, final int parentalRating) {
        final TVMessage msg = new TVMessage();
        msg.flags = 257;
        msg.type = 1;
        msg.programBlockType = 1;
        msg.programID = programID;
        msg.parentalRating = parentalRating;
        return msg;
    }

    public static TVMessage programBlock(final int programID, final String dimension, final String ratingAbbrev, final String ratingText) {
        final TVMessage msg = new TVMessage();
        msg.flags = 257;
        msg.type = 1;
        msg.programBlockType = 2;
        msg.programID = programID;
        msg.vchipDimension = dimension;
        msg.vchipAbbrev = ratingAbbrev;
        msg.vchipText = ratingText;
        return msg;
    }

    public static TVMessage programUnblock(final int programID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 1;
        msg.type = 2;
        msg.programID = programID;
        return msg;
    }

    public static TVMessage programUpdate(final int programID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 1;
        msg.type = 14;
        msg.programID = programID;
        return msg;
    }

    public static TVMessage programStart(final int programID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 1;
        msg.type = 15;
        msg.programID = programID;
        return msg;
    }

    public static TVMessage programStop(final int programID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 1;
        msg.type = 16;
        msg.programID = programID;
        return msg;
    }

    public static TVMessage programNumber(final int type, final TVProgramNumber no) {
        final TVMessage msg = new TVMessage();
        msg.flags = 64;
        msg.type = 20;
        msg.programType = type;
        msg.programNo = no;
        return msg;
    }

    public static TVMessage programSwitch(final int programID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 1;
        msg.type = 41;
        msg.programID = programID;
        return msg;
    }

    public static TVMessage usbLost() {
        final TVMessage msg = new TVMessage();
        msg.type = 53;
        return msg;
    }

    public static TVMessage signalLost(final int channelID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 2;
        msg.type = 3;
        msg.channelID = channelID;
        return msg;
    }

    public static TVMessage signalResume(final int channelID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 2;
        msg.type = 4;
        msg.channelID = channelID;
        return msg;
    }

    public static TVMessage dataLost(final int programID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 1;
        msg.type = 5;
        msg.programID = programID;
        return msg;
    }

    public static TVMessage ac3NoLience(final int programID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 1;
        msg.type = 51;
        msg.programID = programID;
        return msg;
    }

    public static TVMessage ac3LienceResume(final int programID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 1;
        msg.type = 52;
        msg.programID = programID;
        return msg;
    }

    public static TVMessage dataResume(final int programID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 1;
        msg.type = 6;
        msg.programID = programID;
        return msg;
    }

    public static TVMessage programScrambled(final int programID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 1;
        msg.type = 47;
        msg.programID = programID;
        return msg;
    }

    public static TVMessage bookingRemind(final int bookingID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 4;
        msg.type = 7;
        msg.bookingID = bookingID;
        return msg;
    }

    public static TVMessage bookingStart(final int bookingID) {
        final TVMessage msg = new TVMessage();
        msg.flags = 4;
        msg.type = 8;
        msg.bookingID = bookingID;
        return msg;
    }

    public static TVMessage configChanged(final String name, final TVConfigValue value) {
        final TVMessage msg = new TVMessage();
        msg.flags = 8;
        msg.type = 9;
        msg.cfgName = name;
        msg.cfgValue = value;
        return msg;
    }

    public static TVMessage scanUpdate(final int progressVal, final int curChan, final int totalChan, final TVChannelParams curChanParam, final int lockStatus, final String programName, final int programType) {
        final TVMessage msg = new TVMessage();
        msg.flags = 16;
        msg.type = 10;
        msg.scanProgress = progressVal;
        msg.scanCurChanNo = curChan;
        msg.scanTotalChanCount = totalChan;
        msg.scanCurChanParams = curChanParam;
        msg.scanCurChanLocked = lockStatus;
        msg.scanProgramName = programName;
        msg.scanProgramType = programType;
        return msg;
    }

    public static TVMessage scanStoreBegin() {
        final TVMessage msg = new TVMessage();
        msg.flags = 16;
        msg.type = 11;
        return msg;
    }

    public static TVMessage scanStoreEnd() {
        final TVMessage msg = new TVMessage();
        msg.flags = 16;
        msg.type = 12;
        return msg;
    }

    public static TVMessage scanEnd() {
        final TVMessage msg = new TVMessage();
        msg.flags = 16;
        msg.type = 13;
        return msg;
    }

    public static TVMessage blindScanProgressUpdate(final int progressVal, final String scanMsg) {
        final TVMessage msg = new TVMessage();
        msg.flags = 16;
        msg.type = 28;
        msg.scanProgress = progressVal;
        msg.scanMsg = scanMsg;
        return msg;
    }

    public static TVMessage blindScanNewChannelUpdate(final TVChannelParams curChanParam) {
        final TVMessage msg = new TVMessage();
        msg.flags = 16;
        msg.type = 29;
        msg.scanCurChanParams = curChanParam;
        return msg;
    }

    public static TVMessage blindScanEnd() {
        final TVMessage msg = new TVMessage();
        msg.flags = 16;
        msg.type = 30;
        return msg;
    }

    public static TVMessage timeUpdate() {
        final TVMessage msg = new TVMessage();
        msg.type = 17;
        return msg;
    }

    public static TVMessage eventUpdate() {
        final TVMessage msg = new TVMessage();
        msg.type = 18;
        return msg;
    }

    public static TVMessage inputSourceChanged(final int src) {
        final TVMessage msg = new TVMessage();
        msg.type = 19;
        msg.flags = 32;
        msg.inputSource = src;
        return msg;
    }

    public static TVMessage recordsUpdate() {
        final TVMessage msg = new TVMessage();
        msg.type = 21;
        return msg;
    }

    public static TVMessage recordConflict(final int conflict, final int newRecordProgramID) {
        final TVMessage msg = new TVMessage();
        msg.type = 22;
        msg.flags = 129;
        msg.recordConflict = conflict;
        msg.programID = newRecordProgramID;
        return msg;
    }

    public static TVMessage recordEnd(final int errCode) {
        final TVMessage msg = new TVMessage();
        msg.type = 23;
        msg.flags = 512;
        msg.errorCode = errCode;
        return msg;
    }

    public static TVMessage sigChange(final TvinInfo tvin_info) {
        final TVMessage msg = new TVMessage();
        msg.type = 27;
        msg.tvin_info = tvin_info;
        if (tvin_info == null) {
            Log.d("TVMessage", "*************tvin_info is null TVMessage************");
        }
        return msg;
    }

    public static TVMessage scanDTVChannelStart(final int channelNo) {
        final TVMessage msg = new TVMessage();
        msg.flags = 16;
        msg.type = 42;
        msg.scanCurChanNo = channelNo;
        return msg;
    }

    public static TVMessage secRequest(final int type) {
        final TVMessage msg = new TVMessage();
        msg.flags = 1024;
        msg.type = type;
        return msg;
    }

    public static TVMessage secRequest(final int type, final TVChannelParams seccurparams) {
        final TVMessage msg = new TVMessage();
        msg.flags = 1024;
        msg.type = type;
        msg.secCurParams = seccurparams;
        return msg;
    }

    public static TVMessage secRequest(final int type, final TVChannelParams seccurparams, final int secpositionermoveunit) {
        final TVMessage msg = new TVMessage();
        msg.flags = 1024;
        msg.type = type;
        msg.secCurParams = seccurparams;
        msg.secPositionerMoveUnit = secpositionermoveunit;
        return msg;
    }

    public static TVMessage transformDBStart() {
        final TVMessage msg = new TVMessage();
        msg.flags = 0;
        msg.type = 43;
        return msg;
    }

    public static TVMessage transformDBEnd(final int errCode) {
        final TVMessage msg = new TVMessage();
        msg.flags = 512;
        msg.type = 44;
        msg.errorCode = errCode;
        return msg;
    }

    public static TVMessage playbackStart(final DTVRecordParams info) {
        final TVMessage msg = new TVMessage();
        msg.flags = 2048;
        msg.type = 45;
        msg.recordParams = info;
        return msg;
    }

    public static TVMessage playbackStop() {
        final TVMessage msg = new TVMessage();
        msg.type = 46;
        return msg;
    }

    public static TVMessage restoreFactoryEnd() {
        final TVMessage msg = new TVMessage();
        msg.type = 54;
        return msg;
    }

    public static TVMessage startRecordByPhone() {
        final TVMessage msg = new TVMessage();
        msg.type = 55;
        return msg;
    }

    public int describeContents() {
        return 0;
    }

    public static Parcelable.Creator<TVMessage> getCreator() {
        return TVMessage.CREATOR;
    }

    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<TVMessage>() {
            public TVMessage createFromParcel(final Parcel in) {
                return new TVMessage(in);
            }
            
            public TVMessage[] newArray(final int size) {
                return new TVMessage[size];
            }
        };
    }
}
