package com.appmojo.sdk;

import android.content.Context;

/**
 * Created by nutron on 7/3/15 AD.
 */
public class AppMojo {

    private AppMojo(){
    }

    public static void setDebugMode(boolean isDebug) {
        AMAppEngine.getInstance().setDebugMode(isDebug);
    }

    public static void start(Context context, String appId, String appSecret) {
        AMAppEngine.getInstance().start(context, appId, appSecret);
    }

}
