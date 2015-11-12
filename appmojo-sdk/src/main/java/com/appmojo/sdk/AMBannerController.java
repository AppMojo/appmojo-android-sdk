package com.appmojo.sdk;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.appmojo.sdk.events.AMEvent;
import com.appmojo.sdk.utils.AMLog;


class AMBannerController extends AMController {
    private static final int MINIMUM_REFRESH_RATE_SECOND = 30;

    //no prefix private because it easy to test
    AMCustomBannerListener mCustomListener;
    private AMCustomBanner mCustomBanner;
    private AMBannerView mBannerView;
    private Handler mHandler;
    private Runnable mRefreshRunnable;
    private int mVisibility = View.VISIBLE;

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

        //get configuration
        mCustomAdRequest = getApplyAdRequest(mAdRequest);

        //create custom class to handle configuration
        if(mCustomAdRequest != null) {
            try {
                className = AMClassFactory.getClassName(mCustomAdRequest.getAdNetwork(), AMAdType.BANNER);
                mCustomBanner = AMCustomBannerFactory.create(className);
            } catch (Exception e) {
                mCustomAdRequest = null;
                AMLog.w("AppMojo", "AppMojo cannot find class %s, make sure you add it to SDK module.", className);
            }
        }

        //apply configuration
        applyAdRequest(mCustomAdRequest);
    }


    @Override
    public void onVisibilityChanged(int visibility) {
        this.mVisibility = visibility;
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
        if(mCustomBanner != null && adRequest != null) {
            applyAutoHideView(false);
            if(mVisibility == View.VISIBLE) {
                mCustomBanner.loadBanner(mContext, mCustomListener, (AMBannerAdRequest) adRequest);
            } else {
                AMLog.d("Banner is not visible. Not refreshing ad.");
            }
            scheduleRefreshTime((AMBannerAdRequest)adRequest);

        } else {
            if(mAMView != null) {
                AMLog.w("No configuration to be applied for placement id: " + mAMView.getPlacementUid());
            }
            notifyNotApplyConfiguration();
            applyAutoHideView(true);
        }
    }


    private void notifyNotApplyConfiguration() {
        //if will doesn't have config to be apply.
        if(mBannerView != null && mBannerView.getListener() != null) {
                ((AMBannerListener) mBannerView.getListener()).onNotApplyConfiguration(mBannerView);
        }
    }


    private void applyAutoHideView(boolean isHide) {
        if(mBannerView != null && mBannerView.isAutoHideView()) {
            if(isHide) {
                mBannerView.setVisibility(View.GONE);
            } else {
                mBannerView.setVisibility(View.VISIBLE);
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

        cancelRefreshTime();

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
        return (AMBannerConfiguration) mAppEngine.getConfiguration(
                AMAdType.BANNER, mBannerView.getPlacementUid());

    }


    private void scheduleRefreshTime (AMBannerAdRequest adRequest) {
        int refreshRate = adRequest.getRefreshRate();
        if( refreshRate >= MINIMUM_REFRESH_RATE_SECOND) {
            cancelRefreshTime();

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


    private void cancelRefreshTime() {
        AMLog.d("Cancel refresh ad timer.");
        if(mHandler != null) {
            mHandler.removeCallbacks(mRefreshRunnable);
        }
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

            //log session
            AMAppEngine.getInstance().logSession();

            //log activity
            logActivity(AMEvent.IMPRESSION);

            if(getAMView() != null && getAMView().getListener() != null) {
                ((AMBannerListener)getAMView().getListener()).onAdLoaded(mBannerView);
            }


        }

        @Override
        public void onCustomBannerFailed(int errCode) {
            //log session
            AMAppEngine.getInstance().logSession();

            if(mBannerView != null) {
                AMLog.i("Banner failed to load on placement id: " + mBannerView.getPlacementUid());
            }
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
            AMLog.i("Banner clicked...");

            //log session
            AMAppEngine.getInstance().logSession();

            //log activity
            logActivity(AMEvent.CLICK);

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
