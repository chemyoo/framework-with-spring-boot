package pers.chemyoo.core.utils;

import java.util.Map;

/** 
 * @author Author : jianqing.liu
 * @version version : created time：2019年2月28日 下午4:55:41 
 * @since since from 2019年2月28日 下午4:55:41 to now.
 * @description class description
 */
public class MapUtils {
	
	private MapUtils() {}
	
	@SafeVarargs
	public static <T> void removeValuesByKeys(Map<T, Object> map, T... keys) {
		if(map != null) {
			for(T key : keys) {
				map.remove(key);
			}
		}
	}
}
