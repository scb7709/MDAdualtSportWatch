
package com.headlth.management.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.blue.BleService;
import com.headlth.management.blue.adapters.BleServicesAdapter;
import com.headlth.management.blue.sensor.BleSensor;
import com.headlth.management.blue.sensor.BleSensors;
import com.headlth.management.circle.RoundProgressBar;
import com.headlth.management.circle.smallProgressCircle;
import com.headlth.management.entity.upCallBack;
import com.headlth.management.scan.ToJson;
import com.headlth.management.service.FakePlayerService;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DiskBitmap;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ImageUtil;
import com.headlth.management.utils.InternetUtils;
import com.headlth.management.utils.Share;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.StringForTime;
import com.headlth.management.utils.VersonUtils;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2016/2/26.
 */
@ContentView(R.layout.activity_aerobicsport)//复用我的处方布局
public class AerobicSportActivity extends BaseActivity implements View.OnClickListener/*, IWeiboHandler.Response*/ {
    @ViewInject(R.id.slideUnlockView)
    private com.headlth.management.movelistview.SlideUnlockView slideUnlockView;
    private Vibrator vibrator;
    private ScreenOnOffReceiver receiver;

    @ViewInject(R.id.lock_relativeLayout)
    private RelativeLayout lock_relativeLayout;
    PopupWindow popupWindoww;

    //ble蓝牙
    private BleServicesAdapter gattServiceAdapter;
    private String deviceAddress;
    private static BleService bleService;
    private BleSensor<?> heartRateSensor;

    //有效运动时间
    @ViewInject(R.id.home_my_effective_time)
    private TextView mEffectiveTime;
    public static int S_TOTAL_SEC = 0;
    private boolean isStop = false;
    //折线布局
    @ViewInject(R.id.drawline)
    private RelativeLayout drawline;
    //当前心率
    @ViewInject(R.id.ValidTime)
    private TextView mBmp;
    //折线布局顶
    @ViewInject(R.id.share)
    private RelativeLayout share;//分享

    @ViewInject(R.id.up)
    private TextView up;
    //折线布局底
    @ViewInject(R.id.low)
    private TextView low;
    //有效白条
    @ViewInject(R.id.effect)
    private TextView effect;
    //移动标尺
    @ViewInject(R.id.move)
    private ImageView move;
    //总条
    @ViewInject(R.id.out)
    private TextView out;
    //有效目标
    @ViewInject(R.id.activity_aerobicsport_Target)
    private TextView Target;
    //下限
    @ViewInject(R.id.LBound)
    private TextView LBound;
    //上限
    @ViewInject(R.id.UBound)
    private TextView UBound;


    private int uBound, lBound;
    //结束左上角按钮
    @ViewInject(R.id.btback)
    private RelativeLayout btback;
    //停止运动按钮
    @ViewInject(R.id.bt_controller)
    private Button bt_controller;
    //结束热身 暂停 开始
    @ViewInject(R.id.im_lock)
    private ImageButton im_lock;
    @ViewInject(R.id.re)
    RelativeLayout re;
    @ViewInject(R.id.bt_stop)
    private Button bt_stop;
    //计时器TextView
    @ViewInject(R.id.stepTimeTV)
    private TextView stepTimeTV;

    //标题状态标志
    @ViewInject(R.id.titlemark)
    private TextView titlemark;
    //有效运动环'
    @ViewInject(R.id.smallseekCircle)
    private smallProgressCircle smallprogressCircle;
    //时间
    private SimpleDateFormat df;
    //时间集合
    private ArrayList<String> mDateArray = new ArrayList<String>();
    //心率集合
    private ArrayList<Integer> mHeartArray = new ArrayList<Integer>();
    private Handler mHandler = new Handler();
    //PopWindow弹出警告
    private View view;
    //屏幕宽高
    private int screenWidth;
    private int screenHeight;
    private PopupWindow pop;
    private AlertDialog.Builder dialog;
    private static Boolean isDismiss;
    //用来判断是否用户点击确定按钮
    private Boolean clicked = false;
    //用来判断广播是否接受成功
    private boolean isConnected = false;

    //范围区间
    @ViewInject(R.id.qujian)
    private RelativeLayout qujian;
    private static String url;
    //Thread myThread;
    private int con_state = -1;//当前状态 -1：热身准备 ，0 ：暂停，1：运动中
    private boolean con_isstate;//true:运动，false：暂停
    private int fifteen_minutes = 0;//15分钟倒计时
    private boolean lock;//锁切换
    String instruct = "";
    Intent intent;
    String UploadTime = "";
    private boolean warning = true;//心率不在范围内震动警告
    private MediaPlayer exceedMediaPlayer, underMediaPlayer;
    private int tenvibratro;
    private BroadcastReceiver ServiceToActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (con_state != 2) {
                setData_Update();//各种计数数据跟新
                setPOP_Detection();//POP 检测
                if (value != 0) {
                    setDraw();
                }
            }
        }
    };

    @ViewInject(R.id.weizhi)
    RelativeLayout weizhi;
//提醒恢复到心率区间的弹框

    private Holder holder;


    public class Holder {
        //左上角删除
        @ViewInject(R.id.delete)
        private Button delete;
        @ViewInject(R.id.img_state)
        private ImageView img_state;
        @ViewInject(R.id.thirtyCount)
        private TextView thirtyCount;
        @ViewInject(R.id.line)
        private TextView line;
        @ViewInject(R.id.huifu)
        private Button huifu;

    }

    public static Activity activity;
    private boolean sportover;//运动完成 小结界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        if (isOverSport) {// 如果分享等操作出现奔溃 本页面会出现在栈顶 并且重新调用oncreat 方法 此处用来标记此种情况
            finish();
        } else {
            mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constant.SINA_KEY);//注册微博分享SDK
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);//锁屏下 让本界面置于锁屏下
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

            activity = this;
            startService(new Intent(AerobicSportActivity.this, FakePlayerService.class).putExtra("flag", "sport"));
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//禁止自动息屏
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            UploadTime = format.format(new Date());
            Log.e("dadth", "onCreate");
            url = Constant.BASE_URL;
            S_TOTAL_SEC = 0;
            flag = 0;
            isDismiss = false;
            showTime = 0;
            alltime = 0;
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            intent = new Intent();
            intent.setAction("ActivityToServiceReceiver");
            //初始化运动界面的view
            initView();
            //初始化pop
            popWindow();
            //首次连接蓝牙
            connectBle();
            //菊花等待可以单独提出来
            initDialog(AerobicSportActivity.this);
            //滑动解锁
            setSildeLock();
            setPopWindow();
            registServiceToActivityReceiver();
            setDatee();

/*
        if (savedInstanceState != null) {//获取保存数据
            showTime = savedInstanceState.getInt("showTime");
            alltime = savedInstanceState.getInt("alltime");
            Data = savedInstanceState.getString("Data");
            progress = max;
            mDateArray = savedInstanceState.getStringArrayList("mDateArray");
            mHeartArray = savedInstanceState.getIntegerArrayList("mHeartArray");
            startTime = System.currentTimeMillis();
            h.sendEmptyMessage(35);

        }*/
        }
    }

    private void setPopWindow() {
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        View view = LayoutInflater.from(AerobicSportActivity.this).inflate(R.layout.dialog_fifteen_minutes, null);
        popupWindoww = new PopupWindow(view, ImageUtil.dp2px(this, 300), ImageUtil.dp2px(this, 100), true);
        popupWindoww.setOutsideTouchable(true);
    }

    private void setPOP_Detection() {//pop 检测
        if (con_state == 1) {
            fifteen_minutes = 0;
            Log.e("xiancheng", "pop检测线程" + fifteen_minutes);
            //达到最大值准备活动结束
            //点击确定上传数据了
            if (clicked) {
                if (pop.isShowing()) {
                    h.sendEmptyMessage(28);
                }
                return;
            }
            //推到后台，就不进行检测了
            if (mOnpaused) {
                // return;
            }
            //大于上限出现红色警告,否者再分，如果第一次扣除过那么只有当第二次再达到有效后再进行30秒计时，否者只扣一次
            if (value > Integer.parseInt(UBound.getText().toString().trim())) {
                Log.e("isKouchued", "大于");
                h.sendEmptyMessage(29);

            } else {
                h.sendEmptyMessage(31);
                if (value < Integer.parseInt(LBound.getText().toString().trim())) {
                    Log.e("isKouchued", "小于");
                } else {
                    Log.e("isKouchued", "等于");
                    wait = false;
                }
            }
            //到达扣除界面线程停止，当wait为true是表示处在扣除界面停止检测3秒，当为false是又开始检测
            if (!wait) {
                Log.e("xiancheng", "开启POP检查" + fifteen_minutes);
                continueTest();
            }
            if (isDismiss) {
                return;
            }
        } else if (con_state == 0) {
            thirty = 50;
            five = 5;
            //5秒计时弹出popWindow
            start = 0;
            end = 0;
            ifFive = 0;
            firstIn = false;
            keepFive = false;
            feepifFive = 0;

            Log.e("xiancheng", "pop检测线程暂停");
            Message message = Message.obtain();
            //暂停状态不检测
            fifteen_minutes += 1;
            message.arg1 = fifteen_minutes;
            message.what = 105;
            h.sendMessage(message);

        }
    }

    //横向拉动解锁
    private void setSildeLock() {
        // 注册屏幕锁屏的广播
        registScreenOffReceiver();
        // 获取系统振动器服务
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // 设置滑动解锁-解锁的监听
        slideUnlockView.setOnUnLockListener(new com.headlth.management.movelistview.SlideUnlockView.OnUnLockListener() {
            @Override
            public void setUnLocked(boolean unLock) {
                // 如果是true，证明解锁
                if (unLock) {
                    // 启动震动器 100ms
                    vibrator.vibrate(100);
                    // 当解锁的时候，执行逻辑操作，在这里仅仅是将图片进行展示
                    im_lock.setImageDrawable(getResources().getDrawable(R.drawable.openlock));

                    bt_controller.setClickable(true);
                    bt_stop.setClickable(true);
                    // 重置一下滑动解锁的控件
                    slideUnlockView.reset();
                    // 让滑动解锁控件消失
                    lock_relativeLayout.setVisibility(View.GONE);
                } else {


                }
            }
        });
    }

    //标尺水平动画
    private TranslateAnimation ta1;
    private Intent gattServiceIntent;
    //绑定服务
    private Boolean isBanded = false;
    //json数据
    private String Data;
    //广播注册标志
    private Boolean isUnregisted = false;

    private static int flag = 0;
    //是否进入Onpaused了
    private boolean mOnpaused = false;
    private com.headlth.management.clenderutil.WaitDialog waitDialog;


    public void initDialog(Context context) {
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(context);
        waitDialog.setMessage("正在上传,请稍候...");
    }

    //准备的环
    @ViewInject(R.id.roundProgressBar2)
    private RoundProgressBar mRoundProgressBar2;

    @ViewInject(R.id.statechange)
    private TextView statechange;

    private int progress = 0;
    //当前时间值
    private int roundnum = 90;
    //环形时间最大值
    int max = 90;
    @ViewInject(R.id.pretime)
    TextView pretime;
    @ViewInject(R.id.zhubutishentip)
    TextView zhubutishentip;
    @ViewInject(R.id.preround)
    RelativeLayout preround;
    @ViewInject(R.id.youxiaohuan)
    RelativeLayout youxiaohuan;
    int qqq = 0;

    private void setDatee() {

        RoundProgressBar bar = new RoundProgressBar(getApplicationContext(), 0);
        bar.setMmnun(max);
        stepTimeTV.setText("00:00:00");
        startTime = System.currentTimeMillis();
        if (progress == max) {
            LBound.setVisibility(View.VISIBLE);
            UBound.setVisibility(View.VISIBLE);

            if (!ShareUitls.getString(getApplicationContext(), "LBound", "null").equals("null")) {
                lBound = Integer.parseInt(ShareUitls.getString(getApplicationContext(), "LBound", "null"));
                LBound.setText(lBound + "");
            }

            if (!ShareUitls.getString(getApplicationContext(), "UBound", "null").equals("null")) {
                uBound = Integer.parseInt(ShareUitls.getString(getApplicationContext(), "UBound", "null"));
                UBound.setText(uBound + "");
            }
        } else {
            LBound.setVisibility(View.INVISIBLE);
            UBound.setVisibility(View.INVISIBLE);
            move.setBackgroundColor(Color.parseColor("#00000000"));
            out.setBackgroundColor(Color.parseColor("#00000000"));
            effect.setBackgroundColor(Color.parseColor("#00000000"));
        }


        Log.e("dadth", "onStart");
        //判断控件长度是否为空
        h.sendEmptyMessageDelayed(1, 1);
        mOnpaused = false;
        //总时间控制心率值为0不开始计时
           /*计时开始*/
        if (flag == 0) {
            h.sendEmptyMessage(9);
            flag = 1;
        } else {
            Log.e("roundnum", "2");
            statechange.setText("运动中");
            move.setBackgroundColor(Color.parseColor("#ffffff"));
            out.setBackgroundColor(Color.parseColor("#00000000"));
            effect.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        //pop右上角删除按钮
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(true, "确定停止运动并上传数据");
            }
        });
        //停止运动
        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pop.isShowing()) {
                    Log.e("rrr", mTotaltime + "。。。。。。 mTotaltime");
                    if (con_state != -1) {
                        showAlert(true, "确定停止运动并上传数据");
                    } else {
                        showAlert(false, "确定停止准备运动");
                    }

                }
            }
        });
        //
        bt_controller.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 if (!pop.isShowing()) {
                                                     if (con_state == -1) {
                                                         showAlert1(true, "确定跳过热身");
                                                     } else {
                                                         if (con_isstate) {
                                                             con_state = 1;
                                                             bt_controller.setText("暂停");
                                                             statechange.setText("运动中");
                                                             // mHandler.post(mRunnable);
                                                             intent.putExtra("con_state", 1);
                                                             sendBroadcast(intent);
                                                         } else {
                                                             con_state = 0;
                                                             intent.putExtra("con_state", 0);
                                                             sendBroadcast(intent);
                                                             bt_controller.setText("继续");
                                                             // mHandler.removeCallbacks(mRunnable);
                                                             statechange.setText("休息中");
                                                         }
                                                         con_isstate = !con_isstate;
                                                     }

                                                 }
                                             }
                                         }

        );
    }

    public void continueTest() {
        Log.e("isKouchued", "进入wait了");
        //处在有效外
        if (value <= lBound) {
            keepFive = false;
            if (!firstIn) {
                Log.e("rrrr", "1");
                //获取当前时间开始断线5秒计时
                start = System.currentTimeMillis();//记录第一次在有效外的时间起点  10秒
                firstIn = true;
            }
            //判断离开有效区域是否达到5秒
            if (pop.isShowing()) {
                Log.e("rrrr", "7.1");
                //已经显示过了，然后更新数据即可，如果30秒内没有达到有效运动范围那么进入扣除页面
                h.sendEmptyMessage(25);
            } else {

                ifFive = (System.currentTimeMillis() - start) / 1000;
                Log.e("rrrr", "2     " + ifFive);
                //离开有效区域达到5秒开始显示popWindow
                if (ifFive >= 5) {
                    Log.e("rrrr", "3");
                    //第一次显示popWindow
                    if (!pop.isShowing()) {
                        h.sendEmptyMessage(23);
                    }

                } else {

                }
            }
        } else {

            firstIn = false;
            //进入有效内，重新开始5秒外计时
            if (pop.isShowing()) {
                //如果已经有popWindow出现，开始计数在有效运动中是否保持了5秒
                if (!keepFive) {
                    //有效范围开始计数
                    keepFiveStart = System.currentTimeMillis();
                    keepFive = true;
                }
                feepifFive = (System.currentTimeMillis() - keepFiveStart) / 1000;
                if (feepifFive == 5) {
                    //在有效中达到5秒跳转到恢复界面
                    h.sendEmptyMessage(27);
                } else {
                    //没有达到5秒跳转到请保持5秒之内恢复界面
                    h.sendEmptyMessage(26);
                }
            } else {

            }
        }
    }

    int alltime;
    String content = "";
    int fineMinute = 0;

    private boolean setData_Update() {
        if (con_state == 2) {
            return true;
        }
        //   synchronized (AerobicSportActivity.this) {
        //  Log.e("xiancheng", "/计时器");
        if (fineMinute == 300) {
            overTask();
            return true;
        }
        if (isDismiss) {
            return true;
        }
        //断开即为0
        if (!isConnected) {
            h.sendEmptyMessage(4);
        } else {
        }
        Log.e("rrr", System.currentTimeMillis() - startTime + "");
        if (stoped) {
            value = 0;
            //return;
        }
        if (value <= uBound && value >= lBound) {
            if (value != uBound && value != lBound) {
                tenvibratro = 0;
            }
            vibrator.cancel();
            if (exceedMediaPlayer.isPlaying()) {
                exceedMediaPlayer.pause();
            }
            if (underMediaPlayer.isPlaying()) {
                underMediaPlayer.pause();
            }
            if (con_state == 1) {
                S_TOTAL_SEC++;
            }
        }
        if (value >= uBound || value <= lBound) {

            Log.i("KKKKKKKKKK", value + "  " + tenvibratro + "  " + uBound + "  " + lBound);
            if (con_state == 1) {
                if (tenvibratro % 10 == 0) {
                    // if(!vibrator.hasVibrator()){
                    vibrator.vibrate(3000);
                    // }
                    if (value >= uBound) {
                        if (!exceedMediaPlayer.isPlaying()) {
                            exceedMediaPlayer.start();
                        }

                    }
                    if (value <= lBound) {
                        if (!underMediaPlayer.isPlaying()) {
                            underMediaPlayer.start();
                        }
                    }
                } /*else {
                    if (exceedMediaPlayer.isPlaying()) {
                        exceedMediaPlayer.pause();
                    }
                    if (underMediaPlayer.isPlaying()) {
                        underMediaPlayer.pause();
                    }
                }*/
                ++tenvibratro;//超过范围 十秒震动
            } else if (con_state == 0) {
                vibrator.cancel();
                if (exceedMediaPlayer.isPlaying()) {
                    exceedMediaPlayer.pause();
                }
                if (underMediaPlayer.isPlaying()) {
                    underMediaPlayer.pause();
                }
            }

        }

        // showTime = showTime <= alltime ? showTime : alltime;
        // 用户只要在累计5s之后才开始显示时间
        showTime = S_TOTAL_SEC >= 5 ? S_TOTAL_SEC : 0;
        Log.e("kouchu", S_TOTAL_SEC + "未扣除的");
        String min = (showTime / 60) + "";
        String sec = (showTime % 60) + "";
        String time = min + "'" + sec + "''";
        mEffectiveTime.setText(time);
        if (con_state == -1) {//热生阶段
            ++alltime;
            progress = alltime;
            h.sendEmptyMessage(34);
            mRoundProgressBar2.setProgress(progress);
            if (progress == max) {
                Log.e("roundnum", "progress == roundnum");
                //跟换状态
                mDateArray.clear();
                mHeartArray.clear();
                //
                // mHandler.post(mRunnable);
                startTime = System.currentTimeMillis();

                alltime = 0;
                h.sendEmptyMessage(35);
            }
        }

        if (con_state == 1) {
            if (value != 0) {
                fineMinute = 0;
                ++alltime;
                if (alltime % 10 == 0) {
                    Data = ToJson.getRequestData(mDateArray, mHeartArray);
                    Log.i("AADDDDDDaaa", "" + Data);
                    saveNativeData(ShareUitls.getString(getApplicationContext(), "UID", "null"), Data, "1", (mTotaltime / 1000), showTime);
                }
            } else {
                ++fineMinute;

            }

        } else if (con_state == 0) {


        }
        /*if (popupWindoww.isShowing()) {
            popupWindoww.dismiss();
        }*/
        if (con_state != -1) {
            alltime = alltime <= showTime ? showTime : alltime;
        }
        content = StringForTime.stringForTime(alltime);
        mTotaltime = alltime * 1000;

        stepTimeTV.setText(content);
        Log.e("rrr", alltime + "wwwwwww" + mTotaltime + "  " + fineMinute);
        return false;
    }

    private long startTime = 0;
    private Boolean circleStop = false;
    int x0;
    int y0;
    int effectX;
    int effectY;
    int moveX;
    int moveY;
    int gap;
    int width;
    int effectlength;
    int outlength;


    int thirty = 50;
    int five = 5;
    //5秒计时弹出popWindow
    long start;
    long end;
    long ifFive;
    Boolean firstIn = false;
    Boolean keepFive = false;
    long feepifFive;
    //保持5秒的计时器
    long keepFiveStart;

    Boolean wait = false;
    List<Integer> datas = new ArrayList<>();


    public Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 200) {
                mEffectiveTime.setText(msg.obj.toString());
            }
            if (msg.what == 1) {
                if (out.getWidth() != 0 && out.getHeight() != 0 && effect.getWidth() != 0 && effect.getHeight() != 0 && up.getWidth() != 0 && up.getHeight() != 0 && low.getWidth() != 0 && low.getHeight() != 0 && drawline.getWidth() != 0 && drawline.getHeight() != 0) {
                    int[] location666 = new int[2];
                    drawline.getLocationOnScreen(location666);
                    x0 = location666[0];
                    y0 = location666[1];
                    gap = low.getTop() - up.getTop();
                    width = up.getWidth();
                    // 判断动态靶目标显示
                    //数值差
                    int xp = Integer.parseInt(ShareUitls.getString(getApplicationContext(), "UBound", "0")) - Integer.parseInt(ShareUitls.getString(getApplicationContext(), "LBound", "0"));
                    //位置像素差
                    int xxp = (gap * (Integer.parseInt(ShareUitls.getString(getApplicationContext(), "LBound", "0")) - 50)) / 200;
                    //高像素差
                    int xxp2 = (gap * xp) / 200;
                    setMargins(qujian, 0, 0, 0, xxp);
                    //设置高度
                    RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) qujian.getLayoutParams();
                    // 取控件aaa当前的布局参数
                    linearParams.height = xxp2; // 当控件的高
                    qujian.setLayoutParams(linearParams);

                    int[] locationmove = new int[2];
                    move.getLocationInWindow(locationmove);
                    moveX = locationmove[0];
                    moveY = locationmove[1];

                    int[] locationyouxiao = new int[2];
                    effect.getLocationInWindow(locationyouxiao);
                    effectX = locationyouxiao[0];
                    effectY = locationyouxiao[1];

                    effectlength = effect.getWidth();
                    outlength = out.getWidth();
                } else {
                    h.sendEmptyMessageDelayed(1, 1);
                }
            }
            if (msg.what == 3) {
                smallprogressCircle.setProgress((100 * (showTime)) / (60 * Integer.parseInt(ShareUitls.getString(getApplicationContext(), "Target", "1"))));
            }
            if (msg.what == 4) {

                tra(0, 0);
                value = 0;
                if (!clicked) {
                    mBmp.setText("- -");
                }
            }
            if (msg.what == 5) {
                StartStopScan(true);
            }
            if (msg.what == 7) {
                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 8) {
                Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 100) {
                Toast.makeText(AerobicSportActivity.this, "本次没有效运动数据", Toast.LENGTH_LONG).show();
                // showDialog(false);
            }

            if (msg.what == 9) {
                //  timeCount();
            }
            if (msg.what == 11) {
                //运动小节结果显示
                stoped = true;
                int sum = 0;
                for (int p = 0; p < thirdData.size(); p++) {
                    sum = sum + thirdData.get(p);
                }
                if (thirdData.size() != 0) {
                    mBmp.setText(sum / thirdData.size() + "");
                } else {
                    mBmp.setText("0");
                }
                titlemark.setText("  平均心率  ");
                titlemark.setTextColor(Color.parseColor("#836313"));
                mBmp.setTextColor(Color.parseColor("#000000"));
                out.setBackgroundColor(Color.parseColor("#FFb809"));
                btback.setVisibility(View.VISIBLE);
                btback.setClickable(true);
                sportover = true;

                for (int p = 0; p < thirdData.size(); p++) {
                    Log.e("aaaa", thirdData.size() + ";;;;" + thirdData.get(p));
                    drawline.removeView(d);
                    drawline.addView(new draw2(getApplicationContext(), thirdData));
                }
            }
            if (msg.what == 22) {
            }
            if (msg.what == 23) {
                //一开始就让pop实例化完成
                if (con_state == 1) {
                    pop.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
            if (msg.what == 25) {
                five = 5;
                if (thirty != 0) {
                    Log.e("tttt", "7.2");
                    Log.e("kouchu", ifFive + "在30秒计时当中");
                    holder.thirtyCount.setText((--thirty) + "秒");
                    holder.img_state.setVisibility(View.VISIBLE);
                    holder.line.setVisibility(View.VISIBLE);
                    holder.huifu.setVisibility(View.INVISIBLE);
                    holder.img_state.setBackground(getResources().getDrawable(R.drawable.a));
                    holder.thirtyCount.setVisibility(View.VISIBLE);
                } else {
                    //到达扣除页面。
                    // if (S_TOTAL_SEC != 0) {
                    Log.e("tttt", "7.3");
                    thirty = 50;
                    five = 5;
                    firstIn = false;

                       /* if (S_TOTAL_SEC != 0) {
                            S_TOTAL_SEC = S_TOTAL_SEC - (int) (S_TOTAL_SEC * 0.2);
                            Log.e("kouchu", S_TOTAL_SEC + "kouchule ,,,,,");
                        }*/
                    // img_state.setBackground(getResources().getDrawable(R.drawable.kouchu));
                    // thirtyCount.setText("20%");
                    wait = true;
                    h.sendEmptyMessageDelayed(28, 3000);
                    //  } else {
                    //第一次S_TOTAL_SEC等于0不扣除
                    pop.dismiss();

                    //  }


                }
            }

            if (msg.what == 26) {
                thirty = 50;
                if (five != 0) {
                    holder.thirtyCount.setText((five--) + "秒");
                    holder.img_state.setBackground(getResources().getDrawable(R.drawable.keep));
                } else {
                    h.sendEmptyMessage(27);
                }
            }
            if (msg.what == 27) {
                thirty = 50;
                five = 5;
                holder.img_state.setVisibility(View.GONE);
                holder.line.setVisibility(View.GONE);
                holder.huifu.setVisibility(View.VISIBLE);
                holder.huifu.setBackground(getResources().getDrawable(R.drawable.huifu));
                holder.thirtyCount.setVisibility(View.INVISIBLE);
                h.sendEmptyMessageDelayed(28, 3000);
            }
            if (msg.what == 28) {
                Log.e("rrrr", "7.4");
                thirty = 50;
                holder.thirtyCount.setText((--thirty) + "秒");
                holder.img_state.setVisibility(View.VISIBLE);
                holder.line.setVisibility(View.VISIBLE);
                holder.huifu.setVisibility(View.INVISIBLE);
                holder.img_state.setBackground(getResources().getDrawable(R.drawable.a));
                holder.thirtyCount.setVisibility(View.VISIBLE);
                pop.dismiss();
            }
            if (msg.what == 29) {
                if (!clicked) {
                    titlemark.setTextColor(Color.parseColor("#ff0000"));
                    titlemark.setText("警告:超出上限");
                    mBmp.setTextColor(Color.parseColor("#ff0000"));
                    out.setBackgroundColor(Color.parseColor("#ff0000"));
                }

            }
            if (msg.what == 31) {
                titlemark.setTextColor(Color.parseColor("#836313"));
                titlemark.setText("  当前心率  ");
                mBmp.setTextColor(Color.parseColor("#000000"));
                out.setBackgroundColor(Color.parseColor("#FFb809"));
            }
            if (msg.what == 30) {
                reConned();
            }
            if (msg.what == 32) {
                Toast.makeText(getApplicationContext(), "本次没有运动数据", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 33) {
                if (pop.isShowing()) {
                    pop.dismiss();
                }
                AlertDialog dialog = (AlertDialog) msg.obj;
                dialog.dismiss();
                // showDialog(true);
            }
            if (msg.what == 34) {
                //  progress=progress<=alltime?progress:alltime;
                pretime.setText((progress) + "");
            }
            if (msg.what == 35) {


                Log.i("YYYYYYYYYY", "kaishi运动了");


                con_state = 1;

                statechange.setText("运动中");
                bt_controller.setText("暂停");

                intent.putExtra("con_state", 1);
                sendBroadcast(intent);

                zhubutishentip.setVisibility(View.INVISIBLE);
                LBound.setVisibility(View.VISIBLE);
                UBound.setVisibility(View.VISIBLE);
                move.setBackgroundColor(Color.parseColor("#ffffff"));
                out.setBackgroundColor(Color.parseColor("#00000000"));
                effect.setBackgroundColor(Color.parseColor("#ffffff"));
                preround.setVisibility(View.GONE);
                youxiaohuan.setVisibility(View.VISIBLE);
            }
            if (msg.what == 105) {
                if (msg.arg1 == 1) {
                    popupWindoww.showAtLocation(new View(AerobicSportActivity.this), Gravity.CENTER, 0, 0);
                    // popupWindoww.update();
                } else if (msg.arg1 >= 15 * 60) {
                    overTask();
                } else if (msg.arg1 >= 5) {
                    if (popupWindoww.isShowing()) {
                        popupWindoww.dismiss();
                    }
                }
            }
        }

    };

    private void overTask() {
        underMediaPlayer.stop();
        underMediaPlayer.release();
        exceedMediaPlayer.stop();
        exceedMediaPlayer.release();
        if (pop.isShowing()) {
            pop.dismiss();
        }
        stopService(new Intent(AerobicSportActivity.this, FakePlayerService.class));
        //按钮点击标志
        clicked = true;
        //断开连接

        if (bleService != null)
            // 解除注册广播
            AerobicSportActivity.this.unregisterReceiver(gattUpdateReceiver);
        isUnregisted = true;
        //youxianFlag = true;
        bleService = null;
        if (isBanded) {
            //解除绑定服务
            AerobicSportActivity.this.unbindService(serviceConnection);
            isBanded = false;
        }
        //停止计时器


        //如果准备运动停止了就tag为true
        if (true) {


            isUpdate = false;//数据正常上传过
            con_state = 2;
            intent.putExtra("con_state", 2);
            sendBroadcast(intent);
            statechange.setText("运动小结");
            move.setBackgroundColor(Color.parseColor("#00000000"));
            out.setBackgroundColor(Color.parseColor("#00000000"));
            effect.setBackgroundColor(Color.parseColor("#00000000"));
            LBound.setVisibility(View.INVISIBLE);
            UBound.setVisibility(View.INVISIBLE);
            re.setVisibility(View.GONE);
            mEnd = System.currentTimeMillis();

    /*        Log.e("pinjie", pinjie(mDateArray, mHeartArray).getBytes().length + "原始"+pinjie(mDateArray, mHeartArray));*/
            Data = ToJson.getRequestData(mDateArray, mHeartArray);

      /*      Log.e("pinjie", ToJson.getRequestData(mDateArray, mHeartArray).getBytes().length + "不加密"+ToJson.getRequestData(mDateArray, mHeartArray));
            Log.e("pinjie", Base64.encodeToString(ToJson.getRequestData(mDateArray, mHeartArray).getBytes(), Base64.DEFAULT).getBytes().length + "加密"+ Base64.encodeToString(ToJson.getRequestData(mDateArray, mHeartArray).getBytes(), Base64.DEFAULT));
     */
            if (mHeartArray.size() > 33) {
                int cha = mHeartArray.size() / 33;
                Log.e("aaaa", cha + "" + mHeartArray.size());
                for (int p = 0; p < mHeartArray.size(); p++) {
                    if (p < mHeartArray.size()) {
                        if (thirdData.size() <= 30) {
                            thirdData.add(mHeartArray.get(p));
                        }
                    } else {
                    }
                    p = p + cha;
                }
            } else {
                thirdData = mHeartArray;
            }
            h.sendEmptyMessage(11);
            Log.e("rrr", showTime + "showTime" + thirdData.size() + "thirdData.size()" + mHeartArray.size() + "mHeartArray.size()");
            if ((mTotaltime / 1000) == 0) {
                h.sendEmptyMessage(32);




            } else {
               /* new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {*/
                if (mHeartArray.size() == 0 || mTotaltime == 0) {
                    Log.e("BBBBBB", "111111111111");
                    h.sendEmptyMessage(100);
                } else {
                    Log.e("BBBBBB", "333333333");
                    Log.i("AAAAAAAAAA", Data.toString());
                    // xutilsGet(ShareUitls.getString(getApplicationContext(), "UID", "null"), Data, "1", (mTotaltime / 1000) + "", showTime + "");
                    //  upDate(ShareUitls.getString(getApplicationContext(), "UID", "null"), Data, "1", (mTotaltime / 1000), showTime);

                    UpDate(ShareUitls.getString(getApplicationContext(), "UID", "null"), Data, "1", (mTotaltime / 1000), showTime);

                }
               /*         } catch (Throwable throwable) {
                            Log.e("post", "？？？？？？？");
                            throwable.printStackTrace();
                        }
                    }
                }).start();*/
            }
        } else {
         /*   if (MainActivity.Activity != null) {
                MainActivity.Activity.finish();
            }*/
            startActivity(new Intent(AerobicSportActivity.this, MainActivity.class));
            finish();
        }
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    int valuetemp = 0;
    //控制停止运行
    Boolean stoped = false;

    //运动中目标动画
    public void tra(final int first, final int last) {
        Log.e("test", "first:::" + first + "las::::" + last);
        ta1 = new TranslateAnimation(
                first, last,
                0, 0);
        ta1.setDuration(1000);
        ta1.setStartTime(0);
        ta1.setRepeatCount(Integer.MAX_VALUE);
          /*    ta1.setRepeatMode(Animation.REVERSE);*/
        move.startAnimation(ta1);
        valuetemp = last;
    }

    @Override
    public void onClick(View v) {

    }


    //停止运动静态折现图
    public class draw2 extends View {
        int x = 0;
        int y = 0;

        public draw2(Context context, final List<Integer> thirdData) {
            super(context);
            setWillNotDraw(false);

        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#FFF68F"));
            paint.setStrokeWidth((float) 5.0);
            paint.setTextSize(20);

            Log.e("aaaa", thirdData.size() + "onDraw" + "");
            if (thirdData.size() > 1) {
                Path path = new Path();
                path.moveTo(0, gap);
                for (int i = 0; i < thirdData.size(); i++) {
                    /*  异常？？？？？？？？？？？*/
                    path.lineTo(i * (width / 30), (gap / 4 + gap) - ((thirdData.get(i) * (gap / 4 + gap)) / 250));
                /*    paint.setShader(new LinearGradient(i *( width/30), (gap - (datas.get(i)*gap))/150, i *( width/30), gap, Color.RED, Color.YELLOW, Shader.TileMode.REPEAT));*/
                }
                path.lineTo((thirdData.size() - 1) * (width / 30), gap);
                canvas.drawPath(path, paint);
            }
            invalidate();
        }
    }


    //运行中动态折现图
    private draw d;
    //记录数据的临时集合
    private List<Integer> temp = new ArrayList<>();

    public class draw extends View {
        int x = 0;
        int y = 0;

        public draw(Context context) {
            super(context);
            setWillNotDraw(false);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //  Log.e("0000", x + "开始画了---22222" + y + "开始画了");
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setAntiAlias(true);
            paint.setColor(Color.parseColor("#FFF68F"));
            paint.setStrokeWidth((float) 5.0);
            paint.setTextSize(20);
            if (datas.size() > 1) {
                Path path = new Path();
                path.moveTo(0, gap);
                for (int i = 0; i < datas.size(); i++) {
                    path.lineTo(i * (width / 30), (gap / 4 + gap) - ((datas.get(i) * (gap / 4 + gap)) / 250));
                /*    paint.setShader(new LinearGradient(i *( width/30), (gap - (datas.get(i)*gap))/150, i *( width/30), gap, Color.RED, Color.YELLOW, Shader.TileMode.REPEAT));*/
                }
                path.lineTo((datas.size() - 1) * (width / 30), gap);
                canvas.drawPath(path, paint);
            }
            invalidate();
        }
    }

    private boolean setDraw() {
        // mChartView.AddPointToList(value);


        Log.e("xiancheng", "/drawwwww");
        if (clicked) {
            return true;
        }

        if (circleStop) {
            showTime = 0;
            return true;
        }
        if (datas.size() <= 30) {
            if (temp.size() <= 5) {
                temp.add(value);
            }
            datas.add(value);
        } else {
            //mOnpaused后就不进行刷新
            if (mOnpaused) {

            } else {
                datas.remove(0);
                datas.add(value);
            }
        }
        h.sendEmptyMessage(3);
        return false;
    }

    public void showAlert(final Boolean tag, final String msg) {
        dialog = new AlertDialog.Builder(AerobicSportActivity.this);
        dialog.setMessage(msg + "？")//设置显示的内容
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        Message msg = h.obtainMessage();
                        msg.what = 33;
                        msg.obj = dialog;
                        h.sendMessage(msg);
                        //按钮点击标志
                        clicked = true;
                        //断开连接

                        if (bleService != null)
                            // 解除注册广播
                            AerobicSportActivity.this.unregisterReceiver(gattUpdateReceiver);
                        isUnregisted = true;
                        //移除环形进度条线程

                        // youxianFlag = true;
                        bleService = null;
                        if (isBanded) {
                            //解除绑定服务
                            AerobicSportActivity.this.unbindService(serviceConnection);
                            isBanded = false;
                        }


                        //如果准备运动停止了就tag为true
                        if (tag) {
                            underMediaPlayer.stop();
                            underMediaPlayer.release();
                            exceedMediaPlayer.stop();
                            exceedMediaPlayer.release();
                            stopService(new Intent(AerobicSportActivity.this, FakePlayerService.class));
                            con_state = 2;
                            intent.putExtra("con_state", 2);
                            sendBroadcast(intent);
                            isUpdate = false;//数据正常上传过
                            statechange.setText("运动小结");
                            move.setBackgroundColor(Color.parseColor("#00000000"));
                            out.setBackgroundColor(Color.parseColor("#00000000"));
                            effect.setBackgroundColor(Color.parseColor("#00000000"));
                            LBound.setVisibility(View.INVISIBLE);
                            UBound.setVisibility(View.INVISIBLE);


                            lock_relativeLayout.setVisibility(View.GONE);
                            re.setVisibility(View.GONE);
                            mEnd = System.currentTimeMillis();

                /*        Log.e("pinjie", pinjie(mDateArray, mHeartArray).getBytes().length + "原始"+pinjie(mDateArray, mHeartArray));*/
                            Data = ToJson.getRequestData(mDateArray, mHeartArray);

                  /*      Log.e("pinjie", ToJson.getRequestData(mDateArray, mHeartArray).getBytes().length + "不加密"+ToJson.getRequestData(mDateArray, mHeartArray));
                        Log.e("pinjie", Base64.encodeToString(ToJson.getRequestData(mDateArray, mHeartArray).getBytes(), Base64.DEFAULT).getBytes().length + "加密"+ Base64.encodeToString(ToJson.getRequestData(mDateArray, mHeartArray).getBytes(), Base64.DEFAULT));
                 */
                            if (mHeartArray.size() > 33) {
                                int cha = mHeartArray.size() / 33;
                                Log.e("aaaa", cha + "" + mHeartArray.size());
                                for (int p = 0; p < mHeartArray.size(); p++) {
                                    if (p < mHeartArray.size()) {
                                        if (thirdData.size() <= 30) {
                                            thirdData.add(mHeartArray.get(p));
                                        }
                                    } else {
                                    }
                                    p = p + cha;
                                }
                            } else {
                                thirdData = mHeartArray;
                            }
                            h.sendEmptyMessage(11);
                            Log.e("rrr", showTime + "showTime" + thirdData.size() + "thirdData.size()" + mHeartArray.size() + "mHeartArray.size()");
                            if ((mTotaltime / 1000) == 0) {
                                h.sendEmptyMessage(32);
                            } else {
                               /* new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {*/
                                if (mHeartArray.size() == 0 || mTotaltime == 0) {
                                    Log.e("BBBBBB", "111111111111");
                                    h.sendEmptyMessage(100);
                                } else {
                                    Log.e("BBBBBB", "333333333");
                                    Log.i("AAAAAAAAAA", Data.toString());
                                    //  xutilsGet(ShareUitls.getString(getApplicationContext(), "UID", "null"), Data, "1", (mTotaltime / 1000) + "", showTime + "");
                                    // upDate(ShareUitls.getString(getApplicationContext(), "UID", "null"), Data, "1", (mTotaltime / 1000), showTime);
                                    UpDate(ShareUitls.getString(getApplicationContext(), "UID", "null"), Data, "1", (mTotaltime / 1000), showTime);


                                }
                                    /*    } catch (Throwable throwable) {
                                            Log.e("post", "？？？？？？？");
                                            throwable.printStackTrace();
                                        }
                                    }
                                }).start();*/
                            }
                        } else {//停止准备运动退出
                            finish();
                        }

                    }

                }).show();
    }

    public void showAlert1(final Boolean tag, final String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AerobicSportActivity.this);
        dialog.setMessage(msg + "？")//设置显示的内容
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        alltime = 0;
                        progress = max;
                        Log.i("roundnumqqqqqqqqqqqqqq", progress + "  " + max);
                        mDateArray.clear();
                        mHeartArray.clear();
                        //  mHandler.post(mRunnable);
                        startTime = System.currentTimeMillis();
                        h.sendEmptyMessage(35);
                    }

                }).show();
    }


    private upCallBack upBack;
    Gson g = new Gson();
    private List<Integer> thirdData = new ArrayList<>();
    private boolean isOverSport;//是否完成运动过 如果分享等操作出现奔溃 本页面会出现在栈顶 并且重新调用oncreat 方法 此处用来标记此种情况


    private void UpDate(final String UID, final String Data, final String WatchType, final int EveryTime, final int EveryVolidTime) {
        share.setVisibility(View.VISIBLE);
        isOverSport = true;
        if (EveryTime != 0) {
            if (InternetUtils.internett(AerobicSportActivity.this)) {
                initDialog(AerobicSportActivity.this);
                waitDialog.setMessage("正在上传,请稍后...");
                waitDialog.showDailog();
                // String url = Constant.BASE_URL + "/MdMobileService.ashx?do=PostWxPayRequest";
                final String urlPath = new String(Constant.BASE_URL + "/MdMobileService.ashx?do=PostSportDataRequest");

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            String param = "UID=" + URLEncoder.encode(UID, "UTF-8")
                                    + "&Data=" + URLEncoder.encode(Data, "UTF-8")
                                    + "&UploadTime=" + URLEncoder.encode(UploadTime, "UTF-8")
                                    + "&WatchType=" + URLEncoder.encode(WatchType, "UTF-8")
                                    + "&EveryTime=" + EveryTime
                                    + "&EveryVolidTime=" + EveryVolidTime
                                    + "&ResultJWT=" + ShareUitls.getString(AerobicSportActivity.this, "ResultJWT", "0")
                                    + "&VersionNum=" + VersonUtils.getVersionName(AerobicSportActivity.this);
                            //建立连接
                            URL url = new URL(urlPath);
                            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                            //设置参数
                            httpConn.setDoOutput(true);   //需要输出
                            httpConn.setDoInput(true);   //需要输入
                            httpConn.setUseCaches(false);  //不允许缓存
                            httpConn.setRequestMethod("POST");   //设置POST方式连接
                            //设置请求属性
                            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                            httpConn.setRequestProperty("Charset", "UTF-8");
                            //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
                            httpConn.connect();
                            //建立输入流，向指向的URL传入参数
                            DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
                            dos.writeBytes(param);
                            dos.flush();
                            dos.close();
                            //获得响应状态
                            int resultCode = httpConn.getResponseCode();
                            //
                            Log.i("AAOrderNO111aa", resultCode + "");
                            if (HttpURLConnection.HTTP_OK == resultCode) {
                                StringBuffer sb = new StringBuffer();
                                String readLine = new String();
                                BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                                while ((readLine = responseReader.readLine()) != null) {
                                    sb.append(readLine).append("\n");
                                }
                                responseReader.close();

                                Message message = Message.obtain();
                                message.obj = g.fromJson(sb.toString(), upCallBack.class);
                                message.what = 1;
                                upDatehandler.sendMessage(message);
                            } else {

                              /*  List<Object> list=new ArrayList<Object>();
                                list.add(UID);
                                list.add(Data);
                                list.add(WatchType);
                                list.add(EveryTime);
                                list.add(EveryVolidTime);*/

                                Message message = Message.obtain();
                                //message.obj = list;
                                message.what = 2;
                                upDatehandler.sendMessage(message);

                            }


                        } catch (Exception e) {
                        /*    List<Object> list=new ArrayList<Object>();
                            list.add(UID);
                            list.add(Data);
                            list.add(WatchType);
                            list.add(EveryTime);
                            list.add(EveryVolidTime);*/
                            Message message = Message.obtain();
                            //  message.obj = list;
                            message.what = 2;
                            upDatehandler.sendMessage(message);
                            Log.i("AAOrderNO111aa", e.toString());

                        }

                    }

                }.start();
            } else {
                saveNativeData(UID, Data, WatchType, EveryTime, EveryVolidTime);
                Toast.makeText(AerobicSportActivity.this, "当前无网络连接", Toast.LENGTH_LONG).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("当前无网络连接，是否前去设置网络？")//设置显示的内容
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
// 跳转到系统的网络设置界面
                                Intent intent = null;
                                // 先判断当前系统版本
                                if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                                    intent = new Intent(Settings.ACTION_SETTINGS);
                                } else {
                                    intent = new Intent();
                                    intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                                }
                                startActivityForResult(intent, 1234);


                            }

                        }).show();

            }
        } else {
            Toast.makeText(AerobicSportActivity.this, "没有运动数据", Toast.LENGTH_LONG).show();
        }

    }

    Handler upDatehandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (waitDialog != null) {
                waitDialog.dismissDialog();
            }

            if (msg.what == 1) {
                upCallBack upBack = (upCallBack) msg.obj;
                if (upBack.getErrCode() != null && (upBack.getErrCode().toString().equals("601") || upBack.getErrCode().toString().equals("600"))) {

                    if (upBack.getErrCode().toString().equals("601")) {
                        Toast.makeText(AerobicSportActivity.this, "您的账号已在其他设备登录", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AerobicSportActivity.this, "您的登录信息已过期", Toast.LENGTH_LONG).show();
                    }
                    saveNativeData(ShareUitls.getString(getApplicationContext(), "UID", "null"), Data, "1", (mTotaltime / 1000), showTime);
                    stopService(new Intent(AerobicSportActivity.this, FakePlayerService.class));
                    //按钮点击标志
                    clicked = true;
                    //断开连接

                    if (bleService != null)
                        // 解除注册广播

                        isUnregisted = true;
                    //youxianFlag = true;
                    bleService = null;

                    if (isBanded) {
                        //解除绑定服务
                        AerobicSportActivity.this.unbindService(serviceConnection);
                        isBanded = false;
                    }

                    Intent i = new Intent(AerobicSportActivity.this, Login.class);
                    startActivity(i);
                    ShareUitls.putString(AerobicSportActivity.this, "questionnaire", "1");//设置首页刷新
                    ShareUitls.putString(AerobicSportActivity.this, "maidong", "1");//设置首页刷新
                    ShareUitls.putString(AerobicSportActivity.this, "analize", "1");//分析重新刷新
                  /*  if (MainActivity.Activity != null) {
                        MainActivity.Activity.finish();
                    }*/
                    finish();

                } else {

                    ShareUitls.putString(AerobicSportActivity.this, "maidong", "1");//首页界面新刷新
                    ShareUitls.putString(AerobicSportActivity.this, "analize", "1");//分析界面重新刷新
                    ShareUitls.cleanString2(AerobicSportActivity.this);


                    Log.i("AAOrderNO111aaAA", upBack.toString());
                    if (upBack.getStatus() == 1) {
                        Toast.makeText(getApplicationContext(), "数据上传成功", Toast.LENGTH_SHORT).show();

                    } else if (upBack.getStatus() == 2) {
                        Toast.makeText(getApplicationContext(), "数据异常,上传失败", Toast.LENGTH_SHORT).show();
                    }

                }
            } else {
                //  List<Object> list=(List<Object> )msg.obj;
                Toast.makeText(getApplicationContext(), "上传数据失败,请确认网络连接", Toast.LENGTH_SHORT).show();
                saveNativeData(ShareUitls.getString(getApplicationContext(), "UID", "null"), Data, "1", (mTotaltime / 1000), showTime);


                //   saveNativeData(list.get(0).toString(), list.get(1).toString(), list.get(2).toString(), (int)list.get(3), (int)list.get(4));
            }

        }

    };

    private void upDate(final String UID, final String Data, final String WatchType, final int EveryTime, final int EveryVolidTime) {
        //showDialog(true);

        Log.i("updateeeeeeeeee", "  UID=  " + UID + "    Data= " + Data + "  WatchType=  " + WatchType + "    EveryTime= " + EveryTime + "   EveryVolidTime= " + EveryVolidTime + "  ");

        if (EveryTime != 0) {
            if (InternetUtils.internett(AerobicSportActivity.this)) {
                waitDialog.showDailog();
                // ShareUitls.putString(getApplicationContext(), "hasNewDate", "1");
                //在这里设置需要post的参数
                RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostSportDataRequest");
                params.addBodyParameter("ResultJWT", ShareUitls.getString(AerobicSportActivity.this, "ResultJWT", "0"));
                params.addBodyParameter("UID", UID);
                params.addBodyParameter("Data", Data);
                params.addBodyParameter("WatchType", WatchType);
                params.addBodyParameter("EveryTime", EveryTime + "");
                params.addBodyParameter("EveryVolidTime", EveryVolidTime + "");
                params.addBodyParameter("UploadTime", UploadTime);

                HttpUtils.getInstance(AerobicSportActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {


                            //  Log.e("update", Data + "Data" + WatchType + "WatchType" + EveryTime + "EveryTime" + EveryVolidTime + "EveryVolidTime");


                            //  upDate(ShareUitls.getString(getApplicationContext(), "UID", "null"), Data, "1", (mTotaltime / 1000) + "", showTime + "");

                            //   Log.e("pinjie", ShareUitls.getString(getApplicationContext(), "UID", "null") + "----" + Data + "----" + WatchType + "---" + EveryTime + "---" + EveryVolidTime + "---");

                            @Override
                            public void onResponse(String response) {
                                ShareUitls.putString(AerobicSportActivity.this, "maidong", "1");//首页界面新刷新
                                ShareUitls.putString(AerobicSportActivity.this, "analize", "1");//分析界面重新刷新
                                waitDialog.dismissDialog();
                                ShareUitls.cleanString2(AerobicSportActivity.this);
                                //{"Status":2,"Message":null,"IsSuccess":false,"IsError":true,"ErrMsg":"参数错误：SPID为空","ErrCode":"500"}
                                Log.e("WWWWWWWW", response.toString());
                                upBack = g.fromJson(response.toString(), upCallBack.class);
                                if (upBack.getStatus() == 1) {
                                    Toast.makeText(getApplicationContext(), "数据上传成功", Toast.LENGTH_SHORT).show();

                                } else if (upBack.getStatus() == 2) {
                                    Toast.makeText(getApplicationContext(), "数据异常,上传失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onErrorResponse(Throwable ex) {
                                waitDialog.dismissDialog();

                                Toast.makeText(getApplicationContext(), "上传数据失败,请确认网络连接", Toast.LENGTH_SHORT).show();
                                saveNativeData(UID, Data, WatchType, EveryTime, EveryVolidTime);
                                return;

                            }
                        }

                );
            } else {
                saveNativeData(UID, Data, WatchType, EveryTime, EveryVolidTime);
                Toast.makeText(AerobicSportActivity.this, "当前无网络连接", Toast.LENGTH_LONG).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("当前无网络连接，是否前去设置网络？")//设置显示的内容
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
// 跳转到系统的网络设置界面
                                Intent intent = null;
                                // 先判断当前系统版本
                                if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                                    intent = new Intent(Settings.ACTION_SETTINGS);
                                } else {
                                    intent = new Intent();
                                    intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                                }
                                startActivityForResult(intent, 1234);


                            }

                        }).show();

            }
        } else {
            Toast.makeText(AerobicSportActivity.this, "没有运动数据", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {//
            if (InternetUtils.internett(this)) {
                UpDate(ShareUitls.getString(getApplicationContext(), "UID", "null"), Data, "1", (mTotaltime / 1000), showTime);
            } else {
                Toast.makeText(AerobicSportActivity.this, "当前无网络连接", Toast.LENGTH_LONG).show();
            }
        }

    }

    //本地保存数据
    private void saveNativeData(String UID, String Data, String WatchType, int EveryTime, int EveryVolidTime) {
        ShareUitls.cleanString2(AerobicSportActivity.this);
        Log.i("WWWWWWWWWW", "正在保存数据");
        // Toast.makeText(getApplicationContext(), "正在保存数据...", Toast.LENGTH_SHORT).show();
        ShareUitls.putSportString(AerobicSportActivity.this, "SportID", UID + "");
        ShareUitls.putSportString(AerobicSportActivity.this, "Data", Data + "");
        ShareUitls.putSportString(AerobicSportActivity.this, "WatchType", WatchType + "");
        ShareUitls.putSportString(AerobicSportActivity.this, "EveryTime", EveryTime + "");
        ShareUitls.putSportString(AerobicSportActivity.this, "EveryVolidTime", EveryVolidTime + "");
        ShareUitls.putSportString(AerobicSportActivity.this, "UploadTime", UploadTime);
        waitDialog.dismissDialog();
    }

    boolean result = false;

    @Override
    public void onResume() {
        super.onResume();
        mOnpaused = false;

        ShareUitls.putString(AerobicSportActivity.this, "AerobicSportActivityistop", "yes");//用来记录当前界面是否处于最前端  当处于最前端是  接收到友盟推送的消息 点击不进入 消息详情
        Log.e("dadth", "onResume;;;");
        //添加动态折线
        if (d == null) {
            d = new draw(getApplicationContext());
            drawline.addView(d);
        }


        if (!isStop) {
            isStop = false;
        }
        //注册广播
        this.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter());
        if (bleService != null) {
            result = bleService.connect(deviceAddress);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("dadth", "onPause;;;");
        isStop = false;
        mOnpaused = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        mOnpaused = true;
        Log.e("dadth", "onStop();;;");
    }

    boolean crash = true;
    boolean isUpdate = true;

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShareUitls.putString(AerobicSportActivity.this, "AerobicSportActivityistop", "");//用来记录当前界面是否处于最前端  当处于最前端是  接收到友盟推送的消息 点击不进入 消息详情
        stopService(new Intent(AerobicSportActivity.this, FakePlayerService.class));
        con_state = 2;//结束POP线程
        intent.putExtra("con_state", 2);
        sendBroadcast(intent);
        try {
            unregisterReceiver(receiver);
            unregisterReceiver(ServiceToActivityReceiver);
        } catch (Exception e) {
        }

        receiver = null;
        isConnected = true;
        StartStopScan(true);

        mOnpaused = true;
        Log.e("dadth", "onDestroy();;;" + crash);

        stoped = true;
        S_TOTAL_SEC = 0;
        showTime = 0;
        circleStop = true;


        //断开连接
        if (bleService != null) {
            bleService.disconnect();
        }


        //如果没有点击上传那么就会执行解除广播
        if (!clicked) {
            AerobicSportActivity.this.unregisterReceiver(gattUpdateReceiver);
        }

        // 解除注册广播
        if (!isUnregisted) {
       /*     AerobicSportActivity.this.unregisterReceiver(gattUpdateReceiver);*/

            bleService = null;

        }

        if (isBanded) {
            //解除绑定服务
            AerobicSportActivity.this.unbindService(serviceConnection);
            isBanded = false;
        }
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bleService = ((BleService.LocalBinder) service).getService();
            if (!bleService.initialize()) {
//                finish();


            }
            //断线重连可能会用得到暂时先保留。。。。。。。。
            bleService.connect(deviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bleService = null;
        }
    };
    private int value = 0;
    private long mEnd = 0;
    private int mTotaltime = 0;
    private int showTime = 0;
    private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("intent", intent + "intent：：：;;;");
            final String action = intent.getAction();
            if (BleService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.e("dadth", "ACTION_GATT_CONNECTED：：：;;;");
            } else if (BleService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.e("dadth", "ACTION_GATT_DISCONNECTED：：：;;;");
                //广播接受失败之后就让心率值为0，并且开始重新扫描等待重连
                isConnected = false;
                //重新扫描
                StartStopScan(false);

            } else if (BleService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.e("dadth", "ACTION_GATT_SERVICES_DISCOVERED：：：;;;");
                /*会有bug.........*/
                try {
                    displayGattServices(bleService.getSupportedGattServices());
                } catch (NullPointerException n) {
                }
                enableHeartRateSensor();
            } else if (BleService.ACTION_DATA_AVAILABLE.equals(action)) {
                String data = intent.getStringExtra(BleService.EXTRA_TEXT);
                Log.e("dadth", "ACTION_DATA_AVAILABLE：：：;;;");
                //断开就扫描
                isConnected = true;
                //停止扫描
                StartStopScan(true);
                //设置当前的心跳数是多少、实现水平动画移动标尺
                value = Float.valueOf(data).intValue();


                Log.e("test", "value;;;" + value + "valuetemp;;;" + valuetemp + "---------------(value*youxiaolength)/100" + (value * effectlength) / 100);
                if (value < Integer.parseInt(LBound.getText().toString().trim())) {
                    tra(0, 0);
                } else if (value > Integer.parseInt(UBound.getText().toString().trim())) {
                    tra(valuetemp, outlength - 20);
                } else {
                    tra(valuetemp, (effectX - moveX) + (value - Integer.parseInt(LBound.getText().toString().trim())) * effectlength / (Integer.parseInt(UBound.getText().toString().trim()) - Integer.parseInt(LBound.getText().toString().trim())) - 10);
                }
                mBmp.setText(value + "");
                //保存心率值跟采集的时间获取12小时制
                String curTime = DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString();
                Log.e("test", curTime + "计时器。。。。。。 ");
                // [{"SportTime":"2015-07-20 06:21:16","HeartRate":106},{"SportTime":"2015-07-20 06:21:16","HeartRate":120},{"SportTime":"2015-07-20 06:21:16","HeartRate":130}]
                if (value > 30) {
                    //字符串String获取24小时制
                    if (mDateArray.size() != 0) {
                        if (!mDateArray.get((mDateArray.size() - 1)).equals(df.format(new Date()))) {
                            if (con_state == 1) {
                                mDateArray.add(df.format(new Date()));
                                //整型Int
                                mHeartArray.add(value);
                            }
                        }
                    } else {
                        if (con_state == 1) {
                            mDateArray.add(df.format(new Date()));
                            //整型Int
                            mHeartArray.add(value);
                        }
                    }
                }
            }
        }
    };


    //拼接json字符串.
    //{"SportTime":"2015-07-20 06:21:16","HeartRate":106}
    String sum = "";
    String Last = ",";

    public String pinjie(ArrayList<String> mDateArray, ArrayList<Integer> mHeartArray) {
        for (int i = 0; i < mDateArray.size(); i++) {
            if (i == mDateArray.size() - 1) {
                sum = sum + ("{" + "\"" + "SportTime" + "\"" + ":" + "\"" + mDateArray.get(i) + "\"" + "," + "\"" + "HeartRate" + "\"" + ":" + mHeartArray.get(i) + "}");
            } else {
                sum = sum + ("{" + "\"" + "SportTime" + "\"" + ":" + "\"" + mDateArray.get(i) + "\"" + "," + "\"" + "HeartRate" + "\"" + ":" + mHeartArray.get(i) + "}") + Last;
            }
        }

        return "[" + sum + "]";
    }


    //蓝牙
    BluetoothGattCharacteristic characteristic = null;

    private boolean enableHeartRateSensor() {
        if (gattServiceAdapter == null)
            return false;
        //如果不是相符合设备就会闪图报错
        Log.e("panduan", gattServiceAdapter + "gattServiceAdapter");
        characteristic = gattServiceAdapter
                .getHeartRateCharacteristic();
        Log.e("panduan", characteristic + "characteristic");
        if (characteristic == null) {
            return false;
        }
        final BleSensor<?> sensor = BleSensors.getSensor(characteristic.getService().getUuid().toString());

        if (heartRateSensor != null)
            bleService.enableSensor(heartRateSensor, false);

        if (sensor == null) {
            bleService.readCharacteristic(characteristic);
            return true;
        }

        if (sensor == heartRateSensor)
            return true;

        heartRateSensor = sensor;
        bleService.enableSensor(sensor, true);

        return true;
    }

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        gattServiceAdapter = new BleServicesAdapter(this, gattServices);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BleService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    /*
   * 检测一旦断线就重新连接
   * */
    // 蓝牙适配器
    private BluetoothAdapter blueadapter = null;
    private BluetoothManager bluetoothManager;
    // 设备列表集合
    private List<String> deviceList = new ArrayList<String>();
    //
    private ListView deviceListview;
    // 数组适配器
    private ArrayAdapter<String> adapter;
    // 蓝牙地址备份
    private List<HashMap<String, String>> nAddr = new ArrayList<HashMap<String, String>>();


    public void reConned() {
        StartStopScan(false);
        h.sendEmptyMessageDelayed(5,
                2000);
    }

    private boolean mScanning;

    //包含开始搜索,停止搜索的方法
    public void StartStopScan(boolean bStop) {
        //控制重连失败？
        heartRateSensor = null;
        if (bStop) // 停止查找
        {
            if (mScanning) {
                blueadapter.stopLeScan(mLeScanCallback);
                mScanning = false;
            }
        } else // 查找查找
        {
            if (!mScanning) {
                blueadapter.startLeScan(mLeScanCallback);
                mScanning = true;
            }
        }
        invalidateOptionsMenu();
    }

    //重连
    Intent registed = null;
    String adder;
    // 查找到设备之后将设备的地址添加到集合当中
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {
            //用于备份地址名称
            HashMap<String, String> nMap = new HashMap<String, String>();
            nMap.put("addr", device.getAddress().toUpperCase().trim());
            adder = device.getAddress().toUpperCase().trim();

            //点击返回按钮即不再扫描了
            Log.e("step", "努力重新连接中--------1");
            if (adder != null) {
                Log.e("step", "努力重新连接中--------2");
                if (bleService != null) {
                    bleService.disconnect();
                }
                if (adder.equals(ShareUitls.getString(getApplicationContext(), "adrs", null))) {
                    Log.e("step", "努力重新连接中--------3");
                    if (!isConnected) {
                        Log.e("step", "努力重新连接中--------4");
                        //初始化数据
                        if (bleService != null) {
                            Log.e("step", "努力重新连接中--------4.1");
                            bleService.disconnect();
                        }

                        // 支持后台数据显示
                        bleService = null;
                        heartRateSensor = null;
                        //解除绑定服务
                        if (isBanded) {
                            Log.e("step", "努力重新连接中--------4.2");
                            AerobicSportActivity.this.unbindService(serviceConnection);
                        }
                        if (!clicked) {
                            deviceAddress = ShareUitls.getString(getApplicationContext(), "adrs", null);
                            Log.e("step", "努力重新连接中--------5");
                            gattServiceIntent = new Intent(AerobicSportActivity.this, BleService.class);
                            Log.e("step", "努力重新连接中--------6");
                            //重新绑定服务

                            isBanded = AerobicSportActivity.this.bindService(gattServiceIntent, serviceConnection, Activity.BIND_AUTO_CREATE);
                            //重新注册广播
                            registed = AerobicSportActivity.this.registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter());

                            if (bleService != null) {
                                bleService.connect(deviceAddress);
                            }
                        } else {

                            StartStopScan(true);
                        }
                        Log.e("step", "努力重新连接中--------7");
                    } else {

                    }

                } else {

                }
            }
        }
    };

    public void connectBle() {
         /*
        * 首次开始蓝牙采集
        * */
        //现获取蓝牙管理器
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        //然后获取蓝牙适配器
        blueadapter = bluetoothManager.getAdapter();
        String adrs = ShareUitls.getString(getApplicationContext(), "adrs", "null");
        Log.i("PAUSEPAUSE", adrs);
        //连接硬件设备
        if (!adrs.equals("null")) {
            deviceAddress = adrs;
            gattServiceIntent = new Intent(this, BleService.class);
            isBanded = this.bindService(gattServiceIntent, serviceConnection, Activity.BIND_AUTO_CREATE);

        } else {
            Toast.makeText(getApplicationContext(), "连接失败,请重新尝试", Toast.LENGTH_SHORT);
        }
    }


    public void initView() {
        //准备环
        exceedMediaPlayer = MediaPlayer.create(this, R.raw.exceedheartrate);
        underMediaPlayer = MediaPlayer.create(this, R.raw.underheartrate);
        //控制禁止锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //获取屏幕宽高
        WindowManager wm = this.getWindowManager();
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        Log.e("fenbianlv", screenWidth + "-----" + screenHeight);
        if (!ShareUitls.getString(getApplicationContext(), "Target", "0").equals("0")) {
            Log.e("ttt", Target + "Target" + ShareUitls.getString(getApplicationContext(), "Target", "0"));
            Log.e("sds", second(Integer.parseInt(ShareUitls.getString(getApplicationContext(), "Target", "0")) / 60));
            Log.e("sds", second(Integer.parseInt(ShareUitls.getString(getApplicationContext(), "Target", "null")) % 60));
            Target.setText("目标：" + Integer.parseInt(ShareUitls.getString(getApplicationContext(), "Target", "0")) + "'" + second(60 * Integer.parseInt(ShareUitls.getString(getApplicationContext(), "Target", "0")) % 60) + "");


        }
        if (!ShareUitls.getString(getApplicationContext(), "LBound", "null").equals("null")) {
            lBound = Integer.parseInt(ShareUitls.getString(getApplicationContext(), "LBound", "null"));
            LBound.setText(lBound + "");
        }
        if (!ShareUitls.getString(getApplicationContext(), "UBound", "null").equals("null")) {
            uBound = Integer.parseInt(ShareUitls.getString(getApplicationContext(), "UBound", "null"));
            UBound.setText(uBound + "");
        }

        btback.setVisibility(View.INVISIBLE);
        btback.setClickable(false);
        move.setBackgroundColor(Color.parseColor("#ffffff"));
        smallprogressCircle = new smallProgressCircle(this);
        statechange.setText("准备活动");
        zhubutishentip.setText("逐步提升至" + ShareUitls.getString(getApplicationContext(), "LBound", "0") + "以上");
        //运动小节返回按钮
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (MainActivity.Activity != null) {
                    MainActivity.Activity.finish();
                }*/
                startActivity(new Intent(AerobicSportActivity.this, MainActivity.class));
                finish();

            }
        });
        im_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pop.isShowing()) {
                    im_lock.setImageDrawable(getResources().getDrawable(R.drawable.closelock));
                    bt_controller.setClickable(false);
                    bt_stop.setClickable(false);
                    lock_relativeLayout.setVisibility(View.VISIBLE);
                }


            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String pictime = System.currentTimeMillis() + "";
                //  Bitmap bitmap = ScreenShot.takeScreenShot(AerobicSportActivity.this, pictime);
                //  if (bitmap != null) {
                Share.getInstance().showPopFormBottom(AerobicSportActivity.this, webHandler);
                //}
            }
        });


    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //实例化对象
    public void popWindow() {
        // 创建PopupWindow对象
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.showpopu, null);
        //实例化pop包裹内容
        pop = new PopupWindow(view, screenWidth - 60, -2, false);
        // 引入窗口配置文
        // 需要设置一下此参数，点击外边可消
        // 失
      /*  pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_analyze_negative));*/
        //设置点击窗口外边窗口消失
        pop.setOutsideTouchable(false);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(false);
        pop.setTouchable(true);

        holder = new Holder();
        x.view().inject(holder, view);
        holder.huifu.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {

        // TODO Auto-generated method stub
        if (((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
            if (sportover) {
               /* if (MainActivity.Activity != null) {
                    MainActivity.Activity.finish();
                }*/
                startActivity(new Intent(AerobicSportActivity.this, MainActivity.class));
                finish();
            } else {
                if (con_state != -1) {
                    showAlert(true, "确定停止运动并上传数据");
                } else {
                    showAlert(false, "确定停止准备运动");
                }
            }
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 注册一个屏幕锁屏的广播
     */

    private void registScreenOffReceiver() {
        // TODO Auto-generated method stub
        receiver = new ScreenOnOffReceiver();
        // 创建一个意图过滤器
        IntentFilter filter = new IntentFilter();
        // 添加屏幕锁屏的广播
        filter.addAction("android.intent.action.SCREEN_OFF");
        // 在代码里边来注册广播
        registerReceiver(receiver, filter);

    }

    private void registServiceToActivityReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ServiceToActivityReceiver");
        registerReceiver(ServiceToActivityReceiver, filter);
    }

    class ScreenOnOffReceiver extends BroadcastReceiver {

        private static final String TAG = "ScreenOnOffReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 关屏的操作
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
            /*    // 当手机关屏时，我们同时也锁屏
                slideUnlockView.setVisibility(View.VISIBLE);
                // 设置图片消失
                bt_controller.setClickable(false);
                bt_stop.setClickable(false);
                im_lock.setImageDrawable(getResources().getDrawable(R.drawable.closelock));*/
            }
        }
    }

    public String second(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return "" + i;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //将需要保存的数据放入Bundle中
        if (Data != null && showTime != 0) {
            outState.putInt("mTotaltime", mTotaltime);
            outState.putInt("showTime", showTime);
            outState.putString("Data", Data);
            outState.putStringArrayList("mDateArray", mDateArray);
            outState.putIntegerArrayList("mHeartArray", mHeartArray);

        }

    }


    IWeiboShareAPI mWeiboShareAPI;

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
}
