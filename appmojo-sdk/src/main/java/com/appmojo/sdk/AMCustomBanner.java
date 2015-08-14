package com.appmojo.sdk;

import android.content.Context;


abstract class AMCustomBanner {
    public abstract void loadBanner(Context context, AMCustomBannerListener listener, AMBannerAdRequest config);
    public abstract void invalidate();
    public abstract void destroy();
}
