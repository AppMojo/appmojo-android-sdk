package com.appmojo.sdk.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by nutron on 10/30/15 AD.
 */
public class TimeUtils {

    private TimeUtils(){
    }

    public static int getUTCCurrentHour() {
        Calendar calendar = getUTCCalendar();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static String getUTCCurrentDate() {
        Calendar calendar = getUTCCalendar();
        SimpleDateFormat dateFormat = getUTCDateFormat();
        return dateFormat.format(calendar.getTime());
    }

    public static Calendar getUTCCalendar() {
        long currentTime = System.currentTimeMillis();
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(new Date(currentTime));
        return calendar;
    }


    public static SimpleDateFormat getUTCDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat;
    }
}
