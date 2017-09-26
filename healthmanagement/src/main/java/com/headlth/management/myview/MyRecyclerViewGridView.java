package com.headlth.management.myview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by abc on 2017/8/9.
 */
public class MyRecyclerViewGridView  extends RecyclerView{
    public MyRecyclerViewGridView(Context context) {
        super(context);
    }

    public MyRecyclerViewGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerViewGridView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public boolean hasScrollBar = true;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = heightMeasureSpec;
        if (hasScrollBar) {
            expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);// 注意这里,这里的意思是直接测量出GridView的高度
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
