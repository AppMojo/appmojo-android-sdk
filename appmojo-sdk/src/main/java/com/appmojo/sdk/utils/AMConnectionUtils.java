package com.appmojo.sdk.utils;

import java.io.IOException;

/**
 * Created by nutron on 8/21/15 AD.
 */
public class AMConnectionUtils {

    private AMConnectionUtils() {
    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return exitValue == 0;
        } catch (IOException e) {
            AMLog.w("Failed to check the internet connection.", e);
        } catch (InterruptedException e) {
            AMLog.w("Failed to check the internet connection.", e);
        }

        return false;
    }
}
