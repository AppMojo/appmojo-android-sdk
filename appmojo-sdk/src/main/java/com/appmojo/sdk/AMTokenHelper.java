package com.appmojo.sdk;

import android.content.Context;

import com.appmojo.sdk.utils.AMFileUtils;
import com.appmojo.sdk.utils.AMSharePrefs;


class AMTokenHelper {
    private static final String TOKEN_FILE_NAME = "auth_token";
    private static final String PREF_KEY_TOKEN = "pref_key_token";

    private AMTokenHelper(){
    }

    static void writeToken(Context context, AMToken token) {
        if(token != null && token.getToken() != null) {
            AMSharePrefs.setPreference(context, PREF_KEY_TOKEN, token.getToken());
            AMFileUtils.serializeObjectInCacheDir(context, TOKEN_FILE_NAME, token);
        }
    }

    public static String getToken(Context context) {
        return AMSharePrefs.getPreferenceString(context, PREF_KEY_TOKEN, null);
    }

    public static AMToken readToken(Context context) {
        return (AMToken)AMFileUtils.readSerializeObjectFormCacheDir(context, TOKEN_FILE_NAME);
    }
}
