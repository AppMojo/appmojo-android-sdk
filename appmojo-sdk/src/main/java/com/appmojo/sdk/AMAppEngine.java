package com.appmojo.sdk;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.appmojo.sdk.connections.AMResponseListener;
import com.appmojo.sdk.errors.AMError;
import com.appmojo.sdk.events.AMEvent;
import com.appmojo.sdk.repository.AMEventRepository;
import com.appmojo.sdk.utils.AMLog;


class AMAppEngine implements AMEventTriggerListener {

    private enum ToKenStep {
        REQUEST_TOKEN, REFRESH_TOKEN
    }

    //TODO: for test
    private boolean isDebugMode;
    private boolean isUsingDevServer;

    private static AMAppEngine sInstance;
    private String mAppId;
    private String mAppSecret;
    private AMConfigurationManager mConfigurationManager;
    private Context mContext;
    private AMAuthenticationManager mAuthenManager;
    private AMSessionManager mSessionManager;
    private AMEventDelivery mEventDelivery;
    private AMEventRepository mEventRepository;
    private AMActivityTracker mActivityTracker;

    private AMAppEngine() {
    }

    public static synchronized AMAppEngine getInstance() {
        if (sInstance == null) {
            sInstance = new AMAppEngine();
        }
        return sInstance;
    }

    public void setDebugMode(boolean isDebug) {
        isDebugMode = isDebug;
    }


    public boolean isDebugMode() {
        return isDebugMode;
    }

    public void setUsingDevServer(boolean isDevServer) {
        isUsingDevServer = isDevServer;
    }


    public boolean isUsingDevServer() {
        return isUsingDevServer;
    }


    public String getAppId() {
        return mAppId;
    }


    public String getAppSecret() {
        return mAppSecret;
    }


    public void start(Context context, String appId, String appSecretKey) {
        mAppId = appId;
        mAppSecret = appSecretKey;
        mContext = context.getApplicationContext();
        mAuthenManager = new AMAuthenticationManager(mContext);

        prepareConfigurationManager(mContext);
        prepareEventRepository(mContext);
        prepareEventDelivery(mContext);
        prepareSessionManager(mContext);
        prepareActivityTracker(mContext);

        //start process
        requestToken(ToKenStep.REQUEST_TOKEN);
    }


    private void stop(){
        if(mAuthenManager != null) {
            mAuthenManager.onDestroy();
            mAuthenManager = null;
        }

        if (mConfigurationManager != null) {
            mConfigurationManager.onDestroy();
            mConfigurationManager = null;
        }
    }

    public String getCurrentSessionId(){
        return mSessionManager.getCurrentSessionId();
    }

    public boolean isSessionExpired() {
        return mSessionManager.isSessionExpired(System.currentTimeMillis());
    }

    public void logSession() {
        mSessionManager.logSession();
    }


    private void prepareActivityTracker(Context context) {
        mActivityTracker = new AMActivityTracker(context);
        mActivityTracker.setEventRepository(mEventRepository);
    }

    private void prepareEventDelivery(Context context) {
        mEventDelivery = new AMEventDelivery(context);
        mEventDelivery.setEventRepository(mEventRepository);
    }


    private void prepareEventRepository(Context context) {
        mEventRepository = new AMEventRepository(context);
    }


    private void prepareSessionManager(Context context) {
        mSessionManager = new AMSessionManager(context);
        mSessionManager.setEventTriggerListener(this);
        mSessionManager.setEventRepository(mEventRepository);
    }


    private void prepareConfigurationManager(Context context) {
        if (mConfigurationManager != null) {
            mConfigurationManager.onDestroy();
            mConfigurationManager = null;
        }
        mConfigurationManager = new AMConfigurationManager(context);
        mConfigurationManager.prepare();
    }


    public AMConfiguration getConfiguration(@AMAdType.Type int adType, String placementId) {
        if (mConfigurationManager != null && mConfigurationManager.getConfiguration() != null) {
            if(adType == AMAdType.INTERSTITIAL) {
                return mConfigurationManager.getConfiguration()
                        .getAllInterstitialConfig().get(placementId);
            } else {
                return mConfigurationManager.getConfiguration()
                        .getAllBannerConfig().get(placementId);
            }
        }
        return null;
    }

    public void saveConfiguration() {
        if (mConfigurationManager != null && mConfigurationManager.getConfiguration() != null) {
            mConfigurationManager.getConfiguration().save(mContext);
        }
    }

    private void requestToken(final ToKenStep step) {
        mAuthenManager.requestToken(mAppId, mAppSecret, new AMResponseListener<AMToken>() {
            @Override
            public void onSuccess(AMToken token) {
                AMLog.d("Authentication succeed.");
                if(step == ToKenStep.REQUEST_TOKEN) {
                    requestConfiguration();
                }
            }

            @Override
            public void onFail(AMError error) {
                if(error != null) {
                    AMLog.d("Authentication failed, " + error.toString());
                } else {
                    AMLog.d("Authentication failed with unknown.");
                }
            }
        });
    }

    public void requestConfiguration() {
        final String uuid = AMClientProvider.readUuid(mContext);
        final AMConfigurationRequest request = new AMConfigurationRequest(mAppId, uuid);
        mConfigurationManager.requestConfiguration(request, new AMResponseListener<Boolean>() {
            @Override
            public void onSuccess(Boolean hasChanged) {
                //log session
                mSessionManager.logSession();
                handleOnConfigurationSuccess(hasChanged);
            }

            @Override
            public void onFail(AMError error) {
                //log session
                mSessionManager.logSession();
                handleOnConfigurationFailed(error);
            }
        });
    }

    private void handleOnConfigurationSuccess(boolean hasChanged) {
        AMLog.d("Get configuration success...");
        if (hasChanged) {
            AMLog.d("Notify on configuration change...");
            Intent intent = new Intent(AMBaseConfiguration.ACTION_CONFIGURATION_CHANGE);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
    }

    private void handleOnConfigurationFailed(AMError error) {
        AMLog.d("Get configuration failed...");
        if (error != null) {
            AMLog.w(error.toString());
        }
    }


    /**
     * This method was trigger from Session manager when session is end.
     */
    @Override
    public void onTriggerDelivery() {
        mEventDelivery.startDeliverEvent();
    }

    /**
     * This method was will log the all activity to the database.
     */
    public void logActivity(@AMEvent.Type int type, String placementId, String adUnitId) {
        //call log session to extend the session time
        mSessionManager.logSession();

        //if the experiment id is null or empty that mean it not start experiment yet. Needn't to log activity.
        String experimentId = AMConfigurationHelper.readExperimentId(mContext);
        if(experimentId == null || experimentId.length() < 1) {
            AMLog.w("No running experiment, activity will not be logged ...");
            return;
        }
        mActivityTracker.logActivity(type, placementId, adUnitId);
    }


    /**
     * <b>**THIS METHOD FOR TEST ONLY.**</b>
     * @return AMConfigurationManager
     */
    public AMConfigurationManager getConfigurationManager() {
        return mConfigurationManager;
    }




}
