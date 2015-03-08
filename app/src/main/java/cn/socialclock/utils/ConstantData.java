package cn.socialclock.utils;

public class ConstantData {

    public static class ConstantTime {
        public static final long MILLIS_OF_DAY = 24 * 60 * 60 * 1000; // milliseconds of one day
        public static final int RINGTONES_LONG = 5 * 60 * 1000; // 持续响铃时限
    }

    public static class AlarmType {
        public static final int ALARM_NORMAL = 1; // 响铃类型:正常响铃
        public static final int ALARM_SNOOZE = 2; // 闹钟类型：贪睡闹钟
    }

    public static class Graphy {
        public static final int DISTANCE = 12;
    }

    public static class BundleArgs {
        public static final String ALARM_TYPE = "alarm_type";
    }

    public static class Logger {
        static final String LOG_TAG = "social_clock";
    }
}
