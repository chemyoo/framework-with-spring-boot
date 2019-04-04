package pers.chemyoo.core.task;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月13日 下午5:16:19
 * @since since from 2019年3月13日 下午5:16:19 to now.
 * @description 更新预约状态，并将失约人人员列入黑名单做记录，失约3次将不能微信预约
 */
@Slf4j
@Component
@Profile("maintain")
public class AppointmentTask {

	/**
	 * 定时：凌晨 00:00:05执行 将昨天前的预约信息状态置为过期
	 */
	@Scheduled(cron = "5 0 0 * * ?")
	private synchronized void setAppointmentExpire() {
		log.info("定时任务");
	}

}
