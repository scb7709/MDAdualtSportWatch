package com.headlth.management.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.headlth.management.R;
import com.headlth.management.ShareImageUtils.ImageModel;
import com.headlth.management.ShareImageUtils.Utils;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.entity.CircleList;
import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.myview.MGridView;
import com.headlth.management.utils.Bimp;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.DiskBitmap;

import com.headlth.management.utils.ScreenShot;

import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.VersonUtils;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import java.util.ArrayList;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by abc on 2016/8/1.
 */
@ContentView(R.layout.activity_share)
public class ShareActivity extends BaseActivity {

    @ViewInject(R.id.share_cancel)
    private Button share_cancel;
    @ViewInject(R.id.share_send)
    private Button share_send;

    @ViewInject(R.id.share_shareText)
    private EditText share_shareText;

    @ViewInject(R.id.share_noScrollgridview)
    private MGridView noScrollgridview;
    private GridAdapter adapter;
    /*   @ViewInject(R.id.share_select_pic)
       private ImageButton share_select_pic;
       @ViewInject(R.id.share_select_pho)
       private ImageButton share_select_pho;
   */
    View view;//点击图放大
    PopupWindow popupWindow;
    PopupWindow addpopupWindownew;
    public static Activity activity;
    int preItem = 0;
    int leftORright = 0;
    public static List<ImageModel> mImageList;
    private static com.headlth.management.clenderutil.WaitDialog waitDialog;
    private Intent intent;
    private BottomMenuDialog bottomMenuDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initialize();
    }

    private void initialize() {
        activity = this;
        mImageList = Utils.getImages(this);
        intent = getIntent();
        if (intent.getStringExtra("share").equals("first")) {
            Bimp.getInstance().url.clear();
            Bimp.getInstance().bmp.clear();
            if (!getIntent().getStringExtra("pictime").equals("")) {
                Bimp.getInstance().bmp.add(DiskBitmap.getDiskBitmap(getIntent().getStringExtra("pictime"), ShareActivity.this));
                Bimp.getInstance().url.add(getIntent().getStringExtra("pictime"));
            }
        } else if (intent.getStringExtra("share").equals("nofirst")) {
            if (intent.getStringExtra("text").toString().length() != 0) {
                share_shareText.setText(intent.getStringExtra("text"));
            }
        }
        Init();
    }

    private static void initDialog() {
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(activity);
        waitDialog.setCancleable(true);
    }

    public void Init() {
        initDialog();
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        noScrollgridview.setHorizontalSpacing(dip2px(ShareActivity.this, 3));

        adapter = new GridAdapter(this);
        noScrollgridview.setAdapter(adapter);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
               /* if (arg2 == Bimp.bmp.size()) {
                    new PopupWindows(ShareActivity.this, noScrollgridview);
                } else {
                   // clickPicChangeBig(arg2);
                 *//*   Intent intent = new Intent(PublishedActivity.this, PhotoActivity.class);
                    intent.putExtra("ID", arg2);
                    startActivity(intent);*//*
                }*/
            }
        });

    }

    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        private int selectedPosition = -1;// 选中的位置
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        /*public void update() {
            loading();
        }*/

        public int getCount() {
            return (Bimp.getInstance().bmp.size() + 1);
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            final int coord = position;
            final ViewHolder holder;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
                holder = new ViewHolder();
                holder.image = (Button) convertView.findViewById(R.id.item_grida_image);


                 /*
                ViewGroup.LayoutParams lp = holder.image.getLayoutParams();
                lp.width = screenWidth;
                lp.height = dip2px(ShareActivity.this, 120);
                holder.image.setLayoutParams(lp);
                holder.image.setMaxWidth(screenWidth / 3);
                holder.image.setMaxHeight(dip2px(ShareActivity.this, 120));*/

                holder.delete = (ImageView) convertView.findViewById(R.id.item_grida_del);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.image.setWidth(1000);

            if (position == Bimp.getInstance().bmp.size()) {
                holder.image.setBackgroundResource(R.mipmap.pic_add);
                holder.delete.setVisibility(View.GONE);
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Bimp.getInstance().bmp.size() < 9) {
                            openPhotoPop();
                        }
                    }
                });
                if (position == 9) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                // holder.delete.setVisibility(View.VISIBLE);
                holder.image.setBackground(new BitmapDrawable(Bimp.getInstance().bmp.get(position)));

                //  int[] end_location = new int[2];
                //  holder.image.getLocationInWindow(end_location);
                //  Log.i("WWWWWWWWend",end_location[0]+"  "+end_location[1]);
                // Bimp.getInstance().animotionCoordinate.add(new AnimotionCoordinate(end_location[0],end_location[1]));


                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AnimationSet animationSet = new AnimationSet(true);
                        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 2, 1, 2, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        scaleAnimation.setDuration(300);
                        animationSet.addAnimation(scaleAnimation);
                        animationSet.setFillAfter(true);
                        holder.image.startAnimation(animationSet);


                        clickPicChangeBig(position);
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (ImageModel imageModel : mImageList) {
                            if (Bimp.getInstance().url.get(position).equals(imageModel.getPath())) {
                                imageModel.setIsChecked(false);
                            }
                        }
                        Bimp.getInstance().bmp.remove(position);
                        Bimp.getInstance().url.remove(position);

                        adapter.notifyDataSetChanged();
                        noScrollgridview.setAdapter(adapter);


                    }
                });
            }


            return convertView;
        }

        public class ViewHolder {
            public Button image;
            public ImageView delete;
        }

    }

    private void openPhotoPop() {
        bottomMenuDialog = new BottomMenuDialog.Builder(ShareActivity.this)
                .addMenu("拍照", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        photo();
                        bottomMenuDialog.dismiss();
                    }
                }).addMenu("相册", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        takePic();
                        bottomMenuDialog.dismiss();

                    }
                }).create();

        bottomMenuDialog.show();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Event(value = {R.id.share_cancel, R.id.share_send})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.share_cancel:
                cancelShare();
                break;

            case R.id.share_send:

                share();
                break;
        }
    }

    private void share() {
        if (share_shareText.getText().toString().length() == 0 && Bimp.getInstance().bmp.size() == 0) {
            Toast.makeText(ShareActivity.this, "还未添加任何内容", Toast.LENGTH_LONG).show();
        } else {
            waitDialog.setMessage("正在分享,请稍后...");
            waitDialog.showDailog();
            RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostShareContentRequest");
            if (share_shareText.getText().toString().length() == 0) {
                params.addBodyParameter("ShareContent", " ");
            } else {
                params.addBodyParameter("ShareContent", share_shareText.getText().toString());
            }
            params.addBodyParameter("UID", ShareUitls.getString(getApplicationContext(), "UID", "null"));
            params.addBodyParameter("ResultJWT", ShareUitls.getString(this, "ResultJWT", "0"));
            params.addBodyParameter("VersionNum", VersonUtils.getVersionName(this));

            int i = 0;
            for (String file : Bimp.getInstance().url) {
                params.addBodyParameter("file" + (i++), new File(file), "image/png");
                Log.i("QQQQQQQQQ", "" + file);
            }

            //设置断点续传
            params.setMultipart(true);
            Callback.Cancelable cancelable = x.http().post(params,
                    new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {


                            Log.i("QQQQQQQQQonSuccess", "" + result.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(result.toString());


                                if (jsonObject.getString("ErrCode").equals("601") || jsonObject.getString("ErrCode").equals("600") || jsonObject.getString("ErrCode").equals("602")) {
                                    if(jsonObject.getString("ErrCode").equals("601")){
                                        Toast.makeText(ShareActivity.this, "您的账号已在其他设备登录", Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(ShareActivity.this, "您的登录信息已过期", Toast.LENGTH_LONG).show();
                                    }
                                    Intent i = new Intent(ShareActivity.this, Login.class);
                                    ShareActivity.this.startActivity(i);
                                    ShareUitls.cleanSharedPreference(ShareActivity.this);
                                    CircleList.getInstance().circlelist.clear();
                                    CircleList.getInstance().commentlist.clear();
                                    CircleList.getInstance().replylist.clear();
                                    finish();
                                    return;
                                } else {

                                    if (jsonObject.getString("IsSuccess").equals("true")) {
                                        setResult(258);
                                        finish();
                                        Toast.makeText(ShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Toast.makeText(ShareActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            waitDialog.dismissDialog();
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            //Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                            if (ex instanceof HttpException) { // 网络错误
                                Toast.makeText(ShareActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                            } else { // 其他错误
                                Toast.makeText(ShareActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                            }
                            waitDialog.dismissDialog();
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {
                            Log.i("QQQQQQQQQoonCancelled", "");
                            waitDialog.dismissDialog();
                        }

                        @Override
                        public void onFinished() {
                            Log.i("QQQQQQQQonFinished", "");
                            waitDialog.dismissDialog();
                        }
                    });
        }
    }


    private void cancelShare() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ShareActivity.this);
        dialog.setMessage("是否取消分享？")//设置显示的内容
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        Bimp.getInstance().bmp.clear();
                        Bimp.getInstance().url.clear();
                        dialog.dismiss();
                        finish();
                        if (!getIntent().getStringExtra("pictime").equals("")) {
                            new File(getIntent().getStringExtra("pictime")).delete();
                        }

                    }

                }).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            } else {
                cancelShare();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    int prePosition = 0;
    int animotionCoordinate = 0;
    boolean flag = true;

    private void clickPicChangeBig(final int position) {
        flag = true;
        WindowManager m = getWindowManager();
        final Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        view = LayoutInflater.from(ShareActivity.this).inflate(R.layout.popwindow_clickchangebig, null);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.clickpicchangebig);
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.clickpicchangebig_re);
        final Button back = (Button) view.findViewById(R.id.clickpicchangebig_back);
        final ImageView del = (ImageView) view.findViewById(R.id.clickpicchangebig_del);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

      /*  viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
            }
        });*/
        final List<ImageView> list = new ArrayList<ImageView>();

        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.clickpicchangebig_layout);
        final List<ImageView> sublist = new ArrayList<ImageView>();
        linearLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);

        for (int i = 0; i < Bimp.getInstance().url.size(); i++) {
            animotionCoordinate = i;
            final PhotoView imageView = new PhotoView(ShareActivity.this);
          /*  int screenWidth = d.getWidth();
            imageView.setMaxWidth(screenWidth);
            imageView.setMaxHeight(d.getHeight());*/
            // DiskBitmap.getDiskBitmap(Bimp.getInstance().url.get(i), ShareActivity.this)
            // imageView.setImageBitmap(Bimp.getInstance().bmp.get(i));
            Glide.with(ShareActivity.this).load(Uri.fromFile(new File(Bimp.getInstance().url.get(i)))).into(imageView);
            //  Picasso.with(ShareActivity.this).load(new File(Bimp.getInstance().url.get(i))).into(imageView);
            // imageView.setBackground(new BitmapDrawable(Bimp.getInstance().bmp.get(i)));
            imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float v, float v1) {
                    if (flag) {
                        relativeLayout.setVisibility(View.VISIBLE);
                        flag = false;
                    } else {
                        relativeLayout.setVisibility(View.GONE);
                        flag = true;
                    }
                }
            });
            list.add(imageView);

            ImageView icon = new ImageView(activity);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                lp.setMargins(15, 0, 0, 0);
            }
            icon.setLayoutParams(lp);
            icon.setBackgroundResource(R.drawable.black_icon_shape);
            linearLayout.addView(icon);
        }

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public ImageView instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
                container.addView(list.get(position), 0);//添加页卡
                return list.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(list.get(position));//删除页卡
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        viewPager.setCurrentItem(position);
        prePosition = position;
        linearLayout.getChildAt(prePosition).setBackgroundResource(R.drawable.yellow_icon_shape);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                relativeLayout.setVisibility(View.GONE);
                flag = true;
                linearLayout.getChildAt(position).setBackgroundResource(R.drawable.yellow_icon_shape);
                linearLayout.getChildAt(prePosition).setBackgroundResource(R.drawable.black_icon_shape);
                prePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ImageModel imageModel : mImageList) {
                    if (Bimp.getInstance().url.get(prePosition).equals(imageModel.getPath())) {
                        imageModel.setIsChecked(false);
                    }
                }
                Bimp.getInstance().bmp.remove(prePosition);
                Bimp.getInstance().url.remove(prePosition);
                adapter.notifyDataSetChanged();
                noScrollgridview.setAdapter(adapter);
                popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(view, d.getWidth(), d.getHeight(), true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.showAtLocation(new View(this), Gravity.CENTER, 0, 0);
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void takePic() {
      /*  Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image*//*");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
        */

        String text = share_shareText.getText().toString();
        Log.i("TTTTTTTText", text);
        startActivityForResult(new Intent(ShareActivity.this, ShareImageHandleActivity.class).putExtra("text", text), 1);
    }

    public void photo() {
        Intent intentt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentt, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK && data != null && Bimp.getInstance().bmp.size() < 9) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                String pictime = System.currentTimeMillis() + "";
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/image/" + pictime + ".png";
                ScreenShot.saveMyBitmap(bitmap, pictime, false, ShareActivity.this);

                Bimp.getInstance().bmp.add(bitmap);
                Bimp.getInstance().url.add(path);
                adapter.notifyDataSetChanged();
                noScrollgridview.setAdapter(adapter);
            } else {
                Toast.makeText(ShareActivity.this, "没有选中图片", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public class MyCallBack<ResultType> implements Callback.CommonCallback<ResultType> {

        @Override
        public void onSuccess(ResultType result) {
            //可以根据公司的需求进行统一的请求成功的逻辑处理
            Log.i("QQQQQQQQQonSuccess", "" + result.toString());
            try {
                JSONObject jsonObject = new JSONObject(result.toString());
                if (jsonObject.getString("IsSuccess").equals("true")) {
                    ShareActivity.activity.finish();
                    Toast.makeText(ShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ShareActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            //可以根据公司的需求进行统一的请求网络失败的逻辑处理
            Log.i("QQQQQQQQQonErrorss", "");
        }

        @Override
        public void onCancelled(CancelledException cex) {
            Log.i("QQQQQQQQQoncelleds", "");

        }

        @Override
        public void onFinished() {
            Log.i("QQQQQQQQQononFinished", "");
        }


    }
}
