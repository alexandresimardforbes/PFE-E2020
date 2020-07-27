// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ITVCallback extends IInterface
{
    void onMessage(final TVMessage p0) throws RemoteException;
    
    public abstract static class Stub extends Binder implements ITVCallback
    {
        private static final String DESCRIPTOR = "com.amlogic.tvutil.ITVCallback";
        static final int TRANSACTION_onMessage = 1;
        
        public Stub() {
            this.attachInterface((IInterface)this, "com.amlogic.tvutil.ITVCallback");
        }
        
        public static ITVCallback asInterface(final IBinder obj) {
            if (obj == null) {
                return null;
            }
            final IInterface iin = obj.queryLocalInterface("com.amlogic.tvutil.ITVCallback");
            if (iin != null && iin instanceof ITVCallback) {
                return (ITVCallback)iin;
            }
            return new Proxy(obj);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int code, final Parcel data, final Parcel reply, final int flags) throws RemoteException {
            switch (code) {
                case 1598968902: {
                    reply.writeString("com.amlogic.tvutil.ITVCallback");
                    return true;
                }
                case 1: {
                    data.enforceInterface("com.amlogic.tvutil.ITVCallback");
                    TVMessage _arg0;
                    if (0 != data.readInt()) {
                        _arg0 = (TVMessage) TVMessage.CREATOR.createFromParcel(data);
                    }
                    else {
                        _arg0 = null;
                    }
                    this.onMessage(_arg0);
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }
        
        private static class Proxy implements ITVCallback
        {
            private IBinder mRemote;
            
            Proxy(final IBinder remote) {
                this.mRemote = remote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            public String getInterfaceDescriptor() {
                return "com.amlogic.tvutil.ITVCallback";
            }
            
            public void onMessage(final TVMessage msg) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("com.amlogic.tvutil.ITVCallback");
                    if (msg != null) {
                        _data.writeInt(1);
                        msg.writeToParcel(_data, 0);
                    }
                    else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, (Parcel)null, 1);
                }
                finally {
                    _data.recycle();
                }
            }
        }
    }
}
