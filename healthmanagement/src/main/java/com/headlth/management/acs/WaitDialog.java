package com.headlth.management.acs;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.headlth.management.R;


public class WaitDialog extends Dialog{

    private Dialog waitDialog = null;
    private TextView tv;

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
            waitDialog.dismiss();
        }
    }

    public void setCancleable(boolean enable) {
        waitDialog.setCancelable(enable);
    }

}
