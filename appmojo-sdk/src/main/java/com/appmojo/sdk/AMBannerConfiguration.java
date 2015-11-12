package com.appmojo.sdk;

import com.appmojo.sdk.utils.AMLog;

import org.json.JSONException;
import org.json.JSONObject;


class AMBannerConfiguration extends AMConfiguration {

    private static final String KEY_REFRESH_RATE = "refresh_rate";

    private int refreshRate;

    public AMBannerConfiguration() {
        this.refreshRate = 30;
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }

    @Override
    public String toString() {
        return String.format("%s, refreshRate: %s", super.toString(), refreshRate);
    }

    @Override
    public AMBannerConfiguration parse(JSONObject jsonObject) {
        if(jsonObject == null) {
            return null;
        }

        try {
            //parse default
            super.parse(jsonObject);

            if (jsonObject.has(KEY_REFRESH_RATE)) {
                this.setRefreshRate(jsonObject.getInt(KEY_REFRESH_RATE));
            }
        } catch (JSONException e) {
            AMLog.e("JSON banner object format exception.", e);
        }

        return this;
    }


    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject =  super.toJsonObject();
        jsonObject.put(KEY_REFRESH_RATE, this.getRefreshRate());
        return jsonObject;
    }
}
