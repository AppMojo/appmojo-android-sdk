package com.appmojo.sdk.events;

import android.util.SparseArray;

public enum  AMEventType {

    SESSION(0),
    CLICK(1),
    IMPRESSION(2);

    int value = -1;
    private static final SparseArray<AMEventType> sValues = new SparseArray<AMEventType>();

    static {
        for (AMEventType type : AMEventType.values()) {
            sValues.put(type.value, type);
        }
    }

    AMEventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static AMEventType forValue(int value) {
        return sValues.get(value);
    }
}
