package pers.chemyoo.core.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import pers.chemyoo.core.setting.exception.CustomException;

/**
 * @author Author : jianqing.liu
 * @version version : created time：2019年3月5日 上午9:25:14
 * @since since from 2019年3月5日 上午9:25:14 to now.
 * @description class description
 */
@Slf4j
public class WeChatUtils {

	private WeChatUtils() {
	}

	private static final Properties PROPS = PropertiesUtil.getInstance();

	private static final String TOKEN_URL = PROPS.getProperty("token.url");

	private static final String USER_URL = PROPS.getProperty("user.url");

	private static final String OPEN_ID_URL = PROPS.getProperty("openid.url");

	private static final String APP_ID = PROPS.getProperty("wx.app.id");

	private static final String SECRET = PROPS.getProperty("wx.secret");

	private static final String OPEN_ID_KEY = "openid";

	private static AccessToken accessToken;

	private static final String WORK_FOLDER = PropertiesUtil.getWorkFolder();

	private static final File FILE = new File(WORK_FOLDER + File.separator + "accessToken.auth");

	public static String getToken(String appId, String secret) {
		if (StringUtils.isAnyBlank(appId, secret)) {
			appId = APP_ID;
			secret = SECRET;
		}
		// 若token过期，则重新请求一个新token
		if (!checkToken(appId, secret)) {
			try {
				getNewAccessToken(appId, secret);
			} catch (IOException e) {
				throw new CustomException(e.getMessage(), e);
			}
		}
		return accessToken.getValue();
	}

	private static boolean checkToken(String appId, String secret) {
		if (accessToken == null) {
			// accessToken == null 则初始化一个
			initAccessToken(appId, secret);
		}
		Date expire = accessToken.getExpire();
		String value = accessToken.getValue();
		Date now = DateUtils.getCurrentTime();
		return (StringUtils.isNotBlank(value) && now.before(expire));
	}

	private static void getNewAccessToken(String appId, String secret) throws IOException {
		accessToken = new AccessToken();
		log.info("\n------------------>重新获取token...");
		String accessTokenKey = "access_token";
		String url = repalceChars(TOKEN_URL, appId, secret);
		JSONObject json = ConnectApi.connect(url);
		if (!json.has(accessTokenKey)) {
			log.info("获取accessToken失败", json.toString());
			Validate.isTrue(false, "获取accessToken失败：%s", json.toString());
		}
		accessToken.setValue(json.getString(accessTokenKey));
		// 有效时间为微信有效时间
		accessToken.setExpire(new Date(json.getInt("expires_in") * 1000 + System.currentTimeMillis()));
		if (!FILE.exists()) {
			FILE.getParentFile().mkdirs();
		}
		FileUtils.writeByteArrayToFile(FILE, ProtoStuffUtil.serialize(accessToken));
	}

	private static void initAccessToken(String appId, String secret) {
		try {
			// 先从本地文件缓存获取token对象，如果本地没有则请求一个新的
			if (FILE.exists()) {
				byte[] bytes = FileUtils.readFileToByteArray(FILE);
				accessToken = ProtoStuffUtil.deserialize(bytes, AccessToken.class);
			} else {
				getNewAccessToken(appId, secret);
			}
		} catch (IOException e) {
			throw new CustomException(e.getMessage(), e);
		}
	}

	public static int subscribeStatus(String openId, String appId, String secret) {
		if (StringUtils.isAnyBlank(appId, secret)) {
			appId = APP_ID;
			secret = SECRET;
		}
		log.info("appId = {}, secret = {}", APP_ID, SECRET);
		String token = getToken(appId, secret);
		String url = repalceChars(USER_URL, token, openId);
		JSONObject json = ConnectApi.connect(url);
		String key = "subscribe";
		if (!json.has(key)) {
			try {
				log.info("获取关注公众号状态失败", json.toString());
				// 若accessToken 超时，则重新获取
				String errcode = json.getString("errcode") + ",";
				List<String> codes = Arrays.asList("42001", "40003", "40001");
				if (codes.contains(errcode)) {
					log.info("正在重新尝试获取关注公众号状态...");
					getNewAccessToken(appId, secret);
					return subscribeStatus(openId, appId, secret);
				}
			} catch (IOException e) {
				log.info("重新尝试获取关注公众号状态失败：", e);
			}
			Validate.isTrue(false, "获取关注公众号状态失败：%s", json.toString());
		}
		return json.getInt("subscribe");
	}

	public static String getOpenId(String code, String appId, String secret) {
		if (StringUtils.isAnyBlank(appId, secret)) {
			appId = APP_ID;
			secret = SECRET;
		}
		log.info("appId = {}, secret = {}", APP_ID, SECRET);
		String url = repalceChars(OPEN_ID_URL, appId, secret, code);
		JSONObject json = ConnectApi.connect(url);
		if (!json.has(OPEN_ID_KEY)) {
			log.info("用户凭证获取失败", json.toString());
			Validate.isTrue(false, "用户凭证获取失败：%s", json.toString());
		}
		return json.getString(OPEN_ID_KEY);
	}

	private static String repalceChars(String str, String... target) {
		int index = 1;
		String temp = str;
		for (String s : target) {
			temp = temp.replace("{" + index + "}", s);
			index++;
		}
		return temp;
	}

}
