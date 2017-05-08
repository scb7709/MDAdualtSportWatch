package com.headlth.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.entity.MessageList;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by abc on 2016/11/4.
 */
@ContentView(R.layout.activity_messagedetials)
public class MessageDetialsActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;

    @ViewInject(R.id.activity_messagedetials_listview)
    private ListView activity_messagedetials_listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();
    }

    @Event(value = {R.id.view_publictitle_back})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                if (MainActivity.Activity == null) {
                    startActivity(new Intent(MessageDetialsActivity.this, MainActivity.class));
                }
                finish();
                break;
        }
    }

    private void init() {
        view_publictitle_title.setText("温馨提醒");
        getMessage();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            if (MainActivity.Activity == null) {
                startActivity(new Intent(MessageDetialsActivity.this, MainActivity.class));
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getMessage() {


        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostMsgRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(MessageDetialsActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(MessageDetialsActivity.this, "UID", "null"));
        HttpUtils.getInstance(MessageDetialsActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("hfhfhfh", response.toString());
                        MessageList message = new Gson().fromJson(response.toString(), MessageList.class);
                        if (message.Status.equals("1")) {



                        } else {
                            Toast.makeText(MessageDetialsActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA", "LoginupToken");

                        Toast.makeText(MessageDetialsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                    }
                }

        );

    }

}
