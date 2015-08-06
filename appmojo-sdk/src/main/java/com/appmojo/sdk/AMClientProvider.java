package com.appmojo.sdk;

import android.content.Context;

import com.appmojo.sdk.utils.AMSharePrefs;

import java.util.UUID;

/**
 * Created by nutron on 7/23/15 AD.
 */
class AMClientProvider {

    private static final String PREF_KEY_CLIENT_UUID = "com.appmojo.sdk.client_uuid";

    private AMClientProvider(){
    }

    static String readUuid(Context context) {
        String uuid = AMSharePrefs.getPreferenceString(context, PREF_KEY_CLIENT_UUID, null);
        if(uuid == null) {
            uuid = UUID.randomUUID().toString();
            writeUuid(context, uuid);
        }
        return uuid;
    }

    static void writeUuid(Context context, String uuid) {
        AMSharePrefs.setPreference(context, PREF_KEY_CLIENT_UUID, uuid);
    }
}
