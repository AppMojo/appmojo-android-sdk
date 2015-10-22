package com.appmojo.sdk.events;

public class AMClickEvent extends AMActivityEvent {

    @Override
    public int getType() {
        return AMEvent.CLICK;
    }

    @Override
    public String getMetricName() {
        return "CLICKS";
    }
}
