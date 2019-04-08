package pers.chemyoo.core.utils;

import java.util.GregorianCalendar;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.time.FastDateFormat;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Author : jianqing.liu
 * @version version : created time：2016年3月23日 上午11:24:33
 * @since since from 2016年3月23日 上午11:24:33 to now.
 * @description class description
 */
@Slf4j
public class KeyGenerator {

	private KeyGenerator() {
	}

	/** 小量的键值缓存 */
	private static Set<String> keyCache = Sets.newHashSet();

	private static final AtomicInteger INDEX = new AtomicInteger(0);

	private static final char A = 'A';

	/** 线程安全的日期格式化类 */
	public static final FastDateFormat fastFormat = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");

	private static KeyGenerator keyGenerator = new KeyGenerator();

	public static KeyGenerator getGenerator() {
		return keyGenerator;
	}

	/**
	 * 多线程安全产生永不重复的key值
	 * 
	 * @author jianqing.liu
	 * @since 2016-03-23
	 * @version 2.0
	 * @return
	 */
	public synchronized String getKey() {
		StringBuilder key = new StringBuilder();
		GregorianCalendar grego = new GregorianCalendar();
		key.append(fastFormat.format(grego.getTime()));
		key.append(getUppercaseLetter());
		String currentKey = key.toString();
		if (keyCache.contains(currentKey) || keyCache.size() >= 500) {
			try {
				keyCache.clear();
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) {
				log.error(e.getMessage(), e);
				Thread.currentThread().interrupt();
			}
			return getKey();
		} else {
			keyCache.add(currentKey);
		}
		return currentKey;
	}

	/**
	 * @return a upper case character
	 */
	public synchronized char getUppercaseLetter() {
		int index = INDEX.getAndIncrement();
		if (index >= 25) {
			INDEX.set(0);
		}
		return (char) (index + A);
	}
}
