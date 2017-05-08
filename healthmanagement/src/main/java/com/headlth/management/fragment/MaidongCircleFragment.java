package com.headlth.management.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.activity.ContentDetailsActivity;
import com.headlth.management.activity.ShareActivity;
import com.headlth.management.adapter.CircleAdapter;
import com.headlth.management.entity.Circle;
import com.headlth.management.entity.CircleList;
import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.myview.PullToRefreshLayout;
import com.headlth.management.myview.PullableListView;
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
@ContentView(R.layout.fragment_maidongcircle)
public class MaidongCircleFragment extends BaseFragment {
    @ViewInject(R.id.maidongcircle_listview)
    private PullableListView maidongcircle_listview;
    @ViewInject(R.id.refresh_view)
    private PullToRefreshLayout ptrl;
    @ViewInject(R.id.maidongcircle_nodata)
    private RelativeLayout maidongcircle_nodata;


    private CircleAdapter circleAdapter;
    private RelativeLayout share;
    private ImageView main_share_add;

    private String userIDFlag = "0";
    private int flag;
    private String UserID;
    Gson gson;
    private BottomMenuDialog bottomMenuDialog;

    private TopRightMenu mTopRightMenu;  //点击加号的弹框


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        mTopRightMenu = new TopRightMenu(getActivity());
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gson = new Gson();
        UserID = ShareUitls.getString(getActivity(), "UID", "0");
        share = (RelativeLayout) getActivity().findViewById(R.id.main_share);
        main_share_add = (ImageView) getActivity().findViewById(R.id.main_share_add);

        long circleOldTime = Long.parseLong(ShareUitls.getString(getActivity(), "circleOldTime", "0"));
        long circleNewTime = new Date().getTime();
        ShareUitls.putString(getActivity(), "circleOldTime", circleNewTime + "");
        if (circleNewTime - circleOldTime >= 30 * 60000) {
            CircleList.getInstance().circlelist.clear();
            CircleList.getInstance().commentlist.clear();
            CircleList.getInstance().replylist.clear();

            Glide.get(getActivity()).clearMemory();
            //b;清理磁盘缓存
        }
        circleAdapter = new CircleAdapter(CircleList.getInstance().circlelist, getActivity());
        maidongcircle_listview.setAdapter(circleAdapter);
        if (CircleList.getInstance().circlelist.size() == 0) {
            getAllContent(0, null);
            maidongcircle_nodata.setVisibility(View.VISIBLE);
        } else {
            maidongcircle_nodata.setVisibility(View.GONE);
            int position = Integer.parseInt(ShareUitls.getString(getActivity(), "circleListviewPosition", "0"));
            maidongcircle_listview.setSelection(position);

        }

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
     share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           mTopRightMenu
                .setHeight(ImageUtil.dp2px(getActivity(),150))     //默认高度480
                .setWidth(ImageUtil.dp2px(getActivity(),80))      //默认宽度wrap_content
                .showIcon(true)     //显示菜单图标，默认为true
                .dimBackground(true)           //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
           /*     .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE*/
                .addMenuItem(new MenuItem( "发表"))
                .addMenuItem(new MenuItem(" 我"))
                .addMenuItem(new MenuItem("全部"))
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                      switch (position){
                          case 0:
                              Intent intent = new Intent(getActivity(), ShareActivity.class);
                              intent.putExtra("pictime", "");
                              intent.putExtra("share", "first");
                              startActivityForResult(intent, 147);
                              break;
                          case 1:
                              userIDFlag = ShareUitls.getString(getActivity(), "UID", "0");
                              CircleList.getInstance().circlelist.clear();
                              Glide.get(getActivity()).clearMemory();
                              getAllContent(1, null);
                              break;
                          case 2:
                              userIDFlag = "0";
                              CircleList.getInstance().circlelist.clear();
                              Glide.get(getActivity()).clearMemory();
                              getAllContent(0, null);
                              break;

                      }
                    }
                })
                .showAsDropDown(main_share_add, 20, 0);
                  /*
                View dialog_circle_share_view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_circle_share, null);
                TextView send = (TextView) dialog_circle_share_view.findViewById(R.id.dialog_circle_share_send);
                TextView i = (TextView) dialog_circle_share_view.findViewById(R.id.dialog_circle_share_i);
                TextView all = (TextView) dialog_circle_share_view.findViewById(R.id.dialog_circle_share_all);

                final PopupWindow share_popupWindoww = new PopupWindow(dialog_circle_share_view, dip2px(getActivity(), 80), dip2px(getActivity(), 150), true);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ShareActivity.class);
                        intent.putExtra("pictime", "");
                        intent.putExtra("share", "first");
                        startActivityForResult(intent, 147);
                        share_popupWindoww.dismiss();
                    }
                });
                i.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userIDFlag = ShareUitls.getString(getActivity(), "UID", "0");
                        CircleList.getInstance().circlelist.clear();
                        Glide.get(getActivity()).clearMemory();

                        getAllContent(1, null);
                        share_popupWindoww.dismiss();

                    }
                });
                all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userIDFlag = "0";
                        CircleList.getInstance().circlelist.clear();
                        Glide.get(getActivity()).clearMemory();
                        getAllContent(0, null);
                        share_popupWindoww.dismiss();
                    }
                });

                share_popupWindoww.setOutsideTouchable(true);
                share_popupWindoww.setFocusable(true);
                share_popupWindoww.setBackgroundDrawable(new ColorDrawable(0x00000000));
                share_popupWindoww.showAsDropDown(share);*/
            }


        });
        maidongcircle_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CircleList.getInstance().commentlist.clear();
                Intent intent = new Intent(getActivity(), ContentDetailsActivity.class);
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

                    bottomMenuDialog = new BottomMenuDialog.Builder(getActivity())
                            .addMenu("删除", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    DeleteCircle(position);

                                    bottomMenuDialog.dismiss();
                                }
                            }).create();
                    bottomMenuDialog.show();;
                }
                return true;
            }
        });
        maidongcircle_listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            /**
             * 滚动状态改变时调用
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 不滚动时保存当前滚动到的位置
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    ShareUitls.putString(getActivity(), "circleListviewPosition", maidongcircle_listview.getFirstVisiblePosition() + "");
                }
            }

            /**
             * 滚动时调用
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

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
        HttpUtils.getInstance(getActivity()).sendRequestRequestParams("正在删除...", params,true, new HttpUtils.ResponseListener() {
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
                        Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }

    //参数：UserID, ContentID， flag 后两个值初始为0，上拉时传入当前页面第一条内容ContentID，flag=7；下拉时传入当前页面最后一条内容的ContentID,flag=8
    private void getAllContent(final int flag, final PullToRefreshLayout pullToRefreshLayout) {


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
        HttpUtils.getInstance(getActivity()).sendRequestRequestParams("正在加载请稍后...", params,true, new HttpUtils.ResponseListener() {

//   //请求：MdMobileService.ashx?do=GetShareContentRequest。   参数：AuthorUserID:（作者的UserID，0为所有）,
        // ContentID:（分享ID，仍然是上拉时传入当页最后一个ContentID，下拉时传入当页第一个ContentID），MyUserID: (本人的UserID），flag:(7为上拉，8为下拉）

                    @Override
                    public void onResponse(String response) {
                        Log.i("AAAAAAAAA  ", response);
                        ShareContentList shareContentList = gson.fromJson(response.toString(), ShareContentList.class);
                        if (shareContentList.ShareContentList!=null) {
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
                        }else {
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


        //新增一条评论：  参数：UserID 用户ID ContentID 要评论的帖子ID ContentText 评论的内容
        //获取评论详情：
        //新增一条回复：MdMobileService.ashx?do=PostShareCommentReplyRequestUserID：用户ID， ContentID：分享内容的ID，CommentID：评论的ID，ReplyText：回复的内容        BelongReplyID:默认为0
        //获取回复：MdMobileService.ashx?do=GetShareReplyRequestUserID  ContentID  CommentID
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("mmmmmmain", "mmmmmmain");
        circleAdapter.notifyDataSetChanged();
    }

    private void sendReply() {

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("resultCode", "" + resultCode + "  " + requestCode);
        if (resultCode == 258) {
            getAllContent(8, null);
        }

    }
}
