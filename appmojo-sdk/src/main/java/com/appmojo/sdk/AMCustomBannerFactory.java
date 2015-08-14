package com.appmojo.sdk;

import java.lang.reflect.Constructor;


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
