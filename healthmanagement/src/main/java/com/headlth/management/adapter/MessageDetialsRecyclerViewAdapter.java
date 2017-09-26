package com.headlth.management.adapter;


import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.entity.MessageDetials;
import com.headlth.management.entity.MessageList;
import com.headlth.management.utils.DataString;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Created by abc on 2017/4/28.
 */
public class MessageDetialsRecyclerViewAdapter extends RecyclerView.Adapter<MyDetialsMessageViewHolder> {


    private List<MessageDetials.Medication> pushMedicInfoLists;
    // private Activity activity;
    private List<Integer> MedicationID;
    private List<String> MedicationTime;

    public MessageDetialsRecyclerViewAdapter(List<MessageDetials.Medication> pushMedicInfoLists/*, Activity activity*/) {
        this.pushMedicInfoLists = pushMedicInfoLists;
        //  this.activity = activity;

        MedicationID = new ArrayList<>();
        MedicationTime = new ArrayList<>();
        Log.i("messageListlist", pushMedicInfoLists.size() + "   "+pushMedicInfoLists.toString());
    }

    @Override
    public MyDetialsMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_messagedetials, parent, false);
        return new MyDetialsMessageViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MyDetialsMessageViewHolder holder, final int position) {
        final MessageDetials.Medication medication = pushMedicInfoLists.get(position);
        //判断是否设置了监听器

      /*  //为ItemView设置监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("mOnItemClickListener", "mOnItemClickListener");
                Message message = Message.obtain();
                message.arg1 = position;
                message.arg2 = 0;
                myhandler.sendMessage(message);
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

                return true;
            }
        });*/

        if (!MedicationTime.contains(medication.MedicationTimeName)) {
            MedicationTime.add(medication.MedicationTimeName);
            medication.isfirst = true;
            // MedicationID.add(medication.ID);
        }
        if (!medication.isfirst) {
            holder.recyclerview_item_messagedetials_datalayout.setVisibility(View.GONE);
        } else {
            holder.recyclerview_item_messagedetials_datalayout.setVisibility(View.VISIBLE);
            holder.recyclerview_item_messagedetials_ricedata.setText(medication.MedicationTimeName);
        }
        holder.recyclerview_item_messagedetials_medicinename.setText(medication.DrugName);
        holder.recyclerview_item_messagedetials_medicinecount.setText(medication.DosageSize + "" + medication.MinDoseUnit);

    }

    @Override
    public int getItemCount() {
        return pushMedicInfoLists.size();
    }

}

class MyDetialsMessageViewHolder extends RecyclerView.ViewHolder {

    @ViewInject(R.id.recyclerview_item_messagedetials_datalayout)
    public LinearLayout recyclerview_item_messagedetials_datalayout;
    @ViewInject(R.id.recyclerview_item_messagedetials_ricedata)
    public TextView recyclerview_item_messagedetials_ricedata;


    @ViewInject(R.id.recyclerview_item_message_contentlayout)
    public RelativeLayout recyclerview_item_message_contentlayout;


    @ViewInject(R.id.recyclerview_item_messagedetials_medicinename)
    public TextView recyclerview_item_messagedetials_medicinename;

    @ViewInject(R.id.recyclerview_item_messagedetials_medicinecount)
    public TextView recyclerview_item_messagedetials_medicinecount;


    public MyDetialsMessageViewHolder(View itemView) {
        super(itemView);
        x.view().inject(this, itemView);
    }


}
