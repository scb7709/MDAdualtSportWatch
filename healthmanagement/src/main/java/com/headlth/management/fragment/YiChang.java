package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.headlth.management.R;
import com.headlth.management.chufang.selfItemAdapter;
import com.headlth.management.chufang.yichangItemAdapter;
import com.headlth.management.entity.tiJianCallBack;

/**
 * Created by Administrator on 2016/3/24.
 */
public class YiChang extends BaseFragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.cl2, null);
        return view;
    }

    tiJianCallBack tijian;

    @SuppressLint("ValidFragment")
    public YiChang(tiJianCallBack tijian) {
        this.tijian = tijian;
    }

    public YiChang() {
    }
    ListView listView;
    private selfItemAdapter selfAdapter;
    private yichangItemAdapter yichangAdapter;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.cl2);
        yichangAdapter = new yichangItemAdapter(getActivity(), tijian.getAbnormalList());
        listView.setAdapter(yichangAdapter);


    }
}
