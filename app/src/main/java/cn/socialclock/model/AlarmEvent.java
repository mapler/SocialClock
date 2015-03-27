package cn.socialclock.model;

import java.util.Calendar;

import cn.socialclock.utils.ConstantData;

/**
 * @author mapler
 * Alarm Event
 * */
public class AlarmEvent {

    private String eventId;
    private String userId;
    private String userName;
    private Calendar startAt;
    private Calendar endAt;
    private int snoozeTimes;
    private Calendar syncAt;
    private Calendar deletedAt;

    /**
     * constructor when create an alarm
     * @param eventId String alarm event id
     * @param userId String user id
     * @param userName String user name
     * @param startAt Calendar alarmed time
     */
    public AlarmEvent(String eventId, String userId, String userName, Calendar startAt) {
        this.eventId = eventId;
        this.userId = userId;
        this.userName = userName;
        this.startAt = startAt;
    }

    /**
     * constructor
     * @param eventId String alarm event id (primary key)
     * @param userId String user id
     * @param userName String user name
     * @param startAt Calendar alarmed time
     * @param endAt Calendar get up time
     * @param snoozeTimes int snooze times
     * @param syncAt Calendar server sync time
     * @param deletedAt Calendar deleted time if was deleted
     */
    public AlarmEvent(String eventId,
                      String userId,
                      String userName,
                      Calendar startAt,
                      Calendar endAt,
                      int snoozeTimes,
                      Calendar syncAt,
                      Calendar deletedAt) {

        this.eventId = eventId;
        this.userId = userId;
        this.userName = userName;
        this.startAt = startAt;
        this.endAt = endAt;
        this.snoozeTimes = snoozeTimes;
        this.syncAt = syncAt;
        this.deletedAt = deletedAt;
    }

    public String getEventId() {
        return eventId;
    }

    public String getUserId() {
        return userId;
    }

    /**
     * get user name
     * if username is null, return "ANONYMOUS"
     * @return username String
     */
    public String getUserName() {
        if (userName == null) {
            return ConstantData.UserName.ANONYMOUS_USER;
        }
        return userName;
    }

    public Calendar getStartAt() {
        return startAt;
    }

    public Calendar getEndAt() {
        return endAt;
    }

    public void setEndAt(Calendar endAt) {
        this.endAt = endAt;
    }

    public int getSnoozeTimes() {
        return snoozeTimes;
    }

    public void setSnoozeTimes(int snoozeTimes) {
        this.snoozeTimes = snoozeTimes;
    }

    /** count up snooze time */
    public void snoozeTimeCountUp() {
        this.snoozeTimes++;
    }

    public Calendar getSyncAt() {
        return syncAt;
    }

    public void setSyncAt(Calendar syncAt) {
        this.syncAt = syncAt;
    }

    public Calendar getDeletedAt() {
        return deletedAt;
    }

    private void setDeletedAt(Calendar deletedAt) {
        this.deletedAt = deletedAt;
    }

    /** delete called by client */
    public void delete() {
        this.setDeletedAt(Calendar.getInstance());
    }

    /** delete called when sync */
    public void delete(Calendar deletedAt) {
        this.setDeletedAt(deletedAt);
    }
}
