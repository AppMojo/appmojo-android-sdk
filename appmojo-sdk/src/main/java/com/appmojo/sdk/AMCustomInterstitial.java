package com.appmojo.sdk;

import android.content.Context;

/**
 * Created by nutron on 6/23/15 AD.
 */
abstract class AMCustomInterstitial {
    public abstract void loadInterstitial(Context context, AMCustomInterstitialListener listener, AMInterstitialAdRequest config);
    public abstract void show();
    public abstract boolean isLoaded();
    public abstract void invalidate();

}
