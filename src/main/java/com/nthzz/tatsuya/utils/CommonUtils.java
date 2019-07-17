package com.nthzz.tatsuya.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by robert on 06/09/2017.
 */
public class CommonUtils {

	public static boolean isStringNull(String object) {
		boolean isNull = false;
		if (object == null || object.isEmpty()) {
			isNull = true;
		}
		return isNull;
	}

	public static boolean isNull(Object object) {
		boolean isNull = true;
		if (object != null) {
			isNull = false;
		}
		return isNull;
	}

	public static boolean isNull(Object... objects) {
		boolean isNull = true;
		if (objects == null || objects.length == 0) return isNull;
		for (Object o : objects) {
			if (o != null) {
				isNull = false;
				break;
			}
		}
		return isNull;
	}

	public static boolean isAllNotNull(Object... objects) {
		if (objects == null || objects.length == 0) return false;
		boolean isAllNotNull = true;
		for (Object o : objects) {
			if (o == null) {
				isAllNotNull = false;
				break;
			}
		}
		return isAllNotNull;
	}

	public static String removePageLimit(String requestUri) {
		String pagePattern = "&page=\\d+";
		String limitPattern = "&limit=\\d+";

		String output = requestUri.replaceAll(pagePattern, "").replaceAll(limitPattern, "");
		return output;
	}

	public static Double div(double d1, double d2, int numAfterPoint) {
		return d2 == 0 ? 0.0 : (double) Math.round(d1 * Math.pow(10, numAfterPoint) / d2) / Math.pow(10, numAfterPoint);
	}

	public static Long divRound(double d1, double d2) {
		return d2 == 0 ? 0L : Math.round(d1 / d2);
	}

	public static Timestamp parseTime(long timeInMillis) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(timeInMillis);
		String t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(c.getTime());
		return Timestamp.valueOf(t);
	}
}
