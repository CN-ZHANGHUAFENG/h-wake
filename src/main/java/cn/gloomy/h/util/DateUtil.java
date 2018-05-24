package cn.gloomy.h.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtil {
	public static final String FULL_PATTERN = "yyyyMMddHHmmss";

	public static String defaultFormat(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(FULL_PATTERN);
		return format.format(date);
	}
	
	public static String format(Date date,String pattern) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	public static Date defaultToDate(String dateStr) {
		SimpleDateFormat format = new SimpleDateFormat(FULL_PATTERN);
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date toAdd(Date date,int add,TimeUnit unit) {
		long timeInMillis = date.getTime();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeInMillis);
		timeInMillis += unit.toMillis(add);
		return new Date(timeInMillis);
	}
}
