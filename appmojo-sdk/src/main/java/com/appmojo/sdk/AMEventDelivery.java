package com.appmojo.sdk;

import android.content.Context;

import com.appmojo.sdk.connections.AMConnectionHelper;
import com.appmojo.sdk.connections.AMConnectionListener;
import com.appmojo.sdk.errors.AMError;
import com.appmojo.sdk.events.AMActivityEvent;
import com.appmojo.sdk.events.AMEvent;
import com.appmojo.sdk.events.AMSessionEvent;
import com.appmojo.sdk.repository.AMEventRepository;
import com.appmojo.sdk.repository.criterias.AMActivityCriteria;
import com.appmojo.sdk.repository.criterias.AMCriteria;
import com.appmojo.sdk.utils.AMConnectionUtils;
import com.appmojo.sdk.utils.AMLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


class AMEventDelivery {
    private Context mContext;
    private AMConnectionHelper mConnectionHelper;
    private boolean isStarted;
    private int index;
    private AMEventRepository mEventRepository;
    private String mAppId;

    public AMEventDelivery(Context context) {
        mContext = context;
        mConnectionHelper = new AMConnectionHelper();
        mAppId = AMAppEngine.getInstance().getAppId();
    }


    public void setEventRepository(AMEventRepository repository) {
        mEventRepository = repository;
    }


    public synchronized void startDeliverEvent() {
        if(!isStarted) {
            index = -1;
            isStarted = true;
            deliverNextEvent();
        }
    }


    private int getNextType() {
        int type = -1;
        index++;
        if(index < AMEvent.getTypes().length) {
            type = AMEvent.getTypes()[index];
        }
        return type;
    }


    private void deliverNextEvent() {
        //Is internet available?
        if(AMConnectionUtils.isOnline()) {
            int type = getNextType();
            if (type == AMEvent.SESSION) {
                deliverSessionEvent();
            } else if (type == AMEvent.IMPRESSION) {
                deliverClickEvent();
            } else if (type == AMEvent.CLICK) {
                deliverImpressEvent();
            } else { //no deliver event
                index = -1;
                isStarted = false;
            }
        } else {
            AMLog.w("Delivery has been stop, no internet connection.");
            index = -1;
            isStarted = false;
        }
    }


    private void deliverSessionEvent() {
        List<AMEvent> events = mEventRepository.get(AMEvent.SESSION);
        if(events != null && !events.isEmpty()) {
            AMSessionEvent ssEvent;
            long currentTime = System.currentTimeMillis();

            for(int i = events.size()-1 ; i >=0 ; i--) {
                ssEvent = (AMSessionEvent) events.get(i);
                if(ssEvent.getStartTime() <= currentTime && ssEvent.getEndTime() >= currentTime) { //[start, end]
                    events.remove(i); //remove active session
                }
            }

            String body = createSessionBody(events);
            sendEvent(AMEvent.SESSION, AMBaseConfiguration.getUrlSession(mAppId), body);
        } else {
            deliverNextEvent();
        }
    }


    private void deliverClickEvent() {
        List<AMEvent> events = mEventRepository.get(AMEvent.CLICK);
        if(events != null && !events.isEmpty()) {
            //filter only in-active event
            events = filterInactiveActivity(events);
            //create body
            String body = createActivityBody(events);
            //deliver event
            sendEvent(AMEvent.CLICK, AMBaseConfiguration.getUrlActivities(mAppId), body);
        } else {
            deliverNextEvent();
        }
    }


    private void deliverImpressEvent() {
        List<AMEvent> events = mEventRepository.get(AMEvent.IMPRESSION);
        if(events != null && !events.isEmpty()) {
            //filter only in-active event
            events = filterInactiveActivity(events);
            //create body
            String body = createActivityBody(events);
            //deliver event
            sendEvent(AMEvent.IMPRESSION, AMBaseConfiguration.getUrlActivities(mAppId), body);
        } else {
            deliverNextEvent();
        }
    }


    private void sendEvent(@AMEvent.Type final int type, String url, String body) {
        Map<String, String> headers = getHeaderData();
        mConnectionHelper.put(url, headers, body, new AMConnectionListener() {
            @Override
            public void onConnectionSuccess(AMConnectionResponse response) {
                onDeliverSuccess(type, response);
            }

            @Override
            public void onConnectionFail(AMError error) {
                onDeliverFailed(error);
            }
        });
    }

    private void onDeliverSuccess(@AMEvent.Type int type, AMConnectionResponse response) {
        try {
            AMLog.i(response.toString());
            JSONArray jsonArray = new JSONArray(response.getResponse());
            AMCriteria criteria = null;
            for(int i = 0 ; i < jsonArray.length() ; i++) {
                String value = jsonArray.getString(i);
                if(type == AMEvent.SESSION) {
                    criteria = new AMCriteria();
                    criteria.setSessionId(value);
                }

                if(type == AMEvent.IMPRESSION || type == AMEvent.CLICK) {
                    criteria = new AMActivityCriteria(type);
                    ((AMActivityCriteria)criteria).setTransactionId(value);
                }

                if(criteria != null) {
                    mEventRepository.delete(type, criteria);
                }
            }
        } catch (JSONException e) {
            AMLog.w("Error while paring response after delivery event.", e);
        } finally {
            //deliver next event type
            deliverNextEvent();
        }
    }

    private void onDeliverFailed(AMError error) {
        try {
            if(error != null) {
                AMLog.w("Error code: " + error.getCode() + "Message: " + error.getMessage());
            } else {
                AMLog.w("Error while paring session response after updated.");
            }
        } finally {
            //deliver next event type
            deliverNextEvent();
        }
    }


    private Map<String, String> getHeaderData() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", AMTokenHelper.getToken(mContext));
        return headers;
    }


    private String createSessionBody(List<AMEvent> events) {
        JSONArray jsonArray = new JSONArray();
        if(events != null) {
            JSONObject jObj;
            AMSessionEvent ssEvent;
            for (AMEvent event : events) {
                ssEvent = (AMSessionEvent) event;
                jObj = ssEvent.getJsonObject();
                if (jObj != null) {
                    jsonArray.put(jObj);
                }
            }
        }
        return jsonArray.toString();
     }


    private String createActivityBody(List<AMEvent> events) {
        JSONArray jsonArray = new JSONArray();
        if(events != null) {
            JSONObject jObj;
            for (AMEvent event : events) {
                jObj = event.getJsonObject();
                if (jObj != null) {
                    jsonArray.put(jObj);
                }
            }
        }
        return jsonArray.toString();
    }


    private List<AMEvent> filterInactiveActivity(List<AMEvent> events) {
        try {
            if(events != null && !events.isEmpty()) {
                long currentTime = System.currentTimeMillis();
                TimeZone timeZone = TimeZone.getTimeZone("UTC");
                Calendar calendar = Calendar.getInstance(timeZone);
                calendar.setTime(new Date(currentTime));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                simpleDateFormat.setTimeZone(timeZone);

                int activeHour = calendar.get(Calendar.HOUR_OF_DAY);
                String activeDate = simpleDateFormat.format(calendar.getTime());
                String sessionId = AMAppEngine.getInstance().getCurrentSessionId();

                AMActivityEvent actEvent;
                for (int i = events.size() - 1; i >= 0; i--) {
                    actEvent = (AMActivityEvent) events.get(i);
                    if (sessionId != null && sessionId.equals(actEvent.getSessionId())
                            && actEvent.getDate().equals(activeDate) && actEvent.getAtHour() == activeHour) {
                        events.remove(i); //remove active session
                    }
                }
            }
        } catch (Exception e) {
            AMLog.w("Filter event before delivery failed.", e);
        }
        return events;
    }
}
