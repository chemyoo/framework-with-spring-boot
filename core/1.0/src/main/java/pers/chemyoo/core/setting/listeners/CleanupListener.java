package pers.chemyoo.core.setting.listeners;

import javax.servlet.annotation.WebListener;

import org.springframework.web.util.IntrospectorCleanupListener;

/** 
 * @author Author : jianqing.liu
 * @version version : created time：2019年2月27日 下午1:05:19 
 * @since since from 2019年2月27日 下午1:05:19 to now.
 * @description 防止内存溢出监听
 */
@WebListener
public class CleanupListener extends IntrospectorCleanupListener{
	
}
