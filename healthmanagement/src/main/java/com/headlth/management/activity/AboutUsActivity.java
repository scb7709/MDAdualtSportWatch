package com.headlth.management.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.utils.Constant;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by abc on 2016/11/4.
 */
@ContentView(R.layout.activity_aboutus)
public class AboutUsActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;


    @ViewInject(R.id.activity_aboutus_webview)
    private WebView activity_aboutus_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        activity_aboutus_webview.getSettings().setJavaScriptEnabled(true);
        activity_aboutus_webview.loadUrl(Constant.BASE_URL+"/about/contract.html");
        view_publictitle_title.setText("关于我们");
        activity_aboutus_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
    @Event(value = {R.id.view_publictitle_back})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
        }
    }
}
