package com.appmojo.sdk;

import org.json.JSONObject;


public interface AMJsonParser<T> {

    T parse(String jsonStr);
    T parse(JSONObject jsonObject);

}
