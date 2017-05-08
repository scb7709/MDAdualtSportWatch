package com.headlth.management.movelistview;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.activity.BaseActivity;
import com.headlth.management.entity.deleteMessagCallBack;
import com.headlth.management.entity.xiaoXiCallBack;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleActivity2 extends BaseActivity {

    private List<messegeEntity> mAppList;
    private List<SwipeMenuListView> SwipeMenuListViews;
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    private static String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liner);
        url = Constant.BASE_URL;


        SwipeMenuListViews = new ArrayList<>();
        try {
            initView();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void intListView(final SwipeMenuListView swipeMenuListView, final List<messegeEntity> mAppList, final AppAdapter appAdapter, final TextView tv) {

        swipeMenuListView.setAdapter(appAdapter);
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
          /*      openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));*/
                // set item width
                openItem.setWidth(dp2px(0));
                // set item title
          /*      openItem.setTitle("Open");*/
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        swipeMenuListView.setMenuCreator(creator);

        // step 2. listener item click event
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
              /*  String item = mAppList.get(position);*/
                switch (index) {
                    case 0:
                        // open
                /*	open(item);*/
                        break;
                    case 1:
                        // delete
//					delete(item);

                        deleteMsg(mAppList.get(position).getID() + "", position, mAppList, appAdapter, tv);


                        break;
                }
            }
        });

        // set SwipeListener
        swipeMenuListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        swipeMenuListView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(getApplicationContext(), position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    LinearLayout layout;
    xiaoXiCallBack xiaoxi;

    private void initView() throws ParseException {
        Intent intent = this.getIntent();
        xiaoxi = (xiaoXiCallBack) intent.getSerializableExtra("xiaoxi");

        // 获取xml的RelativeLayout
        layout = (LinearLayout) findViewById(R.id.liner);
        layout.setOrientation(LinearLayout.VERTICAL);

     /*   Log.e("xiaoxi", xiaoxi.getMsgList().getDate() + "");
        Log.e("xiaoxi", xiaoxi.getMsgList().getInfo().size() + "iaoxi.getMsgList().getInfo().size()");
        Log.e("xiaoxi", xiaoxi.getMsgList().getInfo().get(0).getList_msgdata().size() + "xiaoxi.getMsgList().getInfo().get(0).getList_msgdata().size()");
*/
        //历史
       if(xiaoxi.getMsgList().getInfo().size()!=0){
           for (int i = 0; i < xiaoxi.getMsgList().getDate().size(); i++) {
               TextView tv = new TextView(getApplicationContext());
               tv.setText("  "+xiaoxi.getMsgList().getDate().get(i));
               tv.setGravity(Gravity.CENTER_VERTICAL);

               tv.setTextColor(Color.parseColor("#999999"));
               tv.setTextSize(12);
               tv.setBackgroundColor(Color.parseColor("#c7c7c7"));
               LinearLayout.LayoutParams layoutParams_txt = new LinearLayout.LayoutParams(
                       ViewGroup.LayoutParams.MATCH_PARENT, 60);
               tv.setLayoutParams(layoutParams_txt);

               layout.addView(tv);

               //实例化
               mListView = new SwipeMenuListView(getApplicationContext());
               SwipeMenuListViews.add(mListView);
               mAppList = new ArrayList<>();
               mAdapter = new AppAdapter(mAppList);


               for (int j = 0; j < xiaoxi.getMsgList().getInfo().get(i).getList_msgdata().size(); j++) {
                   messegeEntity mesEntity = new messegeEntity();
                   mesEntity.setUserID(xiaoxi.getMsgList().getInfo().get(i).getList_msgdata().get(j).getUserID());
                   mesEntity.setContent(xiaoxi.getMsgList().getInfo().get(i).getList_msgdata().get(j).getContent());
                   //  Log.e("xiaoxi",xiaoxi.getMsgList().getInfo().get(i).getList_msgdata().get(j).getContent() + " xiaoxi.getMsgList().getInfo().get(i).getList_msgdata().get(j).getContent()");
                   mesEntity.setTitle(xiaoxi.getMsgList().getInfo().get(i).getList_msgdata().get(j).getTitle());
                   mesEntity.setID(xiaoxi.getMsgList().getInfo().get(i).getList_msgdata().get(j).getID());


                   SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:m:s");
                   Date date = format.parse(xiaoxi.getMsgList().getInfo().get(i).getList_msgdata().get(j).getCreateTime());
                   Log.e("xiaoxi", format(date) + "format(date)");
                   mesEntity.setCreateTime(format(date));
                   mAppList.add(mesEntity);
               }
               Log.e("xiaoxi", mAppList.size() + " mAppList.size()");
               LinearLayout.LayoutParams layoutParams_list = new LinearLayout.LayoutParams(
                       ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
               mListView.setLayoutParams(layoutParams_list);
               layout.addView(mListView);
               intListView(mListView, mAppList, mAdapter, tv);
           }
       }else{
           Toast.makeText(getApplicationContext(),"无消息可查看!",Toast.LENGTH_SHORT).show();
       }


    }


    private void delete(ApplicationInfo item) {
        // delete app
        try {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.fromParts("package", item.packageName, null));
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    private void open(ApplicationInfo item) {
        // open app
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(item.packageName);
        List<ResolveInfo> resolveInfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            ResolveInfo resolveInfo = resolveInfoList.get(0);
            String activityPackageName = resolveInfo.activityInfo.packageName;
            String className = resolveInfo.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName componentName = new ComponentName(
                    activityPackageName, className);

            intent.setComponent(componentName);
            startActivity(intent);
        }
    }

    class AppAdapter extends BaseAdapter {
        List<messegeEntity> mAppList = null;

        AppAdapter(List<messegeEntity> mAppList) {
            this.mAppList = mAppList;
        }

        @Override
        public int getCount() {
            return mAppList.size();
        }

        @Override
        public messegeEntity getItem(int position) {
            return mAppList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.listview_item_oldmessage, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
   /*         xiaoXiCallBack.MsgListBean.InfoBean.ListMsgdataBean item = getItem(position);*/
            holder.tv_name.setText(mAppList.get(position).getTitle());
            Log.e("xiaoxi", mAppList.get(position).getContent() + "mAppList.get(position).getContent()");
            holder.tv_content.setText(mAppList.get(position).getContent());
            holder.data_time.setText(mAppList.get(position).getCreateTime());
            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name;
            TextView tv_content;
            TextView data_time;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.title);
                tv_content = (TextView) view.findViewById(R.id.msg_content);
                data_time = (TextView) view.findViewById(R.id.data_time);
                view.setTag(this);
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void back(View view) {
        finish();
    }

    /*时间差*/
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    public static String format(Date date) {
        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }


    //删除消息
    //*/请求地址：http://192.168.0.250:8082/MdMobileService.ashx?do=PostDelMsgRequest

    Gson g = new Gson();

    deleteMessagCallBack deleteMesg;

    private void deleteMsg(final String ID, final int position, final List<messegeEntity> mAppList, final AppAdapter appAdapter, final TextView tv) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("ID", ID);

        HttpUtils.getInstance(SimpleActivity2.this).sendRequest(Constant.DIALOG_MESSAGE_LOADING, Constant.BASE_URL+ "/MdMobileService.ashx?do=PostDeviceTokenRequest", map, 0, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        deleteMesg = g.fromJson(response.toString(),
                                deleteMessagCallBack.class);
                        if (deleteMesg.getStatus() == 1) {
                            mAppList.remove(position);
                            if (mAppList.size() == 0) {
                                layout.removeView(tv);
                            }
                            appAdapter.notifyDataSetChanged();
                            return;
                        } else {
                            Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable error) {

                        Toast.makeText(SimpleActivity2.this, "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );

      /*  referenceQueue = new volleyque(getApplicationContext()).getRequestQueue();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url + "/MdMobileService.ashx?do=PostDelMsgRequest",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("hfhfhfh", response.toString());
                        deleteMesg = g.fromJson(response.toString(),
                                deleteMessagCallBack.class);
                        if (deleteMesg.getStatus() == 1) {
                            mAppList.remove(position);
                            if (mAppList.size() == 0) {
                                layout.removeView(tv);
                            }
                            appAdapter.notifyDataSetChanged();
                            return;
                        } else {
                            Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
                return;
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();

                map.put("ID", ID);
                return map;
            }
        };
        stringRequest.setShouldCache(false);
        referenceQueue.add(stringRequest);
        referenceQueue.start();*/
    }

}
