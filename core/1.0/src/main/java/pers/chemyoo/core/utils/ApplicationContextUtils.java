package pers.chemyoo.core.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * Spring ApplicationContext.用于获取被Spring管理的Bean.
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
	
    private static final Context CONTEXT = new Context();
 
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        CONTEXT.setApplicationContext(applicationContext);
    }
    
    public static ApplicationContext getApplicationContext() {
        return CONTEXT.getApplicationContext();
    }
    
    public static <T> T getBean(Class<T> clazz) {
    	return CONTEXT.getApplicationContext().getBean(clazz);
    }
    
    @Data
    static class Context {
    	private ApplicationContext applicationContext;
    }
}
