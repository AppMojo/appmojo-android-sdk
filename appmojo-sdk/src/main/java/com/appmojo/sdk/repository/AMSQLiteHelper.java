package com.appmojo.sdk.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appmojo.sdk.utils.AMLog;

class AMSQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "appmojo.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_ACTIVITIES = "activities";
    public static final String TABLE_SESSIONS = "sessions";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DEVICE_ID = "device_id";
    public static final String COLUMN_EXPERIMENT_ID = "experiment_id";
    public static final String COLUMN_VARIANT_ID = "variant_id";
    public static final String COLUMN_REVISION_NUMBER = "revision_number";
    public static final String COLUMN_SESSION_ID = "session_id";
    public static final String COLUMN_PLACEMENT_ID = "placement_id";
    public static final String COLUMN_AD_UNIT_ID = "ad_unit_id";
    public static final String COLUMN_ACT_DATE = "act_date";
    public static final String COLUMN_HOUR= "hour";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_ACT_COUNT = "act_count";
    public static final String COLUMN_TRANSACTION_ID = "transaction_id";

    public static final String COLUMN_SESSION_START_TIME = "start_time";
    public static final String COLUMN_SESSION_EXPIRY_TIME = "expiry_time";
    public static final String COLUMN_SESSION_DURATION = "duration";


    public AMSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateTableActivitiesCmd());
        db.execSQL(getCreateTableSessionCmd());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        AMLog.w("Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITIES);
        onCreate(db);
    }

    public String getCreateTableActivitiesCmd(){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(TABLE_ACTIVITIES).append(" ");
        builder.append("(");
        builder.append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        builder.append(COLUMN_DEVICE_ID).append(" TEXT NOT NULL, ");
        builder.append(COLUMN_EXPERIMENT_ID).append(" TEXT NOT NULL, ");
        builder.append(COLUMN_VARIANT_ID).append(" TEXT NOT NULL, ");
        builder.append(COLUMN_REVISION_NUMBER).append(" INTEGER DEFAULT -1, ");
        builder.append(COLUMN_SESSION_ID).append(" TEXT NOT NULL, ");
        builder.append(COLUMN_PLACEMENT_ID).append(" TEXT NOT NULL, ");
        builder.append(COLUMN_AD_UNIT_ID).append(" TEXT NOT NULL, ");
        builder.append(COLUMN_ACT_DATE).append(" TEXT NOT NULL, ");
        builder.append(COLUMN_HOUR).append(" INTEGER, ");
        builder.append(COLUMN_TYPE).append(" INTEGER, ");
        builder.append(COLUMN_ACT_COUNT).append(" INTEGER DEFAULT 0, ");
        builder.append(COLUMN_TRANSACTION_ID).append(" TEXT NOT NULL");
        builder.append(");");
        return builder.toString();
    }

    public String getCreateTableSessionCmd(){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ").append(TABLE_SESSIONS).append(" ");
        builder.append("(");
        builder.append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        builder.append(COLUMN_DEVICE_ID).append(" TEXT NOT NULL, ");
        builder.append(COLUMN_SESSION_ID).append(" TEXT NOT NULL, ");
        builder.append(COLUMN_EXPERIMENT_ID).append(" TEXT NOT NULL, ");
        builder.append(COLUMN_VARIANT_ID).append(" TEXT NOT NULL, ");
        builder.append(COLUMN_REVISION_NUMBER).append(" INTEGER DEFAULT 0, ");
        builder.append(COLUMN_SESSION_START_TIME).append(" INTEGER DEFAULT 0, ");
        builder.append(COLUMN_SESSION_EXPIRY_TIME).append(" INTEGER DEFAULT 0, ");
        builder.append(COLUMN_SESSION_DURATION).append(" INTEGER DEFAULT 0");
        builder.append(");");
        return builder.toString();
    }
}
