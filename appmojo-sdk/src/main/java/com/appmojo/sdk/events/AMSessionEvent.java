package com.appmojo.sdk.events;

import com.appmojo.sdk.utils.AMLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class AMSessionEvent extends AMEvent implements Serializable{

    private static final long serialVersionUID = -2911518855621130780L;
    private static final String JKEY_SESSION_ID = "session_id";
    private static final String JKEY_DEVICE_ID = "device_id";
    private static final String JKEY_SESSION_START_TIME = "start_time";
    private static final String JKEY_SESSION_EXPIRY_TIME = "expiry_time";
    private static final String JKEY_SESSION_DURATION = "duration";
    private static final String JKEY_SESSION_VARIANT_ID = "variant_id";
    private static final String JKEY_SESSION_EXPERIMENT_ID = "experiment_id";
    private static final String JKEY_SESSION_REVISION_NUMBER = "revision_number";

    private String deviceId;
    private String sessionId;
    private long startTime;
    private long endTime;
    private long duration;
    private String variantId;
    private String experimentId;
    private int revisionNumber;

    @Override
    public int getType() {
        return AMEvent.SESSION;
    }


    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
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

    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public int getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(int revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    @Override
    public JSONObject getJsonObject() {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            jsonObject.put(JKEY_DEVICE_ID, getDeviceId());
            jsonObject.put(JKEY_SESSION_ID, getSessionId());
            jsonObject.put(JKEY_SESSION_START_TIME, getStartTime());
            jsonObject.put(JKEY_SESSION_EXPIRY_TIME, getEndTime());
            jsonObject.put(JKEY_SESSION_DURATION, getDuration());
            jsonObject.put(JKEY_SESSION_VARIANT_ID, getVariantId());
            jsonObject.put(JKEY_SESSION_EXPERIMENT_ID, getExperimentId());
            jsonObject.put(JKEY_SESSION_REVISION_NUMBER, getRevisionNumber());
        } catch (JSONException e) {
            AMLog.w("Failed to change data to json format.", e);
            jsonObject = null;
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("sessionId: ").append(getSessionId())
                .append(", startTime: ").append(getStartTime())
                .append(", expiry: ").append(getEndTime())
                .append(", duration: ").append(getDuration())
                .append(", experimentId: ").append(getExperimentId())
                .append(", VariantId: ").append(getVariantId())
                .append(", RevisionNumber: ").append(getRevisionNumber()).toString();
    }
}
