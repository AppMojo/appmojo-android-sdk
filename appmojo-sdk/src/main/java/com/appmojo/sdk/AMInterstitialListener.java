package com.appmojo.sdk;


public abstract class AMInterstitialListener extends AMListener {
    public void onAdLoaded(AMInterstitial view) {
    }
    public void onAdClosed(AMInterstitial view) {
    }
    public void onAdFailed(AMInterstitial view, int errorCode) {
    }
    public void onAdOpened(AMInterstitial view) {
    }
    /**
     * @param view Interstitial view
     * @param criteria One of {DAY, HOUR, SESSION}.
     */
    public void onReachedFrequencyCap(AMInterstitial view, int criteria) {
    }
    public void onNotApplyConfiguration(AMInterstitial view) {
    }
}
