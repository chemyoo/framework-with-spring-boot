package pers.chemyoo.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import pers.chemyoo.core.setting.listeners.CleanupListener;
import tk.mybatis.spring.annotation.MapperScan;

@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = { "pers.chemyoo.core" })
@MapperScan(basePackages = { "pers.chemyoo.core.mapper" })
@ServletComponentScan(basePackageClasses = { CleanupListener.class })
public class CoreApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(CoreApplication.class);
	}

}
