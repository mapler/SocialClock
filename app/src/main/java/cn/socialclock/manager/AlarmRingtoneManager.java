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

    private static MediaPlayer ringtoneMediaPlayer;
    private static Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    public static MediaPlayer getAlarmRingtoneManager() {
        if (ringtoneMediaPlayer == null) {
            ringtoneMediaPlayer = new MediaPlayer();
        }
        ringtoneMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        ringtoneMediaPlayer.setLooping(true);
        return ringtoneMediaPlayer;
    }

    public static void playRingtone(Context context) {
        try {
            ringtoneMediaPlayer = getAlarmRingtoneManager();
            if (!ringtoneMediaPlayer.isPlaying()) {
                SocialClockLogger.log("Play Ringtone");
                ringtoneMediaPlayer.setDataSource(context, alarmUri);
                /* start player ringtone */
                ringtoneMediaPlayer.prepare();
                ringtoneMediaPlayer.start();
            }
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
                SocialClockLogger.log("Stop Ringtone by Timer");
                stopRingtone();
            }
        }, ConstantData.ConstantTime.RINGTONE_DURATION);
    }

    public static void stopRingtone() {
        ringtoneMediaPlayer = getAlarmRingtoneManager();
        if (ringtoneMediaPlayer.isPlaying()) {
            SocialClockLogger.log("Stop Ringtone");
            ringtoneMediaPlayer.stop();
        }
        ringtoneMediaPlayer.reset();
    }
}

