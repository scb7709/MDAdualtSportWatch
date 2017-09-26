package com.headlth.management.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.headlth.management.R;
import com.headlth.management.activity.ShareActivity;

import com.headlth.management.activity.ShareNewActivity;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;

import com.tencent.mm.sdk.openapi.WXAPIFactory;

import com.tencent.open.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.List;
import java.util.Locale;

/**
 * Created by abc on 2016/8/12.
 */
public class Share {


    //QQ分享
    private Tencent mTencent;
    private static String QQ_ID = "1105190496";
    private static String CHAT_ID = "wx7d8b93a61963d44c";


    private static PopupWindow share_popupWindoww;
    private static View share_popupWindoww_view;//分享

    private static Share share;
    private static Activity Activity;
    private static String ImageUrl;
    private static com.headlth.management.clenderutil.WaitDialog waitDialog;

    private Share() {
    }

    public static Share getInstance() {
        if (share == null) {
            synchronized (Share.class) {
                if (share == null) {
                    share = new Share();
                }

            }
        }
        return share;
    }

    public void QQshare(boolean qqflag) {
        Log.i("sssssssssssQQQQQQ",ImageUrl);
        //qqflag  选择分享到QQ好友还是QQ空间： false 好友  true 空间
        // 第一个参数就是上面所说的申请的APPID，第二个是全局的Context上下文，这句话实现了调用QQ登录
        mTencent = Tencent.createInstance(QQ_ID, Activity);
        final Bundle params = new Bundle();
      /*  params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "加入迈动,开启您的健康之旅");
       // params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://www.qq.com/news/1.html");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/" + pictime + ".png");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "测试应用1105190496");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);*/
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, ImageUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用1105190496");
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);

        if (qqflag) {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        } else {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        }
        mTencent.shareToQQ(Activity, params, new BaseUiListener());
        //  waitDialog.ShowDialog(false);
    }


    public class BaseUiListener implements IUiListener {

        public void onCancel() {
            // 取消
            //   waitDialog.ShowDialog(false);

        }

        public void onComplete(Object response) {
     /*   *//**//*    *//**//**//**//**//**//**//**//**
             * 到此已经获得OpneID以及其他你想获得的内容了
             * QQ登录成功了，我们还想获取一些QQ的基本信息，比如昵称，头像什么的，这个时候怎么办？
             * sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
             * 如何得到这个UserInfo类呢？
             *//**//**//**//**//**//**//**//**//**//**/
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(Activity, qqToken);
            // 这样我们就拿到这个类了，之后的操作就跟上面的一样了，同样是解析JSON
            info.getUserInfo(new IUiListener() {

                public void onComplete(final Object response) {
                    waitDialog.ShowDialog(false);
                    Toast.makeText(Activity, "分享成功", Toast.LENGTH_LONG).show();
                }

                public void onCancel() {
                    waitDialog.ShowDialog(false);
                    // TODO Auto-generated method stub
                }

                public void onError(UiError arg0) {
                    // TODO Auto-generated method stub
                    waitDialog.ShowDialog(false);
                }

            });

        }

        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            waitDialog.ShowDialog(false);
        }

    }

    public void chatshare(boolean flag) {
        Log.i("sssssssssssWWWW",ImageUrl);
        IWXAPI api = WXAPIFactory.createWXAPI(Activity, CHAT_ID);
        api.registerApp(CHAT_ID);
        if (!api.isWXAppInstalled()) {
            Toast.makeText(Activity, "无微信", Toast.LENGTH_SHORT).show();
            return;
        }
        // Bitmap bmp = BitmapFactory.decodeResource(Activity.getResources(), R.mipmap.weixin);
        Bitmap bmp = BitmapFactory.decodeFile(ImageUrl);
        //  Bitmap bmp=DiskBitmap.getDiskBitmap(ImageUrl,Activity);
        // bmp=Bitmap.createScaledBitmap(bmp,80,80,true);
        WXImageObject wxImageObject = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxImageObject;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 80, 80, true);
        //bmp.recycle();
        msg.thumbData = Util.getBytesUTF8(ImageUrl);
      //  msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("maidong");
        req.message = msg;
        req.scene = flag ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        ShareUitls.putLoginString(Activity,"chatflag","share");
        // api.registerApp(CHAT_ID);
        api.sendReq(req);
        waitDialog.ShowDialog(false);
        Log.i("wwwwwwwwwdd", flag + "");
        }



    public void showPopFormBottom(final Activity activity, final Handler webHandler) {

        waitDialog = new com.headlth.management.clenderutil.WaitDialog(activity, "正在分享请稍后...");
        Activity = activity;
        // ImageUrl=imageUrl;
        share_popupWindoww_view = LayoutInflater.from(activity).inflate(R.layout.dialog_sport_share, null);
        share_popupWindoww = new PopupWindow(share_popupWindoww_view, dip2px(activity, 300), dip2px(activity, 300), true);

        ImageView maidong = (ImageView) share_popupWindoww_view.findViewById(R.id.dialog_sport_share_maidongquan);
        ImageView weiixn = (ImageView) share_popupWindoww_view.findViewById(R.id.dialog_sport_share_weiixn);
        ImageView QQ = (ImageView) share_popupWindoww_view.findViewById(R.id.dialog_sport_share_qq);
        ImageView QQZone = (ImageView) share_popupWindoww_view.findViewById(R.id.dialog_sport_share_qq_zone);
        ImageView weiixn_circle = (ImageView) share_popupWindoww_view.findViewById(R.id.dialog_sport_share_weixin_circle);
        ImageView sina = (ImageView) share_popupWindoww_view.findViewById(R.id.dialog_sport_share_sina);
        TextView sinaText=(TextView) share_popupWindoww_view.findViewById(R.id.dialog_sport_share_sina_text);
        if(!isWeiboInstalled(Activity)){
            sina.setVisibility(View.GONE);
            sinaText.setVisibility(View.GONE);
        }
        maidong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_popupWindoww.dismiss();
                String pictime = System.currentTimeMillis() + "";
                Bitmap bitmap = ScreenShot.takeScreenShot(activity, pictime);

                if (bitmap != null) {
                    ImageUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/image/" + pictime + ".png";
                    Intent intent = new Intent(activity, ShareNewActivity.class);
                    intent.putExtra("pictime", ImageUrl);
                    intent.putExtra("share", "first");
                    activity.startActivity(intent);
                }


            }

        });
        weiixn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pictime = System.currentTimeMillis() + "";
                Bitmap bitmap = ScreenShot.takeScreenShot(activity, pictime);

                if (bitmap != null) {
                    ImageUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/image/" + pictime + ".png";
                    chatshare(false);
                    // new File(ImageUrl).delete();
                }
                share_popupWindoww.dismiss();

            }
        });
        weiixn_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pictime = System.currentTimeMillis() + "";
                Bitmap bitmap = ScreenShot.takeScreenShot(activity, pictime);
                if (bitmap != null) {
                    ImageUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/image/" + pictime + ".png";
                    chatshare(true);
                    //  new File(ImageUrl).delete();
                }
                share_popupWindoww.dismiss();

            }
        });
        QQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //waitDialog.ShowDialog(true);
                String pictime = System.currentTimeMillis() + "";
                Bitmap bitmap = ScreenShot.takeScreenShot(activity, pictime);
                if (bitmap != null) {
                    ImageUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/image/" + pictime + ".png";
                    QQshare(false);
                    //  new File(ImageUrl).delete();
                }
                share_popupWindoww.dismiss();

            }

        });
        QQZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  waitDialog.ShowDialog(true);
                String pictime = System.currentTimeMillis() + "";
                Bitmap bitmap = ScreenShot.takeScreenShot(activity, pictime);
                if (bitmap != null) {
                    ImageUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/image/" + pictime + ".png";
                    QQshare(true);
                    //  new File(ImageUrl).delete();
                }
                share_popupWindoww.dismiss();

            }

        });
        sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //waitDialog.ShowDialog(true);
                String pictime = System.currentTimeMillis() + "";
                Bitmap bitmap = ScreenShot.takeScreenShot(activity, pictime);
                if (bitmap != null) {
                    IWeiboShareAPI mWeiboShareAPI= WeiboShareSDK.createWeiboAPI(activity, Constant.SINA_KEY);
                    mWeiboShareAPI.registerApp();
                    ImageUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/image/" + pictime + ".png";
                    Log.i("sssssssssssbbbb",ImageUrl);
                    Message message=new Message();
                    message.obj=ImageUrl;
                    webHandler.sendMessage(message);
                }

                share_popupWindoww.dismiss();

            }

        });


        share_popupWindoww.setOutsideTouchable(true);
        share_popupWindoww.setFocusable(true);
        //share_popupWindoww.setAnimationStyle(R.style.take_photo_anim);
        share_popupWindoww.setBackgroundDrawable(new ColorDrawable(0x00000000));
        share_popupWindoww.showAtLocation(new View(activity), Gravity.CENTER, 0, 0);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    public static boolean isWeiboInstalled(@NonNull Context context) {
        PackageManager pm;
        if ((pm = context.getApplicationContext().getPackageManager()) == null) {
            return false;
        }
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo info : packages) {
            String name = info.packageName.toLowerCase(Locale.ENGLISH);
            if ("com.sina.weibo".equals(name)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isQQInstalled(@NonNull Context context) {
        PackageManager pm;
        if ((pm = context.getApplicationContext().getPackageManager()) == null) {
            return false;
        }
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo info : packages) {
            String name = info.packageName.toLowerCase(Locale.ENGLISH);
            if ("com.tencent.mobileqq".equals(name)) {
                return true;
            }
        }
        return false;
    }
}
