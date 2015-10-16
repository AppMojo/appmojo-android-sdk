package com.appmojo.sdk.repository;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.appmojo.sdk.events.AMEvent;
import com.appmojo.sdk.repository.criterias.AMActivityCriteria;
import com.appmojo.sdk.repository.criterias.AMCriteria;
import com.appmojo.sdk.utils.AMLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

abstract class AMDao {

    private static final String CONDITION_TEXT_FORMAT = "%s='%s'";
    private static final String CONDITION_NUMBER_FORMAT = "%s=%d";
    private static final String CONDITION_AND_TEXT_FORMAT = " AND %s='%s'";
    private static final String CONDITION_AND_NUMBER_FORMAT = " AND %s=%d";

    protected SQLiteDatabase mDb;
    protected AMSQLiteHelper mDbHelper;

    public AMDao(Context context) {
        mDbHelper = new AMSQLiteHelper(context);
    }

    public abstract AMEvent getLastItem();
    public abstract List<AMEvent> getItem();
    public abstract List<AMEvent> getItem(AMCriteria criteria);
    public abstract long addItem(AMEvent event);
    public abstract int updateItem(AMEvent event);
    public abstract int removeItem(long id);
    public abstract int removeItem(AMCriteria criteria);
    public abstract AMEvent cursorToEvent(Cursor cursor);


    public void open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
    }

    public void close() {
        if(mDbHelper != null) {
            mDbHelper.close();
        }
    }


    public List<AMEvent> query(String table, String[] column,
                                    String selection, String[] selectionArgs,
                                    String groupBy, String having, String order, String limit) {

        List<AMEvent> events = new ArrayList<>();
        Cursor cursor = null;
        AMEvent event;
        try {
            cursor = mDb.query(table, column, selection, selectionArgs, groupBy, having, order, limit);

            while (cursor.moveToNext()) {
                event = cursorToEvent(cursor);
                if(event != null)
                    events.add(event);
            }

        } catch (CursorIndexOutOfBoundsException e){
            AMLog.e("CursorIndexOutOfBoundsException on query event.", e);

        } catch (NullPointerException e) {
            AMLog.e("NullPointerException on on query event.", e);
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return events;
    }


    public long insert(String table, ContentValues values) {
        return mDb.insert(table, null, values);
    }


    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return mDb.update(table, values, whereClause, whereArgs);
    }


    public int delete(String table, String whereClause, String[] whereArgs) {
        return mDb.delete(table, whereClause, whereArgs);
    }


    public String getWhereClause(AMCriteria criteria) {
        String where = null;
        if(criteria != null) {
            if (criteria.getDeviceId() != null) {
                where = String.format(Locale.getDefault(), CONDITION_TEXT_FORMAT,
                        AMSQLiteHelper.COLUMN_DEVICE_ID, criteria.getDeviceId());
            }


            if (criteria.getSessionId() != null) {
                if(where == null) {
                    where = String.format(Locale.getDefault(), CONDITION_TEXT_FORMAT,
                            AMSQLiteHelper.COLUMN_SESSION_ID, criteria.getSessionId());
                } else {
                    where += String.format(Locale.getDefault(), CONDITION_AND_TEXT_FORMAT,
                            AMSQLiteHelper.COLUMN_SESSION_ID, criteria.getSessionId());
                }
            }


            if (criteria instanceof AMActivityCriteria) {
                String actWhere = getActivityWhereClause((AMActivityCriteria)criteria);
                if(actWhere != null) {
                    if (where == null) {
                        where = actWhere;
                    } else {
                        where += String.format(Locale.getDefault(), " AND %s", actWhere);
                    }
                }
            }
        }
        return where;
    }


    private String getActivityWhereClause(AMActivityCriteria actCriteria) {
        //Event type MUST have.
        String where = String.format(Locale.getDefault(), CONDITION_NUMBER_FORMAT,
                AMSQLiteHelper.COLUMN_TYPE, actCriteria.getActivityType().getValue());

        if (actCriteria.getExperimentId() != null) {
            where += String.format(Locale.getDefault(), CONDITION_AND_TEXT_FORMAT,
                    AMSQLiteHelper.COLUMN_EXPERIMENT_ID, actCriteria.getExperimentId());
        }

        if (actCriteria.getVariantId() != null) {
            where += String.format(Locale.getDefault(), CONDITION_AND_TEXT_FORMAT,
                    AMSQLiteHelper.COLUMN_VARIANT_ID, actCriteria.getVariantId());
        }

        if (actCriteria.getRevisionId() != -1) {
            where += String.format(Locale.getDefault(), CONDITION_AND_NUMBER_FORMAT,
                    AMSQLiteHelper.COLUMN_REVISION_NUMBER, actCriteria.getRevisionId());
        }


        if (actCriteria.getPlacementId() != null) {
            where += String.format(Locale.getDefault(), CONDITION_AND_TEXT_FORMAT,
                    AMSQLiteHelper.COLUMN_PLACEMENT_ID, actCriteria.getPlacementId());
        }

        if (actCriteria.getAdUnitId() != null) {
            where += String.format(Locale.getDefault(), CONDITION_AND_TEXT_FORMAT,
                    AMSQLiteHelper.COLUMN_AD_UNIT_ID, actCriteria.getAdUnitId());
        }

        if (actCriteria.getDate() != null) {
            where += String.format(Locale.getDefault(), CONDITION_AND_TEXT_FORMAT,
                    AMSQLiteHelper.COLUMN_ACT_DATE, actCriteria.getDate());
        }

        if (actCriteria.getHour() != -1) {
            where += String.format(Locale.getDefault(), CONDITION_AND_NUMBER_FORMAT,
                    AMSQLiteHelper.COLUMN_HOUR, actCriteria.getHour());
        }


        if (actCriteria.getTransactionId() != null) {
            where += String.format(Locale.getDefault(), CONDITION_AND_TEXT_FORMAT,
                    AMSQLiteHelper.COLUMN_TRANSACTION_ID, actCriteria.getTransactionId());
        }

        return where;
    }

}
