package pers.chemyoo.core.entity;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;

import lombok.Data;
import pers.chemyoo.core.annotations.Comment;
import pers.chemyoo.core.annotations.Sid;
import pers.chemyoo.core.enums.CheckFieldType;
import pers.chemyoo.core.enums.ExcludeField;
import pers.chemyoo.core.utils.AttributesUtils;
import pers.chemyoo.core.utils.DateUtils;

/**
 * @author Author : jianqing.liu
 * @version version : created time：2019年2月27日 下午3:35:27
 * @since since from 2019年2月27日 下午3:35:27 to now.
 * @description class description
 */
@Data
@MappedSuperclass
public class IdModel {

	public static final String SID = "id";

	public static final String LMT = "lastModifiedTime";

	@Sid
	@Comment("流水号")
	@Column(name = "id", type = MySqlTypeConstant.VARCHAR, length = 18, isKey = true)
	private String id;

	@JsonIgnore
	@Comment(value = "最后修改时间", exclude = { ExcludeField.ALWAYS })
	@Column(name = "last_mod_time", type = MySqlTypeConstant.DATETIME, isNull = false)
	private Date lastModTime;

	/**
	 * 检查除SID和LastModifiedTime外的字段不能为空 Not null or not empty.
	 */
	public void selfCheck() {
		List<Field> fields = AttributesUtils.getFields(getClass(), SID, LMT);
		AttributesUtils.checkNotEmpty(this, fields);
	}

	/**
	 * 检查除SID和LastModifiedTime外的字段不能为空 Not null or not empty.
	 */
	public void selfCheck(boolean setModifiedTime) {
		this.setModifiedTime(setModifiedTime);
		this.selfCheck();
	}

	/**
	 * 自查指定检测类型字段值是否为空
	 * <p>
	 * Not null or not empty.
	 * </p>
	 * 
	 * @param type
	 *            {@link CheckFieldType}
	 */
	public void selfCheck(CheckFieldType type) {
		AttributesUtils.checkNotEmpty(getClass(), this, type);
	}

	/**
	 * 自查指定检测类型字段值是否为空
	 * <p>
	 * Not null or not empty.
	 * </p>
	 * 
	 * @param type
	 *            {@link CheckFieldType}
	 */
	public void selfCheck(CheckFieldType type, boolean setModifiedTime) {
		this.setModifiedTime(setModifiedTime);
		this.selfCheck(type);
	}

	private void setModifiedTime(boolean setModifiedTime) {
		if (setModifiedTime)
			this.setLastModTime(DateUtils.getCurrentTime());
	}

}
