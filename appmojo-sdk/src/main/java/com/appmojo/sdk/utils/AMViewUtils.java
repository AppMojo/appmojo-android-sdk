package com.appmojo.sdk.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nutron on 6/24/15 AD.
 */
public class AMViewUtils {

    private AMViewUtils(){
    }

    public static void removeFromParent(View view) {
        if (view == null || view.getParent() == null) {
            return;
        }

        if (view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
