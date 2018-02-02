package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.adapter.MyFragmentPagerAdapter;
import com.headlth.management.clenderutil.DateUtil;
import com.headlth.management.entity.anlyseCallBack;
import com.headlth.management.myview.MyToash;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;

import java.util.ArrayList;


public class AnalizeFragment extends BaseFragment {
    Gson g = new Gson();
    private ViewPager vPager;

    ArrayList<Fragment> fragmentsList;
    AnalizeEffectSportFragment frag1;
    AnalizeClFragment frag2;
    Activity activity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_analize, null);
        vPager = (ViewPager) view.findViewById(R.id.vPager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    private void initialize() {
        activity=getActivity();
        String year = DateUtil.getYear() + "-" + second(DateUtil.getMonth()) + "-" + second(DateUtil.getCurrentMonthDay());
        String anlysedata = ShareUitls.getString(getActivity(), "anlysedata", "");//str;//


        if(anlysedata.length()!=0){
            String analize= ShareUitls.getString(getActivity(),"analize","1");//分析界面是否重新刷新（运动完有新数据）
            if(analize.equals("1")){
                fenxi(year, 0);
            }else {
                anlyseCallBack anlyse = g.fromJson(anlysedata, anlyseCallBack.class);
                InitViewPager(anlyse);
            }
        }else {

            fenxi(year, 0);
        }
    }

    public TextView activity_main_title_left;

    public TextView activity_main_title_center;

    public TextView activity_main_title_right;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity_main_title_center = (TextView) getActivity().findViewById(R.id.activity_main_title_center);
        activity_main_title_right = (TextView) getActivity().findViewById(R.id.activity_main_title_right);
        activity_main_title_left = (TextView) getActivity().findViewById(R.id.activity_main_title_left);


    }

   private anlyseCallBack anlyse;

    @SuppressLint("ValidFragment")
    public AnalizeFragment(anlyseCallBack anlyse) {
        this.anlyse = anlyse;
    }
    public AnalizeFragment() {
    }
    private void InitViewPager(anlyseCallBack anlyse) {

        fragmentsList = new ArrayList<Fragment>();
        frag1 = new AnalizeEffectSportFragment(anlyse);
        frag2 = new AnalizeClFragment(anlyse);
        fragmentsList.add(frag1);
        fragmentsList.add(frag2);
        //跟换的画会有问题
        vPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentsList));
      /*  mPager.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(), fragmentsList));*/
        vPager.addOnPageChangeListener(new MyOnPageChangeListener());
        vPager.setOffscreenPageLimit(0);
        vPager.setCurrentItem(0);
    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            if (arg0== 0) {
                activity_main_title_left.setText("");
                activity_main_title_center.setText("有效运动");
                activity_main_title_right.setText("卡路里");

            } else {
                activity_main_title_left.setText("有效运动");
                activity_main_title_center.setText("卡路里");
                activity_main_title_right.setText("");
            }
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }



        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == 2) {
            }
        }


    }//新的叫PostNewAnalysisRequest GetPlanClassListRequest GetPlanNameListRequest     参数 UserID，PlanClassID
    private void fenxi(final String monthDay, final int i) {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostNewAnalysisRequest");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(getActivity(), "ResultJWT", "0"));
        params.addBodyParameter("UID", ShareUitls.getString(getActivity(), "UID", "0"));
        params.addBodyParameter("SportTime", monthDay);
        HttpUtils.getInstance(getActivity()).sendRequestRequestParams("", params,false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("hfhfhfh", response.toString());
                        try {
                            anlyseCallBack anlyse = g.fromJson(response.toString(), anlyseCallBack.class);
                            if (anlyse.getStatus() == 1) {
                                ShareUitls.putString(getActivity(),"analize","0");//分析界面是否重新刷新
                                ShareUitls.putString(getActivity(), "anlysedata", response);//
                                InitViewPager(anlyse);
                                return;
                            } else {
                                MyToash.Toash(activity, "暂无数据");
                            }
                        } catch (Exception j) {
                            MyToash.Toash(activity, "暂无数据");

                        }
                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                       // Log.i("AAAAAAAAA", "LoginupToken");
                        MyToash.Toash(activity, "请求失败");



                    }
                }

        );
    }
    public String second(int i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return "" + i;
        }
    }


    String  str="{\"Status\":1,\"Data\":{\"Detail\":[{\"Calory\":\"0\",\"Day\":\"周二\",\"EffectTime\":\"120\",\"StatDate\":\"2017-12-19\",\"TotalTime\":\"0\"},{\"Calory\":\"0\",\"Day\":\"周三\",\"EffectTime\":\"130\",\"StatDate\":\"2017-12-20\",\"TotalTime\":\"0\"},{\"Calory\":\"0\",\"Day\":\"周四\",\"EffectTime\":\"500\",\"StatDate\":\"2017-12-21\",\"TotalTime\":\"0\"},{\"Calory\":\"0\",\"Day\":\"周五\",\"EffectTime\":\"60\",\"StatDate\":\"2017-12-22\",\"TotalTime\":\"0\"},{\"Calory\":\"0\",\"Day\":\"周六\",\"EffectTime\":\"360\",\"StatDate\":\"2017-12-23\",\"TotalTime\":\"0\"},{\"Calory\":\"0\",\"Day\":\"周日\",\"EffectTime\":\"700\",\"StatDate\":\"2017-12-24\",\"TotalTime\":\"0\"},{\"Calory\":\"14\",\"Day\":\"周一\",\"EffectTime\":\"1559\",\"StatDate\":\"2017-12-25\",\"TotalTime\":\"1703\"}],\"Summary\":[{\"AvgEffectTime\":\"1559\",\"AvgTotalTime\":\"8160\",\"AvgCal\":\"2\",\"CaloryRate\":\"希望你，再接再厉\",\"MaxCalory\":\"100\",\"MaxTotalTime\":\"2520\",\"Percentage\":\"19.11%\",\"UID\":\"2045\",\"TotalCal\":\"14\",\"TotalDays\":\"0\"}]},\"Message\":\"获取成功!\",\"IsSuccess\":true,\"IsError\":false,\"ErrMsg\":null,\"ErrCode\":null,\"ResultJWT\":null}\n";
}


