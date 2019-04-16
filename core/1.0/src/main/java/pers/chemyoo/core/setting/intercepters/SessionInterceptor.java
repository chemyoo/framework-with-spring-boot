package pers.chemyoo.core.setting.intercepters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import pers.chemyoo.core.setting.exception.CustomException;
import pers.chemyoo.core.setting.handler.CustomExceptionHandler;
import pers.chemyoo.core.utils.ApplicationContextUtils;
import pers.chemyoo.core.utils.Constant;
import pers.chemyoo.core.utils.DateUtils;

@Slf4j
public class SessionInterceptor implements HandlerInterceptor {
	
	// 创建缓存对象,如果存在集群可以缓存Redis
	private static final Cache<String, Object> CACHES = CacheBuilder.newBuilder()
            // 最大缓存个数
            .maximumSize(10000)
            // 设置写缓存过期时间
            .expireAfterWrite(600, TimeUnit.MILLISECONDS)
            .build();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String key = this.keyBuilder(request);
		// 同浏览器、接口、参数的情况会被认为重复请求
		if(CACHES.getIfPresent(key) != null) {
			CustomExceptionHandler exHandler = ApplicationContextUtils.getBean(CustomExceptionHandler.class);
			CustomException customException = new CustomException(Constant.CUSTOM_CODE, "您操作频率过快，请不要重复操作！");
			Object obj = exHandler.exceptionHandler(customException);
			// 将提示信息输出到浏览器终端
			return this.writer(response, obj);
		}
		CACHES.put(key, DateUtils.getCurrentTime());
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
		// empty
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		// empty
	}
	
	/**
	 * 生成缓存主键，存在hash碰撞，但几率十分小，可以忽略
	 * @param request
	 * @return
	 */
	private String keyBuilder(HttpServletRequest request) {
		HttpSession session = request.getSession();
		log.info("Current sessionId is {} ", session.getId());
		request.getParameterMap();
		StringBuilder buidler = new StringBuilder(session.getId()).append(session.getId());
		for(Map.Entry<String, String[]> param : request.getParameterMap().entrySet()) {
			buidler.append(param.getKey()).append("=").append(toArrayString(param.getValue()));
		}
		return DigestUtils.md5Hex(buidler.toString());
	}
	
	private String toArrayString(String[] array) {
		if(array == null) {
			return StringUtils.EMPTY;
		}
		StringBuilder buidler = new StringBuilder();
		for(String str : array) {
			buidler.append(str).append(",");
		}
		return buidler.toString();
	}
	
	/**
	 * 将提示信息通过 response 流向 浏览器
	 * @param response
	 * @param obj
	 * @return
	 */
	private boolean writer(HttpServletResponse response, Object obj) {
		response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
		try(PrintWriter out = response.getWriter()) {
	        JSONObject res = JSONObject.fromObject(obj);
	        out.write(res.toString());
	        return false;
		} catch (IOException e) {
			// ignore
		}
		return true;
	}
}
