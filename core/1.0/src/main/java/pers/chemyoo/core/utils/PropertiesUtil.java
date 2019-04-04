package pers.chemyoo.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 作者 : jianqing.liu
 * @version 创建时间：2018年6月25日 下午7:02:15
 * @since 2018年6月25日 下午7:02:15
 * @description 配置文件(config.properties)内容获取，单例模式
 */
@Slf4j
public class PropertiesUtil {
	private PropertiesUtil() {
	}

	private static Properties properties = new Properties();

	private static final String PROPERTIE_FILE_PATH = "application.properties";

	/**
	 * Initialize properties.
	 * 
	 * @return Properties what you were defined.
	 */
	public static Properties getInstance() {
		if (properties.isEmpty()) {
			init();
		}
		return properties;
	}

	private static synchronized void init() {
		if (properties.isEmpty()) {
			log.info("初始化properties");
			try (InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(PROPERTIE_FILE_PATH)) {
				properties.load(in);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	public static String getWorkFolder() {
		String path = System.getProperty("catalina.home", System.getProperty("user.dir"));
		File file = new File(path);
		File parent = file.getParentFile();
		if (parent != null) {
			path = parent.getAbsolutePath();
		}
		return path;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getProperty(String key, Class<T> clazz) {
		String className = clazz.getSimpleName().toLowerCase().intern();
		Object value;
		switch (className) {
		case "long":
			value = Long.parseLong(properties.getProperty(key, "0"));
			break;
		case "short":
		case "byte":
		case "integer":
			value = Integer.parseInt(properties.getProperty(key, "0"));
			break;
		case "double":
			value = Double.parseDouble(properties.getProperty(key, "0.0"));
			break;
		case "float":
			value = Float.parseFloat(properties.getProperty(key, "0.0"));
			break;
		default:
			value = properties.getProperty(key, "");
		}
		return (T) value;
	}

}
