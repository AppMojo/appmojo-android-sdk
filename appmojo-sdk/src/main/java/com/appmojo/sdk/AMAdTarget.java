package com.appmojo.sdk;

import android.location.Location;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


class AMAdTarget {
    private Date birthDay;
    private String contentUrl;
    private int gender;
    private final Set<String> keywords;
    private Location location;
    private final Set<String> testDeviceIds;
    private String requestAgent;

    public AMAdTarget() {
        keywords = new HashSet<>();
        testDeviceIds = new HashSet<>();
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void addKeyword(String keyword) {
        this.keywords.add(keyword);
    }

    public void addAllKeywords(Set<String> keywords) {
        this.keywords.addAll(keywords);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<String> getTestDeviceIds() { return testDeviceIds; }

    public void addAllTestDevice(Set<String> deviceIds) { this.testDeviceIds.addAll(deviceIds); }

    public void addTestDevice(String testDeviceId) {
        this.testDeviceIds.add(testDeviceId);
    }

    public String getRequestAgent() {
        return requestAgent;
    }

    public void setRequestAgent(String requestAgent) {
        this.requestAgent = requestAgent;
    }
}
