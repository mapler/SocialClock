package cn.socialclock.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by mapler on 2015/03/11.
 * Format between String and Calendar
 */
public class DatetimeFormatter {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            DATETIME_FORMAT, Locale.getDefault());

    /**
     * Format Calendar object to String
     * @param calendar Calendar object
     * @return datetime string
     */
    public static String calendarToString(Calendar calendar) {
        try {
            return dateFormat.format(calendar.getTime());
        } catch (NullPointerException e) {
            return null;
        }

    }

    /**
     * create Calendar object by String
     * @param datetimeString String
     * @return Calendar object or null
     */
    public static Calendar stringToCalendar(String datetimeString) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(datetimeString));
            return calendar;
        } catch (ParseException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }
}
