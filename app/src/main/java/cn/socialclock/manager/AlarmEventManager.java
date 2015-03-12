package cn.socialclock.manager;

import java.util.Calendar;

import cn.socialclock.R;
import cn.socialclock.db.AlarmEventDatabaseHelper;
import cn.socialclock.db.AlarmEventDbAdapter;
import cn.socialclock.model.AlarmEvent;
import cn.socialclock.model.ClockSettings;
import cn.socialclock.receiver.AlarmReceiver;
import cn.socialclock.ui.NotificationTouchActivity;
import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.DatetimeFormatter;
import cn.socialclock.utils.SocialClockLogger;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author mapler
 * Alarm Event Manager
 *  1. update normal clock
 *  2. update snooze clock
 *  3. cancel clock
 */
public class AlarmEventManager {

    private ClockSettings clockSettings;
    private Context context;
    private Intent alarmIntent;
    private PendingIntent pendingIntent;

    private AlarmManager alarmManager;
    private AlarmEventDbAdapter dbAdapter;

    private NotificationManager notificationManager;

    /**
     * Constructor
     * @param context Context
     */
    public AlarmEventManager(Context context) {

        this.context = context;
        this.clockSettings = new ClockSettings(context);
        this.alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        this.alarmIntent = new Intent(context, AlarmReceiver.class);

        AlarmEventDatabaseHelper dbHelper = new AlarmEventDatabaseHelper(context, null, 1);
        this.dbAdapter = new AlarmEventDbAdapter(dbHelper.getReadableDatabase());
    }

    /**
     * Update(or create) a snooze alarm
     * @param currentAlarmEvent AlarmEvent
     */
    public void updateSnoozeAlarm(AlarmEvent currentAlarmEvent) {
        //

        // update alarm event snooze time count up
        currentAlarmEvent.snoozeTimeCountUp();
        dbAdapter.update(currentAlarmEvent);

        // get snooze timestamp
        int snoozeDuration = clockSettings.getSnoozeDuration();
        Calendar snoozeTime = Calendar.getInstance();
        snoozeTime.add(Calendar.MINUTE, snoozeDuration);
        snoozeTime.set(Calendar.SECOND, 0);

        long snoozeTimeStamp = snoozeTime.getTimeInMillis();

        // cancel snooze notification if has one
        cancelNotification();

        // create alarm
        createAlarm(currentAlarmEvent.getEventId(),
                ConstantData.AlarmType.ALARM_SNOOZE,
                snoozeTimeStamp);

        createNotification(currentAlarmEvent, snoozeTime);

        // write log
        SocialClockLogger.log("AlarmEventManager: snooze to "
                + DatetimeFormatter.calendarToString(snoozeTime));
    }

    /**
     * Create a normal alarm
     * @return created alarm event id
     */
    public String createNormalAlarm() {
        // get settings
        int hour = clockSettings.getHour();
        int minute = clockSettings.getMinute();
        String userId = clockSettings.getUserId();

        // make a time obj
        Calendar alarmAt = Calendar.getInstance();
        alarmAt.set(Calendar.HOUR_OF_DAY, hour);
        alarmAt.set(Calendar.MINUTE, minute);
        alarmAt.set(Calendar.SECOND, 0);
        alarmAt.set(Calendar.MILLISECOND, 0);

        // get alarm time stamp
        long alarmTimeStamp = alarmAt.getTimeInMillis();
        if (alarmTimeStamp <= System.currentTimeMillis()) {
            // plus 1 day if alarm time has past
            alarmAt.add(Calendar.DATE, 1);
            alarmTimeStamp = alarmAt.getTimeInMillis();
        }

        // cancel snooze notification if has one
        cancelNotification();

        // create an alarm event by userId and alarmAt
        AlarmEvent alarmEvent = new AlarmEvent(userId, alarmAt);
        // insert alarmEvent to db
        dbAdapter.insert(alarmEvent);

        // create alarm
        createAlarm(alarmEvent.getEventId(),
                ConstantData.AlarmType.ALARM_NORMAL,
                alarmTimeStamp);

        // write log
        SocialClockLogger.log("AlarmEventManager: clockTime is set at "
                + DatetimeFormatter.calendarToString(alarmAt));
        return alarmEvent.getEventId();
    }

    /** Create(or Update) a alarm
     * @param eventId String alarm event id
     * @param alarmType int alarm type
     * @param alarmTimeStamp long alarm time stamp
     */
    private void createAlarm(String eventId, int alarmType, long alarmTimeStamp) {
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
    public void cancelAlarm() {
        // cancel a alarm intent
        pendingIntent = PendingIntent.getBroadcast(context, 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        // cancel notification
        cancelNotification();
    }

    /**
     * Cancel an alarm with an alarm event id
     * @param alarmEventId String
     */
    public void cancelAlarm(String alarmEventId) {
        this.cancelAlarm();
        // delete current alarmEvent
        dbAdapter.delete(alarmEventId);
    }

    /**
     * Get alarm event from db by alarm event id
     * @param alarmEventId String
     * @return AlarmEvent obj
     */
    public AlarmEvent getAlarmEventById(String alarmEventId) {
        return dbAdapter.findByEventId(alarmEventId);
    }

    /**
     * Cancel a notification
     */
    public void cancelNotification() {
        notificationManager = (NotificationManager) context.getSystemService(
                        Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ConstantData.AlarmType.ALARM_SNOOZE);
    }

    /** Create a notification
     * todo
     * @param currentAlarmEvent AlarmEvent
     * @param snoozeTime Calendar
     * */
    public void createNotification(AlarmEvent currentAlarmEvent, Calendar snoozeTime) {

        Notification notification = new Notification(
                R.drawable.alarm_notification_icon, "SocialAlarm", System
                .currentTimeMillis());

        Intent notificationIntent = new Intent(context,
                NotificationTouchActivity.class);
        // bundle the alarmEventId
        notificationIntent.putExtra(ConstantData.BundleArgsName.ALARM_EVENT_ID,
                currentAlarmEvent.getEventId());

    String notificationText = "Snooze to "
            + String.format("%02d", snoozeTime.get(Calendar.HOUR_OF_DAY)) + ":"
            + String.format("%02d", snoozeTime.get(Calendar.MINUTE))
            + ", Touch to cancel";
    PendingIntent contentIntent = PendingIntent.getActivity(
            context, ConstantData.AlarmType.ALARM_SNOOZE,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    notification.setLatestEventInfo(context, "SocialAlarm", notificationText, contentIntent);
    // send notification
    notificationManager.notify(ConstantData.AlarmType.ALARM_SNOOZE, notification);
}

    /** Get up Action
     * todo
     * @param currentAlarmEvent AlarmEvent
     */
    public void getUp(AlarmEvent currentAlarmEvent) {
        SocialClockLogger.log("GetUpAction");

        cancelNotification();

		/* update alarm event to db */
        Calendar getUpTime = Calendar.getInstance();
        currentAlarmEvent.setEndAt(getUpTime);
        dbAdapter.update(currentAlarmEvent);

        long snoozePeriodInSeconds = (currentAlarmEvent.getEndAt().getTimeInMillis() -
                currentAlarmEvent.getStartAt().getTimeInMillis()) / 1000;

        String snoozePeriodMessage = ""
                + (snoozePeriodInSeconds / 60 > 0 ? (snoozePeriodInSeconds / 60 + "分") : "")
                + snoozePeriodInSeconds % 60 + "秒";

        String textGetupWeibo = "(这是测试)"
                + "我本来定的"
                + currentAlarmEvent.getStartAt().get(Calendar.HOUR)
                + "点"
                + String.format("%02d", currentAlarmEvent.getStartAt().get(Calendar.MINUTE))
                + "分的闹钟，结果"
                + currentAlarmEvent.getEndAt().get(Calendar.HOUR)
                + "点"
                + String.format("%02d", currentAlarmEvent.getEndAt().get(Calendar.MINUTE))
                + "分才起来。按掉"
                + currentAlarmEvent.getSnoozeTimes() + "次闹铃，赖床" + snoozePeriodMessage
                + "...我果然是个废物。(" + DatetimeFormatter.calendarToString(currentAlarmEvent.getEndAt())
                + ")";
        SocialClockLogger.log(textGetupWeibo);
        textGetupWeibo += "(send to sns)";
        Toast.makeText(context, textGetupWeibo, Toast.LENGTH_LONG).show();

        // create next alarm
        createNormalAlarm();
    }
}
