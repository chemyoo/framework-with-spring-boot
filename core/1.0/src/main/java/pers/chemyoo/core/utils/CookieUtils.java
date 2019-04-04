package pers.chemyoo.core.utils;
import javax.servlet.http.Cookie;

import org.apache.commons.codec.digest.DigestUtils;

/** 
 * @author Author : jianqing.liu
 * @version version : created time：2018年11月6日 下午6:06:10 
 * @since since from 2018年11月6日 下午6:06:10 to now.
 * @description class description
 */
public class CookieUtils {
	
	private CookieUtils() {}
	
	private static Cookie cookie = null;
	
	public static Cookie getCookie() {
		if(cookie != null)
			return cookie;
		setCookie();
		return cookie;
	}
	
	private static void setCookie() {
		cookie = new Cookie("JSESSIONID", 
				DigestUtils.md5Hex(DateUtils.getCurrentTime().toString()).toUpperCase());
		cookie.setComment("Web Cookies");
		cookie.setMaxAge(24 * 60 * 60);
		cookie.setPath("/");
	}
	
}
