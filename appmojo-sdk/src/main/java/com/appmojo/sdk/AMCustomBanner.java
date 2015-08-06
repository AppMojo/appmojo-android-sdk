package com.appmojo.sdk;

import android.content.Context;

/**
 * Created by nutron on 6/23/15 AD.
 */
abstract class AMCustomBanner {
    public abstract void loadBanner(Context context, AMCustomBannerListener listener, AMBannerAdRequest config);
    public abstract void invalidate();
    public abstract void destroy();
}
