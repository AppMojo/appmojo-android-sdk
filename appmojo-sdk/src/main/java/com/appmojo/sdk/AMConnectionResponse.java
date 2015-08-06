package com.appmojo.sdk;

/**
 * Created by nutron on 7/29/15 AD.
 */
public class AMConnectionResponse {
    private final int statusCode;
    private final String response;

    public AMConnectionResponse(int statusCode, String response) {
        this.statusCode = statusCode;
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
