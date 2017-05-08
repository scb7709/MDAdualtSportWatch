package com.headlth.management.clenderutil;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * 视图Base类
 * 
 */
public abstract class TViewBase implements OnClickListener {
	public View view;// 布局视图
	public Activity mActivity;
	public TViewOnClickListener viewOnClickListener;// 控件点击或调接口

	/**
	 * @param activity
	 * @param isFillParent
	 *            是否填充整个父类的剩余空间
	 */
	public TViewBase(Activity activity, boolean isFillParent) {
		this.mActivity = activity;
		Log.e("sss",view+":::::"+mActivity);
		view = LayoutInflater.from(this.mActivity).inflate(viewLayoutRes(), null);
		if (isFillParent) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, 0, 1);
			view.setLayoutParams(params);
		}
		initView();
		setViewListener();
		initData();
	}

	/**
	 * 子类实现 需要返回Layout布局的resource id
	 */
	public abstract int viewLayoutRes();

	/** 1、初始化view */
	public abstract void initView();

	/** 2、设置监听 */
	public abstract void setViewListener();

	/** 3、初始化数据 */
	public abstract void initData();

	/** 获取布局视图 */
	public View getView() {
		return view;
	}

	/** 设置View点击事件监听 */
	public void setViewOnClickListener(TViewOnClickListener viewOnClickListener) {
		this.viewOnClickListener = viewOnClickListener;
	}

	@Override
	public void onClick(View v) {
		if (null != viewOnClickListener) {
			viewOnClickListener.OnClick(v.getId());
		}
	}
}
