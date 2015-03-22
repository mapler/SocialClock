package cn.socialclock.manager;

import android.content.Context;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
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
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by mapler
 * Sns Manager
 */
public class SnsManager {

    private Context context;

    /**
     * Constructor
     */
    public SnsManager() {
        getSession();
    }

    /**
     * Constructor
     * @param context Context
     */
    public SnsManager(Context context) {
        this.context = context;
        getSession();
    }

    /**
     * require AppSession when TwitterSession not found
     */
    private void requireGuestSession() {
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result appSessionResult) {
                SocialClockLogger.log("SnsManager, requireGuestSession, guest login success. result = " + appSessionResult.toString());
            }
            @Override
            public void failure(TwitterException exception) {
                SocialClockLogger.error("SnsManager, requireGuestSession, guest login failure. " + exception.toString());
            }
        });
    }

    /**
     * Get active session.
     * @return AppSession or TwitterSession
     */
    private Session getSession() {
        Session session = Twitter.getSessionManager().getActiveSession();
        if (session == null) {
            session = TwitterCore.getInstance().getAppSessionManager().getActiveSession();
            if (session == null) {
                requireGuestSession();
            }
            session = TwitterCore.getInstance().getAppSessionManager().getActiveSession();
        }
        SocialClockLogger.log("getSession " + session);
        return session;
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
                    if (context != null) {
                        Toast.makeText(context, "Tweet!", Toast.LENGTH_SHORT).show();
                    }
                }

                public void failure(TwitterException exception) {
                    SocialClockLogger.error("SnsManager tweet failure. " + exception.toString());
                    if (context != null) {
                        Toast.makeText(context, "Tweet Fail!", Toast.LENGTH_SHORT).show();
                    }
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

    /**
     * Twitter Api Client for Custom service
     */
    protected class CustomTwitterApiClient extends TwitterApiClient {

        /**
         * Constructor with Session
         * @param session Session
         */
        protected CustomTwitterApiClient(Session session) {
            super(session);
        }

        /**
         * Provide CustomService with defined endpoints
         */
        protected CustomService getCustomService() {
            return getService(CustomService.class);
        }
    }

    /**
     * CustomService Interface
     * for service those Fabric does not implement.
     */
    public interface CustomService {
        @GET("/1.1/users/show.json")
        void showById(@Query("user_id") long id, Callback<com.twitter.sdk.android.core.models.User> cb);
    }

    public CustomService getCustomService() {
        Session session = getSession();
        if (session != null ) {
            SocialClockLogger.log("session = " + session.toString());
            CustomTwitterApiClient client = new CustomTwitterApiClient(session);
            return client.getCustomService();
        }
        else {
            return null;
        }
    }
}
