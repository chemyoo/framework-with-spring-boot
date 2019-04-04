package pers.chemyoo.core.utils;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.springframework.util.ResourceUtils;

import lombok.extern.slf4j.Slf4j;

/*******************************************************************
 * 该JavaBean可以直接在其他Java应用程序中调用，实现屏幕的"拍照" This JavaBean is used to snapshot the
 * GUI in a Java application! You can embeded it in to your java application
 * source code, and us it to snapshot the right GUI of the application
 * javax.ImageIO
 * 
 * @see
 * @version 1.0 前端传入坐标，后端就可以生成图片打印了。
 *
 *****************************************************/
@Slf4j
public class GuiCamera {
	private String fileName;// 文件的前缀
	private String defaultName = "GuiCamera";
	private int serialNum = 0;
	private String imageFormat; // 图像文件的格式
	private String defaultImageFormat = "png";
	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

	/****************************************************************
	 * 默认的文件前缀为GuiCamera，文件格式为PNG格式 The default constrUCt will use the default Image
	 * file surname "GuiCamera", and default image format "png"
	 ****************************************************************/
	public GuiCamera() {
		fileName = defaultName;
		imageFormat = defaultImageFormat;

	}

	/****************************************************************
	 * @param s
	 *            the surname of the snapshot file
	 * @param format
	 *            the format of the image file, it can be "jpg" or "png"
	 *            本构造支持JPG和PNG文件的存储
	 ****************************************************************/
	public GuiCamera(String s, String format) {

		fileName = s;
		imageFormat = format;
	}

	/****************************************************************
	 * 对屏幕进行拍照 snapShot the Gui once
	 ****************************************************************/
	public void snapShot() {

		try {
			// 拷贝屏幕到一个BufferedImage对象screenshot
			BufferedImage screenshot = (new Robot())
					.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
			serialNum++;

			// 文件存储路径
			// 获取跟目录
			File path = new File(ResourceUtils.getURL("classpath:").getPath());
			if (!path.exists())
				path = new File("");
			log.info("path:" + path.getAbsolutePath());
			File upload = new File(path.getAbsolutePath(), "static/images/");

			if (!upload.exists())
				upload.mkdirs();
			log.info("upload url:" + upload.getAbsolutePath());

			// 根据文件前缀变量和文件格式变量，自动生成文件名
			String name = upload.getAbsolutePath() + File.separator + fileName + String.valueOf(serialNum) + "."
					+ imageFormat;
			File f = new File(name);
			log.info("Save File " + name);
			// 将screenshot对象写入图像文件
			ImageIO.write(screenshot, imageFormat, f);
			log.info("..Finished!\n");
		} catch (Exception ex) {
			log.info(ex.getMessage(), ex);
		}
	}
}
