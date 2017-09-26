package com.headlth.management.activity;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import com.headlth.management.R;

import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.ScreenShot;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.UpadteApp;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by abc on 2016/9/23.
 */
@ContentView(R.layout.activity_lookbigimage)
public class LookBigImageActivity extends Activity {


    @ViewInject(R.id.clickpicchangebig)
    private ViewPager viewPager;
    @ViewInject(R.id.clickpicchangebig_layout)
    private LinearLayout linearLayout;
    private ArrayList<String> list;
    // int Position;
    private int prePosition;
    private int arg0;
    private BottomMenuDialog bottomMenuDialog;
    private Activity activity;
    private View view;//
    private String imgurl;
    private PagerAdapter pagerAdapter;
    private boolean More21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        activity = LookBigImageActivity.this;
        view = LayoutInflater.from(activity).inflate(R.layout.popwindow_clickchangebig, null);
        initialize();
    }

    private void initialize() {


        list = new ArrayList<>();
        Intent intent = getIntent();

        List<String> listtemp = (List<String>) intent.getSerializableExtra("cicle");

        arg0 = intent.getIntExtra("arg", 0);
        if (listtemp != null && listtemp.size() != 0) {
            for (String url : listtemp) {
                if (!url.equals("000000")) {
                    list.add(url);
                    //  Log.i("pathArrrrl", url + "  " + url);
                }
            }

        }
        //   Log.i("pathArrrrl", arg0 + "  " + list.size() + "  " );
        linearLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
        for (int i = 0; i < list.size(); i++) {
            ImageView icon = new ImageView(LookBigImageActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                lp.setMargins(15, 0, 0, 0);
            }
            icon.setLayoutParams(lp);
            icon.setBackgroundResource(R.drawable.black_icon_shape);
            linearLayout.addView(icon);
        }
        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public ImageView instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
                PhotoView imageView = new PhotoView(activity);
                if (UpadteApp.More21()) {//超过5.0使用动画
                    // 这里指定了被共享的视图元素
                    ViewCompat.setTransitionName(imageView, "circle");
                }
                final ProgressBar loading = new ProgressBar(activity);
                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingLayoutParams.gravity = Gravity.CENTER;
                loading.setLayoutParams(loadingLayoutParams);
                ((FrameLayout) view).addView(loading);
                imgurl = Constant.BASE_URL + "/" + list.get(position);
                loadBigImage(imageView, loading, imgurl);
                imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float v, float v1) {
                        finish();
                        // overridePendingTransition(0, 0);
                    }
                });
                imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        bottomMenuDialog = new BottomMenuDialog.Builder(activity)
                                .addMenu("保存", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Glide.with(activity)
                                                .load(imgurl)
                                                .asBitmap()
                                                .skipMemoryCache(true)
                                                .into(new SimpleTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                                        if (bitmap != null) {
                                                            String pictime = System.currentTimeMillis() + "";
                                                            ScreenShot.saveMyBitmap(bitmap, pictime, true, activity);
                                                        } else {
                                                            Toast.makeText(activity, "保存失败", Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                        super.onLoadFailed(e, errorDrawable);
                                                        Toast.makeText(activity, "保存失败", Toast.LENGTH_LONG).show();
                                                    }

                                                });
                                        bottomMenuDialog.dismiss();
                                    }
                                }).create();
                        bottomMenuDialog.show();
                        return true;
                    }
                });
                container.addView(imageView, 0);//添加页卡
                return imageView;
            }

            private void loadBigImage(final PhotoView imageView, final ProgressBar loading, String imgurl) {
                final Uri uri = Uri.parse(imgurl);
                Glide.with(activity).load(uri).thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                        .error(R.drawable.nodate)
                        .into(new GlideDrawableImageViewTarget(imageView) {
                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                                loading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                loading.setVisibility(View.GONE);
                            }
                        });

            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((ImageView) object);//删除页卡
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view.equals(object);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(arg0);
        prePosition = arg0;
        linearLayout.getChildAt(prePosition).setBackgroundResource(R.drawable.yellow_icon_shape);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                linearLayout.getChildAt(position).setBackgroundResource(R.drawable.yellow_icon_shape);
                linearLayout.getChildAt(prePosition).setBackgroundResource(R.drawable.black_icon_shape);
                prePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

  /*  *//**
     * 取消Activity关闭动画
     *//*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            finish();
           // overridePendingTransition(0, 0);
        return super.onKeyDown(keyCode, event);
    }*/
}
