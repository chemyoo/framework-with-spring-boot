package pers.chemyoo.core.setting.config;

import javax.annotation.PostConstruct;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import pers.chemyoo.core.setting.intercepters.PrimaryKeyInterceptor;

/**
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月1日 下午5:24:30
 * @since since from 2019年3月1日 下午5:24:30 to now.
 * @description class description
 */
// @Configuration
public class InterceptorConfig {

	@Autowired
	SqlSessionFactory sqlSessionFactory;

	@Bean
	public Interceptor getInterceptor() {
		return new PrimaryKeyInterceptor();
	}

	@PostConstruct
	public void addInterceptor() {
		if (sqlSessionFactory != null) {
			sqlSessionFactory.getConfiguration().addInterceptor(getInterceptor());
		}
	}

}
