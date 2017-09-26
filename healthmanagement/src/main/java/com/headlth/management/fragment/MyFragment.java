package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.headlth.management.R;
import com.headlth.management.activity.AboutMaiDongActivity;
import com.headlth.management.activity.AccountManageActivity;
import com.headlth.management.activity.CompleteInformationActivity;
import com.headlth.management.activity.HealthDatumActivity;
import com.headlth.management.activity.MyDevicesActivity;
import com.headlth.management.activity.MyOrderActivity;
import com.headlth.management.activity.MyPrescriptionActivity;
import com.headlth.management.activity.MyShareActivity;

import com.headlth.management.entity.User;
import com.headlth.management.entity.VersionClass;
import com.headlth.management.myview.CircleImageView;

import com.headlth.management.service.UpdateService;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import com.headlth.management.utils.VersonUtils;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.Date;

/**
 * Created by abc on 2016/11/10.
 */
@ContentView(R.layout.fragment_my)
public class MyFragment extends BaseFragment {


    @ViewInject(R.id.fragmrnt_my_icon)
    private CircleImageView fragmrnt_my_icon;
    @ViewInject(R.id.fragmrnt_my_name)
    private TextView fragmrnt_my_name;
    @ViewInject(R.id.fragmrnt_my_allsporttime)
    private TextView fragmrnt_my_allsporttime;
    @ViewInject(R.id.fragmrnt_my_allvalidsporttime)
    private TextView fragmrnt_my_allvalidsporttime;

    @ViewInject(R.id.fragment_my_newversion)
    private TextView fragment_my_newversion;


    private String url;
    private View view;
    private boolean isupdate;
    private User user;
    private User.UserInformation userInformation;
    private String UID;
    VersionClass.Version version;

    @SuppressLint("ValidFragment")
    public MyFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        url = Constant.BASE_URL;
        version = ShareUitls.getVersion(getActivity());
        isupdate = version != null && VersonUtils.getVerisonCode(getActivity()) != -1 && version.VersionCode > VersonUtils.getVerisonCode(getActivity());
        UID = ShareUitls.getString(getActivity(), "UID", "");
        Log.i("strstrstrstr","onCreateView");
        return x.view().inject(this, inflater, container);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("strstrstrstr","onViewCreated");
        if (isupdate) {
            fragment_my_newversion.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onStart() {//修改资料后回到本界面需要刷新，因此放在onStart方法里
        super.onStart();
        if (!UID.equals("")) {
            user = ShareUitls.getUser(getActivity());
            try {
                userInformation = user.getUserInformation();
                fragmrnt_my_name.setText(userInformation.getNickName());
            }catch (NullPointerException n){}
        }
        String str = ShareUitls.getString(getActivity(), "my", "");//我界面是否需要刷新
        String strdata= ShareUitls.getString(getActivity(), "mydata", "");//

        if (str.length() != 0||strdata.length()==0) {

            getTotalTimeRequest();
        }else {
            setData(strdata);
        }

    }
    @Event(value = {R.id.fragmrnt_my_update,
            R.id.fragment_my_aboutus,
            R.id.fragment_my_health,
            R.id.fragment_my_updateversion,
            R.id.fragment_my_user,
            R.id.fragmrnt_my_order,
            R.id.fragmrnt_my_prescription,
            R.id.fragmrnt_my_share,
            R.id.fragment_my_mydevice
    })
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.fragmrnt_my_update:
                startActivity(new Intent(getActivity(), CompleteInformationActivity.class).putExtra("flag", "yes").putExtra("userInformation", "MY"));
                break;
            case R.id.fragment_my_aboutus:

                startActivity(new Intent(getActivity(), AboutMaiDongActivity.class));
                break;
            case R.id.fragment_my_mydevice:

                startActivity(new Intent(getActivity(), MyDevicesActivity.class));
                break;
            case R.id.fragment_my_health:
                startActivity(new Intent(getActivity(), HealthDatumActivity.class));
                break;
            case R.id.fragment_my_updateversion:

                if (isupdate) {
                   /* UpadteApp upadteApp = new UpadteApp(getActivity(), version, true, new UpadteApp.UpdateResult() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onError() {
                        }
                    });*/
                    Intent intent = new Intent(getActivity(), UpdateService.class);
                    intent.putExtra("apkUrl", version.DownloadUrl);
                    getActivity().startService(intent);
                } else {
                    Toast.makeText(getActivity(), "没有新版本可更新", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fragment_my_user:
                startActivity(new Intent(getActivity(), AccountManageActivity.class));
                break;
            case R.id.fragmrnt_my_order:
                startActivity(new Intent(getActivity(), MyOrderActivity.class));
                break;
            case R.id.fragmrnt_my_prescription:

                startActivity(new Intent(getActivity(), MyPrescriptionActivity.class));
                break;
            case R.id.fragmrnt_my_share:
                startActivity(new Intent(getActivity(), MyShareActivity.class));

                break;

        }
    }
    private void getTotalTimeRequest() {
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetTotalTimeRequest");

        params.addBodyParameter("UID", ShareUitls.getString(getActivity(), "UID", "") + "");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(getActivity(), "ResultJWT", "0"));
        HttpUtils.getInstance(getActivity()).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {


                        Log.i("aaaaaaaa", UID + "" + response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            int TotalSportTime = jsonObject.getInt("TotalSportTime");
                            int TotalVolidSportTime = jsonObject.getInt("TotalVolidSportTime");
                            String UserImageUrl = jsonObject.getString("UserImageUrl");
                            ShareUitls.putUserInformationFile(getActivity(), UserImageUrl);
                           // Log.i("SSSSSEEEAA", userInformation.getFile() + "   AAAA" + UserImageUrl);
                            fragmrnt_my_allsporttime.setText(getTime(TotalSportTime));
                            fragmrnt_my_allvalidsporttime.setText(getTime(TotalVolidSportTime));
                            //Constant.BASE_URL + "/" + userInformation.getFile();
                            Glide.with(getActivity())
                                    .load(Constant.BASE_URL + "/" + UserImageUrl)
                                    .asBitmap()
                                    .error(R.mipmap.icon_camera)
                                    .into(fragmrnt_my_icon);

                            ShareUitls.putString(getActivity(), "mydata", response);//
                            ShareUitls.putString(getActivity(), "my", "");//我界面是否需要刷新
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                    }
                }

        );

    }

    private void setData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int TotalSportTime = jsonObject.getInt("TotalSportTime");
            int TotalVolidSportTime = jsonObject.getInt("TotalVolidSportTime");
            String UserImageUrl = jsonObject.getString("UserImageUrl");
          //  Log.i("SSSSSEEEAA", userInformation.getFile() + "   AAAA" + UserImageUrl);
            fragmrnt_my_allsporttime.setText(getTime(TotalSportTime));
            fragmrnt_my_allvalidsporttime.setText(getTime(TotalVolidSportTime));
            Glide.with(getActivity())
                    .load(Constant.BASE_URL + "/" + UserImageUrl)
                    .asBitmap()
                    .error(R.mipmap.icon_camera)
                    .into(fragmrnt_my_icon);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getTime(int time) {
        int hours = time / (60 * 60);
        int minutes = time / 60 - hours * 60;
        int seconds = time - minutes * 60 - hours * 60 * 60;


        return hours + ":" + minutes + ":" + seconds;
    }


}
