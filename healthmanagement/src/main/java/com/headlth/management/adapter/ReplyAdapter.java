package com.headlth.management.adapter;

import android.app.Activity;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.entity.Comment;
import com.headlth.management.entity.Reply;
import com.headlth.management.myview.RoundImageView;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DataString;

import com.squareup.picasso.Picasso;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by abc on 2016/8/15.
 */
public class ReplyAdapter extends BaseAdapter {
    private List<Reply> list;
    private Activity activity; //
    private PopupWindow popupWindow;


    public ReplyAdapter(List<Reply> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Reply getItem(int position) {
        return list.get(position);
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
            x.view().inject(holder,convertView);
            convertView.setTag(holder);
        } else {
            holder = (CircleHolder) convertView.getTag();
        }
        final Reply reply = getItem(position);
        Picasso.with(activity)
                .load(Constant.BASE_URL+"/"+reply.getUserImageUrl())//图片网址
                .placeholder(R.mipmap.hand)//默认图标
                .into(holder.listview_contentdetails_icon);//控件
        holder.listview_contentdetails_user.setText(reply.getUserRealname());
        holder.listview_contentdetails_text.setText(reply.getReplyText());
        holder.listview_contentdetails_time.setText(DataString.showTime(reply.getCreateTime()));

        holder.listview_contentdetails_count.setVisibility(View.GONE);
        holder.listview_maidongcircle_view.setVisibility(View.GONE);

       /* if (reply.getAttitudeCount() == 0) {
            holder.listview_contentdetails_attitude_count.setText("赞");
        } else {
            holder.listview_contentdetails_attitude_count.setText(reply.getAttitudeCount() + "");
        }

        if (reply.getReplyCount() == 0) {
            holder.listview_contentdetails_reply_count.setText("回复");
        } else {
            holder.listview_contentdetails_reply_count.setText(reply.getReplyCount() + "");
        }*/
        holder.listview_contentdetails_attitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reply.getIsAttitude().equals("0")) {

                } else {

                }
            }
        });
        holder.listview_contentdetails_attitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.listview_contentdetails_attitude_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        @ViewInject(R.id.listview_maidongcircle_view)
        public View listview_maidongcircle_view;
        @ViewInject(R.id.listview_contentdetails_reply_count)
        public TextView listview_contentdetails_reply_count;



        @ViewInject(R.id.listview_contentdetails_all)
        public LinearLayout listview_contentdetails_count;

    }
}