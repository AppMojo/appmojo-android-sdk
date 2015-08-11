package com.appmojo.sdk;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.appmojo.sdk.connections.AMResponseListener;
import com.appmojo.sdk.errors.AMError;
import com.appmojo.sdk.mock.AMMock;
import com.appmojo.sdk.utils.AMLog;

import java.util.Map;


/**
 * Created by nutron on 7/20/15 AD.
 */
public class AMAppEngine {
    private enum ToKenStep {
        REQUEST_TOKEN, REFRESH_TOKEN
    }

    private static AMAppEngine sInstance;
    private String mAppId;
    private String mAppSecret;
    private AMConfigurationManager mConfigurationManager;
    private Context mContext;
    private boolean isDebugMode;
    private AMAuthenticationManager mAuthenManager;

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

    public void start(Context context, String appId, String appSecretKey) {
        mAppId = appId;
        mAppSecret = appSecretKey;
        mContext = context.getApplicationContext();
        mAuthenManager = new AMAuthenticationManager(mContext);
        prepareConfigurationManager(mContext);

        //start process
        requestToken(mAppId, mAppSecret, ToKenStep.REQUEST_TOKEN);
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


    private void prepareConfigurationManager(Context context) {
        if (mConfigurationManager != null) {
            mConfigurationManager.onDestroy();
            mConfigurationManager = null;
        }
        mConfigurationManager = new AMConfigurationManager(context);
        mConfigurationManager.prepare();
    }

    public AMConfiguration getConfiguration(String placementId) {
        if (mConfigurationManager != null) {
            return mConfigurationManager.getConfiguration(placementId);
        }
        return null;
    }


    public void refreshToken(final AMRefreshTokenListener listener) {
        mAuthenManager.requestToken(mAppId, mAppSecret, new AMResponseListener<AMToken>() {

            @Override
            public void onSuccess(AMToken token) {
                AMLog.d("Refresh token succeed.");
                AMLog.d("Token : " + token.toString());
                if(listener != null)
                    listener.onRefreshTokenSuccess();
            }

            @Override
            public void onFail(AMError error) {
                AMLog.d("Refresh token failed.");
                if(listener != null)
                    listener.onRefreshTokenFail();
            }
        });
    }


    private void requestToken(String appId, String appSecretKey, final ToKenStep step) {
        mAuthenManager.requestToken(appId, appSecretKey, new AMResponseListener<AMToken>() {
            @Override
            public void onSuccess(AMToken token) {
                AMLog.d("Authentication succeed.");
                AMLog.d("Token : " + token.toString());
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

    private void requestConfiguration() {
        final String uuid = AMClientProvider.readUuid(mContext);
        final AMConfigurationRequest request = new AMConfigurationRequest(mAppId, uuid);
        mConfigurationManager.requestConfiguration(request, new AMResponseListener<Boolean>() {
            @Override
            public void onSuccess(Boolean hasChanged) {
                handleOnConfigurationSuccess(hasChanged);
            }

            @Override
            public void onFail(AMError error) {
                handleOnConfigurationFailed(error);
            }
        });
    }

    private void handleOnConfigurationSuccess(boolean hasChanged) {
        AMLog.d("Get configuration success...");
        if (hasChanged) {
            AMLog.d("Broadcasting on configuration change...");
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
     *  =================== FOR TEST ======================
     * @param mock : mock data
     */
    public void setMockData(Map<String, AMMock> mock) {
        AMMockHelper mockHelper = new AMMockHelper();
        mConfigurationManager.setTestData(mockHelper.generateConfiguration(mock));
    }
}
