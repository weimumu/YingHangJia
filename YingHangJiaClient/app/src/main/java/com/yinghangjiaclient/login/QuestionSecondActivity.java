package com.yinghangjiaclient.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QuestionSecondActivity extends AppCompatActivity {
    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private RadioGroup radioGroup3;
    private RadioGroup radioGroup4;
    private RadioGroup radioGroup5;

    private Button next_btn;
    private SharedPreferences sp;

    private Map<Integer, Integer> idMapScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register_fifth);
            sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            init_map();

            radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
            radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
            radioGroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
            radioGroup4 = (RadioGroup) findViewById(R.id.radioGroup4);
            radioGroup5 = (RadioGroup) findViewById(R.id.radioGroup5);


            // 下一页按钮监听
            next_btn = (Button) findViewById(R.id.next_button);
            next_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (validate()) {
                        Intent intent = new Intent();
                        intent.setClass(QuestionSecondActivity.this, QuestionThirdActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(QuestionSecondActivity.this, "信息未完善，请补全缺失信息",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Button backBtn = (Button) findViewById(R.id.back_button);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }

    void init_map() {
        idMapScore = new HashMap<Integer, Integer>();
        idMapScore.put(R.id.radioButto1_threepoint, 3);
        idMapScore.put(R.id.radioButton2_onepoint, 1);
        idMapScore.put(R.id.radioButton3_fourpoint, 4);

        idMapScore.put(R.id.radioButton4_onepoint, 1);
        idMapScore.put(R.id.radioButton5_threepoint, 3);
        idMapScore.put(R.id.radioButton6_fourpoint, 4);

        idMapScore.put(R.id.radioButton7_onepoint, 1);
        idMapScore.put(R.id.radioButton8_threepoint, 3);

        idMapScore.put(R.id.radioButton9_twopoint, 2);
        idMapScore.put(R.id.radioButton10_onepoint, 1);

        idMapScore.put(R.id.radioButton11_twopoint, 2);
        idMapScore.put(R.id.radioButton12_onepoint, 1);
        idMapScore.put(R.id.radioButton13_fourpoint, 4);
        idMapScore.put(R.id.radioButton14_onepoint, 1);

    }

    boolean validate() {
        try {
            String name = sp.getString("USERNAME", "");
            SharedPreferences.Editor editor = sp.edit();
            // 如果已经有了 name + "_info" 这个信息，那么就向其保留map数据，并且向其中添加新数据
            String mapStr = sp.getString(name + "_info", "");
            JSONObject map = null;
            if (StringUtils.isBlank(mapStr)) {
                map = new JSONObject();
            } else {
                map = new JSONObject(mapStr);
            }

            Integer score = radioGroup1.getCheckedRadioButtonId();
            if (score == -1) return false;
            map.put("question_1", idMapScore.get(score).toString());

            score = radioGroup2.getCheckedRadioButtonId();
            if (score == -1) return false;
            map.put("question_2", idMapScore.get(score).toString());

            score = radioGroup3.getCheckedRadioButtonId();
            if (score == -1) return false;
            map.put("question_3", idMapScore.get(score).toString());

            score = radioGroup4.getCheckedRadioButtonId();
            if (score == -1) return false;
            map.put("question_4", idMapScore.get(score).toString());

            score = radioGroup5.getCheckedRadioButtonId();
            if (score == -1) return false;
            map.put("question_5", idMapScore.get(score).toString());

            // 保存用户问卷信息
            editor.putString(name + "_info", map.toString());
            editor.apply();
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
            return false;
        }
    }
}
