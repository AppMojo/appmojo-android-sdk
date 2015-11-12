package com.appmojo.sdk;

import android.content.Context;


public class AppMojo {

    private AppMojo(){
    }

    public static void setDebugMode(boolean isDebug) {
        AMAppEngine.getInstance().setDebugMode(isDebug);
    }

    public static void start(Context context, String appId, String appSecret) {
        AMAppEngine.getInstance().start(context, appId, appSecret);
    }

    public static void addConfiguration(AMConfiguration configuration) {
        AMAppEngine.getInstance().getConfigurationManager().addTestData(configuration);

    }

}
