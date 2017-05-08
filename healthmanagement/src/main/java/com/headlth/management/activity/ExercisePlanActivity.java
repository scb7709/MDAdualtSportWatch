package com.headlth.management.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.MuchWidthImangView;
import com.headlth.management.adapter.ImageAdapter;
import com.headlth.management.adapter.ImageHandler;
import com.headlth.management.entity.Prescription;
import com.headlth.management.entity.PrescriptionJson;
import com.headlth.management.fragment.PrescriptionFragment;
import com.headlth.management.myview.SubscriptView;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by abc on 2016/9/20.
 */
@ContentView(R.layout.activity_exerciseplan)
public class ExercisePlanActivity extends BaseActivity {


    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;


/*    @ViewInject(R.id.activity_exerciseplan_back)
    private Button activity_exerciseplan_back;*/


    @ViewInject(R.id.activity_exerciseplan_title_vp)
    public ViewPager activity_exerciseplan_title_vp;
    // @ViewInject(R.id.activity_exerciseplan_layout)
    //  private LinearLayout activity_exerciseplan_layout;
    @ViewInject(R.id.activity_exerciseplan_prescription_layout)
    private LinearLayout activity_exerciseplan_prescription_layout;
    @ViewInject(R.id.activity_exerciseplan_prescription_HorizontalScrollView)
    private HorizontalScrollView activity_exerciseplan_prescription_HorizontalScrollView;


    private List<TextView> TextList;
    private List<TextView> LineList;
    private List<SubscriptView> SubscriptViewList;
    private int textid;
    private int lineid;
    private ViewPagerPrescriptionAdapter viewPagerPrescriptionAdapter;
    @ViewInject(R.id.activity_exerciseplan_prescription)
    private ViewPager activity_exerciseplan_prescription;
    private List<Fragment> prescriptionFragmentList;
    public ImageHandler handler;

    public ArrayList<View> images;
    private List<View> dots;
    private int currentItem;
    //记录上一次点的位置
    private int oldPosition = 0;
    //存放图片的id
    private String[] imageIds = {
            "http://scimg.jb51.net/allimg/160815/103-160Q509544OC.jpg",
            "http://pic.4j4j.cn/upload/pic/20130815/31e652fe2d.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/6c224f4a20a446230761b9b79c22720e0df3d7bf.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/cdbf6c81800a19d8baa1150531fa828ba61e460b.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/a686c9177f3e670900d880193fc79f3df9dc5578.jpg"
    };
    //存放图片的标题
    private String[] titles = new String[]{
    };
    private TextView title;
    private ScheduledExecutorService scheduledExecutorService;
    private boolean isBanner = true;
    //初始化偏移量
    private int offset = 0;
    private int scrollViewWidth = 0;
    private int currentPageScrollStatus;
    private int imagesize;
    private LayoutInflater inflater;


    private List<PrescriptionJson.PrescriptionClass> PrescriptionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        view_publictitle_title.setText("锻炼计划");
        imagesize = imageIds.length;
        ;
        initializeBanner();
        initializeLineList();
        initializePrescriptionFragmentList();
    }


    @Event(value = {R.id.view_publictitle_back})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
        }
    }


    private void initializePrescriptionFragmentList() {

        prescriptionFragmentList = new ArrayList<>();


        activity_exerciseplan_prescription.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changechoose(TextList.get(position).getId(), LineList.get(position).getId());
                final SubscriptView textView = SubscriptViewList.get(position);
                scrollViewWidth = activity_exerciseplan_prescription_HorizontalScrollView.getWidth();

                if ((scrollViewWidth + offset) < textView.getRight()) {//需要向右移动
                    activity_exerciseplan_prescription_HorizontalScrollView.smoothScrollBy(textView.getRight() - (scrollViewWidth + offset), 0);
                    offset += textView.getRight() - (scrollViewWidth + offset);
                }

                if (offset > textView.getLeft()) {//需要向左移动
                    activity_exerciseplan_prescription_HorizontalScrollView.smoothScrollBy(textView.getLeft() - offset, 0);
                    offset += textView.getLeft() - offset;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getTotalTimeRequest();
    }

    String[] str = new String[10];

    private void initializeLineList() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LineList = new ArrayList<>();
        TextList = new ArrayList<>();
        SubscriptViewList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            str[i] = "所有处方" + i;
        }
        for (int i = 0; i < 10; i++) {
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            textParams.setMargins(30, 0, 30, 0);
            final SubscriptView view = (SubscriptView) layoutInflater.inflate(R.layout.item_activity_exerciseplan_prescription_layout, null);
            view.Subscript = i;
            SubscriptView item_activity_exerciseplan_prescription = (SubscriptView) view.findViewById(R.id.item_activity_exerciseplan_prescription);

            final TextView item_activity_exerciseplan_prescription_text = (TextView) view.findViewById(R.id.item_activity_exerciseplan_prescription_text);
            final TextView item_activity_exerciseplan_prescription_line = (TextView) view.findViewById(R.id.item_activity_exerciseplan_prescription_line);
            item_activity_exerciseplan_prescription_text.setId(View.generateViewId());
            item_activity_exerciseplan_prescription_line.setId(View.generateViewId());

            item_activity_exerciseplan_prescription_text.setText(str[i]);
            item_activity_exerciseplan_prescription_line.setText(str[i]);
            item_activity_exerciseplan_prescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changechoose(item_activity_exerciseplan_prescription_text.getId(), item_activity_exerciseplan_prescription_line.getId());
                    for (int j = 0; j < SubscriptViewList.size(); j++) {
                        if (SubscriptViewList.get(j).Subscript == view.Subscript) {
                            activity_exerciseplan_prescription.setCurrentItem(j);
                        }
                    }

                }
            });

            TextList.add(item_activity_exerciseplan_prescription_text);
            LineList.add(item_activity_exerciseplan_prescription_line);
            SubscriptViewList.add(view);
            view.setLayoutParams(textParams);
            activity_exerciseplan_prescription_layout.addView(view);
        }
        changechoose(TextList.get(0).getId(), LineList.get(0).getId());
    }

    private void changechoose(int textid, int lineid) {
        for (int i = 0; i < LineList.size(); i++) {
            if (TextList.get(i).getId() == textid) {
                TextList.get(i).setTextColor(Color.parseColor("#ffac04"));
            } else {
                TextList.get(i).setTextColor(Color.parseColor("#999999"));
            }
            if (LineList.get(i).getId() == lineid) {
                LineList.get(i).setVisibility(View.VISIBLE);
            } else {
                LineList.get(i).setVisibility(View.INVISIBLE);
            }

        }

    }

    private void initializeBanner() {
        handler = new ImageHandler(new WeakReference<ExercisePlanActivity>(this));
        // activity_exerciseplan_layout.setVerticalGravity(Gravity.CENTER_VERTICAL);
        images = new ArrayList<View>();
        //显示的小点
        dots = new ArrayList<View>();
        inflater = LayoutInflater.from(this);
        for (int i = 0; i < imagesize; i++) {
            FrameLayout view = (FrameLayout) inflater.inflate(R.layout.item_exerciseplan_banner, null, false);
            final Button button = (Button) view.findViewById(R.id.item_activity_exerciseplan_image);
            final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.item_activity_exerciseplan_layout);
            linearLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
            Glide.with(ExercisePlanActivity.this)
                    .load(imageIds[i])
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (bitmap != null) {
                                button.setBackground(new BitmapDrawable(bitmap));
                            }

                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                        }

                    });
            for (int j = 0; j < imagesize; j++) {
                ImageView icon = new ImageView(ExercisePlanActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(10, 0, 10, 0);
                icon.setLayoutParams(lp);
                if (i == j) {
                    icon.setBackgroundResource(R.drawable.yellow_icon_shape);
                } else {
                    icon.setBackgroundResource(R.drawable.black_icon_shape);
                }

               linearLayout.addView(icon);
                // dots.add(icon);

            }
            images.add(view);
        }
        activity_exerciseplan_title_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, position, 0));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }
        });

        activity_exerciseplan_title_vp.setAdapter(new ImageAdapter(images));
        activity_exerciseplan_title_vp.setCurrentItem(imagesize*1000);//默认在中间，使用户看不到边界
        //开始轮播效果
        //handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
       // handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
          handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
        // handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, 5000, 0));
    }

    private class ViewPagerPrescriptionAdapter extends FragmentPagerAdapter {
        public ViewPagerPrescriptionAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return prescriptionFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return prescriptionFragmentList.size();
        }


    }

    public static void scrollToBottom(final View scroll, final View inner) {
        Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }
                scroll.scrollTo(0, offset);
            }
        });
    }



    private void getTotalTimeRequest() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("UserID", ShareUitls.getString(ExercisePlanActivity.this,"UID",""));
        //map.put("PlanClassID", ShareUitls.getString(ExercisePlanActivity.this,"UID",""));

        HttpUtils.getInstance(ExercisePlanActivity.this).sendRequest("", Constant.BASE_URL + "/MdMobileService.ashx?do=GetPlanNameListRequest", map, 10, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        PrescriptionJson prescriptionJson=new Gson().fromJson(response,PrescriptionJson.class);
                        if(prescriptionJson.Status.equals("1")){
                            PrescriptionList =prescriptionJson.PlanNameList;



                            for (PrescriptionJson.PrescriptionClass prescriptionClass:PrescriptionList) {
                                prescriptionFragmentList.add(new PrescriptionFragment(prescriptionClass));
                            }

                            viewPagerPrescriptionAdapter = new ViewPagerPrescriptionAdapter();
                            activity_exerciseplan_prescription.setAdapter(viewPagerPrescriptionAdapter);
                        }




                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(ExercisePlanActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                }

        );

    }

    ///MdMobileService.ashx?do=GetPlanNameListRequest
}
