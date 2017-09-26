package com.headlth.management.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.headlth.management.R;
import com.headlth.management.utils.Constant;
import com.squareup.picasso.Picasso;

/**
 * Created by WangChang on 2016/4/14.
 */
public class OtherActivity extends Activity {

    private ImageView imageView;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        imageView = (ImageView) findViewById(R.id.iv_image);
        path = getIntent().getExtras().getString("flag");
        Picasso.with(this).load(Constant.BASE_URL + "/" +path).resize(200,200).into(imageView);
    }
}
