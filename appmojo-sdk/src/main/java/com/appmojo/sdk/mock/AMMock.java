package com.appmojo.sdk.mock;

import com.appmojo.sdk.AMAdType;
import com.appmojo.sdk.base.AMAdNetwork;

/**
 * Created by nutron on 7/6/15 AD.
 */
@SuppressWarnings("SameParameterValue")
public class AMMock {
    private final AMAdType adType;
    private final AMAdNetwork adNetwork;
    private final String adUitId;
    private int refresh;
    private int sessionFrequency;
    private int hourFrequency;
    private int dayFrequency;

    public AMMock(AMAdType adType, AMAdNetwork adNetwork, String adUitId) {
        this.adType = adType;
        this.adNetwork = adNetwork;
        this.adUitId = adUitId;
    }

    public AMAdNetwork getAdNetwork() {
        return adNetwork;
    }

    public String getAdUitId() {
        return adUitId;
    }

    public AMAdType getAdType() {
        return adType;
    }

    public int getRefresh() {
        return refresh;
    }

    /**
     * Banner refresh rate
     * @param refresh : in second term
     */
    public void setRefresh(int refresh) {
        this.refresh = refresh;
    }



    public int getSessionFrequency() {
        return sessionFrequency;
    }

    /**
     * Interstitial sessionFrequency
     * @param sessionFrequency : in second term
     */
    public void setSessionFrequency(int sessionFrequency) {
        this.sessionFrequency = sessionFrequency;
    }


    public int getHourFrequency() {
        return hourFrequency;
    }

    /**
     * Interstitial hourFrequency
     * @param hourFrequency : in second term
     */
    public void setHourFrequency(int hourFrequency) {
        this.hourFrequency = hourFrequency;
    }

    public int getDayFrequency() {
        return dayFrequency;
    }

    /**
     * Interstitial dayFrequency
     * @param dayFrequency : in second term
     */
    public void setDayFrequency(int dayFrequency) {
        this.dayFrequency = dayFrequency;
    }
}
