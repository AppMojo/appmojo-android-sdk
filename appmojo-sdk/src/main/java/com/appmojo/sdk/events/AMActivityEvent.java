package com.appmojo.sdk.events;

import com.appmojo.sdk.utils.AMLog;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class AMActivityEvent extends AMEvent {

    private static final String JKEY_TRANSACTION_ID = "transaction_id";
    private static final String JKEY_DEVICE_ID = "device_id";
    private static final String JKEY_SESSION_ID = "session_id";
    private static final String JKEY_EXPERIMENT_ID = "experiment_id";
    private static final String JKEY_REVISION_NUMBER = "revision_number";
    private static final String JKEY_VARIANT_ID = "variant_id";
    private static final String JKEY_PLACEMENT_ID = "placement_id";
    private static final String JKEY_AD_UNIT_ID = "ad_unit_id";
    private static final String JKEY_DATE = "date";
    private static final String JKEY_HOUR = "hour";
    private static final String JKEY_METRIC = "metric";
    private static final String JKEY_COUNT = "count";

    protected String transactionId;
    protected String experimentId;
    protected String variantId;
    protected int revisionNumber;
    protected String placementId;
    protected String sessionId;
    protected String adUnitId;
    protected String deviceId;
    protected String date;
    protected int atHour;
    protected int count;

    public abstract String getMetricName();

    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public int getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(int revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public String getPlacementId() {
        return placementId;
    }

    public void setPlacementId(String placementId) {
        this.placementId = placementId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getAdUnitId() {
        return adUnitId;
    }

    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAtHour() {
        return atHour;
    }

    public void setAtHour(int atHour) {
        this.atHour = atHour;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    @Override
    public JSONObject getJsonObject() {
        JSONObject jsonObject = null;
        if(getCount() > 0) {
            try {
                jsonObject = new JSONObject();
                jsonObject.put(JKEY_TRANSACTION_ID, getTransactionId());
                jsonObject.put(JKEY_DEVICE_ID, getDeviceId());
                jsonObject.put(JKEY_SESSION_ID, getSessionId());
                jsonObject.put(JKEY_EXPERIMENT_ID, getExperimentId());
                jsonObject.put(JKEY_REVISION_NUMBER, getRevisionNumber());
                jsonObject.put(JKEY_VARIANT_ID, getVariantId());
                jsonObject.put(JKEY_PLACEMENT_ID, getPlacementId());
                jsonObject.put(JKEY_AD_UNIT_ID, getAdUnitId());
                jsonObject.put(JKEY_DATE, getDate());
                jsonObject.put(JKEY_HOUR, getAtHour());
                jsonObject.put(JKEY_METRIC, getMetricName());
                jsonObject.put(JKEY_COUNT, getCount());

            } catch (JSONException e) {
                AMLog.w("Failed to change data to json format.", e);
                jsonObject = null;
            }
        }
        return jsonObject;
    }

}
