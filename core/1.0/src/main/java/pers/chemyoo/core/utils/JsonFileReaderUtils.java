package pers.chemyoo.core.utils;
/** 
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月11日 下午6:17:58 
 * @since since from 2019年3月11日 下午6:17:58 to now.
 * @description class description
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import net.sf.json.JSONObject;

public class JsonFileReaderUtils {
	
	private static OldFile oldFile = new OldFile();

	private JsonFileReaderUtils() {}
	
	private static JSONObject json;
	
	/**
	 * 从jar或本地路径读取文件
	 * @param fileName
	 * @return
	 */
	public static JSONObject read(String fileName) {
		if(json != null) return json;
		try(InputStream is = getClassLoader().getResourceAsStream(fileName);){
			json = JSONObject.fromObject(IOUtils.toString(is, "utf-8"));
		} catch (Exception e) {
			// ignore
		}
		return json;
	}
	
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * 从本地路径读取文件，当有更改时，重新读取文件
	 * @param file
	 * @return
	 */
	public static JSONObject read(File file) {
		String path = file.getAbsolutePath();
		long modifyTime = file.lastModified();
		if(path.equals(oldFile.path) && modifyTime <= oldFile .modifyTime) {
			return json;
		}
		try {
			String str = FileUtils.readFileToString(file, "utf-8");
			json = JSONObject.fromObject(str);
			oldFile.path = path;
			oldFile.modifyTime = modifyTime;
		} catch (IOException e) {
			// ignore
		}
		return json;
	}
	
	public static class OldFile{
		private String path;
		private long modifyTime = 0;
	}
	
	public static String getResourceFolder() {
		return JsonFileReaderUtils.class.getClassLoader().getResource("").getPath() + File.separator;
	}
	
}
