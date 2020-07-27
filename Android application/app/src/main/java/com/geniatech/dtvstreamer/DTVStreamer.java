// 
// Decompiled by Procyon v0.5.36
// 

package com.geniatech.dtvstreamer;

public class DTVStreamer
{
    private static native boolean nativeStreamerCreate(final int p0);
    
    private static native boolean nativeStreamerDestroy();
    
    private static native boolean nativeStreamerInit();
    
    private static native boolean nativeStreamerDeinit();
    
    private static native boolean nativeStreamerStartStream(final String p0, final int[] p1);
    
    private static native boolean nativeStreamerStopStream(final String p0);
    
    public DTVStreamer(final int listenPort) {
        nativeStreamerCreate(listenPort);
    }
    
    @Override
    protected void finalize() {
        nativeStreamerDestroy();
    }
    
    public boolean init() {
        return nativeStreamerInit();
    }
    
    public boolean deinit() {
        return nativeStreamerDeinit();
    }
    
    public boolean startStream(final String clientAddr, final int[] pids) {
        return nativeStreamerStartStream(clientAddr, pids);
    }
    
    public boolean stopStream(final String clientAddr) {
        return nativeStreamerStopStream(clientAddr);
    }
    
    static {
        System.loadLibrary("dtvstreamer_jni");
    }
}
