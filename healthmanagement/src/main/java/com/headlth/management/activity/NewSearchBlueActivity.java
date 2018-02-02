package com.headlth.management.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.headlth.management.MyBlue.RssiUtil;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.adapter.CircleRecyclerViewAdapter;
import com.headlth.management.adapter.SearchBlueRecyclerViewAdapter;
import com.headlth.management.entity.CircleList;
import com.headlth.management.entity.deviceEntity;
import com.headlth.management.myview.MyToash;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by abc on 2017/7/24.
 */
@ContentView(R.layout.activity_newserachblue)
public class NewSearchBlueActivity extends BaseActivity {
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
    private RecyclerView activity_serachblue_devicelist;
    private static List<deviceEntity> devices;
    //开启动画扫描效果
    private TranslateAnimation ta1;
    private MyBuleSearchManager myBuleSerachManager;
    private Activity activity;
    private SearchBlueRecyclerViewAdapter searchBlueRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        activity = this;
        initialize();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int position = msg.arg1;
            String adrs = devices.get(position).getAddress();
            myBuleSerachManager.endSearch();
            myBuleSerachManager = null;
            Intent intent = new Intent();
            intent.putExtra("MAC", adrs);
            intent.setClass(activity, AerobicSportActivity.class);//por开始运动
            startActivity(intent);
            finish();
        }
    };

    private void initialize() {
        view_publictitle_title.setText("搜索心率设备");
        devices = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        activity_serachblue_devicelist.setLayoutManager(linearLayoutManager);
        searchBlueRecyclerViewAdapter = new SearchBlueRecyclerViewAdapter(devices, handler);
        activity_serachblue_devicelist.setAdapter(searchBlueRecyclerViewAdapter);
        startAnimation();
        myBuleSerachManager = MyBuleSearchManager.getInstance(activity, 3000, new MyBuleSearchManager.LeScanCallbackListener() {
            @Override
            public void getBluetoothDeviceList(List<MyBuleSearchManager.BluetoothDeviceEntity> bluetoothDevices) {
            }

            @Override
            public void getBluetoothDevice(MyBuleSearchManager.BluetoothDeviceEntity bluetoothDevice) {
                if (bluetoothDevice.rssi == 1111) {//搜索八秒结束
                    MyToash.Log("搜到了新设备==1111" + devices.size());
                    activity_serachblue_tration.setVisibility(View.INVISIBLE);
                    activity_serachblue_img.setVisibility(View.INVISIBLE);
                    activity_serachblue_btclick.setVisibility(View.VISIBLE);
                    activity_serachblue_tration.clearAnimation();
                    if (devices.size() != 0) {
                        activity_serachblue_devicelist.removeAllViews();
                        Collections.sort(devices, new Comparator<deviceEntity>() {
                            @Override
                            public int compare(deviceEntity deviceEntity, deviceEntity t1) {
                                return  new Double(deviceEntity.getDistance()).compareTo(new Double(t1.getDistance()));
                            }
                        });
                        searchBlueRecyclerViewAdapter.notifyDataSetChanged();
                        activity_serachblue_btclick.setText("重新搜索");
                        activity_serachblue_isfail.setText("搜索成功");
                        activity_serachblue_tishi.setText("点击心率带即可开始运动");
                    } else {
                        activity_serachblue_tishi.setText("请确保心率带已正确佩戴并靠近手机");
                        activity_serachblue_isfail.setText("搜索失败");
                        activity_serachblue_btclick.setText("重新搜索");
                    }
                } else {
                    if (bluetoothDevice.device != null) {
                        if (bluetoothDevice.device.getAddress() != null && bluetoothDevice.device.getName() != null) {
                            MyToash.Log("搜到了新设备00" + bluetoothDevice.device.getName());
                            deviceEntity entity = new deviceEntity(bluetoothDevice.device.getAddress());
                            if (!devices.contains(entity)) {
                                MyToash.Log("搜到了新设备11" + bluetoothDevice.device.getName() + "  " + devices.size());
                                //  if (bluetoothDeviceEntity.device.getName().startsWith("Polar")) {//是否屏蔽Polar
                                entity.setName(bluetoothDevice.device.getName());
                                entity.setRssi(bluetoothDevice.rssi);
                                double bigDecimal = new BigDecimal(RssiUtil.getDistance(bluetoothDevice.rssi)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                entity.setDistance(bigDecimal);
                                devices.add(entity);
                                searchBlueRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }

            }
        });


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
                activity_serachblue_devicelist.removeAllViews();
                devices.clear();
                searchBlueRecyclerViewAdapter.notifyDataSetChanged();
                startAnimation();
                activity_serachblue_isfail.setText("搜索心率监视器");
                activity_serachblue_tishi.setText("请保持心率带正确佩戴并靠近手机");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
