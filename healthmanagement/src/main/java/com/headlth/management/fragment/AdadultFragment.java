package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.activity.NewChuFang;
import com.headlth.management.activity.StrengthSportActivity;
import com.headlth.management.activity.XinlvTu;
import com.headlth.management.activity.tijianBaoGao;
import com.headlth.management.circle.SeekCircleLeftHalf;
import com.headlth.management.circle.SeekCircleRightHalf;
import com.headlth.management.clenderutil.DateUtil;
import com.headlth.management.entity.chufangCallBack;
import com.headlth.management.entity.logcallback;
import com.headlth.management.entity.newChuFangCallback;
import com.headlth.management.entity.tiJianCallBack;
import com.headlth.management.entity.xinlvtuCallBack;
import com.headlth.management.scan.Search;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.InternetUtils;
import com.headlth.management.utils.Share;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.HttpUtils;
import com.zaaach.toprightmenu.TopRightMenu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AdadultFragment extends BaseFragment {

    @InjectView(R.id.ValidTime)
    TextView ValidTime;

    @InjectView(R.id.ValidTime_strength)
    TextView ValidTime_strength;
    @InjectView(R.id.Target)
    TextView Target;
    @InjectView(R.id.BigCar)
    TextView BigCar;
    @InjectView(R.id.Duration)
    TextView Duration;
    @InjectView(R.id.HeartRateAvg)
    TextView HeartRateAvg;
    @InjectView(R.id.seekCircle)
    SeekCircleLeftHalf progressCircle;
    @InjectView(R.id.seekCircle_strength)
    SeekCircleRightHalf seekCircle_strength;


    private Button btstart;
    private Button gerenchufang;
    private Button TJbaogao;
    private Button xinlv;
    private View view;
    private int target = 0;
    TextView first;
    TextView today;
    TextView second;
    private static logcallback log;
    public chufangCallBack chufang;
    String tempData;
    ImageButton bt_rili;
    String url;
    String isPlay;
    int PowerTrainDuration;
    String date;//当前日期
    int flag;





    @SuppressLint("ValidFragment")
    public AdadultFragment(logcallback log, String tempData, String date) {
        this.tempData = tempData;
        isDismiss = false;
        circleEnd = false;
        this.log = log;
        this.date = date;
        progressCircle = null;
        seekCircle_strength = null;


    }

    public AdadultFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        i = 0;
        url = Constant.BASE_URL;
        isDismiss = false;
        Log.e("ffff", "onCreateView");
        if (date == null) {
            date = DateUtil.getYear() + "-" + second(DateUtil.getMonth()) + "-" + second(DateUtil.getCurrentMonthDay());
        }
        flag = isDate(date);
        view = inflater.inflate(R.layout.main, null);

        Log.i("ppppwwww", isPlay + "   " + PowerTrainDuration);
        bt_rili = (ImageButton) getActivity().findViewById(R.id.bt_rili);
        bt_rili.setVisibility(View.VISIBLE);
        gerenchufang = (Button) view.findViewById(R.id.gerenchufang);
        TJbaogao = (Button) view.findViewById(R.id.TJbaogao);
        xinlv = (Button) view.findViewById(R.id.chakanxinlv);
        ButterKnife.inject(this, view);
        first = (TextView) getActivity().findViewById(R.id.statechange);
        today = (TextView) getActivity().findViewById(R.id.today);
        second = (TextView) getActivity().findViewById(R.id.second);
        Button  share = (Button) getActivity().findViewById(R.id.main_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  //  Share.getInstance().showPopFormBottom(getActivity());
            }
        });
        return view;
    }

    private int i = 0;
    private int youxiao = 0;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        i = 0;
        isDismiss = false;
        Log.e("ffff", "onViewCreated");
        today.setText("");
        second.setText("");
        circleEnd = false;
        //progressCircle = ;
        if (log != null) {
            HeartRateAvg.setText(log.getUserList().get(0).getHeartRateAvg() + "");
            Duration.setText((log.getUserList().get(0).getDuration() / 60) + "'" + second(log.getUserList().get(0).getDuration() % 60) + "\"");
            BigCar.setText(log.getUserList().get(0).getBigCar() + "");
            Target.setText("" + Integer.parseInt(log.getUserList().get(0).getTarget()) / 60 + "'" + second(Integer.parseInt(log.getUserList().get(0).getTarget()) % 60) + "\"");
            ValidTime.setText((log.getUserList().get(0).getValidTime() / 60) + "'" + second(log.getUserList().get(0).getValidTime() % 60) + "\"");
            PowerTrainDuration = Integer.parseInt(log.getUserList().get(0).getPowerTrainDuration());
            isPlay = log.getUserList().get(0).getIsPlay();

            if (flag == -1) {//过去某天
                Log.i("TTTTTTTTWWWWWW过去是否有力量训练", isPlay + " " + PowerTrainDuration);

                if (PowerTrainDuration==0) {

                    ValidTime_strength.setText("您今天没有\n力量训练");
                } else {
                    ValidTime_strength.setText("您今天已完成\n力量训练");
                }

            } else if (flag == 0) {
                Log.i("TTTTTTTTWWWWWW今天是否有力量训练", isPlay + "    " + PowerTrainDuration);
                if (PowerTrainDuration!=0) {
                    ValidTime_strength.setText("您今天已完成\n力量训练");
                } else if (isPlay.equals("0")) {

                    ValidTime_strength.setText("您今天没有\n力量训练");
                } else {
                    ValidTime_strength.setText("您今天未完成\n力量训练");
                }
            } else {


            }
            if (log.getUserList().get(0).getValidTime() <= 5) {
                youxiao = 1;
            } else {
                youxiao = log.getUserList().get(0).getValidTime();
            }

        }

        btstart = (Button) getActivity().findViewById(R.id.btstart);
        btstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    if (InternetUtils.internet(getActivity())) {
                        getAdadultFragmentStartSportDalog(v);
                    } else {
                        Log.i("aaaaaaaaaaa", "bbbbbbbbbbb");
                    }
                } else {
                    Log.i("aaaaaaaaaaa", "cccccccc");
                    Toast.makeText(getActivity(), "当前时间不能开始运动", Toast.LENGTH_LONG).show();
                }

            }
        });

        xinlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xinlv(tempData, "");
            }
        });

        gerenchufang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newChuFang();


            /*    go2("", "");*/

            }
        });
        TJbaogao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent();
                intent.setClass(getActivity(), TiJianbaogao.class);
                getActivity().startActivity(intent);*/
                tiJianBaoGao("", "");
            }
        });


        if (!circleEnd) {
            //环形条
  /*          Log.e("ttt", youxiao + "执行了？" + Integer.parseInt(log.getUserList().get(0).getTarget()));*/
            // if (flag == 0) {
            setYouyangSportThread();
        }
        if (PowerTrainDuration!=0) {
            Log.i("TTTTTTTTWWWWWW线程开始画", isPlay + "    " + PowerTrainDuration);
            setStrengthSportThread();
        }
        else  {
            setNoStrengthSportThread();
        }

    }

    private void setYouyangSportThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isDismiss) {
                        return;
                    }
         /*         Log.e("ttt", youxiao + "null???????????" + Integer.parseInt(log.getUserList().get(0).getTarget()));*/
                    if (Integer.parseInt(log.getUserList().get(0).getTarget()) != 0) {
                        if (i <= (100 * (youxiao)) / (Integer.parseInt(log.getUserList().get(0).getTarget()))) {
                            circleEnd = false;
                            Message msg = Message.obtain();
                            msg.arg1 = i;
                            msg.what = 1;
                            h.sendMessage(msg);
                        } else {
                            circleEnd = true;
                            return;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    i = i + 5;
                }
            }
        }).start();
    }

    private void setNoStrengthSportThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isDismiss) {
                        return;
                    }
                    if (Integer.parseInt(log.getUserList().get(0).getTarget()) != 0) {
                        if (i <= 50) {
                            Message msg = Message.obtain();
                            msg.what = 3;
                            h.sendMessage(msg);
                        } else {
                            circleEnd = true;
                            return;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    i = i + 5;
                }
            }
        }).start();
    }
    private void setStrengthSportThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isDismiss) {
                        return;
                    }
                    if (Integer.parseInt(log.getUserList().get(0).getTarget()) != 0) {
                        if (i <= 50) {
                            Message msg = Message.obtain();
                            msg.arg1 = i;
                            msg.what = 2;
                            h.sendMessage(msg);
                        } else {
                            circleEnd = true;
                            return;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    i = i + 5;
                }
            }
        }).start();
    }

    public String second(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return "" + i;
        }
    }

    Boolean isDismiss = false;
    public static Boolean circleEnd = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ffff", "onDestroy");
        isDismiss = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isDismiss = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isDismiss = true;
        Log.e("ffff", "onPause");
    }

    Gson g = new Gson();
    tiJianCallBack tijian;

    private void tiJianBaoGao(final String phone, final String pwd) {
        //在这里设置需要post的参数
        Map<String, String> map = new HashMap<String, String>();
        if (!ShareUitls.getString(getActivity(), "UID", "null").equals("null")) {
            map.put("UID", ShareUitls.getString(getActivity(), "UID", "null"));
        }
        HttpUtils.getInstance(getActivity()).sendRequest(Constant.DIALOG_MESSAGE_LOADING, Constant.BASE_URL+ "/MdMobileService.ashx?do=PostRepostRequest", map, 0, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("ffff", response.toString());

                        tijian = g.fromJson(response.toString(),
                                tiJianCallBack.class);
                        if (tijian.getStatus() == 1) {

                            ShareUitls.putString(getActivity(), "selfFirst", 1 + "");


                            //对象序列化
                            try {
                                saveObject(serialize(tijian));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            Intent intent = new Intent();
                            intent.setClass(getActivity(), tijianBaoGao.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("tijian", tijian);
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);
                        } else {
                            ShareUitls.putString(getActivity(), "selfFirst", "null");
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA","LoginupToken");

                            ShareUitls.putString(getActivity(), "selfFirst", "null");
                            Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                            return;

                    }
                }

        );

    }


    //心率http://www.ssp365.com:8066/MdMobileService.ashx?do=PostHeartRateRequest
    xinlvtuCallBack xinlvCall;

    private void xinlv(final String phone, final String pwd) {
        //在这里设置需要post的参数
        Map<String, String> map = new HashMap<String, String>();
        if (!ShareUitls.getString(getActivity(), "UID", "null").equals("null")) {
            map.put("UID", ShareUitls.getString(getActivity(), "UID", "null"));
        }
        map.put("SportTime", phone);
        HttpUtils.getInstance(getActivity()).sendRequest(Constant.DIALOG_MESSAGE_LOADING, Constant.BASE_URL+ "/MdMobileService.ashx?do=PostHeartRateRequest", map, 0, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("xinlvtu", "xinlv" + response.toString());
                        xinlvCall = g.fromJson(response.toString(),
                                xinlvtuCallBack.class);
                        if (xinlvCall.getStatus() == 1) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), XinlvTu.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("xinlvCall", xinlvCall);
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "huoqishibai", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {

                            Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                            return;

                    }
                }

        );
    }


    //新的处方接口
    private newChuFangCallback newChuFang;

    private void newChuFang() {
        //在这里设置需要post的参数
        Map<String, String> map = new HashMap<String, String>();
        if (!ShareUitls.getString(getActivity(), "UID", "null").equals("null")) {
            map.put("UID", ShareUitls.getString(getActivity(), "UID", "null"));
        }
        HttpUtils.getInstance(getActivity()).sendRequest(Constant.DIALOG_MESSAGE_LOADING, Constant.BASE_URL+ "/MdMobileService.ashx?do=PostPrescriptionNewRequest", map, 0, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VVVVVVVVVVVVVV", "" + response.toString());
                        newChuFang = g.fromJson(response.toString(),
                                newChuFangCallback.class);
                        if (newChuFang.getStatus() == 1) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), NewChuFang.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("newChuFang", newChuFang);
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), "对不起，您还没有运动处方可查看!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {

                            Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                            return;

                    }
                }

        );
    }


    // private SeekCircle progressCircle;
    public Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.e("iiii", i + ".........");
                try {
                    progressCircle.setProgress(msg.arg1);
                }catch (NullPointerException n){

                }
            }
            if (msg.what == 2) {
                Log.e("iiii", i + ".........");
                try {
                    seekCircle_strength.setProgress(msg.arg1);
                }catch (NullPointerException n){

                }

            }
            if (msg.what == 3) {
                Log.e("iiii", i + ".........");
                try {
                    seekCircle_strength.setProgress(0);
                }catch (NullPointerException n){

                }

            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    void saveObject(String strObject) {
        SharedPreferences sp = getActivity().getSharedPreferences("tijian", 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("tijian", strObject);
        edit.commit();
    }

    /**
     * 序列化对象
     *
     * @param person
     * @return
     * @throws IOException
     */
    private String serialize(tiJianCallBack person) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(person);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }

    //判断今天是否是礼拜三或者礼拜六
    private boolean getToday() {
        boolean today = false;
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 4 || weekday == 7) {
            today = true;
        }
        // return today;
        return true;

    }

    PopupWindow popupWindow;

    //点击开始运动的对话框
    private void getAdadultFragmentStartSportDalog(View v) {
        View view = null;
        int hei = 0;

        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        boolean flag = isPlay.equals("1") && PowerTrainDuration == 0;
       // flag=true;
        if (flag) {
            hei = (int) (d.getHeight() * 0.4);
            view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_adadultfragmentstartsport2, null);
            Button liliangSport = (Button) view.findViewById(R.id.AdadultFragmentStartSportDalog_liliang);
            liliangSport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().startActivity(new Intent(getActivity(), StrengthSportActivity.class));
                    popupWindow.dismiss();
                }
            });
        } else {
            hei = (int) (d.getHeight() * 0.2);
            view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_adadultfragmentstartsport, null);
        }


        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                popupWindow.dismiss();

                return false;
            }
        });
        Button youyangSport = (Button) view.findViewById(R.id.AdadultFragmentStartSportDalog_youyang);
        popupWindow = new PopupWindow(view, (int) (d.getWidth() * 0.6), hei, true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        youyangSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), Search.class);
                getActivity().startActivity(intent); //
                popupWindow.dismiss();

            }
        });

        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


    }

    //判断所选时间是否是过去时间
    private int isDate(String goalDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());
        Log.i("jjjjjjjjjjjjjjjj", goalDate + "  " + today + "  " + today.substring(0, 4) + " " + goalDate.substring(0, 4) + " " + goalDate.length() + "  " + today.length());

        int year = Integer.parseInt(today.substring(0, 4)) - Integer.parseInt(goalDate.substring(0, 4));
        int month = Integer.parseInt(today.substring(5, 7)) - Integer.parseInt(goalDate.substring(5, 7));
        int day = Integer.parseInt(today.substring(8, 10)) - Integer.parseInt(goalDate.substring(8, 10));
        if (year < 0) {
            return 1;
        } else if (year > 0) {
            return -1;
        } else {
            if (month < 0) {
                return 1;
            } else if (month > 0) {
                return -1;
            } else {
                if (day < 0) {
                    return 1;
                } else if (day > 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }
}
