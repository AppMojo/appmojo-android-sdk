package com.appmojo.sdk;


import android.content.Context;

import com.appmojo.sdk.events.AMActivityEvent;
import com.appmojo.sdk.events.AMClickEvent;
import com.appmojo.sdk.events.AMEvent;
import com.appmojo.sdk.events.AMEventType;
import com.appmojo.sdk.events.AMImpressionEvent;
import com.appmojo.sdk.repository.criterias.AMActivityCriteria;
import com.appmojo.sdk.repository.AMEventRepository;
import com.appmojo.sdk.utils.AMLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

class AMActivityTracker {

    private Context mContext;
    private AMSessionManager mSessionManager;
    private AMEventRepository mRepository;
    private AMConfigurationManager mConfigurationManager;

    public AMActivityTracker(Context context) {
        mContext = context;
    }


    public void setSessionManager(AMSessionManager manager) {
        mSessionManager = manager;
    }


    public void setEventRepository(AMEventRepository repository) {
        mRepository = repository;
    }


    public void setConfigurationManager(AMConfigurationManager configurationManager) {
        mConfigurationManager = configurationManager;
    }


    public synchronized void logActivity(AMEventType type, String placementId, String adUnitId) {
        Calendar calendar = getCalendar();
        SimpleDateFormat dateFormat = getDateFormat();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String date = dateFormat.format(calendar.getTime());

        //quest last data
        AMEvent preEvent = getPreviousData(type, placementId, adUnitId, date, hour);

        AMActivityEvent event;
        if(preEvent != null) { //have previous data, update
            event = (AMActivityEvent) preEvent;
            event.setCount(event.getCount() + 1);
            mRepository.update(event);

        } else { //no previous data add
            event = createActivityEventObject(type);
            if(event != null) {
                event.setDeviceId(AMClientProvider.readUuid(mContext));
                event.setExperimentId(mConfigurationManager.getExperimentId());
                event.setVariantId(mConfigurationManager.getVariantId());
                event.setRevisionNumber(mConfigurationManager.getRevisionId());
                event.setSessionId(mSessionManager.getCurrentSessionId());
                event.setPlacementId(placementId);
                event.setAdUnitId(adUnitId);
                event.setDate(date);
                event.setAtHour(hour);
                event.setCount(1);
                event.setTransactionId(UUID.randomUUID().toString());
                mRepository.insert(event);
            } else {
                AMLog.d("Event type not found, " + type);
            }
        }
    }

    private AMActivityEvent createActivityEventObject(AMEventType type) {
        if(type == AMEventType.IMPRESSION) {
            return new AMImpressionEvent();
        }

        if(type == AMEventType.CLICK) {
            return new AMClickEvent();
        }

        return null;
    }


    private Calendar getCalendar() {
        long currentTime = System.currentTimeMillis();
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(new Date(currentTime));
        return calendar;
    }


    private SimpleDateFormat getDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat;
    }


    private AMEvent getPreviousData(AMEventType type, String placementId,
                                String adUnitId, String activeDate, int activeHour) {
        AMEvent event = null;
        AMActivityCriteria criteria = new AMActivityCriteria(type);
        criteria.setDeviceId(AMClientProvider.readUuid(mContext));
        criteria.setSessionId(AMAppEngine.getInstance().getCurrentSessionId());
        criteria.setExperimentId(mConfigurationManager.getExperimentId());
        criteria.setVariantId(mConfigurationManager.getVariantId());
        criteria.setRevisionId(mConfigurationManager.getRevisionId());
        criteria.setPlacementId(placementId);
        criteria.setAdUnitId(adUnitId);
        criteria.setDate(activeDate);
        criteria.setHour(activeHour);

        try {
            List<AMEvent> events = mRepository.get(type, criteria);
            if(events != null && !events.isEmpty()) {
                event = events.get(0);
            }

        } catch (Exception e) {
            AMLog.w("Exception while query data from repository", e);
        }
        return event;
    }
}
