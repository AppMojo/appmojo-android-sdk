package com.appmojo.sdk;

import android.content.Context;
import android.util.Log;

import com.appmojo.sdk.base.AMAdSize;
import com.appmojo.sdk.utils.AMViewUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


class AMAdMobBannerAdapter extends AMCustomBanner {
    private static final String TAG = "AppMojo";

    private AdView mAdView;
    private Context mContext;
    private AMCustomBannerListener mListener;
    private AMBannerAdRequest mAdRequest;
    private BannerAdMobListener mAdMobListener;

    @Override
    public void loadBanner(Context context, AMCustomBannerListener listener, AMBannerAdRequest adRequest) {
        if (mAdView != null) {
            mAdView.setAdListener(null);
            mAdView.pause();
            mAdView.invalidate();
        }
        mAdMobListener = new BannerAdMobListener();
        mContext = context;
        mListener = listener;
        mAdRequest = adRequest;

        loadAd(mAdRequest, mAdMobListener);
    }

    private void loadAd(AMBannerAdRequest adRequest, BannerAdMobListener listener) {
        mAdView = createAdView(adRequest);
        mAdView.setAdListener(listener);

        AdRequest.Builder builder = new AdRequest.Builder();
        if (adRequest.getRequestAgent() != null) {
            builder.setRequestAgent(adRequest.getRequestAgent());
        }

        if (adRequest.getContentUrl() != null) {
            builder.setContentUrl(adRequest.getContentUrl());
        }

        if (adRequest.getBirthday() != null) {
            builder.setBirthday(adRequest.getBirthday());
        }

        if (adRequest.getLocation() != null) {
            builder.setLocation(adRequest.getLocation());
        }

        if (adRequest.getGender() == AdRequest.GENDER_MALE
                || adRequest.getGender() == AdRequest.GENDER_FEMALE) {
            builder.setGender(adRequest.getGender());
        }

        for (String deviceId : adRequest.getTestDeviceIds()) {
            builder.addTestDevice(deviceId);
        }

        for (String keyword : adRequest.getKeywords()) {
            builder.addKeyword(keyword);
        }
        mAdView.loadAd(builder.build());
    }

    private AdView createAdView(AMBannerAdRequest adRequest) {
        AdSize adSize = parseAdSize(adRequest.getAdSize());
        AdView adView = new AdView(mContext);
        adView.setAdSize(adSize);
        adView.setAdUnitId(adRequest.getAdUnitId());
        return adView;
    }

    @Override
    public void invalidate() {
        if (mAdView != null) {
            AMViewUtils.removeFromParent(mAdView);
            mAdView.invalidate();
            mAdView.setAdListener(null);
            mAdView.destroy();
        }
    }

    @Override
    public void destroy() {
        invalidate();
        mAdView = null;
        mAdRequest = null;
        mContext = null;
        mAdMobListener = null;
    }

    private AdSize parseAdSize(AMAdSize adSize) {
        AdSize size;
        if (adSize == LARGE_BANNER) {
            size = AdSize.LARGE_BANNER;

        } else if (adSize == FULL_BANNER) {
            size = AdSize.FULL_BANNER;

        } else if (adSize == LEADER_BOARD) {
            size = AdSize.LEADERBOARD;

        } else if (adSize == MEDIUM_RECTANGLE) {
            size = AdSize.MEDIUM_RECTANGLE;

        } else if (adSize == WIDE_SKYSCRAPER) {
            size = AdSize.WIDE_SKYSCRAPER;

        } else if (adSize == SMART_BANNER) {
            size = AdSize.SMART_BANNER;

        } else {
            size = AdSize.BANNER;

        }
        return size;
    }

    //   _____ __  __ __  __             ____ _
    //  |_   _|  \ | |  | | | ___  __ __/ ___| | ____    ___  ___
    //    | | | \| | | \| | |/ _ \| |/ | |   | |/ _  \  / __|/ __|
    //   _| |_| |\ | | |\ | |  __/|   /| |___| | (_)  \ \__ \\__ \
    //  |_____|_| \__|_| \__|\___||__|  \____|_|\___/|_\|___/|___/

    private class BannerAdMobListener extends AdListener {
        /*
         * Google Play Services AdListener implementation
         */
        @Override
        public void onAdClosed() {
            Log.d(TAG, "Google Play Services banner ad closed");
            if (mListener != null) {
                mListener.onCustomBannerClosed();
            }
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            Log.d(TAG, "Google Play Services banner ad failed to load.");
            if (mListener != null) {
                mListener.onCustomBannerFailed(errorCode);
            }
        }

        @Override
        public void onAdLeftApplication() {
            Log.d(TAG, "Google Play Services banner ad left application.");
            if (mListener != null) {
                mListener.onCustomBannerLeftApplication();
            }
        }

        @Override
        public void onAdLoaded() {
            Log.d(TAG, "Google Play Services banner ad loaded successfully. Showing ad...");
            if (mListener != null) {
                mListener.onCustomBannerLoaded(mAdView);
            }
        }

        @Override
        public void onAdOpened() {
            Log.d(TAG, "Google Play Services banner ad clicked.");
            if (mListener != null) {
                mListener.onCustomBannerOpened();
            }
        }
    }
}
