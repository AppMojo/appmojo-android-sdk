package com.appmojo.sdk;


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
