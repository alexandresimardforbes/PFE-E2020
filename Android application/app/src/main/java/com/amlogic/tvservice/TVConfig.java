// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvservice;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.RemoteCallbackList;
import android.util.Log;

import com.amlogic.tvdataprovider.TVDatabase;
import com.amlogic.tvutil.ITVCallback;
import com.amlogic.tvutil.TVConfigValue;
import com.amlogic.tvutil.TVMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TVConfig
{
    private static final String TAG = "TVConfig";
    private static final String CFG_FILE_NAME = "tv.cfg";
    private static final String CFG_BACKUP_FILE_NAME = "tv_backup.cfg";
    private static final String CFG_NEW_FILE_NAME = "tv_new.cfg";
    private static final String CFG_END_FLAG = "config:end:flag";
    private static String CFG_FILE_DEFAULT_NAME;
    private static final String CFG_FILE_DVBC_NAME = "dvbc_default.cfg";
    private static final String CFG_FILE_ATSC_NAME = "atsc_default.cfg";
    private static final String CFG_FILE_DVBT_NAME = "dvbt_default.cfg";
    private static final String CFG_FILE_ISDBT_NAME = "isdbt_default.cfg";
    private static final String CFG_FILE_DVBS_NAME = "dvbs_default.cfg";
    private Context context;
    private TVConfigEntry root;
    private boolean need_save;
    private Handler save_handler;
    Runnable save_runnable;
    
    private void loadConfigFile(final InputStream is) throws Exception {
        InputStreamReader isr = null;
        BufferedReader br = null;
        boolean flag = false;
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            if (line.matches("[ \\t\\n]*")) {
                continue;
            }
            final String[] sub = line.split("=");
            String name = sub[0];
            final String vstr = sub[1];
            name = name.replaceAll("[ \\t\\n]*([\\w:]+)[ \\t\\n]*", "$1");
            TVConfigValue value;
            if (vstr.matches("[ \\t\\n]*\".*\"[ \\t\\n]*")) {
                final String sv = vstr.replaceAll("[ \\t\\n]*\"(.*)\"[ \\t\\n]*", "$1");
                value = new TVConfigValue(sv);
                Log.d("TVConfig", name + "=\"" + sv + "\"");
            }
            else if (vstr.matches("[ \\t\\n]*true[ \\t\\n]*")) {
                value = new TVConfigValue(true);
                Log.d("TVConfig", name + "=true");
            }
            else if (vstr.matches("[ \\t\\n]*false[ \\t\\n]*")) {
                value = new TVConfigValue(false);
                Log.d("TVConfig", name + "=false");
            }
            else {
                final String[] istrs = vstr.split(",");
                if (istrs.length == 1) {
                    final String istr = istrs[0].replaceAll("[ \\t\\n]*([+-]?\\d*)", "$1");
                    value = new TVConfigValue(Integer.parseInt(istr));
                    Log.d("TVConfig", name + "=" + Integer.parseInt(istr));
                }
                else {
                    final int[] v = new int[istrs.length];
                    for (int i = 0; i < istrs.length; ++i) {
                        final String istr2 = istrs[i].replaceAll("[ \\t\\n]*([+-]?\\d*)", "$1");
                        v[i] = Integer.parseInt(istr2);
                    }
                    value = new TVConfigValue(v);
                    Log.d("TVConfig", name + "=" + vstr);
                }
            }
            if (name.equals("config:end:flag")) {
                flag = true;
            }
            else {
                this.set(name, value);
            }
        }
        if (!flag) {
            Log.e("TVConfig", "cannot get config end flag");
            throw new FileException();
        }
    }
    
    private void getConfigStrings(final ArrayList<ConfigString> list, final String pname, final TVConfigEntry ent) {
        if (ent.value != null) {
            String val = "";
            try {
                if (ent.read == null) {
                    switch (ent.value.getType()) {
                        case 1: {
                            val = "\"" + ent.value.getString() + "\"";
                            break;
                        }
                        case 3: {
                            val = (ent.value.getBoolean() ? "true" : "false");
                            break;
                        }
                        case 2: {
                            val = new Integer(ent.value.getInt()).toString();
                            break;
                        }
                        case 4: {
                            final StringBuilder sb = new StringBuilder();
                            final int[] v = ent.value.getIntArray();
                            for (int i = 0; i < v.length; ++i) {
                                if (i != 0) {
                                    sb.append(",");
                                }
                                sb.append(new Integer(v[i]).toString());
                            }
                            val = sb.toString();
                            break;
                        }
                    }
                    final ConfigString cstr = new ConfigString(pname, val);
                    list.add(cstr);
                }
            }
            catch (Exception ex) {}
        }
        if (ent.children != null && ent.read == null) {
            for (final Map.Entry map_entry : ent.children.entrySet()) {
                final String name = (String) map_entry.getKey();
                final TVConfigEntry child = (TVConfigEntry) map_entry.getValue();
                String cname;
                if (pname == null) {
                    cname = name;
                }
                else {
                    cname = pname + ":" + name;
                }
                this.getConfigStrings(list, cname, child);
            }
        }
    }
    
    public synchronized void save() {
        final ArrayList<ConfigString> list = new ArrayList<ConfigString>();
        this.getConfigStrings(list, null, this.root);
        Collections.sort(list, new ConfigStringComparator());
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            final Context context = this.context;
            final String s = "tv.cfg";
            final Context context2 = this.context;
            fos = context.openFileOutput(s, 0);
            osw = new OutputStreamWriter(fos);
            for (int i = 0; i < list.size(); ++i) {
                final ConfigString cstr = list.get(i);
                osw.write(cstr.name);
                osw.write(61);
                osw.write(cstr.value);
                osw.write(10);
                Log.d("TVConfig", "save " + cstr.name + "=" + cstr.value);
            }
            osw.write("config:end:flag=true");
        }
        catch (Exception e) {
            Log.d("TVConfig", "write config file failed " + e.getMessage());
        }
        finally {
            try {
                if (osw != null) {
                    osw.close();
                }
                if (fos != null) {
                    fos.close();
                }
            }
            catch (Exception ex) {}
        }
        try {
            final File cfg = this.context.getFileStreamPath("tv.cfg");
            final File bak = this.context.getFileStreamPath("tv_backup.cfg");
            cfg.renameTo(bak);
        }
        catch (Exception ex2) {}
        try {
            final File cfg = this.context.getFileStreamPath("tv.cfg");
            final File ncfg = this.context.getFileStreamPath("tv_new.cfg");
            ncfg.renameTo(cfg);
        }
        catch (Exception ex3) {}
        TVDatabase.sync();
    }
    
    private void init() {
        this.root = new TVConfigEntry();
        InputStream is = null;
        boolean loaded = false;
        try {
            Log.d("TVConfig", "try to load tv.cfg");
            is = this.context.openFileInput("tv.cfg");
            this.loadConfigFile(is);
            loaded = true;
        }
        catch (Exception e) {
            Log.d("TVConfig", "load config failed");
        }
        finally {
            try {
                if (is != null) {
                    is.close();
                }
            }
            catch (Exception ex) {}
        }
        if (!loaded) {
            is = null;
            try {
                Log.d("TVConfig", "try to load tv_backup.cfg");
                is = this.context.openFileInput("tv_backup.cfg");
                this.loadConfigFile(is);
                loaded = true;
            }
            catch (Exception e) {
                Log.d("TVConfig", "load config failed");
            }
            finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                }
                catch (Exception ex2) {}
            }
        }
        if (!loaded) {
            this.root = new TVConfigEntry();
            final AssetManager assetManager = this.context.getAssets();
            is = null;
            try {
                is = assetManager.open(TVConfig.CFG_FILE_DEFAULT_NAME);
                this.loadConfigFile(is);
            }
            catch (Exception e2) {
                Log.d("TVConfig", "load config failed");
            }
            finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                }
                catch (Exception ex3) {}
            }
        }
    }
    
    public TVConfig(final Context context) {
        this.need_save = false;
        this.save_handler = new Handler();
        this.save_runnable = new Runnable() {
            public void run() {
                synchronized (TVConfig.this) {
                    if (TVConfig.this.need_save) {
                        TVConfig.this.need_save = false;
                        TVConfig.this.save();
                    }
                }
            }
        };
        this.context = context;
        this.init();
    }
    
    public TVConfig(final Context context, final int deviceMode) {
        this.need_save = false;
        this.save_handler = new Handler();
        this.save_runnable = new Runnable() {
            public void run() {
                synchronized (TVConfig.this) {
                    if (TVConfig.this.need_save) {
                        TVConfig.this.need_save = false;
                        TVConfig.this.save();
                    }
                }
            }
        };
        this.context = context;
        Log.i("TVConfig", "deviceMode>>>>>>>>>>>>>>>>>>>" + deviceMode);
        if (deviceMode == DEVICE_MODE.FE_QPSK.ordinal()) {
            TVConfig.CFG_FILE_DEFAULT_NAME = "dvbs_default.cfg";
        }
        else if (deviceMode == DEVICE_MODE.FE_QAM.ordinal()) {
            TVConfig.CFG_FILE_DEFAULT_NAME = "dvbc_default.cfg";
        }
        else if (deviceMode == DEVICE_MODE.FE_OFDM.ordinal() || deviceMode == DEVICE_MODE.FE_DTMB.ordinal()) {
            TVConfig.CFG_FILE_DEFAULT_NAME = "dvbt_default.cfg";
        }
        else if (deviceMode == DEVICE_MODE.FE_ATSC.ordinal()) {
            TVConfig.CFG_FILE_DEFAULT_NAME = "atsc_default.cfg";
        }
        else if (deviceMode != DEVICE_MODE.FE_ANALOG.ordinal()) {
            if (deviceMode == DEVICE_MODE.FE_ISDBT.ordinal()) {
                TVConfig.CFG_FILE_DEFAULT_NAME = "isdbt_default.cfg";
            }
        }
        switch (deviceMode) {
            case 11: {
                TVConfig.CFG_FILE_DEFAULT_NAME = "dvbc_default.cfg";
                break;
            }
            case 12: {
                TVConfig.CFG_FILE_DEFAULT_NAME = "atsc_default.cfg";
                break;
            }
            case 13: {
                TVConfig.CFG_FILE_DEFAULT_NAME = "dvbt_default.cfg";
                break;
            }
            case 14: {
                TVConfig.CFG_FILE_DEFAULT_NAME = "isdbt_default.cfg";
                break;
            }
            case 15: {
                TVConfig.CFG_FILE_DEFAULT_NAME = "dvbs_default.cfg";
                break;
            }
        }
        this.init();
    }
    
    private TVConfigEntry getEntry(final String name) throws Exception {
        final String[] names = name.split(":");
        TVConfigEntry curr = this.root;
        TVConfigValue value = null;
        int i;
        for (i = 0; i < names.length; ++i) {
            if (curr.children == null) {
                curr.children = (HashMap<String, TVConfigEntry>)new HashMap();
            }
            final TVConfigEntry ent = curr.children.get(names[i]);
            if (ent == null) {
                break;
            }
            curr = ent;
        }
        boolean new_ent = false;
        if (i >= names.length) {
            if (curr.cacheable) {
                return curr;
            }
            new_ent = true;
        }
        while (i < names.length) {
            final TVConfigEntry ent = new TVConfigEntry();
            ent.parent = curr;
            if (curr.children == null) {
                curr.children = (HashMap<String, TVConfigEntry>)new HashMap();
            }
            curr.children.put(names[i], ent);
            curr = ent;
            new_ent = true;
            ++i;
        }
        if (new_ent) {
            for (TVConfigEntry parent = curr; parent != null; parent = parent.parent) {
                if (parent.read != null) {
                    value = parent.read.read(name, curr);
                    if (value != null) {
                        break;
                    }
                }
            }
            if (value != null) {
                curr.value = value;
            }
        }
        return curr;
    }
    
    public synchronized void setCacheable(final String name, final boolean v) throws Exception {
        final TVConfigEntry ent = this.getEntry(name);
        ent.setCacheable(v);
    }
    
    public synchronized TVConfigValue get(final String name) throws Exception {
        final TVConfigEntry ent = this.getEntry(name);
        return new TVConfigValue(ent.value);
    }
    
    public boolean getBoolean(final String name) throws Exception {
        final TVConfigValue v = this.get(name);
        if (v.getType() != 3) {
            throw new TypeException();
        }
        return v.getBoolean();
    }
    
    public int getInt(final String name) throws Exception {
        final TVConfigValue v = this.get(name);
        if (v.getType() != 2) {
            throw new TypeException();
        }
        return v.getInt();
    }
    
    public String getString(final String name) throws Exception {
        final TVConfigValue v = this.get(name);
        if (v.getType() != 1) {
            throw new TypeException();
        }
        return v.getString();
    }
    
    public synchronized void set(final String name, final TVConfigValue value) throws Exception {
        final TVConfigEntry org_ent;
        TVConfigEntry ent = org_ent = this.getEntry(name);
        if (ent.value != null && ent.value.getType() != 0 && ent.value.getType() != value.getType()) {
            throw new TypeException();
        }
        ent.value = value;
        do {
            if (ent.update != null) {
                ent.update.onUpdate(name, value);
            }
            if (ent.callbacks != null) {
                final int N = ent.callbacks.beginBroadcast();
                final TVMessage msg = TVMessage.configChanged(name, value);
                for (int i = 0; i < N; ++i) {
                    Log.d("TVConfig", "config " + name + " callback " + i);
                    ((ITVCallback)ent.callbacks.getBroadcastItem(i)).onMessage(msg);
                }
                ent.callbacks.finishBroadcast();
            }
            ent = ent.parent;
        } while (ent != null);
        if (org_ent.read == null) {
            synchronized (this) {
                if (!this.need_save) {
                    this.need_save = true;
                    this.save_handler.postDelayed(this.save_runnable, 200L);
                }
            }
        }
    }
    
    public synchronized void registerRemoteCallback(final String name, final ITVCallback cb) throws Exception {
        final TVConfigEntry ent = this.getEntry(name);
        if (ent.callbacks == null) {
            ent.callbacks = (RemoteCallbackList<ITVCallback>)new RemoteCallbackList();
        }
        ent.callbacks.register((ITVCallback) cb);
        Log.d("TVConfig", "registerRemoteCallback " + name);
    }
    
    public synchronized void unregisterRemoteCallback(final String name, final ITVCallback cb) throws Exception {
        if (cb == null) {
            return;
        }
        final TVConfigEntry ent = this.getEntry(name);
        if (ent.callbacks == null) {
            return;
        }
        ent.callbacks.unregister((ITVCallback) cb);
    }
    
    synchronized void registerUpdate(final String name, final Update update) throws Exception {
        final TVConfigEntry ent = this.getEntry(name);
        ent.update = update;
    }
    
    synchronized void registerRead(final String name, final Read read) throws Exception {
        final TVConfigEntry ent = this.getEntry(name);
        ent.read = read;
    }
    
    public synchronized void restore() {
        final File file = new File(this.context.getFilesDir(), "tv.cfg");
        if (file.exists()) {
            file.delete();
        }
        final File file2 = new File(this.context.getFilesDir(), "tv_backup.cfg");
        if (file2.exists()) {
            file2.delete();
        }
        this.init();
    }
    
    static {
        TVConfig.CFG_FILE_DEFAULT_NAME = "";
    }
    
    private enum DEVICE_MODE
    {
        FE_QPSK, 
        FE_QAM, 
        FE_OFDM, 
        FE_ATSC, 
        FE_ANALOG, 
        FE_DTMB, 
        FE_ISDBT;
    }
    
    public class NotExistException extends Exception
    {
    }
    
    public class TypeException extends Exception
    {
    }
    
    public class FileException extends Exception
    {
    }
    
    class TVConfigEntry
    {
        private TVConfigValue value;
        private Update update;
        private Read read;
        private RemoteCallbackList<ITVCallback> callbacks;
        private HashMap<String, TVConfigEntry> children;
        private TVConfigEntry parent;
        private boolean cacheable;
        
        TVConfigEntry() {
            this.cacheable = true;
        }
        
        void setCacheable(final boolean v) {
            this.cacheable = v;
        }
    }
    
    private class ConfigString
    {
        private String name;
        private String value;
        
        private ConfigString(final String name, final String value) {
            this.name = name;
            this.value = value;
        }
    }
    
    private class ConfigStringComparator implements Comparator
    {
        public int compare(final Object lhs, final Object rhs) {
            final ConfigString l = (ConfigString)lhs;
            final ConfigString r = (ConfigString)rhs;
            return l.name.compareTo(r.name);
        }
    }
    
    public interface Update
    {
        void onUpdate(final String p0, final TVConfigValue p1);
    }
    
    public interface Read
    {
        TVConfigValue read(final String p0, final TVConfigEntry p1);
    }
}
