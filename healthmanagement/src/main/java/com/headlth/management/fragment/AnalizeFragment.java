package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.adapter.MyFragmentPagerAdapter;
import com.headlth.management.clenderutil.DateUtil;
import com.headlth.management.entity.anlyseCallBack;
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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_analize, null);
        initialize(view);
        return view;
    }

    private void initialize(View view) {
        vPager = (ViewPager) view.findViewById(R.id.vPager);
        String year = DateUtil.getYear() + "-" + second(DateUtil.getMonth()) + "-" + second(DateUtil.getCurrentMonthDay());
        String anlysedata = ShareUitls.getString(getActivity(), "anlysedata", "");//


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
            Log.e("onPageSelected", arg0 + "");
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
                        Log.e("hfhfhfh", response.toString());
                        try {
                            anlyseCallBack anlyse = g.fromJson(response.toString(), anlyseCallBack.class);
                            if (anlyse.getStatus() == 1) {
                                ShareUitls.putString(getActivity(),"analize","0");//分析界面是否重新刷新
                                ShareUitls.putString(getActivity(), "anlysedata", response);//
                                InitViewPager(anlyse);
                                return;
                            } else {
                                Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception j) {
                            Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Log.i("AAAAAAAAA", "LoginupToken");

                        Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                        return;

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
}


