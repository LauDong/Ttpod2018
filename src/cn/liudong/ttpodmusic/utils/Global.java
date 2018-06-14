package cn.liudong.ttpodmusic.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * 项目工具类：封装一些代码，方便直接调用
 * 
 * @author 刘东
 *
 */
public class Global {

	// 格式字符串
	// int为毫秒值
	// 设为静态方法，方便调用
	public static String setTime(int time) {

		// 获取格式字符串工具
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		// 把毫秒值转化为Date对象
		Date date = new Date(time);
		String str = sdf.format(date);

		return str;
	}
}
