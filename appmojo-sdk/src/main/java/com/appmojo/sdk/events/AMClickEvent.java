package com.appmojo.sdk.events;

public class AMClickEvent extends AMActivityEvent {

    @Override
    public AMEventType getType() {
        return AMEventType.CLICK;
    }

    @Override
    public String getMetricName() {
        return "CLICKS";
    }
}
