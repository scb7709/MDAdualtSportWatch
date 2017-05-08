package com.headlth.management.adapter;


import android.app.Activity;

import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;

import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.headlth.management.R;

import com.headlth.management.entity.LivingHabit;
import com.headlth.management.entity.LivingHabitJson;
import com.headlth.management.utils.ImageUtil;


import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


/**
 * Created by abc on 2016/8/15.
 */
public class LivingHabitAdapter extends BaseAdapter {


    public  List<LivingHabitJson.ListDetail.ListItems> DiseaseList;
    public  List<LivingHabitJson.ListDetail.ListItems>  BadHabitList;
    private Activity activity;
    private int size;

    public LivingHabitAdapter(List<LivingHabitJson.ListDetail.ListItems> DiseaseList, List<LivingHabitJson.ListDetail.ListItems>  BadHabitList, Activity activity) {
        this.DiseaseList = DiseaseList;
        this.BadHabitList = BadHabitList;
        this.activity = activity;
        size = DiseaseList.size() + BadHabitList.size();
    }

    @Override
    public int getCount() {
        return size + 3;
    }

    @Override
    public LivingHabitJson.ListDetail.ListItems getItem(int position) {
        if (position <= DiseaseList.size() && position >= 1) {
            return DiseaseList.get(position-1);
        } else if (position < size + 2 && position >= DiseaseList.size() + 2) {
            return BadHabitList.get(position-(DiseaseList.size() + 2));
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<LivingHabitJson.ListDetail.ListItems>  getDiseaseListList() {
        return DiseaseList;
    }
    public List<LivingHabitJson.ListDetail.ListItems> getBadHabitListList() {
        return BadHabitList;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.listview_livinghabit_1, null);
            holder = new Holder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if (position <DiseaseList.size() + BadHabitList.size()+2) {
            holder.listview_livinghabit_commit.setVisibility(View.GONE);
            holder.listview_livinghabit_FrameLayout.setVisibility(View.VISIBLE);
            holder.listview_livinghabit_line.setVisibility(View.VISIBLE);
            if (position == 0) {
                holder.listview_livinghabit_layout.setVisibility(View.GONE);
                holder.listview_livinghabit_title.setVisibility(View.VISIBLE);
                holder.listview_livinghabit_title.setText("是否确诊以下疾病");
            } else if (position <= DiseaseList.size() && position >= 1) {
                holder.listview_livinghabit_layout.setVisibility(View.VISIBLE);
                holder.listview_livinghabit_title.setVisibility(View.GONE);
                final LivingHabitJson.ListDetail.ListItems disease = getItem(position);
                holder.listview_livinghabit_appellation.setText(disease.Content);
                if (disease.IsChosen==0) {
                    holder.listview_livinghabit_ischoose.setBackgroundResource(R.mipmap.icon_choice_negative);
                } else {
                    holder.listview_livinghabit_ischoose.setBackgroundResource(R.mipmap.icon_choice_active);
                }
                holder.listview_livinghabit_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (disease.IsChosen==1) {
                            disease.IsChosen = 0;
                        } else {
                            disease.IsChosen = 1;
                        }
                        notifyDataSetChanged();
                    }
                });
            } else if (position == DiseaseList.size() + 1) {

                holder.listview_livinghabit_layout.setVisibility(View.GONE);
                holder.listview_livinghabit_title.setVisibility(View.VISIBLE);
                holder.listview_livinghabit_title.setText("是否有以下不良生活习惯");
            } else {
                holder.listview_livinghabit_layout.setVisibility(View.VISIBLE);
                holder.listview_livinghabit_title.setVisibility(View.GONE);
                final LivingHabitJson.ListDetail.ListItems Habit = getItem(position);
                holder.listview_livinghabit_appellation.setText(Habit.Content);
                if (Habit.IsChosen==0) {
                    holder.listview_livinghabit_ischoose.setBackgroundResource(R.mipmap.icon_choice_negative);
                } else {
                    holder.listview_livinghabit_ischoose.setBackgroundResource(R.mipmap.icon_choice_active);
                }
                holder.listview_livinghabit_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Habit.IsChosen==1) {
                            Habit.IsChosen =0;
                        } else {
                            Habit.IsChosen = 1;
                        }
                        notifyDataSetChanged();
                    }
                });

            }

        } else {
            holder.listview_livinghabit_commit.setVisibility(View.VISIBLE);
            holder.listview_livinghabit_FrameLayout.setVisibility(View.GONE);
            holder.listview_livinghabit_line.setVisibility(View.GONE);

        }
        return convertView;
    }

    public class Holder {
        @ViewInject(R.id.listview_livinghabit_layout)
        public RelativeLayout listview_livinghabit_layout;
        @ViewInject(R.id.listview_livinghabit_FrameLayout)
        public FrameLayout listview_livinghabit_FrameLayout;


        @ViewInject(R.id.listview_livinghabit_appellation)
        public TextView listview_livinghabit_appellation;
        @ViewInject(R.id.listview_livinghabit_title)
        public TextView listview_livinghabit_title;


        @ViewInject(R.id.listview_livinghabit_ischoose)
        public ImageView listview_livinghabit_ischoose;
        @ViewInject(R.id.listview_livinghabit_commit)
        public RelativeLayout listview_livinghabit_commit;
        @ViewInject(R.id.listview_livinghabit_line)
        public View listview_livinghabit_line;


    }
}