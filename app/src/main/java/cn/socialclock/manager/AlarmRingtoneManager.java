package cn.socialclock.manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.SocialClockLogger;


public class AlarmRingtoneManager {

    private MediaPlayer ringtoneMediaPlayer;
    private Context context;
    private Uri alarmUri;

    public AlarmRingtoneManager(Context context) {
        this.context = context;
        this.alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        this.ringtoneMediaPlayer = new MediaPlayer();
        this.ringtoneMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        this.ringtoneMediaPlayer.setLooping(true);
    }

    public void playRingtone() {
        try {
            ringtoneMediaPlayer.setDataSource(context, alarmUri);
            /* start player ringtone */
            ringtoneMediaPlayer.prepare();
            ringtoneMediaPlayer.start();
        } catch (IOException e) {
            /* if fail */
            Toast.makeText(context, "Player Ringtone fail.", Toast.LENGTH_SHORT);
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

    public void stopRingtone() {
        ringtoneMediaPlayer.stop();
    }
}

