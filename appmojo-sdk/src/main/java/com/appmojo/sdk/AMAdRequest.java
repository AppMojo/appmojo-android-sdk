package com.appmojo.sdk;

import android.location.Location;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;
import java.util.Set;


public class AMAdRequest {
    @IntDef({GENDER_MALE, GENDER_FEMALE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Gender {}
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    private final AMAdTarget mTarget;

    private AMAdRequest(AMAdRequest.Builder builder) {
        mTarget = builder.adTarget;
    }

    public Date getBirthday() {
        return this.mTarget.getBirthDay();
    }

    public String getContentUrl() {
        return this.mTarget.getContentUrl();
    }

    @Gender
    public int getGender() {
        return this.mTarget.getGender();
    }

    public Set<String> getKeywords() {
        return this.mTarget.getKeywords();
    }

    public Set<String> getTestDeviceIds() {
        return this.mTarget.getTestDeviceIds();
    }

    public Location getLocation() {
        return this.mTarget.getLocation();
    }

    public String getRequestAgent() {
        return this.mTarget.getRequestAgent();
    }

    public static final class Builder {
        private final AMAdTarget adTarget = new AMAdTarget();

        public AMAdRequest.Builder addKeyword(String keyword) {
            adTarget.addKeyword(keyword);
            return this;
        }

        public AMAdRequest.Builder addTestDevice(String deviceId) {
            this.adTarget.addTestDevice(deviceId);
            return this;
        }

        public AMAdRequest.Builder setBirthday(Date birthday) {
            this.adTarget.setBirthDay(birthday);
            return this;
        }

        public AMAdRequest.Builder setContentUrl(String contentUrl) {
            this.adTarget.setContentUrl(contentUrl);
            return this;
        }

        public AMAdRequest.Builder setGender(@Gender int gender) {
            this.adTarget.setGender(gender);
            return this;
        }

        public AMAdRequest.Builder setLocation(Location location) {
            this.adTarget.setLocation(location);
            return this;
        }

        public AMAdRequest.Builder setRequestAgent(String requestAgent) {
            this.adTarget.setRequestAgent(requestAgent);
            return this;
        }

        public AMAdRequest build() {
            return new AMAdRequest(this);
        }

    }



}
