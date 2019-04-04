package pers.chemyoo.core.utils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.json.JSONException;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.google.common.collect.Maps;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月27日 下午1:38:11
 * @since since from 2019年3月27日 下午1:38:11 to now.
 * @description 短信验证码工具类
 */
@Slf4j
public class MessageUtils {

	// 短信应用SDK AppID
	private static final int SDK_APPID = PropertiesUtil.getProperty("sms.sdk.appid", Integer.class);

	// 短信应用SDK AppKey
	private static final String SDK_APPKEY = PropertiesUtil.getProperty("sms.sdk.appkey", String.class);

	// 短信模板ID，需要在短信应用中申请
	// templateId7839对应的内容是"您的验证码是: {1}"
	// 模板ID需要在短信控制台中申请
	private static final int TEMPLATE_ID = PropertiesUtil.getProperty("sms.template.id", Integer.class);

	// 签名
	// 真实的签名需要在短信控制台中申请，
	// 另外签名参数使用的是`签名内容`，而不是`签名ID`
	private static final String SMS_SIGN = PropertiesUtil.getProperty("sms.sign", String.class);

	private static final String CODE = "000000";

	private static final Random random = new Random();

	private static final String PHONE_NUM_CK = "手机号码格式不正确：%s";

	private static final Integer MINUTE = 5;

	private static final long EXPIRE = TimeUnit.MINUTES.toMillis(MINUTE);

	private static Map<String, VerifyCodeCache> verifyCode = Maps.newConcurrentMap();

	private static DelayQueue<VerifyCodeCache> delayQueue = new DelayQueue<>();

	private MessageUtils() {

	}

	static {
		// 及时清理过期验证码
		startRemoveItem();
	}

	public static MessageResult sendMessage(String phoneNumber) {
		Validate.isTrue(Pattern.matches("^(1[3,4,5,7,8][0-9])\\d{8}$", phoneNumber), PHONE_NUM_CK, phoneNumber);
		MessageResult message = new MessageResult(getCode());

		// 数组具体的元素个数和模板中变量个数必须一致，例如事例中templateId:5678对应一个变量
		// 参数数组中元素个数也必须是一个
		String[] params = { message.code, MINUTE.toString() };
		SmsSingleSender ssender = new SmsSingleSender(SDK_APPID, SDK_APPKEY);
		try {
			// 签名参数未提供或者为空时，会使用默认签名发送短信
			SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber, TEMPLATE_ID, params, SMS_SIGN, "",
					"");
			convert(result, message);
			log.info("短信发送结果：{}", message);
			if (message.isSuccess()) {
				createCodeCache(phoneNumber, message.getCode());
			}
		} catch (HTTPException | JSONException | IOException e) {
			log.error(e.getMessage(), e);
		}
		return message;
	}

	private static String getCode() {
		String temp = new StringBuilder(CODE).append(random.nextInt(999999)).toString();
		return temp.substring(temp.length() - 6);
	}

	@Data
	public static class MessageResult {
		boolean success = false;
		String errMsg;
		String ext;
		String sid;
		int fee;
		String code;

		public MessageResult(String code) {
			this.code = code;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("{result:");
			builder.append(success);
			builder.append(", errMsg:");
			builder.append(errMsg);
			builder.append(", ext:");
			builder.append(ext);
			builder.append(", sid:");
			builder.append(sid);
			builder.append(", fee:");
			builder.append(fee);
			builder.append(", code:");
			builder.append(code);
			builder.append("}");
			return builder.toString();
		}

	}

	private static void convert(SmsSingleSenderResult result, MessageResult message) {
		message.errMsg = result.errMsg;
		message.ext = result.ext;
		message.fee = result.fee;
		message.success = result.result == 0;
		message.sid = result.sid;
	}

	@Data
	public static class VerifyCodeCache implements Delayed {
		String code;
		String phoneNumber;
		Date expireDate = new Date(System.currentTimeMillis() + EXPIRE);

		VerifyCodeCache(String code, String phoneNumber) {
			this.code = code;
			this.phoneNumber = phoneNumber;
		}

		@Override
		public int compareTo(Delayed o) {
			return (int) (this.getDelay(TimeUnit.MINUTES) - o.getDelay(TimeUnit.MINUTES));
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return unit.convert(this.expireDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("{code:");
			builder.append(code);
			builder.append(", phoneNumber:");
			builder.append(phoneNumber);
			builder.append(", expireDate:");
			builder.append(expireDate);
			builder.append("}");
			return builder.toString();
		}

	}

	public static void createCodeCache(String phoneNum, String code) {
		VerifyCodeCache cache = new VerifyCodeCache(code, phoneNum);
		delayQueue.add(cache);
		verifyCode.put(phoneNum, cache);
	}

	private static String getVCode(String phoneNum) {
		VerifyCodeCache vcode = verifyCode.get(phoneNum);
		if (vcode == null || DateUtils.getCurrentTime().after(vcode.getExpireDate())) {
			return null;
		}
		return vcode.getCode();
	}

	public static boolean verify(String vcode, String phoneNum) {
		if (StringUtils.isNotBlank(vcode) && vcode.equals(getVCode(phoneNum))) {
			removeCode(phoneNum);
			return true;
		} else {
			return false;
		}
	}

	private static void removeCode(String phoneNum) {
		verifyCode.remove(phoneNum);
	}

	private static void startRemoveItem() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						if (!delayQueue.isEmpty()) {
							VerifyCodeCache cache = delayQueue.take();
							log.info("remove item :" + cache);
							verifyCode.remove(cache.getPhoneNumber(), cache);
						} else {
							TimeUnit.MINUTES.sleep(MINUTE);
						}
					} catch (InterruptedException ex) {
						log.error(ex.getMessage(), ex);
						// restore interrupted state...
						Thread.currentThread().interrupt();
					}
				}
			}
		}.start();
	}

}
