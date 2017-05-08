package com.headlth.management.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by abc on 2016/9/26.
 */
public class SubscriptImageView extends ImageView {
    public int Subscript;
    public SubscriptImageView(Context context) {
        super(context);
    }
    public SubscriptImageView(Context context,int Subscript) {
        super(context);
        this.Subscript=Subscript;

    }
    public SubscriptImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubscriptImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
