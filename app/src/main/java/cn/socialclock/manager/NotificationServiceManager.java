package cn.socialclock.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import cn.socialclock.R;
import cn.socialclock.ui.AlarmNotificationTouchActivity;
import cn.socialclock.ui.SnoozeNotificationTouchActivity;
import cn.socialclock.utils.ConstantData;

/**
 * Created by mapler
 * Manage the notification. (Old API)
 */
public class NotificationServiceManager {

    private Context context;
    private NotificationManager notificationManager;

    protected NotificationServiceManager(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
    }
    /**
     * Cancel all notifications
     */
    protected void cancelAllNotifications() {
        cancelAlarmNotification();
        cancelSnoozeNotification();
    }

    /**
     * Cancel a snooze notification
     */
    protected void cancelSnoozeNotification() {
        notificationManager.cancel(ConstantData.AlarmType.ALARM_SNOOZE);
    }

    /**
     * Cancel a alarm notification
     */
    protected void cancelAlarmNotification() {
        notificationManager.cancel(ConstantData.AlarmType.ALARM_NORMAL);
    }

    /** Create a snooze notification
     * @param alarmEventId String
     * @param snoozeTime Calendar
     * */
    protected void createSnoozeNotification(String alarmEventId, Calendar snoozeTime) {

        Notification notification = new Notification(
                R.drawable.alarm_notification_icon, "SocialAlarm", System
                .currentTimeMillis());

        Intent notificationIntent = new Intent(context,
                SnoozeNotificationTouchActivity.class);
        // bundle the alarmEventId
        notificationIntent.putExtra(ConstantData.BundleArgsName.ALARM_EVENT_ID, alarmEventId);

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

    /** Create a alarm notification
     * @param alarmEventId String
     * @param alarmTime Calendar
     * */
    protected void createAlarmNotification(String alarmEventId, Calendar alarmTime) {

        Notification notification = new Notification(
                R.drawable.alarm_notification_icon, "SocialAlarm", System
                .currentTimeMillis());

        Intent notificationIntent = new Intent(context,
                AlarmNotificationTouchActivity.class);
        // bundle the alarmEventId
        notificationIntent.putExtra(ConstantData.BundleArgsName.ALARM_EVENT_ID, alarmEventId);

        String notificationText = "Alarm at "
                + String.format("%02d", alarmTime.get(Calendar.HOUR_OF_DAY)) + ":"
                + String.format("%02d", alarmTime.get(Calendar.MINUTE));
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, ConstantData.AlarmType.ALARM_NORMAL,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, "SocialAlarm", notificationText, contentIntent);
        // send notification
        notificationManager.notify(ConstantData.AlarmType.ALARM_NORMAL, notification);
    }
}
