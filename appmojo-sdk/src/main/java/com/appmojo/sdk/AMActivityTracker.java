package com.appmojo.sdk;


import android.content.Context;

import com.appmojo.sdk.events.AMActivityEvent;
import com.appmojo.sdk.events.AMClickEvent;
import com.appmojo.sdk.events.AMEvent;
import com.appmojo.sdk.events.AMImpressionEvent;
import com.appmojo.sdk.repository.AMCriteria;
import com.appmojo.sdk.repository.AMEventRepository;
import com.appmojo.sdk.utils.AMLog;
import com.appmojo.sdk.utils.TimeUtils;

import java.util.List;
import java.util.UUID;

class AMActivityTracker {

    private Context mContext;
    private AMEventRepository mRepository;

    public AMActivityTracker(Context context) {
        mContext = context;
    }


    public void setEventRepository(AMEventRepository repository) {
        mRepository = repository;
    }


    public synchronized void logActivity(@AMEvent.Type int type, String placementId, String adUnitId) {
        int hour = TimeUtils.getUTCCurrentHour();
        String date = TimeUtils.getUTCCurrentDate();

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
                event.setExperimentId(AMConfigurationHelper.readExperimentId(mContext));
                event.setVariantId(AMConfigurationHelper.readVariantId(mContext));
                event.setRevisionNumber(AMConfigurationHelper.readRevisionNumber(mContext));
                event.setSessionId(AMAppEngine.getInstance().getCurrentSessionId());
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

    private AMActivityEvent createActivityEventObject(@AMEvent.Type int type) {
        if(type == AMEvent.IMPRESSION) {
            return new AMImpressionEvent();
        }

        if(type == AMEvent.CLICK) {
            return new AMClickEvent();
        }

        return null;
    }


    private AMEvent getPreviousData(@AMEvent.Type int type, String placementId,
                                String adUnitId, String activeDate, int activeHour) {
        AMEvent event = null;
        AMCriteria criteria = new AMCriteria();
        criteria.setActivityType(type);
        criteria.setDeviceId(AMClientProvider.readUuid(mContext));
        criteria.setSessionId(AMAppEngine.getInstance().getCurrentSessionId());
        criteria.setExperimentId(AMConfigurationHelper.readExperimentId(mContext));
        criteria.setVariantId(AMConfigurationHelper.readVariantId(mContext));
        criteria.setRevisionId(AMConfigurationHelper.readRevisionNumber(mContext));
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
