package com.appmojo.sdk;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

public class AMConnectionData {

    private static final int MAX_RETRY_COUNT = 1;

    @IntDef({POST, GET, PUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Method {}
    public static final int POST = 0;
    public static final int GET = 1;
    public static final int PUT = 2;

    private int method;
    private String url;
    private Map<String, String> header;
    private String body;
    private int retryCount;

    public AMConnectionData() {
        retryCount = 0;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(@Method int method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getMaxRetryCount() {
        return MAX_RETRY_COUNT;
    }


}
