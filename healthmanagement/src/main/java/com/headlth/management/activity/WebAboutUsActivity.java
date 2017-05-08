package com.headlth.management.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.headlth.management.R;
import com.umeng.analytics.MobclickAgent;


public class WebAboutUsActivity extends BaseActivity {

    private WebView webView;
    private String newsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weblayout);
        webView = (WebView) findViewById(R.id.webView);
        //http://www.ssp365.com/ArticleView.aspx?id=123
        Log.e("asknews", "http://www.ssp365.com/ArticleView.aspx?id=" + newsUrl);
        webView.loadUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.headlth.management");
        //加载页面居中显示
       /* WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);*/
//支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
// 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
// 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
//扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
//自适应屏幕

      /*  1.NARROW_COLUMNS：可能的话使所有列的宽度不超过屏幕宽度

        2.NORMAL：正常显示不做任何渲染

        3.SINGLE_COLUMN：把所有内容放大webview等宽的一列中

        用SINGLE_COLUMN类型可以设置页面居中显示，页面可以放大缩小，但这种方法不怎么好，有时候会让你的页面布局走样而且我测了一下，只能显示中间那一块，超出屏幕的部分都不能显示。
  */
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setLoadWithOverviewMode(true);

    }

    public void back(View view) {

        this.finish();
    }



}
