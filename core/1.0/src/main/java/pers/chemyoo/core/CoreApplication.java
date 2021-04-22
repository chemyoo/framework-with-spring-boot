package pers.chemyoo.core;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import pers.chemyoo.core.setting.listeners.CleanupListener;
import tk.mybatis.spring.annotation.MapperScan;

@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = { "pers.chemyoo.core", "com.bw" })
@MapperScan(basePackages = { "pers.chemyoo.core.mappers", "com.bw" })
@ServletComponentScan(basePackageClasses = { CleanupListener.class })
public class CoreApplication extends SpringBootServletInitializer {

	public static void main(String[] args) throws IOException {
//		SpringApplication.run(CoreApplication.class, args);
		JSONObject json = JSONObject.parseObject(FileUtils.readFileToString(new File("C:\\Users\\chemyoo\\Desktop\\asw.json")));
		JSONObject data = json.getJSONObject("data");
		JSONArray jzAnswerCardList = data.getJSONArray("jzAnswerCardList");
		for(int i = 0; i < jzAnswerCardList.size(); i++) {
			JSONObject item = jzAnswerCardList.getJSONObject(i);
			int userItemOrder = item.getInteger("userItemOrder");
			JSONObject jzItem = item.getJSONObject("jzItem");
			StringBuilder sb = new StringBuilder();
			sb.append(userItemOrder+ ":" + jzItem.getString("itemContent")).append(": ").append("\r\n");
//			listJzItemChooses
			JSONArray listJzItemChooses = jzItem.getJSONArray("listJzItemChooses");
			for(int j = 0; j < listJzItemChooses.size(); j++) {
				JSONObject itemChoose = listJzItemChooses.getJSONObject(j);
				if(itemChoose.getInteger("isAnswer") != null) {
					sb.append(itemChoose.getString("chooseKey")).append(": ").append(itemChoose.getString("chooseValue"));
				}
			}
			System.err.println(sb.toString());
		}
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		return application.sources(CoreApplication.class);
	}

}
