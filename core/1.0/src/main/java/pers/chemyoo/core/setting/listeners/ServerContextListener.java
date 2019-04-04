package pers.chemyoo.core.setting.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebListener
public class ServerContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		log.info("自定义监听器:ServletContextListener->contextInitialized");

		// 读取属性文件，设置环境变量
		// 初始化菜单缓存数据
		// 初始化系统公用全局变量
		servletContextEvent.getServletContext().setAttribute("ctx", "ContextdemoPro");

	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// Empty
	}
}
