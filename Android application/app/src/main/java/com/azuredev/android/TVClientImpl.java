package com.azuredev.android;

import android.content.Context;
import android.widget.VideoView;

import com.amlogic.tvclient.TVClient;
import com.amlogic.tvutil.TVChannelParams;
import com.amlogic.tvutil.TVConst;
import com.amlogic.tvutil.TVMessage;
import com.amlogic.tvutil.TVScanParams;

public class TVClientImpl extends TVClient {
    private VideoView videoView;
    private boolean connected;
    private final TVConst.SourceInput[] sourceInputArray = {TVConst.SourceInput.SOURCE_DTV, TVConst.SourceInput.SOURCE_HDMI1, TVConst.SourceInput.SOURCE_HDMI2};

    @Override
    public void onConnected() {
        connected = true;
        // startScanATSC(); // we trigger the scan when connected
        setInputSource(TVConst.SourceInput.SOURCE_DTV);
        openVideo();
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

    public void setInputSource(String source) {
        if (source.equalsIgnoreCase("HDMI1")) {
            setInputSource(TVConst.SourceInput.SOURCE_HDMI1);
        } else if (source.equalsIgnoreCase("HDMI2")) {
            setInputSource(TVConst.SourceInput.SOURCE_HDMI2);
        } else if (source.equalsIgnoreCase("DIGITAL")) {
            setInputSource(TVConst.SourceInput.SOURCE_DTV);
        }
    }

    /**
     * Function that loop on the source input and takes the next in array
     */
    public void setNextInputSource() {
        //we get the current input
        TVConst.SourceInput currentInput = getCurInputSource();

        //we find the next input
        for (int i = 0; i < sourceInputArray.length; i++) {
            if (currentInput == sourceInputArray[i]) {
                setInputSource(sourceInputArray[(i + 1) % sourceInputArray.length]);
            }
        }
    }

    public void updateVideoWindow() {
        if (this.videoView == null || !this.connected) {
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
    }

    public void openVideo() {
        this.videoView.getHolder().setFormat(258);
        this.updateVideoWindow();
    }

    public void onCreate(final Context context, final VideoView videoView) {
        this.videoView = videoView;
        connect(context);
    }

    /**
     * ATSC is the digital television standard of north america
     */
    public void startScanATSC() {
        TVScanParams sp;

        //supposed to be faster than TVScanParams.dtvAllbandScanParams(0, TVChannelParams.MODE_ATSC);
        sp = TVScanParams.adtvScanParams(0, TVChannelParams.MODE_ATSC);

        startScan(sp);
    }

}
