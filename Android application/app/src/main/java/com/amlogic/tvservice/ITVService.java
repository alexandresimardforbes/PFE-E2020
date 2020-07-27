// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.amlogic.tvutil.DTVPlaybackParams;
import com.amlogic.tvutil.DTVRecordParams;
import com.amlogic.tvutil.ITVCallback;
import com.amlogic.tvutil.TVChannelParams;
import com.amlogic.tvutil.TVConfigValue;
import com.amlogic.tvutil.TVMessage;
import com.amlogic.tvutil.TVPlayParams;
import com.amlogic.tvutil.TVScanParams;
import com.amlogic.tvutil.TVStatus;
import com.amlogic.tvutil.TvinInfo;

public interface ITVService extends IInterface
{
    TVStatus getStatus() throws RemoteException;
    
    void registerCallback(final ITVCallback p0) throws RemoteException;
    
    void unregisterCallback(final ITVCallback p0) throws RemoteException;
    
    void setConfig(final String p0, final TVConfigValue p1) throws RemoteException;
    
    TVConfigValue getConfig(final String p0) throws RemoteException;
    
    void registerConfigCallback(final String p0, final ITVCallback p1) throws RemoteException;
    
    void unregisterConfigCallback(final String p0, final ITVCallback p1) throws RemoteException;
    
    long getTime() throws RemoteException;
    
    void switchAudioTrack(final int p0) throws RemoteException;
    
    void setInputSource(final int p0) throws RemoteException;
    
    int getCurInputSource() throws RemoteException;
    
    void setProgramType(final int p0) throws RemoteException;
    
    void setVideoWindow(final int p0, final int p1, final int p2, final int p3) throws RemoteException;
    
    void playProgram(final TVPlayParams p0) throws RemoteException;
    
    void stopPlaying() throws RemoteException;
    
    void switchAudio(final int p0) throws RemoteException;
    
    void resetATVFormat() throws RemoteException;
    
    void startTimeshifting() throws RemoteException;
    
    void stopTimeshifting() throws RemoteException;
    
    void startRecording(final long p0) throws RemoteException;
    
    void startTestRecording(final long p0) throws RemoteException;
    
    void startRecordingByBack(final int p0) throws RemoteException;
    
    void stopRecording() throws RemoteException;
    
    void setFrontendByDtvView(final int p0) throws RemoteException;
    
    DTVRecordParams getRecordingParams() throws RemoteException;
    
    void startPlayback(final String p0) throws RemoteException;
    
    void stopPlayback() throws RemoteException;
    
    DTVPlaybackParams getPlaybackParams() throws RemoteException;
    
    void startScan(final TVScanParams p0) throws RemoteException;
    
    void stopScan(final boolean p0) throws RemoteException;
    
    void startBooking(final int p0) throws RemoteException;
    
    void pause() throws RemoteException;
    
    void resume() throws RemoteException;
    
    void fastForward(final int p0) throws RemoteException;
    
    void fastBackward(final int p0) throws RemoteException;
    
    void seekTo(final int p0) throws RemoteException;
    
    int getFrontendStatus() throws RemoteException;
    
    int getFrontendSignalStrength() throws RemoteException;
    
    int getFrontendSNR() throws RemoteException;
    
    int getFrontendBER() throws RemoteException;
    
    void fineTune(final int p0) throws RemoteException;
    
    void restoreFactorySetting(final int p0) throws RemoteException;
    
    void playValid() throws RemoteException;
    
    void setVGAAutoAdjust() throws RemoteException;
    
    int GetSrcInputType() throws RemoteException;
    
    TvinInfo getCurrentSignalInfo() throws RemoteException;
    
    void replay() throws RemoteException;
    
    void unblock() throws RemoteException;
    
    void lock(final TVChannelParams p0) throws RemoteException;
    
    void setCvbsAmpOut(final int p0) throws RemoteException;
    
    void secRequest(final TVMessage p0) throws RemoteException;
    
    void switch_video_blackout(final int p0) throws RemoteException;
    
    void importDatabase(final String p0) throws RemoteException;
    
    void exportDatabase(final String p0) throws RemoteException;
    
    void playIptv(final int p0, final int p1, final int p2, final int p3, final int p4) throws RemoteException;
    
    int getDTVType() throws RemoteException;
    
    void tryRestDevice() throws RemoteException;
    
    void importdb(final String p0) throws RemoteException;
    
    void exportdb(final String p0) throws RemoteException;
    
    void startRecordById(final int p0, final int p1, final int p2, final int p3, final int p4, final String p5, final String p6, final int p7) throws RemoteException;
    
    public abstract static class Stub extends Binder implements ITVService
    {
        private static final String DESCRIPTOR = "com.amlogic.tvservice.ITVService";
        static final int TRANSACTION_getStatus = 1;
        static final int TRANSACTION_registerCallback = 2;
        static final int TRANSACTION_unregisterCallback = 3;
        static final int TRANSACTION_setConfig = 4;
        static final int TRANSACTION_getConfig = 5;
        static final int TRANSACTION_registerConfigCallback = 6;
        static final int TRANSACTION_unregisterConfigCallback = 7;
        static final int TRANSACTION_getTime = 8;
        static final int TRANSACTION_switchAudioTrack = 9;
        static final int TRANSACTION_setInputSource = 10;
        static final int TRANSACTION_getCurInputSource = 11;
        static final int TRANSACTION_setProgramType = 12;
        static final int TRANSACTION_setVideoWindow = 13;
        static final int TRANSACTION_playProgram = 14;
        static final int TRANSACTION_stopPlaying = 15;
        static final int TRANSACTION_switchAudio = 16;
        static final int TRANSACTION_resetATVFormat = 17;
        static final int TRANSACTION_startTimeshifting = 18;
        static final int TRANSACTION_stopTimeshifting = 19;
        static final int TRANSACTION_startRecording = 20;
        static final int TRANSACTION_startTestRecording = 21;
        static final int TRANSACTION_startRecordingByBack = 22;
        static final int TRANSACTION_stopRecording = 23;
        static final int TRANSACTION_setFrontendByDtvView = 24;
        static final int TRANSACTION_getRecordingParams = 25;
        static final int TRANSACTION_startPlayback = 26;
        static final int TRANSACTION_stopPlayback = 27;
        static final int TRANSACTION_getPlaybackParams = 28;
        static final int TRANSACTION_startScan = 29;
        static final int TRANSACTION_stopScan = 30;
        static final int TRANSACTION_startBooking = 31;
        static final int TRANSACTION_pause = 32;
        static final int TRANSACTION_resume = 33;
        static final int TRANSACTION_fastForward = 34;
        static final int TRANSACTION_fastBackward = 35;
        static final int TRANSACTION_seekTo = 36;
        static final int TRANSACTION_getFrontendStatus = 37;
        static final int TRANSACTION_getFrontendSignalStrength = 38;
        static final int TRANSACTION_getFrontendSNR = 39;
        static final int TRANSACTION_getFrontendBER = 40;
        static final int TRANSACTION_fineTune = 41;
        static final int TRANSACTION_restoreFactorySetting = 42;
        static final int TRANSACTION_playValid = 43;
        static final int TRANSACTION_setVGAAutoAdjust = 44;
        static final int TRANSACTION_GetSrcInputType = 45;
        static final int TRANSACTION_getCurrentSignalInfo = 46;
        static final int TRANSACTION_replay = 47;
        static final int TRANSACTION_unblock = 48;
        static final int TRANSACTION_lock = 49;
        static final int TRANSACTION_setCvbsAmpOut = 50;
        static final int TRANSACTION_secRequest = 51;
        static final int TRANSACTION_switch_video_blackout = 52;
        static final int TRANSACTION_importDatabase = 53;
        static final int TRANSACTION_exportDatabase = 54;
        static final int TRANSACTION_playIptv = 55;
        static final int TRANSACTION_getDTVType = 56;
        static final int TRANSACTION_tryRestDevice = 57;
        static final int TRANSACTION_importdb = 58;
        static final int TRANSACTION_exportdb = 59;
        static final int TRANSACTION_startRecordById = 60;
        
        public Stub() {
            this.attachInterface((IInterface)this, "com.amlogic.tvservice.ITVService");
        }
        
        public static ITVService asInterface(final IBinder obj) {
            if (obj == null) {
                return null;
            }
            final IInterface iin = obj.queryLocalInterface("com.amlogic.tvservice.ITVService");
            if (iin != null && iin instanceof ITVService) {
                return (ITVService)iin;
            }
            return new Proxy(obj);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int code, final Parcel data, final Parcel reply, final int flags) throws RemoteException {
            switch (code) {
                case 1598968902: {
                    reply.writeString("com.amlogic.tvservice.ITVService");
                    return true;
                }
                case 1: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final TVStatus _result = this.getStatus();
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    }
                    else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 2: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final ITVCallback _arg0 = ITVCallback.Stub.asInterface(data.readStrongBinder());
                    this.registerCallback(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final ITVCallback _arg0 = ITVCallback.Stub.asInterface(data.readStrongBinder());
                    this.unregisterCallback(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 4: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final String _arg2 = data.readString();
                    TVConfigValue _arg3;
                    if (0 != data.readInt()) {
                        _arg3 = (TVConfigValue) TVConfigValue.CREATOR.createFromParcel(data);
                    }
                    else {
                        _arg3 = null;
                    }
                    this.setConfig(_arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 5: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final String _arg2 = data.readString();
                    final TVConfigValue _result2 = this.getConfig(_arg2);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    }
                    else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 6: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final String _arg2 = data.readString();
                    final ITVCallback _arg4 = ITVCallback.Stub.asInterface(data.readStrongBinder());
                    this.registerConfigCallback(_arg2, _arg4);
                    reply.writeNoException();
                    return true;
                }
                case 7: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final String _arg2 = data.readString();
                    final ITVCallback _arg4 = ITVCallback.Stub.asInterface(data.readStrongBinder());
                    this.unregisterConfigCallback(_arg2, _arg4);
                    reply.writeNoException();
                    return true;
                }
                case 8: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final long _result3 = this.getTime();
                    reply.writeNoException();
                    reply.writeLong(_result3);
                    return true;
                }
                case 9: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.switchAudioTrack(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 10: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.setInputSource(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 11: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _result4 = this.getCurInputSource();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                }
                case 12: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.setProgramType(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 13: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    final int _arg6 = data.readInt();
                    final int _arg7 = data.readInt();
                    final int _arg8 = data.readInt();
                    this.setVideoWindow(_arg5, _arg6, _arg7, _arg8);
                    reply.writeNoException();
                    return true;
                }
                case 14: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    TVPlayParams _arg9;
                    if (0 != data.readInt()) {
                        _arg9 = (TVPlayParams) TVPlayParams.CREATOR.createFromParcel(data);
                    }
                    else {
                        _arg9 = null;
                    }
                    this.playProgram(_arg9);
                    reply.writeNoException();
                    return true;
                }
                case 15: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    this.stopPlaying();
                    reply.writeNoException();
                    return true;
                }
                case 16: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.switchAudio(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 17: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    this.resetATVFormat();
                    reply.writeNoException();
                    return true;
                }
                case 18: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    this.startTimeshifting();
                    reply.writeNoException();
                    return true;
                }
                case 19: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    this.stopTimeshifting();
                    reply.writeNoException();
                    return true;
                }
                case 20: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final long _arg10 = data.readLong();
                    this.startRecording(_arg10);
                    reply.writeNoException();
                    return true;
                }
                case 21: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final long _arg10 = data.readLong();
                    this.startTestRecording(_arg10);
                    reply.writeNoException();
                    return true;
                }
                case 22: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.startRecordingByBack(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 23: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    this.stopRecording();
                    reply.writeNoException();
                    return true;
                }
                case 24: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.setFrontendByDtvView(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 25: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final DTVRecordParams _result5 = this.getRecordingParams();
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    }
                    else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 26: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final String _arg2 = data.readString();
                    this.startPlayback(_arg2);
                    reply.writeNoException();
                    return true;
                }
                case 27: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    this.stopPlayback();
                    reply.writeNoException();
                    return true;
                }
                case 28: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final DTVPlaybackParams _result6 = this.getPlaybackParams();
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                    }
                    else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 29: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    TVScanParams _arg11;
                    if (0 != data.readInt()) {
                        _arg11 = (TVScanParams) TVScanParams.CREATOR.createFromParcel(data);
                    }
                    else {
                        _arg11 = null;
                    }
                    this.startScan(_arg11);
                    reply.writeNoException();
                    return true;
                }
                case 30: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final boolean _arg12 = 0 != data.readInt();
                    this.stopScan(_arg12);
                    reply.writeNoException();
                    return true;
                }
                case 31: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.startBooking(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 32: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    this.pause();
                    reply.writeNoException();
                    return true;
                }
                case 33: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    this.resume();
                    reply.writeNoException();
                    return true;
                }
                case 34: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.fastForward(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 35: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.fastBackward(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 36: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.seekTo(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 37: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _result4 = this.getFrontendStatus();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                }
                case 38: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _result4 = this.getFrontendSignalStrength();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                }
                case 39: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _result4 = this.getFrontendSNR();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                }
                case 40: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _result4 = this.getFrontendBER();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                }
                case 41: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.fineTune(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 42: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.restoreFactorySetting(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 43: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    this.playValid();
                    reply.writeNoException();
                    return true;
                }
                case 44: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    this.setVGAAutoAdjust();
                    reply.writeNoException();
                    return true;
                }
                case 45: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _result4 = this.GetSrcInputType();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                }
                case 46: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final TvinInfo _result7 = this.getCurrentSignalInfo();
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    }
                    else {
                        reply.writeInt(0);
                    }
                    return true;
                }
                case 47: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    this.replay();
                    reply.writeNoException();
                    return true;
                }
                case 48: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    this.unblock();
                    reply.writeNoException();
                    return true;
                }
                case 49: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    TVChannelParams _arg13;
                    if (0 != data.readInt()) {
                        _arg13 = (TVChannelParams) TVChannelParams.CREATOR.createFromParcel(data);
                    }
                    else {
                        _arg13 = null;
                    }
                    this.lock(_arg13);
                    reply.writeNoException();
                    return true;
                }
                case 50: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.setCvbsAmpOut(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 51: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    TVMessage _arg14;
                    if (0 != data.readInt()) {
                        _arg14 = (TVMessage) TVMessage.CREATOR.createFromParcel(data);
                    }
                    else {
                        _arg14 = null;
                    }
                    this.secRequest(_arg14);
                    reply.writeNoException();
                    return true;
                }
                case 52: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    this.switch_video_blackout(_arg5);
                    reply.writeNoException();
                    return true;
                }
                case 53: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final String _arg2 = data.readString();
                    this.importDatabase(_arg2);
                    reply.writeNoException();
                    return true;
                }
                case 54: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final String _arg2 = data.readString();
                    this.exportDatabase(_arg2);
                    reply.writeNoException();
                    return true;
                }
                case 55: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    final int _arg6 = data.readInt();
                    final int _arg7 = data.readInt();
                    final int _arg8 = data.readInt();
                    final int _arg15 = data.readInt();
                    this.playIptv(_arg5, _arg6, _arg7, _arg8, _arg15);
                    reply.writeNoException();
                    return true;
                }
                case 56: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _result4 = this.getDTVType();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                }
                case 57: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    this.tryRestDevice();
                    reply.writeNoException();
                    return true;
                }
                case 58: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final String _arg2 = data.readString();
                    this.importdb(_arg2);
                    reply.writeNoException();
                    return true;
                }
                case 59: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final String _arg2 = data.readString();
                    this.exportdb(_arg2);
                    reply.writeNoException();
                    return true;
                }
                case 60: {
                    data.enforceInterface("com.amlogic.tvservice.ITVService");
                    final int _arg5 = data.readInt();
                    final int _arg6 = data.readInt();
                    final int _arg7 = data.readInt();
                    final int _arg8 = data.readInt();
                    final int _arg15 = data.readInt();
                    final String _arg16 = data.readString();
                    final String _arg17 = data.readString();
                    final int _arg18 = data.readInt();
                    this.startRecordById(_arg5, _arg6, _arg7, _arg8, _arg15, _arg16, _arg17, _arg18);
                    reply.writeNoException();
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }
        
        private static class Proxy implements ITVService
        {
            private IBinder mRemote;
            
            Proxy(final IBinder remote) {
                this.mRemote = remote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            public String getInterfaceDescriptor() {
                return "com.amlogic.tvservice.ITVService";
            }
            
            public TVStatus getStatus() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                TVStatus _result;
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (TVStatus) TVStatus.CREATOR.createFromParcel(_reply);
                    }
                    else {
                        _result = null;
                    }
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            public void registerCallback(final ITVCallback cb) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeStrongBinder((cb != null) ? cb.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void unregisterCallback(final ITVCallback cb) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeStrongBinder((cb != null) ? cb.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void setConfig(final String name, final TVConfigValue value) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeString(name);
                    if (value != null) {
                        _data.writeInt(1);
                        value.writeToParcel(_data, 0);
                    }
                    else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public TVConfigValue getConfig(final String name) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                TVConfigValue _result;
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeString(name);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (TVConfigValue) TVConfigValue.CREATOR.createFromParcel(_reply);
                    }
                    else {
                        _result = null;
                    }
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            public void registerConfigCallback(final String name, final ITVCallback cb) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeString(name);
                    _data.writeStrongBinder((cb != null) ? cb.asBinder() : null);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void unregisterConfigCallback(final String name, final ITVCallback cb) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeString(name);
                    _data.writeStrongBinder((cb != null) ? cb.asBinder() : null);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public long getTime() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                long _result;
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readLong();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            public void switchAudioTrack(final int aud_track) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(aud_track);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void setInputSource(final int source) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(source);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public int getCurInputSource() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            public void setProgramType(final int type) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(type);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void setVideoWindow(final int x, final int y, final int w, final int h) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(x);
                    _data.writeInt(y);
                    _data.writeInt(w);
                    _data.writeInt(h);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void playProgram(final TVPlayParams tp) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    if (tp != null) {
                        _data.writeInt(1);
                        tp.writeToParcel(_data, 0);
                    }
                    else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void stopPlaying() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void switchAudio(final int id) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(id);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void resetATVFormat() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void startTimeshifting() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void stopTimeshifting() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void startRecording(final long duration) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeLong(duration);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void startTestRecording(final long duration) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeLong(duration);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void startRecordingByBack(final int db_id) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(db_id);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void stopRecording() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void setFrontendByDtvView(final int db_id) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(db_id);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public DTVRecordParams getRecordingParams() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                DTVRecordParams _result;
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (DTVRecordParams) DTVRecordParams.CREATOR.createFromParcel(_reply);
                    }
                    else {
                        _result = null;
                    }
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            public void startPlayback(final String filePath) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeString(filePath);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void stopPlayback() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public DTVPlaybackParams getPlaybackParams() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                DTVPlaybackParams _result;
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (DTVPlaybackParams) DTVPlaybackParams.CREATOR.createFromParcel(_reply);
                    }
                    else {
                        _result = null;
                    }
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            public void startScan(final TVScanParams sp) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    if (sp != null) {
                        _data.writeInt(1);
                        sp.writeToParcel(_data, 0);
                    }
                    else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void stopScan(final boolean store) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt((int)(store ? 1 : 0));
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void startBooking(final int bookingID) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(bookingID);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void pause() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void resume() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void fastForward(final int speed) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(speed);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void fastBackward(final int speed) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(speed);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void seekTo(final int pos) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(pos);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public int getFrontendStatus() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            public int getFrontendSignalStrength() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            public int getFrontendSNR() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            public int getFrontendBER() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            public void fineTune(final int freq) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(freq);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void restoreFactorySetting(final int flags) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(flags);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void playValid() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void setVGAAutoAdjust() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public int GetSrcInputType() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            public TvinInfo getCurrentSignalInfo() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                TvinInfo _result;
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (TvinInfo) TvinInfo.CREATOR.createFromParcel(_reply);
                    }
                    else {
                        _result = null;
                    }
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            public void replay() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void unblock() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void lock(final TVChannelParams curParams) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    if (curParams != null) {
                        _data.writeInt(1);
                        curParams.writeToParcel(_data, 0);
                    }
                    else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void setCvbsAmpOut(final int amp) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(amp);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void secRequest(final TVMessage sec_msg) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    if (sec_msg != null) {
                        _data.writeInt(1);
                        sec_msg.writeToParcel(_data, 0);
                    }
                    else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void switch_video_blackout(final int val) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(val);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void importDatabase(final String inputXmlPath) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeString(inputXmlPath);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void exportDatabase(final String outputXmlPath) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeString(outputXmlPath);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void playIptv(final int pmtPid, final int vpid, final int vfmt, final int apid, final int afmt) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(pmtPid);
                    _data.writeInt(vpid);
                    _data.writeInt(vfmt);
                    _data.writeInt(apid);
                    _data.writeInt(afmt);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public int getDTVType() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            public void tryRestDevice() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void importdb(final String inputPath) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeString(inputPath);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void exportdb(final String outputPath) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeString(outputPath);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void startRecordById(final int pmtPid, final int vpid, final int vfmt, final int apid, final int afmt, final String storagePath, final String prefixName, final int duration) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvservice.ITVService");
                    _data.writeInt(pmtPid);
                    _data.writeInt(vpid);
                    _data.writeInt(vfmt);
                    _data.writeInt(apid);
                    _data.writeInt(afmt);
                    _data.writeString(storagePath);
                    _data.writeString(prefixName);
                    _data.writeInt(duration);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
