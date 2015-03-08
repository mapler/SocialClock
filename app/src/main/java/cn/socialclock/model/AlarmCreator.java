package cn.socialclock.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.socialclock.receiver.AlarmReceiver;
import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.SocialClockLogger;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * @author mapler
 * Alarm Creator
 *  1. update normal clock
 *  2. update snooze clock
 *  3. cancel clock
 */
public class AlarmCreator {

    private ClockSettings clockSettings;
    private Context context;
    private Intent alarmIntent;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;

    public AlarmCreator(Context context) {
        /** Init a AlarmCreator with a context */
        this.context = context;
        this.clockSettings = new ClockSettings(context);
        this.alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        this.alarmIntent = new Intent(context, AlarmReceiver.class);
    }

    public void updateSnoozeAlarm(long delayTime) {
        // update(or create) a snooze alarm
        alarmIntent.putExtra(ConstantData.BundleArgs.ALARM_TYPE,
                ConstantData.AlarmType.ALARM_SNOOZE);
        pendingIntent = PendingIntent.getBroadcast(context, 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, delayTime, pendingIntent);
    }

    public void createNormalAlarm() {
        // get settings
        int hour = clockSettings.getHour();
        int minute = clockSettings.getMinute();

        // make a time obj
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // get alarm time stamp
        long alarmTimeStamp = calendar.getTimeInMillis();
        if (alarmTimeStamp <= System.currentTimeMillis()) {
            // plus 1 day if alarm time has past 
            alarmTimeStamp += ConstantData.ConstantTime.MILLIS_OF_DAY;
        }

        // update(or create) a normal alarm
        alarmIntent.putExtra(ConstantData.BundleArgs.ALARM_TYPE,
                ConstantData.AlarmType.ALARM_NORMAL);
        pendingIntent = PendingIntent.getBroadcast(context, 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeStamp, pendingIntent);

        // write log
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SocialClockLogger.log("AlarmCreator: clockTime is set at "
                + (dateFormat.format(new Date(alarmTimeStamp))));
    }

    public void cancelAlarm(){
        // cancel a alarm intent
        pendingIntent = PendingIntent.getBroadcast(context, 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}
