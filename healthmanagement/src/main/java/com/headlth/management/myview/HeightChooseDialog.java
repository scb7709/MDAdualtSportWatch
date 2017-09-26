package com.headlth.management.myview;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.headlth.management.R;
import com.headlth.management.utils.Constant;

import java.util.ArrayList;
import java.util.List;


public class HeightChooseDialog extends Dialog {

    public HeightChooseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Params {
        private final List<BottomMenu> menuList = new ArrayList<>();
        private View.OnClickListener cancelListener;
        private String menuTitle;
        private String cancelText;
        private Context context;
    }

    public static class Builder {
        private boolean canCancel = true;
        private boolean shadow = true;
        private final Params p;
        List<String> height_data;
        List<String> weight_data1;
        List<String> weight_data2;
        String temp_weight1 = "";
        String temp_weight2 = "";

        public Builder(Context context) {
            p = new Params();
            p.context = context;
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
            // temp_weight2 = weight_data2.get(weight_data2.size() / 2);


        }

        public Builder setCanCancel(boolean canCancel) {
            this.canCancel = canCancel;
            return this;
        }

        public Builder setShadow(boolean shadow) {
            this.shadow = shadow;
            return this;
        }

        public Builder setTitle(String title) {
            this.p.menuTitle = title;
            return this;
        }


        public Builder setCancelListener(View.OnClickListener cancelListener) {
            p.cancelListener = cancelListener;
            return this;
        }

        public Builder setCancelText(int resId) {
            p.cancelText = p.context.getString(resId);
            return this;
        }

        public Builder setCancelText(String text) {
            p.cancelText = text;
            return this;
        }

        public HeightChooseDialog create(final boolean flag, final TextView textView, final String old) {
            final HeightChooseDialog dialog = new HeightChooseDialog(p.context, shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.Animation_Bottom_Rising);

            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);

            // View view = LayoutInflater.from(p.context).inflate(R.layout.dialog_weight_pick, null);
            View height_view = LayoutInflater.from(p.context).inflate(R.layout.dialog_weight_pick, null);

            Button btnCancel = (Button) height_view.findViewById(R.id.btnCancel);
            Button ok = (Button) height_view.findViewById(R.id.btnSubmit);
            TextView tvTitle = (TextView) height_view.findViewById(R.id.tvTitle);
            PickerView height_PickerView = (PickerView) height_view.findViewById(R.id.dialog_height_pick_pickerview);
            PickerView weight2_PickerView = (PickerView) height_view.findViewById(R.id.dialog_weight_pick_pickerview);
            MyToash.Log(old);
            if (flag) {
                tvTitle.setText("身高");
                // textView.setText("165");
                weight2_PickerView.setVisibility(View.GONE);
                int initialize = 0;
                for (int i = 0; i < height_data.size(); i++) {
                    if (old.equals(height_data.get(i))) {
                        initialize = i;
                        break;
                    }
                }
                height_PickerView.setData(height_data, initialize == 0 ? 65 : initialize);
            } else {
                tvTitle.setText("体重");
                //textView.setText( "45");
                weight2_PickerView.setVisibility(View.VISIBLE);
                int initialize1 = 0;
                int initialize2 = 0;
               // String[] sold ={"0","0"};

                String[]   sold = old.split("\\.");

               /* if(old.contains(".")) {
                    sold = old.split("\\.");
                }else {
                    sold[0]=old;
                }*/
                for (int i = 0; i < weight_data1.size(); i++) {
                    if (sold[0].equals(weight_data1.get(i))) {
                        initialize1 = i;
                        break;
                    }
                }
                if(sold.length==2) {
                    for (int i = 0; i < weight_data2.size(); i++) {
                        if (("." + sold[1]).equals(weight_data2.get(i))) {
                            initialize2 = i;
                            break;
                        }
                    }
                }
                height_PickerView.setData(weight_data1, initialize1 == 0 ? 15 : initialize1);
                weight2_PickerView.setData(weight_data2,initialize2);
            }
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    temp_weight1 = "";
                    temp_weight2 = "";
                    dialog.dismiss();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    temp_weight1 = "";
                    temp_weight2 = "";
                    if (flag) {
                        textView.setText(old);
                    } else {
                        textView.setText(old);
                    }

                    dialog.dismiss();
                }
            });
            height_PickerView.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    if (flag) {
                        textView.setText(text);
                    } else {
                        temp_weight1 = text;
                        textView.setText(temp_weight1 + temp_weight2);
                    }
                }
            });
            weight2_PickerView.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    if(text.equals(".0")){
                        temp_weight2="";
                    }else {
                        temp_weight2 =text;
                    }
                    textView.setText(temp_weight1 + temp_weight2);
                }
            });
            dialog.setContentView(height_view);
            dialog.setCanceledOnTouchOutside(canCancel);
            dialog.setCancelable(canCancel);
            return dialog;
        }


    }

    private static class BottomMenu {
        public String funName;
        public View.OnClickListener listener;

        public BottomMenu(String funName, View.OnClickListener listener) {
            this.funName = funName;
            this.listener = listener;
        }
    }
}
