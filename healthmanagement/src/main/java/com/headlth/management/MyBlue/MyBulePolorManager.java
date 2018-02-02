package com.headlth.management.MyBlue;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by abc on 2017/7/19.
 */
public class MyBulePolorManager implements Serializable {
    public final static String POLOR_SERVICE_UUID = "0000180d-0000-1000-8000-00805f9b34fb";
    public final static String POLPR_RateCharacteristic_UUID = "00002a37-0000-1000-8000-00805f9b34fb";


    /**
     * 发现服务、数据读写操作接口
     */
    public interface OnCharacteristicListener {
        void onCharacteristicChanged(BluetoothGattCharacteristic characteristic);
    }

    private static OnCharacteristicListener CharacteristicListener;
    private static MyBulePolorManager myBuleConnectManager;
    private Activity activity;
    private BluetoothAdapter bluetoothAdapter;

    private String ADRS;
    private static BluetoothGatt mBluetoothGatt;//发现蓝牙服务，根据特征值处理数据交互
    public static boolean IS_CONNECT;//连接状态
    private static int CONNECT_TIME;//两次重新连接间的最小时间间隔

    public static boolean OVER;//结束
    public static boolean FIRDT_CONNECT = true;//第一次连接
    private Timer timer;
    private TimerTask timerTask;
    private MyBuleSearchManager myBuleSerachManager;

    public MyBulePolorManager(Activity activity, String ADRS, OnCharacteristicListener mCharacteristicListener) {
        this.activity = activity;
        this.ADRS = ADRS;
        CONNECT_TIME = 5000;
        CharacteristicListener = mCharacteristicListener;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean flag = bluetoothAdapter.isEnabled();
        timer = new Timer();
        activity.registerReceiver(receiver, MyBuleSearchManager.registBroadcast());
        if (flag) {
            first_connect();//开启首次连接
        } else {
            MyBuleSearchManager.openBluetooth(activity);
        }


    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("", "蓝牙广播回调 action=" + action);
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {//关闭系统蓝牙
                IS_CONNECT = false;
                MyBuleSearchManager.openBluetooth(activity);
            } else if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON) {//系统蓝牙打开
                myBuleSerachManager = MyBuleSearchManager.getInstance(activity, 3000, new MyBuleSearchManager.LeScanCallbackListener() {
                    @Override
                    public void getBluetoothDeviceList(List<MyBuleSearchManager.BluetoothDeviceEntity> bluetoothDevices) {
                    }

                    @Override
                    public void getBluetoothDevice(MyBuleSearchManager.BluetoothDeviceEntity bluetoothDevice) {
                        if (bluetoothDevice.rssi == 1111) {//搜索八秒结束
                            // MyToash.Toash(activity,"收不到");
                            myBuleSerachManager.StartScan();
                        } else {
                            if (bluetoothDevice.device != null) {
                                if (bluetoothDevice.device.getAddress() != null && bluetoothDevice.device.getName() != null) {
                                    if (bluetoothDevice.device.getAddress().equals(ADRS)) {
                                        myBuleSerachManager.endSearch();
                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        if (FIRDT_CONNECT) {
                                            first_connect();//开启首次连接
                                        } else {

                                            nofirst_connect();
                                        }
                                    }
                                }
                            }
                        }

                    }
                });


            }
        }
    };

    public static MyBulePolorManager getInstance(Activity activity, String ADRS, OnCharacteristicListener mCharacteristicListener) {
        if (myBuleConnectManager == null) {
            synchronized (MyBulePolorManager.class) {
                if (myBuleConnectManager == null) {
                    myBuleConnectManager = new MyBulePolorManager(activity, ADRS, mCharacteristicListener);
                }
            }
        } else {
            if (mCharacteristicListener != null) {
                if (CharacteristicListener != null) {
                    CharacteristicListener = null;
                }
            }
            CharacteristicListener = mCharacteristicListener;

        }
        return myBuleConnectManager;
    }


  /*  public static MyBuleWatchManager getInstance(OnCharacteristicListener mCharacteristicListener) {

        return myBuleConnectManager;
    }*/

    /**
     * 使用handler返回主线程,避免UI层直接操作而导致的奔溃
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    /**
     * 判断设备是否连接
     *
     * @return true 已连接
     */
    public boolean first_connect() {
        FIRDT_CONNECT = false;
        // Log.i("myblue", "first_connect开始连接+" + ADRS);
        if (ADRS == null) {
            return false;
        }
        BluetoothDevice device;
        if (bluetoothAdapter == null) {
            device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(ADRS);

        } else {
            device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(ADRS);
        }

        if (device == null) {
            return false;
        }
        // Log.i("myblue", "first_connect马上连接+");
        mBluetoothGatt = device.connectGatt(activity, false, mGattCallback);//创建新的连接
        return true;
    }

    public void nofirst_connect() {
        if (bluetoothAdapter.isEnabled()) {

            if (mBluetoothGatt != null && mBluetoothGatt.connect()) {
                Log.i("myblue", "拦截" + mBluetoothGatt.connect());
                return;
            }
            try {
                if (mBluetoothGatt != null) {
                    mBluetoothGatt.disconnect();
                    // mBluetoothGatt.close();
                }
            } catch (Exception e) {

            }
            first_connect();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://断开连接
                    Log.i("myblue", "断开连接");
                    IS_CONNECT = false;
                    if (timerTask != null) {
                        timerTask.cancel();
                        timerTask = null;
                    }
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(1);//重启连接每 CONNECT_TIME 秒重连一次
                        }
                    };
                    timer.schedule(timerTask, 0, CONNECT_TIME);
                    break;
                case 1://重启连接
                    Log.i("myblue", "重启连接");
                    if (!IS_CONNECT && bluetoothAdapter.isEnabled()) {
                        nofirst_connect();
                    }
                    break;
                case 2://成功连接
                    Log.i("myblue", "连接上了");
                    IS_CONNECT = true;
                    if (timerTask != null) {
                        timerTask.cancel();
                        timerTask = null;
                    }
                    break;

                case 3://设备可用

                    break;

                case 5://
                    if (!bluetoothAdapter.enable()) {
                        // MyBuleSearchManager.openBluetooth(activity);
                    } else {
                        first_connect();//开启首次连接
                    }
                    break;

            }
        }
    };

    /**
     * 蓝牙协议回调
     */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        /**
         * 连接状态
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            if (newState == BluetoothGatt.STATE_CONNECTED) {// 连接状态
                handler.sendEmptyMessage(2);//重启连接每 CONNECT_TIME 秒重连一次
                gatt.discoverServices();//设备连接成功，查找服务!
                //   }
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {// 断开连接
                if (!OVER)
                    handler.sendEmptyMessage(0);//重启连接每 CONNECT_TIME 秒重连一次
            }
        }

        /**
         * 是否发现服务
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                List<BluetoothGattService> gattServices = gatt.getServices();
                Log.i("myblue", "发现服务");
                for (BluetoothGattService gattService : gattServices) {
                    Log.i("myblue", "服务UUID" + gattService.getUuid().toString());
                    if (gattService.getUuid().equals(UUID.fromString(POLOR_SERVICE_UUID))) {
                        List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : gattCharacteristics) {
                            Log.i("myblue", "特征的" + bluetoothGattCharacteristic.getUuid().toString());
                            if (bluetoothGattCharacteristic.getUuid().equals(UUID.fromString(POLPR_RateCharacteristic_UUID))) {
                                boolean isEnableNotification = mBluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, true);
                                if (isEnableNotification) {
                                    List<BluetoothGattDescriptor> descriptorList = bluetoothGattCharacteristic.getDescriptors();
                                    if (descriptorList != null && descriptorList.size() > 0) {
                                        for (BluetoothGattDescriptor descriptor : descriptorList) {
                                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                            gatt.writeDescriptor(descriptor);
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        /**
         * 读操作回调
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            //    Log.i("myblue", "数据通知11");
            if (CharacteristicListener != null) {
                CharacteristicListener.onCharacteristicChanged(characteristic);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);

        }

        /**
         * 信号强度
         */
        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }
    };

    public void endConnect() {
        OVER = true;
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        try {
            activity.unregisterReceiver(receiver);
        } catch (Exception e) {
        }
        try {
            if (mBluetoothGatt != null) {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
                mBluetoothGatt = null;
            }
        } catch (Exception e) {
        }
        try {
            if (myBuleSerachManager != null) {
                myBuleSerachManager.endSearch();
            }
        } catch (Exception e) {
        }
        if (CharacteristicListener != null)
            CharacteristicListener = null;
        IS_CONNECT = false;
        myBuleConnectManager = null;

        Log.i("myblue", "disconnect");
    }

}
