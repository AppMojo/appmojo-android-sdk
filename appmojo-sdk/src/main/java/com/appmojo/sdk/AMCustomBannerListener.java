package com.appmojo.sdk;

import android.view.View;


interface AMCustomBannerListener {
    void onCustomBannerLoaded(View view);
    void onCustomBannerFailed(int errCode);
    void onCustomBannerClosed();
    void onCustomBannerOpened();
    void onCustomBannerLeftApplication();
}
