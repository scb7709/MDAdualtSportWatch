package com.headlth.management.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.adapter.CommentAdapter;
import com.headlth.management.entity.Circle;
import com.headlth.management.entity.CircleList;
import com.headlth.management.entity.Comment;
import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.myview.MGridView;
import com.headlth.management.myview.PullToRefreshLayout;
import com.headlth.management.myview.PullableListView;
import com.headlth.management.myview.RoundImageView;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DataString;
import com.headlth.management.utils.ScreenShot;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

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
    private  Holder holder;

    public  class Holder{


        @ViewInject(R.id.contentdetails_icon)
        public RoundImageView contentdetails_icon;
        @ViewInject(R.id.contentdetails_user)
        public TextView contentdetails_user;
        @ViewInject(R.id.contentdetails_text)
        public TextView contentdetails_text;
        @ViewInject(R.id.contentdetails_time)
        public TextView contentdetails_time;
        @ViewInject(R.id.contentdetails_Scrollgridview)
        public MGridView contentdetails_Scrollgridview;
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
    private View view;//点击图放大
    private PopupWindow popupWindow;
    private ShareCommentList shareContentList;
    private int commentCount;
    private int position;
    private View footer;
    private BottomMenuDialog bottomMenuDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        CircleList.getInstance().replylist.clear();
        view_publictitle_title.setText("动态详情");
        position = Integer.parseInt(getIntent().getStringExtra("position"));
        circle = CircleList.getInstance().circlelist.get(position);
        Log.i("aaaaaaaaaaXIANGQING", circle.toString());
        setData();
        commentAdapter = new CommentAdapter(ContentDetailsActivity.this);
        contentdetails_maidongcircle_listview.setAdapter(commentAdapter);
        // if(CircleList.getInstance().commentlist.size()==0){
        getComment("");
        //  }

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
                        bundle.putSerializable("comment", (Serializable) comment);
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
        holder=new Holder();

        x.view().inject(holder,view);
        //  ViewUtils.inject(this, view);
        Picasso.with(ContentDetailsActivity.this)
                .load(Constant.BASE_URL + "/" + circle.getAvatarUrl())//图片网址
                .placeholder(R.mipmap.hand)//默认图标
                .into(holder.contentdetails_icon);//控件


        holder. contentdetails_user.setText(circle.getUsername());
        holder. contentdetails_time.setText(DataString.showTime(circle.getCreateTime()));
        holder. contentdetails_text.setText(circle.getContentText());

     /*   contentdetails_comment_count.setText(circle.getCommentCount() + "");
        contentdetails_like_count.setText(circle.getLikeCount() + "");*/


        //  if (count != 0 && count != 1 && count != 2 && count != 4) {
        holder.  contentdetails_Scrollgridview.setHorizontalSpacing(5);
        // }
      /*  if (circle.getImageUrls().size() == 4) {
            contentdetails_Scrollgridview.setNumColumns(2);
        } else {
            contentdetails_Scrollgridview.setNumColumns(3);
        }*/
        if (circle.getImageUrls() != null && circle.getImageUrls().size() != 0) {
            holder.   contentdetails_Scrollgridview.setVisibility(View.VISIBLE);
            GridAdapter adapter = new GridAdapter();
            holder. contentdetails_Scrollgridview.setAdapter(adapter);
        } else {
            holder. contentdetails_Scrollgridview.setVisibility(View.GONE);
        }
     /*   contentdetails_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        contentdetails_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        contentdetails_maidongcircle_listview.addHeaderView(view, null, false);
        contentdetails_maidongcircle_listview.setHeaderDividersEnabled(true);
        //禁止底部出现分割线
        contentdetails_maidongcircle_listview.setFooterDividersEnabled(false);

    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器

        public GridAdapter() {
            inflater = LayoutInflater.from(ContentDetailsActivity.this);
            Log.i("ooooooo", circle.getImageUrls().size() + "");
        }


        public int getCount() {
            return (circle.getImageUrls().size());
        }

        public String getItem(int arg0) {

            return circle.getImageUrls().get(arg0);
        }

        public long getItemId(int arg0) {

            return arg0;
        }

        public View getView(final int arg, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_circle_grida, parent, false);
                holder = new ViewHolder();
                holder.image = (Button) convertView.findViewById(R.id.item_grida_circle_image);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.image.setWidth(1000);
            if (position == 1 || position == 2) {
                holder.image.setHeight(dip2px(ContentDetailsActivity.this, 200));
            }
            holder.image.setBackground(getResources().getDrawable(R.drawable.loding));
            setGlide(arg, holder);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPicChangeBig(position, arg);
                }
            });
            return convertView;
        }

        private void setGlide(final int arg, final ViewHolder holder) {
            Glide.with(ContentDetailsActivity.this)
                    .load(Constant.BASE_URL + "/" + getItem(arg))
                    .asBitmap()
                    .skipMemoryCache(true)
                    .override(100, 100)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (bitmap != null) {
                                holder.image.setBackground(new BitmapDrawable(bitmap));
                                holder.image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        clickPicChangeBig(position, arg);

                                    }
                                });
                            } else {
                                holder.image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setGlide(arg, holder);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            holder.image.setBackgroundResource(R.mipmap.nodata);
                            holder.image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setGlide(arg, holder);
                                }
                            });

                        }
                    });
        }

        public class ViewHolder {
            //  public NetworkImageView  image;
            public Button image;
        }
    }

    int prePosition = 0;

    private void clickPicChangeBig(final int position, final int arg0) {
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        view = LayoutInflater.from(ContentDetailsActivity.this).inflate(R.layout.popwindow_clickchangebig, null);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.clickpicchangebig);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.clickpicchangebig_layout);
        final List<ImageView> sublist = new ArrayList<ImageView>();
        linearLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);

        for (int i = 0; i < circle.getImageUrls().size(); i++) {
            //final PhotoView imageView = new PhotoView(activity);
            // loadBigImage(Position, i, imageView);

            ImageView icon = new ImageView(ContentDetailsActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                lp.setMargins(15, 0, 0, 0);
            }
            icon.setLayoutParams(lp);
            icon.setBackgroundResource(R.drawable.black_icon_shape);

            linearLayout.addView(icon);

            // sublist.add(imageView);
        }
        // linearLayout.setPadding(10,0,10,10);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return circle.getImageUrls().size();
            }

            @Override
            public ImageView instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
                PhotoView imageView = new PhotoView(ContentDetailsActivity.this);
                final ProgressBar loading = new ProgressBar(ContentDetailsActivity.this);
                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingLayoutParams.gravity = Gravity.CENTER;
                loading.setLayoutParams(loadingLayoutParams);
                ((FrameLayout) view).addView(loading);
//circle.getImageUrls().size()
                final String imgurl = Constant.BASE_URL + "/" + circle.getImageUrls().get(position);
                loadBigImage(imageView, loading, imgurl);
                imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        bottomMenuDialog = new BottomMenuDialog.Builder(ContentDetailsActivity.this)
                                .addMenu("保存", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Glide.with(ContentDetailsActivity.this)
                                                .load(imgurl)
                                                .asBitmap()
                                                .skipMemoryCache(true)
                                                .into(new SimpleTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                                        if (bitmap != null) {
                                                            String pictime = System.currentTimeMillis() + "";
                                                            ScreenShot.saveMyBitmap(bitmap, pictime, true, ContentDetailsActivity.this);
                                                        } else {
                                                            Toast.makeText(ContentDetailsActivity.this, "保存失败", Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                        super.onLoadFailed(e, errorDrawable);
                                                        Toast.makeText(ContentDetailsActivity.this, "保存失败", Toast.LENGTH_LONG).show();
                                                    }

                                                });
                                        bottomMenuDialog.dismiss();
                                    }
                                }).create();
                        bottomMenuDialog.show();


                        return true;
                    }
                });
                imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float v, float v1) {
                        popupWindow.dismiss();
                    }
                });
                container.addView(imageView, 0);//添加页卡
                return imageView;
            }

            private void loadBigImage(final PhotoView imageView, final ProgressBar loading, String imgurl) {
                Glide.with(ContentDetailsActivity.this)
                        .load(imgurl)
                        .skipMemoryCache(true)
                        .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                        .error(R.drawable.nodate)
                        .into(new GlideDrawableImageViewTarget(imageView) {
                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                               /* if(smallImageView!=null){
                                    smallImageView.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(imgurl).into(smallImageView);
                                }*/
                                loading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                /*if(smallImageView!=null){
                                    smallImageView.setVisibility(View.GONE);
                                }*/
                                loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                loading.setVisibility(View.GONE);
                                /*if(smallImageView!=null){
                                    smallImageView.setVisibility(View.GONE);
                                }*/
                            }
                        });
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((ImageView) object);//删除页卡
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view.equals(object);
            }
        });
        viewPager.setCurrentItem(arg0);
        prePosition = arg0;
        linearLayout.getChildAt(prePosition).setBackgroundResource(R.drawable.yellow_icon_shape);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                linearLayout.getChildAt(position).setBackgroundResource(R.drawable.yellow_icon_shape);
                linearLayout.getChildAt(prePosition).setBackgroundResource(R.drawable.black_icon_shape);
                prePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        popupWindow = new PopupWindow(view, d.getWidth(), d.getHeight(), true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.showAtLocation(new View(ContentDetailsActivity.this), Gravity.CENTER, 0, 0);
    }

    //参数：UserID, ContentID， flag 后两个值初始为0，上拉时传入当前页面第一条内容ContentID，flag=7；下拉时传入当前页面最后一条内容的ContentID,flag=8
    private void getComment(String flag) {



        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetShareCommentRequest");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(this, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(this, "UID", "0"));
        params.addBodyParameter("ContentID",  circle.getContentID());

        params.addBodyParameter("flag", flag);
        HttpUtils.getInstance(this).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {

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
        params.addBodyParameter("ResultJWT",ShareUitls.getString(this, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(this, "UID", "0"));
        params.addBodyParameter("ContentID",  circle.getContentID());

        params.addBodyParameter("CommentText", commentContentt);
        HttpUtils.getInstance(this).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {
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
                                Toast.makeText(ContentDetailsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                Log.i("AAAAAAAAAXIZNHENG新增  ", CircleList.getInstance().circlelist.get(position).getCommentCount() + "");
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

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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
        requestnetworking=true;
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostShareAttitudeRequest");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(this, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(this, "UID", "0"));
        params.addBodyParameter("ContentID",  circle.getContentID());
        params.addBodyParameter("CommentID", "0");
        params.addBodyParameter("ReplyID", "0");
        params.addBodyParameter("Flag", Flag);
        HttpUtils.getInstance(this).sendRequestRequestParams("", params,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        requestnetworking=false;
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


                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        requestnetworking=false;
                        Toast.makeText(ContentDetailsActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }
}
