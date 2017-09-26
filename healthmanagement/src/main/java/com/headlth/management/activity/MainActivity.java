package com.headlth.management.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.MyBlue.MyBuleWatchManager;
import com.headlth.management.MyBlue.MyWatchBlueHandler;
import com.headlth.management.MyBlue.WatchBlueTestActivity;
import com.headlth.management.R;
import com.headlth.management.acs.App;
import com.headlth.management.MyBlue.MyBuleSearchManager;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.fragment.AnalizeFragment;
import com.headlth.management.fragment.MaidongFragment;
import com.headlth.management.fragment.MyFragment;
import com.headlth.management.fragment.NewMaidongCircleFragment;
import com.headlth.management.myview.MyToash;
import com.headlth.management.myview.PubLicDialog;
import com.headlth.management.utils.DataTransferUtils;
import com.headlth.management.utils.InternetUtils;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.watchdatasqlite.MySQLiteBaseClass;
import com.headlth.management.watchdatasqlite.MySQLiteDataDao;
import com.headlth.management.watchdatasqlite.UpLoadingWatchData;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by abc on 2016/9/23.
 */
@ContentView(R.layout.activity_mainn)
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @ViewInject(R.id.activity_main_tabs)
    private RadioGroup activity_main_tabs;
    @ViewInject(R.id.activity_main_maindong)
    private RadioButton activity_main_maindong;
    @ViewInject(R.id.main_messages)
    private RelativeLayout main_messages;
    @ViewInject(R.id.main_listCount)
    public TextView main_listCount;
    @ViewInject(R.id.main_share)
    private RelativeLayout main_share;
    @ViewInject(R.id.activity_main_title_left)
    public TextView activity_main_title_left;
    @ViewInject(R.id.activity_main_title_center)
    public TextView activity_main_title_center;
    @ViewInject(R.id.activity_main_title_right)
    public TextView activity_main_title_right;

    private App app;
    public static Activity Activity;
    private FragmentManager fragmentManager;
    private Gson g = new Gson();
    private MyBuleSearchManager myBuleSerachManager;
    private MyBuleWatchManager myBuleWatchManager;
    //  private BluetoothGattCharacteristic READE_BluetoothGattCharacteristic;
    private BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristic;
    private BluetoothGatt mBluetoothGatt;//发现蓝牙服务，根据特征值处理数据交互
    private DialogInterface dialogInterface;//是否弹出了同步数据的提示框
    private MySQLiteDataDao mySQLiteDataDao;
    private MyWatchBlueHandler myWatchBlueHandler;//发出的蓝牙指令10秒内收不到回复的处理类
    private String MAC;
    boolean IsfLoginToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mayRequestLocation();
        x.view().inject(this);
        Activity = this;
        initialize();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//获取拍照权限
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }

    private void initialize() {

        mySQLiteDataDao = MySQLiteDataDao.getInstance(Activity);
        IsfLoginToMain = ShareUitls.getString(MainActivity.this, "IsfLoginToMain", "").equals("true");
        ShareUitls.putString(MainActivity.this, "isConnectActivity", "");
        fragmentManager = getSupportFragmentManager();
        activity_main_tabs.check(activity_main_maindong.getId());
        activity_main_tabs.setOnCheckedChangeListener(this);
        changeFragment(new MaidongFragment(true), "MaidongFragment");
        InternetUtils.watchDataupload(Activity);//检测本地数据
        activity_main_title_center.setText("迈动");
        ShareUitls.putString(this, "MainActivity", "YES");//用来记录当前界面是否存在
        registServiceToActivityReceiver();
        myWatchBlueHandler();
        watchSportCheck();
        //
    }

    private void changeFragment(Fragment fragment, String tag) {
        //1.获取事务对象
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //2.切换内容的显示
        transaction.replace(R.id.activity_main_frame, fragment, tag);
//		//3.进站
//		transaction.addToBackStack(null);
        //4.提交事务
        transaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.activity_main_maindong:
                activity_main_title_center.setText("迈动");
                activity_main_title_left.setText("");
                activity_main_title_right.setText("");
                main_share.setVisibility(View.GONE);
                main_messages.setVisibility(View.VISIBLE);
                changeFragment(new MaidongFragment(false), "MaidongFragment");

                break;
            case R.id.activity_main_analyze:
                main_share.setVisibility(View.GONE);

                activity_main_title_left.setText("");
                activity_main_title_center.setText("有效运动");
                activity_main_title_right.setText("卡路里");
                main_messages.setVisibility(View.GONE);
                changeFragment(new AnalizeFragment(), "AnalizeFragment");
                break;
            case R.id.activity_main_maidongcircle:
                main_share.setVisibility(View.VISIBLE);
                activity_main_title_center.setText("迈动圈");
                activity_main_title_left.setText("");
                activity_main_title_right.setText("");
                main_messages.setVisibility(View.GONE);
                //  changeFragment(new MaidongCircleFragment(), "MaidongCircleFragment");
                changeFragment(new NewMaidongCircleFragment(), "NewMaidongCircleFragment");
                break;
            case R.id.activity_main_my:
                main_share.setVisibility(View.GONE);
                activity_main_title_center.setText("我");
                activity_main_title_left.setText("");
                activity_main_title_right.setText("");
                main_messages.setVisibility(View.GONE);
                changeFragment(new MyFragment(), "MyFragment");
                break;
        }
    }

    @Event(value = {R.id.main_messages})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.main_messages:
                startActivity(new Intent(MainActivity.this, MessageActivity.class));
                main_listCount.setVisibility(View.GONE);
                ShareUitls.putString(MainActivity.this, "main_listCount", "0");
        }
    }

    private void registServiceToActivityReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("main_listCount");
        registerReceiver(main_listCountReceiver, filter);
    }

    //收到通知后台推送的新消息 然后首页铃铛+  1   的 广播
    private BroadcastReceiver main_listCountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int main_list_Count = Integer.parseInt(ShareUitls.getString(context, "main_listCount", "0"));
            Log.i("main_list_Count", "" + main_list_Count);
            ShareUitls.putString(context, "main_listCount", (main_list_Count + 1) + "");
            main_listCount.setVisibility(View.VISIBLE);
            main_listCount.setText((main_list_Count + 1) + "");
        }
    };


    //boolean
    @Override
    protected void onStart() {
        super.onStart();
        watchSportMonitoring();
    }

    private void watchSportCheck() {
        String WATCHSPORT = ShareUitls.getString(MainActivity.this, "WATCHSPORT", "");
        Log.i("myblue", WATCHSPORT + "  --- START");
        if (WATCHSPORT.equals("START")) {
            if (IsfLoginToMain) {//第一次进入首页需要同步数据 打开蓝牙检测数据
                // ShareUitls.putString(MainActivity.this, "isfirst", "false");
                serachBule();

            }
        }
    }

    private void watchSportMonitoring() {
        String isConnectActivity = ShareUitls.getString(MainActivity.this, "isConnectActivity", "");//
        if (isConnectActivity.equals("YES")) {
            MaidongFragment.showNotDialog(MainActivity.this, false, null);
            connectBule(true);
            ShareUitls.putString(MainActivity.this, "isConnectActivity", "");
        }
    }

    boolean ToWatchSportSummaryActivity;//已经收到数据弹框提示进入运动小结了 避免二次收数据 再次弹框
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {//收不到数据通知
                String text = msg.obj.toString();
                Log.i("myblue", text);
                if (text.length() > 5) {
                    String head = text.substring(0, 2);
                    HandlerData(head, text);
                }
            } else {

            }
        }
    };

    private void HandlerData(String head, final String text) {
        byte[] bytes;
        switch (head) {
            case "08"://单次运动结果查询
                Handle08(text);
                break;
            case "13"://得到腕表运动状态13040017
               /* String result=text.substring(4,6);
                switch (result){
                    case  "01":
                       // break;
                    case  "02":
                     //   break;
                    case  "03":

                        break;
                    case  "00"://没在运动
                        bytes = WatchBlueTestActivity.getWatchBuleData("Single_motion_results");
                        sendToBule(bytes, WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                        break;
                }*/
                //   break;
            case "14"://腕表运动状态变更通知
                String result = text.substring(4, 6);
                switch (result) {
                    case "01":
                        // break;
                    case "02":
                        //   break;
                    case "03":

                        break;
                    case "00"://没在运动
                        bytes = WatchBlueTestActivity.getWatchBuleData("Single_motion_results");
                        sendToBule(bytes, WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                        break;
                }
                //   bytes = WatchBlueTestActivity.getWatchBuleData("WatchSportStateChange");
                //  sendToBule(bytes, WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                break;

        }
    }

    private void Handle08(final String text) {
        ShareUitls.putString(Activity, "WATCHSPORT", "");
        if (!text.equals("081400000000000000000000000000000000001c")/* && !ToWatchSportSummaryActivity*/) {//包含非0数据 则证明有数据 提示并跳转到腕表运动小结同步数据
            mySQLiteDataDao.insertSingleAndOriginal(new MySQLiteBaseClass.Single_Original(DataTransferUtils.getInt_10(text.substring(4, 12)) + "", text));//保存单次运动数据
            UpLoadingWatchData.getInstance(Activity).uploadingWatchData("Single_motion_results", text, 0);//上传单次数据

            if (IsfLoginToMain) {
                ShareUitls.putString(Activity, "IsfLoginToMain", "false");
                MaidongFragment.fragment_maidong_yougang_go.setText("开始运动");
                MaidongFragment.fragment_maidong_yougang_go.setClickable(true);
            }
            RemindSyncDataDailog(Activity);

        } else {
            if (IsfLoginToMain) {
                ShareUitls.putString(Activity, "IsfLoginToMain", "false");
                MaidongFragment.fragment_maidong_yougang_go.setText("开始运动");
                MaidongFragment.fragment_maidong_yougang_go.setClickable(true);
            }

        }


    }

    //弹出是否同步数据的提示框
    public static void RemindSyncDataDailog(final Activity activity) {
        PubLicDialog.showNotDialog2(activity, new String[]{"提示:", "您有未同步的数据,是否同步?", "同步", "暂不"}, new PubLicDialog.PubLicDialogOnClickListener2() {
            @Override
            public void setPositiveButton() {
                Intent intent = new Intent(activity, WatchSportSummaryActivity.class);
                activity.startActivity(intent);
            }

            @Override
            public void setNegativeButton() {
             /*   if (MyBuleWatchManager.getInstance(null) != null) {
                    MyBuleWatchManager.getInstance(null).endConnect();//销毁单例 重新创建
                }*/
            }
        });
    }

    public void sendToBule(byte[] bytes, BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristic, BluetoothGatt mBluetoothGatt) {
        if (bytes != null && bytes.length > 0) {
            WRITE_BluetoothGattCharacteristic.setValue(bytes);
            mBluetoothGatt.writeCharacteristic(WRITE_BluetoothGattCharacteristic);
        }
    }


    private void serachBule() {
        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Single_motion_results", 15000);
        MAC = ShareUitls.getUserInformationMac(MainActivity.this);
        myBuleSerachManager = MyBuleSearchManager.getInstance(MainActivity.this, 0, new MyBuleSearchManager.LeScanCallbackListener() {
            @Override
            public void getBluetoothDeviceList(List<MyBuleSearchManager.BluetoothDeviceEntity> bluetoothDevices) {
            }

            @Override
            public void getBluetoothDevice(MyBuleSearchManager.BluetoothDeviceEntity bluetoothDevice) {
                if (bluetoothDevice == null) {
                    return;//监听未触发
                } else {
                    if (bluetoothDevice.rssi == 1111) {//搜索八秒也未搜索到目标设备
                        WatchBlueTestActivity.Nosearchtothetargetdevice(myBuleSerachManager, MainActivity.this, false);
                        return;
                    }
                    if (bluetoothDevice.device != null) {
                        if (bluetoothDevice.device.getAddress() != null && bluetoothDevice.device.getName() != null) {
                            if (bluetoothDevice.device.getAddress().equals(MAC)) {//搜索到目标设备
                                if (myBuleSerachManager != null) {
                                    myBuleSerachManager.endSearch();//停止搜索
                                }
                                connectBule(false);
                            }

                        }
                    }

                }
            }
        });
    }

    private void myWatchBlueHandler() {
        myWatchBlueHandler = MyWatchBlueHandler.getInstance(new MyWatchBlueHandler.MyWatchBlueHandlerListener() {
            @Override
            public void strikeDelayed(String flag) {
                switch (flag) {
                    case "Single_motion_results":
                        MyToash.Log("收不到数据通知");
                        if (myBuleWatchManager != null) {
                            myBuleWatchManager.endConnect();
                        }
                        ShareUitls.putString(Activity, "WATCHSPORT", "");
                        MaidongFragment.fragment_maidong_yougang_go.setText("开始运动");
                        MaidongFragment.fragment_maidong_yougang_go.setClickable(true);
                        break;

                }
            }
        });

    }

    private void connectBule(final boolean isConnectActivity) {
            myBuleWatchManager = MyBuleWatchManager.getInstance(Activity, MAC, new MyBuleWatchManager.OnCharacteristicListener() {
                @Override
                public void onServicesDiscovered(BluetoothGatt bluetoothGatt,BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristi) {
                    WRITE_BluetoothGattCharacteristic = WRITE_BluetoothGattCharacteristi;
                    mBluetoothGatt = bluetoothGatt;
                    if (WRITE_BluetoothGattCharacteristic != null&&mBluetoothGatt!=null) {
                        if (!isConnectActivity) {
                            byte[] bytes = WatchBlueTestActivity.getWatchBuleData("Single_motion_results");
                            sendToBule(bytes, WRITE_BluetoothGattCharacteristic, mBluetoothGatt);

                    /*    sendToBule(   WatchBlueTestActivity.snycDataOriginal_data(1000000000), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);//请求单次运动原始数据
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Original_data");*/
                        }
                    }
                }
                @Override
                public void onCharacteristicChanged(byte[] data) {
                    //  Log.i("myblue", "数据通知");
                    String text = DataTransferUtils.format(data);
                    Message message = Message.obtain();
                    message.obj = text;
                    message.what = 1;
                    handler.sendMessage(message);

                }
            });
    }

    long temptime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            if (System.currentTimeMillis() - temptime > 2000) // 2s内再次选择back键有效
            {
                Toast.makeText(this, "请再按一次返回退出", Toast.LENGTH_SHORT).show();
                temptime = System.currentTimeMillis();
            } else {
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareUitls.putString(this, "MainActivity", "");//用来记录当前界面是否存在
        unregisterReceiver(main_listCountReceiver);//解除广播 收到通知后台推送的新小新 然后首页铃铛+  1   的 广播
        if (myBuleWatchManager != null) {
            myBuleWatchManager.endConnect();
        }
        if (myBuleSerachManager != null) {
            myBuleSerachManager.endSearch();//停止搜索
        }
    }
}
