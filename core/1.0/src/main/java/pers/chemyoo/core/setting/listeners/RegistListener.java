package pers.chemyoo.core.setting.listeners;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RegistListener {

	@Bean
	public ServletListenerRegistrationBean<CommHttpSessionListener> listenerRegist() {
		ServletListenerRegistrationBean<CommHttpSessionListener> srb = new ServletListenerRegistrationBean<>();
		srb.setListener(new CommHttpSessionListener());
		log.info("启动会话：HttpSessionListener");
		return srb;
	}

	@Bean
	public ServletListenerRegistrationBean<ServerContextListener> listenerRegist2() {
		ServletListenerRegistrationBean<ServerContextListener> srb2 = new ServletListenerRegistrationBean<>();
		srb2.setListener(new ServerContextListener());
		log.info("启动容器：ServerContextListener");
		return srb2;
	}
}
