package pers.chemyoo.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import pers.chemyoo.core.annotations.CheckField;
import pers.chemyoo.core.enums.CheckGroups;
import pers.chemyoo.core.enums.CheckType;

/**
 * class description 反射获取类属性工具类
 * 
 * @author Author : jianqing.liu
 * @version version : created time：2018年11月8日 下午3:13:45
 * @since since from 2018年11月8日 下午3:13:45 to now.
 */
public class CheckUtils
{

	private CheckUtils() throws NoSuchMethodException
	{
		throw new NoSuchMethodException("CheckUtils can not be instansed");
	}

	private static final String GET = "get";
	
//	private static List<CheckType> checkTypes = enumToList(CheckType.class);
	
	private static final String LENGTH_MESSAGE = "%s最大允许长度为%d，当前超出%d个字符";

	/**
	 * 从对象中获取指定的属性值
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValue(Object obj, String fieldName)
	{
		T value = null;
		if (obj != null)
		{
			Validate.notBlank(fieldName);
			Class<?> clazz = obj.getClass();
			try
			{
				if (obj instanceof java.util.Map)
				{
					Method method = clazz.getMethod(GET, Object.class);
					return (T) method.invoke(obj, fieldName);
				}
				Field field = getFieldMap(clazz).get(fieldName.toLowerCase());
				if (field == null)
				{
					return value;
				}
				boolean accessible = field.isAccessible();
				if (!accessible)
				{
					field.setAccessible(!accessible);
				}
				value = (T) field.get(obj);
				if (!accessible)
				{
					field.setAccessible(accessible);
				}
			}
			catch (IllegalAccessException | SecurityException | NoSuchMethodException | InvocationTargetException e)
			{
				throw new IllegalArgumentException(e);
			}
		}
		return value;
	}

	public static void copyValues(Object dest, Object orig) throws IllegalAccessException
	{
		List<Field> fields = getFieldsWithAnnotation(dest.getClass());
		for (Field field : fields)
		{
			Object value = getValue(orig, field.getName());
			if (value != null)
			{
				FieldUtils.writeField(field, dest, value, true);
			}
		}
	}

	/**
	 * get Fields from class and include super.
	 * 
	 * @param clazz
	 * @param annotations
	 * @return
	 */
	@SafeVarargs
	public static List<Field> getFieldsWithAnnotation(Class<?> clazz, Class<? extends Annotation>... annotations)
	{
		List<Field> fields = Lists.newArrayList();
		do
		{
			findFields(clazz, fields, annotations);
			clazz = clazz.getSuperclass();
		} while (clazz != null && clazz != Object.class);
		return fields;
	}

	public static Map<String, Field> getFieldMap(Class<?> clazz)
	{
		return convert2FieldMap(getFieldsWithAnnotation(clazz));
	}

	public static Map<String, Field> convert2FieldMap(List<Field> fields)
	{
		Map<String, Field> fieldMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(fields))
		{
			for (Field field : fields)
			{
				fieldMap.put(field.getName().toLowerCase(), field);
			}
		}
		return fieldMap;
	}

	@SafeVarargs
	private static void findFields(Class<?> clazz, List<Field> fields, Class<? extends Annotation>... annotations)
	{
		Field[] thisFields = clazz.getDeclaredFields();
		int c = annotations.length;
		for (Field field : thisFields)
		{
			if (c == 0 && !Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()))
			{
				// add neither final nor static field.
				fields.add(field);
			}
			else
			{
				for (Class<? extends Annotation> annotation : annotations)
				{
					if (field.isAnnotationPresent(annotation))
					{
						fields.add(field);
						break;
					}
				}
			}
		}
	}
	
	public static <T> CheckResults check(T t, CheckGroups... groups) {
		CheckResults result = new CheckResults();
		List<Field> fields = getFieldsWithAnnotation(t.getClass(), CheckField.class);
		if(!fields.isEmpty()) {
			for(Field field : fields) {
				CheckField checkField = field.getAnnotation(CheckField.class);
				CheckGroups[] checkGroups = checkField.groups();
				if(!hasValue(checkGroups, CheckGroups.NONE) && !hasValue(checkGroups, groups)) 
				{
					continue;
				}
				doCheckType(t, result, checkField, field);
			}
		}
		return result;
	}
	
	private static <T> void doCheckType(T t, CheckResults result, CheckField checkField, Field field) {
		CheckType chektype = checkField.type();
		Object value = getValue(t, field.getName());
		int length = checkField.length();
		if(CheckType.NONE != chektype && field.getType() == String.class) {
			boolean error = false;
			if(CheckType.NOT_EMPTY == chektype) {
				error = value != null && ((String) value).length() != 0;
			}else if(CheckType.NOT_BLANK == chektype) {
				error = value != null && ((String) value).trim().length() != 0;
			} else if(CheckType.NOT_NULL == chektype) {
				error = value != null;
			} else {
				error = isMatches(chektype, value);
			}
			if(!error) {
				result.addMessages(checkField.value() + checkField.message());
			}
		} 
		if(value != null && length > 0) {
			int diff = value.toString().length() - length;
			if(diff > 0) {
				result.addMessages(String.format(LENGTH_MESSAGE, checkField.value(), length, diff));
			}
		}
	}
	
	private static boolean hasValue(CheckGroups[] checkGroups, CheckGroups... groups) {
		for(CheckGroups g : groups) {
			if(ArrayUtils.contains(checkGroups, g)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isMatches(CheckType chektype, Object value) {
		String regexp = chektype.getRegexp();
		if(!CheckResults.isBlank(regexp)) {
			return value != null || !Pattern.matches(chektype.getRegexp(), (String) value);
		}
		return true;
	}
	
	/**
	 * 枚举类转换成List<CheckType>
	 * @param clazz
	 * @return
	 */
	public static <T extends Enum<?>> List<CheckType> enumToList(Class<T> clazz) {
		List<CheckType> array = new ArrayList<>();
		CheckType[] clazzs = (CheckType[]) clazz.getEnumConstants();
		for(CheckType t : clazzs) {
			array.add(t);
		}
		return array;
	}

}
