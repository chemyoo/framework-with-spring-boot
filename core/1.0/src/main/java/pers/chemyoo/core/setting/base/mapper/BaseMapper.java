package pers.chemyoo.core.setting.base.mapper;

import java.util.List;

import pers.chemyoo.core.entity.IdModel;
import pers.chemyoo.core.utils.PersistUtils;

/** 
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月19日 上午10:48:33 
 * @since since from 2019年3月19日 上午10:48:33 to now.
 * @description 将更新、保存和按主键查询的方法进行简单的封装
 */
public interface BaseMapper<T extends IdModel> extends TkMapper<T> {

	/**
	 * 保存或更新，根据SID是否为Null进行逻辑判断更新还是插入
	 * @param t
	 * @return
	 */
	default boolean save(T t) {
		return PersistUtils.save(this, t);
	}
	
	/**
	 * 保存或更新列表，列表中可以存在更新或插入的数据
	 * @param t
	 * @return
	 */
	default int saveList(List<T> list) {
		return PersistUtils.saveList(this, list);
	}
	
	/**
	 * 按主键查询数据
	 * @param t
	 * @return
	 */
	default T selectByPrimaryKey(Class<T> clazz, String id) {
		return PersistUtils.selectByPrimaryKey(this, clazz, id);
	}
	
	/**
	 * 按主键删除数据
	 * @param t
	 * @return
	 */
	default int deleteByPrimaryKey(Class<T> clazz, String id) {
		return PersistUtils.deleteByPrimaryKey(this, clazz, id);
	}
	
}
