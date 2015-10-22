package com.appmojo.sdk;


import com.appmojo.sdk.base.AMAdNetwork;


class AMClassFactory {

    private AMClassFactory(){
    }

    public static String getClassName(@AMAdNetwork.Network String adNetwork, @AMAdType.Type int adType) {
        if(AMAdNetwork.ADMOB.equalsIgnoreCase(adNetwork)) {
            if(adType == AMAdType.BANNER) {
                return "com.appmojo.sdk.AMAdMobBannerAdapter";
            } else {
                return "com.appmojo.sdk.AMAdMobInterstitialAdapter";
            }
        }
        return null;
    }
}
