package com.appmojo.sdk.connections;

import com.appmojo.sdk.AMConnectionResponse;
import com.appmojo.sdk.errors.AMError;

import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class AMConnectionTask {

    private AMConnectionHelper mConnectionHelper;
    private AMConnectionListener listener;
    private Map<String, String> headers;
    private String urlStr;
    private String body;
    private String method;

    private AMConnectionResponse mConnectionResponse;


    public AMConnectionTask(AMConnectionHelper connectionHelper) {
        mConnectionHelper = connectionHelper;
    }


    public void prepareTask(String urlStr, String method, Map<String, String> headers, String body, AMConnectionListener listener) {
        this.urlStr = urlStr;
        this.body = body;
        this.method = method;
        this.headers = headers;
        this.listener = listener;
    }


    public Map<String, String> getHeaders() {
        return headers;
    }


    public String getUrlStr() {
        return urlStr;
    }


    public String getMethod() {
        return method;
    }


    public String getBody() {
        return body;
    }


    public AMConnectionListener getListener() {
        return listener;
    }

    public AMConnectionResponse getConnectionResponse() {
        return mConnectionResponse;
    }

    public void setConnectionResponse(AMConnectionResponse connectionResponse) {
        this.mConnectionResponse = connectionResponse;
    }

    public void handleOutput(AMConnectionResponse response) {
        if(listener == null) {
            return;
        }

        if(response == null) {
            AMError amError = new AMError();
            amError.setMessage("parsing error failed when connecting with server.");
            listener.onConnectionFail(amError);
            return;
        }

        if(response.getStatusCode() == HttpsURLConnection.HTTP_OK) {
            listener.onConnectionSuccess(response);
        } else {
            AMError amError = new AMError();
            amError = amError.parse(response.getResponse());
            if (amError == null) {
                amError = new AMError();
                amError.setMessage(response.getResponse());
            }
            amError.setHttpStatusCode(response.getStatusCode());
            listener.onConnectionFail(amError);
        }
    }


    public void handleConnectionState(int state) {
        int outState = AMConnectionHelper.TASK_FAIL;
        // Converts the decode state to the overall state.
        if (state == AMConnectionThread.CONNECT_STATE_COMPLETED) {
            outState = AMConnectionHelper.TASK_COMPLETE;

        }
        // Calls the generalized state method
        handleState(outState);
    }


    // Passes the state to PhotoManager
    public void handleState(int state) {
        /*
         * Passes a handle to this task and the
         * current state to the class that created
         * the thread pools
         */
        mConnectionHelper.handleState(this, state);
    }

}
