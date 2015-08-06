package com.appmojo.sdk;


import com.appmojo.sdk.base.AMAdNetwork;

/**
 * Created by nutron on 6/25/15 AD.
 */
class AMClassFactory {

    private AMClassFactory(){
    }

    public static String getClassName(AMAdNetwork adNetwork, AMAdType adType) {
        if(adNetwork == AMAdNetwork.ADMOB) {
            if(adType == AMAdType.BANNER) {
                return "com.appmojo.sdk.AMAdMobBannerAdapter";
            } else {
                return "com.appmojo.sdk.AMAdMobInterstitialAdapter";
            }
        }
        return null;
    }
}
