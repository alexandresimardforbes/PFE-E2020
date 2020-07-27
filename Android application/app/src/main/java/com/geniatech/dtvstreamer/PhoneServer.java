// 
// Decompiled by Procyon v0.5.36
// 

package com.geniatech.dtvstreamer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.amlogic.tvclient.TVClient;
import com.amlogic.tvservice.TVConfig;
import com.amlogic.tvutil.DTVRecordParams;
import com.amlogic.tvutil.TVEvent;
import com.amlogic.tvutil.TVMessage;
import com.amlogic.tvutil.TVProgram;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PhoneServer extends Service
{
    private static final int PORT = 6002;
    public static final String TAG = "PhoneServer";
    private static ArrayList<DTVViewer> DTVViewerList;
    private static boolean mIsAvaiable;
    private static final int MSG_OPEN_DEVICE = 1901;
    private static final int MSG_START_PROGRAM = 1902;
    private static final int MSG_START_SCNA = 1903;
    private static final int MSG_STOP_SCNA = 1904;
    private static final int PHONE_REQUST_PROGRAM = 257;
    private static final int PHONE_REQUST_TTXINFO = 258;
    private static final int PHONE_REQUST_PLAYER_STATUS = 259;
    private static final int PHONE_REQUST_EPG = 260;
    private static final int PHONE_REQUST_HEART = 261;
    private static final int PHONE_REQUST_CHANNELCHANGE = 262;
    private static final int PHONE_REQUST_ALL = 263;
    private static final int PHONE_REQUST_PRV = 264;
    private static final int PHONE_REQUST_STOP_PRV = 265;
    private static final int PHONE_REQUST_ISPRV = 272;
    private static final int PHONE_REQUST_DELETE = 273;
    private static final int BOX_CHANNEL_INFO = 10003;
    private static final int BOX_CONTROL_INFO = 10004;
    private static final int BOX_EPG_INFO = 10005;
    private static final int BOX_ERROR_NULL = 88888;
    private static final int BOX_DTV_OFF = 10006;
    private static final int BOX_CHANNELCHANGE = 10007;
    private static final int BOX_NO_CHANNEL = 10008;
    private static final int BOX_CHANEL_AUDIO = 10009;
    private static final int BOX_CHANEL_ALL_INFO = 10010;
    private static final int BOX_SCAN = 10011;
    private static final int BOX_EXTERNAL_PRV = 10012;
    private static final int BOX_PRV = 10013;
    private static final int BOX_PRV_STOP = 10014;
    private static final int BOX_DELETE_SECCUSS = 10015;
    private static final int BOX_PLAYER_FREQUENCY = 10016;
    private static final int BOX_ALL_CLIENT_INFO = 10017;
    private int DTV_PLAYER_STATUS;
    private static int DTV_PLAYER_ID;
    private static int DTV_FREQUENCY;
    private static DTVStreamer dtvStreamer;
    private static final int DTV_PORT = 6003;
    private static boolean isOpenDevice;
    public TVConfig tvConfig;
    private VerifyDevice verifyDevice;
    private static boolean isInit;
    private MountEventReceiver mount_receiver;
    TVClient tvClient;
    private final IPhoneServer.Stub mBinder;
    Handler handler;
    private Thread Server;
    ArrayList<DeviceItem> deviceList;
    int dev_list_sel;
    private Runnable runnable;
    private static Object lock;
    
    public PhoneServer() {
        this.DTV_PLAYER_STATUS = -1;
        this.mount_receiver = null;
        this.tvClient = new TVClient() {
            @Override
            public void onMessage(final TVMessage msg) {
                try {
                    PhoneServer.this.solveMessage(msg);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            @Override
            public void onDisconnected() {
            }
            
            @Override
            public void onConnected() {
                PhoneServer.this.getValidId();
            }
        };
        this.mBinder = new IPhoneServer.Stub() {
            public void isOpenDevice(final boolean openDevice) throws RemoteException {
                final Message msg = PhoneServer.this.handler.obtainMessage(1901, (Object)new Boolean(openDevice));
                PhoneServer.this.handler.sendMessage(msg);
            }
            
            public void startProgram(final int programId) throws RemoteException {
                final Message msg = PhoneServer.this.handler.obtainMessage(1902, (Object)new Integer(programId));
                PhoneServer.this.handler.sendMessage(msg);
            }
            
            public void startScan() throws RemoteException {
                final Message msg = PhoneServer.this.handler.obtainMessage(1903);
                PhoneServer.this.handler.sendMessage(msg);
            }
            
            public void stopScan() throws RemoteException {
                final Message msg = PhoneServer.this.handler.obtainMessage(1904);
                PhoneServer.this.handler.sendMessage(msg);
            }
        };
        this.handler = new Handler() {
            public void handleMessage(final Message msg) {
                switch (msg.what) {
                    case 1901: {
                        PhoneServer.isOpenDevice = (boolean)msg.obj;
                        PhoneServer.this.frequencyPkg();
                        break;
                    }
                    case 1902: {
                        final TVProgram tvProgram = TVProgram.selectByID((Context) PhoneServer.this, (int)msg.obj);
                        if (tvProgram != null) {
                            PhoneServer.DTV_FREQUENCY = tvProgram.getChannel().getParams().getFrequency();
                            PhoneServer.this.frequencyPkg();
                            break;
                        }
                        break;
                    }
                    case 1903: {
                        PhoneServer.this.sendMesg(PhoneServer.this.startScanpkg());
                        break;
                    }
                    case 1904: {
                        PhoneServer.this.frequencyPkg();
                        break;
                    }
                }
                super.handleMessage(msg);
            }
        };
        this.Server = new Thread(new Runnable() {
            public void run() {
                try {
                    final ServerSocket serverSocket = new ServerSocket(6002);
                    while (PhoneServer.mIsAvaiable) {
                        final Socket s = serverSocket.accept();
                        final DTVViewer dViewer = new DTVViewer();
                        dViewer.socket = s;
                        PhoneServer.DTVViewerList.add(dViewer);
                        Log.i("PhoneServer", "connect=======socket========" + s.getInetAddress());
                        System.out.println("this is " + PhoneServer.DTVViewerList.size());
                        if (!PhoneServer.isInit) {
                            PhoneServer.dtvStreamer.init();
                            PhoneServer.isInit = true;
                        }
                        final Thread t = new Thread(new ThreadServerSocket(dViewer));
                        t.start();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        this.deviceList = null;
        this.dev_list_sel = 0;
        this.runnable = new Runnable() {
            public void run() {
                PhoneServer.this.sendMesg(PhoneServer.this.allInfoPakg());
            }
        };
    }
    
    public IBinder onBind(final Intent intent) {
        return (IBinder)this.mBinder;
    }
    
    public boolean onUnbind(final Intent intent) {
        Log.d("PhoneServer", "ServiceServer onUnbind");
        return super.onUnbind(intent);
    }
    
    public void onCreate() {
        Log.i("PhoneServer", "onCreate===============");
        this.tvClient.connect((Context)this);
        this.verifyDevice = new VerifyDevice();
        this.tvConfig = new TVConfig((Context)this);
        PhoneServer.dtvStreamer = new DTVStreamer(6003);
        this.startServer();
        this.registerMount();
        super.onCreate();
    }
    
    @Deprecated
    public void onStart(final Intent intent, final int startId) {
        super.onStart(intent, startId);
    }
    
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        Log.i("PhoneServer", "onStartCommand");
        this.onStart(intent, startId);
        return 3;
    }
    
    public void onDestroy() {
        Log.i("PhoneServer", "onDestroy===============");
        this.verifyDevice.stopVerifyDevice();
        this.tvClient.disconnect((Context)this);
        this.unRegister();
        super.onDestroy();
    }
    
    private void startServer() {
        PhoneServer.mIsAvaiable = true;
        this.Server.start();
    }
    
    public void disconnect() {
        PhoneServer.mIsAvaiable = false;
        PhoneServer.dtvStreamer.deinit();
    }
    
    private void registerMount() {
        this.mount_receiver = new MountEventReceiver();
        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        filter.addAction("android.intent.action.MEDIA_MOUNTED");
        filter.addDataScheme("file");
        this.registerReceiver((BroadcastReceiver)this.mount_receiver, filter);
    }
    
    private void unRegister() {
        if (this.mount_receiver != null) {
            this.unregisterReceiver((BroadcastReceiver)this.mount_receiver);
        }
    }
    
    public static int getDeviceNumber() {
        return PhoneServer.DTVViewerList.size();
    }
    
    private void getPrvDevice() {
        this.deviceList = new ArrayList<DeviceItem>();
        final File[] files = new File("/storage/external_storage").listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.getPath().startsWith("/storage/external_storage/sd") && !file.getPath().startsWith("/storage/external_storage/sdcard")) {
                    final File myfile = file;
                    Log.d("PhoneServer", "device path: " + myfile.getName());
                    final DeviceItem item = new DeviceItem();
                    this.readUsbDevice(item.Path = myfile.getPath(), item, 0);
                    item.VolumeName = item.VolumeName + " [" + myfile.getName() + "]";
                    Log.d("PhoneServer", "device path: " + item.Path + " device format: " + item.format + " name: " + item.VolumeName);
                    this.deviceList.add(item);
                }
            }
        }
        final String cur_record_path = this.tvClient.getStringConfig("tv:dtv:record_storage_path");
        if (this.deviceList != null) {
            for (int i = 0; i < this.deviceList.size(); ++i) {
                final DeviceItem item2 = this.deviceList.get(i);
                if (cur_record_path != null && cur_record_path.equals(item2.Path)) {
                    final int dev_list_sel = i;
                    break;
                }
            }
        }
        if (this.dev_list_sel == 0 && this.deviceList.size() > 0) {
            this.tvClient.setConfig("tv:dtv:record_storage_path", this.deviceList.get(this.dev_list_sel).Path);
        }
    }
    
    private void readUsbDevice(final String path, final DeviceItem item, final int mode) {
        final Runtime runtime = Runtime.getRuntime();
        final String cmd = "df " + path;
        try {
            final Process proc = runtime.exec(cmd);
            final InputStream input = proc.getInputStream();
            final BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String strLine;
            while (null != (strLine = br.readLine())) {
                Log.d("PhoneServer", ">>>" + strLine);
                if (strLine.startsWith(path)) {
                    strLine = this.deleteExtraSpace(strLine);
                    final String[] byteStrings = strLine.split(" ");
                    Log.d("PhoneServer", "size=" + byteStrings[1] + "free==" + byteStrings[3]);
                    item.total = byteStrings[1];
                    item.spare = byteStrings[3];
                    break;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String deleteExtraSpace(final String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0 || str.equals(" ")) {
            return new String();
        }
        final char[] oldStr = str.toCharArray();
        final int len = str.length();
        final char[] tmpStr = new char[len];
        boolean keepSpace = false;
        int j = 0;
        for (int i = 0; i < len; ++i) {
            final char tmpChar = oldStr[i];
            if (oldStr[i] != ' ') {
                tmpStr[j++] = tmpChar;
                keepSpace = true;
            }
            else if (keepSpace) {
                tmpStr[j++] = tmpChar;
                keepSpace = false;
            }
        }
        int newLen = j;
        if (tmpStr[j - 1] == ' ') {
            --newLen;
        }
        final char[] newStr = new char[newLen];
        for (int k = 0; k < newLen; ++k) {
            newStr[k] = tmpStr[k];
        }
        return new String(newStr);
    }
    
    private String startScanpkg() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", 10011);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    
    private void getValidId() {
        int lastPlayedID = -1;
        final int type = 5;
        boolean typeMatch = false;
        try {
            lastPlayedID = this.tvConfig.getInt("tv:last_program_id");
        }
        catch (Exception ex) {}
        TVProgram p = TVProgram.selectByID((Context)this, lastPlayedID);
        if (p != null) {
            if (p.getType() == type) {
                typeMatch = true;
            }
            else if (type == 5 && (p.getType() == 1 || p.getType() == 2)) {
                typeMatch = true;
            }
        }
        if (!typeMatch) {
            Log.d("PhoneServer", "Get first valid program at type " + type);
            p = TVProgram.selectFirstValid((Context)this, type);
        }
        if (p != null) {
            PhoneServer.DTV_PLAYER_ID = p.getID();
            PhoneServer.DTV_FREQUENCY = p.getChannel().getParams().getFrequency();
            this.tvClient.setFrontendByDtvView(PhoneServer.DTV_PLAYER_ID);
        }
    }
    
    private void frequencyPkg() {
        try {
            for (int i = 0; i < PhoneServer.DTVViewerList.size(); ++i) {
                final JSONObject jObject = new JSONObject();
                jObject.put("code", 10016);
                jObject.put("data", PhoneServer.DTV_FREQUENCY);
                if (!PhoneServer.isOpenDevice && i == 0) {
                    jObject.put("access", true);
                }
                else {
                    jObject.put("access", false);
                }
                Socket socket = null;
                try {
                    socket = PhoneServer.DTVViewerList.get(i).socket;
                    final OutputStream out = socket.getOutputStream();
                    out.write((jObject.toString() + "\n").getBytes());
                    out.flush();
                }
                catch (IOException e2) {
                    this.colseSocket(PhoneServer.DTVViewerList.get(i));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    public void solveMessage(final TVMessage msg) {
        if (msg == null) {
            return;
        }
        switch (msg.getType()) {
            case 9:
            case 16:
            case 45:
            case 46:
            case 48: {}
            case 13: {}
            case 23: {
                switch (msg.getErrorCode()) {
                    case 1:
                    case 2: {
                        this.stopRcording(msg.getErrorCode());
                        break;
                    }
                }
                break;
            }
            case 15: {
                final TVProgram tvProgram = TVProgram.selectByID((Context)this, this.tvClient.getCurrentProgramID());
                if (tvProgram != null) {
                    PhoneServer.DTV_FREQUENCY = tvProgram.getChannel().getParams().getFrequency();
                    this.frequencyPkg();
                    break;
                }
                break;
            }
            case 12: {
                this.sendMesg(this.channelPkg());
                break;
            }
        }
    }
    
    private void stopRcording(final int error) {
        final JSONObject jObject = new JSONObject();
        try {
            jObject.put("code", 10014);
            jObject.put("data", error);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < PhoneServer.DTVViewerList.size(); ++i) {
            if (PhoneServer.DTVViewerList.get(i).recorder = true) {
                PhoneServer.DTVViewerList.get(i).recorder = false;
                final Socket socket = PhoneServer.DTVViewerList.get(i).socket;
                try {
                    final OutputStream out = socket.getOutputStream();
                    out.write((jObject.toString() + "\n").getBytes());
                    out.flush();
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    private void sendMesg(final String msg) {
        for (int i = 0; i < PhoneServer.DTVViewerList.size(); ++i) {
            Socket socket = null;
            try {
                socket = PhoneServer.DTVViewerList.get(i).socket;
                final OutputStream out = socket.getOutputStream();
                out.write((msg + "\n").getBytes());
                out.flush();
            }
            catch (IOException e) {
                this.colseSocket(PhoneServer.DTVViewerList.get(i));
            }
        }
    }
    
    public void reStartStreamer() {
        for (int i = 0; i < PhoneServer.DTVViewerList.size(); ++i) {
            final String server_ip = PhoneServer.DTVViewerList.get(i).inetAddress;
            Log.i("PhoneServer", "server_ip===" + server_ip);
            PhoneServer.dtvStreamer.stopStream(server_ip);
            PhoneServer.dtvStreamer.startStream(server_ip, this.getpid(new int[] { -1, -1 }));
        }
    }
    
    public void stopStreamer() {
        for (int i = 0; i < PhoneServer.DTVViewerList.size(); ++i) {
            final String server_ip = PhoneServer.DTVViewerList.get(i).inetAddress;
            PhoneServer.dtvStreamer.stopStream(server_ip);
        }
    }
    
    private String allInfoPakg() {
        final JSONObject jsonObject = new JSONObject();
        try {
            final JSONArray jArray = new JSONArray();
            jArray.put((Object)this.programInfoPkg());
            jArray.put((Object)this.switchBoxPkg(true));
            jArray.put((Object)this.channelPkg());
            jArray.put((Object)this.epgPkg());
            jArray.put((Object)this.audioPkg());
            jsonObject.put("code", 10010);
            jsonObject.put("data", (Object)jArray);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    
    private TVEvent[][] setup_db() {
        final TVEvent[][] mTempTVEvent = new TVEvent[7][];
        synchronized (PhoneServer.lock) {
            mTempTVEvent[0] = this.DTVEpg_getDateEIT(this.get_current_datetime(), this.get_firstmillisofcurrentday() + 86400000L - this.get_current_datetime());
            mTempTVEvent[1] = this.DTVEpg_getDateEIT(this.get_firstmillisofcurrentday() + 86400000L, 86400000L);
            mTempTVEvent[2] = this.DTVEpg_getDateEIT(this.get_firstmillisofcurrentday() + 172800000L, 86400000L);
            mTempTVEvent[3] = this.DTVEpg_getDateEIT(this.get_firstmillisofcurrentday() + 259200000L, 86400000L);
            mTempTVEvent[4] = this.DTVEpg_getDateEIT(this.get_firstmillisofcurrentday() + 345600000L, 86400000L);
            mTempTVEvent[5] = this.DTVEpg_getDateEIT(this.get_firstmillisofcurrentday() + 432000000L, 86400000L);
            mTempTVEvent[6] = this.DTVEpg_getDateEIT(this.get_firstmillisofcurrentday() + 518400000L, 86400000L);
        }
        return mTempTVEvent;
    }
    
    public TVEvent[] DTVEpg_getDateEIT(final long start, final long duration) {
        final TVProgram mTVProgram = TVProgram.selectByID((Context)this, this.getCurrentProgramID());
        if (mTVProgram != null) {
            return mTVProgram.getScheduleEvents((Context)this, start, duration);
        }
        return null;
    }
    
    private long get_firstmillisofcurrentday() {
        final Date date1 = new Date(this.get_current_datetime());
        date1.setHours(0);
        date1.setMinutes(0);
        date1.setSeconds(0);
        return date1.getTime();
    }
    
    private long get_current_datetime() {
        return this.tvClient.getUTCTime();
    }
    
    private int getCurrentProgramID() {
        return PhoneServer.DTV_PLAYER_ID;
    }
    
    public String getPvrFileList() {
        Log.d("PhoneServer", "getPvrFileList=================");
        final String externalStorageState = Environment.getExternalStorageState();
        List<String> pvr_file_list = null;
        final JSONObject jsonObject = new JSONObject();
        final JSONArray jsonArray = new JSONArray();
        if (pvr_file_list == null) {
            pvr_file_list = new ArrayList<String>();
        }
        final File[] files = new File("/storage/external_storage").listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.getPath().startsWith("/storage/external_storage/sd") && !file.getPath().startsWith("/storage/external_storage/sdcard")) {
                    final File[] myfiles = new File(file.getPath()).listFiles();
                    if (myfiles != null) {
                        for (final File my_file : myfiles) {
                            if (my_file.getPath().endsWith("TVRecordFiles")) {
                                final File[] mytsfiles = new File(my_file.getPath()).listFiles();
                                if (mytsfiles != null) {
                                    for (final File myts_file : mytsfiles) {
                                        if (!myts_file.isDirectory()) {
                                            if (myts_file.getName().endsWith(".ts")) {
                                                final JSONObject jObject = new JSONObject();
                                                try {
                                                    jObject.put("path", (Object)myts_file.getPath());
                                                    jObject.put("name", (Object)myts_file.getName());
                                                    jObject.put("size", myts_file.length());
                                                    jObject.put("date", myts_file.lastModified());
                                                    jsonArray.put((Object)jObject);
                                                }
                                                catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                Log.d("U disk", "DB satellites file name:" + myts_file.getPath());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (externalStorageState.equals("mounted")) {
            final File[] ts_file_sds = new File("/storage/external_storage/sdcard1").listFiles();
            if (ts_file_sds != null) {
                for (final File ts_file_sd : ts_file_sds) {
                    if (ts_file_sd.getPath().endsWith("TVRecordFiles")) {
                        final File[] mytsfiles2 = new File(ts_file_sd.getPath()).listFiles();
                        if (mytsfiles2 != null) {
                            for (final File myts_file2 : mytsfiles2) {
                                if (!myts_file2.isDirectory()) {
                                    if (myts_file2.getName().endsWith(".ts")) {
                                        final JSONObject jObject2 = new JSONObject();
                                        try {
                                            jObject2.put("path", (Object)myts_file2.getPath());
                                            jObject2.put("name", (Object)myts_file2.getName());
                                            jObject2.put("size", myts_file2.length());
                                            jObject2.put("date", myts_file2.lastModified());
                                            jsonArray.put((Object)jObject2);
                                        }
                                        catch (JSONException e2) {
                                            e2.printStackTrace();
                                        }
                                        Log.d("SDCard", "DB satellites file name:" + myts_file2.getName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.getPrvDevice();
        if (this.deviceList != null && this.deviceList.size() > 0) {
            try {
                final String devicepath = this.deviceList.get(this.dev_list_sel).Path;
                final String total = this.deviceList.get(this.dev_list_sel).total;
                final String spare = this.deviceList.get(this.dev_list_sel).spare;
                final JSONObject jObject3 = new JSONObject();
                jObject3.put("path", (Object)devicepath);
                jObject3.put("total", (Object)total);
                jObject3.put("spare", (Object)spare);
                jsonObject.put("device", (Object)jObject3);
            }
            catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        try {
            jsonObject.put("code", 10012);
            jsonObject.put("data", (Object)jsonArray);
        }
        catch (JSONException e4) {
            e4.printStackTrace();
        }
        return jsonObject.toString();
    }
    
    private String audioPkg() {
        Log.i("PhoneServer", "audioPkg=======");
        final TVProgram mTempTVProgram = TVProgram.selectByID((Context)this, PhoneServer.DTV_PLAYER_ID);
        final int mAudioCount = mTempTVProgram.getAudioCount();
        final JSONObject jsonObject = new JSONObject();
        try {
            final JSONArray jsonArray = new JSONArray();
            if (mAudioCount > 0) {
                final TVProgram.Audio[] mAudio = new TVProgram.Audio[mAudioCount];
                final String[] mAudioLang = new String[mAudioCount];
                for (int i = 0; i < mAudioCount; ++i) {
                    mAudio[i] = mTempTVProgram.getAudio(i);
                    mAudioLang[i] = mAudio[i].getLang();
                    Log.d("PhoneServer", "Audio Lang:" + mAudioLang[i]);
                    final JSONObject jobj = new JSONObject();
                    jobj.put("audio_id", mAudio[i].getPID());
                    jobj.put("audio_lang", (Object)mAudioLang[i]);
                    jsonArray.put((Object)jobj);
                }
                jsonObject.put("code", 10009);
                jsonObject.put("data", (Object)jsonArray);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    
    private String epgPkg() {
        final JSONObject jsonObject = new JSONObject();
        try {
            final JSONArray jsonArray = new JSONArray();
            final TVEvent[][] mTempTVEvent = this.setup_db();
            for (int i = 0; i < mTempTVEvent.length; ++i) {
                if (mTempTVEvent[i] != null && mTempTVEvent[i].length > 0) {
                    for (int j = 0; j < mTempTVEvent[i].length; ++j) {
                        final JSONObject jobj = new JSONObject();
                        jobj.put("paly_start_time", mTempTVEvent[i][j].getStartTime());
                        jobj.put("paly_end_time", mTempTVEvent[i][j].getEndTime());
                        jobj.put("paly_content", (Object)mTempTVEvent[i][j].getName());
                        jobj.put("paly_eventdescr", (Object)mTempTVEvent[i][j].getEventDescr());
                        jobj.put("paly_extdescr", (Object)mTempTVEvent[i][j].getEventExtDescr());
                        jobj.put("play_date", i);
                        jsonArray.put((Object)jobj);
                    }
                }
            }
            jsonObject.put("code", 10005);
            jsonObject.put("time", this.get_current_datetime());
            jsonObject.put("data", (Object)jsonArray);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    
    public String playerStatusPkg(final int TVMessage, final int ProgramID) {
        final JSONObject jsonObject = new JSONObject();
        try {
            final JSONObject obj = new JSONObject();
            final JSONArray jaArray = new JSONArray();
            obj.put("ProgramID", ProgramID);
            jaArray.put((Object)obj);
            jsonObject.put("code", TVMessage);
            jsonObject.put("data", (Object)jaArray);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    
    public String offDTVPkg() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", 10006);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    
    public String channelPkg() {
        final JSONObject jsonObject = new JSONObject();
        try {
            final TVProgram[] mTVProgramList = TVProgram.selectByType((Context)this, 1, true);
            final JSONArray jaArray = new JSONArray();
            final boolean isRegionATSC = this.getScanRegion().contains("ATSC");
            for (int i = 0; i < mTVProgramList.length; ++i) {
                String number;
                if (isRegionATSC) {
                    number = Integer.toString(mTVProgramList[i].getNumber().getNumber()) + "-" + Integer.toString(mTVProgramList[i].getNumber().getMinor());
                }
                else {
                    number = Integer.toString(mTVProgramList[i].getNumber().getNumber());
                }
                final String name = mTVProgramList[i].getName();
                final int db_id = mTVProgramList[i].getID();
                final boolean isfav = mTVProgramList[i].getFavoriteFlag();
                final int video_id = mTVProgramList[i].getVideo().getPID();
                final int audio_id = mTVProgramList[i].getAudio().getPID();
                final boolean dtvplayer_b_lock = mTVProgramList[i].getLockFlag();
                final boolean dtvplayer_b_scrambled = mTVProgramList[i].getScrambledFlag();
                final int frequency = mTVProgramList[i].getChannel().getParams().getFrequency();
                final JSONObject obj = new JSONObject();
                obj.put("number", (Object)number);
                obj.put("name", (Object)name);
                obj.put("db_id", db_id);
                obj.put("isfav", isfav);
                obj.put("video_id", video_id);
                obj.put("audio_id", audio_id);
                obj.put("dtvplayer_b_lock", dtvplayer_b_lock);
                obj.put("dtvplayer_b_scrambled", dtvplayer_b_scrambled);
                obj.put("dtvplayer_b_scrambled", dtvplayer_b_scrambled);
                obj.put("frequency", frequency);
                jaArray.put((Object)obj);
            }
            jsonObject.put("code", 10003);
            jsonObject.put("data", (Object)jaArray);
            jsonObject.put("frequency", PhoneServer.DTV_FREQUENCY);
        }
        catch (Exception e) {
            Log.i("PhoneServer", "channelPkg error=" + e.getMessage());
        }
        return jsonObject.toString();
    }
    
    public String programInfoPkg() {
        String pkg = "";
        try {
            final int db_id = this.getCurrentProgramID();
            final TVProgram mTempTVProgram = TVProgram.selectByID((Context)this, db_id);
            final TVEvent mTVEventPresent = mTempTVProgram.getPresentEvent((Context)this, this.get_current_datetime());
            String dtvplayer_cur_event = "NOW: Information not available";
            String dtvplayer_event_des = "No Title Information";
            String dtvplayer_event_ext_des = "No Detailed Information";
            String dtvplayer_proname = "";
            String dtvplayer_next_event = "NEXT: Information not available";
            final String dtvplayer_name = mTempTVProgram.getName();
            boolean dtvplayer_b_lock = false;
            boolean dtvplayer_b_fav = false;
            boolean dtvplayer_b_scrambled = false;
            final int dtvplayer_signal = 0;
            if (mTVEventPresent != null) {
                dtvplayer_cur_event = mTVEventPresent.getName();
                dtvplayer_event_des = mTVEventPresent.getEventDescr();
                dtvplayer_event_ext_des = mTVEventPresent.getEventExtDescr();
            }
            final TVEvent mTVEventFollow = mTempTVProgram.getFollowingEvent((Context)this, this.tvClient.getUTCTime());
            if (mTVEventFollow != null) {
                dtvplayer_next_event = mTVEventFollow.getName();
            }
            if (!this.getScanRegion().contains("ATSC")) {
                dtvplayer_proname = Integer.toString(mTempTVProgram.getNumber().getNumber());
            }
            else {
                dtvplayer_proname = Integer.toString(mTempTVProgram.getNumber().getNumber()) + "-" + Integer.toString(mTempTVProgram.getNumber().getMinor());
            }
            dtvplayer_b_lock = mTempTVProgram.getLockFlag();
            dtvplayer_b_fav = mTempTVProgram.getFavoriteFlag();
            dtvplayer_b_scrambled = mTempTVProgram.getScrambledFlag();
            final int frequency = mTempTVProgram.getChannel().getParams().getFrequency();
            try {
                final JSONObject jsonObject = new JSONObject();
                final JSONObject obj = new JSONObject();
                final JSONArray jaArray = new JSONArray();
                obj.put("db_id", db_id);
                obj.put("dtvplayer_name", (Object)dtvplayer_name);
                obj.put("dtvplayer_cur_event", (Object)dtvplayer_cur_event);
                obj.put("dtvplayer_next_event", (Object)dtvplayer_next_event);
                obj.put("dtvplayer_event_des", (Object)dtvplayer_event_des);
                obj.put("dtvplayer_event_ext_des", (Object)dtvplayer_event_ext_des);
                obj.put("dtvplayer_proname", (Object)dtvplayer_proname);
                obj.put("dtvplayer_b_lock", dtvplayer_b_lock);
                obj.put("dtvplayer_b_scrambled", dtvplayer_b_scrambled);
                obj.put("dtvplayer_b_fav", dtvplayer_b_fav);
                obj.put("dtvplayer_signal", dtvplayer_signal);
                obj.put("frequency", frequency);
                jaArray.put((Object)obj);
                jsonObject.put("code", 10004);
                jsonObject.put("data", (Object)jaArray);
                pkg = jsonObject.toString();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return pkg;
    }
    
    public String getConfig(final String config) {
        String info = "";
        try {
            info = this.tvConfig.getString(config);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }
    
    public String getStringConfig(final String name) {
        return this.tvClient.getStringConfig(name);
    }
    
    public String getScanRegion() {
        final String region = this.getStringConfig("tv:scan:dtv:region");
        return region;
    }
    
    public int[] getpid(final int[] pid) {
        int audioPid = pid[0];
        int videoPid = pid[1];
        try {
            final int db_id = this.getCurrentProgramID();
            final TVProgram mTempTVProgram = TVProgram.selectByID((Context)this, db_id);
            if (pid[0] < 0) {
                audioPid = mTempTVProgram.getAudio().getPID();
            }
            if (pid[1] < 0) {
                videoPid = mTempTVProgram.getVideo().getPID();
            }
            Log.i("PhoneServer", "audioPid=" + audioPid + ";" + videoPid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new int[] { videoPid, audioPid };
    }
    
    public String switchBoxPkg(final boolean isseccuss) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", 10007);
            jsonObject.put("data", isseccuss);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    
    public String startRecording(final JSONObject jsonObject, final DTVViewer dtvView) {
        String result = null;
        try {
            if (this.DTVPlayerIsRecording()) {
                Log.i("PhoneServer", "DTVPlayerIsRecording==true");
                result = this.PrvPkg(false);
            }
            else {
                final long duration = jsonObject.getLong("duration");
                final int db_id = jsonObject.getInt("db_id");
                this.tvClient.startRecordingByBackRound(db_id);
                dtvView.recorder = true;
                result = this.PrvPkg(true);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            result = this.PrvPkg(false);
        }
        return result;
    }
    
    public String PrvPkg(final boolean isPrv) {
        final JSONObject jObject = new JSONObject();
        try {
            jObject.put("code", 10013);
            jObject.put("data", isPrv);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jObject.toString();
    }
    
    public void stopRecording(final DTVViewer dtvView) {
        if (dtvView.recorder && this.DTVPlayerIsRecording()) {
            dtvView.recorder = false;
            this.tvClient.stopRecording();
        }
    }
    
    public boolean DTVPlayerIsRecording() {
        final DTVRecordParams recPara = this.tvClient.getRecordingParams();
        return recPara != null;
    }
    
    public void clientInfoPkg() {
        final JSONObject jsonObject = new JSONObject();
        try {
            final JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < PhoneServer.DTVViewerList.size(); ++i) {
                final String ip = PhoneServer.DTVViewerList.get(i).socket.getInetAddress().toString().replace("/", "");
                final int db_id = PhoneServer.DTVViewerList.get(i).playId;
                final TVProgram mTempTVProgram = TVProgram.selectByID((Context)this, db_id);
                final JSONObject jObject = new JSONObject();
                jObject.put("ip", (Object)ip);
                jObject.put("index", i);
                jObject.put("db_id", db_id);
                jObject.put("name", (Object)mTempTVProgram.getName());
                jsonArray.put((Object)jObject);
            }
            jsonObject.put("code", 10017);
            jsonObject.put("data", (Object)jsonArray);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.sendMesg(jsonObject.toString());
    }
    
    public void colseSocket(final DTVViewer dtvViewer) {
        try {
            Log.i("PhoneServer", "---------------------colseSocket------------");
            if (dtvViewer.isAvaiable) {
                dtvViewer.isAvaiable = false;
            }
            if (dtvViewer.in != null) {
                dtvViewer.in.close();
            }
            if (dtvViewer.out != null) {
                dtvViewer.out.close();
            }
            if (dtvViewer != null) {
                dtvViewer.socket.close();
            }
            if (PhoneServer.dtvStreamer != null) {
                PhoneServer.dtvStreamer.stopStream(dtvViewer.inetAddress);
            }
            if (PhoneServer.DTVViewerList.contains(dtvViewer)) {
                this.stopRecording(dtvViewer);
                PhoneServer.DTVViewerList.remove(dtvViewer);
            }
            if (PhoneServer.DTVViewerList.size() == 0 && PhoneServer.isInit) {
                PhoneServer.dtvStreamer.deinit();
                PhoneServer.isInit = false;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static {
        PhoneServer.DTVViewerList = new ArrayList<DTVViewer>();
        PhoneServer.mIsAvaiable = false;
        PhoneServer.DTV_PLAYER_ID = -1;
        PhoneServer.DTV_FREQUENCY = -1;
        PhoneServer.isOpenDevice = false;
        PhoneServer.isInit = false;
        PhoneServer.lock = new Object();
    }
    
    private class DTVViewer
    {
        private boolean recorder;
        private Socket socket;
        private String Mac;
        private InputStream in;
        private OutputStream out;
        private String inetAddress;
        private int playId;
        private TVProgram tvProgram;
        private boolean isAvaiable;
        private boolean isSwitchFrequency;
        
        private DTVViewer() {
            this.recorder = false;
            this.isSwitchFrequency = true;
        }
    }
    
    private class DeviceItem
    {
        String Path;
        String VolumeName;
        String format;
        String spare;
        String total;
        Bitmap icon;
    }
    
    class MountEventReceiver extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            final Uri uri = intent.getData();
            if (uri.getScheme().equals("file")) {
                PhoneServer.this.sendMesg(PhoneServer.this.getPvrFileList());
            }
        }
    }
    
    class ThreadServerSocket implements Runnable
    {
        private DTVViewer dtvViewer;
        private InputStream in;
        private OutputStream out;
        private long tmpTime;
        Timer timer;
        TimerTask task;
        
        public ThreadServerSocket(final DTVViewer mDViewer) {
            this.tmpTime = 0L;
            this.timer = new Timer();
            this.task = new TimerTask() {
                @Override
                public void run() {
                    final long cuurentTime = System.currentTimeMillis();
                    if (cuurentTime - ThreadServerSocket.this.tmpTime > 10000L) {
                        ThreadServerSocket.this.timer.cancel();
                    }
                }
            };
            (this.dtvViewer = mDViewer).isAvaiable = true;
            this.dtvViewer.inetAddress = this.dtvViewer.socket.getInetAddress().toString().replace("/", "");
            this.dtvViewer.playId = PhoneServer.DTV_PLAYER_ID;
            this.startStream(new int[] { -1, -1 });
            PhoneServer.this.clientInfoPkg();
            this.sendAllMsg();
        }
        
        public boolean startStream(final int[] pid) {
            if (PhoneServer.this.getCurrentProgramID() == -1) {
                return false;
            }
            PhoneServer.dtvStreamer.stopStream(this.dtvViewer.inetAddress);
            return PhoneServer.dtvStreamer.startStream(this.dtvViewer.inetAddress, PhoneServer.this.getpid(pid));
        }
        
        public void run() {
            try {
                this.timer.schedule(this.task, 5000L, 5000L);
                this.dtvViewer.in = (this.in = this.dtvViewer.socket.getInputStream());
                this.dtvViewer.out = (this.out = this.dtvViewer.socket.getOutputStream());
                this.tmpTime = System.currentTimeMillis();
                final InputStreamReader reader = new InputStreamReader(this.in, "utf-8");
                final BufferedReader br = new BufferedReader(reader);
                while (this.dtvViewer.isAvaiable) {
                    this.handlePacket(br.readLine());
                    Thread.sleep(100L);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public void handlePacket(final String ask) {
            try {
                if (ask == null) {
                    PhoneServer.this.colseSocket(this.dtvViewer);
                    return;
                }
                int phoneAsk = -1;
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(ask);
                    if (!jObject.isNull("ask")) {
                        phoneAsk = jObject.getInt("ask");
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                switch (phoneAsk) {
                    case 257: {
                        this.sendClientMesg(PhoneServer.this.channelPkg());
                        break;
                    }
                    case 258: {
                        this.sendClientMesg(PhoneServer.this.programInfoPkg());
                        break;
                    }
                    case 259: {
                        this.sendClientMesg(PhoneServer.this.playerStatusPkg(PhoneServer.this.DTV_PLAYER_STATUS, PhoneServer.DTV_PLAYER_ID));
                        break;
                    }
                    case 260: {
                        this.sendClientMesg(PhoneServer.this.epgPkg());
                        break;
                    }
                    case 261: {
                        this.tmpTime = System.currentTimeMillis();
                        break;
                    }
                    case 262: {
                        this.switchProgram(jObject);
                        break;
                    }
                    case 264: {
                        this.sendClientMesg(PhoneServer.this.startRecording(jObject, this.dtvViewer));
                        break;
                    }
                    case 265: {
                        PhoneServer.this.stopRecording(this.dtvViewer);
                        break;
                    }
                    case 263: {
                        this.sendAllMsg();
                        break;
                    }
                    case 272: {
                        this.sendClientMesg(PhoneServer.this.getPvrFileList());
                        this.sendClientMesg(PhoneServer.this.PrvPkg(PhoneServer.this.DTVPlayerIsRecording()));
                        break;
                    }
                    case 273: {
                        this.deleteFile(jObject.getString("path"));
                        this.sendClientMesg(PhoneServer.this.getPvrFileList());
                        break;
                    }
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        
        public String deleteFile(final String path) {
            final File file = new File(path);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("code", 10015);
                jsonObject.put("data", (Object)path);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
        
        public String noChanelPkg() {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject();
                jsonObject.put("code", 10008);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }
        
        private void sendClientMesg(final String reply) {
            try {
                if (reply != null && this.out != null) {
                    this.out.write((reply + "\n").getBytes());
                    this.out.flush();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                PhoneServer.this.colseSocket(this.dtvViewer);
            }
        }
        
        private Boolean setFrontendByDtvView(final int db_id) {
            boolean isSetSuccess = false;
            try {
                this.dtvViewer.tvProgram = TVProgram.selectByID((Context) PhoneServer.this, db_id);
                int index = 0;
                for (int i = 0; i < PhoneServer.DTVViewerList.size(); ++i) {
                    if (this.dtvViewer == PhoneServer.DTVViewerList.get(i)) {
                        index = i;
                        break;
                    }
                }
                if (!PhoneServer.isOpenDevice && index == 0) {
                    PhoneServer.this.tvClient.setFrontendByDtvView(db_id);
                    PhoneServer.DTV_FREQUENCY = this.dtvViewer.tvProgram.getChannel().getParams().getFrequency();
                    PhoneServer.this.frequencyPkg();
                    isSetSuccess = true;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return isSetSuccess;
        }
        
        public boolean switchProgram(final JSONObject jobj) {
            boolean isSwitchSeccuss = false;
            try {
                final int db_id = Integer.parseInt(jobj.get("db_id").toString());
                final int audio_id = Integer.parseInt(jobj.get("audio_id").toString());
                final int video_id = Integer.parseInt(jobj.get("video_id").toString());
                this.dtvViewer.playId = (PhoneServer.DTV_PLAYER_ID = db_id);
                this.setFrontendByDtvView(db_id);
                if (this.startStream(new int[] { audio_id, video_id })) {
                    this.sendClientMesg(PhoneServer.this.switchBoxPkg(true));
                    this.sendClientMesg(PhoneServer.this.programInfoPkg());
                    this.sendClientMesg(PhoneServer.this.audioPkg());
                    this.sendClientMesg(PhoneServer.this.epgPkg());
                    PhoneServer.this.clientInfoPkg();
                    isSwitchSeccuss = true;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return isSwitchSeccuss;
        }
        
        private void sendAllMsg() {
            PhoneServer.this.frequencyPkg();
            this.sendClientMesg(PhoneServer.this.switchBoxPkg(true));
            this.sendClientMesg(PhoneServer.this.programInfoPkg());
            this.sendClientMesg(PhoneServer.this.channelPkg());
            this.sendClientMesg(PhoneServer.this.audioPkg());
            this.sendClientMesg(PhoneServer.this.epgPkg());
            this.sendClientMesg(PhoneServer.this.getPvrFileList());
            PhoneServer.this.clientInfoPkg();
        }
    }
}
