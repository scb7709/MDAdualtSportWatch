package com.headlth.management.movelistview;

import android.widget.TextView;

import com.headlth.management.activity.OldMessageActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
public class tempEntity {
    private  int position;
    private  List<messegeEntity> mAppList;
    private OldMessageActivity.AppAdapter appAdapter;
    private TextView tv;

    public tempEntity(int position, TextView tv, OldMessageActivity.AppAdapter appAdapter, List<messegeEntity> mAppList) {
        this.position = position;
        this.tv = tv;
        this.appAdapter = appAdapter;
        this.mAppList = mAppList;
    }

    public int getPosition() {
        return position;
    }

    public TextView getTv() {
        return tv;
    }

    public OldMessageActivity.AppAdapter getAppAdapter() {
        return appAdapter;
    }

    public List<messegeEntity> getmAppList() {
        return mAppList;
    }
}
