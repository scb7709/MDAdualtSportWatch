package com.headlth.management.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.adapter.ReplyAdapter;
import com.headlth.management.entity.CircleList;
import com.headlth.management.entity.Comment;
import com.headlth.management.entity.Reply;
import com.headlth.management.myview.PullToRefreshLayout;
import com.headlth.management.myview.PullableListView;
import com.headlth.management.myview.RoundImageView;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DataString;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.HttpUtils;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abc on 2016/8/29.
 */
@ContentView(R.layout.activity_circlereply)
public class CircleReplyActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;




    private boolean requestnetworking;
    @ViewInject(R.id.circlereply_layout)
    private LinearLayout circlereply_layout;
    @ViewInject(R.id.circlereply_cancel)
    private Button circlereply_cancel;
    @ViewInject(R.id.circlereply_send)
    private Button circlereply_send;
    @ViewInject(R.id.circlereply_replyContent)
    private EditText circlereply_replyContent;
    @ViewInject(R.id.circlereply_nodata)
    private RelativeLayout circlereply_nodata;

    @ViewInject(R.id.circlereply_maidongcircle_listview)
    private PullableListView circlereply_maidongcircle_listview;
    @ViewInject(R.id.circlereply_refresh_view)
    private PullToRefreshLayout circlereply_refresh_view;

    @ViewInject(R.id.circlereply_Like)
    private RelativeLayout circlereply_Like;
    @ViewInject(R.id.circlereply_Comment)
    private RelativeLayout circlereply_Comment;


    @ViewInject(R.id.circlereply_like_IM)
    private ImageView circlereply_like_IM;
    @ViewInject(R.id.circlereply_like_reply)
    private ImageView circlereply_like_reply;

    @ViewInject(R.id.circlereply_comment_Count)
    private TextView circlereply_comment_Count;
    @ViewInject(R.id.circlereply_Like_Count)
    private TextView circlereply_Like_Count;
    private Holder holder;

    private class Holder {
        @ViewInject(R.id.listview_contentdetails_icon)
        public RoundImageView listview_contentdetails_icon;
        @ViewInject(R.id.listview_contentdetails_user)
        public TextView listview_contentdetails_user;
        @ViewInject(R.id.listview_contentdetails_text)
        public TextView listview_contentdetails_text;
        @ViewInject(R.id.listview_contentdetails_attitude)
        public ImageView listview_contentdetails_attitude;
        @ViewInject(R.id.listview_contentdetails_attitude_count)
        public TextView listview_contentdetails_attitude_count;
        @ViewInject(R.id.listview_contentdetails_time)
        public TextView listview_contentdetails_time;
        @ViewInject(R.id.listview_maidongcircle_comment_layout)
        public LinearLayout listview_maidongcircle_comment_layout;
    }


    private Comment comment;
    private ReplyAdapter replyAdapter;
    private ShareReplyList shareReplyList;
    private int replyCount;
    private int position;
    private View footer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        view_publictitle_title.setText("回复");
        position = Integer.parseInt(getIntent().getStringExtra("position"));
        comment = CircleList.getInstance().commentlist.get(position);
        Log.i("aaaaaaaaaaXIANGQING", comment.toString());
        setData();
        replyAdapter = new ReplyAdapter(CircleList.getInstance().replylist, CircleReplyActivity.this);
        circlereply_maidongcircle_listview.setAdapter(replyAdapter);

        //if(CircleList.getInstance().replylist.size()==0){
        getReply();
        // }

    }

    private void setData() {
        addReplyListener();
        view_publictitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CircleList.getInstance().replylist.clear();

            }
        });
        circlereply_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circlereply_layout.setVisibility(View.VISIBLE);
            }
        });
        circlereply_Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CircleList.getInstance().commentlist.get(position).getIsAttitude().equals("0")) {

                    if(!requestnetworking){
                        addLike("0");
                    }



                } else {
                    if(!requestnetworking){
                        addLike("1");
                    }


                }
            }
        });
        circlereply_refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getReply();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
        circlereply_maidongcircle_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ddddddddddd点的", "" + position);
                if (position != 0) {
                    // showPopFormBottom(false, position - 1);
                }
            }
        });
        replyCount = comment.getReplyCount();
        circlereply_comment_Count.setText(replyCount + "");
        if (comment.getIsAttitude().equals("1")) {
            circlereply_like_IM.setImageResource(R.mipmap.icon_zan);
        } else {
            circlereply_like_IM.setImageResource(R.mipmap.icon_no_zan);
        }
        if (comment.getAttitudeCount() == 0) {
            circlereply_Like_Count.setText("赞");
        } else {
            circlereply_Like_Count.setText(comment.getAttitudeCount() + "");
        }
        if (comment.getReplyCount() == 0) {
            circlereply_comment_Count.setText("回复");
            circlereply_like_reply.setImageResource(R.mipmap.icon_no_reply);
        } else {
            circlereply_comment_Count.setText(comment.getReplyCount() + "");
            circlereply_like_reply.setImageResource(R.mipmap.icon_reply);
        }


        footer = LayoutInflater.from(this).inflate(R.layout.listview_footer_view, null);
        TextView textView = (TextView) footer.findViewById(R.id.listview_footer_view_text);
        textView.setText("暂无回复,快来抢沙发吧...");
        circlereply_maidongcircle_listview.addFooterView(footer, null, false);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circlereply_layout.setVisibility(View.VISIBLE);
            }
        });
        holder = new Holder();
        View view = LayoutInflater.from(this).inflate(R.layout.listview_comment, null);
        x.view().inject(holder, view);
        holder.listview_maidongcircle_comment_layout.setVisibility(View.GONE);
        Picasso.with(CircleReplyActivity.this)
                .load(Constant.BASE_URL+"/"+comment.getUserImageUrl())//图片网址
                .placeholder(R.mipmap.hand)//默认图标
                .into(holder.listview_contentdetails_icon);//控件
        holder.listview_contentdetails_user.setText(comment.getUserRealname());
        holder.listview_contentdetails_text.setText(comment.getCommentText());
        holder.listview_contentdetails_time.setText(DataString.showTime(comment.getCreateTime()));
        // listview_contentdetails_attitude_count.setText(comment.getAttitudeCount()+"");
        circlereply_maidongcircle_listview.addHeaderView(view, null, false);
        circlereply_maidongcircle_listview.setHeaderDividersEnabled(true);
        //禁止底部出现分割线
        circlereply_maidongcircle_listview.setFooterDividersEnabled(false);
    }

    //获取回复：MdMobileService.ashx?do=GetShareReplyRequest。  UserID  ContentID  CommentID
    private void getReply() {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetShareReplyRequest");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(this, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(this, "UID", "0"));
        params.addBodyParameter("ContentID", comment.getContentID());
        params.addBodyParameter("CommentID", comment.getCommentID());

        HttpUtils.getInstance(this).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("AAAAAAAAhuo评论", response);
                        shareReplyList = new Gson().fromJson(response.toString(), ShareReplyList.class);
                        if (shareReplyList.Status.equals("1") && shareReplyList.ShareReplyList.size() != 0) {
                            // Collections.sort(shareContentList.ShareCommentList,Collections.reverseOrder());
                            // Collections.reverse(shareReplyList.ShareReplyList);
                            CircleList.getInstance().replylist.clear();
                            CircleList.getInstance().replylist.addAll(0, shareReplyList.ShareReplyList);
                            replyAdapter.notifyDataSetChanged();
                        }
                        if (CircleList.getInstance().replylist.size() == 0) {
                            circlereply_maidongcircle_listview.addFooterView(footer);
                            // contentdetails_nodata.setVisibility(View.VISIBLE);
                        } else {
                            circlereply_maidongcircle_listview.removeFooterView(footer);

                        }
                        replyAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        if (CircleList.getInstance().replylist.size() == 0) {
                            circlereply_maidongcircle_listview.addFooterView(footer);
                            // contentdetails_nodata.setVisibility(View.VISIBLE);
                        } else {
                            circlereply_maidongcircle_listview.removeFooterView(footer);

                        }
                        replyAdapter.notifyDataSetChanged();
                        Log.i("AAAAAAAAA评论", "LoginupToken");
                        Toast.makeText(CircleReplyActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );

    }

    public void addReplyListener() {//发评论 replay=true,,回复评论 replay=false
        circlereply_replyContent.setHint("回复" + comment.getUserRealname());
      /*  circlereply_replyContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String commentContentt = circlereply_replyContent.getText().toString();
                if (commentContentt.length() != 0) {
                    circlereply_send.setBackgroundColor(Color.parseColor("#ffad00"));
                } else {
                    circlereply_send.setBackgroundColor(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
        circlereply_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circlereply_layout.setVisibility(View.GONE);
                circlereply_replyContent.setText("");

            }
        });
        circlereply_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContentt = circlereply_replyContent.getText().toString();
                if (commentContentt.length() != 0) {
                    sendReply(commentContentt);
                    circlereply_layout.setVisibility(View.GONE);
                    circlereply_replyContent.setText("");
                }

            }
        });

    }

    private void sendReply(String ReplyText) {
        // UserID：用户ID， ContentID：分享内容的ID，CommentID：评论的ID，ReplyText：回复的内容


        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostShareCommentReplyRequest");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(this, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(this, "UID", "0"));
        params.addBodyParameter("ContentID", comment.getContentID());
        params.addBodyParameter("CommentID", comment.getCommentID());
        params.addBodyParameter("ReplyText", ReplyText);
        HttpUtils.getInstance(this).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("IsSuccess").equals("true")) {
                                int replyCount = CircleList.getInstance().commentlist.get(position).getReplyCount() + 1;
                                circlereply_comment_Count.setText(replyCount + "");
                                CircleList.getInstance().commentlist.get(position).setReplyCount(replyCount);
                                circlereply_like_reply.setImageResource(R.mipmap.icon_reply);
                                getReply();
                                Toast.makeText(CircleReplyActivity.this, "回复成功", Toast.LENGTH_SHORT).show();
                                Log.i("AAAAAAAAAXIZNHENG新增  ", response);
                            } else {
                                Toast.makeText(CircleReplyActivity.this, "回复失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA", "LoginupToken");

                        Toast.makeText(CircleReplyActivity.this, "回复失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }

    private void addLike(final String Flag) {
        //UserID:用户ID，ContentID:帖子ID，CommentID:评论ID(如果对帖子点赞则该项为0）,ReplyID(默认为0），Flag：0为点赞，1为取消赞
        Comment comment = CircleList.getInstance().commentlist.get(position);

        requestnetworking=true;
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostShareAttitudeRequest");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(this, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(this, "UID", "0"));
        params.addBodyParameter("ContentID", comment.getContentID());
        params.addBodyParameter("CommentID", comment.getCommentID());
        params.addBodyParameter("ReplyID", "0");
        params.addBodyParameter("Flag", Flag);
        HttpUtils.getInstance(this).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        requestnetworking=false;
                        Log.i("AAAAAAAAA", "Log" + response);
                        Comment comment = CircleList.getInstance().commentlist.get(position);
                        int likecount = comment.getAttitudeCount();
                        if (Flag.equals("0")) {
                            circlereply_like_IM.setImageResource(R.mipmap.icon_zan);
                            CircleList.getInstance().commentlist.get(position).setIsAttitude("1");
                            CircleList.getInstance().commentlist.get(position).setAttitudeCount(likecount + 1);
                            circlereply_Like_Count.setText((likecount + 1) + "");

                        } else {
                            circlereply_like_IM.setImageResource(R.mipmap.icon_no_zan);
                            CircleList.getInstance().commentlist.get(position).setIsAttitude("0");

                            if (likecount - 1>0) {
                                circlereply_Like_Count.setText((likecount - 1) + "");
                                CircleList.getInstance().commentlist.get(position).setAttitudeCount(likecount - 1);

                            } else {
                                circlereply_Like_Count.setText("赞");
                                CircleList.getInstance().commentlist.get(position).setAttitudeCount(0);
                            }
                        }

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        requestnetworking=false;
                        Toast.makeText(CircleReplyActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public class ShareReplyList {
        String Status;
        List<Reply> ShareReplyList;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {


        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            finish();
            CircleList.getInstance().replylist.clear();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
