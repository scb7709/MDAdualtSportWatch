package com.headlth.management.clenderutil;

public class preferences {
	public static final boolean DEBUG = true;
	
	public static final int REQUEST_PAGE_SIZE = 15;//请求参数的条数

	public static final String PREFS_NAME = "me.lam.maidong";

	public static final String PREFERENCE_USER_NAME = "username";//存储用户名
	public static final String PREFERENCE_PASSWORD = "password";//存储密码
	public static final String PREFERENCE_REMEMBER="remember";//存储记住密码状态
	public static final String PREFERENCE_LOGIN = "login";//存储登录信息

	/**带返回参数启动页面的requestCode*/
	public static final int PREFERENCE_REQUEST_CODE = 1111;
	/**数据请求 返回后的status标准数据*/
	public static final String PREFERENCE_QUEST_STATUS_SUCCESS ="1";
	
}
