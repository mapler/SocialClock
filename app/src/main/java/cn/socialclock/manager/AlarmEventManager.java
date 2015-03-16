package cn.socialclock.manager;

import java.util.Calendar;
import java.util.UUID;

import cn.socialclock.db.AlarmEventDatabaseHelper;
import cn.socialclock.db.AlarmEventDbAdapter;
import cn.socialclock.model.AlarmEvent;
import cn.socialclock.model.ClockSettings;
import cn.socialclock.receiver.AlarmReceiver;
import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.DatetimeFormatter;
import cn.socialclock.utils.SocialClockLogger;

import android.app.AlarmManager;
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
    private AlarmNotificationManager alarmNotificationManager;

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
        this.alarmNotificationManager = new AlarmNotificationManager(context);
    }

    /**
     * Generate an UUID of alarm event
     * @return uuid String
     */
    private String genEventId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Get or new an alarm event from db
     * @param alarmEventId String
     * @param startAt Calendar
     * @return AlarmEvent obj
     */
    public AlarmEvent startAlarmEvent(String alarmEventId, Calendar startAt) {
        AlarmEvent alarmEvent = getAlarmEventById(alarmEventId);
        /* init an alarm event if not exist */
        if (alarmEvent == null){
            String userId = clockSettings.getUserId();
            alarmEvent = new AlarmEvent(alarmEventId, userId, startAt);
            dbAdapter.insert(alarmEvent);
        }
        return alarmEvent;
    }

    /**
     * Update(or create) a snooze alarm
     * 1. alarm event snooze times
     * 2. set next snooze alarm with intent
     * 3. cancel if any snooze notification
     * 4. create new notification
     * @param currentAlarmEvent AlarmEvent
     */
    public void updateSnoozeAlarm(AlarmEvent currentAlarmEvent) {

        // update alarm event snooze time count up
        currentAlarmEvent.snoozeTimeCountUp();
        dbAdapter.update(currentAlarmEvent);

        // get snooze timestamp
        int snoozeDuration = clockSettings.getSnoozeDuration();
        Calendar snoozeTime = Calendar.getInstance();
        snoozeTime.add(Calendar.MINUTE, snoozeDuration);
        snoozeTime.set(Calendar.SECOND, 0);

        long snoozeTimeStamp = snoozeTime.getTimeInMillis();

        // create alarm
        setAlarm(currentAlarmEvent.getEventId(),
                ConstantData.AlarmType.ALARM_SNOOZE,
                snoozeTimeStamp);

        // cancel snooze notification if has one
        alarmNotificationManager.cancelNotification();

        // create next notification
        alarmNotificationManager.createNotification(currentAlarmEvent.getEventId(), snoozeTime);

        // write log
        SocialClockLogger.log("AlarmEventManager: snooze to "
                + DatetimeFormatter.calendarToString(snoozeTime));
    }

    /**
     * Create a normal alarm
     * 1. read alarm time from preference settings
     * 2. generate an alarm event id
     * 2. set an alarm with intent
     * 3. cancel if any snooze notification
     * @return alarmEventId String
     */
    public String createNormalAlarm() {
        // get settings
        int hour = clockSettings.getHour();
        int minute = clockSettings.getMinute();

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

        // generate alarm event id
        String currentAlarmEventId = genEventId();
        // write log
        SocialClockLogger.log("AlarmEventManager: createNormalAlarm: "
                + "create event: " + currentAlarmEventId
                + DatetimeFormatter.calendarToString(alarmAt));

        // set an alarm
        setAlarm(currentAlarmEventId,
                ConstantData.AlarmType.ALARM_NORMAL,
                alarmTimeStamp);

        // cancel snooze notification if has one
        alarmNotificationManager.cancelNotification();

        // write log
        SocialClockLogger.log("AlarmEventManager: createNormalAlarm: "
                + "clock is set at "
                + DatetimeFormatter.calendarToString(alarmAt));

        return currentAlarmEventId;
    }

    /** Set(or Update) a alarm
     * @param eventId String alarm event id
     * @param alarmType int alarm type
     * @param alarmTimeStamp long alarm time stamp
     */
    private void setAlarm(String eventId, int alarmType, long alarmTimeStamp) {
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
        alarmNotificationManager.cancelNotification();
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

    /** Get up Action
     * todo
     * @param currentAlarmEvent AlarmEvent
     */
    public void getUp(AlarmEvent currentAlarmEvent) {
        SocialClockLogger.log("GetUpAction");

        alarmNotificationManager.cancelNotification();

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
