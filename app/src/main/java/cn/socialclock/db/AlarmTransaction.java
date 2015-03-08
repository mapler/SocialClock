package cn.socialclock.db;

import java.sql.Date;
import java.sql.Time;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class AlarmTransaction {

    public AlarmTransaction(Integer transaction_id, Integer user_id,
                            Date alarm_date, Time alarm_time, Time getup_time,
                            Integer snooze_times, Integer isgetup) {
        this.transaction_id = transaction_id;
        this.user_id = user_id;
        this.alarm_date = alarm_date;
        this.alarm_time = alarm_time;
        this.getup_time = getup_time;
        this.snooze_times = 0;
        this.isgetup = 0;
    }

    private Integer transaction_id;
    private Integer user_id;
    private Date alarm_date;
    private Time alarm_time;
    private Time getup_time;
    private Integer snooze_times;
    private Integer isgetup;

    public Integer getTransaction_id() {
        return transaction_id;
    }
    public void setTransaction_id(Integer transaction_id) {
        this.transaction_id = transaction_id;
    }
    public Integer getUser_id() {
        return user_id;
    }
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    public Date getAlarm_date() {
        return alarm_date;
    }
    public void setAlarm_date(Date alarm_date) {
        this.alarm_date = alarm_date;
    }
    public Time getAlarm_time() {
        return alarm_time;
    }
    public void setAlarm_time(Time alarm_time) {
        this.alarm_time = alarm_time;
    }
    public Time getGetup_time() {
        return getup_time;
    }
    public void setGetup_time(Time getup_time) {
        this.getup_time = getup_time;
    }
    public Integer getSnooze_times() {
        return snooze_times;
    }
    public void setSnooze_times(Integer snooze_times) {
        this.snooze_times = snooze_times;
    }
    public void upperSnooze_times(){
        this.snooze_times++;
    }
    public Integer getIsgetup() {
        return isgetup;
    }
    public void setIsgetup(Integer isgetup) {
        this.isgetup = isgetup;
    }

    public void insertToDb(Context context){
        AlarmTransactionDb aldb = new AlarmTransactionDb();
        aldb.insertToDb(context, this);
    }


}
