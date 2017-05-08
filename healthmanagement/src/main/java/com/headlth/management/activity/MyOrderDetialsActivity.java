package com.headlth.management.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.headlth.management.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by abc on 2016/11/23.
 */


@ContentView(R.layout.activity_myorderdetials)//复用我的处方布局
public class MyOrderDetialsActivity extends BaseActivity {
    @ViewInject(R.id.activity_myprescription_alltext)
    private TextView activity_myprescription_alltext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
      //  AddList();

    }
}
