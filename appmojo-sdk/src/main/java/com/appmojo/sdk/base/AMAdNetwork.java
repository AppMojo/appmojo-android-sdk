package com.appmojo.sdk.base;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by nutron on 6/11/15 AD.
 */
public enum AMAdNetwork {
    UNKNOWN("UNKNOWN"),
    ADMOB("ADMOB"),
    AMAZON("AMAZON");

    String value = null;
    private static final Map<String, AMAdNetwork> sValues = new HashMap<>();

    static {
        for (AMAdNetwork type : AMAdNetwork.values()) {
            sValues.put(type.value, type);
        }
    }

    AMAdNetwork(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static AMAdNetwork forValue(String value) {
        if(value != null) {
            return sValues.get(value.toUpperCase(Locale.US));
        }
        return UNKNOWN;
    }
}
