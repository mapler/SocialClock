package cn.socialclock.manager;

import android.content.Context;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.Calendar;

import cn.socialclock.model.AlarmEvent;
import cn.socialclock.utils.DatetimeFormatter;
import cn.socialclock.utils.SocialClockLogger;

/**
 * Created by mapler
 * Sns Manager
 */
public class SnsManager {

    private Context context;

    /**
     * Contractor
     * @param context Context
     */
    public SnsManager(Context context) {
        this.context = context;
    }

    /**
     * Get twitter status service
     * return null if twitter session not exist
     * @return StatusesService
     */
    private StatusesService getStatusesService() {
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        if(session != null) {
            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
            return twitterApiClient.getStatusesService();
        }
        else {
            SocialClockLogger.log("SnsManager initClient failure. Can't get valid session. ");
            return null;
        }
    }

    /**
     * Tweet a message
     * @param message String
     */
    public void tweet(String message) {
        StatusesService statusesService = getStatusesService();
        if (statusesService != null) {
            statusesService.update(message, null, false, null, null, null, false, null, new Callback<Tweet>() {
                @Override
                public void success(Result result) {
                    SocialClockLogger.log("SnsManager tweet success. result = " + result.toString());
                    Toast.makeText(context, "Tweet!", Toast.LENGTH_SHORT).show();
                }

                public void failure(TwitterException exception) {
                    SocialClockLogger.log("SnsManager tweet failure. " + exception.toString());
                    Toast.makeText(context, "Tweet Fail!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Format an AlarmEvent obj to String
     * todo pattern class
     * @param alarmEvent AlarmEvent obj
     * @return String
     */
    protected String buildSnsMessage(AlarmEvent alarmEvent) {

        long snoozePeriodInSeconds = (alarmEvent.getEndAt().getTimeInMillis() -
                alarmEvent.getStartAt().getTimeInMillis()) / 1000;

        String snsMessage;
        if (snoozePeriodInSeconds > 60 || alarmEvent.getSnoozeTimes() > 0 ) {
            String snoozePeriodMessage = ""
                    + (snoozePeriodInSeconds / 60 > 0 ? (snoozePeriodInSeconds / 60 + " min") :
                    snoozePeriodInSeconds % 60 + " sec");
            snsMessage = "(test) "
                    + "Alarm at "
                    + alarmEvent.getStartAt().get(Calendar.HOUR)
                    + ":"
                    + String.format("%02d", alarmEvent.getStartAt().get(Calendar.MINUTE))
                    + ". Get up at "
                    + alarmEvent.getEndAt().get(Calendar.HOUR)
                    + ":"
                    + String.format("%02d", alarmEvent.getEndAt().get(Calendar.MINUTE))
                    + ". Snooze "
                    + alarmEvent.getSnoozeTimes() + (alarmEvent.getSnoozeTimes() > 1 ? " times. " : " time. ")
                    + "Late " + snoozePeriodMessage
                    + " (" + DatetimeFormatter.calendarToString(alarmEvent.getEndAt())
                    + ")";
        }
        else {
            snsMessage = "(test) "
                    + "Alarm and get up at "
                    + alarmEvent.getStartAt().get(Calendar.HOUR)
                    + ":"
                    + String.format("%02d", alarmEvent.getStartAt().get(Calendar.MINUTE))
                    + ". (" + DatetimeFormatter.calendarToString(alarmEvent.getEndAt())
                    + ")";
        }

        return snsMessage;
    }
}
