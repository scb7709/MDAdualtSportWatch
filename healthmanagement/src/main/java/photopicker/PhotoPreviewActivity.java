package photopicker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.headlth.management.R;
import com.headlth.management.utils.ImageUtil;
import com.headlth.management.utils.ScreenShot;


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
 * Created by foamtrace on 2015/8/25.
 */
@ContentView(R.layout.activity_image_preview)
public class PhotoPreviewActivity extends Activity {

    public static final String EXTRA_PHOTOS = "extra_photos";
    public static final String EXTRA_CURRENT_ITEM = "extra_current_item";

    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "preview_result";

    /**
     * 预览请求状态码
     */
    public static final int REQUEST_PREVIEW = 99;
    private int currentItem = 0;

    private TextView view_publictitle_title;


    @ViewInject(R.id.activity_image_preview_view_viewpage)
    private ViewPager viewPager;
    @ViewInject(R.id.activity_image_preview_view_clickpicchangebig_layout)
    private LinearLayout linearLayout;


    @ViewInject(R.id.view)
    private View headView;

    private ArrayList<String> list;
    private int prePosition;
    private int CurrentItem;
    private Activity activity;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        x.view().inject(this);
        activity = PhotoPreviewActivity.this;
        initialize();
    }

    private void initialize() {
        showPopupWindow(this);
        // activity_image_preview_view_head.getBackground().setAlpha(200);
        list = new ArrayList<>();
        Intent intent = getIntent();
        List<String> listtemp = (List<String>) intent.getSerializableExtra("imagePaths");
        CurrentItem = intent.getIntExtra("CurrentItem", 0);
        if (listtemp != null && listtemp.size() != 0) {
            for (String url : listtemp) {
                if (!url.equals("000000")) {
                    list.add(url);

                }
            }

        }
        Log.i("pathArrrrl", CurrentItem + "  " + list.size() + "  ");
        linearLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
        for (int i = 0; i < list.size(); i++) {
            ImageView icon = new ImageView(PhotoPreviewActivity.this);
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
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public PhotoView instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
                final PhotoView photoView = new PhotoView(activity);
                loadBigImage(photoView, list.get(position));
                photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float v, float v1) {
                        Log.i("onPhotoTap", CurrentItem + "  " + list.size() + "  ");
                        if (!popupWindow.isShowing()) {
                            popupWindow.showAtLocation(new View(PhotoPreviewActivity.this), Gravity.TOP, 0, 0);
                        } else {
                            popupWindow.dismiss();
                        }
                    }
                });

                container.addView(photoView, 0);//添加页卡
                return photoView;
            }

            /*    @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                *//*    View layout = photoViewArrayList.get(position);
                container.removeView(layout);*//*
                container.removeView((PhotoView) object);
            }*/
            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        };
        viewPager.setAdapter(pagerAdapter);
        view_publictitle_title.setText("预览(" + getString(R.string.image_index, CurrentItem + 1, list.size()) + ")");

        viewPager.setCurrentItem(CurrentItem);
        viewPager.setOffscreenPageLimit(5);
        prePosition = CurrentItem;
        linearLayout.getChildAt(prePosition).setBackgroundResource(R.drawable.yellow_icon_shape);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

               /* if (activity_image_preview_view_head.getVisibility() == View.VISIBLE) {
                    activity_image_preview_view_head.setVisibility(View.GONE);
                }*/

                view_publictitle_title.setText("预览(" + getString(R.string.image_index, position + 1, list.size()) + ")");
                try {
                    linearLayout.getChildAt(position).setBackgroundResource(R.drawable.yellow_icon_shape);
                    linearLayout.getChildAt(prePosition).setBackgroundResource(R.drawable.black_icon_shape);
                } catch (Exception e) {
                }
                prePosition = position;
                if (!popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // startAnimation(ImageUtil.dp2px(PhotoPreviewActivity.this,50),ImageUtil.dp2px(PhotoPreviewActivity.this,-50));

            }
        });


    }

    private void loadBigImage(final PhotoView imageView, String imgurl) {
        final Uri uri;

        uri = Uri.fromFile(new File(imgurl));

        Glide.with(activity).load(uri)
                .skipMemoryCache(true)
                .thumbnail(0.1f)//先显示缩略图  缩略图为原图的1/10
                .error(R.drawable.nodate)
                .into(imageView);


    }

    /**
     * 取消Activity关闭动画
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        onBackPressed();

        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, list);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }
    private void DeleteImage() {
        final int index = viewPager.getCurrentItem();
        Log.i("index", "" + index);
        //   final String deletedPath = paths.get(index);
        // Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), R.string.deleted_a_photo, Snackbar.LENGTH_LONG);
        if (list.size() <= 1) {
            // 最后一张照片弹出删除提示
            new AlertDialog.Builder(this)
                    .setTitle(R.string.confirm_to_delete)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            list.remove(index);
                            onBackPressed();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        } else {

            //    snackbar.show();
            linearLayout.removeViewAt(prePosition);
            list.remove(index);
            //  photoViewArrayList.remove(index);
            pagerAdapter.notifyDataSetChanged();

            if (index == list.size()) {
                view_publictitle_title.setText("预览(" + getString(R.string.image_index, index, list.size()) + ")");
                linearLayout.getChildAt(index - 1).setBackgroundResource(R.drawable.yellow_icon_shape);
            } else {
                view_publictitle_title.setText("预览(" + getString(R.string.image_index, index + 1, list.size()) + ")");
                linearLayout.getChildAt(index).setBackgroundResource(R.drawable.yellow_icon_shape);
            }

        }
    }


    /**
     * 弹出是否绑定腕表的提示框
     */
    PopupWindow popupWindow;

    public void showPopupWindow(final Activity activity) {//flag=true是提示绑定腕表 否则是 提示监控数据已经同步
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_photopreview, null);
        popupWindow = new PopupWindow(view, ScreenShot.getwidthPixels(activity), ImageUtil.dp2px(activity, 50), true);
        view_publictitle_title = (TextView) view.findViewById(R.id.activity_image_preview_view_title);
        view.findViewById(R.id.activity_image_preview_view_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteImage();
            }
        });
        view. findViewById(R.id.activity_image_preview_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        popupWindow.setFocusable(false);
        popupWindow.setAnimationStyle(R.style.AnimationPreview);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
// TODO Auto-generated method stub
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
        return super.onTouchEvent(event);
    }
}
