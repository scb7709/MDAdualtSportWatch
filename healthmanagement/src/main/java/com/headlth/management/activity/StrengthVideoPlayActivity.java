package com.headlth.management.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.headlth.management.R;
import com.headlth.management.entity.Video;
import com.headlth.management.myview.ScreenListener;

import com.headlth.management.service.NetworkService;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.FileViewer;
import com.headlth.management.utils.GetUtf8;
import com.headlth.management.utils.InternetUtils;
import com.headlth.management.utils.ShareUitls;

import com.umeng.message.PushAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;




/**
 * Created by abc on 2016/10/10.
 */
@ContentView(R.layout.activity_strengthvideoplay)
public class StrengthVideoPlayActivity extends Activity {


    @ViewInject(R.id.strength_video_play_VideoView)
    private VideoView videoView;

    @ViewInject(R.id.video_play_re)
    private RelativeLayout video_play_re;

    @ViewInject(R.id.strength_video_play_parseOrplay)
    private ImageView video_play_parseOrplay;
    @ViewInject(R.id.strength_video_play_count)
    private TextView video_play_count;
    @ViewInject(R.id.strength_video_play_time)
    private TextView video_play_time;
    @ViewInject(R.id.strength_video_play_back)
    private ImageButton video_play_back;

    @ViewInject(R.id.strength_video_play_rest_lay)
    private LinearLayout video_play_rest_lay;
    @ViewInject(R.id.strength_video_play_rest_time)
    private TextView video_play_rest_time;

    private Video video;
    private long CurrentPosition = 2000000000;
    private int point;
    private boolean isplay = true;
    private boolean flag;//该视频是否只显示总次数
    private boolean isflag = true;//界面结束时用来结束广播接收
    private boolean isOnprase;//是否界面失去焦点
    private boolean isflag2 = true;//点击设置网络后不弹出对话框 只需要提示一次 没有网络
    private boolean isflag3 = false;//是否是广告时间
    private List<String> urls = new ArrayList<String>();
    private int count;
    private long totalTime;
    private String time;//开始运动时间
    private int timeSport;//总运动时间
    private String UID;//用户ID
    private ScreenListener screenListener = new ScreenListener(this);
    private AlertDialog.Builder dialog;
    private com.headlth.management.clenderutil.WaitDialog waitDialog;
    private String Stage;//当前是第几阶段
    List<String> list = new ArrayList<>();
    // boolean videotype;//用来标记是播放本地视频还是网络视频 (false 本地)
    Intent intentService;
    private boolean downloading;//是否正在下载状态（true  正在下载
    private String playvideo;//当前正在播放的视频的本地地址

    // Intent intentRecever;
    public static Activity activity;
    private String SDPATH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initliaize();
    }

    private void initliaize() {
        activity = this;
        PushAgent.getInstance(this).onAppStart();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        dialog = new AlertDialog.Builder(StrengthVideoPlayActivity.this);
        UID = ShareUitls.getString(StrengthVideoPlayActivity.this, "UID", "null");
        video = (Video) intent.getSerializableExtra("Video");
        Stage = video.getStage();
        intentService = new Intent(StrengthVideoPlayActivity.this, NetworkService.class);
        //   intentRecever=  new Intent("ActivityToServiceReceiver");
        startService(intentService);//开启服务用于全程检测

        SDPATH= Environment.getExternalStorageDirectory().getAbsolutePath()+ "/maidong/maidongvideo/maidongvideo" + Stage;

        list = FileViewer.getListFiles(SDPATH, "maid", true);

        // list = VideoDataManageUtils.getInstance(StrengthVideoPlayActivity.this).queryListData();
       /* if (list.size() == 0) {//没有本地视频 在线播放
            videotype = true;
        }*/
        // videotype = true;


        //Vitamio.isInitialized(getApplicationContext());

        Log.i("aaaaaaSSSS", list.size() + "  " + video.getActionList().size());
        for (Video.SubVideo subview : video.getActionList()) {
            timeSport += Integer.parseInt(subview.getDuration());
        }
        initDialog();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        time = format.format(new Date());
        // point=video.getActionList().size()-1;//测试用
        //ShareUitls.putString(getApplicationContext(), "CurrentPosition", "2000000000");
        setListener();
        // showDialog(true);
        start();
        videoView.start();
    }

    private Handler handler = new Handler() {
        @Override

        public void handleMessage(Message msg) {
            // call update gui method.

            switch (msg.what) {
                case 1:

                    Log.i("ACVB", "SSSSSSS" + msg.arg2 + "SSSSSS" + msg.arg1);
                   /* int CurrentPosition=Integer.parseInt(ShareUitls.getString(StrengthVideoPlayActivity.this,"CurrentPosition","2000000000"));
                    if(CurrentPosition==2000000000){

                    }*/
                    //  int temp=CurrentPosition>msg.arg1?CurrentPosition:msg.arg1;
                    int temp = msg.arg1;
                    video_play_time.setText(stringForTime(temp) + "/" + stringForTime(totalTime));
                    if (flag) {
                        video_play_count.setText(stringForTime(temp));
                    } else {
                        if (totalTime > 0) {
                            video_play_count.setText(temp * count / totalTime + "/" + count);
                        }

                    }
                    break;
                case 2:
                    break;
                case 3://正在休息 倒计时
                    if (videoView.isPlaying()) {
                        videoView.seekTo(0);
                        videoView.pause();
                    }
                    Log.i("VVVVVVVVVVVVVV", msg.arg1 + "");
                    video_play_rest_time.setText(msg.arg1 + "");
                    setAnmotion(video_play_rest_time);
                    break;
                case 4://休息倒计时完成
                    video_play_rest_lay.setVisibility(View.GONE);
                    isflag3 = false;
                    CurrentPosition = 2000000000;
                    // start();
                    videoView.start();

                    break;
                case 5:
                    Toast.makeText(StrengthVideoPlayActivity.this, "当前无网络连接", Toast.LENGTH_LONG).show();
                    break;

                case 6://致为0
                    video_play_time.setText(0 + "/" + 0);
                    video_play_count.setText(0 + "/" + 0);

                    break;
                case 7://暂停
                    video_play_parseOrplay.setImageResource(R.drawable.video_pause);
                    videoView.pause();
                    break;

                case 8:///时时监测网络

                    video_play_parseOrplay.setImageResource(R.drawable.video_pause);
                    videoView.pause();
                    if (isflag2) {
                        Toast.makeText(StrengthVideoPlayActivity.this, "当前无网络连接", Toast.LENGTH_LONG).show();
                        isflag2 = false;
                    }
                    break;

            }

        }
    };

    private void start() {
        if (point < video.getActionList().size()) {
            if (true) {//播放本地
                isplay = true;

                Log.e("ppppppointvvv0", "" + point);

                //视频文件后缀名改为MP4 才能播放
                FileViewer.renameFile(SDPATH , video.getActionList().get(point).getID() + ".maid", video.getActionList().get(point).getID() + ".mp4");
                String path = SDPATH+ "/" + video.getActionList().get(point).getID() + ".mp4";
                playvideo = path;
                videoView.setVideoURI(Uri.parse(path));
                videoView.start();

            } else {
                if (InternetUtils.internett(this)) {

                    isplay = true;
                    String p = null;
                    p = Constant.BASE_URL + "/" + video.getActionList().get(point).getAddress();
                    Log.i("aaaaaabbbb", p.toString());
                    String path = "";
                    char[] c = p.toCharArray();
                    for (int i = 0; i < p.length(); i++) {
                        path += GetUtf8.getUTF8XMLString(String.valueOf(c[i]));
                        //  Log.i("XXXXXXXXXXXX",path);
                    }
                    Log.i("XXXXXXXXXXXX", path);
                    videoView.setVideoURI(Uri.parse(path));
                    //  videoView.setVideoPath(path);
                    // videoView.start();
                    // ++point;
                    Log.i("PPPPPP启动播放", point + "");
                } else {
                    Toast.makeText(StrengthVideoPlayActivity.this, "当前无网络连接", Toast.LENGTH_LONG).show();
                    isplay = false;
                    video_play_parseOrplay.setImageResource(R.drawable.video_pause);
                }
            }
        }
    }

    private void initDialog() {
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(this);
        waitDialog.setMessage("");
        waitDialog.setCancleable(true);
    }

    private void showDialog(boolean isShow) {
        if (isShow) {
            waitDialog.showDailog();
        } else {
            if (waitDialog != null) {
                waitDialog.dismissDialog();
            }
        }
    }

    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            handler.sendEmptyMessage(4);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i("VVVVVVVVVVVVVV", millisUntilFinished + "");
            Message msg = new Message();
            msg.what = 3;
            msg.arg1 = (int) (millisUntilFinished / 1000);
            handler.sendMessage(msg);
        }

    }

    private String stringForTime(long timeMs) {
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = (int) (timeMs / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public void setAnmotion(final TextView v) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 2, 1, 2, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet animationSet = new AnimationSet(true);
                ScaleAnimation scaleAnimation = new ScaleAnimation(2, 1, 2, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(500);
                animationSet.addAnimation(scaleAnimation);
                animationSet.setFillAfter(true);
                v.startAnimation(animationSet);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scaleAnimation.setDuration(500);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setFillAfter(true);
        v.startAnimation(animationSet);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            if (isflag3) {
                return true;
            } else {
                setDialog();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setListener() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(layoutParams);
        videoView.requestFocus();
        video_play_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog();
            }
        });
        screenListener.begin(new ScreenListener.ScreenStateListener() {

            @Override
            public void onUserPresent() {
                Log.e("onUserPresent", "onUserPresent");
            }

            @Override
            public void onScreenOn() {
            }

            @Override
            public void onScreenOff() {
                if (!isflag3) {
                    CurrentPosition = videoView.getCurrentPosition();
                    // ShareUitls.putString(getApplicationContext(), "CurrentPosition", CurrentPosition+ "");
                    if (videoView.isPlaying()) {
                        videoView.pause();
                        video_play_parseOrplay.setImageResource(R.drawable.video_pause);
                        Log.i("onScreenOff", "zanting   " + CurrentPosition);
                    }
                    Log.i("onScreenOffffff", "zanting   " + CurrentPosition);
                    isplay = false;
                }
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //视频文件后缀名改为maid 用户手机自带视频播放器检测不到
                FileViewer.renameFile(SDPATH , video.getActionList().get(point).getID() + ".mp4", video.getActionList().get(point).getID() + ".maid");

                CurrentPosition = 2000000000;
                // if (!videotype || (InternetUtils.internett(StrengthVideoPlayActivity.this) && videotype)) {
                ++point;
                Log.i("ppppppointvvv1", point + "");
                Log.i("PPPPPoint", point + "  完成播放  " + video.getActionList().size());
                if (point == video.getActionList().size() && point != 0) {
                    Toast.makeText(StrengthVideoPlayActivity.this, "力量训练完成", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(StrengthVideoPlayActivity.this, StrengthSportSummarizeActivity.class);
                    i.putExtra("VID", video.getID());
                    i.putExtra("UID", UID);
                    i.putExtra("SportCal", video.getTotalCal());
                    i.putExtra("SportDuration", timeSport + "");
                    i.putExtra("SportDate", time);
                    i.putExtra("Tip", video.getTip());
                    i.putExtra("Quotes", video.getQuotes());
                    startActivity(i);
                    if (StrengthSportActivity.activity != null) {
                        StrengthSportActivity.activity.finish();
                    }
                    finish();
                    return;
                } else {
                    int restTime = Integer.parseInt(video.getActionList().get(point - 1).getInterval());
                    Log.i("SSSSSSSSSSDDDS", restTime + "");
                    if (restTime != 0) {
                        Log.i("SSSSSSSSSSDDDS", "111111111");
                        isflag3 = true;
                        handler.sendEmptyMessage(6);
                        video_play_rest_lay.setVisibility(View.VISIBLE);
                        new MyCountDownTimer(restTime * 1000 + 1000, 1000).start();
                        String path = SDPATH  + "/" + video.getActionList().get(point).getID() + ".maid";
                        File file = new File(path);
                        if (!file.exists()) {
                            // if(!downloading){
                            downloadFile();
                            //  }

                        } else {
                            start();
                        }
                    } else if (!downloading) {
                        Log.i("SSSSSSSSSSDDDS", "2222222222");
                        isflag3 = false;
                        start();
                    }
                }
                //   }


            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                isplay = false;
                if (InternetUtils.internett(StrengthVideoPlayActivity.this)) {

                    if (!videoView.isPlaying() && !downloading) {
                        if (videoView.canPause()) {
                            videoView.pause();
                        }
                        showDialog(true);
                        downloadFile();
                    }
                } else {
                    Toast.makeText(StrengthVideoPlayActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (!downloading) {
                    if (CurrentPosition != 2000000000) {
                        video_play_parseOrplay.setImageResource(R.drawable.video_pause);
                        videoView.seekTo((int) CurrentPosition);
                        videoView.pause();
                        Log.i("PPPPPAAA准备播放", point + "");
                    }
                    totalTime = mp.getDuration();
                    Log.i("PPPPPP准备播放1AAAA", totalTime + "");

                    // videoView.seekTo((int) CurrentPosition);
                    count = Integer.parseInt(video.getActionList().get(point).getCount());
                    if (count == 1) {
                        Log.i("PPPPPP准备播放2", count + "");
                        flag = true;
                        count = (int) totalTime;
                    } else {
                        flag = false;
                    }
                    Log.i("PPPPPP准备播放3", count + "");
                    if (isflag3) {//休息时间缓冲完成的视频 先暂停
                        if (videoView.canPause()) {
                            videoView.pause();
                            Log.i("PPPPPP准备播放4", count + "");
                        }
                    }
                }

            }
        });
        video_play_parseOrplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     Log.i("设置", isplay + "");
                if (!downloading) {
                    if (videoView.isPlaying()) {
                        isplay = false;
                        video_play_parseOrplay.setImageResource(R.drawable.video_pause);
                        videoView.pause();

                    } else {
                        if (true) {
                            isplay = true;
                            video_play_parseOrplay.setImageResource(R.drawable.video_start);
                      /*  if (videoView.getCurrentPosition() == videoView.getDuration()) {
                            CurrentPosition = 2000000000;
                            start();
                        } else {*/
                            if (CurrentPosition != 2000000000) {
                                videoView.seekTo((int) CurrentPosition);
                            }
                            videoView.start();
                            //  }
                            isflag2 = true;
                            CurrentPosition = 2000000000;

                        } else {
                            Toast.makeText(StrengthVideoPlayActivity.this, "当前无网络连接", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        registServiceToActivityReceiver();//注册广播

    }

    private BroadcastReceiver ServiceToActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i("SSSSSSSSSSSAAA", "SSSSS线程SHUNHUAN内部");

            if (!isflag) {
                return;
            }
            if (downloading) {
                return;
            }
           /* if (videotype && !InternetUtils.internett(StrengthVideoPlayActivity.this)) {//时时监测网络
                handler.sendEmptyMessage(8);
                return;
            }*/
            if (isflag3) {//休息时间
                return;
            }
            if (CurrentPosition != 2000000000) {
                handler.sendEmptyMessage(7);
                //  return;
            }
            if (videoView.isPlaying()) {//正在播放视频时更新计时数据
                totalTime = videoView.getDuration();
                Message message = Message.obtain();
                //  Log.i("SSSSSSSSSSS", "SSSSS线程SHUNHUAN内部");
                message.what = 1;
                message.arg1 = videoView.getCurrentPosition();
                message.arg2 = (int) totalTime;
                handler.sendMessage(message);
            }
            Log.i("JJJJJJJ", isplay + "");


        }
    };

    private void registServiceToActivityReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ServiceToActivityReceiver");
        this.registerReceiver(ServiceToActivityReceiver, filter);
    }

    private void setDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("退出之后本次运动将不会被记录，是否确定退出？")//设置显示的内容
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        FileViewer.renameFile(SDPATH, video.getActionList().get(point).getID() + ".mp4", video.getActionList().get(point).getID() + ".maid");
                        videoView.stopPlayback();
                        // sendBroadcast(intentRecever);
                        finish();

                    }

                }).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isflag3) {
            isOnprase = true;
            CurrentPosition = videoView.getCurrentPosition();
            //ShareUitls.putString(getApplicationContext(), "CurrentPosition", CurrentPosition + "");
            if (videoView.isPlaying()) {
                videoView.pause();
                video_play_parseOrplay.setImageResource(R.drawable.video_pause);
                Log.i("tttttttttttonPause", "zanting   " + CurrentPosition);
            }
            isplay = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        ShareUitls.putString(StrengthVideoPlayActivity.this, "StrengthVideoPlayActivityistop", "yes");//用来记录当前界面是否处于最前端  当处于最前端是  接收到友盟推送的消息 点击不进入 消息详情

        if (!isflag3) {
            if (isOnprase) {
                // int CurrentPosition = Integer.parseInt(ShareUitls.getString(StrengthVideoPlayActivity.this, "CurrentPosition", "2000000000"));
                if (CurrentPosition != 2000000000) {
                    if (isplay) {
                        videoView.pause();
                        video_play_parseOrplay.setImageResource(R.drawable.video_pause);
                        Log.i("tttttttttttonResume", "zanting   " + CurrentPosition);
                    }
                    isplay = false;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareUitls.putString(StrengthVideoPlayActivity.this, "StrengthVideoPlayActivityistop", "");//用来记录当前界面是否处于最前端  当处于最前端是  接收到友盟推送的消息 点击不进入 消息详情

        isflag = false;
        // sendBroadcast(intentRecever);
        stopService(intentService);
        try {//如果没有下载的话 也就没有注册广播  此刻解除注册会异常
            unregisterReceiver(ServiceToActivityReceiver);
        } catch (Exception e) {
        }


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {//横竖屏幕
        super.onConfigurationChanged(newConfig);
        Log.i("onConfigurati", "A");
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            videoView.setLayoutParams(layoutParams);

            Log.i("onConfigurati", "B");
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("onConfigurati", "C");
        }
    }


    private void downloadFile() {//某视频播放异常重新下载播放
        if (!downloading) {
            downloading = true;

            // File palyfile=new File(playvideo);


            String p = Constant.BASE_URL + "/" + video.getActionList().get(point).getAddress();
            Log.i("ppppppointvvv2", point + "");
            String url = "";
            char[] c = p.toCharArray();
            for (int i = 0; i < p.length(); i++) {
                url += GetUtf8.getUTF8XMLString(String.valueOf(c[i]));

            }

            String path = new File(SDPATH + "/").getPath();
            File file = new File(path);
            if (!file.exists()) {
                Log.e("aaaaaaaaaccc", "路径不存在");
                file.mkdirs();
            }
            File videopath = new File(file.getPath(), video.getActionList().get(point).getID() + ".maid");
            RequestParams requestParams = new RequestParams(url);
            requestParams.setSaveFilePath(videopath.getAbsolutePath());
            x.http().get(requestParams, new Callback.ProgressCallback<File>() {
                @Override
                public void onWaiting() {
                }

                @Override
                public void onStarted() {
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {

                }


                @Override
                public void onSuccess(File result) {
                    downloading = false;
                    showDialog(false);

                    Log.i("ppppppointvvv3", point + "");
                    File temp = new File(SDPATH  + "/" + video.getActionList().get(point).getID() + ".maid");
                    if (temp.exists()) {
                        start();
                    } else {
                        downloadFile();
                    }

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    downloading = false;
                    showDialog(false);
                    Toast.makeText(StrengthVideoPlayActivity.this, "视频播放异常", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    downloading = false;
                    //getRegistrationId();
                    //  popupWindow.dismiss();
                }

                @Override
                public void onFinished() {
                    downloading = false;
                    //  popupWindow.dismiss();
                }
            });

        }
    }

}
