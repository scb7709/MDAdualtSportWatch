package com.headlth.management.clenderutil;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 字符串的工具类 */
public class StringUtils {

	/** 判断字符串是否为空 */
	public static boolean isEmpty(String str) {
		return (str == null || str.trim().length() == 0 || "null".equals(str));
	}

	/** 判断多个字符串是否全部都不为空 */
	public static boolean isExist(String... strings) {
		for (String str : strings) {
			if (isEmpty(str)) {
				return false;
			}
		}
		return true;
	}

	/** 判断字符串是否为数字 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 格式化字符串 如输入123456789和长度为4时返回1234 5678 9
	 * 
	 * @param str
	 *            要格式的字符串
	 * @param len
	 *            长度
	 * @return
	 */
	public static String formateString(String str, int len) {
		if (len < 1) {
			return str;
		}
		StringBuffer buffer = new StringBuffer();
		while (str.length() > len) {
			buffer.append(str.subSequence(0, len));
			buffer.append("  ");
			str = str.substring(len);
		}
		buffer.append(str);
		return buffer.toString();
	}

	/**
	 * 去掉str中的空格、换行、制表符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 把字符串中的部分类容替换成星号
	 * 
	 * @param str
	 *            要替换的字符串
	 * @param num
	 *            要替换的个数
	 * @param end
	 *            距离末尾的字符串个数
	 * @return
	 */
	public static String relaceWithStar(String str, int num, int end) {
		if (str.length() <= (num + end) || !isExist(str))
			return str;
		StringBuffer buffer = new StringBuffer();
		buffer.append(str.substring(0, str.length()-(num + end)));
		for (int i = 0; i < num; i++) {
			buffer.append("*");
		}
		buffer.append(str.substring(str.length()- end, str.length()));
		return buffer.toString();
	}

	/**
	 * 判断字符串是否相同，若data为空，也返回true
	 * @param indicator
	 * 				参照参数
	 * @param data
	 * 				数据参数
	 */
	public static boolean IsEquals(String indicator, String data){
		if (TextUtils.equals(indicator, data) || TextUtils.equals("", data)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检测密码强度
	 */
	public static int CheckSecurity(String pwd) {
		String str1 = "[0-9]{1,20}";
		String str2 = "[a-z]{1,20}";
		String str3 = "[A-Z]{1,20}";
		String str4 ="[0-9|a-z]{1,20}";
		String str5 ="[0-9|A-Z]{1,20}";
		String str6 ="[a-z|A-Z]{1,20}";
		if(pwd.matches(str1) || pwd.matches(str2) || pwd.matches(str3)){
			return 1;
		} else if (pwd.matches(str4) || pwd.matches(str5) || pwd.matches(str6)){
			return 2;
		} else {
			return 3;
		}
	}

}
