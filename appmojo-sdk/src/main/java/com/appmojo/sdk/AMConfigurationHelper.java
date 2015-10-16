package com.appmojo.sdk;

import android.content.Context;

import com.appmojo.sdk.utils.AMSharePrefs;

import java.util.Map;


class AMConfigurationHelper {

    private static final String KEY_CONFIG_FILE_NAME = "com.appmojo.sdk.configurations";
    private static final String KEY_PREF_EXPERIMENT_ID = "com.appmojo.sdk.pref.experiment_id";
    private static final String KEY_PREF_REVISION_ID = "com.appmojo.sdk.pref.revision_id";
    private static final String KEY_PREF_VARIANT_ID = "com.appmojo.sdk.pref.variant_id";

    private AMConfigurationHelper() {
    }

    static String readExperimentId(Context context) {
        return AMSharePrefs.getPreferenceString(context, KEY_PREF_EXPERIMENT_ID, null);
    }


    static void writeExperimentId(Context context, String experimentId) {
        AMSharePrefs.setPreference(context, KEY_PREF_EXPERIMENT_ID, experimentId);
    }


    static int readRevisionNumber(Context context) {
        return AMSharePrefs.getPreferenceInt(context, KEY_PREF_REVISION_ID, -1);
    }

    static void writeRevisionNumber(Context context, int revision) {
        AMSharePrefs.setPreference(context, KEY_PREF_REVISION_ID, revision);
    }

    static String readVariantId(Context context) {
        return AMSharePrefs.getPreferenceString(context, KEY_PREF_VARIANT_ID, null);
    }

    static void writeVariantId(Context context, String variant) {
        AMSharePrefs.setPreference(context, KEY_PREF_VARIANT_ID, variant);
    }


    static Map<String, AMConfiguration> readConfiguration(Context context) {
        String configStr = AMSharePrefs.getPreferenceString(context, KEY_CONFIG_FILE_NAME, null);
        AMConfigurationResponse configResponse = new AMConfigurationResponse();
        configResponse = configResponse.parse(configStr);
        if(configResponse == null) {
            return null;
        }
        return configResponse.getAllConfiguration();
    }

    static void writeConfiguration(Context context, String configJsonStr) {
        if (configJsonStr != null && configJsonStr.length() > 0) {
            AMSharePrefs.setPreference(context, KEY_CONFIG_FILE_NAME, configJsonStr);
        }
    }
}
