package com.appmojo.sdk;

/**
 * Created by nutron on 6/23/15 AD.
 */
interface AMCustomInterstitialListener {
    void onCustomInterstitialLoaded();
    void onCustomInterstitialFailed(int error);
    void onCustomInterstitialShown();
    void onCustomInterstitialDismissed();
    void onCustomInterstitialLeftApplication();
}
