package com.headlth.management.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by abc on 2016/9/26.
 */
public class SubscriptView extends RelativeLayout {
    public int Subscript;
    public SubscriptView(Context context) {
        super(context);
    }
    public SubscriptView(Context context, int Subscript) {
        super(context);
        this.Subscript=Subscript;

    }
    public SubscriptView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubscriptView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
