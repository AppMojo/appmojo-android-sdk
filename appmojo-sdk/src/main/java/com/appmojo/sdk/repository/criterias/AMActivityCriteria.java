package com.appmojo.sdk.repository.criterias;

import com.appmojo.sdk.events.AMEvent;

public class AMActivityCriteria extends  AMCriteria {

    private int type;
    private String experimentId;
    private String variantId;
    private int revisionId;
    private String placementId;
    private String adUnitId;
    private String transactionId;
    private String date;
    private int hour;

    public AMActivityCriteria(@AMEvent.Type int type) {
        this.type = type;
        this.hour = -1;
        this.revisionId = -1;
    }

    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    @AMEvent.Type
    public int getActivityType() {
        return type;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(int revisionId) {
        this.revisionId = revisionId;
    }

    public String getPlacementId() {
        return placementId;
    }

    public void setPlacementId(String placementId) {
        this.placementId = placementId;
    }

    public String getAdUnitId() {
        return adUnitId;
    }

    public void setAdUnitId(String adUnitId) {
        this.adUnitId = adUnitId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
