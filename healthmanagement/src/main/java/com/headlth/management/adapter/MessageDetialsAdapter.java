package com.headlth.management.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.entity.Reply;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by abc on 2016/8/15.
 */
public class MessageDetialsAdapter extends BaseAdapter {
    private List<Reply> list;
    private Activity activity; //



    public MessageDetialsAdapter(List<Reply> list, Activity activity) {
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
            convertView = LayoutInflater.from(activity).inflate(R.layout.listview_item_messagedetials, null);
            holder = new CircleHolder();
            x.view().inject(holder,convertView);
            convertView.setTag(holder);
        } else {
            holder = (CircleHolder) convertView.getTag();
        }
        final Reply reply = getItem(position);



        return convertView;
    }

    public class CircleHolder {
        @ViewInject(R.id.listview_item_messagedetials_ricedata)
        public TextView listview_item_messagedetials_ricedata;
        @ViewInject(R.id.listview_item_messagedetials_medicinename)
        public TextView listview_item_messagedetials_medicinename;
        @ViewInject(R.id.listview_item_messagedetials_medicinecount)
        public TextView listview_item_messagedetials_medicinecount;


        @ViewInject(R.id.listview_item_messagedetials_datalayout)
        public LinearLayout listview_item_messagedetials_datalayout;


    }
}