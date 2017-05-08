package com.headlth.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.adapter.MessageRecyclerViewAdapter;
import com.headlth.management.entity.MessageList;
import com.headlth.management.myview.EndLessOnScrollListener;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by abc on 2016/11/4.
 */
@ContentView(R.layout.activity_message)
public class MessageActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;

    @ViewInject(R.id.activity_message_SwipeRefreshLayout)
    private SwipeRefreshLayout activity_message_SwipeRefreshLayout;


    @ViewInject(R.id.activity_message_recyclerView)
    private RecyclerView activity_message_recyclerView;
    private List<MessageList.Message> messageListlist;
    LinearLayoutManager linearLayoutManager;
    MessageRecyclerViewAdapter messageRecyclerViewAdapter;
    private String UID;
    Gson gson;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int position = msg.arg1;
            MessageList.Message message = messageListlist.get(position);
            if (msg.arg2 == 0) {
                if (message.MsgtypeId==2) {
                    Intent intent = new Intent(MessageActivity.this, MessageDetialsActivity.class);
                    intent.putExtra("messageID", message.ID+"");
                    startActivity(intent);
                }

            } else {


            }
        }

    };

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
                finish();
                break;
        }
    }

    private void init() {
        view_publictitle_title.setText("消息");
        gson = new Gson();
        UID = ShareUitls.getString(MessageActivity.this, "UID", "0");
        linearLayoutManager = new LinearLayoutManager(MessageActivity.this);
        activity_message_recyclerView.setLayoutManager(linearLayoutManager);
        activity_message_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                activity_message_SwipeRefreshLayout.setRefreshing(true);
            }
        });


        activity_message_recyclerView.addOnScrollListener(
                new EndLessOnScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int currentPage) {


                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        LinearLayoutManager layoutManager = (LinearLayoutManager) activity_message_recyclerView.getLayoutManager();
                        //获取可视的第一个view
                        View topView = layoutManager.getChildAt(0);
                        if (topView != null) {
                            //获取与该view的顶部的偏移量
                            int lastOffset = topView.getTop();
                            //得到该View的数组位置
                            int lastPosition = layoutManager.getPosition(topView);
                            ShareUitls.putString(MessageActivity.this, "MessagelastOffset", lastOffset + "");
                            ShareUitls.putString(MessageActivity.this, "MessagelastPosition", lastPosition + "");
                        }

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }


                }

        );
       getMessage();


    }

    private void getMessage() {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostMsgRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(MessageActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(MessageActivity.this, "UID", "null"));
        HttpUtils.getInstance(MessageActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("hfhfhfh", response.toString());
/*
                        MessageList messageList = new Gson().fromJson(str.toString(), MessageList.class);
                        if (messageList.Status.equals("1")) {
                            messageListlist=messageList.MsgList;
                            messageRecyclerViewAdapter=new MessageRecyclerViewAdapter(messageListlist,MessageActivity.this,handler);
                            activity_message_recyclerView.setAdapter(messageRecyclerViewAdapter);

                        } else {
                            Toast.makeText(MessageActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }*/
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA", "LoginupToken");

                        Toast.makeText(MessageActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                    }
                }

        );

    }
    String str="{\n" +
            "  \"Status\": 1,\n" +
            "  \"MsgList\": [\n" +
            "    {\n" +
            "      \"ID\": 8796,\n" +
            "      \"UserID\": 4,\n" +
            "      \"Title\": \"迈动小教练\",\n" +
            "      \"Content\": \"本周有未完成的运动目标，请继续努力。\",\n" +
            "      \"CreateTime\": \"2017/5/5 16:30:11\",\n" +
            "      \"CreateMonth\": \"05月05日\",\n" +
            "      \"MsgtypeId\": 0,\n" +
            "      \"MedictimeslotId\": 0\n" +
            "    },\n" +
            "{\n" +
            "      \"ID\": 8797,\n" +
            "      \"UserID\": 4,\n" +
            "      \"Title\": \"迈动小教练\",\n" +
            "      \"Content\": \"本周有未完成的运动目标，请继续努力。\",\n" +
            "      \"CreateTime\": \"2017/5/5 16:30:11\",\n" +
            "      \"CreateMonth\": \"05月06日\",\n" +
            "      \"MsgtypeId\": 0,\n" +
            "      \"MedictimeslotId\": 0\n" +
            "    },\n" +
            "{\n" +
            "      \"ID\": 8798,\n" +
            "      \"UserID\": 4,\n" +
            "      \"Title\": \"迈动小教练\",\n" +
            "      \"Content\": \"本周有未完成的运动目标，请继续努力。\",\n" +
            "      \"CreateTime\": \"2017/5/5 16:30:11\",\n" +
            "      \"CreateMonth\": \"05月05日\",\n" +
            "      \"MsgtypeId\": 1,\n" +
            "      \"MedictimeslotId\": 0\n" +
            "    },\n" +
            "{\n" +
            "      \"ID\": 8799,\n" +
            "      \"UserID\": 4,\n" +
            "      \"Title\": \"迈动小教练\",\n" +
            "      \"Content\": \"本周有未完成的运动目标，请继续努力。\",\n" +
            "      \"CreateTime\": \"2017/5/5 16:30:11\",\n" +
            "      \"CreateMonth\": \"05月07日\",\n" +
            "      \"MsgtypeId\": 2,\n" +
            "      \"MedictimeslotId\": 0\n" +
            "    }\n" +
            "  ],\n" +
            "  \"Message\": \"获取用户消息成功!\",\n" +
            "  \"IsSuccess\": true,\n" +
            "  \"IsError\": false,\n" +
            "  \"ErrMsg\": null,\n" +
            "  \"ErrCode\": null,\n" +
            "  \"ResultJWT\": null\n" +
            "}";

}
