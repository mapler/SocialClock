package cn.socialclock.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.SocialClockLogger;

/**
 * @author mapler
 * Action when click notification
 * Start AlarmPopActivity
 */

public class AlarmNotificationTouchActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SocialClockLogger.log("AlarmNotificationAction: onCreate");

        // get alarm type
        int alarmType = this.getIntent().getIntExtra(ConstantData.BundleArgsName.ALARM_TYPE,
                ConstantData.AlarmType.ALARM_NORMAL);
        // get alarm event id
        String alarmEventId = this.getIntent().getStringExtra(ConstantData.BundleArgsName.ALARM_EVENT_ID);
        SocialClockLogger.log("AlarmNotificationAction: onCreate: alarmEventId: " + alarmEventId);

        /* start alarm popup activity */
        Intent popupIntent = new Intent(this, AlarmPopActivity.class);
        popupIntent.putExtra(ConstantData.BundleArgsName.ALARM_TYPE, alarmType);
        popupIntent.putExtra(ConstantData.BundleArgsName.ALARM_EVENT_ID, alarmEventId);
        popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        this.startActivity(popupIntent);

        this.finish();
    }
}
