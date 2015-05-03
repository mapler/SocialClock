package cn.socialclock.ui;


import android.app.Activity;
import android.os.Bundle;

import cn.socialclock.manager.SocialClockManager;
import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.SocialClockLogger;

/**
 * @author mapler
 * Action when click notification
 * 1. get up
 * 2. send sns
 */

public class SnoozeNotificationTouchActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SocialClockLogger.log("SnoozeNotificationAction: onCreate");

        // get alarm event id
        String alarmEventId = this.getIntent().getStringExtra(ConstantData.BundleArgsName.ALARM_EVENT_ID);
        SocialClockLogger.log("SnoozeNotificationAction: onCreate: alarmEventId: " + alarmEventId);

        SocialClockManager socialClockManager = new SocialClockManager(this);

        // get up action
        socialClockManager.getUp(alarmEventId);

        // sns
        socialClockManager.sendSns(alarmEventId);

        this.finish();
    }
}
