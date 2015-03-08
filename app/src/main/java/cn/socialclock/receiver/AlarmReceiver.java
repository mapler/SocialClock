package cn.socialclock.receiver;

import java.util.Calendar;

import cn.socialclock.model.AlarmCreator;
import cn.socialclock.model.ClockSettings;
import cn.socialclock.ui.AlarmPopActivity;
import cn.socialclock.utils.ConstantData;
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
        /** Called when receiving an Intent from alarm */
        SocialClockLogger.log("AlarmReceiver: onReceive start");

        ClockSettings clockSettings = new ClockSettings(context);

        int alarmType = intent.getIntExtra(ConstantData.BundleArgs.ALARM_TYPE,
                ConstantData.AlarmType.ALARM_NORMAL);

        Calendar now = Calendar.getInstance();

        int weekdayId = now.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
        SocialClockLogger.log("AlarmReceiver: todayOfWeek = " + weekdayId);

        if ((alarmType == ConstantData.AlarmType.ALARM_SNOOZE)
                || (clockSettings.isWeekdayEnable(weekdayId))) {
            /* if today is alarm weekday or alarm is a snooze type then do alarm */
            SocialClockLogger.log("AlarmReceiver: alarmed at " + now.toString());

            Intent popupIntent = new Intent(context, AlarmPopActivity.class);
            popupIntent.putExtra(ConstantData.BundleArgs.ALARM_TYPE, alarmType);
            popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(popupIntent);
        } else {
            /* else start next alarm */
            SocialClockLogger.log("AlarmReceiver: silence day, " + now.toString());
            AlarmCreator alarmCreator = new AlarmCreator(context);
            alarmCreator.createNormalAlarm();
        }
    }
}
