package com.appmojo.sdk.connections;

import com.appmojo.sdk.errors.AMError;

public interface AMResponseListener<T> {
    void onSuccess(T result);
    void onFail(AMError error);
}
