package com.appmojo.sdk.events;

public class AMImpressionEvent extends AMActivityEvent {

    @Override
    public int getType() {
        return AMEvent.IMPRESSION;
    }


    @Override
    public String getMetricName() {
        return "IMPRESSIONS";
    }
}
