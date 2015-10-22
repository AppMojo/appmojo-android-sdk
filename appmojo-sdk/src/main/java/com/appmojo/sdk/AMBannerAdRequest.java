package com.appmojo.sdk;

import com.appmojo.sdk.base.AMAdSize;


class AMBannerAdRequest extends AMCustomAdRequest {

    private int adSize;
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

    @AMAdSize.Size
    public int getAdSize() {
        return adSize;
    }

    public void setAdSize(@AMAdSize.Size int adSize) {
        this.adSize = adSize;
    }




}
