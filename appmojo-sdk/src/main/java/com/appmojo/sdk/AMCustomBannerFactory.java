package com.appmojo.sdk;

import java.lang.reflect.Constructor;

/**
 * Created by nutron on 6/23/15 AD.
 */
class AMCustomBannerFactory {

    private AMCustomBannerFactory(){
    }

    static AMCustomBanner create(String className) throws Exception {

        Class<? extends AMCustomBanner> bannerClass = Class.forName(className)
                .asSubclass(AMCustomBanner.class);
        Constructor<?> bannerConstructor = bannerClass.getDeclaredConstructor((Class[]) null);
        bannerConstructor.setAccessible(true);
        return (AMCustomBanner) bannerConstructor.newInstance();
    }
}
