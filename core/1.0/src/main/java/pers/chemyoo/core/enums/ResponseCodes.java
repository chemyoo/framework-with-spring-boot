package pers.chemyoo.core.enums;
/** 
 * @author Author : jianqing.liu
 * @version version : created time：2019年2月25日 下午4:17:41 
 * @since since from 2019年2月25日 下午4:17:41 to now.
 * @description class description
 */
public enum ResponseCodes {
	
	/**
	 * code : 0<br/>
	 * description : 请求成功
	 */
	SUCCESS(0, "成功"),
	/**
	 * code : 1<br/>
	 * description : 服务器内部错误
	 */
	SERVICE_INTERNAL_ERROR(1, "服务器内部错误"),
	/**
	 * code : 2<br/>
	 * description : 参数错误
	 */
	PARAMTER_ERROR(2, "参数错误"),
	/**
	 * code : 101<br/>
	 * description : 服务禁用/拒绝访问
	 */
	SERVICE_FORBIDDEN(101, "服务禁用/拒绝访问"),
	/**
	 * code : 201<br/>
	 * description : 无权限
	 */
	NO_AUTHORIZATION(201, "无权限"),
	
	/**
	 * code : 3<br/>
	 * description : 功能正在开发上线中
	 */
	DEVELOPING(3, "功能正在开发上线中..."),
	/**
	 * code : 4<br/>
	 * description : 唯一识别信息已被使用
	 */
	DUPLICATE(4, "唯一识别信息已被使用..."),
	/**
	 * code : 5<br/>
	 * description : 无号源
	 */
	NORESOURCE(5, "无号源...");
	
	/** 结果码 */
	private int resultCode;
	/** 结果码描述 */
	private String description;
	/** 结果码名称 */
	public static final String CODE = "code";
	/** 错误描述信息 */
	public static final String DESC = "desc";

	ResponseCodes(int resultCode, String description) {
		this.resultCode = resultCode;
		this.description = description;
	}

	/**
	 * 获取结果码
	 * 
	 * @return the resultCode
	 */
	public int getResultCode() {
		return resultCode;
	}

	/**
	 * 获取结果码描述
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

}
