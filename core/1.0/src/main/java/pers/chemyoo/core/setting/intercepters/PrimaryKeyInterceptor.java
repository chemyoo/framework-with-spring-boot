package pers.chemyoo.core.setting.intercepters;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import lombok.extern.slf4j.Slf4j;
import pers.chemyoo.core.entity.IdModel;
import pers.chemyoo.core.utils.DateUtils;
import pers.chemyoo.core.utils.KeyGenerator;

/**
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月1日 下午4:02:18
 * @since since from 2019年3月1日 下午4:02:18 to now.
 * @description class description
 */
@Slf4j
@Intercepts({
		// @Signature(type = StatementHandler.class, method = "update", args =
		// {Statement.class}),
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class PrimaryKeyInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object invoke = invocation.getTarget();
		if (invoke instanceof StatementHandler) {
			StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
			log.info("Interceptor Method is {}.", invocation.getMethod().getName());
			BoundSql boundSql = statementHandler.getBoundSql();
			Object object = boundSql.getParameterObject();
			List<ParameterMapping> mappings = boundSql.getParameterMappings();
			for (ParameterMapping mapping : mappings) {
				String field = mapping.getProperty();
				if (IdModel.SID.equals(field)) {
					Object sid = FieldUtils.readField(object, field, true);
					if (sid == null) {
						String key = KeyGenerator.getGenerator().getKey();
						FieldUtils.writeField(object, field, key, true);
						log.info("auto set {} is : {}", IdModel.SID, key);
					}
				} else if (IdModel.LMT.equalsIgnoreCase(field)) {
					Object time = DateUtils.getCurrentTime();
					FieldUtils.writeField(object, field, time, true);
					log.info("auto set {} is : {}", IdModel.LMT, time.toString());
					break;
				}
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		String dialect = properties.getProperty("dialect");
		log.info("mybatis intercept dialect:{}", dialect);
	}

}
