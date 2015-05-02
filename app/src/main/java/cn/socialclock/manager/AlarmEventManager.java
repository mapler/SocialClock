package cn.socialclock.manager;

import android.content.Context;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import cn.socialclock.db.AlarmEventDbAdapter;
import cn.socialclock.model.AlarmEvent;

/**
 * @author mapler
 * Alarm Event Manager
 */
public class AlarmEventManager {

    private AlarmEventDbAdapter dbAdapter;

    /**
     * Constructor
     * @param context Context
     */
    protected AlarmEventManager(Context context) {
        this.dbAdapter = new AlarmEventDbAdapter(context);
    }

    /**
     * Generate an UUID of alarm event
     * @return uuid String
     */
    private String genEventId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Get alarm event from db by alarm event id
     * @param alarmEventId String
     * @return AlarmEvent obj
     */
    protected AlarmEvent getAlarmEventById(String alarmEventId) {
        return dbAdapter.getByEventId(alarmEventId);
    }

    /**
     * Get all alarm event from db
     * @return AlarmEvent list
     */
    protected List<AlarmEvent> getAllAlarmEvents() {
        return dbAdapter.findAll();
    }

    /**
     * Generate alarm event id
     * @return alarmEventId String
     */
    protected String initAlarmEvent() {
        return genEventId();
    }

    /**
     * new an alarm event from db
     * @param alarmEventId String
     * @param userId String
     * @param startAt Calendar
     */
    protected void startAlarmEvent(String alarmEventId, String userId, String userName, Calendar startAt) {
        AlarmEvent alarmEvent = new AlarmEvent(alarmEventId, userId, userName, startAt);
        dbAdapter.insert(alarmEvent);
    }

    /**
     * Update alarm event snooze count
     * @param alarmEventId String alarm event id
     */
    protected void snoozeAlarmEvent(String alarmEventId) {
        AlarmEvent alarmEvent = getAlarmEventById(alarmEventId);
        // update alarm event snooze time count up
        alarmEvent.snoozeTimeCountUp();
        dbAdapter.update(alarmEvent);
    }

    /**
     * Delete an alarm with an alarm event id
     * @param alarmEventId String
     */
    protected void deleteAlarmEvent(String alarmEventId) {
        dbAdapter.delete(alarmEventId);
    }

    /**
     * finishAlarmEvent if event not finished
     * @param alarmEventId String
     */
    protected void finishAlarmEvent(String alarmEventId) {
        Calendar getUpTime = Calendar.getInstance();
        AlarmEvent alarmEvent = getAlarmEventById(alarmEventId);
        if (alarmEvent.getEndAt() == null) {
            alarmEvent.setEndAt(getUpTime);
            dbAdapter.update(alarmEvent);
        }
    }
}
