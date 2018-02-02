package com.headlth.management.adapter;


import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.entity.deviceEntity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


/**
 * Created by abc on 2017/4/28.
 */
public class SearchBlueRecyclerViewAdapter extends RecyclerView.Adapter<SearchBlueViewHolder> {


   private List<deviceEntity> mAppList;
    private Handler myhandler;
    public SearchBlueRecyclerViewAdapter(List<deviceEntity> mAppList, Handler handler) {
        this.mAppList = mAppList;
        myhandler = handler;
    }

    @Override
    public SearchBlueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        return new SearchBlueViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final SearchBlueViewHolder holder, final int position) {
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

        holder.tv_name.setText(mAppList.get(position).getName());
        holder.tv_content.setText(mAppList.get(position).getAddress());
        double Distance = mAppList.get(position).getDistance();
        if (Distance > 10) {
            Distance = 10;
        }
        holder.data_time.setText(Distance+"(米)");
     /*   if (position == 0) {//"离我最近: " +
            holder.data_time.setText(Distance);
        } else {
            holder.data_time.setText(Distance+"");
        }*/

    }

    @Override
    public int getItemCount() {
        return mAppList.size();
    }

}

class SearchBlueViewHolder extends RecyclerView.ViewHolder {

    @ViewInject(R.id.title)
    TextView tv_name;
    @ViewInject(R.id.msg_content)
    TextView tv_content;
    @ViewInject(R.id.data_time)
    TextView data_time;
    public SearchBlueViewHolder(View itemView) {
        super(itemView);
        x.view().inject(this, itemView);
    }


}