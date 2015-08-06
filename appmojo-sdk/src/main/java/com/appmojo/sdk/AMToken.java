package com.appmojo.sdk;

import com.appmojo.sdk.utils.AMLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by nutron on 7/23/15 AD.
 */
class AMToken implements Serializable, AMJsonParser<AMToken>{

    private static final long serialVersionUID = -8507278560283408481L;
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRY = "expiry";

    private String token;
    private long lifeTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(long lifeTime) {
        this.lifeTime = lifeTime;
    }

    @Override
    public AMToken parse(String jsonStr) {
        if(jsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                return parse(jsonObject);
            } catch (JSONException e) {
                AMLog.e("Parsing error message field.", e);
            }
        }
        return null;
    }

    @Override
    public AMToken parse(JSONObject jsonObject) {
        if(jsonObject == null) {
            return null;
        }

        try {
            if(jsonObject.has(KEY_ACCESS_TOKEN)) {
                this.setToken(jsonObject.getString(KEY_ACCESS_TOKEN));
            }

            if(jsonObject.has(KEY_EXPIRY)) {
                this.setLifeTime(jsonObject.getLong(KEY_EXPIRY));
            }

        } catch (JSONException e) {
            AMLog.e("Parsing error message field.", e);
        }
        return this;
    }

    @Override
    public String toString() {
        return new StringBuffer()
                .append("Token: ")
                .append(token)
                .append("\n")
                .append("lifeTime")
                .append(lifeTime).toString();
    }

}
