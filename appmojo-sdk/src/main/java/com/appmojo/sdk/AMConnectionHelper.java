package com.appmojo.sdk;

import android.content.Context;

import com.appmojo.sdk.connections.AMConnectionHandler;
import com.appmojo.sdk.connections.AMConnectionHandlerListener;
import com.appmojo.sdk.connections.AMResponseListener;
import com.appmojo.sdk.errors.AMError;
import com.appmojo.sdk.errors.AMErrorConstant;
import com.appmojo.sdk.utils.AMLog;

class AMConnectionHelper {
    public interface AMConnectionListener {
        void onConnectSuccess(AMConnectionResponse response);
        void onConnectFailed(AMError error);
    }

    private Context mContext;
    AMConnectionHandler mConExecutor;

    public AMConnectionHelper(Context context) {
        mContext = context;
        mConExecutor = new AMConnectionHandler();
    }

    public void request(AMConnectionData data, final AMConnectionListener listener) {
        if(data.getMethod() == AMConnectionData.POST) {
            post(data, listener);
        }

        if(data.getMethod() == AMConnectionData.GET) {
            get(data, listener);
        }

        if(data.getMethod() == AMConnectionData.PUT) {
            put(data, listener);
        }
    }

    private void post(final AMConnectionData data, final AMConnectionListener listener) {
        mConExecutor.post(data.getUrl(), data.getHeader(), data.getBody(),
                new AMConnectionHandlerListener() {
                    @Override
                    public void onConnectSuccess(AMConnectionResponse response) {
                        notifyOnSuccess(data, response, listener);
                    }

                    @Override
                    public void onConnectFailed(AMError error) {
                        handleOnFailed(data, error, listener);
                    }
                });
    }

    public void put(final AMConnectionData data, final AMConnectionListener listener) {
        mConExecutor.put(data.getUrl(), data.getHeader(), data.getBody(),
                new AMConnectionHandlerListener() {
                    @Override
                    public void onConnectSuccess(AMConnectionResponse response) {
                        notifyOnSuccess(data, response, listener);
                    }

                    @Override
                    public void onConnectFailed(AMError error) {
                        handleOnFailed(data, error, listener);
                    }
                });
    }

    public void get(final AMConnectionData data, final AMConnectionListener listener) {
        mConExecutor.get(data.getUrl(), data.getHeader(), data.getBody(),
                new AMConnectionHandlerListener() {
                    @Override
                    public void onConnectSuccess(AMConnectionResponse response) {
                        notifyOnSuccess(data, response, listener);
                    }

                    @Override
                    public void onConnectFailed(AMError error) {
                        handleOnFailed(data, error, listener);
                    }
                });
    }

    private void handleOnFailed(AMConnectionData data, AMError error, AMConnectionListener listener) {
        AMLog.d("handle on connecting to server failed...");
        if (error != null && error.getCode().equals(AMErrorConstant.ERR_TOKEN_EXPIRED)) {
            AMLog.w(error.toString());
            handleTokenExpired(data, listener);
        } else {
            notifyOnFailed(data, error, listener);
        }
    }

    private void handleTokenExpired(final AMConnectionData data, final AMConnectionListener listener) {
        AMLog.d("Handle token expired, Refresh token...");
        AMAuthenticationManager authenManager = new AMAuthenticationManager(mContext);
        authenManager.requestToken(AMAppEngine.getInstance().getAppId(),
                AMAppEngine.getInstance().getAppSecret(), new AMResponseListener<AMToken>() {
                    @Override
                    public void onSuccess(AMToken result) {
                        AMLog.i("Refresh token succeed...");
                        if (data.getHeader().get("Authorization") != null) {
                            data.getHeader().put("Authorization", result.getToken());
                        }
                        request(data, listener);
                    }

                    @Override
                    public void onFail(AMError error) {
                        AMLog.w("Refresh token failed...");
                        data.setRetryCount(data.getRetryCount() + 1);
                        if (data.getRetryCount() < data.getMaxRetryCount()) {
                            handleTokenExpired(data, listener);
                        } else {
                            AMLog.w("Reach maximum retry count to refresh, return error...");
                            notifyOnFailed(data, error, listener);
                        }
                    }
                });
    }

    private void notifyOnSuccess(AMConnectionData data, AMConnectionResponse response, AMConnectionListener listener) {
        data.setRetryCount(0);
        if (listener != null) {
            listener.onConnectSuccess(response);
        }
    }

    private void notifyOnFailed(AMConnectionData data, AMError error, AMConnectionListener listener) {
        data.setRetryCount(0);
        if (listener != null) {
            listener.onConnectFailed(error);
        }
    }

    public void  onDestroy() {
        if(mConExecutor != null) {
            mConExecutor.onDestroy();
            mConExecutor = null;
        }
        mContext = null;
    }
}
