package pers.chemyoo.core.setting.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import pers.chemyoo.core.utils.CookieUtils;

//@ServletComponentScan,如果默认配置被覆盖,主类上要加扫描注解，才会生效。
//SpringBoot是从主入口包往内扫描，注意不要放到入口包外，特别注意。
@Slf4j
@Configuration
@WebFilter(urlPatterns = "/*")
public class AllowFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException

	{
		HttpServletResponse rsp = (HttpServletResponse) response;
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String originUrl = getOrigin(httpServletRequest.getHeader("Origin"));
		
		log.info("访问接口：【{}】", httpServletRequest.getRequestURI());
		
		rsp.setHeader("Access-Control-Allow-Origin", originUrl); // 解决跨域访问报错
		rsp.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		rsp.setHeader("Access-Control-Max-Age", "3600"); // 设置过期时间
		rsp.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, client_id, uuid, Authorization");
		rsp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // 支持HTTP 1.1.
		rsp.setHeader("Pragma", "no-cache"); // 支持HTTP 1.0. response.setHeader("Expires", "0")
		rsp.setHeader("Set-Cookie", "dyping"); // 支持HTTP 1.0. response.setHeader("Expires", "0")
		rsp.setHeader("Access-Control-Allow-Credentials", "true");
		rsp.setHeader("Origin", originUrl);
		rsp.addCookie(CookieUtils.getCookie());
		chain.doFilter(request, response);
	}
	
	private String getOrigin(String origin) {
    	if(StringUtils.isBlank(origin)) {
    		return "/*";
    	}
    	return origin;
    }

	@Override
	public void init(FilterConfig filterConfig) {
		// Empty
	}

	@Override
	public void destroy() {
		// Empty
	}
}
