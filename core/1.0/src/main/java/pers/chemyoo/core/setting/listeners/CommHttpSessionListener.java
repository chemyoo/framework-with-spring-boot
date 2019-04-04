package pers.chemyoo.core.setting.listeners;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpSessionEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommHttpSessionListener implements javax.servlet.http.HttpSessionListener {

	public static final AtomicInteger ONLINE = new AtomicInteger(0);

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		se.getSession().setAttribute("ctx2", "SessondemoPro");

		log.info("创建session");
		log.info("online sessions count is: ", ONLINE.incrementAndGet());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		log.info("销毁session");

	}

}