package com.headlth.management.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.activity.ContentDetailsActivity;
import com.headlth.management.activity.ShareNewActivity;
import com.headlth.management.adapter.CircleRecyclerViewAdapter;
import com.headlth.management.entity.Circle;
import com.headlth.management.entity.CircleList;
import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.myview.EndLessOnScrollListener;
import com.headlth.management.myview.MyItemAnimator;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ImageUtil;
import com.headlth.management.utils.ShareUitls;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by abc on 2016/8/8.
 */
@ContentView(R.layout.new_fragment_maidongcircle)
public class NewMaidongCircleFragment extends BaseFragment {

    @ViewInject(R.id.frgment_maidongcircle_SwipeRefreshLayout)
    private SwipeRefreshLayout frgment_maidongcircle_SwipeRefreshLayout;


    @ViewInject(R.id.frgment_maidongcircle_recyclerView)
    private RecyclerView frgment_maidongcircle_recyclerView;

    @ViewInject(R.id.maidongcircle_nodata)
    private RelativeLayout maidongcircle_nodata;


    private CircleRecyclerViewAdapter circleRecyclerViewAdapter;
    private RelativeLayout share;
    private ImageView main_share_add;

    private String userIDFlag = "0";
    private int flag;
    private String UserID;
    private String UID;
    Gson gson;
    private BottomMenuDialog bottomMenuDialog;

    private TopRightMenu mTopRightMenu;  //点击加号的弹框
    LinearLayoutManager linearLayoutManager;
    boolean IS_ContentDetailsActivity;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int position = msg.arg1;
            if (msg.arg2 == 0) {
                CircleList.getInstance().commentlist.clear();
                Intent intent = new Intent(getActivity(), ContentDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("circle", CircleList.getInstance().circlelist.get(position));
                bundle.putString("position", "" + position);
                bundle.putString("flag", "NewMaidongCircleFragment");
                intent.putExtras(bundle);
                startActivity(intent);
                IS_ContentDetailsActivity = true;

            } else {

                Log.i("VVVV", UserID + "  " + CircleList.getInstance().circlelist.get(position).getUserID());
                if (UserID.equals(CircleList.getInstance().circlelist.get(position).getUserID())) {

                    bottomMenuDialog = new BottomMenuDialog.Builder(getActivity())
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

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        mTopRightMenu = new TopRightMenu(getActivity());
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();

    }

    private void initialize() {
        gson = new Gson();
        UserID = ShareUitls.getString(getActivity(), "UID", "0");
        share = (RelativeLayout) getActivity().findViewById(R.id.main_share);
        main_share_add = (ImageView) getActivity().findViewById(R.id.main_share_add);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        frgment_maidongcircle_recyclerView.setLayoutManager(linearLayoutManager);
        frgment_maidongcircle_recyclerView.setItemAnimator(new MyItemAnimator());
     /*
        ((SimpleItemAnimator)frgment_maidongcircle_recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);*/


        //frgment_maidongcircle_recyclerView.getItemAnimator().setChangeDuration(0);
        long circleOldTime = Long.parseLong(ShareUitls.getString(getActivity(), "circleOldTime", "0"));
        long circleNewTime = new Date().getTime();
        if (circleNewTime - circleOldTime >= 30 * 60000) {
            ShareUitls.putString(getActivity(), "circleOldTime", circleNewTime + "");
            CircleList.getInstance().circlelist.clear();
            CircleList.getInstance().commentlist.clear();
            CircleList.getInstance().replylist.clear();

            Glide.get(getActivity()).clearMemory();
            //b;清理磁盘缓存
        }

       circleRecyclerViewAdapter = new CircleRecyclerViewAdapter(CircleList.getInstance().circlelist, getActivity(), handler,false);
        frgment_maidongcircle_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                frgment_maidongcircle_SwipeRefreshLayout.setRefreshing(true);
                getAllContent(8);
            }
        });


        frgment_maidongcircle_recyclerView.addOnScrollListener(
                new EndLessOnScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int currentPage) {

                        getAllContent(7);
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        LinearLayoutManager layoutManager = (LinearLayoutManager) frgment_maidongcircle_recyclerView.getLayoutManager();
                        //获取可视的第一个view
                        View topView = layoutManager.getChildAt(0);
                        if (topView != null) {
                            //获取与该view的顶部的偏移量
                            int lastOffset = topView.getTop();
                            //得到该View的数组位置
                            int lastPosition = layoutManager.getPosition(topView);
                            ShareUitls.putString(getActivity(), "circlelastOffset", lastOffset + "");
                            ShareUitls.putString(getActivity(), "circlelastPosition", lastPosition + "");
                        }

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }


                }

        );
        if (CircleList.getInstance().circlelist.size() == 0) {
            ShareUitls.putString(getActivity(), "circlelastOffset", "0");
            ShareUitls.putString(getActivity(), "circlelastPosition", "0");
            maidongcircle_nodata.setVisibility(View.VISIBLE);
            getAllContent(0);

        } else {
            maidongcircle_nodata.setVisibility(View.GONE);
            int lastOffset = Integer.parseInt(ShareUitls.getString(getActivity(), "circlelastOffset", "0"));
            int lastPosition = Integer.parseInt(ShareUitls.getString(getActivity(), "circlelastPosition", "0"));
            if (frgment_maidongcircle_recyclerView.getLayoutManager() != null && lastPosition >= 0) {
                ((LinearLayoutManager) frgment_maidongcircle_recyclerView.getLayoutManager()).scrollToPositionWithOffset(lastPosition, lastOffset);
            }

        }
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTopRightMenu
                        .setHeight(ImageUtil.dp2px(getActivity(), 150))     //默认高度480
                        .setWidth(ImageUtil.dp2px(getActivity(), 80))      //默认宽度wrap_content
                        .showIcon(true)     //显示菜单图标，默认为true
                        .dimBackground(true)           //背景变暗，默认为true
                        .needAnimationStyle(true)   //显示动画，默认为true
           /*     .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE*/
                        .addMenuItem(new MenuItem("发表"))
                        .addMenuItem(new MenuItem(" 我"))
                        .addMenuItem(new MenuItem("全部"))
                        .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                            @Override
                            public void onMenuItemClick(int position) {
                                switch (position) {
                                    case 0:
                                        Log.i("mybule", "  ShareNewActivity");
                                        Intent intent = new Intent(getActivity(), ShareNewActivity.class);
                                        intent.putExtra("pictime", "");
                                        intent.putExtra("share", "first");
                                        startActivityForResult(intent, 147);
                                        break;
                                    case 1:
                                        userIDFlag = ShareUitls.getString(getActivity(), "UID", "0");
                                        CircleList.getInstance().circlelist.clear();
                                        Glide.get(getActivity()).clearMemory();
                                        getAllContent(1);
                                        break;
                                    case 2:
                                        userIDFlag = "0";
                                        CircleList.getInstance().circlelist.clear();
                                        Glide.get(getActivity()).clearMemory();
                                        getAllContent(0);
                                        break;

                                }
                            }
                        })
                        .showAsDropDown(main_share_add, 20, 0);
            }


        });
       // circleRecyclerViewAdapter = new CircleRecyclerViewAdapter(CircleList.getInstance().circlelist, getActivity(), handler);
        frgment_maidongcircle_recyclerView.setAdapter(circleRecyclerViewAdapter);
    }

    //MdMobileService.ashx?do=PostShareUpdateRequest。 UserID:用户ID，ContentID：删除的帖子ID，CommentID:删除的评论ID（如果删除的是帖子则此项为0）,ReplyID(为0）
    private void DeleteCircle(final int position) {
        Log.i("AAAAAAAAA", "准备删除");
        Circle circle = CircleList.getInstance().circlelist.get(position);

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostShareUpdateRequest");

        params.addBodyParameter("UID", ShareUitls.getString(getActivity(), "UID", "") + "");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(getActivity(), "ResultJWT", "0"));
        params.addBodyParameter("UserID", ShareUitls.getString(getActivity(), "UID", "0"));
        params.addBodyParameter("ContentID", circle.getContentID());
        params.addBodyParameter("CommentID", "0");
        params.addBodyParameter("ReplyID", "0");
        HttpUtils.getInstance(getActivity()).sendRequestRequestParams("正在删除...", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("AAAAAAAAA", "Log" + response);
                        CircleList.getInstance().circlelist.remove(position);
                        circleRecyclerViewAdapter.notifyItemRemoved(position);
                        if (CircleList.getInstance().circlelist.size() == 0) {
                            frgment_maidongcircle_recyclerView.setVisibility(View.GONE);
                            maidongcircle_nodata.setVisibility(View.VISIBLE);
                        } else {
                            frgment_maidongcircle_recyclerView.setVisibility(View.VISIBLE);
                            maidongcircle_nodata.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        // Log.i("AAAAAAAAAshibai", "Log" + error.toString());
                        Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }

    //参数：UserID, ContentID， flag 后两个值初始为0，上拉时传入当前页面第一条内容ContentID，flag=7；下拉时传入当前页面最后一条内容的ContentID,flag=8
    private void getAllContent(final int flag) {


        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetShareContentRequest");

        params.addBodyParameter("UID", ShareUitls.getString(getActivity(), "UID", "") + "");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(getActivity(), "ResultJWT", "0"));
        params.addBodyParameter("AuthorUserID", userIDFlag);
        params.addBodyParameter("MyUserID", ShareUitls.getString(getActivity(), "UID", "0"));
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

        Log.i("onResponse  ", flag + "");
        HttpUtils.getInstance(getActivity()).sendRequestRequestParams("加载中...", params, true, new HttpUtils.ResponseListener() {

//   //请求：MdMobileService.ashx?do=GetShareContentRequest。   参数：AuthorUserID:（作者的UserID，0为所有）,
                    // ContentID:（分享ID，仍然是上拉时传入当页最后一个ContentID，下拉时传入当页第一个ContentID），MyUserID: (本人的UserID），flag:(7为上拉，8为下拉）

                    @Override
                    public void onResponse(String response) {
                        Log.i("onResponse  ", response);
                        ShareContentList shareContentList = gson.fromJson(response.toString(), ShareContentList.class);
                        if (shareContentList.ShareContentList != null) {
                            int size = shareContentList.ShareContentList.size();
                            if (shareContentList.Status.equals("1") && size != 0) {

                                // Collections.reverse(shareContentList.ShareContentList);
                                if (flag == 8) {
                                    Log.i("AAAAAAAAA11  ", response);
                                    CircleList.getInstance().circlelist.addAll(0, shareContentList.ShareContentList);
                                    circleRecyclerViewAdapter.notifyItemRangeInserted(0, shareContentList.ShareContentList.size());
                                } else if (flag == 7) {
                                   CircleList.getInstance().circlelist.addAll(CircleList.getInstance().circlelist.size(), shareContentList.ShareContentList);
                                    circleRecyclerViewAdapter.notifyItemRangeInserted(CircleList.getInstance().circlelist.size(), shareContentList.ShareContentList.size());

                                } else {
                                    Log.i("AAAAAAAAA22  ", response);
                                    CircleList.getInstance().circlelist.addAll(0, shareContentList.ShareContentList);
                                    circleRecyclerViewAdapter.notifyItemRangeInserted(0, shareContentList.ShareContentList.size());
                                }

                            } else {
                                Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
                            }

                        }else {

                            Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
                        }
                        if (CircleList.getInstance().circlelist.size() == 0) {
                            frgment_maidongcircle_recyclerView.setVisibility(View.GONE);
                            maidongcircle_nodata.setVisibility(View.VISIBLE);
                        } else {
                            frgment_maidongcircle_recyclerView.setVisibility(View.VISIBLE);
                            maidongcircle_nodata.setVisibility(View.GONE);
                        }
                        if (flag == 8) {
                            frgment_maidongcircle_SwipeRefreshLayout.setRefreshing(false);
                            ((LinearLayoutManager) frgment_maidongcircle_recyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);

                        } else if (flag == 7) {
                            frgment_maidongcircle_SwipeRefreshLayout.setRefreshing(false);
                        }

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        if (CircleList.getInstance().circlelist.size() == 0) {
                            frgment_maidongcircle_recyclerView.setVisibility(View.GONE);
                            maidongcircle_nodata.setVisibility(View.VISIBLE);
                        } else {
                            frgment_maidongcircle_recyclerView.setVisibility(View.VISIBLE);
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
    public void onResume() {
        super.onResume();
        Log.i("mmmmmmain", "mmmmmmain");
        if (IS_ContentDetailsActivity) {
            circleRecyclerViewAdapter.notifyDataSetChanged();
            IS_ContentDetailsActivity = false;
        }
    }

    private void sendReply() {

    }


    class ShareContentList {
        String Status;
        List<Circle> ShareContentList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("resultCode", "" + resultCode + "  " + requestCode);
        if (resultCode == 258) {
            getAllContent(8);
        }

    }
}
