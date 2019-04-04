package pers.chemyoo.core.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @author Author : jianqing.liu
 * @version version : created time：2018年10月17日 下午12:02:25 
 * @since since from 2018年10月17日 下午12:02:25 to now.
 * @description class description
 */
public class EnumUtils {
	
	private EnumUtils() {}
	
	/**
	 * 枚举类转换成List<Map<String,String>>
	 * @param clazz
	 * @return
	 */
	public static <T extends Enum<?>> List<Map<String,Object>> enumToList(Class<T> clazz) {
		List<Map<String,Object>> array = new ArrayList<>();
		// if statement can be removed, because the <T extends Enum<?>> limit the class is enumerate.
		if(clazz.isEnum()) {
			T[] clazzs = clazz.getEnumConstants();
			for(T t : clazzs) {
				List<Field> fields = AttributesUtils.getFields(t.getClass());
				array.add(fieldToMap(fields, t));
			}
		}
		return array;
	}
	
	/**
	 * 将枚举类映射字段转成Map<String,Object>
	 * @param fields
	 * @param obj
	 * @return
	 */
	private static Map<String,Object> fieldToMap(List<Field> fields, Object obj) {
		Map<String,Object> map = new HashMap<>();
		for(Field field : fields) {
			map.put(field.getName(), AttributesUtils.getFieldValue(field, obj));
		}
		return map;
	}
}
