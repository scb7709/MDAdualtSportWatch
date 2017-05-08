package com.headlth.management.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.entity.HealthDatum;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abc on 2016/11/24.
 */

@ContentView(R.layout.activity_healthdatum)//复用我的处方布局
public class HealthDatumActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;

    @ViewInject(R.id.activity_healthdatum_listview)
    private ListView activity_healthdatum_listview;

    private List<HealthDatum.Abnormal> AbnormalList;
    private HealthDatumAdapter healthDatumAdapter;

    //  * AbnormalList : [{"Title":"异常A","Content":"肥胖，吃得太多，不爱运动"},{"Title":"异常B","Content":"心脏不好，吃得太多，不爱运动"}]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        init();

    }

    private void init() {
        view_publictitle_title.setText("健康资料");
        view_publictitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
    }

    private void getData() {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostRepostRequest");
        params.addBodyParameter("UID",ShareUitls.getString(HealthDatumActivity.this, "UID", "") + "");
        params.addBodyParameter("ResultJWT",ShareUitls.getString(HealthDatumActivity.this, "ResultJWT", "0"));
        HttpUtils.getInstance(HealthDatumActivity.this).sendRequestRequestParams(Constant.DIALOG_MESSAGE_LOADING,params ,true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("ffff", response.toString());
                        HealthDatum healthDatum=new Gson().fromJson(response,HealthDatum.class);
                        AbnormalList=healthDatum.AbnormalList;
                        healthDatumAdapter=new HealthDatumAdapter();
                        activity_healthdatum_listview.setAdapter(healthDatumAdapter);

                     /*   if(healthDatum.AbnormalList.size()==0){


                        }else {

                        }*/


                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {

                        Toast.makeText(HealthDatumActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );

    }


    public class HealthDatumAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return AbnormalList.size();
        }

        @Override
        public HealthDatum.Abnormal getItem(int position) {
            return AbnormalList.get(position);


        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(HealthDatumActivity.this).inflate(R.layout.view_healthdatum_item, null);
            TextView textView = (TextView) convertView.findViewById(R.id.view_healthdatum_item_text);
            textView.setText(getItem(position).Content);
            return convertView;
        }
    }
}
