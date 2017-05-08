package com.headlth.management.movelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MySc extends ScrollView {
      
        public MySc(Context context) {
            super(context);  
            mGestureDetector = new GestureDetector(new YScrollDetector());
            setFadingEdgeLength(0);  
        }  
      
        public MySc(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);  
            mGestureDetector = new GestureDetector(new YScrollDetector());  
            setFadingEdgeLength(0);  
        }  
      
        public MySc(Context context, AttributeSet attrs) {  
            super(context, attrs);  
            mGestureDetector = new GestureDetector(new YScrollDetector());  
            setFadingEdgeLength(0);  
        }  
      
        private GestureDetector mGestureDetector;  
        OnTouchListener mGestureListener;
      
        @Override  
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return super.onInterceptTouchEvent(ev)  
                    && mGestureDetector.onTouchEvent(ev);  
        }  
      
        class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
            @Override  
            public boolean onScroll(MotionEvent e1, MotionEvent e2,  
                    float distanceX, float distanceY) {  
                if (distanceY != 0 && distanceX != 0) {  
      
                }  
                if (Math.abs(distanceY) >= Math.abs(distanceX)) {  
                    return true;  
                }  
                return false;  
            }  
        }  
    }  