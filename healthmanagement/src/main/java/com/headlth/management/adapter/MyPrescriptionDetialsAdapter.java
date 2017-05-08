package com.headlth.management.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.entity.MyPrescriptionDetialsAdvise;
import com.headlth.management.entity.MyPrescriptionDetialsData;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


/**
 * Created by abc on 2016/8/15.
 */
public class MyPrescriptionDetialsAdapter extends BaseAdapter {
    private List<MyPrescriptionDetialsAdvise> MyPrescriptionDetialsAdviseList;
    private List<MyPrescriptionDetialsData> MyPrescriptionDetialsDataList;
    private LayoutInflater layoutInflater;

    public MyPrescriptionDetialsAdapter(Activity activity, List<MyPrescriptionDetialsAdvise> MyPrescriptionDetialsAdviseList, List<MyPrescriptionDetialsData> MyPrescriptionDetialsDataList) {
        layoutInflater = LayoutInflater.from(activity);
        this.MyPrescriptionDetialsAdviseList = MyPrescriptionDetialsAdviseList;
        this.MyPrescriptionDetialsDataList = MyPrescriptionDetialsDataList;
    }

    @Override
    public int getCount() {
        return MyPrescriptionDetialsAdviseList.size()+MyPrescriptionDetialsDataList.size();
    }

    @Override
    public Object getItem(int position) {
        if(position<MyPrescriptionDetialsAdviseList.size()){
            return MyPrescriptionDetialsAdviseList.get(position);
        }else {
            return MyPrescriptionDetialsDataList.get(position-MyPrescriptionDetialsAdviseList.size());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.view_myprescriptiondetials, null);
            holder = new Holder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (position < MyPrescriptionDetialsAdviseList.size()) {
            holder.  view_mypresriptiondetials_data_layout.setVisibility(View.GONE);
            holder.  view_mypresriptiondetials_advise_layout.setVisibility(View.VISIBLE);
            final MyPrescriptionDetialsAdvise myPrescriptionDetialsAdvise = (MyPrescriptionDetialsAdvise)getItem(position);
            holder.view_mypresriptiondetials_advise_title.setText(myPrescriptionDetialsAdvise.title);
            holder.view_mypresriptiondetials_advise_type.setText(myPrescriptionDetialsAdvise.Type);
            holder.view_mypresriptiondetials_advise_explain.setText(myPrescriptionDetialsAdvise.Advise);

        }else {
            holder.  view_mypresriptiondetials_data_layout.setVisibility(View.VISIBLE);
            holder.  view_mypresriptiondetials_advise_layout.setVisibility(View.GONE);
            final MyPrescriptionDetialsData myPrescriptionDetialsData = (MyPrescriptionDetialsData)getItem(position);
            holder.view_myprescriptiondetials_data_title.setText(myPrescriptionDetialsData.title);
            holder.view_myprescriptiondetials_data_data.setText(myPrescriptionDetialsData.Data);
        }


        return convertView;
    }

    public class Holder {
        @ViewInject(R.id.view_mypresriptiondetials_advise_title)
        public TextView view_mypresriptiondetials_advise_title;
        @ViewInject(R.id.view_mypresriptiondetials_advise_type)
        public TextView view_mypresriptiondetials_advise_type;
        @ViewInject(R.id.view_mypresriptiondetials_advise_explain)
        public TextView view_mypresriptiondetials_advise_explain;

        @ViewInject(R.id.view_mypresriptiondetials_advise_layout)
        public LinearLayout view_mypresriptiondetials_advise_layout;
        @ViewInject(R.id.view_mypresriptiondetials_data_layout)
        public LinearLayout view_mypresriptiondetials_data_layout;

        @ViewInject(R.id.view_myprescriptiondetials_data_title)
        public TextView view_myprescriptiondetials_data_title;
        @ViewInject(R.id.view_myprescriptiondetials_data_data)
        public TextView view_myprescriptiondetials_data_data;


    }
}