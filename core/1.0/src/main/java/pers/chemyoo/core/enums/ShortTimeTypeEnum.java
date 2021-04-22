package pers.chemyoo.core.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 短日期格式
 * 
 * @author jianqing.liu
 * @since 2020年6月10日 下午12:32:47
 */
public enum ShortTimeTypeEnum
{
	/**
	 * 连字符格式（-）
	 * 
	 * @return
	 */
	DAY_HYPHEN("hyphen", "yyyy-MM-dd"),

	/**
	 * 斜杠格式（/）
	 * 
	 * @return
	 */
	DAY_SLASH("slash", "yyyy/MM/dd"),

	/**
	 * 年月日格式
	 * 
	 * @return
	 */
	DAY_YMD("ymd", "yyyy年MM月dd日"),

	/**
	 * 连续数字格式（yyyyMMdd）
	 * 
	 * @return
	 */
	DAY_NONE("none", "yyyyMMdd");

	ShortTimeTypeEnum(String code, String pattern)
	{
		this.code = code;
		this.pattern = pattern;
	}

	private String code;
	private String pattern;

	public String getCode()
	{
		return this.code;
	}

	public String getPattern()
	{
		return this.pattern;
	}

	/**
	 * 根据编码获取格式类型</br>
	 * code == null 或 code为空时返回 {@link com.bw.hnzljdxxh.tenum.ShortTimeTypeEnum.DAY_NONE}
	 * 
	 * @param code
	 * @return {@link ShortTimeTypeEnum}
	 */
	public static ShortTimeTypeEnum getTypeByCode(String code)
	{
		if (StringUtils.isNotEmpty(code))
		{
			for (ShortTimeTypeEnum type : values())
			{
				if (type.getCode().equalsIgnoreCase(code))
				{
					return type;
				}
			}
		}
		return ShortTimeTypeEnum.DAY_NONE;
	}
}
