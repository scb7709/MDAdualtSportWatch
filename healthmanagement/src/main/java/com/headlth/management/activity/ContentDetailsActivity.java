package com.headlth.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.adapter.CommentAdapter;
import com.headlth.management.adapter.ImageShowRecycleAdapter;
import com.headlth.management.entity.Circle;
import com.headlth.management.entity.CircleList;
import com.headlth.management.entity.Comment;
import com.headlth.management.myview.MyRecyclerViewGridView;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2016/8/26.
 */

@ContentView(R.layout.activity_contentdetails)
public class ContentDetailsActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;


    private boolean requestnetworking;
    @ViewInject(R.id.contentdetails_maidongcircle_listview)
    private PullableListView contentdetails_maidongcircle_listview;
    @ViewInject(R.id.contentdetails_refresh_view)
    private PullToRefreshLayout contentdetails_refresh_view;


    @ViewInject(R.id.contentdetails_sendcomment_re)
    private LinearLayout contentdetails_sendcomment_re;
    @ViewInject(R.id.dialog_contentdetails_addcomment_cancel)
    private Button cancel;
    @ViewInject(R.id.dialog_contentdetails_addcomment_send)
    private Button send;
    @ViewInject(R.id.dialog_contentdetails_addcomment_commentContent)
    private EditText commentContent;

    @ViewInject(R.id.contentdetails_Like)
    private RelativeLayout contentdetails_Like;
    @ViewInject(R.id.contentdetails_Comment)
    private RelativeLayout contentdetails_Comment;
    @ViewInject(R.id.contentdetails_like_IM)
    private ImageView contentdetails_like_IM;

    @ViewInject(R.id.contentdetails_like_reply)
    private ImageView contentdetails_like_reply;

    @ViewInject(R.id.contentdetails_comment_Count)
    private TextView contentdetails_comment_Count;
    @ViewInject(R.id.contentdetails_Like_Count)
    private TextView contentdetails_Like_Count;
    private Holder holder;

    public class Holder {


        @ViewInject(R.id.contentdetails_icon)
        public RoundImageView contentdetails_icon;
        @ViewInject(R.id.contentdetails_user)
        public TextView contentdetails_user;
        @ViewInject(R.id.contentdetails_text)
        public TextView contentdetails_text;
        @ViewInject(R.id.contentdetails_time)
        public TextView contentdetails_time;
        @ViewInject(R.id.contentdetails_Scrollgridview)
        public MyRecyclerViewGridView contentdetails_Scrollgridview;
    }



  /*  @ViewInject(R.id.contentdetails_comment)
    public RelativeLayout contentdetails_comment;

    @ViewInject(R.id.contentdetails_comment_count)
    public TextView contentdetails_comment_count;
    @ViewInject(R.id.contentdetails_like)
    public RelativeLayout contentdetails_like;
    @ViewInject(R.id.contentdetails_like_count)
    public TextView contentdetails_like_count;*/

    private Circle circle;
    private CommentAdapter commentAdapter;
    private int position,commentCount;
    private View footer;
    private boolean flag;//判断是我的分享界面传来的还是迈动圈子

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initialize();
        //  }

    }

    private void initialize() {
        CircleList.getInstance().replylist.clear();
        view_publictitle_title.setText("动态详情");
        position = Integer.parseInt(getIntent().getStringExtra("position"));
        String tempflag = getIntent().getStringExtra("flag");
        if (tempflag.equals("MyShareActivity")) {
           flag = true;
            circle = CircleList.getInstance().mycirclelist.get(position);
        } else {
          flag = false;
            circle = CircleList.getInstance().circlelist.get(position);
        }
        Log.i("aaaaaaaaaaXIANGQING", circle.toString());
        setData();
        commentAdapter = new CommentAdapter(ContentDetailsActivity.this);
        contentdetails_maidongcircle_listview.setAdapter(commentAdapter);
        // if(CircleList.getInstance().commentlist.size()==0){
        getComment("");
    }

    private void setListViewHeaderData() {


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {


        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            finish();
            CircleList.getInstance().commentlist.clear();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setData() {

        addCommentListener();
        view_publictitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                CircleList.getInstance().commentlist.clear();
            }
        });
        contentdetails_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentdetails_sendcomment_re.setVisibility(View.VISIBLE);
            }
        });
        contentdetails_refresh_view.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getComment("");
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
        contentdetails_maidongcircle_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ddddddddddd点的", "" + position);
                if (position != 0) {
                    if (CircleList.getInstance().commentlist.size() != 0) {
                        CircleList.getInstance().replylist.clear();
                        Intent intent = new Intent(ContentDetailsActivity.this, CircleReplyActivity.class);
                        Bundle bundle = new Bundle();
                        Comment comment = CircleList.getInstance().commentlist.get(position - 1);
                        comment.setContentID(circle.getContentID());
                        bundle.putString("position", "" + (position - 1));
                        bundle.putSerializable("comment", comment);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
        commentCount = circle.getCommentCount();
        if (circle.getCommentCount() == 0) {
            contentdetails_comment_Count.setText("评论");
            contentdetails_like_reply.setImageResource(R.mipmap.icon_no_reply);
        } else {
            contentdetails_comment_Count.setText(circle.getCommentCount() + "");
            contentdetails_like_reply.setImageResource(R.mipmap.icon_reply);
        }
        if (circle.getLikeCount() == 0) {
            contentdetails_Like_Count.setText("赞");
        } else {
            contentdetails_Like_Count.setText(circle.getLikeCount() + "");
        }

        if (circle.getIsAttitude().equals("1")) {
            contentdetails_like_IM.setImageResource(R.mipmap.icon_zan);
        } else {
            contentdetails_like_IM.setImageResource(R.mipmap.icon_no_zan);
        }
        contentdetails_Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circle.getIsAttitude().equals("0")) {

                    if (!requestnetworking) {
                        addLike("0");
                    }


                } else {
                    if (!requestnetworking) {
                        addLike("1");
                    }


                }
            }
        });
        footer = LayoutInflater.from(this).inflate(R.layout.listview_footer_view, null);
        TextView textView = (TextView) footer.findViewById(R.id.listview_footer_view_text);
        textView.setText("暂无评论,快来抢沙发吧...");
        contentdetails_maidongcircle_listview.addFooterView(footer, null, false);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentdetails_sendcomment_re.setVisibility(View.VISIBLE);
            }
        });

        View view = LayoutInflater.from(this).inflate(R.layout.listviewheader_contentdetails, null);
        holder = new Holder();

        x.view().inject(holder, view);
        //  ViewUtils.inject(this, view);
        Picasso.with(ContentDetailsActivity.this)
                .load(Constant.BASE_URL + "/" + circle.getAvatarUrl())//图片网址
                .placeholder(R.mipmap.hand)//默认图标
                .into(holder.contentdetails_icon);//控件


        holder.contentdetails_user.setText(circle.getUsername());
        holder.contentdetails_time.setText(DataString.showTime(circle.getCreateTime()));
        holder.contentdetails_text.setText(circle.getContentText());
        if (circle.getImageUrls() != null && circle.getImageUrls().size() != 0) {
            holder.contentdetails_Scrollgridview.setVisibility(View.VISIBLE);

            ImageShowRecycleAdapter imageShowRecycleAdapter = new ImageShowRecycleAdapter(this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            holder.contentdetails_Scrollgridview.setVisibility(View.VISIBLE);
            holder.contentdetails_Scrollgridview.setHasFixedSize(true);
            holder.contentdetails_Scrollgridview.setLayoutManager(gridLayoutManager);
            holder.contentdetails_Scrollgridview.setAdapter(imageShowRecycleAdapter);
            imageShowRecycleAdapter.replaceAll((ArrayList<String>) circle.getImageUrls());
        } else {
            holder.contentdetails_Scrollgridview.setVisibility(View.GONE);
        }
        contentdetails_maidongcircle_listview.addHeaderView(view, null, false);
        contentdetails_maidongcircle_listview.setHeaderDividersEnabled(true);
        //禁止底部出现分割线
        contentdetails_maidongcircle_listview.setFooterDividersEnabled(false);

    }


    //参数：UserID, ContentID， flag 后两个值初始为0，上拉时传入当前页面第一条内容ContentID，flag=7；下拉时传入当前页面最后一条内容的ContentID,flag=8
    private void getComment(String flag) {


        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetShareCommentRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(this, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(this, "UID", "0"));
        params.addBodyParameter("ContentID", circle.getContentID());

        params.addBodyParameter("flag", flag);
        HttpUtils.getInstance(this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("AAAAAAAAhuo评论", response);
                        ShareCommentList shareContentList = new Gson().fromJson(response.toString(), ShareCommentList.class);
                        if (shareContentList.Status.equals("1") && shareContentList.ShareCommentList.size() != 0) {
                            CircleList.getInstance().commentlist.clear();
                            CircleList.getInstance().commentlist.addAll(0, shareContentList.ShareCommentList);
                            for (Comment comment : CircleList.getInstance().commentlist) {
                                comment.setContentID(circle.getContentID());
                            }
                            commentAdapter.notifyDataSetChanged();
                        }
                        if (CircleList.getInstance().commentlist.size() == 0) {
                            contentdetails_maidongcircle_listview.addFooterView(footer);
                            // contentdetails_nodata.setVisibility(View.VISIBLE);
                        } else {
                            contentdetails_maidongcircle_listview.removeFooterView(footer);

                        }

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA", "LoginupToken");
                        if (CircleList.getInstance().commentlist.size() == 0) {
                            contentdetails_maidongcircle_listview.addFooterView(footer);
                            // contentdetails_nodata.setVisibility(View.VISIBLE);
                        } else {
                            contentdetails_maidongcircle_listview.removeFooterView(footer);

                        }
                        Toast.makeText(ContentDetailsActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );

    }

    private void addContent(final String commentContentt) {


        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostShareCommentRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(this, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(this, "UID", "0"));
        params.addBodyParameter("ContentID", circle.getContentID());

        params.addBodyParameter("CommentText", commentContentt);
        HttpUtils.getInstance(this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("IsSuccess").equals("true")) {
                                int commentCount = circle.getCommentCount() + 1;
                                contentdetails_comment_Count.setText(commentCount + "");
                                circle.setCommentCount(commentCount);
                                contentdetails_like_reply.setImageResource(R.mipmap.icon_reply);
                                getComment("");
                                if(flag){
                                    CircleList.getInstance().circlelist.clear();//迈动圈子数据变化 清空数据
                                }
                                Toast.makeText(ContentDetailsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                               // Log.i("AAAAAAAAAXIZNHENG新增  ", CircleList.getInstance().circlelist.get(position).getCommentCount() + "");
                            } else {
                                Toast.makeText(ContentDetailsActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA", "LoginupToken");
                        Toast.makeText(ContentDetailsActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );


    }


    public void addCommentListener() {//发评论 replay=true,,回复评论 replay=false
      /*  // waitDialog = new com.headlth.management.clenderutil.WaitDialog(activity, "正在分享请稍后...");
        final View share_popupWindoww_view = LayoutInflater.from(ContentDetailsActivity.this).inflate(R.layout.dialog_contentdetails_addcomment, null);
        final PopupWindow share_popupWindoww = new PopupWindow(share_popupWindoww_view, GetWindowSize.getInstance(this).getGetWindowwidth(), GetWindowSize.getInstance(this).getGetWindowheight(), true);

        Button cancel = (Button) share_popupWindoww_view.findViewById(R.id.dialog_contentdetails_addcomment_cancel);
        final Button send = (Button) share_popupWindoww_view.findViewById(R.id.dialog_contentdetails_addcomment_send);
        TextView contentdetails_statechange = (TextView) share_popupWindoww_view.findViewById(R.id.contentdetails_statechange);
        final EditText commentContent = (EditText) share_popupWindoww_view.findViewById(R.id.dialog_contentdetails_addcomment_commentContent);*/

     /*   commentContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String commentContentt = commentContent.getText().toString();
                if (commentContentt.length() != 0) {
                    send.setBackgroundColor(Color.parseColor("#ffad00"));
                } else {
                    send.setBackgroundColor(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentdetails_sendcomment_re.setVisibility(View.GONE);
                commentContent.setText("");
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContentt = commentContent.getText().toString();
                if (commentContentt.length() != 0) {
                    addContent(commentContentt);
                    contentdetails_sendcomment_re.setVisibility(View.GONE);
                    commentContent.setText("");
                }

            }
        });
    }
    public class ShareCommentList {
        String Status;
        List<Comment> ShareCommentList;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("connent", "connent");
        commentAdapter.notifyDataSetChanged();
    }

    private void addLike(final String Flag) {
        //UserID:用户ID，ContentID:帖子ID，CommentID:评论ID(如果对帖子点赞则该项为0）,ReplyID(默认为0），Flag：0为点赞，1为取消赞
        requestnetworking = true;
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostShareAttitudeRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(this, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(this, "UID", "0"));
        params.addBodyParameter("ContentID", circle.getContentID());
        params.addBodyParameter("CommentID", "0");
        params.addBodyParameter("ReplyID", "0");
        params.addBodyParameter("Flag", Flag);
        HttpUtils.getInstance(this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        requestnetworking = false;
                        int likecount = circle.getAttitudeCount();
                        if (Flag.equals("0")) {
                            contentdetails_like_IM.setImageResource(R.mipmap.icon_zan);
                            circle.setIsAttitude("1");
                            circle.setAttitudeCount(likecount + 1);
                            contentdetails_Like_Count.setText((likecount + 1) + "");

                        } else {
                            contentdetails_like_IM.setImageResource(R.mipmap.icon_no_zan);
                            circle.setIsAttitude("0");


                            if (likecount - 1 > 0) {
                                contentdetails_Like_Count.setText((likecount - 1) + "");
                                circle.setAttitudeCount(likecount - 1);
                            } else {
                                contentdetails_Like_Count.setText("赞");
                                circle.setAttitudeCount(0);
                            }
                        }
                        if(flag){
                            CircleList.getInstance().circlelist.clear();//迈动圈子数据变化 清空数据
                        }

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        requestnetworking = false;
                        Toast.makeText(ContentDetailsActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }
}
