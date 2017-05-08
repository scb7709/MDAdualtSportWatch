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
        return view;
    }

    TextView today;
    TextView first;
    TextView second;
    ImageButton bt_rili;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        today = (TextView) getActivity().findViewById(R.id.today);
        first = (TextView) getActivity().findViewById(R.id.statechange);
        second = (TextView) getActivity().findViewById(R.id.second);

        today.setTextSize(17);
        today.setTextColor(Color.parseColor("#3c3c3c"));
        today.setText("有效运动");
        first.setText("");
        second.setTextSize(13);
        second.setTextColor(Color.parseColor("#000000"));
        second.setText("卡路里");
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
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());
        vPager.setOffscreenPageLimit(0);
        vPager.setCurrentItem(0);
    }
    int i = 1;
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Log.e("onPageSelected", arg0 + "");
            if (i % 2 != 0) {
                today.setTextSize(13);
                today.setTextColor(Color.parseColor("#000000"));
                today.setText("有效运动");
                first.setText("");
                second.setTextSize(17);
                second.setTextColor(Color.parseColor("#3c3c3c"));
                second.setText("卡路里");
                i++;
            } else {
                today.setTextSize(17);
                today.setTextColor(Color.parseColor("#3c3c3c"));
                today.setText("有效运动");
                first.setText("");
                second.setTextSize(13);
                second.setTextColor(Color.parseColor("#000000"));
                second.setText("卡路里");
                i++;
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


