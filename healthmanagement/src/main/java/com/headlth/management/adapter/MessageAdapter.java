package com.headlth.management.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.headlth.management.R;
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
public class MessageAdapter extends BaseAdapter {
    private List<Reply> list;
    private Activity activity; //



    public MessageAdapter(List<Reply> list, Activity activity) {
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.listview_item_message, null);
            holder = new CircleHolder();
            x.view().inject(holder,convertView);
            convertView.setTag(holder);
        } else {
            holder = (CircleHolder) convertView.getTag();
        }
        final Reply reply = getItem(position);

        holder.listview_item_message_contentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    public class CircleHolder {
        @ViewInject(R.id.listview_item_message_content)
        public TextView listview_item_message_content;
        @ViewInject(R.id.listview_item_message_time)
        public TextView listview_item_message_time;
        @ViewInject(R.id.listview_item_message_icon)
        public ImageView listview_item_message_icon;
        @ViewInject(R.id.listview_contentdetails_attitude_count)
        public TextView listview_contentdetails_attitude_count;
        @ViewInject(R.id.listview_item_message_man)
        public TextView listview_item_message_man;



        @ViewInject(R.id.listview_item_message_data)
        public TextView listview_item_message_data;



        @ViewInject(R.id.listview_item_message_datalayout)
        public LinearLayout listview_item_message_datalayout;
        @ViewInject(R.id.listview_item_message_contentlayout)
        public LinearLayout listview_item_message_contentlayout;


    }
}