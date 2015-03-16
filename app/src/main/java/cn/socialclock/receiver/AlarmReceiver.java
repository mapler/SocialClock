package cn.socialclock.receiver;

import java.util.Calendar;

import cn.socialclock.manager.SocialClockManager;
import cn.socialclock.model.ClockSettings;
import cn.socialclock.ui.AlarmPopActivity;
import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.DatetimeFormatter;
import cn.socialclock.utils.SocialClockLogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author mapler
 *  Alarm Receiver
 *  if today is alarm weekday or alarm is a snooze type
 *  then do alarm
 *  else create next alarm
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SocialClockLogger.log("AlarmReceiver: onReceive start");

        ClockSettings clockSettings = new ClockSettings(context);

        int alarmType = intent.getIntExtra(ConstantData.BundleArgsName.ALARM_TYPE,
                ConstantData.AlarmType.ALARM_NORMAL);
        String alarmEventId = intent.getStringExtra(ConstantData.BundleArgsName.ALARM_EVENT_ID);

        Calendar now = Calendar.getInstance();

        int weekdayId = now.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
        SocialClockLogger.log("AlarmReceiver: todayOfWeek = " + weekdayId);

        if ((alarmType == ConstantData.AlarmType.ALARM_SNOOZE)
                || (clockSettings.isWeekdayEnable(weekdayId))) {
            /* if today is alarm weekday or alarm is a snooze type then do alarm */
            SocialClockLogger.log("AlarmReceiver: alarmed at " + DatetimeFormatter.calendarToString(now));

            /* start alarm popup activity */
            Intent popupIntent = new Intent(context, AlarmPopActivity.class);
            popupIntent.putExtra(ConstantData.BundleArgsName.ALARM_TYPE, alarmType);
            popupIntent.putExtra(ConstantData.BundleArgsName.ALARM_EVENT_ID, alarmEventId);
            popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(popupIntent);
        } else {
            /* else start next alarm */
            SocialClockLogger.log("AlarmReceiver: silence day, " + now.toString());
            SocialClockManager socialClockManager = new SocialClockManager(context);
            // cancel current Alarm Event
            socialClockManager.cancelAlarm(alarmEventId);
            // create next Alarm
            socialClockManager.createAlarm();
        }
    }
}
