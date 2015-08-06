package com.appmojo.sdk;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.appmojo.sdk.utils.AMLog;

/**
 * Created by nutron on 6/23/15 AD.
 */
class AMBannerController extends AMController {
    private static final int MINIMUM_REFRESH_RATE_SECOND = 30;

    //no prefix private because it easy to test
    AMCustomBannerListener mCustomListener;
    private AMCustomBanner mCustomBanner;
    private AMBannerView mBannerView;
    private Handler mHandler;
    private Runnable mRefreshRunnable;

    public AMBannerController(Context context, AMView view) {
        super(context, view);
        mCustomListener = new AMBannerControllerListener();
        mBannerView = (AMBannerView)mAMView;
        mHandler = new Handler();
    }

    public int getRefreshRate() {
        if(mCustomAdRequest != null && mCustomAdRequest instanceof AMBannerAdRequest) {
            return ((AMBannerAdRequest)mCustomAdRequest).getRefreshRate();
        }
        return 0;
    }

    @Override
    public void loadAd(AMAdRequest adRequest) {
        AMLog.d("load banner ad...");
        mAdRequest = adRequest;
        String className = null;
        try {
            mCustomAdRequest = getApplyAdRequest(mAdRequest);
            if(mCustomAdRequest != null) {
                className = AMClassFactory.getClassName(
                        mCustomAdRequest.getAdNetwork(), AMAdType.BANNER);
                mCustomBanner = AMCustomBannerFactory.create(className);
                applyAdRequest(mCustomAdRequest);
            } else {
                AMLog.d("Banner cannot load because no configuration to apply.");
            }
        } catch (Exception e) {
            mCustomBanner = null;
            AMLog.d("AppMojo cannot find class %s. Make sure you add it to SDK module.", className, e);
        }

    }

    @Override
    public void reloadAd() {
        AMLog.d("reload banner ad...");
        if (mCustomAdRequest != null && mCustomAdRequest instanceof AMBannerAdRequest) {
            ((AMBannerAdRequest)mCustomAdRequest).setAdSize(mBannerView.getAdSize());
        }
        applyAdRequest(mCustomAdRequest);

    }

    @Override
    protected void applyAdRequest(AMCustomAdRequest adRequest) {
        AMLog.d("apply banner configuration...");
        if(mCustomBanner != null) {
            if (adRequest != null && adRequest instanceof AMBannerAdRequest) {
                scheduleRefreshTime((AMBannerAdRequest)adRequest);
                mCustomBanner.loadBanner(mContext, mCustomListener, (AMBannerAdRequest)adRequest);
            } else {
                AMLog.d("Cannot load banner because it has no configuration to be applied.");
            }
        } else {
            AMLog.d("Cannot load banner. Have you ever called method loadAd()?");
        }
    }

    private void setContentView(View bannerView) {
        AMLog.d("display banner ad...");
        if(bannerView != null) {
            mBannerView.removeAllViews();
            mBannerView.addView(bannerView);
        }
    }

    @Override
    protected void destroy() {
        AMLog.d("destroy banner object...");
        if(mCustomBanner != null) {
            mCustomBanner.destroy();
        }
        if(mHandler != null) {
            mHandler.removeCallbacks(mRefreshRunnable);
        }
        if(mBannerView != null) {
            mBannerView.removeAllViews();
        }
        mHandler = null;
        mRefreshRunnable = null;
        mCustomListener = null;
        mBannerView = null;
        mCustomBanner = null;
        mCustomAdRequest = null;
    }

    private AMBannerAdRequest getApplyAdRequest(AMAdRequest adRequest) {
        AMBannerAdRequest adBannerRequest = null;
        if(adRequest != null) {
            AMBannerConfiguration configBanner = getConfiguration();
            if (configBanner != null) {
                adBannerRequest = new AMBannerAdRequest(adRequest);
                adBannerRequest.setRefreshRate(configBanner.getRefreshRate());
                adBannerRequest.setAdUnitId(configBanner.getAdUnitId());
                adBannerRequest.setAdNetwork(configBanner.getAdNetwork());
                adBannerRequest.setAdSize(mBannerView.getAdSize());
            }
        }
        return adBannerRequest;
    }

    private AMBannerConfiguration getConfiguration() {
        AMConfiguration config = mAppEngine.getConfiguration(mBannerView.getPlacementUid());
        if(config != null && config instanceof AMBannerConfiguration) {
            return (AMBannerConfiguration) config;
        }
        return null;
    }

    private void scheduleRefreshTime (AMBannerAdRequest adRequest) {
        int refreshRate = adRequest.getRefreshRate();
        if( refreshRate >= MINIMUM_REFRESH_RATE_SECOND) {
            if(mHandler != null) {
                mHandler.removeCallbacks(mRefreshRunnable);
            }

            if(mRefreshRunnable == null) {
                mRefreshRunnable = createRunnable(adRequest);
            }
            AMLog.d("AppMojo", "Scheduling ad refresh %s seconds from now.", refreshRate);
            mHandler.postDelayed(mRefreshRunnable, refreshRate * 1000);
        } else {
            AMLog.d("Not scheduling refresh ad.");
        }
    }

    private Runnable createRunnable(final AMBannerAdRequest adRequest) {
        return new Runnable() {
            @Override
            public void run() {
                AMLog.d("re-apply configuration...");
                applyAdRequest(adRequest);
            }
        };
    }

    //   _____ __  __ __  __             ____ _
    //  |_   _|  \ | |  | | | ___  __ __/ ___| | ____    ___  ___
    //    | | | \| | | \| | |/ _ \| |/ | |   | |/ _  \  / __|/ __|
    //   _| |_| |\ | | |\ | |  __/|   /| |___| | (_)  \ \__ \\__ \
    //  |_____|_| \__|_| \__|\___||__|  \____|_|\___/|_\|___/|___/


    private class AMBannerControllerListener implements AMCustomBannerListener {
        @Override
        public void onCustomBannerLoaded(View view) {
            AMLog.i("Banner loaded...");
            setContentView(view);

            AMListener listener = getAMView().getListener();
            if(listener != null) {
                ((AMBannerListener)listener).onAdLoaded(mBannerView);
            }
        }

        @Override
        public void onCustomBannerFailed(int errCode) {
            AMLog.w("Banner failed to load...");
            AMListener listener = getAMView().getListener();
            if(listener != null) {
                ((AMBannerListener)listener).onAdFailed(mBannerView, errCode);
            }
        }

        @Override
        public void onCustomBannerClosed() {
            AMListener listener = getAMView().getListener();
            if(listener != null) {
                ((AMBannerListener)listener).onAdClosed(mBannerView);
            }
        }

        @Override
        public void onCustomBannerOpened() {
            AMListener listener = getAMView().getListener();
            if(listener != null) {
                ((AMBannerListener)listener).onAdOpened(mBannerView);
            }
        }

        @Override
        public void onCustomBannerLeftApplication() {
            AMListener listener = getAMView().getListener();
            if(listener != null && listener instanceof AMBannerListener) {
                listener.onAdLeftApplication();
            }
        }
    }
}
