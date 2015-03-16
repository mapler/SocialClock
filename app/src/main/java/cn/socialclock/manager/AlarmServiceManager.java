package cn.socialclock.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import cn.socialclock.receiver.AlarmReceiver;
import cn.socialclock.utils.ConstantData;

/**
 * Created by mapler
 *
 */
public class AlarmServiceManager {

    private Context context;
    private Intent alarmIntent;
    private PendingIntent pendingIntent;

    private AlarmManager alarmManager;

    protected AlarmServiceManager (Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        this.alarmIntent = new Intent(context, AlarmReceiver.class);
    }

    /** Set(or Update) a alarm
     * @param eventId String alarm event id
     * @param alarmType int alarm type
     * @param alarmTimeStamp long alarm time stamp
     */
    protected void setAlarm(String eventId, int alarmType, long alarmTimeStamp) {
        // bundle the alarmEventId
        alarmIntent.putExtra(ConstantData.BundleArgsName.ALARM_EVENT_ID, eventId);
        // bundle alarm type
        alarmIntent.putExtra(ConstantData.BundleArgsName.ALARM_TYPE, alarmType);

        pendingIntent = PendingIntent.getBroadcast(context, 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeStamp, pendingIntent);
    }

    /**
     * Cancel an alarm
     */
    protected void cancelAlarm() {
        // cancel a alarm intent
        pendingIntent = PendingIntent.getBroadcast(context, 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}
