package cn.socialclock;

import cn.socialclock.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author mapler
 *	主界面
 */
public class MainActivity extends Activity implements OnClickListener,
		OnSharedPreferenceChangeListener {
	/** Called when the activity is first created. */

	private Button btn_Week_Mon;
	private Button btn_Week_Tue;
	private Button btn_Week_Wed;
	private Button btn_Week_Thu;
	private Button btn_Week_Fri;
	private Button btn_Week_Sat;
	private Button btn_Week_Sun;
	
	private Button btn_setClockOn;
	private Button btn_setClockOff;
	
	private Button btn_tabAnalytics;
	private Button btn_tabSettings;

	private TextView txHour;
	private TextView txMinutes;

	private SharedPreferences clockSettings;
	private SharedPreferences.Editor clockSettingsEditor;

	private boolean isClockSettingButtonVisable = false; // 时间调节按钮组显示与否

	private int hour;
	private int minutes;
	private int exhour = 0;
	private int exminutes = 0;

	private AlarmCreator alarmcreator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Log.d("socialalarmlog", "MainActivity: onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		clockSettings = getSharedPreferences("ClockSettings", MODE_PRIVATE);
		clockSettingsEditor = clockSettings.edit();
		if (!clockSettings.contains("snoozetime")) {
			clockSettingsEditor.putInt("hour", 7);
			clockSettingsEditor.putInt("minutes", 30);
			clockSettingsEditor.putInt("daysofweek", 62);
			clockSettingsEditor.putBoolean("enable", true);
			clockSettingsEditor.putInt("snoozetime", 10);
			clockSettingsEditor.commit();
		}

		/* 各种初始化 */
		alarmcreator = new AlarmCreator(this);

		hour = clockSettings.getInt("hour", 0);
		minutes = clockSettings.getInt("minutes", 0);
		TextView textHour = (TextView) findViewById(R.id.texthour);
		textHour.setText(Utils.timeFormat(hour));
		textHour.setOnClickListener(this);
		TextView textMinutes = (TextView) findViewById(R.id.textminutes);
		textMinutes.setText(Utils.timeFormat(minutes));
		textMinutes.setOnClickListener(this);

		updateWeekdaysColor(); // 更新星期字体颜色

		/* 实验broadcast用 */
//		TextView txTitle = (TextView) findViewById(R.id.texttitle);
//		txTitle.setOnClickListener(this);

		/* Buttons Register */
		btn_Week_Sun = (Button) findViewById(R.id.btn_sun);
		btn_Week_Sun.setOnClickListener(this);
		btn_Week_Mon = (Button) findViewById(R.id.btn_mon);
		btn_Week_Mon.setOnClickListener(this);
		btn_Week_Tue = (Button) findViewById(R.id.btn_tue);
		btn_Week_Tue.setOnClickListener(this);
		btn_Week_Wed = (Button) findViewById(R.id.btn_wed);
		btn_Week_Wed.setOnClickListener(this);
		btn_Week_Thu = (Button) findViewById(R.id.btn_thu);
		btn_Week_Thu.setOnClickListener(this);
		btn_Week_Fri = (Button) findViewById(R.id.btn_fri);
		btn_Week_Fri.setOnClickListener(this);
		btn_Week_Sat = (Button) findViewById(R.id.btn_sat);
		btn_Week_Sat.setOnClickListener(this);
		btn_setClockOn = (Button) findViewById(R.id.btn_setClockOn);
		btn_setClockOn.setOnClickListener(this);
		btn_setClockOff = (Button) findViewById(R.id.btn_setClockOff);
		btn_setClockOff.setOnClickListener(this);
		btn_tabAnalytics = (Button)findViewById(R.id.btn_tabAnalytics);
		btn_tabAnalytics.setOnClickListener(this);
		btn_tabSettings = (Button)findViewById(R.id.btn_tabSettings);
		btn_tabSettings.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sun:
		case R.id.btn_mon:
		case R.id.btn_tue:
		case R.id.btn_wed:
		case R.id.btn_thu:
		case R.id.btn_fri:
		case R.id.btn_sat: {
			/* 星期按钮动作 */
			TextView clickedButton = (TextView) v;
			int daysofweek = (int) Math.pow(2, v.getId() - R.id.btn_sun);
			int weekdayssetting = clockSettings.getInt("daysofweek", 62);// 62为默认工作日闹铃
			if ((weekdayssetting | daysofweek) == weekdayssetting) {
				clickedButton.setTextColor(R.color.textblue);
			} else {
				clickedButton.setTextColor(Color.WHITE);
			}
			weekdayssetting = weekdayssetting ^ daysofweek;
			clockSettingsEditor.putInt("daysofweek", weekdayssetting).commit();
			break;
		}
		case R.id.texthour:
		case R.id.textminutes: {
			/* 时间TextView动作 */
			View tableWeek = (View) findViewById(R.id.table_week);
			View layoutBtnUp = (View) findViewById(R.id.buttonupgroup);
			View layoutBtnDown = (View) findViewById(R.id.buttondowngroup);
			txHour = (TextView) findViewById(R.id.texthour);
			txMinutes = (TextView) findViewById(R.id.textminutes);
			if (isClockSettingButtonVisable == false) {
				/* 隐藏星期层 */
				tableWeek.postInvalidate();
				tableWeek.setVisibility(View.INVISIBLE);
				/* 显示时间调节按钮 */
				layoutBtnUp.setVisibility(View.VISIBLE);
				layoutBtnDown.setVisibility(View.VISIBLE);
				/* 判断是否有过修改 */
				exhour = hour;
				exminutes = minutes;

				isClockSettingButtonVisable = true;
				Button btnHourUp = (Button) findViewById(R.id.btn_clockhourup);
				Button btnHourDown = (Button) findViewById(R.id.btn_clockhourdown);
				Button btnMinutesUp = (Button) findViewById(R.id.btn_clockminutesup);
				Button btnMinutesDown = (Button) findViewById(R.id.btn_clockminutesdown);
				btnHourUp.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						upHourTime();
					}
				});
				btnHourUp
						.setOnLongClickListener(new View.OnLongClickListener() {

							@Override
							public boolean onLongClick(View v) {
								// TODO Auto-generated method stub
								return false;
							}

						});
				btnHourDown.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						downHourTime();
					}
				});
				btnMinutesUp.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						upMinutesTime();
					}
				});
				btnMinutesDown.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						downMinutesTime();
					}
				});
			} else {
				/* 隐藏时间调节按钮 */
				layoutBtnUp.setVisibility(View.INVISIBLE);
				layoutBtnDown.setVisibility(View.INVISIBLE);
				isClockSettingButtonVisable = false;
				clockSettingsEditor.commit();
				if ((hour != exhour) || (minutes != exminutes)) {
					Toast.makeText(
							this,
							"AlarmTime is update to "
									+ Utils.timeFormat(hour) + ":"
									+ Utils.timeFormat(minutes),
							Toast.LENGTH_LONG).show();
				}
				/* 显示星期层 */
				tableWeek.postInvalidate();
				tableWeek.setVisibility(View.VISIBLE);
			}
			break;
		}
		case R.id.btn_setClockOn: {
			/* 开启闹钟 */
			alarmcreator.createNormalAlarm();
			Toast.makeText(this, "Alarm is set ON", Toast.LENGTH_LONG).show();
			Log.d("socialalarmlog", "MainActivity: set clock on");
			break;
		}
		case R.id.btn_setClockOff: {
			/* 关闭闹钟 */
			alarmcreator.cancelAlarm();
			Toast.makeText(this, "Alarm is set OFF", Toast.LENGTH_LONG).show();
			Log.d("socialalarmlog", "MainActivity: clock cancel");
			break;
		}
			// case R.id.texttitle: {
			// /* 手动发送一个broadcast(实验) */
			// // intent = new Intent(this, AlarmReceiver.class);
			// // intent.putExtra("daysofweek",
			// // clockSettings.getInt("daysofweek", -1));
			// // sendBroadcast(intent);
			// break;
			// }
		case R.id.btn_tabAnalytics: {
			Intent switchTabAnalytics = new Intent(MainActivity.this, AnalyticsActivity.class);
			MainActivity.this.startActivity(switchTabAnalytics);
			break;
		}
		case R.id.btn_tabSettings: {
			Intent switchTabAnalytics = new Intent(MainActivity.this, Settings.class);
			MainActivity.this.startActivity(switchTabAnalytics);
			break;
		}
			// more buttons
		}

	}

	/* 时间调整方法 */
	private void upHourTime() {
		clockSettingsEditor.putInt("hour", hour < 23 ? (++hour) : (hour = 0));
		txHour.setText(Utils.timeFormat(hour));
	}

	private void downHourTime() {
		clockSettingsEditor.putInt("hour", hour > 0 ? (--hour) : (hour = 23));
		txHour.setText(Utils.timeFormat(hour));
	}

	private void upMinutesTime() {
		clockSettingsEditor.putInt("minutes", minutes < 59 ? (++minutes)
				: (minutes = 0));
		txMinutes.setText(Utils.timeFormat(minutes));
	}

	private void downMinutesTime() {
		clockSettingsEditor.putInt("minutes", minutes > 0 ? (--minutes)
				: (minutes = 59));
		txMinutes.setText(Utils.timeFormat(minutes));
	}

	/* 更新星期字体颜色 */
	private void updateWeekdaysColor() {
		int weekdayssetting = clockSettings.getInt("daysofweek", 31);
		for (int days = 0; days < 7; ++days) {
			Button daysButton = (Button) findViewById(R.id.btn_sun + days);
			if ((weekdayssetting | ((int) Math.pow(2, days))) == weekdayssetting) {
				daysButton.setTextColor(Color.WHITE);
			} else {
				daysButton.setTextColor(R.color.textblue);
			}
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO preference改变时的动作
	}

}