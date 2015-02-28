package cn.socialclock;

import java.util.Date;

import cn.socialclock.utils.ConstantData;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author mapler
 *	闹钟Receiver
 *	有一个周日或者SNOOZE判断，	OK即弹出AlarmPop。
 *							否则建立下一个闹钟
 */
public class AlarmReceiver extends BroadcastReceiver {
	private SharedPreferences clockSettings;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("socialalarmlog", "AlarmReceiver: onReceive");
		/* 初始化 */
		clockSettings = context.getSharedPreferences("ClockSettings",
				Context.MODE_PRIVATE);
		int daysofweek = clockSettings.getInt("daysofweek", -1);
		int alarmtype = intent.getIntExtra("alarmtype",
				ConstantData.AlarmType.ALARM_NORMAL);

		Date now = new Date(System.currentTimeMillis());
		int todayofweek = (int) Math.pow(2, now.getDay());
		Log.d("socialalarmlog", "AlarmReceiver: preferences>daysofweek = "
				+ daysofweek);
		Log.d("socialalarmlog", "AlarmReceiver: todayofweek = " + todayofweek);

		if ((alarmtype == ConstantData.AlarmType.ALARM_SNOOZE)
				|| (daysofweek | todayofweek) == daysofweek) {
			/* 如果是Snooze闹钟或者今天是闹钟日 */
			Log.d("socialalarmlog",
					"AlarmReceiver: alarmed at " + now.toLocaleString());
			Intent popup = new Intent(context, AlarmPop.class);
			popup.putExtra("alarmtype", alarmtype);
			popup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			context.startActivity(popup);
		} else {
			/* 如果不是，则启动下一个Normal闹钟 */
			Log.d("socialalarmlog",
					"AlarmReceiver: silence day, " + now.toLocaleString());
			AlarmCreator alcreator = new AlarmCreator(context);
			alcreator.createNormalAlarm();
		}
	}
}
