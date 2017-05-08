package com.headlth.management.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.headlth.management.R;
import com.headlth.management.entity.Circle;
import com.headlth.management.entity.CircleList;
import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.myview.MGridView;
import com.headlth.management.myview.RoundImageView;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DataString;
import com.headlth.management.utils.GetWindowSize;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ScreenShot;
import com.headlth.management.utils.ShareUitls;
import com.squareup.picasso.Picasso;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by abc on 2017/4/28.
 */
public class CircleRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }


    private List<Circle> list;
    private Activity activity;
    private Display d; // 获取屏幕宽、高用
    private View view;//
    private PopupWindow popupWindow;
    private BottomMenuDialog bottomMenuDialog;
    private boolean requestnetworking;
    private Handler myhandler;

    public CircleRecyclerViewAdapter(List<Circle> list, Activity activity, Handler handler) {
        this.list = list;
        this.activity = activity;
        d = activity.getWindowManager().getDefaultDisplay();
        myhandler=handler;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_maidongcircle, parent, false);


        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Circle circle = list.get(position);
        //判断是否设置了监听器

        //为ItemView设置监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("mOnItemClickListener", "mOnItemClickListener");
                Message message=Message.obtain();
                message.arg1=position;
                message.arg2=0;
                myhandler.sendMessage(message);
            /*    int position = holder.getLayoutPosition(); // 1
                mOnItemClickListener.onItemClick(holder.itemView, position); // 2*/
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("mOnItemClickListener", "mOnItemLongClickListener");
                int position = holder.getLayoutPosition();
                Message message=Message.obtain();
                message.arg1=position;
                message.arg2=1;
                myhandler.sendMessage(message);
              /*  mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                //返回true 表示消耗了事件 事件不会继续传递*/
                return true;
            }
        });


        Picasso.with(activity)
                .load(Constant.BASE_URL + "/" + circle.getAvatarUrl())//图片网址
                .placeholder(R.mipmap.hand)//默认图标
                .into(holder.listview_maidongcircle_icon);//控件

        if (circle.getUsername().length() >= 25) {
            holder.listview_maidongcircle_user.setText(circle.getUsername().substring(0, 25) + "...");
        } else {
            holder.listview_maidongcircle_user.setText(circle.getUsername());

        }


        if (circle.getContentText().length() >= 150) {
            holder.listview_maidongcircle_text_all.setVisibility(View.VISIBLE);
            holder.listview_maidongcircle_text.setText(circle.getContentText().substring(0, 150));
        } else {
            holder.listview_maidongcircle_text.setText(circle.getContentText());
            holder.listview_maidongcircle_text_all.setVisibility(View.GONE);
        }

        holder.listview_maidongcircle_time.setText(DataString.showTime(circle.getCreateTime()));
        if (circle.getCommentCount() == 0) {
            holder.listview_maidongcircle_comment_count.setText("评论");
            holder.listview_maidongcircle_reply_ImageView.setImageResource(R.mipmap.icon_no_reply);

        } else {
            holder.listview_maidongcircle_comment_count.setText(circle.getCommentCount() + "");
            holder.listview_maidongcircle_reply_ImageView.setImageResource(R.mipmap.icon_reply);
        }
        if (circle.getLikeCount() == 0) {
            holder.listview_maidongcircle_like_count.setText("赞");
        } else {
            holder.listview_maidongcircle_like_count.setText(circle.getLikeCount() + "");
        }

        if (circle.getIsAttitude().equals("1")) {
            holder.listview_maidongcircle_like_ImageView.setImageResource(R.mipmap.icon_zan);

        } else {
            holder.listview_maidongcircle_like_ImageView.setImageResource(R.mipmap.icon_no_zan);
        }

        int count = 0;
        holder.listview_maidongcircle_Scrollgridview.setHorizontalSpacing(dip2px(activity, 3));
        if (circle.getImageUrls() != null && circle.getImageUrls().size() != 0) {
            holder.listview_maidongcircle_Scrollgridview.setVisibility(View.VISIBLE);
            Log.i("ooooooo", list.get(position).getImageUrls().size() + "------" + circle.getContentText());
           /* if (list.get(position).getImageUrls().size() == 4) {
                holder.listview_maidongcircle_Scrollgridview.setNumColumns(2);
            } else {
                holder.listview_maidongcircle_Scrollgridview.setNumColumns(3);
            }
*/
            GridAdapter adapter = new GridAdapter(position, count);

            holder.listview_maidongcircle_Scrollgridview.setOnTouchInvalidPositionListener(new MGridView.OnTouchInvalidPositionListener() {
                @Override
                public boolean onTouchInvalidPosition(int motionEvent) {
                    return false;
                }
            });
            holder.listview_maidongcircle_Scrollgridview.setAdapter(adapter);
        } else {
            holder.listview_maidongcircle_Scrollgridview.setVisibility(View.GONE);
        }
        holder.listview_maidongcircle_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FFFFF", circle.getIsAttitude().equals("0") + "");
                if (circle.getIsAttitude().equals("0")) {

                    if (!requestnetworking) {
                        addLike(position, "0");
                    }


                } else {
                    if (!requestnetworking) {
                        addLike(position, "1");
                    }


                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        int position;
        int count;

        public GridAdapter(int position, int count) {
            inflater = LayoutInflater.from(activity);
            this.position = position;
            this.count = count;
            // Log.i("ooooooo", list.get(position).getImageUrls().size() + "");
        }


        public int getCount() {
            int temp = 0;
            try {
                temp = list.get(position).getImageUrls().size();
            } catch (Exception n) {
            }
            return temp;
        }

        public String getItem(int arg0) {

            return list.get(position).getImageUrls().get(arg0);
        }

        public long getItemId(int arg0) {

            return arg0;
        }

        public View getView(final int arg, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_circle_grida, parent, false);
                holder = new ViewHolder();
                //holder.image = (NetworkImageView ) convertView.findViewById(R.id.item_grida_circle_image);
                holder.image = (Button) convertView.findViewById(R.id.item_grida_circle_image);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.image.setWidth(GetWindowSize.getInstance(activity).getGetWindowwidth() / 3);
            //holder.image.setHeight(200);
            final String tag = (String) holder.image.getTag();
            if (!(Constant.BASE_URL + "/" + getItem(arg)).equals(tag)) {
                holder.image.setBackgroundResource(R.mipmap.nodata);
            }

            setGlide(arg, holder);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPicChangeBig(position, arg);
                }
            });

            /*holder.image.setDefaultImageResId(R.mipmap.hand);
            holder.image.setErrorImageResId(R.mipmap.hand);
            holder.image.setImageUrl(getItem(arg), imageLoader);*/

            return convertView;
        }

        private void setGlide(final int arg, final ViewHolder holder) {
            String url = Constant.BASE_URL + "/" + getItem(arg);
            holder.image.setTag(url);
            Glide.with(activity)
                    .load(url)
                    .asBitmap()
                    .skipMemoryCache(true)
                    .override(100, 100)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (bitmap != null) {
                                holder.image.setBackground(new BitmapDrawable(bitmap));
                                holder.image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        clickPicChangeBig(position, arg);
                                    }
                                });
                            } else {
                                holder.image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setGlide(arg, holder);
                                    }
                                });
                            }

                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            holder.image.setBackgroundResource(R.mipmap.nodata);
                            holder.image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    setGlide(arg, holder);
                                }
                            });

                        }

                    });


        }

        public class ViewHolder {
            //  public NetworkImageView  image;
            public Button image;
        }

    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    int prePosition = 0;

    private void clickPicChangeBig(final int Position, final int arg0) {

        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        view = LayoutInflater.from(activity).inflate(R.layout.popwindow_clickchangebig, null);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.clickpicchangebig);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.clickpicchangebig_layout);
        final List<ImageView> sublist = new ArrayList<ImageView>();
        linearLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);

        for (int i = 0; i < list.get(Position).getImageUrls().size(); i++) {
            //final PhotoView imageView = new PhotoView(activity);
            // loadBigImage(Position, i, imageView);

            ImageView icon = new ImageView(activity);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                lp.setMargins(15, 0, 0, 0);
            }
            icon.setLayoutParams(lp);
            icon.setBackgroundResource(R.drawable.black_icon_shape);

            linearLayout.addView(icon);

            // sublist.add(imageView);
        }
        // linearLayout.setPadding(10,0,10,10);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.get(Position).getImageUrls().size();
            }

            @Override
            public ImageView instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
                PhotoView imageView = new PhotoView(activity);
                final ProgressBar loading = new ProgressBar(activity);
                FrameLayout.LayoutParams loadingLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                loadingLayoutParams.gravity = Gravity.CENTER;
                loading.setLayoutParams(loadingLayoutParams);
                ((FrameLayout) view).addView(loading);

                final String imgurl = Constant.BASE_URL + "/" + list.get(Position).getImageUrls().get(position);
                loadBigImage(imageView, loading, imgurl);
                imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float v, float v1) {
                        notifyDataSetChanged();
                        popupWindow.dismiss();
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
                Glide.with(activity)
                        .load(imgurl)
                        .skipMemoryCache(true)
                        .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                        .error(R.drawable.nodate)
                        .into(new GlideDrawableImageViewTarget(imageView) {
                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                               /* if(smallImageView!=null){
                                    smallImageView.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(imgurl).into(smallImageView);
                                }*/
                                loading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                /*if(smallImageView!=null){
                                    smallImageView.setVisibility(View.GONE);
                                }*/
                                loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                loading.setVisibility(View.GONE);
                                /*if(smallImageView!=null){
                                    smallImageView.setVisibility(View.GONE);
                                }*/
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
        });
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
        popupWindow = new PopupWindow(view, d.getWidth(), d.getHeight(), true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.showAtLocation(new View(activity), Gravity.CENTER, 0, 0);
    }


    private void addLike(final int position, final String Flag) {
        requestnetworking = true;
        //UserID:用户ID，ContentID:帖子ID，CommentID:评论ID(如果对帖子点赞则该项为0）,ReplyID(默认为0），Flag：0为点赞，1为取消赞
        Circle circle = CircleList.getInstance().circlelist.get(position);

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostShareAttitudeRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(activity, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(activity, "UID", "0"));
        params.addBodyParameter("ContentID", circle.getContentID());
        params.addBodyParameter("CommentID", "0");
        params.addBodyParameter("ReplyID", "0");
        params.addBodyParameter("Flag", Flag);
        HttpUtils.getInstance(activity).sendRequestRequestParams("", params, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        requestnetworking = false;
                        Log.i("onResponse", Flag + "   " + CircleList.getInstance().circlelist.get(position).getUsername() + "  " + position);
                        Circle circle = CircleList.getInstance().circlelist.get(position);
                        int likecount = circle.getAttitudeCount();
                        if (Flag.equals("0")) {
                            CircleList.getInstance().circlelist.get(position).setIsAttitude("1");
                            CircleList.getInstance().circlelist.get(position).setAttitudeCount(likecount + 1);
                        } else {
                            CircleList.getInstance().circlelist.get(position).setIsAttitude("0");
                            if (likecount - 1 > 0) {
                                CircleList.getInstance().circlelist.get(position).setAttitudeCount(likecount - 1);
                            } else {
                                CircleList.getInstance().circlelist.get(position).setAttitudeCount(0);
                            }

                        }
                        notifyItemChanged(position, "notifyItemChanged");
                        // handler.sendEmptyMessage(0);

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        requestnetworking = false;
                        Toast.makeText(activity, "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            notifyDataSetChanged();

        }
    };


}

class MyViewHolder extends RecyclerView.ViewHolder {

    @ViewInject(R.id.listview_maidongcircle_icon)
    public RoundImageView listview_maidongcircle_icon;
    @ViewInject(R.id.listview_maidongcircle_user)
    public TextView listview_maidongcircle_user;
    @ViewInject(R.id.listview_maidongcircle_text)
    public TextView listview_maidongcircle_text;
    @ViewInject(R.id.listview_maidongcircle_text_all)
    public TextView listview_maidongcircle_text_all;
//GridLayoutManager
    @ViewInject(R.id.listview_maidongcircle_Scrollgridview)
    public MGridView listview_maidongcircle_Scrollgridview;


    @ViewInject(R.id.listview_maidongcircle_like_ImageView)
    public ImageView listview_maidongcircle_like_ImageView;
    @ViewInject(R.id.listview_maidongcircle_reply_ImageView)
    public ImageView listview_maidongcircle_reply_ImageView;


    @ViewInject(R.id.listview_maidongcircle_time)
    public TextView listview_maidongcircle_time;

    @ViewInject(R.id.listview_maidongcircle_comment_count)
    public TextView listview_maidongcircle_comment_count;
    @ViewInject(R.id.listview_maidongcircle_like)
    public RelativeLayout listview_maidongcircle_like;
    @ViewInject(R.id.listview_maidongcircle_like_count)
    public TextView listview_maidongcircle_like_count;

    public MyViewHolder(View itemView) {
        super(itemView);
        x.view().inject(this, itemView);
    }


}

