package com.headlth.management.MyBlue;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
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

/**
 * Created by abc on 2017/7/19.
 */
public class MyBuleSearchManager {
    private static MyBuleSearchManager myBuleSerachManager;
    private Activity activity;
    public boolean mScanning;
    public boolean isend;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager mBluetoothManager;
    private static int SERACH_TIME;//单次搜索时长
    private LeScanCallbackListener leScanCallbackListener;//搜索到设备的监听器
    private List<BluetoothDeviceEntity> bluetoothDeviceList;//单次搜索到的蓝牙设备的集合
    private  boolean SEARCH_METHOD;//控制搜索方法切换
    // BluetoothLeScanner scanner;
    //  private Timer timer = new Timer();

    public MyBuleSearchManager(Activity activity, int SERACH_TIME, LeScanCallbackListener leScanCallbackListener) {
        this.activity = activity;
        if (!checkIfSupportBle()) {
            MyToash.Toash(activity, "设备版本过低,不支持低功耗蓝牙蓝牙服务");
            activity.finish();
            return;
        } else {
            this.bluetoothAdapter = getAdapter();

            if (bluetoothAdapter != null) {
                MyBuleSearchManager.SERACH_TIME = SERACH_TIME;
                this.bluetoothDeviceList = SERACH_TIME == 0 ? null : new ArrayList<BluetoothDeviceEntity>();
                this.leScanCallbackListener = leScanCallbackListener;
                activity.registerReceiver(receiver, registBroadcast());
                if (bluetoothAdapter.isEnabled()) {
                    StartScan();
                } else {
                    openBluetooth(activity);
                    //mHandler.sendEmptyMessageDelayed(5,500);
                }
            } else {
                MyToash.Toash(activity, "设备版本过低,不支持低功耗蓝牙蓝牙服务");
                activity.finish();
            }
        }
        //
    }

    //检查设备是否支持BLE功能。
    private boolean checkIfSupportBle() {
        return activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    //如果设备支持BLE，那么就可以获取蓝牙适配器。
    private BluetoothAdapter getAdapter() {
        BluetoothAdapter bluetoothAdapter;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            BluetoothManager mBluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = mBluetoothManager.getAdapter();
        } else {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return bluetoothAdapter;
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
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {//搜索的
                MyToash.Log("DiscoverReceiver");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int RSSI = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);

               // Short RSSI = intent.getExtras().getShort( BluetoothDevice.EXTRA_RSSI);
                if (device != null && device.getAddress() != null) {
                    BluetoothDeviceEntity bluetoothDeviceEntity = new BluetoothDeviceEntity(device, RSSI, null);
                    leScanCallbackListener.getBluetoothDevice(bluetoothDeviceEntity);
                }
            } else {//蓝牙开关
                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {//关闭系统蓝牙
                    Log.i("", "系统蓝牙断开！！");
                    boolean isEnable = enable();
                    if (!isEnable) {
                        openBluetooth(activity);
                    }
                } else if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_ON) {//系统蓝牙打开
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mScanning = false;
                    StartScan();//搜索设备
                    Log.i("", "系统蓝牙打开！！");

                }
            }
        }
    };

    public boolean enable() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public static void openBluetooth(final Activity activity) {
    /*    PubLicDialog.showNotDialog(activity, new String[]{"提示:", "蓝牙已断开是否前往打开蓝牙?", "打开蓝牙", "暂不打开"}, new PubLicDialog.PubLicDialogOnClickListener() {
            @Override
            public void setPositiveButton() {
                activity.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));//直接进入手机中的蓝牙设置界面
            }
        });*/



/*//启动修改蓝牙可见性的Intent
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//设置蓝牙可见性的时间，方法本身规定最多可见300秒
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        activity.startActivity(intent);*/



        //打开蓝牙提示框
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        enableBtIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        activity.startActivityForResult(enableBtIntent, 333);

    }

    public static IntentFilter registBroadcast() {
        //  Logger.e("注册监听系统蓝牙状态变化广播 ");
        IntentFilter filter = new IntentFilter();
        //蓝牙状态改变action
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //蓝牙断开action
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        return filter;
    }

    //
    public void StartScan() {
        if (bluetoothDeviceList != null) {
            bluetoothDeviceList.clear();
        }
        if(SEARCH_METHOD){
            MyToash.Log("SEARCH_METHODstartDiscovery1");
            bluetoothAdapter.startDiscovery();
        }else {
            MyToash.Log("SEARCH_METHODleScanCallback1");
            bluetoothAdapter.startLeScan(leScanCallback);
        }
        mHandler.sendEmptyMessageDelayed(3, 10000);
    }
    BluetoothAdapter.LeScanCallback leScanCallback =new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int RSSI, byte[] bytes) {
            if (bluetoothDevice != null && bluetoothDevice.getAddress() != null) {
                BluetoothDeviceEntity bluetoothDeviceEntity = new BluetoothDeviceEntity(bluetoothDevice, RSSI, null);
                leScanCallbackListener.getBluetoothDevice(bluetoothDeviceEntity);
            }

        }
    }  ;
    public interface LeScanCallbackListener {//搜索到蓝牙设备的回调接口

        void getBluetoothDeviceList(List<BluetoothDeviceEntity> bluetoothDevices);//当 SERACH_TIME不为0时 搜索完成后返回一个设备集合

        void getBluetoothDevice(BluetoothDeviceEntity bluetoothDevice);//当 SERACH_TIME为0时 每次收到一个设备就抛出
        // void SearchStart();//当 SERACH_TIME不为0时 该方法表示搜索开始 可用于跟新UI
    }
    public void endSearch() {

        isend = true;
        if (bluetoothDeviceList != null) {
            bluetoothDeviceList.clear();
        }
        try {
            bluetoothAdapter.cancelDiscovery();
            mHandler.removeMessages(3);
        } catch (Exception e) {
        }
        try {
            bluetoothAdapter.stopLeScan(leScanCallback);
            mHandler.removeMessages(3);
        } catch (Exception e) {
        }
        try {
            activity.unregisterReceiver(receiver);
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
            try {
                if (SEARCH_METHOD) {
                    MyToash.Log("SEARCH_METHODstartDiscovery2");
                    bluetoothAdapter.cancelDiscovery();
                } else {
                    MyToash.Log("SEARCH_METHODleScanCallback2");
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            }catch (Exception e){}
            SEARCH_METHOD=!SEARCH_METHOD;
            leScanCallbackListener.getBluetoothDevice(new BluetoothDeviceEntity(null, 1111, null));
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
