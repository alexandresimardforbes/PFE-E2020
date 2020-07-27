// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvactivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.amlogic.tvclient.TVClient;
import com.amlogic.tvsubtitle.TVSubtitleView;
import com.amlogic.tvutil.DTVPlaybackParams;
import com.amlogic.tvutil.DTVRecordParams;
import com.amlogic.tvutil.TVChannel;
import com.amlogic.tvutil.TVChannelParams;
import com.amlogic.tvutil.TVConfigValue;
import com.amlogic.tvutil.TVConst;
import com.amlogic.tvutil.TVMessage;
import com.amlogic.tvutil.TVProgram;
import com.amlogic.tvutil.TVProgramNumber;
import com.amlogic.tvutil.TVScanParams;
import com.amlogic.tvutil.TvinInfo;
import com.geniatech.dtvstreamer.IPhoneServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public abstract class TVActivity extends Activity
{
    private static final String TAG = "TVActivity";
    private static final int SUBTITLE_NONE = 0;
    private static final int SUBTITLE_SUB = 1;
    private static final int SUBTITLE_TT = 2;
    private VideoView videoView;
    private TVSubtitleView subtitleView;
    private boolean connected;
    private boolean externalVideoView;
    private boolean externalSubtitleView;
    private boolean subtitleViewActive;
    private int currSubtitleMode;
    private int currSubtitlePID;
    private int currTeletextPID;
    private int currSubtitleID1;
    private int currSubtitleID2;
    private Context mContext;
    private TVClient client;
    SurfaceHolder.Callback surfaceHolderCallback;
    private IPhoneServer iService;
    private ServiceConnection connection;
    
    public TVActivity() {
        this.connected = false;
        this.externalVideoView = false;
        this.externalSubtitleView = false;
        this.subtitleViewActive = false;
        this.currSubtitleMode = 0;
        this.currSubtitlePID = -1;
        this.currTeletextPID = -1;
        this.currSubtitleID1 = -1;
        this.currSubtitleID2 = -1;
        this.client = new TVClient() {
            @Override
            public void onConnected() {
                TVActivity.this.connected = true;
                TVActivity.this.initSubtitle();
                TVActivity.this.updateVideoWindow();
                TVActivity.this.onConnected();
            }
            
            @Override
            public void onDisconnected() {
                TVActivity.this.connected = false;
                TVActivity.this.onDisconnected();
            }
            
            @Override
            public void onMessage(final TVMessage m) {
                TVActivity.this.solveMessage(m);
                TVActivity.this.onMessage(m);
                if (m.getType() == 48) {
                    TVActivity.this.finish();
                }
                if (m.getType() == 53) {
                    TVActivity.this.finish();
                    TVActivity.this.client.disconnect((Context) TVActivity.this);
                    final Intent tIntent = new Intent("com.amlogic.tvservice.TVService");
                    TVActivity.this.mContext.stopService(tIntent);
                    System.exit(0);
                }
            }
        };
        this.surfaceHolderCallback = (SurfaceHolder.Callback)new SurfaceHolder.Callback() {
            public void surfaceChanged(final SurfaceHolder holder, final int format, final int w, final int h) {
                Log.d("TVActivity", "surfaceChanged");
                try {
                    this.initSurface(holder);
                    TVActivity.this.updateVideoWindow();
                }
                catch (Exception ex) {}
            }
            
            public void surfaceCreated(final SurfaceHolder holder) {
                Log.d("TVActivity", "surfaceCreated");
                try {
                    this.initSurface(holder);
                }
                catch (Exception ex) {}
            }
            
            public void surfaceDestroyed(final SurfaceHolder holder) {
                Log.d("TVActivity", "surfaceDestroyed");
                TVActivity.this.disConnectPhoneService();
            }
            
            private void initSurface(final SurfaceHolder h) {
                Canvas c = null;
                try {
                    Log.d("TVActivity", "initSurface");
                    c = h.lockCanvas();
                }
                finally {
                    if (c != null) {
                        h.unlockCanvasAndPost(c);
                    }
                }
            }
        };
        this.iService = null;
        this.connection = (ServiceConnection)new ServiceConnection() {
            public void onServiceConnected(final ComponentName name, final IBinder service) {
                TVActivity.this.iService = IPhoneServer.Stub.asInterface(service);
                try {
                    TVActivity.this.iService.isOpenDevice(true);
                    TVActivity.this.iService.startProgram(TVActivity.this.client.getCurrentProgramID());
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            
            public void onServiceDisconnected(final ComponentName name) {
                TVActivity.this.iService = null;
            }
        };
    }
    
    protected void onPause() {
        Log.d("TVActivity", "onPause");
        this.subtitleViewActive = false;
        if (this.subtitleView != null) {
            this.subtitleView.setActive(false);
        }
        super.onStop();
    }
    
    protected void onResume() {
        Log.d("TVActivity", "onResume");
        super.onResume();
        this.subtitleViewActive = true;
        this.updateVideoWindow();
        if (this.subtitleView != null) {
            this.subtitleView.setActive(true);
        }
    }
    
    public void onCreate(final Bundle savedInstanceState) {
        Log.d("TVActivity", "onCreate");
        this.mContext = this;
        super.onCreate(savedInstanceState);
        this.client.connect((Context)this);
    }
    
    protected void onDestroy() {
        Log.d("TVActivity", "onDestroy");
        if (this.subtitleView != null) {
            this.unregisterConfigCallback("tv:subtitle:enable");
            this.unregisterConfigCallback("tv:subtitle:language");
            this.unregisterConfigCallback("tv:teletext:language");
            this.unregisterConfigCallback("tv:atsc:cc:caption");
            this.unregisterConfigCallback("tv:atsc:cc:foregroundcolor");
            this.unregisterConfigCallback("tv:atsc:cc:foregroundopacity");
            this.unregisterConfigCallback("tv:atsc:cc:backgroundcolor");
            this.unregisterConfigCallback("tv:atsc:cc:backgroundopacity");
            this.unregisterConfigCallback("tv:atsc:cc:fontstyle");
            this.unregisterConfigCallback("tv:atsc:cc:fontsize");
            this.unregisterConfigCallback("tv:atsc:cc:enable");
            this.subtitleView.dispose();
            this.subtitleView = null;
        }
        this.client.disconnect((Context)this);
        super.onDestroy();
    }
    
    private int getTeletextRegionID(final String ttxRegionName) {
        final String[] supportedRegions = { "English", "Deutsch", "Svenska/Suomi/Magyar", "Italiano", "Fran\u00e7ais", "Portugu\u00eas/Espa\u00f1ol", "Cesky/Slovencina", "T\u00fcrk\u00e7e", "Ellinika", "Alarabia / English" };
        final int[] regionIDMaps = { 16, 17, 18, 19, 20, 21, 14, 22, 55, 64 };
        int i;
        for (i = 0; i < supportedRegions.length && !supportedRegions[i].equals(ttxRegionName); ++i) {}
        if (i >= supportedRegions.length) {
            Log.d("TVActivity", "Teletext defaut region " + ttxRegionName + " not found, using 'English' as default!");
            i = 0;
        }
        Log.d("TVActivity", "Teletext default region id: " + regionIDMaps[i]);
        return regionIDMaps[i];
    }
    
    private void resetProgramCC(final TVProgram prog) {
        this.subtitleView.stop();
        if (!this.getBooleanConfig("tv:atsc:cc:enable")) {
            Log.d("TVActivity", "CC is disabled !");
            return;
        }
        if (prog.getType() != 3) {
            final TVSubtitleView.DTVCCParams subp = new TVSubtitleView.DTVCCParams(this.getIntConfig("tv:atsc:cc:caption"), this.getIntConfig("tv:atsc:cc:foregroundcolor"), this.getIntConfig("tv:atsc:cc:foregroundopacity"), this.getIntConfig("tv:atsc:cc:backgroundcolor"), this.getIntConfig("tv:atsc:cc:backgroundopacity"), this.getIntConfig("tv:atsc:cc:fontstyle"), this.getIntConfig("tv:atsc:cc:fontsize"));
            this.subtitleView.setSubParams(subp);
        }
        this.subtitleView.startSub();
        this.subtitleView.show();
    }
    
    private void resetSubtitle(final int mode) {
        this.resetSubtitle(mode, -1);
    }
    
    private void resetSubtitle(final int mode, final int sub_id) {
        if (this.subtitleView == null) {
            return;
        }
        if (mode == 0) {
            this.subtitleView.stop();
            this.subtitleView.hide();
            this.currSubtitleMode = mode;
            this.currSubtitlePID = -1;
            this.currSubtitleID1 = -1;
            this.currSubtitleID2 = -1;
            return;
        }
        final int prog_id = this.client.getCurrentProgramID();
        Log.d("TVActivity", "reset subtitle, current program id " + prog_id);
        if (prog_id == -1) {
            return;
        }
        final TVProgram prog = TVProgram.selectByID((Context)this, prog_id);
        if (prog == null) {
            return;
        }
        int pid = -1;
        int id1 = -1;
        int id2 = -1;
        int pm = -1;
        if (mode == 1) {
            if (this.getStringConfig("tv:dtv:mode").equals("atsc")) {
                this.resetProgramCC(prog);
                return;
            }
            TVProgram.Subtitle sub = null;
            if (sub_id >= 0) {
                sub = prog.getSubtitle(sub_id);
                if (sub != null) {
                    prog.setCurrentSubtitle(sub_id);
                }
            }
            else {
                final int sub_idx = prog.getCurrentSubtitle(this.getStringConfig("tv:subtitle:language"));
                if (sub_idx >= 0) {
                    sub = prog.getSubtitle(sub_idx);
                }
            }
            if (sub == null) {
                this.resetSubtitle(0);
                return;
            }
            switch (sub.getType()) {
                case 1: {
                    pid = sub.getPID();
                    id1 = sub.getCompositionPageID();
                    id2 = sub.getAncillaryPageID();
                    pm = 1;
                    break;
                }
                case 2: {
                    pid = sub.getPID();
                    id1 = sub.getMagazineNumber();
                    id2 = sub.getPageNumber();
                    pm = 2;
                    break;
                }
            }
        }
        else if (mode == 2) {
            TVProgram.Teletext tt = null;
            if (sub_id >= 0) {
                tt = prog.getTeletext(sub_id);
                if (tt != null) {
                    prog.setCurrentTeletext(sub_id);
                }
            }
            else {
                final int tt_idx = prog.getCurrentTeletext(this.getStringConfig("tv:teletext:language"));
                if (tt_idx >= 0) {
                    tt = prog.getTeletext(tt_idx);
                }
            }
            if (tt == null) {
                return;
            }
            pid = tt.getPID();
            id1 = tt.getMagazineNumber();
            id2 = tt.getPageNumber();
            pm = 2;
        }
        if (mode == this.currSubtitleMode && pid == this.currSubtitlePID && id1 == this.currSubtitleID1 && id2 == this.currSubtitleID2) {
            return;
        }
        this.subtitleView.stop();
        if (pm == 2 && pid != this.currTeletextPID) {
            this.subtitleView.clear();
        }
        final int dmx_id = 0;
        if (pm == 1) {
            final TVSubtitleView.DVBSubParams subp = new TVSubtitleView.DVBSubParams(dmx_id, pid, id1, id2);
            this.subtitleView.setSubParams(subp);
        }
        else {
            int pgno = (id1 == 0) ? 800 : (id1 * 100);
            pgno += (id2 & 0xF) + (id2 >> 4 & 0xF) * 10 + (id2 >> 8 & 0xF) * 100;
            final TVSubtitleView.DTVTTParams ttp = new TVSubtitleView.DTVTTParams(dmx_id, pid, pgno, 16255, this.getTeletextRegionID(this.getStringConfig("tv:teletext:region")));
            if (mode == 1) {
                this.subtitleView.setSubParams(ttp);
            }
            else {
                this.subtitleView.setTTParams(ttp);
            }
        }
        boolean show_flag = true;
        if (mode == 1) {
            show_flag = this.getBooleanConfig("tv:subtitle:enable");
            this.subtitleView.startSub();
        }
        else {
            this.subtitleView.startTT();
            show_flag = true;
        }
        if (show_flag) {
            this.subtitleView.show();
        }
        else {
            this.subtitleView.hide();
        }
        this.currSubtitleMode = mode;
        this.currSubtitlePID = pid;
        this.currSubtitleID1 = id1;
        this.currSubtitleID2 = id2;
        if (pm == 2) {
            this.currTeletextPID = pid;
        }
    }
    
    public void onProgramStart(final int prog_id) {
        Log.d("TVActivity", "onProgramStart ;");
        this.resetSubtitle(1);
    }
    
    private void onProgramStop(final int prog_id) {
        Log.d("TVActivity", "onProgramStop");
        this.resetSubtitle(0);
        this.currTeletextPID = -1;
    }
    
    private void onPlaybackStart() {
        Log.d("TVActivity", "onPlaybackStart");
        this.resetSubtitle(1);
    }
    
    private void onPlaybackStop() {
        Log.d("TVActivity", "onPlaybackStop");
        this.resetSubtitle(0);
        this.currTeletextPID = -1;
    }
    
    private void onConfigChanged(final String name, final TVConfigValue val) throws Exception {
        Log.d("TVActivity", "config " + name + " changed");
        if (name.equals("tv:subtitle:enable")) {
            final boolean v = val.getBoolean();
            Log.d("TVActivity", "tv:subtitle:enable changed -> " + v);
            if (this.subtitleView != null && this.currSubtitleMode == 1) {
                if (v) {
                    this.subtitleView.show();
                }
                else {
                    this.subtitleView.hide();
                }
            }
        }
        else if (name.equals("tv:subtitle:language")) {
            final String lang = val.getString();
            Log.d("TVActivity", "tv:subtitle:language changed -> " + lang);
            if (this.currSubtitleMode == 1) {}
        }
        else if (name.equals("tv:teletext:language")) {
            final String lang = val.getString();
            Log.d("TVActivity", "tv:teletext:language changed -> " + lang);
            if (this.currSubtitleMode == 2) {}
        }
        else if (name.equals("tv:atsc:cc:caption") || name.equals("tv:atsc:cc:foregroundcolor") || name.equals("tv:atsc:cc:foregroundopacity") || name.equals("tv:atsc:cc:backgroundcolor") || name.equals("tv:atsc:cc:backgroundopacity") || name.equals("tv:atsc:cc:fontstyle") || name.equals("tv:atsc:cc:fontsize") || name.equals("tv:atsc:cc:enable")) {
            Log.d("TVActivity", name + " changed, reset cc now.");
            final int prog_id = this.client.getCurrentProgramID();
            if (prog_id == -1) {
                return;
            }
            final TVProgram prog = TVProgram.selectByID((Context)this, prog_id);
            if (prog == null) {
                return;
            }
            this.resetProgramCC(prog);
        }
    }
    
    private void solveMessage(final TVMessage msg) {
        switch (msg.getType()) {
            case 15: {
                this.onProgramStart(msg.getProgramID());
                this.updateSmallWindow();
                break;
            }
            case 16: {
                this.onProgramStop(msg.getProgramID());
                break;
            }
            case 45: {
                this.onPlaybackStart();
                break;
            }
            case 46: {
                this.onPlaybackStop();
                break;
            }
            case 9: {
                try {
                    this.onConfigChanged(msg.getConfigName(), msg.getConfigValue());
                }
                catch (Exception e) {
                    Log.e("TVActivity", "error in onConfigChanged");
                }
                break;
            }
            case 53: {
                this.finish();
                break;
            }
        }
    }
    
    public abstract void onConnected();
    
    public abstract void onDisconnected();
    
    public abstract void onMessage(final TVMessage p0);
    
    private void initSubtitle() {
        if (this.subtitleView == null) {
            return;
        }
        if (!this.connected) {
            return;
        }
        this.subtitleView.setMargin(this.getIntConfig("tv:subtitle:margin_left"), this.getIntConfig("tv:subtitle:margin_top"), this.getIntConfig("tv:subtitle:margin_right"), this.getIntConfig("tv:subtitle:margin_bottom"));
        Log.d("TVActivity", "register subtitle/teletext config callbacks");
        this.registerConfigCallback("tv:subtitle:enable");
        this.registerConfigCallback("tv:subtitle:language");
        this.registerConfigCallback("tv:teletext:language");
        this.registerConfigCallback("tv:atsc:cc:caption");
        this.registerConfigCallback("tv:atsc:cc:foregroundcolor");
        this.registerConfigCallback("tv:atsc:cc:foregroundopacity");
        this.registerConfigCallback("tv:atsc:cc:backgroundcolor");
        this.registerConfigCallback("tv:atsc:cc:backgroundopacity");
        this.registerConfigCallback("tv:atsc:cc:fontstyle");
        this.registerConfigCallback("tv:atsc:cc:fontsize");
        this.registerConfigCallback("tv:atsc:cc:enable");
    }
    
    public void updateVideoWindow() {
        if (this.videoView == null) {
            return;
        }
        if (!this.connected) {
            return;
        }
        final int[] loc = new int[2];
        this.videoView.getLocationOnScreen(loc);
        Log.d("TVActivity", "--" + loc[0] + "---" + loc[1] + "---" + this.videoView.getWidth() + "---" + this.videoView.getHeight());
        if (loc[0] == 0 && loc[1] == 0) {
            this.client.setVideoWindow(0, 0, 0, 0);
        }
        else {
            final int x = loc[0];
            final int y = loc[1];
            final int w = this.videoView.getWidth();
            final int h = this.videoView.getHeight();
            Log.d("TVActivity", "--" + x + "---" + y + "---" + w + "---" + h);
            this.client.setVideoWindow(x, y, w, h);
        }
    }
    
    private void updateSmallWindow() {
        if (this.videoView == null) {
            return;
        }
        final int[] loc = new int[2];
        this.videoView.getLocationOnScreen(loc);
        if (loc[0] != 0 && loc[1] != 0) {
            this.updateVideoWindow();
        }
    }
    
    public void setVideoWindow(final int x, final int y, final int w, final int h) {
        Log.d("TVActivity", "setVideoWindow--" + x + "-" + y + "-" + w + "-" + h);
        if (this.client != null) {
            this.client.setVideoWindow(x, y, w, h);
        }
    }
    
    public void openVideo(final VideoView view, final TVSubtitleView subv) {
        Log.d("TVActivity", "openVideo");
        final ViewGroup root = (ViewGroup)this.getWindow().getDecorView().findViewById(16908290);
        if (subv != null) {
            this.subtitleView = subv;
            this.externalSubtitleView = true;
            this.subtitleView.setLayerType(1, (Paint)null);
            this.initSubtitle();
        }
        else if (this.subtitleView == null) {
            Log.d("TVActivity", "create subtitle view");
            this.subtitleView = new TVSubtitleView((Context)this);
            this.externalSubtitleView = false;
            this.subtitleView.setLayerType(1, (Paint)null);
            root.addView((View)this.subtitleView, 0);
            this.initSubtitle();
        }
        if (view != null) {
            this.videoView = view;
            this.externalVideoView = true;
            this.updateVideoWindow();
        }
        else if (this.videoView == null) {
            Log.d("TVActivity", "create video view");
            this.videoView = new VideoView((Context)this);
            this.externalVideoView = false;
            root.addView((View)this.videoView, 0);
            this.videoView.getHolder().addCallback(this.surfaceHolderCallback);
            if (Integer.valueOf(Build.VERSION.SDK) < 21) {
                this.videoView.getHolder().setFormat(258);
            }
            else {
                this.videoView.getHolder().setFormat(1);
            }
            this.updateVideoWindow();
            this.startPhoneSerVer();
        }
        if (this.subtitleViewActive && this.subtitleView != null) {
            this.subtitleView.setActive(true);
        }
    }
    
    public boolean isServiceRunning(final String serviceName) {
        final ActivityManager manager = (ActivityManager)this.getSystemService("activity");
        final List<ActivityManager.RunningServiceInfo> infos = (List<ActivityManager.RunningServiceInfo>)manager.getRunningServices(30);
        for (final ActivityManager.RunningServiceInfo info : infos) {
            if (info.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
    
    public void startPhoneSerVer() {
        if (this.client.getBooleanConfig("tv:dtv:phone:watch")) {
            if (!this.isServiceRunning("com.amlogic.tvbox.PhoneServer")) {
                final Intent intent2 = new Intent("com.amlogic.tvbox.PhoneServer");
                intent2.setFlags(268435456);
                this.startService(intent2);
            }
            this.bindService(new Intent("com.amlogic.tvbox.PhoneServer"), this.connection, 1);
        }
    }
    
    public void openVideo() {
        this.openVideo(null, null);
    }
    
    public void setVideoWindow(final Rect r) {
        if (this.videoView != null && !this.externalVideoView) {
            this.videoView.layout(r.left, r.top, r.right, r.bottom);
            this.updateVideoWindow();
        }
        if (this.subtitleView != null && !this.externalSubtitleView) {
            this.subtitleView.layout(r.left, r.top, r.right, r.bottom);
        }
    }
    
    public void setSubtitleView(final TVSubtitleView subView) {
        Log.d("TVActivity", "setSubtitleView");
        this.subtitleView = subView;
        this.externalSubtitleView = true;
        this.initSubtitle();
    }
    
    public long getLocalTime(final long utc) {
        return this.client.getLocalTime(utc);
    }
    
    public long getLocalTime() {
        return this.client.getLocalTime();
    }
    
    public long getUTCTime(final long local) {
        return this.client.getUTCTime(local);
    }
    
    public long getUTCTime() {
        return this.client.getUTCTime();
    }
    
    public void setInputSource(final TVConst.SourceInput source) {
        this.client.setInputSource(source.ordinal());
    }
    
    public TVConst.SourceInput getCurInputSource() {
        return this.client.getCurInputSource();
    }
    
    public void setProgramType(final int type) {
        this.client.setProgramType(type);
    }
    
    public void stopPlaying() {
        this.client.stopPlaying();
    }
    
    public void switchSubtitle(final int id) {
        if (this.currSubtitleMode == 1) {
            this.resetSubtitle(1, id);
        }
    }
    
    public void switchAudio(final int id) {
        final int prog_id = this.client.getCurrentProgramID();
        if (prog_id == -1) {
            return;
        }
        final TVProgram prog = TVProgram.selectByID((Context)this, prog_id);
        if (prog == null) {
            return;
        }
        final TVChannel chan = prog.getChannel();
        if (chan == null && prog.getType() != 6) {
            return;
        }
        if (chan != null && chan.isAnalogMode()) {
            final TVChannelParams params = chan.getParams();
            if (params.setATVAudio(id)) {
                this.client.resetATVFormat();
            }
        }
        else {
            this.client.switchAudio(id);
        }
    }
    
    public void switchATVVideoFormat(final TVConst.CC_ATV_VIDEO_STANDARD fmt) {
        final int prog_id = this.client.getCurrentProgramID();
        if (prog_id == -1) {
            return;
        }
        final TVProgram prog = TVProgram.selectByID((Context)this, prog_id);
        if (prog == null) {
            return;
        }
        final TVChannel chan = prog.getChannel();
        if (chan == null) {
            return;
        }
        final TVChannelParams params = chan.getParams();
        if (params == null && !params.isAnalogMode()) {
            return;
        }
        if (chan.setATVVideoFormat(fmt)) {
            Log.v("TVActivity", "setATVVideoFormat");
            this.client.resetATVFormat();
        }
    }
    
    public void switchATVAudioFormat(final TVConst.CC_ATV_AUDIO_STANDARD fmt) {
        final int prog_id = this.client.getCurrentProgramID();
        if (prog_id == -1) {
            return;
        }
        final TVProgram prog = TVProgram.selectByID((Context)this, prog_id);
        if (prog == null) {
            return;
        }
        final TVChannel chan = prog.getChannel();
        if (chan == null) {
            return;
        }
        final TVChannelParams params = chan.getParams();
        if (params == null && !params.isAnalogMode()) {
            return;
        }
        if (chan.setATVAudioFormat(fmt)) {
            Log.v("TVActivity", "setATVAudioFormat");
            this.client.resetATVFormat();
        }
    }
    
    public void startTimeshifting() {
        this.client.startTimeshifting();
    }
    
    public void stopTimeshifting() {
        this.client.stopTimeshifting();
    }
    
    public void startTestRecording(final long duration) {
        this.client.startTestRecording(duration);
    }
    
    public void startRecording(final long duration) {
        this.client.startRecording(duration);
    }
    
    public void stopRecording() {
        this.client.stopRecording();
    }
    
    public DTVRecordParams getRecordingParams() {
        return this.client.getRecordingParams();
    }
    
    public void startPlayback(final String filePath) {
        this.client.startPlayback(filePath);
    }
    
    public void stopPlayback() {
        this.client.stopPlayback();
    }
    
    public DTVPlaybackParams getPlaybackParams() {
        return this.client.getPlaybackParams();
    }
    
    public void startScan(final TVScanParams sp) {
        this.client.startScan(sp);
        if (this.iService != null) {
            try {
                this.iService.startScan();
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void stopScan(final boolean store) {
        this.client.stopScan(store);
    }
    
    public void startBooking(final int bookingID) {
        this.client.startBooking(bookingID);
    }
    
    public void channelUp() {
        this.client.channelUp();
    }
    
    public void channelDown() {
        this.client.channelDown();
    }
    
    public void playProgram(final TVProgramNumber no) {
        this.client.playProgram(no);
    }
    
    public void playProgram(final int id) {
        this.client.playProgram(id);
    }
    
    public void pause() {
        this.client.pause();
    }
    
    public void resume() {
        this.client.resume();
    }
    
    public void fastForward(final int speed) {
        this.client.fastForward(speed);
    }
    
    public void fastBackward(final int speed) {
        this.client.fastBackward(speed);
    }
    
    public void seekTo(final int pos) {
        this.client.seekTo(pos);
    }
    
    public boolean isInTeletextMode() {
        return this.currSubtitleMode == 2;
    }
    
    public void ttShow() {
        if (this.subtitleView == null) {
            return;
        }
        Log.d("TVActivity", "show teletext");
        this.resetSubtitle(2);
    }
    
    public void ttHide() {
        if (this.subtitleView == null) {
            return;
        }
        Log.d("TVActivity", "hide teletext");
        this.resetSubtitle(1);
    }
    
    public void ttGotoNextPage() {
        if (this.subtitleView == null) {
            return;
        }
        Log.d("TVActivity", "goto next teletext page");
        this.subtitleView.nextPage();
    }
    
    public void ttGotoPreviousPage() {
        if (this.subtitleView == null) {
            return;
        }
        Log.d("TVActivity", "goto next previous page");
        this.subtitleView.previousPage();
    }
    
    public void ttGotoPage(final int page) {
        if (this.subtitleView == null) {
            return;
        }
        this.subtitleView.gotoPage(page);
    }
    
    public void ttGoHome() {
        if (this.subtitleView == null) {
            return;
        }
        this.subtitleView.goHome();
    }
    
    public void ttGotoColorLink(final int color) {
        if (this.subtitleView == null) {
            return;
        }
        this.subtitleView.colorLink(color);
    }
    
    public void ttSetSearchPattern(final String pattern, final boolean casefold) {
        if (this.subtitleView == null) {
            return;
        }
        this.subtitleView.setSearchPattern(pattern, casefold);
    }
    
    public void ttSearchNext() {
        if (this.subtitleView == null) {
            return;
        }
        this.subtitleView.searchNext();
    }
    
    public void ttSearchPrevious() {
        if (this.subtitleView == null) {
            return;
        }
        this.subtitleView.searchPrevious();
    }
    
    public void setConfig(final String name, final TVConfigValue value) {
        this.client.setConfig(name, value);
    }
    
    public void setConfig(final String name, final boolean value) {
        this.client.setConfig(name, value);
    }
    
    public void setConfig(final String name, final int value) {
        this.client.setConfig(name, value);
    }
    
    public void setConfig(final String name, final String value) {
        this.client.setConfig(name, value);
    }
    
    public boolean getBooleanConfig(final String name) {
        return this.client.getBooleanConfig(name);
    }
    
    public int getIntConfig(final String name) {
        return this.client.getIntConfig(name);
    }
    
    public String getStringConfig(final String name) {
        return this.client.getStringConfig(name);
    }
    
    public TVConfigValue getConfig(final String name) {
        return this.client.getConfig(name);
    }
    
    public void registerConfigCallback(final String name) {
        this.client.registerConfigCallback(name);
    }
    
    public void unregisterConfigCallback(final String name) {
        this.client.unregisterConfigCallback(name);
    }
    
    public int getFrontendStatus() {
        return this.client.getFrontendStatus();
    }
    
    public int getFrontendSignalStrength() {
        return this.client.getFrontendSignalStrength();
    }
    
    public int getFrontendSNR() {
        return this.client.getFrontendSNR();
    }
    
    public int getFrontendBER() {
        return this.client.getFrontendBER();
    }
    
    public int getCurrentProgramID() {
        return this.client.getCurrentProgramID();
    }
    
    public int getCurrentProgramType() {
        return this.client.getCurrentProgramType();
    }
    
    public TVProgramNumber getCurrentProgramNumber() {
        return this.client.getCurrentProgramNumber();
    }
    
    public void fineTune(final int freq) {
        this.client.fineTune(freq);
    }
    
    public void restoreFactorySetting() {
        this.client.restoreFactorySetting();
    }
    
    public void restoreFactorySetting(final int flags) {
        this.client.restoreFactorySetting(flags);
    }
    
    public void playValid() {
        this.client.playValid();
    }
    
    public void setVGAAutoAdjust() {
        this.client.setVGAAutoAdjust();
    }
    
    public TvinInfo getCurrentSignalInfo() {
        return this.client.getCurrentSignalInfo();
    }
    
    public int GetSrcInputType() {
        return this.client.GetSrcInputType();
    }
    
    public void replay() {
        this.client.replay();
    }
    
    public void unblock() {
        this.client.unblock();
    }
    
    public void lock(final TVChannelParams curParams) {
        this.client.lock(curParams);
    }
    
    public void sec_setLnbsSwitchCfgValid(final TVChannelParams curParams) {
        this.client.secRequest(TVMessage.secRequest(31, curParams));
    }
    
    public void diseqcPositionerStopMoving() {
        this.client.secRequest(TVMessage.secRequest(32));
    }
    
    public void diseqcPositionerDisableLimit() {
        this.client.secRequest(TVMessage.secRequest(33));
    }
    
    public void diseqcPositionerSetEastLimit() {
        this.client.secRequest(TVMessage.secRequest(34));
    }
    
    public void diseqcPositionerSetWestLimit() {
        this.client.secRequest(TVMessage.secRequest(35));
    }
    
    public void diseqcPositionerMoveEast(final TVChannelParams curParams, final int unit) {
        Log.d("TVActivity", "diseqcPositionerMoveEast " + unit);
        this.client.secRequest(TVMessage.secRequest(36, curParams, unit));
    }
    
    public void diseqcPositionerMoveWest(final TVChannelParams curParams, final int unit) {
        Log.d("TVActivity", "diseqcPositionerMoveWest " + unit);
        this.client.secRequest(TVMessage.secRequest(37, curParams, unit));
    }
    
    public void diseqcPositionerStorePosition(final TVChannelParams curParams) {
        this.client.secRequest(TVMessage.secRequest(38, curParams));
    }
    
    public void diseqcPositionerGotoPosition(final TVChannelParams curParams) {
        this.client.secRequest(TVMessage.secRequest(39, curParams));
    }
    
    public void diseqcPositionerGotoX(final TVChannelParams curParams) {
        this.client.secRequest(TVMessage.secRequest(40, curParams));
    }
    
    public void importDatabase(final String inputXmlPath) {
        this.client.importDatabase(inputXmlPath);
    }
    
    public void exportDatabase(final String outputXmlPath) {
        this.client.exportDatabase(outputXmlPath);
    }
    
    public void importdb(final String inputPath) {
        this.client.importdb(inputPath);
    }
    
    public void exportdb(final String outputPath) {
        this.client.exportdb(outputPath);
    }
    
    public void switchAudioTrack(final int mode) {
        this.client.switchAudioTrack(mode);
        final int prog_id = this.client.getCurrentProgramID();
        if (prog_id == -1) {
            return;
        }
        final TVProgram prog = TVProgram.selectByID((Context)this, prog_id);
        if (prog == null) {
            return;
        }
        prog.setAudTrack(mode);
    }
    
    public int getAudioTrack() {
        final int prog_id = this.client.getCurrentProgramID();
        if (prog_id == -1) {
            return 0;
        }
        final TVProgram prog = TVProgram.selectByID((Context)this, prog_id);
        if (prog == null) {
            return 0;
        }
        return prog.getAudTrack();
    }
    
    public void switchScreenType(final int mode) {
        String value = null;
        switch (mode) {
            case 0: {
                value = "0";
                break;
            }
            case 1: {
                value = "1";
                break;
            }
            case 2: {
                value = "2";
                break;
            }
            case 3: {
                value = "3";
                break;
            }
        }
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter("/sys/class/video/screen_mode"));
            try {
                writer.write(value);
            }
            finally {
                writer.close();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e2) {
            Log.e("TVActivity", "set screen_mode ERROR!", (Throwable)e2);
        }
    }
    
    public int getScreenType() {
        String val = null;
        final File file = new File("/sys/class/video/screen_mode");
        if (!file.exists()) {
            return 0;
        }
        try {
            final BufferedReader in = new BufferedReader(new FileReader("/sys/class/video/screen_mode"), 1);
            try {
                val = in.readLine();
            }
            finally {
                in.close();
            }
        }
        catch (Exception e) {
            Log.e("TVActivity", "IOException when read screen_mode");
        }
        if (val != null) {
            Log.d("TVActivity", "---" + val);
            if (val.equals("0:normal")) {
                return 0;
            }
            if (val.equals("1:full stretch")) {
                return 1;
            }
            if (val.equals("2:4-3")) {
                return 2;
            }
            if (val.equals("3:16-9")) {
                return 3;
            }
        }
        return 0;
    }
    
    public String getVideoWindowSize() {
        String val = null;
        final File file = new File("/sys/class/video/axis");
        if (!file.exists()) {
            return "0 0 0 0";
        }
        try {
            final BufferedReader in = new BufferedReader(new FileReader("/sys/class/video/axis"), 1);
            try {
                val = in.readLine();
            }
            finally {
                in.close();
            }
        }
        catch (Exception e) {
            Log.e("TVActivity", "IOException when read screen_mode");
        }
        if (val != null) {
            return val;
        }
        return "0 0 0 0";
    }
    
    public void setVideoWindowSize(final String val) {
        try {
            final BufferedWriter writer = new BufferedWriter(new FileWriter("/sys/class/video/axis"));
            try {
                writer.write(val);
            }
            finally {
                writer.close();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e2) {
            Log.e("TVActivity", "setVideoWindowSize ERROR!", (Throwable)e2);
        }
    }
    
    public void setBlackoutPolicy(final int val) {
        this.client.switch_video_blackout(val);
    }
    
    public void onConfigurationChanged(final Configuration config) {
        Log.d("TVActivity", "onConfigurationChanged");
        super.onConfigurationChanged(config);
    }
    
    public void tryRestDevice() {
        this.client.tryRestDevice();
    }
    
    public void disConnectPhoneService() {
        if (this.iService != null) {
            try {
                this.iService.isOpenDevice(false);
                this.unbindService(this.connection);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
