package com.headlth.management.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by abc on 2016/8/4.
 *
 * bitmap 压缩
 */
public class DiskBitmap {
    public static Bitmap getDiskBitmap(String pathString, Activity activity) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                // 这个isjustdecodebounds很重要
                opt.inJustDecodeBounds = true;
                bitmap = BitmapFactory.decodeFile(pathString, opt);

                // 获取到这个图片的原始宽度和高度
                int picWidth = opt.outWidth;
                int picHeight = opt.outHeight;

                // 获取屏的宽度和高度
                WindowManager windowManager = activity.getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                int screenWidth = display.getWidth();
                int screenHeight = display.getHeight();

                // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
                opt.inSampleSize = 2;
                // 根据屏的大小和图片大小计算出缩放比例
                if (picWidth > picHeight) {
                    if (picWidth > screenWidth)
                        opt.inSampleSize = picWidth / screenWidth;
                } else {
                    if (picHeight > screenHeight)

                        opt.inSampleSize = picHeight / screenHeight;
                }

                // 这次再真正地生成一个有像素的，经过缩放了的bitmap
                opt.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(pathString, opt);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }


        return bitmap;
    }
    public  static void saveMyBitmap(Bitmap bitmap,Activity activity) {

        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            // Toast.makeText(this, "sdcard不存在!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 文件在sdcard的路径
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/temp");
        if (!file.exists()) {
            file.mkdirs();
        }
        String time = System.currentTimeMillis() + "";
        File filepic = new File(file.getPath(),time+ ".png");
        try {
            if (!filepic.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filepic);
            BufferedOutputStream out = new BufferedOutputStream(fos);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            out.flush();
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
