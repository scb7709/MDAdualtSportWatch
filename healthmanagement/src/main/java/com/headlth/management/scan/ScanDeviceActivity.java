package com.headlth.management.scan;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.headlth.management.R;
import com.headlth.management.activity.BaseActivity;

public class ScanDeviceActivity extends BaseActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 500;

    private BluetoothAdapter bluetoothAdapter;
    private Scanner scanner;
    private ListView mListView;
 /*   private GridListViewAdapter mAdapter;

    private List<BluetoothDevice> mDeviceList = Lists.newArrayList();*/

    private Intent enableBtIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_device_fragment);
        mListView = (ListView) this.findViewById(R.id.scan_device_listview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("dddd", "ScanDeviceActivity");

    /*    mAdapter = new GridListViewAdapter(1, new Holder());
        mListView.setAdapter(mAdapter);*/

        mListView.setOnItemClickListener(mItemClickListener);

        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }
    /*BluetoothAdapter是Android系统中所有蓝牙操作都需要的，它对应本地Android设备的蓝牙模块，
    在整个系统中BluetoothAdapter是单例的。当你获取到它的示例之后，就能进行相关的蓝牙操作了。*/
    /*注：这里通过getSystemService获取BluetoothManager，再通过BluetoothManager获取BluetoothAdapter。
    BluetoothManager在Android4.3以上支持(API level 18)。*/
        final BluetoothManager bluetoothManager =
                (BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }

        /*2、判断是否支持蓝牙，并打开蓝牙获取到BluetoothAdapter之后，还需要判断是否支持蓝牙，以及蓝牙是否打开。
        如果没打开，需要让用户打开蓝牙：*/
        if (!bluetoothAdapter.isEnabled()) {
            enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        /*mDeviceList.clear();*/

        init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
//                getActivity().removeStickyBroadcast(enableBtIntent);
                ScanDeviceActivity.this.getSupportFragmentManager().popBackStack();
            } else {
                init();
            }
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (scanner != null) {
            scanner.stopScanning();
            scanner = null;
        }
    }

    private void init() {

        if (scanner == null) {
            scanner = new Scanner(bluetoothAdapter, mLeScanCallback);
            scanner.startScanning();
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                    Log.e("dddd", device + "");
                    if (device != null) {
                        Log.e("dddd", device.getName());
                    }

                   /* ScanDeviceActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (mDeviceList.contains(device)) {
                                return;
                            }
                            Log.e("dddd", device.getName());
                            mDeviceList.add(device);
                            DeviceInfo info = new DeviceInfo(device, rssi);
                            mAdapter.getData().add(info);
                            mAdapter.notifyDataSetChanged();
                        }
                    });*/
                }
            };


    private static class Scanner extends Thread {
        private final BluetoothAdapter bluetoothAdapter;
        private final BluetoothAdapter.LeScanCallback mLeScanCallback;

        private volatile boolean isScanning = false;

        Scanner(BluetoothAdapter adapter, BluetoothAdapter.LeScanCallback callback) {
            bluetoothAdapter = adapter;
            mLeScanCallback = callback;
        }

        public boolean isScanning() {
            return isScanning;
        }

        public void startScanning() {
            synchronized (this) {
                isScanning = true;
                start();
            }
        }

        public void stopScanning() {
            synchronized (this) {
                isScanning = false;
                bluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    synchronized (this) {
                        if (!isScanning)
                            break;

                        bluetoothAdapter.startLeScan(mLeScanCallback);
                    }

                    sleep(SCAN_PERIOD);

                    synchronized (this) {
                        bluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }
            } catch (InterruptedException ignore) {
            } finally {
                bluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

         /*   DeviceInfo info = (DeviceInfo) mAdapter.getData().get(position);
            MyActivity activity = (MyActivity)getApplicationContext();
            Bundle args = new Bundle();
            args.putString(HomeFragment.EXTRAS_DEVICE_NAME, info.mDevice.getName());
            args.putString(HomeFragment.EXTRAS_DEVICE_ADDRESS, info.mDevice.getAddress());

            activity.notifyChange(HomeFragment.class, args);*/
        }
    };

    /*   class Holder extends GridListViewAdapter.ViewHolder<DeviceInfo> {

           private TextView mName;

           public TextView mContent;

           @Override
           public GridListViewAdapter.ViewHolder newInstance() {
               return new Holder();
           }

           @Override
           public View createView(int index, LayoutInflater inflater) {
               View view = inflater.inflate(R.layout.bluetooth_device_item, null);
               mName = (TextView) view.findViewById(R.id.bluetooth_device_name);
               mContent = (TextView) view.findViewById(R.id.bluetooth_device_content);
               return view;
           }

           @Override
           public void showData(int index, DeviceInfo data) {
               mName.setText(data.mDevice.getName());
               mContent.setText(data.mDevice.getAddress());
           }
       }
   */
    class DeviceInfo {

        public String mName;
        public String mContent;
        public BluetoothDevice mDevice;
        public int mRssi;

        public DeviceInfo(BluetoothDevice device, int rssi) {
            mDevice = device;
            mRssi = rssi;
        }
    }
}
