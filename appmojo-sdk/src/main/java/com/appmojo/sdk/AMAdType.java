package com.appmojo.sdk;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AMAdType {

    @IntDef({BANNER, INTERSTITIAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {}
    public static final int BANNER = 0;
    public static final int INTERSTITIAL = 1;
}
