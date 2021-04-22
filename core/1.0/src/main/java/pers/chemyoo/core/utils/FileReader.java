package pers.chemyoo.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * 支持动态加载
 * 
 * @author jianqing.liu
 * @since 2020年4月9日 上午10:48:17
 */
@Slf4j
public final class FileReader
{

	private FileReader()
	{
		throw new AbstractMethodError("FileReader can not instance.");
	}

	private static final Map<String, Object> MAP_CACHE = Maps.newHashMap();
	private static final Map<String, Long> VERSION_CACHE = Maps.newHashMap();

	private static final String EMPTY = "";

	/**
	 * 文件读取标尺文件
	 */
	private static final File TEMPLATES_FOLDER = new File(getOptPath(), File.separator + "ReaderCursor" + File.separator);
	private static final String CHARSET = "utf-8";

	public static String getOptPath()
	{
		return "/bwopt/booway/hnzljdxxh/";
	}

	public static byte[] readToByte(String fileName)
	{
		try
		{
			Validate.notBlank(fileName, "fileName不能为空");
			File file = new File(TEMPLATES_FOLDER, fileName);
			long version = file.lastModified();
			String key = DigestUtils.md5Hex(fileName);
			if (MAP_CACHE.containsKey(key) && VERSION_CACHE.get(key) == version)
			{
				return (byte[]) MAP_CACHE.get(fileName);
			}
			MAP_CACHE.put(key, FileUtils.readFileToByteArray(file));
			VERSION_CACHE.put(key, version);
			return (byte[]) MAP_CACHE.get(key);
		}
		catch (IOException e)
		{
			log.error(e.getMessage(), e);
		}
		return new byte[0];
	}

	public static String readToString(String fileName)
	{
		try
		{
			File file = new File(TEMPLATES_FOLDER, fileName);
			long version = file.lastModified();
			String key = DigestUtils.md5Hex(fileName) + "_STRING";
			if (MAP_CACHE.containsKey(key) && VERSION_CACHE.get(key) == version)
			{
				return (String) MAP_CACHE.get(fileName);
			}
			MAP_CACHE.put(key, new String(readToByte(fileName), CHARSET));
			VERSION_CACHE.put(key, version);
			return (String) MAP_CACHE.get(key);
		}
		catch (UnsupportedEncodingException e)
		{
			log.error(e.getMessage(), e);
		}
		return EMPTY;
	}

	public static JSONObject readToJson(String fileName)
	{
		File file = new File(TEMPLATES_FOLDER, fileName);
		long version = file.lastModified();
		String key = DigestUtils.md5Hex(fileName) + "_JSON";
		if (MAP_CACHE.containsKey(key) && VERSION_CACHE.get(key) == version)
		{
			return (JSONObject) MAP_CACHE.get(fileName);
		}
		MAP_CACHE.put(key, JSONObject.parse(readToByte(fileName)));
		VERSION_CACHE.put(key, version);
		return (JSONObject) MAP_CACHE.get(key);
	}

	/**
	 * 创建一个标尺文件
	 */
	public static void createCursorFile(String fileName, String data, boolean upadte)
	{
		if (!TEMPLATES_FOLDER.exists())
		{
			TEMPLATES_FOLDER.mkdirs();
		}
		if (StringUtils.isNotBlank(fileName))
		{
			File newFile = new File(TEMPLATES_FOLDER.getAbsolutePath(), fileName);
			if (!newFile.exists() || upadte)
			{
				try
				{
					FileUtils.writeStringToFile(newFile, data == null ? "" : data, CHARSET);
				}
				catch (IOException e)
				{
					log.error(e.getMessage(), e);
				}
			}
		}
	}

}
