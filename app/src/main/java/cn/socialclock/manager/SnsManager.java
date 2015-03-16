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

        String snoozePeriodMessage = ""
                + (snoozePeriodInSeconds / 60 > 0 ? (snoozePeriodInSeconds / 60 + "分") : "")
                + snoozePeriodInSeconds % 60 + "秒";

        String snsMessage = "(这是测试)"
                + "我本来定的"
                + alarmEvent.getStartAt().get(Calendar.HOUR)
                + "点"
                + String.format("%02d", alarmEvent.getStartAt().get(Calendar.MINUTE))
                + "分的闹钟，结果"
                + alarmEvent.getEndAt().get(Calendar.HOUR)
                + "点"
                + String.format("%02d", alarmEvent.getEndAt().get(Calendar.MINUTE))
                + "分才起来。按掉"
                + alarmEvent.getSnoozeTimes() + "次闹铃，赖床" + snoozePeriodMessage
                + "...我果然是个废物。(" + DatetimeFormatter.calendarToString(alarmEvent.getEndAt())
                + ")";

        return snsMessage;
    }
}
