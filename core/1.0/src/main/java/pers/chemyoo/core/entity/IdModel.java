package pers.chemyoo.core.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import pers.chemyoo.core.annotations.Comment;
import pers.chemyoo.core.annotations.Sid;
import pers.chemyoo.core.enums.ExcludeField;

/**
 * @author Author : jianqing.liu
 * @version version : created time：2019年2月27日 下午3:35:27
 * @since since from 2019年2月27日 下午3:35:27 to now.
 * @description class description
 */
@Data
public class IdModel {

	@Sid
	@Comment("流水号")
	private String id;

	@JsonIgnore
	@Comment(value = "最后修改时间", exclude = { ExcludeField.ALWAYS })
	private Date lastModTime;

}
