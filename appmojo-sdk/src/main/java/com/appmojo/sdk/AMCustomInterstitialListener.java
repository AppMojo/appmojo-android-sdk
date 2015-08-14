package com.appmojo.sdk;


interface AMCustomInterstitialListener {
    void onCustomInterstitialLoaded();
    void onCustomInterstitialFailed(int error);
    void onCustomInterstitialShown();
    void onCustomInterstitialDismissed();
    void onCustomInterstitialLeftApplication();
}
