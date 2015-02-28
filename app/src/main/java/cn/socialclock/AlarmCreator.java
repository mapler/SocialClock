package cn.socialclock;

import java.util.Calendar;
import java.util.Date;

import cn.socialclock.utils.ConstantData;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author mapler
 *	闹钟生成器
 *	1.生成延时闹钟
 *	2.生成日常闹钟
 *	3.取消闹钟
 *	
 */
public class AlarmCreator {
	private int hour;
	private int minutes;
	private Context context;
	private Intent intent;
	private PendingIntent pendingIntent;
	private AlarmManager alarmManager;
	private SharedPreferences clockSettings;

	public AlarmCreator(Context context) {
		/* 初始化闹钟生成器 */
		this.context = context;
		alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		intent = new Intent(context, AlarmReceiver.class);
	}

	public void createDelayAlarm(long delaytime) {
		/* 新建延时闹钟 */
		intent.putExtra("alarmtype", ConstantData.AlarmType.ALARM_SNOOZE);
		pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, delaytime, pendingIntent);
	}

	public void createNormalAlarm() {
		/* 按preference新建闹钟 */

		clockSettings = context.getSharedPreferences("ClockSettings",
				Context.MODE_PRIVATE);
		hour = clockSettings.getInt("hour", 0);
		minutes = clockSettings.getInt("minutes", 0);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		intent.putExtra("alarmtype", ConstantData.AlarmType.ALARM_NORMAL);
		pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		long clocktime = calendar.getTimeInMillis();
		if (clocktime <= System.currentTimeMillis()) {
			clocktime += ConstantData.ConstantTime.TIME_OF_DAY;
		}
		Log.d("socialalarmlog", "AlarmCreator: clocktime is set at "
				+ (new Date(clocktime).toLocaleString()));
		alarmManager.set(AlarmManager.RTC_WAKEUP, clocktime, pendingIntent);
	}

	public void cancelAlarm(){
		intent = new Intent(context, AlarmReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pendingIntent);
	}


}
