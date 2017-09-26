package com.headlth.management.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.acs.MuchHeigthImangView;

import com.headlth.management.entity.Video;

import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.myview.NumberProgressBar;
import com.headlth.management.service.NetworkService;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.FileViewer;
import com.headlth.management.utils.GetUtf8;
import com.headlth.management.utils.InternetUtils;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.HttpUtils;
import com.squareup.picasso.Picasso;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.HttpManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
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
 * Created by abc on 2016/7/5.
 */
@ContentView(R.layout.activity_strengthsport)
public class StrengthSportActivity extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;




    @ViewInject(R.id.strengthsport_time)
    TextView strengthsport_time;
    @ViewInject(R.id.strengthsport_action)
    TextView strengthsport_action;
    @ViewInject(R.id.strengthsport_consume)
    TextView strengthsport_consume;
    @ViewInject(R.id.strengthsport_sport_time)
    TextView strengthsport_sport_time;

    @ViewInject(R.id.strengthsport_pic1)
    ImageView strengthsport_pic1;

    @ViewInject(R.id.acyivity_strength_parse)
    ImageView acyivity_strength_parse;

    @ViewInject(R.id.strengthsport_btstart)
    Button strengthsport_btstart;
    private int carousel;//轮播图片数量


    @ViewInject(R.id.strengthsport_fitmans)
    TextView strengthsport_fitmans;//适合人群
    @ViewInject(R.id.strengthsport_sport_principle)
    TextView strengthsport_sport_principle;//训练原则

    @ViewInject(R.id.strengthsport_pic_layout)
    LinearLayout strengthsport_pic_layout;//

    @ViewInject(R.id.strengthsport_process_layout)
    LinearLayout strengthsport_process_layout;//
    @ViewInject(R.id.strengthsport_process_text)
    TextView strengthsport_process_text;//


    @ViewInject(R.id.strengthsport_process_numberProgressbar)
    NumberProgressBar strengthsport_process_numberProgressbar;//


    private Gson g = new Gson();
    private Video video;

    private String Stage;//当前是第几阶段
    private List<String> videoIdlist = new ArrayList<>();//网络端的ID本地地址
    private List<String> localvideolist = new ArrayList<>();//本地所有视频的地址


    // private List<Video.SubVideo> netvideolist = new ArrayList<>();//没有被下载视频

    // private List<String> helpervideoIdlist = new ArrayList();//数据库里寸的所有视频的ID
    Intent i;
    private int possition;//当前下载视频的位置
    private HttpManager httpManager;
    Callback.Cancelable cancelable;
    private long Total;
    private long Current;//当前已经下载的视频大小
    int CurrentCount;//当前正在下载的是第几的个视频
    private long Surplus;//还剩余的视频大小

    private String loadingvideo = "";//当前正在下载的视频

    private boolean downloading;//是否正在下载状态（true  正在下载
    private boolean downloadstop;//是否手动点击了暂停
    private boolean nonet = true;//没网提示一次（true 需要提示）
    private boolean isload;//是否开启过下载
    private boolean isnowifi = true;//移动网络状态下提示下载 只提示一次（true  需要提示）
    private boolean agreenowifiload;//同意移动网络下载
    private int redownload;//下载失败 重新下载 尝试三次
    private boolean proscenium;//当前页面是否处于前台，用于视频下载完了是否直接播放
    private boolean canPlay;//是否是都已经下载完了可以直接播放的 用于后台下载完了 回到界面需要再次点击开始锻炼才能进入一下页面播放
    int count;
    public static Activity activity;
    private BottomMenuDialog bottomMenuDialog;
    private BottomMenuDialog bottomMenuDialogMobileNetworks;
    private String SDPATH;//SD卡根目录

    //  Intent intentRecever;//停止服务的
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    private void initialize() {
        x.view().inject(this);
        view_publictitle_title.setText("力量训练");
        PushAgent.getInstance(this).onAppStart();
        activity = this;
        //  intentRecever=  new Intent("ActivityToServiceReceiver");
        httpManager = x.http();
        getData();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        i = new Intent(StrengthSportActivity.this, StrengthVideoPlayActivity.class);
    }

    private void startSport() {
        if (video != null) {

            for(int point=0;point<video.getActionList().size();point++){//视频播放过程中出现异常退出 会导致 mp4文件没有改为maid文件
                FileViewer.renameFile(SDPATH , video.getActionList().get(point).getID() + ".mp4", video.getActionList().get(point).getID() + ".maid");
            }

            List<String> list = FileViewer.getListFiles(SDPATH, "maid", true);
            String errprvideo = ShareUitls.getString(StrengthSportActivity.this, "errprvideo", "");//看是否有未完成下载的视频
            if (videoIdlist.size() != 0) {
                Log.e("fffffffffffAA", videoIdlist.size() + "   " + video.getActionList().size() + "   " + list.toString() + " ssss  " + videoIdlist.toString());





                if (list.containsAll(videoIdlist) && errprvideo.length() == 0) {//本地视频下载好的 没有出现异常视屏
                    isload = false;
                    i.putExtra("Video", video);
                    startActivity(i);
                    //   finish();
                } else {

                    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(StrengthSportActivity.this, "手机内存不足!", Toast.LENGTH_SHORT).show();

                        return;
                    } else {
                        if (list.size() != 0) {//去掉本地多余的视频
                            for (int i = 0; i < list.size(); i++) {
                                if (!videoIdlist.contains(list.get(i))) {
                                    File file = new File(list.get(i));
                                    if (file.exists()) {
                                        file.delete();
                                    }

                                }
                            }
                        }
                        Surplus = 0;
                        if (list.size() != 0) {
                            for (String localid : list) {//算出总需要下载的视频大小
                                for (Video.SubVideo subVideo : video.getActionList()) {
                                    //  ++count;
                                    if (localid.equals(SDPATH + "/" + subVideo.getID() + ".maid")) {
                                        Surplus += subVideo.getSize();
                                        Log.i("ffffffffffffXXX11", "" + Surplus);
                                    }

                                }

                            }
                            Log.i("ffffffffffffXXX22", "" + Surplus);
                            Surplus = video.getVideoSize() - Surplus;
                        } else {
                            Surplus = video.getVideoSize();
                        }
                        Log.i("ffffffffffffXXX33", "" + Surplus);

                        String str = "";
                        final int nettype = InternetUtils.getNetworkState(StrengthSportActivity.this);

                        if (nettype != 0) {//有网
                            if (nettype == 1) {
                                str = "我们为您安排了本次运动的视频,当前是wifi网络可以放心下载";
                            } else {
                                str = "我们为您安排了本次运动的视频,当前是移动网络是否选择下载";
                            }
                            bottomMenuDialog = new BottomMenuDialog.Builder(this)
                                    .addMenu(str, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    })
                                    .addMenu("下载本次视频(约" + (Surplus / (1024 * 1024)) + "M)", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            long sdsize[] = FileViewer.getSDSpace(StrengthSportActivity.this);
                                            Log.e("aaaaaaaaaccc内存", sdsize[0] + "   " + sdsize[1] + "'    " + Surplus);
                                            if (sdsize[1] <= Surplus) {//手机内存不够

                                                Log.e("aaaaaaaaaccc内存", sdsize[0] + "" + sdsize[1] + "'  " + Surplus);

                                                Toast.makeText(StrengthSportActivity.this, "手机内存不足!", Toast.LENGTH_SHORT).show();
                                                return;
                                            } else {
                                                if (nettype == 1) {
                                                } else {
                                                    isnowifi = false;
                                                    agreenowifiload = true;
                                                }
                                                startLoading();
                                            }
                                            bottomMenuDialog.dismiss();
                                        }
                                    }).create();
                            bottomMenuDialog.show();

                        } else {
                            Toast.makeText(StrengthSportActivity.this, "当前无网络连接", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    private void startLoading() {
        isload = true;
        Total = video.getVideoSize();
        Log.e("aaaaaaaaaaaacccaa", Total + "    ss");
        strengthsport_process_numberProgressbar.setMax((int) (Total));
        String errprvideo = ShareUitls.getString(StrengthSportActivity.this, "errprvideo", "");//看是否有未完成下载的视频
        if (errprvideo.length() != 0) {
            File file = new File(errprvideo);
            if (file.exists()) {
                Log.e("aaaaaaaaaccc", "删除");
                file.delete();
                ShareUitls.putString(StrengthSportActivity.this, "errprvideo", "");//
            }
        }//上次有未完成下载的视频 直接删除重新下载

        // List<String> localvideolist = FileViewer.getListFiles(Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/maidongvideo" + Stage, "mp4", true);//获取本地已经下载好的所有视频文件名字
        strengthsport_btstart.setVisibility(View.GONE);
        strengthsport_process_layout.setVisibility(View.VISIBLE);
        registServiceToActivityReceiver();//注册广播
        startService(new Intent(StrengthSportActivity.this, NetworkService.class));//开启服务用于全程检测
        //downloaddialog();


        String path = new File(SDPATH + "/").getPath();
        File file = new File(path);
        if (!file.exists()) {
            Log.e("aaaaaaaaaccc", "路径不存在");
            file.mkdirs();
        }
        downloadFile();
    }

    @Event(value = {R.id.view_publictitle_back
            , R.id.strengthsport_btstart
            , R.id.strengthsport_process_layout
    })
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                if (!downloading) {//正在下载不允许返回
                    if (isload) {//已经开始过下载 移除服务广播
                        stopService(new Intent(StrengthSportActivity.this, NetworkService.class));
                        try {
                            unregisterReceiver(ServiceToActivityReceiver);
                        } catch (Exception e) {
                        }

                    }
                    finish();

                }

                break;
            case R.id.strengthsport_btstart:
                startSport();
                break;
            case R.id.strengthsport_process_layout:
                if (!downloadstop) {//当前不处于手动暂停状态 点击暂停
                    acyivity_strength_parse.setVisibility(View.VISIBLE);
                    if (downloading) {//正在下载 点击暂停
                        if (cancelable != null && !cancelable.isCancelled()) {
                            cancelable.cancel();
                        }
                        ShareUitls.putString(StrengthSportActivity.this, "errprvideo", loadingvideo);//记录是下载那个视频失败的
                        downloading = false;
                    }
                } else {//当前处于暂停状态 点击继续下载
                    acyivity_strength_parse.setVisibility(View.GONE);
                    if (!downloading) {//没处于下载中再能继续下载

                        int internet = InternetUtils.getNetworkState(StrengthSportActivity.this);
                        if (internet != 0) {//有网络
                            if (internet != 1 && isnowifi) {
                                isnowifi = false;

                                List<String> list = FileViewer.getListFiles(SDPATH, "maid", true);
                                Surplus = 0;
                                if (list.size() != 0) {
                                    for (String localid : list) {
                                        for (Video.SubVideo subVideo : video.getActionList()) {
                                            //  ++count;
                                            if (localid.equals(SDPATH + "/" + subVideo.getID() + ".maid")) {
                                                Surplus += subVideo.getSize();
                                                Log.i("ffffffffffffXXX11", "" + Surplus);
                                            }

                                        }

                                    }
                                    Log.i("ffffffffffffXXX22", "" + Surplus);
                                    Surplus = video.getVideoSize() - Surplus;
                                } else {
                                    Surplus = video.getVideoSize();
                                }


                                bottomMenuDialogMobileNetworks = new BottomMenuDialog.Builder(StrengthSportActivity.this)
                                        .addMenu("使用移动网络下载视频大约会消耗" + (Surplus / (1024 * 1024)) + "M流量)", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                            }
                                        })
                                        .addMenu("继续下载", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                agreenowifiload = true;
                                                if (!downloading) {//不处于正在下载状态 继续下载

                                                    downloadFile();

                                                }
                                                bottomMenuDialogMobileNetworks.dismiss();
                                            }
                                        }).create();
                                bottomMenuDialogMobileNetworks.show();
                            } else if (internet == 1 || agreenowifiload) {//wifi 或者移动网络同意过
                                downloadFile();
                            } else {

                            }
                        } else {
                            Toast.makeText(StrengthSportActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                downloadstop = !downloadstop;
                break;

        }
    }

    private String stringForTime(int timeMs) {
        Log.i("RRRRRRR", timeMs + "");
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs;

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

    private void getData() {
      //  ShareUitls.putString(StrengthSportActivity.this, "todayvideo", "");//
        String oldtodayvideotime = ShareUitls.getString(StrengthSportActivity.this, "todayvideotime", "");
        String newtodayvideotime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String todayvideo = ShareUitls.getString(StrengthSportActivity.this, "todayvideo", "");

        if (oldtodayvideotime.equals(newtodayvideotime) && todayvideo.length() != 0) {
            setData(todayvideo);
        } else if (InternetUtils.internet(this)) {
            RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostVideoRequest");
            params.addBodyParameter("VID", ShareUitls.getString(StrengthSportActivity.this, "vlist", "") + "");
            params.addBodyParameter("UID", ShareUitls.getString(StrengthSportActivity.this, "UID", "") + "");
            params.addBodyParameter("ResultJWT", ShareUitls.getString(StrengthSportActivity.this, "ResultJWT", "0"));

            Log.i("rrrrrrAASCC",ShareUitls.getString(StrengthSportActivity.this, "vlist", "") + "   "+ShareUitls.getString(StrengthSportActivity.this, "UID", "") + ""+ShareUitls.getString(StrengthSportActivity.this, "ResultJWT", "0"));
            HttpUtils.getInstance(StrengthSportActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {

                        public void onResponse(String response) {
                            ShareUitls.putString(StrengthSportActivity.this, "todayvideo", response);//
                            String todayvideotimee = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            ShareUitls.putString(StrengthSportActivity.this, "todayvideotime", todayvideotimee);
                            setData(response);
                        }

                        @Override
                        public void onErrorResponse(Throwable ex) {

                            Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                            return;

                        }
                    }

            );
        }
    }

    private void setData(String response) {
        strengthsport_btstart.setVisibility(View.VISIBLE);
        Log.e("rrrrrrrrrrrrr", ShareUitls.getString(StrengthSportActivity.this, "vlist", "") + "22222222" + response.toString());

        try {
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();

           /* WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用*/
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width / 4, 150);

            video = g.fromJson(String.valueOf(new JSONObject(new JSONObject(response.toString()).getJSONObject("Video").toString())), Video.class);
            Stage = video.getStage();
            SDPATH=Environment.getExternalStorageDirectory().getAbsolutePath()+ "/maidong/maidongvideo/maidongvideo" + Stage;
            strengthsport_fitmans.setText("      -" + video.getFitPerson());
            strengthsport_sport_time.setText("      -" + video.getTrainTime());
            strengthsport_sport_principle.setText("      -" + video.getTrainRule());

            Log.i("RRRRRRRRR", stringForTime(Integer.parseInt(video.getDuration())));
            strengthsport_time.setText(stringForTime(Integer.parseInt(video.getDuration())));

            strengthsport_action.setText(video.getActionCount());

            strengthsport_consume.setText((int) Double.parseDouble(video.getTotalCal()) + "");
            Log.e("rrrrrVIDER", "" + video.getTotalCal());
            setIcon(strengthsport_pic1, Constant.BASE_URL + "/" + video.getImgUrl());
            Log.e("rrrrrVIDER", video.toString());
            int size = video.getActionList().size();
            Log.e("rrrrrSize", size + "");
            for (int i = 0; i < size; i++) {
                String url = Constant.BASE_URL + "/" + video.getActionList().get(i).getImgUrl();

                MuchHeigthImangView imageView = new MuchHeigthImangView(StrengthSportActivity.this);
                setIcon(imageView, url);
                View view = new View(StrengthSportActivity.this);
                view.setMinimumWidth(10);
                strengthsport_pic_layout.addView(imageView);
                strengthsport_pic_layout.addView(view);
            }
            if (video.getActionList() != null && video.getActionList().size() != 0) {
                for (Video.SubVideo subVideo : video.getActionList()) {
                    videoIdlist.add(SDPATH + "/" + subVideo.getID() + ".maid");
                }

            }


        } catch (JSONException e) {
            ShareUitls.putString(StrengthSportActivity.this, "todayvideo", "");//
            getData();
            e.printStackTrace();
            Log.e("rrrrrVIDER", "异常走一波");
        }
    }


    //加载图片
    private void setIcon(ImageView iv, String url) {
        Log.e("aaaaurl", url);
        String path = "";
        char[] c = url.toCharArray();
        for (int i = 0; i < url.length(); i++) {
            path += GetUtf8.getUTF8XMLString(String.valueOf(c[i]));
        }
        if (url.length() != 0) {
            Picasso.with(StrengthSportActivity.this)
                    .load(path)//图片网址
                    .placeholder(R.mipmap.logo)//默认图标
                    .into(iv);//控件
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void downloadFile() {
        nonet = true;//没网提示恢复
        downloading = true;
        String errprvideo = ShareUitls.getString(StrengthSportActivity.this, "errprvideo", "");//看是否有未完成下载的视频

        Log.i("ACVBBFFF", errprvideo+"        ggggggggg");
        if (errprvideo.length() != 0) {
            File file = new File(errprvideo);
            if (file.exists()) {
                if (possition > 0) {
                    --possition;//指针减一
                }
                Log.e("aaaaaaaaaccc", "删除");
                file.delete();
            }

            Log.i("ACVBBFFF", errprvideo);
            ShareUitls.putString(StrengthSportActivity.this, "errprvideo", "");//
        }//上次有未完成下载的视频 直接删除重新下载
        localvideolist.clear();
        localvideolist = FileViewer.getListFiles(SDPATH, "maid", true);//获取本地已经下载好的所有视频文件名字


        Current = 0;
        CurrentCount = 0;
        if (localvideolist.size() != 0) {//算出已经下载的视频总大小
            for (String localid : localvideolist) {
                for (Video.SubVideo subVideo : video.getActionList()) {
                    //  ++count;
                    if (localid.equals(SDPATH + "/" + subVideo.getID() + ".maid")) {
                        Current += subVideo.getSize();
                        CurrentCount++;
                    }

                }

            }
        }
        if (!localvideolist.containsAll(videoIdlist) && possition < video.getActionList().size()) {
            if (localvideolist.contains(SDPATH + "/" + video.getActionList().get(possition).getID() + ".maid")) {//已经下载的跳过
                ++possition;
                downloadFile();
            } else {
                final Video.SubVideo subVideo = video.getActionList().get(possition);
                // Log.e("aaaaaaaaaccc", str[0]+""+str[1]);
                loadingvideo = SDPATH + "/" + subVideo.getID() + ".maid";


                String Url = Constant.BASE_URL + "/" + subVideo.getAddress();
                Log.e("aaaaaaaaaccc", Url);
                String url = "";
                char[] c = Url.toCharArray();
                for (int i = 0; i < Url.length(); i++) {
                    url += GetUtf8.getUTF8XMLString(String.valueOf(c[i]));
                    //  Log.i("XXXXXXXXXXXX",path);
                }
                Log.e("aaaaaaaaacccvv", url);
                // 文件在sdcard的路径
                String path = new File(SDPATH + "/").getPath();
                File file = new File(path);
                if (!file.exists()) {
                    Log.e("aaaaaaaaaccc", "路径不存在");
                    file.mkdirs();
                }
                File videopath = new File(file.getPath(), subVideo.getID() + ".maid");
                // subVideo.setLocalAddress(file.getPath() + "/" + subVideo.getID() + ".mp4");

                Log.i("ACVBBFFF", "开始");
                RequestParams requestParams = new RequestParams(url);
                requestParams.setSaveFilePath(videopath.getAbsolutePath());
                cancelable = httpManager.get(requestParams, new Callback.ProgressCallback<File>() {
                    @Override
                    public void onWaiting() {
                        Log.i("ACVBBFFF", "开始1");
                    }

                    @Override
                    public void onStarted() {
                        Log.i("ACVBBFFF", "开始2");
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isDownloading) {
                        Log.i("ACVBBFFF", "开始3");
                        downloading = true;
                        Log.e("aaaaaaaaaaaacccaa", total + "  " + subVideo.getID());
                        strengthsport_process_text.setText((CurrentCount + 1) + "/" + videoIdlist.size());// 更新下载进度
                        //strengthsport_process_numberProgressbar.setMax((int) (total / 1000));
                        // strengthsport_process_numberProgressbar.setProgress((int) (current / 1000));

                        strengthsport_process_numberProgressbar.setMax((int) (Total / 1000));
                        strengthsport_process_numberProgressbar.setProgress((int) (((Current + current) / 1000)));


                    }

                    @Override
                    public void onSuccess(File result) {
                        Log.i("ACVBBFFF", "开始4");
                        //  if(canaleLoad) {//没有被取消下载 也就是暂停
                        redownload = 0;//下载失败重新下载次数标记 设置为 0
                        Current += subVideo.getSize();
                        ShareUitls.putString(StrengthSportActivity.this, "errprvideo", "");//记录是下载那个视频失败的 设置为空
                        loadingvideo = "";
                        ++possition;
                        downloadFile();
                        //  }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.i("ACVBBFFF", "开始5");
                        Log.i("","");
                        downloading = false;
                        ex.printStackTrace();
                        ShareUitls.putString(StrengthSportActivity.this, "errprvideo", loadingvideo);//记录是下载那个视频失败的
                        int internet = InternetUtils.getNetworkState(StrengthSportActivity.this);
                        switch (internet) {
                            case 0://没有网
                                Toast.makeText(StrengthSportActivity.this, "下载失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                                break;
                            case 1://wifi
                                if (!downloadstop && !downloading && redownload <= 3) {//不处于手动暂停状态 不处于 正在下载状态 重复次数不到三次
                                    redownload++;
                                    downloadFile();
                                } else {
                                    Toast.makeText(StrengthSportActivity.this, "下载异常", Toast.LENGTH_SHORT).show();
                                }

                                break;
                            default://移动网
                                if (agreenowifiload && !downloadstop && !downloading && redownload <= 3) {//同意过移动网下载，不处于手动暂停状态 不处于 正在下载状态 重复次数不到三次
                                    redownload++;
                                    downloadFile();
                                } else {
                                    Toast.makeText(StrengthSportActivity.this, "下载异常", Toast.LENGTH_SHORT).show();
                                }
                                break;

                        }


                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                        Log.i("ACVBBFFF", "开始6");
                        downloading = false;
                        ShareUitls.putString(StrengthSportActivity.this, "errprvideo", loadingvideo);//记录是下载那个视频失败的
                    }

                    @Override
                    public void onFinished() {
                        Log.i("ACVBBFFF", "开始7");
                        //  popupWindow.dismiss();
                    }
                });
            }


        } else {
            downloading = false;
            //重置指针在校验一遍
            List temp = FileViewer.getListFiles(SDPATH, "maid", true);//获取本地已经下载好的所有视频文件名字
            if (temp.size() < video.getActionList().size()) {
                possition = 0;
                downloadFile();
            } else {

                isload = false;
                Log.i("counttttt", "" + count);

                strengthsport_process_layout.setVisibility(View.GONE);
                canPlay = true;
                // sendBroadcast(intentRecever);
                stopService(new Intent(StrengthSportActivity.this, NetworkService.class));
                try {//如果没有下载的话 也就没有注册广播  此刻解除注册会异常
                    unregisterReceiver(ServiceToActivityReceiver);
                } catch (Exception e) {
                }
                if (proscenium) {//处于前台直接播放
                    startActivity(new Intent(StrengthSportActivity.this, StrengthVideoPlayActivity.class).putExtra("Video", video));
                    // finish();
                }
                strengthsport_btstart.setVisibility(View.VISIBLE);
                ShareUitls.putString(StrengthSportActivity.this, "errprvideo", "");//
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // sendBroadcast(intentRecever);

    }

    private void registServiceToActivityReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("ServiceToActivityReceiver");
        this.registerReceiver(ServiceToActivityReceiver, filter);
    }

    private BroadcastReceiver ServiceToActivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!downloadstop) {//不处于手动暂停状态

                int internet = InternetUtils.getNetworkState(StrengthSportActivity.this);
                if (internet != 1) {//非wifi状态
                    if (internet == 0) {//没有网络暂停下载 保存数据
                        if (downloading) {//正在下载
                            ShareUitls.putString(StrengthSportActivity.this, "errprvideo", loadingvideo);//记录是下载那个视频失败的
                            if (cancelable != null && !cancelable.isCancelled()) {
                                cancelable.cancel();
                            }
                            downloading = false;
                        }
                        if (nonet) {
                            nonet = false;
                            Toast.makeText(StrengthSportActivity.this, "未检测到网络连接", Toast.LENGTH_SHORT).show();
                        }

                    } else if (isnowifi) {//移动网络 没有询问过
                        if (downloading) {//正在下载
                            ShareUitls.putString(StrengthSportActivity.this, "errprvideo", loadingvideo);//记录是下载那个视频失败的
                            if (cancelable != null && !cancelable.isCancelled()) {
                                cancelable.cancel();
                            }
                            downloading = false;
                        }
                        isnowifi = false;

                        List<String> list = FileViewer.getListFiles(SDPATH, "maid", true);
                        Surplus = 0;
                        if (list.size() != 0) {
                            for (String localid : list) {
                                for (Video.SubVideo subVideo : video.getActionList()) {
                                    //  ++count;
                                    if (localid.equals(SDPATH + "/" + subVideo.getID() + ".maid")) {
                                        Surplus += subVideo.getSize();
                                        Log.i("ffffffffffffXXX11", "" + Surplus);
                                    }

                                }

                            }
                            Log.i("ffffffffffffXXX22", "" + Surplus);
                            Surplus = video.getVideoSize() - Surplus;
                        } else {
                            Surplus = video.getVideoSize();
                        }


                        bottomMenuDialogMobileNetworks = new BottomMenuDialog.Builder(StrengthSportActivity.this)
                                .addMenu("使用移动网络下载视频大约会消耗" + (Surplus / (1024 * 1024)) + "M流量)", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                })
                                .addMenu("继续下载", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        agreenowifiload = true;
                                        if (!downloading) {//不处于正在下载状态 继续下载

                                            downloadFile();

                                        }
                                        bottomMenuDialogMobileNetworks.dismiss();
                                    }
                                }).create();
                        bottomMenuDialogMobileNetworks.show();


                    } else if (agreenowifiload && !downloading) {
                        //已经允许过移动下载且不处于正在下载状态 继续下载
                        downloadFile();
                    } else {//移动网络 已经询问过  拒绝下载的情况
                        if (downloading) {//正在下载
                            ShareUitls.putString(StrengthSportActivity.this, "errprvideo", loadingvideo);//记录是下载那个视频失败的
                            if (cancelable != null && !cancelable.isCancelled()) {
                                cancelable.cancel();
                            }
                            downloading = false;
                        }
                    }

                } else if (!downloading) {//wifi 恢复 且不处于正在下载状态 继续下载

                    downloadFile();

                }
            }

        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            if (!downloading) {//正在下载不允许返回
                if (isload) {
                    stopService(new Intent(StrengthSportActivity.this, NetworkService.class));
                    try {
                        unregisterReceiver(ServiceToActivityReceiver);
                    } catch (Exception e) {
                    }

                }
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();

        proscenium = true;//处于前台
    }

    @Override
    public void onPause() {
        super.onPause();
        proscenium = false;//处于后台
    }
}
