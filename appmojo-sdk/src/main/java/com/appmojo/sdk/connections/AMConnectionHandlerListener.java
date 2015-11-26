package com.appmojo.sdk.connections;

import com.appmojo.sdk.AMConnectionResponse;
import com.appmojo.sdk.errors.AMError;

public interface AMConnectionHandlerListener {
    void onConnectSuccess(AMConnectionResponse response);
    void onConnectFailed(AMError error);

}
