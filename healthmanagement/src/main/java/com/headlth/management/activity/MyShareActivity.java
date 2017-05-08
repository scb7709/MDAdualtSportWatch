package com.headlth.management.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.App;
import com.headlth.management.adapter.CircleAdapter;
import com.headlth.management.entity.Circle;
import com.headlth.management.entity.CircleList;
import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.myview.PullToRefreshLayout;
import com.headlth.management.myview.PullableListView;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abc on 2016/11/18.
 */

@ContentView(R.layout.activity_myshare)
public class MyShareActivity extends BaseActivity {


    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;


    @ViewInject(R.id.activity_myshare_listview)
    private PullableListView maidongcircle_listview;
    @ViewInject(R.id.activity_myshare_refresh_view)
    private PullToRefreshLayout ptrl;
    @ViewInject(R.id.activity_myshare_nodata)
    private RelativeLayout maidongcircle_nodata;




    private CircleAdapter circleAdapter;
    private Button share;
    private String userIDFlag = "0";
    private int flag;
    private String UserID;
    Gson gson;
    private BottomMenuDialog bottomMenuDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        view_publictitle_title.setText("我的分享");
        gson = new Gson();
        UserID = ShareUitls.getString(MyShareActivity.this, "UID", "0");
        CircleList.getInstance().circlelist.clear();
        CircleList.getInstance().commentlist.clear();
        CircleList.getInstance().replylist.clear();
        circleAdapter = new CircleAdapter(CircleList.getInstance().circlelist, MyShareActivity.this);
        maidongcircle_listview.setAdapter(circleAdapter);
        getAllContent(1, null);
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getAllContent(8, pullToRefreshLayout);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getAllContent(7, pullToRefreshLayout);
            }
        });
        maidongcircle_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CircleList.getInstance().commentlist.clear();
                Intent intent = new Intent(MyShareActivity.this, ContentDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("circle", (Serializable) CircleList.getInstance().circlelist.get(position));
                bundle.putString("position", "" + position);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        maidongcircle_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.i("VVVV", UserID + "  " + CircleList.getInstance().circlelist.get(position).getUserID());
                if (UserID.equals(CircleList.getInstance().circlelist.get(position).getUserID())) {

                    bottomMenuDialog = new BottomMenuDialog.Builder(MyShareActivity.this)
                            .addMenu("删除", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    DeleteCircle(position);

                                    bottomMenuDialog.dismiss();
                                }
                            }).create();
                    bottomMenuDialog.show();
                    ;
                }
                return true;
            }
        });

        view_publictitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CircleList.getInstance().circlelist.clear();
                CircleList.getInstance().commentlist.clear();
                CircleList.getInstance().replylist.clear();
                finish();
            }
        });

    }

    //MdMobileService.ashx?do=PostShareUpdateRequest。 UserID:用户ID，ContentID：删除的帖子ID，CommentID:删除的评论ID（如果删除的是帖子则此项为0）,ReplyID(为0）
    private void DeleteCircle(final int position) {
        Circle circle = CircleList.getInstance().circlelist.get(position);
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostShareUpdateRequest");
        params.addBodyParameter("UID",ShareUitls.getString(MyShareActivity.this, "UID", "0"));
        params.addBodyParameter("ResultJWT",ShareUitls.getString(MyShareActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("ContentID", circle.getContentID());
        params.addBodyParameter("CommentID", "0");
        params.addBodyParameter("ReplyID", "0");
        HttpUtils.getInstance(MyShareActivity.this).sendRequestRequestParams(Constant.DIALOG_MESSAGE_LOADING,params ,true,
     new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("AAAAAAAAA", "Log" + response);
                        CircleList.getInstance().circlelist.remove(position);
                        circleAdapter.notifyDataSetChanged();
                        if (CircleList.getInstance().circlelist.size() == 0) {
                            maidongcircle_listview.setVisibility(View.GONE);
                            maidongcircle_nodata.setVisibility(View.VISIBLE);
                        } else {
                            maidongcircle_listview.setVisibility(View.VISIBLE);
                            maidongcircle_nodata.setVisibility(View.GONE);
                        }
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
    private void getAllContent(final int flag, final PullToRefreshLayout pullToRefreshLayout) {
        userIDFlag = ShareUitls.getString(MyShareActivity.this, "UID", "0");
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetShareContentRequest");
        params.addBodyParameter("UID",ShareUitls.getString(MyShareActivity.this, "UID", "0"));
        params.addBodyParameter("ResultJWT",ShareUitls.getString(MyShareActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("AuthorUserID", userIDFlag);
        params.addBodyParameter("MyUserID", ShareUitls.getString(MyShareActivity.this, "UID", "0"));
        if (flag == 8) {
            if (CircleList.getInstance().circlelist.size() != 0) {
                params.addBodyParameter("ContentID", CircleList.getInstance().circlelist.get(0).getContentID());
            } else {
                params.addBodyParameter("ContentID", "0");
            }

        } else if (flag == 7) {
            if (CircleList.getInstance().circlelist.size() != 0) {

                params.addBodyParameter("ContentID", CircleList.getInstance().circlelist.get(CircleList.getInstance().circlelist.size() - 1).getContentID());
            } else {
                params.addBodyParameter("ContentID", "0");
            }

        } else {
            params.addBodyParameter("ContentID", "0");
        }

        params.addBodyParameter("flag", flag + "");
      
//   //请求：MdMobileService.ashx?do=GetShareContentRequest。   参数：AuthorUserID:（作者的UserID，0为所有）,
        // ContentID:（分享ID，仍然是上拉时传入当页最后一个ContentID，下拉时传入当页第一个ContentID），MyUserID: (本人的UserID），flag:(7为上拉，8为下拉）
        HttpUtils.getInstance(MyShareActivity.this).sendRequestRequestParams(Constant.DIALOG_MESSAGE_LOADING,params ,true,new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("AAAAAAAAA  ", response);
                        ShareContentList shareContentList = gson.fromJson(response.toString(), ShareContentList.class);
                        if (shareContentList.ShareContentList != null) {
                            int size = shareContentList.ShareContentList.size();
                            if (shareContentList.Status.equals("1") && size != 0) {

                                // Collections.reverse(shareContentList.ShareContentList);
                                if (flag == 8) {
                                    CircleList.getInstance().circlelist.addAll(0, shareContentList.ShareContentList);
                                } else if (flag == 7) {
                                    if (CircleList.getInstance().circlelist.size() == 0) {
                                        CircleList.getInstance().circlelist.addAll(0, shareContentList.ShareContentList);
                                    } else {
                                        CircleList.getInstance().circlelist.addAll(CircleList.getInstance().circlelist.size(), shareContentList.ShareContentList);
                                    }
                                } else {
                                    CircleList.getInstance().circlelist.addAll(0, shareContentList.ShareContentList);
                                }


                                circleAdapter.notifyDataSetChanged();
                                // maidongcircle_listview.setAdapter(circleAdapter);

                            }

                            if (flag == 8) {
                                if (pullToRefreshLayout != null) {
                                    pullToRefreshLayout.refreshFinish(size);
                                }

                            } else if (flag == 7) {
                                if (pullToRefreshLayout != null) {
                                    pullToRefreshLayout.loadmoreFinish(size);
                                }
                            }
                        } else {
                            if (flag == 8) {
                                if (pullToRefreshLayout != null) {
                                    pullToRefreshLayout.refreshFinish(-1);
                                }

                            } else if (flag == 7) {
                                if (pullToRefreshLayout != null) {
                                    pullToRefreshLayout.loadmoreFinish(-1);
                                }
                            }
                        }
                        if (CircleList.getInstance().circlelist.size() == 0) {
                            maidongcircle_listview.setVisibility(View.GONE);
                            maidongcircle_nodata.setVisibility(View.VISIBLE);
                        } else {
                            maidongcircle_listview.setVisibility(View.VISIBLE);
                            maidongcircle_nodata.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        if (CircleList.getInstance().circlelist.size() == 0) {
                            maidongcircle_listview.setVisibility(View.GONE);
                            maidongcircle_nodata.setVisibility(View.VISIBLE);
                        } else {
                            maidongcircle_listview.setVisibility(View.VISIBLE);
                            maidongcircle_nodata.setVisibility(View.GONE);
                        }
                        if (flag == 8) {
                            if (pullToRefreshLayout != null) {
                                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                            }

                        } else if (flag == 7) {
                            if (pullToRefreshLayout != null) {
                                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                            }
                        }

                    }
                }

        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("resultCode", "" + resultCode + "  " + requestCode);
        if (resultCode == 258) {
            getAllContent(8, null);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("mmmmmmain", "mmmmmmain");
        circleAdapter.notifyDataSetChanged();
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
            CircleList.getInstance().circlelist.clear();
            CircleList.getInstance().commentlist.clear();
            CircleList.getInstance().replylist.clear();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
