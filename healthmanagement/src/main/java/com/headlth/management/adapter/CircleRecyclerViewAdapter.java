package com.headlth.management.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.headlth.management.R;
import com.headlth.management.activity.LookBigImageActivity;
import com.headlth.management.activity.OtherActivity;
import com.headlth.management.entity.Circle;
import com.headlth.management.entity.CircleList;
import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.myview.MGridView;
import com.headlth.management.myview.MyRecyclerViewGridView;
import com.headlth.management.myview.MyToash;
import com.headlth.management.myview.RoundImageView;
import com.headlth.management.myview.SpaceItemDecoration;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DataString;
import com.headlth.management.utils.GetWindowSize;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ImageUtil;
import com.headlth.management.utils.ScreenShot;
import com.headlth.management.utils.ShareUitls;
import com.squareup.picasso.Picasso;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by abc on 2017/4/28.
 */
public class CircleRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }


    private List<Circle> list;
    private Activity activity;
    //  private Display d; // 获取屏幕宽、高用
    private boolean requestnetworking;
    private Handler myhandler;
    boolean flag;//判断是那个界面的    false是迈动圈  true 我的分享

    public CircleRecyclerViewAdapter(List<Circle> list, Activity activity, Handler handler, boolean flag) {
        this.list = list;
        this.flag = flag;
        this.activity = activity;
        //   d = activity.getWindowManager().getDefaultDisplay();000
        myhandler = handler;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_maidongcircle, parent, false);


        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Circle circle = list.get(position);
        //判断是否设置了监听器

        //为ItemView设置监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("mOnItemClickListener", "mOnItemClickListener");
                Message message = Message.obtain();
                message.arg1 = position;
                message.arg2 = 0;
                myhandler.sendMessage(message);
            /*    int position = holder.getLayoutPosition(); // 1
                mOnItemClickListener.onItemClick(holder.itemView, position); // 2*/
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("mOnItemClickListener", "mOnItemLongClickListener");
                int position = holder.getLayoutPosition();
                Message message = Message.obtain();
                message.arg1 = position;
                message.arg2 = 1;
                myhandler.sendMessage(message);
              /*  mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                //返回true 表示消耗了事件 事件不会继续传递*/
                return true;
            }
        });


        Picasso.with(activity)
                .load(Constant.BASE_URL + "/" + circle.getAvatarUrl())//图片网址
                .placeholder(R.mipmap.hand)//默认图标
                .into(holder.listview_maidongcircle_icon);//控件

        if (circle.getUsername().length() >= 25) {
            holder.listview_maidongcircle_user.setText(circle.getUsername().substring(0, 25) + "...");
        } else {
            holder.listview_maidongcircle_user.setText(circle.getUsername());

        }
        int length = circle.getContentText().length();//replace(" ","").length()==0 length==1&&
        if (length == 0||(circle.getContentText().equals(" "))) {//服务器返回的数据 如果没有文字内容 默认是空格
            MyToash.Log("length0="+length);
            holder.listview_maidongcircle_text.setVisibility(View.GONE);
            holder.listview_maidongcircle_text_all.setVisibility(View.GONE);
        } else {
            MyToash.Log("length!0="+length);
            holder.listview_maidongcircle_text.setVisibility(View.VISIBLE);
            if (length >= 150) {
                holder.listview_maidongcircle_text_all.setVisibility(View.VISIBLE);
                holder.listview_maidongcircle_text.setText(circle.getContentText().substring(0, 150));
            } else {
                holder.listview_maidongcircle_text.setText(circle.getContentText());
                holder.listview_maidongcircle_text_all.setVisibility(View.GONE);
            }
        }

        holder.listview_maidongcircle_time.setText(DataString.showTime(circle.getCreateTime()));
        if (circle.getCommentCount() == 0) {
            holder.listview_maidongcircle_comment_count.setText("评论");
            holder.listview_maidongcircle_reply_ImageView.setImageResource(R.mipmap.icon_no_reply);

        } else {
            holder.listview_maidongcircle_comment_count.setText(circle.getCommentCount() + "");
            holder.listview_maidongcircle_reply_ImageView.setImageResource(R.mipmap.icon_reply);
        }
        if (circle.getLikeCount() == 0) {
            holder.listview_maidongcircle_like_count.setText("赞");
        } else {
            holder.listview_maidongcircle_like_count.setText(circle.getLikeCount() + "");
        }

        if (circle.getIsAttitude().equals("1")) {
            holder.listview_maidongcircle_like_ImageView.setImageResource(R.mipmap.icon_zan);

        } else {
            holder.listview_maidongcircle_like_ImageView.setImageResource(R.mipmap.icon_no_zan);
        }
        int size = circle.getImageUrls().size();
        if (circle.getImageUrls() != null && size != 0) {
            ImageShowRecycleAdapter imageShowRecycleAdapter = new ImageShowRecycleAdapter(activity);
            GridLayoutManager gridLayoutManager = null;
            switch (size) {
                case 1:
                    gridLayoutManager = new GridLayoutManager(activity, 1);
                    break;
                case 2:
                case 4:
                    gridLayoutManager = new GridLayoutManager(activity, 2);
                    break;
                default:
                    gridLayoutManager = new GridLayoutManager(activity, 3);
                    break;
            }

            holder.listview_maidongcircle_RecyclerView.setVisibility(View.VISIBLE);
            holder.listview_maidongcircle_RecyclerView.setHasFixedSize(true);
            holder.listview_maidongcircle_RecyclerView.setLayoutManager(gridLayoutManager);
            holder.listview_maidongcircle_RecyclerView.setAdapter(imageShowRecycleAdapter);
            imageShowRecycleAdapter.replaceAll((ArrayList<String>) circle.getImageUrls());
        } else {
            holder.listview_maidongcircle_RecyclerView.setVisibility(View.GONE);

        }
        holder.listview_maidongcircle_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FFFFF", circle.getIsAttitude().equals("0") + "");
                if (circle.getIsAttitude().equals("0")) {

                    if (!requestnetworking) {
                        addLike(position, "0");
                    }


                } else {
                    if (!requestnetworking) {
                        addLike(position, "1");
                    }


                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void addLike(final int position, final String Flag) {
        requestnetworking = true;
        //UserID:用户ID，ContentID:帖子ID，CommentID:评论ID(如果对帖子点赞则该项为0）,ReplyID(默认为0），Flag：0为点赞，1为取消赞
        Circle circle = list.get(position);

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostShareAttitudeRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(activity, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(activity, "UID", "0"));
        params.addBodyParameter("ContentID", circle.getContentID());
        params.addBodyParameter("CommentID", "0");
        params.addBodyParameter("ReplyID", "0");
        params.addBodyParameter("Flag", Flag);
        HttpUtils.getInstance(activity).sendRequestRequestParams("", params, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        requestnetworking = false;
                        // Log.i("onResponse", Flag + "   " + CircleList.getInstance().circlelist.get(position).getUsername() + "  " + position);
                        Circle circle = list.get(position);
                        int likecount = circle.getAttitudeCount();
                        if (Flag.equals("0")) {
                            list.get(position).setIsAttitude("1");
                            list.get(position).setAttitudeCount(likecount + 1);


                        } else {
                            list.get(position).setIsAttitude("0");
                            if (likecount - 1 > 0) {
                                list.get(position).setAttitudeCount(likecount - 1);
                            } else {
                                list.get(position).setAttitudeCount(0);
                            }

                        }
                        notifyItemChanged(position, "notifyItemChanged");
                        // handler.sendEmptyMessage(0);
                        if (flag) {
                            CircleList.getInstance().circlelist.clear();//迈动圈子数据变化 清空数据
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        requestnetworking = false;
                        Toast.makeText(activity, "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            notifyDataSetChanged();

        }
    };


}

class MyViewHolder extends RecyclerView.ViewHolder {

    @ViewInject(R.id.listview_maidongcircle_icon)
    public RoundImageView listview_maidongcircle_icon;
    @ViewInject(R.id.listview_maidongcircle_user)
    public TextView listview_maidongcircle_user;
    @ViewInject(R.id.listview_maidongcircle_text)
    public TextView listview_maidongcircle_text;
    @ViewInject(R.id.listview_maidongcircle_text_all)
    public TextView listview_maidongcircle_text_all;
    //GridLayoutManager
    @ViewInject(R.id.listview_maidongcircle_Scrollgridview)
    public MGridView listview_maidongcircle_Scrollgridview;
    @ViewInject(R.id.listview_maidongcircle_RecyclerView)
    public MyRecyclerViewGridView listview_maidongcircle_RecyclerView;


    @ViewInject(R.id.listview_maidongcircle_like_ImageView)
    public ImageView listview_maidongcircle_like_ImageView;
    @ViewInject(R.id.listview_maidongcircle_reply_ImageView)
    public ImageView listview_maidongcircle_reply_ImageView;


    @ViewInject(R.id.listview_maidongcircle_time)
    public TextView listview_maidongcircle_time;

    @ViewInject(R.id.listview_maidongcircle_comment_count)
    public TextView listview_maidongcircle_comment_count;
    @ViewInject(R.id.listview_maidongcircle_like)
    public RelativeLayout listview_maidongcircle_like;
    @ViewInject(R.id.listview_maidongcircle_like_count)
    public TextView listview_maidongcircle_like_count;

    public MyViewHolder(View itemView) {
        super(itemView);
        x.view().inject(this, itemView);
    }


}

