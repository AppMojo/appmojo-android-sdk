package com.appmojo.sdk;

import com.appmojo.sdk.base.AMAdSize;

/**
 * Created by nutron on 6/29/15 AD.
 */
class AMBannerAdRequest extends AMCustomAdRequest {

    private AMAdSize adSize;
    private int refreshRate;


    public AMBannerAdRequest(AMAdRequest adRequest) {
        if(adRequest != null) {
            setRequestAgent(adRequest.getRequestAgent());
            setLocation(adRequest.getLocation());
            setBirthday(adRequest.getBirthday());
            setContentUrl(adRequest.getContentUrl());
            setGender(adRequest.getGender());
            addAllKeywords(adRequest.getKeywords());
            addAllTestDevice(adRequest.getTestDeviceIds());
        }
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }

    public AMAdSize getAdSize() {
        return adSize;
    }

    public void setAdSize(AMAdSize adSize) {
        this.adSize = adSize;
    }




}
