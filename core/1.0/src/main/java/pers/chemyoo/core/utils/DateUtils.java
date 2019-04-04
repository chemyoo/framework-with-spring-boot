package pers.chemyoo.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
public class DateUtils {
	
	public static final String YEAR_TIME_FORMAT = "yyyy";
	public static final String LONG_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SHORT_TIME_FORMAT = "yyyy-MM-dd";
	public static final String MONTH_TIME_FORMAT = "yyyy-MM";
	public static final String MONTH_TIME_FORMAT2 = "yyyy.MM";
	public static final String MIDDLE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String HOMI_TIME_FORMAT  = "HH:mm";
	
	public static final long MILLSECOND_OF_DAY = TimeUnit.DAYS.toMillis(1);
	
	// 时区时间偏移量
	public static final int OFFSET = Calendar.getInstance().getTimeZone().getRawOffset();
	
	private DateUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	public static String convertDateToString(Date date, String pattern){
		if(date == null){
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	public static Date convertStringToDate(String date,String pattern){
		if(StringUtils.isBlank(date)){
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date getCurrentTime() {
		return Calendar.getInstance().getTime();
	}
	
	public static boolean validateYear(String year) {
		if(StringUtils.isBlank(year)) {
			return false;
		}
		return Pattern.matches("^+?\\d{4}$", year);
	}
	
	/**
	 * 日期字符串计算
	 * @param dateTime
	 * @param value
	 * @return
	 */
	public static String dateAdd(String dateTime, int value) {
		String format = getDateFormat(dateTime, dateTime);
		return dateAdd(convertStringToDate(dateTime, format), value, format);
	}
	
	/**
	 * 日期字符串计算
	 * @param dateTime
	 * @param value
	 * @return
	 */
	public static Date dateAdd(Date dateTime,int calendarField, int value) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		calendar.add(calendarField, value);
		return calendar.getTime();
	}
	
	public static Date getNextDay() {
		return dateAdd(getDateWithZeroTime(),Calendar.DAY_OF_YEAR, 1);
	}
	
	/**
	 * 日期字符串计算
	 * @param dateTime
	 * @param value
	 * @return
	 */
	public static String dateAdd(Date dateTime, int value, String dataFormat) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(dateTime);
		if(MONTH_TIME_FORMAT.equals(dataFormat)) {
			gregorianCalendar.add(GregorianCalendar.MONTH, value);
		} else if(YEAR_TIME_FORMAT.equals(dataFormat)) {
			gregorianCalendar.add(GregorianCalendar.YEAR, value);
		} else {
			gregorianCalendar.add(GregorianCalendar.DAY_OF_YEAR, value);
		}
		return convertDateToString(gregorianCalendar.getTime(), dataFormat);
	}
	
	/**
	 * 日期字符串计算
	 * @param dateTime
	 * @param value
	 * @return
	 */
	public static String dateAddHours(Date dateTime, int value, String dataFormat) {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(dateTime);
		gregorianCalendar.add(GregorianCalendar.HOUR_OF_DAY, value);
		return convertDateToString(gregorianCalendar.getTime(), dataFormat);
	}
	
	public static boolean validateMonth(String month) {
		if(StringUtils.isBlank(month)) {
			return false;
		}
		return Pattern.matches("^[1-9]\\d{3}-(0[1-9]|1[0-2])$", month);
	}
	
	public static boolean validateDay(String day) {
		if(StringUtils.isBlank(day)) {
			return false;
		}
		return Pattern.matches("^[1-9]\\d{3}[-年](0[1-9]|1[0-2])[-月]((0[1-9]|[1-2][0-9]|3[0-1])日?)$", day);
	}
	
	
	/**
	 * 计算年龄，精确到日，如2018-08-03 生日到 2018-08-03 才为 1周岁，小于2018-08-03都为 0周岁。
	 * @param date2 出生日期
	 * @return
	 */
	public static int getAge(Date date2) {
		GregorianCalendar currentTime = new GregorianCalendar();
		GregorianCalendar birthTime = new GregorianCalendar();
		if(date2 != null)
			birthTime.setTime(date2);
		if(birthTime.after(currentTime)) {
			return 0;
		}
		int y1 = currentTime.get(Calendar.YEAR);
		int y2 = birthTime.get(Calendar.YEAR);
		int age = y1 - y2;
		if(age > 0) {
			if(currentTime.get(Calendar.MONTH) < birthTime.get(Calendar.MONTH))
				age -= 1;
			else if(currentTime.get(Calendar.MONTH) == birthTime.get(Calendar.MONTH) && 
					currentTime.get(Calendar.DAY_OF_MONTH) < birthTime.get(Calendar.DAY_OF_MONTH)){
				age -= 1;
			}
		} 
		return age;
	}
	
	public static String getDateFormat(String startTime, String endTime) {
		String format = DateUtils.SHORT_TIME_FORMAT;
		if(DateUtils.validateYear(startTime) && DateUtils.validateYear(endTime)) {
			format = DateUtils.YEAR_TIME_FORMAT;
		} else if(DateUtils.validateMonth(startTime) && DateUtils.validateMonth(endTime)){
			format = DateUtils.MONTH_TIME_FORMAT;
		} else if(isContains(startTime)){
			format = "yyyy年MM月dd日";
		}
		return format;
	}
	
	public static boolean isContains(String time) {
		String[] hanzi= {"年","月", "日"};
		for(String str : hanzi) {
			if(!time.contains(str)) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 将字符串转换成年、月、日的开始或结束时间
	 * @param type 1：一天/年的开始时间，2：一天/年的结束时间
	 * @return
	 */
	public static Date getTime(String time, int type) {
		Validate.notBlank(time, "The parameter time must not be blank.");
		String dateFormat = DateUtils.getDateFormat(time, time);
		if(type == 1) {
			return DateUtils.convertStringToDate(time, dateFormat);
		} else {
			Date date = DateUtils.convertStringToDate(time, dateFormat);
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(getDateField(dateFormat), 1);
			calendar.add(Calendar.MILLISECOND, -1);
			return calendar.getTime();
		}
	}
	
	/**
	 * 根据日期格式获取年、月、日的字段值
	 * @param dateFormat
	 * @return
	 */
	public static int getDateField(String dateFormat) {
		if(MONTH_TIME_FORMAT.equals(dateFormat)) {
			return Calendar.MONTH;
		} else if(YEAR_TIME_FORMAT.equals(dateFormat)) {
			return Calendar.YEAR;
		} else {
			return Calendar.DAY_OF_YEAR;
		}
	}

	/** 一天中最早的时间，即0点0分0秒0毫秒*/
	public static Date getDateWithZeroTime() {
		Calendar calendar = Calendar.getInstance();
		long oneDay = TimeUnit.DAYS.toMillis(1);
		long days = calendar.getTimeInMillis() / oneDay;
		calendar.setTimeInMillis(days * oneDay);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}
	
	/** 一天中最晚的时间，即23点59分59秒999毫秒*/
	public static Date getDateAtEnd() {
		return dateAdd(getNextDay(), Calendar.MILLISECOND, -1);
	}
	
	
	/** 判断是不是今天的日期 */
	public static boolean isToday(Date date) {
		return !(date.before(getDateWithZeroTime()) || date.after(getDateAtEnd()));
	}
	
	/**
	 * 获取当前是时间是上午/下午
	 * @return 上午/下午
	 */
	public static String getTimePriod() {
		return String.format("%tp", getCurrentTime());
	}
	
	/**
	 * 获取当前时间所在的预约时间段
	 * @return
	 */
	public static String getAppointTime() {
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		String pattern = "HH:00";
		if((hour > 8 && hour < 12) || (hour > 12 && hour < 15)) {
			return DateUtils.convertDateToString(now.getTime(), pattern) + "-" + dateAddHours(now.getTime(), 1, pattern);
		} else if(hour == 15 || (hour == 16 && minute < 31)) {
			return "15:00-16:30";
		}
		return null;
	}
	
	/**
	 * 获取当前日期是周几
	 * @param date 待处理时间
	 * @return 周一~周日中的某一个
	 */
	public static String getWeek(Date date){
		return String.format("%ta", date).replace("星期", "周");
	}
	
	
	/**
	 * 依据预约时间的编码规则，将编码值转换成日期
	 * @param code
	 * @return
	 */
	public static Date getDateByCoding(String code) {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		switch (code.trim()) {
		case "1":
			cal.set(Calendar.HOUR_OF_DAY, 10);
			break;
		case "2":
			cal.set(Calendar.HOUR_OF_DAY, 11);
			break;
		case "3":
			cal.set(Calendar.HOUR_OF_DAY, 12);
			break;
		case "4":
			cal.set(Calendar.HOUR_OF_DAY, 14);
			break;
		case "5":
			cal.set(Calendar.HOUR_OF_DAY, 15);
			break;
		case "6":
			cal.set(Calendar.HOUR_OF_DAY, 16);
			cal.set(Calendar.MINUTE, 30);
			break;
		default:
			cal.set(Calendar.HOUR_OF_DAY, 23);
		}
		return cal.getTime();
	}
	
	/**
	 * 获取两个日期间隔天数
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public static int getDayNum(Date startTime, Date endTime) {
		if(startTime == null || endTime == null) {
			return 0;
		}
		return Math.abs((int)((endTime.getTime() - startTime.getTime()) / 60 / 1000));
	}
	
	/**
	 * 判断当前时间是否超过预约时间
	 * @param now 当前时间
	 * @param appointPeriod 预约时间段
	 * @return
	 */
	public static boolean passTakTicketTime(Date now, String appointPeriod) {
		if(now == null || appointPeriod == null) {
			return false;
		}
		String day = convertDateToString(now, SHORT_TIME_FORMAT);
		Date appointEndTime = convertStringToDate(day + " " + appointPeriod.split("-")[1], MIDDLE_TIME_FORMAT);
		if(appointEndTime != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(appointEndTime);
			calendar.add(Calendar.MINUTE, -10);
			if(now.getTime() > calendar.getTimeInMillis()) {
				return true;
			}
		}
		return false;
		
	}
}
