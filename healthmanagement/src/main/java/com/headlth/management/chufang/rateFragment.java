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


public class rateFragment  extends BaseFragment {
	TextView weektotime;
	TextView daycount;
	TextView	weeklimitday;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.rate, null);
		return view;
	}
	chufangCallBack chufang=null;
	@SuppressLint("ValidFragment")
	public rateFragment(chufangCallBack chufang) {
		this.chufang = chufang;
		Log.e("json", "rateFragment调过来了的对象" + chufang);
	}
	public rateFragment() {

	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		weektotime= (TextView) view.findViewById(R.id.xinlvfanwei);
		daycount= (TextView) view.findViewById(R.id.daycount);
		weeklimitday= (TextView) view.findViewById(R.id.weeklimitday);


		if(chufang!=null){

			if(chufang.getPList().size()!=0){
				weektotime.setText(chufang.getPList().get(0).getWeeklyTotalFrom()+"-"+chufang.getPList().get(0).getWeeklyTotalTo()+"分钟");
				weeklimitday.setText("至少"+chufang.getPList().get(0).getWeekAtLeast()+"天");
				daycount.setText(chufang.getPList().get(0).getDailyFrom()+"-"+chufang.getPList().get(0).getDailyTo()+"次");
			}
		}




		super.onViewCreated(view, savedInstanceState);
	}

}
