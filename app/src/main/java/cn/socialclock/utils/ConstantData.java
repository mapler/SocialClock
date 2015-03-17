package cn.socialclock.utils;

public class ConstantData {

    public static class ConstantTime {
        public static final int RINGTONE_DURATION = 5 * 60 * 1000; // ringtone_duration
    }

    public static class AlarmType {
        public static final int ALARM_NORMAL = 1; // alarm type: normal alarm
        public static final int ALARM_SNOOZE = 2; // alarm type: snooze alarm
    }

    public static class BundleArgsName {
        public static final String ALARM_TYPE = "alarm_type";
        public static final String ALARM_EVENT_ID = "alarm_event";
    }

    public static class Logger {
        static final String LOG_TAG = "social_clock";
    }
}
