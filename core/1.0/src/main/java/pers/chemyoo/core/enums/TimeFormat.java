package pers.chemyoo.core.enums;
/** 
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月11日 下午4:02:17 
 * @since since from 2019年3月11日 下午4:02:17 to now.
 * @description class description
 */
public enum TimeFormat {
	/**
	 * yy-M-d
	 */
	SHORT("yy-M-d"),
	/**
	 * yyyy-MM-dd
	 */
	LONG("yyyy-MM-dd"),
	/**
	 * yyyy-MM-dd HH:mm:ss.SSS
	 */
	FULL("yyyy-MM-dd HH:mm:ss.SSS"),
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	DATETIME("yyyy-MM-dd HH:mm:ss"),
	/**
	 * yyyy年MM月dd日 HH:mm:ss
	 */
	CN_DATETIME("yyyy年MM月dd日 HH:mm:ss"),
	/**
	 * yyyy年MM月dd日 a
	 */
	CN_DATETIME_APM("yyyy年MM月dd日 a"),
	/**
	 * yyyy年MM月dd日
	 */
	CN_DATE("yyyy年MM月dd日"),
	/**
	 * yyyy年MM月dd日 HH:mm
	 */
	CN_DATETIME_NO_SEC("yyyy年MM月dd日 HH:mm");
	
	TimeFormat(String pattern) {
		this.pattern = pattern;
	}
	
	private String pattern;
	
	public String getPattern() {
		return pattern;
	}
	
}
