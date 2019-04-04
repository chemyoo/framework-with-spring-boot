package pers.chemyoo.core.setting.config;

import java.io.IOException;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

import pers.chemyoo.core.setting.intercepters.PrimaryKeyInterceptor;
import pers.chemyoo.core.utils.PropertiesUtil;

@Configuration
@Profile("maintain")
@ComponentScan(basePackages = { "com.gitee.sunchenbin.mybatis.actable.manager.*" })
public class AutoTableConfig {

	@Value("${spring.datasource.driver-class-name}")
	private String driver;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Bean
	public PropertiesFactoryBean configProperties() {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setProperties(PropertiesUtil.getInstance());
		propertiesFactoryBean.setSingleton(true);
		return propertiesFactoryBean;
	}

	@Bean
	public DruidDataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setMaxActive(30);
		dataSource.setInitialSize(10);
		dataSource.setValidationQuery("SELECT 1");
		dataSource.setTestOnBorrow(true);
		return dataSource;
	}

	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager() {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource());
		return dataSourceTransactionManager;
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactory() throws IOException {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(
				resolver.getResources("classpath*:com/gitee/sunchenbin/mybatis/actable/mapping/*/*.xml"));
		sqlSessionFactoryBean.setTypeAliasesPackage("pers.chemyoo.core.entity.*");
		// 添加持久化拦截器
		sqlSessionFactoryBean.setPlugins(new Interceptor[] { new PrimaryKeyInterceptor() });
		return sqlSessionFactoryBean;
	}

}
