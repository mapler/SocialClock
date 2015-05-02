package cn.socialclock.ui;


import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import cn.socialclock.R;
import cn.socialclock.manager.SocialClockManager;
import cn.socialclock.model.ClockSettings;
import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.SocialClockLogger;

/**
 * @author mapler
 * Alarm Popup UI
 * 1. start a alarm event if not snooze
 * 2. build ui
 * 3. play the alarm ringtone
 * 4. snooze clicked
 *      4.1 stop play ringtone
 *      4.2 snooze alarm
 * 5. get up clicked
 *      5.1 stop play ringtone
 *      5.2 get up
 *      5.3 send sns
 */
public class AlarmPopActivity extends Activity {

    private ClockSettings clockSettings;

    private SocialClockManager socialClockManager;

    private Calendar nowCalendar;

    private String currentAlarmEventId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SocialClockLogger.log("AlarmPop: onCreate");

        super.onCreate(savedInstanceState);

        // get setting preference
        clockSettings = new ClockSettings(this);

        // alarm creator init
        socialClockManager = new SocialClockManager(this);

        // get current time
        nowCalendar = Calendar.getInstance();

        int alarmType = this.getIntent().getIntExtra(ConstantData.BundleArgsName.ALARM_TYPE, 1);
        currentAlarmEventId = this.getIntent().getStringExtra(ConstantData.BundleArgsName.ALARM_EVENT_ID);
        SocialClockLogger.log("AlarmPop: alarmType = " + alarmType + ", currentAlarmEventId = " + currentAlarmEventId);

        // start alarm
        socialClockManager.startAlarm(currentAlarmEventId);

        // build ui
        buildInterface();

        // play alarm ringtone
        playAlarmRingtone();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socialClockManager.stopRingtone();
    }

    private void buildInterface() {
        // set view layout
        setContentView(R.layout.alarmpop);

        // build time text
        buildTimeTextInterface();

        // build button
        buildButtonInterface();
    }

    private void buildButtonInterface() {

        Button btnSnooze = (Button) findViewById(R.id.btn_snooze);
        Button btnGetup = (Button) findViewById(R.id.btn_wakeup);

        /** snooze button */
        btnSnooze.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // stop ringtone
                socialClockManager.stopRingtone();

                socialClockManager.snoozeAlarm(currentAlarmEventId);
                int snoozeDuration = clockSettings.getSnoozeDuration();
                Toast.makeText(AlarmPopActivity.this,
                        "Snooze " + snoozeDuration + " minutes",
                        Toast.LENGTH_SHORT).show();

                AlarmPopActivity.this.finish();
            }
        });

        /** get up button */
        btnGetup.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* stop ringtone */
                socialClockManager.stopRingtone();

                /* get up action */
                socialClockManager.getUp(currentAlarmEventId);

                // sns
                socialClockManager.sendSns(currentAlarmEventId);

                AlarmPopActivity.this.finish();
            }
        });
    }

    /** build clock time text */
    private void buildTimeTextInterface() {
        String nowHour = String.format("%02d", nowCalendar.get(Calendar.HOUR));
        String nowMinute = String.format("%02d", nowCalendar.get(Calendar.MINUTE));
        TextView txTime = (TextView) findViewById(R.id.txClock);
        txTime.setText(nowHour + ":" + nowMinute);
    }

    /** play the alarm ringtone */
    private void playAlarmRingtone() {
        socialClockManager.playRingtone();
    }

    /** forbidden hard keys
     * todo forbidden other buttons
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {
            return false;
        }
        return false;
    }
}
