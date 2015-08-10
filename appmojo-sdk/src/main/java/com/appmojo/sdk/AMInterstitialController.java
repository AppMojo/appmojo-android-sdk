package com.appmojo.sdk;

import android.content.Context;

import com.appmojo.sdk.utils.AMLog;

/**
 * Created by nutron on 6/23/15 AD.
 */
class AMInterstitialController  extends AMController {
    //no prefix private because it easy to test
    AMCustomInterstitialListener mCustomListener;
    private AMCustomInterstitial mCustomInterstitial;
    private AMInterstitial mAMInterstitial;

    public AMInterstitialController(Context context, AMView view) {
        super(context, view);
        mCustomListener = new AMInterstitialControllerListener();
        mAMInterstitial = (AMInterstitial) mAMView;
    }

    int getSessionFrequency() {
        if(mCustomAdRequest != null && mCustomAdRequest instanceof AMInterstitialAdRequest) {
            return ((AMInterstitialAdRequest)mCustomAdRequest).getSessionFrequency();
        }
        return 0;
    }

    int getHourFrequency() {
        if(mCustomAdRequest != null && mCustomAdRequest instanceof AMInterstitialAdRequest) {
            return ((AMInterstitialAdRequest)mCustomAdRequest).getHourFrequency();
        }
        return 0;
    }

    int getDayFrequency() {
        if(mCustomAdRequest != null && mCustomAdRequest instanceof AMInterstitialAdRequest) {
            return ((AMInterstitialAdRequest)mCustomAdRequest).getDayFrequency();
        }
        return 0;
    }

    @Override
    public void loadAd(AMAdRequest adRequest) {
        AMLog.d("load interstitial ad...");
        //sometime user call multiple time
        String className = null;
        try {
            mCustomAdRequest = getApplyAdRequest(adRequest);
            if(mCustomAdRequest != null) {
                className = AMClassFactory.getClassName(mCustomAdRequest.getAdNetwork(), AMAdType.INTERSTITIAL);
                mCustomInterstitial = AMCustomInterstitialFactory.create(className);
                applyAdRequest(mCustomAdRequest);
            } else {
                AMLog.d("Cannot load Interstitial because it has no configuration to be applied.");
                notifyNotApplyConfiguration();
            }
        } catch (Exception e) {
            mCustomInterstitial = null;
            AMLog.e("AppMojo cannot find class %s. Make sure you add it to SDK module.", className, e);
            notifyNotApplyConfiguration();
        }

    }

    @Override
    public void reloadAd() {
        AMLog.d("Reload interstitial ad...");
        applyAdRequest(mCustomAdRequest);
    }

    @Override
    protected void applyAdRequest(AMCustomAdRequest adRequest) {
        AMLog.d("Apply interstitial configuration...");
        if(mCustomInterstitial != null) {
            mCustomInterstitial.invalidate();
            if (adRequest != null && adRequest instanceof AMInterstitialAdRequest) {
                mCustomInterstitial.loadInterstitial(mContext, mCustomListener, (AMInterstitialAdRequest) adRequest);
            } else {
                AMLog.d("Cannot load interstitial ad because it has no configuration to be applied.");
                notifyNotApplyConfiguration();
            }
        } else {
            AMLog.d("Cannot load interstitial ad. Have you ever called method loadAd()?");
        }
    }

    public boolean isLoaded() {
        if(mCustomInterstitial != null) {
            return mCustomInterstitial.isLoaded();
        } else {
            AMLog.d("Interstitial ad not load yet.");
        }
        return false;
    }

    public void show() {
        AMLog.d("show interstitial ad...");
        if(mCustomInterstitial != null && mCustomInterstitial.isLoaded()) {
            mCustomInterstitial.show();
        } else {
            AMLog.d("Tried to show a interstitial ad before it finished loading. Please try again.");
        }
    }


    private void notifyNotApplyConfiguration() {
        if(getAMView() != null) {
            AMListener listener = getAMView().getListener();
            if (listener != null) {
                ((AMInterstitialListener) listener).onNotApplyConfiguration(mAMInterstitial);
            }
        }
    }

    @Override
    protected void destroy() {
        AMLog.d("destroy interstitial object...");
        if(mCustomInterstitial != null) {
            mCustomInterstitial.invalidate();
        }
        mCustomInterstitial = null;
        mCustomListener = null;
        mAMInterstitial = null;
        mCustomAdRequest = null;
    }

    private AMInterstitialAdRequest getApplyAdRequest(AMAdRequest adRequest) {
        AMInterstitialAdRequest interstitialAdRequest = null;
        AMInterstitialConfiguration configInterstitial = getConfiguration();
        if(configInterstitial != null) {
            interstitialAdRequest = new AMInterstitialAdRequest(adRequest);
            interstitialAdRequest.setAdUnitId(configInterstitial.getAdUnitId());
            interstitialAdRequest.setAdNetwork(configInterstitial.getAdNetwork());
            interstitialAdRequest.setSessionFrequency(configInterstitial.getSessionFrequency());
            interstitialAdRequest.setHourFrequency(configInterstitial.getHourFrequency());
            interstitialAdRequest.setDayFrequency(configInterstitial.getDayFrequency());
        }
        return interstitialAdRequest;
    }

    private AMInterstitialConfiguration getConfiguration() {
        AMConfiguration config = mAppEngine.getConfiguration(mAMInterstitial.getPlacementUid());
        if(config != null && config instanceof AMInterstitialConfiguration) {
            return (AMInterstitialConfiguration) config;
        }
        return null;
    }


    //   _____ __  __ __  __             ____ _
    //  |_   _|  \ | |  | | | ___  __ __/ ___| | ____    ___  ___
    //    | | | \| | | \| | |/ _ \| |/ | |   | |/ _  \  / __|/ __|
    //   _| |_| |\ | | |\ | |  __/|   /| |___| | (_)  \ \__ \\__ \
    //  |_____|_| \__|_| \__|\___||__|  \____|_|\___/|_\|___/|___/


    private class AMInterstitialControllerListener implements AMCustomInterstitialListener {
        @Override
        public void onCustomInterstitialLoaded() {
            AMListener listener = getAMView().getListener();
            if(listener != null) {
                ((AMInterstitialListener)listener).onAdLoaded(mAMInterstitial);
            }
        }

        @Override
        public void onCustomInterstitialFailed(int error) {
            AMListener listener = getAMView().getListener();
            if(listener != null) {
                ((AMInterstitialListener)listener).onAdFailed(mAMInterstitial, error);
            }
        }

        @Override
        public void onCustomInterstitialShown() {
            AMListener listener = getAMView().getListener();
            if(listener != null) {
                ((AMInterstitialListener)listener).onAdOpened(mAMInterstitial);
            }
        }

        @Override
        public void onCustomInterstitialDismissed() {
            AMListener listener = getAMView().getListener();
            if(listener != null) {
                ((AMInterstitialListener)listener).onAdClosed(mAMInterstitial);
            }
        }

        @Override
        public void onCustomInterstitialLeftApplication() {
            AMListener listener = getAMView().getListener();
            if(listener != null) {
                listener.onAdLeftApplication();
            }
        }
    }
}
