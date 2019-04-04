package pers.chemyoo.core.utils;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/** 
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月14日 下午3:24:30 
 * @since since from 2019年3月14日 下午3:24:30 to now.
 * @description class description
 */
@Data
public class AccessToken implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4733384018283867484L;

	private String value;
	
	private Date expire = DateUtils.getCurrentTime();
	
}
