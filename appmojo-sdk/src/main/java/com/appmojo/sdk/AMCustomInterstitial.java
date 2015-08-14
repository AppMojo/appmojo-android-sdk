package com.appmojo.sdk;

import android.content.Context;


abstract class AMCustomInterstitial {
    public abstract void loadInterstitial(Context context, AMCustomInterstitialListener listener, AMInterstitialAdRequest config);
    public abstract void show();
    public abstract boolean isLoaded();
    public abstract void invalidate();

}
