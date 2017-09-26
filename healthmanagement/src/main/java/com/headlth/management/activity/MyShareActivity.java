package com.headlth.management.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.adapter.CircleRecyclerViewAdapter;
import com.headlth.management.entity.Circle;
import com.headlth.management.entity.CircleList;
import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.myview.EndLessOnScrollListener;
import com.headlth.management.myview.MyItemAnimator;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.List;

/**
 * Created by abc on 2016/11/18.
 */

@ContentView(R.layout.activity_myshare)
public class MyShareActivity extends BaseActivity {


    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;


    @ViewInject(R.id.activity_myshare_SwipeRefreshLayout)
    private SwipeRefreshLayout activity_myshare_SwipeRefreshLayout;


    @ViewInject(R.id.activity_myshare_recyclerView)
    private RecyclerView activity_myshare_recyclerView;


    @ViewInject(R.id.activity_myshare_nodata)
    private RelativeLayout maidongcircle_nodata;



    boolean IS_ContentDetailsActivity;
   // private CircleAdapter circleAdapter;
    private CircleRecyclerViewAdapter circleRecyclerViewAdapter;
    private String UserID;
    private Gson gson;
    private BottomMenuDialog bottomMenuDialog;
    private LinearLayoutManager linearLayoutManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int position = msg.arg1;
            if (msg.arg2 == 0) {
                CircleList.getInstance().commentlist.clear();
                Intent intent = new Intent(MyShareActivity.this, ContentDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("circle", CircleList.getInstance().mycirclelist.get(position));
                bundle.putString("position", "" + position);
                bundle.putString("flag", "MyShareActivity");
                intent.putExtras(bundle);
                startActivity(intent);
                IS_ContentDetailsActivity = true;

            } else {

                Log.i("VVVV", UserID + "  " + CircleList.getInstance().mycirclelist.get(position).getUserID());
                if (UserID.equals(CircleList.getInstance().mycirclelist.get(position).getUserID())) {

                    bottomMenuDialog = new BottomMenuDialog.Builder(MyShareActivity.this)
                            .addMenu("删除", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    DeleteCircle(position);

                                    bottomMenuDialog.dismiss();
                                }
                            }).create();
                    bottomMenuDialog.show();
                }

            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        view_publictitle_title.setText("我的分享");
        gson = new Gson();
        UserID = ShareUitls.getString(MyShareActivity.this, "UID", "0");
        linearLayoutManager = new LinearLayoutManager(MyShareActivity.this);
        activity_myshare_recyclerView.setLayoutManager(linearLayoutManager);
       // activity_myshare_recyclerView.getItemAnimator().setChangeDuration(0);
       activity_myshare_recyclerView.setItemAnimator(new MyItemAnimator());//自定义动画解决点赞后刷新item图片闪烁
        circleRecyclerViewAdapter = new CircleRecyclerViewAdapter(CircleList.getInstance().mycirclelist, MyShareActivity.this, handler,true);
        activity_myshare_recyclerView.setAdapter(circleRecyclerViewAdapter);
        activity_myshare_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                activity_myshare_SwipeRefreshLayout.setRefreshing(true);
                getAllContent(8);
            }
        });
        activity_myshare_recyclerView.addOnScrollListener(
                new EndLessOnScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int currentPage) {

                        getAllContent(7);
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                     /*   LinearLayoutManager layoutManager = (LinearLayoutManager) activity_myshare_recyclerView.getLayoutManager();
                        //获取可视的第一个view
                        View topView = layoutManager.getChildAt(0);
                        if (topView != null) {
                            //获取与该view的顶部的偏移量
                            int lastOffset = topView.getTop();
                            //得到该View的数组位置
                            int lastPosition = layoutManager.getPosition(topView);
                            ShareUitls.putString(MyShareActivity.this, "mycirclelastOffset", lastOffset + "");
                            ShareUitls.putString(MyShareActivity.this, "mycirclelastPosition", lastPosition + "");
                        }*/

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }


                }

        );

        getAllContent(0);
    /*    if (CircleList.getInstance().mycirclelist.size() == 0) {
            ShareUitls.putString(MyShareActivity.this, "mycirclelastOffset", "0");
            ShareUitls.putString(MyShareActivity.this, "mycirclelastPosition", "0");
            maidongcircle_nodata.setVisibility(View.VISIBLE);
            getAllContent(0);

        } else {
            maidongcircle_nodata.setVisibility(View.GONE);
            int lastOffset = Integer.parseInt(ShareUitls.getString(MyShareActivity.this, "mycirclelastOffset", "0"));
            int lastPosition = Integer.parseInt(ShareUitls.getString(MyShareActivity.this, "mycirclelastPosition", "0"));

            if (activity_myshare_recyclerView.getLayoutManager() != null && lastPosition >= 0) {
                ((LinearLayoutManager) activity_myshare_recyclerView.getLayoutManager()).scrollToPositionWithOffset(lastPosition, lastOffset);
            }

        }*/
        view_publictitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CircleList.getInstance().mycirclelist.clear();
                CircleList.getInstance().commentlist.clear();
                CircleList.getInstance().replylist.clear();
                finish();
            }
        });

    }

    private void DeleteCircle(final int position) {
        Log.i("AAAAAAAAA", "准备删除");
        Circle circle = CircleList.getInstance().mycirclelist.get(position);

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostShareUpdateRequest");

        params.addBodyParameter("UID", UserID+ "");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(MyShareActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UserID", UserID);
        params.addBodyParameter("ContentID", circle.getContentID());
        params.addBodyParameter("CommentID", "0");
        params.addBodyParameter("ReplyID", "0");
        HttpUtils.getInstance(MyShareActivity.this).sendRequestRequestParams("正在删除...", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("AAAAAAAAA", "Log" + response);
                        CircleList.getInstance().mycirclelist.remove(position);
                        circleRecyclerViewAdapter.notifyItemRemoved(position);
                        if (CircleList.getInstance().mycirclelist.size() == 0) {
                            activity_myshare_recyclerView.setVisibility(View.GONE);
                            maidongcircle_nodata.setVisibility(View.VISIBLE);
                        } else {
                            activity_myshare_recyclerView.setVisibility(View.VISIBLE);
                            maidongcircle_nodata.setVisibility(View.GONE);
                        }
                        CircleList.getInstance().circlelist.clear();
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        // Log.i("AAAAAAAAAshibai", "Log" + error.toString());
                        Toast.makeText(MyShareActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }
    //参数：UserID, ContentID， flag 后两个值初始为0，上拉时传入当前页面第一条内容ContentID，flag=7；下拉时传入当前页面最后一条内容的ContentID,flag=8
    private void getAllContent(final int flag) {


        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetShareContentRequest");

        params.addBodyParameter("UID", UserID + "");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(MyShareActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("AuthorUserID",UserID);
        params.addBodyParameter("MyUserID",UserID);
        if (flag == 8) {
            if (CircleList.getInstance().mycirclelist.size() != 0) {
                params.addBodyParameter("ContentID", CircleList.getInstance().mycirclelist.get(0).getContentID());
            } else {
                params.addBodyParameter("ContentID", "0");
            }

        } else if (flag == 7) {
            if (CircleList.getInstance().mycirclelist.size() != 0) {

                params.addBodyParameter("ContentID", CircleList.getInstance().mycirclelist.get(CircleList.getInstance().mycirclelist.size() - 1).getContentID());
            } else {
                params.addBodyParameter("ContentID", "0");
            }

        } else {
            params.addBodyParameter("ContentID", "0");
        }

        params.addBodyParameter("flag", flag + "");

        Log.i("onResponse  ", flag + "");
        HttpUtils.getInstance(MyShareActivity.this).sendRequestRequestParams("加载中...", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                       // String response="{\"Status\":1,\"ShareContentList\":[{\"UserID\":\"1243\",\"UserRealname\":\"枫雨潇\",\"UserImageUrl\":\"UserHeadImage/1489651592579.png\",\"ContentID\":\"20170814115541131\",\"ContentText\":\"go\",\"PicUrl\":[\"ShareContentPicture/20170814/1501233859398.png\"],\"CommentCount\":\"0\",\"AttitudeCount\":\"0\",\"CreateTime\":\"2017/8/14 11:55:41\",\"IsAttitude\":\"0\"},{\"UserID\":\"1243\",\"UserRealname\":\"枫雨潇\",\"UserImageUrl\":\"UserHeadImage/1489651592579.png\",\"ContentID\":\"20170814115541131\",\"ContentText\":\"go\",\"PicUrl\":[\"ShareContentPicture/20170814/1501233859398.png\"],\"CommentCount\":\"0\",\"AttitudeCount\":\"0\",\"CreateTime\":\"2017/8/14 11:55:41\",\"IsAttitude\":\"0\"}],\"Message\":\"获取成功!\",\"IsSuccess\":true,\"IsError\":false,\"ErrMsg\":null,\"ErrCode\":null,\"ResultJWT\":null}";
                        Log.i("onResponse  ",flag+"   "+ response);
                        ShareContentList shareContentList = gson.fromJson(response.toString(), ShareContentList.class);
                        if (shareContentList.ShareContentList != null) {
                            int size = shareContentList.ShareContentList.size();
                            if (shareContentList.Status.equals("1") && size != 0) {
//08-14 11:55:49.876 26573-26573/com.headlth.management I/AAAAAAAAAA: {"Status":1,"ShareContentList":[{"UserID":"1243","UserRealname":"枫雨潇","UserImageUrl":"UserHeadImage/1489651592579.png","ContentID":"20170814115541131","ContentText":"go","PicUrl":["ShareContentPicture/20170814/1501233859398.png"],"CommentCount":"0","AttitudeCount":"0","CreateTime":"2017/8/14 11:55:41","IsAttitude":"0"}],"Message":"获取成功!","IsSuccess":true,"IsError":false,"ErrMsg":null,"ErrCode":null,"ResultJWT":null}'

                                // Collections.reverse(shareContentList.ShareContentList);
                                if (flag == 8) {
                                    Log.i("AAAAAAAAA11  ", response);
                                    CircleList.getInstance().mycirclelist.addAll(0, shareContentList.ShareContentList);
                                    circleRecyclerViewAdapter.notifyItemRangeInserted(0, shareContentList.ShareContentList.size());
                                } else if (flag == 7) {
                                    if (CircleList.getInstance().mycirclelist.size() == 0) {
                                        CircleList.getInstance().mycirclelist.addAll(0, shareContentList.ShareContentList);
                                    } else {
                                        CircleList.getInstance().mycirclelist.addAll(CircleList.getInstance().mycirclelist.size(), shareContentList.ShareContentList);
                                    }
                                    circleRecyclerViewAdapter.notifyItemRangeInserted(CircleList.getInstance().mycirclelist.size(), shareContentList.ShareContentList.size());
                                } else {
                                    Log.i("AAAAAAAAA22  ", response);
                                    CircleList.getInstance().mycirclelist.addAll(0, shareContentList.ShareContentList);
                                    circleRecyclerViewAdapter.notifyItemRangeInserted(0, shareContentList.ShareContentList.size());
                                   // circleRecyclerViewAdapter.notifyDataSetChanged();
                                }
                            }else {
                                Toast.makeText(MyShareActivity.this," 没有更多内容",Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(MyShareActivity.this," 没有更多内容",Toast.LENGTH_LONG).show();
                        }

                        Log.i("AAAAAAAAAXX  ", CircleList.getInstance().mycirclelist.size()+" "+flag);
                        if (CircleList.getInstance().mycirclelist.size() == 0) {
                            activity_myshare_recyclerView.setVisibility(View.GONE);
                            maidongcircle_nodata.setVisibility(View.VISIBLE);
                        } else {
                            activity_myshare_recyclerView.setVisibility(View.VISIBLE);
                            maidongcircle_nodata.setVisibility(View.GONE);
                        }
                        if (flag == 8) {
                            activity_myshare_SwipeRefreshLayout.setRefreshing(false);
                            ((LinearLayoutManager) activity_myshare_recyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
                        }else if (flag == 7){
                            activity_myshare_SwipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override//circlelist
                    public void onErrorResponse(Throwable ex) {
                        if (CircleList.getInstance().mycirclelist.size() == 0) {
                            activity_myshare_recyclerView.setVisibility(View.GONE);
                            maidongcircle_nodata.setVisibility(View.VISIBLE);
                        } else {
                            activity_myshare_recyclerView.setVisibility(View.VISIBLE);
                            maidongcircle_nodata.setVisibility(View.GONE);
                        }
                    }
                }

        );


        //新增一条评论：  参数：UserID 用户ID ContentID 要评论的帖子ID ContentText 评论的内容
        //获取评论详情：
        //新增一条回复：MdMobileService.ashx?do=PostShareCommentReplyRequestUserID：用户ID， ContentID：分享内容的ID，CommentID：评论的ID，ReplyText：回复的内容        BelongReplyID:默认为0
        //获取回复：MdMobileService.ashx?do=GetShareReplyRequestUserID  ContentID  CommentID
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("resultCode", "" + resultCode + "  " + requestCode);
        if (resultCode == 258) {
            getAllContent(8);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (IS_ContentDetailsActivity) {
            circleRecyclerViewAdapter.notifyDataSetChanged();
            IS_ContentDetailsActivity = false;
        }
    }

    class ShareContentList {
        String Status;
        List<Circle> ShareContentList;
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            CircleList.getInstance().mycirclelist.clear();
            CircleList.getInstance().commentlist.clear();
            CircleList.getInstance().replylist.clear();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
