package com.appmojo.sdk.repository;


import android.content.Context;

import com.appmojo.sdk.events.AMEvent;

class AMDaoFactory {

    private AMDaoFactory(){
    }

    public static AMDao createDao(Context context, @AMEvent.Type int type) {
        if(type == AMEvent.SESSION) {
            return new AMSessionDao(context);
        }

        if(type == AMEvent.IMPRESSION) {
            return new AMImpressionDao(context);
        }

        if(type == AMEvent.CLICK) {
            return new AMClickDao(context);
        }

        return null;
    }
}
