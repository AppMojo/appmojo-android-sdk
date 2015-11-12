package com.appmojo.sdk.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.appmojo.sdk.events.AMEvent;
import com.appmojo.sdk.events.AMSessionEvent;
import com.appmojo.sdk.utils.AMLog;

import java.util.List;


class AMSessionDao extends AMDao {

    public AMSessionDao(Context context) {
        super(context);
    }


    @Override
    public List<AMEvent> getItem() {
        return query(AMSQLiteHelper.TABLE_SESSIONS, null, null, null, null, null, null, null);
    }


    @Override
    public List<AMEvent> getItem(AMCriteria criteria) {
        String where = getWhereClause(criteria);
        return query(AMSQLiteHelper.TABLE_SESSIONS, null, where, null, null, null, null, null);
    }


    @Override
    public AMEvent getLastItem() {
        List<AMEvent> events = query(AMSQLiteHelper.TABLE_SESSIONS,
                null, null, null, null, null, AMSQLiteHelper.COLUMN_ID + " DESC", "1");
        if(events != null && !events.isEmpty()) {
            return events.get(0);
        }
        return null;
    }


    @Override
    public long addItem(AMEvent event) {
        long id = -1;
        if (event != null && event instanceof AMSessionEvent) {
            AMSessionEvent ssEvent = (AMSessionEvent) event;
            ContentValues values = new ContentValues();
            values.put(AMSQLiteHelper.COLUMN_DEVICE_ID, ssEvent.getDeviceId());
            values.put(AMSQLiteHelper.COLUMN_SESSION_ID, ssEvent.getSessionId());
            values.put(AMSQLiteHelper.COLUMN_EXPERIMENT_ID, ssEvent.getExperimentId());
            values.put(AMSQLiteHelper.COLUMN_VARIANT_ID, ssEvent.getVariantId());
            values.put(AMSQLiteHelper.COLUMN_REVISION_NUMBER, ssEvent.getRevisionNumber());
            values.put(AMSQLiteHelper.COLUMN_SESSION_START_TIME, ssEvent.getStartTime());
            values.put(AMSQLiteHelper.COLUMN_SESSION_EXPIRY_TIME, ssEvent.getEndTime());
            values.put(AMSQLiteHelper.COLUMN_SESSION_DURATION, ssEvent.getDuration());

            id = insert(AMSQLiteHelper.TABLE_SESSIONS, values);
        }
        return id;
    }


    @Override
    public int updateItem(AMEvent event) {
        int number = 0;
            if (event != null && event instanceof AMSessionEvent) {
                AMSessionEvent ssEvent = (AMSessionEvent) event;
                ContentValues values = new ContentValues();
                values.put(AMSQLiteHelper.COLUMN_ID, ssEvent.getId());
                values.put(AMSQLiteHelper.COLUMN_DEVICE_ID, ssEvent.getDeviceId());
                values.put(AMSQLiteHelper.COLUMN_SESSION_ID, ssEvent.getSessionId());
                values.put(AMSQLiteHelper.COLUMN_EXPERIMENT_ID, ssEvent.getExperimentId());
                values.put(AMSQLiteHelper.COLUMN_VARIANT_ID, ssEvent.getVariantId());
                values.put(AMSQLiteHelper.COLUMN_REVISION_NUMBER, ssEvent.getRevisionNumber());
                values.put(AMSQLiteHelper.COLUMN_SESSION_START_TIME, ssEvent.getStartTime());
                values.put(AMSQLiteHelper.COLUMN_SESSION_EXPIRY_TIME, ssEvent.getEndTime());
                values.put(AMSQLiteHelper.COLUMN_SESSION_DURATION, ssEvent.getDuration());

                number = update(AMSQLiteHelper.TABLE_SESSIONS, values,
                        AMSQLiteHelper.COLUMN_ID + "=" + event.getId(), null);
            }
        return number;
    }


    @Override
    public int removeItem(long id) {
        return delete(AMSQLiteHelper.TABLE_SESSIONS, AMSQLiteHelper.COLUMN_ID + "=" + id, null);
    }


    @Override
    public int removeItem(AMCriteria criteria) {
        String whereClause = getWhereClause(criteria);
        return delete(AMSQLiteHelper.TABLE_SESSIONS, whereClause, null);
    }


    @Override
    public AMSessionEvent cursorToEvent(Cursor cursor) {
        AMSessionEvent sessionEvent = new AMSessionEvent();
        try {
            sessionEvent.setId(cursor.getInt(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_ID)));
            sessionEvent.setSessionId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_SESSION_ID)));
            sessionEvent.setDeviceId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_DEVICE_ID)));
            sessionEvent.setExperimentId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_EXPERIMENT_ID)));
            sessionEvent.setVariantId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_VARIANT_ID)));
            sessionEvent.setRevisionNumber(cursor.getInt(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_REVISION_NUMBER)));
            sessionEvent.setStartTime(cursor.getLong(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_SESSION_START_TIME)));
            sessionEvent.setEndTime(cursor.getLong(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_SESSION_EXPIRY_TIME)));
            sessionEvent.setDuration(cursor.getLong(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_SESSION_DURATION)));
        } catch (Exception e) {
            AMLog.w("Failed to read store data.", e);
           return null;
        }
        return sessionEvent;
    }
}
