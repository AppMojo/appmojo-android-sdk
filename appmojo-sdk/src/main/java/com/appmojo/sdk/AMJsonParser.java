package com.appmojo.sdk;

import org.json.JSONObject;

/**
 * Created by nutron on 7/31/15 AD.
 */
public interface AMJsonParser<T> {

    T parse(String jsonStr);
    T parse(JSONObject jsonObject);

}
