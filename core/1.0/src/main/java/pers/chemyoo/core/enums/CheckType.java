package pers.chemyoo.core.enums;

public enum CheckType
{
	/**
	 * 默认类型
	 */
	NONE(""),
	/**
	 * 不能为null
	 */
	NOT_NULL(""),
	/**
	 * 不能为空串包括且不能是空格
	 */
	NOT_BLANK(""),
	/**
	 * 不能为空串
	 */
	NOT_EMPTY(""),
	/**
	 * 身份证号码
	 */
	ID_CARD(""),
	/**
	 * 电话号码
	 */
	TEL(""),
	/**
	 * 电话号码
	 */
	PHONE_NUMBER(""),
	/**
	 * 电子邮件
	 */
	EMAIL("");
	
	private String regexp;
	
	CheckType(String regexp){
		this.regexp = regexp;
	}
	
	public String getRegexp() {
		return this.regexp;
	}
	
}
