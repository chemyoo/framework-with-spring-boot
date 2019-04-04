package pers.chemyoo.core.enums;

/**
 * 预约时间段
 * @author xianlin.yang
 * @since 2019-03-06
 */
public enum TimePeriods {
	
	PERIOD1(1, "09:00-10:00"),
	PERIOD2(2, "10:00-11:00"),
	PERIOD3(3, "11:00-12:00"),
	PERIOD4(4, "13:00-14:00"),
	PERIOD5(5, "14:00-15:00"),
	PERIOD6(6, "15:00-16:30");
	
	/**
	 * 序号
	 */
	private int num;
	
	/**
	 * 时间段
	 */
	private String timePeriod;
	
	public static int getNum(String timePeriod) {
		for(TimePeriods period : TimePeriods.values()) {
			if(timePeriod.equals(period.timePeriod)) {
				return period.num;
			}
		}
		return 0;
	}
	
	TimePeriods(int num, String timePeriod){
		this.timePeriod = timePeriod;
		this.num = num;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public int getNum() {
		return num;
	}

}
