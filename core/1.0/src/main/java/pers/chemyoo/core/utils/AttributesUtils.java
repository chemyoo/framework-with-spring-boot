package pers.chemyoo.core.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import pers.chemyoo.core.annotations.Comment;
import pers.chemyoo.core.entity.IdModel;
import pers.chemyoo.core.enums.CheckFieldType;
import pers.chemyoo.core.enums.ExcludeField;
import pers.chemyoo.core.enums.TimeFormat;

/**
 * @author Author : jianqing.liu
 * @version version : created time：2018年11月8日 下午3:13:45
 * @since since from 2018年11月8日 下午3:13:45 to now.
 * @description class description
 */
@Slf4j
public class AttributesUtils {

	private AttributesUtils() {
	}

	/**
	 * 设置对象中指定的属性值
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static void setAttributeValue(Object obj, String fieldName, Object value) {
		if (obj != null) {
			Validate.notBlank(fieldName);
			String beanName = fieldName.replace("_", StringUtils.EMPTY).toLowerCase();
			Class<?> clazz = obj.getClass();
			try {
				if (obj instanceof java.util.Map) {
					Method method = clazz.getMethod("put", Object.class, Object.class);
					method.invoke(obj, fieldName, value);
					return;
				}
				PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
				PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
				for (int i = 0, length = descriptors.length; i < length; i++) {
					String name = descriptors[i].getName();
					String lowerCaseName = name.toLowerCase();
					if (!StringUtils.equals(name, Constant.CLASS_EXT) && lowerCaseName.equals(beanName)) {
						propertyUtilsBean.setNestedProperty(obj, name, value);
					}
				}
			} catch (IllegalAccessException | SecurityException | NoSuchMethodException | InvocationTargetException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	public static Map<String, Object> beanToMap(Object obj) {
		return beanToMap(obj, null);
	}

	public static <T extends IdModel> List<Map<String, Object>> listBeanToMap(List<T> list, TimeFormat format,
			ExcludeField ex) {
		List<Map<String, Object>> listMap = Lists.newArrayList();
		for (T t : list) {
			Class<? extends Object> clazz = t.getClass();
			List<Field> fields = getFields(clazz);
			listMap.add(findIncludeFields(fields, ex, format, t));
		}
		return listMap;
	}

	public static <T extends IdModel> Map<String, Object> beanToMap(T bean, TimeFormat format, ExcludeField ex) {
		List<Map<String, Object>> listMap = Lists.newArrayList();
		if (bean == null)
			return null;
		List<T> list = Lists.newArrayList();
		list.add(bean);
		for (T t : list) {
			Class<? extends Object> clazz = t.getClass();
			List<Field> fields = getFields(clazz);
			listMap.add(findIncludeFields(fields, ex, format, t));
		}
		return listMap.get(0);
	}

	private static <T> Map<String, Object> findIncludeFields(List<Field> fields, ExcludeField ex, TimeFormat format,
			T obj) {
		Map<String, Object> map = Maps.newHashMap();
		for (Field field : fields) {
			Comment comment = field.getAnnotation(Comment.class);
			if (comment != null) {
				Set<ExcludeField> excludes = Sets.newHashSet(comment.exclude());
				if (ex == null || (!excludes.contains(ex) && !excludes.contains(ExcludeField.ALWAYS))) {
					Object value = getFieldValue(field, obj);
					map.put(field.getName(), castValue(value, format));
				}
			}
		}
		return map;
	}

	public static Map<String, Object> beanToMap(Object obj, TimeFormat format) {
		Map<String, Object> params = Maps.newHashMap();
		try {
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
			PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
			for (int i = 0; i < descriptors.length; i++) {
				String name = descriptors[i].getName();
				if (!StringUtils.equals(name, Constant.CLASS_EXT)) {
					Object value = propertyUtilsBean.getNestedProperty(obj, name);
					params.put(name, castValue(value, format));
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		return params;
	}

	// get Fields from class that include super.
	public static List<Field> getFields(Class<?> clazz, String... exculde) {
		List<Field> fields = Lists.newArrayList();
		Set<String> sets = Sets.newHashSet();
		for (String ex : exculde) {
			sets.add(ex.toLowerCase());
		}
		findFields(clazz, fields, sets);
		return fields;
	}

	// get Fields name.
	public static List<String> getFieldNames(Class<?> clazz) {
		PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
		PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(clazz);
		List<String> fieldNames = Lists.newArrayList();
		for (PropertyDescriptor field : descriptors) {
			fieldNames.add(field.getName());
		}
		return fieldNames;
	}

	// get Fields name.
	public static String[] getExculdeFieldNames(Class<?> clazz, ExcludeField exclude) {
		List<String> fieldNames = Lists.newArrayList();
		List<Field> fields = getFields(clazz);
		for (Field field : fields) {
			Comment comment = field.getAnnotation(Comment.class);
			if (comment != null) {
				ExcludeField[] dt = comment.exclude();
				List<ExcludeField> array = Arrays.asList(dt);
				if (array.contains(exclude) || array.contains(ExcludeField.ALWAYS)) {
					fieldNames.add(field.getName());
				}
			}
		}
		return fieldNames.toArray(new String[0]);
	}

	// get Fields name.
	public static List<String> getFieldNames(Class<?> clazz, String... exculde) {
		List<String> fieldNames = Lists.newArrayList();
		List<Field> fields = getFields(clazz, exculde);
		for (Field field : fields) {
			fieldNames.add(field.getName());
		}
		return fieldNames;
	}

	public static Map<String, Field> getFieldMap(Class<?> clazz) {
		return convert2FieldMap(getFields(clazz));
	}

	public static Map<String, Field> convert2FieldMap(List<Field> fields) {
		Map<String, Field> fieldMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(fields)) {
			for (Field field : fields) {
				fieldMap.put(field.getName().toLowerCase(), field);
			}
		}
		return fieldMap;
	}

	private static void findFields(Class<?> clazz, List<Field> fields, Set<String> exculdes) {
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null && superClass != Object.class) {
			findFields(superClass, fields, exculdes);
		}
		Field[] thisFields = clazz.getDeclaredFields();
		for (Field field : thisFields) {
			if (!exculdes.contains(field.getName().toLowerCase()) && !Modifier.isStatic(field.getModifiers()))
				fields.add(field);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Field field, Object obj) {
		try {
			return (T) FieldUtils.readField(field, obj, true);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static String getCommentValue(Field field) {
		Comment annotation = field.getAnnotation(Comment.class);
		if (annotation != null) {
			return annotation.value();
		}
		return field.getName();
	}

	public static <T> Map<String, Object> getNotEmptyFields(Class<T> clazz, Object obj) {
		Map<String, Object> map = Maps.newHashMap();
		if (obj != null) {
			List<Field> fields = getFields(clazz);
			for (Field f : fields) {
				Object o = getFieldValue(f, obj);
				if (o != null) {
					if (o instanceof String && StringUtils.isBlank(o.toString())) {
						continue;
					}
					map.put(f.getName(), o);
				}
			}
		}
		return map;
	}

	public static <T> void checkNotEmpty(Class<T> clazz, Object obj, CheckFieldType type) {
		List<Field> fields = getFields(clazz);
		for (Field field : fields) {
			Comment comment = field.getAnnotation(Comment.class);
			if (comment != null) {
				CheckFieldType[] dt = comment.type();
				List<CheckFieldType> array = Arrays.asList(dt);
				if (array.contains(type) || CheckFieldType.ALL == type) {
					castValue(field, obj);
				}
			}
		}
	}

	public static void checkNotEmpty(Object obj, List<Field> fields) {
		for (Field field : fields) {
			castValue(field, obj);
		}
	}

	public static void checkNotEmpty(Object obj) {
		checkNotEmpty(obj, getFields(obj.getClass()));
	}

	private static void castValue(Field field, Object obj) {
		Object value = getFieldValue(field, obj);
		String comment = getCommentValue(field);
		if (value instanceof String) {
			Validate.notBlank((String) value, Constant.CHECK_MESSAGE, comment);
		} else {
			Validate.notNull(value, Constant.CHECK_MESSAGE, comment);
		}
	}

	private static Object castValue(Object obj, TimeFormat format) {
		if (obj instanceof Date && format != null) {
			FastDateFormat fdf = FastDateFormat.getInstance(format.getPattern());
			return fdf.format(obj);
		}
		return obj;
	}
}
