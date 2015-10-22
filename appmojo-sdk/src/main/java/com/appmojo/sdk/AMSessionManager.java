package com.appmojo.sdk;


import android.content.Context;

import com.appmojo.sdk.events.AMEventType;
import com.appmojo.sdk.events.AMSessionEvent;
import com.appmojo.sdk.repository.AMEventRepository;
import com.appmojo.sdk.utils.AMLog;

import java.util.UUID;

class AMSessionManager {
    private static final long SESSION_TIME = 5*60*1000L; //30 minutes

    private Context mContext;
    private long mSessionLifeTime = SESSION_TIME;
    private AMSessionEvent mSession;
    private AMEventRepository mEventRepository;
    private AMEventTriggerListener mEventTriggerListener;

    public AMSessionManager(Context context) {
        mContext = context;
    }


    public void setEventRepository(AMEventRepository repository) {
        mEventRepository = repository;
    }


    public void setEventTriggerListener(AMEventTriggerListener listener) {
        mEventTriggerListener = listener;
    }


    public void setSessionLifeTime(long lifeTime) {
        mSessionLifeTime = lifeTime;
    }


    public long getSessionLifeTime() {
        return mSessionLifeTime;
    }


    public String getCurrentSessionId() {
        if(getCurrentSession() != null) {
            return getCurrentSession().getSessionId();
        } else {
            return null;
        }
    }


    public AMSessionEvent getCurrentSession() {
        return mSession;
    }


    public synchronized void logSession() {
        String experimentId = AMConfigurationHelper.readExperimentId(mContext);
        if(experimentId != null && experimentId.length() > 0) {

            long curTime = System.currentTimeMillis();
            if (mSession == null) {
                mSession = getLastSession();
                if (mSession == null) {
                    mSession = createSession(curTime);
                }
            }

            boolean isTriggerDelivery = false;
            //verify session not expired
            if (mSession.getEndTime() < curTime) { //session expired
                AMLog.w("session has expired.");
                mSession = createSession(curTime);
                isTriggerDelivery = true;
            } else {
                if (mSession.getStartTime() > curTime) { //user change time to the pass
                    AMLog.w("session time invalid.");
                    mSession = createSession(curTime);
                    isTriggerDelivery = true;
                }
            }

            //update session time
            mSession.setEndTime(curTime + getSessionLifeTime());
            mSession.setDuration(curTime - mSession.getStartTime());

            //save session
            saveSession(mSession);
            if (isTriggerDelivery && mEventTriggerListener != null) {
                mEventTriggerListener.onTriggerDelivery();
            }

        } else {
            AMLog.w("No running experiment, session will not be logged...");
            if (mEventTriggerListener != null) {
                mEventTriggerListener.onTriggerDelivery();
            }
        }
    }


    private void saveSession(AMSessionEvent session) {
        if(mEventRepository != null) {
            if (session.getId() == -1) { //new session
                long id = mEventRepository.insert(session);
                session.setId(id);
            } else { // old session
                mEventRepository.update(session);
            }
        }
    }


    private String  createSessionId(){
        return UUID.randomUUID().toString();
    }


    private AMSessionEvent getLastSession() {
        if(mEventRepository != null) {
            return (AMSessionEvent) mEventRepository.getLastItem(AMEventType.SESSION);
        }
        return null;
    }


    private AMSessionEvent createSession(long time) {
        AMSessionEvent session = new AMSessionEvent();
        session.setDeviceId(AMClientProvider.readUuid(mContext));
        session.setSessionId(createSessionId());
        session.setStartTime(time);
        session.setEndTime(time + getSessionLifeTime());
        session.setExperimentId(AMConfigurationHelper.readExperimentId(mContext));
        session.setVariantId(AMConfigurationHelper.readVariantId(mContext));
        session.setRevisionNumber(AMConfigurationHelper.readRevisionNumber(mContext));
        return session;
    }

}
