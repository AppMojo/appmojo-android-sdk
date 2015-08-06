package com.appmojo.sdk;

import java.lang.reflect.Constructor;

/**
 * Created by nutron on 6/23/15 AD.
 */
class AMCustomInterstitialFactory {

    private AMCustomInterstitialFactory(){
    }

    static AMCustomInterstitial create(String className) throws Exception {

        Class<? extends AMCustomInterstitial> interstitialClass = Class.forName(className)
                .asSubclass(AMCustomInterstitial.class);
        Constructor<?> interstitialConstructor = interstitialClass.getDeclaredConstructor((Class[]) null);
        interstitialConstructor.setAccessible(true);
        return (AMCustomInterstitial) interstitialConstructor.newInstance();
    }
}
