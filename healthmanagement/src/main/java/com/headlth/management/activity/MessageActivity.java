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
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.adapter.MessageRecyclerViewAdapter;
import com.headlth.management.entity.MessageList;
import com.headlth.management.entity.deleteMessagCallBack;
import com.headlth.management.myview.EndLessOnScrollListener;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
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
            Log.i("Message", message.toString());
            if (msg.arg2 == 0) {
                if (message.MsgtypeId == 2) {
                    Intent intent = new Intent(MessageActivity.this, MessageDetialsActivity.class);
                    intent.putExtra("MedictimeslotId", message.MedictimeslotId + "");
                    intent.putExtra("MsgtypeId", message.MsgtypeId + "");
                    intent.putExtra("CreateTime", message.CreateTime + "");
                    startActivity(intent);
                }

            } else if (msg.arg2 == 2) {
                deleteMsg(message.ID + "", position);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initialize();
    }

    @Event(value = {R.id.view_publictitle_back})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
        }
    }

    private void initialize() {

        messageListlist=new ArrayList<>();
        view_publictitle_title.setText("消息");
        gson = new Gson();
        UID = ShareUitls.getString(MessageActivity.this, "UID", "0");
        linearLayoutManager = new LinearLayoutManager(MessageActivity.this);
        activity_message_recyclerView.setLayoutManager(linearLayoutManager);
        // activity_message_SwipeRefreshLayout.openLeftMenu(0);
        activity_message_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessage(8);

            }
        });


        activity_message_recyclerView.addOnScrollListener(
                new EndLessOnScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int currentPage) {

                        getMessage(7);
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
        getMessage(0);


    }

    private void getMessage(final int flag) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostMsgRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(MessageActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(MessageActivity.this, "UID", "null"));
        if (messageListlist!=null&&messageListlist.size() >=1) {
            if (flag == 7) {
                params.addBodyParameter("ID", messageListlist.get(messageListlist.size() - 1).ID + "");
                params.addBodyParameter("flag", flag + "");
            }/* else if (flag == 8) {
                params.addBodyParameter("ID", messageListlist.get(0).ID + "");
                params.addBodyParameter("flag", flag + "");
            }*/
        }
        HttpUtils.getInstance(MessageActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {


                        Log.i("PostMsgRequest", response.toString());
                        MessageList messageList = new Gson().fromJson(response.toString(), MessageList.class);
                        if (messageList.Status.equals("1")&&messageList.MsgList!=null&&messageList.MsgList.size()!=0) {
                            switch (flag) {
                                case 0:
                                    messageListlist = messageList.MsgList;
                                    messageRecyclerViewAdapter = new MessageRecyclerViewAdapter(messageListlist, MessageActivity.this, handler);
                                    activity_message_recyclerView.setAdapter(messageRecyclerViewAdapter);
                                    break;
                                case 7:
                                    activity_message_SwipeRefreshLayout.setRefreshing(false);
                                    messageListlist.addAll(messageListlist.size(), messageList.MsgList);
                                    messageRecyclerViewAdapter.notifyItemRangeInserted(messageListlist.size(), messageList.MsgList.size());
                                    break;

                                case 8:
                                    messageListlist.clear();
                                    messageListlist = messageList.MsgList;
                                    messageRecyclerViewAdapter = new MessageRecyclerViewAdapter(messageListlist, MessageActivity.this, handler);
                                    activity_message_recyclerView.setAdapter(messageRecyclerViewAdapter);
                                    activity_message_SwipeRefreshLayout.setRefreshing(false);
                               /*     activity_message_SwipeRefreshLayout.setRefreshing(false);
                                    messageListlist.addAll(0, messageList.MsgList);
                                    messageRecyclerViewAdapter.notifyItemRangeInserted(0, messageList.MsgList.size());*/
                                    break;

                            }

                        } else {
                          if(flag==7||flag==8){
                              activity_message_SwipeRefreshLayout.setRefreshing(false);

                            }
                            Toast.makeText(MessageActivity.this, "没有更多消息", Toast.LENGTH_SHORT).show();
                        }
                      //  activity_message_SwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA", "LoginupToken");
                        activity_message_SwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MessageActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                    }
                }

        );

    }

    private void deleteMsg(final String ID, final int Position) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostDelMsgRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(this, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(this, "UID", "0"));
        params.addBodyParameter("ID", ID);
        HttpUtils.getInstance(this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("hfhfhfh", response.toString());
                        deleteMessagCallBack deleteMesg = new Gson().fromJson(response.toString(), deleteMessagCallBack.class);
                        if (deleteMesg.getStatus() == 1) {
                            messageListlist.remove(Position);
                            messageRecyclerViewAdapter = new MessageRecyclerViewAdapter(messageListlist, MessageActivity.this, handler);
                            activity_message_recyclerView.setAdapter(messageRecyclerViewAdapter);
                        } else {
                            Toast.makeText(MessageActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable error) {

                        Toast.makeText(MessageActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }




}
