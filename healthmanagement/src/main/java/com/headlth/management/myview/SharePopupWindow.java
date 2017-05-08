package com.headlth.management.myview;

/**
 * Created by abc on 2016/7/29.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.headlth.management.R;
import android.widget.RelativeLayout;

public class SharePopupWindow extends PopupWindow {

    private Activity mContext;

    private View view;

    private ImageView maidong, btn_pick_photo, btn_cancel;


    public SharePopupWindow(Activity mContext, View.OnClickListener itemsOnClick) {
        this.mContext=mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.dialog_sport_share, null);
        maidong = (ImageView) view.findViewById(R.id.dialog_sport_share_maidongquan);
        // 设置按钮监听
        maidong.setOnClickListener(itemsOnClick);
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    /* 设置弹出窗口特征 */
        // 设置视图
        WindowManager m = mContext.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(dip2px(mContext,100));
        this.setWidth(dip2px(mContext,100));

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#ffffff"));
        // 设置弹出窗体的背景


       this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);

    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}