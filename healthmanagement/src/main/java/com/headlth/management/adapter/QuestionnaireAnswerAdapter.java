package com.headlth.management.adapter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.headlth.management.R;
import com.headlth.management.activity.GetHeartActivity;
import com.headlth.management.activity.QuestionnaireActivity;
import com.headlth.management.activity.QuestionnaireResultActivity;
import com.headlth.management.entity.QuestionaireResultJson;
import com.headlth.management.entity.QuestionnaireAnswerGson;
import com.headlth.management.entity.QuestionnaireGson;
import com.headlth.management.myview.ClearEditText;
import com.headlth.management.myview.SubscriptImageView;
import com.headlth.management.utils.Constant;
import com.headlth.management.utils.HttpUtils;
import com.headlth.management.utils.ImageUtil;
import com.headlth.management.utils.ShareUitls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by abc on 2016/8/15.
 */
public class QuestionnaireAnswerAdapter extends BaseAdapter {

    private Activity activity; //
    private List<QuestionnaireAnswerGson.QuestionnaireAnswer.Questionnaireanswer> list;
    private LayoutInflater layoutInflater;

    public QuestionnaireAnswerAdapter(Activity activity, List<QuestionnaireAnswerGson.QuestionnaireAnswer.Questionnaireanswer> list) {
        layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.list = list;


      //  getHeartRateReceiver();
        //drawable= activity.getResources().getDrawable(R.drawable.button_white);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public QuestionnaireAnswerGson.QuestionnaireAnswer.Questionnaireanswer getItem(int position) {
     //   if (position < list.size()) {
            return list.get(position);
      /*  } else {
            return null;
        }*/

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        convertView = layoutInflater.inflate(R.layout.listview_questionnaire, null);
        holder = new Holder();
        x.view().inject(holder, convertView);
        holder.listview_questionnaire_getheart_layout.setVisibility(View.GONE);
        holder.listview_questionnaire_noheart.setVisibility(View.VISIBLE);
        holder.listview_questionnaire_commit.setVisibility(View.GONE);
        holder.listview_questionnaire.setVisibility(View.VISIBLE);
        if (getItem(position) != null) {
            final QuestionnaireAnswerGson.QuestionnaireAnswer.Questionnaireanswer questionnaire = getItem(position);
            holder.listview_questionnaire_title.setText(0 + "" + (position + 1) + "." + questionnaire.QuestionTitle);

            if (!questionnaire.QuestionType.equals("2")) {
                holder.listview_questionnaire_layout.setVisibility(View.VISIBLE);
                holder.listview_questionnaire_edit.setVisibility(View.GONE);

                holder.listview_questionnaire_layout.removeAllViews();
                final List<SubscriptImageView> list = new ArrayList<>();

                for (int i = 0; i < questionnaire.QuestionContent.size(); i++) {
                    final QuestionnaireAnswerGson.QuestionnaireAnswer.Questionnaireanswer.Option option = questionnaire.QuestionContent.get(i);
                    final View view = layoutInflater.inflate(R.layout.listview_questionnaire_item, null);
                    view.setMinimumHeight(ImageUtil.dp2px(activity, 35));
                    final SubscriptImageView imageView = (SubscriptImageView) view.findViewById(R.id.listview_questionnaire_item_imageview);
                    final TextView textView = (TextView) view.findViewById(R.id.listview_questionnaire_item_text);
                    textView.setText(option.Content);
                    if (option.IsSelect==1) {
                        imageView.setImageResource(R.mipmap.button_choose_active);
                    } else {
                        imageView.setImageResource(R.mipmap.button_choose_negative);
                    }
                    list.add(imageView);
                    holder.listview_questionnaire_layout.addView(view);
                }
            } else {
                holder.listview_questionnaire_edit.setVisibility(View.VISIBLE);
                holder.listview_questionnaire_edit.setText(questionnaire.QuestionAnswer);
                holder.listview_questionnaire_layout.setVisibility(View.GONE);


            }

        }
        return convertView;
    }

    public class Holder {
        @ViewInject(R.id.listview_questionnaire)
        public LinearLayout listview_questionnaire;
        @ViewInject(R.id.listview_questionnaire_title)
        public TextView listview_questionnaire_title;
        @ViewInject(R.id.listview_questionnaire_layout)
        public LinearLayout listview_questionnaire_layout;
        @ViewInject(R.id.listview_questionnaire_edit)
        public ClearEditText listview_questionnaire_edit;
        @ViewInject(R.id.listview_questionnaire_commit)
        public Button listview_questionnaire_commit;


        @ViewInject(R.id.listview_questionnaire_getheart_layout)
        public LinearLayout listview_questionnaire_getheart_layout;
        @ViewInject(R.id.listview_questionnaire_noheart)
        public LinearLayout listview_questionnaire_noheart;

        @ViewInject(R.id.listview_questionnaire_getheart)
        public Button listview_questionnaire_getheart;
        @ViewInject(R.id.listview_questionnaire_heart)
        public TextView listview_questionnaire_heart;

    }

    public static String getABC(String str) {
        String temp = "";
        switch (str) {
            case "1":
                temp = "A";
                break;
            case "2":
                temp = "B";
                break;
            case "3":
                temp = "C";
                break;
            case "4":
                temp = "D";
                break;
            case "5":
                temp = "E";
                break;
            case "6":
                temp = "F";
                break;
            case "7":
                temp = "G";
                break;
        }
        return temp;
    }


}
//



