/**
 * 
 */
package pers.chemyoo.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 驼峰命名转换
 * 
 * @author liujianqing 下午8:22:50
 */
public final class CamelCaseUtils
{

	private CamelCaseUtils() throws NoSuchMethodException
	{
		throw new NoSuchMethodException("CamelCaseUtils can not Instanse");
	}

	private static final String UNDERLINE = "_";

	private static final char UPPER_A = 'A';
	private static final char LOWER_A = 'a';
	private static final char LOWER_Z = 'z';

	/**
	 * 大小写的ASCII码距离.
	 */
	private static final int ASCII_DISTANCE = LOWER_A - UPPER_A;

	/**
	 * 将带下划线的词语转换为驼峰
	 */
	public static String toCamelCase(String str)
	{
		String[] split = str.toLowerCase().split(UNDERLINE);
		int length = split.length;
		if (length <= 1)
		{
			return str;
		}
		else
		{
			StringBuilder keyBuilder = new StringBuilder(split[0]);
			for (int i = 1; i < length; i++)
			{
				keyBuilder.append(firstUpperCase(split[i]));
			}
			return keyBuilder.toString();
		}
	}

	/**
	 * 首字母大写
	 * 
	 * @param key
	 * @return
	 */
	public static String firstUpperCase(String key)
	{
		if (StringUtils.isNotBlank(key))
		{
			char first = key.charAt(0);
			StringBuilder builder = new StringBuilder();
			if (first >= CamelCaseUtils.LOWER_A && first <= CamelCaseUtils.LOWER_Z)
			{
				builder.append((char) (first - CamelCaseUtils.ASCII_DISTANCE));
			}
			else
			{
				builder.append(first);
			}
			builder.append(key.substring(1));
			return builder.toString();
		}

		return key;
	}

}
