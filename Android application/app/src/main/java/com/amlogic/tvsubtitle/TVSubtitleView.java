// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvsubtitle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TVSubtitleView extends View
{
    private static final String TAG = "TVSubtitleView";
    private static Object lock;
    private static final int BUFFER_W = 1920;
    private static final int BUFFER_H = 1080;
    private static final int MODE_NONE = 0;
    private static final int MODE_DTV_TT = 1;
    private static final int MODE_DTV_CC = 2;
    private static final int MODE_DVB_SUB = 3;
    private static final int MODE_ATV_TT = 4;
    private static final int MODE_ATV_CC = 5;
    private static final int PLAY_NONE = 0;
    private static final int PLAY_SUB = 1;
    private static final int PLAY_TT = 2;
    public static final int COLOR_RED = 0;
    public static final int COLOR_GREEN = 1;
    public static final int COLOR_YELLOW = 2;
    public static final int COLOR_BLUE = 3;
    private static int init_count;
    private int disp_left;
    private int disp_right;
    private int disp_top;
    private int disp_bottom;
    private boolean active;
    private static SubParams sub_params;
    private static TTParams tt_params;
    private static int play_mode;
    private static boolean visible;
    private static boolean destroy;
    private static Bitmap bitmap;
    private static TVSubtitleView activeView;
    
    private native int native_sub_init();
    
    private native int native_sub_destroy();
    
    private native int native_sub_lock();
    
    private native int native_sub_unlock();
    
    private native int native_sub_clear();
    
    private native int native_sub_start_dvb_sub(final int p0, final int p1, final int p2, final int p3);
    
    private native int native_sub_start_dtv_tt(final int p0, final int p1, final int p2, final int p3, final int p4, final boolean p5);
    
    private native int native_sub_stop_dvb_sub();
    
    private native int native_sub_stop_dtv_tt();
    
    private native int native_sub_tt_goto(final int p0);
    
    private native int native_sub_tt_color_link(final int p0);
    
    private native int native_sub_tt_home_link();
    
    private native int native_sub_tt_next(final int p0);
    
    private native int native_sub_tt_set_search_pattern(final String p0, final boolean p1);
    
    private native int native_sub_tt_search_next(final int p0);
    
    protected native int native_get_subtitle_picture_width();
    
    protected native int native_get_subtitle_picture_height();
    
    private native int native_sub_start_atsc_cc(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final int p6);
    
    private native int native_sub_stop_atsc_cc();
    
    private native int native_sub_set_active(final boolean p0);
    
    private void update() {
        this.postInvalidate();
    }
    
    private void stopDecoder() {
        synchronized (TVSubtitleView.lock) {
            Label_0132: {
                switch (TVSubtitleView.play_mode) {
                    case 2: {
                        switch (TVSubtitleView.tt_params.mode) {
                            case 1: {
                                this.native_sub_stop_dtv_tt();
                                break Label_0132;
                            }
                            default: {
                                break Label_0132;
                            }
                        }
//                        break;
                    }
                    case 1: {
                        switch (TVSubtitleView.sub_params.mode) {
                            case 1: {
                                this.native_sub_stop_dtv_tt();
                                break Label_0132;
                            }
                            case 3: {
                                this.native_sub_stop_dvb_sub();
                                break Label_0132;
                            }
                            case 2: {
                                this.native_sub_stop_atsc_cc();
                                break Label_0132;
                            }
                        }
                        break;
                    }
                }
            }
            TVSubtitleView.play_mode = 0;
        }
    }
    
    private void init() {
        synchronized (TVSubtitleView.lock) {
            if (TVSubtitleView.init_count == 0) {
                TVSubtitleView.play_mode = 0;
                TVSubtitleView.visible = true;
                TVSubtitleView.destroy = false;
                TVSubtitleView.tt_params = new TTParams();
                TVSubtitleView.sub_params = new SubParams();
                if (TVSubtitleView.bitmap == null) {
                    TVSubtitleView.bitmap = Bitmap.createBitmap(1920, 1080, Bitmap.Config.ARGB_8888);
                }
                if (this.native_sub_init() < 0) {}
            }
            ++TVSubtitleView.init_count;
        }
    }
    
    public TVSubtitleView(final Context context) {
        super(context);
        this.disp_left = 0;
        this.disp_right = 0;
        this.disp_top = 0;
        this.disp_bottom = 0;
        this.active = true;
        this.init();
    }
    
    public TVSubtitleView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.disp_left = 0;
        this.disp_right = 0;
        this.disp_top = 0;
        this.disp_bottom = 0;
        this.active = true;
        this.init();
    }
    
    public TVSubtitleView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.disp_left = 0;
        this.disp_right = 0;
        this.disp_top = 0;
        this.disp_bottom = 0;
        this.active = true;
        this.init();
    }
    
    public void setMargin(final int left, final int top, final int right, final int bottom) {
        this.disp_left = left;
        this.disp_top = top;
        this.disp_right = right;
        this.disp_bottom = bottom;
    }
    
    public void setActive(final boolean active) {
        synchronized (TVSubtitleView.lock) {
            this.native_sub_set_active(active);
            this.active = active;
            if (active) {
                TVSubtitleView.activeView = this;
            }
            this.postInvalidate();
        }
    }
    
    public void setSubParams(final DVBSubParams params) {
        synchronized (TVSubtitleView.lock) {
            TVSubtitleView.sub_params.mode = 3;
            TVSubtitleView.sub_params.dvb_sub = params;
            if (TVSubtitleView.play_mode == 1) {
                this.startSub();
            }
        }
    }
    
    public void setSubParams(final DTVTTParams params) {
        synchronized (TVSubtitleView.lock) {
            TVSubtitleView.sub_params.mode = 1;
            TVSubtitleView.sub_params.dtv_tt = params;
            if (TVSubtitleView.play_mode == 1) {
                this.startSub();
            }
        }
    }
    
    public void setSubParams(final DTVCCParams params) {
        synchronized (TVSubtitleView.lock) {
            TVSubtitleView.sub_params.mode = 2;
            TVSubtitleView.sub_params.dtv_cc = params;
            if (TVSubtitleView.play_mode == 1) {
                this.startSub();
            }
        }
    }
    
    public void setTTParams(final DTVTTParams params) {
        synchronized (TVSubtitleView.lock) {
            TVSubtitleView.tt_params.mode = 1;
            TVSubtitleView.tt_params.dtv_tt = params;
            if (TVSubtitleView.play_mode == 2) {
                this.startTT();
            }
        }
    }
    
    public void show() {
        if (TVSubtitleView.visible) {
            return;
        }
        Log.d("TVSubtitleView", "show");
        TVSubtitleView.visible = true;
        this.update();
    }
    
    public void hide() {
        if (!TVSubtitleView.visible) {
            return;
        }
        Log.d("TVSubtitleView", "hide");
        TVSubtitleView.visible = false;
        this.update();
    }
    
    public void startTT() {
        synchronized (TVSubtitleView.lock) {
            if (TVSubtitleView.activeView != this) {
                return;
            }
            this.stopDecoder();
            if (TVSubtitleView.tt_params.mode == 0) {
                return;
            }
            int ret = 0;
            switch (TVSubtitleView.tt_params.mode) {
                case 1: {
                    ret = this.native_sub_start_dtv_tt(TVSubtitleView.tt_params.dtv_tt.dmx_id, TVSubtitleView.tt_params.dtv_tt.region_id, TVSubtitleView.tt_params.dtv_tt.pid, TVSubtitleView.tt_params.dtv_tt.page_no, TVSubtitleView.tt_params.dtv_tt.sub_page_no, false);
                    break;
                }
            }
            if (ret >= 0) {
                TVSubtitleView.play_mode = 2;
            }
        }
    }
    
    public void startSub() {
        synchronized (TVSubtitleView.lock) {
            if (TVSubtitleView.activeView != this) {
                return;
            }
            this.stopDecoder();
            if (TVSubtitleView.sub_params.mode == 0) {
                return;
            }
            int ret = 0;
            switch (TVSubtitleView.sub_params.mode) {
                case 3: {
                    ret = this.native_sub_start_dvb_sub(TVSubtitleView.sub_params.dvb_sub.dmx_id, TVSubtitleView.sub_params.dvb_sub.pid, TVSubtitleView.sub_params.dvb_sub.composition_page_id, TVSubtitleView.sub_params.dvb_sub.ancillary_page_id);
                    break;
                }
                case 1: {
                    ret = this.native_sub_start_dtv_tt(TVSubtitleView.sub_params.dtv_tt.dmx_id, TVSubtitleView.sub_params.dtv_tt.region_id, TVSubtitleView.sub_params.dtv_tt.pid, TVSubtitleView.sub_params.dtv_tt.page_no, TVSubtitleView.sub_params.dtv_tt.sub_page_no, true);
                    break;
                }
                case 2: {
                    ret = this.native_sub_start_atsc_cc(TVSubtitleView.sub_params.dtv_cc.caption_mode, TVSubtitleView.sub_params.dtv_cc.fg_color, TVSubtitleView.sub_params.dtv_cc.fg_opacity, TVSubtitleView.sub_params.dtv_cc.bg_color, TVSubtitleView.sub_params.dtv_cc.bg_opacity, TVSubtitleView.sub_params.dtv_cc.font_style, TVSubtitleView.sub_params.dtv_cc.font_size);
                    break;
                }
            }
            if (ret >= 0) {
                TVSubtitleView.play_mode = 1;
            }
        }
    }
    
    public void stop() {
        synchronized (TVSubtitleView.lock) {
            if (TVSubtitleView.activeView != this) {
                return;
            }
            this.stopDecoder();
        }
    }
    
    public void clear() {
        synchronized (TVSubtitleView.lock) {
            if (TVSubtitleView.activeView != this) {
                return;
            }
            this.stopDecoder();
            this.native_sub_clear();
            TVSubtitleView.tt_params.mode = 0;
            TVSubtitleView.sub_params.mode = 0;
        }
    }
    
    public void nextPage() {
        synchronized (TVSubtitleView.lock) {
            if (TVSubtitleView.activeView != this) {
                return;
            }
            if (TVSubtitleView.play_mode != 2) {
                return;
            }
            this.native_sub_tt_next(1);
        }
    }
    
    public void previousPage() {
        synchronized (TVSubtitleView.lock) {
            if (TVSubtitleView.activeView != this) {
                return;
            }
            if (TVSubtitleView.play_mode != 2) {
                return;
            }
            this.native_sub_tt_next(-1);
        }
    }
    
    public void gotoPage(final int page) {
        synchronized (TVSubtitleView.lock) {
            if (TVSubtitleView.activeView != this) {
                return;
            }
            if (TVSubtitleView.play_mode != 2) {
                return;
            }
            this.native_sub_tt_goto(page);
        }
    }
    
    public void goHome() {
        synchronized (TVSubtitleView.lock) {
            if (TVSubtitleView.activeView != this) {
                return;
            }
            if (TVSubtitleView.play_mode != 2) {
                return;
            }
            this.native_sub_tt_home_link();
        }
    }
    
    public void colorLink(final int color) {
        synchronized (TVSubtitleView.lock) {
            if (TVSubtitleView.activeView != this) {
                return;
            }
            if (TVSubtitleView.play_mode != 2) {
                return;
            }
            this.native_sub_tt_color_link(color);
        }
    }
    
    public void setSearchPattern(final String pattern, final boolean casefold) {
        synchronized (TVSubtitleView.lock) {
            if (TVSubtitleView.activeView != this) {
                return;
            }
            if (TVSubtitleView.play_mode != 2) {
                return;
            }
            this.native_sub_tt_set_search_pattern(pattern, casefold);
        }
    }
    
    public void searchNext() {
        synchronized (TVSubtitleView.lock) {
            if (TVSubtitleView.activeView != this) {
                return;
            }
            if (TVSubtitleView.play_mode != 2) {
                return;
            }
            this.native_sub_tt_search_next(1);
        }
    }
    
    public void searchPrevious() {
        synchronized (TVSubtitleView.lock) {
            if (TVSubtitleView.activeView != this) {
                return;
            }
            if (TVSubtitleView.play_mode != 2) {
                return;
            }
            this.native_sub_tt_search_next(-1);
        }
    }
    
    public void onDraw(final Canvas canvas) {
        synchronized (TVSubtitleView.lock) {
            final Rect dr = new Rect(this.disp_left, this.disp_top, this.getWidth() - this.disp_right, this.getHeight() - this.disp_bottom);
            if (!this.active || !TVSubtitleView.visible || TVSubtitleView.play_mode == 0) {
                return;
            }
            this.native_sub_lock();
            Rect sr;
            if (TVSubtitleView.play_mode == 2 || TVSubtitleView.sub_params.mode == 1 || TVSubtitleView.sub_params.mode == 4) {
                sr = new Rect(0, 0, 492, 250);
            }
            else if (TVSubtitleView.play_mode == 1) {
                sr = new Rect(0, 0, this.native_get_subtitle_picture_width(), this.native_get_subtitle_picture_height());
            }
            else {
                sr = new Rect(0, 0, 1920, 1080);
            }
            final Bitmap bitmap2 = Bitmap.createScaledBitmap(TVSubtitleView.bitmap, 1920, 1080, true);
            canvas.drawBitmap(bitmap2, sr, dr, new Paint());
            this.native_sub_unlock();
        }
    }
    
    public void dispose() {
        synchronized (TVSubtitleView.lock) {
            if (!TVSubtitleView.destroy) {
                --TVSubtitleView.init_count;
                TVSubtitleView.destroy = true;
                if (TVSubtitleView.init_count == 0) {
                    this.stopDecoder();
                    this.native_sub_clear();
                    this.native_sub_destroy();
                }
            }
        }
    }
    
    protected void finalize() throws Throwable {
        super.finalize();
    }
    
    static {
        TVSubtitleView.lock = new Object();
        TVSubtitleView.init_count = 0;
        System.loadLibrary("am_adp");
        System.loadLibrary("am_mw");
        System.loadLibrary("zvbi");
        System.loadLibrary("jnitvsubtitle");
        TVSubtitleView.play_mode = 0;
        TVSubtitleView.bitmap = null;
        TVSubtitleView.activeView = null;
    }
    
    public static class DVBSubParams
    {
        private int dmx_id;
        private int pid;
        private int composition_page_id;
        private int ancillary_page_id;
        
        public DVBSubParams(final int dmx_id, final int pid, final int page_id, final int anc_page_id) {
            this.dmx_id = dmx_id;
            this.pid = pid;
            this.composition_page_id = page_id;
            this.ancillary_page_id = anc_page_id;
        }
    }
    
    public static class DTVTTParams
    {
        private int dmx_id;
        private int pid;
        private int page_no;
        private int sub_page_no;
        private int region_id;
        
        public DTVTTParams(final int dmx_id, final int pid, final int page_no, final int sub_page_no, final int region_id) {
            this.dmx_id = dmx_id;
            this.pid = pid;
            this.page_no = page_no;
            this.sub_page_no = sub_page_no;
            this.region_id = region_id;
        }
    }
    
    public static class ATVTTParams
    {
    }
    
    public static class DTVCCParams
    {
        private int caption_mode;
        private int fg_color;
        private int fg_opacity;
        private int bg_color;
        private int bg_opacity;
        private int font_style;
        private int font_size;
        
        public DTVCCParams(final int caption, final int fg_color, final int fg_opacity, final int bg_color, final int bg_opacity, final int font_style, final int font_size) {
            this.caption_mode = caption;
            this.fg_color = fg_color;
            this.fg_opacity = fg_opacity;
            this.bg_color = bg_color;
            this.bg_opacity = bg_opacity;
            this.font_style = font_style;
            this.font_size = font_size;
        }
    }
    
    public static class ATVCCParams
    {
    }
    
    private class SubParams
    {
        int mode;
        DVBSubParams dvb_sub;
        DTVTTParams dtv_tt;
        ATVTTParams atv_tt;
        DTVCCParams dtv_cc;
        ATVCCParams atv_cc;
        
        private SubParams() {
            this.mode = 0;
        }
    }
    
    private class TTParams
    {
        int mode;
        DTVTTParams dtv_tt;
        ATVTTParams atv_tt;
        
        private TTParams() {
            this.mode = 0;
        }
    }
}
