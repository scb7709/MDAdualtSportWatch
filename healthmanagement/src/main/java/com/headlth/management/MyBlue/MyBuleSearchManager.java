package com.headlth.management.MyBlue;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.headlth.management.myview.MyToash;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by abc on 2017/7/19.
 */
public class MyBuleSearchManager {
    private static MyBuleSearchManager myBuleSerachManager;
    private Activity activity;
    public boolean mScanning;
    public boolean isend;
    private BluetoothAdapter bluetoothAdapter;
    private static int SERACH_TIME;//单次搜索时长
    private LeScanCallbackListener leScanCallbackListener;//搜索到设备的监听器
    private List<BluetoothDeviceEntity> bluetoothDeviceList;//单次搜索到的蓝牙设备的集合
    // BluetoothLeScanner scanner;
    //  private Timer timer = new Timer();

    public MyBuleSearchManager(Activity activity, int SERACH_TIME, LeScanCallbackListener leScanCallbackListener) {
        this.activity = activity;
        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            MyToash.Toash(activity, "设备版本过低,不支持低功耗蓝牙蓝牙服务");
            activity.finish();
            return;
        } else {
            this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter!=null) {
                MyBuleSearchManager.SERACH_TIME = SERACH_TIME;
                this.bluetoothDeviceList = SERACH_TIME == 0 ? null : new ArrayList<BluetoothDeviceEntity>();
                this.leScanCallbackListener = leScanCallbackListener;
                // scanner = bluetoothAdapter.getBluetoothLeScanner();
                activity.registerReceiver(receiver, registBroadcast());
                boolean flag = bluetoothAdapter.isEnabled();
                // Log.i("myblue", flag + "    " + SERACH_TIME);
                if (flag) {
                    StartScan();
                } else {
                   // mHandler.sendEmptyMessageDelayed(3, 8000);
                    openBluetooth(activity);
                }
            }else {
                MyToash.Toash(activity, "设备版本过低,不支持低功耗蓝牙蓝牙服务");
                activity.finish();
            }
        }
        //
    }

    public static MyBuleSearchManager getInstance(Activity activity, int SERACH_TIME, LeScanCallbackListener leScanCallbackListener) {
      /*  if (myBuleSerachManager == null) {
            synchronized (MyBuleSearchManager.class) {
                if (myBuleSerachManager == null) {
                    myBuleSerachManager = new MyBuleSearchManager(activity, SERACH_TIME, leScanCallbackListener);
                }
            }
        }*/
        myBuleSerachManager = new MyBuleSearchManager(activity, SERACH_TIME, leScanCallbackListener);
        return myBuleSerachManager;
    }


    /**
     * 查找蓝牙广播回调
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("", "蓝牙广播回调 action=" + action);
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {//关闭系统蓝牙
                Log.i("", "系统蓝牙断开！！");
                boolean isEnable = enable();
                if (!isEnable)
                    openBluetooth(activity);
            } else if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON) {//系统蓝牙打开
                mScanning = false;
                StartScan();//搜索设备
                Log.i("", "系统蓝牙打开！！");

            }
        }
    };

    public boolean enable() {
        return bluetoothAdapter != null && bluetoothAdapter.enable();
    }

    public static void openBluetooth(Activity activity) {
        //打开蓝牙提示框
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, 100);
    }

    public static IntentFilter registBroadcast() {
        //  Logger.e("注册监听系统蓝牙状态变化广播 ");
        IntentFilter filter = new IntentFilter();
        //蓝牙状态改变action
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //蓝牙断开action
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        return filter;
    }


/*    TimerTask timerTask1 = new TimerTask() {//每隔十秒搜索一次 每次搜索5秒
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    };
    TimerTask timerTask2 = new TimerTask() {//每隔十秒关闭一次
        @Override
        public void run() {
            mHandler.sendEmptyMessage(2);
        }
    };*/

    TimerTask timerTask;

    //包含开始搜索,停止搜索的方法
    public void StartScan() {
        if (bluetoothDeviceList != null) {
            bluetoothDeviceList.clear();
        }
        bluetoothAdapter.startLeScan(mLeScanCallback);
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (SERACH_TIME != 0) {
                    mHandler.sendEmptyMessageDelayed(3, SERACH_TIME);
                } else {
                    mHandler.sendEmptyMessageDelayed(3, 8000);
                }
            }
        }.start();


    }

    public void StopScan() {

        if (!isend) {
            try {
                mScanning = false;
                if (SERACH_TIME != 0) {

                }
                leScanCallbackListener.getBluetoothDeviceList(bluetoothDeviceList);
                bluetoothDeviceList.clear();
                bluetoothAdapter.stopLeScan(mLeScanCallback);
            } catch (Exception e) {
            }
        }

    }

    public interface LeScanCallbackListener {//搜索到蓝牙设备的回调接口

        void getBluetoothDeviceList(List<BluetoothDeviceEntity> bluetoothDevices);//当 SERACH_TIME不为0时 搜索完成后返回一个设备集合

        void getBluetoothDevice(BluetoothDeviceEntity bluetoothDevice);//当 SERACH_TIME为0时 每次收到一个设备就抛出
        // void SearchStart();//当 SERACH_TIME不为0时 该方法表示搜索开始 可用于跟新UI

    }

    // 查找到设备之后将设备的地址添加到集合当中
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        if (device != null && device.getAddress() != null) {
                            BluetoothDeviceEntity bluetoothDeviceEntity = new BluetoothDeviceEntity(device, rssi, scanRecord);
                            if (SERACH_TIME == 0) {//搜索到一个处理一个
                                Message message = Message.obtain();
                                message.obj = bluetoothDeviceEntity;
                                message.what = 2;
                                mHandler.sendMessage(message);
                            } else {//搜索固定时间了统一处理
                                bluetoothDeviceList.add(bluetoothDeviceEntity);
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }.start();

        }
    };

/*
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            //Batch 一批
            for (ScanResult result : results) {
                Log.i("llll", "onBatchScanResults " + result);
            }
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device= result.getDevice();
            Message message = Message.obtain();
            message.obj = new BluetoothDeviceEntity(device, result.getRssi(), null);
            mHandler.sendMessage(message);
        }
    };
*/

    public void endSearch() {
      /*  if (timerTask != null) {
            timerTask.cancel();
        }*/
        Log.i("myblue", "endSearch！！");
        isend = true;
        if (bluetoothDeviceList != null) {
            bluetoothDeviceList.clear();
        }
        try {
            mHandler.removeMessages(3);
        } catch (Exception e) {
        }
        try {
            activity.unregisterReceiver(receiver);
        } catch (Exception e) {
        }
        try {
            bluetoothAdapter.stopLeScan(mLeScanCallback);
        } catch (Exception e) {
        }
        myBuleSerachManager = null;
    }


    /**
     * 使用handler返回主线程,避免UI层直接操作而导致的奔溃
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {//
                bluetoothDeviceList.clear();
                bluetoothAdapter.startLeScan(mLeScanCallback);
            } else if (msg.what == 2) {//搜索一个处理一个
                leScanCallbackListener.getBluetoothDevice((BluetoothDeviceEntity) msg.obj);

            } else if (msg.what == 3) {//统一处理
                try {
                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                    if (SERACH_TIME != 0) {
                        leScanCallbackListener.getBluetoothDeviceList(bluetoothDeviceList);
                    } else {//未搜索到目标设备
                        leScanCallbackListener.getBluetoothDevice(new BluetoothDeviceEntity(null, 1111, null));
                    }
                } catch (Exception e) {
                }

            } else {

            }

        }
    };


    public class BluetoothDeviceEntity {
        public BluetoothDevice device;
        public int rssi;
        public byte[] scanRecord;

        public BluetoothDeviceEntity(BluetoothDevice device, int rssi, byte[] scanRecord) {
            this.device = device;
            this.rssi = rssi;
            this.scanRecord = scanRecord;
        }
    }
}
