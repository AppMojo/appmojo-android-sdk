package com.appmojo.sdk;


class AMInterstitialAdRequest extends AMCustomAdRequest {

    private int sessionFrequency;
    private int hourFrequency;
    private int dayFrequency;

    public AMInterstitialAdRequest(AMAdRequest adRequest) {
        if(adRequest != null) {
            setRequestAgent(adRequest.getRequestAgent());
            setLocation(adRequest.getLocation());
            setBirthday(adRequest.getBirthday());
            setContentUrl(adRequest.getContentUrl());
            setGender(adRequest.getGender());
            addAllKeywords(adRequest.getKeywords());
            addAllTestDevice(adRequest.getTestDeviceIds());
        }
    }

    public int getFrequencySession() {
        return sessionFrequency;
    }

    public void setSessionFrequency(int sessionFrequency) {
        this.sessionFrequency = sessionFrequency;
    }

    public int getFrequencyHour() {
        return hourFrequency;
    }

    public void setHourFrequency(int hourFrequency) {
        this.hourFrequency = hourFrequency;
    }

    public int getFrequencyDay() {
        return dayFrequency;
    }

    public void setDayFrequency(int dayFrequency) {
        this.dayFrequency = dayFrequency;
    }

}
