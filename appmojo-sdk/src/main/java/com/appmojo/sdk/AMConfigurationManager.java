package com.appmojo.sdk;

import android.content.Context;

import com.appmojo.sdk.connections.AMResponseListener;
import com.appmojo.sdk.errors.AMError;
import com.appmojo.sdk.utils.AMLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


class AMConfigurationManager {
    private static final int MAX_RETRY_COUNT = 1;

    private Context mContext;
    private AMConfigurationResponse mConfigResponse;
    private AMConfigurationRequest mRequest;
    private AMConnectionHelper mConnectionHelper;

    public AMConfigurationManager(Context context) {
        mContext = context;
    }


    public void prepare() {
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


    public void requestConfiguration(AMConfigurationRequest amRequest, AMResponseListener<Boolean> listener) {
        AMLog.d("Request configuration...");
        mRequest = amRequest;
        startRequestProcess(mRequest, listener);
    }


    private void startRequestProcess(AMConfigurationRequest amRequest, final AMResponseListener<Boolean> listener) {
        String urlStr = AMBaseConfiguration.getUrlConfiguration(amRequest.getAppId());
        String body = createJsonBody(amRequest.getDeviceId());
        Map<String, String> headers = getHeaderData(mContext);

        AMConnectionData data = new AMConnectionData();
        data.setUrl(urlStr);
        data.setMethod(AMConnectionData.PUT);
        data.setBody(body);
        data.setHeader(headers);

        mConnectionHelper = new AMConnectionHelper(mContext);
        mConnectionHelper.request(data, new AMConnectionHelper.AMConnectionListener() {
            @Override
            public void onConnectSuccess(AMConnectionResponse response) {
                handleResponse(response, listener);
            }

            @Override
            public void onConnectFailed(AMError error) {
                if (listener != null)
                    listener.onFail(error);
            }
        });
    }


    private void handleResponse(AMConnectionResponse response, AMResponseListener<Boolean> listener) {
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
                if (listener != null) {
                    listener.onFail(error);
                }
            } else {
                if (listener != null) {
                    error = new AMError();
                    error.setMessage("Failed to get configuration from server, parsing error failed.");
                    listener.onFail(error);
                }
            }
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
            mConnectionHelper.onDestroy();
            mConnectionHelper = null;
        }

        mContext = null;
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
