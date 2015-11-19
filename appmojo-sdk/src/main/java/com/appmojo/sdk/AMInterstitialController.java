package com.appmojo.sdk;

import android.content.Context;

import com.appmojo.sdk.events.AMEvent;
import com.appmojo.sdk.utils.AMLog;
import com.appmojo.sdk.utils.TimeUtils;


class AMInterstitialController extends AMController {
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
            return ((AMInterstitialAdRequest)mCustomAdRequest).getFrequencySession();
        }
        return 0;
    }

    int getHourFrequency() {
        if(mCustomAdRequest != null && mCustomAdRequest instanceof AMInterstitialAdRequest) {
            return ((AMInterstitialAdRequest)mCustomAdRequest).getFrequencyHour();
        }
        return 0;
    }

    int getDayFrequency() {
        if(mCustomAdRequest != null && mCustomAdRequest instanceof AMInterstitialAdRequest) {
            return ((AMInterstitialAdRequest)mCustomAdRequest).getFrequencyDay();
        }
        return 0;
    }

    @Override
    public void loadAd(AMAdRequest adRequest) {
        AMLog.d("AppMojo", "[PlcID: %s] load interstitial ad...", mAMInterstitial.getPlacementUid());
        //sometime user call multiple time
        String className = null;

        mCustomAdRequest = getApplyAdRequest(adRequest);

        if(mCustomAdRequest != null) {
            try {
                className = AMClassFactory.getClassName(mCustomAdRequest.getAdNetwork(), AMAdType.INTERSTITIAL);
                mCustomInterstitial = AMCustomInterstitialFactory.create(className);

            } catch (Exception e) {
                mCustomInterstitial = null;
                AMLog.w("AppMojo", "AppMojo cannot find class %s, make sure you add it to SDK module.", className);
            }
        }

        applyAdRequest(mCustomAdRequest);
    }

    @Override
    public void onVisibilityChanged(int isVisibility) {
        //not thing
    }

    @Override
    public void reloadAd() {
        AMLog.d("Reload interstitial ad...");
        if(mCustomAdRequest != null) {
            applyAdRequest(mCustomAdRequest);
        } else {
            AMLog.w("AppMojo", "[PlcID: %s] No configuration could be applied. Did you call loadAd()?", mAMView.getPlacementUid());
        }
    }

    @Override
    protected void applyAdRequest(AMCustomAdRequest adRequest) {
        AMLog.d("AppMojo", "[PlcID: %s] Apply interstitial configuration...", mAMView.getPlacementUid());
        if(mCustomInterstitial != null && adRequest != null) {
            if(isAllowToShow()) {
                mCustomInterstitial.invalidate();
                mCustomInterstitial.loadInterstitial(mContext, mCustomListener, (AMInterstitialAdRequest) adRequest);
                saveConfiguration();
            } else {
                AMLog.d("AppMojo", "[PlcID: %s] Interstitial ad not allow to load.", mAMView.getPlacementUid());
            }
        } else {
            AMLog.w("AppMojo", "[PlcID: %s] No configuration could be applied. ", mAMView.getPlacementUid());
            notifyNotApplyConfiguration();
        }
    }

    public boolean isLoaded() {
        if(mCustomInterstitial != null) {
            return mCustomInterstitial.isLoaded();
        } else {
            AMLog.d("AppMojo", "[PlcID: %s] Interstitial ad not load yet.", mAMView.getPlacementUid());
        }
        return false;
    }

    public void show() {
        if(mCustomInterstitial != null && mCustomInterstitial.isLoaded()) {
            if(isAllowToShow()) {
                AMLog.d("AppMojo", "[PlcID: %s] show interstitial ad...", mAMView.getPlacementUid());
                mCustomInterstitial.show();
                increaseFrequencyCapAndSave();
            } else {
                AMLog.d("AppMojo", "[PlcID: %s] Interstitial ad not allow to show.", mAMView.getPlacementUid());
            }
        } else {
            AMLog.d("AppMojo", "[PlcID: %s] Tried to show a interstitial ad before it finished loading. Please try again.", mAMView.getPlacementUid());
        }
    }

    private void increaseFrequencyCapAndSave() {
        AMInterstitialConfiguration config = getConfiguration();
        if(mCustomAdRequest == null || config == null) { // verify View already has been called loadAd()
            return;
        }
        config.setDayFrequencyCount(config.getDayFrequencyCount() + 1);
        config.setHourFrequencyCount(config.getHourFrequencyCount() + 1);
        config.setSessionFrequencyCount(config.getSessionFrequencyCount() + 1);
        saveConfiguration();
    }


    private void saveConfiguration() {
        AMAppEngine.getInstance().saveConfiguration();
    }


    private boolean isAllowToShow() {
        AMInterstitialConfiguration config = getConfiguration();
        if(mCustomAdRequest == null || config == null) {
            AMLog.w("AppMojo", "[PlcID: %s] No configuration could be applied for placement id: ", mAMView.getPlacementUid());
            return false;
        }

        boolean isAllow;
        int curHour = TimeUtils.getUTCCurrentHour();
        String curDate = TimeUtils.getUTCCurrentDate();


        if(curDate.equals(config.getLastDate())) { //same date
            isAllow = isAllowToShowInTermOfDay(config, curHour);
        } else { // new date
            config.setLastDate(curDate);
            config.setDayFrequencyCount(0);
            config.setLastHour(curHour);
            config.setHourFrequencyCount(0);
            isAllow = isAllowToShowInTermOfDay(config, curHour);
        }
        return isAllow;
    }


    private boolean isAllowToShowInTermOfDay(AMInterstitialConfiguration config, int curHour) {
        if (config.getDayFrequency() == 0 ||
                config.getDayFrequencyCount() < config.getDayFrequency()) { //not reach day limit
            if (curHour == config.getLastHour()) { //same date, hour
                return isAllowToShowInTermOfHour(config);

            } else {  //same date, but NOT same hour
                config.setLastHour(curHour); // update hour
                config.setHourFrequencyCount(0); // reset hour count
                return isAllowToShowInTermOfHour(config);
            }
        } else { //same date but reach day limit
            AMLog.w("AppMojo", "[PlcID: %s] Reach day frequency limit.", mAMView.getPlacementUid());
            return false;
        }
    }


    private boolean isAllowToShowInTermOfHour(AMInterstitialConfiguration config) {
        String sessionId = AMAppEngine.getInstance().getCurrentSessionId();

        if (config.getHourFrequency() == 0 ||
                config.getHourFrequencyCount() < config.getHourFrequency()) { //not reach hour limit
            if (sessionId.equals(config.getLastSessionId())) { //same date, hour, session
                return isAllowToShowInTermOfSession(config);

            } else { // same date, hour, but NOT session
                config.setLastSessionId(sessionId); // update session
                config.setSessionFrequencyCount(0); // reset session count
                return isAllowToShowInTermOfSession(config);
            }

        } else { //reach hour limit
            AMLog.w("AppMojo", "[PlcID: %s] reach hour frequency limit.", mAMView.getPlacementUid());
            return false; //needn't to show ad
        }
    }


    private boolean isAllowToShowInTermOfSession(AMInterstitialConfiguration config) {
        if (config.getSessionFrequency() == 0 ||
                config.getSessionFrequencyCount() < config.getSessionFrequency()) { //NOT reach session limit
            return true;
        } else { //reach session limit
            AMLog.w("AppMojo", "[PlcID: %s] Reach session frequency limit.", mAMView.getPlacementUid());
            return false;
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
        return (AMInterstitialConfiguration) AMAppEngine.getInstance()
                .getConfiguration(AMAdType.INTERSTITIAL, mAMInterstitial.getPlacementUid());
    }


    //   _____ __  __ __  __             ____ _
    //  |_   _|  \ | |  | | | ___  __ __/ ___| | ____    ___  ___
    //    | | | \| | | \| | |/ _ \| |/ | |   | |/ _  \  / __|/ __|
    //   _| |_| |\ | | |\ | |  __/|   /| |___| | (_)  \ \__ \\__ \
    //  |_____|_| \__|_| \__|\___||__|  \____|_|\___/|_\|___/|___/


    private class AMInterstitialControllerListener implements AMCustomInterstitialListener {
        @Override
        public void onCustomInterstitialLoaded() {
            //log session
            AMAppEngine.getInstance().logSession();

            if(getAMView() != null && getAMView().getListener() != null) {
                ((AMInterstitialListener)getAMView().getListener()).onAdLoaded(mAMInterstitial);
            }
        }

        @Override
        public void onCustomInterstitialFailed(int error) {
            //log session
            AMAppEngine.getInstance().logSession();

            if(getAMView() != null && getAMView().getListener() != null) {
                ((AMInterstitialListener)getAMView().getListener()).onAdFailed(mAMInterstitial, error);
            }
        }

        @Override
        public void onCustomInterstitialShown() {
            //log session
            AMAppEngine.getInstance().logSession();

            logActivity(AMEvent.IMPRESSION);
            if(getAMView() != null && getAMView().getListener() != null) {
                ((AMInterstitialListener)getAMView().getListener()).onAdOpened(mAMInterstitial);
            }
        }

        @Override
        public void onCustomInterstitialDismissed() {
            if(getAMView() != null && getAMView().getListener() != null) {
                ((AMInterstitialListener)getAMView().getListener()).onAdClosed(mAMInterstitial);
            }
        }

        @Override
        public void onCustomInterstitialLeftApplication() {
            logActivity(AMEvent.CLICK);
            if(getAMView() != null &&  getAMView().getListener() != null) {
                getAMView().getListener().onAdLeftApplication();
            }
        }
    }
}
