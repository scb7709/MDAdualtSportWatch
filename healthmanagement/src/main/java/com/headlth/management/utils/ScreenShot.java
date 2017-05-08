package com.headlth.management.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by abc on 2016/7/29.
 */
public class ScreenShot {
    public static Bitmap takeScreenShot(Activity activity, String time) {
// View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

// 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("TAG", "" + statusBarHeight);

// 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
// 去掉标题栏
// Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        saveMyBitmap(b, time, false, activity);
        return b;
    }

    //保存图片
    public static void saveMyBitmap(Bitmap bitmap, String time, boolean remind, Activity activity) {

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // Toast.makeText(this, "sdcard不存在!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 文件在sdcard的路径
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/image");
        if (!file.exists()) {
            file.mkdirs();
        }
        File filepic = new File(file.getPath(), time + ".png");
        try {
            if (!filepic.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filepic);
            BufferedOutputStream out = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            out.flush();
            if (remind)
                Toast.makeText(activity, "保存成功", Toast.LENGTH_LONG).show();
            // RequestParams params = new RequestParams();
            // params.addBodyParameter("file", filepic);
            //uploadMethod(params, Constants.BASE_URL + "/file-upload");
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
