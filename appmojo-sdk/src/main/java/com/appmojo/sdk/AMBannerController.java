package com.appmojo.sdk;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.appmojo.sdk.utils.AMLog;


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
                className = AMClassFactory.getClassName(mCustomAdRequest.getAdNetwork(), AMAdType.BANNER);
                mCustomBanner = AMCustomBannerFactory.create(className);
                applyAdRequest(mCustomAdRequest);
            } else {
                AMLog.d("Cannot load banner because it has no configuration to be applied.");
                notifyNotApplyConfiguration();
                setViewVisibility(false);
            }
        } catch (Exception e) {
            mCustomBanner = null;
            AMLog.w("AppMojo cannot find class %s. Make sure you add it to SDK module.", className, e);
            notifyNotApplyConfiguration();
            setViewVisibility(false);
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
                setViewVisibility(true);
                scheduleRefreshTime((AMBannerAdRequest)adRequest);
                mCustomBanner.loadBanner(mContext, mCustomListener, (AMBannerAdRequest)adRequest);
            } else {
                AMLog.d("Cannot load banner because it has no configuration to be applied.");
                notifyNotApplyConfiguration();
                setViewVisibility(false);
            }
        } else {
            AMLog.d("Cannot load banner. Have you ever called method loadAd()?");
            notifyNotApplyConfiguration();
            setViewVisibility(false);
        }
    }


    private void notifyNotApplyConfiguration() {
        //if will doesn't have config to be apply.
        if(mBannerView != null && mBannerView.getListener() != null) {
                ((AMBannerListener) mBannerView.getListener()).onNotApplyConfiguration(mBannerView);
        }
    }

    private void setViewVisibility(boolean isVisible) {
        if(mBannerView != null && mBannerView.isAutoHideView()) {
            if(isVisible) {
                mBannerView.setVisibility(View.VISIBLE);
            } else {
                mBannerView.setVisibility(View.GONE);
            }
        }
    }


    private void setContentView(View bannerView) {
        AMLog.d("display banner ad...");
        if(mBannerView != null && bannerView != null) {
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

            if(getAMView() != null && getAMView().getListener() != null) {
                ((AMBannerListener)getAMView().getListener()).onAdLoaded(mBannerView);
            }
        }

        @Override
        public void onCustomBannerFailed(int errCode) {
            AMLog.w("Banner failed to load...");
            if(getAMView() != null && getAMView().getListener() != null) {
                ((AMBannerListener)getAMView().getListener()).onAdFailed(mBannerView, errCode);
            }
        }

        @Override
        public void onCustomBannerClosed() {
            if(getAMView() != null && getAMView().getListener() != null) {
                ((AMBannerListener)getAMView().getListener()).onAdClosed(mBannerView);
            }
        }

        @Override
        public void onCustomBannerOpened() {
            if(getAMView() != null && getAMView().getListener() != null) {
                ((AMBannerListener)getAMView().getListener()).onAdOpened(mBannerView);
            }
        }

        @Override
        public void onCustomBannerLeftApplication() {
            if( getAMView() != null) {
                AMListener listener = getAMView().getListener();
                if (listener != null && listener instanceof AMBannerListener) {
                    listener.onAdLeftApplication();
                }
            }
        }
    }
}
