package com.headlth.management.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.entity.Prescription;
import com.headlth.management.entity.PrescriptionJson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


/**
 * Created by abc on 2016/8/15.
 */
public class PrescriptionAdapter extends BaseAdapter {
    List<PrescriptionJson.PrescriptionClass>   list;
    private LayoutInflater layoutInflater;
    public PrescriptionAdapter(Activity activity,List<PrescriptionJson.PrescriptionClass>  list) {
        layoutInflater = LayoutInflater.from(activity);
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public PrescriptionJson.PrescriptionClass getItem(int position) {
        return list.get(position);


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.view_maidong_recommend_exercise_plan, null);
            holder = new Holder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

       final PrescriptionJson.PrescriptionClass prescription = getItem(position);
         holder.view_fragment_maidong_recommend_exercise_plan_type.setText(prescription.PlanName);
        holder.view_fragment_maidong_recommend_exercise_plan_goal.setText(prescription.SportTarget);
        if(prescription.IsMoney.equals("1")){
            holder.view_fragment_maidong_recommend_exercise_plan_charge.setImageResource(R.mipmap.button_pay);
        }else {
            holder.view_fragment_maidong_recommend_exercise_plan_charge.setImageResource(R.mipmap.button_free);
        }
        return convertView;
    }

    public class Holder {
        @ViewInject(R.id.view_fragment_maidong_recommend_exercise_plan_type)
        public TextView view_fragment_maidong_recommend_exercise_plan_type;
        @ViewInject(R.id.view_fragment_maidong_recommend_exercise_plan_goal)
        public TextView view_fragment_maidong_recommend_exercise_plan_goal;
        @ViewInject(R.id.view_fragment_maidong_recommend_exercise_plan_charge)
        public ImageView view_fragment_maidong_recommend_exercise_plan_charge;



    }

    public static String getABC(String str) {
        String temp = "";
        switch (str) {
            case "1":
                temp = "A";
                break;
            case "2":
                temp = "B";
                break;
            case "3":
                temp = "C";
                break;
            case "4":
                temp = "D";
                break;
            case "5":
                temp = "E";
                break;
            case "6":
                temp = "F";
                break;
            case "7":
                temp = "G";
                break;
        }
        return temp;
    }
}