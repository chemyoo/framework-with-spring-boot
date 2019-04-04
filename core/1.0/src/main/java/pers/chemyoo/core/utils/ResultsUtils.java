package pers.chemyoo.core.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import pers.chemyoo.core.enums.ResponseCodes;

/**
 * @author 作者 : jianqing.liu
 * @version 创建时间：2018年1月2日 下午3:53:43
 * @since 2018年1月2日 下午3:53:43
 * @description 结果返回集
 */
@Slf4j
public class ResultsUtils {
	private ResultsUtils() {
	}

	private static final String DESCRIPTION = "CODE ERROR --> ResultCode.SUCCESS 返回码只用于返回结果"
			+ "正确的情况，即Result.success()方法返回，请检查参数！";

	public static final String RESULTS = "results";

	public static final String RESULT = "result";

	public static final String TOTAL = "total";

	public static Map<String, Object> success() {
		Map<String, Object> map = Maps.newHashMap();
		map.put(ResponseCodes.CODE, ResponseCodes.SUCCESS.getResultCode());
		return map;
	}

	public static Map<String, Object> success(String description) {
		Map<String, Object> map = Maps.newHashMap();
		if (StringUtils.isEmpty(description)) {
			description = ResponseCodes.SUCCESS.getDescription();
		}
		map.put(ResponseCodes.CODE, ResponseCodes.SUCCESS.getResultCode());
		map.put(ResponseCodes.DESC, description);
		return map;
	}

	public static <T> Map<String, Object> success(List<T> results) {
		Map<String, Object> map = Maps.newHashMap();
		map.put(ResponseCodes.CODE, ResponseCodes.SUCCESS.getResultCode());
		map.put(RESULTS, results);
		return map;
	}

	public static <T> Map<String, Object> success(List<T> results, long total) {
		Map<String, Object> map = Maps.newHashMap();
		map.put(ResponseCodes.CODE, ResponseCodes.SUCCESS.getResultCode());
		map.put(RESULTS, results);
		map.put(TOTAL, total);
		return map;
	}

	public static <T> Map<String, Object> success(String resultsKey, List<T> results, long total, String totalKey) {
		Map<String, Object> map = Maps.newHashMap();
		map.put(ResponseCodes.CODE, ResponseCodes.SUCCESS.getResultCode());
		map.put(resultsKey, results);
		map.put(totalKey, total);
		return map;
	}

	public static <T> Map<String, Object> success(T result) {
		Map<String, Object> map = Maps.newHashMap();
		map.put(ResponseCodes.CODE, ResponseCodes.SUCCESS.getResultCode());
		map.put(RESULT, result);
		return map;
	}

	public static <T> Map<String, Object> success(T result, String description) {
		Map<String, Object> map = Maps.newHashMap();
		map.put(ResponseCodes.CODE, ResponseCodes.SUCCESS.getResultCode());
		map.put(RESULT, result);
		map.put(ResponseCodes.DESC, description);
		return map;
	}

	public static Map<String, Object> error(ResponseCodes responseCode) {
		if (!checkResultCode(responseCode)) {
			return errorResultCode();
		}

		Map<String, Object> map = Maps.newHashMap();
		map.put(ResponseCodes.CODE, responseCode.getResultCode());
		map.put(ResponseCodes.DESC, responseCode.getDescription());
		return map;
	}

	public static Map<String, Object> error(ResponseCodes responseCode, String description) {
		if (!checkResultCode(responseCode)) {
			return errorResultCode();
		}

		if (responseCode != ResponseCodes.NO_AUTHORIZATION && StringUtils.isNotEmpty(description)
				&& (description.contains("无访问权限") || description.contains("无权限"))) {
			responseCode = ResponseCodes.NO_AUTHORIZATION;
		}

		Map<String, Object> map = Maps.newHashMap();
		map.put(ResponseCodes.CODE, responseCode.getResultCode());
		if (description == null || description.isEmpty()) {
			description = responseCode.getDescription();
		}
		map.put(ResponseCodes.DESC, description);
		return map;
	}

	private static boolean checkResultCode(ResponseCodes responseCode) {
		return (responseCode != ResponseCodes.SUCCESS);
	}

	private static Map<String, Object> errorResultCode() {
		Map<String, Object> map = Maps.newHashMap();
		map.put(ResponseCodes.DESC, "Error result code.");
		map.put(ResponseCodes.CODE, ResponseCodes.SERVICE_INTERNAL_ERROR.getResultCode());
		log.error(getLineSeparator() + DESCRIPTION);
		return map;
	}

	/**
	 * 换行符
	 * 
	 * @return System.lineSeparator();
	 */
	private static String getLineSeparator() {
		return System.getProperty("line.separator");
	}

}
