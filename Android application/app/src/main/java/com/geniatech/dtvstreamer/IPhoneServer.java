// 
// Decompiled by Procyon v0.5.36
// 

package com.geniatech.dtvstreamer;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPhoneServer extends IInterface
{
    void isOpenDevice(final boolean p0) throws RemoteException;
    
    void startProgram(final int p0) throws RemoteException;
    
    void startScan() throws RemoteException;
    
    void stopScan() throws RemoteException;
    
    public abstract static class Stub extends Binder implements IPhoneServer
    {
        private static final String DESCRIPTOR = "com.geniatech.dtvstreamer.IPhoneServer";
        static final int TRANSACTION_isOpenDevice = 1;
        static final int TRANSACTION_startProgram = 2;
        static final int TRANSACTION_startScan = 3;
        static final int TRANSACTION_stopScan = 4;
        
        public Stub() {
            this.attachInterface((IInterface)this, "com.geniatech.dtvstreamer.IPhoneServer");
        }
        
        public static IPhoneServer asInterface(final IBinder obj) {
            if (obj == null) {
                return null;
            }
            final IInterface iin = obj.queryLocalInterface("com.geniatech.dtvstreamer.IPhoneServer");
            if (iin != null && iin instanceof IPhoneServer) {
                return (IPhoneServer)iin;
            }
            return new Proxy(obj);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int code, final Parcel data, final Parcel reply, final int flags) throws RemoteException {
            switch (code) {
                case 1598968902: {
                    reply.writeString("com.geniatech.dtvstreamer.IPhoneServer");
                    return true;
                }
                case 1: {
                    data.enforceInterface("com.geniatech.dtvstreamer.IPhoneServer");
                    final boolean _arg0 = 0 != data.readInt();
                    this.isOpenDevice(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface("com.geniatech.dtvstreamer.IPhoneServer");
                    final int _arg2 = data.readInt();
                    this.startProgram(_arg2);
                    reply.writeNoException();
                    return true;
                }
                case 3: {
                    data.enforceInterface("com.geniatech.dtvstreamer.IPhoneServer");
                    this.startScan();
                    reply.writeNoException();
                    return true;
                }
                case 4: {
                    data.enforceInterface("com.geniatech.dtvstreamer.IPhoneServer");
                    this.stopScan();
                    reply.writeNoException();
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }
        
        private static class Proxy implements IPhoneServer
        {
            private IBinder mRemote;
            
            Proxy(final IBinder remote) {
                this.mRemote = remote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            public String getInterfaceDescriptor() {
                return "com.geniatech.dtvstreamer.IPhoneServer";
            }
            
            public void isOpenDevice(final boolean openDevice) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.geniatech.dtvstreamer.IPhoneServer");
                    _data.writeInt((int)(openDevice ? 1 : 0));
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void startProgram(final int programId) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.geniatech.dtvstreamer.IPhoneServer");
                    _data.writeInt(programId);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void startScan() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.geniatech.dtvstreamer.IPhoneServer");
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            public void stopScan() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.geniatech.dtvstreamer.IPhoneServer");
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
