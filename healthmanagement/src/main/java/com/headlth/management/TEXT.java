package com.headlth.management;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by abc on 2016/10/13.
 */
@ContentView(R.layout.text)
public class TEXT extends Activity {
    @ViewInject(R.id.text)
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        text.setText(getIntent().getStringExtra("jsonObject1"));
    }
}
