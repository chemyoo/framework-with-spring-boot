package pers.chemyoo.core.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/** 
 * @author 作者 : jianqing.liu
 * @version 创建时间：2018年6月25日 下午5:47:04 
 * @since 2018年6月25日 下午5:47:04 
 * @description 连接微信认证服务
 */
@Slf4j
@Component
public class ConnectApi {
	
	private ConnectApi() {}
	
	private static final String UTF_8 = "utf-8";
	
	private static final String POST_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
	
	/**
	 * @return JSONObject
	 */
	public static JSONObject connect(String apiUrl) {
		String result = "";
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int timeout = (int) TimeUnit.SECONDS.toMillis(30);
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            if (conn.getResponseCode() == HttpStatus.OK.value()) {
                result = IOUtils.toString(conn.getInputStream(), UTF_8);
            }
            conn.disconnect();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
		return JSONObject.fromObject(result);
	}
	
	/**
	 * @param token 
	 * @param json
	 * @return String
	 */
	public static String postMessage(String token, JSONObject json) {
		String result = "";
        try {
            URL url = new URL(POST_URL + token);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            int timeout = (int) TimeUnit.SECONDS.toMillis(30);
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setRequestProperty("Connection", "Keep-Alive");//保持长链接
            conn.setRequestProperty("Charset", UTF_8);
            // 设置文件类型:
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            // 设置接收类型否则返回415错误
            conn.setRequestProperty("accept","application/json");
            // 往服务器里面发送数据
            if (json != null && !json.isEmpty()) {
                byte[] writebytes = json.toString().getBytes(UTF_8);
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(writebytes);
                outwritestream.flush();
                outwritestream.close();
            }
            if (conn.getResponseCode() == HttpStatus.OK.value()) {
                result = IOUtils.toString(conn.getInputStream(), UTF_8);
            }
            log.info("doJsonPost: conn status:" + conn.getResponseCode());
            conn.disconnect();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
		return result;
	}
	
}
