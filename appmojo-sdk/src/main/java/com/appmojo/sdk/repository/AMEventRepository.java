package com.appmojo.sdk.repository;


import android.content.Context;

import com.appmojo.sdk.events.AMEvent;
import com.appmojo.sdk.utils.AMLog;

import java.util.List;

public class AMEventRepository {

    private Context mContext;

    public AMEventRepository(Context context) {
        mContext = context;
    }

    public long insert(AMEvent event) {
        long id = -1;
        AMDao amDao = AMDaoFactory.createDao(mContext, event.getType());
        try {
            if(amDao != null) {
                amDao.open();
                id = amDao.addItem(event);
            }
        } catch (Exception e) {
            AMLog.w("Error while add event.", e);
        } finally {
            if(amDao != null)
                amDao.close();
        }

        return id;
    }


    public List<AMEvent> get(@AMEvent.Type int type, AMCriteria criteria) {
        List<AMEvent> events = null;
        AMDao amDao = AMDaoFactory.createDao(mContext, type);
        try {
            if(amDao != null) {
                amDao.open();
                events = amDao.getItem(criteria);
            }
        } catch (Exception e) {
            AMLog.w("Error while get event.", e);
        } finally {
            if(amDao != null)
                amDao.close();
        }
        return events;
    }


    public List<AMEvent> get(@AMEvent.Type int type) {
        List<AMEvent> events = null;
        AMDao amDao = AMDaoFactory.createDao(mContext, type);
        try {
            if(amDao != null) {
                amDao.open();
                events = amDao.getItem();
            }
        } catch (Exception e) {
            AMLog.w("Error while get event.", e);
        } finally {
            if(amDao != null)
                amDao.close();
        }
        return events;
    }


    public AMEvent getLastItem(@AMEvent.Type int type) {
        AMEvent event = null;
        AMDao amDao = AMDaoFactory.createDao(mContext, type);
        try {
            if(amDao != null) {
                amDao.open();
                event = amDao.getLastItem();
            }
        } catch (Exception e) {
            AMLog.w("Error while get last event.", e);
        } finally {
            if(amDao != null)
                amDao.close();
        }
        return event;
    }


    public int update(AMEvent event) {
        int number = 0;
        AMDao amDao = AMDaoFactory.createDao(mContext, event.getType());
        try {
            if(amDao != null) {
                amDao.open();
                number = amDao.updateItem(event);
            }
        } catch (Exception e) {
            AMLog.w("Error while update event.", e);
        } finally {
            if(amDao != null)
                amDao.close();
        }
        return number;
    }


    public int delete(@AMEvent.Type int type, long id) {
        int number = 0;
        AMDao amDao = AMDaoFactory.createDao(mContext, type);
        try {
            if(amDao != null) {
                amDao.open();
                number = amDao.removeItem(id);
            }
        } catch (Exception e) {
            AMLog.w("Error while remove event.", e);
        } finally {
            if(amDao != null)
                amDao.close();
        }
        return number;
    }


    public int delete(@AMEvent.Type int type, AMCriteria criteria) {
        int number = 0;
        AMDao amDao = AMDaoFactory.createDao(mContext, type);
        try {
            if(amDao != null) {
                amDao.open();
                number = amDao.removeItem(criteria);
            }
        } catch (Exception e) {
            AMLog.w("Error while remove event.", e);
        } finally {
            if(amDao != null)
                amDao.close();
        }
        return number;
    }

}
