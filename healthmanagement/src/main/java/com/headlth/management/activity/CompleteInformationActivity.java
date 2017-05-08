package com.headlth.management.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.headlth.management.R;
import com.headlth.management.acs.App;
import com.headlth.management.datepickerview.TimePickerView;
import com.headlth.management.entity.CircleList;
import com.headlth.management.entity.User;
import com.headlth.management.myview.BottomMenuDialog;
import com.headlth.management.myview.CircleImageView;
import com.headlth.management.myview.HeightChooseDialog;
import com.headlth.management.utils.Bimp;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ImageUtil;
import com.headlth.management.utils.MiPictureHelper;
import com.headlth.management.utils.ScreenShot;
import com.headlth.management.utils.ShareUitls;
import com.headlth.management.utils.VersonUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by abc on 2016/9/13.
 */
@ContentView(R.layout.activity_completeinformation)
public class CompleteInformationActivity extends BaseActivity {
    @ViewInject(R.id.activity_completeinformation_name_layout)
    private LinearLayout activity_completeinformation_name_layout;
    @ViewInject(R.id.activity_completeinformation_height_layout)
    private LinearLayout activity_completeinformation_height_layout;
    @ViewInject(R.id.activity_completeinformation_birthday_layout)
    private LinearLayout activity_completeinformation_birthday_layout;
    @ViewInject(R.id.activity_completeinformation_weight_layout)
    private LinearLayout activity_completeinformation_weight_layout;

    @ViewInject(R.id.activity_completeinformation_back)
    private RelativeLayout activity_completeinformation_back;

    @ViewInject(R.id.activity_completeinformation_save)
    private TextView activity_completeinformation_save;


    @ViewInject(R.id.activity_completeinformation_sex_man)
    private Button activity_completeinformation_sex_man;
    @ViewInject(R.id.activity_completeinformation_sex_woman)
    private Button activity_completeinformation_sex_woman;
    @ViewInject(R.id.activity_completeinformation_bt_commit)
    private Button activity_completeinformation_bt_commit;

    @ViewInject(R.id.activity_completeinformation_name)
    private TextView activity_completeinformation_name;
    @ViewInject(R.id.activity_completeinformation_height)
    private TextView activity_completeinformation_height;
    @ViewInject(R.id.activity_completeinformation_birthday)
    private TextView activity_completeinformation_birthday;
    @ViewInject(R.id.activity_completeinformation_weight)
    private TextView activity_completeinformation_weight;

    @ViewInject(R.id.activity_completeinformation_title)
    private TextView activity_completeinformation_title;

    @ViewInject(R.id.activity_completeinformation_icon)
    private CircleImageView activity_completeinformation_icon;

  /*  @ViewInject(R.id.activity_completeinformation_icon_bt)
    private Button activity_completeinformation_icon_bt;*/


    private String sex = "1";
    private TimePickerView pvTime;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private List<String> height_data;
    private List<String> weight_data1;
    private List<String> weight_data2;
    private String temp_weight1 = "";
    private String temp_weight2 = "";
    private HeightChooseDialog heightChooseDialog;
    private BottomMenuDialog bottomMenuDialog;
    private Date d;

    private String path = "";//头像的本地地址
    private boolean nameIsExist;
    private PopupWindow popupWindow;
    private boolean ismy;//是否是我的界面 跳过来的
    String headimgurl, nickname;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        activity = this;
        setDate();
    }

    private void setDate() {
        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");//有初始信息传过来（三方账号登录和我的界面修改资料）
        if (flag.equals("yes")) {

            String str = intent.getStringExtra("userInformation");
            User user = ShareUitls.getUser(CompleteInformationActivity.this);
            User.UserInformation userInformation = user.getUserInformation();

            if (str != null) {//我界面点修改资料跳转而来
                activity_completeinformation_bt_commit.setVisibility(View.GONE);
                activity_completeinformation_title.setText("修改资料");
                ismy = true;//我界面跳转过来
                headimgurl = Constant.BASE_URL + "/" + userInformation.getFile();
                nickname = userInformation.getNickName();
                sex = userInformation.getGender();
                activity_completeinformation_weight.setText(userInformation.getWeight());
                activity_completeinformation_height.setText(userInformation.getHeight());
                Log.i("LLLLLLLA", "" + userInformation.getBirthday());

                //String Birthday = userInformation.getBirthday().replace("/", "-").substring(0, userInformation.getBirthday().length() - 8);
                String Birthday = userInformation.getBirthday();
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Birthday = format.format(new Date(Birthday));
                } catch (IllegalArgumentException i) {

                }
                activity_completeinformation_birthday.setText(Birthday);

                //path = userInformation.getFile();
                //  activity_completeinformation_bt_commit.setText("提交");


            } else {

                headimgurl = intent.getStringExtra("headimgurl");
                nickname = intent.getStringExtra("nickname");
                sex = intent.getStringExtra("sex");
            }


            activity_completeinformation_name.setText(nickname);

            if (sex.equals("男") || sex.equals("m") || sex.equals("1")) {
                sex = "1";
                activity_completeinformation_sex_man.setBackgroundResource(R.mipmap.icon_sex_man);
                activity_completeinformation_sex_woman.setBackgroundResource(R.mipmap.icon_sex);
                activity_completeinformation_sex_man.setTextColor(Color.parseColor("#2385e0"));
                activity_completeinformation_sex_woman.setTextColor(Color.parseColor("#aaaaaa"));

            } else if (sex.equals("女") || sex.equals("w") || sex.equals("2")) {
                sex = "2";
                activity_completeinformation_sex_woman.setBackgroundResource(R.mipmap.icon_sex_woman);
                activity_completeinformation_sex_man.setBackgroundResource(R.mipmap.icon_sex);
                activity_completeinformation_sex_woman.setTextColor(Color.parseColor("#f259d7"));
                activity_completeinformation_sex_man.setTextColor(Color.parseColor("#aaaaaa"));
            }

            Log.i("ccccccccccc", headimgurl);
            Glide.with(CompleteInformationActivity.this)
                    .load(headimgurl)
                    .asBitmap()
                    .skipMemoryCache(true)
                    .override(100, 100)
                    .error(R.mipmap.icon_camera)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (bitmap != null) {
                                activity_completeinformation_icon.setImageBitmap(bitmap);
                                if (!ismy) {//如果不是从我界面跳转过来（三方账号注册跳转而来）这需要下载三方头像
                                    String pictime = System.currentTimeMillis() + "";
                                    path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/image/" + pictime + ".png";
                                    ScreenShot.saveMyBitmap(bitmap, pictime, false, CompleteInformationActivity.this);
                                }
                            }

                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                        }

                    });
        } else {

        }
        height_data = new ArrayList<String>();
        weight_data1 = new ArrayList<String>();
        weight_data2 = new ArrayList<String>();

        for (int i = 100; i < 211; i++) {
            height_data.add(i + "");
        }
        for (int i = 30; i < 201; i++) {
            weight_data1.add(i + "");
        }
        for (int i = 0; i < Constant.WEIGHT2.length; i++) {
            weight_data2.add(Constant.WEIGHT2[i]);
        }
        temp_weight1 = weight_data1.get(weight_data1.size() / 2);
        temp_weight2 = weight_data2.get(weight_data2.size() / 2);


    }

    @Event(value = {
            R.id.activity_completeinformation_back, R.id.activity_completeinformation_sex_man,
            R.id.activity_completeinformation_sex_woman, R.id.activity_completeinformation_bt_commit,
            R.id.activity_completeinformation_name_layout, R.id.activity_completeinformation_height_layout,
            R.id.activity_completeinformation_birthday_layout, R.id.activity_completeinformation_weight_layout,
            R.id.activity_completeinformation_icon, R.id.activity_completeinformation_save
    })

    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.activity_completeinformation_back:
                if (!ismy) {
                    startActivity(new Intent(CompleteInformationActivity.this, Login.class));
                }
                finish();
                break;
            case R.id.activity_completeinformation_sex_man:
                sex = "1";
                activity_completeinformation_sex_man.setBackgroundResource(R.mipmap.icon_sex_man);
                activity_completeinformation_sex_woman.setBackgroundResource(R.mipmap.icon_sex);
                activity_completeinformation_sex_man.setTextColor(Color.parseColor("#2385e0"));
                activity_completeinformation_sex_woman.setTextColor(Color.parseColor("#aaaaaa"));
                if (ismy) {
                    activity_completeinformation_save.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.activity_completeinformation_sex_woman:
                if (ismy) {
                    activity_completeinformation_save.setVisibility(View.VISIBLE);
                }
                sex = "2";
                activity_completeinformation_sex_woman.setBackgroundResource(R.mipmap.icon_sex_woman);
                activity_completeinformation_sex_man.setBackgroundResource(R.mipmap.icon_sex);
                activity_completeinformation_sex_woman.setTextColor(Color.parseColor("#f259d7"));
                activity_completeinformation_sex_man.setTextColor(Color.parseColor("#aaaaaa"));
                break;

            case R.id.activity_completeinformation_bt_commit:
                //&& activity_completeinformation_weight.length() != 0 && activity_completeinformation_height.length() != 0 && activity_completeinformation_birthday.length() != 0
                String username = activity_completeinformation_name.getText().toString();
                if (username.length() >= 3 && username.length() <= 20) {

                    if (!username.matches("[\u4e00-\u9fa5A-Za-z0-9_-]+")) {
                        Toast.makeText(CompleteInformationActivity.this, "昵称只能由汉子、数字、字母、下划线、-号组成", Toast.LENGTH_LONG).show();
                    } else {
                        if (ismy) {
                            next();

                        } else {

                            if (nameIsExist) {
                                next();
                            } else {
                                nameIsExist(username);
                            }
                        }

                    }
                } else {
                    Toast.makeText(CompleteInformationActivity.this, "昵称只能输入3-20位字符", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.activity_completeinformation_name_layout:
                setUserName(activity_completeinformation_name_layout);
                if (ismy) {
                    activity_completeinformation_save.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.activity_completeinformation_height_layout:
                if (ismy) {
                    activity_completeinformation_save.setVisibility(View.VISIBLE);
                }
                heightChooseDialog = new HeightChooseDialog.Builder(this).create(true, activity_completeinformation_height, activity_completeinformation_height.getText().toString());
                heightChooseDialog.show();
                // weightAndHeightPick(true);

                break;

            case R.id.activity_completeinformation_birthday_layout:
                if (ismy) {
                    activity_completeinformation_save.setVisibility(View.VISIBLE);
                }
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    d = df.parse("2000-01-01");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
                pvTime.setTime(d);
                pvTime.setCyclic(false);
                pvTime.setCancelable(true);
                pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        activity_completeinformation_birthday.setText(format.format(date));
                    }
                });
                pvTime.show();
                break;
            case R.id.activity_completeinformation_weight_layout:
                if (ismy) {
                    activity_completeinformation_save.setVisibility(View.VISIBLE);
                }
                heightChooseDialog = new HeightChooseDialog.Builder(this).create(false, activity_completeinformation_weight, activity_completeinformation_weight.getText().toString());
                heightChooseDialog.show();
                // weightAndHeightPick(false);
                break;
            case R.id.activity_completeinformation_icon:
                if (ismy) {
                    activity_completeinformation_save.setVisibility(View.VISIBLE);
                }
                openPhotoPop();
                break;

            case R.id.activity_completeinformation_save:
                String usernam = activity_completeinformation_name.getText().toString();
                if (usernam.length() >= 3 && usernam.length() <= 20) {

                    if (!usernam.matches("[\u4e00-\u9fa5A-Za-z0-9_-]+")) {
                        Toast.makeText(CompleteInformationActivity.this, "昵称只能由汉子、数字、字母、下划线、-号组成", Toast.LENGTH_LONG).show();
                    } else {
                        if (ismy) {
                            next();
                        }
                    }
                } else {
                    Toast.makeText(CompleteInformationActivity.this, "昵称只能输入3-20位字符", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    private void next() {
        String NickName = activity_completeinformation_name.getText().toString();
        String Height = activity_completeinformation_height.getText().toString();
        String Birthday = activity_completeinformation_birthday.getText().toString();
        String Weight = activity_completeinformation_weight.getText().toString();
        User user = new User();
        User.UserInformation userInformation = user.getUserInformation();
        userInformation.setNickName(NickName);
        userInformation.setWeight(Weight);
        userInformation.setHeight(Height);
        userInformation.setGender(sex);
        userInformation.setBirthday(Birthday);
        if (path.length() != 0) {
            userInformation.setFile(path);
        }
        if (ismy) {
            userInformationHttp(userInformation);
        } else {
            Log.i("userInformationAAAA", userInformation.toString());
            Intent intent = new Intent(this, LivingHabitActivity.class);
            intent.putExtra("userInformation", userInformation);
            // Log.i("userInformationAAA",path);
            startActivity(intent);
        }
    }

    private void openPhotoPop() {
        bottomMenuDialog = new BottomMenuDialog.Builder(CompleteInformationActivity.this)
                .addMenu("拍照", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intentt, 0);
                        bottomMenuDialog.dismiss();
                    }
                }).addMenu("相册", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, 1);
                        bottomMenuDialog.dismiss();

                    }
                }).create();

        bottomMenuDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {//拍照
            if (resultCode == Activity.RESULT_OK && data != null) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");


                //activity_completeinformation_icon_bt.setVisibility(View.GONE);
                // activity_completeinformation_icon.setVisibility(View.VISIBLE);
                activity_completeinformation_icon.setImageBitmap(bitmap);

                String pictime = System.currentTimeMillis() + "";
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/image/" + pictime + ".png";
                ScreenShot.saveMyBitmap(bitmap, pictime, false, CompleteInformationActivity.this);

            } else {
                Toast.makeText(CompleteInformationActivity.this, "没有选中图片", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 1) {//相册选图
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri uri = data.getData();
                String pickPath = MiPictureHelper.getPath(CompleteInformationActivity.this, uri);  // 获取图片路径的方法调用

              /*  Cursor cursor = CompleteInformationActivity.this.getContentResolver().query(data.getData(), null, null, null, null);
               cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                File file = new File(cursor.getString(idx));*/


                // Bitmap bitmap = BitmapFactory.decodeFile(pickPath);
                Bitmap bitmap = Bimp.getSmallBitmap(pickPath);
               /* String pictime = System.currentTimeMillis() + "";
                ScreenShot.saveMyBitmap( Bimp.getSmallBitmap(path), pictime, false, CompleteInformationActivity.this);
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/" + pictime + ".png";*/


                //activity_completeinformation_icon_bt.setVisibility(View.GONE);
                // activity_completeinformation_icon.setVisibility(View.VISIBLE);
                activity_completeinformation_icon.setImageBitmap(bitmap);


                String pictime = System.currentTimeMillis() + "";
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/image/" + pictime + ".png";

                ScreenShot.saveMyBitmap(bitmap, pictime, false, CompleteInformationActivity.this);

                // cursor.close();//cursor查询完之后要关掉
            }

        }
    }

    //输入昵称
    private void setUserName(View v) {
        View view = LayoutInflater.from(CompleteInformationActivity.this).inflate(R.layout.dialog_setusername, null);
        popupWindow = new PopupWindow(view, ImageUtil.dp2px(this, 300), ImageUtil.dp2px(this, 230), true);
        Button cancle = (Button) view.findViewById(R.id.dialog_setusername_cancle);
        Button ok = (Button) view.findViewById(R.id.dialog_setusername_ok);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_setusername_edittext);
        String name = activity_completeinformation_name.getText().toString();
        if (name.length() != 0) {
            editText.setText(name);
        }
        cancle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = editText.getText().toString();
                if (username.length() < 3 || username.length() > 20) {
                    Toast.makeText(CompleteInformationActivity.this, "昵称只能输入3-20位字符", Toast.LENGTH_LONG).show();
                } else {
                    if (!username.matches("[\u4e00-\u9fa5A-Za-z0-9_-]+")) {
                        Toast.makeText(CompleteInformationActivity.this, "昵称只能由汉子、数字、字母、下划线、-号组成", Toast.LENGTH_LONG).show();
                    } else {
                        nameIsExist(username);
                    }
                }

            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return false;
            }
        });


        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


    }

    private void nameIsExist(final String NickName) {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=CheckExistRequest");
        params.addBodyParameter("Mobile", "0");
        params.addBodyParameter("NickName", NickName);
        params.addBodyParameter("UID", ShareUitls.getString(CompleteInformationActivity.this, "UID", "") + "");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(CompleteInformationActivity.this, "ResultJWT", "0"));
        HttpUtils.getInstance(CompleteInformationActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getSMSjson", response);
                        String Status = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Status = jsonObject.getString("Status");
                            if (Status.equals("1")) {
                                Toast.makeText(CompleteInformationActivity.this, "昵称已存在", Toast.LENGTH_SHORT).show();
                            } else {
                                if (popupWindow != null) {
                                    nameIsExist = true;
                                    activity_completeinformation_name.setText(NickName);
                                    popupWindow.dismiss();
                                } else {
                                    next();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {
                        Toast.makeText(getApplicationContext(), "获取获取码失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        nameIsExist = false;
    }

    private static com.headlth.management.clenderutil.WaitDialog waitDialog;

    private void userInformationHttp(final User.UserInformation userInformation) {
        Log.i("userInformationAAAA", userInformation.toString());
        waitDialog = new com.headlth.management.clenderutil.WaitDialog(CompleteInformationActivity.this);
        waitDialog.setMessage("正在上传,请稍后...");
        waitDialog.setCancleable(true);
        waitDialog.showDailog();
        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostUserInfoUpdateRequest");
        params.addBodyParameter("UID", ShareUitls.getString(CompleteInformationActivity.this, "UID", "") + "");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(CompleteInformationActivity.this, "ResultJWT", "0"));
        params.addBodyParameter("NickName", userInformation.getNickName());
        params.addBodyParameter("Birthday", userInformation.getBirthday());
        params.addBodyParameter("Weight", userInformation.getWeight());
        params.addBodyParameter("Height", userInformation.getHeight());
        params.addBodyParameter("Gender", userInformation.getGender());
        params.addBodyParameter("VersionNum", VersonUtils.getVersionName(this));
        ;
        Log.i("userInformationSSS", "" + path.length());
        if (path.length() != 0) {//从我的界面过来 而且头像没更改//        二次压缩
            String pictime = System.currentTimeMillis() + "";
            ScreenShot.saveMyBitmap(Bimp.getSmallBitmap(path), pictime, false, CompleteInformationActivity.this);
            final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maidong/image/" + pictime + ".png";

            Log.i("userInformationSSSAA", "" + path.length());
            params.addBodyParameter("File", new File(path), "image/png");
        }
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().post(params,
                new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.i("userInformationAA", "" + result.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(result.toString());
                            if (jsonObject.getString("ErrCode").equals("601") || jsonObject.getString("ErrCode").equals("600")) {
                                if (jsonObject.getString("ErrCode").equals("601")) {
                                    Toast.makeText(CompleteInformationActivity.this, "您的账号已在其他设备登录", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(CompleteInformationActivity.this, "您的登录信息已过期", Toast.LENGTH_LONG).show();
                                }
                                Intent i = new Intent(CompleteInformationActivity.this, Login.class);
                                CompleteInformationActivity.this.startActivity(i);
                             /*   if(MainActivity.Activity!=null){
                                    MainActivity.Activity.finish();
                                }*/
                                //清除所有文件数据
                                ShareUitls.cleanSharedPreference(CompleteInformationActivity.this);
                                //清空圈子
                                CircleList.getInstance().circlelist.clear();
                                CircleList.getInstance().commentlist.clear();
                                CircleList.getInstance().replylist.clear();
                                Glide.get(CompleteInformationActivity.this).clearMemory();
                                finish();
                            } else {
                                String Status = jsonObject.getString("Status");
                                if (Status.equals("1")) {
                                    //File path=new File();
                                    User.UserInformation userInformationn = new User().getUserInformation();
                                    userInformationn.setNickName(userInformation.getNickName());
                                    userInformationn.setHeight(userInformation.getHeight());
                                    userInformationn.setWeight(userInformation.getWeight());
                                    userInformationn.setBirthday(userInformation.getBirthday());
                                    userInformationn.setGender(userInformation.getGender());
                                    ShareUitls.putUserInformation(CompleteInformationActivity.this, userInformationn);
                                    if (path.length() != 0) {//
                                        ShareUitls.putString(CompleteInformationActivity.this, "my", "1");//头像更改 我界面否需要刷新
                                    }
                                    finish();
                                } else {
                                    Toast.makeText(CompleteInformationActivity.this, "信息更新失败", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(CompleteInformationActivity.this, "网络异常", Toast.LENGTH_SHORT).show();

                        } else { // 其他错误
                            Toast.makeText(CompleteInformationActivity.this, "信息更新失败", Toast.LENGTH_SHORT).show();
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {


            new AlertDialog.Builder(CompleteInformationActivity.this).setTitle("系统提示")//设置对话框标题

                    .setMessage("确定放弃当前编辑的信息吗?")//设置显示的内容

                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        @Override

                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            if (!ismy) {
                                startActivity(new Intent(CompleteInformationActivity.this, Login.class));
                            }
                            finish();

                        }

                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮


                @Override

                public void onClick(DialogInterface dialog, int which) {//响应事件

                }

            }).show();//在按键响应事件中显示此对话框
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
