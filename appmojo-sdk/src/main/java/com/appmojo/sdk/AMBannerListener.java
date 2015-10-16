package com.appmojo.sdk;


public abstract class AMBannerListener extends AMListener {
    public void onAdLoaded(AMBannerView view) {
    }
    public void onAdClosed(AMBannerView view) {
    }
    public void onAdFailed(AMBannerView view, int errorCode) {
    }
    public void onAdOpened(AMBannerView view) {
    }
    public abstract void onNotApplyConfiguration(AMBannerView view);
}
