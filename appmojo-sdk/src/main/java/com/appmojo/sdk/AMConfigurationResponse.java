package com.appmojo.sdk;

import com.appmojo.sdk.utils.AMLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


class AMConfigurationResponse implements AMJsonParser<AMConfigurationResponse> {

    private static final String KEY_EXPERIMENT_ID = "experiment_id";
    private static final String KEY_REVISION_NUMBER = "revision_number";
    private static final String KEY_VARIANT_ID = "variant_id";
    private static final String KEY_BANNERS = "banners";
    private static final String KEY_INTERSTITIALS = "interstitials";

    private String mExperimentId;
    private int mRevisionNumber;
    private String mVariantId;
    private ArrayList<AMBannerConfiguration> mBanners;
    private ArrayList<AMInterstitialConfiguration> mInterstitials;

    AMConfigurationResponse() {
        mBanners = new ArrayList<>();
        mInterstitials = new ArrayList<>();
    }

    public String getExperimentId() {
        return mExperimentId;
    }

    public int getRevisionNumber() {
        return mRevisionNumber;
    }


    public String getVariantId() {
        return mVariantId;
    }


    public Map<String, AMConfiguration> getAllConfiguration() {
        Map<String, AMConfiguration> configMap = new HashMap<>();
        for(AMConfiguration item : mBanners) {
            configMap.put(item.getPlacementId(), item);
        }

        for(AMConfiguration item : mInterstitials) {
            configMap.put(item.getPlacementId(), item);
        }
        return configMap;
    }

    @Override
    public AMConfigurationResponse parse(String jsonString) {
        if(jsonString != null) {
            try {
                JSONObject rootJsonObject = new JSONObject(jsonString);
                return parse(rootJsonObject);
            } catch (JSONException e) {
                AMLog.e("JSON format exception.", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public AMConfigurationResponse parse(JSONObject jsonObject) {
        if(jsonObject == null) {
            return null;
        }

        try {
            if (jsonObject.has(KEY_EXPERIMENT_ID)) {
                mExperimentId = jsonObject.getString(KEY_EXPERIMENT_ID);
            }

            if (jsonObject.has(KEY_REVISION_NUMBER)) {
                mRevisionNumber = jsonObject.getInt(KEY_REVISION_NUMBER);
            }

            if (jsonObject.has(KEY_VARIANT_ID)) {
                mVariantId = jsonObject.getString(KEY_VARIANT_ID);
            }

            //parsing banner configuration
            parseBanner(jsonObject);

            //parsing interstitial configuration
            parseInterstitial(jsonObject);

        } catch (JSONException e) {
            AMLog.e("JSON format exception.", e);
            e.printStackTrace();
            return null;
        }

        return this;
    }


    private void parseBanner(JSONObject jsonObject) {

        try {
            //parsing banner configuration
            if (jsonObject.has(KEY_BANNERS)) {
                JSONArray bannerObjArray = jsonObject.getJSONArray(KEY_BANNERS);
                JSONObject objBanner;
                AMBannerConfiguration bannerConfig;
                for (int i = 0; i < bannerObjArray.length(); i++) {
                    objBanner = bannerObjArray.getJSONObject(i);
                    bannerConfig = new AMBannerConfiguration().parse(objBanner);
                    if (bannerConfig != null && bannerConfig.getPlacementId() != null) {
                        mBanners.add(bannerConfig);
                    }
                }
            }
        } catch (JSONException e) {
            AMLog.e("JSON Banner parsing exception.", e);
        }
    }


    private void parseInterstitial(JSONObject jsonObject) {
        try {
            //parsing banner configuration
            if (jsonObject.has(KEY_INTERSTITIALS)) {
                JSONArray interObjArray = jsonObject.getJSONArray(KEY_INTERSTITIALS);
                JSONObject objInter;
                AMInterstitialConfiguration interConfig;
                for (int i = 0; i < interObjArray.length(); i++) {
                    objInter = interObjArray.getJSONObject(i);
                    interConfig = new AMInterstitialConfiguration().parse(objInter);
                    if (interConfig != null && interConfig.getPlacementId() != null) {
                        mInterstitials.add(interConfig);
                    }
                }
            }
        } catch (JSONException e) {
            AMLog.e("JSON Interstitial paring exception.", e);

        }
    }
}
