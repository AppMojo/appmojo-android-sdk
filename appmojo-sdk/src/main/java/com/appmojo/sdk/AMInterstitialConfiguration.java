package com.appmojo.sdk;

import com.appmojo.sdk.utils.AMLog;

import org.json.JSONException;
import org.json.JSONObject;


class AMInterstitialConfiguration extends AMConfiguration {

    private static final String KEY_SESSION_FREQUENCY = "session_frequency";
    private static final String KEY_HOUR_FREQUENCY = "hour_frequency";
    private static final String KEY_DAY_FREQUENCY = "day_frequency";

    private int sessionFrequency;
    private int hourFrequency;
    private int dayFrequency;

    public int getDayFrequency() {
        return dayFrequency;
    }

    public void setDayFrequency(int dayFrequency) {
        this.dayFrequency = dayFrequency;
    }

    public int getHourFrequency() {
        return hourFrequency;
    }

    public void setHourFrequency(int hourFrequency) {
        this.hourFrequency = hourFrequency;
    }

    public int getSessionFrequency() {
        return sessionFrequency;
    }

    public void setSessionFrequency(int sessionFrequency) {
        this.sessionFrequency = sessionFrequency;
    }

    @Override
    public String toString() {
        return String.format("%s, sessionFrequency: %s, hourFrequency: %s, dayFrequency: %s",
                super.toString(), sessionFrequency, hourFrequency, dayFrequency);
    }

    @Override
    public AMInterstitialConfiguration parse(JSONObject jsonObject) {
        if(jsonObject == null) {
            return null;
        }

        try {
            //parse default
            super.parse(jsonObject);

            if (jsonObject.has(KEY_SESSION_FREQUENCY)) {
                this.setSessionFrequency(jsonObject.getInt(KEY_SESSION_FREQUENCY));
            }

            if (jsonObject.has(KEY_HOUR_FREQUENCY)) {
                this.setHourFrequency(jsonObject.getInt(KEY_HOUR_FREQUENCY));
            }

            if (jsonObject.has(KEY_DAY_FREQUENCY)) {
                this.setDayFrequency(jsonObject.getInt(KEY_DAY_FREQUENCY));
            }
        } catch (JSONException e) {
            AMLog.e("JSON interstitial object format exception.", e);
        }
        return this;
    }
}
