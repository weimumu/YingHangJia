package com.yinghangjiaclient.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yinghangjiaclient.MainActivity;
import com.yinghangjiaclient.R;
import com.yinghangjiaclient.recommend.RecommendMainActivity;
import com.yinghangjiaclient.util.MapUtils;
import com.yinghangjiaclient.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class QuestionResultActivity extends AppCompatActivity {
    private Button next_btn;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.init("ying");
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register_eighth);
            sp = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

            String name = sp.getString("USERNAME", "");
            String mapStr = sp.getString(name + "_info", "");
            JSONObject map = null;
            if (StringUtils.isBlank(mapStr)) {
                map = new JSONObject();
            } else {
                map = new JSONObject(mapStr);
            }

            int scoreRes = 0;
            for (int i = 1; i <= 13; i++) {
                int score = map.getInt("question_" + i);
                scoreRes += score;
            }
            TextView preference_result = (TextView) findViewById(R.id.preference_result);
            if (scoreRes < 21) {
                preference_result.setText("偏向保守");
            } else if (scoreRes >= 21 && scoreRes <= 35) {
                preference_result.setText("风格中庸");
            } else {
                preference_result.setText("投资激进");
            }

            scoreRes = 0;
            for (int i = 14; i <= 18; i++) {
                int score = map.getInt("question_" + i);
                scoreRes += score;
            }
            int age = map.getInt("age");
            if (age <= 25) {
                scoreRes = scoreRes + 50;
            } else if (age > 25 && scoreRes < 75) {
                scoreRes = scoreRes + 50 - (age - 25);
            }
            TextView ability_result = (TextView) findViewById(R.id.ability_result);
            if (scoreRes <= 19) {
                ability_result.setText("低能力");
            } else if (scoreRes >= 20 && scoreRes <= 39) {
                ability_result.setText("中低能力");
            } else if (scoreRes >= 40 && scoreRes <= 59) {
                ability_result.setText("中能力");
            } else if (scoreRes >= 60 && scoreRes <= 79) {
                ability_result.setText("中高能力");
            } else {
                ability_result.setText("高能力");
            }

            // 下一页按钮监听
            next_btn = (Button) findViewById(R.id.search_recomment_button);
            next_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.setClass(QuestionResultActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
    }
}
