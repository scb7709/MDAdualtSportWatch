package com.headlth.management.activity;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.headlth.management.MyBlue.MyBuleSearchManager;
import com.headlth.management.MyBlue.MyBuleWatchManager;
import com.headlth.management.MyBlue.MyWatchBlueHandler;
import com.headlth.management.MyBlue.WatchBlueTestActivity;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.clenderutil.WaitDialog;
import com.headlth.management.entity.PostParameterRequest;
import com.headlth.management.entity.PublicDataClass;
import com.headlth.management.entity.TemperatureeAndWeathere;
import com.headlth.management.entity.User;
import com.headlth.management.myview.MyToash;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DataTransferUtils;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ImageUtil;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.VersonUtils;
import com.headlth.management.watchdatasqlite.MySQLiteBaseClass;
import com.headlth.management.watchdatasqlite.MySQLiteDataDao;
import com.headlth.management.watchdatasqlite.UpLoadingWatchData;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by abc on 2017/7/24.
 */
@ContentView(R.layout.activity_connectblue)
public class ConnectBlueActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;
    @ViewInject(R.id.activity_connectblue_center)
    private TextView activity_connectblue_center;

    @ViewInject(R.id.activity_connectblue_remind)
    private TextView activity_connectblue_remind;

    private User.UserInformation userInformation;
    private MyBuleSearchManager myBuleSerachManager;
    private WaitDialog waitDialog;
    //    private MyBuleSearchManager myBuleSerachManager;
    private MyBuleWatchManager myBuleWatchManager;

    //  private BluetoothGattCharacteristic READE_BluetoothGattCharacteristic;
    private BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristic;
    private BluetoothGatt mBluetoothGatt;//发现蓝牙服务，根据特征值处理数据交互
    private String MAC = "";
    private String version;

    private String flag = "";
    private PopupWindow success;
    //  private String instructType;
    private String UID;
    //   private boolean isfirstConnect = true, ISOVER;
    private TemperatureeAndWeathere temperatureeAndWeathere;
    private PostParameterRequest postParameterRequest;
    private Activity activity;
    private MyWatchBlueHandler myWatchBlueHandler;//发出的蓝牙指令10秒内收不到回复的处理类


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        view_publictitle_back.setVisibility(View.GONE);
        activity = this;
        MAC = getIntent().getStringExtra("MAC");
        if (!MAC.equals("") && MAC != null) {
            Log.i("myblue", MAC);
            initialize();
        }
    }

    private void initialize() {
        myWatchBlueHandler();
        UID = ShareUitls.getString(ConnectBlueActivity.this, "UID", "0");
        version = VersonUtils.getVersionName(ConnectBlueActivity.this);
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        temperatureeAndWeathere = (TemperatureeAndWeathere) intent.getSerializableExtra("temperatureeAndWeathere");
        WeatherTemperatureSize = temperatureeAndWeathere.Data.size();
        MyToash.Log(WeatherTemperatureSize + "");
        // if (flag.equals("firstsport") || flag.equals("bangding")) {
        postParameterRequest = (PostParameterRequest) intent.getSerializableExtra("postParameterRequest");
        //  }
        view_publictitle_title.setText("连接腕表");
        userInformation = ShareUitls.getUser(ConnectBlueActivity.this).getUserInformation();
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(this, false);
        waitDialog.setMessage("正连接腕表...");
        waitDialog.showDailog();
        initializeDialog(this);
        serachBule();

    }


    private void serachBule() {
        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("connectBule", 30000);
        myBuleSerachManager = MyBuleSearchManager.getInstance(ConnectBlueActivity.this, 0, new MyBuleSearchManager.LeScanCallbackListener() {
            @Override
            public void getBluetoothDeviceList(List<MyBuleSearchManager.BluetoothDeviceEntity> bluetoothDevices) {
            }

            @Override
            public void getBluetoothDevice(MyBuleSearchManager.BluetoothDeviceEntity bluetoothDevice) {
                if (bluetoothDevice == null) {
                    return;//监听未触发
                } else {
                    if (bluetoothDevice.rssi == 1111) {//搜索八秒也未搜索到目标设备
                        // waitDialog.dismissDialog();
                        WatchBlueTestActivity.Nosearchtothetargetdevice(myBuleSerachManager, ConnectBlueActivity.this, true);
                        return;
                    }
                    if (bluetoothDevice.device != null) {
                        if (bluetoothDevice.device.getAddress() != null && bluetoothDevice.device.getName() != null) {
                            if (bluetoothDevice.device.getAddress().equals(MAC)) {//搜索到目标设备
                                myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("", 0);
                                Log.i("myblue", "搜索到目标设备");
                                if (myBuleSerachManager != null) {
                                    myBuleSerachManager.endSearch();//停止搜索
                                    myBuleSerachManager = null;
                                }
                                waitDialog.setMessage("正在连接腕表设备");
                                if (flag.equals("firstsport") || flag.equals("bangding")) {
                                    checkMac(MAC);//开启绑定流程
                                } else {
                                    connectBule();    //开启连接同步数据
                                }
                                return;
                            }

                        }
                    }

                }
            }
        });
    }

    private void connectBule() {


        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("connectBule", 60000);
        myBuleWatchManager = MyBuleWatchManager.getInstance(ConnectBlueActivity.this, MAC, new MyBuleWatchManager.OnCharacteristicListener() {
            @Override
            public void onServicesDiscovered(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristi) {
                if (activity != null) {
                    WRITE_BluetoothGattCharacteristic = WRITE_BluetoothGattCharacteristi;
                    mBluetoothGatt = bluetoothGatt;
                    if (WRITE_BluetoothGattCharacteristic != null && mBluetoothGatt != null) {
                        //  mBluetoothGatt.requestMtu(512);
                        if (mBluetoothGatt != null) {
                            if ((flag.equals("firstsport") || flag.equals("bangding"))) {//第一次连接才发送绑定命令
                                waitDialog.setMessage("正在绑定设备");
                                sendToBule(WatchBlueTestActivity.bangding(true, UID));
                                myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("bangding", 20000);
                            } else {

                                waitDialog.setMessage("正在查询设备状态");
                                byte[] bytes = WatchBlueTestActivity.getWatchBuleData("GetWatchSportState");
                                sendToBule(bytes);
                                myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("GetWatchSportState");


                            }
                        }
                    }
                }
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                Log.i("myblue", "数据通知");
                // String value = MyBlueDataAnalysis.getBlueData(characteristic);
                // Log.i("myblue", "心率值=  " + value);
                if (activity != null) {
                    String text = DataTransferUtils.format(data);
                    Message message = Message.obtain();
                    message.obj = text;
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }

        });
    }


    @Event(value = {R.id.view_publictitle_back, R.id.activity_connectblue_remind
    })
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.activity_connectblue_remind:
                //   snycData();
                break;
        }


    }


    /**
     * 同步数据成功的提示框
     */
    private void initializeDialog(Activity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_bangdingwatch, null);
        success = new PopupWindow(view, ImageUtil.dp2px(activity, 300), ImageUtil.dp2px(activity, 300), true);
        ImageView ICON = (ImageView) view.findViewById(R.id.dialog_bangdingwatch_icon);
        ICON.setImageResource(R.mipmap.register_success);
        TextView TEXT = (TextView) view.findViewById(R.id.dialog_bangdingwatch_text);
        TEXT.setText("数据同步成功");
        RelativeLayout go = (RelativeLayout) view.findViewById(R.id.dialog_bangdingwatch);
        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                syncOver();
                success.dismiss();
                finish();
            }
        });
        success.setBackgroundDrawable(new ColorDrawable());
        success.setFocusable(false);
        success.setOutsideTouchable(false);
        success.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    success.dismiss();

                    return true;
                }
                return false;
            }
        });
    }

    private void syncOver() {

        if (!flag.equals("bangding")) {//是从首页连接腕表运动过来的
            Log.i("myblue", "bangding");
            ShareUitls.putString(ConnectBlueActivity.this, "WATCHSPORT", "START");
            ShareUitls.putString(ConnectBlueActivity.this, "isConnectActivity", "YES");
        } else {
          /*  if (myBuleWatchManager != null) {
                myBuleWatchManager.endConnect();
                WRITE_BluetoothGattCharacteristic = null;
                mBluetoothGatt = null;
            }*/
        }
    }

    private void showNotDialog() {
        waitDialog.dismissDialog();
        success.showAtLocation(new View(this), Gravity.CENTER, 0, 0);
    }

    public void sendToBule(byte[] bytes) {
        Log.i("myblue", (WRITE_BluetoothGattCharacteristic == null) + "  ==null   " + (mBluetoothGatt == null));
        if (bytes != null && bytes.length > 0 && WRITE_BluetoothGattCharacteristic != null && mBluetoothGatt != null) {
            WRITE_BluetoothGattCharacteristic.setValue(bytes);
            mBluetoothGatt.writeCharacteristic(WRITE_BluetoothGattCharacteristic);
        }
    }

    int connnectcount;//重连次数

    private void myWatchBlueHandler() {
        myWatchBlueHandler = MyWatchBlueHandler.getInstance(new MyWatchBlueHandler.MyWatchBlueHandlerListener() {
            @Override
            public void strikeDelayed(String flag) {
                switch (flag) {
                    case "connectBule":
                        MyToash.Toash(activity, "连接失败,请确认设备是否打开了蓝牙");
                        if (myBuleSerachManager != null) {
                            myBuleSerachManager.endSearch();//停止搜索
                            myBuleSerachManager = null;
                        }
                        if (myBuleWatchManager != null) {
                            myBuleWatchManager.endConnect();
                        }
                        finish();//091459b61d094a000000ff000000ff000000f48e
                        break;
                    //个人信息
                    case "bangding":
                        MyToash.Toash(activity, "绑定失败");
                        if (myBuleWatchManager != null) {
                            myBuleWatchManager.endConnect();
                        }
                        finish();
                        break;
                    //个人信息
                    case "GetWatchSportState":
                        if (connnectcount < 3) {//重连
                            myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("");
                            connnectcount++;
                            if (myBuleWatchManager != null) {
                                myBuleWatchManager.endConnect();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            connectBule();
                        } else {
                            waitDialog.setMessage("正在查询设备数据");
                            //查询设备是否有未上传的数据（单次运动数据）
                            byte[] bytes = WatchBlueTestActivity.getWatchBuleData("Single_motion_results");
                            sendToBule(bytes);
                            myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Single_motion_results");
                        }
                        break;

                    case "Single_motion_results":
                        waitDialog.setMessage("正在同步数据");
                        snycData("Personal_information_synchronization");//开启同步
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Personal_information_synchronization");
                        break;

                    //个人信息
                    case "Personal_information_synchronization":

                        // break;
                        //时间
                    case "time_synchronization":

                        // break;
                        //运动参数
                    case "sportparameter_synchronization":

                        //  break;
                        //名字
                    case "name_synchronization":
                        SnycFail("同步失败(连接异常)");
                        // show += "姓名同步失败...\n";
                        // activity_connectblue_remind.setText(show);
                        //snycData("weather_synchronization");
                        break;
                    //天气
                    case "weather_synchronization":
                        show += "天气同步失败...\n";
                        activity_connectblue_remind.setText(show);
                        sendToBule(WatchBlueTestActivity.snycDataTemperaturee(temperatureeAndWeathere.Data.get(0).Temperature, 0));
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("temperature_synchronization");
                        break;
                    //气温temperature_synchronization
                    case "temperature_synchronization":
                        show += "气温同步失败...\n";
                        activity_connectblue_remind.setText(show);
                        snycData("samplingparameter_synchronization");
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("samplingparameter_synchronization");


                    /*    if (!flag.equals("nofirstsport")) {
                            snycData("samplingparameter_synchronization");
                            myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("samplingparameter_synchronization");
                        } else {
                            SnycSuccess();
                        }*/
                        break;
                    //采样参数
                    case "samplingparameter_synchronization":
                        show += "采样参数同步失败...\n";
                        activity_connectblue_remind.setText(show);
                        if (!flag.equals("nofirstsport")) {
                            SnycFail("同步失败(连接异常)");
                        } else {
                            SnycSuccess();
                        }
                        break;
                }
            }
        });

    }


    private int synchronizationTemperatureCount;
    private int synchronizationWeatherCount;
    private int WeatherTemperatureSize;
    private int synchronizationNameCount;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {//有数据 跳转到运动小结
                waitDialog.dismissDialog();
                Intent intent = new Intent(ConnectBlueActivity.this, WatchSportSummaryActivity.class);
                //intent.putExtra("Single_motion_results", (String) msg.obj);
                startActivity(intent);
                finish();
            } else {
                myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("");//收到信息了都取消计十秒计时
                Log.i("myblue", "设备可读可写");
                String text = msg.obj.toString();
                Log.i("myblue", text);
                if (text.length() > 5) {
                    String head = text.substring(0, 2);
                    HandlerData(head, text);
                } else {

                }
            }
        }
    };

    String show = "";


    private void HandlerData(String head, final String text) {

        switch (head) {
            case "13"://单次运动结果查询
                if (text.substring(0, 6).equals("130400")) {//
                    waitDialog.setMessage("正在查询设备数据");
                    //查询设备是否有未上传的数据（单次运动数据）
                    byte[] bytes = WatchBlueTestActivity.getWatchBuleData("Single_motion_results");
                    sendToBule(bytes);
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Single_motion_results");
                } else {
                    show += "同步失败";
                    SnycFail("同步失败(腕表正处于运动模式中)");
                }
                break;
            case "08"://单次运动结果查询
                if (!text.contains("00000000000000000000000000000000")) {//包含非0数据 则证明有数据 提示并跳转到腕表运动小结同步数据
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("");
                    MySQLiteDataDao.getInstance(activity).insertSingleAndOriginal(new MySQLiteBaseClass.Single_Original(DataTransferUtils.getInt_10(text.substring(4, 12)) + "", text));//保存单次运动数据
                    UpLoadingWatchData.getInstance(activity).uploadingWatchData("Single_motion_results", text, 0);//上传单次数据
                    waitDialog.setMessage("您有未上传的数据");
                    Toast.makeText(ConnectBlueActivity.this, "检测到您有未上传的数据即将上传数据", Toast.LENGTH_LONG).show();
                    waitDialog.dismissDialog();
                    Intent intent = new Intent(ConnectBlueActivity.this, WatchSportSummaryActivity.class);
                    startActivity(intent);
                    finish();

                 /*
                    myBuleConnectManager.endConnect();//销毁单例 重新创建
                    Message message = Message.obtain();
                    message.obj = text;
                    message.what = 2;
                    handler.sendMessage(message);*/
                } else {
                    waitDialog.setMessage("正在同步数据");
                    snycData("Personal_information_synchronization");//开启同步
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Personal_information_synchronization");
                }
                break;
            case "01":
                myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("");
                if (text.substring(4, 6).equals("00")) {
                    show += "绑定成功...\n";
                    updatekMac(MAC);//上传蓝牙地址
                } else {
                    String str = "";//二次校验 判断是否存在 APP或者服务器端未绑定但是腕表处于绑定
                    for (int i = 0; i < UID.length(); i++) {
                        str += (int) (UID.charAt(i));//获取字符的Ascii码
                    }
                    if (DataTransferUtils.getString_10(text).contains(str)) {
                        show += "绑定成功...\n";
                        updatekMac(MAC);//上传蓝牙地址
                    } else {
                        show += "绑定失败...\n";
                        SnycFail("此腕表设备已被其他账户绑定");
                    }
                }

                break;
            case "03":
                Log.i("myblue", "个人信息同步完成");
                if (text.substring(4, 6).equals("00")) {
                    show += "个人信息同步成功...\n";
                    snycData("time_synchronization");
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("time_synchronization");
                } else {
                    show += "个人信息同步失败...\n";
                    SnycFail("同步失败(连接异常)");
                }

                break;
            case "05"://时间同步
                Log.i("myblue", "时间同步完成");
                if (text.substring(4, 6).equals("00")) {
                    show += "时间同步成功...\n";
                    if (!flag.equals("bangding")) {
                        snycData("sportparameter_synchronization");
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("sportparameter_synchronization");
                    } else {//绑定不同步运动参数
                        if (userInformation.getNickName().length() > 8) {
                            synchronizationNameCount = 0;
                        } else {
                            synchronizationNameCount = 1;
                        }
                        sendToBule(WatchBlueTestActivity.snycDataName(userInformation.getNickName(), true));
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("name_synchronization");
                    }
                } else {
                    show += "时间同步失败...\n";
                    SnycFail("同步失败(连接异常)");
                }

                break;

            case "11"://运动参数同步
                if (text.substring(4, 6).equals("00")) {
                    show += "运动参数同步成功...\n";
                    if (userInformation.getNickName().length() > 8) {
                        synchronizationNameCount = 0;
                    } else {
                        synchronizationNameCount = 1;
                    }
                    sendToBule(WatchBlueTestActivity.snycDataName(userInformation.getNickName(), true));
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("name_synchronization");
                } else {
                    show += "运动参数同步失败...\n";
                    SnycFail("同步失败(连接异常)");
                }


                break;

            case "04"://姓名同步
                Log.i("myblue", "姓名同步完成" + synchronizationNameCount);
                if (synchronizationNameCount == 0) {
                    synchronizationNameCount = 1;
                    sendToBule(WatchBlueTestActivity.snycDataName(userInformation.getNickName(), false));
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("name_synchronization");
                } else {
                    if (text.substring(4, 6).equals("00")) {
                        show += "姓名同步成功...\n";
                        sendToBule(WatchBlueTestActivity.snycDataWeathere(temperatureeAndWeathere.Data.get(0).WeatherDetails, 0));
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("weather_synchronization");
                    } else {
                        show += "姓名同步失败...\n";
                        SnycFail("同步失败(连接异常)");
                    }

                }

                break;
            case "0e"://天气同步
                MyToash.Log(synchronizationWeatherCount + "  天气同步  " + WeatherTemperatureSize);
                ++synchronizationWeatherCount;
                if (synchronizationWeatherCount < WeatherTemperatureSize) {
                    MyToash.Log(temperatureeAndWeathere.Data.get(synchronizationWeatherCount).WeatherDetails);
                    sendToBule(WatchBlueTestActivity.snycDataWeathere(temperatureeAndWeathere.Data.get(synchronizationWeatherCount).WeatherDetails, synchronizationWeatherCount));
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("weather_synchronization");
                } else {
                    if (text.substring(4, 6).equals("00")) {
                        show += "天气同步成功...\n";
                    } else {
                        show += "天气同步失败...\n";
                    }
                    sendToBule(WatchBlueTestActivity.snycDataTemperaturee(temperatureeAndWeathere.Data.get(0).Temperature, 0));
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("temperature_synchronization");
                    //  snycData("temperaturee_synchronization");

                }
                break;
            case "0f"://气温同步
                ++synchronizationTemperatureCount;
                if (synchronizationTemperatureCount < WeatherTemperatureSize) {
                    //   MyToash.Log(temperatureeAndWeathere.Data.get(synchronizationTemperatureCount).Temperature);
                    sendToBule(WatchBlueTestActivity.snycDataTemperaturee(temperatureeAndWeathere.Data.get(synchronizationTemperatureCount).Temperature, synchronizationTemperatureCount));
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("temperature_synchronization");

                } else {
                    if (text.substring(4, 6).equals("00")) {
                        show += "气温同步成功...\n";
                    } else {
                        show += "气温同步失败...\n";
                    }
                    snycData("samplingparameter_synchronization");
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("samplingparameter_synchronization");

                 /*   if (!flag.equals("nofirstsport")) {
                        snycData("samplingparameter_synchronization");
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("samplingparameter_synchronization");
                    } else {
                        SnycSuccess();

                    }*/

                }

                break;

            case "12"://采样参数同步
                if (text.substring(4, 6).equals("00")) {
                    show += "采样参数同步成功...\n";
                    SnycSuccess();
                } else {
                    show += "采样参数同步失败...\n";
                    SnycFail("同步失败(连接异常)");
                }
                break;

        }

        activity_connectblue_remind.setText(show);
    }

    private void SnycSuccess() {
        syncOver();
        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("");
        waitDialog.setMessage("同步数据成功");
        waitDialog.dismissDialog();
        showNotDialog();
    }

    private void SnycFail(String message) {
        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("");
        //myWatchBlueHandler = null;
        MyToash.Toash(activity, message);
        waitDialog.dismissDialog();
        if (message.equals("同步失败(腕表正处于运动模式中)")) {
            //  MyToash.Log("同步失败(腕表正处于运动模式中)");
            ShareUitls.putString(ConnectBlueActivity.this, "isConnectActivity", "SPORTING");
        } else {
            if (myBuleWatchManager != null) {
                myBuleWatchManager.endConnect();
            }

        }
        // startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    private void snycData(String instructType) {
        //  Log.i("myblueEEE", instructType + "     " + (++count));
        switch (instructType) {
            //个人信息
            case "Personal_information_synchronization":
                sendToBule(WatchBlueTestActivity.snycDataInformation(userInformation));
                break;
            //时间
            case "time_synchronization":
                sendToBule(WatchBlueTestActivity.snycDataTime());
                break;
            //运动参数
            case "sportparameter_synchronization":
                sendToBule(WatchBlueTestActivity.snycDataSportparameter(userInformation));
                break;
            //名字
            case "name_synchronization":
                sendToBule(WatchBlueTestActivity.snycDataName(userInformation.getNickName(), true));
                break;
            //天气
            case "weather_synchronization":
                //  sendToBule(WatchBlueTestActivity.snycDataWeathere(temperatureeAndWeathere.WeatherDetails));
                break;
            //气温
            case "temperaturee_synchronization":
                //  sendToBule(WatchBlueTestActivity.snycDataTemperaturee(temperatureeAndWeathere.Temperature));
                break;
            //采样参数
            case "samplingparameter_synchronization":
                sendToBule(WatchBlueTestActivity.snycDataSamplingparameter(DataTransferUtils.hexStr2ByteArray("120B" + postParameterRequest.Parameters)));
        }

    }

    public void checkMac(final String MAC) {//接口判断该蓝牙是否被绑定

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostCheckMACRequest&version=v2.9.6");
        params.addBodyParameter("MACAddress", MAC);
        HttpUtils.getInstance(ConnectBlueActivity.this).sendRequestRequestParamsNew("", params, true, new HttpUtils.ResponseListenerNew() {
                    @Override
                    public void onResponse(String response, PublicDataClass.MdResponse mdResponse) {
                        if (mdResponse.Status.equals("3")) {
                            /// showNotDialog(MAC, true);
                            connectBule();//开启蓝牙连接 成功之后发送绑定指令
                        } else {
                            //showNotDialog(MAC,true);
                            waitDialog.dismissDialog();
                            Toast.makeText(ConnectBlueActivity.this, "绑定失败(此腕表已经被其他用户绑定)", Toast.LENGTH_LONG).show();
                            finish();
                        }

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {


                    }
                }

        );


    }

    public void updatekMac(final String MAC) {//上传更新User蓝牙地址
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostMACRequest&version=v2.9.6");
        params.addBodyParameter("MACAddress", MAC);
        Log.i("myblue", ShareUitls.getString(ConnectBlueActivity.this, "UID", "0") + " " + ShareUitls.getString(ConnectBlueActivity.this, "ResultJWT", "0") + "  " + MAC);
        HttpUtils.getInstance(ConnectBlueActivity.this).sendRequestRequestParamsNew("", params, true, new HttpUtils.ResponseListenerNew() {
                    @Override
                    public void onResponse(String response, PublicDataClass.MdResponse mdResponse) {
                        if (mdResponse.Status.equals("1")) {
                            ShareUitls.putUserInformationMac(ConnectBlueActivity.this, MAC);//保存本地蓝牙地址
                            //同步
                            waitDialog.setMessage("正在同步数据");
                            snycData("Personal_information_synchronization");
                            myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Personal_information_synchronization");
                        } else {
                        }


                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {


                    }
                }

        );


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBuleSerachManager != null) {
            myBuleSerachManager.endSearch();//停止搜索
        }
    }

}
