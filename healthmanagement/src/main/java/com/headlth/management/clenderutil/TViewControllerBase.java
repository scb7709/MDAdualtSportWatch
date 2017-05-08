package com.headlth.management.clenderutil;

import android.app.Activity;


/**
 * 该类是视图控制类的基本类
 * 
 */
public abstract class TViewControllerBase  {
	public Activity mActivity;

	String code;
	public TViewControllerBase(Activity activity,String code) {
		this.mActivity = activity;
	/*	this.aCache = ACache.get(mActivity);*/

		initObject();
	}

	public abstract void initObject();


}