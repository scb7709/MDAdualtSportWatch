package com.headlth.management.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.entity.deleteMessagCallBack;
import com.headlth.management.entity.xiaoXiCallBack;
import com.headlth.management.movelistview.SwipeMenu;
import com.headlth.management.movelistview.SwipeMenuCreator;
import com.headlth.management.movelistview.SwipeMenuItem;
import com.headlth.management.movelistview.SwipeMenuListView;
import com.headlth.management.movelistview.messegeEntity;
import com.headlth.management.movelistview.tempEntity;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by abc on 2016/10/21.
 *
 * 已经废弃
 */
@ContentView(R.layout.activity_oldmessage)
public class OldMessageActivity extends BaseActivity {
    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;



    @ViewInject(R.id.liner)
    LinearLayout layout;
    private List<messegeEntity> mAppList;
    private List<SwipeMenuListView> SwipeMenuListViews;
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;

    Gson g = new Gson();
    deleteMessagCallBack deleteMesg;

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
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

    public Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

               /* mAppList.remove(position);
                if (mAppList.size() == 0) {
                    layout.removeView(tv);
                }*/
                tempEntity temp = (tempEntity) msg.obj;
                temp.getmAppList().remove(temp.getPosition());
                if (temp.getmAppList().size() == 0) {
                    layout.removeView(temp.getTv());
                }
                temp.getAppAdapter().notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        xiaoXi();
        view_publictitle_title.setText("消息");
        view_publictitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();

            }
        });

    }


    private void initView(xiaoXiCallBack xiaoxi) {
        layout.setOrientation(LinearLayout.VERTICAL);
        Log.e("lishixiaoxi", xiaoxi + "");
        Log.e("lishixiaoxi", xiaoxi.getMsgList() + "");
        Log.e("lishixiaoxi", xiaoxi.getMsgList().getDate() + "");
        if (xiaoxi.getMsgList().getInfo().size() != 0) {
            for (int i = 0; i < xiaoxi.getMsgList().getDate().size(); i++) {
                TextView tv = new TextView(OldMessageActivity.this);
                tv.setText("  " + xiaoxi.getMsgList().getDate().get(i));
                tv.setGravity(Gravity.CENTER_VERTICAL);

                tv.setTextColor(Color.parseColor("#999999"));
                tv.setTextSize(12);
                tv.setBackgroundColor(Color.parseColor("#c7c7c7"));
                LinearLayout.LayoutParams layoutParams_txt = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 60);
                tv.setLayoutParams(layoutParams_txt);

                layout.addView(tv);

                //实例化
                mListView = new SwipeMenuListView(OldMessageActivity.this);
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
                    Date date = null;
                    try {
                        date = format.parse(xiaoxi.getMsgList().getInfo().get(i).getList_msgdata().get(j).getCreateTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        } else {
            Toast.makeText(OldMessageActivity.this, "无消息可查看!", Toast.LENGTH_SHORT).show();
            finish();
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
                        OldMessageActivity.this);
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
                        OldMessageActivity.this);
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
        swipeMenuListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                return false;
            }
        });
    }

    public class AppAdapter extends BaseAdapter {
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
                convertView = View.inflate(OldMessageActivity.this, R.layout.listview_item_oldmessage, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
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

    private void deleteMsg(final String ID, final int position, final List<messegeEntity> mAppList, final AppAdapter appAdapter, final TextView tv) {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostDeviceTokenRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(this, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(this, "UID", "0"));
        params.addBodyParameter("ID", ID);
        HttpUtils.getInstance(this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("hfhfhfh", response.toString());
                        deleteMesg = g.fromJson(response.toString(), deleteMessagCallBack.class);
                        if (deleteMesg.getStatus() == 1) {
                            tempEntity temp = new tempEntity(position, tv, appAdapter, mAppList);
                            Message msg = h.obtainMessage();
                            msg.what = 1;
                            msg.obj = temp;
                            h.sendMessage(msg);
                            return;
                        } else {
                            Toast.makeText(OldMessageActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable error) {

                        Toast.makeText(OldMessageActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }

        );
    }

    private void xiaoXi() {
        SwipeMenuListViews = new ArrayList<>();

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostMsgRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(OldMessageActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(OldMessageActivity.this, "UID", "null"));
        HttpUtils.getInstance(OldMessageActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("hfhfhfh", response.toString());
                        xiaoXiCallBack xiaoxi = g.fromJson(response.toString(), xiaoXiCallBack.class);
                        if (xiaoxi.getStatus() == 1) {
                            initView(xiaoxi);
                        } else {
                            Toast.makeText(OldMessageActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA", "LoginupToken");

                        Toast.makeText(OldMessageActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                    }
                }

        );

    }

}
