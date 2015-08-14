package com.appmojo.sdk.connections;

import com.appmojo.sdk.AMConnectionResponse;
import com.appmojo.sdk.errors.AMError;

public interface AMConnectionListener {
    void onConnectionSuccess(AMConnectionResponse response);
    void onConnectionFail(AMError error);

}
