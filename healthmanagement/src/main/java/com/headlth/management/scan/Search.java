package com.headlth.management.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.headlth.management.R;
import com.headlth.management.activity.AerobicSportActivity;
import com.headlth.management.activity.BaseActivity;
import com.headlth.management.activity.MainActivity;
import com.headlth.management.entity.deviceEntity;
import com.headlth.management.utils.ShareUitls;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/2/25.
 */
public class Search extends BaseActivity implements AdapterView.OnItemClickListener {

    //A和n的值，需要根据实际环境进行检测得出
    private static final double A_Value = 59;
    /**
     * A - 发射端和接收端相隔1米时的信号强度
     */
    private static final double n_Value = 2.0;
    /**
     * n - 环境衰减因子
     */


    // 蓝牙适配器
    private BluetoothAdapter blueadapter = null;
    private ListView deviceListview;
    // 数组适配器
    private AppAdapter adapter;
    // 蓝牙地址备份
    private BluetoothManager bluetoothManager;
    private boolean tag = false;
    Button tration;
    ImageView img;
    Button btclick;
    Button btback;
    private TextView isfail;
    private TextView tishi;

    private static List<deviceEntity> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);


        Log.e("PAUSE", "onCreate");
        devices = new ArrayList<>();
        devices.clear();
        isfail = (TextView) this.findViewById(R.id.isfail);
        tishi = (TextView) this.findViewById(R.id.tishi);
        tration = (Button) this.findViewById(R.id.tration);
        img = (ImageView) this.findViewById(R.id.img);
        btclick = (Button) this.findViewById(R.id.btclick);
        btclick.setVisibility(View.INVISIBLE);
        deviceListview = (ListView) this.findViewById(R.id.devicelist);
        adapter = new AppAdapter(devices);
        deviceListview.setAdapter(adapter);
        //现获取蓝牙管理器
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        //然后获取蓝牙适配器
        blueadapter = bluetoothManager.getAdapter();
        //提示用户打开蓝牙
        startBluetooth(true);
        deviceListview.setOnItemClickListener(this);


        //初始化
        devices.clear();
        tration.setVisibility(View.INVISIBLE);
        img.setVisibility(View.INVISIBLE);
        btclick.setVisibility(View.VISIBLE);
        if (devices.size() == 0) {
            conneced = false;
            tishi.setText("请确定心率监测器已正确佩戴并靠近手机");
            isfail.setText("连接失败");
            btclick.setText("开始搜索");
            dcHandler.sendEmptyMessage(1);
        }
    }

    public void back(View view) {
        map.clear();
        devices.clear();
     /*   if( MainActivity.Activity!=null){
            MainActivity.Activity.finish();
        }
        startActivity(new Intent(Search.this, MainActivity.class));*/
        finish();
    }

    public void scanner(View v) {
        tra();
        Log.e("PAUSE", "scanner");
        Log.e("PAUSE", devices.size() + "devices.size()");
        // 开始扫面，3s后停止扫描蓝牙设备。
        devices.clear();
        dcHandler.sendEmptyMessage(1);
        isfail.setText("搜索心率监视器");
        tishi.setText("请确定心率监测器已正确佩戴并靠近手机");
        tration.setVisibility(View.VISIBLE);
        btclick.setVisibility(View.INVISIBLE);
        img.setVisibility(View.VISIBLE);


        new Thread(new Runnable() {
            @Override
            public void run() {
                //开始查找设备
                if (devices.size() == 0) {
                    StartStopScan(false);
                }
            }
        }).start();
        dcHandler.sendEmptyMessageDelayed(2,
                3000);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
     /*   Toast.makeText(getApplicationContext(), arg3+"列表点击了"+arg2, Toast.LENGTH_SHORT).show();*/

        Log.e("addressbb", devices.get(arg2).getAddress());
        ShareUitls.putString(getApplicationContext(), "adrs", devices.get(arg2).getAddress());
        //建立连接
        Intent i = new Intent(this, AerobicSportActivity.class);
        startActivity(i);
        finish();


    }

    public void SleepWait2() {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
    }

    private boolean mScanning;
    private boolean conneced = false;

    //包含开始搜索,停止搜索的方法
    public void StartStopScan(boolean bStop) {
        if (bStop) // 停止查找
        {
            if (mScanning) {
                tration.setVisibility(View.INVISIBLE);
                img.setVisibility(View.INVISIBLE);
                btclick.setVisibility(View.VISIBLE);
                blueadapter.stopLeScan(mLeScanCallback);
                dcHandler.sendEmptyMessage(1);
                mScanning = false;
                if (devices.size() == 0) {
                    conneced = false;
                    tration.clearAnimation();
                    tishi.setText("请确定心率监测器已正确佩戴并靠近手机");
                    isfail.setText("连接失败");
                    btclick.setText("重新搜索");
                    dcHandler.sendEmptyMessage(1);
                } else {
                    tration.clearAnimation();
                    conneced = true;
                    btclick.setText("重新搜索");
                    isfail.setText("搜索成功".trim());
                    tishi.setText("点击心率带即可开始运动".trim());
                    dcHandler.sendEmptyMessage(1);
                }
            }
        } else // 查找查找
        {
            if (!mScanning) {
                blueadapter.startLeScan(mLeScanCallback);
                mScanning = true;
            }
        }
        invalidateOptionsMenu();
    }


    public Handler dcHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    // 停止扫描蓝牙设备
                    StartStopScan(true);
                    List<deviceEntity> tempdevicess = new ArrayList<>();
                    tempdevicess.clear();
                    Log.i("DDDDDDDDDDDA", devices.size() + "   " + devices.toString());

                    for (int i = 0; i < devices.size(); i++) {//去空
                        deviceEntity deviceEntity = devices.get(i);
                        if (deviceEntity != null && deviceEntity.getName() != null) {
                            tempdevicess.add(deviceEntity);
                        }
                    }
                    Log.i("DDDDDDDDDDDb", tempdevicess.size() + "   " + tempdevicess.toString());
                    devices.clear();

                    for (int i = 0; i < tempdevicess.size(); i++) {//去空
                        deviceEntity deviceEntity = tempdevicess.get(i);
                        devices.add(deviceEntity);
                    }
                    tempdevicess.clear();
                    Log.i("DDDDDDDDDDDB", devices.size() + "   " + devices.toString());
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

                    Log.i("DDDDDDDDDDDC", devices.size() + "   " + devices.toString());

                    Collections.sort(devices); // 顺序排列
                    adapter = new AppAdapter(devices);
                    deviceListview.setAdapter(adapter);

                /*    int max = 0;
                    for (int i = 0; i < devices.size(); i++) {
                        Log.e("map", devices.get(i).getRssi() + "还没去重跟重新排序" + devices.get(i).getAddress());
                    }
                    map.clear();
                    //treeMap去重
                    for (int i = 0; i < devices.size(); i++) {
                        map.put(devices.get(i).getName() + "=" + devices.get(i).getAddress(), devices.get(i).getRssi());
                    }
                    devices.clear();
                    Iterator<String> iterator_2 = map.keySet().iterator();
                    //重新拆分
                    while (iterator_2.hasNext()) {
                        Object key = iterator_2.next();
                        deviceEntity entity = new deviceEntity();
                        entity.setAddress(changDataType3((String) key));
                        entity.setName(changDataType2((String) key));
                        entity.setRssi((String) map.get(key));
                        devices.add(entity);
                    }

                    if (devices.size() > 1) {
                        //寻找最大值
                        max = Integer.parseInt(devices.get(0).getRssi());
                        for (int i = 0; i < devices.size(); i++) {
                            if (Integer.parseInt(devices.get(i).getRssi()) > max) {
                                max = Integer.parseInt(devices.get(i).getRssi());
                            }
                        }
                        Log.e("map", max + "maxmaxmaxmaxmax");
                        Log.e("map", devices.size() + "devices.size()");
                        deviceEntity temp;
                        //把最大值放首位

                        for (int i = 0; i < devices.size(); i++) {


                            ;
                            if (max == Integer.parseInt(devices.get(i).getRssi())) {
                                Log.e("map", i + "最大的位置");
                                if (devices.size() > 1) {
                                    temp = devices.get(0);


                                    devices.get(i).setRssi("离我最近：" + new BigDecimal(Math.pow(10, (Math.abs(max) - A_Value) / (10 * n_Value))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");

                                    ;

                                    devices.set(0, devices.get(i));
                                    devices.set(i, temp);
                                }

                            }


                        }

                        for (int i = 0; i < devices.size(); i++) {
                            Log.e("map", devices.get(i).getRssi() + "重新排序并且去重");
                        }
                    }
                    for (int i = 0; i < devices.size(); i++) {
                        if (devices.get(i).getName() == null || devices.get(i).getName().equals("null") || devices.get(i).getName().equals("")) {
                            devices.remove(i);
                        }
                    }*/

                   // adapter.notifyDataSetChanged();
                    break;

                case 1:
                    adapter.notifyDataSetChanged();
                    break;
                case 3:
                    tration.startAnimation(ta1);
                    break;
            }
        }
    };

    TreeMap map = new TreeMap();
    // 查找到设备之后将设备的地址添加到集合当中
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.e("device.getAddress", device.getAddress() + "");
            Log.e("device.getAddress", rssi + "");
            deviceEntity entity = new deviceEntity();
            entity.setAddress(device.getAddress());
            entity.setName(device.getName());


          /*  rssi = Math.abs(rssi);
            Log.e("device.getAddress", Math.abs(rssi) + "Math.abs(rssi)");
            int ff = (int) ((rssi - 80) / (10 * 2.0));
            Log.e("device.getAddress", (rssi - 80) / 20 + "(rssi-80)/20");
            int a = 10 ^ ff;
            Log.e("device.getAddress", a + "10^rssi");*/
            entity.setRssi(rssi);

            double bigDecimal = new BigDecimal(Math.pow(10, (Math.abs(rssi)) - A_Value) / (10 * n_Value)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            entity.setDistance(bigDecimal);
            devices.add(entity);
        }
    };

    //打开蓝牙
    private void startBluetooth(boolean state) {
        if (state == true) {
            if (blueadapter == null) {
                Toast.makeText(getApplicationContext(), "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            }
            if (!blueadapter.isEnabled()) {
                // btAdapt.enable(); //这种方法不做提示，直接打开
                // 提示用户是否打开蓝牙
                Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enabler, 1); // 同startActivity(enabler);
            }
        } else {

        }
    }

    public String changDataType(String str) {
        String s3 = str;
        String[] temp = null;
        temp = s3.split("--");
        return temp[0];
    }

    public static String changDataType2(String str) {
        String s3 = str;
        String[] temp = null;
        temp = s3.split("=");
        Log.e("dsfsdf", temp[0]);
        return temp[0];
    }

    public static String changDataType3(String str) {
        String s3 = str;
        String[] temp = null;
        temp = s3.split("=");
        Log.e("dsfsdf", temp[1]);
        return temp[1];
    }


    //开启动画扫描效果
    TranslateAnimation ta1;

    public void tra() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ta1 = new TranslateAnimation(
                        -200, 200,
                        0, 0);
                ta1.setDuration(800);
                ta1.setStartTime(0);
                ta1.setRepeatCount(Integer.MAX_VALUE);
                ta1.setRepeatMode(Animation.REVERSE);
                dcHandler.sendEmptyMessage(3);
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
   /*         xiaoXiCallBack.MsgListBean.InfoBean.ListMsgdataBean item = getItem(position);*/
            holder.tv_name.setText(mAppList.get(position).getName());
            Log.e("xiaoxi", mAppList.get(position).getAddress() + "mAppList.get(position).getContent()");
            holder.tv_content.setText(mAppList.get(position).getAddress());

            // double bigDecimal = new BigDecimal(mAppList.get(position).getDistance()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            String str = mAppList.get(position).getDistance() + "";
            if (str.length() > 4) {
                str = str.substring(0, 4).replace("E","0");
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
        Log.e("PAUSE", "onDestroy");
        blueadapter.stopLeScan(mLeScanCallback);
        tration.clearAnimation();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
         /*   if( MainActivity.Activity!=null){
                MainActivity.Activity.finish();
            }
            startActivity(new Intent(Search.this, MainActivity.class));*/
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
