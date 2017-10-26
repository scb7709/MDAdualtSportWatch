package com.headlth.management.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.headlth.management.R;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.clenderutil.WaitDialog;
import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.utils.FileViewer;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.UpadteApp;
import com.headlth.management.utils.VersonUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * Created by abc on 2016/11/24.
 */

@ContentView(R.layout.activity_aboutmaidong)//关于迈动
public class AboutMaiDongActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;

    @ViewInject(R.id.activity_aboutmaidong_versioncode)
    private TextView activity_aboutmaidong_versioncode;

    @ViewInject(R.id.activity_aboutmaidong_cache)
    private TextView activity_aboutmaidong_cache;
    @ViewInject(R.id.fragment_my_newversion)
    private TextView fragment_my_newversion;


    private boolean isupdate;

    // VersionClass.Version version;

    private long TOTALcache;//总缓存大小
    private long VIDEOcache;//视频缓存大小
    private long IMAGEcache;//图片缓存大小
    private long APKcache;//安装包缓存大小

    WaitDialog waitDialog;
    private String SDPATH;//SD卡根目录
Activity activity;
    private BottomMenuDialog bottomMenuDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        initialize();

    }

    private void initialize() {
        activity=this;
        isupdate = UpadteApp.updateAPP(activity, false);
        if (isupdate) {
            fragment_my_newversion.setVisibility(View.VISIBLE);
        }
        //version = ShareUitls.getVersion(this);;
        activity_aboutmaidong_versioncode.setText("当前版本: " + VersonUtils.getVersionName(AboutMaiDongActivity.this));
        view_publictitle_title.setText("关于迈动");
        view_publictitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TOTALcache = FileViewer.getFolderSize(new File(SDPATH + "/maidong"));


        activity_aboutmaidong_cache.setText(FileViewer.getFormatSize(TOTALcache));
        initDialog();
    }

    private void initDialog() {
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(AboutMaiDongActivity.this);
        waitDialog.setCancleable(true);
        waitDialog.setMessage("正在清理,请稍后...");
    }

    @Event(value = {R.id.view_publictitle_back
            , R.id.activity_aboutmaidong_feedback
            , R.id.activity_aboutmaidong_clearcache
            , R.id.activity_aboutmaidong_aboutus
            , R.id.fragment_my_updateversion

    })
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.activity_aboutmaidong_feedback:
               // FeedbackAgent agent = new FeedbackAgent(this);
               // agent.startFeedbackActivity();
                break;
            case R.id.activity_aboutmaidong_clearcache:
                clearcache();
                ///
                break;
            case R.id.activity_aboutmaidong_aboutus:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.fragment_my_updateversion:

                if (isupdate) {
                    UpadteApp.updateAPP(activity, true);
                } else {
                    Toast.makeText(activity, "没有新版本可更新", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void clearcache() {
        if (TOTALcache != 0) {
            VIDEOcache = FileViewer.getFolderSize(new File(SDPATH + "/maidong/maidongvideo"));
            IMAGEcache = FileViewer.getFolderSize(new File(SDPATH + "/maidong/image"));
            APKcache = FileViewer.getFolderSize(new File(SDPATH + "/maidong/apk"));


            BottomMenuDialog.Builder builder=new BottomMenuDialog.Builder(AboutMaiDongActivity.this);
            if (VIDEOcache!=0) {
                builder. addMenu("清理缓存视频(" + FileViewer.getFormatSize(VIDEOcache) + ")", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        waitDialog.showDailog();
                        FileViewer.deleteFolderFile(SDPATH + "/maidong/maidongvideo", true, handler);
                    }
                });

            }
            if (IMAGEcache!=0) {
                builder  .addMenu("清理缓存图片(" + FileViewer.getFormatSize(IMAGEcache) + ")", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        waitDialog.showDailog();
                        FileViewer.deleteFolderFile(SDPATH + "/maidong/image", true, handler);
                    }
                });

            }
            if (APKcache!=0) {
                builder.addMenu("清理安装包(" + FileViewer.getFormatSize(APKcache) + ")", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        waitDialog.showDailog();
                        FileViewer.deleteFolderFile(SDPATH + "/maidong/apk", true, handler);
                    }
                });

            }
            builder.addMenu("全部清理", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUitls.putString(AboutMaiDongActivity.this, "anlysedata", "");//分析界面数据
                    ShareUitls.putString(AboutMaiDongActivity.this, "todaydata", "");//首页今日处方相关数据
                    ShareUitls.putString(AboutMaiDongActivity.this, "prescriptionlist", "");//推荐处方列表
                    ShareUitls.putString(AboutMaiDongActivity.this, "todayvideo", "");//今日视频数据
                    ShareUitls.putString(AboutMaiDongActivity.this, "mydata", "");//我界面数据
                    waitDialog.showDailog();
                    FileViewer.deleteFolderFile(SDPATH + "/maidong", true, handler);
                }
            }).create();

            bottomMenuDialog =builder.create();
            bottomMenuDialog.show();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (waitDialog != null) {
                TOTALcache = FileViewer.getFolderSize(new File(SDPATH + "/maidong"));
                activity_aboutmaidong_cache.setText(FileViewer.getFormatSize(TOTALcache));
                waitDialog.dismissDialog();
                bottomMenuDialog.dismiss();
            }


        }
    };
}
