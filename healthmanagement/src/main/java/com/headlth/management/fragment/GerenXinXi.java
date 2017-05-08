package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.chufang.selfItemAdapter;
import com.headlth.management.entity.tiJianCallBack;


/**
 * Created by Administrator on 2016/3/24.
 */
public class GerenXinXi extends BaseFragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sp2, null);

        return view;
    }


    tiJianCallBack tijian;

    @SuppressLint("ValidFragment")
    public GerenXinXi(tiJianCallBack tijian) {
        this.tijian = tijian;
    }

    public GerenXinXi() {
    }
    ListView listView;
    private selfItemAdapter selfAdapter;
    TextView line1;
    TextView line2;
    Button geren;
    Button yichang;
    Button chankanchufang;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        geren = (Button) getActivity().findViewById(R.id.geren);
        yichang = (Button) getActivity().findViewById(R.id.yichang);
        chankanchufang = (Button) getActivity().findViewById(R.id.chankanchufang);
        line1 = (TextView) getActivity().findViewById(R.id.line1);
        line2 = (TextView) getActivity().findViewById(R.id.line2);
        line1.setBackgroundColor(Color.parseColor("#ffad00"));
        line2.setBackgroundColor(Color.parseColor("#00000000"));
        geren.setTextColor(Color.parseColor("#000000"));
        yichang.setTextColor(Color.parseColor("#999999"));
        listView = (ListView)view.findViewById(R.id.sp);
        selfAdapter = new selfItemAdapter(getActivity(), tijian.getUserInfo());
        listView.setAdapter(selfAdapter);

    }
}
