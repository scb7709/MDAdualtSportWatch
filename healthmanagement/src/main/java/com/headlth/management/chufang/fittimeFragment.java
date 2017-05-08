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


public class fittimeFragment extends BaseFragment {
    TextView zuijiashijian;
    TextView BestTimeRemark;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fittime, null);
        return view;
    }

    chufangCallBack chufang = null;

    @SuppressLint("ValidFragment")
    public fittimeFragment(chufangCallBack chufang) {
        this.chufang = chufang;
        Log.e("json", "调过来了的对象" + chufang);
    }

    public fittimeFragment() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        zuijiashijian = (TextView) view.findViewById(R.id.xinlvfanwei);
        BestTimeRemark = (TextView) view.findViewById(R.id.BestTimeRemark);

        if(chufang!=null){
            if(chufang.getPList().size()!=0){
                zuijiashijian.setText(chufang.getPList().get(0).getBestTime());
                BestTimeRemark.setText(chufang.getPList().get(0).getBestTimeRemark());
            }
        }


        super.onViewCreated(view, savedInstanceState);
    }

}
