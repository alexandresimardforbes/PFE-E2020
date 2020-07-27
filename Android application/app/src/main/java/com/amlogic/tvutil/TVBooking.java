// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.amlogic.tvdataprovider.TVDataProvider;

import java.io.File;
import java.util.ArrayList;

public class TVBooking
{
    private static final String TAG = "TVBooking";
    public static final int FL_PLAY = 1;
    public static final int FL_RECORD = 2;
    public static final int ST_WAIT_START = 0;
    public static final int ST_CANCELLED = 1;
    public static final int ST_STARTED = 2;
    public static final int ST_END = 3;
    public static final int RP_NONE = 0;
    public static final int RP_DAILY = 1;
    public static final int RP_WEEKLY = 2;
    public static final int ERR_PARAM = -1;
    public static final int ERR_CONFLICT = -2;
    private int id;
    private int status;
    private int flag;
    private int repeat;
    private long start;
    private long duration;
    private Context context;
    private TVProgram program;
    private TVEvent event;
    private String recFilePath;
    private String recStoragePath;
    private String programName;
    private String eventName;
    private TVProgram.Video video;
    private TVProgram.Audio[] audios;
    private TVProgram.Subtitle[] subtitles;
    private TVProgram.Teletext[] teletexts;
    
    public TVBooking(final Context context, final Cursor c) {
        this.context = context;
        int col = c.getColumnIndex("db_id");
        this.id = c.getInt(col);
        col = c.getColumnIndex("db_srv_id");
        this.program = TVProgram.selectByID(context, c.getInt(col));
        col = c.getColumnIndex("db_evt_id");
        this.event = TVEvent.selectByID(context, c.getInt(col));
        col = c.getColumnIndex("status");
        this.status = c.getInt(col);
        col = c.getColumnIndex("flag");
        this.flag = c.getInt(col);
        col = c.getColumnIndex("start");
        this.start = c.getInt(col) * 1000L;
        col = c.getColumnIndex("duration");
        this.duration = c.getInt(col) * 1000L;
        col = c.getColumnIndex("srv_name");
        this.programName = c.getString(col);
        col = c.getColumnIndex("evt_name");
        this.eventName = c.getString(col);
        col = c.getColumnIndex("file_name");
        this.recFilePath = c.getString(col);
        col = c.getColumnIndex("from_storage");
        this.recStoragePath = c.getString(col);
        col = c.getColumnIndex("repeat");
        this.repeat = c.getInt(col);
        TVProgram p = this.program;
        if (p == null) {
            p = new TVProgram();
        }
        col = c.getColumnIndex("vid_pid");
        final int pid = c.getInt(col);
        col = c.getColumnIndex("vid_fmt");
        final int fmt = c.getInt(col);
        this.video = p.new Video(pid, fmt);
        col = c.getColumnIndex("aud_pids");
        String tmpStr = c.getString(col);
        final String[] apids = tmpStr.split(" ");
        col = c.getColumnIndex("aud_fmts");
        tmpStr = c.getString(col);
        final String[] afmts = tmpStr.split(" ");
        col = c.getColumnIndex("aud_languages");
        tmpStr = c.getString(col);
        final String[] alangs = tmpStr.split(" ");
        if (apids != null && apids.length > 0 && !apids[0].isEmpty()) {
            this.audios = new TVProgram.Audio[apids.length];
            for (int i = 0; i < apids.length; ++i) {
                this.audios[i] = p.new Audio(Integer.parseInt(apids[i]), alangs[i], Integer.parseInt(afmts[i]));
            }
        }
        else {
            this.audios = null;
        }
        final ArrayList subList = new ArrayList();
        col = c.getColumnIndex("sub_pids");
        tmpStr = c.getString(col);
        final String[] spids = tmpStr.split(" ");
        col = c.getColumnIndex("sub_types");
        tmpStr = c.getString(col);
        final String[] stypes = tmpStr.split(" ");
        col = c.getColumnIndex("sub_composition_page_ids");
        tmpStr = c.getString(col);
        final String[] scpgids = tmpStr.split(" ");
        col = c.getColumnIndex("sub_ancillary_page_ids");
        tmpStr = c.getString(col);
        final String[] sapgids = tmpStr.split(" ");
        col = c.getColumnIndex("sub_languages");
        tmpStr = c.getString(col);
        final String[] slangs = tmpStr.split(" ");
        if (spids != null && spids.length > 0 && !spids[0].isEmpty()) {
            for (int i = 0; i < spids.length; ++i) {
                subList.add(p.new Subtitle(Integer.parseInt(spids[i]), slangs[i], Integer.parseInt(stypes[i]), Integer.parseInt(scpgids[i]), Integer.parseInt(sapgids[i])));
            }
        }
        final ArrayList ttxList = new ArrayList();
        col = c.getColumnIndex("ttx_pids");
        tmpStr = c.getString(col);
        final String[] tpids = tmpStr.split(" ");
        col = c.getColumnIndex("ttx_magazine_numbers");
        tmpStr = c.getString(col);
        final String[] tmagnums = tmpStr.split(" ");
        col = c.getColumnIndex("ttx_page_numbers");
        tmpStr = c.getString(col);
        final String[] tpgnums = tmpStr.split(" ");
        col = c.getColumnIndex("ttx_languages");
        tmpStr = c.getString(col);
        final String[] tlangs = tmpStr.split(" ");
        if (tpids != null && tpids.length > 0 && !tpids[0].isEmpty()) {
            for (int i = 0; i < tpids.length; ++i) {
                ttxList.add(p.new Teletext(Integer.parseInt(tpids[i]), tlangs[i], Integer.parseInt(tmagnums[i]), Integer.parseInt(tpgnums[i])));
            }
        }
        this.subtitles = (TVProgram.Subtitle[]) subList.toArray(new TVProgram.Subtitle[0]);
        this.teletexts = (TVProgram.Teletext[]) ttxList.toArray(new TVProgram.Teletext[0]);
    }
    
    public TVBooking(final TVProgram program, final long start, final long duration) {
        this.id = -1;
        this.flag = 3;
        this.program = program;
        this.start = start;
        this.duration = duration;
        this.programName = program.getName();
        this.eventName = "";
        this.video = program.getVideo();
        this.audios = program.getAllAudio();
        this.subtitles = program.getAllSubtitle();
        this.teletexts = program.getAllTeletext();
    }
    
    public static String sqliteEscape(String keyWord) {
        keyWord = keyWord.replace("'", "''");
        return keyWord;
    }
    
    public static TVBooking selectByID(final Context context, final int id) {
        TVBooking p = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from booking_table where db_id = " + id, (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                p = new TVBooking(context, c);
            }
            c.close();
        }
        return p;
    }
    
    public void updateStart(final long start, final int st) {
        final String cmd = "update booking_table set start=" + start + " where db_id=" + this.id + "and status=" + st;
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
    }
    
    public static TVBooking[] selectAllBook(final Context context) {
        TVBooking[] bookings = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from booking_table order by start", (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                bookings = new TVBooking[c.getCount()];
                do {
                    bookings[id++] = new TVBooking(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return bookings;
    }
    
    public static TVBooking[] selectByStatus(final Context context, final int st) {
        TVBooking[] bookings = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from booking_table where status=" + st + " order by start", (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                bookings = new TVBooking[c.getCount()];
                do {
                    bookings[id++] = new TVBooking(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return bookings;
    }
    
    public static TVBooking[] selectRecordBookingsByStatus(final Context context, final int st) {
        TVBooking[] bookings = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from booking_table where (flag & 2) != 0 and status=" + st + " order by start", (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                bookings = new TVBooking[c.getCount()];
                do {
                    bookings[id++] = new TVBooking(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return bookings;
    }
    
    public static TVBooking[] selectPlayBookingsByStatus(final Context context, final int st) {
        TVBooking[] bookings = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from booking_table where (flag & 1) != 0 and status=" + st + " order by start", (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                bookings = new TVBooking[c.getCount()];
                do {
                    bookings[id++] = new TVBooking(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return bookings;
    }
    
    public static TVBooking[] selectAllPlayBookings(final Context context) {
        TVBooking[] bookings = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from booking_table where (flag & 1) != 0 order by start", (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                bookings = new TVBooking[c.getCount()];
                do {
                    bookings[id++] = new TVBooking(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return bookings;
    }
    
    public static TVBooking[] selectAllRecordBookings(final Context context) {
        TVBooking[] bookings = null;
        final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, "select * from booking_table where (flag & 2) != 0 order by start", (String[])null, (String)null);
        if (c != null) {
            if (c.moveToFirst()) {
                int id = 0;
                bookings = new TVBooking[c.getCount()];
                do {
                    bookings[id++] = new TVBooking(context, c);
                } while (c.moveToNext());
            }
            c.close();
        }
        return bookings;
    }
    
    public static void bookProgram(final Context context, final TVProgram program, final int flag, final long start, final long duration, final int repeat, final boolean allowConflict) throws TVBookingConflictException {
        if (program == null || start < 0L || (flag & 0x3) == 0x0) {
            Log.d("TVBooking", "Invalid param for booking program");
            return;
        }
        final int status = 0;
        if (!allowConflict) {
            final long end = start + duration;
            final int s = (int)(start / 1000L);
            final int e = (int)(end / 1000L);
            String sql = "select * from booking_table where status<=2";
            sql = sql + " and ((start<=" + s + " and (start+duration)>=" + s + ")";
            sql = sql + " or (start<=" + e + " and (start+duration)>=" + e + ")) limit 1";
            final Cursor c = context.getContentResolver().query(TVDataProvider.RD_URL, (String[])null, sql, (String[])null, (String)null);
            if (c != null) {
                if (c.moveToFirst()) {
                    final int col = c.getColumnIndex("db_id");
                    final int cid = c.getInt(col);
                    Log.d("TVBooking", "Conflict with booking " + cid + ", cannot book.");
                    c.close();
                    throw new TVBookingConflictException();
                }
                c.close();
            }
        }
        String cmd = "insert into booking_table(db_srv_id, db_evt_id, srv_name, evt_name,";
        cmd += "start,duration,flag,status,file_name,vid_pid,vid_fmt,aud_pids,aud_fmts,aud_languages,";
        cmd += "sub_pids,sub_types,sub_composition_page_ids,sub_ancillary_page_ids,sub_languages,";
        cmd += "ttx_pids,ttx_types,ttx_magazine_numbers,ttx_page_numbers,ttx_languages, other_pids,from_storage,repeat)";
        cmd = cmd + "values(" + program.getID() + ",-1,'" + sqliteEscape(program.getName());
        cmd = cmd + "',''," + start / 1000L + "," + duration / 1000L + "," + flag;
        cmd = cmd + "," + status + ",''," + program.getVideo().getPID() + "," + program.getVideo().getFormat();
        final TVProgram.Audio[] auds = program.getAllAudio();
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
        cmd = cmd + ",'" + apids + "','" + afmts + "','" + alangs + "'";
        final TVProgram.Subtitle[] subs = program.getAllSubtitle();
        String spids = "";
        String stypes = "";
        String scpgids = "";
        String sapgids = "";
        String slangs = "";
        if (subs != null && subs.length > 0) {
            spids += subs[0].getPID();
            stypes += subs[0].getType();
            scpgids += subs[0].getCompositionPageID();
            sapgids += subs[0].getAncillaryPageID();
            slangs += subs[0].getLang();
            for (int j = 1; j < subs.length; ++j) {
                spids = spids + " " + subs[j].getPID();
                stypes = stypes + " " + subs[j].getType();
                scpgids = scpgids + " " + subs[j].getCompositionPageID();
                sapgids = sapgids + " " + subs[j].getAncillaryPageID();
                slangs = slangs + " " + subs[j].getLang();
            }
        }
        cmd = cmd + ",'" + spids + "','" + stypes + "','" + scpgids + "','" + sapgids + "','" + slangs + "'";
        final TVProgram.Teletext[] ttxs = program.getAllTeletext();
        String tpids = "";
        final String ttypes = "";
        String tmagnums = "";
        String tpgnums = "";
        String tlangs = "";
        if (ttxs != null && ttxs.length > 0) {
            tpids += ttxs[0].getPID();
            tmagnums += ttxs[0].getMagazineNumber();
            tpgnums += ttxs[0].getPageNumber();
            tlangs += ttxs[0].getLang();
            for (int k = 1; k < ttxs.length; ++k) {
                tpids = tpids + " " + ttxs[k].getPID();
                tmagnums = tmagnums + " " + ttxs[k].getMagazineNumber();
                tpgnums = tpgnums + " " + ttxs[k].getPageNumber();
                tlangs = tlangs + " " + ttxs[k].getLang();
            }
        }
        cmd = cmd + ",'" + tpids + "','" + ttypes + "','" + tmagnums + "','" + tpgnums + "','" + tlangs + "'";
        cmd = cmd + ",'',''," + repeat + ")";
        context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
    }
    
    public static void bookEvent(final Context context, final TVEvent event, final int flag, final int repeat, final boolean allowConflict) throws TVBookingConflictException {
        if (event == null) {
            Log.d("TVBooking", "Invalid param for booking event");
            return;
        }
        bookProgram(context, event.getProgram(), flag, event.getStartTime(), event.getEndTime() - event.getStartTime(), repeat, allowConflict);
        final int start = (int)(event.getStartTime() / 1000L);
        final int duration = (int)((event.getEndTime() - event.getStartTime()) / 1000L);
        String cmd = "update booking_table set evt_name='" + sqliteEscape(event.getName());
        cmd = cmd + "',db_evt_id=" + event.getID() + " where db_srv_id=" + event.getProgram().getID();
        cmd = cmd + " and start=" + start + " and duration=" + duration;
        context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
        cmd = "update evt_table set sub_flag=" + flag + " where db_id=" + event.getID();
        context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
    }
    
    public boolean isTimeStart(final long timeInMs) {
        long tmpStart = this.start;
        long tmpTime = timeInMs;
        final long MS_PER_DAY = 86400000L;
        final long MS_PER_WEEK = 604800000L;
        if (this.repeat == 1) {
            tmpStart %= 86400000L;
            tmpTime %= 86400000L;
        }
        else if (this.repeat == 2) {
            tmpStart %= 604800000L;
            tmpTime %= 604800000L;
        }
        final long ret = tmpTime - tmpStart;
        return ret > 0L;
    }
    
    public boolean isTimeEnd(final long timeInMs) {
        return this.duration > 0L && this.isTimeStart(timeInMs - this.duration);
    }
    
    public int getFlag() {
        return this.flag;
    }
    
    public int getID() {
        return this.id;
    }
    
    public String getRecordFilePath() {
        return this.recFilePath;
    }
    
    public String getRecordStoragePath() {
        return this.recStoragePath;
    }
    
    public long getStart() {
        return this.start;
    }
    
    public long getDuration() {
        return this.duration;
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public int getRepeat() {
        return this.repeat;
    }
    
    public TVProgram getProgram() {
        return this.program;
    }
    
    public String getProgramName() {
        return this.programName;
    }
    
    public TVEvent getEvent() {
        return this.event;
    }
    
    public String getEventName() {
        return this.eventName;
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
    
    public void updateStatus(final int status) {
        if (status < 0 || status > 3) {
            Log.d("TVBooking", "Invalid booking status " + status);
            return;
        }
        this.status = status;
        Log.d("TVBooking", "Booking " + this.id + "' status updated to " + status);
        final String cmd = "update booking_table set status=" + status + " where db_id=" + this.id;
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
    }
    
    public void updateFlag(final int flag) {
        this.flag = flag;
        Log.d("TVBooking", "Booking " + this.id + "' flag updated to " + flag);
        final String cmd = "update booking_table set flag=" + flag + " where db_id=" + this.id;
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
    }
    
    public void updateRepeat(final int repeat) {
        this.repeat = repeat;
        Log.d("TVBooking", "Booking " + this.id + "' repeat updated to " + repeat);
        final String cmd = "update booking_table set repeat=" + repeat + " where db_id=" + this.id;
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
    }
    
    public void updateDuration(final long duration) {
        this.duration = duration;
        Log.d("TVBooking", "Booking " + this.id + "' duration updated to " + duration / 1000L);
        final String cmd = "update booking_table set duration=" + duration / 1000L + " where db_id=" + this.id;
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
    }
    
    public void updateStartTime(final long start) {
        this.start = start;
        Log.d("TVBooking", "Booking " + this.id + "' start updated to " + start / 1000L);
        final String cmd = "update booking_table set start=" + start / 1000L + " where db_id=" + this.id;
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
    }
    
    public void updateRecordFilePath(final String path) {
        this.recFilePath = new String(path);
        Log.d("TVBooking", "Booking " + this.id + "' file path updated to " + path);
        final String cmd = "update booking_table set file_name='" + sqliteEscape(path) + "' where db_id=" + this.id;
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
    }
    
    public void updateRecordStoragePath(final String path) {
        this.recStoragePath = new String(path);
        Log.d("TVBooking", "Booking " + this.id + "' storage path updated to " + path);
        final String cmd = "update booking_table set from_storage='" + sqliteEscape(path) + "' where db_id=" + this.id;
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
    }
    
    public void delete() {
        final String cmd = "delete from booking_table where db_id=" + this.id;
        this.context.getContentResolver().query(TVDataProvider.WR_URL, (String[])null, cmd, (String[])null, (String)null);
        if ((this.flag & 0x2) != 0x0) {
            Log.d("TVBooking", "Delete the record files for this booking...");
            try {
                final File rfile = new File(this.recStoragePath + "/" + this.recFilePath);
                rfile.delete();
                final File rifile = new File(this.recStoragePath + "/" + this.recFilePath.replace("amrec", "amri"));
                rifile.delete();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("TVBooking", "Booking " + this.id + " deleted");
        this.id = -1;
    }
    
    public static class TVBookingConflictException extends Exception
    {
    }
}
