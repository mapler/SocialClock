package cn.socialclock.ui;


import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import cn.socialclock.R;
import cn.socialclock.model.AlarmEvent;
import cn.socialclock.manager.AlarmEventManager;
import cn.socialclock.model.ClockSettings;
import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.SocialClockLogger;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author mapler
 * Alarm Popup UI
 * 1. build ui
 * 2. cancel the notification
 * 3. play the alarm ringtone
 * 4. start record a alarm event if normal alarm
 * 5. snooze clicked
 *      5.1 stop play ringtone
 *      5.2 create snooze alarm
 *      5.3 create snooze notification
 *      5.4 update once snooze to alarm event
 * 6. get up clicked
 *      6.1 stop play ringtone
 *      6.2 create next alarm
 *      6.3 finish alarm event
 * 7. auto stop ringtone after 5 minutes
 */
public class AlarmPopActivity extends Activity {

    private ClockSettings clockSettings;
    private NotificationManager notificationManager;

    private AlarmEventManager alarmEventManager;

    private Calendar nowCalendar;

    private MediaPlayer ringtoneMediaPlayer;

    private AlarmEvent currentAlarmEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SocialClockLogger.log("AlarmPop: onCreate");

        super.onCreate(savedInstanceState);

        // get setting preference
        clockSettings = new ClockSettings(this);

        // alarm creator init
        alarmEventManager = new AlarmEventManager(this);

        // get notification service
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // cancel the snooze notification if exist
        notificationManager.cancel(ConstantData.AlarmType.ALARM_SNOOZE);

        // get current time
        nowCalendar = Calendar.getInstance();

        // get ringtone player
        ringtoneMediaPlayer = new MediaPlayer();

        int alarmType = this.getIntent().getIntExtra(ConstantData.BundleArgsName.ALARM_TYPE, 1);
        String alarmEventId = this.getIntent().getStringExtra(ConstantData.BundleArgsName.ALARM_EVENT_ID);
        SocialClockLogger.log("AlarmPop: alarmType = " + alarmType + "alarmEventId = " + alarmEventId);

        currentAlarmEvent = alarmEventManager.getAlarmEventById(alarmEventId);

        // build ui
        buildInterface();

        // play alarm ringtone
        playAlarmRingtone();

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

        btnSnooze.setOnClickListener(new Button.OnClickListener() {
            /** snooze button */
            @Override
            public void onClick(View v) {
                alarmEventManager.updateSnoozeAlarm(currentAlarmEvent);
                int snoozeDuration = clockSettings.getSnoozeDuration();
                Toast.makeText(AlarmPopActivity.this,
                        "Snooze " + snoozeDuration + " minutes",
                        Toast.LENGTH_SHORT).show();

                // stop ringtone
                ringtoneMediaPlayer.stop();

                AlarmPopActivity.this.finish();
            }
        });

        btnGetup.setOnClickListener(new Button.OnClickListener() {
            /** get up button */
            @Override
            public void onClick(View v) {
                /* stop ringtone */
                ringtoneMediaPlayer.stop();

                /* get up action */
                alarmEventManager.getUp(currentAlarmEvent);

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
        // todo create a media player singleton class
        // set player to loop
        ringtoneMediaPlayer.setLooping(true);

        // get default alarm ringtone uri
        Uri mediaUri = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);

        try {
            /* start player ringtone */
            SocialClockLogger.log("Player Ringtone: " + mediaUri.toString());
            ringtoneMediaPlayer.setDataSource(this, mediaUri);
            ringtoneMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            ringtoneMediaPlayer.prepare();
            ringtoneMediaPlayer.start();
        } catch (Exception e) {
            /* if fail */
            Toast.makeText(this, "Player Ringtone fail.", Toast.LENGTH_SHORT);
            SocialClockLogger.log("Player Ringtone: Fail. " + e.toString());
        }

        /* auto stop after RINGTONE_DURATION */
        Timer ringtoneTimer = new Timer();
        ringtoneTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ringtoneMediaPlayer.stop();
            }
        }, ConstantData.ConstantTime.RINGTONE_DURATION);
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
