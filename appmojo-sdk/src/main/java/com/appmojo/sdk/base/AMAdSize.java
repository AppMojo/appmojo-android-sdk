package com.appmojo.sdk.base;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.DisplayMetrics;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AMAdSize {
    @IntDef({BANNER, LARGE_BANNER, FULL_BANNER, LEADER_BOARD, MEDIUM_RECTANGLE, WIDE_SKYSCRAPER, SMART_BANNER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Size {}
    public static final int BANNER = 0;
    public static final int LARGE_BANNER = 1;
    public static final int FULL_BANNER = 2;
    public static final int LEADER_BOARD = 3;
    public static final int MEDIUM_RECTANGLE = 4;
    public static final int WIDE_SKYSCRAPER = 5;
    public static final int SMART_BANNER = 6;

    /**
     * get Ad size in size
     * @param context : context
     * @param adSize :  ad size
     * @return array that contain width and height respective
     */
    public static int[] getAdSizePixel(Context context, @Size int adSize) {
        int[] size = new int[2];
        DisplayMetrics displayMetrics =  context.getResources().getDisplayMetrics();
        int h = (int)(50 * displayMetrics.density);
        int w = (int)(320 * displayMetrics.density);

        if(adSize == AMAdSize.BANNER) {
            h = (int)(50 * displayMetrics.density);
            w = (int)(320 * displayMetrics.density);

        } else if(adSize == AMAdSize.LARGE_BANNER) {
            h = (int)(100 * displayMetrics.density);
            w = (int)(320 * displayMetrics.density);

        } else if(adSize == AMAdSize.FULL_BANNER) {
            h = (int)(60 * displayMetrics.density);
            w = (int)(468 * displayMetrics.density);

        } else if(adSize == AMAdSize.LEADER_BOARD) {
            h = (int)(90 * displayMetrics.density);
            w = (int)(728 * displayMetrics.density);

        } else if(adSize == AMAdSize.MEDIUM_RECTANGLE) {
            h = (int)(250 * displayMetrics.density);
            w = (int)(300 * displayMetrics.density);

        } else if(adSize == AMAdSize.WIDE_SKYSCRAPER) {
            h = (int)(600 * displayMetrics.density);
            w = (int)(160 * displayMetrics.density);

        } else if(adSize == AMAdSize.SMART_BANNER) {
            h = (int)(50 * displayMetrics.density);
            w = (int)(320 * displayMetrics.density);
        }

        size[0] = w;
        size[1] = h;
        return size;
    }

}
