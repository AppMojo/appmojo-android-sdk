package com.appmojo.sdk.events;

public class AMImpressionEvent extends AMActivityEvent {

    @Override
    public AMEventType getType() {
        return AMEventType.IMPRESSION;
    }


    @Override
    public String getMetricName() {
        return "IMPRESSIONS";
    }
}
