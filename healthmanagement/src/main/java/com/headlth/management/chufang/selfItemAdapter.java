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

public class selfItemAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    List<tiJianCallBack.UserInfoEntity> PList;
    public selfItemAdapter(Context context, List<tiJianCallBack.UserInfoEntity> PList) {
        this.PList=PList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
         for(int i=0;i<PList.size();i++){
            Log.e("json", "调过来了的对象" + PList.get(i));
        }
    }
    @Override
    public int getCount() {
        return PList.size();
    }

    @Override
    public tiJianCallBack.UserInfoEntity getItem(int position) {
        return PList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.yichang_item, null);
             viewHolder = new ViewHolder();
            viewHolder.imageButton = (ImageButton) convertView.findViewById(R.id.imageButton);
            viewHolder.textView3 = (TextView) convertView.findViewById(R.id.content);

            convertView.setTag(viewHolder);
        }
        initializeViews((tiJianCallBack.UserInfoEntity)getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(tiJianCallBack.UserInfoEntity object, ViewHolder holder) {
        holder.textView3.setText(object.getTitle()+" : "+object.getContent());
    /*    holder.imageButton.setImageResource(R.drawable.dot);*/
    }

    protected class ViewHolder {
        private ImageButton imageButton;
       private TextView textView3;
    }
}
