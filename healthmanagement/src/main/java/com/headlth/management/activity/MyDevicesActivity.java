package com.headlth.management.activity;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.headlth.management.watchdatasqlite.MySQLiteBaseClass;
import com.headlth.management.watchdatasqlite.MySQLiteDataDao;
import com.headlth.management.watchdatasqlite.UpLoadingWatchData;

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
    @ViewInject(R.id.activity_mydevice_cancerbangding)
    private Button activity_mydevice_cancerbangding;
    @ViewInject(R.id.activity_mydevice_layout)
    private LinearLayout activity_mydevice_layout;

    String MAC = "";
    private MyBuleWatchManager myBuleWatchManager;

    private BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristic;
    private MyBuleSearchManager myBuleSerachManager;
    private WaitDialog waitDialog;
    private BluetoothGatt mBluetoothGatt;//发现蓝牙服务，根据特征值处理数据交互
    private String UID = "", WATCHSPORT;
    private Activity activity;
    private MyWatchBlueHandler myWatchBlueHandler;//发出的蓝牙指令10秒内收不到回复的处理类
    private static MySQLiteDataDao mySQLiteDataDao;
    boolean IsCancleBangding;//解绑了

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        activity = this;
        mySQLiteDataDao = MySQLiteDataDao.getInstance(activity);
        view_publictitle_title.setText("我的设备");
        UID = ShareUitls.getString(MyDevicesActivity.this, "UID", "0");

    }

    @Override
    protected void onStart() {
        super.onStart(); //MyBuleWatchSummaryManager.getInstance()
        MAC = ShareUitls.getUserInformationMac(MyDevicesActivity.this);
        WATCHSPORT = ShareUitls.getString(MyDevicesActivity.this, "WATCHSPORT", "");
        Log.i("myblue", MAC);
        if (!MAC.equals("")) {
            activity_mydevice_cancerbangding.setVisibility(View.VISIBLE);
            activity_mydevice_bangding.setText("绑定新腕表");
            // activity_mydevice_bangding.setBackgroundResource(R.drawable.shape_mydevice_bangding);
            //activity_mydevice_bangding.setText("解绑");
            // activity_mydevice_bangding.setTextColor(Color.parseColor("#bfbfbf"));
            if (MyBuleWatchManager.IS_CONNECT) {
                activity_mydevice_connect.setText("已连接");
            } else {
                activity_mydevice_connect.setText("已断开");
            }
        } else {
            activity_mydevice_bangding.setText("去绑定");
            activity_mydevice_cancerbangding.setVisibility(View.GONE);
            // activity_mydevice_bangding.setBackgroundResource(R.drawable.shape_mydevice_nobangding);
            // activity_mydevice_bangding.setText("去绑定");
            //activity_mydevice_bangding.setTextColor(Color.parseColor("#ffac04"));
            activity_mydevice_connect.setText("未绑定");
        }

    }

    @Event(value = {R.id.view_publictitle_back, R.id.activity_mydevice_bangding, R.id.activity_mydevice_MACNULL, R.id.activity_mydevice_cancerbangding})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.activity_mydevice_bangding:
                MAC = ShareUitls.getUserInformationMac(MyDevicesActivity.this);

                if (!MAC.equals("")) {
                    showNotBnadingDialog();//确认提醒
                } else {
                    MyBuleWatchManager.endConnect();//断开已连接的蓝牙
                    mySQLiteDataDao.deleteall();
                    IsCancleBangding = false;
                    Intent intent = new Intent(MyDevicesActivity.this, CaptureActivity.class);
                    intent.putExtra("flag", "bangding");
                    startActivity(intent);
                }

                break;
            case R.id.activity_mydevice_cancerbangding:
                MAC = ShareUitls.getUserInformationMac(MyDevicesActivity.this);
                if (!MAC.equals("")) {
                    if (WATCHSPORT.equals("START")) {
                        Toast.makeText(MyDevicesActivity.this, "您还有还未同步的数据,请先同步数据", Toast.LENGTH_LONG).show();
                    } else {
                        showNotCancelDialog();
                    }
                }
                break;
            case R.id.activity_mydevice_MACNULL:
                updatekMac(true);
                break;
        }
    }

    public void updatekMac(final boolean flag) {//上传更新User蓝牙地址
        //   String version = VersonUtils.getVersionName(MyDevicesActivity.this);
        activity_mydevice_layout.setClickable(false);
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostMACRequest&version=v2.9.6");
        params.addBodyParameter("MACAddress", "");
        HttpUtils.getInstance(MyDevicesActivity.this).sendRequestRequestParamsNew("", params, true, new HttpUtils.ResponseListenerNew() {
                    @Override
                    public void onResponse(String response, PublicDataClass.MdResponse mdResponse) {
                        activity_mydevice_layout.setClickable(true);
                        MyToash.Log(response);
                        if (mdResponse.Status.equals("1")) {
                            if (flag) {
                                ShareUitls.putUserInformationMac(activity, "");
                                IsCancleBangding = true;
                                waitDialog.setMessage("解绑成功");
                                activity_mydevice_cancerbangding.setVisibility(View.GONE);
                                activity_mydevice_bangding.setText("去绑定");
                                activity_mydevice_connect.setText("未绑定");
                            /*activity_mydevice_bangding.setBackgroundResource(R.drawable.shape_mydevice_nobangding);
                            activity_mydevice_bangding.setText("去绑定");
                            activity_mydevice_connect.setText("未绑定");
                            activity_mydevice_bangding.setTextColor(Color.parseColor("#ffac04"));*/
                                waitDialog.dismissDialog();
                            } else {
                                ShareUitls.putUserInformationMac(activity, "");
                                Intent intent = new Intent(MyDevicesActivity.this, CaptureActivity.class);
                                intent.putExtra("flag", "bangding");
                                startActivity(intent);
                                activity_mydevice_bangding.setText("去绑定");
                                activity_mydevice_cancerbangding.setVisibility(View.GONE);
                                activity_mydevice_connect.setText("未绑定");
                            }
                        } else {


                        }

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {

                        activity_mydevice_layout.setClickable(true);
                    }
                }

        );


    }

    private void showNotCancelDialog() {

        if (mySQLiteDataDao.querySingleNOOriginal().size() != 0) {
            PubLicDialog.showNotDialog2(activity, new String[]{"提示:", "检测到您有未同步的数据是否同步数据?", "同步数据", "直接解绑"}, new PubLicDialog.PubLicDialogOnClickListener2() {
                @Override
                public void setPositiveButton() {
                    Intent intent = new Intent(activity, WatchSportSummaryActivity.class);
                    startActivity(intent);
                }

                @Override
                public void setNegativeButton() {
                    mySQLiteDataDao.deleteall();
                    myWatchBlueHandler();
                    waitDialog = new com.headlth.management.clenderutil.WaitDialog(MyDevicesActivity.this);
                    waitDialog.setCancleable(false);
                    waitDialog.setMessage("正在搜索腕表...");
                    waitDialog.showDailog();
                    connectBule();
                    //    serachBule();
                }
            });


        } else {
            PubLicDialog.showNotDialog(activity, new String[]{"提示:", "是否确定解除绑定?", "确定", "暂不"}, new PubLicDialog.PubLicDialogOnClickListener() {
                @Override
                public void setPositiveButton() {
                    mySQLiteDataDao.deleteall();
                    myWatchBlueHandler();
                    waitDialog = new com.headlth.management.clenderutil.WaitDialog(MyDevicesActivity.this);
                    waitDialog.setCancleable(true);
                    waitDialog.setMessage("正在搜索腕表...");
                    waitDialog.showDailog();
                    connectBule();
                    //  serachBule();
                }
            });
        }

    }

    private void showNotBnadingDialog() {
        PubLicDialog.showNotDialog(activity, new String[]{"提示:", "是否重新绑定腕表设备?", "确定", "暂不"}, new PubLicDialog.PubLicDialogOnClickListener() {
            @Override
            public void setPositiveButton() {
                mySQLiteDataDao.deleteall();
                MyBuleWatchManager.endConnect();//断开已连接的蓝牙
                PubLicDialog.CancleshowNotDialog2();//取消有可能首页弹出的是否同步数据的 弹框
                IsCancleBangding = false;

                //ShareUitls.putUserInformationMac(activity, "");
                Intent intent = new Intent(MyDevicesActivity.this, CaptureActivity.class);
                intent.putExtra("flag", "bangding");
                startActivity(intent);
                //updatekMac(false);

            }
        });
    }

    private void connectBule() {
        activity_mydevice_layout.setClickable(false);
        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("connectBule", 60000);
        myBuleWatchManager = MyBuleWatchManager.getInstance(activity, MAC, new MyBuleWatchManager.OnCharacteristicListener() {
            @Override
            public void onServicesDiscovered(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristi) {

                WRITE_BluetoothGattCharacteristic = myBuleWatchManager.getBluetoothGattCharacteristic();
                mBluetoothGatt = myBuleWatchManager.getBluetoothGatt();
                if (WRITE_BluetoothGattCharacteristic != null && mBluetoothGatt != null) {

                    waitDialog.setMessage("正在查询设备状态");
                    byte[] bytes = WatchBlueTestActivity.getWatchBuleData("GetWatchSportState");
                    sendToBule(bytes, WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("GetWatchSportState");

                }

            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                //  Log.i("myblue", "数据通知");
                if (!IsCancleBangding) {
                    String text = DataTransferUtils.format(data);
                    Message message = Message.obtain();
                    message.obj = text;
                    message.what = 1;
                    handler.sendMessage(message);
                }

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
                    return;//
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

    private void HandlerData(String head, final String text) {

        switch (head) {
            case "13"://运动状态查询
                if (text.substring(0, 6).equals("130400")) {//
                    waitDialog.setMessage("正在查询设备数据");
                    //查询设备是否有未上传的数据（单次运动数据）
                    byte[] bytes = WatchBlueTestActivity.getWatchBuleData("Single_motion_results");
                    sendToBule(bytes, WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Single_motion_results");
                } else {
                    ShareUitls.putString(activity, "isConnectActivity", "SPORTING");
                    waitDialog.dismissDialog();
                    MyToash.Toash(activity, "解绑失败(腕表正处于运动模式中)");
                }
                break;
            case "08"://单次运动结果查询
                if (!text.contains("00000000000000000000000000000000")) {//包含非0数据 则证明有数据 提示并跳转到腕表运动小结同步数据
                    PubLicDialog.showNotDialog2(activity, new String[]{"提示:", "检测到您有未同步的数据是否同步数据?", "同步数据", "直接解绑"}, new PubLicDialog.PubLicDialogOnClickListener2() {
                        @Override
                        public void setPositiveButton() {
                            waitDialog.dismissDialog();
                            MySQLiteDataDao.getInstance(activity).insertSingleAndOriginal(new MySQLiteBaseClass.Single_Original(DataTransferUtils.getInt_10(text.substring(4, 12)) + "", text));//保存单次运动数据
                            UpLoadingWatchData.getInstance(activity).uploadingWatchData("Single_motion_results", text, 0);//上传单次数据
                            Intent intent = new Intent(activity, WatchSportSummaryActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void setNegativeButton() {
                            waitDialog.setMessage("正在解绑设备...");
                            sendToBule(WatchBlueTestActivity.bangding(false, UID), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                            myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("jiechubangding", 15000);
                        }
                    });
                } else {
                    waitDialog.setMessage("正在解绑设备...");
                    sendToBule(WatchBlueTestActivity.bangding(false, UID), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("jiechubangding", 15000);
                }
                break;
            case "02"://
                 /*   if (text.startsWith("14")) {
                        activity_mydevice_bangding.setClickable(true);
                        return;
                    }*/
                mySQLiteDataDao.deleteall();
                if (text.substring(4, 6).equals("00")) {
                    ShareUitls.putString(MyDevicesActivity.this, "WATCHSPORT", "");
                    ShareUitls.putString(MyDevicesActivity.this, "isConnectActivity", "");

                    updatekMac(true);
                } else {
                    MyToash.Log(DataTransferUtils.getString_10(text.substring(6, 38)));
                    if (text.equals("0214010000000000000000000000000000000017")) {//已经处于解除绑定了
                        //ShareUitls.putString(MyDevicesActivity.this, "WATCHSPORT", "");
                        updatekMac(true);
                    } else {
                        MyToash.Toash(activity, "解绑失败");
                        waitDialog.setMessage("解绑失败.");
                        waitDialog.dismissDialog();
                    }
                }
                break;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            activity_mydevice_layout.setClickable(true);
            myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("");//收到信息了都取消计十秒计时
            Log.i("myblue", "设备可读可写");
            String text = msg.obj.toString();
            Log.i("myblue", text);
            if (text.length() > 5) {
                String head = text.substring(0, 2);
                HandlerData(head, text);
            }

        }
    };

    private void myWatchBlueHandler() {
        myWatchBlueHandler = MyWatchBlueHandler.getInstance(new MyWatchBlueHandler.MyWatchBlueHandlerListener() {
            @Override
            public void strikeDelayed(String flag) {
                activity_mydevice_layout.setClickable(true);
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
