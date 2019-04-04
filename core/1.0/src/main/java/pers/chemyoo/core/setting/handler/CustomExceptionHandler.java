package pers.chemyoo.core.setting.handler;

import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import pers.chemyoo.core.enums.ResponseCodes;
import pers.chemyoo.core.setting.exception.CustomException;
import pers.chemyoo.core.utils.ResultsUtils;

/** 
 * @author Author : jianqing.liu
 * @version version : created time：2019年2月25日 下午3:58:13 
 * @since since from 2019年2月25日 下午3:58:13 to now.
 * @description Catch thrown exception in System.
 */
@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {
	
	private static final String VALIDATE_CLASS = "Validate";
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public Object exceptionHandler(Exception e) {
		Map<String,Object> message = null;
		
		// print exception stackTrace
		log.error("System thrown exception, that is stackTrace information：", e);
		
		// determine exception type
		String throwClassName = e.getStackTrace()[0].getClassName();
		boolean isValidate = throwClassName.endsWith(VALIDATE_CLASS);
		boolean duplicate = false;
		if(e.getCause() != null) {
			duplicate = e.getCause().getMessage().startsWith("Duplicate entry");
		}
		if(isValidate || e instanceof IllegalArgumentException || e instanceof CustomException) {
			message = ResultsUtils.error(ResponseCodes.PARAMTER_ERROR, e.getMessage());
		} else if(duplicate) {
			message = ResultsUtils.error(ResponseCodes.DUPLICATE);
		} else {
			message = ResultsUtils.error(ResponseCodes.SERVICE_INTERNAL_ERROR);
		}
		
		// return message to web page.
		return message;
	}

}
