package com.headlth.management.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.headlth.management.R;
import com.headlth.management.activity.LookBigImageActivity;
import com.headlth.management.activity.OtherActivity;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.GetWindowSize;
import com.headlth.management.utils.ImageUtil;
import com.headlth.management.utils.UpadteApp;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by WangChang on 2016/4/10.
 */
public class ImageShowRecycleAdapter extends RecyclerView.Adapter<ImageShowRecycleAdapter.BaseViewHolder> {
    private ArrayList<String> dataList = new ArrayList<>();
    private Context context;
    private int WIDTH;
    private int HEIGHT;

    public ImageShowRecycleAdapter(Activity context) {
        this.context = context;//GetWindowSize.getInstance(context).getGetWindowwidth() / 3
        this.WIDTH = ImageUtil.dp2px(context,85);
        this.HEIGHT = ImageUtil.dp2px(context, 115);
    }

    public void replaceAll(ArrayList<String> list) {
        dataList.clear();
        if (list != null) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public ImageShowRecycleAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_published_circle_grida, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageShowRecycleAdapter.BaseViewHolder holder, int position) {

        holder.setData(dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }


    public class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        void setData(Object data, int position) {
        }


    }

    private class ImageViewHolder extends BaseViewHolder implements Target {
      //  private Button imageView;
        private ImageView imageView;

        public ImageViewHolder(View view) {
            super(view);
          //  imageView = (Button) view.findViewById(R.id.item_grida_circle_image);

           // imageView.setWidth(WIDTH);
           // imageView.setHeight(HEIGHT);

            imageView = (ImageView) view.findViewById(R.id.item_grida_circle_image);
         /*   imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentLookBIgImageActivity(position, imageView);
                }
            });*/
        }

        @Override
        void setData(Object data, final int position) {
            super.setData(data, position);
            if (data != null) {
                final String path = (String) data;
               Picasso.with(itemView.getContext()).load(Constant.BASE_URL + "/" + path).resize(WIDTH, HEIGHT).into(imageView);
             //   setGlide(path, imageView, position);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      IntentLookBIgImageActivity(position, imageView);
                    }
                });
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
             bitmap = new CropSquareTransformation().transform(bitmap);
            imageView.setImageBitmap(bitmap);
          //  imageView.setBackground(new BitmapDrawable(bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

    }

    public class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "square()";
        }
    }

    private void setGlide(final String path, final Button imageView, final int position) {
        String url = Constant.BASE_URL + "/" + path;
        imageView.setTag(url);
        Glide.with(context)
                .load(url)
                .asBitmap()
                .skipMemoryCache(true)
                .override(100, 100)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (bitmap != null) {
                            imageView.setBackground(new BitmapDrawable(bitmap));
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {//LookBigImageActivity
                                //  IntentLookBIgImageActivity(position, imageView);
/*
                                    Intent intent = new Intent(context, OtherActivity.class);
                                    intent.putExtra("flag", dataList.get(position));
                                    if (UpadteApp.More21()) {//超过5.0动画启动
                                        // 这里指定了共享的视图元素
                                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                                (Activity) context, new Pair<View, String>(imageView, "cicle")
                                        );


     *//*       ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((Activity) context, imageView, "cicle");*//*
                                        ActivityCompat.startActivity(context, intent, options.toBundle());
                                    } else {
                                        context.startActivity(intent);
                                    }*/
                                }
                            });
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        imageView.setBackgroundResource(R.mipmap.nodata);
                    }

                });


    }

    private void IntentLookBIgImageActivity(int position, ImageView imageView) {
        Intent intent = new Intent(context, LookBigImageActivity.class);
        intent.putExtra("cicle", dataList);
        intent.putExtra("arg", position);
        if (UpadteApp.More21()) {//超过5.0动画启动
            // 这里指定了共享的视图元素

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (Activity) context, new Pair<View, String>(imageView, "cicle")
            );

     /*       ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation((Activity) context, imageView, "cicle");*/
            ActivityCompat.startActivity(context, intent, options.toBundle());
        } else {
            context.startActivity(intent);
        }
    }
}
