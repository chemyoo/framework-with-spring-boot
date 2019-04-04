package pers.chemyoo.core.provider;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月28日 下午6:32:18
 * @since since from 2019年3月28日 下午6:32:18 to now.
 * @description class description
 */
public class CustomSelectProvider {

	public String selectBussinessByLeftJoin(Map<String, Object> params) {
		SQL sql = new SQL();
		String[] columns = { "main_type as mainName" };
		sql.SELECT_DISTINCT(columns);
		sql.FROM("gzfh_cfg_business c1");
		sql.INNER_JOIN("gzfh_que_windbusiness c2 on c1.code = c2.business_code");
		sql.INNER_JOIN("gzfh_que_window c3 on c3.sid = c2.win_id");
		if (!params.isEmpty())
			sql.WHERE("c3.floor = #{param1} and c2.win_id = #{param2}");
		return sql.toString();
	}

	public String getApplyForms(String tableName, String appointNum) {
		SQL sql = new SQL();
		sql.SELECT_DISTINCT("*");
		sql.FROM(tableName);
		if (StringUtils.isNotBlank(appointNum))
			sql.WHERE("appoint_num = #{param2}");
		return sql.toString();
	}

}
