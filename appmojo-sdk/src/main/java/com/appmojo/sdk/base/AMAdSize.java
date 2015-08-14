package com.appmojo.sdk.base;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.SparseArray;

public enum AMAdSize {

    BANNER(0),
    LARGE_BANNER(1),
    FULL_BANNER(2),
    LEADER_BOARD(3),
    MEDIUM_RECTANGLE(4),
    WIDE_SKYSCRAPER(5),
    SMART_BANNER(6);

    int value = -1;
    private static final SparseArray<AMAdSize> sValues = new SparseArray<AMAdSize>();

    static {
        for (AMAdSize type : AMAdSize.values()) {
            sValues.put(type.value, type);
        }
    }

    AMAdSize(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static AMAdSize forValue(int value) {
        return sValues.get(value);
    }

    /**
     * get Ad size in size
     * @param context : context
     * @param adSize :  ad size
     * @return array that contain width and height respective
     */
    public static int[] getAdSizePixel(Context context, AMAdSize adSize) {
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
