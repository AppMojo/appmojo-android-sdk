package com.appmojo.sdk;


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

    @Override
    public String toString() {
        return String.format("statusCode: %s, response: %s", statusCode, response);
    }
}
