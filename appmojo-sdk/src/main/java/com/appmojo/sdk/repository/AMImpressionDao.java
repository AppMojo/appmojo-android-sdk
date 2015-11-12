package com.appmojo.sdk.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.appmojo.sdk.events.AMEvent;
import com.appmojo.sdk.events.AMImpressionEvent;
import com.appmojo.sdk.utils.AMLog;

import java.util.List;

class AMImpressionDao extends AMDao {

    public AMImpressionDao(Context context) {
        super(context);
    }

    @Override
    public List<AMEvent> getItem() {
        return query(AMSQLiteHelper.TABLE_ACTIVITIES,
                null, String.format("%s=%s", AMSQLiteHelper.COLUMN_TYPE,  AMEvent.IMPRESSION),
                null, null, null, null, null);
    }


    @Override
    public List<AMEvent> getItem(AMCriteria criteria) {
        String selection = getWhereClause(criteria);
        return query(AMSQLiteHelper.TABLE_ACTIVITIES,
                null, selection, null, null, null, null, null);
    }

    @Override
    public AMEvent getLastItem() {
        List<AMEvent> events = query(AMSQLiteHelper.TABLE_ACTIVITIES,
                null, String.format("%s=%s", AMSQLiteHelper.COLUMN_TYPE,  AMEvent.IMPRESSION),
                null,  null,  null,  AMSQLiteHelper.COLUMN_ID + " DESC",  "1");
        if(events != null && !events.isEmpty()) {
            return events.get(0);
        }
        return null;
    }


    @Override
    public long addItem(AMEvent event) {
        long id = -1;
        if (event != null && event instanceof AMImpressionEvent) {
            AMImpressionEvent impEvent = (AMImpressionEvent) event;
            ContentValues values = new ContentValues();
            values.put(AMSQLiteHelper.COLUMN_DEVICE_ID, impEvent.getDeviceId());
            values.put(AMSQLiteHelper.COLUMN_EXPERIMENT_ID, impEvent.getExperimentId());
            values.put(AMSQLiteHelper.COLUMN_VARIANT_ID, impEvent.getVariantId());
            values.put(AMSQLiteHelper.COLUMN_REVISION_NUMBER, impEvent.getRevisionNumber());
            values.put(AMSQLiteHelper.COLUMN_SESSION_ID, impEvent.getSessionId());
            values.put(AMSQLiteHelper.COLUMN_PLACEMENT_ID, impEvent.getPlacementId());
            values.put(AMSQLiteHelper.COLUMN_AD_UNIT_ID, impEvent.getAdUnitId());
            values.put(AMSQLiteHelper.COLUMN_ACT_DATE, impEvent.getDate());
            values.put(AMSQLiteHelper.COLUMN_HOUR, impEvent.getAtHour());
            values.put(AMSQLiteHelper.COLUMN_TYPE, impEvent.getType());
            values.put(AMSQLiteHelper.COLUMN_ACT_COUNT, impEvent.getCount());
            values.put(AMSQLiteHelper.COLUMN_TRANSACTION_ID, impEvent.getTransactionId());

            id = insert(AMSQLiteHelper.TABLE_ACTIVITIES, values);
        }
        return id;
    }

    @Override
    public int updateItem(AMEvent event) {
        int number = 0;
        if (event != null && event instanceof AMImpressionEvent) {
            AMImpressionEvent impEvent = (AMImpressionEvent) event;
            ContentValues values = new ContentValues();
            values.put(AMSQLiteHelper.COLUMN_ID, impEvent.getId());
            values.put(AMSQLiteHelper.COLUMN_DEVICE_ID, impEvent.getDeviceId());
            values.put(AMSQLiteHelper.COLUMN_EXPERIMENT_ID, impEvent.getExperimentId());
            values.put(AMSQLiteHelper.COLUMN_VARIANT_ID, impEvent.getVariantId());
            values.put(AMSQLiteHelper.COLUMN_REVISION_NUMBER, impEvent.getRevisionNumber());
            values.put(AMSQLiteHelper.COLUMN_SESSION_ID, impEvent.getSessionId());
            values.put(AMSQLiteHelper.COLUMN_PLACEMENT_ID, impEvent.getPlacementId());
            values.put(AMSQLiteHelper.COLUMN_AD_UNIT_ID, impEvent.getAdUnitId());
            values.put(AMSQLiteHelper.COLUMN_ACT_DATE, impEvent.getDate());
            values.put(AMSQLiteHelper.COLUMN_HOUR, impEvent.getAtHour());
            values.put(AMSQLiteHelper.COLUMN_TYPE, impEvent.getType());
            values.put(AMSQLiteHelper.COLUMN_ACT_COUNT, impEvent.getCount());
            values.put(AMSQLiteHelper.COLUMN_TRANSACTION_ID, impEvent.getTransactionId());

            number = update(AMSQLiteHelper.TABLE_ACTIVITIES, values,
                    AMSQLiteHelper.COLUMN_ID + "=" + event.getId(), null);
        }
        return number;
    }


    @Override
    public int removeItem(long id) {
        return delete(AMSQLiteHelper.TABLE_ACTIVITIES, AMSQLiteHelper.COLUMN_ID + "=" + id, null);
    }


    @Override
    public int removeItem(AMCriteria criteria) {
        String whereClause = getWhereClause(criteria);
        return delete(AMSQLiteHelper.TABLE_ACTIVITIES, whereClause, null);
    }


    @Override
    public AMEvent cursorToEvent(Cursor cursor) {
        AMImpressionEvent impEvent = new AMImpressionEvent();
        try {
            impEvent.setId(cursor.getInt(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_ID)));
            impEvent.setDeviceId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_DEVICE_ID)));
            impEvent.setExperimentId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_EXPERIMENT_ID)));
            impEvent.setVariantId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_VARIANT_ID)));
            impEvent.setRevisionNumber(cursor.getInt(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_REVISION_NUMBER)));
            impEvent.setSessionId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_SESSION_ID)));
            impEvent.setPlacementId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_PLACEMENT_ID)));
            impEvent.setAdUnitId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_AD_UNIT_ID)));
            impEvent.setDate(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_ACT_DATE)));
            impEvent.setAtHour(cursor.getInt(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_HOUR)));
            impEvent.setCount(cursor.getInt(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_ACT_COUNT)));
            impEvent.setTransactionId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_TRANSACTION_ID)));
        } catch (Exception e) {
            AMLog.w("Failed to read store data.", e);
            return null;
        }
        return impEvent;
    }
}
