package com.headlth.management.chufang;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.entity.tiJianCallBack;

import java.util.List;

public class yichangItemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    List<tiJianCallBack.AbnormalListEntity> targets;
    public yichangItemAdapter(Context context, List<tiJianCallBack.AbnormalListEntity> targets) {
        this.targets=targets;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
         for(int i=0;i<targets.size();i++){
            Log.e("json", "调过来了的对象" + targets.get(i));
        }
    }
    @Override
    public int getCount() {
        return targets.size();
    }

    @Override
    public tiJianCallBack.AbnormalListEntity getItem(int position) {
        return targets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.targets_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageButton = (ImageButton) convertView.findViewById(R.id.imageButton);
            viewHolder.textView3 = (TextView) convertView.findViewById(R.id.content);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(viewHolder);
        }
        initializeViews((tiJianCallBack.AbnormalListEntity)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(tiJianCallBack.AbnormalListEntity object, ViewHolder holder) {
        holder.textView3.setText(object.getTitle());
        holder.content.setText(object.getContent());
        /* holder.imageButton.setImageResource(R.drawable.dot);*/
    }

    protected class ViewHolder {
        private ImageButton imageButton;
        private TextView textView3;
         private TextView content;
    }
}
