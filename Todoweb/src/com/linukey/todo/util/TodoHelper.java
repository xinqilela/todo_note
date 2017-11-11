package com.linukey.todo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.StaticBucketMap;
import org.omg.CORBA.PUBLIC_MEMBER;

import antlr.ParserSharedInputState;

public class TodoHelper {
	public static Map<String, String> UserGroup = new HashMap<String, String>() {
		{
			put("normal", "normal");
			put("root", "root");
		}
	}; 
	public static Map<String, String> From = new HashMap<String, String>() {
		{
			put("server", "server");
			put("client", "client");
		}
	};

	/**
	 * 获取日期
	 * @param count 为0的话，则是今日，1的话，为明日，以此递推
	 * @return
	 */
	public Date getDate(int count) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, count);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		try {
			return formatter.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
















