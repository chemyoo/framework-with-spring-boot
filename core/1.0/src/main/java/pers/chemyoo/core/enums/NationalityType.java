package pers.chemyoo.core.enums;
/** 
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月25日 下午4:26:24 
 * @since since from 2019年3月25日 下午4:26:24 to now.
 * @description 国籍类型
 */
public enum NationalityType {

	/**
	 * {@code CN：中国}
	 */
	CHINESE("中国", "CN"),
	/**
	 * {@code CN_TW：中国台湾}
	 */
	TAIWAN("中国台湾", "TW_CN"),
	/**
	 * {@code CN_HK：中国香港}
	 */
	HONGKONG("中国香港", "HK_CN"),
	/**
	 * {@code CN_MC：中国澳门}
	 */
	MACAO("中国澳门", "MC_CN"),
	/**
	 * {@code OTHER：其他国籍}
	 */
	OTHER("其他国籍", "OTHER");
	
	NationalityType(String typeName, String simName){
		this.typeName = typeName;
		this.simName = simName;
	}
	
	private String typeName;
	
	/**
	 * 简称
	 */
	private String simName;

	/**
	 * @return the code
	 */
	public int getCode() {
		return ordinal();
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @return the simName
	 */
	public String getSimName() {
		return simName;
	}
	
}
