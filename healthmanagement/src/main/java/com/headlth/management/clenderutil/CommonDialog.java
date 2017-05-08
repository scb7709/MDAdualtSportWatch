package com.headlth.management.clenderutil;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.headlth.management.R;


/**
 * 普通Dialog
 */
public class CommonDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private CommonDialogListener commonDialogListener;
    private TextView tv_title;
    private TextView tv_content;
    private Button bt_sure;
    private Button bt_cancle;
    private View view_line;

    public CommonDialog(Context context, CommonDialogListener commonDialogListener) {
        super(context, R.style.dialog);
        setContentView(R.layout.dialog_common);
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mContext = context;
        this.commonDialogListener = commonDialogListener;
        initView();
        setCanceledOnTouchOutside(false);
        show();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        bt_sure = (Button) findViewById(R.id.bt_sure);
        bt_cancle = (Button) findViewById(R.id.bt_cancle);
        view_line = findViewById(R.id.view_line);
        tv_title.setVisibility(View.GONE);
        view_line.setVisibility(View.GONE);
        bt_cancle.setVisibility(View.GONE);
        bt_sure.setOnClickListener(this);
        bt_cancle.setOnClickListener(this);
    }

    public CommonDialog setTitle(String title){
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(title);
        return this;
    }

    public CommonDialog setMessage(String message){
        tv_content.setText(message);
        return this;
    }

    public CommonDialog setCancleText(String cancle){
        bt_cancle.setVisibility(View.VISIBLE);
        view_line.setVisibility(View.VISIBLE);
        bt_cancle.setText(cancle);
        return this;
    }

    public CommonDialog setSureText(String sure){
        bt_sure.setText(sure);
        return this;
    }

    public CommonDialog setTitleTextSize(float size){
        tv_title.setTextSize(size);
        return this;
    }

    public CommonDialog setMessageTextSize(float size){
        tv_content.setTextSize(size);
        return this;
    }

    public CommonDialog setButtonTextSize(float size){
        bt_sure.setTextSize(size);
        bt_cancle.setTextSize(size);
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v == bt_sure) {
            dismiss();
            if(commonDialogListener != null){
                commonDialogListener.sure();
            }
        } else if (v == bt_cancle) {
            dismiss();
            if(commonDialogListener != null){
                commonDialogListener.cancle();
            }
        }
    }


    /**
     * 普通Dialog回调接口
     */
    public interface CommonDialogListener {
        void cancle();

        void sure();
    }

}
