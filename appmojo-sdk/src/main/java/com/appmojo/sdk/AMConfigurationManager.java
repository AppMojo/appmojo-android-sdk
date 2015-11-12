package com.appmojo.sdk;

import android.content.Context;

import com.appmojo.sdk.connections.AMConnectionHelper;
import com.appmojo.sdk.connections.AMConnectionListener;
import com.appmojo.sdk.connections.AMResponseListener;
import com.appmojo.sdk.errors.AMError;
import com.appmojo.sdk.errors.AMErrorConstant;
import com.appmojo.sdk.utils.AMLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;


class AMConfigurationManager {
    private static final int MAX_RETRY_COUNT = 1;

    private Context mContext;
    private AMConfigurationResponse mConfigResponse;
    private AMConfigurationRequest mRequest;
    private int retryCount = 0;
    private AMConnectionHelper mConnectionHelper;

    public AMConfigurationManager(Context context) {
        mContext = context;
    }


    public void prepare() {
        retryCount = 0;
        mConfigResponse = AMConfigurationHelper.readConfiguration(mContext);
    }

    public String getExperimentId() {
        return AMConfigurationHelper.readExperimentId(mContext);
    }


    public String getVariantId() {
        return AMConfigurationHelper.readVariantId(mContext);
    }


    public synchronized AMConfigurationResponse getConfiguration() {
        return mConfigResponse;
    }


    private Map<String, String> getHeaderData(Context context) {
        String token = AMTokenHelper.getToken(context);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", token);
        headers.put("Content-Type", "application/json");
        return headers;
    }


    private String createJsonBody(String deviceUuid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("device_id", deviceUuid);
        } catch (JSONException e) {
            AMLog.e("Failed to create authentication body.", e);
        }
        return jsonObject.toString();
    }


    public void requestConfiguration(AMConfigurationRequest amRequest, AMResponseListener<Boolean> listener) {
        AMLog.d("Request configuration...");
        retryCount = 0;
        mRequest = amRequest;
        startRequestProcess(mRequest, listener);
    }


    private void startRequestProcess(AMConfigurationRequest amRequest, final AMResponseListener<Boolean> listener) {
        String urlStr = AMBaseConfiguration.getUrlConfiguration(amRequest.getAppId());
        String body = createJsonBody(amRequest.getDeviceId());
        Map<String, String> headers = getHeaderData(mContext);

        mConnectionHelper = new AMConnectionHelper();
        mConnectionHelper.put(urlStr, headers, body, new AMConnectionListener() {
            @Override
            public void onConnectionSuccess(AMConnectionResponse response) {
                handleResponse(response, listener);
            }

            @Override
            public void onConnectionFail(AMError error) {
                if (listener != null) {
                    listener.onFail(error);
                }
            }
        });
    }


    private void handleResponse(AMConnectionResponse response, AMResponseListener<Boolean> listener) {
        if(response.getStatusCode() == HttpURLConnection.HTTP_OK) {
            AMConfigurationResponse configResponse = new AMConfigurationResponse();
            configResponse = configResponse.parse(response.getResponse());

            if (configResponse != null) {
                String saveVariantId = mConfigResponse == null ? null : mConfigResponse.getVariantId();
                int saveRevisionNumber = mConfigResponse == null ? -1 : mConfigResponse.getRevisionNumber();
                String variantId = configResponse.getVariantId();
                int revisionNumber = configResponse.getRevisionNumber();
                if (saveVariantId == null || !saveVariantId.equals(variantId)
                        || saveRevisionNumber == -1 || saveRevisionNumber != revisionNumber) {
                    AMLog.d("Configuration's revision not match, update config....");
                    mConfigResponse = configResponse;
                    AMConfigurationHelper.writeConfiguration(mContext, response.getResponse());

                    //notify to caller
                    if (listener != null) {
                        listener.onSuccess(true);
                    }

                } else {
                    AMLog.d("Same configuration's version, no need to update.");
                    //notify to caller
                    if (listener != null) {
                        listener.onSuccess(false);
                    }
                }

            } else {
                AMError error = new AMError().parse(response.getResponse());
                if(error != null) {
                    error.setHttpStatusCode(response.getStatusCode());
                    //check the error type
                    handleOnConfigurationFailed(error, listener);
                } else {
                    if (listener != null) {
                        AMError amError = new AMError();
                        amError.setMessage("Failed to get configuration from server, parsing error failed.");
                        listener.onFail(amError);
                    }
                }
            }

        } else {
            if (listener != null) {
                AMError amError = new AMError();
                amError.setMessage("Failed to get configuration from server, no configuration found.");
                listener.onFail(amError);
            }
        }
    }


    private void handleOnConfigurationFailed(AMError error, AMResponseListener<Boolean> listener) {
        AMLog.d("handle on get configuration failed...");
        if (error != null) {
            AMLog.w(error.toString());
            if (error.getCode().equals(AMErrorConstant.ERR_TOKEN_EXPIRED)) {
                AMLog.d("Failed with ERR_TOKEN_EXPIRED...");
                handleTokenExpired(error, listener);
            } else {
                //notify caller fail
                if (listener != null)
                    listener.onFail(error);
            }
        }
    }


    private void handleTokenExpired(final AMError error, final AMResponseListener<Boolean> listener) {
        AMLog.d("Handle token expired, Refresh token...");
        AMAppEngine.getInstance().refreshToken(new AMRefreshTokenListener() {
            @Override
            public void onRefreshTokenSuccess() {
                AMLog.d("Refresh token succeed...");
                handleOnRefreshTokenSuccess(error, listener);
            }

            @Override
            public void onRefreshTokenFail() {
                AMLog.d("Refresh token failed...");
                //notify caller fail
                if (listener != null)
                    listener.onFail(error);
            }
        });
    }


    private void handleOnRefreshTokenSuccess(AMError error, AMResponseListener<Boolean> listener) {
        if(retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            startRequestProcess(mRequest, listener);
        } else {
            AMLog.w("Reach maximum retry count to refresh, return error...");
            retryCount = 0;
            if (listener != null)
                listener.onFail(error);
        }
    }


    public void onDestroy() {
        if(mConfigResponse != null) {
            try {
                AMConfigurationHelper.writeConfiguration(
                        mContext, mConfigResponse.toJsonObject().toString());
            } catch (JSONException e) {
                AMLog.w("Cannot save configuration while object is being destroyed...");
            }
        }
        if(mConnectionHelper != null) {
            mConnectionHelper.shutdownAllTask();
            mConnectionHelper = null;
        }

        mContext = null;
    }


    /************************** FOR TEST *****************************/
    public void setTestData(AMConfigurationResponse configs) {
        mConfigResponse = configs;
    }

    public void addTestData(AMConfiguration config) {
        if(config instanceof AMInterstitialConfiguration)
            mConfigResponse.getAllInterstitialConfig().put(config.getPlacementId(), (AMInterstitialConfiguration)config);

        if(config instanceof AMBannerConfiguration)
            mConfigResponse.getAllBannerConfig().put(config.getPlacementId(), (AMBannerConfiguration) config);

    }

}
