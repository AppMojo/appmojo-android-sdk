package com.appmojo.sdk;


import com.appmojo.sdk.base.AMAdNetwork;
import com.appmojo.sdk.utils.AMLog;

import org.json.JSONException;
import org.json.JSONObject;


class AMConfiguration implements AMJsonParser<AMConfiguration>{

    protected static final String KEY_PLACEMENT_ID = "placement_id";
    protected static final String KEY_AD_NETWORK = "ad_network";
    protected static final String KEY_AD_UNIT_ID = "ad_unit_id";

    private String placementId;
    private String adUnitId;
    private @AMAdNetwork.Network String adNetwork;

    public void setAdUnitId(String adUnitId){
        this.adUnitId = adUnitId;
    }

    public String getAdUnitId() {
        return adUnitId;
    }

    @AMAdNetwork.Network
    public String getAdNetwork() {
        return adNetwork;
    }

    public void setAdNetwork(@AMAdNetwork.Network String adNetwork) {
        this.adNetwork = adNetwork;
    }


    public String getPlacementId() {
        return placementId;
    }

    public void setPlacementId(String placementId) {
        this.placementId = placementId;
    }

    @Override
    public String toString() {
        return String.format("placementId: %s, adUnitId: %s, adNetwork: %s", placementId, adUnitId, adNetwork);
    }


    @Override
    public AMConfiguration parse(String jsonString) {
        if(jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                return parse(jsonObject);
            } catch (JSONException e) {
                AMLog.e("JSON format exception.", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public AMConfiguration parse(JSONObject jsonObject) {
        if(jsonObject == null) {
            return null;
        }

        try {
            if (jsonObject.has(KEY_PLACEMENT_ID)) {
                this.setPlacementId(jsonObject.getString(KEY_PLACEMENT_ID));
            }

            if (jsonObject.has(KEY_AD_UNIT_ID)) {
                String adUnit = jsonObject.getString(KEY_AD_UNIT_ID);
                if(adUnit != null) {
                    adUnit = adUnit.replace(":","/");
                    this.setAdUnitId(adUnit);
                }
            }

            if (jsonObject.has(KEY_AD_NETWORK)) {
                String network = jsonObject.getString(KEY_AD_NETWORK);
                this.setAdNetwork(AMAdNetwork.forValue(network));
            }


        } catch (JSONException e) {
            AMLog.e("JSON banner object format exception.", e);
        }

        return this;
    }
}
