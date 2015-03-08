package cn.socialclock.db;

import java.sql.Time;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class SnoozeTime {

    private Integer transaction_id;
    private Integer snooze_times;
    private Time snooze_time;

    public SnoozeTime(Integer transaction_id, Integer snooze_times,
                      Time snooze_time) {
        this.transaction_id = transaction_id;
        this.snooze_times = snooze_times;
        this.snooze_time = snooze_time;
    }

    public Integer getTransaction_id() {
        return transaction_id;
    }
    public void setTransaction_id(Integer transaction_id) {
        this.transaction_id = transaction_id;
    }
    public Integer getSnooze_times() {
        return snooze_times;
    }
    public void setSnooze_times(Integer snooze_times) {
        this.snooze_times = snooze_times;
    }
    public Time getSnooze_time() {
        return snooze_time;
    }
    public void setSnooze_time(Time snooze_time) {
        this.snooze_time = snooze_time;
    }

    public void insertToDb(Context context) {
        ContentValues snoozevalue = new ContentValues();
        snoozevalue.put("transaction_id", transaction_id);
        snoozevalue.put("snooze_times", snooze_times);
        snoozevalue.put("snooze_time", snooze_time.toString());
        DatabaseHelper dbHelper = new DatabaseHelper(context, "socialclock_db",	1);
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert("snooze", null, snoozevalue);
            db.close();
        } catch (SQLiteException e) {
            Log.e("socialalarmlog", "dbHelper.getWritableDatabase fail.");
        }
    }


}
