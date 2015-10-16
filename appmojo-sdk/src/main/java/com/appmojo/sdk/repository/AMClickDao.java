package com.appmojo.sdk.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.appmojo.sdk.events.AMClickEvent;
import com.appmojo.sdk.events.AMEvent;
import com.appmojo.sdk.events.AMEventType;
import com.appmojo.sdk.repository.criterias.AMCriteria;
import com.appmojo.sdk.utils.AMLog;

import java.util.List;


class AMClickDao extends AMDao {


    public AMClickDao(Context context) {
        super(context);
    }

    @Override
    public List<AMEvent> getItem() {
        return query(AMSQLiteHelper.TABLE_ACTIVITIES,
                null, String.format("%s=%s", AMSQLiteHelper.COLUMN_TYPE,  AMEventType.CLICK.getValue()),
                null, null, null, null, null);
    }


    @Override
    public List<AMEvent> getItem(AMCriteria criteria) {
        String selection = getWhereClause(criteria);
        return query(AMSQLiteHelper.TABLE_ACTIVITIES, null, selection, null, null, null, null, null);
    }


    @Override
    public AMEvent getLastItem() {
        List<AMEvent> events = query(AMSQLiteHelper.TABLE_ACTIVITIES,
                null, String.format("%s=%s", AMSQLiteHelper.COLUMN_TYPE,  AMEventType.CLICK.getValue()),
                null,  null,  null,  AMSQLiteHelper.COLUMN_ID + " DESC",  "1");
        if(events != null && !events.isEmpty()) {
            return events.get(0);
        }
        return null;
    }


    @Override
    public long addItem(AMEvent event) {
        long id = -1;
        if (event != null && event instanceof AMClickEvent) {
            AMClickEvent clkEvent = (AMClickEvent) event;
            ContentValues values = new ContentValues();
            values.put(AMSQLiteHelper.COLUMN_DEVICE_ID, clkEvent.getDeviceId());
            values.put(AMSQLiteHelper.COLUMN_VARIANT_ID, clkEvent.getVariantId());
            values.put(AMSQLiteHelper.COLUMN_EXPERIMENT_ID, clkEvent.getExperimentId());
            values.put(AMSQLiteHelper.COLUMN_REVISION_NUMBER, clkEvent.getRevisionNumber());
            values.put(AMSQLiteHelper.COLUMN_SESSION_ID, clkEvent.getSessionId());
            values.put(AMSQLiteHelper.COLUMN_PLACEMENT_ID, clkEvent.getPlacementId());
            values.put(AMSQLiteHelper.COLUMN_AD_UNIT_ID, clkEvent.getAdUnitId());
            values.put(AMSQLiteHelper.COLUMN_ACT_DATE, clkEvent.getDate());
            values.put(AMSQLiteHelper.COLUMN_HOUR, clkEvent.getAtHour());
            values.put(AMSQLiteHelper.COLUMN_TYPE, clkEvent.getType().getValue());
            values.put(AMSQLiteHelper.COLUMN_ACT_COUNT, clkEvent.getCount());
            values.put(AMSQLiteHelper.COLUMN_TRANSACTION_ID, clkEvent.getTransactionId());

            id = insert(AMSQLiteHelper.TABLE_ACTIVITIES, values);
        }
        return id;
    }


    @Override
    public int updateItem(AMEvent event) {
        int number = 0;
        if (event != null && event instanceof AMClickEvent) {
            AMClickEvent clkEvent = (AMClickEvent) event;
            ContentValues values = new ContentValues();
            values.put(AMSQLiteHelper.COLUMN_ID, clkEvent.getId());
            values.put(AMSQLiteHelper.COLUMN_DEVICE_ID, clkEvent.getDeviceId());
            values.put(AMSQLiteHelper.COLUMN_EXPERIMENT_ID, clkEvent.getExperimentId());
            values.put(AMSQLiteHelper.COLUMN_VARIANT_ID, clkEvent.getVariantId());
            values.put(AMSQLiteHelper.COLUMN_REVISION_NUMBER, clkEvent.getRevisionNumber());
            values.put(AMSQLiteHelper.COLUMN_SESSION_ID, clkEvent.getSessionId());
            values.put(AMSQLiteHelper.COLUMN_PLACEMENT_ID, clkEvent.getPlacementId());
            values.put(AMSQLiteHelper.COLUMN_AD_UNIT_ID, clkEvent.getAdUnitId());
            values.put(AMSQLiteHelper.COLUMN_ACT_DATE, clkEvent.getDate());
            values.put(AMSQLiteHelper.COLUMN_HOUR, clkEvent.getAtHour());
            values.put(AMSQLiteHelper.COLUMN_TYPE, clkEvent.getType().getValue());
            values.put(AMSQLiteHelper.COLUMN_ACT_COUNT, clkEvent.getCount());
            values.put(AMSQLiteHelper.COLUMN_TRANSACTION_ID, clkEvent.getTransactionId());

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
        AMClickEvent clkEvent = new AMClickEvent();
        try {
            clkEvent.setId(cursor.getInt(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_ID)));
            clkEvent.setDeviceId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_DEVICE_ID)));
            clkEvent.setExperimentId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_EXPERIMENT_ID)));
            clkEvent.setVariantId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_VARIANT_ID)));
            clkEvent.setRevisionNumber(cursor.getInt(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_REVISION_NUMBER)));
            clkEvent.setSessionId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_SESSION_ID)));
            clkEvent.setPlacementId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_PLACEMENT_ID)));
            clkEvent.setAdUnitId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_AD_UNIT_ID)));
            clkEvent.setDate(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_ACT_DATE)));
            clkEvent.setAtHour(cursor.getInt(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_HOUR)));
            clkEvent.setCount(cursor.getInt(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_ACT_COUNT)));
            clkEvent.setTransactionId(cursor.getString(cursor.getColumnIndex(AMSQLiteHelper.COLUMN_TRANSACTION_ID)));
        } catch (Exception e) {
            AMLog.w("Failed to read store data.", e);
            return null;
        }
        return clkEvent;
    }
}
