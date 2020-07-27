// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvclient;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.amlogic.tvservice.ITVService;
import com.amlogic.tvutil.DTVPlaybackParams;
import com.amlogic.tvutil.DTVRecordParams;
import com.amlogic.tvutil.ITVCallback;
import com.amlogic.tvutil.TVChannelParams;
import com.amlogic.tvutil.TVConfigValue;
import com.amlogic.tvutil.TVConst;
import com.amlogic.tvutil.TVMessage;
import com.amlogic.tvutil.TVPlayParams;
import com.amlogic.tvutil.TVProgram;
import com.amlogic.tvutil.TVProgramNumber;
import com.amlogic.tvutil.TVScanParams;
import com.amlogic.tvutil.TVStatus;
import com.amlogic.tvutil.TvinInfo;

public abstract class TVClient
{
    public static final int RESTORE_FL_DATABASE = 1;
    public static final int RESTORE_FL_CONFIG = 2;
    public static final int RESTORE_FL_ALL = 3;
    private static final String TAG = "TVClient";
    private static final String SERVICE_NAME = "com.amlogic.tvservice.TVService";
    private static final int MSG_CONNECTED = 1949;
    private static final int MSG_DISCONNECTED = 1950;
    private static final int MSG_MESSAGE = 1951;
    private ITVService service;
    private Context context;
    private Handler handler;
    private int currProgramType;
    private TVProgramNumber currProgramNo;
    private int currProgramID;
    private ServiceConnection conn;
    private ITVCallback callback;
    
    public TVClient() {
        this.currProgramID = -1;
        this.conn = (ServiceConnection)new ServiceConnection() {
            public void onServiceConnected(final ComponentName className, final IBinder service) {
                Log.d("TVClient", "onServiceConnected");
                final Message msg = TVClient.this.handler.obtainMessage(1949, (Object)service);
                TVClient.this.handler.sendMessage(msg);
            }
            
            public void onServiceDisconnected(final ComponentName className) {
                Log.d("TVClient", "onServiceDisconnected");
                final Message msg = TVClient.this.handler.obtainMessage(1950);
                TVClient.this.handler.sendMessage(msg);
            }
        };
        this.callback = new ITVCallback.Stub() {
            public void onMessage(final TVMessage msg) {
                Log.d("TVClient", "onMessage");
                final Message m = TVClient.this.handler.obtainMessage(1951, (Object)msg);
                TVClient.this.handler.sendMessage(m);
            }
        };
    }
    
    @SuppressLint("HandlerLeak")
    public void connect(final Context context) {
        Log.d("TVClient", "connect");
        this.context = context;
        final Intent intent = new Intent("com.amlogic.tvservice.TVService");
        intent.setFlags(268435456);
        context.startService(intent);
        this.handler = new Handler() {
            public void handleMessage(final Message msg) {
                Log.d("TVClient", "handle message " + msg.what);
                switch (msg.what) {
                    case 1949: {
                        final IBinder binder = (IBinder)msg.obj;
                        TVClient.this.service = ITVService.Stub.asInterface(binder);
                        try {
                            TVClient.this.service.registerCallback(TVClient.this.callback);
                        }
                        catch (RemoteException ex) {}
                        final TVStatus s = TVClient.this.getStatus();
                        if (s != null) {
                            TVClient.this.currProgramType = s.programType;
                            TVClient.this.currProgramNo = s.programNo;
                            TVClient.this.currProgramID = s.programID;
                        }
                        TVClient.this.onConnected();
                        break;
                    }
                    case 1950: {
                        if (TVClient.this.service != null) {
                            TVClient.this.onDisconnected();
                            try {
                                TVClient.this.service.unregisterCallback(TVClient.this.callback);
                            }
                            catch (RemoteException ex2) {}
                            TVClient.this.service = null;
                            break;
                        }
                        break;
                    }
                    case 1951: {
                        final TVMessage tvmsg = (TVMessage)msg.obj;
                        switch (tvmsg.getType()) {
                            case 20: {
                                TVClient.this.currProgramType = tvmsg.getProgramType();
                                TVClient.this.currProgramNo = tvmsg.getProgramNumber();
                                break;
                            }
                            case 15: {
                                TVClient.this.currProgramID = tvmsg.getProgramID();
                                final TVProgram prog = TVProgram.selectByID(TVClient.this.context, TVClient.this.currProgramID);
                                if (prog != null) {
                                    TVClient.this.currProgramType = prog.getType();
                                    TVClient.this.currProgramNo = prog.getNumber();
                                    break;
                                }
                                break;
                            }
                            case 16: {
                                TVClient.this.currProgramID = -1;
                                TVClient.this.currProgramNo = null;
                                break;
                            }
                            case 45: {
                                final DTVRecordParams minfo = tvmsg.getPlaybackMediaInfo();
                                if (minfo != null && TVClient.this.currProgramType != 6) {
                                    TVProgram playbackProg = null;
                                    if (minfo.getTimeshiftMode()) {
                                        playbackProg = TVProgram.selectByID(TVClient.this.context, TVClient.this.getIntConfig("tv:last_program_id"));
                                    }
                                    if (playbackProg == null) {
                                        playbackProg = new TVProgram(TVClient.this.context, minfo.getProgramName(), 6, minfo.getVideo(), minfo.getAllAudio(), minfo.getAllSubtitle(), minfo.getAllTeletext());
                                    }
                                    if (playbackProg != null) {
                                        TVClient.this.currProgramType = 6;
                                        TVClient.this.currProgramNo = playbackProg.getNumber();
                                        TVClient.this.currProgramID = playbackProg.getID();
                                    }
                                    break;
                                }
                                break;
                            }
                            case 46: {
                                TVClient.this.currProgramID = -1;
                                TVClient.this.currProgramNo = null;
                                TVClient.this.currProgramType = 0;
                                break;
                            }
                        }
                        TVClient.this.onMessage((TVMessage)msg.obj);
                        break;
                    }
                }
            }
        };
        context.bindService(new Intent("com.amlogic.tvservice.TVService"), this.conn, 1);
    }
    
    public void disconnect(final Context context) {
        if (this.handler != null) {
            final Message msg = this.handler.obtainMessage(1950);
            this.handler.sendMessage(msg);
        }
        context.unbindService(this.conn);
    }
    
    public abstract void onConnected();
    
    public abstract void onDisconnected();
    
    public abstract void onMessage(final TVMessage p0);
    
    public TVStatus getStatus() {
        TVStatus s = null;
        if (this.service != null) {
            try {
                s = this.service.getStatus();
            }
            catch (RemoteException ex) {}
        }
        return s;
    }
    
    public void setVideoWindow(final int x, final int y, final int w, final int h) {
        if (this.service != null) {
            try {
                this.service.setVideoWindow(x, y, w, h);
                this.service.setVideoWindow(x, y, w, h);
            }
            catch (RemoteException ex) {}
        }
    }
    
    private long getTime() {
        long ret = 0L;
        if (this.service != null) {
            try {
                ret = this.service.getTime();
            }
            catch (RemoteException ex) {}
        }
        return ret;
    }
    
    public long getLocalTime(final long utc) {
        final long offset = this.getIntConfig("tv:time:offset");
        return offset * 1000L + utc;
    }
    
    public long getLocalTime() {
        final long offset = this.getIntConfig("tv:time:offset");
        return offset * 1000L + this.getTime();
    }
    
    public long getUTCTime(final long local) {
        final long offset = this.getIntConfig("tv:time:offset");
        return local - offset * 1000L;
    }
    
    public long getUTCTime() {
        return this.getTime();
    }
    
    public void setInputSource(final int source) {
        if (this.service != null) {
            try {
                this.service.setInputSource(source);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public TVConst.SourceInput getCurInputSource() {
        if (this.service != null) {
            try {
                final int sourceInt = this.service.getCurInputSource();
                final TVConst.SourceInput source = TVConst.SourceInput.values()[sourceInt];
                return source;
            }
            catch (RemoteException ex) {}
        }
        return null;
    }
    
    public void setProgramType(final int type) {
        if (this.service != null) {
            try {
                this.service.setProgramType(type);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void playProgram(final TVPlayParams tp) {
        if (this.service != null) {
            try {
                this.service.playProgram(tp);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void playIptv(final int pmtPid, final int vpid, final int vfmt, final int apid, final int afmt) {
        if (this.service != null) {
            try {
                this.service.playIptv(pmtPid, vpid, vfmt, apid, afmt);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void playProgram(final int id) {
        this.playProgram(TVPlayParams.playProgramByID(id));
    }
    
    public void playProgram(final TVProgramNumber num) {
        this.playProgram(TVPlayParams.playProgramByNumber(num));
    }
    
    public void channelUp() {
        this.playProgram(TVPlayParams.playProgramUp());
    }
    
    public void channelDown() {
        this.playProgram(TVPlayParams.playProgramDown());
    }
    
    public void stopPlaying() {
        if (this.service != null) {
            try {
                this.service.stopPlaying();
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void switchAudio(final int id) {
        if (this.service != null) {
            try {
                this.service.switchAudio(id);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void resetATVFormat() {
        if (this.service != null) {
            try {
                this.service.resetATVFormat();
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void startTimeshifting() {
        if (this.service != null) {
            try {
                this.service.startTimeshifting();
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void stopTimeshifting() {
        if (this.service != null) {
            try {
                this.service.stopTimeshifting();
            }
            catch (RemoteException ex) {}
        }
    }
    
    public synchronized void startTestRecording(final long duration) {
        if (this.service != null) {
            try {
                this.service.startTestRecording(duration);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public synchronized void startRecording(final long duration) {
        if (this.service != null) {
            try {
                this.service.startRecording(duration);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public synchronized void startRecordingByBackRound(final int db_id) {
        if (this.service != null) {
            try {
                this.service.startRecordingByBack(db_id);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public synchronized void setFrontendByDtvView(final int db_id) {
        if (this.service != null) {
            try {
                this.service.setFrontendByDtvView(db_id);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void switchAudioTrack(final int aud_track) {
        if (this.service != null) {
            try {
                this.service.switchAudioTrack(aud_track);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void stopRecording() {
        if (this.service != null) {
            try {
                this.service.stopRecording();
            }
            catch (RemoteException ex) {}
        }
    }
    
    public DTVRecordParams getRecordingParams() {
        if (this.service != null) {
            try {
                return this.service.getRecordingParams();
            }
            catch (RemoteException ex) {}
        }
        return null;
    }
    
    public void startPlayback(final String filePath) {
        if (this.service != null) {
            try {
                this.service.startPlayback(filePath);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void stopPlayback() {
        if (this.service != null) {
            try {
                this.service.stopPlayback();
            }
            catch (RemoteException ex) {}
        }
    }
    
    public DTVPlaybackParams getPlaybackParams() {
        if (this.service != null) {
            try {
                return this.service.getPlaybackParams();
            }
            catch (RemoteException ex) {}
        }
        return null;
    }
    
    public void startScan(final TVScanParams sp) {
        Log.d("TVClient", "---------startScan------------");
        if (this.service != null) {
            try {
                this.service.startScan(sp);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void stopScan(final boolean store) {
        if (this.service != null) {
            try {
                this.service.stopScan(store);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void startBooking(final int bookingID) {
        if (this.service != null) {
            try {
                this.service.startBooking(bookingID);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void pause() {
        if (this.service != null) {
            try {
                this.service.pause();
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void resume() {
        if (this.service != null) {
            try {
                this.service.resume();
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void fastForward(final int speed) {
        if (this.service != null) {
            try {
                this.service.fastForward(speed);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void fastBackward(final int speed) {
        if (this.service != null) {
            try {
                this.service.fastBackward(speed);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void seekTo(final int pos) {
        if (this.service != null) {
            try {
                this.service.seekTo(pos);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void setConfig(final String name, final TVConfigValue value) {
        if (this.service != null) {
            try {
                this.service.setConfig(name, value);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public TVConfigValue getConfig(final String name) {
        TVConfigValue value = null;
        if (this.service != null) {
            try {
                value = this.service.getConfig(name);
            }
            catch (RemoteException ex) {}
        }
        return value;
    }
    
    public void setConfig(final String name, final String value) {
        this.setConfig(name, new TVConfigValue(value));
    }
    
    public void setConfig(final String name, final boolean value) {
        this.setConfig(name, new TVConfigValue(value));
    }
    
    public void setConfig(final String name, final int value) {
        this.setConfig(name, new TVConfigValue(value));
    }
    
    public boolean getBooleanConfig(final String name) {
        final TVConfigValue value = this.getConfig(name);
        boolean b = false;
        try {
            b = value.getBoolean();
        }
        catch (Exception e) {
            Log.e("TVClient", "The config is not a boolean value: " + name);
        }
        return b;
    }
    
    public int getIntConfig(final String name) {
        final TVConfigValue value = this.getConfig(name);
        int i = 0;
        try {
            i = value.getInt();
        }
        catch (Exception e) {
            Log.e("TVClient", "The config is not an integer value: " + name);
        }
        return i;
    }
    
    public String getStringConfig(final String name) {
        final TVConfigValue value = this.getConfig(name);
        String s = "";
        try {
            s = value.getString();
        }
        catch (Exception e) {
            Log.e("TVClient", "The config is not a string value: " + name);
        }
        return s;
    }
    
    public void registerConfigCallback(final String name) {
        if (this.service != null) {
            try {
                this.service.registerConfigCallback(name, this.callback);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void unregisterConfigCallback(final String name) {
        if (this.service != null) {
            try {
                this.service.unregisterConfigCallback(name, this.callback);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public int getFrontendStatus() {
        int ret = 0;
        if (this.service != null) {
            try {
                ret = this.service.getFrontendStatus();
            }
            catch (RemoteException ex) {}
        }
        return ret;
    }
    
    public int getFrontendSignalStrength() {
        int ret = 0;
        if (this.service != null) {
            try {
                ret = this.service.getFrontendSignalStrength();
            }
            catch (RemoteException ex) {}
        }
        return ret;
    }
    
    public int getFrontendSNR() {
        int ret = 0;
        if (this.service != null) {
            try {
                ret = this.service.getFrontendSNR();
            }
            catch (RemoteException ex) {}
        }
        return ret;
    }
    
    public int getFrontendBER() {
        int ret = 0;
        if (this.service != null) {
            try {
                ret = this.service.getFrontendBER();
            }
            catch (RemoteException ex) {}
        }
        return ret;
    }
    
    public int getCurrentProgramID() {
        return this.currProgramID;
    }
    
    public int getCurrentProgramType() {
        return this.currProgramType;
    }
    
    public TVProgramNumber getCurrentProgramNumber() {
        return this.currProgramNo;
    }
    
    public void fineTune(final int freq) {
        if (this.service != null) {
            try {
                this.service.fineTune(freq);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void setCvbsAmpOut(final int amp) {
        if (this.service != null) {
            try {
                this.service.setCvbsAmpOut(amp);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void restoreFactorySetting() {
        this.restoreFactorySetting(3);
    }
    
    public void restoreFactorySetting(final int flags) {
        if (this.service != null) {
            try {
                this.service.restoreFactorySetting(flags);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void playValid() {
        Log.d("TVClient", "playValid");
        if (this.service != null) {
            try {
                Log.d("TVClient", "playValid@");
                this.service.playValid();
                Log.d("TVClient", "playValid@ end");
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void setVGAAutoAdjust() {
        if (this.service != null) {
            try {
                this.service.setVGAAutoAdjust();
            }
            catch (RemoteException ex) {}
        }
    }
    
    public int GetSrcInputType() {
        int type = 0;
        if (this.service != null) {
            try {
                type = this.service.GetSrcInputType();
            }
            catch (RemoteException ex) {}
        }
        return type;
    }
    
    public TvinInfo getCurrentSignalInfo() {
        TvinInfo tvinInfo = null;
        if (this.service != null) {
            try {
                tvinInfo = this.service.getCurrentSignalInfo();
            }
            catch (RemoteException ex) {}
        }
        return tvinInfo;
    }
    
    public void replay() {
        if (this.service != null) {
            try {
                this.service.replay();
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void unblock() {
        if (this.service != null) {
            try {
                this.service.unblock();
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void lock(final TVChannelParams curParams) {
        if (this.service != null) {
            try {
                this.service.lock(curParams);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void secRequest(final TVMessage sec_msg) {
        if (this.service != null) {
            try {
                this.service.secRequest(sec_msg);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void switch_video_blackout(final int val) {
        if (this.service != null) {
            try {
                this.service.switch_video_blackout(val);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void importDatabase(final String inputXmlPath) {
        if (this.service != null) {
            try {
                this.service.importDatabase(inputXmlPath);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void exportDatabase(final String outputXmlPath) {
        if (this.service != null) {
            try {
                this.service.exportDatabase(outputXmlPath);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public int getDTVType() {
        int type = 0;
        if (this.service != null) {
            try {
                type = this.service.getDTVType();
            }
            catch (RemoteException ex) {}
        }
        return type;
    }
    
    public void importdb(final String inputPath) {
        if (this.service != null) {
            try {
                this.service.importdb(inputPath);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void exportdb(final String outputPath) {
        if (this.service != null) {
            try {
                this.service.exportdb(outputPath);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void tryRestDevice() {
        if (this.service != null) {
            try {
                this.service.tryRestDevice();
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void startRecordById(final int pmtPid, final int vpid, final int vfmt, final int apid, final int afmt, final String storagePath, final String prefixName, final int duration) {
        if (this.service != null) {
            try {
                this.service.startRecordById(pmtPid, vpid, vfmt, apid, afmt, storagePath, prefixName, duration);
            }
            catch (RemoteException ex) {}
        }
    }
}
