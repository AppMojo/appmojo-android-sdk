package com.appmojo.sdk;

import android.content.Context;

import com.appmojo.sdk.utils.AMSharePrefs;


class AMConfigurationHelper {

    private static final String KEY_CONFIG_FILE_NAME = "com.appmojo.sdk.configurations";

    private AMConfigurationHelper() {
    }

    static String readExperimentId(Context context) {
        AMConfigurationResponse configs = readConfiguration(context);
        return configs != null ? configs.getExperimentId() : null;
    }

    static int readRevisionNumber(Context context) {
        AMConfigurationResponse configs = readConfiguration(context);
        return configs != null ? configs.getRevisionNumber() : -1;
    }

    static String readVariantId(Context context) {
        AMConfigurationResponse configs = readConfiguration(context);
        return configs != null ? configs.getVariantId() : null;
    }


    static AMConfigurationResponse readConfiguration(Context context) {
        String configStr = AMSharePrefs.getPreferenceString(context, KEY_CONFIG_FILE_NAME, null);
        AMConfigurationResponse configResponse = new AMConfigurationResponse();
        configResponse = configResponse.parse(configStr);
        return configResponse;
    }

    static void writeConfiguration(Context context, String configJsonStr) {
        if (configJsonStr != null && configJsonStr.length() > 0) {
            AMSharePrefs.setPreference(context, KEY_CONFIG_FILE_NAME, configJsonStr);
        }
    }
}
