package com.appmojo.sdk;

import android.view.View;

/**
 * Created by nutron on 6/23/15 AD.
 */
interface AMCustomBannerListener {
    void onCustomBannerLoaded(View view);
    void onCustomBannerFailed(int errCode);
    void onCustomBannerClosed();
    void onCustomBannerOpened();
    void onCustomBannerLeftApplication();

}
