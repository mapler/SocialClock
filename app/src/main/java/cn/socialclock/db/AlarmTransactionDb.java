package cn.socialclock.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class AlarmTransactionDb {

    DatabaseHelper dbHelper;

    public void insertToDb(Context context, AlarmTransaction thisTransaction) {
        ContentValues snoozevalue = new ContentValues();
        snoozevalue.put("transaction_id", thisTransaction.getTransaction_id());
        snoozevalue.put("user_id", thisTransaction.getUser_id());
        snoozevalue.put("alarm_date", thisTransaction.getAlarm_date().toString());
        snoozevalue.put("alarm_time", thisTransaction.getAlarm_time().toString());
        snoozevalue.put("getup_time", thisTransaction.getGetup_time().toString());
        snoozevalue.put("snooze_times", thisTransaction.getSnooze_times());
        snoozevalue.put("isgetup", thisTransaction.getIsgetup());
        dbHelper = new DatabaseHelper(context, "socialclock_db", 1);
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert("alarmtransaction", null, snoozevalue);
            db.close();
        } catch (SQLiteException e) {
            Log.e("socialalarmlog", "dbHelper.getWritableDatabase fail.");
        }
    }

//	public void update(Context context, AlarmTransaction thisTransaction) {
//		ContentValues snoozevalue = new ContentValues();
//		snoozevalue.put("transaction_id", thisTransaction.getTransaction_id());
//		snoozevalue.put("user_id", thisTransaction.getUser_id());
//		snoozevalue.put("alarm_date", thisTransaction.getAlarm_date().toString());
//		snoozevalue.put("alarm_time", thisTransaction.getAlarm_time().toString());
//		snoozevalue.put("getup_time", thisTransaction.getGetup_time().toString());
//		snoozevalue.put("snooze_times", thisTransaction.getSnooze_times());
//		snoozevalue.put("isgetup", thisTransaction.getIsgetup());
//		DatabaseHelper dbHelper = new DatabaseHelper(context, "socialclock_db",	1);
//		try {
//			SQLiteDatabase db = dbHelper.getWritableDatabase();
//			db.insert("alarmtransaction", null, snoozevalue);
//		} catch (SQLiteException e) {
//			Log.e("socialalarmlog", "dbHelper.getWritableDatabase fail.");
//		}
//	}
}
