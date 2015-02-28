package cn.socialclock;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import cn.socialclock.db.AlarmTransaction;
import cn.socialclock.db.SnoozeTime;
import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.Utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author mapler 1.建立UI 2.播放闹铃 3.取消通知栏信息 4.判断是否日常闹铃 是，则创建一个闹钟事务 5.snooze按钮
 *         5.1建立snooze闹钟 5.2建立snooze通知栏 5.3停止响铃 5.4存储snooze信息，写入数据库 6.起床按钮
 *         6.1停止响铃 6.2新建日常闹钟 6.3取消通知栏信息 6.4存储起床数据，写入数据库 7.五分钟后自动停止闹铃 8.禁用返回键
 * 
 */
public class AlarmPop extends Activity {

	private Button btnNooze;
	private Button btnGetup;
	private SharedPreferences clockSettings;
	private NotificationManager notificationManager;

	private AlarmCreator alarmCreator;

	private int user_id = 1;	//暂时设定用户id为1

	private static AlarmTransaction g_thisTransaction;

	private static int daytimes = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("socialalarmlog", "AlarmPop: onCreate");
		setContentView(R.layout.alarmpop);

		/* 初始化 */
		clockSettings = getSharedPreferences("ClockSettings", MODE_PRIVATE);
		alarmCreator = new AlarmCreator(AlarmPop.this);
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		/* 取消掉当前notification */
		notificationManager.cancel(ConstantData.AlarmType.ALARM_SNOOZE);

		/* 显示闹钟时间 */
		final Time now = new java.sql.Time(System.currentTimeMillis());

		final java.sql.Date nowdate = new java.sql.Date(
				System.currentTimeMillis());
		String nowhour = Utils.timeFormat(now.getHours());
		String nowminutes = Utils.timeFormat(now.getMinutes());
		TextView txTime = (TextView) findViewById(R.id.txClock);
		txTime.setText("" + nowhour + ":" + nowminutes);

		/* 建立铃声播放器 */
		final MediaPlayer mpRingtone = new MediaPlayer();
		mpRingtone.setLooping(true);
		Uri mediaUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_ALARM);
		try {
			Log.i("socialalarmlog", mediaUri.toString());
			mpRingtone.setDataSource(this, mediaUri);
			mpRingtone.setAudioStreamType(AudioManager.STREAM_ALARM);
			mpRingtone.prepare();
			mpRingtone.start();
		} catch (Exception e) {
			Log.e("socialalarmlog", e.toString());
		}

		int alarmtype = this.getIntent().getIntExtra("alarmtype", 1);
		Log.d("socialalarm", "AlarmPop: alarmtype = " + alarmtype
				+ " ALARM_NORMAL = 1, ALARM_SNOOZE = 2");

		final Integer transaction_id = ((nowdate.getYear() + 1900)) % 2000
				* 1000000 + (nowdate.getMonth() + 1) * 10000
				+ nowdate.getDate() * 100 + user_id * 10;
		Log.d("socialalarm", "AlarmPop: transaction_id = " + transaction_id);

		/* 创建闹钟事务 */
		if (alarmtype == ConstantData.AlarmType.ALARM_NORMAL) {
			daytimes++;
			g_thisTransaction = new AlarmTransaction(transaction_id + daytimes,
					user_id, nowdate, now, null, 0, 0);
		}

		btnNooze = (Button) findViewById(R.id.btn_snooze);
		btnGetup = (Button) findViewById(R.id.btn_wakeup);
		btnNooze.setOnClickListener(new Button.OnClickListener() {
			/* 贪睡按钮的处理 */
			@Override
			public void onClick(View v) {
				/* 建立延时snooze alarm */
				int snoozetimelong = clockSettings.getInt("snoozetime", 0);
				long snoozetime = System.currentTimeMillis() + snoozetimelong
						* 60 * 1000;
				alarmCreator.createDelayAlarm(snoozetime);
				Time snoozealarm = new Time(snoozetime);
				Toast.makeText(AlarmPop.this,
						"Snooze " + snoozetimelong + " minutes",
						Toast.LENGTH_LONG).show();
				Log.d("socialalarmlog", "AlarmPop: " + "SnoozeAlarm at "
						+ snoozealarm.toLocaleString());

				/* 新建snooze notification */
				CharSequence tickerText = "SocialAlarm";
				Notification notification = new Notification(
						R.drawable.alarm_notification_icon, tickerText, System
								.currentTimeMillis());
				CharSequence notificationTitle = "SocialAlarm";
				Intent notificationIntent = new Intent(AlarmPop.this,
						NotificationTouchAction.class);

				CharSequence notificationText = "Snooze to "
						+ Utils.timeFormat(snoozealarm.getHours()) + ":"
						+ Utils.timeFormat(snoozealarm.getMinutes())
						+ ", Touch to cancel";
				PendingIntent contentIntent = PendingIntent.getActivity(
						AlarmPop.this, ConstantData.AlarmType.ALARM_SNOOZE,
						notificationIntent, 0);
				notification.setLatestEventInfo(getApplicationContext(),
						notificationTitle, notificationText, contentIntent);
				// 把Notification传递给NotificationManager
				notificationManager.notify(ConstantData.AlarmType.ALARM_SNOOZE,
						notification);

				/* 停止响铃 */
				mpRingtone.stop();

				/* 存储snooze数据 */
				g_thisTransaction.upperSnooze_times();
				SnoozeTime thisSnoozeTime = new SnoozeTime(transaction_id
						+ daytimes, g_thisTransaction.getSnooze_times(), now);
				thisSnoozeTime.insertToDb(AlarmPop.this);

				AlarmPop.this.finish();
			}
		});

		btnGetup.setOnClickListener(new Button.OnClickListener() {
			/* 起床按钮的处理 */
			@Override
			public void onClick(View v) {
				/* 停止响铃 */
				mpRingtone.stop();

				/* 起床动作 */
				GetUpAction.doAction(AlarmPop.this);

				AlarmPop.this.finish();
			}
		});

		/* RINGTONES_LONG时间(5分钟)后闹钟未响即自动停止响铃，之后可设定为可选 */
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				mpRingtone.stop();
			}

		}, ConstantData.ConstantTime.RINGTONES_LONG);

	}

	/* 禁用返回键 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return false;
		}
		if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {
			return false;
		}
		return false;
	}

	public static AlarmTransaction getThisTransaction() {
		return g_thisTransaction;
	}

	public static void setThisTransaction(AlarmTransaction thisTransaction) {
		AlarmPop.g_thisTransaction = thisTransaction;
	}
}
