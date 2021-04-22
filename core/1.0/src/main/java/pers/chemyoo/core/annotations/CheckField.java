package pers.chemyoo.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pers.chemyoo.core.enums.CheckGroups;
import pers.chemyoo.core.enums.CheckType;

/** 
 * @author 作者 : jianqing.liu
 * @version 创建时间：2018年6月7日 下午1:58:49 
 * @since 2018年6月7日 下午1:58:49 
 * @description VO值校验
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CheckField
{
	/**
	 * 字段名称
	 * @return
	 */
	String value() default ""; 
	
	/**
	 * 属性值长度
	 * @return
	 */
	int length() default 0;
	
	/**
	 * 检验失败时返回的消息提示
	 * @return
	 */
	String message() default "";
	
	/**
	 * 校验类型
	 * @return
	 */
	CheckType type() default CheckType.NONE;
	
	/**
	 * 校验分组
	 * @return
	 */
	CheckGroups[] groups() default CheckGroups.NONE;
	
}
