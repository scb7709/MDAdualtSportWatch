package com.headlth.management.activity;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.headlth.management.MyBlue.MyBuleWatchManager;
import com.headlth.management.MyBlue.MyWatchBlueHandler;
import com.headlth.management.MyBlue.WatchBlueTestActivity;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.circle.smallProgressCircle;
import com.headlth.management.clenderutil.WaitDialog;
import com.headlth.management.entity.User;
import com.headlth.management.myview.MyToash;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DataTransferUtils;
import com.headlth.management.utils.DiskBitmap;
import com.headlth.management.utils.Share;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.StringForTime;
import com.headlth.management.watchdatasqlite.MySQLiteBaseClass;
import com.headlth.management.watchdatasqlite.MySQLiteDataDao;
import com.headlth.management.watchdatasqlite.UpLoadingWatchData;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;

/**
 * Created by abc on 2016/11/4.
 */
@ContentView(R.layout.activity_watchsportsummary)
public class WatchSportSummaryActivity extends BaseActivity {
    @ViewInject(R.id.activity_aerobicsport_heartrate_drawline)
    private SuitLines suitLines;
    //有效运动时间
    @ViewInject(R.id.home_my_effective_time)
    private TextView mEffectiveTime;
    @ViewInject(R.id.activity_aerobicsport_Target)
    private TextView Target;


    //当前心率
    @ViewInject(R.id.average_heartrate_value)
    private TextView average_heartrate_value;
    @ViewInject(R.id.share)
    private RelativeLayout share;//分享
    @ViewInject(R.id.btback)
    private RelativeLayout btback;//返回

    @ViewInject(R.id.stepTimeTV)
    private TextView stepTimeTV;
    @ViewInject(R.id.smallseekCircle)
    private smallProgressCircle smallprogressCircle;
    private WaitDialog waitDialog;
    private Activity activity;
    private IWeiboShareAPI mWeiboShareAPI;
    private MyBuleWatchManager myBuleWatchManager;
    //  private BluetoothGattCharacteristic READE_BluetoothGattCharacteristic;
    private BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristic;
    private BluetoothGatt mBluetoothGatt;//发现蓝牙服务，根据特征值处理数据交互
    private String MAC;// UID;version,
    private String Single_motion_results;
    private User.UserInformation userInformation;
    private int Starttime, cruuent, startTimeListsize;
    private boolean canback;//数据处理完成可以返回
    private MySQLiteDataDao mySQLiteDataDao;
    private UpLoadingWatchData upLoadingWatchData;
    private MyWatchBlueHandler myWatchBlueHandler;//发出的蓝牙指令10秒内收不到回复的处理类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        activity = this;
        initialize();

    }

    private void initialize() {
        myWatchBlueHandler();
        upLoadingWatchData = UpLoadingWatchData.getInstance(activity);
        mySQLiteDataDao = MySQLiteDataDao.getInstance(activity);
        MAC = ShareUitls.getUserInformationMac(activity);
        ShareUitls.putString(activity, "isConnectActivity", "");
        MyToash.Log("MAC=====" + MAC);
        // UID = ShareUitls.getString(activity, "UID", "0");
        //  version = VersonUtils.getVersionName(activity);
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constant.SINA_KEY);//注册微博分享SDK
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(activity, false);
        waitDialog.setMessage("正连接腕表...");
        waitDialog.showDailog();
        userInformation = ShareUitls.getUser(activity).getUserInformation();

        startTimeList = new ArrayList<>();//保存单次运动总结的开始时间的集合
        Single_motion_resultsS = new ArrayList<>();
        StartEndTimeList = new ArrayList<>();

        // bytesOriginal_data = new ArrayList<>();
        List<MySQLiteBaseClass.Single_Original> single_originals = mySQLiteDataDao.querySingleNOOriginal();//获取已经保存的所有单次运动（无对应的原始数据或者原始数据不完整） 总结的数据
        startTimeListsize = single_originals.size();
        for (MySQLiteBaseClass.Single_Original singleOriginal : single_originals) {
            //  MyToash.Log(startTimeListsize+"----");
            int Starttime = Integer.parseInt(singleOriginal.Starttime);
            String Single_data = singleOriginal.Single_data;
            startTimeList.add(Starttime);
            StartEndTimeList.add(new int[]{Starttime, Starttime + DataTransferUtils.getInt_10(Single_data.substring(12, 16))});
            Single_motion_resultsS.add(Single_data);
            String Original_data = singleOriginal.Original_data;

            if (Original_data != null && Original_data.length() > 0) {
                this.Original_data = Original_data;
                this.Starttime = Starttime;
                MyToash.Log(Starttime + "      老数据   " + Original_data);
            }

            if (bytesOriginal_data == null) {//请求原始数据需要的时间字节数组
                bytesOriginal_data = WatchBlueTestActivity.snycDataOriginal_data(Starttime);
            }
        }


        initsuitLines();
        connectBule();//连接蓝牙
    }
    private void initsuitLines() {
        suitLines.setLineForm(true);
        int[] colors = new int[2];
        suitLines.anim();
        colors[0] = Color.parseColor("#ff4763");
        colors[1] = Color.parseColor("#b51225");
        suitLines.setDefaultOneLineColor(colors);
        suitLines.setLineType(SuitLines.SEGMENT);
    }


    @Event(value = {R.id.btback, R.id.share})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.btback:
                finish();
                break;
            case R.id.share:
                Share.getInstance().showPopFormBottom(activity, webHandler);
                break;

        }
    }

    private void showSingle_motion_results(String text) {
        if (text != null && text.length() == 40 && text.startsWith("0814")) {
            final int TargeT = Integer.parseInt(userInformation.getWatchDuration());//100;//
            final int mEffectiveTimee = DataTransferUtils.getInt_10(text.substring(16, 20));//50;//
            int stepTimeTVv = DataTransferUtils.getInt_10(text.substring(12, 16));

            mEffectiveTime.setText(StringForTime.stringForTime(mEffectiveTimee));//有效时间
            stepTimeTV.setText(StringForTime.stringForTime(stepTimeTVv));//总时间
            Target.setText(StringForTime.stringForTime(TargeT));//目标时间
            //  smallprogressCircle.setMax(TargeT);
            average_heartrate_value.setText(DataTransferUtils.getInt_10(text.substring(22, 24)) + "");//平均心率
            final int steep = 10000 / TargeT;
            if(mEffectiveTimee==0){
                return;
            }
            final int alltime = mEffectiveTimee > TargeT ? TargeT : mEffectiveTimee;
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    // handler.sendEmptyMessage(3);
                    for (int i = 1; i <= alltime; i++) {
                        smallprogressCircle.setProgress(i * 100 / TargeT);
                        try {
                            Thread.sleep(steep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }.start();
        }
    }

    private void connectBule() {
        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("connectBule", 60000);
        myBuleWatchManager = MyBuleWatchManager.getInstance(activity, MAC, new MyBuleWatchManager.OnCharacteristicListener() {
            @Override
            public void onServicesDiscovered(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristi) {
                WRITE_BluetoothGattCharacteristic = WRITE_BluetoothGattCharacteristi;
                mBluetoothGatt = bluetoothGatt;
                if (WRITE_BluetoothGattCharacteristic != null && mBluetoothGatt != null) {
                    waitDialog.setMessage("正在查询设备状态");
                    byte[] bytes = WatchBlueTestActivity.getWatchBuleData("GetWatchSportState");
                    sendToBule(bytes,WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("GetWatchSportState");
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {//画心率图
                myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("");
                // Log.i("mybule", "设备可读可写");
                String text = msg.obj.toString();
                int length = text.length();
                Log.i("myblue", "" + text + "  " + length);
                if (length > 5) {
                    String head = text.substring(0, 2);
                    HandlerData(length, head, text);
                }
            }
        }
    };

    private String Original_data = "";//表示 每次原始数据（每收到一次原始数据的回复表示一个心率点 把每个点用_拼接起来）
    private List<Integer> startTimeList;//保存单次运动结果开始时间的集合 （有多次单次运结果） 该集合表示总共有多少次单次运动结果 也表示需要查询多少次原始数据
    private List<String> Single_motion_resultsS;//保存单次结果

    private List<int[]> StartEndTimeList;//包含每一次单次运动开始和结束的时间集合
    private byte[] bytesOriginal_data;
    private int connectBule_count;

    private void myWatchBlueHandler() {
        myWatchBlueHandler = MyWatchBlueHandler.getInstance(new MyWatchBlueHandler.MyWatchBlueHandlerListener() {
            @Override
            public void strikeDelayed(String flag) {
                switch (flag) {
                    case "connectBule":
                        if (connectBule_count <= 2) {
                            connectBule_count++;//0914  59b6219b6f   000000ff000000ff00000155ab 0914 59b621a76f 000000ff000000ff00000155b7
                            connectBule();
                        } else {
                            dataSyncFail();//数据获取失败
                        }
                        break;
                    case "GetWatchSportState":
                    case "Single_motion_results":

                  /*      cruuent = startTimeList.size() - 1;//用来记录当前集合最后一个
                        if (cruuent >= 0) {//
                           // int Starttime = startTimeList.get(cruuent);
                            sendToBule(bytesOriginal_data, WRITE_BluetoothGattCharacteristic, mBluetoothGatt);//请求单次运动原始数据
                            myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Original_data");
                        } else {
                            sendToBule(WatchBlueTestActivity.getWatchBuleData("Summary_of_the_day"), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);//查询当天运动总结
                            myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Summary_of_the_day");
                        }*/
                        // break;
                    case "Original_data":
                        dataSyncFail();//数据失败
                        //sendToBule(WatchBlueTestActivity.getWatchBuleData("Summary_of_the_day"), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);//查询当天运动总结
                        // myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Summary_of_the_day");
                        break;
                    //时间
                    case "Summary_of_the_day":
                        sendToBule(WatchBlueTestActivity.getWatchBuleData("Historical_static_heart_rate"), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);//查询历史静态
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Historical_static_heart_rate");
                        break;
                    case "Historical_static_heart_rate":
                        sendToBule(WatchBlueTestActivity.getWatchBuleData("PostBloodPressureRequest"), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);//查询睡眠
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("PostBloodPressureRequest");
                        break;
                    case "PostBloodPressureRequest":
                        sendToBule(WatchBlueTestActivity.getWatchBuleData("PostSleepInfoRequest"), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);//查询睡眠
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("PostSleepInfoRequest");
                        break;
                    case "PostSleepInfoRequest":
                        dataSyncFail();//数据失败
                        break;

                }
            }
        });

    }

    // @TargetApi(Build.VERSION_CODES.N)
    private void HandlerData(int length, String head, String text) {
        switch (head) {
            case "13"://运动状态查询
                if (text.substring(0, 6).equals("130400")) {//

                    waitDialog.setMessage("正在同步数据...");
                    byte[] bytes = WatchBlueTestActivity.getWatchBuleData("Single_motion_results");
                    sendToBule(bytes, WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                    myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Single_motion_results");
                } else {
                    ShareUitls.putString(activity, "isConnectActivity", "SPORTING");
                    MyToash.Toash(activity,"同步失败(腕表正处于运动模式中)");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    waitDialog.dismissDialog();
                    finish();
                }
                break;
            case "08"://单次运动总结
                if (length == 40) {
                    if (!text.equals("081400000000000000000000000000000000001c")) {
                        int Starttime = DataTransferUtils.getInt_10(text.substring(4, 12));
                        startTimeList.add(Starttime);
                        StartEndTimeList.add(new int[]{Starttime, Starttime + 60 * DataTransferUtils.getInt_10(text.substring(12, 16))});
                        Single_motion_resultsS.add(text);
                        mySQLiteDataDao.insertSingleAndOriginal(new MySQLiteBaseClass.Single_Original(Starttime + "", text));//保存单次运动数据
                        upLoadingWatchData.uploadingWatchData("Single_motion_results", text, 0);//上传单次数据
                        byte[] bytes = WatchBlueTestActivity.getWatchBuleData("Single_motion_results");
                        sendToBule(bytes, WRITE_BluetoothGattCharacteristic, mBluetoothGatt);
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Single_motion_results");

                    } else {//请求完所有单次 Single_motion_results变量 记录最近最近一次单次
                        Collections.sort(startTimeList);//从小到大排序
                        Collections.sort(Single_motion_resultsS);//从小到大排序
                        Collections.sort(StartEndTimeList, new Comparator<int[]>() {
                            @Override
                            public int compare(int[] ints, int[] t1) {
                                return ints[0] - t1[0];
                            }
                        });

                        startTimeListsize = startTimeList.size();

                        // cruuent = startTimeList.size() - 1;//用来记录当前集合最后一个
                        Single_motion_results = Single_motion_resultsS.get(startTimeListsize - 1);
                        showSingle_motion_results(Single_motion_results);//展示最近单次运动数据

                        for (int i = 0; i < startTimeList.size(); i++) {
                            MyToash.Log(StartEndTimeList.get(i)[0] + "  时间范围   " + StartEndTimeList.get(i)[1]);
                        }

                        if (startTimeListsize >= 0) {//
                            // starttime = startTimeList.get(cruuent);
                            //是否需要判断数据库里是否存在该条原始数据就不用去蓝牙查询了
                            sendToBule(bytesOriginal_data, WRITE_BluetoothGattCharacteristic, mBluetoothGatt);//请求单次运动原始数据
                            myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Original_data");
                        }
                    }
                }
                break;
            case "09"://单次运动原始数据   091459a864846d000000ff000000ff0000000071

                if (length == 40) {
                    if (!text.substring(4, 12).equals("00000000")) {// + 28800
                        int samplingtime = DataTransferUtils.getInt_10(text.substring(4, 12));
                        int starttime = getStarttime(samplingtime);
                        MyToash.Log(samplingtime + "     ----- " + starttime);
                        // MyToash.Log(DataTransferUtils.getDate(samplingtime * 1000) + "  " + DataTransferUtils.getDate(starttime * 1000));
                        //  MyToash.Log(new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date(samplingtime*1000)) + "  " + new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date(starttime*1000)));

                        //15052 48575   15052 12076 15052 75869 15052 77926
                        if (starttime == Starttime) {//如果这个开始时间等于数据库里存的原始数据对应的时间就把这次数据拼接到数据里存的原始数据后面  数据库里存的原始数据对应的时间一定是最早的
                            Original_data += text + "_";
                            mySQLiteDataDao.updateSingleAndOriginal(starttime + "", Original_data);
                        } else {
                            if (Original_data.length() > 0) {
                                if (Original_data.endsWith("_")) {
                                    Original_data = Original_data.substring(0, Original_data.length() - 1);//去掉最后一位_
                                }
                                mySQLiteDataDao.updateSingleAndOriginal(Starttime + "", Original_data);//更新数据库里的老原始数据
                                upLoadingWatchData.uploadingWatchData("Original_data", Original_data, Starttime);//上传单次运动原始数据
                                Original_data = "";

                            }
                            if (starttime == 1000000000) {//补齐找不到开始时间的原始数据 的  单次运动结果
                                mySQLiteDataDao.insertSingleAndOriginal(new MySQLiteBaseClass.Single_Original(1000000000 + "", "081410000000000000000000000000000000001c"));//保存单次运动数据
                            }
                            Starttime = starttime;//重新赋值全局开始时间
                            Original_data = text + "_";//重新赋值全局变量

                        }//
                        sendToBule(bytesOriginal_data, WRITE_BluetoothGattCharacteristic, mBluetoothGatt);//请求单次运动原始数据
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Original_data");
                    } else {//所有原始请求完成
                        if (Original_data.length() > 0) {
                            if (Original_data.endsWith("_")) {
                                Original_data = Original_data.substring(0, Original_data.length() - 1);//去掉最后一位_
                            }

                           /// MyToash.Log(Starttime + "   画心率图 " + startTimeList.get(startTimeListsize - 1));
                            //if (Starttime == startTimeList.get(startTimeListsize - 1)) {//最近一次单次运动时间==最近一次原始时间  有可能最近一次单次运动采集不到原始数据
                            setHeartInage(Original_data);//画最近的心率图
                            //  }
                            mySQLiteDataDao.updateSingleAndOriginal(Starttime + "", Original_data);//更新数据库里的老原始数据
                            upLoadingWatchData.uploadingWatchData("Original_data", Original_data, Starttime);//上传单次运动原始数据
                            // startTimeList.remove(temp);
                            Original_data = "";
                        }
                        mySQLiteDataDao.deleteSingle_motion_results(startTimeList);//删除所有没有对应原始数据的单次数据
                        sendToBule(WatchBlueTestActivity.getWatchBuleData("Summary_of_the_day"), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);//查询当天运动总结
                        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Summary_of_the_day");

                    }
                }
                break;
            case "06"://当天运动总结查询结果 061100b4003cb464012c000a00004e20c4
                mySQLiteDataDao.insert(new MySQLiteBaseClass("Summary_of_the_day", text));//保存当天运动总结
                upLoadingWatchData.uploadingWatchData("Summary_of_the_day", text, 0);//上传当天运动总结
                sendToBule(WatchBlueTestActivity.getWatchBuleData("Historical_static_heart_rate"), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);//查询历史静态
                myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("Historical_static_heart_rate");
                //  dataSyncOver();//数据同步完成
                break;
            case "0c":
                if (!text.equals("0c08000000000014")) {
                    mySQLiteDataDao.insert(new MySQLiteBaseClass("Historical_static_heart_rate", text));//保存历史静态
                    upLoadingWatchData.uploadingWatchData("Historical_static_heart_rate", text, 0);//上传历史静态
                }
                sendToBule(WatchBlueTestActivity.getWatchBuleData("PostBloodPressureRequest"), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);//查询血压
                myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("PostBloodPressureRequest");
                break;
            case "0d":
                if (!text.equals("0d0c00000000000000000019")) {
                    mySQLiteDataDao.insert(new MySQLiteBaseClass("PostBloodPressureRequest", text));//保存血压
                    upLoadingWatchData.uploadingWatchData("PostBloodPressureRequest", text, 0);//上传血压
                }
                sendToBule(WatchBlueTestActivity.getWatchBuleData("PostSleepInfoRequest"), WRITE_BluetoothGattCharacteristic, mBluetoothGatt);//查询睡眠
                myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("PostSleepInfoRequest");
                break;
            case "10":
                if (!text.equals("10130000000000000000000000000000000023")) {
                    mySQLiteDataDao.insert(new MySQLiteBaseClass("PostSleepInfoRequest", text));//保存睡眠数据
                    upLoadingWatchData.uploadingWatchData("PostSleepInfoRequest", text, 0);//上传睡眠数据
                }
                dataSyncOver();//数据同步完成
                break;

        }
    }

    private void dataSyncOver() {
        ShareUitls.putString(activity, "isConnectActivity", "connect");//回到首页的时候 能获取连接
        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("");
        canback = true;
        waitDialog.setMessage("同步完成...");
        waitDialog.dismissDialog();
        share.setVisibility(View.VISIBLE);
        btback.setVisibility(View.VISIBLE);
    }

    private void dataSyncFail() {
        myWatchBlueHandler.sendMyWatchEmptyMessageDelayed("");
        MyToash.Toash(activity, "连接失败");
        waitDialog.dismissDialog();
        if (myBuleWatchManager != null) {
            MyBuleWatchManager.endConnect();
        }
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    public static void sendToBule(byte[] bytes, BluetoothGattCharacteristic WRITE_BluetoothGattCharacteristic, BluetoothGatt mBluetoothGatt) {
        if (bytes != null && bytes.length > 0) {
            WRITE_BluetoothGattCharacteristic.setValue(bytes);
            mBluetoothGatt.writeCharacteristic(WRITE_BluetoothGattCharacteristic);
        }
    }

    List<Unit> lines;

    private void setHeartInage(final String text) {
        new Thread() {
            @Override
            public void run() {
                super.run();

                String[] strings = text.split("_");
               // MyToash.Log("画心率图1"+  strings.length);
                List<Integer> tempValue = new ArrayList<>();
                for (String s : strings) {
                    int value = DataTransferUtils.getInt_10(s.substring(12, 14));
                    tempValue.add(value);
                }
              //  MyToash.Log("画心率图2"+  tempValue.size());
                List<Integer> thirdData = new ArrayList<>();
                int size = strings.length;
                int steep = size / 30;

                if (steep <=1) {
                    thirdData = tempValue;
                } else {
                    for (int i = 0; i < size; i = i + steep) {
                        thirdData.add(tempValue.get(i));
                    }
                }
              //  MyToash.Log("画心率图3"+  thirdData.size());
                int tempsize=thirdData.size();
                if(tempsize>0) {
                    lines = new ArrayList<>();
                    for (int i = 0; i < tempsize; i++) {
                        float vlaue = thirdData.get(i) - 43;
                        lines.add(new Unit(vlaue < 0 ? 1 : vlaue, ""));
                    }

                    if (lines != null && lines.size() > 0) {
                        MyToash.Log("画心率图4"+  lines.size());
                        suitLines.feedWithAnim(lines);
                    }

                    //handler.sendEmptyMessage(101);
                }
            }
        }.start();


    }


    public void sinashare(String webImageUrl) {
        // 创建微博分享接口实例
        // Log.i("sssssssssssssIIII", ImageUrl);
        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        WeiboMessage weiboMessage = new WeiboMessage();
        weiboMessage.mediaObject = getImageObj(webImageUrl);
        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.message = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest(this, request);
        //  waitDialog.ShowDialog(false);


    }

    //微博图片压缩weibo
    private ImageObject getImageObj(String webImageUrl) {
        ImageObject imageObject = new ImageObject();
        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        // Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.umsg);
        Bitmap bitmap = DiskBitmap.getDiskBitmap(webImageUrl, this);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    Handler webHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String webImageUrl = (String) msg.obj;

            if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
                sinashare(webImageUrl);
            }


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareUitls.putString(activity, "WATCHSPORT", "");
        //ShareUitls.putString(WatchSportSummaryActivity.this, "isConnectActivity", "");
       /* if (myBuleWatchManager != null) {
            myBuleWatchManager.endConnect();
        }*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            if (canback) {
                ShareUitls.putString(WatchSportSummaryActivity.this, "WATCHSPORT", "");
                //ShareUitls.putString(WatchSportSummaryActivity.this, "isConnectActivity", "");
              /*  if (myBuleWatchManager != null) {
                    myBuleWatchManager.endConnect();
                }*/
                finish();
            } else
                Toast.makeText(WatchSportSummaryActivity.this, "正在同步数据", Toast.LENGTH_LONG).show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private int getStarttime(int starttime) {//判断采样原始时间的在那个单次时间的范围内
        for (int i = 0; i < StartEndTimeList.size(); i++) {
            if (starttime >= StartEndTimeList.get(i)[0] && starttime <= StartEndTimeList.get(i)[1]) {
                return StartEndTimeList.get(i)[0];
            }
        }
        return 1000000000;//找不到对应开始时间的原始数据
    }
}
