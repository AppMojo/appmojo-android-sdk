package com.appmojo.sdk;

import android.content.Context;

/**
 * Created by nutron on 6/23/15 AD.
 */
class AMViewControllerFactory {

    private AMViewControllerFactory(){
    }

    static AMController create(Context context, AMView amObj, AMAdType type) {
        AMController controller = null;
        if(type == AMAdType.BANNER) {
            controller = new AMBannerController(context, amObj);
        } else if (type == AMAdType.INTERSTITIAL) {
            controller = new AMInterstitialController(context, amObj);
        }
        return controller;
    }
}
