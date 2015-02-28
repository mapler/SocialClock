package cn.socialclock.utils;


public class Utils {
	/* 格式化时间 */
	public static String timeFormat(int timevalue) {
		if (timevalue < 10)
			return "0" + timevalue;
		else
			return "" + timevalue;
	}
	
}
