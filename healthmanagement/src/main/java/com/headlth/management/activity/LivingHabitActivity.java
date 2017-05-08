package com.headlth.management.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.acs.App;
import com.headlth.management.adapter.LivingHabitAdapter;
import com.headlth.management.entity.CircleList;
import com.headlth.management.entity.LivingHabitJson;
import com.headlth.management.entity.User;
import com.headlth.management.utils.Bimp;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ImageUtil;
import com.headlth.management.utils.ScreenShot;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.VersonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.List;

/**
 * Created by abc on 2016/9/20.
 */
@ContentView(R.layout.activity_livinghabit)
public class LivingHabitActivity extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;


    @ViewInject(R.id.activity_livinghabit_ListView)
    private ListView activity_livinghabit_ListView;
    private LivingHabitAdapter livingHabitAdapter;
    private int size;
    private Intent intent;
    private User.UserInformation userInformation;
    private static com.headlth.management.clenderutil.WaitDialog waitDialog;
    static Activity activity;
    private boolean commit;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        view_publictitle_title.setText("身体和生活习惯信息");
        activity = this;
        initDialog();
        intent = getIntent();
        userInformation = (User.UserInformation) intent.getSerializableExtra("userInformation");
        getLivingHabitHttp();

        view_publictitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activity_livinghabit_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("OOOOOOOOO", position + "");
                if (popupWindow.isShowing()) {
                    return;
                } else {
                    if (commit) {
                        maiDongGo(view);
                    } else {
                        if (position == size) {
                            List<LivingHabitJson.ListDetail.ListItems> DiseaseList = livingHabitAdapter.getDiseaseListList();
                            List<LivingHabitJson.ListDetail.ListItems> BadHabitList = livingHabitAdapter.getBadHabitListList();

                            JSONArray tempDiseaseContent = new JSONArray();
                            JSONArray tempHabitContent = new JSONArray();
                            for (LivingHabitJson.ListDetail.ListItems diseaseAndHabitContent : DiseaseList) {
                                tempDiseaseContent.put(getJSONObject(diseaseAndHabitContent.ID, diseaseAndHabitContent.IsChosen + ""));
                            }
                            for (LivingHabitJson.ListDetail.ListItems diseaseAndHabitContent : BadHabitList) {
                                tempHabitContent.put(getJSONObject(diseaseAndHabitContent.ID, diseaseAndHabitContent.IsChosen + ""));
                            }

                            userInformationHttp(tempDiseaseContent, tempHabitContent, view);
                        }
                    }
                    // livingHabitAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initDialog() {
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(activity);
        waitDialog.setCancleable(true);
        initPop();
    }

    private void initPop() {
        View view = LayoutInflater.from(LivingHabitActivity.this).inflate(R.layout.dialog_maidonggo, null);
        popupWindow = new PopupWindow(view, ImageUtil.dp2px(this, 300), ImageUtil.dp2px(this, 300), true);
        RelativeLayout go = (RelativeLayout) view.findViewById(R.id.dialog_maidonggo);
        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("RFVBGT", "ccccccccccc");
                startActivity(new Intent(LivingHabitActivity.this, MainActivity.class));
                popupWindow.dismiss();

                if(Login.activity!=null){
                    Login.activity.finish();
                }
                if(CompleteInformationActivity.activity!=null){
                    CompleteInformationActivity.activity.finish();
                }
                finish();
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
    }

    private void maiDongGo(View v) {
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    private void userInformationHttp(final JSONArray tempDiseaseContent, final JSONArray tempHabitContent, final View view) {
        Log.i("userInformationAAAA", tempDiseaseContent + "   " + tempHabitContent);
        waitDialog.showDailog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostUserInfoUpdateRequest");

        params.addBodyParameter("UID", ShareUitls.getString(LivingHabitActivity.this, "UID", ""));
        params.addBodyParameter("ResultJWT", ShareUitls.getString(LivingHabitActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("NickName", userInformation.getNickName());
        params.addBodyParameter("Birthday", userInformation.getBirthday());
        params.addBodyParameter("Weight", userInformation.getWeight());
        params.addBodyParameter("Height", userInformation.getHeight());
        params.addBodyParameter("Gender", userInformation.getGender());
        params.addBodyParameter("VersionNum", VersonUtils.getVersionName(this));
        ;
        params.addBodyParameter("Illness", tempDiseaseContent.toString());
        params.addBodyParameter("Badhabit", tempHabitContent.toString());


        if (userInformation.getFile() != null) {

            String pictime = System.currentTimeMillis() + "";
            ScreenShot.saveMyBitmap(Bimp.getSmallBitmap(userInformation.getFile()), pictime, false, LivingHabitActivity.this);
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/image/" + pictime + ".png";
            params.addBodyParameter("File", new File(path), "image/png");

        }
        Log.i("userInformation", userInformation.toString());
        Log.i("jjjjjjjjjj", tempDiseaseContent.toString() + "   " + tempHabitContent.toString());
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().post(params,
                new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.i("userInformationAA", "" + result.toString());
                        //   startActivity(new Intent(LivingHabitActivity.this, TEXT.class).putExtra("jsonObject1",ShareUitls.getString(LivingHabitActivity.this, "UID", "")+"   "+userInformation.toString()+"    "+tempDiseaseContent.toString() + "   " + tempHabitContent.toString()+"  "+result.toString()));
                        try {
                            JSONObject jsonObject = new JSONObject(result.toString());
                            if (jsonObject.getString("ErrCode").equals("601") || jsonObject.getString("ErrCode").equals("600")) {
                                if (jsonObject.getString("ErrCode").equals("601")) {
                                    Toast.makeText(LivingHabitActivity.this, "您的账号已在其他设备登录", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LivingHabitActivity.this, "您的登录信息已过期", Toast.LENGTH_LONG).show();
                                }
                                Intent i = new Intent(LivingHabitActivity.this, Login.class);
                                startActivity(i);
                                finish();
                                //清除所有文件数据
                                ShareUitls.cleanSharedPreference(LivingHabitActivity.this);
                            } else {
                                String Status = jsonObject.getString("Status");
                                if (Status.equals("1")) {
                                    User.UserInformation userInformationn = new User().getUserInformation();
                                    userInformationn.setNickName(userInformation.getNickName());
                                    userInformationn.setHeight(userInformation.getHeight());
                                    userInformationn.setWeight(userInformation.getWeight());
                                    userInformationn.setBirthday(userInformation.getBirthday());
                                    userInformationn.setGender(userInformation.getGender());
                                    userInformationn.setFile(userInformation.getFile());
                                    ShareUitls.putUserInformation(LivingHabitActivity.this, userInformationn);
                                    commit = true;
                                    view_publictitle_back.setVisibility(View.GONE);
                                    maiDongGo(view);
                                } else {
                                    Toast.makeText(LivingHabitActivity.this, "信息上传失败", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(LivingHabitActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                        } else { // 其他错误
                            Toast.makeText(LivingHabitActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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

    Gson gson = new Gson();

    private void getLivingHabitHttp() {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetDiseaseAndBadHabitRequest");
        params.addBodyParameter("UID", ShareUitls.getString(LivingHabitActivity.this, "UID", ""));
        params.addBodyParameter("ResultJWT", ShareUitls.getString(LivingHabitActivity.this, "ResultJWT", "0"));
        HttpUtils.getInstance(LivingHabitActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getLivingHabitHttp", response);
                        LivingHabitJson livingHabitJson = gson.fromJson(response, LivingHabitJson.class);
                        Log.e("getLivingHabitHttpAA", livingHabitJson.toString());
                        if (livingHabitJson.IsSuccess.equals("true")) {
                            for (int i = 0; i < livingHabitJson.ListDetail.size(); i++) {
                                size += livingHabitJson.ListDetail.get(i).ListItems.size();
                            }
                            size += 2;
                            livingHabitAdapter = new LivingHabitAdapter(livingHabitJson.ListDetail.get(0).ListItems, livingHabitJson.ListDetail.get(1).ListItems, LivingHabitActivity.this);


                            activity_livinghabit_ListView.setAdapter(livingHabitAdapter);

                        } else {
                            Toast.makeText(LivingHabitActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

        );


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {

            if (popupWindow.isShowing()) {
                // maiDongGo(new View(LivingHabitActivity.this));
            } else {
                if (commit) {
                    maiDongGo(new View(this));
                } else {
                    finish();
                }
            }


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private JSONObject getJSONObject(String ID, String ischoose) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ID", ID);
            jsonObject.put("answer", ischoose);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
