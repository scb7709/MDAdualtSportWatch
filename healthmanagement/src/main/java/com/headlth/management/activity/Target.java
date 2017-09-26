package com.headlth.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.chufang.targetItemAdapter;
import com.headlth.management.entity.chufangCallBack;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2016/4/25.
 */
@ContentView(R.layout.newhucfangtarget)
public class Target extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        view_publictitle_title.setText("运动计划");
    }

    @Event(value = {R.id.view_publictitle_back

    })
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;


        }}

    private ListView listView2;
    private chufangCallBack chufang = null;
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = this.getIntent();
        chufang = (chufangCallBack) intent.getSerializableExtra("chufang");
        listView2= (ListView) this.findViewById(R.id.listView2);
        if(chufang.getPList().get(0).getSportTarget()!=null){
            listView2.setAdapter(new targetItemAdapter(this,chufang.getPList().get(0).getSportTarget()));
        }


    }

}
