package com.appmojo.sdk;

import com.appmojo.sdk.utils.AMLog;

import org.json.JSONException;
import org.json.JSONObject;


class AMInterstitialConfiguration extends AMConfiguration {

    private static final String KEY_FREQUENCY_CAP = "frequency_cap";
    private static final String KEY_SESSION = "session";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_DAY = "day";


    private static final String KEY_LAST_DATE = "last_date";
    private static final String KEY_LAST_HOUR = "last_hour";
    private static final String KEY_LAST_SESSION_ID = "last_session_id";
    private static final String KEY_LAST_SESSION_COUNT = "last_session_count";
    private static final String KEY_LAST_HOUR_COUNT = "last_hour_count";
    private static final String KEY_LAST_DAY_COUNT = "last_day_count";

    private int sessionFrequency = 0;
    private int hourFrequency  = 0;
    private int dayFrequency  = 0;

    private String lastSessionId;
    private String lastDate;
    private int lastHour = -1;
    private int sessionFrequencyCount = 0;
    private int hourFrequencyCount  = 0;
    private int dayFrequencyCount  = 0;

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

    //Frequency using data
    public String getLastSessionId() {
        return lastSessionId;
    }

    public void setLastSessionId(String lastSessionId) {
        this.lastSessionId = lastSessionId;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public int getLastHour() {
        return lastHour;
    }

    public void setLastHour(int lastHour) {
        this.lastHour = lastHour;
    }

    public int getSessionFrequencyCount() {
        return sessionFrequencyCount;
    }

    public void setSessionFrequencyCount(int sessionFrequencyCount) {
        this.sessionFrequencyCount = sessionFrequencyCount;
    }

    public int getHourFrequencyCount() {
        return hourFrequencyCount;
    }

    public void setHourFrequencyCount(int hourFrequencyCount) {
        this.hourFrequencyCount = hourFrequencyCount;
    }

    public int getDayFrequencyCount() {
        return dayFrequencyCount;
    }

    public void setDayFrequencyCount(int dayFrequencyCount) {
        this.dayFrequencyCount = dayFrequencyCount;
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

            if (jsonObject.has(KEY_FREQUENCY_CAP)) {
                JSONObject fCapObj = jsonObject.getJSONObject(KEY_FREQUENCY_CAP);

                if (fCapObj.has(KEY_SESSION)) {
                    this.setSessionFrequency(fCapObj.getInt(KEY_SESSION));
                }

                if (fCapObj.has(KEY_HOUR)) {
                    this.setHourFrequency(fCapObj.getInt(KEY_HOUR));
                }

                if (fCapObj.has(KEY_DAY)) {
                    this.setDayFrequency(fCapObj.getInt(KEY_DAY));
                }

                //internal usage
                if (fCapObj.has(KEY_LAST_DATE)) {
                    this.setLastDate(fCapObj.getString(KEY_LAST_DATE));
                }

                if (fCapObj.has(KEY_LAST_HOUR)) {
                    this.setLastHour(fCapObj.getInt(KEY_LAST_HOUR));
                }

                if (fCapObj.has(KEY_LAST_SESSION_ID)) {
                    this.setLastSessionId(fCapObj.getString(KEY_LAST_SESSION_ID));
                }

                if (fCapObj.has(KEY_LAST_DAY_COUNT)) {
                    this.setDayFrequencyCount(fCapObj.getInt(KEY_LAST_DAY_COUNT));
                }

                if (fCapObj.has(KEY_LAST_HOUR_COUNT)) {
                    this.setHourFrequencyCount(fCapObj.getInt(KEY_LAST_HOUR_COUNT));
                }

                if (fCapObj.has(KEY_LAST_SESSION_COUNT)) {
                    this.setSessionFrequencyCount(fCapObj.getInt(KEY_LAST_SESSION_COUNT));
                }
            }


        } catch (JSONException e) {
            AMLog.e("JSON interstitial object format exception.", e);
        }
        return this;
    }


    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject =  super.toJsonObject();

        JSONObject jFreqObj = new JSONObject();
        jFreqObj.put(KEY_SESSION, this.getSessionFrequency());
        jFreqObj.put(KEY_HOUR, this.getHourFrequency());
        jFreqObj.put(KEY_DAY, this.getDayFrequency());

        jFreqObj.put(KEY_LAST_DATE, this.getLastDate());
        jFreqObj.put(KEY_LAST_HOUR, this.getLastHour());
        jFreqObj.put(KEY_LAST_SESSION_ID, this.getLastSessionId());
        jFreqObj.put(KEY_LAST_DAY_COUNT, this.getDayFrequencyCount());
        jFreqObj.put(KEY_LAST_HOUR_COUNT, this.getHourFrequencyCount());
        jFreqObj.put(KEY_LAST_SESSION_COUNT, this.getSessionFrequencyCount());

        jsonObject.put(KEY_FREQUENCY_CAP, jFreqObj);
        return jsonObject;
    }
}
