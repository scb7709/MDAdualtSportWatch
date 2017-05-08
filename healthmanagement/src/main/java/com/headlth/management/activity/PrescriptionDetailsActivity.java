package com.headlth.management.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.entity.PrescriptionDetails;
import com.headlth.management.entity.PrescriptionJson;
import com.headlth.management.myview.MGridView;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ShareUitls;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by abc on 2016/9/26.
 */
@ContentView(R.layout.activity_prescriptiondetails)
public class PrescriptionDetailsActivity extends BaseActivity {

    @ViewInject(R.id.view_publictitle_title)
    private TextView view_publictitle_title;
    @ViewInject(R.id.view_publictitle_back)
    private RelativeLayout view_publictitle_back;
//    view_publictitle_title.setText("处方详情");


    @ViewInject(R.id.activity_prescritiondetials_back)
    private Button activity_prescritiondetials_back;
    @ViewInject(R.id.activity_prescritiondetials_go)
    private Button activity_prescritiondetials_go;


    @ViewInject(R.id.activity_prescritiondetials_prescritionexplain)
    private TextView activity_prescritiondetials_prescritionexplain;
    @ViewInject(R.id.activity_prescritiondetials_image_layout)
    private MGridView activity_prescritiondetials_image_layout;
    private PrescriptionJson.PrescriptionClass prescription;
    String PAY = "0";//用来标记是否需要去支付
    public static Activity activity;
    String PlanNameID;
    PrescriptionDetails prescriptionDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

       // view_publictitle_title.setText("计划详情");
        activity = this;
        prescription = (PrescriptionJson.PrescriptionClass) getIntent().getSerializableExtra("prescription");
    }

    @Override
    protected void onStart() {
        super.onStart();
        prescriptionDetails = null;
        setDataHttp();
    }

    private void setImages(List<PrescriptionDetails.ImgUrlListClass> ImgUrlList) {
        GridAdapter adapter = new GridAdapter(ImgUrlList);
        activity_prescritiondetials_image_layout.setAdapter(adapter);
    }

    private void setDataHttp() {

        RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=GetPlanDetailRequest");
        params.addBodyParameter("PlanNameID", prescription.PlanNameID);
        params.addBodyParameter("PlanClassID", prescription.PlanClassID);
        params.addBodyParameter("UID", ShareUitls.getString(PrescriptionDetailsActivity.this, "UID", "") + "");
        params.addBodyParameter("ResultJWT", ShareUitls.getString(PrescriptionDetailsActivity.this, "ResultJWT", "0"));
        HttpUtils.getInstance(PrescriptionDetailsActivity.this).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("WWWWWWWWQQQ", prescription.PlanNameID + " " + ShareUitls.getString(PrescriptionDetailsActivity.this, "UID", "") + "  " + response);
                        prescriptionDetails = new Gson().fromJson(response, PrescriptionDetails.class);
                        if (prescriptionDetails.IsSuccess.equals("true")) {
                            if (prescriptionDetails.IsButtonsShow.equals("true")) {
                                activity_prescritiondetials_go.setVisibility(View.VISIBLE);
                            }

                            switch (prescriptionDetails.IsToPay) {
                                case 2://该处方属于用户未支付
                                case 3://该处方不属于用户 且是收费的
                                    PAY = "0";
                                    break;
                                case 1://该处方属于用户已经支付
                                case 4://该处方免费属于该用户
                                case 5://该处方 免费 不属于属于用户
                                    PAY = "1";
                                    break;


                            }
                            view_publictitle_title.setText(prescriptionDetails.PlanName);
                            activity_prescritiondetials_prescritionexplain.setText(prescriptionDetails.Description);
                            setImages(prescriptionDetails.ImgUrlList);


                        }

                    }

                    @Override
                    public void onErrorResponse(Throwable ex) {


                    }
                }

        );

    }

    @Event(value = {R.id.view_publictitle_back, R.id.activity_prescritiondetials_go})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.view_publictitle_back:
                finish();
                break;
            case R.id.activity_prescritiondetials_go:
               /* String PPID = ShareUitls.getString(PrescriptionDetailsActivity.this, "PPID", "0");
                String SPID = ShareUitls.getString(PrescriptionDetailsActivity.this, "SPID", "0");*/
                if (prescriptionDetails != null) {
                    isNext();
                }
                break;
        }
    }

    private void isNext() {
        Log.i("WWWWWWWWQQQAA", PAY);
        Intent intent = new Intent();
        switch (prescriptionDetails.IsToPay) {
            case 3://该处方不属于用户 且是收费的
            case 5://该处方免费不属于该用户
                intent.setClass(PrescriptionDetailsActivity.this, QuestionnaireActivity.class);
                intent.putExtra("prescription", prescription);
                intent.putExtra("PAY", PAY);
                intent.putExtra("flag", "all");
                startActivity(intent);
                break;
            case 1://该处方属于用户已经支付
            case 2://该处方属于用户 未支付
            case 4://该处方免费属于该用户
                intent.setClass(PrescriptionDetailsActivity.this, QuestionnaireResultActivity.class);
                intent.putExtra("PAY", PAY);
                intent.putExtra("QuestionnaireID", prescription.QuestionnaireID);
                intent.putExtra("PlanNameID", prescription.PlanNameID);
                startActivity(intent);
                break;
        }
    }
 /*   Int  IsToPay
    If(isuserplay==1){
        IsToPay=1;//该处方属于用户已经支付
    }else if(Ismoney==1){
        if(isuserown==1){
            IsToPay=2;//该处方属于用户未支付
        }else {
            IsToPay=3;//该处方不属于用户 且是收费的
        }
    }
    else{
        if(isuserown==1){
            IsToPay=4;//该处方免费属于该用户
        }else {
            IsToPay=5;//该处方免费不属于该用户
        }
    }*/


    @SuppressLint("HandlerLeak")
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater inflater; // 视图容器
        List<PrescriptionDetails.ImgUrlListClass> list;

        public GridAdapter(List<PrescriptionDetails.ImgUrlListClass> list) {
            inflater = LayoutInflater.from(activity);
            this.list = list;
        }


        public int getCount() {
            return (list.size());
        }

        public PrescriptionDetails.ImgUrlListClass getItem(int arg0) {

            return list.get(arg0);
        }

        public long getItemId(int arg0) {

            return arg0;
        }

        public View getView(final int arg, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_prescriptiondetials_grida, parent, false);
                holder = new ViewHolder();
                //holder.image = (NetworkImageView ) convertView.findViewById(R.id.item_grida_circle_image);
                holder.image = (Button) convertView.findViewById(R.id.item_grida_circle_image);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.image.setWidth(1000);
            holder.image.setHeight(300);
            final String tag = (String) holder.image.getTag();
            if (!(Constant.BASE_URL + "/" + getItem(arg)).equals(tag)) {
                holder.image.setBackgroundResource(R.mipmap.nodata);
            }

            setGlide(arg, holder);
            return convertView;
        }

        private void setGlide(final int arg, final ViewHolder holder) {
            String url = Constant.BASE_URL + "/" + getItem(arg).Content;
            Glide.with(activity)
                    .load(url)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (bitmap != null) {
                                holder.image.setBackground(new BitmapDrawable(bitmap));
                            }

                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            //holder.image.setBackgroundResource(R.mipmap.nodata);
                        }

                    });


        }

        public class ViewHolder {
            //  public NetworkImageView  image;
            public Button image;
        }

    }
}
