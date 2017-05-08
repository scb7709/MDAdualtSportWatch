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


public class timeFragment  extends BaseFragment {
	TextView everycounttime;
	TextView SuggestReport;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.time, null);
		return view;
	}
	chufangCallBack chufang=null;
	@SuppressLint("ValidFragment")
	public timeFragment(chufangCallBack chufang) {
		this.chufang = chufang;
		Log.e("json", "调过来了的对象" + chufang);
	}
	public timeFragment() {

	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.e("json", "timeFragment调过来了的对象" + chufang);
		everycounttime= (TextView) view.findViewById(R.id.xinlvfanwei);
		SuggestReport=(TextView) view.findViewById(R.id.BestTimeRemark);
		if(chufang!=null){
			if(chufang.getPList().size()!=0){
				everycounttime.setText(chufang.getPList().get(0).getDailyDurationFrom()+"-"+chufang.getPList().get(0).getDailyDurationTo()+"分钟");
				SuggestReport.setText("除准备活动和整理活动之外至少要有"+chufang.getPList().get(0).getDuration()+"分钟使心率保持在靶心率范围之内");
			}
		}



		super.onViewCreated(view, savedInstanceState);
	}

}
