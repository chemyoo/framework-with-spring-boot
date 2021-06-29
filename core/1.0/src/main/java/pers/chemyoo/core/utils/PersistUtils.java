package pers.chemyoo.core.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import pers.chemyoo.core.entity.IdModel;
import pers.chemyoo.core.setting.base.mapper.TkMapper;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月13日 上午11:56:22
 * @since since from 2019年3月13日 上午11:56:22 to now.
 * @description TkMapper 持久化工具
 */
public class PersistUtils
{
	private PersistUtils()
	{
	}

	public static <T extends IdModel> boolean save(TkMapper<T> mapper, T t)
	{
		String sid = t.getId();
		t.setLastModTime(DateUtils.getCurrentTime());
		if (sid == null)
		{
			return mapper.insert(t) > 0;
		}
		else
		{
			return mapper.updateByExample(t, primaryKeyExample(t.getClass(), sid)) > 0;
		}
	}

	public static <T extends IdModel> int saveList(TkMapper<T> mapper, List<T> list)
	{
		if (list.isEmpty())
		{
			return 0;
		}
		int saveCount = 0;
		List<T> savelist = Lists.newArrayList();
		Date lmt = DateUtils.getCurrentTime();
		for (T t : list)
		{
			String sid = t.getId();
			if (sid != null)
			{
				saveCount += mapper.updateByExample(t, primaryKeyExample(t.getClass(), sid));
			}
			else
			{
				t.setId(KeyGenerator.getGenerator().getKey());
				savelist.add(t);
			}
			t.setLastModTime(lmt);
		}
		if (!savelist.isEmpty())
		{
			saveCount += mapper.insertList(savelist);
		}
		return saveCount;
	}

	public static <T extends IdModel> Example primaryKeyExample(Class<T> clazz, String primaryKey)
	{
		Example example = new Example(clazz);
		example.createCriteria().andEqualTo("id", primaryKey);
		return example;
	}

	public static <T extends IdModel> Example equalExampleByEntity(Class<T> clazz, T t)
	{
		Example example = new Example(clazz);
		Map<String, Object> map = AttributesUtils.getNotEmptyFields(clazz, t);
		Example.Criteria criteria = example.createCriteria();
		for (Map.Entry<String, Object> entry : map.entrySet())
		{
			criteria.andEqualTo(entry.getKey(), entry.getValue());
		}
		return example;
	}

	public static <T extends IdModel> Example equalExample(Example example, T t)
	{
		Map<String, Object> map = AttributesUtils.getNotEmptyFields(t.getClass(), t);
		Example.Criteria criteria = example.createCriteria();
		for (Map.Entry<String, Object> entry : map.entrySet())
		{
			criteria.andEqualTo(entry.getKey(), entry.getValue());
		}
		return example;
	}

	public static <T extends IdModel> T selectByPrimaryKey(TkMapper<T> mapper, Class<T> clazz, String id)
	{
		return mapper.selectOneByExample(primaryKeyExample(clazz, id));
	}

	public static <T extends IdModel> Example betweenExampleByEntity(Class<T> clazz, T t, Object start, Object end)
	{
		Example example = new Example(clazz);
		Map<String, Object> map = AttributesUtils.getNotEmptyFields(clazz, t);
		Example.Criteria criteria = example.createCriteria();
		for (Map.Entry<String, Object> entry : map.entrySet())
		{
			criteria.andBetween(entry.getKey(), start, end);
		}
		return example;
	}

	public static <T extends IdModel> int deleteByPrimaryKey(TkMapper<T> mapper, Class<T> clazz, String id)
	{
		return mapper.deleteByExample(primaryKeyExample(clazz, id));
	}

	public static <T extends IdModel> List<T> selectByIn(TkMapper<T> mapper, T t, List<? extends Object> list)
	{
		Example example = new Example(t.getClass());
		Map<String, Object> map = AttributesUtils.getNotEmptyFields(t.getClass(), t);
		Example.Criteria criteria = example.createCriteria();
		for (Map.Entry<String, Object> entry : map.entrySet())
		{
			criteria.andIn(entry.getKey(), list);
		}
		return mapper.selectByExample(example);
	}

}
