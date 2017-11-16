package com.headlth.management.activity;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.headlth.management.MyBlue.MyBuleSearchManager;
import com.headlth.management.MyBlue.MyBuleWatchManager;

import com.headlth.management.MyBlue.MyWatchBlueHandler;
import com.headlth.management.MyBlue.WatchBlueTestActivity;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.clenderutil.WaitDialog;
import com.headlth.management.entity.PublicDataClass;

import com.headlth.management.myview.MyToash;
import com.headlth.management.myview.PubLicDialog;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DataTransferUtils;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import zxing.activity.CaptureActivity;

/**
 * Created by abc on 2017/7/31.
 */
@ContentView(R.layout.activity_mydervices)
public class MyDevicesActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.activity_mydevice_connect)
    private TextView activity_mydevice_connect;
    @ViewInject(R.id.activity_mydevice_bangding)
    private Button activity_mydevice_bangding;


    String MAC = "";
    private MyBuleWatchManager myBuleWatchManager;

    private boolean read, write;
    // private BluetoothGattCharacteristic READE_BluetoothGattCharacteristic;
    private BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristic;
    private MyBuleSearchManager myBuleSerachManager;
    private WaitDialog waitDialog;
    private BluetoothGatt mBluetoothGatt;//发现蓝牙服务，根据特征值处理数据交互
    private String UID = "", WATCHSPORT;
    private Activity activity;
    private MyWatchBlueHandler myWatchBlueHandler;//发出的蓝牙指令10秒内收不到回复的处理类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        activity = this;
        view_publictitle_title.setText("我的设备");
        UID = ShareUitls.getString(MyDevicesActivity.this, "UID", "0");
        WATCHSPORT = ShareUitls.getString(MyDevicesActivity.this, "WATCHSPORT", "");
    }

    @Override
    protected void onStart() {
        super.onStart(); //MyBuleWatchSummaryManager.getInstance()
        MAC =ShareUitls.getUserInformationMac(MyDevicesActivity.this);
        Log.i("myblue", MAC);
        if (!MAC.equals("")) {
            activity_mydevice_bangding.setBackgroundResource(R.drawable.shape_mydevice_bangding);
            activity_mydevice_bangding.setText("解绑");
            activity_mydevice_bangding.setTextColor(Color.parseColor("#bfbfbf"));
            if (MyBuleWatchManager.IS_CONNECT) {
                activity_mydevice_connect.setText("已连接");
            } else {
                activity_mydevice_connect.setText("已断开");
            }
        } else {
            activity_mydevice_bangding.setBackgroundResource(R.drawable.shape_mydevice_nobangding);
            activity_mydevice_bangding.setText("去绑定");
            activity_mydevice_bangding.setTextColor(Color.parseColor("#ffac04"));
            activity_mydevice_connect.setText("未绑定");
        }

    }

    @Event(value = {R.id.view_publictitle_back, R.id.activity_mydevice_layout,R.id.activity_mydevice_MACNULL})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.activity_mydevice_layout:
                MAC = ShareUitls.getUserInformationMac(MyDevicesActivity.this);
                if (MAC.equals("")) {
                    Intent intent = new Intent(MyDevicesActivity.this, CaptureActivity.class);
                    intent.putExtra("flag", "bangding");
                    startActivity(intent);
                } else if (WATCHSPORT.equals("START")) {
                    Toast.makeText(MyDevicesActivity.this, "您还有还未同步的数据,请先同步数据", Toast.LENGTH_LONG).show();
                } else {
                    showNotDialog();
                }
                break;
            case R.id.activity_mydevice_MACNULL:
                updatekMac();
                break;
        }
    }

    public void updatekMac() {//上传更新User蓝牙地址
        //   String version = VersonUtils.getVersionName(MyDevicesActivity.this);
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostMACRequest&version=v2.9.6");
        params.addBodyParameter("MACAddress", "");
        HttpUtils.getInstance(MyDevicesActivity.this).sendRequestRequestParamsNew("", params, false, new HttpUtils.ResponseListenerNew() {
                    @Override
                    public void onResponse(String response, PublicDataClass.MdResponse mdResponse) {
                        if (mdResponse.Status.equals("1")) {
                            ShareUitls.putUserInformationMac(MyDevicesActivity.this, "");
                            if (myBuleWatchManager != null) {
                                myBuleWatchManager.endConnect();
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            waitDialog.setMessage("解绑成功");
                            activity_mydevice_bangding.setBackgroundResource(R.drawable.shape_mydevice_nobangding);
                            activity_mydevice_bangding.setText("去绑定");
                            activity_mydevice_connect.setText("未绑定");
                            activity_mydevice_bangding.setTextColor(Color.parseColor("#ffac04"));
                            waitDialog.dismissDialog();

                        } else {


                        }

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {


                    }
                }

        );


    }

    private void showNotDialog() {
        PubLicDialog.showNotDialog(activity, new String[]{"提示:", "是否确定解除绑定?", "确定", "暂不"}, new PubLicDialog.PubLicDialogOnClickListener() {
            @Override
            public void setPositiveButton() {
                myWatchBlueHandler();
                waitDialog = new com.headlth.management.clenderutil.WaitDialog(MyDevicesActivity.this);
                waitDialog.setCancleable(true);
                waitDialog.setMessage("正在搜索腕表...");
                waitDialog.showDailog();
                //   connectBulee();
            serachBule();
            }
        });
    }


    private void connectBule() {
        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("connectBule", 60000);
        myBuleWatchManager = MyBuleWatchManager.getInstance(activity,MAC, new MyBuleWatchManager.OnCharacteristicListener() {
            @Override
            public void onServicesDiscovered(BluetoothGatt bluetoothGatt,BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristi) {

                WRITE_BluetoothGattCharacteristic = myBuleWatchManager.getBluetoothGattCharacteristic();
                mBluetoothGatt = myBuleWatchManager.getBluetoothGatt();
                if (WRITE_BluetoothGattCharacteristic != null&&mBluetoothGatt!=null) {
                    waitDialog.setMessage("正在解绑设备...");
                    sendToBule(WatchBlueTestActivity.bangding(false, UID), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("jiechubangding",15000);
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
    private void serachBule() {
        waitDialog.setMessage("正在连接腕表...");
        myBuleSerachManager = MyBuleSearchManager.getInstance(MyDevicesActivity.this, 0, new MyBuleSearchManager.LeScanCallbackListener() {
            @Override
            public void getBluetoothDeviceList(List<MyBuleSearchManager.BluetoothDeviceEntity> bluetoothDevices) {
            }

            @Override
            public void getBluetoothDevice(MyBuleSearchManager.BluetoothDeviceEntity bluetoothDevice) {
                if (bluetoothDevice == null) {
                    return;//监听未触发
                } else {
                    if (bluetoothDevice.rssi == 1111) {//搜索八秒也未搜索到目标设备
                        //waitDialog.dismissDialog();
                        WatchBlueTestActivity.Nosearchtothetargetdevice(myBuleSerachManager, activity, true);
                        return;
                    }
                    if (bluetoothDevice.device != null) {
                        if (bluetoothDevice.device.getAddress() != null && bluetoothDevice.device.getName() != null) {
                            if (bluetoothDevice.device.getAddress().equals(MAC)) {//搜索到目标设备
                                if (bluetoothDevice.device.getAddress().equals(MAC)) {
                                    myBuleSerachManager.endSearch();//停止搜索
                                    myBuleSerachManager = null;
                                    connectBule();
                                    return;
                                }
                            }

                        }
                    }

                }
            }
        });
    }


    public static void sendToBule(byte[] bytes, BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristic, BluetoothGatt mBluetoothGatt) {
        if (bytes != null && bytes.length > 0) {
            WRITE_BluetoothGattCharacteristic.setValue(bytes);
            mBluetoothGatt.writeCharacteristic(WRITE_BluetoothGattCharacteristic);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("");
            Log.i("myblue", "收到解绑通知");
            String text = msg.obj.toString();
            Log.i("myblue", text);
            if (text.length() > 5) {
                if(text.startsWith("14")){
                    return;
                }
                if (text.substring(4, 6).equals("00")) {
                    ShareUitls.putString(MyDevicesActivity.this, "WATCHSPORT", "");
                    ShareUitls.putString(MyDevicesActivity.this, "isConnectActivity", "");
                    updatekMac();
                } else {
                    MyToash.Log(DataTransferUtils.getString_10(text.substring(6, 38)));
                    if (text.equals("0214010000000000000000000000000000000017")) {//已经处于解除绑定了
                        //ShareUitls.putString(MyDevicesActivity.this, "WATCHSPORT", "");
                        updatekMac();

                    } else {
                        MyToash.Toash(activity, "解绑失败");
                        waitDialog.setMessage("解绑失败.");
                        waitDialog.dismissDialog();
                    }
                }
            } else {
            }
           /* myBuleWatchManager.endConnect();
            myBuleWatchManager = null;*/

        }
    };
    private void myWatchBlueHandler() {
        myWatchBlueHandler = MyWatchBlueHandler.getInstance(new MyWatchBlueHandler.MyWatchBlueHandlerListener() {
            @Override
            public void strikeDelayed(String flag) {
                MyToash.Toash(activity, "解绑失败");
                waitDialog.setMessage("解绑失败.");
                waitDialog.dismissDialog();
              /*  switch (flag) {

                    case "jiechubangding":

                        break;

                }*/
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  if (myBuleWatchManager != null) {
            myBuleWatchManager.endConnect();
        }*/
        if (myBuleSerachManager != null) {
            myBuleSerachManager.endSearch();//停止搜索
        }
    }
}
