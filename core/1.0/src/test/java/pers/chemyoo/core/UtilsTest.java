package pers.chemyoo.core;
/** 
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月1日 上午11:18:11 
 * @since since from 2019年3月1日 上午11:18:11 to now.
 * @description class description
 */

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import pers.chemyoo.core.enums.CardTypes;
import pers.chemyoo.core.utils.DateUtils;
import pers.chemyoo.core.utils.EnumUtils;
import pers.chemyoo.core.utils.KeyGenerator;

@Slf4j
public class UtilsTest {
	
	KeyGenerator generator = KeyGenerator.getGenerator();
	
	@Test
	public void singleThreadTest() {
		int size = 1000;
		final Set<String> keyss = Sets.newHashSet();
		long s = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			String key = generator.getKey();
			if (!keyss.contains(key)) {
				keyss.add(key);
			} else {
				log.info("----------------------{}", key);
			}
		}
		log.info("singleThreadTest cost: {}s", (System.currentTimeMillis() - s) / 1000D);
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTime(DateUtils.getCurrentTime());
		int weekDay = gcal.get(Calendar.DAY_OF_WEEK);
		log.info("{}", weekDay);
	}
	
	@Test
	public void mutilThreadTest() throws InterruptedException {
		int size = 1000;
		final CountDownLatch lantch = new CountDownLatch(size);
		final Set<String> keyss = Sets.newHashSet();
		long s = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			new Thread() {
				@Override
				public void run() {
					String key = generator.getKey();
					if (!keyss.contains(key)) {
						keyss.add(key);
					} else {
						log.info("----------------------{}", key);
					}
					lantch.countDown();
				}
			}.start();
		}
		lantch.await();
		log.info("mutilThreadTest cost: {}s", (System.currentTimeMillis() - s) / 1000D);
	}
	
	@Test
	public void enumUtilsTest() {
		log.info(EnumUtils.enumToList(CardTypes.class).toString());
	}

}
