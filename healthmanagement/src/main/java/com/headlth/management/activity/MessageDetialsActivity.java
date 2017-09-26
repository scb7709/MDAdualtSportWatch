package com.headlth.management.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.adapter.MessageDetialsRecyclerViewAdapter;
import com.headlth.management.entity.MessageDetials;
import com.headlth.management.myview.CarouselView;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;
import com.squareup.picasso.Picasso;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by abc on 2016/11/4.
 */
@ContentView(R.layout.activity_messagedetials)
public class MessageDetialsActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;

    @ViewInject(R.id.activity_messagedetials_icons)
    private CarouselView carouselView;
    @ViewInject(R.id.activity_messagedetials_icon)
    private ImageView imageView;


    //  @ViewInject(R.id.activity_messagedetials_SwipeRefreshLayout)
    // private SwipeRefreshLayout activity_messagedetials_SwipeRefreshLayout;


   /* @ViewInject(R.id.activity_messagedetials_ricedata)
    private TextView activity_messagedetials_ricedata;*/

    @ViewInject(R.id.activity_messagedetials_recyclerView)
    private RecyclerView activity_messagedetials_recyclerView;
    private String MedictimeslotId = "";
    private String MsgtypeId = "";
    private String CreateTime = "";
    private MessageDetialsRecyclerViewAdapter messageDetialsRecyclerViewAdapter;

    private String[] PushPictureInfoList;
    private CarouselView.Adapter carouselView_Adapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (PushPictureInfoList.length == 1) {
                Picasso.with(MessageDetialsActivity.this)
                        .load(Constant.BASE_URL + "/" + PushPictureInfoList[0])
                        .config(Bitmap.Config.RGB_565)//图片网址
                        .error(R.mipmap.home)
                        .placeholder(R.mipmap.home)//默认图标
                        .into(imageView);//控件
            } else {
                carouselView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                carouselView_Adapter = new CarouselView.Adapter() {
                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public View getView(int position) {
                        View view = LayoutInflater.from(MessageDetialsActivity.this).inflate(R.layout.messagedetials_banner_item, null);
                        ImageView imageView = (ImageView) view.findViewById(R.id.image);
                        Picasso.with(MessageDetialsActivity.this)
                                .load(Constant.BASE_URL + "/" + PushPictureInfoList[position])
                                .config(Bitmap.Config.RGB_565)//图片网址
                                .error(R.mipmap.home)
                                .placeholder(R.mipmap.home)//默认图标
                                .into(imageView);//控件
                        return view;
                    }

                    @Override
                    public int getCount() {
                        return PushPictureInfoList.length;
                    }
                };
                carouselView.setAdapter(carouselView_Adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initialize();
    }

    @Event(value = {R.id.view_publictitle_back})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                String MainActivity = ShareUitls.getString(this, "MainActivity", "");//用来记录当前界面是否存在
                String UID = ShareUitls.getString(this, "UID", "");//用来记录当前界面是否存在
                if (MainActivity.length() == 0&&UID.length()!=0) {//得初始化HomeActicvity界面的数据
                    ShareUitls.putString(MessageDetialsActivity.this, "questionnaire", "1");//首页界面推荐内容重新刷新 (打完题)
                    ShareUitls.putString(MessageDetialsActivity.this, "maidong", "1");//首页界面重新刷新 (新数据)
                    ShareUitls.putString(MessageDetialsActivity.this, "analize", "1");//分析重新刷新
                    ShareUitls.putString(MessageDetialsActivity.this, "todaydata", "{}");//
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    ShareUitls.putString(MessageDetialsActivity.this, "CLICKDADE", format.format(new Date()));//把日历 点击 默认今天
                    ShareUitls.putString(getApplicationContext(), "TODAY", format.format(new Date()));//保存启动APP的时间 确保每天都重新登录过
                    Log.i("MainActivity.Activity", "MainActivity.Activity");
                    HomeActivity.login(MessageDetialsActivity.this,"HomeActivity");
                   // startActivity(new Intent(MessageDetialsActivity.this, MainActivity.class));
                }else if(UID.length()==0){
                    Intent intent = new Intent(MessageDetialsActivity.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
                finish();
                break;
        }
    }

    private void initialize() {
        registServiceToActivityReceiver();
        Intent intent = getIntent();
        // carouselView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CarouselView.dip2px(MessageDetialsActivity.this, 200)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageDetialsActivity.this);
        activity_messagedetials_recyclerView.setLayoutManager(linearLayoutManager);
        MedictimeslotId = intent.getStringExtra("MedictimeslotId");
        MsgtypeId = intent.getStringExtra("MsgtypeId");
        CreateTime = intent.getStringExtra("CreateTime");
        view_publictitle_title.setText("温馨提醒");
        getMessage();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            String MainActivity = ShareUitls.getString(this, "MainActivity", "");//用来记录当前界面是否存在
            String UID = ShareUitls.getString(this, "UID", "");//用来记录当前界面是否存在
            if (MainActivity.length() == 0&&UID.length()!=0) {
                Log.i("MainActivity.Activity", "MainActivity.Activity");
                ShareUitls.putString(MessageDetialsActivity.this, "questionnaire", "1");//首页界面推荐内容重新刷新 (打完题)
                ShareUitls.putString(MessageDetialsActivity.this, "maidong", "1");//首页界面重新刷新 (新数据)
                ShareUitls.putString(MessageDetialsActivity.this, "analize", "1");//分析重新刷新
                ShareUitls.putString(MessageDetialsActivity.this, "todaydata", "{}");//
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                ShareUitls.putString(MessageDetialsActivity.this, "CLICKDADE", format.format(new Date()));//把日历 点击 默认今天
                ShareUitls.putString(getApplicationContext(), "TODAY", format.format(new Date()));//保存启动APP的时间 确保每天都重新登录过

                HomeActivity.login(MessageDetialsActivity.this,"HomeActivity");
               // startActivity(new Intent(MessageDetialsActivity.this, MainActivity.class));
            }else if(UID.length()==0){
                Intent intent = new Intent(MessageDetialsActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getMessage() {


        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetPushMedicInfoRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(MessageDetialsActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(MessageDetialsActivity.this, "UID", "null"));
        params.addBodyParameter("MedictimeslotId", MedictimeslotId);
        params.addBodyParameter("MsgtypeId", MsgtypeId);
        params.addBodyParameter("CreateTime", CreateTime);
        Log.i("GetPushMedicInfoRequest", ShareUitls.getString(MessageDetialsActivity.this, "ResultJWT", "0") + "  '" + ShareUitls.getString(MessageDetialsActivity.this, "UID", "null") + "  " + MedictimeslotId + "  " + MsgtypeId);


        HttpUtils.getInstance(MessageDetialsActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("GetPushMedicInfoReques", response.toString());
                        MessageDetials messageDetials = new Gson().fromJson(response.toString(), MessageDetials.class);
                        if (messageDetials.Status.equals("1")) {
                            PushPictureInfoList = messageDetials.PushPictureInfoList;
                            if (PushPictureInfoList != null && PushPictureInfoList.length != 0) {
                                handler.sendEmptyMessage(0);
                            }
                            if (messageDetials.PushMedicInfoList != null && messageDetials.PushMedicInfoList.size() != 0) {
                               // activity_messagedetials_ricedata.setText(messageDetials.PushMedicInfoList.get(0).MedicationTimeName);

                               // DetialsMessageComparator  detialsMessageComparator = new DetialsMessageComparator();
                              //  Collections.sort(messageDetials.PushMedicInfoList, detialsMessageComparator);
                                messageDetialsRecyclerViewAdapter = new MessageDetialsRecyclerViewAdapter(messageDetials.PushMedicInfoList);
                                activity_messagedetials_recyclerView.setAdapter(messageDetialsRecyclerViewAdapter);
                            }

                        } else {
                            Toast.makeText(MessageDetialsActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA", "LoginupToken");

                        // Toast.makeText(MessageDetialsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                    }
                }

        );

    }
    private void registServiceToActivityReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("MessageDetialsActivityReceiver");
        registerReceiver(MessageDetialsActivityReceiver, filter);
    }

    private BroadcastReceiver MessageDetialsActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MedictimeslotId = intent.getStringExtra("MedictimeslotId");
            MsgtypeId = intent.getStringExtra("MsgtypeId");
            CreateTime = intent.getStringExtra("CreateTime");

            getMessage();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        ShareUitls.putString(this, "MessageDetialsActivity", "yes");//用来MessageActivity界面是否存在
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareUitls.putString(this, "MessageDetialsActivity", "");//用来MessageActivity界面是否存在
        unregisterReceiver(MessageDetialsActivityReceiver);
    }

    class DetialsMessageComparator implements Comparator<MessageDetials.Medication> {


        @Override
        public int compare(MessageDetials.Medication medication, MessageDetials.Medication t1) {
            String MedicationTimeName = medication.MedicationTimeName;
            String MedicationTimeName1 = t1.MedicationTimeName;

            if(MedicationTimeName.contains("前")){
                return -1;
            }else if(MedicationTimeName.contains("后")){
                return 1;
            }
            return MedicationTimeName.compareTo(MedicationTimeName1);
        }
    }
}
