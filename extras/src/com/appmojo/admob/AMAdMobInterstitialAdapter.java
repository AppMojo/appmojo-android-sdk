package com.appmojo.sdk;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


class AMAdMobInterstitialAdapter extends AMCustomInterstitial {
    private static final String TAG = "AppMojo";

    private InterstitialAd mInterstitialAd;
    private Context mContext;
    private AMCustomInterstitialListener mListener;
    private AMInterstitialAdRequest mAdRequest;
    private InterstitialAdMobListener mAdMobListener;

    @Override
    public void loadInterstitial(Context context, AMCustomInterstitialListener listener, AMInterstitialAdRequest config) {
        if (mInterstitialAd != null) {
            invalidate();
        }
        mAdMobListener = new InterstitialAdMobListener();
        mContext = context;
        mListener = listener;
        mAdRequest = config;

        loadAd(mAdRequest, mAdMobListener);
    }

    private void loadAd(AMInterstitialAdRequest adRequest, InterstitialAdMobListener listener) {
        mInterstitialAd = createInterstitialAd(adRequest);
        mInterstitialAd.setAdListener(listener);

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

        mInterstitialAd.loadAd(builder.build());
    }

    @Override
    public void show() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d(TAG, "Tried to show a Google Play Services interstitial ad before it finished loading. Please try again.");
        }
    }

    @Override
    public boolean isLoaded() {
        return mInterstitialAd != null && mInterstitialAd.isLoaded();
    }

    @Override
    public void invalidate() {
        if (mInterstitialAd != null) {
            mInterstitialAd.setAdListener(null);
        }
        mInterstitialAd = null;
        mAdRequest = null;
        mContext = null;
        mAdMobListener = null;
    }

    private InterstitialAd createInterstitialAd(AMInterstitialAdRequest config) {
        InterstitialAd interstitialAd = new InterstitialAd(mContext);
        interstitialAd.setAdUnitId(config.getAdUnitId());
        return interstitialAd;
    }

    //   _____ __  __ __  __             ____ _
    //  |_   _|  \ | |  | | | ___  __ __/ ___| | ____ 	 ___  ___
    //    | | | \| | | \| | |/ _ \| |/ | |   | |/ _  \  / __|/ __|
    //   _| |_| |\ | | |\ | |  __/|   /| |___| | (_)  \ \__ \\__ \
    //  |_____|_| \__|_| \__|\___||__|  \____|_|\___/|_\|___/|___/

    private class InterstitialAdMobListener extends AdListener {
        /*
         * Google Play Services AdListener implementation
         */
        @Override
        public void onAdClosed() {
            Log.d(TAG, "Google Play Services interstitial ad closed");
            if (mListener != null) {
                mListener.onCustomInterstitialDismissed();
            }
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            Log.d(TAG, "Google Play Services interstitial ad failed to load.");
            if (mListener != null) {
                mListener.onCustomInterstitialFailed(errorCode);
            }
        }

        @Override
        public void onAdLoaded() {
            Log.d(TAG, "Google Play Services interstitial ad loaded successfully. Showing ad...");
            if (mListener != null) {
                mListener.onCustomInterstitialLoaded();
            }
        }

        @Override
        public void onAdOpened() {
            Log.d(TAG, "Google Play Services interstitial ad shown.");
            if (mListener != null) {
                mListener.onCustomInterstitialShown();
            }
        }
    }
}
