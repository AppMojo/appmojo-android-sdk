package com.appmojo.sdk;

import android.content.Context;


public class AppMojo {

    private AppMojo(){
    }

    public static void start(Context context, String appId, String appSecret) {
        AMAppEngine.getInstance().start(context, appId, appSecret);
    }

    public static void addConfiguration(AMConfiguration configuration) {
        AMAppEngine.getInstance().getConfigurationManager().addTestData(configuration);

    }

    //TODO: for test only
    public static void setIsUsingDevServer(boolean isDevServer) {
        AMAppEngine.getInstance().setUsingDevServer(isDevServer);
    }

    public static void setDebugMode(boolean isDebug) {
        AMAppEngine.getInstance().setDebugMode(isDebug);
    }


}
