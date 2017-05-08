package com.headlth.management.clenderutil;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.headlth.management.R;

import java.io.Serializable;


public class WaitDialog extends Dialog implements Serializable {

    private Dialog waitDialog = null;
    private TextView tv;
/*
    pd = new ProgressDialog(this);
    pd.setTitle("登录中...");
    pd.setMessage("请稍等...");
//        pd.setCancelable(false);
    pd.setCanceledOnTouchOutside(false);*/


    public WaitDialog(Context context) {
        super(context);
        if (waitDialog == null) {
            waitDialog = new Dialog(context, R.style.progress_dialog);
        }

        waitDialog.setContentView(R.layout.dialog_wait);
        waitDialog.setCancelable(true);
        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tv = (TextView) waitDialog.findViewById(R.id.id_tv_loadingmsg);
        tv.setText("加载中...");
    }

    public WaitDialog(Context context, String string) {
        super(context);
        if (waitDialog == null) {
            waitDialog = new Dialog(context, R.style.progress_dialog);
        }

        waitDialog.setContentView(R.layout.dialog_wait);
        waitDialog.setCancelable(true);
        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tv = (TextView) waitDialog.findViewById(R.id.id_tv_loadingmsg);
        tv.setText(string);
    }

    public void setMessage(String message) {
        if (tv == null) {
            tv = (TextView) waitDialog.findViewById(R.id.id_tv_loadingmsg);
        }
        tv.setText(message);
    }

    public void showDailog() {
        if (tv != null) {
            if (TextUtils.equals(tv.getText().toString(), "") || TextUtils.equals(tv.getText().toString(), null)) {
                tv.setVisibility(View.GONE);
            }
        }
        waitDialog.show();
    }

    public void dismissDialog() {
        if (null != waitDialog && waitDialog.isShowing()) {
            try {//如果该对话框依附的Activity已经消失 调用dismiss(); 会参数异常
                waitDialog.dismiss();
            } catch (Exception E) {
            }
        }
    }

    public void ShowDialog(boolean isShow) {
        if (isShow) {
            this.showDailog();
        } else {
            if (waitDialog != null) {
                this.dismissDialog();
            }
        }
    }

    public void setCancleable(boolean enable) {
        waitDialog.setCancelable(enable);
    }

}
