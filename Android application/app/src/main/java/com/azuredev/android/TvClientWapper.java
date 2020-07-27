package com.azuredev.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.amlogic.tvclient.TVClient;
import com.amlogic.tvutil.TVConst;
import com.amlogic.tvutil.TVMessage;

import java.util.List;

public class TvClientWapper extends TVClient {
    private VideoView videoView;
    private Context mContext;
    private boolean connected;
    private boolean externalVideoView;
    private boolean delay_setinput_source = false;
    private static int dtvactivity_actived_num = 0;

    @Override
    public void onConnected() {
        connected = true;
        updateVideoWindow();
        if (delay_setinput_source) {
            delay_setinput_source = false;
            setInputSource(TVConst.SourceInput.SOURCE_DTV);
        }
        openVideo();
        this.resume();
        playProgram(4);
    }

    @Override
    public void onDisconnected() {
        connected = false;
    }

    @Override
    public void onMessage(final TVMessage m) {
        solveMessage(m);
    }

    public void setInputSource(final TVConst.SourceInput source) {
        setInputSource(source.ordinal());
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

        if (loc[0] == 0 && loc[1] == 0) {
            setVideoWindow(0, 0, 0, 0);
        } else {
            final int x = loc[0];
            final int y = loc[1];
            final int w = this.videoView.getWidth();
            final int h = this.videoView.getHeight();
            setVideoWindow(x, y, w, h);
        }
    }

    private void solveMessage(final TVMessage msg) {
//        switch (msg.getType()) {
//            case 15: {
//                this.onProgramStart(msg.getProgramID());
//                this.updateSmallWindow();
//                break;
//            }
//            case 16: {
//                this.onProgramStop(msg.getProgramID());
//                break;
//            }
//            case 45: {
//                this.onPlaybackStart();
//                break;
//            }
//            case 46: {
//                this.onPlaybackStop();
//                break;
//            }
//            case 9: {
//                try {
//                    this.onConfigChanged(msg.getConfigName(), msg.getConfigValue());
//                }
//                catch (Exception e) {
//                    Log.e("TVActivity", "error in onConfigChanged");
//                }
//                break;
//            }
//            case 53: {
//                this.finish();
//                break;
//            }
//        }
    }

    public void openVideo() {
        this.externalVideoView = true;
        this.videoView.getHolder().setFormat(258);
        this.updateVideoWindow();
        this.startPhoneSerVer();
    }

    public void startPhoneSerVer() {
        if (getBooleanConfig("tv:dtv:phone:watch")) {
//            if (!this.isServiceRunning("com.amlogic.tvbox.PhoneServer")) {
//                final Intent intent2 = new Intent("com.amlogic.tvbox.PhoneServer");
//                intent2.setFlags(268435456);
//                this.startService(intent2);
//            }
//            this.bindService(new Intent("com.amlogic.tvbox.PhoneServer"), this.connection, 1);
        }
    }

    public void onCreate(final Context context, final VideoView videoView) {
        this.videoView = videoView;
        this.mContext = context;
        connect(this.mContext);

        DTVActivity_ActivedStateManage(true);
    }

    private void DTVActivity_ActivedStateManage(boolean actived) {
//        Log.d(TAG, "DTVActivity_ActivedStateManage actived " + actived + " dtvactivity_actived_num " + dtvactivity_actived_num);
        if (actived) {
            if (dtvactivity_actived_num == 0) {
                /* stop music play when dtv actived */
//                Intent stopMusicIntent = new Intent();
//                stopMusicIntent.setAction("com.android.music.musicservicecommand.pause");
//                stopMusicIntent.putExtra("command", "stop");
//                sendBroadcast(stopMusicIntent);

//                Intent exitMusicIntent = new Intent();
//                exitMusicIntent.setAction("com.android.music.musicservicecommand.pause");
//                exitMusicIntent.putExtra("command", "exit");
//                sendBroadcast(exitMusicIntent);

                /* set vpath */
                if (connected) {
                    setInputSource(TVConst.SourceInput.SOURCE_DTV);
                } else {
                    delay_setinput_source = true;
                }

//                Log.d(TAG, "DTVActivity_ActivedStateManage actived stop music set vpath");
            }

            dtvactivity_actived_num++;
        } else {
            dtvactivity_actived_num--;

            if (dtvactivity_actived_num == 0) {
                /* reset vpath, this is borrow SOURCE_ATV parameter */
                setInputSource(TVConst.SourceInput.SOURCE_ATV);
            }
        }
    }
}
