package pers.chemyoo.core.setting.base.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by Administrator on 2019-02-12 11:38
 */
public interface TkMapper<T> extends Mapper<T>, MySqlMapper<T> {
	
}