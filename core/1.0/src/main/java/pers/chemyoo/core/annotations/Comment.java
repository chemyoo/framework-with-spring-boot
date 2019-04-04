package pers.chemyoo.core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pers.chemyoo.core.enums.CheckFieldType;
import pers.chemyoo.core.enums.ExcludeField;

/** 
 * @author 作者 : jianqing.liu
 * @version 创建时间：2018年6月7日 下午1:58:49 
 * @since 2018年6月7日 下午1:58:49 
 * @description 注释内容注解 
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Comment {
	
	String value() default ""; 
	
	CheckFieldType[] type() default {CheckFieldType.ALL};
	
	ExcludeField[] exclude() default {ExcludeField.NEVER};
}
