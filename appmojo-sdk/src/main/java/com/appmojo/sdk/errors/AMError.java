package com.appmojo.sdk.errors;

import com.appmojo.sdk.AMJsonParser;
import com.appmojo.sdk.utils.AMLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AMError implements AMJsonParser<AMError>{

    private static final int DEFAULT_ERROR_CODE = -1;

    private int httpStatusCode;
    private String code;
    private String message;
    private List<AMSubError> subErrors;

    public AMError() {
        code = "Unknown";
        this.httpStatusCode = -1;
        subErrors = new ArrayList<>();
    }


    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHttpStatusCode(int statusCode) {
        this.httpStatusCode = statusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<AMSubError> getSubErrors() {
        return subErrors;
    }

    public void addSubErrors(AMSubError subError) {
        this.subErrors.add(subError);
    }


    @Override
    public AMError parse(String jsonString) {
        if(jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                return parse(jsonObject);
            } catch (JSONException e) {
                AMLog.e("JSON format exception.", e);
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public AMError parse(JSONObject errObject) {
        if(errObject == null) {
            return null;
        }
        try {
            if (errObject.has(AMErrorConstant.KEY_CODE)) {
                this.setCode(errObject.getString(AMErrorConstant.KEY_CODE));
            }
            if (errObject.has(AMErrorConstant.KEY_MESSAGE)) {
                this.setMessage(errObject.getString(AMErrorConstant.KEY_MESSAGE));
            }

            if (errObject.has(AMErrorConstant.KEY_ERRORS)) {
                JSONArray errArray = errObject.getJSONArray(AMErrorConstant.KEY_ERRORS);
                for (int i = 0; i < errArray.length(); i++) {
                    AMSubError subErr = new AMSubError();
                    subErr = subErr.parse(errArray.getJSONObject(i));

                    if (subErr != null && subErr.getCode() != DEFAULT_ERROR_CODE) {
                        this.addSubErrors(subErr);
                    }
                }
            }
        } catch (JSONException e) {
            AMLog.e("Parsing error message failed.", e);
            return null;
        }
        return this;
    }


    @Override
    public String toString() {
        return new StringBuffer()
                .append("httpStatusCode: ")
                .append(httpStatusCode)
                .append("\n")
                .append("Error: ")
                .append(code)
                .append("\n")
                .append("Msg: ")
                .append(message).toString();
    }



}
