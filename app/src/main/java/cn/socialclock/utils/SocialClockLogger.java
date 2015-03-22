package cn.socialclock.utils;

import android.util.Log;

/**
 * Created by mapler on 2015/03/06.
 * Handle Log.
 */
public class SocialClockLogger {

    public static void log(String message) {
        /** wrapper the system log with LOG_TAG */
        Log.d(ConstantData.Logger.LOG_TAG, message);
    }

    public static void error(String message) {
        /** wrapper the system error log with LOG_TAG */
        Log.e(ConstantData.Logger.LOG_TAG, message);
    }
}
