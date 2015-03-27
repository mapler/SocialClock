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

    public static class AdapterKey {
        public static final String ALARM_EVENT_USER_NAME_KEY = "userName";
        public static final String ALARM_EVENT_START_AT_KEY = "startAt";
        public static final String ALARM_EVENT_END_AT_KEY = "endAt";
        public static final String ALARM_EVENT_SNOOZE_TIMES_KEY = "snoozeTimes";
    }

    public static class UserName {
        public static final String ANONYMOUS_USER = "ANONYMOUS";
    }
}
