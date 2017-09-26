package com.headlth.management.activity;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.headlth.management.R;
import com.headlth.management.ShareImageUtils.ImageModel;
import com.headlth.management.ShareImageUtils.ImageWork;
import com.headlth.management.ShareImageUtils.Utils;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.utils.Bimp;


import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * Created by abc on 2016/9/5.
 */
@ContentView(R.layout.activity_shareimagehandleutils)
public class ShareImageHandleActivity extends BaseActivity {



    @ViewInject(R.id.activity_shareimagehandleutils_back)
    private RelativeLayout activity_shareimagehandleutils_back;
    @ViewInject(R.id.activity_shareimagehandleutils_ok)
    private Button activity_shareimagehandleutils_ok;


    @ViewInject(R.id.activity_shareimagehandleutils_gv)
    private GridView activity_shareimagehandleutils_gv;

    @ViewInject(R.id.activity_shareimagehandleutils_imageview)
    private ImageView activity_shareimagehandleutils_imageview;
    @ViewInject(R.id.activity_shareimagehandleutils_layout)
    private LinearLayout activity_shareimagehandleutils_layout;

   // private List<ImageModel> mImageList;//相册图片
    private LayoutInflater mLayoutInflater;
    private Adapter mAdapter;
    private ImageWork mImageWork;//图片加载类
    private int oldsize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    void init() {
        x.view().inject(this);
        oldsize = Bimp.getInstance().url.size();
        mImageWork = new ImageWork(this);
        mLayoutInflater = LayoutInflater.from(this);
       // mImageList = ShareActivity.mImageList;
        mAdapter = new Adapter();
        activity_shareimagehandleutils_gv.setAdapter(mAdapter);
        activity_shareimagehandleutils_gv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Before Honeycomb pause image loading on scroll to help with performance
                    if (!Utils.hasHoneycomb()) {
                        mImageWork.setPauseWork(true);
                    }
                } else {
                    mImageWork.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
        activity_shareimagehandleutils_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Bimp.getInstance().url.size() != oldsize) {
                    for (int i = oldsize; i < Bimp.getInstance().url.size(); i++) {
                        if (i < 9) {
                            Bimp.getInstance().url.remove(i);
                        }
                    }


                }
                finish();
            }
        });
        activity_shareimagehandleutils_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Bimp.getInstance().url.size() >= 0) {
                    Bimp.getInstance().bmp.clear();//清空原有的集合
                    ImageWork imageWork = new ImageWork();
                    for (int i = 0; i < Bimp.getInstance().url.size(); i++) {
                        Log.i("pppppppath", Bimp.getInstance().url.get(i));
                        if (i < 9) {
                            Bimp.getInstance().bmp.add(imageWork.decodeBitmapFromDisk(Bimp.getInstance().url.get(i), dip2px(ShareImageHandleActivity.this, 100), dip2px(ShareImageHandleActivity.this, 100)));
                        }
                    }
                   // ShareActivity.activity.finish();
                   // startActivity(new Intent(ShareImageHandleActivity.this, ShareActivity.class).putExtra("share", "nofirst").putExtra("text", getIntent().getStringExtra("text")));
                    finish();
                } else {
                    Toast.makeText(ShareImageHandleActivity.this, "没有选中图片", Toast.LENGTH_LONG).show();
                }

            }
        });
        activity_shareimagehandleutils_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_shareimagehandleutils_layout.setVisibility(View.GONE);
            }
        });
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ShareActivity.mImageList.size();
        }

        @Override
        public ImageModel getItem(int i) {
            return ShareActivity.mImageList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final ViewHolder viewHolder;
            final ImageModel imageModel = getItem(i);
            final String path = imageModel.getPath();
            final boolean checked = imageModel.getIsChecked();
            if (view == null) {
                viewHolder = new ViewHolder();
                view = mLayoutInflater.inflate(R.layout.shareimagehandleutils_imageview, null);
                viewHolder.imageView = (ImageView) view.findViewById(R.id.iv_imageView);
                viewHolder.checkBox = (CheckBox) view.findViewById(R.id.cb_imageview);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            if(checked){
                viewHolder.checkBox.setChecked(true);
             //   viewHolder.checkBox.setEnabled (false);
            }
            /*viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                   // imageModel.setIsChecked(arg1);
                }
            });*/
            viewHolder.checkBox.setChecked(checked);
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity_shareimagehandleutils_ok.setEnabled(true);
                    if(viewHolder.checkBox.isChecked()){
                        if (Bimp.getInstance().url.size() < 9) {
                            if(!Bimp.getInstance().url.contains(path)) {
                                Bimp.getInstance().url.add(path);
                                getItem(i).setIsChecked(true);
                                activity_shareimagehandleutils_ok.setText("(" + Bimp.getInstance().url.size() + ")完成");
                                activity_shareimagehandleutils_ok.setTextColor(Color.BLACK);
                                //  activity_shareimagehandleutils_ok.setEnabled(true);
                            }
                        } else {
                            viewHolder.checkBox.setChecked(false);
                            Toast.makeText(ShareImageHandleActivity.this, "最多可分享9张图片", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (Bimp.getInstance().url.contains(path)) {
                            Bimp.getInstance().url.remove(path);
                            getItem(i).setIsChecked(false);
                            activity_shareimagehandleutils_ok.setTextColor(Color.BLACK);
                            activity_shareimagehandleutils_ok.setText("("+Bimp.getInstance().url.size()+")完成");
/*
                            if(Bimp.getInstance().url.size()==oldsize){
                                activity_shareimagehandleutils_ok.setEnabled(false);
                                activity_shareimagehandleutils_ok.setTextColor(Color.GRAY);
                                activity_shareimagehandleutils_ok.setText("完成");
                            }else {
                                activity_shareimagehandleutils_ok.setTextColor(Color.BLACK);
                                activity_shareimagehandleutils_ok.setText("("+Bimp.getInstance().url.size()+")完成");

                            }*/
                        }
                    }
                }
            });


            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity_shareimagehandleutils_layout.setVisibility(View.VISIBLE);
                  //  Picasso.with(ShareImageHandleActivity.this).load(new File(path)).into(activity_shareimagehandleutils_imageview);
                  //  new Thread()
                    Glide.with(ShareImageHandleActivity.this).load(Uri.fromFile(new File(path))).into(activity_shareimagehandleutils_imageview);
                }
            });


            mImageWork.loadImage(path, viewHolder.imageView);
            return view;
        }

        class ViewHolder {
            ImageView imageView;
            CheckBox checkBox;
        }
    }
}
