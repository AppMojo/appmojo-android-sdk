package com.appmojo.sdk;

import android.content.Context;

import com.appmojo.sdk.connections.AMConnectionHandler;
import com.appmojo.sdk.connections.AMConnectionHandlerListener;
import com.appmojo.sdk.connections.AMResponseListener;
import com.appmojo.sdk.errors.AMError;
import com.appmojo.sdk.utils.AMLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


class AMAuthenticationManager {

    private Context mContext;
    private AMConnectionHandler mConnectionHelper;

    public AMAuthenticationManager(Context context){
        mContext = context;
        mConnectionHelper = new AMConnectionHandler();
    }

    public void requestToken(String appId, String appSecret, final AMResponseListener<AMToken> listener) {
        String urlStr = AMBaseConfiguration.getUrlAuthen();
        String body = createJsonBody(appId, appSecret);
        Map<String, String> headers = getHeaderData();

        mConnectionHelper.post(urlStr, headers, body, new AMConnectionHandlerListener() {
            @Override
            public void onConnectSuccess(AMConnectionResponse response) {
                handleOnConnectionSuccess(response, listener);
            }

            @Override
            public void onConnectFailed(AMError error) {
                if(listener != null)
                    listener.onFail(error);
            }
        });
    }

    private void handleOnConnectionSuccess(AMConnectionResponse response, AMResponseListener<AMToken> listener) {
        AMToken token = new AMToken();
        token = token.parse(response.getResponse());
        if(token != null && token.getToken() != null) {
            AMTokenHelper.writeToken(mContext, token);
            if(listener != null)
                listener.onSuccess(token);
        } else {
            if(listener != null) {
                AMError amError = new AMError();
                amError.setMessage("Failed to authenticate with server.");
                listener.onFail(amError);
            }
        }
    }

    private Map<String, String> getHeaderData() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }

    private String createJsonBody(String appId, String appSecret) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("grant_type", "client_credentials");
            jsonObject.put("app_id", appId);
            jsonObject.put("app_secret", appSecret);
        } catch (JSONException e) {
            AMLog.e("Failed to create authentication body.", e);
        }
        return jsonObject.toString();
    }

    public void onDestroy(){
        mContext = null;
        if(mConnectionHelper != null) {
            mConnectionHelper.shutdownAllTask();
            mConnectionHelper = null;
        }

    }
}
