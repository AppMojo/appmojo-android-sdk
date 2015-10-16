package com.appmojo.sdk.repository;


import android.content.Context;

import com.appmojo.sdk.events.AMEventType;

class AMDaoFactory {

    private AMDaoFactory(){
    }

    public static AMDao createDao(Context context, AMEventType type) {
        if(type == AMEventType.SESSION) {
            return new AMSessionDao(context);
        }

        if(type == AMEventType.IMPRESSION) {
            return new AMImpressionDao(context);
        }

        if(type == AMEventType.CLICK) {
            return new AMClickDao(context);
        }

        return null;
    }
}
