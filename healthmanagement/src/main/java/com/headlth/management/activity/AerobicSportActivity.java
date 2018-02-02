
package com.headlth.management.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.MyBlue.MyBlueDataAnalysis;
import com.headlth.management.MyBlue.MyBulePolorManager;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.circle.RoundProgressBar;
import com.headlth.management.circle.smallProgressCircle;
import com.headlth.management.entity.upCallBack;
import com.headlth.management.myview.MyToash;
import com.headlth.management.myview.SlideUnlockView;
import com.headlth.management.service.FakePlayerService;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DiskBitmap;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ImageUtil;
import com.headlth.management.utils.InternetUtils;
import com.headlth.management.utils.Share;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.StringForTime;
import com.headlth.management.utils.ToJson;
import com.headlth.management.utils.VersonUtils;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

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
import java.util.List;

import tech.linjiang.suitlines.SuitLines;
import tech.linjiang.suitlines.Unit;


/**
 * Created by Administrator on 2016/2/26.
 */
@ContentView(R.layout.activity_aerobicsportmyblue)//
public class AerobicSportActivity extends BaseActivity implements View.OnClickListener/*, IWeiboHandler.Response*/ {
    @ViewInject(R.id.slideUnlockView)
    private SlideUnlockView slideUnlockView;
    private Vibrator vibrator;
    private ScreenOnOffReceiver screenOnOffReceiver;

    @ViewInject(R.id.lock_relativeLayout)
    private RelativeLayout lock_relativeLayout;
    PopupWindow popupWindoww;
    @ViewInject(R.id.activity_aerobicsport_line)
    private View activity_aerobicsport_line;

    @ViewInject(R.id.activity_aerobicsport_heartrate_drawline)
    private SuitLines suitLines;

    //有效运动时间
    @ViewInject(R.id.home_my_effective_time)
    private TextView mEffectiveTime;
    public static int S_TOTAL_SEC = -1;
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

    //有效白条
    @ViewInject(R.id.effect)
    private TextView effect;
    //移动标尺
    @ViewInject(R.id.move)
    private ImageView move;
    @ViewInject(R.id.movemax)
    private ImageView movemax;

    //总条
    @ViewInject(R.id.activity_aerobicsportmyblue_width)
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

    @ViewInject(R.id.activity_aerobicsportmyblue_width_left)
    private TextView activity_aerobicsportmyblue_width_left;
    @ViewInject(R.id.activity_aerobicsportmyblue_width_right)
    private TextView activity_aerobicsportmyblue_width_right;


    private int uBound, lBound, TargeT;
    float startValue, endtValue, difference;//定义三个常量
    String UID, ADRS;
    //结束左上角按钮
    @ViewInject(R.id.btback)
    private RelativeLayout btback;
    //停止运动按钮
    @ViewInject(R.id.bt_controller)
    private Button bt_controller;
    //结束热身 暂停 开始
    @ViewInject(R.id.im_lock)
    private ImageButton im_lock;
    @ViewInject(R.id.activity_aerobicsport_controller)
    RelativeLayout activity_aerobicsport_controller;

    @ViewInject(R.id.bt_stop)
    private Button bt_stop;
    //计时器TextView
    @ViewInject(R.id.stepTimeTV)
    private TextView stepTimeTV;

    //标题状态标志
    @ViewInject(R.id.titlemark)
    private TextView titlemark;
    private boolean SuperUBound;//超出最大上线心率了 心率条变红
    private boolean NOSuperUBound;//恢复心率了 心率条变白

    //有效运动环'
    @ViewInject(R.id.smallseekCircle)
    private smallProgressCircle smallprogressCircle;
    //时间
    private SimpleDateFormat df;
    //时间集合
    private ArrayList<String> mDateArray = new ArrayList<String>();
    //心率集合
    private ArrayList<Integer> mHeartArray = new ArrayList<Integer>();

    //PopWindow弹出警告
    private View view;
    //屏幕宽高
    private int screenWidth;
    private int screenHeight;
    private PopupWindow pop;//不在心率区间的弹框提醒
    private boolean huifu = true;//恢复到有效区间过  （）
    private Drawable DrawableFifty, DrawableFive, DrawableHuifu;

    private AlertDialog.Builder dialog;
    private static Boolean isDismiss;
    //用来判断是否用户点击确定按钮
    //  private Boolean clicked = false;
    private static String url;
    //Thread myThread;
    private int con_state = -1;//当前状态 -1：热身准备 ，0 ：暂停，1：运动中 2 运动小结
    private boolean con_isstate;//true:运动，false：暂停
    private int fifteen_minutes = -1;//暂时时15分钟倒计时
    private int All_praseTime = 0;//暂时总时间
    private int All_valueTime = 0;//暂时不在心率区间的总时间
    private boolean lock;//锁切换
    String instruct = "";
    Intent intent;
    String UploadTime = "";
    private boolean warning = true;//心率不在范围内震动警告
    private MediaPlayer exceedMediaPlayer, underMediaPlayer;
    private int tenvibratro;
    private MyBulePolorManager myBuleConnectManager;
    int LoseConnectcount;//持续3秒收不到最新心率  心率为-- (失去了连接)
    // boolean LoseConnectSetValue;
    private BroadcastReceiver ServiceToActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (con_state != 2 && !stoped) {
                h.post(KeepTime);
                h.post(Update);
                h.post(POP);
                h.post(Draw);
            /*    KeepTime();//計時
                setData_Update();//心率判断
                setPOP_Detection();//POP 检测
                if (value != 0) {
                    setDraw();//画心率图
                }*/
            }
        }
    };
    Runnable KeepTime = new Runnable() {
        @Override
        public void run() {
            KeepTime();//計時
        }
    };
    Runnable Update = new Runnable() {
        @Override
        public void run() {
            setData_Update();//心率判断
        }
    };
    Runnable POP = new Runnable() {
        @Override
        public void run() {
            setPOP_Detection();//POP 检测
        }
    };
    Runnable Draw = new Runnable() {
        @Override
        public void run() {
            //if (!mOnpaused) {
            setDraw();//画心率图
            // }
        }
    };

    private void startBlue() {
        if (myBuleConnectManager != null) {
            myBuleConnectManager.endConnect();
            myBuleConnectManager = null;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        myBuleConnectManager = MyBulePolorManager.getInstance(activity, ADRS, new MyBulePolorManager.OnCharacteristicListener() {
            @Override
            public void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
                //  Log.i("myblue", "数据通知");
                String tempvalue = MyBlueDataAnalysis.getBlueData(characteristic);
                // if (con_state == 1 || con_state == -1) {
                value = Float.valueOf(tempvalue).intValue();
                h.sendEmptyMessage(1001);
                //  }
            }

        });

    }

    private void setValue() {
        // LoseConnectSetValue = false;
        LoseConnectcount = 0;
        mBmp.setText(value + "");
        if (con_state == 1 || con_state == 0) {
            if (con_state == 1) {
                //保存心率值跟采集的时间获取12小时制
                String curTime = df.format(new Date()).toString();
                if (mDateArray.size() != 0) {
                    //   Log.i("myblue", "2。。。。。。 ");
                    if (!mDateArray.get((mDateArray.size() - 1)).equals(curTime)) {
                        //     Log.i("myblue", "3。。。。。。 ");
                        mDateArray.add(curTime);
                        //整型Int
                        mHeartArray.add(value);
                    }
                } else {
                    //    Log.i("myblue", "4。。。。。。 ");
                    mDateArray.add(curTime);
                    //整型Int
                    mHeartArray.add(value);

                }

            }
//动画
            if (value >= endtValue) {
                if (ta1 != null) {
                    ta1.cancel();
                }
                move.setVisibility(View.INVISIBLE);
                movemax.setVisibility(View.VISIBLE);
            } else {
                move.setVisibility(View.VISIBLE);
                movemax.setVisibility(View.GONE);
                if (value < startValue) {
                    startAnimation(0, 0);
                } else if (value >= startValue && value < lBound) {
                    startAnimation(valuetemp, (activity_aerobicsportmyblue_widthX_left - moveX) + (value - startValue) * activity_aerobicsportmyblue_widthX_leftlength / (lBound - startValue));
                } else if (value >= lBound && value <= uBound) {
                    startAnimation(valuetemp, (effectX - moveX) + (value - lBound) * effectlength / (uBound - lBound));
                } else if (value > uBound && value <= endtValue) {
                    startAnimation(valuetemp, (activity_aerobicsportmyblue_widthX_rightX - moveX) + (value - uBound) * activity_aerobicsportmyblue_widthX_rightlength / (endtValue - uBound));

                }
            }
        }


    }

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
            initialize();

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

    private void initialize() {
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constant.SINA_KEY);//注册微博分享SDK
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);//锁屏下 让本界面置于锁屏下
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        activity = this;
        TargeT = Integer.parseInt(ShareUitls.getString(getApplicationContext(), "Target", "1"));
        lBound = Integer.parseInt(ShareUitls.getString(getApplicationContext(), "LBound", "0"));
        uBound = Integer.parseInt(ShareUitls.getString(getApplicationContext(), "UBound", "0"));

        difference = uBound - lBound;
        startValue = lBound - (difference) / 3;
        endtValue = uBound + (difference) / 3;

        UID = ShareUitls.getString(getApplicationContext(), "UID", "null");
        ADRS = getIntent().getStringExtra("MAC");
        DrawableFifty = getResources().getDrawable(R.drawable.a);
        DrawableFive = getResources().getDrawable(R.drawable.keep);
        DrawableHuifu = getResources().getDrawable(R.drawable.huifu);

        MyToash.Log("startValue" + startValue + "  " + endtValue);
        startService(new Intent(activity, FakePlayerService.class).putExtra("flag", "sport"));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//禁止自动息屏
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        UploadTime = format.format(new Date());
        url = Constant.BASE_URL;
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //初始化运动界面的view
        initView();
        //初始化pop
        popWindow();
        //菊花等待可以单独提出来
        initDialog(activity);
        //滑动解锁
        setSildeLock();
        setPopWindow();//暂停提示
        registServiceToActivityReceiver();//接收 计时器的广播
        setDatee();
        initDrawPaint();//初始化心率图的画笔
        startBlue();//启动蓝牙
    }

    private void setPopWindow() {
        // WindowManager m = getWindowManager();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_fifteen_minutes, null);
        popupWindoww = new PopupWindow(view, ImageUtil.dp2px(this, 300), ImageUtil.dp2px(this, 100), true);
        popupWindoww.setOutsideTouchable(true);
    }

    private void setPOP_Detection() {//pop 检测
        if (con_state == 1) {
            if (value < lBound) {
                five = 5;
                if (!pop.isShowing()) {//开启提醒
                    if (huifu) {
                        huifu = false;
                        thirty = 50;
                        holder.img_state.setVisibility(View.VISIBLE);
                        holder.line.setVisibility(View.VISIBLE);
                        holder.huifu.setVisibility(View.INVISIBLE);
                        holder.thirtyCount.setVisibility(View.VISIBLE);
                        pop.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);
                    }
                } else {
                    //  activity_aerobicsport_controller.setClickable(false);
                    if (thirty != 0) {
                        holder.img_state.setBackground(DrawableFifty);
                        holder.thirtyCount.setText((--thirty) + "秒");

                    } else {
                        //  activity_aerobicsport_controller.setClickable(true);
                        pop.dismiss();
                        holder.thirtyCount.setText("50秒");
                    }
                }

            } else if (value <= uBound) {

                if (pop.isShowing()) {//恢复到了有效区间 坚持5秒
                    if (five == 0) {
                        holder.img_state.setVisibility(View.GONE);
                        holder.line.setVisibility(View.GONE);
                        holder.huifu.setVisibility(View.VISIBLE);
                        holder.huifu.setBackground(DrawableHuifu);
                        holder.thirtyCount.setVisibility(View.INVISIBLE);
                        h.sendEmptyMessageDelayed(500, 2000);//显示两秒后消失
                    } else {
                        holder.thirtyCount.setText((five--) + "秒");
                        holder.img_state.setBackground(DrawableFive);
                    }
                } else {
                    huifu = true;
                    thirty = 50;
                }
            } else {
                if (pop.isShowing()) {
                    if (thirty != 0) {
                        holder.img_state.setBackground(DrawableFifty);
                        holder.thirtyCount.setText((--thirty) + "秒");

                    } else {
                        //   activity_aerobicsport_controller.setClickable(true);
                        pop.dismiss();
                        holder.thirtyCount.setText("50秒");
                    }
                }
            }
        } else if (con_state == 0) {
            thirty = 50;
            five = 5;
        } else {
            if (pop.isShowing()) {
                pop.dismiss();
            }
        }
    }

    //横向拉动解锁
    private void setSildeLock() {
        // 注册屏幕锁屏的广播
        registScreenOffReceiver();
        // 获取系统振动器服务
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // 设置滑动解锁-解锁的监听
        slideUnlockView.setOnUnLockListener(new SlideUnlockView.OnUnLockListener() {
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
    //json数据
    private String Data;
    //是否进入Onpaused了
    private boolean mOnpaused = false;//屏幕暗屏
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

    private void setDatee() {
        mRoundProgressBar2.setMax(max);
        stepTimeTV.setText("00:00:00");
        LBound.setVisibility(View.INVISIBLE);
        UBound.setVisibility(View.INVISIBLE);
        h.sendEmptyMessageDelayed(1, 1);
        out.setBackgroundColor(Color.parseColor("#00000000"));
        effect.setBackgroundColor(Color.parseColor("#00000000"));
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
                    //  Log.e("rrr", mTotaltime + "。。。。。。 mTotaltime");
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
                                                             fifteen_minutes = -1;
                                                             con_state = 1;
                                                             bt_controller.setText("暂停");
                                                             statechange.setText("运动中");
                                                         } else {
                                                             con_state = 0;
                                                             bt_controller.setText("继续");
                                                             statechange.setText("休息中");
                                                         }
                                                         con_isstate = !con_isstate;
                                                     }

                                                 }
                                             }
                                         }

        );
    }

    int alltime;
    String content = "";
    int fineMinute = 0;

    private boolean setData_Update() {
        if (LoseConnectcount >= 3) {//超过3秒了没收到心率带返回的心率
            if (LoseConnectcount == 3) {//
                value = 0;
                mBmp.setText("--");
            }
            if (LoseConnectcount == 30) {//30秒内没有收到心率重启连接
                LoseConnectcount = 0;
                startBlue();
                //myBuleConnectManager.nofirst_connect();
            }
        }
        LoseConnectcount++;
        if (con_state == 2) {
            return true;
        }
        //断开即为0
        if (!MyBulePolorManager.IS_CONNECT) {
            startAnimation(0, 0);
            value = 0;
            if (!stoped) {
                mBmp.setText("- -");
            }
        }
        //Log.e("rrr", System.currentTimeMillis() - startTime + "");
        if (stoped) {
            value = 0;
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
        }
        if (value >= uBound || value <= lBound) {
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
                }
                ++tenvibratro;//超过范围 十秒震动
                if (value > uBound) {
                    if (!SuperUBound) {
                        SuperUBound = true;
                        NOSuperUBound = false;
                        titlemark.setTextColor(Color.parseColor("#ff0000"));
                        titlemark.setText("警告:超出上限");
                        mBmp.setTextColor(Color.parseColor("#ff0000"));
                        out.setBackgroundColor(Color.parseColor("#ff0000"));
                    }
                }
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
        if (value <= uBound) {
            if (!NOSuperUBound) {
                NOSuperUBound = true;
                SuperUBound = false;
                titlemark.setTextColor(Color.parseColor("#836313"));
                titlemark.setText("  当前心率  ");
                mBmp.setTextColor(Color.parseColor("#000000"));
                out.setBackgroundColor(Color.parseColor("#FFb809"));
            }
        }
        // Log.e("rrr", alltime + "wwwwwww" + mTotaltime + "  " + fineMinute);
        return false;
    }

    private void KeepTime() {
        if (con_state != 0) {
            if (con_state == -1) {//热身阶段
               // alltime=  (int)((System.currentTimeMillis()-startTime)/1000)-All_praseTime;
              ++alltime;
                progress = alltime;
                h.sendEmptyMessage(34);
                mRoundProgressBar2.setProgress(progress);
                if (progress == max) {
                    alltime = -1;
                    h.sendEmptyMessage(35);
                }
            } else if (con_state == 1) {//运动阶段
               alltime=  (int)((System.currentTimeMillis()-startTime)/1000)-All_praseTime;
               // ++alltime;//总时间++
                mTotaltime = alltime;
                if (value != 0) {
                    fineMinute = 0;
                    if (alltime % 10 == 0) {
                        Data = ToJson.getRequestData(mDateArray, mHeartArray);
                        // Log.i("AADDDDDDaaa", "" + Data);
                        saveNativeData(UID, Data, "1", (mTotaltime), showTime);//每十秒保存十一数据
                    }
                    if (value <= uBound && value >= lBound) {
                        showTime=  (int)((System.currentTimeMillis()-startTime)/1000)-(All_valueTime+All_praseTime);
                       // ++showTime;
                        //++S_TOTAL_SEC;//有效时间++
                        // 用户只要在累计5s之后才开始显示时间
                      //  showTime = S_TOTAL_SEC ;//>= 5 ? S_TOTAL_SEC : 0;
                        mEffectiveTime.setText(StringForTime.stringForTime(showTime));
                        // smallprogressCircle.setProgress((100 * (showTime)) / (TargeT));
                        h.sendEmptyMessage(3);//有效进度环更新
                    }else {
                        All_valueTime++;
                    }
                } else {
                    All_valueTime++;
                    ++fineMinute;
                    if (fineMinute == 300) {//心率为0持续5分钟
                        overTask();
                    }
                }

            }
            if (con_state != -1) {//修正心率
                alltime = alltime <= showTime ? showTime : alltime;
            }
            content = StringForTime.stringForTime(alltime);
            stepTimeTV.setText(content);
        } else {//暂停
            fifteen_minutes++;
            All_praseTime++;
            if (fifteen_minutes == 0) {
                popupWindoww.showAtLocation(new View(activity), Gravity.CENTER, 0, 0);
                // popupWindoww.update();
            } else if (fifteen_minutes == 4) {
                if (popupWindoww.isShowing()) {
                    popupWindoww.dismiss();
                }
            } else if (fifteen_minutes >= 900) {//暂停超过15分钟自动上传
                overTask();

            }
        }

    }

    private long startTime = 0;
    private Boolean circleStop = false;
    int x0;
    int y0;
    int effectX;
    int moveX;
    int effectlength;
    int outlength;


    int activity_aerobicsportmyblue_widthX_left;
    int activity_aerobicsportmyblue_widthX_leftlength;
    int activity_aerobicsportmyblue_widthX_rightX;
    int activity_aerobicsportmyblue_widthX_rightlength;


    int gap;//200 dp
    int width;//屏幕宽度

    int thirty = 50;
    int five = 5;
    List<Integer> datas = new ArrayList<>();
    public Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1001) {//记录心率 设置游标移动
                setValue();
            } else if (msg.what == 200) {
                mEffectiveTime.setText(msg.obj.toString());
            } else if (msg.what == 1) {
                if (out.getWidth() != 0 && out.getHeight() != 0 && effect.getWidth() != 0 && effect.getHeight() != 0 && drawline.getWidth() != 0 && drawline.getHeight() != 0 && drawline.getWidth() != 0 && (drawline.getBottom() - drawline.getTop()) != 0) {
                    int[] location666 = new int[2];
                    drawline.getLocationOnScreen(location666);
                    x0 = location666[0];
                    y0 = location666[1];
                    gap = drawline.getBottom() - drawline.getTop();
                    width = drawline.getWidth();
                    int[] locationmove = new int[2];
                    move.getLocationInWindow(locationmove);
                    moveX = locationmove[0];
                    int[] locationyouxiao = new int[2];
                    effect.getLocationInWindow(locationyouxiao);
                    effectX = locationyouxiao[0];
                    effectlength = effect.getWidth();
                    int[] left = new int[2];
                    activity_aerobicsportmyblue_width_left.getLocationInWindow(left);
                    activity_aerobicsportmyblue_widthX_left = left[0];
                    activity_aerobicsportmyblue_widthX_leftlength = activity_aerobicsportmyblue_width_left.getWidth();

                    int[] right = new int[2];
                    activity_aerobicsportmyblue_width_right.getLocationInWindow(right);
                    activity_aerobicsportmyblue_widthX_rightX = right[0];
                    activity_aerobicsportmyblue_widthX_rightlength = activity_aerobicsportmyblue_width_right.getWidth();
                    outlength = out.getWidth();
                } else {
                    h.sendEmptyMessageDelayed(1, 1);
                }
            } else if (msg.what == 3) {
                smallprogressCircle.setProgress((100 * (showTime)) / (TargeT));
            } else if (msg.what == 34) {
                pretime.setText((progress) + "");
            } else if (msg.what == 35) {
                con_state = 1;
                statechange.setText("运动中");
                bt_controller.setText("暂停");
                zhubutishentip.setVisibility(View.INVISIBLE);
                LBound.setVisibility(View.VISIBLE);
                UBound.setVisibility(View.VISIBLE);
                move.setVisibility(View.VISIBLE);
                out.setBackgroundColor(Color.parseColor("#00000000"));
                effect.setBackgroundColor(Color.parseColor("#ffffff"));
                preround.setVisibility(View.GONE);
                youxiaohuan.setVisibility(View.VISIBLE);
                startTime = System.currentTimeMillis();
            } else if (msg.what == 105) {
                if (msg.arg1 == 1) {
                    popupWindoww.showAtLocation(new View(activity), Gravity.CENTER, 0, 0);
                    // popupWindoww.update();
                } else if (msg.arg1 >= 15 * 60) {
                    overTask();
                } else if (msg.arg1 >= 5) {
                    if (popupWindoww.isShowing()) {
                        popupWindoww.dismiss();
                    }
                }
            } else if (msg.what == 500) {
                huifu = true;
                thirty = 50;
                // activity_aerobicsport_controller.setClickable(true);
                if (pop.isShowing()) {
                    pop.dismiss();
                }
                holder.img_state.setVisibility(View.VISIBLE);
                holder.line.setVisibility(View.VISIBLE);
                holder.huifu.setVisibility(View.INVISIBLE);
                holder.img_state.setBackground(DrawableFifty);
                holder.thirtyCount.setVisibility(View.VISIBLE);
                holder.thirtyCount.setText("50秒");
            }
        }

    };

    private void overTask() {
        con_state = 2;
        if (pop.isShowing()) {
            pop.dismiss();
        }
        myBuleConnectManager.endConnect();
        myBuleConnectManager = null;
      /*  try {
            myBuleSerachManager.endSearch();
            myBuleSerachManager = null;
        } catch (Exception n) {
        }*/
        underMediaPlayer.stop();
        underMediaPlayer.release();
        exceedMediaPlayer.stop();
        exceedMediaPlayer.release();
        if (pop.isShowing()) {
            pop.dismiss();
        }
        try {

            unregisterReceiver(screenOnOffReceiver);
            unregisterReceiver(ServiceToActivityReceiver);
        } catch (Exception e) {
        }
        stopService(new Intent(activity, FakePlayerService.class));
        //按钮点击标志
        stoped = true;
        isUpdate = false;//数据正常上传过
        statechange.setText("运动小结");
        move.clearAnimation();
        move.setVisibility(View.GONE);
        movemax.setVisibility(View.GONE);
        // activity_aerobicsport_line.setVisibility(View.VISIBLE);
        out.setBackgroundColor(Color.parseColor("#00000000"));
        effect.setBackgroundColor(Color.parseColor("#00000000"));
        LBound.setVisibility(View.INVISIBLE);
        UBound.setVisibility(View.INVISIBLE);
        lock_relativeLayout.setVisibility(View.GONE);
        activity_aerobicsport_controller.setVisibility(View.GONE);
        mEnd = System.currentTimeMillis();
        Data = ToJson.getRequestData(mDateArray, mHeartArray);
        if (mHeartArray.size() >= 30) {
            int cha = mHeartArray.size() / 30;

            for (int p = 0; p < mHeartArray.size(); p = p + cha) {
                if (p < mHeartArray.size()) {
                    if (thirdData.size() <= 30) {
                        thirdData.add(mHeartArray.get(p));
                    }
                }
            }

        } else {
            thirdData = mHeartArray;
        }
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
        btback.setVisibility(View.VISIBLE);
        btback.setClickable(true);
        sportover = true;
        setHeartInage();//画出平均心率图

        if (mHeartArray.size() == 0) {
            MyToash.Toash(activity, "本次没有效运动数据");

        } else {
            UpDate(UID, Data, "1", (mTotaltime), showTime);
        }


    }

    private void setHeartInage() {
        drawline.setVisibility(View.GONE);
        suitLines.setVisibility(View.VISIBLE);
        List<Unit> lines = new ArrayList<>();
        for (int i = 0; i < thirdData.size(); i++) {
            float vlaue = thirdData.get(i) - 43;
            lines.add(new Unit(vlaue < 0 ? 1 : vlaue, ""));
        }
        suitLines.setLineForm(true);
        int[] colors = new int[2];
        suitLines.anim();
        colors[0] = Color.parseColor("#ff4763");
        colors[1] = Color.parseColor("#b51225");
        suitLines.setDefaultOneLineColor(colors);
        suitLines.setLineType(SuitLines.SEGMENT);
        suitLines.feedWithAnim(lines);
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    float valuetemp;
    //控制停止运行
    Boolean stoped = false;

    //运动中目标动画
    public void startAnimation(final float first, final float last) {
        //  Log.i("myblue", "" + first + "  " + last + "  ");
        ta1 = new TranslateAnimation(first, last, 0, 0);
        ta1.setDuration(1000);
        ta1.setRepeatCount(Integer.MAX_VALUE);
        ta1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                move.clearAnimation();
            }
        });
        move.startAnimation(ta1);
        valuetemp = last;


    }

    @Override
    public void onClick(View v) {

    }

    //运行中动态折现图
    private draw d;
    Paint paint;
    public class draw extends View {
        int x = 0;
        int y = 0;

        public draw(Context context) {
            super(context);
            setWillNotDraw(false);//可不写  默认就是 false
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //  Log.i("myblue",gap + "开始画了" + width+ "    "+datas.size());
            if (datas.size() > 1) {
                Path path = new Path();
                path.moveTo(0, gap);
                for (int i = 0; i < datas.size(); i++) {//(datas.get(i)
                    int  value=datas.get(i);
                    path.lineTo(i * (width / 30), (gap / 4 + gap) - (((value<51?51:value) * (gap / 4 + gap)) / 250));
                }
                // path.setLastPoint((datas.size() - 1) * (width / 30), gap);
               path.lineTo((datas.size() - 1) * (width / 30), gap);
                canvas.drawPath(path, paint);
            }
            invalidate();
        }
    }

    private void initDrawPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#b51225"));
        paint.setStrokeWidth((float) 5.0);
        paint.setTextSize(20);
    }

    private boolean setDraw() {
        if (datas.size() <= 30) {
            datas.add(value);
        } else {
            datas.remove(0);
            datas.add(value);
        }
        return false;
    }

    public void showAlert(final Boolean tag, final String msg) {
        dialog = new AlertDialog.Builder(activity);
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
                        //如果准备运动停止了就tag为true
                        if (tag) {
                            overTask();
                        } else {//停止准备运动退出
                            myBuleConnectManager.endConnect();
                            myBuleConnectManager = null;
                            try {
                                unregisterReceiver(screenOnOffReceiver);
                                unregisterReceiver(ServiceToActivityReceiver);
                            } catch (Exception e) {
                            }
                            stopService(new Intent(activity, FakePlayerService.class));
                         /*   try {
                                myBuleSerachManager.endSearch();
                                myBuleSerachManager = null;
                            } catch (Exception n) {
                            }*/
                            finish();
                        }

                    }

                }).show();
    }

    public void showAlert1(final Boolean tag, final String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
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
                        alltime = -1;
                        progress = max;
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
        if (EveryVolidTime > 0) {
            if (InternetUtils.internett(activity)) {
                // initDialog(activity);
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
                                    + "&ResultJWT=" + ShareUitls.getString(activity, "ResultJWT", "0")
                                    + "&VersionNum=" + VersonUtils.getVersionName(activity);
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
                            // Log.i("myblue", "上传的结果码" + resultCode + "");
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
                            //   Log.i("AAOrderNO111aa", e.toString());

                        }

                    }

                }.start();
            } else {
                saveNativeData(UID, Data, WatchType, EveryTime, EveryVolidTime);
                Toast.makeText(activity, "当前无网络连接", Toast.LENGTH_LONG).show();
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
            ShareUitls.cleanString2(activity);
            MyToash.Toash(activity, "本次没有效运动数据");
//
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

                // Log.i("upBack", "" + upBack.toString());
                if (upBack.getErrCode() != null && (upBack.getErrCode().toString().equals("601") || upBack.getErrCode().toString().equals("600"))) {

                    if (upBack.getErrCode().toString().equals("601")) {
                        MyToash.Toash(activity, "您的账号已在其他设备登录");
                        // Toast.makeText(activity, "您的账号已在其他设备登录", Toast.LENGTH_LONG).show();
                    } else {
                        MyToash.Toash(activity, "您的登录信息已过期");
                        // Toast.makeText(activity, "您的登录信息已过期", Toast.LENGTH_LONG).show();
                    }
                    saveNativeData(UID, Data, "1", (mTotaltime), showTime);
                    stopService(new Intent(activity, FakePlayerService.class));
                    Intent i = new Intent(activity, Login.class);
                    startActivity(i);
                    ShareUitls.putString(activity, "questionnaire", "1");//设置首页刷新
                    ShareUitls.putString(activity, "maidong", "1");//设置首页刷新
                    ShareUitls.putString(activity, "analize", "1");//分析重新刷新
                  /*  if (MainActivity.Activity != null) {
                        MainActivity.Activity.finish();
                    }*/
                    finish();

                } else {

                    ShareUitls.putString(activity, "maidong", "1");//首页界面新刷新
                    ShareUitls.putString(activity, "analize", "1");//分析界面重新刷新
                    ShareUitls.cleanString2(activity);


                    // Log.i("AAOrderNO111aaAA", upBack.toString());
                    if (upBack.getStatus() == 1) {
                        MyToash.Toash(activity, "数据上传成功");
                    } else if (upBack.getStatus() == 2) {
                        MyToash.Toash(activity, "数据异常,上传失败");

                    }

                }
            } else {
                //  List<Object> list=(List<Object> )msg.obj;
                MyToash.Toash(activity, "上传数据失败,请确认网络连接");

                saveNativeData(UID, Data, "1", (mTotaltime), showTime);


                //   saveNativeData(list.get(0).toString(), list.get(1).toString(), list.get(2).toString(), (int)list.get(3), (int)list.get(4));
            }

        }

    };

    private void upDate(final String UID, final String Data, final String WatchType, final int EveryTime, final int EveryVolidTime) {
        //showDialog(true);

        //Log.i("updateeeeeeeeee", "  UID=  " + UID + "    Data= " + Data + "  WatchType=  " + WatchType + "    EveryTime= " + EveryTime + "   EveryVolidTime= " + EveryVolidTime + "  ");

        if (EveryTime != 0) {
            if (InternetUtils.internett(activity)) {
                waitDialog.showDailog();
                // ShareUitls.putString(getApplicationContext(), "hasNewDate", "1");
                //在这里设置需要post的参数
                RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostSportDataRequest");
                params.addBodyParameter("ResultJWT", ShareUitls.getString(activity, "ResultJWT", "0"));
                params.addBodyParameter("UID", UID);
                params.addBodyParameter("Data", Data);
                params.addBodyParameter("WatchType", WatchType);
                params.addBodyParameter("EveryTime", EveryTime + "");
                params.addBodyParameter("EveryVolidTime", EveryVolidTime + "");
                params.addBodyParameter("UploadTime", UploadTime);

                HttpUtils.getInstance(activity).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {


                            //  Log.e("update", Data + "Data" + WatchType + "WatchType" + EveryTime + "EveryTime" + EveryVolidTime + "EveryVolidTime");


                            //  upDate(ShareUitls.getString(getApplicationContext(), "UID", "null"), Data, "1", (mTotaltime / 1000) + "", showTime + "");

                            //   Log.e("pinjie", ShareUitls.getString(getApplicationContext(), "UID", "null") + "----" + Data + "----" + WatchType + "---" + EveryTime + "---" + EveryVolidTime + "---");

                            @Override
                            public void onResponse(String response) {
                                ShareUitls.putString(activity, "maidong", "1");//首页界面新刷新
                                ShareUitls.putString(activity, "analize", "1");//分析界面重新刷新
                                waitDialog.dismissDialog();
                                ShareUitls.cleanString2(activity);
                                //{"Status":2,"Message":null,"IsSuccess":false,"IsError":true,"ErrMsg":"参数错误：SPID为空","ErrCode":"500"}
                                //  Log.e("WWWWWWWW", response.toString());
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
                MyToash.Toash(activity);
                // Toast.makeText(activity, "当前无网络连接", Toast.LENGTH_LONG).show();
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
            ShareUitls.cleanString2(activity);
            Toast.makeText(activity, "没有有效运动数据", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {//
            if (InternetUtils.internett(this)) {
                UpDate(UID, Data, "1", (mTotaltime), showTime);
            } else {
                MyToash.Toash(activity);
                // Toast.makeText(activity, "当前无网络连接", Toast.LENGTH_LONG).show();
            }
        }

    }

    //本地保存数据
    private void saveNativeData(String UID, String Data, String WatchType, int EveryTime, int EveryVolidTime) {
        ShareUitls.cleanString2(activity);
        // Log.i("WWWWWWWWWW", "正在保存数据");
        // Toast.makeText(getApplicationContext(), "正在保存数据...", Toast.LENGTH_SHORT).show();
        ShareUitls.putSportString(activity, "SportID", UID + "");
        ShareUitls.putSportString(activity, "Data", Data + "");
        ShareUitls.putSportString(activity, "WatchType", WatchType + "");
        ShareUitls.putSportString(activity, "EveryTime", EveryTime + "");
        ShareUitls.putSportString(activity, "EveryVolidTime", EveryVolidTime + "");
        ShareUitls.putSportString(activity, "UploadTime", UploadTime);
        waitDialog.dismissDialog();
    }

    boolean result = false;

    @Override
    public void onResume() {
        super.onResume();
        mOnpaused = false;
        ShareUitls.putString(activity, "AerobicSportActivityistop", "yes");//用来记录当前界面是否处于最前端  当处于最前端是  接收到友盟推送的消息 点击不进入 消息详情
        // Log.e("dadth", "onResume;;;");
        //添加动态折线
        if (d == null) {
            d = new draw(activity);
            drawline.addView(d);
        }

        if (!isStop) {
            isStop = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.e("dadth", "onPause;;;");
        isStop = false;
        mOnpaused = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        mOnpaused = true;
        //Log.e("dadth", "onStop();;;");
    }

    boolean crash = true;
    boolean isUpdate = true;

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShareUitls.putString(activity, "AerobicSportActivityistop", "");//用来记录当前界面是否处于最前端  当处于最前端是  接收到友盟推送的消息 点击不进入 消息详情
        mOnpaused = true;
        stoped = true;
        S_TOTAL_SEC = -1;
        showTime = 0;
        circleStop = true;
    }

    private int value = 0;

    private int mTotaltime = 0;
    private int showTime = -1;
    private long mEnd = 0;

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
        Target.setText("目标：" + StringForTime.stringForTime(TargeT));
        LBound.setText(lBound + "");
        UBound.setText(uBound + "");
        btback.setVisibility(View.INVISIBLE);
        btback.setClickable(false);
        statechange.setText("准备活动");
        zhubutishentip.setText("逐步提升至" + lBound + "以上");
        //运动小节返回按钮
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (MainActivity.Activity != null) {
                    MainActivity.Activity.finish();
                }*/
                startActivity(new Intent(activity, MainActivity.class));
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
                Share.getInstance().showPopFormBottom(activity, webHandler);
            }
        });


    }


    //实例化对象
    public void popWindow() {
        // 创建PopupWindow对象
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.showpopu, null);
        pop = new PopupWindow(view, screenWidth - 60, -2, true);
        pop.setOutsideTouchable(false);
        pop.setFocusable(false);
        holder = new Holder();
        x.view().inject(holder, view);
        holder.huifu.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {

        // TODO Auto-generated method stub
        if (((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
            if (stoped) {
               /* if (MainActivity.Activity != null) {
                    MainActivity.Activity.finish();
                }*/
                startActivity(new Intent(activity, MainActivity.class));
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
        screenOnOffReceiver = new ScreenOnOffReceiver();
        // 创建一个意图过滤器
        IntentFilter filter = new IntentFilter();
        // 添加屏幕锁屏的广播
        filter.addAction("android.intent.action.SCREEN_OFF");
        // 在代码里边来注册广播
        registerReceiver(screenOnOffReceiver, filter);

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
        if (Data != null && showTime > 0) {
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


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (pop != null && pop.isShowing()) {
            return false;
        }
        return super.dispatchTouchEvent(event);
    }
}
