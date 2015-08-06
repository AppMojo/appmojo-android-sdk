package com.appmojo.sdk;

/**
 * Created by nutron on 6/23/15 AD.
 */
class AMConfigurationRequest {
    private String appId;
    private String deviceId;

    public AMConfigurationRequest(String appId, String deviceId) {
        this.appId = appId;
        this.deviceId = deviceId;
    }

    public String getAppId() {
        return appId;
    }

    public String getDeviceId() {
        return deviceId;
    }

}
