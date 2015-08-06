package com.appmojo.sdk.errors;

import com.appmojo.sdk.AMJsonParser;
import com.appmojo.sdk.utils.AMLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nutron on 7/20/15 AD.
 */
public class AMSubError implements AMJsonParser<AMSubError> {

    private int code;
    private String field;
    private String message;

    public AMSubError() {
        code = AMErrorConstant.DEFAULT_ERROR_CODE;
    }

    public int getCode() {
        return code;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public AMSubError parse(String jsonString) {
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
    public AMSubError parse(JSONObject subErrObj) {
        if(subErrObj == null) {
            return null;
        }
        try {
            if(subErrObj.has(AMErrorConstant.KEY_CODE))
                this.setCode(subErrObj.getInt(AMErrorConstant.KEY_CODE));
            if(subErrObj.has(AMErrorConstant.KEY_FIELD))
                this.setField(subErrObj.getString(AMErrorConstant.KEY_FIELD));
            if(subErrObj.has(AMErrorConstant.KEY_MESSAGE))
                this.setMessage(subErrObj.getString(AMErrorConstant.KEY_MESSAGE));

        } catch (JSONException e) {
            AMLog.e("Parsing error message field.", e);
        }
        return this;
    }
}
