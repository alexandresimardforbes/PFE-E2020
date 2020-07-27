// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.amlogic.tvdataprovider.TVDataProvider;

import java.util.ArrayList;

public class TVProgram implements Parcelable
{
    private static final String TAG = "TVProgram";
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_TV = 1;
    public static final int TYPE_RADIO = 2;
    public static final int TYPE_ATV = 3;
    public static final int TYPE_DATA = 4;
    public static final int TYPE_DTV = 5;
    public static final int TYPE_PLAYBACK = 6;
    public static final int SOURCE_DVBS = 1;
    public static final int SOURCE_DVBC = 2;
    public static final int SOURCE_DVBT = 4;
    public static final int SOURCE_ATSC = 8;
    public static final int SOURCE_ANALOG = 16;
    public static final int SOURCE_DTMB = 32;
    private Context context;
    private int id;
    private int dvbServiceID;
    private int type;
    private String name;
    private TVProgramNumber number;
    private int channelID;
    private TVChannel channel;
    private int skip;
    private boolean lock;
    private boolean scrambled;
    private boolean favorite;
    private int volume;
    private int src;
    private int sourceID;
    private int audioTrack;
    private int pmtPID;
    private int pcrPID;
    private int dvbt2_plp_id;
    private int netID;
    private int encrypt;
    public static final Parcelable.Creator<TVProgram> CREATOR;
    private Video video;
    private Audio[] audioes;
    private Subtitle[] subtitles;
    private Teletext[] teletexts;

    public static Parcelable.Creator<TVProgram> getCreator() {
        return TVProgram.CREATOR;
    }

    public TVProgram(final Parcel in) {
        this.readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.dvbServiceID);
        dest.writeInt(this.type);
        dest.writeString(this.name);
        dest.writeParcelable((Parcelable)this.number, 0);
        dest.writeInt(this.channelID);
        dest.writeParcelable((Parcelable)this.channel, 0);
        dest.writeInt(this.skip);
        dest.writeInt((int)(this.lock ? 1 : 0));
        dest.writeInt((int)(this.scrambled ? 1 : 0));
        dest.writeInt((int)(this.favorite ? 1 : 0));
        dest.writeInt(this.volume);
        dest.writeInt(this.src);
        dest.writeInt(this.sourceID);
        dest.writeInt(this.audioTrack);
        dest.writeInt(this.pmtPID);
        dest.writeInt(this.pcrPID);
        dest.writeInt(this.dvbt2_plp_id);
        dest.writeInt(this.netID);
        dest.writeInt(this.encrypt);
    }

    public void readFromParcel(final Parcel in) {
        this.id = in.readInt();
        this.dvbServiceID = in.readInt();
        this.type = in.readInt();
        this.name = in.readString();
        this.number = (TVProgramNumber)in.readParcelable(TVProgramNumber.class.getClassLoader());
        this.channelID = in.readInt();
        this.channel = (TVChannel)in.readParcelable(TVChannel.class.getClassLoader());
        this.skip = in.readInt();
        this.lock = (in.readInt() != 0);
        this.lock = (in.readInt() != 0);
        this.scrambled = (in.readInt() != 0);
        this.favorite = (in.readInt() != 0);
        this.volume = in.readInt();
        this.src = in.readInt();
        this.sourceID = in.readInt();
        this.audioTrack = in.readInt();
        this.pmtPID = in.readInt();
        this.pcrPID = in.readInt();
        this.dvbt2_plp_id = in.readInt();
        this.netID = in.readInt();
        this.encrypt = in.readInt();
    }

    private int cgetInt(final Cursor c, final int col) {
        return ( col < 0)? 0: c.getInt(col);
    }

    private void constructFromCursor(final Context context, final Cursor c) {
        this.context = context;
        int col = c.getColumnIndex("db_id");
        if (col < 0)
            return;
        this.id = cgetInt(c,col);
        col = c.getColumnIndex("source_id");
        this.sourceID = cgetInt(c,col);
        col = c.getColumnIndex("src");
        this.src = cgetInt(c,col);
        col = c.getColumnIndex("service_id");
        this.dvbServiceID = cgetInt(c,col);
        col = c.getColumnIndex("db_net_id");
        this.netID = cgetInt(c,col);
        col = c.getColumnIndex("encrypt");
        this.encrypt = cgetInt(c,col);
        col = c.getColumnIndex("db_ts_id");
        this.channelID = cgetInt(c,col);
        col = c.getColumnIndex("name");
        this.name = this.parsingName(c.getString(col));
        if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            col = c.getColumnIndex("chan_num");
        }
        else {
            col = c.getColumnIndex("lcn");
        }
        final int num = cgetInt(c,col);
        col = c.getColumnIndex("major_chan_num");
        final int major = cgetInt(c,col);
        col = c.getColumnIndex("minor_chan_num");
        final int minor = cgetInt(c,col);
        col = c.getColumnIndex("aud_track");
        this.audioTrack = cgetInt(c,col);
        col = c.getColumnIndex("dvbt2_plp_id");
        this.dvbt2_plp_id = cgetInt(c,col);
        if (this.src == 3 || (this.src == 4 && major > 0)) {
            this.number = new TVProgramNumber(major, minor);
        }
        else {
            this.number = new TVProgramNumber(num);
        }
        col = c.getColumnIndex("service_type");
        final int type = cgetInt(c,col);
        col = c.getColumnIndex("pmt_pid");
        this.pmtPID = cgetInt(c,col);
        col = c.getColumnIndex("pcr_pid");
        this.pcrPID = cgetInt(c,col);
        if (type == 1) {
            this.type = 1;
        }
        else if (type == 2) {
            this.type = 2;
        }
        else if (type == 3) {
            this.type = 3;
        }
        else if (type == 6) {
            this.type = 6;
        }
        else {
            this.type = 4;
        }
        col = c.getColumnIndex("skip");
        this.skip = cgetInt(c,col);
        col = c.getColumnIndex("lock");
        this.lock = (cgetInt(c,col) != 0);
        col = c.getColumnIndex("scrambled_flag");
        this.scrambled = (cgetInt(c,col) != 0);
        col = c.getColumnIndex("favor");
        this.favorite = (cgetInt(c,col) != 0);
        col = c.getColumnIndex("volume");
        this.volume = cgetInt(c,col);
        col = c.getColumnIndex("vid_pid");
        int pid = cgetInt(c,col);
        col = c.getColumnIndex("vid_fmt");
        int fmt = cgetInt(c,col);
        this.video = new Video(pid, fmt);
        String[] pids = null;
        String[] fmts = null;
        String[] langs = null;
        col = c.getColumnIndex("aud_pids");
        String str = c.getString(col);
        if (str != null && str.length() != 0) {
            pids = str.split(" ");
        }
        col = c.getColumnIndex("aud_fmts");
        str = c.getString(col);
        if (str != null && str.length() != 0) {
            fmts = str.split(" ");
        }
        col = c.getColumnIndex("aud_langs");
        str = c.getString(col);
        if (str != null && str.length() != 0) {
            langs = str.split(" ");
        }
        int count;
        if (pids != null) {
            count = pids.length;
            this.audioes = new Audio[count];
        }
        else {
            count = 0;
            this.audioes = null;
        }
        for (int i = 0; i < count; ++i) {
            pid = Integer.parseInt(pids[i]);
            fmt = Integer.parseInt(fmts[i]);
            String lang;
            if (langs != null && i < langs.length) {
                lang = langs[i];
            }
            else {
                lang = " ";
            }
            this.audioes[i] = new Audio(pid, lang, fmt);
        }
        String[] cids = null;
        String[] aids = null;
        pids = null;
        langs = null;
        col = c.getColumnIndex("sub_pids");
        str = c.getString(col);
        if (str != null && str.length() != 0) {
            pids = str.split(" ");
        }
        col = c.getColumnIndex("sub_composition_page_ids");
        str = c.getString(col);
        if (str != null && str.length() != 0) {
            cids = str.split(" ");
        }
        col = c.getColumnIndex("sub_ancillary_page_ids");
        str = c.getString(col);
        if (str != null && str.length() != 0) {
            aids = str.split(" ");
        }
        col = c.getColumnIndex("sub_langs");
        str = c.getString(col);
        if (str != null && str.length() != 0) {
            langs = str.split(" ");
        }
        if (pids != null) {
            count = pids.length;
        }
        else {
            count = 0;
            this.subtitles = null;
        }
        int ttx_count = 0;
        int ttx_sub_count = 0;
        String[] ttx_pids = null;
        String[] ttx_types = null;
        String[] mag_nos = null;
        String[] page_nos = null;
        String[] ttx_langs = null;
        col = c.getColumnIndex("ttx_pids");
        str = c.getString(col);
        if (str != null && str.length() != 0) {
            ttx_pids = str.split(" ");
        }
        col = c.getColumnIndex("ttx_types");
        str = c.getString(col);
        if (str != null && str.length() != 0) {
            ttx_types = str.split(" ");
        }
        col = c.getColumnIndex("ttx_magazine_nos");
        str = c.getString(col);
        if (str != null && str.length() != 0) {
            mag_nos = str.split(" ");
        }
        col = c.getColumnIndex("ttx_page_nos");
        str = c.getString(col);
        if (str != null && str.length() != 0) {
            page_nos = str.split(" ");
        }
        col = c.getColumnIndex("ttx_langs");
        str = c.getString(col);
        if (str != null && str.length() != 0) {
            ttx_langs = str.split(" ");
        }
        if (ttx_pids != null) {
            for (int i = 0; i < ttx_pids.length; ++i) {
                final int ttype = Integer.parseInt(ttx_types[i]);
                if (ttype == 2 || ttype == 5) {
                    ++ttx_sub_count;
                }
                else {
                    ++ttx_count;
                }
            }
            if (ttx_count > 0) {
                this.teletexts = new Teletext[ttx_count];
            }
            else {
                this.teletexts = null;
            }
        }
        else {
            ttx_count = 0;
            this.teletexts = null;
        }
        if (count + ttx_sub_count > 0) {
            this.subtitles = new Subtitle[count + ttx_sub_count];
        }
        else {
            this.subtitles = null;
        }
        for (int i = 0; i < count; ++i) {
            this.subtitles[i] = new Subtitle(Integer.parseInt(pids[i]), langs[i], 1, Integer.parseInt(cids[i]), Integer.parseInt(aids[i]));
        }
        int ittx = 0;
        int isubttx = 0;
        for (int i = 0; i < ttx_sub_count + ttx_count; ++i) {
            final int ttype2 = Integer.parseInt(ttx_types[i]);
            if (ttype2 == 2 || ttype2 == 5) {
                this.subtitles[isubttx + count] = new Subtitle(Integer.parseInt(ttx_pids[i]), ttx_langs[i], 2, Integer.parseInt(mag_nos[i]), Integer.parseInt(page_nos[i]));
                ++isubttx;
            }
            else {
                this.teletexts[ittx++] = new Teletext(Integer.parseInt(ttx_pids[i]), ttx_langs[i], Integer.parseInt(mag_nos[i]), Integer.parseInt(page_nos[i]));
            }
        }
    }

    private Cursor selectProgramInChannelByNumber(final Context context, final int channelID, final TVProgramNumber num) {
        String cmd = "select * from srv_table where db_ts_id = " + channelID + " and ";
        if (num.isATSCMode()) {
            cmd = cmd + "major_chan_num = " + num.getMajor() + " and minor_chan_num = " + num.getMinor();
        }
        else if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd = cmd + "chan_num = " + num.getNumber();
        }
        else {
            cmd = cmd + "lcn = " + num.getNumber();
        }
        return context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
    }

    private TVProgram(final Context context, final Cursor c) {
        this.constructFromCursor(context, c);
    }

    private static String getCurrentSourceString(final Context context) {
        String strSource = "(";
        int selectSources = 0;
        final String strInputSource = TVConfigResolver.getConfig(context, "tv:input_source", "MPEG");
        final String strDtvMode = TVConfigResolver.getConfig(context, "tv:dtv:mode", "DVBC");
        final boolean mixAtvDtv = TVConfigResolver.getConfig(context, "tv:mix_atv_dtv", false);
        final boolean mixAllDtv = TVConfigResolver.getConfig(context, "tv:dtv:mix_all_modes", false);
        if (strInputSource.equalsIgnoreCase("ATV") || mixAtvDtv) {
            selectSources |= 0x10;
        }
        if (strInputSource.equalsIgnoreCase("DTV") || mixAtvDtv) {
            if (mixAllDtv) {
                selectSources |= 0xFFFFFFEF;
            }
            else if (strDtvMode.equalsIgnoreCase("dvbc")) {
                selectSources |= 0x2;
            }
            else if (strDtvMode.equalsIgnoreCase("dvbt")) {
                selectSources |= 0x4;
            }
            else if (strDtvMode.equalsIgnoreCase("dvbs")) {
                selectSources |= 0x1;
            }
            else if (strDtvMode.equalsIgnoreCase("atsc")) {
                selectSources |= 0x8;
            }
            else if (strDtvMode.equalsIgnoreCase("dtmb")) {
                selectSources |= 0x20;
            }
        }
        if (selectSources == 0) {
            strSource += "src >= 0";
        }
        else {
            if ((selectSources & 0x1) != 0x0) {
                strSource += "src = 0";
            }
            if ((selectSources & 0x2) != 0x0) {
                if (strSource.length() > 1) {
                    strSource += " or ";
                }
                strSource += "src = 1";
            }
            if ((selectSources & 0x4) != 0x0) {
                if (strSource.length() > 1) {
                    strSource += " or ";
                }
                strSource += "src = 2";
            }
            if ((selectSources & 0x8) != 0x0) {
                if (strSource.length() > 1) {
                    strSource += " or ";
                }
                strSource += "src = 3";
            }
            if ((selectSources & 0x10) != 0x0) {
                if (strSource.length() > 1) {
                    strSource += " or ";
                }
                strSource += "src = 4";
            }
            if ((selectSources & 0x20) != 0x0) {
                if (strSource.length() > 1) {
                    strSource += " or ";
                }
                strSource += "src = 5";
            }
        }
        if (strSource.length() > 1) {
            strSource += ")";
        }
        else {
            strSource = "";
        }
        return strSource;
    }

    public TVProgram(final Context context, final int channelID, final int type, final TVProgramNumber num, final int skipFlag) {
        final TVChannel channel = TVChannel.selectByID(context, channelID);
        if (channel == null) {
            Log.d("TVProgram", "Cannot add new program, invalid channel id " + channelID);
            this.id = -1;
        }
        else {
            final TVChannelParams params = channel.getParams();
            final Cursor c = this.selectProgramInChannelByNumber(context, channelID, num);
            if (c != null) {
                if (c.moveToFirst()) {
                    this.constructFromCursor(context, c);
                }
                else {
                    String cmd = "insert into srv_table(db_net_id,db_ts_id,service_id,src,name,service_type,";
                    cmd += "eit_schedule_flag,eit_pf_flag,running_status,free_ca_mode,volume,aud_track,vid_pid,";
                    cmd += "vid_fmt,aud_pids,aud_fmts,aud_langs,skip,lock,chan_num,major_chan_num,";
                    cmd += "minor_chan_num,access_controlled,hidden,hide_guide,source_id,favor,current_aud,";
                    cmd += "db_sat_para_id,scrambled_flag,lcn,hd_lcn,sd_lcn,default_chan_num,chan_order) ";
                    cmd = cmd + "values(-1," + channelID + ",65535," + params.getMode() + ",''," + type + ",";
                    cmd += "0,0,0,0,0,0,8191,";
                    final int chanNum = num.isATSCMode() ? (num.getMajor() << 16 | num.getMinor()) : num.getNumber();
                    final int majorNum = num.isATSCMode() ? num.getMajor() : 0;
                    cmd = cmd + "-1,'','',''," + skipFlag + ",0," + chanNum + "," + majorNum + ",";
                    cmd = cmd + "" + num.getMinor() + ",0,0,0,-1,0,-1,";
                    cmd += "-1,0,-1,-1,-1,-1,0)";
                    context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
                    final Cursor cr = this.selectProgramInChannelByNumber(context, channelID, num);
                    if (cr != null) {
                        if (cr.moveToFirst()) {
                            this.constructFromCursor(context, cr);
                        }
                        else {
                            Log.d("TVProgram", "Cannot add new program, sqlite error");
                            this.id = -1;
                        }
                        cr.close();
                    }
                }
                c.close();
            }
        }
    }

    public TVProgram() {
    }

    public TVProgram(final int vpid, final int vfmt, final int apid, final int afmt) {
        this.video = new Video(vpid, vfmt);
        (this.audioes = new Audio[1])[0] = new Audio(apid, "eng", afmt);
    }

    public TVProgram(final Context context, final String name, final int type, final Video vid, final Audio[] auds, final Subtitle[] subs, final Teletext[] ttxs) {
        final int vpid = (vid != null) ? vid.getPID() : 8191;
        final int vfmt = (vid != null) ? vid.getFormat() : -1;
        String apids = "";
        String afmts = "";
        String alangs = "";
        if (auds != null && auds.length > 0) {
            apids += auds[0].getPID();
            afmts += auds[0].getFormat();
            alangs += auds[0].getLang();
            for (int i = 1; i < auds.length; ++i) {
                apids = apids + " " + auds[i].getPID();
                afmts = afmts + " " + auds[i].getFormat();
                alangs = alangs + " " + auds[i].getLang();
            }
        }
        final ArrayList dvbSubsList = new ArrayList();
        final ArrayList ttxSubsList = new ArrayList();
        Subtitle[] dvbSubs = null;
        Subtitle[] ttxSubs = null;
        for (int j = 0; subs != null && j < subs.length; ++j) {
            if (subs[j].getType() == 2) {
                ttxSubsList.add(subs[j]);
            }
            else {
                dvbSubsList.add(subs[j]);
            }
        }
        dvbSubs = (Subtitle[]) dvbSubsList.toArray(new Subtitle[0]);
        ttxSubs = (Subtitle[]) ttxSubsList.toArray(new Subtitle[0]);
        String spids = "";
        String stypes = "";
        String scpgids = "";
        String sapgids = "";
        String slangs = "";
        if (dvbSubsList.size() > 0) {
            spids += dvbSubs[0].getPID();
            stypes += dvbSubs[0].getType();
            scpgids += dvbSubs[0].getCompositionPageID();
            sapgids += dvbSubs[0].getAncillaryPageID();
            slangs += dvbSubs[0].getLang();
            for (int k = 1; k < dvbSubs.length; ++k) {
                spids = spids + " " + dvbSubs[k].getPID();
                stypes = stypes + " " + dvbSubs[k].getType();
                scpgids = scpgids + " " + dvbSubs[k].getCompositionPageID();
                sapgids = sapgids + " " + dvbSubs[k].getAncillaryPageID();
                slangs = slangs + " " + dvbSubs[k].getLang();
            }
        }
        String tpids = "";
        String ttypes = "";
        String tmagnums = "";
        String tpgnums = "";
        String tlangs = "";
        if (ttxs != null && ttxs.length > 0) {
            tpids += ttxs[0].getPID();
            tmagnums += ttxs[0].getMagazineNumber();
            tpgnums += ttxs[0].getPageNumber();
            tlangs += ttxs[0].getLang();
//            ttypes++;
            for (int l = 1; l < ttxs.length; ++l) {
                tpids = tpids + " " + ttxs[l].getPID();
                tmagnums = tmagnums + " " + ttxs[l].getMagazineNumber();
                tpgnums = tpgnums + " " + ttxs[l].getPageNumber();
                tlangs = tlangs + " " + ttxs[l].getLang();
                ttypes += " 1";
            }
            for (int l = 0; l < ttxSubsList.size(); ++l) {
                tpids = tpids + " " + ttxSubs[l].getPID();
                tmagnums = tmagnums + " " + ttxSubs[l].getCompositionPageID();
                tpgnums = tpgnums + " " + ttxSubs[l].getAncillaryPageID();
                tlangs = tlangs + " " + ttxSubs[l].getLang();
                ttypes += " 2";
            }
        }
        else if (ttxSubsList.size() > 0) {
            tpids += ttxSubs[0].getPID();
            tmagnums += ttxSubs[0].getCompositionPageID();
            tpgnums += ttxSubs[0].getAncillaryPageID();
            tlangs += ttxSubs[0].getLang();
            ttypes += 2;
            for (int l = 1; l < ttxSubsList.size(); ++l) {
                tpids = tpids + " " + ttxSubs[l].getPID();
                tmagnums = tmagnums + " " + ttxSubs[l].getCompositionPageID();
                tpgnums = tpgnums + " " + ttxSubs[l].getAncillaryPageID();
                tlangs = tlangs + " " + ttxSubs[l].getLang();
                ttypes += " 2";
            }
        }
        boolean newInsert = true;
        if (type == 6) {
            String cmd = "select * from srv_table where service_type = " + type;
            final Cursor cr = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
            if (cr != null) {
                if (cr.moveToFirst()) {
                    final int col = cr.getColumnIndex("db_id");
                    final int db_id = cr.getInt(col);
                    cmd = "update srv_table set ";
                    cmd = cmd + "name='" + sqliteEscape(name) + "',";
                    cmd = cmd + "vid_pid=" + vpid + ",vid_fmt=" + vfmt + ",";
                    cmd = cmd + "current_aud=-1,aud_pids='" + apids + "',aud_fmts='" + afmts + "',aud_langs='" + alangs + "',";
                    cmd = cmd + "current_sub=-1,sub_pids='" + spids + "',sub_types='" + stypes + "',sub_composition_page_ids='" + scpgids + "',sub_ancillary_page_ids='" + sapgids + "',sub_langs='" + slangs + "',";
                    cmd = cmd + "current_ttx=-1,ttx_pids='" + tpids + "',ttx_types='" + ttypes + "',ttx_magazine_nos='" + tmagnums + "',ttx_page_nos='" + tpgnums + "',ttx_langs='" + tlangs + "' ";
                    cmd = cmd + "where db_id=" + db_id;
                    context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
                    newInsert = false;
                }
                cr.close();
            }
        }
        if (newInsert) {
            String cmd = "insert into srv_table(db_net_id,db_ts_id,service_id,src,name,service_type,";
            cmd += "eit_schedule_flag,eit_pf_flag,running_status,free_ca_mode,volume,aud_track,vid_pid,";
            cmd += "vid_fmt,aud_pids,aud_fmts,aud_langs,skip,lock,chan_num,major_chan_num,";
            cmd += "minor_chan_num,access_controlled,hidden,hide_guide,source_id,favor,current_aud,";
            cmd += "current_sub,sub_pids,sub_types,sub_composition_page_ids,sub_ancillary_page_ids,sub_langs,";
            cmd += "current_ttx,ttx_pids,ttx_types,ttx_magazine_nos,ttx_page_nos,ttx_langs,";
            cmd += "db_sat_para_id,scrambled_flag,lcn,hd_lcn,sd_lcn,default_chan_num,chan_order) ";
            cmd = cmd + "values(-1,-1,65535,-1,'" + sqliteEscape(name) + "'," + type + ",";
            cmd = cmd + "0,0,0,0,0,0," + vpid + ",";
            cmd = cmd + "" + vfmt + ",'" + apids + "','" + afmts + "','" + alangs + "',0,0,0,0,";
            cmd += "0,0,0,0,-1,0,-1,";
            cmd = cmd + "-1,'" + spids + "','" + stypes + "','" + scpgids + "','" + sapgids + "','" + slangs + "',";
            cmd = cmd + "-1,'" + tpids + "','" + ttypes + "','" + tmagnums + "','" + tpgnums + "','" + tlangs + "',";
            cmd += "-1,0,-1,-1,-1,-1,0)";
            context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
        }
        String cmd = "select * from srv_table where vid_pid=" + vpid + " and vid_fmt=" + vfmt + " and service_type = " + type;
        final Cursor cr = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (cr != null) {
            if (cr.moveToFirst()) {
                this.constructFromCursor(context, cr);
            }
            else {
                Log.d("TVProgram", "Cannot add new program, sqlite error");
                this.id = -1;
            }
            cr.close();
        }
    }

    public static TVProgram selectByID(final Context context, final int id) {
        TVProgram p = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from srv_table where srv_table.db_id = " + id, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                p = new TVProgram(context, c);
            }
            c.close();
        }
        return p;
    }

    public static TVProgram selectByNumber(final Context context, final int type, final TVProgramNumber num) {
        TVProgram p = null;
        String cmd = "select * from srv_table where " + getCurrentSourceString(context) + " and ";
        if (type != 0) {
            if (type == 5) {
                cmd += "(service_type = 1 or service_type = 2) and ";
            }
            else {
                cmd = cmd + "service_type = " + type + " and ";
            }
        }
        if (num.isATSCMode()) {
            if (num.getMinor() < 0) {
                p = selectByNumber(context, 5, new TVProgramNumber(num.getMajor(), 1, 3));
                if (p == null) {
                    p = selectByNumber(context, 3, new TVProgramNumber(num.getMajor(), 0, 0));
                }
                return p;
            }
            if (num.getMinor() >= 1) {
                final int minorCheck = num.getMinorCheck();
                if (minorCheck == 1) {
                    cmd = cmd + "major_chan_num = " + num.getMajor() + " and minor_chan_num >= " + num.getMinor() + " ";
                    cmd += "order by minor_chan_num DESC limit 1";
                }
                else if (minorCheck == 2) {
                    cmd = cmd + "major_chan_num = " + num.getMajor() + " and minor_chan_num <= " + num.getMinor() + " ";
                    cmd += "order by minor_chan_num limit 1";
                }
                else if (minorCheck == 3) {
                    cmd = cmd + "major_chan_num = " + num.getMajor() + " and minor_chan_num >= " + num.getMinor() + " ";
                    cmd += "order by minor_chan_num limit 1";
                }
                else if (minorCheck == 4) {
                    cmd = cmd + "major_chan_num = " + num.getMajor() + " and minor_chan_num <= " + num.getMinor() + " ";
                    cmd += "order by minor_chan_num DESC limit 1";
                }
                else {
                    cmd = cmd + "major_chan_num = " + num.getMajor() + " and minor_chan_num = " + num.getMinor();
                }
            }
            else {
                cmd = cmd + "major_chan_num = " + num.getMajor() + " and minor_chan_num = " + num.getMinor();
            }
        }
        else if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd = cmd + "chan_num = " + num.getNumber();
        }
        else {
            cmd = cmd + "lcn = " + num.getNumber();
        }
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                p = new TVProgram(context, c);
            }
            c.close();
        }
        return p;
    }

    private static int slectChanNum(final Context context, final int type, final TVProgramNumber num) {
        int chanNum = 0;
        String cmd = "select * from srv_table where skip=0 and " + getCurrentSourceString(context) + " and ";
        if (type != 0) {
            if (type == 5) {
                cmd += "(service_type = 1 or service_type = 2) and ";
            }
            else {
                cmd = cmd + "service_type = " + type + " and ";
            }
        }
        if (num.isATSCMode()) {
            cmd = cmd + "(major_chan_num = " + num.getMajor() + " and minor_chan_num = " + num.getMinor() + ") ";
        }
        else if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd = cmd + "chan_num = " + num.getNumber() + " ";
        }
        else {
            cmd = cmd + "lcn = " + num.getNumber() + " ";
        }
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                chanNum = c.getInt(c.getColumnIndex("chan_num"));
            }
            c.close();
        }
        return chanNum;
    }

    public static TVProgram selectUp(final Context context, final int type, final TVProgramNumber num) {
        TVProgram p = null;
        int chan_num = 0;
        String cmd = "select * from srv_table where skip=0 and " + getCurrentSourceString(context) + " and ";
        if (type != 0) {
            if (type == 5) {
                cmd += "(service_type = 1 or service_type = 2) and ";
            }
            else {
                cmd = cmd + "service_type = " + type + " and ";
            }
        }
        if (num.isATSCMode()) {
            chan_num = slectChanNum(context, type, num);
            cmd = cmd + "chan_num > " + chan_num + " ";
        }
        else if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd = cmd + "chan_num > " + num.getNumber() + " ";
        }
        else {
            cmd = cmd + "lcn > " + num.getNumber() + " ";
        }
        if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd += "order by chan_num";
        }
        else {
            cmd += "order by lcn";
        }
        Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                p = new TVProgram(context, c);
            }
            c.close();
        }
        if (p != null) {
            return p;
        }
        cmd = "select * from srv_table where skip=0 and " + getCurrentSourceString(context) + " and ";
        if (type != 0) {
            if (type == 5) {
                cmd += "(service_type = 1 or service_type = 2) and ";
            }
            else {
                cmd = cmd + "service_type = " + type + " and ";
            }
        }
        if (num.isATSCMode()) {
            cmd = cmd + "chan_num < " + chan_num + " ";
        }
        else if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd = cmd + "chan_num < " + num.getNumber() + " ";
        }
        else {
            cmd = cmd + "lcn < " + num.getNumber() + " ";
        }
        if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd += "order by chan_num";
        }
        else {
            cmd += "order by lcn";
        }
        c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                p = new TVProgram(context, c);
                Log.d("TVProgram", "selectUp " + p.getNumber().getMinor());
            }
            c.close();
        }
        return p;
    }

    public static TVProgram selectDown(final Context context, final int type, final TVProgramNumber num) {
        TVProgram p = null;
        int chan_num = 0;
        String cmd = "select * from srv_table where skip=0 and " + getCurrentSourceString(context) + " and ";
        if (type != 0) {
            if (type == 5) {
                cmd += "(service_type = 1 or service_type = 2) and ";
            }
            else {
                cmd = cmd + "service_type = " + type + " and ";
            }
        }
        if (num.isATSCMode()) {
            chan_num = slectChanNum(context, type, num);
            cmd = cmd + "chan_num < " + chan_num + " ";
        }
        else if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd = cmd + "chan_num < " + num.getNumber() + " ";
        }
        else {
            cmd = cmd + "lcn < " + num.getNumber() + " ";
        }
        if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd += "order by chan_num desc";
        }
        else {
            cmd += "order by lcn desc";
        }
        Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                p = new TVProgram(context, c);
            }
            c.close();
        }
        if (p != null) {
            return p;
        }
        cmd = "select * from srv_table where skip=0 and " + getCurrentSourceString(context) + " and ";
        if (type != 0) {
            if (type == 5) {
                cmd += "(service_type = 1 or service_type = 2) and ";
            }
            else {
                cmd = cmd + "service_type = " + type + " and ";
            }
        }
        if (num.isATSCMode()) {
            cmd = cmd + "chan_num > " + chan_num + " ";
        }
        else if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd = cmd + "chan_num > " + num.getNumber() + " ";
        }
        else {
            cmd = cmd + "lcn > " + num.getNumber() + " ";
        }
        if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd += "order by chan_num desc";
        }
        else {
            cmd += "order by lcn desc";
        }
        c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                p = new TVProgram(context, c);
            }
            c.close();
        }
        return p;
    }

    public static TVProgram selectFirstValid(final Context context, final int type) {
        TVProgram p = null;
        if (type == 1 || type == 5 || type == 0) {
            String cmd = "select * from srv_table where " + getCurrentSourceString(context) + " and ";
            if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
                cmd += "skip = 0 and service_type = 1 order by chan_num";
            }
            else {
                cmd += "skip = 0 and service_type = 1 order by lcn";
            }
            final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
            if (c != null) {
                if (c.moveToFirst()) {
                    p = new TVProgram(context, c);
                }
                c.close();
            }
            if (p != null) {
                return p;
            }
        }
        if (type == 2 || type == 5 || type == 0) {
            String cmd = "select * from srv_table where " + getCurrentSourceString(context) + " and ";
            if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
                cmd += "skip = 0 and service_type = 2 order by chan_num";
            }
            else {
                cmd += "skip = 0 and service_type = 2 order by lcn";
            }
            final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
            if (c != null) {
                if (c.moveToFirst()) {
                    p = new TVProgram(context, c);
                }
                c.close();
            }
            if (p != null) {
                return p;
            }
        }
        if (type == 3 || type == 0) {
            String cmd = "select * from srv_table where " + getCurrentSourceString(context) + " and ";
            if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
                cmd += "skip = 0 and service_type = 3 order by chan_num";
            }
            else {
                cmd += "skip = 0 and service_type = 3 order by lcn";
            }
            final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
            if (c != null) {
                if (c.moveToFirst()) {
                    p = new TVProgram(context, c);
                }
                c.close();
            }
            if (p != null) {
                return p;
            }
        }
        return null;
    }

    public static TVProgram[] selectAll(final Context context, final boolean no_skip) {
        return selectByType(context, 0, no_skip);
    }

    public static TVProgram[] selectByType(final Context context, final int type, final int skip) {
        TVProgram[] p = null;
        boolean where = false;
        String cmd = "select * from srv_table ";
        if (type == 5) {
            cmd += "where (service_type = 1 or service_type = 2) ";
            where = true;
        }
        else if (type != 0) {
            cmd = cmd + "where service_type = " + type + " ";
            where = true;
        }
        if (where) {
            cmd = cmd + " and skip = " + skip + " ";
        }
        else {
            cmd = cmd + " where skip = " + skip + " ";
        }
        cmd = cmd + " and " + getCurrentSourceString(context);
        if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd += " order by chan_order";
        }
        else {
            cmd += " order by lcn";
        }
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                p = new TVProgram[c.getCount()];
                do {
                    p[id++] = new TVProgram(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return p;
    }

    public static TVProgram[] selectByType(final Context context, final int type, final boolean no_skip) {
        TVProgram[] p = null;
        boolean where = false;
        String cmd = "select * from srv_table ";
        if (type == 5) {
            cmd += "where (service_type = 1 or service_type = 2) ";
            where = true;
        }
        else if (type != 0) {
            cmd = cmd + "where service_type = " + type + " ";
            where = true;
        }
        if (no_skip) {
            if (where) {
                cmd += " and skip = 0 ";
            }
            else {
                cmd += " where skip = 0 ";
            }
        }
        else if (where) {
            cmd += " and skip <= 1 ";
        }
        else {
            cmd += " where skip <= 1 ";
        }
        cmd = cmd + " and " + getCurrentSourceString(context);
        if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd += " order by chan_num";
        }
        else {
            cmd += " order by lcn";
        }
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                p = new TVProgram[c.getCount()];
                do {
                    p[id++] = new TVProgram(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return p;
    }

    public static TVProgram[] selectByChannel(final Context context, final int channelID, final int type) {
        TVProgram[] p = null;
        boolean where = false;
        String cmd = "select * from srv_table ";
        if (type == 5) {
            cmd += "where (service_type = 1 or service_type = 2) ";
            where = true;
        }
        else if (type != 0) {
            cmd = cmd + "where service_type = " + type + " ";
            where = true;
        }
        cmd = cmd + " and db_ts_id = " + channelID + " order by chan_order";
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                p = new TVProgram[c.getCount()];
                do {
                    p[id++] = new TVProgram(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return p;
    }

    public static TVProgram[] selectBySatID(final Context context, final int sat_id) {
        TVProgram[] p = null;
        final String cmd = "select * from srv_table where db_sat_para_id = " + sat_id + " and (service_type = " + 1 + " or service_type = " + 2 + ") ";
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                p = new TVProgram[c.getCount()];
                do {
                    p[id++] = new TVProgram(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return p;
    }

    public static TVProgram[] selectBySatIDAndType(final Context context, final int sat_id, final int type) {
        TVProgram[] p = null;
        String cmd;
        if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd = "select * from srv_table where db_sat_para_id = " + sat_id + " and service_type = " + type + " order by chan_num";
        }
        else {
            cmd = "select * from srv_table where db_sat_para_id = " + sat_id + " and service_type = " + type + " order by lcn";
        }
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                p = new TVProgram[c.getCount()];
                do {
                    p[id++] = new TVProgram(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return p;
    }

    public static TVProgram[] selectByName(final Context context, final String key) {
        TVProgram[] p = null;
        String cmd = "select * from srv_table where name like '%" + key + "%'";
        cmd = cmd + " and " + getCurrentSourceString(context);
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                p = new TVProgram[c.getCount()];
                do {
                    p[id++] = new TVProgram(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return p;
    }

    public static TVProgram[] selectByNameAndType(final Context context, final String key, final int type) {
        TVProgram[] p = null;
        String cmd = "select * from srv_table where name like '%" + key + "%'" + " and service_type = " + type;
        cmd = cmd + " and " + getCurrentSourceString(context);
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                p = new TVProgram[c.getCount()];
                do {
                    p[id++] = new TVProgram(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return p;
    }

    public static void tvProgramDelByChannelID(final Context context, final int channel_id) {
        TVProgram[] p = null;
        int idx = 0;
        int count = 0;
        Log.d("TVProgram", "tvProgramDelByChannelID:" + channel_id);
        final String cmd = "select * from srv_table where db_ts_id = " + channel_id;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        count = c.getCount();
        if (c != null) {
            if (c.moveToFirst()) {
                p = new TVProgram[count];
                do {
                    p[idx++] = new TVProgram(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        for (idx = 0; idx < count; ++idx) {
            TVEvent.tvEventDelBySrvID(context, p[idx].getID());
            p[idx].deleteSubtitle();
            p[idx].deleteTeletext();
            p[idx].deleteFromGroupBySrvID();
        }
        final Cursor cur = context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "delete from srv_table where db_ts_id = " + channel_id, (String[])null, (String)null);
        if (cur != null) {
            cur.close();
        }
    }

    public static void tvProgramDelBySatID(final Context context, final int sat_id) {
        TVProgram[] p = null;
        int idx = 0;
        int count = 0;
        Log.d("TVProgram", "tvProgramDelBySatID:" + sat_id);
        final String cmd = "select * from srv_table where db_sat_para_id = " + sat_id;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        count = c.getCount();
        if (c != null) {
            if (c.moveToFirst()) {
                p = new TVProgram[count];
                do {
                    p[idx++] = new TVProgram(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        for (idx = 0; idx < count; ++idx) {
            TVEvent.tvEventDelBySrvID(context, p[idx].getID());
            p[idx].deleteSubtitle();
            p[idx].deleteTeletext();
            p[idx].deleteFromGroupBySrvID();
        }
        final Cursor cur = context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "delete from srv_table where db_sat_para_id = " + sat_id, (String[])null, (String)null);
        if (cur != null) {
            cur.close();
        }
    }

    public int getID() {
        return this.id;
    }

    public int getAudTrack() {
        return this.audioTrack;
    }

    public void setAudTrack(final int aud) {
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update srv_table set aud_track = " + aud + " where srv_table.db_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }

    public int getDVBServiceID() {
        return this.dvbServiceID;
    }

    public int getDVBTsID() {
        return this.channelID;
    }

    public int getDVBNetID() {
        return this.netID;
    }

    public boolean isEncrypt() {
        Log.i("TVProgram", "encrypt>>>>>>>>" + this.encrypt);
        return this.encrypt == 1;
    }

    public int getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public TVProgramNumber getNumber() {
        return this.number;
    }

    public Video getVideo() {
        return this.video;
    }

    public int getAudioCount() {
        if (this.audioes == null) {
            return 0;
        }
        return this.audioes.length;
    }

    public Audio getAudio() {
        return this.getAudio(null);
    }

    public Audio getAudio(int id) {
        if (this.audioes == null || this.audioes.length == 0) {
            return null;
        }
        if (id >= this.audioes.length) {
            id = 0;
        }
        return this.audioes[id];
    }

    public Audio getAudio(final String lang) {
        if (this.audioes == null) {
            return null;
        }
        if (lang != null) {
            for (int i = 0; i < this.audioes.length; ++i) {
                if (this.audioes[i].getLang().equals(lang)) {
                    return this.audioes[i];
                }
            }
        }
        return this.audioes[0];
    }

    public Audio[] getAllAudio() {
        return this.audioes;
    }

    public void setCurrentAudio(final int id) {
        if (this.audioes == null || id < 0 || id >= this.audioes.length) {
            Log.d("TVProgram", "Invalid audio id " + id);
            return;
        }
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update srv_table set current_aud=" + id + " where db_id = " + this.id, (String[])null, (String)null);
    }

    public int getCurrentAudio(final String defaultLang) {
        int id = -1;
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select current_aud from srv_table where db_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                id = c.getInt(0);
                if (id < 0 && this.audioes != null) {
                    if (defaultLang != null) {
                        for (int i = 0; i < this.audioes.length; ++i) {
                            if (this.audioes[i].getLang().equals(defaultLang)) {
                                id = i;
                                break;
                            }
                        }
                    }
                    if (id < 0) {
                        id = 0;
                    }
                }
            }
            c.close();
        }
        return id;
    }

    private void selectSubtitle() {
    }

    private void deleteSubtitle() {
        Log.d("TVProgram", "deleteSubtitle NOT IMPLEMENT YET!");
    }

    public int getSubtitleCount() {
        this.selectSubtitle();
        if (this.subtitles == null) {
            return 0;
        }
        return this.subtitles.length;
    }

    public Subtitle getSubtitle() {
        return this.getSubtitle(null);
    }

    public Subtitle getSubtitle(int id) {
        this.selectSubtitle();
        if (this.subtitles == null) {
            return null;
        }
        if (id >= this.subtitles.length) {
            id = 0;
        }
        return this.subtitles[id];
    }

    public Subtitle getSubtitle(final String lang) {
        this.selectSubtitle();
        if (this.subtitles == null || this.subtitles.length == 0) {
            return null;
        }
        if (lang != null) {
            for (int i = 0; i < this.subtitles.length; ++i) {
                if (this.subtitles[i].getLang().equals(lang)) {
                    return this.subtitles[i];
                }
            }
        }
        return this.subtitles[0];
    }

    public Subtitle[] getAllSubtitle() {
        this.selectSubtitle();
        return this.subtitles;
    }

    public void setCurrentSubtitle(final int id) {
        if (this.subtitles == null || id < 0 || id >= this.subtitles.length) {
            Log.d("TVProgram", "Invalid subtitle id " + id);
            return;
        }
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update srv_table set current_sub=" + id + " where db_id = " + this.id, (String[])null, (String)null);
    }

    public int getCurrentSubtitle(final String defaultLang) {
        int id = -1;
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select current_sub from srv_table where db_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                id = c.getInt(0);
                if (id < 0 && this.subtitles != null) {
                    if (defaultLang != null) {
                        for (int i = 0; i < this.subtitles.length; ++i) {
                            if (this.subtitles[i].getLang().equals(defaultLang)) {
                                id = i;
                                break;
                            }
                        }
                    }
                    if (id < 0) {
                        id = 0;
                    }
                }
            }
            c.close();
        }
        return id;
    }

    private void selectTeletext() {
    }

    private void deleteTeletext() {
        Log.d("TVProgram", "deleteTeletext NOT IMPLEMENT YET!");
    }

    public int getTeletextCount() {
        this.selectTeletext();
        if (this.teletexts == null) {
            return 0;
        }
        return this.teletexts.length;
    }

    public Teletext getTeletext() {
        return this.getTeletext(null);
    }

    public Teletext getTeletext(int id) {
        this.selectTeletext();
        if (this.teletexts == null) {
            return null;
        }
        if (id >= this.teletexts.length) {
            id = 0;
        }
        return this.teletexts[id];
    }

    public Teletext getTeletext(final String lang) {
        this.selectTeletext();
        if (this.teletexts == null || this.teletexts.length == 0) {
            return null;
        }
        if (lang != null) {
            for (int i = 0; i < this.teletexts.length; ++i) {
                if (this.teletexts[i].getLang().equals(lang)) {
                    return this.teletexts[i];
                }
            }
        }
        return this.teletexts[0];
    }

    public Teletext[] getAllTeletext() {
        this.selectTeletext();
        return this.teletexts;
    }

    public void setCurrentTeletext(final int id) {
        if (this.teletexts == null || id < 0 || id >= this.teletexts.length) {
            Log.d("TVProgram", "Invalid teletext id " + id);
            return;
        }
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update srv_table set current_ttx=" + id + " where db_id = " + this.id, (String[])null, (String)null);
    }

    public int getCurrentTeletext(final String defaultLang) {
        int id = -1;
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select current_ttx from srv_table where db_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                id = c.getInt(0);
                if (id < 0 && this.teletexts != null) {
                    if (defaultLang != null) {
                        for (int i = 0; i < this.teletexts.length; ++i) {
                            if (this.teletexts[i].getLang().equals(defaultLang)) {
                                id = i;
                                break;
                            }
                        }
                    }
                    if (id < 0) {
                        id = 0;
                    }
                }
            }
            c.close();
        }
        return id;
    }

    public TVChannel getChannel() {
        if (this.channel == null) {
            this.channel = TVChannel.selectByID(this.context, this.channelID);
        }
        return this.channel;
    }

    public boolean getLockFlag() {
        return this.lock;
    }

    public boolean getSkipFlag() {
        return this.skip != 0;
    }

    public int getSkip() {
        return this.skip;
    }

    public boolean getScrambledFlag() {
        return this.scrambled;
    }

    public boolean getFavoriteFlag() {
        return this.favorite;
    }

    public int getVolume() {
        return this.volume;
    }

    public int getPmtPID() {
        return this.pmtPID;
    }

    public void setLockFlag(final boolean f) {
        this.lock = f;
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update srv_table set lock = " + (f ? 1 : 0) + " where srv_table.db_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }

    public void setSkipFlag(final boolean f) {
        this.skip = (f ? 1 : 0);
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update srv_table set skip = " + (f ? 1 : 0) + " where srv_table.db_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }

    public void setFavoriteFlag(final boolean f) {
        this.favorite = f;
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update srv_table set favor = " + (f ? 1 : 0) + " where srv_table.db_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }

    public TVEvent getPresentEvent(final Context context, final long now) {
        final int time = (int)(now / 1000L);
        TVEvent evt = null;
        String cmd = "select * from evt_table where evt_table.";
        if (this.src == 3) {
            cmd = cmd + "source_id = " + this.sourceID + " and db_ts_id =" + this.channelID;
        }
        else {
            cmd = cmd + "db_srv_id = " + this.getID();
        }
        cmd = cmd + " and evt_table.start <= " + time + " and evt_table.end > " + time;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                evt = new TVEvent(context, c);
            }
            c.close();
        }
        return evt;
    }

    public TVEvent getFollowingEvent(final Context context, final long now) {
        final int time = (int)(now / 1000L);
        TVEvent evt = null;
        String cmd = "select * from evt_table where evt_table.";
        if (this.src == 3) {
            cmd = cmd + "source_id = " + this.sourceID;
        }
        else {
            cmd = cmd + "db_srv_id = " + this.getID();
        }
        cmd = cmd + " and evt_table.start > " + time + " order by evt_table.start";
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                evt = new TVEvent(context, c);
            }
            c.close();
        }
        return evt;
    }

    public TVEvent[] getScheduleEvents(final Context context, final long start, final long duration) {
        final int begin = (int)(start / 1000L);
        final int end = (int)((start + duration) / 1000L);
        TVEvent[] evts = null;
        String cmd = "select * from evt_table where evt_table.";
        if (this.src == 3) {
            cmd = cmd + "source_id = " + this.sourceID + " and db_ts_id =" + this.channelID;
        }
        else {
            cmd = cmd + "db_srv_id = " + this.getID();
        }
        cmd += " and ";
        cmd = cmd + " ((start < " + begin + " and end > " + begin + ") ||";
        cmd = cmd + " (start >= " + begin + " and start < " + end + "))";
        cmd += " order by evt_table.start";
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                evts = new TVEvent[c.getCount()];
                int id = 0;
                do {
                    evts[id++] = new TVEvent(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return evts;
    }

    public static TVProgram[] selectByFavorite(final Context context, final boolean no_skip) {
        TVProgram[] p = null;
        boolean where = false;
        String cmd = "select * from srv_table ";
        cmd += "where favor = 1 ";
        where = true;
        if (no_skip) {
            if (where) {
                cmd += " and skip = 0 ";
            }
            else {
                cmd += " where skip = 0 ";
            }
        }
        cmd = cmd + " and " + getCurrentSourceString(context);
        cmd += " order by chan_order";
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                p = new TVProgram[c.getCount()];
                do {
                    p[id++] = new TVProgram(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return p;
    }

    public static TVProgram[] selectByGroupMap(final Context context, final int group_id, final boolean no_skip) {
        TVProgram[] p = null;
        String cmd = "select * from srv_table left join grp_map_table on srv_table.db_id = grp_map_table.db_srv_id where grp_map_table.db_grp_id=" + group_id;
        cmd = cmd + " and srv_table.skip = " + (no_skip ? 0 : 1) + " ";
        cmd = cmd + " and " + getCurrentSourceString(context);
        cmd += " order by chan_order";
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                p = new TVProgram[c.getCount()];
                do {
                    p[id++] = new TVProgram(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return p;
    }

    public static String sqliteEscape(String keyWord) {
        keyWord = keyWord.replace("'", "''");
        return keyWord;
    }

    public static TVProgram selectByNameAndServiceId(final Context context, final String name, final int service_id, final int type) {
        TVProgram p = null;
        String cmd = "select * from srv_table where name = '" + sqliteEscape(name) + "'" + " and service_type = " + type;
        cmd = cmd + " and " + getCurrentSourceString(context);
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                p = new TVProgram(context, c);
            }
            c.close();
        }
        if (p != null) {
            return p;
        }
        return null;
    }

    public void addProgramToGroup(final int id) {
        final int group_id = id;
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "insert into grp_map_table  (db_srv_id, db_grp_id) values (" + this.id + " ," + group_id + ")", (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }

    public void deleteFromGroup(final int id) {
        final int group_id = id;
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "delete from grp_map_table where db_srv_id = " + this.id + " and db_grp_id = " + group_id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }

    private void deleteFromGroupBySrvID() {
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "delete from grp_map_table where db_srv_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }

    public boolean checkGroup(final int id) {
        boolean b = false;
        final String cmd = "select * from grp_map_table where db_srv_id = " + this.id + " and db_grp_id = " + id;
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            if (c.getCount() > 0) {
                b = true;
            }
            c.close();
        }
        return b;
    }

    public void deleteFromDb() {
        final int group_id = this.id;
        Cursor c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "delete from grp_map_table where db_srv_id = " + this.id, (String[])null, (String)null);
        c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "delete from srv_table where db_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }

    public static void modifyChanOrder(final Context context, final int id, final int pos) {
        String cmd = "update srv_table set";
        if (!TVConfigResolver.getConfig(context, "tv:dtv:dvbt:lcn", false)) {
            cmd += " chan_num";
        }
        cmd = cmd + " = " + pos + " where db_id = " + id;
        final Cursor c = context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }

    public void setProgramName(final String name) {
        this.name = name;
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update srv_table set name = '" + sqliteEscape(name) + "'" + " where srv_table.db_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }

    public void setProgramSkip(final boolean myskip) {
        this.setSkipFlag(myskip);
    }

    public void setProgramNumber(final int number) {
        if (this.number.getNumber() != number) {
            this.number = new TVProgramNumber(number);
            final Cursor c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update srv_table set chan_num = " + number + " where srv_table.db_id = " + this.id, (String[])null, (String)null);
            if (c != null) {
                c.close();
            }
        }
    }

    public void setProgramVolume(final int mvolume) {
        if (this.volume != mvolume) {
            this.volume = mvolume;
            final Cursor c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update srv_table set volume = " + mvolume + " where srv_table.db_id = " + this.id, (String[])null, (String)null);
            if (c != null) {
                c.close();
            }
        }
    }

    public int getDvbt2PlpID() {
        return this.dvbt2_plp_id;
    }

    public int getPCRPID() {
        return this.pcrPID;
    }

    public void setEncrypt(final int encrypt) {
        final Cursor c = this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, "update srv_table set encrypt = " + encrypt + " where srv_table.db_id = " + this.id, (String[])null, (String)null);
        if (c != null) {
            c.close();
        }
    }

    public String parsingName(final String name) {
        String ret = TVMultilingualText.getText(this.context, name);
        if (ret == null || ret.isEmpty()) {
            ret = TVMultilingualText.getText(this.context, name, "first");
        }
        if (ret == null) {
            ret = "";
        }
        return ret;
    }

    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<TVProgram>() {
            public TVProgram createFromParcel(final Parcel in) {
                return new TVProgram(in);
            }
            
            public TVProgram[] newArray(final int size) {
                return new TVProgram[size];
            }
        };
    }
    
    public class Element
    {
        private int pid;
        
        public Element(final int pid) {
            this.pid = pid;
        }
        
        public int getPID() {
            return this.pid;
        }
    }
    
    public class MultiLangElement extends Element
    {
        private String lang;
        
        public MultiLangElement(final int pid, final String lang) {
            super(pid);
            this.lang = lang;
        }
        
        public String getLang() {
            return this.lang;
        }
    }
    
    public class Video extends Element
    {
        public static final int FORMAT_MPEG12 = 0;
        public static final int FORMAT_MPEG4 = 1;
        public static final int FORMAT_H264 = 2;
        public static final int FORMAT_MJPEG = 3;
        public static final int FORMAT_REAL = 4;
        public static final int FORMAT_JPEG = 5;
        public static final int FORMAT_VC1 = 6;
        public static final int FORMAT_AVS = 7;
        public static final int FORMAT_YUV = 8;
        public static final int FORMAT_H264MVC = 9;
        public static final int FORMAT_QJPEG = 10;
        private int format;
        
        public Video(final int pid, final int fmt) {
            super(pid);
            this.format = fmt;
        }
        
        public int getFormat() {
            return this.format;
        }
    }
    
    public class Audio extends MultiLangElement
    {
        public static final int FORMAT_MPEG = 0;
        public static final int FORMAT_PCM_S16LE = 1;
        public static final int FORMAT_AAC = 2;
        public static final int FORMAT_AC3 = 3;
        public static final int FORMAT_ALAW = 4;
        public static final int FORMAT_MULAW = 5;
        public static final int FORMAT_DTS = 6;
        public static final int FORMAT_PCM_S16BE = 7;
        public static final int FORMAT_FLAC = 8;
        public static final int FORMAT_COOK = 9;
        public static final int FORMAT_PCM_U8 = 10;
        public static final int FORMAT_ADPCM = 11;
        public static final int FORMAT_AMR = 12;
        public static final int FORMAT_RAAC = 13;
        public static final int FORMAT_WMA = 14;
        public static final int FORMAT_WMAPRO = 15;
        public static final int FORMAT_PCM_BLURAY = 16;
        public static final int FORMAT_ALAC = 17;
        public static final int FORMAT_VORBIS = 18;
        public static final int FORMAT_AAC_LATM = 19;
        public static final int FORMAT_APE = 20;
        private int format;
        
        public Audio(final int pid, final String lang, final int fmt) {
            super(pid, lang);
            this.format = fmt;
        }
        
        public int getFormat() {
            return this.format;
        }
    }
    
    public class Subtitle extends MultiLangElement
    {
        public static final int TYPE_DVB_SUBTITLE = 1;
        public static final int TYPE_DTV_TELETEXT = 2;
        public static final int TYPE_ATV_TELETEXT = 3;
        public static final int TYPE_DTV_CC = 4;
        public static final int TYPE_ATV_CC = 5;
        private int compositionPage;
        private int ancillaryPage;
        private int magazineNo;
        private int pageNo;
        private int type;
        
        public Subtitle(final int pid, final String lang, final int type, final int num1, final int num2) {
            super(pid, lang);
            this.type = type;
            if (type == 1) {
                this.compositionPage = num1;
                this.ancillaryPage = num2;
            }
            else if (type == 2) {
                this.magazineNo = num1;
                this.pageNo = num2;
            }
        }
        
        public int getType() {
            return this.type;
        }
        
        public int getCompositionPageID() {
            return this.compositionPage;
        }
        
        public int getAncillaryPageID() {
            return this.ancillaryPage;
        }
        
        public int getMagazineNumber() {
            return this.magazineNo;
        }
        
        public int getPageNumber() {
            return this.pageNo;
        }
    }
    
    public class Teletext extends MultiLangElement
    {
        private int magazineNo;
        private int pageNo;
        
        public Teletext(final int pid, final String lang, final int mag, final int page) {
            super(pid, lang);
            this.magazineNo = mag;
            this.pageNo = page;
        }
        
        public int getMagazineNumber() {
            return this.magazineNo;
        }
        
        public int getPageNumber() {
            return this.pageNo;
        }
    }
}
