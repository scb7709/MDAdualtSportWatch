package com.headlth.management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.headlth.management.MyBlue.MyBuleSearchManager;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.entity.deviceEntity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by abc on 2017/7/24.
 */
@ContentView(R.layout.activity_serachblue)
public class SearchBlueActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;


    @ViewInject(R.id.activity_serachblue_isfail)
    private TextView activity_serachblue_isfail;
    @ViewInject(R.id.activity_serachblue_tishi)
    private TextView activity_serachblue_tishi;


    @ViewInject(R.id.activity_serachblue_btclick)
    private Button activity_serachblue_btclick;
    @ViewInject(R.id.activity_serachblue_img)
    private ImageView activity_serachblue_img;
    @ViewInject(R.id.activity_serachblue_tration)
    private Button activity_serachblue_tration;
    @ViewInject(R.id.activity_serachblue_devicelist)
    private ListView activity_serachblue_devicelist;

    //A和n的值，需要根据实际环境进行检测得出
    private static final double A_Value = 59;
    /**
     * A - 发射端和接收端相隔1米时的信号强度
     */
    private static final double n_Value = 2.0;
    /**
     * n - 环境衰减因子
     */


    private static List<deviceEntity> devices;
    // 数组适配器
    private AppAdapter adapter;


    //开启动画扫描效果
    TranslateAnimation ta1;
    private MyBuleSearchManager myBuleSerachManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initialize();

    }

    private void initialize() {
        view_publictitle_title.setText("搜索心率设备");
        devices = new ArrayList<>();
        adapter = new AppAdapter(devices);
        activity_serachblue_devicelist.setAdapter(adapter);
        activity_serachblue_devicelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String adrs = devices.get(i).getAddress();
                myBuleSerachManager.endSearch();
                myBuleSerachManager = null;
                Intent intent = new Intent();
                intent.putExtra("MAC", adrs);
                intent.setClass(SearchBlueActivity.this, AerobicSportActivity.class);//por开始运动
                startActivity(intent);
                finish();


            }
        });

        startAnimation();
        myBuleSerachManager = MyBuleSearchManager.getInstance(SearchBlueActivity.this, 3000, new MyBuleSearchManager.LeScanCallbackListener() {
            @Override
            public void getBluetoothDeviceList(List<MyBuleSearchManager.BluetoothDeviceEntity> bluetoothDevices) {
                activity_serachblue_tration.setVisibility(View.INVISIBLE);
                activity_serachblue_img.setVisibility(View.INVISIBLE);
                activity_serachblue_btclick.setVisibility(View.VISIBLE);
                activity_serachblue_tration.clearAnimation();

                Log.i("myblue", bluetoothDevices.size() + "");
                if (bluetoothDevices == null) {
                    return;//监听未触发
                } else {


                    if (bluetoothDevices.size() == 0) {
                        activity_serachblue_tishi.setText("请确定心率监测器已正确佩戴并靠近手机");
                        activity_serachblue_isfail.setText("连接失败");
                        activity_serachblue_btclick.setText("重新搜索");

                    } else {
                        for (MyBuleSearchManager.BluetoothDeviceEntity bluetoothDeviceEntity : bluetoothDevices) {
                            //    bluetoothDeviceEntity.device.getBluetoothClass().describeContents().
                            try {
                                if (bluetoothDeviceEntity.device.getAddress() != null && bluetoothDeviceEntity.device.getName() != null) {
                                    deviceEntity entity = new deviceEntity();
                                    entity.setAddress(bluetoothDeviceEntity.device.getAddress());
                                    entity.setName(bluetoothDeviceEntity.device.getName());
                                    entity.setRssi(bluetoothDeviceEntity.rssi);
                                    double bigDecimal = new BigDecimal(Math.pow(10, (Math.abs(bluetoothDeviceEntity.rssi)) - A_Value) / (10 * n_Value)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                    entity.setDistance(bigDecimal);
                                    devices.add(entity);
                                }
                            } catch (Exception e) {
                            }
                        }
                        setListView();
                    }
                }


            }

            @Override
            public void getBluetoothDevice(MyBuleSearchManager.BluetoothDeviceEntity bluetoothDevice) {

            }
        });


    }

    private void setListView() {
        List<deviceEntity> tempdevicess = new ArrayList<>();
        tempdevicess.clear();
        // Log.i("DDDDDDDDDDDA", devices.size() + "   " + devices.toString());
        for (int i = 0; i < devices.size(); i++) {//去空
            deviceEntity deviceEntity = devices.get(i);
            if (deviceEntity != null && deviceEntity.getName() != null) {
                tempdevicess.add(deviceEntity);
            }
        }
        // Log.i("DDDDDDDDDDDb", tempdevicess.size() + "   " + tempdevicess.toString());
        devices.clear();

        for (int i = 0; i < tempdevicess.size(); i++) {//去空
            deviceEntity deviceEntity = tempdevicess.get(i);
            devices.add(deviceEntity);
        }
        tempdevicess.clear();
        // Log.i("DDDDDDDDDDDB", devices.size() + "   " + devices.toString());
        for (com.headlth.management.entity.deviceEntity deviceEntity : devices) {//去重
            if (deviceEntity.getName() != null && !tempdevicess.contains(deviceEntity)) {
                tempdevicess.add(deviceEntity);
            }

        }
        devices.clear();
        for (int i = 0; i < tempdevicess.size(); i++) {//去空
            deviceEntity deviceEntity = tempdevicess.get(i);
            devices.add(deviceEntity);
        }
        tempdevicess.clear();
        if (devices.size() != 0) {
            Collections.sort(devices); // 顺序排列
            adapter = new AppAdapter(devices);
            activity_serachblue_devicelist.setAdapter(adapter);
            activity_serachblue_btclick.setText("重新搜索");
            activity_serachblue_isfail.setText("搜索成功");
            activity_serachblue_tishi.setText("点击心率带即可开始运动");
        } else {
            activity_serachblue_tishi.setText("请确定心率监测器已正确佩戴并靠近手机");
            activity_serachblue_isfail.setText("连接失败");
            activity_serachblue_btclick.setText("重新搜索");
        }

    }

    @Event(value = {R.id.view_publictitle_back
            , R.id.activity_serachblue_btclick


    })
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.activity_serachblue_btclick:
                startAnimation();
                devices.clear();
                adapter.notifyDataSetChanged();
                activity_serachblue_isfail.setText("搜索心率监视器");
                activity_serachblue_tishi.setText("请确定心率监测器已正确佩戴并靠近手机");
                activity_serachblue_tration.setVisibility(View.VISIBLE);
                activity_serachblue_btclick.setVisibility(View.INVISIBLE);
                activity_serachblue_img.setVisibility(View.VISIBLE);
                myBuleSerachManager.StartScan();
                break;
        }
    }

    public Handler dcHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    activity_serachblue_tration.startAnimation(ta1);
                    break;
            }
        }
    };

    public void startAnimation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ta1 = new TranslateAnimation(-200, 200, 0, 0);
                ta1.setDuration(800);
                ta1.setStartTime(0);
                ta1.setRepeatCount(Integer.MAX_VALUE);
                ta1.setRepeatMode(Animation.REVERSE);
                dcHandler.sendEmptyMessage(1);
            }
        }).start();
    }

    class AppAdapter extends BaseAdapter {
        List<deviceEntity> mAppList = null;

        AppAdapter(List<deviceEntity> mAppList) {
            this.mAppList = mAppList;
        }

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public deviceEntity getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.search_list_item, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.tv_name.setText(mAppList.get(position).getName());
            holder.tv_content.setText(mAppList.get(position).getAddress());
            String str = mAppList.get(position).getDistance() + "";
            if (str.length() > 4) {
                str = str.substring(0, 4).replace("E", "0");
            }
            if (position == 0) {
                holder.data_time.setText("离我最近: " + str + "");
            } else {
                holder.data_time.setText(str);
            }

            return convertView;
        }

        class ViewHolder {
            TextView tv_name;
            TextView tv_content;
            TextView data_time;

            public ViewHolder(View view) {
                tv_name = (TextView) view.findViewById(R.id.title);
                tv_content = (TextView) view.findViewById(R.id.msg_content);
                data_time = (TextView) view.findViewById(R.id.data_time);
                view.setTag(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
