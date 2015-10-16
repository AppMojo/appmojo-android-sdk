package com.appmojo.sdk.events;


import org.json.JSONObject;

public abstract class AMEvent {

    protected long id = -1;
    protected AMEventType type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public abstract AMEventType getType();

    public abstract JSONObject getJsonObject();

}
