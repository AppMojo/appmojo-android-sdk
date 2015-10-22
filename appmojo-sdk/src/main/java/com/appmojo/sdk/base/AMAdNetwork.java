package com.appmojo.sdk.base;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AMAdNetwork {

    @StringDef({UNKNOWN, ADMOB, AMAZON})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Network {}
    public static final String UNKNOWN = "UNKNOWN";
    public static final String ADMOB = "ADMOB";
    public static final String AMAZON = "AMAZON";


    @Network
    public static String forValue(String value) {
        if(value == null) {
            return AMAdNetwork.UNKNOWN;
        }

        if(ADMOB.equalsIgnoreCase(value)) {
            return ADMOB;
        }

        if(AMAZON.equalsIgnoreCase(value)) {
            return AMAZON;
        }

        return AMAdNetwork.UNKNOWN;
    }
}
