package com.headlth.management.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.headlth.management.R;
import com.headlth.management.entity.CircleList;
import com.headlth.management.entity.Comment;
import com.headlth.management.myview.RoundImageView;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DataString;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.HttpUtils;

import com.squareup.picasso.Picasso;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by abc on 2016/8/15.
 */
public class CommentAdapter extends BaseAdapter {

    private Activity activity; //
    private PopupWindow popupWindow;
    private String UserID = "0";
    private boolean requestnetworking;
    public CommentAdapter(Activity activity) {
        UserID = ShareUitls.getString(activity, "UID", "0");
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return CircleList.getInstance().commentlist.size();
    }

    @Override
    public Comment getItem(int position) {
        return CircleList.getInstance().commentlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CircleHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.listview_comment, null);
            holder = new CircleHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (CircleHolder) convertView.getTag();
        }
        final Comment comment = getItem(position);
        Picasso.with(activity)
                .load(Constant.BASE_URL+"/"+comment.getUserImageUrl())//图片网址
                .placeholder(R.mipmap.hand)//默认图标
                .into(holder.listview_contentdetails_icon);//控件
        if (comment.getUserRealname().length() >= 25) {
            holder.listview_contentdetails_user.setText(comment.getUserRealname().substring(0, 25) + "...");
        } else {
            holder.listview_contentdetails_user.setText(comment.getUserRealname());

        }

        holder.listview_contentdetails_text.setText(comment.getCommentText());
        holder.listview_contentdetails_time.setText(DataString.showTime(comment.getCreateTime()));
        holder.listview_contentdetails_all.setVisibility(View.GONE);
        if (comment.getIsAttitude().equals("1")) {
            holder.listview_contentdetails_attitude.setImageResource(R.mipmap.icon_zan);
        } else {
            holder.listview_contentdetails_attitude.setImageResource(R.mipmap.icon_no_zan);
        }
        if (comment.getAttitudeCount() == 0) {
            holder.listview_contentdetails_attitude_count.setText("赞");
        } else {
            holder.listview_contentdetails_attitude_count.setText(comment.getAttitudeCount() + "");
        }
        if (comment.getReplyCount() == 0) {
            holder.listview_contentdetails_reply_count.setText("回复");

            holder.listview_contentdetails_reply.setImageResource(R.mipmap.icon_no_reply);
        } else {
            holder.listview_contentdetails_reply_count.setText(comment.getReplyCount() + "");
            holder.listview_contentdetails_reply.setImageResource(R.mipmap.icon_reply);
        }
        holder.listview_maidongcircle_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CircleList.getInstance().commentlist.get(position).getIsAttitude().equals("0")) {
                    if(!requestnetworking){
                        addLike(position, "0");
                    }



                } else {
                    if(!requestnetworking){
                        addLike(position, "1");
                    }



                }
            }
        });

        return convertView;
    }

    public class CircleHolder {
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
        @ViewInject(R.id.listview_contentdetails_reply_count)
        public TextView listview_contentdetails_reply_count;
        @ViewInject(R.id.listview_contentdetails_reply)
        public ImageView listview_contentdetails_reply;



        @ViewInject(R.id.listview_maidongcircle_like)
        public RelativeLayout listview_maidongcircle_like;
        @ViewInject(R.id.listview_contentdetails_all)
        public LinearLayout listview_contentdetails_all;

    }

    private void addLike(final int position, final String Flag) {
        requestnetworking=true;
        //UserID:用户ID，ContentID:帖子ID，CommentID:评论ID(如果对帖子点赞则该项为0）,ReplyID(默认为0），Flag：0为点赞，1为取消赞
        Comment comment = CircleList.getInstance().commentlist.get(position);

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostShareAttitudeRequest");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(activity, "ResultJWT", "0"));
        params.addBodyParameter("UID",ShareUitls.getString(activity, "UID", "0"));
        params.addBodyParameter("ContentID", comment.getContentID());
        params.addBodyParameter("CommentID", comment.getCommentID());
        params.addBodyParameter("ReplyID", "0");
        params.addBodyParameter("Flag", Flag);
        HttpUtils.getInstance(activity).sendRequestRequestParams("", params,false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        requestnetworking=false;
                        Log.i("AAAAAAAAA", "Log" + response);
                        Comment comment = CircleList.getInstance().commentlist.get(position);
                        int likecount = comment.getAttitudeCount();
                        if (Flag.equals("0")) {
                            CircleList.getInstance().commentlist.get(position).setIsAttitude("1");
                            CircleList.getInstance().commentlist.get(position).setAttitudeCount(likecount + 1);
                        } else {
                            CircleList.getInstance().commentlist.get(position).setIsAttitude("0");
                            if(likecount - 1>0){
                                CircleList.getInstance().commentlist.get(position).setAttitudeCount(likecount - 1);
                            }else {
                                CircleList.getInstance().commentlist.get(position).setAttitudeCount(0);
                            }

                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onErrorResponse(Throwable error) {
                        requestnetworking=false;
                        Toast.makeText(activity, "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }
}