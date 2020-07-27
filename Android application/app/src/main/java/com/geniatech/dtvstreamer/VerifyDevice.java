// 
// Decompiled by Procyon v0.5.36
// 

package com.geniatech.dtvstreamer;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class VerifyDevice
{
    private static final String TAG = "RevDevice";
    private static final String UDP_DEVICE_SCAN = "dtv-client-scan";
    private static final String UDP_DEVICE_REQ = "dtv-client-request-connect";
    private static final String UDP_DEVICE_MAIMUM_REQ = "dtv-client-request-maximum";
    private static final int UDP_DEVICE_PORT = 6001;
    private static final int MAX_DATA_PACKET_LEN = 32;
    private static final int UDP_CONNECT_TIMEROUT = 500;
    private DatagramPacket mDevicePacket;
    private DatagramPacket mDeviceRecvPacket;
    private DatagramSocket mDeviceSocket;
    private boolean mIsAvaiable;
    private static final int MAX_DEVICE_NUM = 2;
    private Thread mReceiveThread;
    
    public VerifyDevice() {
        this.mDevicePacket = null;
        this.mDeviceRecvPacket = null;
        this.mIsAvaiable = false;
        this.mReceiveThread = new Thread(new Runnable() {
            public void run() {
                while (VerifyDevice.this.mIsAvaiable) {
                    try {
                        VerifyDevice.this.UdpDeviceRecv();
                        Thread.sleep(100L);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.UdpDeviceInit();
        this.startVerifyDevice();
    }
    
    public void startVerifyDevice() {
        if (this.mIsAvaiable) {
            return;
        }
        this.mIsAvaiable = true;
        this.mReceiveThread.start();
    }
    
    public void stopVerifyDevice() {
        Log.d("RevDevice", "UdpDeviceClose");
        this.mIsAvaiable = false;
        if (this.mDeviceSocket != null) {
            this.mDeviceSocket.close();
            this.mDeviceSocket = null;
        }
    }
    
    public void UdpDeviceInit() {
        try {
            (this.mDeviceSocket = new DatagramSocket(6001)).setSoTimeout(500);
            this.mDeviceSocket.setReuseAddress(true);
        }
        catch (SocketException e) {
            Log.e("RevDevice", e.getMessage());
        }
        (this.mDevicePacket = new DatagramPacket("dtv-client-scan".getBytes(), "dtv-client-scan".length())).setPort(6001);
    }
    
    public void UdpDeviceRecv() {
        int length = 0;
        int offset = 0;
        byte[] buf = null;
        String addr = null;
        if (this.mDeviceSocket != null) {
            try {
                this.mDeviceRecvPacket = new DatagramPacket(new byte[32], 32);
                this.mDeviceSocket.receive(this.mDeviceRecvPacket);
                buf = this.mDeviceRecvPacket.getData();
                length = this.mDeviceRecvPacket.getLength();
                offset = this.mDeviceRecvPacket.getOffset();
                addr = this.mDeviceRecvPacket.getAddress().getHostAddress();
                Log.d("RevDevice", "receive ip:" + addr);
                if (buf != null) {
                    Log.d("RevDevice", "buf size=" + buf.length);
                    final String strBuf = new String(buf, offset, length, "UTF-8");
                    Log.d("RevDevice", "StrBuf=" + strBuf);
                    if (strBuf.startsWith("dtv-client-scan")) {
                        Log.d("RevDevice", "new device==============");
                        if (PhoneServer.getDeviceNumber() > 2) {
                            this.UdpDevicesend(addr, "dtv-client-request-maximum");
                        }
                        else {
                            this.UdpDevicesend(addr, "dtv-client-request-connect");
                        }
                    }
                }
            }
            catch (IOException ex) {}
        }
    }
    
    public boolean UdpDevicesend(final String IPaddr, final String data) {
        if (this.mDeviceSocket != null) {
            try {
                this.mDevicePacket.setData(data.getBytes());
                this.mDevicePacket.setLength(data.length());
                this.mDevicePacket.setAddress(InetAddress.getByName(IPaddr));
                this.mDeviceSocket.send(this.mDevicePacket);
            }
            catch (UnknownHostException ex) {
                Log.e("RevDevice", ex.getMessage());
                return false;
            }
            catch (IOException e) {
                Log.e("RevDevice", e.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }
}
