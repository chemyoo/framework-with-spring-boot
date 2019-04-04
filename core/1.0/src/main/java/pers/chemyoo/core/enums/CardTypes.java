package pers.chemyoo.core.enums;

/**
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月19日 上午11:17:42
 * @since since from 2019年3月19日 上午11:17:42 to now.
 * @description 证件类型
 */
public enum CardTypes {

	/**
	 * {@code IC：身份证}
	 */
	ID_CARD("身份证", "IC"),
	/**
	 * {@code OC：军官证}
	 */
	OFFICIAL_CARD("军官证", "OC"),
	/**
	 * {@code PASS：护照}
	 */
	PASSPORT("护照", "PASS"),
	/**
	 * {@code BC：出生证}
	 */
	BIRTH_CERTIFICATE("出生证", "BC"),
	/**
	 * {@code HMRC：港澳居民来往内地通行证}
	 */
	HK_MC_RESIDENTS("港澳居民来往内地通行证", "HMRC"),
	/**
	 * {@code TRC：台湾居民往来大陆通行证}
	 */
	TAIWAN_RESIDENTS("港澳居民来往内地通行证", "TRC"),
	/**
	 * {@code RC：居住证}
	 */
	RESIDENCE_CARD("居住证", "RC"),
	/**
	 * {@code OTC：其他}
	 */
	OTHER_CARD("其他", "OTC");

	/**
	 * @return the simName
	 */
	public String getSimName() {
		return simName;
	}

	CardTypes(String typeName, String simName) {
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
}
