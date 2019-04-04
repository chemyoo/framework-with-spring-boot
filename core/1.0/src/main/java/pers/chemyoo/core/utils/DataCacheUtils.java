package pers.chemyoo.core.utils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/** 
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月9日 下午6:17:40 
 * @since since from 2019年3月9日 下午6:17:40 to now.
 * @description 数据缓存工具类
 */
@Slf4j
@Component
public class DataCacheUtils {
	
	private DataCacheUtils() {}
	
	private static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	
	private static final Map<String, Map<String, Object>> DATA_CACHE = Maps.newConcurrentMap();
	
	static {
		// 定时(每天00:01)执行
		service.scheduleWithFixedDelay(() -> {
			DataCacheUtils.clearAllCache();
			log.info("定时缓存清理完成...");
		}, getMillsNextDay(), 1, TimeUnit.DAYS);
	}
	
	/**
	 * 当前时间到明天00:01的毫秒数
	 * @return
	 */
	public static long getMillsNextDay() {
		Date date = DateUtils.getNextDay();
		return date.getTime() - System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1);
	}
	
	public static void createContainer(String ckey) {
		ckey = repalceKey(ckey);
		DATA_CACHE.put(ckey, Maps.newHashMap());
	}
	
	public static boolean containsCKey(String ckey) {
		ckey = repalceKey(ckey);
		return DATA_CACHE.containsKey(ckey);
	}
	
	public static void putCache(String ckey, String key, Object value) {
		ckey = repalceKey(ckey);
		key = repalceKey(key);
		if(DATA_CACHE.containsKey(ckey)) {
			DATA_CACHE.get(ckey).put(key, value);
		} else {
			Map<String,Object> entry = Maps.newHashMap();
			entry.put(key, value);
			DATA_CACHE.put(ckey, entry);
		}
	}
	
	public static void putAllCache(String ckey, Map<String, Object> mapCache) {
		ckey = repalceKey(ckey);
		if(DATA_CACHE.containsKey(ckey)) {
			DATA_CACHE.get(ckey).clear();
		} 
		DATA_CACHE.put(ckey, mapCache);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getCache(String ckey, String key) {
		ckey = repalceKey(ckey);
		key = repalceKey(key);
		if(DATA_CACHE.containsKey(ckey)) {
			return (T) DATA_CACHE.get(ckey).get(key);
		} 
		return null;
	}
	
	public static void clearCache(String ckey) {
		ckey = repalceKey(ckey);
		if(DATA_CACHE.containsKey(ckey)) {
			DATA_CACHE.get(ckey).clear();
		} 
		DATA_CACHE.clear();
	}
	
	public static void clearAllCache() {
		for(Map.Entry<String, Map<String,Object>> e : DATA_CACHE.entrySet()) {
			e.getValue().clear();
		}
		DATA_CACHE.clear();
	}
	
	/**
	 * 替换key值中的年月日
	 * @param key
	 * @return
	 */
	private static String repalceKey(String key) {
		if(DateUtils.isContains(key)) {
			key = key.replaceAll("[年月]", "-").replace("日", "");
		}
		return key;
	}
	
}
