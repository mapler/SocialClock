package cn.socialclock.model;

import android.content.Context;
import android.content.SharedPreferences;

import cn.socialclock.R;

/**
 * Created by mapler on 2015/03/06.
 * Handle the Preferences.
 */

public class ClockSettings {

    private SharedPreferences clockSettingsPreferences;
    private SharedPreferences.Editor clockSettingsEditor;

    private int defaultHour;
    private int defaultMinutes;
    private int defaultWeekdayFlag;
    private int defaultSnoozeTime;
    private boolean defaultIsEnable;

    public final String KEY_HOUR = "hour";
    public final String KEY_MINUTE = "minute";
    public final String KEY_WEEK_DAY_FLAG = "weekday_flag";
    public final String KEY_IS_ENABLE = "is_enable";
    public final String KEY_SNOOZE_TIME = "snooze_time";

    public ClockSettings(Context context) {
        /** Init a ClockSettings with a context */
        this.defaultHour = context.getResources().getInteger(R.integer.hour);
        this.defaultMinutes = context.getResources().getInteger(R.integer.minutes);
        this.defaultWeekdayFlag = context.getResources().getInteger(R.integer.weekdayFlag);
        this.defaultSnoozeTime = context.getResources().getInteger(R.integer.snoozeTime);
        this.defaultIsEnable = context.getResources().getBoolean(R.bool.isEnable);

        this.clockSettingsPreferences = context.getSharedPreferences("ClockSettings", Context.MODE_PRIVATE);
        this.clockSettingsEditor = this.clockSettingsPreferences.edit();
    }

    public int getHour() {
        /** Get hour(int) from preference */
        return clockSettingsPreferences.getInt(KEY_HOUR, defaultHour);
    }

    public ClockSettings setHours(int hour) {
        /** Set hour(int) to preference */
        clockSettingsEditor.putInt(KEY_HOUR, hour);
        clockSettingsEditor.commit();
        return this;
    }

    public int getMinute() {
        /** Get minute(int) from preference */
        return clockSettingsPreferences.getInt(KEY_MINUTE, defaultMinutes);
    }

    public ClockSettings setMinute(int minute) {
        /** Set minute(int) to preference */
        clockSettingsEditor.putInt(KEY_MINUTE, minute);
        clockSettingsEditor.commit();
        return this;
    }

    private int getWeekdayFlag() {
        /** Get week day flag (int) from preference */
        // TODO, change weekDayFlag to an object with compare method
        return clockSettingsPreferences.getInt(KEY_WEEK_DAY_FLAG, defaultWeekdayFlag);
    }

    private ClockSettings setWeekdayFlag(int weekdayFlag) {
        /** Set week day flag (int) to preference */
        clockSettingsEditor.putInt(KEY_WEEK_DAY_FLAG, weekdayFlag);
        clockSettingsEditor.commit();
        return this;
    }

    public boolean isWeekdayEnable(int weekdayId) {
        /** check if weekday is enable with bitwise compare */
        int binaryDayId = (int) Math.pow(2, weekdayId);
        int weekDayFlag = getWeekdayFlag();
        return (weekDayFlag | binaryDayId) == weekDayFlag;
    }

    public void switchWeekdayEnable(int weekdayId) {
        /** switch weekday enable settings by bitwise XOR */
        int binaryDayId = (int) Math.pow(2, weekdayId);
        int weekDayFlag = getWeekdayFlag();
        weekDayFlag = weekDayFlag ^ binaryDayId;
        setWeekdayFlag(weekDayFlag);
    }

    public int getSnoozeTime() {
        /** Get snooze time (int) from preference */
        return clockSettingsPreferences.getInt(KEY_SNOOZE_TIME, defaultSnoozeTime);
    }

    public ClockSettings setSnoozeTime(int snoozeTime) {
        /** Set snooze time (int) to preference */
        clockSettingsEditor.putInt(KEY_SNOOZE_TIME, snoozeTime);
        clockSettingsEditor.commit();
        return this;
    }

    public boolean getIsEnable() {
        /** Get if enable (boolean) from preference */
        return clockSettingsPreferences.getBoolean(KEY_IS_ENABLE, defaultIsEnable);
    }

    public ClockSettings setIsEnable(int isEnable) {
        /** Set if enable (boolean) to preference */
        clockSettingsEditor.putInt(KEY_IS_ENABLE, isEnable);
        clockSettingsEditor.commit();
        return this;
    }

}
