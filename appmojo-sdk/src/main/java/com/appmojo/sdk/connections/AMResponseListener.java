package com.appmojo.sdk.connections;

import com.appmojo.sdk.errors.AMError;

/**
 * Created by nutron on 8/4/15 AD.
 */
public interface AMResponseListener<T> {
    void onSuccess(T result);
    void onFail(AMError error);
}
