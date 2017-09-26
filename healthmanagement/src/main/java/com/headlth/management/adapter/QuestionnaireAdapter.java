package com.headlth.management.adapter;

import android.app.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.headlth.management.R;

import com.headlth.management.activity.AdvancedPrescriptionActivity;
import com.headlth.management.activity.PrescriptionDetailsActivity;
import com.headlth.management.activity.QuestionnaireActivity;
import com.headlth.management.activity.QuestionnaireResultActivity;
import com.headlth.management.entity.QuestionaireResultJson;
import com.headlth.management.entity.QuestionnaireGson;
import com.headlth.management.myview.ClearEditText;

import com.headlth.management.myview.PubLicDialog;
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
public class QuestionnaireAdapter extends BaseAdapter {

    private Activity activity; //
    private String UserID = "0";
    private List<QuestionnaireGson.Questionnaire> list;
    private LayoutInflater layoutInflater;
    private int A;
    private String QuestionaireID;
    private String PlanNameID;
    private static TextView HeartRateTextView;
    String heartrate = "";
    Drawable drawable;
    int HeartRatePossition;
    boolean falg;
    boolean Falg = true;//不允许多次上传问卷

    public QuestionnaireAdapter(boolean falg, Activity activity, List<QuestionnaireGson.Questionnaire> list, String PlanNameID, String QuestionaireID) {
        UserID = ShareUitls.getString(activity, "UID", "0");
        layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.list = list;
        this.PlanNameID = PlanNameID;
        this.QuestionaireID = QuestionaireID;
        this.falg = falg;
        // getHeartRateReceiver();
      //  drawable = activity.getResources().getDrawable(R.drawable.doubleline_shape);
    }
/*
    public QuestionnaireAdapter(boolean falg, Activity activity, List<QuestionnaireGson.Questionnaire> list, String PlanNameID, String QuestionaireID) {
        UserID = ShareUitls.getString(activity, "UID", "0");
        layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.list = list;
        this.falg = falg;
        this.QuestionaireID = QuestionaireID;
        this.PlanNameID = PlanNameID;
        getHeartRateReceiver();
        drawable = activity.getResources().getDrawable(R.drawable.button_white);
    }*/


    @Override
    public int getCount() {
        return list.size() + 1;
    }

    @Override
    public QuestionnaireGson.Questionnaire getItem(int position) {
        if (position < list.size()) {
            return list.get(position);
        } else {
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        //  if (convertView == null) {
        convertView = layoutInflater.inflate(R.layout.listview_questionnaire, null);
        holder = new Holder();
        x.view().inject(holder, convertView);
        // convertView.setTag(holder);
       /* } else {
            holder = (Holder) convertView.getTag();
        }*/
        if (getItem(position) != null) {
            final QuestionnaireGson.Questionnaire questionnaire = getItem(position);
           /* if (questionnaire.IsHRrest == 1) {
                HeartRatePossition = position;
                holder.listview_questionnaire_heart.setText(heartrate);

                if (heartrate.length() != 0) {

                    holder.listview_questionnaire_getheart.setText("重新获取安静心率");
                    holder.listview_questionnaire_getheart.setTextColor(Color.parseColor("#c7c7c7"));
                    holder.listview_questionnaire_getheart.setBackground(drawable);
                }
                // HeartRateTextView = holder.listview_questionnaire_heart;

                holder.listview_questionnaire_getheart_layout.setVisibility(View.VISIBLE);
                holder.listview_questionnaire_noheart.setVisibility(View.GONE);


                holder.listview_questionnaire_getheart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        activity.startActivity(new Intent(activity, GetHeartActivity.class));
                    }
                });

            } else {*/
            holder.listview_questionnaire_getheart_layout.setVisibility(View.GONE);
            holder.listview_questionnaire_noheart.setVisibility(View.VISIBLE);


            // }

            holder.listview_questionnaire_commit.setVisibility(View.GONE);
            holder.listview_questionnaire.setVisibility(View.VISIBLE);

            if (position < 9) {
                holder.listview_questionnaire_title.setText(0 + "" + (position + 1) + "." + questionnaire.QuestionTitle);
            } else {
                holder.listview_questionnaire_title.setText((position + 1) + "." + questionnaire.QuestionTitle);
            }
            list.get(position).Number = (position + 1) + "";//给题编号

            if (!questionnaire.QuestionTypeID.equals("2")) {
                holder.listview_questionnaire_layout.setVisibility(View.VISIBLE);
                holder.listview_questionnaire_edit.setVisibility(View.GONE);

                // final String[] str = questionnaire.getQuestionContent().split(",");

                holder.listview_questionnaire_layout.removeAllViews();
                final List<SubscriptImageView> list = new ArrayList<>();

                for (int i = 0; i < questionnaire.QuestionAnswer.size(); i++) {
                    A = i;
                    final QuestionnaireGson.Questionnaire.Option option = questionnaire.QuestionAnswer.get(i);
                    final View view = layoutInflater.inflate(R.layout.listview_questionnaire_item, null);
                    view.setMinimumHeight(ImageUtil.dp2px(activity, 35));
                    final SubscriptImageView imageView = (SubscriptImageView) view.findViewById(R.id.listview_questionnaire_item_imageview);
                    imageView.Subscript = i;
                    final TextView textView = (TextView) view.findViewById(R.id.listview_questionnaire_item_text);
                    textView.setText(option.Content);
                    if (option.Selected) {
                        imageView.setImageResource(R.mipmap.button_choose_active);
                    } else {
                        imageView.setImageResource(R.mipmap.button_choose_negative);
                    }
                    list.add(imageView);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (option.Selected) {
                                //  imageView.setImageResource(R.mipmap.button_choose_negative);
                                option.Selected = false;
                            } else {
                                if (questionnaire.QuestionTypeID.equals("1")) {
                                    for (int j = 0; j < questionnaire.QuestionAnswer.size(); j++) {

                                        //  list.get(j).setImageResource(R.mipmap.button_choose_active);
//  list.get(j).setImageResource(R.mipmap.button_choose_negative);
                                        questionnaire.QuestionAnswer.get(j).Selected = list.get(j).Subscript == imageView.Subscript;


                                    }

                                } else {
                                    // imageView.setImageResource(R.mipmap.button_choose_active);
                                }
                                option.Selected = true;


                            }
                            notifyDataSetChanged();
                        }
                    });
                    holder.listview_questionnaire_layout.addView(view);
                }
            } else {
                holder.listview_questionnaire_edit.setVisibility(View.VISIBLE);
                holder.listview_questionnaire_edit.setText(questionnaire.CompletionAnswer);
                holder.listview_questionnaire_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        questionnaiersetuseranswer(v, position);
                    }
                });

                holder.listview_questionnaire_layout.setVisibility(View.GONE);
            }

        } else {
            holder.listview_questionnaire_commit.setVisibility(View.VISIBLE);
            holder.listview_questionnaire.setVisibility(View.GONE);
            holder.listview_questionnaire_commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Falg)
                        commitAnswerDataHttp();
                    else {
                        Toast.makeText(activity, "正在提交,请勿重复请求", Toast.LENGTH_LONG).show();
                    }
                }
            });
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


    private void commitAnswerDataHttp() {
        Falg = false;
        JSONArray jsonArray = new JSONArray();
        List<String> num = new ArrayList<>();
        for (QuestionnaireGson.Questionnaire questionnaire : list) {
            String type = questionnaire.QuestionTypeID;
            if (type.equals("1")) {
                List<QuestionnaireGson.Questionnaire.Option> QuestionAnswer = questionnaire.QuestionAnswer;
                boolean flag = false;
                for (int i = 0; i < QuestionAnswer.size(); i++) {
                    QuestionnaireGson.Questionnaire.Option option = questionnaire.QuestionAnswer.get(i);
                    if (option.Selected) {
                        flag = true;
                    }
                }
                if (!flag) {//如果该题的没有选 这记录在集合
                    num.add(questionnaire.Number);
                }
            } else if (type.equals("2")) {
                if (questionnaire.CompletionAnswer==null||questionnaire.CompletionAnswer.length() == 0) {
                    Toast.makeText(activity, "填空题必须作答,没有填 0", Toast.LENGTH_LONG).show();
                    Falg = true;
                    return;
                }
            }
            JSONObject jsonObject = getJSONObject(questionnaire);
            jsonArray.put(jsonObject);
        }
        Log.i("SSSSSSSSSSSSSCCC", num.size() + "");
        if (num.size() == 1) {
            Toast.makeText(activity, "您第" + num.get(0) + "道题尚未做答", Toast.LENGTH_LONG).show();
            Falg = true;
            return;
        } else if (num.size() > 1) {
            Toast.makeText(activity, "选择题必须全部作答", Toast.LENGTH_LONG).show();
            Falg = true;
            return;
        } else {
            RequestParams params = new RequestParams(Constant.BASE_URL + "/MdMobileService.ashx?do=PostAnswerDataRequest");
            params.addBodyParameter("QuestionnaireID", QuestionaireID);
            params.addBodyParameter("UID", ShareUitls.getString(activity, "UID", "") + "");
            params.addBodyParameter("ResultJWT", ShareUitls.getString(activity, "ResultJWT", "0"));
            params.addBodyParameter("PlanNameID", PlanNameID);
            params.addBodyParameter("AnswerData", jsonArray.toString());
            HttpUtils.getInstance(activity).sendRequestRequestParams("", params, true, new HttpUtils.ResponseListener() {

                        @Override
                        public void onResponse(String response) {
                            // Falg=true;
                            Log.i("TTTTTTTTTTTAAABB", ShareUitls.getString(activity, "UID", "") + "'" + response);
                            QuestionaireResultJson questionaireResultJson = new Gson().fromJson(response, QuestionaireResultJson.class);
                            if (questionaireResultJson.IsHighRisk.equals("1")) {//属于高危病患
                                showUpdateDialog(questionaireResultJson.Message);
                                return;
                            } else if (questionaireResultJson.IsSuccess.equals("true")) {
                                ShareUitls.putString(activity, "SPID", questionaireResultJson.SPID + "");
                                if (questionaireResultJson.Status.equals("1")) {
                                    ShareUitls.putString(activity, "questionnaire", "1");//首页界面是否重新刷新 (打完题)
                                    ShareUitls.putString(activity, "maidong", "1");//首页界面是否重新刷新 (新数据)
                                    Intent intent = new Intent(activity, QuestionnaireResultActivity.class);
                                    intent.putExtra("QuestionnaireID", QuestionaireID);
                                    intent.putExtra("PlanNameID", PlanNameID);


                                    if (questionaireResultJson.IsMoney.equals("true")) {
                                        intent.putExtra("PAY", "0");
                                    } else {
                                        intent.putExtra("PAY", "1");
                                    }
                                    //   intent.putExtra("prescriptionList",questionaireResultJson.prescriptionList);

                                    if (AdvancedPrescriptionActivity.activity != null) {
                                        AdvancedPrescriptionActivity.activity.finish();
                                    }

                                    activity.startActivity(intent);
                                    QuestionnaireActivity.activity.finish();
                                }
                            } else {
                                Falg = true;
                            }

                        }

                        @Override
                        public void onErrorResponse(Throwable ex) {
                            Falg = true;
                        }
                    }

            );

        }
    }

    private PopupWindow popupWindow;

    private void questionnaiersetuseranswer(View v, final int position) {
        final QuestionnaireGson.Questionnaire questionnaire = getItem(position);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_questionaiersetuseranswer, null);
        popupWindow = new PopupWindow(view, ImageUtil.dp2px(activity, 300), ImageUtil.dp2px(activity, 230), true);
        Button cancle = (Button) view.findViewById(R.id.dialog_questionnaiersetuseranswer_cancle);
        Button ok = (Button) view.findViewById(R.id.dialog_questionnaiersetuseranswer_ok);
        TextView question = (TextView) view.findViewById(R.id.dialog_questionnaiersetuseranswer_title);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_questionnaiersetuseranswer_edittext);
        editText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        question.setText(questionnaire.QuestionTitle + "?");
        String name = questionnaire.CompletionAnswer;
        if (name!=null&&name.length() != 0) {
            editText.setText(name);
        }
        cancle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String answer = editText.getText().toString();
                if (answer.length() != 0) {
                    switch (questionnaire.UserInputID) {
                        case "1":
                            if (answer.matches("[0-9]+")) {
                                getItem(position).CompletionAnswer = answer;
                                popupWindow.dismiss();

                            } else {

                                Toast.makeText(activity, "此答案只能是整数", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case "2":
                            if (answer.matches("[0-9]+") || answer.matches("[0-9]+([.]{1}[0-9]+){0,1}")) {
                                getItem(position).CompletionAnswer = answer;
                                popupWindow.dismiss();

                            } else {

                                Toast.makeText(activity, "此答案只能是整数或小数", Toast.LENGTH_LONG).show();
                            }
                            break;
                        default:
                            getItem(position).CompletionAnswer = answer;
                            popupWindow.dismiss();
                            break;
                    }
                } else {
                    Toast.makeText(activity, "答案输入不能为空", Toast.LENGTH_LONG).show();
                }
                notifyDataSetChanged();
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

    private JSONObject getJSONObject(QuestionnaireGson.Questionnaire questionnaire) {
        JSONObject jsonObject = new JSONObject();
        String answer = "";
        if (!questionnaire.QuestionTypeID.equals("2")) {
            List<QuestionnaireGson.Questionnaire.Option> QuestionAnswer = questionnaire.QuestionAnswer;
            for (int i = 0; i < QuestionAnswer.size(); i++) {
                QuestionnaireGson.Questionnaire.Option option = questionnaire.QuestionAnswer.get(i);
                if (option.Selected) {
                    answer += option.ID + ",";
                }
            }
            if (answer.length() != 0) {
                answer = answer.substring(0, answer.length() - 1);
            }
        } else {
            answer = questionnaire.CompletionAnswer;

        }
        try {
            jsonObject.put("ID", questionnaire.QuestionID);
            jsonObject.put("answer", answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


   /* private BroadcastReceiver HeartRateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  Log.i("PPPPPPPPPPPP", "Activity接收到了参数");
            heartrate = intent.getStringExtra("heartrate");

            if (heartrate.length() == 0 || heartrate == null) {
                heartrate = "0";
            }
            getItem(HeartRatePossition).CompletionAnswer = heartrate;
            // HeartRateTextView.setText(heartrate);
            //   Toast.makeText(activity, heartrate, Toast.LENGTH_LONG).show();
            notifyDataSetChanged();
        }
    };

    private void getHeartRateReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("getHeartRateReceiver");
        activity.registerReceiver(HeartRateReceiver, filter);
    }*/

    /**
     * 弹出高危提示对话框
     */
    private void showUpdateDialog(String message) {
        PubLicDialog.showNotDialog(activity, new String[]{"提示:", message, "我已知晓"}, new PubLicDialog.PubLicDialogOnClickListener() {
            @Override
            public void setPositiveButton() {
                QuestionnaireActivity.activity.finish();
                if (AdvancedPrescriptionActivity.activity != null) {
                    AdvancedPrescriptionActivity.activity.finish();
                }
                if (PrescriptionDetailsActivity.activity != null) {
                    PrescriptionDetailsActivity.activity.finish();
                }
            }
        });
    }
}
//



