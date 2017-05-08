package com.headlth.management.chufang;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.headlth.management.R;
import com.headlth.management.entity.chufangCallBack;
import com.headlth.management.fragment.BaseFragment;

import java.util.List;

public class targetFragment  extends BaseFragment {
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		     view=inflater.inflate(R.layout.target, null);
		return view;
	}
	List<chufangCallBack.PListEntity.SportTargetEntity> targets=null;
	@SuppressLint("ValidFragment")
	public targetFragment(List<chufangCallBack.PListEntity.SportTargetEntity> targets) {
		this.targets = targets;
		Log.e("ffff", "调过来了的对象" + targets);
	}
	public targetFragment() {

	}
	ListView listView2;
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		listView2= (ListView) view.findViewById(R.id.listView2);
		if(targets!=null){
			listView2.setAdapter(new targetItemAdapter(getActivity(),targets));
		}
		super.onViewCreated(view, savedInstanceState);
	}

}
