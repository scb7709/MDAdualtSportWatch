package com.headlth.management.chufang;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.entity.chufangCallBack;
import com.headlth.management.fragment.BaseFragment;


public class strongFragment extends BaseFragment {
    TextView LBound;
    TextView SuggestReport;
    View vieww;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vieww = inflater.inflate(R.layout.strong, null);
        return vieww;
    }

    chufangCallBack chufang = null;

    @SuppressLint("ValidFragment")
    public strongFragment(chufangCallBack chufang) {
        this.chufang = chufang;
        Log.e("json", "strongFragment调过来了的对象" + chufang);
    }

    public strongFragment() {

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        LBound = (TextView) vieww.findViewById(R.id.xinlvfanwei);
        SuggestReport = (TextView) vieww.findViewById(R.id.BestTimeRemark);
        if (chufang != null) {
            if (chufang.getPList().size() != 0) {
                LBound.setText(chufang.getPList().get(0).getLBound() + "-"+chufang.getPList().get(0).getUBound());
                SuggestReport.setText(chufang.getPList().get(0).getSuggestReport());
            }
        }
        super.onViewCreated(view, savedInstanceState);
    }

}
