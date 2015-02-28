package cn.socialclock.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DatabaseHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	public DatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("Database Created");
		db.execSQL("create table alarmtransaction("
				+ "_id integer primary key autoincrement,"
				+ "transaction_id decimal(12,0)," + "user_id integer,"
				+ "alarm_date date," + "alarm_time time," + "getup_time time,"
				+ "snooze_times integer," + "isgetup integer)");
		db.execSQL("create table user("
				+ "user_id integer primary key autoincrement,"
				+ "user_name verchar," + "alarm_time time,"
				+ "daysofweek integer)");
		db.execSQL("create table snooze("
				+ "_id integer primary key autoincrement,"
				+ "transaction_id decimal(11,0)," + "snooze_times integer,"
				+ "snooze_time time)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("update a Database");
	}

}
