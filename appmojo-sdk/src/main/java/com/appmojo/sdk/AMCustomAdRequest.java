package com.appmojo.sdk;

import android.location.Location;

import com.appmojo.sdk.base.AMAdNetwork;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nutron on 6/29/15 AD.
 */
class AMCustomAdRequest {

    private int gender;
    private Date birthday;
    private String adUnitId;
    private String contentUrl;
    private Location location;
    private String requestAgent;
    private AMAdNetwork adNetwork;
    private final Set<String> keywords;
    private final Set<String> testDeviceIds;

    AMCustomAdRequest() {
        keywords = new HashSet<>();
        testDeviceIds = new HashSet<>();
    }

    void setRequestAgent(String requestAgent) {
        this.requestAgent = requestAgent;
    }

    void addAllTestDevice(Set<String> deviceIds) { this.testDeviceIds.addAll(deviceIds); }

    void setLocation(Location location) {
        this.location = location;
    }

    void addAllKeywords(Set<String> keywords) {
        this.keywords.addAll(keywords);
    }

    void setGender(int gender) {
        this.gender = gender;
    }

    void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    void setBirthday(Date birthday) { this.birthday = birthday; }

    public AMAdNetwork getAdNetwork() { return adNetwork; }

    public void setAdNetwork(AMAdNetwork adNetwork) {
        this.adNetwork = adNetwork;
    }

    public String getAdUnitId() {
        return adUnitId;
    }

    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public int getGender() {
        return gender;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void addKeyword(String keyword) {
        this.keywords.add(keyword);
    }

    public Location getLocation() {
        return location;
    }

    public Set<String> getTestDeviceIds() { return testDeviceIds; }

    public void addTestDevice(String testDeviceId) {
        this.testDeviceIds.add(testDeviceId);
    }

    public String getRequestAgent() {
        return requestAgent;
    }


}
