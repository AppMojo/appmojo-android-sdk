package com.appmojo.sdk;

/**
 * Created by nutron on 7/23/15 AD.
 */
class AMBaseConfiguration {

    static final String ACTION_CONFIGURATION_CHANGE = "com.appmojo.sdk.configuration_change";

    private static final String APPMOJO_URL_DEV = "http://dev.appmojo.com/api/v1";
    private static final String APPMOJO_URL_PRODUCTION = "https://appmojo.com/api/v1";
    private static final String SUB_URL_AUTHEN = "/auth";
    private static final String SUB_URL_APPS = "/apps";
    private static final String SUB_URL_CONFIGURATION = "/running-configuration";

    private AMBaseConfiguration(){
    }

    public static String getUrlAuthen(){
        StringBuilder builder = new StringBuilder();
        builder.append(AMAppEngine.getInstance().isDebugMode() ? APPMOJO_URL_DEV : APPMOJO_URL_PRODUCTION);
        builder.append(SUB_URL_AUTHEN);
        return builder.toString();
    }

    public static String getUrlConfiguration(String appId){
        StringBuilder builder = new StringBuilder();
        builder.append(AMAppEngine.getInstance().isDebugMode() ? APPMOJO_URL_DEV : APPMOJO_URL_PRODUCTION);
        builder.append(SUB_URL_APPS);
        builder.append("/");
        builder.append(appId);
        builder.append(SUB_URL_CONFIGURATION);
        return builder.toString();
    }
}
