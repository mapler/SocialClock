package cn.socialclock.manager;

import java.util.Calendar;

import cn.socialclock.model.AlarmEvent;
import cn.socialclock.utils.DatetimeFormatter;

/**
 * Created by mapler
 * Sns Manager
 * todo
 */
public class SnsManager {

    protected static String buildSnsMessage(AlarmEvent alarmEvent) {

        long snoozePeriodInSeconds = (alarmEvent.getEndAt().getTimeInMillis() -
                alarmEvent.getStartAt().getTimeInMillis()) / 1000;

        String snsMessage;
        if (snoozePeriodInSeconds > 60 || alarmEvent.getSnoozeTimes() > 0 ) {
            String snoozePeriodMessage = ""
                    + (snoozePeriodInSeconds / 60 > 0 ? (snoozePeriodInSeconds / 60 + " min") :
                    snoozePeriodInSeconds % 60 + " sec");
            snsMessage = "(test) "
                    + "Alarm at "
                    + alarmEvent.getStartAt().get(Calendar.HOUR)
                    + ":"
                    + String.format("%02d", alarmEvent.getStartAt().get(Calendar.MINUTE))
                    + ". Get up at "
                    + alarmEvent.getEndAt().get(Calendar.HOUR)
                    + ":"
                    + String.format("%02d", alarmEvent.getEndAt().get(Calendar.MINUTE))
                    + ". Snooze "
                    + alarmEvent.getSnoozeTimes() + (alarmEvent.getSnoozeTimes() > 1 ? " times. " : " time. ")
                    + "Late " + snoozePeriodMessage
                    + " (" + DatetimeFormatter.calendarToString(alarmEvent.getEndAt())
                    + ")";
        }
        else {
            snsMessage = "(test) "
                    + "Alarm and get up at "
                    + alarmEvent.getStartAt().get(Calendar.HOUR)
                    + ":"
                    + String.format("%02d", alarmEvent.getStartAt().get(Calendar.MINUTE))
                    + ". (" + DatetimeFormatter.calendarToString(alarmEvent.getEndAt())
                    + ")";
        }

        return snsMessage;
    }
}
