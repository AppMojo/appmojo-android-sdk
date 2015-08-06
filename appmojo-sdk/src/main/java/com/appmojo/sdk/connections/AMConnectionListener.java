package com.appmojo.sdk.connections;

import com.appmojo.sdk.AMConnectionResponse;
import com.appmojo.sdk.errors.AMError;

/**
 * Created by nutron on 8/3/15 AD.
 */
public interface AMConnectionListener {
    void onConnectionSuccess(AMConnectionResponse response);
    void onConnectionFail(AMError error);

}
