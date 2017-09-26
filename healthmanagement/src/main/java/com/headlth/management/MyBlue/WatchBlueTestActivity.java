package com.headlth.management.MyBlue;

import android.app.Activity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;


import android.os.Bundle;
import android.os.Handler;

import android.os.Message;
import android.util.Log;

import android.view.View;

import android.widget.TextView;
import android.widget.Toast;


import com.headlth.management.R;
import com.headlth.management.clenderutil.WaitDialog;
import com.headlth.management.entity.User;
import com.headlth.management.myview.MyToash;
import com.headlth.management.myview.PubLicDialog;
import com.headlth.management.utils.DataTransferUtils;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.VersonUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by abc on 2017/5/2.
 */
@ContentView(R.layout.activity_watchbluetest)
public class WatchBlueTestActivity extends Activity {

    @ViewInject(R.id.show)
    TextView Show;

    private SimpleDateFormat df;
    boolean result = false;


    byte[] usersny = new byte[19];

    private MyBuleWatchManager myBuleConnectManager;
    private boolean read, write;
    private User.UserInformation userInformation;
    private MyBuleSearchManager myBuleSerachManager;
    private BluetoothGattCharacteristic READE_BluetoothGattCharacteristic;
    private BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristic;
    private BluetoothGatt mBluetoothGatt;//发现蓝牙服务，根据特征值处理数据交互
    private String MAC = "";
    private WaitDialog waitDialog;
    private String version;
    int starttime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        MAC = "D6:48:63:F7:97:79";getIntent().getStringExtra("MAC");
        if (!MAC.equals("") && MAC != null) {
            Log.i("myblue", MAC);
            initialize();
        }

        Show = (TextView) findViewById(R.id.show);
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    }

    private void initialize() {
        version = VersonUtils.getVersionName(WatchBlueTestActivity.this);//&version=" + version
        //  flag = getIntent().getStringExtra("flag");
        //  view_publictitle_title.setText("连接腕表");
        userInformation = ShareUitls.getUser(WatchBlueTestActivity.this).getUserInformation();
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(WatchBlueTestActivity.this);
        waitDialog.setCancleable(true);
        waitDialog.setMessage("正在搜索腕表...");
        waitDialog.showDailog();

        if (true) {
            serachBule();
        } else {
            connectBule();
        }


        // ;


    }

    private void serachBule() {
        myBuleSerachManager = MyBuleSearchManager.getInstance(WatchBlueTestActivity.this, 0, new MyBuleSearchManager.LeScanCallbackListener() {
            @Override
            public void getBluetoothDeviceList(List<MyBuleSearchManager.BluetoothDeviceEntity> bluetoothDevices) {
            }

            @Override
            public void getBluetoothDevice(MyBuleSearchManager.BluetoothDeviceEntity bluetoothDevice) {
                if (bluetoothDevice == null) {
                    return;//监听未触发
                } else {
                    if (bluetoothDevice.rssi == 1111) {//搜索八秒也未搜索到目标设备
                        Nosearchtothetargetdevice(myBuleSerachManager, WatchBlueTestActivity.this, true);
                        return;
                    }
                    if (bluetoothDevice.device.getAddress() != null && bluetoothDevice.device.getName() != null) {
                        if (bluetoothDevice.device.getAddress().equals(MAC)) {
                            myBuleSerachManager.endSearch();
                            connectBule();
                            return;
                        }

                    }

                }
            }
        });
    }

    private void connectBule() {
        waitDialog.setMessage("正在连接腕表...");
        myBuleConnectManager = MyBuleWatchManager.getInstance(WatchBlueTestActivity.this, MAC,  new MyBuleWatchManager.OnCharacteristicListener() {
            @Override
            public void onServicesDiscovered(BluetoothGatt bluetoothGatt,BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristi) {
                WRITE_BluetoothGattCharacteristic = WRITE_BluetoothGattCharacteristi;
                waitDialog.setMessage("设备已经连接...");
                waitDialog.dismissDialog();
                mBluetoothGatt = myBuleConnectManager.getBluetoothGatt();
                Show.setText("设备可读可写");
            }


            @Override
            public void onCharacteristicChanged(byte[] data) {
                //     Log.i("myblue", "数据通知");
                String text = DataTransferUtils.format(data);
                Message message = Message.obtain();
                message.obj = text;
                message.what = 0;
                instructHandler.sendMessage(message);

            }

        });
    }

    String instructType = "";
    String show;

    @Event(value = {R.id.watch_bangding
            , R.id.watch_canclebangding
            , R.id.time_synchronization
            , R.id.name_synchronization
            , R.id.name_synchronization2
            ,
            R.id.Personal_information_synchronization
            , R.id.weather_synchronization
            , R.id.sportparameter_synchronization
            , R.id.samplingparameter_synchronization
            , R.id.temperaturee_synchronization
            ,
            R.id.Summary_of_the_day
            , R.id.Single_motion_results
            , R.id.Historical_static_heart_rate
            , R.id.Current_exercise_real_time_location_heart_rate_data
            , R.id.original_data

            , R.id.The_current_campaign_details_data
            , R.id.sleep_synchronization
            , R.id.blood_synchronization
            ,
            R.id.start
            , R.id.stop
            , R.id.pause
            , R.id.recover
            , R.id.GetWatchSportState

    })
    private void getEvent(View view) {
        byte[] bytes = new byte[4];
        String str = "";
        boolean flag = false;
        switch (view.getId()) {
            case R.id.watch_bangding://绑定 0x01
                instructType = "watch_bangding";
                flag = true;
                String UID = ShareUitls.getString(WatchBlueTestActivity.this, "UID", "0");
                sendToBule(bangding(true, UID), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                break;
            case R.id.watch_canclebangding://解除 0x02
                flag = true;
                instructType = "watch_canclebangding";
                String UIDD = ShareUitls.getString(WatchBlueTestActivity.this, "UID", "0");
                sendToBule(bangding(false, UIDD), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                break;
            case R.id.time_synchronization://时间 0x05
                flag = true;

                instructType = "time_synchronization";
                sendToBule(snycDataTime(), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                break;
            case R.id.name_synchronization:// 0x04姓名
                flag = true;
                instructType = "name_synchronization";
                String name = userInformation.getNickName();
                sendToBule(snycDataName(userInformation.getNickName(), true), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
            case R.id.name_synchronization2:// 0x04姓名
                flag = true;
                instructType = "name_synchronization2";
                String namee = userInformation.getNickName();
                sendToBule(snycDataName(userInformation.getNickName(), false), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);

                break;
            case R.id.Personal_information_synchronization://个人信息同步 0x03
                instructType = "Personal_information_synchronization";
                flag = true;
                sendToBule(snycDataInformation(userInformation), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                break;
            case R.id.weather_synchronization://天气 0x01
                flag = true;
                instructType = "weather_synchronization";
                sendToBule(snycDataWeathere("多云"), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                break;
            case R.id.temperaturee_synchronization://气温 0x01
                flag = true;
                instructType = "temperaturee_synchronization";
                byte[] temperaturee = {10, 30, -10};
                sendToBule(snycDataTemperaturee(""), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);

                break;
            case R.id.sportparameter_synchronization://运动参数 0x01
                flag = true;
                instructType = "sportparameter_synchronization";

                sendToBule(snycDataSportparameter(userInformation), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);


                break;
            case R.id.samplingparameter_synchronization://采样参数 0x01
                flag = true;
                instructType = "samplingparameter_synchronization";

                break;

            case R.id.Summary_of_the_day://当天运动总结 0x06
                instructType = "Summary_of_the_day";
                bytes[0] = 6;
                bytes[1] = 4;
                bytes[2] = 1;
                bytes[3] = 11;
                break;
            case R.id.Single_motion_results://单次运动结果0x08
                instructType = "Single_motion_results";
                str = "04040109";

                bytes[0] = 8;
                bytes[1] = 4;
                bytes[2] = 1;
                bytes[3] = 13;
                break;
            case R.id.original_data://单次原始数据09

                flag = true;
                instructType = "original_data";
                if (starttime == 0) {
                    Toast.makeText(WatchBlueTestActivity.this, "请先查询单次运动结果", Toast.LENGTH_LONG).show();
                } else {
                    MyToash.Log("starttime=     " + starttime + "");
                    sendToBule(snycDataOriginal_data(starttime), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                }
                break;
            case R.id.Historical_static_heart_rate://历史静态心率 0x0C
                instructType = "Historical_static_heart_rate";
                bytes[0] = 0x0C;
                bytes[1] = 4;
                bytes[2] = 1;
                bytes[3] = 11;
                break;

            case R.id.Current_exercise_real_time_location_heart_rate_data://当前运动时时心率数据0x0B
                instructType = "Current_exercise_real_time_location_heart_rate_data";
                bytes[0] = 0x0b;
                bytes[1] = 0x04;
                bytes[2] = 0x01;
                bytes[3] = 0x0f;
                break;


            case R.id.The_current_campaign_details_data://当前运动时时数据详情 0x0A
                instructType = "The_current_campaign_details_data";
                bytes[0] = 0x0A;
                bytes[1] = 0x04;
                bytes[2] = 0x01;
                bytes[3] = 16;
                break;
            case R.id.sleep_synchronization://睡眠时间
                instructType = "sleep_synchronization";
                bytes[0] = 10;
                bytes[1] = 0x04;
                bytes[2] = 0x01;
                bytes[3] = 0x0A;
                break;
            case R.id.blood_synchronization://血压 0x0D
                instructType = "blood_synchronization";
                bytes[0] = 0x0D;
                bytes[1] = 0x04;
                bytes[2] = 0x01;
                bytes[3] = 0x0A;
                break;

            case R.id.start://开始 0x05
                instructType = "start";
                bytes[0] = 0x05;
                bytes[1] = 0x04;
                bytes[2] = 0x01;
                bytes[3] = 0x0A;
                break;
            case R.id.pause://暂停 0x05
                instructType = "pause";
                bytes[0] = 0x05;
                bytes[1] = 0x04;
                bytes[2] = 0x01;
                bytes[3] = 0x0A;
                break;
            case R.id.recover://恢复 0x05
                instructType = "recover";
                bytes[0] = 0x05;
                bytes[1] = 0x04;
                bytes[2] = 0x01;
                bytes[3] = 0x0A;
                break;
            case R.id.stop://停止0x05
                instructType = "stop";
                bytes[0] = 0x05;
                bytes[1] = 0x04;
                bytes[2] = 0x01;
                bytes[3] = 0x0A;
                break;
            case R.id.GetWatchSportState://停止0x05
                instructType = "GetWatchSportState";
                bytes = getWatchBuleData("GetWatchSportState");
                break;
            default:
                break;

        }

        if (!flag) {
            Log.i("myblue", instructType);
            sendToBule(bytes, WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
        }
    }

    boolean nameFrontOrBehind;
    Handler instructHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //bluetoothGatt.readCharacteristic(READE_BluetoothGattCharacteristic);

            } else {
                String text = msg.obj.toString();
                if (text.startsWith("14")) {
                    return;
                }
                //  text = text.replace(" ", "").replace("00000:", "");
                Log.i("myblue", text + "     " + text.length());
                switch (instructType) {

                    case "watch_bangding":
                        if (text.substring(4, 6).equals("00")) {
                            show = "绑定成功";
                        } else {
                            show = "绑定失败";
                        }
                        break;
                    case "watch_canclebangding":
                        if (text.substring(4, 6).equals("00")) {
                            show = "解绑成功";
                        } else {
                            show = "解绑失败";
                        }
                        break;
                    case "Personal_information_synchronization":
                        if (text.substring(4, 6).equals("00")) {
                            show = "个人信息同步成功";
                        } else {
                            show = "个人信息同步失败";
                        }
                        break;
                    case "name_synchronization"://时间同步
                        if (text.substring(4, 6).equals("00")) {
                            show = "姓名同步成功1";

                        } else {
                            show = "姓名同步失败1";
                        }
                        break;

                    case "name_synchronization2"://时间同步
                        if (text.substring(4, 6).equals("00")) {
                            show = "姓名同步成功2";

                        } else {
                            show = "姓名同步失败2";
                        }
                        break;


                    case "time_synchronization"://时间同步
                        if (text.substring(4, 6).equals("00")) {
                            show = "时间同步成功";
                        } else {
                            show = "时间同步失败";
                        }
                        break;

                    case "weather_synchronization"://天气同步
                        if (text.substring(4, 6).equals("00")) {
                            show = "天气同步成功";
                        } else {
                            show = "天气同步失败";
                        }
                        break;
                    case "temperaturee_synchronization"://气温同步
                        if (text.substring(4, 6).equals("00")) {
                            show = "气温同步成功";
                        } else {
                            show = "气温同步失败";
                        }
                        break;

                    case "sportparameter_synchronization"://运动参数同步
                        if (text.substring(4, 6).equals("00")) {
                            show = "运动参数同步成功";
                        } else {
                            show = "运动参数同步失败";
                        }
                        break;
                    case "samplingparameter_synchronization"://采样参数同步
                        if (text.substring(4, 6).equals("00")) {
                            show = "采样参数同步成功";
                        } else {
                            show = "采样参数同步失败";
                        }
                        break;


                    case "Summary_of_the_day":
                        show = "运动总时间： " + DataTransferUtils.getInt_10(text.substring(4, 8)) + "\n" +
                                "有效运动时间： " + DataTransferUtils.getInt_10(text.substring(8, 12)) + "\n" +
                                "最大心率： " + DataTransferUtils.getInt_10(text.substring(12, 14)) + "\n" +
                                "平均心率： " + DataTransferUtils.getInt_10(text.substring(14, 16)) + "\n" +
                                "消耗热量： " + DataTransferUtils.getInt_10(text.substring(16, 20)) + "\n" +
                                "里程： " + DataTransferUtils.getInt_10(text.substring(20, 24)) + "\n" +
                                "步数： " + DataTransferUtils.getInt_10(text.substring(24, 32));
                        break;
                    case "Single_motion_results":
                        try {
                            starttime = DataTransferUtils.getInt_10(text.substring(4, 12));
                            show = "运动开始时间： " + DataTransferUtils.getDate(starttime) + "\n" +
                                    "运动总时间： " + DataTransferUtils.getInt_10(text.substring(12, 16)) + "\n" +
                                    "有效运动时间： " + DataTransferUtils.getInt_10(text.substring(16, 20)) + "\n" +
                                    "最大心率： " + DataTransferUtils.getInt_10(text.substring(20, 22)) + "\n" +
                                    "平均心率： " + DataTransferUtils.getInt_10(text.substring(22, 24)) + "\n" +
                                    "消耗热量： " + DataTransferUtils.getInt_10(text.substring(24, 28)) + "\n" +
                                    "里程： " + DataTransferUtils.getInt_10(text.substring(28, 32)) + "\n" +
                                    "步数： " + DataTransferUtils.getInt_10(text.substring(32, 38));
                        } catch (Exception e) {
                            MyToash.Log(text);
                        }

                        break;
                    case "original_data":
                        MyToash.Log(text + "   " + text.substring(4, 12) + "   " + DataTransferUtils.getInt_10(text.substring(4, 12)));
                        show = "采样时间： " + DataTransferUtils.getDate(DataTransferUtils.getInt_10(text.substring(4, 12))) + "\n" +
                                "心率值： " + DataTransferUtils.getInt_10(text.substring(12, 14)) + "\n" +
                                "经度： " + DataTransferUtils.getInt_10(text.substring(14, 22)) + "\n" +
                                "纬度： " + DataTransferUtils.getInt_10(text.substring(22, 30)) + "\n" +
                                "步数： " + DataTransferUtils.getInt_10(text.substring(30, 38));

                        break;

                    case "The_current_campaign_details_data":
                        show = "运动开始时间： " + DataTransferUtils.getDate(DataTransferUtils.getInt_10(text.substring(4, 12))) + "\n" +
                                "运动总时间： " + DataTransferUtils.getInt_10(text.substring(12, 16)) + "\n" +
                                "有效运动时间： " + DataTransferUtils.getInt_10(text.substring(16, 20)) + "\n" +
                                "最大心率： " + DataTransferUtils.getInt_10(text.substring(20, 22)) + "\n" +
                                "平均心率： " + DataTransferUtils.getInt_10(text.substring(22, 24)) + "\n" +
                                "消耗热量： " + DataTransferUtils.getInt_10(text.substring(24, 28)) + "\n" +
                                "里程： " + DataTransferUtils.getInt_10(text.substring(28, 32)) + "\n" +
                                "步数： " + DataTransferUtils.getInt_10(text.substring(32, 38));
                        break;
                    case "Current_exercise_real_time_location_heart_rate_data":
                        show = "采样时间： " + DataTransferUtils.getDate(DataTransferUtils.getInt_10(text.substring(4, 12))) + "\n" +
                                "心率值： " + DataTransferUtils.getInt_10(text.substring(12, 14)) + "\n" +
                                "经度： " + DataTransferUtils.getInt_10(text.substring(14, 22)) + "\n" +
                                "纬度： " + DataTransferUtils.getInt_10(text.substring(22, 30)) + "\n";
                        break;
                    case "Historical_static_heart_rate":
                        int temp = DataTransferUtils.getInt_10(text.substring(4, 12));
                        show = "采样时间： " + DataTransferUtils.getDate(temp) + "\n" + "心率值： " + DataTransferUtils.getInt_10(text.substring(12, 14)) + "\n";
                        break;
                    case "sleep_synchronization":
                        show = "睡眠开始时间： " + DataTransferUtils.getDate(DataTransferUtils.getInt_10(text.substring(4, 12))) + "\n"
                                + "睡眠总时间： " + DataTransferUtils.getInt_10(text.substring(12, 14)) + "\n"
                                + "深睡眠时长： " + DataTransferUtils.getInt_10(text.substring(14, 22)) + "\n"
                                + "清醒时长： " + DataTransferUtils.getInt_10(text.substring(22, 30)) + "\n";
                        break;
                    case "blood_synchronization":
                        show = "采样时间： " + DataTransferUtils.getDate(DataTransferUtils.getInt_10(text.substring(4, 12))) + "\n"
                                + "心率值： " + DataTransferUtils.getInt_10(text.substring(12, 14)) + "\n"
                                + "高压： " + DataTransferUtils.getInt_10(text.substring(14, 18)) + "\n"
                                + "低压： " + DataTransferUtils.getInt_10(text.substring(18, 22)) + "\n";
                        break;
                    case "GetWatchSportState":
                        show = text;
                        break;


                }


                Show.setText(show);

            }
        }
    };

    public static byte[] bangding(boolean isbangding, String UID) {
        byte[] bytes = new byte[19];
        for (int i = 0; i < 19; i++) {//初始化全部为0
            bytes[i] = 0;
        }
        if (isbangding)
            bytes[0] = 1;
        else
            bytes[0] = 2;
        bytes[1] = 19;
        String str = "";
        for (int i = 0; i < UID.length(); i++) {
            byte asc = (byte) UID.charAt(i);//获取字符的Ascii码
            bytes[i + 2] = asc;
            str += asc;
        }
        byte sum = 0;

        for (int i = 0; i < bytes.length; i++) {
            Log.i("myblue", bytes[i] + "");
            sum += bytes[i];
        }
        bytes[18] = sum;
        Log.i("myblue", str + "");
        return bytes;
    }

    static int count;

    public static byte[] snycDataName(String name, boolean isup) {
        byte[] bytes = new byte[20];
        for (int i = 0; i < 20; i++) {//初始化全部为0
            bytes[i] = 0;
        }
        bytes[0] = 04;
        bytes[1] = 20;// Byte.parseByte(Integer.toHexString(20) + "");
        if (isup) {//前八个数字
            bytes[2] = 00;
        } else {//后八个
            bytes[2] = 01;
        }
        //MyToash.Log("name=    " + name + " " + name.length() + "  ");
        String namee = "";
        if (name.length() > 8) {
            if (isup) {//前八个数字
                namee = DataTransferUtils.string2Unicode(name.substring(0, 8));
            } else {
                namee = DataTransferUtils.string2Unicode(name.substring(8, name.length()));
            }
        } else {
            namee = DataTransferUtils.string2Unicode(name);
        }
       // MyToash.Log("namee=    " + namee + " " + namee.length() + "  ");


        byte[] hexStr2ByteArray = DataTransferUtils.hexStr2ByteArray(namee);
      //  Log.i("name=    ", hexStr2ByteArray.length + "");
        for (int i = 0; i < hexStr2ByteArray.length; i++) {
            bytes[i + 3] = hexStr2ByteArray[i];
        }
        byte sum = 0;
        for (int i = 0; i < bytes.length; i++) {
            //Log.i("myblue", bytes[i] + "");
            sum += bytes[i];
        }
        bytes[19] = sum;
        return bytes;
    }

    public static byte[] snycDataWeathere(String Weathere) {
        byte[] bytes = new byte[20];
        for (int i = 0; i < 20; i++) {//初始化全部为0
            bytes[i] = 0;
        }
        StringBuffer strFirst = new StringBuffer();
        strFirst.append("0e");
        strFirst.append(Integer.toHexString(20));
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(d);
        Log.i("snycDataWeathere111", date);

        String tempdate = Integer.toHexString(Integer.parseInt(date.substring(2, 8)));
        switch (tempdate.length()) {
            case 1:
                tempdate = "00000" + tempdate;
                break;
            case 2:
                tempdate = "0000" + tempdate;
                break;

            case 3:
                tempdate = "000" + tempdate;
                break;
            case 4:
                tempdate = "00" + tempdate;
                break;
            case 5:
                tempdate = "0" + tempdate;
                break;

        }
        strFirst.append(tempdate);

        strFirst.append(Weathere);

        byte[] hexStr2ByteArray = DataTransferUtils.hexStr2ByteArray(strFirst.toString());
        byte sum = 0;
        for (int i = 0; i < (19 > hexStr2ByteArray.length ? hexStr2ByteArray.length : 19); i++) {
            bytes[i] = hexStr2ByteArray[i];
            sum += bytes[i];
        }
        bytes[19] = sum;

        return bytes;
    }

    public static byte[] snycDataTemperaturee(String temperaturee) {
        byte[] bytes = new byte[9];
        for (int i = 0; i < 9; i++) {//初始化全部为0
            bytes[i] = 0;
        }
        StringBuffer strFirst = new StringBuffer();
        strFirst.append("0f");
        strFirst.append("09");
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(d);//Integer.parseInt(date.substring(2, 8))
        String tempdate = Integer.toHexString(Integer.parseInt(date.substring(2, 8)));
        switch (tempdate.length()) {
            case 1:
                tempdate = "00000" + tempdate;
                break;
            case 2:
                tempdate = "0000" + tempdate;
                break;

            case 3:
                tempdate = "000" + tempdate;
                break;
            case 4:
                tempdate = "00" + tempdate;
                break;
            case 5:
                tempdate = "0" + tempdate;
                break;

        }
        // String tempdate2 = Integer.toHexString(991231);
        strFirst.append(tempdate);
        strFirst.append(temperaturee);
        byte[] hexStr2ByteArray = DataTransferUtils.hexStr2ByteArray(strFirst.toString());
        byte sum = 0;
        for (int i = 0; i < (8 > hexStr2ByteArray.length ? hexStr2ByteArray.length : 8); i++) {
            bytes[i] = hexStr2ByteArray[i];
            sum += bytes[i];
        }
        bytes[8] = sum;
        return bytes;
    }

    public static byte[] snycDataTime() {
        byte[] calendar2Bytes = DataTransferUtils.calendar2Bytes();
        byte[] bytes = new byte[7];
        bytes[0] = 05;
        bytes[1] = 07;
        for (int i = 0; i < calendar2Bytes.length; i++) {
            bytes[i + 2] = calendar2Bytes[i];
        }
        byte sum = 0;
        for (int i = 0; i < bytes.length; i++) {
            Log.i("myblue", bytes[i] + "");
            sum += bytes[i];
        }
        bytes[6] = sum;
        return bytes;
    }

    public static byte[] snycDataInformation(User.UserInformation userInformation) {


        //  byte[] usersny = new byte[8];
        byte[] bytes = new byte[8];
        for (int i = 0; i < 8; i++) {//初始化全部为0
            bytes[i] = 0;
        }
        bytes[0] = 03;
        bytes[1] = 8;
        bytes[2] = (byte) Integer.parseInt(userInformation.getHeight());
        String weightt[] = userInformation.getWeight().split("\\.");
        bytes[3] = (byte) Integer.parseInt(weightt[0]);
        if (weightt.length == 2) {
            bytes[4] = (byte) Integer.parseInt(weightt[1]);
        } else {
            bytes[4] = 00;
        }

        bytes[5] = (byte) User.getAge(userInformation.getBirthday());
        String sex = userInformation.getGender();
        if (sex.equals("1")) {
            bytes[6] = 01;
        } else {
            bytes[6] = 00;
        }
        byte sum = 0;
        for (int i = 0; i < bytes.length; i++) {
            sum += bytes[i];

        }
        bytes[7] = sum;




       /* // String namee = userInformation.getNickName();
        String agee = User.getAge(userInformation.getBirthday()) + "";
        String heightt = userInformation.getHeight();
        String weightt [] = userInformation.getWeight().split("\\.");
        String sex = userInformation.getGender();
        if (sex.equals("1")) {
            sex = "01";
        } else {
            sex = "00";
        }
        if (!agee.matches("[0-9]+") || !heightt.matches("[0-9]+")||weightt.length>2) {
            return usersny;
        }
        if (sex.equals("1")) {
            sex = "01";
        } else {
            sex = "00";
        }
        MyToash.Log( " " + agee + " " + heightt + " " + weightt + "  " + sex);
        StringBuffer strFirst = new StringBuffer();
        strFirst.append("0308");
        String Stringheightt = Integer.toHexString(Integer.parseInt(heightt));
        String Stringweightt = Integer.toHexString(Integer.parseInt(weightt[0]));
        String Stringweightt2="00";
        if(weightt.length==2){
             Stringweightt2 = Integer.toHexString(Integer.parseInt(weightt[1]));
        }
        String Stringagee = Integer.toHexString(Integer.parseInt(agee));
        strFirst.append(Stringheightt.length() == 1 ? "0" + Stringheightt : Stringheightt);
        strFirst.append(Stringweightt.length() == 1 ? "0" + Stringweightt : Stringweightt);
        strFirst.append(Stringweightt2.length() == 1 ? "0" + Stringweightt2 : Stringweightt2);
        strFirst.append(Stringagee.length() == 1 ? "0" + Stringagee : Stringagee);
        strFirst.append(sex);
        byte[] hexStr2ByteArray = DataTransferUtils.hexStr2ByteArray(strFirst.toString());
        byte sum = 0;
        for (int i = 0; i < hexStr2ByteArray.length; i++) {
            sum += hexStr2ByteArray[i];
            usersny[i] = hexStr2ByteArray[i];
        }
        usersny[7] = sum;*/
        return bytes;

    }

    //运动参数同步
    public static byte[] snycDataSportparameter(User.UserInformation userInformation) {
        //  MyToash.Log(userInformation.getWatchDuration()+"  "userInformation.getUBound()+"  "+"  ");
        byte[] WatchDuration = DataTransferUtils.get4Bytes(Integer.parseInt(userInformation.getWatchDuration()), 4);

        byte[] bytes = new byte[9];
        for (int i = 0; i < 9; i++) {//初始化全部为0
            bytes[i] = 0;
        }
        bytes[0] = 0x11;
        bytes[1] = 9;
        for (int i = 0; i < WatchDuration.length; i++) {
            bytes[i + 2] = WatchDuration[i];
        }
        bytes[6] = (byte)(Integer.parseInt( userInformation.getUBound())); //hexStr2ByteArray[0];
        bytes[7] = (byte) (Integer.parseInt( userInformation.getLBound()));//hexStr2ByteArray[1];

        byte sum = 0;
        for (int i = 0; i < bytes.length; i++) {
            sum += bytes[i];
        }
        bytes[8] = sum;
        return bytes;
    }

    //采样参数同步samplingparameter_synchronization
    public static byte[] snycDataSamplingparameter(byte[] parameters) {

        byte[] bytes = new byte[11];
        for (int i = 0; i < 11; i++) {//初始化全部为0
            bytes[i] = 0;
        }
   /*     bytes[0] = 18;
        bytes[1] = 0x0B;*/
        for (int i = 0; i < parameters.length; i++) {//初始化全部为0
            bytes[i] = parameters[i];
        }
        byte sum = 0;
        for (int i = 0; i < bytes.length; i++) {
            Log.i("myblue", bytes[i] + "");
            sum += bytes[i];
        }
        bytes[10] = sum;
        return bytes;
    }


    //请求单次运动原始数据
    public static byte[] snycDataOriginal_data(int starttime) {
        byte[] calendar2Bytes = DataTransferUtils.get4Bytes(starttime, 4);
        byte[] bytes = new byte[7];
        bytes[0] = 9;
        bytes[1] = 7;
        for (int i = 0; i < calendar2Bytes.length; i++) {
            bytes[i + 2] = calendar2Bytes[i];
        }
        byte sum = 0;
        for (int i = 0; i < bytes.length; i++) {
            //   Log.i("myblue", bytes[i] + "");
            sum += bytes[i];
        }
        bytes[6] = sum;
        return bytes;
    }


    public static byte[] getWatchBuleData(String flag) {
        byte[] bytes = new byte[4];
        switch (flag) {
            case "Single_motion_results":
                bytes[0] = 8;
                bytes[1] = 4;
                bytes[2] = 1;
                bytes[3] = 13;
                break;
            case "Original_data":

                break;
            case "Summary_of_the_day":
                bytes[0] = 6;
                bytes[1] = 4;
                bytes[2] = 1;
                bytes[3] = 11;
                break;
            case "Historical_static_heart_rate":
                bytes[0] = 12;
                bytes[1] = 4;
                bytes[2] = 1;
                bytes[3] = 17;
                break;
            case "PostBloodPressureRequest":
                bytes[0] = 13;
                bytes[1] = 4;
                bytes[2] = 1;
                bytes[3] = 18;
                break;

            case "PostSleepInfoRequest":
                bytes[0] = 16;
                bytes[1] = 4;
                bytes[2] = 1;
                bytes[3] = 21;
                break;

            case "GetWatchSportState":
                bytes[0] = 19;
                bytes[1] = 4;
                bytes[2] = 1;
                bytes[3] = 24;
                break;

            case "WatchSportStateChange":
                bytes[0] = 20;
                bytes[1] = 4;
                bytes[2] = 00;
                bytes[3] = 24;
                break;
        }

        return bytes;
    }


    public static void sendToBule(byte[] bytes, BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristic, BluetoothGatt mBluetoothGatt) {
        if (bytes != null && bytes.length > 0) {
            WRITE_BluetoothGattCharacteristic.setValue(bytes);
            mBluetoothGatt.writeCharacteristic(WRITE_BluetoothGattCharacteristic);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBuleConnectManager != null) {
            myBuleConnectManager.endConnect();
        }
        if (myBuleSerachManager != null) {
            myBuleSerachManager.endSearch();//停止搜索
        }
    }

    public static void Nosearchtothetargetdevice(final MyBuleSearchManager myBuleSerachManager, final Activity activity, final boolean finish) {
        PubLicDialog.showNotDialog2(activity, new String[]{"搜索超时:", "未搜索到目标设备是否重新搜索?", "重新搜索", "退出搜索"}, new PubLicDialog.PubLicDialogOnClickListener2() {
            @Override
            public void setPositiveButton() {
                myBuleSerachManager.StartScan();
            }

            @Override
            public void setNegativeButton() {
                myBuleSerachManager.endSearch();//停止搜索
                if (finish) {
                    activity.finish();
                }
            }
        });
    }
}
