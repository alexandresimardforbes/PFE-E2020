// 
// Decompiled by Procyon v0.5.36
// 

package com.amlogic.tvutil;

import android.os.Parcel;
import android.os.Parcelable;

public class TVConfigValue implements Parcelable
{
    private static final String TAG = "TVConfigValue";
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_STRING = 1;
    public static final int TYPE_INT = 2;
    public static final int TYPE_BOOL = 3;
    public static final int TYPE_INT_ARRAY = 4;
    private int type;
    private String strValue;
    private int intValue;
    private boolean boolValue;
    private int[] intArrayValue;
    public static final Parcelable.Creator<TVConfigValue> CREATOR;
    
    public void readFromParcel(final Parcel in) {
        switch (this.type = in.readInt()) {
            case 2: {
                this.intValue = in.readInt();
                break;
            }
            case 3: {
                this.boolValue = (in.readInt() != 0);
                break;
            }
            case 1: {
                this.strValue = in.readString();
                break;
            }
            case 4: {
                final int size = in.readInt();
                if (size != 0) {
                    in.readIntArray(this.intArrayValue = new int[size]);
                    break;
                }
                break;
            }
        }
    }
    
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(this.type);
        switch (this.type) {
            case 2: {
                dest.writeInt(this.intValue);
                break;
            }
            case 3: {
                dest.writeInt((int)(this.boolValue ? 1 : 0));
                break;
            }
            case 1: {
                dest.writeString(this.strValue);
                break;
            }
            case 4: {
                if (this.intArrayValue != null) {
                    dest.writeInt(this.intArrayValue.length);
                    dest.writeIntArray(this.intArrayValue);
                    break;
                }
                dest.writeInt(0);
                break;
            }
        }
    }
    
    public TVConfigValue(final Parcel in) {
        this.readFromParcel(in);
    }
    
    public TVConfigValue(final int v) {
        this.type = 2;
        this.intValue = v;
    }
    
    public TVConfigValue(final String v) {
        this.type = 1;
        this.strValue = v;
    }
    
    public TVConfigValue(final boolean v) {
        this.type = 3;
        this.boolValue = v;
    }
    
    public TVConfigValue(final int[] v) {
        this.type = 4;
        this.intArrayValue = v;
    }
    
    public TVConfigValue() {
        this.type = 0;
    }
    
    public TVConfigValue(final TVConfigValue v) {
        this.type = v.type;
        switch (v.type) {
            case 2: {
                this.intValue = v.intValue;
                break;
            }
            case 3: {
                this.boolValue = v.boolValue;
                break;
            }
            case 1: {
                this.strValue = v.strValue;
                break;
            }
            case 4: {
                if (v.intArrayValue != null) {
                    this.intArrayValue = v.intArrayValue.clone();
                    break;
                }
                break;
            }
        }
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getInt() throws TypeException {
        if (this.type != 2) {
            throw new TypeException();
        }
        return this.intValue;
    }
    
    public boolean getBoolean() throws TypeException {
        if (this.type != 3) {
            throw new TypeException();
        }
        return this.boolValue;
    }
    
    public String getString() throws TypeException {
        if (this.type != 1) {
            throw new TypeException();
        }
        return this.strValue;
    }
    
    public int[] getIntArray() throws TypeException {
        if (this.type != 4) {
            throw new TypeException();
        }
        return this.intArrayValue;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public static Parcelable.Creator<TVConfigValue> getCreator() {
        return TVConfigValue.CREATOR;
    }
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<TVConfigValue>() {
            public TVConfigValue createFromParcel(final Parcel in) {
                return new TVConfigValue(in);
            }
            
            public TVConfigValue[] newArray(final int size) {
                return new TVConfigValue[size];
            }
        };
    }
    
    public class TypeException extends Exception
    {
    }
}
