package cn.socialclock.ui;

import cn.socialclock.R;
import cn.socialclock.model.AlarmCreator;
import cn.socialclock.model.ClockSettings;
import cn.socialclock.utils.ConstantData;
import cn.socialclock.utils.SocialClockLogger;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * @author mapler
 * Main UI
 */
public class MainActivity extends Activity implements OnClickListener {

    private TextView textHour;
    private TextView textMinute;

    private ClockSettings clockSettings;

    // to show the time set mode
    private boolean isClockSettingModeOn;

    private int hour;
    private int minute;
    private int ex_hour = 0;
    private int ex_minute = 0;

    private AlarmCreator alarmCreator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /** Called when the activity is first created. */
        SocialClockLogger.log("MainActivity: onCreate start");

        super.onCreate(savedInstanceState);

        // get setting preference
        clockSettings = new ClockSettings(this);

        // alarm creator init
        alarmCreator = new AlarmCreator(this);

        // build ui
        buildInterface();
    }

    private void buildInterface() {

        // set screen orientation portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // set title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set view layout
        setContentView(R.layout.main);

        // build clock dial interface
        buildClockDialInterface();

        // weekday init
        buildWeekdaySettingInterface();

        // build clock on/off init
        buildOnOffButton();

        // build tab menu init
        buildTabMenuButton();
    }

    private void buildClockDialInterface() {

        // hour init
        hour = clockSettings.getHour();
        textHour = (TextView) findViewById(R.id.texthour);
        textHour.setText(String.format("%02d", hour));
        textHour.setOnClickListener(this);

        // minute init
        minute = clockSettings.getMinute();
        textMinute = (TextView) findViewById(R.id.textminute);
        textMinute.setText(String.format("%02d", minute));
        textMinute.setOnClickListener(this);

        // init setting mode, off
        isClockSettingModeOn = false;
    }

    private void buildWeekdaySettingInterface() {
        /** use binary value to store and set weekday on/off */
        for (int weekdayId = 0; weekdayId < Calendar.DAY_OF_WEEK; ++weekdayId) {
            Button weekDaysButton = (Button) findViewById(R.id.btn_sun + weekdayId);
            if (clockSettings.isWeekdayEnable(weekdayId)) {
                weekDaysButton.setTextColor(Color.WHITE);
            } else {
                weekDaysButton.setTextColor(getResources().getColor(R.color.textblue));
            }
        }

        /* buttons register */
        Button btn_Week_Sun = (Button) findViewById(R.id.btn_sun);
        btn_Week_Sun.setOnClickListener(this);
        Button btn_Week_Mon = (Button) findViewById(R.id.btn_mon);
        btn_Week_Mon.setOnClickListener(this);
        Button btn_Week_Tue = (Button) findViewById(R.id.btn_tue);
        btn_Week_Tue.setOnClickListener(this);
        Button btn_Week_Wed = (Button) findViewById(R.id.btn_wed);
        btn_Week_Wed.setOnClickListener(this);
        Button btn_Week_Thu = (Button) findViewById(R.id.btn_thu);
        btn_Week_Thu.setOnClickListener(this);
        Button btn_Week_Fri = (Button) findViewById(R.id.btn_fri);
        btn_Week_Fri.setOnClickListener(this);
        Button btn_Week_Sat = (Button) findViewById(R.id.btn_sat);
        btn_Week_Sat.setOnClickListener(this);
    }

    private void buildOnOffButton() {
        /** on/off button init */
        Button btn_setClockOn = (Button) findViewById(R.id.btn_setClockOn);
        btn_setClockOn.setOnClickListener(this);
        Button btn_setClockOff = (Button) findViewById(R.id.btn_setClockOff);
        btn_setClockOff.setOnClickListener(this);
    }

    private void buildTabMenuButton() {
        /** tab menu init */
        Button btn_tabAnalytics = (Button)findViewById(R.id.btn_tabAnalytics);
        btn_tabAnalytics.setOnClickListener(this);
        Button btn_tabSettings = (Button)findViewById(R.id.btn_tabSettings);
        btn_tabSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /** handle all clickable elements' click events */
        switch (v.getId()) {
            case R.id.btn_sun:
            case R.id.btn_mon:
            case R.id.btn_tue:
            case R.id.btn_wed:
            case R.id.btn_thu:
            case R.id.btn_fri:
            case R.id.btn_sat: {
                onClickWeekdays(v);
                break;
            }
            case R.id.texthour:
            case R.id.textminute: {
                onClickClickDial();
                break;
            }
            case R.id.btn_setClockOn: {
                onClickClockOn();
                break;
            }
            case R.id.btn_setClockOff: {
                onCLickClockOff();
                break;
            }
            case R.id.btn_clockhourup: {
                upHourTime();
                break;
            }
            case R.id.btn_clockhourdown: {
                downHourTime();
                break;
            }
            case R.id.btn_clockminuteup: {
                upMinuteTime();
                break;
            }
            case R.id.btn_clockminutedown: {
                downMinuteTime();
                break;
            }
            case R.id.btn_tabAnalytics: {
                /* direct to analytics view */
                Intent switchTabAnalytics = new Intent(this, AnalyticsActivity.class);
                startActivity(switchTabAnalytics);
                break;
            }
            case R.id.btn_tabSettings: {
                /* direct to settings view */
                Intent switchTabSettings = new Intent(this, SettingsActivity.class);
                startActivity(switchTabSettings);
                break;
            }
            default: {
                break;
            }
        }
    }

    private void onClickWeekdays(View v) {
        /** weekday settings with binary compare */
        TextView clickedButton = (TextView) v;
        int clickedWeekdayId = v.getId() - R.id.btn_sun;
        if (clockSettings.isWeekdayEnable(clickedWeekdayId)) {
            clickedButton.setTextColor(getResources().getColor(R.color.textblue));
        } else {
            clickedButton.setTextColor(Color.WHITE);
        }
        clockSettings.switchWeekdayEnable(clickedWeekdayId);
    }

    private void onClickClickDial() {
        /**
         * Handle dial onClick.
         * Switch time setting mode on/off.
         */

        View tableWeek = findViewById(R.id.table_week);
        View layoutBtnUp = findViewById(R.id.buttonupgroup);
        View layoutBtnDown = findViewById(R.id.buttondowngroup);

        textHour = (TextView) findViewById(R.id.texthour);
        textMinute = (TextView) findViewById(R.id.textminute);
        if (!isClockSettingModeOn) {
            // hide weekday setting
            tableWeek.postInvalidate();
            tableWeek.setVisibility(View.INVISIBLE);
            // show time settings
            layoutBtnUp.setVisibility(View.VISIBLE);
            layoutBtnDown.setVisibility(View.VISIBLE);
            // saved the past value for checking if is modified
            ex_hour = hour;
            ex_minute = minute;

            /* buttons register */
            Button btn_clockHourUp = (Button)findViewById(R.id.btn_clockhourup);
            btn_clockHourUp.setOnClickListener(this);
            Button btn_clockHourDown = (Button)findViewById(R.id.btn_clockhourdown);
            btn_clockHourDown.setOnClickListener(this);
            Button btn_clockMinuteUp = (Button)findViewById(R.id.btn_clockminuteup);
            btn_clockMinuteUp.setOnClickListener(this);
            Button btn_clockMinuteDown = (Button)findViewById(R.id.btn_clockminutedown);
            btn_clockMinuteDown.setOnClickListener(this);

            // set setting mode on
            isClockSettingModeOn = true;
        } else {
            // update settings
            clockSettings.setHours(hour);
            clockSettings.setMinute(minute);
            if ((hour != ex_hour) || (minute != ex_minute)) {
                String message = "AlarmTime is update to "
                        + String.format("%02d", hour) + ":"
                        + String.format("%02d", minute);
                Toast.makeText(this, message,
                        Toast.LENGTH_SHORT).show();

                // start new alarm
                alarmCreator.createNormalAlarm();

                // cancel the snooze notification if exist
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(ConstantData.AlarmType.ALARM_SNOOZE);
            }

            // hide time settings
            layoutBtnUp.setVisibility(View.INVISIBLE);
            layoutBtnDown.setVisibility(View.INVISIBLE);

            // show weekday settings
            tableWeek.postInvalidate();
            tableWeek.setVisibility(View.VISIBLE);

            // return from setting mode
            isClockSettingModeOn = false;
        }
    }

    private void onCLickClockOff() {
        /** set clock off */
        alarmCreator.cancelAlarm();
        Toast.makeText(this, "Alarm is set OFF", Toast.LENGTH_SHORT).show();
        SocialClockLogger.log("MainActivity: clock cancel");
    }

    private void onClickClockOn() {
        /** set clock on */
        alarmCreator.createNormalAlarm();
        Toast.makeText(this, "Alarm is set ON", Toast.LENGTH_SHORT).show();
        SocialClockLogger.log("MainActivity: set clock on");
    }

    private void upHourTime() {
        /** handle adjust hour up */
        hour = hour < 23 ? (++hour) : 0;
        textHour.setText(String.format("%02d", hour));
    }

    private void downHourTime() {
        /** handle adjust hour down */
        hour = hour > 0 ? (--hour) : 23;
        textHour.setText(String.format("%02d", hour));
    }

    private void upMinuteTime() {
        /** handle adjust minute up */
        minute = minute < 59 ? (++minute) : 0;
        textMinute.setText(String.format("%02d", minute));
    }

    private void downMinuteTime() {
        /** handle adjust minute down */
        minute = minute > 0 ? (--minute) : 59;
        textMinute.setText(String.format("%02d", minute));
    }
}