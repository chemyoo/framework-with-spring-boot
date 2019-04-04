package pers.chemyoo.core.setting.exception;

public class CustomException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private final String code;

	public String getCode() {
		return code;
	}

	// 无参构造方法
	public CustomException() {
		super();
		this.code = "500";
	}
	

	public CustomException(String code) {
		super();
		this.code = code;
	}

	// 有参的构造方法
	public CustomException(String code, String message) {
		super(message);
		this.code = code;
	}

	// 用指定的详细信息和原因构造一个新的异常
	public CustomException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	
	// 用指定的详细信息和原因构造一个新的异常
	public CustomException(String message, Throwable cause) {
		super(message, cause);
		this.code = "500";
	}

}
