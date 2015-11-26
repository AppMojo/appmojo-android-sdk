package com.appmojo.sdk;

import android.content.Context;

import com.appmojo.sdk.errors.AMError;
import com.appmojo.sdk.events.AMActivityEvent;
import com.appmojo.sdk.events.AMEvent;
import com.appmojo.sdk.events.AMSessionEvent;
import com.appmojo.sdk.repository.AMCriteria;
import com.appmojo.sdk.repository.AMEventRepository;
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
    private int mCurrentDeliveringEventType;

    public AMEventDelivery(Context context) {
        mContext = context;
        mConnectionHelper = new AMConnectionHelper(mContext);
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

    private void deliverNextEvent(){
        mCurrentDeliveringEventType = getNextType();
        deliver(mCurrentDeliveringEventType);
    }


    private int getNextType() {
        int type = -1;
        index++;
        if(index < AMEvent.getTypes().length) {
            type = AMEvent.getTypes()[index];
        }
        return type;
    }


    private void deliver(int type) {
        if (type == AMEvent.SESSION) {
            AMLog.i("delete session event...");
            deliverSessionEvent();
        } else if (type == AMEvent.IMPRESSION) {
            AMLog.i("delete impression event...");
            deliverClickEvent();
        } else if (type == AMEvent.CLICK) {
            AMLog.i("delete click event...");
            deliverImpressEvent();
        } else { //no deliver event
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
                } else {
                    if(ssEvent.getExperimentId() == null || ssEvent.getExperimentId().length() < 1) { //empty experiment ID
                        events.remove(i); //remove from delivery chuck
                        mEventRepository.delete(AMEvent.SESSION, ssEvent.getId()); //delete from data base
                    }
                }
            }

            String body = createSessionBody(events);
            if(body != null) {
                sendEvent(AMEvent.SESSION, AMBaseConfiguration.getUrlSession(mAppId), body);
            } else {
                deliverNextEvent();
            }

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
        AMConnectionData data = new AMConnectionData();
        data.setUrl(url);
        data.setMethod(AMConnectionData.PUT);
        data.setBody(body);
        data.setHeader(getHeaderData());

        mConnectionHelper = new AMConnectionHelper(mContext);
        mConnectionHelper.request(data, new AMConnectionHelper.AMConnectionListener() {
            @Override
            public void onConnectSuccess(AMConnectionResponse response) {
                onDeliverSuccess(type, response);
            }

            @Override
            public void onConnectFailed(AMError error) {
                onDeliverFailed(type, error);
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
                    criteria = new AMCriteria();
                    criteria.setActivityType(type);
                    criteria.setTransactionId(value);
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

    private void onDeliverFailed(@AMEvent.Type int type, AMError error) {
        try {
            if(type == AMEvent.SESSION) {
                AMLog.w("Error while deliver 'session' event to server.");
            }

            if(type == AMEvent.CLICK) {
                AMLog.w("Error while deliver 'click' event to server.");
            }

            if(type == AMEvent.IMPRESSION) {
                AMLog.w("Error while deliver 'impression' event to server.");
            }

            if(error != null) {
                AMLog.w("Error code: " + error.getCode() + " Message: " + error.getMessage());
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
        String body = null;
        if(events != null && !events.isEmpty()) {
            JSONArray jsonArray = new JSONArray();

            JSONObject jObj;
            AMSessionEvent ssEvent;
            for (AMEvent event : events) {
                ssEvent = (AMSessionEvent) event;
                jObj = ssEvent.getJsonObject();
                if (jObj != null) {
                    jsonArray.put(jObj);
                }
            }
            body = jsonArray.toString();
        }
        return body;
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
