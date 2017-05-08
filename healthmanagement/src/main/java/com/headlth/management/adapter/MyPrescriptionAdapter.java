package com.headlth.management.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.entity.MyPrescriptionJson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by abc on 2016/8/15.
 */
public class MyPrescriptionAdapter extends BaseAdapter {
    private List<MyPrescriptionJson.MyPrescription> MyPrescriptionList;
    private LayoutInflater layoutInflater;

    public MyPrescriptionAdapter(Activity activity, List<MyPrescriptionJson.MyPrescription> MyPrescriptionList) {
        layoutInflater = LayoutInflater.from(activity);
        this.MyPrescriptionList = MyPrescriptionList;
    }

    @Override
    public int getCount() {
        return MyPrescriptionList.size();
    }

    @Override
    public MyPrescriptionJson.MyPrescription getItem(int position) {
        return MyPrescriptionList.get(position);


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.view_myprescription, null);
            holder = new Holder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final MyPrescriptionJson.MyPrescription prescription = getItem(position);
        holder.view_myprescription_name.setText(prescription.Title);
        try {//预防可能不返这个参数
            holder.view_myprescription_starttime.setText(prescription.Prescription_Start.replace("T", " "));
            holder.view_myprescription_stoptime.setText(prescription.Prescription_End.replace("T", " "));
        } catch (NullPointerException n) {
        }
        switch (prescription.PrescriptionState) {
            case "3":
                holder.view_myprescription_flag.setImageResource(R.mipmap.icon_begin);
                break;
            case "1":
                holder.view_myprescription_flag.setImageResource(R.mipmap.icon_no_begin);
                break;
            case "2":
                holder.view_myprescription_flag.setImageResource(R.mipmap.icon_over);
                break;
        }

        return convertView;
    }

    public class Holder {
        @ViewInject(R.id.view_myprescription_name)
        public TextView view_myprescription_name;
        @ViewInject(R.id.view_myprescription_starttime)
        public TextView view_myprescription_starttime;
        @ViewInject(R.id.view_myprescription_stoptime)
        public TextView view_myprescription_stoptime;
        @ViewInject(R.id.view_myprescription_flag)
        public ImageView view_myprescription_flag;


    }
}