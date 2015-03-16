package cn.socialclock.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import cn.socialclock.R;
import cn.socialclock.ui.NotificationTouchActivity;
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
     * Cancel a notification
     */
    protected void cancelNotification() {
        notificationManager.cancel(ConstantData.AlarmType.ALARM_SNOOZE);
    }

    /** Create a notification
     * @param alarmEventId String
     * @param snoozeTime Calendar
     * */
    protected void createNotification(String alarmEventId, Calendar snoozeTime) {

        Notification notification = new Notification(
                R.drawable.alarm_notification_icon, "SocialAlarm", System
                .currentTimeMillis());

        Intent notificationIntent = new Intent(context,
                NotificationTouchActivity.class);
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

}
