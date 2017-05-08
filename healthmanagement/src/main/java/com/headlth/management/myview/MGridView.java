package com.headlth.management.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by abc on 2016/8/15.
 */
public class MGridView extends GridView {

    public boolean hasScrollBar = true;

    /**
     * @param context
     */
    public MGridView(Context context) {
        this(context, null);
    }

    public MGridView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

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

    public interface OnTouchInvalidPositionListener {

        boolean onTouchInvalidPosition(int motionEvent);

    }
    OnTouchInvalidPositionListener mTouchInvalidPosListener;
    public void setOnTouchInvalidPositionListener(OnTouchInvalidPositionListener listener) {

        mTouchInvalidPosListener = listener;

    }

    @Override

    public boolean onTouchEvent(MotionEvent event) {

        if (mTouchInvalidPosListener == null) {

            return super.onTouchEvent(event);

        }

        if (!isEnabled()) {

// A disabled view that is clickable still consumes the touch

// events, it just doesn't respond to them.

            return isClickable() || isLongClickable();

        }

        final int motionPosition = pointToPosition((int) event.getX(), (int) event.getY());

        if (motionPosition == INVALID_POSITION) {

            super.onTouchEvent(event);

            return mTouchInvalidPosListener.onTouchInvalidPosition(event.getActionMasked());

        }

        return super.onTouchEvent(event);

    }

}
