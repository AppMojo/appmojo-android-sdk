package com.appmojo.sdk;

import android.content.Context;

import com.appmojo.sdk.utils.AMSharePrefs;

import java.util.Map;

/**
 * Created by nutron on 7/31/15 AD.
 */
public class AMConfigurationHelper {

    private static final String KEY_CONFIG_FILE_NAME = "com.appmojo.sdk.configurations";
    private static final String KEY_PREF_PATCH_VERSION = "com.appmojo.sdk.pref.patch_version";

    private AMConfigurationHelper() {
    }

    public static String readPatchVersion(Context context) {
        return AMSharePrefs.getPreferenceString(context, KEY_PREF_PATCH_VERSION, null);
    }

    public static void writePatchVersion(Context context, String patchVersion) {
        AMSharePrefs.setPreference(context, KEY_PREF_PATCH_VERSION, patchVersion);
    }

    public static Map<String, AMConfiguration> readConfiguration(Context context) {
        String configStr = AMSharePrefs.getPreferenceString(context, KEY_CONFIG_FILE_NAME, null);
        AMConfigurationResponse configResponse = new AMConfigurationResponse();
        configResponse = configResponse.parse(configStr);
        if(configResponse == null) {
            return null;
        }
        return configResponse.getAllConfiguration();
    }

    public static void writeConfiguration(Context context, String configJsonStr) {
        if (configJsonStr != null && configJsonStr.length() > 0) {
            AMSharePrefs.setPreference(context, KEY_CONFIG_FILE_NAME, configJsonStr);
        }
    }
}
