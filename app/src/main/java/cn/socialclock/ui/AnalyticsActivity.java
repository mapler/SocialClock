package cn.socialclock.ui;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import cn.socialclock.R;
import cn.socialclock.db.AlarmTransaction;
import cn.socialclock.utils.StyleUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AnalyticsActivity extends Activity implements OnClickListener {
	public static final int DISTANCE = 12;
	private Button btnTabMain;
	private Button btnTabSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("socialalarmlog", "AnalyticsActivity: onCreate");

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.analytics);

		btnTabMain = (Button) findViewById(R.id.btn_tabMain);
		btnTabMain.setOnClickListener(this);
		btnTabSettings = (Button) findViewById(R.id.btn_tabSettings);
		btnTabSettings.setOnClickListener(this);
		TextView tvTitle = (TextView) findViewById(R.id.tv_analytitle);
		StyleUtils.toBold(tvTitle);
		RelativeLayout rlColumnBoard = (RelativeLayout) findViewById(R.id.column_board);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int widthPixels = dm.widthPixels;
		// int heightPixels= dm.heightPixels;
		float density = dm.density; // 比例换算
		float screenWidth = widthPixels * density; // 相对屏宽
		// float screenHeight = heightPixels * density; //相对屏高
		float widthcolumn = (float) ((widthPixels - 40 - DISTANCE) / 7 - DISTANCE); // 固定直方柱宽

		ArrayList<AlarmTransaction> alistAlarmTransaction = new ArrayList<AlarmTransaction>(); // 一周闹铃数据

		/* 获取一周起床事件数据 */
		/* 模拟 */
		Date nowdate = new Date(System.currentTimeMillis());
		final Time now = new java.sql.Time(System.currentTimeMillis());
		int flag = 1;
		for (int i = 0; i < 7; i++) {
			flag *= -1;
			AlarmTransaction transaction = new AlarmTransaction(
					11112611 + i * 100, 1, new Date(nowdate.getTime() + i * 24
							* 3600 * 1000), now, new Time(
							System.currentTimeMillis() + i * 100000 * flag), i,
					1);
			alistAlarmTransaction.add(transaction);
		}

		/* 获取最长时间 */
		long maxSnoozeTime = 0;
		long maxEarlyTime = 0;
		for (int index = 0; index < alistAlarmTransaction.size(); index++) {
			long snoozetime = alistAlarmTransaction.get(index).getGetup_time()
					.getTime()
					- alistAlarmTransaction.get(index).getAlarm_time()
							.getTime();
			if (maxSnoozeTime < snoozetime) {
				maxSnoozeTime = snoozetime;
			}
			if (maxEarlyTime > snoozetime) {
				maxEarlyTime = snoozetime;
			}
		}
		long maxTime = maxSnoozeTime - maxEarlyTime;

		/* 早起画板 */
		RelativeLayout.LayoutParams lpEarlyBoard = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lpEarlyBoard.addRule(RelativeLayout.BELOW, R.id.title_line);
		RelativeLayout rlEarlyBoard = new RelativeLayout(this);
		rlEarlyBoard.setId(R.id.title_line + 100);
		rlEarlyBoard.setMinimumHeight((int) ((int) (((160 - 60)
				* (-maxEarlyTime) / maxTime) * density)));
		rlEarlyBoard.setMinimumWidth((int) (screenWidth));
		// rlEarlyBoard.setBackgroundColor(Color.DKGRAY);
		rlEarlyBoard.setGravity(Gravity.BOTTOM);
		rlColumnBoard.addView(rlEarlyBoard, lpEarlyBoard);

		/* 中横线 */
		RelativeLayout.LayoutParams lpMidLine = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		// lpMidLine.addRule(RelativeLayout.BELOW, llEarlyBoard.getId());
		TextView vMidLine = new TextView(this);
		vMidLine.setId(rlEarlyBoard.getId() + 100);
		vMidLine.setHeight(1);
		vMidLine.setWidth((int) (screenWidth));
		vMidLine.setBackgroundColor(Color.BLACK);
		rlEarlyBoard.addView(vMidLine, lpMidLine);

		/* 贪睡画板 */
		RelativeLayout.LayoutParams lpSnoozeBoard = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lpSnoozeBoard.addRule(RelativeLayout.BELOW, rlEarlyBoard.getId());
		RelativeLayout rlSnoozeBoard = new RelativeLayout(this);
		rlSnoozeBoard.setId(rlEarlyBoard.getId() + 1);
		rlSnoozeBoard.setMinimumHeight((int) ((int) (((160 - 60)
				* maxSnoozeTime / maxTime) * density)));
		rlSnoozeBoard.setMinimumWidth((int) (screenWidth));
		// rlSnoozeBoard.setBackgroundColor(Color.GRAY);
		rlColumnBoard.addView(rlSnoozeBoard, lpSnoozeBoard);

		int id = vMidLine.getId() + 1;
		for (int index = 0; index < alistAlarmTransaction.size(); index++) {

			String date = ""
					+ alistAlarmTransaction.get(index).getAlarm_date()
							.getMonth()
					+ 1
					+ "."
					+ alistAlarmTransaction.get(index).getAlarm_date()
							.getDate();

			long snoozetime = alistAlarmTransaction.get(index).getGetup_time()
					.getTime()
					- alistAlarmTransaction.get(index).getAlarm_time()
							.getTime();

			RelativeLayout.LayoutParams lpEarly = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams lpSnooze = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);

			lpEarly.addRule(RelativeLayout.ABOVE, vMidLine.getId());
			if (index != 0) {
				lpEarly.addRule(RelativeLayout.RIGHT_OF, id - 1);
			}
			if (index != 0) {
				lpSnooze.addRule(RelativeLayout.RIGHT_OF, id);
			}

			TextView vText = new TextView(AnalyticsActivity.this);
			TextView vColumn = new TextView(AnalyticsActivity.this);

			long height = (int) ((180 - 60)
					* (((float) (snoozetime > 0 ? snoozetime
							: (-1 * snoozetime))) / ((float) maxTime)) * density);
			if (height < 1 && height >= 0) {
				height = 1;
			}

			vColumn.setHeight((int) height);
			// vColumn.setText("" + snoozetime);

			vText.setTextSize(8);
			vText.setText(date);
			vText.setGravity(Gravity.CENTER);
			vText.setTextColor(Color.BLACK);
			vColumn.setWidth((int) widthcolumn);
			vText.setWidth((int) widthcolumn);
			lpEarly.setMargins(DISTANCE, 0, 0, 0);
			lpSnooze.setMargins(DISTANCE, 0, 0, 0);

			if (snoozetime >= 0) {
				vText.setId(++id);
				vColumn.setId(++id);
				vColumn.setBackgroundDrawable(StyleUtils
						.getShapeGradientRed(AnalyticsActivity.this));
				rlEarlyBoard.addView(vText, lpEarly);
				rlSnoozeBoard.addView(vColumn, lpSnooze);
			} else {
				vColumn.setId(++id);
				vText.setId(++id);
				vColumn.setBackgroundDrawable(StyleUtils
						.getShapeGradientBlue(AnalyticsActivity.this));
				rlSnoozeBoard.addView(vText, lpSnooze);
				rlEarlyBoard.addView(vColumn, lpEarly);
			}

			// ScaleAnimation animCount = new ScaleAnimation(1, 1, 0, 1);
			// animCount.setFillAfter(true);
			// animCount.setDuration(1 * 1000);
			// vColumn.startAnimation(animCount);
            Toast.makeText(AnalyticsActivity.this, "in development", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_tabMain: {
			Intent switchTabMain = new Intent(AnalyticsActivity.this,
					MainActivity.class);
			AnalyticsActivity.this.startActivity(switchTabMain);
			break;
		}
		case R.id.btn_tabSettings: {
			Intent switchTabMain = new Intent(AnalyticsActivity.this,
					SettingsActivity.class);
			AnalyticsActivity.this.startActivity(switchTabMain);
			break;
		}
			// more buttons
		}
	}
}
