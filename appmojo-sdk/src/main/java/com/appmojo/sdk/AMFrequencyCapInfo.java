package com.appmojo.sdk;

import org.json.JSONException;
import org.json.JSONObject;

class AMFrequencyCapInfo implements AMJsonParser<AMFrequencyCapInfo>{

    private static final String KEY_DATE = "date";
    private static final String KEY_HOUR_AT = "hour_at";
    private static final String KEY_SESSION_ID = "session_id";
    private static final String KEY_AD_UNIT_ID = "ad_unit_id";
    private static final String KEY_SESSION_COUNT = "session_count";
    private static final String KEY_HOUR_COUNT = "hour_count";
    private static final String KEY_DAY_COUNT = "day_count";

    private String adUnitId;
    private String sessionId;
    private int hourAt;
    private String date;
    private int sessionCount;
    private int hourCount;
    private int dayCount;

    public String getAdUnitId() {
        return adUnitId;
    }

    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getHourAt() {
        return hourAt;
    }

    public void setHourAt(int hourAt) {
        this.hourAt = hourAt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }

    public int getHourCount() {
        return hourCount;
    }

    public void setHourCount(int hourCount) {
        this.hourCount = hourCount;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put(KEY_AD_UNIT_ID, getAdUnitId());
            jsonObject.put(KEY_DATE, getDate());
            jsonObject.put(KEY_HOUR_AT, getHourAt());
            jsonObject.put(KEY_SESSION_ID, getDate());
            jsonObject.put(KEY_SESSION_COUNT, getSessionCount());
            jsonObject.put(KEY_HOUR_COUNT, getHourCount());
            jsonObject.put(KEY_DAY_COUNT, getDayCount());
        } catch (JSONException e) {
            return null;
        }
        return jsonObject;
    }

    @Override
    public AMFrequencyCapInfo parse(String jsonStr) {
        return null;
    }

    @Override
    public AMFrequencyCapInfo parse(JSONObject jsonObject) {
        return null;
    }
}
