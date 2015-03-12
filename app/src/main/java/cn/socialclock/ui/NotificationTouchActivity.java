package cn.socialclock.ui;


import android.app.Activity;
import android.os.Bundle;

import cn.socialclock.model.AlarmEvent;
import cn.socialclock.manager.AlarmEventManager;
import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.SocialClockLogger;

/**
 * @author mapler
 * Action when click notification
 */

public class NotificationTouchActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SocialClockLogger.log("NotificationAction: onCreate");

        // get alarm event id
        String alarmEventId = this.getIntent().getStringExtra(ConstantData.BundleArgsName.ALARM_EVENT_ID);
        SocialClockLogger.log("NotificationAction: onCreate: alarmEventId: " + alarmEventId);

        AlarmEventManager alarmEventManager = new AlarmEventManager(this);
        AlarmEvent currentAlarmEvent = alarmEventManager.getAlarmEventById(alarmEventId);

        // get up action
        alarmEventManager.getUp(currentAlarmEvent);

        this.finish();
    }
}
