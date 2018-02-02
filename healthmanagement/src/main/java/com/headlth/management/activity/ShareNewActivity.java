package com.headlth.management.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.headlth.management.R;
import com.headlth.management.ShareImageUtils.ImageWork;
import com.headlth.management.acs.BaseActivity;
import com.headlth.management.clenderutil.WaitDialog;
import com.headlth.management.entity.CircleList;
import com.headlth.management.myview.MGridView;
import com.headlth.management.myview.MyToash;
import com.headlth.management.utils.Bimp;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.ImageUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import photopicker.PhotoPickerActivity;
import photopicker.PhotoPreviewActivity;
import photopicker.SelectModel;
import photopicker.intent.PhotoPickerIntent;

/**
 * Created by abc on 2016/8/1.
 */
@ContentView(R.layout.activity_share)
public class ShareNewActivity extends BaseActivity {

    @ViewInject(R.id.share_cancel)
    private Button share_cancel;
    @ViewInject(R.id.share_send)
    private Button share_send;

    @ViewInject(R.id.share_shareText)
    private EditText share_shareText;

    @ViewInject(R.id.share_noScrollgridview)
    private MGridView noScrollgridview;
    private GridAdapter gridAdapter;


    public static Activity activity;


    private static WaitDialog waitDialog;
    private Intent intent;


    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;
    private ArrayList<String> imagePaths;
    private String SportIMage = "";
    private Drawable add;
    private Resources resources;
    private RequestParams params;
    private boolean StartcompressImage;
    private boolean ClickShare;
    private Map<String, String> compressImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initialize();
    }

    private void initialize() {
        imagePaths = new ArrayList<>();
        compressImage = new HashMap<>();
        activity = this;
        intent = getIntent();
        SportIMage = getIntent().getStringExtra("pictime");//运动完后传过来的截图
        add = ContextCompat.getDrawable(this, R.mipmap.pic_add);
        resources = getResources();
        if (!SportIMage.equals("")) {
            imagePaths.add(SportIMage);
        }
        imagePaths.add("000000");

        Init();
    }

    private static void initDialog() {
        waitDialog = new WaitDialog(activity);
        waitDialog.setCancleable(true);
        waitDialog.setMessage("正在分享,请稍后...");
    }

    public void Init() {
        initDialog();
        initRequestParams();
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        noScrollgridview.setHorizontalSpacing(ImageUtil.dp2px(activity, 3));

        gridAdapter = new GridAdapter(imagePaths);
        noScrollgridview.setAdapter(gridAdapter);
    }

    @Event(value = {R.id.share_cancel, R.id.share_send})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.share_cancel:
                cancelShare();
                break;

            case R.id.share_send:
                share();


                break;
        }
    }

    private void share() {
        MyToash.Log("waitDialog1==" + imagePaths.size());
        if (share_shareText.getText().toString().length() == 0 && imagePaths.size() == 1) {
            Toast.makeText(activity, "还未添加任何内容", Toast.LENGTH_LONG).show();
        } else {
            waitDialog.showDailog();

            if (share_shareText.getText().toString().length() == 0) {
                params.addBodyParameter("ShareContent", " ");
            } else {
                params.addBodyParameter("ShareContent", share_shareText.getText().toString());
            }

            if (imagePaths.size() != 0) {
                MyToash.Log("进入压缩线程3" + StartcompressImage + "  " + ClickShare + "  " + compressImage.size());
                if (!StartcompressImage) {
                   /* int i = 0;
                    for (final String path : compressImage.keySet()) {
                        if (imagePaths.contains(path)) {
                            MyToash.Log(path);
                            params.addBodyParameter("file" + (i++), new File(compressImage.get(path)), "image/png");
                        }
                    }*/
                    int i = 0;
                    for (final String path : imagePaths) {
                        if (compressImage.keySet().contains(path)) {
                            MyToash.Log(path);
                            params.addBodyParameter("file" + (i++), new File(compressImage.get(path)), "image/png");
                        }
                    }

                    send();
                } else {
                    ClickShare = true;
                }

            } else {
                send();
            }

        }
    }

    private void initRequestParams() {
        params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostShareContentRequest");
        params.setMultipart(true);
        params.addBodyParameter("UID", ShareUitls.getString(activity, "UID", "null"));
        params.addBodyParameter("ResultJWT", ShareUitls.getString(this, "ResultJWT", "0"));
        params.addBodyParameter("VersionNum", VersonUtils.getVersionName(this));
        if (!SportIMage.equals("")) {
            MyToash.Log("进入" + SportIMage);
            String pictime = System.currentTimeMillis() + "";
            Bimp.saveBitmap(SportIMage, pictime, new Bimp.OnSaveSuccessListener() {
                @Override
                public void onSuccess(String filepath) {
                    // MyToash.Log("进入压缩线程4" + pictime + "    " + filepath);
                    compressImage.put(SportIMage, filepath);
                }
            });
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // MyToash.Log("进入压缩线程2" + StartcompressImage + "  " + ClickShare);
            /*int i = 0;
            for (final String path : compressImage.keySet()) {
                if (imagePaths.contains(path)) {
                    MyToash.Log(path);
                    params.addBodyParameter("file" + (i++), new File(compressImage.get(path)), "image/png");
                }
            }*/

            int i = 0;
            for (final String path : imagePaths) {
                if (compressImage.keySet().contains(path)) {
                    MyToash.Log(path);
                    params.addBodyParameter("file" + (i++), new File(compressImage.get(path)), "image/png");
                }
            }

            send();
        }
    };
    Runnable compressRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (activity.getClass()) {
                MyToash.Log("进入压缩线程1" + StartcompressImage + "  " + ClickShare);
                if (!StartcompressImage) {

                    StartcompressImage = true;
                    for (final String path : imagePaths) {
                        if (path != null && !path.equals("000000") && !compressImage.containsKey(path)) {
                            String pictime = System.currentTimeMillis() + "";
                            Bimp.saveBitmap(path, pictime, new Bimp.OnSaveSuccessListener() {
                                @Override
                                public void onSuccess(String filepath) {
                                    MyToash.Log("进入压缩线程4" + path + "    " + filepath);
                                    compressImage.put(path, filepath);
                                }
                            });
                        }

                    }
                    StartcompressImage = false;
                    if (ClickShare) {
                        handler.sendEmptyMessage(0);
                    }
                }
            }

        }

    };

    private void send() {
        Callback.Cancelable cancelable = x.http().post(params,
                new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ClickShare = false;

                        Log.i("QQQQQQQQQonSuccess", "" + result.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(result.toString());


                            if (jsonObject.getString("ErrCode").equals("601") || jsonObject.getString("ErrCode").equals("600") || jsonObject.getString("ErrCode").equals("602")) {
                                if (jsonObject.getString("ErrCode").equals("601")) {
                                    Toast.makeText(activity, "您的账号已在其他设备登录", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(activity, "您的登录信息已过期", Toast.LENGTH_LONG).show();
                                }
                                Intent i = new Intent(activity, Login.class);
                                activity.startActivity(i);
                                ShareUitls.cleanSharedPreference(activity);
                                CircleList.getInstance().circlelist.clear();
                                CircleList.getInstance().commentlist.clear();
                                CircleList.getInstance().replylist.clear();
                                finish();
                            } else {

                                if (jsonObject.getString("IsSuccess").equals("true")) {
                                    Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
                                    setResult(258);
                                    finish();

                                } else {
                                    Toast.makeText(activity, "分享失败", Toast.LENGTH_SHORT).show();
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //LoadingActivity.activity.finish();
                        waitDialog.dismissDialog();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ClickShare = false;
                        //Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        if (ex instanceof HttpException) { // 网络错误
                            Toast.makeText(activity, "网络异常", Toast.LENGTH_SHORT).show();

                        } else { // 其他错误
                            Toast.makeText(activity, "分享失败", Toast.LENGTH_SHORT).show();
                        }
                        waitDialog.dismissDialog();
                        //LoadingActivity.activity.finish();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        ClickShare = false;
                    }
                });
    }

    private void cancelShare() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage("是否取消分享？")//设置显示的内容
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        dialog.dismiss();
                        finish();
                        if (!getIntent().getStringExtra("pictime").equals("")) {
                            new File(getIntent().getStringExtra("pictime")).delete();
                        }

                    }

                }).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    //  Log.d(TAG, "list: " + "list = [" + list.size());
                    loadAdpater(list);
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    //  Log.d(TAG, "ListExtra: " + "ListExtra = [" + ListExtra.size());
                    loadAdpater(ListExtra);
                    break;
            }
        }
    }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths != null && imagePaths.size() > 0) {
            imagePaths.clear();
        }
        if (paths.contains("000000")) {
            paths.remove("000000");
        }
        imagePaths.addAll(paths);
        if (imagePaths.size() < 9) {
            imagePaths.add("000000");
        }
        if (imagePaths.size() >= 2) {
            handler.post(compressRunnable);
        }

        gridAdapter = new GridAdapter(imagePaths);
        noScrollgridview.setAdapter(gridAdapter);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
    {
        // TODO Auto-generated method stub

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
            cancelShare();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class GridAdapter extends BaseAdapter {
        private ArrayList<String> listUrls;
        private LayoutInflater inflater;

        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
            if (listUrls.size() == 10) {
                listUrls.remove(listUrls.size() - 1);
            }
            inflater = LayoutInflater.from(activity);
        }

        public int getCount() {
            return listUrls.size();
        }

        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_image, parent, false);
                holder.image = (Button) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.image.setWidth(1000);

            final String path = listUrls.get(position);
            Log.i("mybule", path);
            if (path.equals("000000")) {
                holder.image.setBackground(add);

            } else {
                Bitmap bitmap = new ImageWork().decodeBitmapFromDisk(path, ImageUtil.dp2px(activity, 100), ImageUtil.dp2px(activity, 100));
                holder.image.setBackground(new BitmapDrawable(resources, bitmap));

            }
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("000000".equals(path)) {
                        PhotoPickerIntent intent = new PhotoPickerIntent(activity);
                        intent.setSelectModel(SelectModel.MULTI);
                        intent.setShowCarema(true); // 是否显示拍照
                        intent.setMaxTotal(9); // 最多选择照片数量，默认为6
                        intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                        startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    } else {

                      /*  Intent intent=new Intent(activity,PhotoPreviewActivity.class);
                        intent.putExtra(PhotoPreviewActivity.EXTRA_PHOTOS,imagePaths);
                        intent.putExtra(PhotoPreviewActivity.EXTRA_CURRENT_ITEM,position);
                        startActivityForResult(intent, REQUEST_PREVIEW_CODE);*/
                     /*   PhotoPreviewIntent intent = new PhotoPreviewIntent(activity);
                        intent.setCurrentItem(position);
                        intent.setPhotoPaths(imagePaths);*/


                        Intent intent = new Intent(activity, PhotoPreviewActivity.class);
                        intent.putExtra("imagePaths", imagePaths);
                        intent.putExtra("CurrentItem", position);
                        startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                    }
                }
            });

            return convertView;
        }

        class ViewHolder {
            Button image;
        }
    }
}
