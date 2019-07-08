package pers.chemyoo.core.setting.aspecter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class CommAspecter {

	// Point cut of aspecter.
	@Pointcut("execution( * pers.chemyoo.core.controller.*.*(..))  && "
			+ "(@annotation(org.springframework.web.bind.annotation.RequestMapping))")

	public void excudeService() {
		// Empty.
	}
	
	// Point cut of aspecter.
	@Pointcut("execution( * pers.chemyoo.core.mapper.*.insert*(..)) || "
			+ "execution( * pers.chemyoo.core.mapper.*.update*(..)) || "
			+ "execution( * pers.chemyoo.core.mapper.*.save*(..))")

	public void includeService() {
		// Empty.
	}

//	@Around("excudeService()")
//	public Object twiceAsOld(ProceedingJoinPoint thisJoinPoint) throws Throwable 
//	return thisJoinPoint.proceed()

//	@After("includeService()")
//	public void afterSave() 
//		log.info("afterSave：开始执行缓存清理------------------------------")
//		DataCacheUtils.clearAllCache()

	@After("excudeService()")
	public void after(JoinPoint point) {
		log.info("@After：切面执行完成，执行参数：{}",  point.getArgs());
	}
	
//	@AfterReturning("excudeService()")
//  public void afterReturning(JoinPoint point)
}
