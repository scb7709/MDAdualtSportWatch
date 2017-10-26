package com.headlth.management.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.headlth.management.R;
import com.headlth.management.entity.VersionClass;
import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.myview.BottomMenuUpdateAPPDialog;
import com.headlth.management.myview.MyToash;
import com.headlth.management.myview.NumberProgressBar;
import com.headlth.management.myview.PubLicDialog;
import com.headlth.management.service.UpdateService;

import org.xutils.HttpManager;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Date;


/**
 * Created by abc on 2017/8/10.
 */
public class UpadteApp {
    private Activity activity;
    private VersionClass.Version version;
    private UpdateResult updateResult;


    private static VersionClass.Version Version;
    public static BottomMenuUpdateAPPDialog bottomMenuUpdateAPPDialog;


    public interface UpdateResult {
        void onSuccess();

        void onError();
    }


    public UpadteApp(Activity activity, VersionClass.Version version, boolean update, UpdateResult updateResult) {
        this.activity = activity;
        this.version = version;
        this.updateResult = updateResult;
        if (!update) {
            downloaddialog();
            downloadFile(version.DownloadUrl);
        } else {
            showUpdateDialog();
        }
    }


    /**
     * 弹出升级对话框
     */
    private void showUpdateDialog() {
        String[] Description = version.Description.split(";");
        String description = "";
        for (int i = 0; i < Description.length; i++) {
            description += Description[i] + ";\n";
        }
        PubLicDialog.showNotDialog(activity, new String[]{"最新版本:" + version.VersionName, description, "立即更新", "以后再说"}, new PubLicDialog.PubLicDialogOnClickListener() {
            @Override
            public void setPositiveButton() {
                downloaddialog();
                downloadFile(version.DownloadUrl);
            }
        });
    }

    /**
     * 下载apk
     */
    private PopupWindow popupWindow;
    //下载的对话框
    private SeekBar progressBar;//下载进度条
    private TextView downtext;//下载进度条
    HttpManager httpManager;
    NumberProgressBar numberProgressBar;

    protected void downloaddialog() {


        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_download, null);
        popupWindow = new PopupWindow(view, ImageUtil.dp2px(activity, 300), ImageUtil.dp2px(activity, 400), true);
        progressBar = (SeekBar) view.findViewById(R.id.dialog_download_progress);
        // numberProgressBar = (NumberProgressBar) view.findViewById(R.id.dialog_download_NumberProgressBar);


        downtext = (TextView) view.findViewById(R.id.dialog_download_tv);
        TextView dialog_download_Description = (TextView) view.findViewById(R.id.dialog_download_Description);
        TextView dialog_download_versionname = (TextView) view.findViewById(R.id.dialog_download_versionname);

        String[] Description = version.Description.split(";");
        String description = "";
        for (int i = 0; i < Description.length; i++) {
            description += Description[i] + ";\n\n";
        }

        dialog_download_Description.setText(description);

        dialog_download_versionname.setText(version.VersionName);
        Button cancel = (Button) view.findViewById(R.id.dialog_download__cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (httpManager != null) {
                }

                updateResult.onError();
            }
        });


        popupWindow.setTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(new View(activity), Gravity.CENTER, 0, 0);
    }

    private void downloadFile(final String url) {
        MyToash.Log("url==" + url);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(activity, "sdcard不存在!", Toast.LENGTH_SHORT).show();
            updateResult.onError();
            return;
        }

        // 文件在sdcard的路径
        File tempfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/apk/" + new Date().getTime() + "Version");
        if (!tempfile.exists()) {
            tempfile.mkdirs();
        }
        String path = tempfile.getPath();
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        httpManager = x.http();
        httpManager.get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                int percent = (int) (current * 100 / total);
//                System.out.println("下载进度:" + percent + "%");
                downtext.setText("正在下载，已完成:" + percent + "%");// 更新下载进度
                progressBar.setMax((int) (total / 1000));
                progressBar.setProgress((int) (current / 1000));

            }

            @Override
            public void onSuccess(File result) {
                Toast.makeText(activity, "下载成功", Toast.LENGTH_SHORT).show();
                VersonUtils.installApk(result, activity);// 安装apk
                popupWindow.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                updateResult.onError();
                ex.printStackTrace();
                Toast.makeText(activity, "下载失败，请检查网络和SD卡", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();

            }

            @Override
            public void onCancelled(CancelledException cex) {
                updateResult.onError();
                //getRegistrationId();
                popupWindow.dismiss();
            }

            @Override
            public void onFinished() {
                popupWindow.dismiss();
            }
        });
    }


    public static boolean updateAPP(final Activity activity, boolean nowupdate) {
        //  BottomMenuUpdateAPPDialog bottomMenuUpdateAPPDialog=null;
        if (Version == null) {
            Version = ShareUitls.getVersion(activity);
        }
        //  Log.e("版本aaaaa",Version.VersionCode+" "+VersonUtils.getVerisonCode(activity));
        boolean isupdate = Version != null && VersonUtils.getVerisonCode(activity) != -1 && Version.VersionCode > VersonUtils.getVerisonCode(activity);
        if (isupdate) {
            if (nowupdate) {
                String[] Description = Version.Description.split(";");
                String description = "";
                for (int i = 0; i < Description.length; i++) {
                    if (i < Description.length - 1) {
                        description += Description[i] + ";\n\n";
                    } else {
                        description += Description[i] + ";";
                    }
                }
                BottomMenuUpdateAPPDialog.Builder builder = new BottomMenuUpdateAPPDialog.Builder(activity).setTitle("新版本" + Version.VersionName);
                builder.addMenu(description, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                        .addMenu("立即更新", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bottomMenuUpdateAPPDialog.dismiss();
                                Intent intent = new Intent(activity, UpdateService.class);
                                intent.putExtra("apkUrl", Version.DownloadUrl);
                                activity.startService(intent);
                            }
                        });
                bottomMenuUpdateAPPDialog = builder.create();
                bottomMenuUpdateAPPDialog.show();
            }


            return true;
        }
        return false;
    }


    public static boolean More21() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean MoreAPI(int level) {
        return Build.VERSION.SDK_INT >= level;
    }

}
