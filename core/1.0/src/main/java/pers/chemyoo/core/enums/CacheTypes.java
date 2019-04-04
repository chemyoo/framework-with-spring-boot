package pers.chemyoo.core.enums;
/** 
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月20日 下午8:39:08 
 * @since since from 2019年3月20日 下午8:39:08 to now.
 * @description class description
 */
public enum CacheTypes {
	WINDOW_CACHE("queWindow");
	
	CacheTypes(String clazzName){
		this.clazzName = clazzName;
	}
	
	private String clazzName;
	
	public String getClazzName() {
		return this.clazzName;
	}
}
