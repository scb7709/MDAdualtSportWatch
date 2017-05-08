package com.headlth.management.clenderutil;

import android.util.Log;


public class LogUtil {
	public static void e(String tag, String string) {
		if (preferences.DEBUG) {
			Log.e("", "======" + tag + "=======" + string);
		}
	}

	public static void d(String tag, String string) {
		if (preferences.DEBUG) {
			Log.d("", "======" + tag + "=======" + string);
		}
	}

	public static void i(String tag, String string) {
		if (preferences.DEBUG) {
			Log.i("", "======" + tag + "=======" + string);
		}
	}

	public static void v(String tag, String string) {
		if (preferences.DEBUG) {
			Log.v("", "======" + tag + "=======" + string);
		}
	}

	public static void w(String tag, String string) {
		if (preferences.DEBUG) {
			Log.w("", "======" + tag + "=======" + string);
		}
	}
}
